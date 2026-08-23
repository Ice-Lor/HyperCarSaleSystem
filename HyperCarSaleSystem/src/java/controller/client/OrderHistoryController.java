package controller.client;

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

/**
 * Controller xem Lịch sử các hợp đồng đặt cọc siêu xe của khách hàng VIP.
 */
@WebServlet(name = "OrderHistoryController", urlPatterns = {"/order-history"})
public class OrderHistoryController extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=auth_required");
            return;
        }

        // Lấy toàn bộ danh sách hợp đồng đặt cọc của khách hàng này
        List<Order> orders = orderDAO.getOrdersByUserId(currentUser.getUserId());
        request.setAttribute("orders", orders);

        request.getRequestDispatcher("/WEB-INF/views/client/order-history.jsp").forward(request, response);
    }
}
