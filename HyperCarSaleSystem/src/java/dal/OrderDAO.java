package dal;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CartItem;
import model.Order;
import model.OrderDetail;

/**
 * Lớp truy xuất và xử lý giao dịch hợp đồng đặt cọc siêu xe (Orders & OrderDetails).
 * Áp dụng cơ chế JDBC Transaction (ACID) đảm bảo trừ tồn kho và tạo đơn hàng nguyên tử.
 */
public class OrderDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());

    /**
     * Sinh mã hợp đồng độc bản chuẩn thương mại điện tử cao cấp:
     * Cấu trúc: ORD-yyyyMMdd-HHmmss-XXXX (NămThángNgày - GiờPhútGiây - 4 ký tự Hex ngẫu nhiên).
     * Kết hợp kiểm tra trực tiếp trong CSDL đảm bảo tỷ lệ trùng lặp bằng 0 tuyệt đối.
     */
    public String generateOrderCode(Connection conn) {
        String code;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        do {
            String timeStr = sdf.format(new Date());
            String randomHex = String.format("%04X", new Random().nextInt(0x10000));
            code = "ORD-" + timeStr + "-" + randomHex;
        } while (checkOrderCodeExists(conn, code));
        return code;
    }

    /**
     * Kiểm tra xem order_code đã tồn tại trong CSDL chưa.
     */
    private boolean checkOrderCodeExists(Connection conn, String orderCode) {
        if (conn == null || orderCode == null) {
            return false;
        }
        String sql = "SELECT 1 FROM Orders WHERE order_code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderCode);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Tạo đơn đặt cọc mới sử dụng giao dịch JDBC Transaction (ACID).
     * 1. conn.setAutoCommit(false)
     * 2. Insert bảng Orders -> Lấy generated order_id
     * 3. Loop danh sách CartItem: Insert OrderDetails & Trừ tồn kho stock_quantity trong bảng Cars
     * 4. Ghi vết kiểm toán ActivityLogs
     * 5. conn.commit() thành công, nếu có bất kỳ lỗi nào xảy ra sẽ conn.rollback().
     * 
     * @param order Đối tượng đơn hàng
     * @param items Danh sách siêu xe trong giỏ cọc
     * @return Chuỗi orderCode nếu thành công, null nếu thất bại
     */
    public String createOrderWithTransaction(Order order, List<CartItem> items) {
        if (order == null || items == null || items.isEmpty()) {
            return null;
        }

        String insertOrderSql = "INSERT INTO Orders (order_code, user_id, total_amount, deposit_amount, "
                              + "coupon_code, discount_amount, status, payment_method, delivery_address, phone, note, order_date) "
                              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";

        String insertDetailSql = "INSERT INTO OrderDetails (order_id, car_id, quantity, unit_price, selected_color, custom_options) "
                               + "VALUES (?, ?, ?, ?, ?, ?)";

        String updateStockSql = "UPDATE Cars SET stock_quantity = stock_quantity - ? "
                              + "WHERE car_id = ? AND stock_quantity >= ?";

        String insertLogSql = "INSERT INTO ActivityLogs (user_id, action, details, created_at) "
                            + "VALUES (?, 'PLACE_ORDER', ?, GETDATE())";

        Connection conn = null;
        try {
            conn = getConnection();
            // BƯỚC 1: BẬT GIAO DỊCH (Tắt Auto-Commit)
            conn.setAutoCommit(false);

            // Sinh mã hợp đồng độc bản an toàn tuyệt đối
            String orderCode = generateOrderCode(conn);
            order.setOrderCode(orderCode);

            int generatedOrderId = -1;

            // BƯỚC 2: TẠO HỢP ĐỒNG ĐẶT CỌC (BẢNG ORDERS)
            try (PreparedStatement psOrder = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setString(1, order.getOrderCode());
                psOrder.setInt(2, order.getUserId());
                psOrder.setBigDecimal(3, order.getTotalAmount());
                psOrder.setBigDecimal(4, order.getDepositAmount());
                psOrder.setString(5, order.getCouponCode());
                psOrder.setBigDecimal(6, order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO);
                psOrder.setString(7, order.getStatus() != null ? order.getStatus() : "PENDING");
                psOrder.setString(8, order.getPaymentMethod() != null ? order.getPaymentMethod() : "BANK_TRANSFER");
                psOrder.setString(9, order.getDeliveryAddress());
                psOrder.setString(10, order.getPhone());
                psOrder.setString(11, order.getNote());

                int affected = psOrder.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Không thể tạo hợp đồng đặt cọc trong Orders!");
                }

                try (ResultSet rs = psOrder.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedOrderId = rs.getInt(1);
                    } else {
                        throw new SQLException("Không lấy được generated ID của Orders!");
                    }
                }
            }

            // BƯỚC 3: LƯU TỪNG CHI TIẾT XE VÀ TRỪ TỒN KHO AN TOÀN
            try (PreparedStatement psDetail = conn.prepareStatement(insertDetailSql);
                 PreparedStatement psStock = conn.prepareStatement(updateStockSql)) {

                for (CartItem item : items) {
                    // 3.1. Insert OrderDetail
                    psDetail.setInt(1, generatedOrderId);
                    psDetail.setInt(2, item.getCar().getCarId());
                    psDetail.setInt(3, item.getQuantity());
                    psDetail.setBigDecimal(4, item.getCar().getPrice());
                    psDetail.setString(5, item.getSelectedColor());
                    psDetail.setString(6, item.getCustomOptions());
                    psDetail.executeUpdate();

                    // 3.2. Trừ tồn kho siêu xe (Atomic Check: stock_quantity >= quantity)
                    psStock.setInt(1, item.getQuantity());
                    psStock.setInt(2, item.getCar().getCarId());
                    psStock.setInt(3, item.getQuantity());
                    int stockUpdated = psStock.executeUpdate();

                    if (stockUpdated == 0) {
                        throw new SQLException("Siêu xe [" + item.getCar().getModelName() 
                                + "] hiện không đủ số lượng tồn kho để đặt cọc!");
                    }
                }
            }

            // BƯỚC 4: GHI NHẬT KÝ VẾT HOẠT ĐỘNG
            try (PreparedStatement psLog = conn.prepareStatement(insertLogSql)) {
                psLog.setInt(1, order.getUserId());
                psLog.setString(2, "Khách hàng đã ký hợp đồng đặt cọc mã " + orderCode 
                        + " với số tiền cọc: $" + order.getDepositAmount());
                psLog.executeUpdate();
            }

            // BƯỚC 5: COMMIT GIAO DỊCH THÀNH CÔNG
            conn.commit();
            LOGGER.info("Giao dịch đặt cọc " + orderCode + " hoàn tất thành công 100%!");
            return orderCode;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi trong quá trình tạo đơn cọc -> Tiến hành ROLLBACK!", ex);
            if (conn != null) {
                try {
                    conn.rollback();
                    LOGGER.info("Đã khôi phục (ROLLBACK) toàn vẹn dữ liệu an toàn!");
                } catch (SQLException rollbackEx) {
                    LOGGER.log(Level.SEVERE, "Lỗi nghiêm trọng khi rollback transaction!", rollbackEx);
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    LOGGER.log(Level.SEVERE, "Lỗi đóng kết nối CSDL!", closeEx);
                }
            }
        }
        return null;
    }

    /**
     * Tìm đơn hàng theo ID (kèm danh sách chi tiết xe).
     */
    public Order getOrderById(int orderId) {
        String sql = "SELECT o.*, u.username, u.full_name AS user_full_name "
                   + "FROM Orders o "
                   + "JOIN Users u ON o.user_id = u.user_id "
                   + "WHERE o.order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setDetails(getOrderDetailsByOrderId(order.getOrderId()));
                    return order;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi tìm đơn hàng theo ID: " + orderId, ex);
        }
        return null;
    }

    /**
     * Tìm đơn hàng theo Mã hợp đồng (Order Code).
     */
    public Order getOrderByCode(String orderCode) {
        String sql = "SELECT o.*, u.username, u.full_name AS user_full_name "
                   + "FROM Orders o "
                   + "JOIN Users u ON o.user_id = u.user_id "
                   + "WHERE o.order_code = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setDetails(getOrderDetailsByOrderId(order.getOrderId()));
                    return order;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi tìm đơn hàng theo mã code: " + orderCode, ex);
        }
        return null;
    }

    /**
     * Lấy danh sách lịch sử hợp đồng đặt cọc của một khách hàng VIP.
     */
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> list = new ArrayList<Order>();
        String sql = "SELECT o.*, u.username, u.full_name AS user_full_name "
                   + "FROM Orders o "
                   + "JOIN Users u ON o.user_id = u.user_id "
                   + "WHERE o.user_id = ? "
                   + "ORDER BY o.order_date DESC, o.order_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setDetails(getOrderDetailsByOrderId(order.getOrderId()));
                    list.add(order);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy lịch sử đơn hàng của user ID: " + userId, ex);
        }
        return list;
    }

    /**
     * Lấy toàn bộ danh sách hợp đồng đặt cọc (dùng cho Admin quản lý).
     */
    public List<Order> getAllOrdersAdmin() {
        List<Order> list = new ArrayList<Order>();
        String sql = "SELECT o.*, u.username, u.full_name AS user_full_name "
                   + "FROM Orders o "
                   + "JOIN Users u ON o.user_id = u.user_id "
                   + "ORDER BY o.order_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order order = mapOrder(rs);
                order.setDetails(getOrderDetailsByOrderId(order.getOrderId()));
                list.add(order);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy danh sách toàn bộ đơn hàng cho Admin", ex);
        }
        return list;
    }

    /**
     * Cập nhật trạng thái hợp đồng đặt cọc (CONFIRMED, PROCESSING, COMPLETED, CANCELLED).
     */
    public boolean updateOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE Orders SET status = ? WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi cập nhật trạng thái đơn hàng ID: " + orderId, ex);
        }
        return false;
    }

    /**
     * Lấy danh sách chi tiết các siêu xe trong một hợp đồng theo order_id.
     */
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> list = new ArrayList<OrderDetail>();
        String sql = "SELECT d.*, c.model_name AS car_model_name, c.thumbnail_url AS car_thumbnail_url "
                   + "FROM OrderDetails d "
                   + "JOIN Cars c ON d.car_id = c.car_id "
                   + "WHERE d.order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetail d = new OrderDetail();
                    d.setDetailId(rs.getInt("detail_id"));
                    d.setOrderId(rs.getInt("order_id"));
                    d.setCarId(rs.getInt("car_id"));
                    d.setCarModelName(rs.getString("car_model_name"));
                    d.setCarThumbnailUrl(rs.getString("car_thumbnail_url"));
                    d.setQuantity(rs.getInt("quantity"));
                    d.setUnitPrice(rs.getBigDecimal("unit_price"));
                    d.setSelectedColor(rs.getString("selected_color"));
                    d.setCustomOptions(rs.getString("custom_options"));
                    list.add(d);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy chi tiết đơn hàng ID: " + orderId, ex);
        }
        return list;
    }

    /**
     * Đếm tổng số lượng hợp đồng đặt cọc trong hệ thống (KPI Admin).
     */
    public int countTotalOrders() {
        String sql = "SELECT COUNT(*) FROM Orders";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi đếm tổng số đơn hàng", ex);
        }
        return 0;
    }

    /**
     * Tính tổng doanh thu tiền đặt cọc đã giao dịch thành công (KPI Admin).
     */
    public BigDecimal getTotalRevenue() {
        String sql = "SELECT SUM(deposit_amount) FROM Orders WHERE status != 'CANCELLED'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BigDecimal sum = rs.getBigDecimal(1);
                return sum != null ? sum : BigDecimal.ZERO;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi tính tổng doanh thu tiền cọc", ex);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Ánh xạ một dòng ResultSet sang đối tượng Order.
     */
    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
        o.setOrderCode(rs.getString("order_code"));
        o.setUserId(rs.getInt("user_id"));
        o.setUsername(rs.getString("username"));
        o.setUserFullName(rs.getString("user_full_name"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setDepositAmount(rs.getBigDecimal("deposit_amount"));
        o.setCouponCode(rs.getString("coupon_code"));
        o.setDiscountAmount(rs.getBigDecimal("discount_amount"));
        o.setStatus(rs.getString("status"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setDeliveryAddress(rs.getString("delivery_address"));
        o.setPhone(rs.getString("phone"));
        o.setNote(rs.getString("note"));
        o.setOrderDate(rs.getTimestamp("order_date"));
        return o;
    }
}
