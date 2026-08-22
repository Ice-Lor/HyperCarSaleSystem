package util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Tiện ích kiểm tra (Validation) và làm sạch dữ liệu (Sanitization) đầu vào.
 * Ngăn chặn lỗ hổng XSS (Cross-Site Scripting) và lỗi NumberFormatException.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^(0|\\+84)[0-9]{9,10}$");

    /**
     * Làm sạch chuỗi ký tự đầu vào để chống tấn công XSS (Cross-Site Scripting).
     * Chuyển đổi các ký tự HTML nhạy cảm thành HTML entities an toàn.
     * 
     * @param input Chuỗi văn bản thô từ người dùng
     * @return Chuỗi đã được mã hóa an toàn
     */
    public static String sanitize(String input) {
        if (input == null) {
            return "";
        }
        return input.trim()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;")
                .replace("/", "&#x2F;");
    }

    /**
     * Kiểm tra chuỗi có rỗng hoặc null không.
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Kiểm tra định dạng Email hợp lệ.
     */
    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Kiểm tra định dạng Số điện thoại hợp lệ (Việt Nam).
     */
    public static boolean isValidPhone(String phone) {
        if (!isNotEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Ép kiểu chuỗi sang int an toàn, nếu lỗi trả về giá trị mặc định.
     */
    public static int parseInt(String str, int defaultVal) {
        if (!isNotEmpty(str)) {
            return defaultVal;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    /**
     * Ép kiểu chuỗi sang double an toàn, nếu lỗi trả về giá trị mặc định.
     */
    public static double parseDouble(String str, double defaultVal) {
        if (!isNotEmpty(str)) {
            return defaultVal;
        }
        try {
            return Double.parseDouble(str.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    /**
     * Ép kiểu chuỗi sang BigDecimal an toàn (dùng cho giá tiền siêu xe).
     */
    public static BigDecimal parseBigDecimal(String str, BigDecimal defaultVal) {
        if (!isNotEmpty(str)) {
            return defaultVal;
        }
        try {
            return new BigDecimal(str.trim());
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
