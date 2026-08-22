package util;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Tiện ích sinh và xác thực CSRF Token (Cross-Site Request Forgery).
 * Ngăn chặn các cuộc tấn công giả mạo yêu cầu từ các trang web độc hại của hacker.
 */
public class CSRFUtil {

    public static final String CSRF_SESSION_ATTR = "csrfToken";
    public static final String CSRF_PARAM_NAME = "csrf_token";
    public static final String CSRF_HEADER_NAME = "X-CSRF-TOKEN";

    /**
     * Sinh CSRF Token ngẫu nhiên (UUID) và lưu trữ trong HttpSession.
     * 
     * @param session Phiên làm việc của người dùng
     * @return Chuỗi token ngẫu nhiên dạng Hex
     */
    public static String generateToken(HttpSession session) {
        if (session == null) {
            return null;
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        session.setAttribute(CSRF_SESSION_ATTR, token);
        return token;
    }

    /**
     * Lấy CSRF Token hiện tại từ Session, nếu chưa có thì tự động sinh mới.
     * 
     * @param session Phiên làm việc của người dùng
     * @return Chuỗi CSRF Token hợp lệ
     */
    public static String getToken(HttpSession session) {
        if (session == null) {
            return null;
        }
        String token = (String) session.getAttribute(CSRF_SESSION_ATTR);
        if (token == null || token.trim().isEmpty()) {
            token = generateToken(session);
        }
        return token;
    }

    /**
     * Xác thực token được gửi lên từ request (qua Form parameter hoặc AJAX HTTP Header)
     * với token lưu trữ trong Session.
     * 
     * @param request HTTP Request gửi lên từ Client
     * @return true nếu hợp lệ, false nếu token không khớp hoặc bị thiếu
     */
    public static boolean validateToken(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        String sessionToken = (String) session.getAttribute(CSRF_SESSION_ATTR);
        if (sessionToken == null || sessionToken.trim().isEmpty()) {
            return false;
        }

        // 1. Lấy token từ form parameter (gửi form POST truyền thống)
        String requestToken = request.getParameter(CSRF_PARAM_NAME);
        
        // 2. Nếu form không có thì kiểm tra HTTP Header (gửi bằng AJAX / Fetch API)
        if (requestToken == null || requestToken.trim().isEmpty()) {
            requestToken = request.getHeader(CSRF_HEADER_NAME);
        }

        if (requestToken == null || requestToken.trim().isEmpty()) {
            return false;
        }

        return sessionToken.equals(requestToken);
    }
}
