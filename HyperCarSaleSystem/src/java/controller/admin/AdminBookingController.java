package controller.admin;

import dal.TestDriveDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.TestDriveBooking;
import util.CSRFUtil;

public class AdminBookingController extends HttpServlet {

    private final TestDriveDAO testDriveDAO = new TestDriveDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<TestDriveBooking> bookings = testDriveDAO.getAllBookings();
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("/WEB-INF/views/admin/booking-manage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (!CSRFUtil.isValidToken(request)) {
            if (session != null) session.setAttribute("errorMessage", "CSRF Token không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/admin/bookings");
            return;
        }

        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            String status = request.getParameter("status");
            testDriveDAO.updateStatus(bookingId, status);
            if (session != null) session.setAttribute("toastMessage", "Đã cập nhật trạng thái lịch lái thử!");
        } catch (Exception e) {
            if (session != null) session.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/bookings");
    }
}
