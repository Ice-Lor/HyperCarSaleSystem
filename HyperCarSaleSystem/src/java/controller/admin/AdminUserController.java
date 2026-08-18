package controller.admin;

import dal.ActivityLogDAO;
import dal.UserDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import util.CSRFUtil;

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

        if (!CSRFUtil.isValidToken(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        int userId = Integer.parseInt(request.getParameter("userId"));
        int status = Integer.parseInt(request.getParameter("status"));

        if (userId == currentUser.getUserId()) {
            session.setAttribute("errorMessage", "Không thể tự khóa tài khoản của chính mình!");
        } else {
            userDAO.toggleUserStatus(userId, status);
            logDAO.log(currentUser.getUserId(), "TOGGLE_USER_STATUS", "Thay đổi trạng thái tài khoản ID: " + userId + " thành: " + status);
            session.setAttribute("successMessage", "Cập nhật trạng thái người dùng thành công!");
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
