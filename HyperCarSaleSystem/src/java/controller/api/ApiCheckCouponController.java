package controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dal.CouponDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Coupon;
import util.FormatUtil;
import util.ValidationUtil;

/**
 * RESTful API kiểm tra và tính toán số tiền giảm giá của Voucher / Coupon (AJAX Realtime).
 */
@WebServlet(name = "ApiCheckCouponController", urlPatterns = {"/api/coupon/check"})
public class ApiCheckCouponController extends HttpServlet {

    private final CouponDAO couponDAO = new CouponDAO();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        String couponCode = ValidationUtil.sanitize(request.getParameter("couponCode"));
        BigDecimal totalAmount = ValidationUtil.parseBigDecimal(request.getParameter("totalAmount"), BigDecimal.ZERO);

        Map<String, Object> result = new HashMap<String, Object>();

        if (!ValidationUtil.isNotEmpty(couponCode)) {
            result.put("valid", false);
            result.put("message", "Vui lòng nhập mã ưu đãi!");
            mapper.writeValue(response.getWriter(), result);
            return;
        }

        Coupon coupon = couponDAO.getCouponByCode(couponCode);

        if (coupon == null) {
            result.put("valid", false);
            result.put("message", "Mã ưu đãi '" + couponCode + "' không tồn tại hoặc đã hết hạn!");
        } else if (!coupon.isValid(totalAmount)) {
            result.put("valid", false);
            result.put("message", "Đơn hàng chưa đạt giá trị tối thiểu " 
                    + FormatUtil.formatCurrency(coupon.getMinOrderAmount()) + " để áp dụng mã này!");
        } else {
            BigDecimal discountAmount = coupon.calculateDiscount(totalAmount);
            result.put("valid", true);
            result.put("code", coupon.getCouponCode());
            result.put("discountPercent", coupon.getDiscountPercent());
            result.put("discountAmount", discountAmount);
            result.put("formattedDiscount", FormatUtil.formatCurrency(discountAmount));
            result.put("message", "Áp dụng mã ưu đãi thành công! Bạn được chiết khấu " 
                    + FormatUtil.formatCurrency(discountAmount));
        }

        mapper.writeValue(response.getWriter(), result);
    }
}
