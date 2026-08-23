package controller.client;

import dal.OrderDAO;
import java.io.IOException;
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
 * Controller xem Chi tiết hợp đồng đặt cọc siêu xe (Hóa đơn điện tử & Thông tin thanh toán).
 */
@WebServlet(name = "OrderDetailController", urlPatterns = {"/order-detail"})
public class OrderDetailController extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int orderId = ValidationUtil.parseInt(request.getParameter("id"), 0);
        String orderCode = ValidationUtil.sanitize(request.getParameter("code"));

        Order order = null;
        if (orderId > 0) {
            order = orderDAO.getOrderById(orderId);
        } else if (ValidationUtil.isNotEmpty(orderCode)) {
            order = orderDAO.getOrderByCode(orderCode);
        }

        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/order-history");
            return;
        }

        // Kiểm tra quyền hạn xem đơn
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        if (currentUser == null || (currentUser.getUserId() != order.getUserId() && !currentUser.isAdmin())) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        request.setAttribute("order", order);
        request.getRequestDispatcher("/WEB-INF/views/client/order-detail.jsp").forward(request, response);
    }
}
