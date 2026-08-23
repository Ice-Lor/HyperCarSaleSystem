package controller.admin;

import dal.ActivityLogDAO;
import dal.TestDriveDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.TestDriveBooking;
import model.User;
import util.ValidationUtil;

/**
 * Controller Quản lý và Phê duyệt Lịch Lái Thử Trường Đua VIP (Admin Booking Management).
 */
@WebServlet(name = "AdminBookingController", urlPatterns = {"/admin/bookings"})
public class AdminBookingController extends HttpServlet {

    private final TestDriveDAO testDriveDAO = new TestDriveDAO();
    private final ActivityLogDAO logDAO = new ActivityLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<TestDriveBooking> bookings = testDriveDAO.getAllBookingsAdmin();
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("/WEB-INF/views/admin/booking-manage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("update_status".equalsIgnoreCase(action)) {
            int bookingId = ValidationUtil.parseInt(request.getParameter("bookingId"), 0);
            String status = ValidationUtil.sanitize(request.getParameter("status"));

            if (bookingId > 0 && ValidationUtil.isNotEmpty(status)) {
                testDriveDAO.updateBookingStatus(bookingId, status);
                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("user") != null) {
                    User admin = (User) session.getAttribute("user");
                    logDAO.log(admin.getUserId(), "UPDATE_BOOKING_STATUS", 
                            "Admin đã cập nhật trạng thái lịch lái thử ID " + bookingId + " sang: " + status);
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/bookings?msg=status_updated");
    }
}
