package controller.client;

import dal.CarDAO;
import dal.TestDriveDAO;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Car;
import model.TestDriveBooking;
import model.User;
import util.CSRFUtil;
import util.ValidationUtil;

public class TestDriveController extends HttpServlet {

    private final TestDriveDAO testDriveDAO = new TestDriveDAO();
    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        
        String carIdStr = request.getParameter("carId");
        if (carIdStr != null && !carIdStr.trim().isEmpty()) {
            try {
                Car selectedCar = carDAO.getCarById(Integer.parseInt(carIdStr.trim()));
                request.setAttribute("selectedCar", selectedCar);
            } catch (NumberFormatException ignored) {}
        }

        List<Car> carList = carDAO.filterCars(null, null, null, null, null, null, null, 1, 20);
        request.setAttribute("carList", carList);

        if (currentUser != null) {
            List<TestDriveBooking> myBookings = testDriveDAO.getBookingsByUserId(currentUser.getUserId());
            request.setAttribute("myBookings", myBookings);
        }

        request.getRequestDispatcher("/WEB-INF/views/client/test-drive.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (!CSRFUtil.isValidToken(request)) {
            session.setAttribute("errorMessage", "Phiên làm việc hết hạn hoặc token CSRF không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/test-drive");
            return;
        }

        try {
            int carId = Integer.parseInt(request.getParameter("carId"));
            String dateStr = request.getParameter("bookingDate");
            String timeSlot = request.getParameter("timeSlot");
            String locationTrack = ValidationUtil.sanitize(request.getParameter("locationTrack"));
            String licenseNumber = ValidationUtil.sanitize(request.getParameter("driverLicenseNumber"));
            String note = ValidationUtil.sanitize(request.getParameter("note"));

            Date bookingDate = Date.valueOf(dateStr);

            TestDriveBooking booking = new TestDriveBooking();
            booking.setUserId(currentUser.getUserId());
            booking.setCarId(carId);
            booking.setBookingDate(bookingDate);
            booking.setTimeSlot(timeSlot);
            booking.setLocationTrack(locationTrack);
            booking.setDriverLicenseNumber(licenseNumber);
            booking.setNote(note);
            booking.setStatus("PENDING");

            boolean success = testDriveDAO.createBooking(booking);
            if (success) {
                session.setAttribute("toastMessage", "Đặt lịch lái thử VIP Track thành công! Chuyên viên tư vấn sẽ liên hệ với đại ca sớm nhất.");
            } else {
                session.setAttribute("errorMessage", "Không thể gửi yêu cầu đặt lịch. Vui lòng thử lại!");
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Thông tin đặt lịch không hợp lệ: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/test-drive");
    }
}
