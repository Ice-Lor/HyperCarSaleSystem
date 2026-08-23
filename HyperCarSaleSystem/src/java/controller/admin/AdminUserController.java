package controller.admin;

import dal.ActivityLogDAO;
import dal.UserDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import util.ValidationUtil;

/**
 * Controller Quản lý Thành viên VIP và Khóa/Mở tài khoản (Admin User Management).
 */
@WebServlet(name = "AdminUserController", urlPatterns = {"/admin/users"})
public class AdminUserController extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final ActivityLogDAO logDAO = new ActivityLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<User> users = userDAO.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/views/admin/user-manage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("toggle_status".equalsIgnoreCase(action)) {
            int userId = ValidationUtil.parseInt(request.getParameter("userId"), 0);
            int newStatus = ValidationUtil.parseInt(request.getParameter("status"), 1);

            if (userId > 0) {
                userDAO.updateStatus(userId, newStatus);
                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("user") != null) {
                    User admin = (User) session.getAttribute("user");
                    String statusText = (newStatus == 1) ? "Mở khóa" : "Khóa tài khoản";
                    logDAO.log(admin.getUserId(), "TOGGLE_USER_STATUS", 
                            "Admin đã " + statusText + " thành viên ID: " + userId);
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/users?msg=status_updated");
    }
}
