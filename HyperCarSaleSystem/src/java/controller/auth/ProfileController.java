package controller.auth;

import dal.ActivityLogDAO;
import dal.UserDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import util.PasswordUtil;
import util.ValidationUtil;

/**
 * Controller xử lý xem và cập nhật Hồ sơ cá nhân & Đổi mật khẩu thành viên VIP.
 */
@WebServlet(name = "ProfileController", urlPatterns = {"/profile"})
public class ProfileController extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final ActivityLogDAO logDAO = new ActivityLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User sessionUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=auth_required");
            return;
        }

        // Luôn làm mới thông tin User từ Database
        User freshUser = userDAO.getUserById(sessionUser.getUserId());
        if (freshUser != null) {
            session.setAttribute("user", freshUser);
            request.setAttribute("profileUser", freshUser);
        }

        request.getRequestDispatcher("/WEB-INF/views/client/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User sessionUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=auth_required");
            return;
        }

        String action = request.getParameter("action");

        if ("update_profile".equalsIgnoreCase(action)) {
            handleUpdateProfile(request, response, session, sessionUser);
        } else if ("change_password".equalsIgnoreCase(action)) {
            handleChangePassword(request, response, session, sessionUser);
        } else {
            response.sendRedirect(request.getContextPath() + "/profile");
        }
    }

    /**
     * Xử lý cập nhật thông tin cá nhân.
     */
    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response,
                                    HttpSession session, User sessionUser)
            throws ServletException, IOException {
        String fullName = ValidationUtil.sanitize(request.getParameter("fullName"));
        String email = ValidationUtil.sanitize(request.getParameter("email"));
        String phone = ValidationUtil.sanitize(request.getParameter("phone"));
        String address = ValidationUtil.sanitize(request.getParameter("address"));

        if (!ValidationUtil.isNotEmpty(fullName) || !ValidationUtil.isNotEmpty(email)) {
            request.setAttribute("profileError", "Họ và tên cùng Email không được để trống!");
            doGet(request, response);
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("profileError", "Định dạng Email không hợp lệ!");
            doGet(request, response);
            return;
        }

        // Kiểm tra email nếu đổi sang email mới mà trùng với user khác
        if (!email.equalsIgnoreCase(sessionUser.getEmail()) && userDAO.checkEmailExists(email)) {
            request.setAttribute("profileError", "Email '" + email + "' đã được sử dụng bởi tài khoản khác!");
            doGet(request, response);
            return;
        }

        sessionUser.setFullName(fullName);
        sessionUser.setEmail(email);
        sessionUser.setPhone(phone);
        sessionUser.setAddress(address);

        boolean success = userDAO.updateProfile(sessionUser);
        if (success) {
            logDAO.log(sessionUser.getUserId(), "UPDATE_PROFILE", "Khách hàng cập nhật thông tin cá nhân.");
            request.setAttribute("profileSuccess", "Cập nhật thông tin hồ sơ thành công!");
        } else {
            request.setAttribute("profileError", "Không thể cập nhật hồ sơ. Vui lòng thử lại!");
        }

        doGet(request, response);
    }

    /**
     * Xử lý đổi mật khẩu tài khoản.
     */
    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response,
                                      HttpSession session, User sessionUser)
            throws ServletException, IOException {
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!ValidationUtil.isNotEmpty(oldPassword) || !ValidationUtil.isNotEmpty(newPassword)
                || !ValidationUtil.isNotEmpty(confirmPassword)) {
            request.setAttribute("passwordError", "Vui lòng nhập đầy đủ mật khẩu cũ và mới!");
            doGet(request, response);
            return;
        }

        // 1. Kiểm tra mật khẩu cũ có đúng không
        User dbUser = userDAO.getUserById(sessionUser.getUserId());
        if (dbUser == null || !PasswordUtil.checkPassword(oldPassword, dbUser.getPasswordHash())) {
            request.setAttribute("passwordError", "Mật khẩu hiện tại không chính xác!");
            doGet(request, response);
            return;
        }

        // 2. Kiểm tra độ dài mật khẩu mới
        if (newPassword.length() < 6) {
            request.setAttribute("passwordError", "Mật khẩu mới phải có tối thiểu 6 ký tự!");
            doGet(request, response);
            return;
        }

        // 3. Kiểm tra mật khẩu xác nhận
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("passwordError", "Mật khẩu mới và mật khẩu xác nhận không trùng khớp!");
            doGet(request, response);
            return;
        }

        // 4. Băm mật khẩu mới và cập nhật vào CSDL
        String newHash = PasswordUtil.hashPassword(newPassword);
        boolean success = userDAO.changePassword(sessionUser.getUserId(), newHash);

        if (success) {
            logDAO.log(sessionUser.getUserId(), "CHANGE_PASSWORD", "Người dùng đã đổi mật khẩu thành công.");
            request.setAttribute("passwordSuccess", "Đổi mật khẩu thành công!");
        } else {
            request.setAttribute("passwordError", "Có lỗi xảy ra khi đổi mật khẩu. Vui lòng thử lại!");
        }

        doGet(request, response);
    }
}
