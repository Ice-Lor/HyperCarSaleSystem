package controller.client;

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

public class OrderHistoryController extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String orderIdStr = request.getParameter("id");
        if (orderIdStr != null && !orderIdStr.trim().isEmpty()) {
            try {
                int orderId = Integer.parseInt(orderIdStr.trim());
                Order order = orderDAO.getOrderById(orderId);
                // Đảm bảo chỉ xem được đơn của chính mình (hoặc Admin)
                if (order != null && (order.getUserId() == currentUser.getUserId() || currentUser.isAdmin())) {
                    request.setAttribute("order", order);
                    request.getRequestDispatcher("/WEB-INF/views/client/order-detail.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException ignored) {}
        }

        List<Order> myOrders = orderDAO.getOrdersByUserId(currentUser.getUserId());
        request.setAttribute("myOrders", myOrders);
        request.getRequestDispatcher("/WEB-INF/views/client/order-history.jsp").forward(request, response);
    }
}
