package util;

import java.security.SecureRandom;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Tiện ích tạo và kiểm tra CSRF Token trong Session
 */
public class CSRFUtil {
    public static final String CSRF_TOKEN_SESSION_ATTR = "CSRF_TOKEN";
    public static final String CSRF_TOKEN_PARAM = "csrf_token";
    
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Lấy token hiện tại trong session hoặc sinh mới nếu chưa có
     */
    public static String getToken(HttpSession session) {
        if (session == null) return null;
        String token = (String) session.getAttribute(CSRF_TOKEN_SESSION_ATTR);
        if (token == null || token.trim().isEmpty()) {
            token = generateNewToken();
            session.setAttribute(CSRF_TOKEN_SESSION_ATTR, token);
        }
        return token;
    }

    /**
     * Sinh token ngẫu nhiên an toàn
     */
    public static String generateNewToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * Xác minh tính hợp lệ của CSRF Token gửi lên từ form hoặc header
     */
    public static boolean isValidToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        
        String sessionToken = (String) session.getAttribute(CSRF_TOKEN_SESSION_ATTR);
        if (sessionToken == null) return false;
        
        String requestToken = request.getParameter(CSRF_TOKEN_PARAM);
        if (requestToken == null || requestToken.isEmpty()) {
            requestToken = request.getHeader("X-CSRF-TOKEN");
        }
        
        return sessionToken.equals(requestToken);
    }
}
