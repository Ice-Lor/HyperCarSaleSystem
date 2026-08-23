package controller.admin;

import dal.ActivityLogDAO;
import dal.BrandDAO;
import dal.CarDAO;
import dal.OrderDAO;
import dal.TestDriveDAO;
import dal.UserDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ActivityLog;
import model.Order;

/**
 * Controller Bàn Quản Trị Trung Tâm (Admin Dashboard).
 * Thống kê KPI: Tổng xe, Tổng hãng, Tổng đơn đặt cọc, Tổng doanh thu tiền cọc, Đơn lái thử chờ duyệt.
 */
@WebServlet(name = "AdminDashboardController", urlPatterns = {"/admin/dashboard", "/admin"})
public class AdminDashboardController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final BrandDAO brandDAO = new BrandDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final UserDAO userDAO = new UserDAO();
    private final TestDriveDAO testDriveDAO = new TestDriveDAO();
    private final ActivityLogDAO logDAO = new ActivityLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Thống kê số liệu KPI
        int totalCars = carDAO.countTotalAvailableCars();
        int totalBrands = brandDAO.countTotalBrands();
        int totalOrders = orderDAO.countTotalOrders();
        BigDecimal totalRevenue = orderDAO.getTotalRevenue();
        int pendingBookings = testDriveDAO.countPendingBookings();
        int totalUsers = userDAO.getAllUsers().size();

        request.setAttribute("totalCars", totalCars);
        request.setAttribute("totalBrands", totalBrands);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("pendingBookings", pendingBookings);
        request.setAttribute("totalUsers", totalUsers);

        // 2. Danh sách 5 hợp đồng đặt cọc gần đây nhất
        List<Order> allOrders = orderDAO.getAllOrdersAdmin();
        List<Order> recentOrders = (allOrders.size() > 5) ? allOrders.subList(0, 5) : allOrders;
        request.setAttribute("recentOrders", recentOrders);

        // 3. Danh sách 8 hoạt động hệ thống gần đây nhất (Audit Trail)
        List<ActivityLog> recentLogs = logDAO.getRecentLogs(8);
        request.setAttribute("recentLogs", recentLogs);

        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }
}
