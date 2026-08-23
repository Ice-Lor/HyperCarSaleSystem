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
import model.User;

/**
 * Bộ lọc xác thực đăng nhập (Authentication Filter).
 * Yêu cầu người dùng phải đăng nhập trước khi truy cập các tính năng cá nhân và đặt cọc xe.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {
    "/checkout",
    "/order-history",
    "/order-detail",
    "/profile",
    "/test-drive",
    "/submit-review"
})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        // Nếu chưa đăng nhập hoặc tài khoản bị khóa
        if (currentUser == null || !currentUser.isActive()) {
            // Lưu lại đường dẫn người dùng đang muốn vào để chuyển hướng lại sau khi login thành công
            String targetUrl = httpRequest.getRequestURI();
            String queryString = httpRequest.getQueryString();
            if (queryString != null && !queryString.trim().isEmpty()) {
                targetUrl += "?" + queryString;
            }
            
            if (session == null) {
                session = httpRequest.getSession(true);
            }
            session.setAttribute("redirectUrl", targetUrl);

            // Chuyển hướng về trang Đăng Nhập kèm thông báo
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?error=auth_required");
            return;
        }

        // Đã đăng nhập hợp lệ -> Cho phép tiếp tục
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
