package controller.admin;

import dal.UserDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Role;
import model.User;
import util.CSRFUtil;

public class AdminUserController extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> users = userDAO.getAllUsers();
        List<Role> roles = userDAO.getAllRoles();
        request.setAttribute("users", users);
        request.setAttribute("roles", roles);
        request.getRequestDispatcher("/WEB-INF/views/admin/user-manage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (!CSRFUtil.isValidToken(request)) {
            if (session != null) session.setAttribute("errorMessage", "CSRF Token không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        String action = request.getParameter("action");

        if ("toggleStatus".equalsIgnoreCase(action)) {
            try {
                int userId = Integer.parseInt(request.getParameter("userId"));
                int currentStatus = Integer.parseInt(request.getParameter("currentStatus"));
                int newStatus = (currentStatus == 1) ? 0 : 1;
                userDAO.updateStatus(userId, newStatus);
                if (session != null) session.setAttribute("toastMessage", "Đã cập nhật trạng thái tài khoản!");
            } catch (Exception e) {
                if (session != null) session.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
