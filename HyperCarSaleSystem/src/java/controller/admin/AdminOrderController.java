package controller.admin;

import dal.OrderDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Order;
import util.CSRFUtil;

public class AdminOrderController extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int orderId = Integer.parseInt(idStr.trim());
                Order order = orderDAO.getOrderById(orderId);
                if (order != null) {
                    request.setAttribute("order", order);
                    request.getRequestDispatcher("/WEB-INF/views/admin/order-detail.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException ignored) {}
        }

        List<Order> orders = orderDAO.getAllOrders();
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/views/admin/order-manage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (!CSRFUtil.isValidToken(request)) {
            if (session != null) session.setAttribute("errorMessage", "CSRF Token không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/admin/orders");
            return;
        }

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String status = request.getParameter("status");
            orderDAO.updateOrderStatus(orderId, status);
            if (session != null) session.setAttribute("toastMessage", "Cập nhật trạng thái đơn hàng thành: " + status);
        } catch (Exception e) {
            if (session != null) session.setAttribute("errorMessage", "Lỗi cập nhật: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}
