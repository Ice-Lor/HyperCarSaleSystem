package util;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Tiện ích cấp phát và xác thực CSRF Token chống giả mạo request POST
 */
public class CSRFUtil {

    private static final String CSRF_SESSION_ATTR = "csrfToken";
    private static final String CSRF_PARAM_NAME = "csrf_token";

    public static String generateToken(HttpSession session) {
        String token = UUID.randomUUID().toString();
        session.setAttribute(CSRF_SESSION_ATTR, token);
        return token;
    }

    public static String getToken(HttpSession session) {
        String token = (String) session.getAttribute(CSRF_SESSION_ATTR);
        if (token == null) {
            token = generateToken(session);
        }
        return token;
    }

    public static boolean isValidToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        String sessionToken = (String) session.getAttribute(CSRF_SESSION_ATTR);
        String requestToken = request.getParameter(CSRF_PARAM_NAME);
        if (sessionToken == null || requestToken == null) {
            return false;
        }
        return sessionToken.equals(requestToken);
    }
}
