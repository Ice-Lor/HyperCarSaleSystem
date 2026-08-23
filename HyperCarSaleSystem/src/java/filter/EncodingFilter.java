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

/**
 * Bộ lọc mã hóa ký tự toàn hệ thống.
 * Ép toàn bộ Request và Response sang chuẩn UTF-8, chống triệt để lỗi font tiếng Việt có dấu.
 */
@WebFilter(filterName = "EncodingFilter", urlPatterns = {"/*"})
public class EncodingFilter implements Filter {

    private static final String ENCODING = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Thiết lập mã hóa UTF-8 cho luồng đọc dữ liệu đầu vào
        httpRequest.setCharacterEncoding(ENCODING);

        // Thiết lập mã hóa UTF-8 cho luồng xuất dữ liệu đầu ra
        httpResponse.setCharacterEncoding(ENCODING);

        // Tiếp tục chuyển tiếp request tới Filter hoặc Servlet tiếp theo
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
