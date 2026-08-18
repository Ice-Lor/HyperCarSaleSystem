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
        User currentUser = (User) session.getAttribute("currentUser");

        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            int orderId = Integer.parseInt(idStr);
            Order order = orderDAO.getOrderById(orderId);
            if (order != null && order.getUserId() == currentUser.getUserId()) {
                request.setAttribute("order", order);
                request.getRequestDispatcher("/WEB-INF/views/client/order-detail.jsp").forward(request, response);
                return;
            }
        }

        List<Order> orders = orderDAO.getOrdersByUserId(currentUser.getUserId());
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/views/client/order-history.jsp").forward(request, response);
    }
}
