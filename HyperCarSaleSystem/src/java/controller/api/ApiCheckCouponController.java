package controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dal.CouponDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Coupon;

public class ApiCheckCouponController extends HttpServlet {

    private final CouponDAO couponDAO = new CouponDAO();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");
        String amountStr = request.getParameter("amount");

        response.setContentType("application/json; charset=UTF-8");
        Map<String, Object> res = new HashMap<String, Object>();

        if (code == null || code.trim().isEmpty() || amountStr == null) {
            res.put("valid", false);
            res.put("message", "Mã không hợp lệ!");
            mapper.writeValue(response.getWriter(), res);
            return;
        }

        Coupon coupon = couponDAO.getValidCoupon(code.trim().toUpperCase());
        if (coupon == null) {
            res.put("valid", false);
            res.put("message", "Mã giảm giá không tồn tại hoặc đã hết hạn!");
        } else {
            BigDecimal orderAmount = new BigDecimal(amountStr);
            if (orderAmount.compareTo(coupon.getMinOrderAmount()) < 0) {
                res.put("valid", false);
                res.put("message", "Đơn hàng tối thiểu để áp dụng mã là $" + coupon.getMinOrderAmount());
            } else {
                res.put("valid", true);
                res.put("code", coupon.getCouponCode());
                res.put("discountPercent", coupon.getDiscountPercent());
                res.put("maxDiscount", coupon.getMaxDiscount());
                res.put("message", "Áp dụng mã thành công! Giảm " + coupon.getDiscountPercent() + "%");
            }
        }
        mapper.writeValue(response.getWriter(), res);
    }
}
