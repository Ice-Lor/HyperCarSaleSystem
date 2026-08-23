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
import util.ValidationUtil;

/**
 * Controller xử lý Đăng nhập tài khoản khách hàng VIP và Quản trị viên.
 */
@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final ActivityLogDAO logDAO = new ActivityLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
            return;
        }

        // Chuyển tiếp tới giao diện đăng nhập
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = ValidationUtil.sanitize(request.getParameter("username"));
        String rawPassword = request.getParameter("password");

        // 1. Kiểm tra dữ liệu đầu vào
        if (!ValidationUtil.isNotEmpty(username) || !ValidationUtil.isNotEmpty(rawPassword)) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ Tên đăng nhập và Mật khẩu!");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        }

        // 2. Xác thực thông tin đăng nhập với cơ sở dữ liệu qua BCrypt
        User user = userDAO.login(username, rawPassword);

        if (user == null) {
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không chính xác (hoặc tài khoản đã bị khóa)!");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        }

        // 3. Đăng nhập thành công -> Lưu thông tin vào Session
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);

        // Ghi nhật ký đăng nhập
        logDAO.log(user.getUserId(), "LOGIN", "Người dùng " + user.getUsername() + " đã đăng nhập vào hệ thống.");

        // 4. Kiểm tra trang chuyển hướng trước đó (nếu bị AuthFilter chặn trước đó)
        String redirectUrl = (String) session.getAttribute("redirectUrl");
        if (redirectUrl != null && !redirectUrl.trim().isEmpty()) {
            session.removeAttribute("redirectUrl");
            response.sendRedirect(redirectUrl);
            return;
        }

        // Phân quyền chuyển hướng
        if (user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}
