package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;

public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            if (session == null) session = req.getSession(true);
            session.setAttribute("returnUrl", req.getRequestURI());
            session.setAttribute("errorMessage", "Vui lòng đăng nhập với tài khoản Quản trị viên!");
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!currentUser.isAdmin()) {
            // Không có quyền Admin -> báo lỗi 403 Forbidden
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang quản trị này!");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
