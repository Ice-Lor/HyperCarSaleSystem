package controller.client;

import dal.CouponDAO;
import dal.OrderDAO;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Cart;
import model.Coupon;
import model.Order;
import model.User;
import util.CSRFUtil;
import util.ValidationUtil;

public class CheckoutController extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final CouponDAO couponDAO = new CouponDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Cart cart = (session != null) ? (Cart) session.getAttribute("cart") : null;
        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/client/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        Cart cart = (session != null) ? (Cart) session.getAttribute("cart") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // Kiểm tra CSRF Token
        if (!CSRFUtil.isValidToken(request)) {
            session.setAttribute("errorMessage", "Yêu cầu không hợp lệ hoặc phiên làm việc đã hết hạn (CSRF)!");
            response.sendRedirect(request.getContextPath() + "/checkout");
            return;
        }

        String phone = ValidationUtil.sanitize(request.getParameter("phone"));
        String deliveryAddress = ValidationUtil.sanitize(request.getParameter("deliveryAddress"));
        String paymentMethod = ValidationUtil.sanitize(request.getParameter("paymentMethod"));
        String couponCode = ValidationUtil.sanitize(request.getParameter("couponCode"));
        String note = ValidationUtil.sanitize(request.getParameter("note"));

        // Áp dụng coupon nếu có
        if (couponCode != null && !couponCode.isEmpty()) {
            Coupon c = couponDAO.getCouponByCode(couponCode);
            if (c != null && c.isValid(cart.getSubTotal())) {
                cart.setAppliedCoupon(c);
            }
        }

        // Tạo mã đơn hàng duy nhất: ORD-YYYYMMDD-XXXX
        String datePrefix = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String randomSuffix = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        String orderCode = "ORD-" + datePrefix + "-" + randomSuffix;

        Order order = new Order();
        order.setOrderCode(orderCode);
        order.setUserId(currentUser.getUserId());
        order.setTotalAmount(cart.getFinalTotal());
        order.setDepositAmount(cart.getFinalDeposit());
        order.setCouponCode(cart.getAppliedCoupon() != null ? cart.getAppliedCoupon().getCouponCode() : null);
        order.setDiscountAmount(cart.getDiscountAmount());
        order.setStatus("PENDING");
        order.setPaymentMethod(paymentMethod != null ? paymentMethod : "BANK_TRANSFER");
        order.setDeliveryAddress(deliveryAddress);
        order.setPhone(phone);
        order.setNote(note);

        // Thực hiện giao dịch an toàn bằng JDBC Transaction
        boolean success = orderDAO.createOrderWithTransaction(order, cart);

        if (success) {
            cart.clear();
            session.setAttribute("lastOrder", order);
            response.sendRedirect(request.getContextPath() + "/order-success");
        } else {
            session.setAttribute("errorMessage", "Đặt cọc không thành công! Một số siêu xe trong kho có thể đã hết hàng.");
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }
}
