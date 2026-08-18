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

public class RegisterController extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);

        if (!CSRFUtil.isValidToken(request)) {
            request.setAttribute("error", "CSRF Token không hợp lệ!");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        String username = ValidationUtil.sanitize(request.getParameter("username"));
        String fullName = ValidationUtil.sanitize(request.getParameter("fullName"));
        String email = ValidationUtil.sanitize(request.getParameter("email"));
        String phone = ValidationUtil.sanitize(request.getParameter("phone"));
        String address = ValidationUtil.sanitize(request.getParameter("address"));
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate server-side
        if (username == null || username.length() < 3) {
            request.setAttribute("error", "Tên đăng nhập phải có ít nhất 3 ký tự!");
            forwardWithError(request, response, username, fullName, email, phone, address);
            return;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Email không đúng định dạng!");
            forwardWithError(request, response, username, fullName, email, phone, address);
            return;
        }
        if (password == null || password.length() < 6) {
            request.setAttribute("error", "Mật khẩu phải có tối thiểu 6 ký tự!");
            forwardWithError(request, response, username, fullName, email, phone, address);
            return;
        }
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            forwardWithError(request, response, username, fullName, email, phone, address);
            return;
        }
        if (userDAO.checkUsernameExists(username)) {
            request.setAttribute("error", "Tên đăng nhập đã tồn tại trong hệ thống!");
            forwardWithError(request, response, username, fullName, email, phone, address);
            return;
        }
        if (userDAO.checkEmailExists(email)) {
            request.setAttribute("error", "Địa chỉ email đã được sử dụng!");
            forwardWithError(request, response, username, fullName, email, phone, address);
            return;
        }

        // Tạo tài khoản mới với mật khẩu mã hóa BCrypt
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(PasswordUtil.hashPassword(password));
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setAddress(address);
        newUser.setRoleId(2); // Role CUSTOMER

        boolean success = userDAO.register(newUser);
        if (success) {
            session.setAttribute("toastMessage", "Đăng ký tài khoản VIP thành công! Vui lòng đăng nhập.");
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra trong quá trình đăng ký. Vui lòng thử lại!");
            forwardWithError(request, response, username, fullName, email, phone, address);
        }
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response,
                                  String username, String fullName, String email, String phone, String address)
            throws ServletException, IOException {
        request.setAttribute("username", username);
        request.setAttribute("fullName", fullName);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("address", address);
        request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
    }
}
