package controller.admin;

import dal.ActivityLogDAO;
import dal.OrderDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Order;
import model.User;
import util.ValidationUtil;

/**
 * Controller Quản lý Hợp đồng đặt cọc siêu xe (Admin Order Management).
 */
@WebServlet(name = "AdminOrderController", urlPatterns = {"/admin/orders"})
public class AdminOrderController extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final ActivityLogDAO logDAO = new ActivityLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Order> orders = orderDAO.getAllOrdersAdmin();
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/views/admin/order-manage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int orderId = ValidationUtil.parseInt(request.getParameter("orderId"), 0);
        String status = ValidationUtil.sanitize(request.getParameter("status"));

        if (orderId > 0 && ValidationUtil.isNotEmpty(status)) {
            boolean updated = orderDAO.updateOrderStatus(orderId, status);
            if (updated) {
                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("user") != null) {
                    User user = (User) session.getAttribute("user");
                    logDAO.log(user.getUserId(), "UPDATE_ORDER_STATUS", 
                            "Admin cập nhật trạng thái hợp đồng ID " + orderId + " sang: " + status);
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/orders?msg=status_updated");
    }
}
