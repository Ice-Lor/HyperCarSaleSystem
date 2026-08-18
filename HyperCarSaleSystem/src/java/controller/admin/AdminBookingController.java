package controller.admin;

import dal.ActivityLogDAO;
import dal.TestDriveDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.TestDriveBooking;
import model.User;
import util.CSRFUtil;

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

        if (!CSRFUtil.isValidToken(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/bookings");
            return;
        }

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        String status = request.getParameter("status");

        testDriveDAO.updateBookingStatus(bookingId, status);
        logDAO.log(currentUser.getUserId(), "UPDATE_BOOKING", "Cập nhật lịch lái thử ID " + bookingId + " thành " + status);
        session.setAttribute("successMessage", "Cập nhật trạng thái lịch lái thử thành công!");

        response.sendRedirect(request.getContextPath() + "/admin/bookings");
    }
}
