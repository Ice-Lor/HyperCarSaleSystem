package dal;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Order;
import model.OrderDetail;

public class OrderDAO extends DBContext {

    /**
     * Kỹ thuật JDBC Transaction Management: Tự động commit/rollback đảm bảo tính toàn vẹn 100%
     */
    public boolean createOrderWithTransaction(Order order) {
        String insertOrderSql = "INSERT INTO Orders (order_code, user_id, total_amount, deposit_amount, "
                              + "coupon_code, discount_amount, status, payment_method, delivery_address, "
                              + "phone, note, order_date) "
                              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";

        String insertDetailSql = "INSERT INTO OrderDetails (order_id, car_id, quantity, unit_price, "
                               + "selected_color, custom_options) "
                               + "VALUES (?, ?, ?, ?, ?, ?)";

        String updateStockSql = "UPDATE Cars SET stock_quantity = stock_quantity - ? "
                              + "WHERE car_id = ? AND stock_quantity >= ?";

        String insertLogSql = "INSERT INTO ActivityLogs (user_id, action, details) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = getConnection();
            if (conn == null) return false;

            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Insert Orders
            int generatedOrderId = -1;
            try (PreparedStatement psOrder = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setString(1, order.getOrderCode());
                psOrder.setInt(2, order.getUserId());
                psOrder.setBigDecimal(3, order.getTotalAmount());
                psOrder.setBigDecimal(4, order.getDepositAmount());
                psOrder.setString(5, order.getCouponCode());
                psOrder.setBigDecimal(6, order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO);
                psOrder.setString(7, order.getStatus() != null ? order.getStatus() : "PENDING");
                psOrder.setString(8, order.getPaymentMethod());
                psOrder.setString(9, order.getDeliveryAddress());
                psOrder.setString(10, order.getPhone());
                psOrder.setString(11, order.getNote());

                int affected = psOrder.executeUpdate();
                if (affected == 0) {
                    conn.rollback();
                    return false;
                }
                try (ResultSet rs = psOrder.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedOrderId = rs.getInt(1);
                        order.setOrderId(generatedOrderId);
                    }
                }
            }

            if (generatedOrderId == -1) {
                conn.rollback();
                return false;
            }

            // 2. Insert OrderDetails & Cập nhật trừ tồn kho Cars
            try (PreparedStatement psDetail = conn.prepareStatement(insertDetailSql);
                 PreparedStatement psStock = conn.prepareStatement(updateStockSql)) {

                for (OrderDetail detail : order.getDetails()) {
                    // Detail
                    psDetail.setInt(1, generatedOrderId);
                    psDetail.setInt(2, detail.getCarId());
                    psDetail.setInt(3, detail.getQuantity());
                    psDetail.setBigDecimal(4, detail.getUnitPrice());
                    psDetail.setString(5, detail.getSelectedColor());
                    psDetail.setString(6, detail.getCustomOptions());
                    psDetail.addBatch();

                    // Stock
                    psStock.setInt(1, detail.getQuantity());
                    psStock.setInt(2, detail.getCarId());
                    psStock.setInt(3, detail.getQuantity());
                    int stockUpdated = psStock.executeUpdate();
                    if (stockUpdated == 0) {
                        // Xe không đủ số lượng trong kho -> Rollback toàn bộ
                        conn.rollback();
                        return false;
                    }
                }
                psDetail.executeBatch();
            }

            // 3. Log Activity
            try (PreparedStatement psLog = conn.prepareStatement(insertLogSql)) {
                psLog.setInt(1, order.getUserId());
                psLog.setString(2, "PLACE_ORDER");
                psLog.setString(3, "Khách hàng đặt cọc đơn hàng: " + order.getOrderCode() + " với số tiền: " + order.getDepositAmount());
                psLog.executeUpdate();
            }

            conn.commit(); // Thành công tuyệt đối -> Commit
            return true;

        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, "Lỗi Transaction khi tạo đơn hàng!", ex);
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback an toàn
                } catch (SQLException rEx) {
                    Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, rEx);
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException cEx) {
                    Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, cEx);
                }
            }
        }
        return false;
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> list = new ArrayList<Order>();
        String sql = "SELECT o.*, u.username as user_name, u.email as user_email "
                   + "FROM Orders o "
                   + "JOIN Users u ON o.user_id = u.user_id "
                   + "WHERE o.user_id = ? "
                   + "ORDER BY o.order_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapOrder(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public Order getOrderById(int orderId) {
        String sql = "SELECT o.*, u.username as user_name, u.email as user_email "
                   + "FROM Orders o "
                   + "JOIN Users u ON o.user_id = u.user_id "
                   + "WHERE o.order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order o = mapOrder(rs);
                    o.setDetails(getOrderDetails(orderId, conn));
                    return o;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<OrderDetail> getOrderDetails(int orderId, Connection conn) throws SQLException {
        List<OrderDetail> list = new ArrayList<OrderDetail>();
        String sql = "SELECT d.*, c.model_name as car_model_name, c.thumbnail_url as car_thumbnail_url "
                   + "FROM OrderDetails d "
                   + "JOIN Cars c ON d.car_id = c.car_id "
                   + "WHERE d.order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetail od = new OrderDetail();
                    od.setDetailId(rs.getInt("detail_id"));
                    od.setOrderId(rs.getInt("order_id"));
                    od.setCarId(rs.getInt("car_id"));
                    od.setCarModelName(rs.getString("car_model_name"));
                    od.setCarThumbnailUrl(rs.getString("car_thumbnail_url"));
                    od.setQuantity(rs.getInt("quantity"));
                    od.setUnitPrice(rs.getBigDecimal("unit_price"));
                    od.setSelectedColor(rs.getString("selected_color"));
                    od.setCustomOptions(rs.getString("custom_options"));
                    list.add(od);
                }
            }
        }
        return list;
    }

    public List<Order> getAllOrdersAdmin() {
        List<Order> list = new ArrayList<Order>();
        String sql = "SELECT o.*, u.username as user_name, u.email as user_email "
                   + "FROM Orders o "
                   + "JOIN Users u ON o.user_id = u.user_id "
                   + "ORDER BY o.order_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapOrder(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE Orders SET status = ? WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public BigDecimal getTotalRevenue() {
        String sql = "SELECT SUM(deposit_amount) FROM Orders WHERE status != 'CANCELLED'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BigDecimal rev = rs.getBigDecimal(1);
                return rev != null ? rev : BigDecimal.ZERO;
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    public int countTotalOrders() {
        String sql = "SELECT COUNT(*) FROM Orders";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
        o.setOrderCode(rs.getString("order_code"));
        o.setUserId(rs.getInt("user_id"));
        o.setUserName(rs.getString("user_name"));
        o.setUserEmail(rs.getString("user_email"));
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
