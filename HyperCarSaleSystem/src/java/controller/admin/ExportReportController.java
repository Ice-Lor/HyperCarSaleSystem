package controller.admin;

import dal.OrderDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Order;

public class ExportReportController extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Order> orders = orderDAO.getAllOrders();

        String fileName = "HyperCar_Orders_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv";

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // Ghi UTF-8 BOM để Microsoft Excel tự nhận diện Unicode không bị lỗi font tiếng Việt
        response.getOutputStream().write(0xEF);
        response.getOutputStream().write(0xBB);
        response.getOutputStream().write(0xBF);

        PrintWriter writer = response.getWriter();

        // Tiêu đề cột
        writer.println("Mã Đơn,Khách Hàng,Email,Số Điện Thoại,Tổng Giá Trị (USD),Tiền Cọc (USD),Mã Giảm Giá,Phương Thức TT,Trạng Thái,Ngày Đặt,Địa Chỉ Giao");

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Order o : orders) {
            StringBuilder sb = new StringBuilder();
            sb.append(escapeCsv(o.getOrderCode())).append(",");
            sb.append(escapeCsv(o.getCustomerName())).append(",");
            sb.append(escapeCsv(o.getCustomerEmail())).append(",");
            sb.append(escapeCsv(o.getPhone())).append(",");
            sb.append(o.getTotalAmount()).append(",");
            sb.append(o.getDepositAmount()).append(",");
            sb.append(escapeCsv(o.getCouponCode() != null ? o.getCouponCode() : "")).append(",");
            sb.append(escapeCsv(o.getPaymentMethod())).append(",");
            sb.append(escapeCsv(o.getStatus())).append(",");
            sb.append(escapeCsv(o.getOrderDate() != null ? df.format(o.getOrderDate()) : "")).append(",");
            sb.append(escapeCsv(o.getDeliveryAddress()));
            writer.println(sb.toString());
        }

        writer.flush();
    }

    private String escapeCsv(String value) {
        if (value == null) return "\"\"";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
