package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.CSRFUtil;

/**
 * Bộ lọc bảo mật chống tấn công giả mạo yêu cầu (CSRF Protection Filter).
 * Tự động cấp phát token cho mọi Session và xác thực token trên các request POST nhạy cảm.
 */
@WebFilter(filterName = "CSRFFilter", urlPatterns = {"/*"})
public class CSRFFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String uri = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        // Bỏ qua kiểm tra đối với các tài nguyên tĩnh (Ảnh, CSS, JS, Fonts)
        if (isStaticResource(uri)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(true);

        // Đảm bảo luôn có CSRF Token trong Session và đẩy ra requestScope để JSP dễ dàng sử dụng: ${csrfToken}
        String csrfToken = CSRFUtil.getToken(session);
        httpRequest.setAttribute("csrfToken", csrfToken);

        // Với phương thức POST thực hiện thay đổi dữ liệu nhạy cảm
        if ("POST".equalsIgnoreCase(method)) {
            // Cho phép đăng nhập và đăng ký qua cổng tự do (nếu chưa có session trước đó)
            boolean isAuthEndpoint = uri.endsWith("/login") || uri.endsWith("/register");

            if (!isAuthEndpoint) {
                // Xác thực token gửi lên từ Form hoặc AJAX Header
                boolean isValid = CSRFUtil.validateToken(httpRequest);
                if (!isValid) {
                    // Nếu là gọi AJAX API
                    if (uri.contains("/api/")) {
                        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        httpResponse.setContentType("application/json;charset=UTF-8");
                        httpResponse.getWriter().write("{\"status\":\"error\",\"message\":\"CSRF Token không hợp lệ hoặc đã hết hạn!\"}");
                        return;
                    }
                    // Nếu là gửi Form POST thông thường -> Báo lỗi 403 Forbidden
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, 
                            "Yêu cầu bị từ chối: CSRF Token không hợp lệ hoặc phiên làm việc đã hết hạn. Vui lòng tải lại trang.");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Kiểm tra xem URI có phải là đường dẫn file tĩnh không.
     */
    private boolean isStaticResource(String uri) {
        String lower = uri.toLowerCase();
        return lower.contains("/assets/")
                || lower.endsWith(".css")
                || lower.endsWith(".js")
                || lower.endsWith(".jpg")
                || lower.endsWith(".jpeg")
                || lower.endsWith(".png")
                || lower.endsWith(".gif")
                || lower.endsWith(".svg")
                || lower.endsWith(".ico")
                || lower.endsWith(".woff")
                || lower.endsWith(".woff2")
                || lower.endsWith(".ttf");
    }

    @Override
    public void destroy() {
    }
}
