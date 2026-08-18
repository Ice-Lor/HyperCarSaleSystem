package controller.admin;

import dal.ActivityLogDAO;
import dal.OrderDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Order;
import model.User;
import util.CSRFUtil;

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

        if (!CSRFUtil.isValidToken(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
            return;
        }

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String newStatus = request.getParameter("status");

        orderDAO.updateOrderStatus(orderId, newStatus);
        logDAO.log(currentUser.getUserId(), "UPDATE_ORDER", "Cập nhật trạng thái đơn hàng ID " + orderId + " thành " + newStatus);
        session.setAttribute("successMessage", "Cập nhật trạng thái đơn cọc thành công!");

        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}
