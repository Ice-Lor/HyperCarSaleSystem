package controller.client;

import dal.CouponDAO;
import dal.OrderDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Cart;
import model.CartItem;
import model.Coupon;
import model.Order;
import model.User;
import util.ValidationUtil;

/**
 * Controller xử lý Thanh toán và Ký hợp đồng đặt cọc siêu xe (Checkout & Place Order).
 */
@WebServlet(name = "CheckoutController", urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final CouponDAO couponDAO = new CouponDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=auth_required");
            return;
        }

        Cart cart = (session != null) ? (Cart) session.getAttribute("cart") : null;
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        request.setAttribute("cart", cart);
        request.setAttribute("user", currentUser);
        request.getRequestDispatcher("/WEB-INF/views/client/checkout.jsp").forward(request, response);
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

        Cart cart = (session != null) ? (Cart) session.getAttribute("cart") : null;
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // 1. Đọc và làm sạch thông tin giao hàng & thanh toán
        String fullName = ValidationUtil.sanitize(request.getParameter("fullName"));
        String phone = ValidationUtil.sanitize(request.getParameter("phone"));
        String deliveryAddress = ValidationUtil.sanitize(request.getParameter("deliveryAddress"));
        String paymentMethod = ValidationUtil.sanitize(request.getParameter("paymentMethod"));
        String couponCode = ValidationUtil.sanitize(request.getParameter("couponCode"));
        String note = ValidationUtil.sanitize(request.getParameter("note"));

        // 2. Kiểm tra thông tin bắt buộc
        if (!ValidationUtil.isNotEmpty(fullName) || !ValidationUtil.isNotEmpty(phone) 
                || !ValidationUtil.isNotEmpty(deliveryAddress)) {
            request.setAttribute("error", "Vui lòng điền đầy đủ Họ tên, Số điện thoại và Địa chỉ bàn giao xe!");
            request.setAttribute("cart", cart);
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/WEB-INF/views/client/checkout.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtil.isValidPhone(phone)) {
            request.setAttribute("error", "Số điện thoại không đúng định dạng 10 chữ số!");
            request.setAttribute("cart", cart);
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/WEB-INF/views/client/checkout.jsp").forward(request, response);
            return;
        }

        // 3. Tính toán tổng tiền niêm yết và số tiền đặt cọc
        BigDecimal totalAmount = cart.getTotalAmount();
        BigDecimal initialDepositAmount = cart.getDepositAmount();
        BigDecimal discountAmount = BigDecimal.ZERO;
        String validCouponCode = null;

        // 4. Kiểm tra mã ưu đãi (nếu có nhập)
        if (ValidationUtil.isNotEmpty(couponCode)) {
            Coupon coupon = couponDAO.getCouponByCode(couponCode);
            if (coupon != null && coupon.isValid(totalAmount)) {
                discountAmount = coupon.calculateDiscount(totalAmount);
                validCouponCode = coupon.getCouponCode();
            }
        }

        // Tiền cọc thực tế cần thanh toán sau khi trừ chiết khấu
        BigDecimal finalDepositAmount = initialDepositAmount.subtract(discountAmount);
        if (finalDepositAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalDepositAmount = BigDecimal.ZERO;
        }

        // 5. Tạo đối tượng Order
        Order order = new Order();
        order.setUserId(currentUser.getUserId());
        order.setTotalAmount(totalAmount);
        order.setDepositAmount(finalDepositAmount);
        order.setCouponCode(validCouponCode);
        order.setDiscountAmount(discountAmount);
        order.setStatus("PENDING");
        order.setPaymentMethod(ValidationUtil.isNotEmpty(paymentMethod) ? paymentMethod : "BANK_TRANSFER");
        order.setDeliveryAddress(deliveryAddress);
        order.setPhone(phone);
        order.setNote(note);

        // 6. Thực thi giao dịch JDBC Transaction nguyên tử trong CSDL
        String createdOrderCode = orderDAO.createOrderWithTransaction(
                order, new ArrayList<CartItem>(cart.getItems()));

        if (createdOrderCode != null) {
            // Đặt cọc thành công -> Xóa sạch giỏ hàng
            cart.clear();
            session.setAttribute("cart", cart);

            // Chuyển hướng tới trang thông báo thành công
            response.sendRedirect(request.getContextPath() + "/order-success?code=" + createdOrderCode);
        } else {
            request.setAttribute("error", "Không thể tạo hợp đồng đặt cọc do có xe trong giỏ đã hết hàng hoặc lỗi hệ thống. Vui lòng thử lại!");
            request.setAttribute("cart", cart);
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/WEB-INF/views/client/checkout.jsp").forward(request, response);
        }
    }
}
