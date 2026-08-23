package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller Điều Hướng Trung Tâm (Front Controller Pattern - Chuẩn đồ án PRJ301 / FPT University).
 * Mọi request (GET & POST) đều đi qua MainController?action=... để phân phối tới Controller chuyên trách.
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

        String actionLower = action.trim().toLowerCase();
        String url = "/home"; // URL mặc định

        switch (actionLower) {
            // ================= 1. XÁC THỰC TÀI KHOẢN =================
            case "login":
            case "loginpage":
            case "signin":
                url = "/login";
                break;
            case "logout":
            case "signout":
                url = "/logout";
                break;
            case "register":
            case "registerpage":
            case "signup":
                url = "/register";
                break;
            case "profile":
            case "updateprofile":
            case "changepassword":
            case "update_profile":
            case "change_password":
                url = "/profile";
                break;

            // ================= 2. KHÁCH HÀNG & SHOWROOM =================
            case "home":
            case "homepage":
            case "index":
                url = "/home";
                break;
            case "cars":
            case "carlist":
            case "search":
            case "filter":
                url = "/cars";
                break;
            case "cardetail":
            case "viewcar":
            case "detail":
                url = "/car-detail";
                break;
            case "submitreview":
            case "review":
            case "addreview":
                url = "/submit-review";
                break;
            case "testdrive":
            case "booktestdrive":
                url = "/test-drive";
                break;

            // ================= 3. GIỎ HÀNG & ĐẶT CỌC =================
            case "cart":
            case "viewcart":
            case "addtocart":
            case "updatecart":
            case "removecart":
            case "clearcart":
                url = "/cart";
                break;
            case "checkout":
            case "placeorder":
            case "deposit":
                url = "/checkout";
                break;
            case "orderhistory":
            case "myorders":
                url = "/order-history";
                break;
            case "orderdetail":
            case "vieworder":
                url = "/order-detail";
                break;
            case "ordersuccess":
                url = "/order-success";
                break;

            // ================= 4. QUẢN TRỊ VIÊN (ADMIN) =================
            case "admindashboard":
            case "dashboard":
                url = "/admin/dashboard";
                break;
            case "admincars":
            case "createcar":
            case "editcar":
            case "deletecar":
            case "togglecarstatus":
                url = "/admin/cars";
                break;
            case "adminbrands":
            case "createbrand":
            case "editbrand":
            case "deletebrand":
                url = "/admin/brands";
                break;
            case "adminorders":
            case "updateorderstatus":
                url = "/admin/orders";
                break;
            case "adminusers":
            case "toggleuserstatus":
                url = "/admin/users";
                break;
            case "adminbookings":
            case "updatebookingstatus":
                url = "/admin/bookings";
                break;
            case "exportreport":
            case "exportorders":
                url = "/admin/export-report";
                break;

            // ================= 5. AJAX REST APIs =================
            case "apisearch":
                url = "/api/search";
                break;
            case "apicheckcoupon":
                url = "/api/coupon/check";
                break;
            case "apicart":
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
