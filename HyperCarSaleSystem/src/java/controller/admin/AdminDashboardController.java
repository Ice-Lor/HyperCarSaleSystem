package controller.admin;

import dal.ActivityLogDAO;
import dal.CarDAO;
import dal.OrderDAO;
import dal.TestDriveDAO;
import dal.UserDAO;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ActivityLog;
import model.Order;
import model.TestDriveBooking;

public class AdminDashboardController extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final CarDAO carDAO = new CarDAO();
    private final TestDriveDAO testDriveDAO = new TestDriveDAO();
    private final UserDAO userDAO = new UserDAO();
    private final ActivityLogDAO logDAO = new ActivityLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Map<String, Object> stats = orderDAO.getDashboardStats();
        List<Order> recentOrders = orderDAO.getAllOrders();
        if (recentOrders.size() > 5) {
            recentOrders = recentOrders.subList(0, 5);
        }

        List<TestDriveBooking> recentBookings = testDriveDAO.getAllBookings();
        if (recentBookings.size() > 5) {
            recentBookings = recentBookings.subList(0, 5);
        }

        List<ActivityLog> recentLogs = logDAO.getRecentLogs(8);

        request.setAttribute("stats", stats);
        request.setAttribute("recentOrders", recentOrders);
        request.setAttribute("recentBookings", recentBookings);
        request.setAttribute("recentLogs", recentLogs);

        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }
}
