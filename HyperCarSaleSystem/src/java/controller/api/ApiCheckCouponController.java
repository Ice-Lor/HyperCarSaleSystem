package controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dal.CouponDAO;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Cart;
import model.Coupon;

public class ApiCheckCouponController extends HttpServlet {

    private final CouponDAO couponDAO = new CouponDAO();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json; charset=UTF-8");
        String code = request.getParameter("couponCode");

        HttpSession session = request.getSession(false);
        Cart cart = (session != null) ? (Cart) session.getAttribute("cart") : null;

        Map<String, Object> res = new HashMap<String, Object>();

        if (cart == null || cart.getItems().isEmpty()) {
            res.put("success", false);
            res.put("message", "Giỏ hàng đang trống!");
            mapper.writeValue(response.getWriter(), res);
            return;
        }

        Coupon coupon = couponDAO.getCouponByCode(code);
        if (coupon != null && coupon.isValid(cart.getSubTotal())) {
            cart.setAppliedCoupon(coupon);
            res.put("success", true);
            res.put("message", "Áp dụng mã giảm giá thành công!");
            res.put("discountAmount", cart.getDiscountAmount());
            res.put("finalTotal", cart.getFinalTotal());
            res.put("finalDeposit", cart.getFinalDeposit());
            res.put("couponCode", coupon.getCouponCode());
        } else {
            res.put("success", false);
            res.put("message", "Mã giảm giá không hợp lệ hoặc không đủ điều kiện áp dụng!");
        }

        mapper.writeValue(response.getWriter(), res);
    }
}
