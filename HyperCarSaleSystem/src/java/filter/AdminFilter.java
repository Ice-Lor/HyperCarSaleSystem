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
 * Bộ lọc phân quyền Quản Trị Viên (Admin Authorization Filter).
 * Ngăn chặn tuyệt đối khách hàng thông thường và kẻ xấu xâm nhập các trang quản trị /admin/*.
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

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

        // 1. Nếu chưa đăng nhập -> Chuyển về trang Login
        if (currentUser == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?error=admin_required");
            return;
        }

        // 2. Nếu đã đăng nhập nhưng KHÔNG PHẢI ADMIN (role_id != 1) -> Chặn đứng ngay
        if (!currentUser.isAdmin()) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, 
                    "Từ chối quyền truy cập: Bạn không có đặc quyền Quản Trị Viên (Admin) để vào khu vực này!");
            return;
        }

        // 3. Đúng quyền Admin -> Cho phép truy cập
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
