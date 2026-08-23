package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller Điều Hướng Trung Tâm (Front Controller Pattern - Chuẩn đồ án PRJ301).
 * Mọi request đều có thể đi qua MainController?action=... để phân phối tới các Controller tương ứng.
 */
@WebServlet(name = "MainController", urlPatterns = {"/MainController", "/main"})
public class MainController extends HttpServlet {

    private static final String DEFAULT_ACTION = "Home";

    /**
     * Xử lý điều phối tập trung cho cả phương thức GET và POST.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");

        if (action == null || action.trim().isEmpty()) {
            action = DEFAULT_ACTION;
        }

        String url = "/home"; // URL mặc định

        switch (action) {
            // ================= 1. XÁC THỰC TÀI KHOẢN =================
            case "Login":
            case "LoginPage":
                url = "/login";
                break;
            case "Logout":
                url = "/logout";
                break;
            case "Register":
            case "RegisterPage":
                url = "/register";
                break;
            case "Profile":
            case "UpdateProfile":
            case "ChangePassword":
                url = "/profile";
                break;

            // ================= 2. KHÁCH HÀNG & SHOWROOM =================
            case "Home":
                url = "/home";
                break;
            case "Cars":
            case "Search":
            case "CarList":
                url = "/cars";
                break;
            case "CarDetail":
            case "ViewCar":
                url = "/car-detail";
                break;
            case "SubmitReview":
            case "Review":
                url = "/submit-review";
                break;
            case "TestDrive":
            case "BookTestDrive":
                url = "/test-drive";
                break;

            // ================= 3. GIỎ HÀNG & ĐẶT CỌC =================
            case "Cart":
            case "AddToCart":
            case "UpdateCart":
            case "RemoveCart":
            case "ClearCart":
                url = "/cart";
                break;
            case "Checkout":
            case "PlaceOrder":
                url = "/checkout";
                break;
            case "OrderHistory":
                url = "/order-history";
                break;
            case "OrderDetail":
                url = "/order-detail";
                break;
            case "OrderSuccess":
                url = "/order-success";
                break;

            // ================= 4. QUẢN TRỊ VIÊN (ADMIN) =================
            case "AdminDashboard":
            case "Dashboard":
                url = "/admin/dashboard";
                break;
            case "AdminCars":
            case "CreateCar":
            case "EditCar":
            case "DeleteCar":
            case "ToggleCarStatus":
                url = "/admin/cars";
                break;
            case "AdminBrands":
            case "CreateBrand":
            case "EditBrand":
            case "DeleteBrand":
                url = "/admin/brands";
                break;
            case "AdminOrders":
            case "UpdateOrderStatus":
                url = "/admin/orders";
                break;
            case "AdminUsers":
            case "ToggleUserStatus":
                url = "/admin/users";
                break;
            case "AdminBookings":
            case "UpdateBookingStatus":
                url = "/admin/bookings";
                break;
            case "ExportReport":
                url = "/admin/export-report";
                break;

            // ================= 5. AJAX APIs =================
            case "ApiSearch":
                url = "/api/search";
                break;
            case "ApiCheckCoupon":
                url = "/api/coupon/check";
                break;
            case "ApiCart":
                url = "/api/cart";
                break;

            default:
                url = "/home";
                break;
        }

        // Chuyển tiếp (forward) request tới Controller chuyên trách
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
