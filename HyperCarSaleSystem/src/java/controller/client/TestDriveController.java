package controller.client;

import dal.CarDAO;
import dal.TestDriveDAO;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private final CarDAO carDAO = new CarDAO();
    private final TestDriveDAO testDriveDAO = new TestDriveDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        String carIdStr = request.getParameter("carId");
        if (carIdStr != null && !carIdStr.isEmpty()) {
            Car car = carDAO.getCarById(Integer.parseInt(carIdStr));
            request.setAttribute("selectedCar", car);
        }

        List<Car> cars = carDAO.getAllCarsAdmin();
        List<TestDriveBooking> myBookings = testDriveDAO.getBookingsByUserId(currentUser.getUserId());

        request.setAttribute("cars", cars);
        request.setAttribute("myBookings", myBookings);
        request.getRequestDispatcher("/WEB-INF/views/client/test-drive.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!CSRFUtil.isValidToken(request)) {
            response.sendRedirect(request.getContextPath() + "/test-drive");
            return;
        }

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            int carId = Integer.parseInt(request.getParameter("carId"));
            String bookingDateStr = request.getParameter("bookingDate");
            String timeSlot = request.getParameter("timeSlot");
            String locationTrack = ValidationUtil.sanitize(request.getParameter("locationTrack"));
            String license = ValidationUtil.sanitize(request.getParameter("driverLicenseNumber"));
            String note = ValidationUtil.sanitize(request.getParameter("note"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date bookingDate = sdf.parse(bookingDateStr);

            TestDriveBooking booking = new TestDriveBooking();
            booking.setUserId(currentUser.getUserId());
            booking.setCarId(carId);
            booking.setBookingDate(bookingDate);
            booking.setTimeSlot(timeSlot);
            booking.setLocationTrack(locationTrack);
            booking.setDriverLicenseNumber(license);
            booking.setNote(note);

            boolean success = testDriveDAO.insertBooking(booking);
            if (success) {
                session.setAttribute("successMessage", "Đại ca đã đặt lịch trải nghiệm lái thử thành công! Chuyên viên VIP sẽ liên hệ xác nhận.");
            } else {
                session.setAttribute("errorMessage", "Đặt lịch không thành công. Vui lòng thử lại!");
            }

        } catch (Exception e) {
            session.setAttribute("errorMessage", "Dữ liệu đặt lịch không hợp lệ!");
        }

        response.sendRedirect(request.getContextPath() + "/test-drive");
    }
}
