package controller.admin;

import dal.ActivityLogDAO;
import dal.CarDAO;
import dal.OrderDAO;
import dal.UserDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ActivityLog;
import model.Order;

public class AdminDashboardController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final UserDAO userDAO = new UserDAO();
    private final ActivityLogDAO logDAO = new ActivityLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BigDecimal totalRevenue = orderDAO.getTotalRevenue();
        int totalCars = carDAO.countTotalCars();
        int totalOrders = orderDAO.countTotalOrders();
        int totalUsers = userDAO.countTotalUsers();

        Map<String, Integer> carsByBrand = carDAO.countCarsByBrand();
        List<Order> recentOrders = orderDAO.getAllOrdersAdmin();
        if (recentOrders.size() > 5) {
            recentOrders = recentOrders.subList(0, 5);
        }
        List<ActivityLog> recentLogs = logDAO.getRecentLogs(6);

        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("totalCars", totalCars);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("carsByBrand", carsByBrand);
        request.setAttribute("recentOrders", recentOrders);
        request.setAttribute("recentLogs", recentLogs);

        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }
}
