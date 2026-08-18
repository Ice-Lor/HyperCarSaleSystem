package controller.auth;

import dal.UserDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import util.CSRFUtil;
import util.PasswordUtil;
import util.ValidationUtil;

public class ProfileController extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");
        User freshUser = userDAO.getUserById(currentUser.getUserId());
        request.setAttribute("user", freshUser);
        request.getRequestDispatcher("/WEB-INF/views/client/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!CSRFUtil.isValidToken(request)) {
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");
        String action = request.getParameter("action");

        if ("updateInfo".equals(action)) {
            String fullName = ValidationUtil.sanitize(request.getParameter("fullName"));
            String email = ValidationUtil.sanitize(request.getParameter("email"));
            String phone = ValidationUtil.sanitize(request.getParameter("phone"));
            String address = ValidationUtil.sanitize(request.getParameter("address"));

            currentUser.setFullName(fullName);
            currentUser.setEmail(email);
            currentUser.setPhone(phone);
            currentUser.setAddress(address);

            userDAO.updateProfile(currentUser);
            session.setAttribute("currentUser", currentUser);
            request.setAttribute("successMessage", "Cập nhật hồ sơ thành công!");

        } else if ("changePassword".equals(action)) {
            String oldPass = request.getParameter("oldPassword");
            String newPass = request.getParameter("newPassword");
            String confirmPass = request.getParameter("confirmPassword");

            User dbUser = userDAO.getUserById(currentUser.getUserId());
            if (!PasswordUtil.checkPassword(oldPass, dbUser.getPasswordHash())) {
                request.setAttribute("errorMessage", "Mật khẩu hiện tại không chính xác!");
            } else if (!newPass.equals(confirmPass)) {
                request.setAttribute("errorMessage", "Mật khẩu mới không khớp!");
            } else if (newPass.length() < 6) {
                request.setAttribute("errorMessage", "Mật khẩu mới phải từ 6 ký tự trở lên!");
            } else {
                userDAO.changePassword(currentUser.getUserId(), PasswordUtil.hashPassword(newPass));
                request.setAttribute("successMessage", "Đổi mật khẩu thành công!");
            }
        }

        User freshUser = userDAO.getUserById(currentUser.getUserId());
        request.setAttribute("user", freshUser);
        request.getRequestDispatcher("/WEB-INF/views/client/profile.jsp").forward(request, response);
    }
}
