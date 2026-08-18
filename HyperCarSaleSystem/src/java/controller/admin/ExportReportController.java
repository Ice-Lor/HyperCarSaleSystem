package controller.admin;

import dal.OrderDAO;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
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

        List<Order> orders = orderDAO.getAllOrdersAdmin();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"HyperCar_Orders_Report.csv\"");

        // UTF-8 BOM cho Excel mở trực tiếp không lỗi font tiếng Việt
        OutputStream os = response.getOutputStream();
        os.write(0xEF);
        os.write(0xBB);
        os.write(0xBF);

        PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        writer.println("Mã Đơn,Khách Hàng,Email,Số Điện Thoại,Tổng Tiền ($),Tiền Đặt Cọc ($),Mã Giảm Giá,Phương Thức,Trạng Thái,Ngày Đặt");

        for (Order o : orders) {
            writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                    o.getOrderCode(),
                    o.getUserName(),
                    o.getUserEmail(),
                    o.getPhone() != null ? o.getPhone() : "",
                    o.getTotalAmount(),
                    o.getDepositAmount(),
                    o.getCouponCode() != null ? o.getCouponCode() : "",
                    o.getPaymentMethod(),
                    o.getStatus(),
                    sdf.format(o.getOrderDate())
            );
        }
        writer.flush();
    }
}
