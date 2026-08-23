package controller.auth;

import dal.ActivityLogDAO;
import dal.UserDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import util.PasswordUtil;
import util.ValidationUtil;

/**
 * Controller xử lý Đăng ký tài khoản thành viên VIP mới.
 */
@WebServlet(name = "RegisterController", urlPatterns = {"/register"})
public class RegisterController extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final ActivityLogDAO logDAO = new ActivityLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = ValidationUtil.sanitize(request.getParameter("username"));
        String email = ValidationUtil.sanitize(request.getParameter("email"));
        String fullName = ValidationUtil.sanitize(request.getParameter("fullName"));
        String phone = ValidationUtil.sanitize(request.getParameter("phone"));
        String address = ValidationUtil.sanitize(request.getParameter("address"));
        String rawPassword = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Giữ lại dữ liệu đã nhập trên form khi có lỗi
        request.setAttribute("username", username);
        request.setAttribute("email", email);
        request.setAttribute("fullName", fullName);
        request.setAttribute("phone", phone);
        request.setAttribute("address", address);

        // 1. Kiểm tra các trường bắt buộc
        if (!ValidationUtil.isNotEmpty(username) || !ValidationUtil.isNotEmpty(email) 
                || !ValidationUtil.isNotEmpty(fullName) || !ValidationUtil.isNotEmpty(rawPassword)) {
            request.setAttribute("error", "Vui lòng điền đầy đủ các thông tin bắt buộc!");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        // 2. Kiểm tra độ dài Tên đăng nhập
        if (username.length() < 4 || username.length() > 30) {
            request.setAttribute("error", "Tên đăng nhập phải có độ dài từ 4 đến 30 ký tự!");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        // 3. Kiểm tra định dạng Email
        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Định dạng địa chỉ Email không hợp lệ!");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        // 4. Kiểm tra định dạng Số điện thoại (nếu có nhập)
        if (ValidationUtil.isNotEmpty(phone) && !ValidationUtil.isValidPhone(phone)) {
            request.setAttribute("error", "Số điện thoại không đúng định dạng (phải gồm 10 chữ số)! ");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        // 5. Kiểm tra độ dài và độ khớp của Mật khẩu
        if (rawPassword.length() < 6) {
            request.setAttribute("error", "Mật khẩu bảo mật phải có ít nhất 6 ký tự!");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        if (!rawPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không trùng khớp!");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        // 6. Kiểm tra trùng lặp Tên đăng nhập
        if (userDAO.checkUsernameExists(username)) {
            request.setAttribute("error", "Tên đăng nhập '" + username + "' đã được sử dụng bởi khách hàng khác!");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        // 7. Kiểm tra trùng lặp Email
        if (userDAO.checkEmailExists(email)) {
            request.setAttribute("error", "Địa chỉ Email '" + email + "' đã được đăng ký trong hệ thống!");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        // 8. Băm mật khẩu bằng jBCrypt
        String passwordHash = PasswordUtil.hashPassword(rawPassword);

        // 9. Tạo đối tượng User và lưu vào CSDL
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(passwordHash);
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setAddress(address);

        int newUserId = userDAO.register(newUser);

        if (newUserId > 0) {
            logDAO.log(newUserId, "REGISTER", "Thành viên VIP " + username + " đã đăng ký tài khoản thành công.");
            response.sendRedirect(request.getContextPath() + "/login?success=registered");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra trong quá trình đăng ký. Vui lòng thử lại sau!");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
        }
    }
}
