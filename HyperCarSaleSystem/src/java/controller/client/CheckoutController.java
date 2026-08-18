package controller.client;

import dal.CouponDAO;
import dal.OrderDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Cart;
import model.CartItem;
import model.Coupon;
import model.Order;
import model.OrderDetail;
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

        if (!CSRFUtil.isValidToken(request)) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cars");
            return;
        }

        String phone = ValidationUtil.sanitize(request.getParameter("phone"));
        String deliveryAddress = ValidationUtil.sanitize(request.getParameter("deliveryAddress"));
        String paymentMethod = request.getParameter("paymentMethod");
        String note = ValidationUtil.sanitize(request.getParameter("note"));
        String couponCode = request.getParameter("couponCode");

        BigDecimal totalAmount = cart.getTotalAmount();
        BigDecimal depositAmount = cart.getTotalDeposit();
        BigDecimal discountAmount = BigDecimal.ZERO;

        if (couponCode != null && !couponCode.trim().isEmpty()) {
            Coupon coupon = couponDAO.getValidCoupon(couponCode.trim().toUpperCase());
            if (coupon != null && totalAmount.compareTo(coupon.getMinOrderAmount()) >= 0) {
                discountAmount = depositAmount.multiply(new BigDecimal(coupon.getDiscountPercent())).divide(new BigDecimal("100.0"));
                if (discountAmount.compareTo(coupon.getMaxDiscount()) > 0) {
                    discountAmount = coupon.getMaxDiscount();
                }
                depositAmount = depositAmount.subtract(discountAmount);
            } else {
                couponCode = null;
            }
        } else {
            couponCode = null;
        }

        String orderCode = "ORD-" + System.currentTimeMillis() % 1000000;
        Order order = new Order();
        order.setOrderCode(orderCode);
        order.setUserId(currentUser.getUserId());
        order.setTotalAmount(totalAmount);
        order.setDepositAmount(depositAmount);
        order.setCouponCode(couponCode);
        order.setDiscountAmount(discountAmount);
        order.setStatus("PENDING");
        order.setPaymentMethod(paymentMethod != null ? paymentMethod : "BANK_TRANSFER");
        order.setDeliveryAddress(deliveryAddress);
        order.setPhone(phone);
        order.setNote(note);

        List<OrderDetail> details = new ArrayList<OrderDetail>();
        for (CartItem item : cart.getItems()) {
            OrderDetail d = new OrderDetail();
            d.setCarId(item.getCar().getCarId());
            d.setQuantity(item.getQuantity());
            d.setUnitPrice(item.getCar().getPrice());
            d.setSelectedColor(item.getSelectedColor());
            d.setCustomOptions(item.getCustomOptions());
            details.add(d);
        }
        order.setDetails(details);

        // Gọi Transaction an toàn
        boolean success = orderDAO.createOrderWithTransaction(order);
        if (success) {
            cart.clear();
            session.setAttribute("lastOrder", order);
            response.sendRedirect(request.getContextPath() + "/order-success?code=" + orderCode);
        } else {
            session.setAttribute("errorMessage", "Đặt cọc xe thất bại do số lượng xe trong kho không đủ hoặc lỗi hệ thống!");
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}
