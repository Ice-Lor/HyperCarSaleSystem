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
 * Controller hiển thị Trang xác nhận đặt cọc thành công (Order Success / Thank You).
 */
@WebServlet(name = "OrderSuccessController", urlPatterns = {"/order-success"})
public class OrderSuccessController extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderCode = ValidationUtil.sanitize(request.getParameter("code"));

        if (!ValidationUtil.isNotEmpty(orderCode)) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Lấy thông tin hợp đồng đặt cọc kèm chi tiết xe
        Order order = orderDAO.getOrderByCode(orderCode);
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Kiểm tra bảo mật: Khách hàng chỉ được xem đơn của chính mình (hoặc Admin)
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        if (currentUser == null || (currentUser.getUserId() != order.getUserId() && !currentUser.isAdmin())) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        request.setAttribute("order", order);
        request.getRequestDispatcher("/WEB-INF/views/client/order-success.jsp").forward(request, response);
    }
}
