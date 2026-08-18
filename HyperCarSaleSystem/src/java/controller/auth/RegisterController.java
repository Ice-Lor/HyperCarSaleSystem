package controller.auth;

import dal.ActivityLogDAO;
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
    private final ActivityLogDAO logDAO = new ActivityLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!CSRFUtil.isValidToken(request)) {
            request.setAttribute("errorMessage", "CSRF Token không hợp lệ!");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        String username = ValidationUtil.sanitize(request.getParameter("username"));
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = ValidationUtil.sanitize(request.getParameter("fullName"));
        String email = ValidationUtil.sanitize(request.getParameter("email"));
        String phone = ValidationUtil.sanitize(request.getParameter("phone"));
        String address = ValidationUtil.sanitize(request.getParameter("address"));

        if (username == null || password == null || fullName == null || email == null
                || username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            request.setAttribute("errorMessage", "Vui lòng điền đầy đủ các thông tin bắt buộc!");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("errorMessage", "Định dạng Email không hợp lệ!");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        if (userDAO.getUserByUsername(username) != null) {
            request.setAttribute("errorMessage", "Tên đăng nhập này đã được sử dụng!");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(PasswordUtil.hashPassword(password));
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setAddress(address);

        boolean success = userDAO.register(newUser);
        if (success) {
            logDAO.log(null, "REGISTER", "Khách hàng mới đăng ký tài khoản: " + username);
            HttpSession session = request.getSession(true);
            session.setAttribute("successMessage", "Chúc mừng đại ca đã đăng ký thành công! Hãy đăng nhập để trải nghiệm.");
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            request.setAttribute("errorMessage", "Đăng ký thất bại. Vui lòng thử lại sau!");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
        }
    }
}
