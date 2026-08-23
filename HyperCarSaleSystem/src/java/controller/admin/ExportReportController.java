package controller.admin;

import dal.OrderDAO;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Order;
import util.FormatUtil;

/**
 * Controller Xuất Báo Cáo Doanh Thu & Hợp Đồng Đặt Cọc ra file CSV (Excel tương thích 100% UTF-8).
 */
@WebServlet(name = "ExportReportController", urlPatterns = {"/admin/export-report", "/admin/export-orders"})
public class ExportReportController extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Order> orders = orderDAO.getAllOrdersAdmin();

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"HyperCar_Orders_Report.csv\"");

        try (OutputStream os = response.getOutputStream()) {
            // Ghi UTF-8 BOM (Byte Order Mark) để Microsoft Excel mở tiếng Việt không bị lỗi font
            os.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

            StringBuilder sb = new StringBuilder();
            sb.append("Mã Hợp Đồng,Họ Tên Khách Hàng,Tài Khoản,Số Điện Thoại,Địa Chỉ Giao Xe,Tổng Giá Trị (USD),Tiền Đặt Cọc (USD),Mã Giảm Giá,Hình Thức Thanh Toán,Trạng Thái,Ngày Ký Cọc\n");

            for (Order o : orders) {
                sb.append(escapeCsv(o.getOrderCode())).append(",");
                sb.append(escapeCsv(o.getUserFullName())).append(",");
                sb.append(escapeCsv(o.getUsername())).append(",");
                sb.append(escapeCsv(o.getPhone())).append(",");
                sb.append(escapeCsv(o.getDeliveryAddress())).append(",");
                sb.append(o.getTotalAmount() != null ? o.getTotalAmount().toString() : "0").append(",");
                sb.append(o.getDepositAmount() != null ? o.getDepositAmount().toString() : "0").append(",");
                sb.append(escapeCsv(o.getCouponCode() != null ? o.getCouponCode() : "")).append(",");
                sb.append(escapeCsv(o.getPaymentMethod())).append(",");
                sb.append(escapeCsv(o.getStatus())).append(",");
                sb.append(escapeCsv(FormatUtil.formatDateTime(o.getOrderDate()))).append("\n");
            }

            os.write(sb.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();
        }
    }

    private String escapeCsv(String data) {
        if (data == null) {
            return "\"\"";
        }
        return "\"" + data.replace("\"", "\"\"").replace("\n", " ").replace("\r", " ") + "\"";
    }
}
