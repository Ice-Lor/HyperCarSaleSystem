package controller.client;

import dal.ActivityLogDAO;
import dal.CarDAO;
import dal.TestDriveDAO;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Car;
import model.TestDriveBooking;
import model.User;
import util.ValidationUtil;

/**
 * Controller xử lý Đăng ký và Quản lý lịch Lái thử trải nghiệm siêu xe Track VIP.
 */
@WebServlet(name = "TestDriveController", urlPatterns = {"/test-drive"})
public class TestDriveController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final TestDriveDAO testDriveDAO = new TestDriveDAO();
    private final ActivityLogDAO logDAO = new ActivityLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=auth_required");
            return;
        }

        // 1. Lấy danh sách siêu xe đang mở bán để hiển thị trong ô chọn (Select Dropdown)
        List<Car> availableCars = carDAO.searchCars(null, null, null, null, null, "newest", 1, 100);
        request.setAttribute("availableCars", availableCars);

        // 2. Xe được chọn sẵn từ trang chi tiết (nếu có tham số ?carId=...)
        int preselectedCarId = ValidationUtil.parseInt(request.getParameter("carId"), 0);
        request.setAttribute("preselectedCarId", preselectedCarId);

        // 3. Lấy lịch sử đăng ký lái thử của chính khách hàng này
        List<TestDriveBooking> myBookings = testDriveDAO.getBookingsByUserId(currentUser.getUserId());
        request.setAttribute("myBookings", myBookings);

        request.getRequestDispatcher("/WEB-INF/views/client/test-drive.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=auth_required");
            return;
        }

        int carId = ValidationUtil.parseInt(request.getParameter("carId"), 0);
        String bookingDateRaw = request.getParameter("bookingDate");
        String timeSlot = ValidationUtil.sanitize(request.getParameter("timeSlot"));
        String locationTrack = ValidationUtil.sanitize(request.getParameter("locationTrack"));
        String driverLicense = ValidationUtil.sanitize(request.getParameter("driverLicenseNumber"));
        String note = ValidationUtil.sanitize(request.getParameter("note"));

        // 1. Kiểm tra tính hợp lệ của dữ liệu
        if (carId <= 0 || !ValidationUtil.isNotEmpty(bookingDateRaw) || !ValidationUtil.isNotEmpty(timeSlot)
                || !ValidationUtil.isNotEmpty(locationTrack) || !ValidationUtil.isNotEmpty(driverLicense)) {
            request.setAttribute("error", "Vui lòng điền đầy đủ các thông tin đăng ký lái thử bắt buộc!");
            doGet(request, response);
            return;
        }

        Date bookingDate;
        try {
            bookingDate = Date.valueOf(bookingDateRaw);
            // Kiểm tra ngày không được ở trong quá khứ
            Date today = new Date(System.currentTimeMillis());
            if (bookingDate.before(today)) {
                request.setAttribute("error", "Ngày đăng ký lái thử phải từ ngày hôm nay trở đi!");
                doGet(request, response);
                return;
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Định dạng ngày đăng ký không hợp lệ!");
            doGet(request, response);
            return;
        }

        // 2. Tạo đối tượng đăng ký
        TestDriveBooking booking = new TestDriveBooking();
        booking.setUserId(currentUser.getUserId());
        booking.setCarId(carId);
        booking.setBookingDate(bookingDate);
        booking.setTimeSlot(timeSlot);
        booking.setLocationTrack(locationTrack);
        booking.setDriverLicenseNumber(driverLicense);
        booking.setNote(note);
        booking.setStatus("PENDING");

        int bookingId = testDriveDAO.createBooking(booking);
        if (bookingId > 0) {
            logDAO.log(currentUser.getUserId(), "BOOK_TESTDRIVE", 
                    "Khách hàng " + currentUser.getUsername() + " đã đăng ký lái thử xe ID: " + carId);
            response.sendRedirect(request.getContextPath() + "/test-drive?success=1");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi tạo lịch đăng ký. Vui lòng thử lại!");
            doGet(request, response);
        }
    }
}
