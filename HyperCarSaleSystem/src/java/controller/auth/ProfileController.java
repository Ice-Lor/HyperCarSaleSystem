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
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User freshUser = userDAO.getUserById(currentUser.getUserId());
        request.setAttribute("user", freshUser);
        request.getRequestDispatcher("/WEB-INF/views/client/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (!CSRFUtil.isValidToken(request)) {
            session.setAttribute("errorMessage", "CSRF Token không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        String action = request.getParameter("action");

        if ("updateProfile".equalsIgnoreCase(action)) {
            String fullName = ValidationUtil.sanitize(request.getParameter("fullName"));
            String email = ValidationUtil.sanitize(request.getParameter("email"));
            String phone = ValidationUtil.sanitize(request.getParameter("phone"));
            String address = ValidationUtil.sanitize(request.getParameter("address"));

            currentUser.setFullName(fullName);
            currentUser.setEmail(email);
            currentUser.setPhone(phone);
            currentUser.setAddress(address);

            boolean success = userDAO.updateUser(currentUser);
            if (success) {
                session.setAttribute("currentUser", currentUser);
                session.setAttribute("toastMessage", "Cập nhật hồ sơ thành công!");
            } else {
                session.setAttribute("errorMessage", "Không thể cập nhật hồ sơ!");
            }

        } else if ("changePassword".equalsIgnoreCase(action)) {
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmNewPassword = request.getParameter("confirmNewPassword");

            User fresh = userDAO.getUserById(currentUser.getUserId());
            if (!PasswordUtil.checkPassword(oldPassword, fresh.getPasswordHash())) {
                session.setAttribute("errorMessage", "Mật khẩu hiện tại không đúng!");
            } else if (newPassword == null || newPassword.length() < 6) {
                session.setAttribute("errorMessage", "Mật khẩu mới phải có tối thiểu 6 ký tự!");
            } else if (!newPassword.equals(confirmNewPassword)) {
                session.setAttribute("errorMessage", "Xác nhận mật khẩu mới không khớp!");
            } else {
                boolean success = userDAO.updatePassword(currentUser.getUserId(), PasswordUtil.hashPassword(newPassword));
                if (success) {
                    session.setAttribute("toastMessage", "Đổi mật khẩu thành công!");
                } else {
                    session.setAttribute("errorMessage", "Đổi mật khẩu thất bại!");
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
