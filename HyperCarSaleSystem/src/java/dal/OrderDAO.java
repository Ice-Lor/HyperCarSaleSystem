package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Cart;
import model.CartItem;
import model.Order;
import model.OrderDetail;

public class OrderDAO extends DBContext {

    /**
     * Tạo đơn hàng / hợp đồng cọc bằng JDBC Transaction đảm bảo tính toàn vẹn tuyệt đối
     */
    public boolean createOrderWithTransaction(Order order, Cart cart) {
        String insertOrderSql = "INSERT INTO Orders (order_code, user_id, total_amount, deposit_amount, "
                              + "coupon_code, discount_amount, status, payment_method, delivery_address, phone, note) "
                              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertDetailSql = "INSERT INTO OrderDetails (order_id, car_id, quantity, unit_price, selected_color, custom_options) "
                               + "VALUES (?, ?, ?, ?, ?, ?)";
        String reduceStockSql = "UPDATE Cars SET stock_quantity = stock_quantity - ? WHERE car_id = ? AND stock_quantity >= ?";
        String logSql = "INSERT INTO ActivityLogs (user_id, action, details) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false); // BẮT ĐẦU TRANSACTION

            // 1. Insert Orders
            int orderId = 0;
            try (PreparedStatement psOrder = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setString(1, order.getOrderCode());
                psOrder.setInt(2, order.getUserId());
                psOrder.setDouble(3, order.getTotalAmount());
                psOrder.setDouble(4, order.getDepositAmount());
                psOrder.setString(5, order.getCouponCode());
                psOrder.setDouble(6, order.getDiscountAmount());
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
                        orderId = rs.getInt(1);
                        order.setOrderId(orderId);
                    }
                }
            }

            // 2. Insert OrderDetails & Reduce Stock
            try (PreparedStatement psDetail = conn.prepareStatement(insertDetailSql);
                 PreparedStatement psStock = conn.prepareStatement(reduceStockSql)) {
                
                for (CartItem item : cart.getItems()) {
                    // Thêm chi tiết
                    psDetail.setInt(1, orderId);
                    psDetail.setInt(2, item.getCar().getCarId());
                    psDetail.setInt(3, item.getQuantity());
                    psDetail.setDouble(4, item.getCar().getPrice());
                    psDetail.setString(5, item.getSelectedColor());
                    psDetail.setString(6, item.getCustomOptions());
                    psDetail.addBatch();

                    // Trừ tồn kho
                    psStock.setInt(1, item.getQuantity());
                    psStock.setInt(2, item.getCar().getCarId());
                    psStock.setInt(3, item.getQuantity());
                    int stockUpdated = psStock.executeUpdate();
                    if (stockUpdated == 0) {
                        // Hết hàng hoặc số lượng không đủ -> Rollback ngay!
                        conn.rollback();
                        return false;
                    }
                }
                psDetail.executeBatch();
            }

            // 3. Log Activity
            try (PreparedStatement psLog = conn.prepareStatement(logSql)) {
                psLog.setInt(1, order.getUserId());
                psLog.setString(2, "PLACE_ORDER");
                psLog.setString(3, "Tạo đơn đặt cọc siêu xe mã: " + order.getOrderCode() + ", cọc: $" + order.getDepositAmount());
                psLog.executeUpdate();
            }

            // HOÀN TẤT THÀNH CÔNG -> COMMIT
            conn.commit();
            return true;

        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, "Lỗi trong Transaction Đặt Hàng. Đang Rollback...", ex);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rEx) {
                    Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, "Lỗi Rollback!", rEx);
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

    public Order getOrderById(int orderId) {
        String sql = "SELECT o.*, u.full_name as customer_name, u.email as customer_email "
                   + "FROM Orders o JOIN Users u ON o.user_id = u.user_id WHERE o.order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order o = extractOrder(rs);
                    o.setOrderDetails(getOrderDetailsByOrderId(orderId));
                    return o;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> list = new ArrayList<Order>();
        String sql = "SELECT o.*, u.full_name as customer_name, u.email as customer_email "
                   + "FROM Orders o JOIN Users u ON o.user_id = u.user_id "
                   + "WHERE o.user_id = ? ORDER BY o.order_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = extractOrder(rs);
                    o.setOrderDetails(getOrderDetailsByOrderId(o.getOrderId()));
                    list.add(o);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<Order>();
        String sql = "SELECT o.*, u.full_name as customer_name, u.email as customer_email "
                   + "FROM Orders o JOIN Users u ON o.user_id = u.user_id "
                   + "ORDER BY o.order_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = extractOrder(rs);
                o.setOrderDetails(getOrderDetailsByOrderId(o.getOrderId()));
                list.add(o);
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

    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> list = new ArrayList<OrderDetail>();
        String sql = "SELECT od.*, c.model_name, c.thumbnail_url, b.brand_name "
                   + "FROM OrderDetails od "
                   + "JOIN Cars c ON od.car_id = c.car_id "
                   + "JOIN Brands b ON c.brand_id = b.brand_id "
                   + "WHERE od.order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetail d = new OrderDetail();
                    d.setDetailId(rs.getInt("detail_id"));
                    d.setOrderId(rs.getInt("order_id"));
                    d.setCarId(rs.getInt("car_id"));
                    d.setQuantity(rs.getInt("quantity"));
                    d.setUnitPrice(rs.getDouble("unit_price"));
                    d.setSelectedColor(rs.getString("selected_color"));
                    d.setCustomOptions(rs.getString("custom_options"));
                    d.setModelName(rs.getString("model_name"));
                    d.setThumbnailUrl(rs.getString("thumbnail_url"));
                    d.setBrandName(rs.getString("brand_name"));
                    list.add(d);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    // Dashboard Analytics
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<String, Object>();
        String sql = "SELECT "
                   + "(SELECT ISNULL(SUM(deposit_amount), 0) FROM Orders WHERE status != 'CANCELLED') as total_revenue, "
                   + "(SELECT COUNT(*) FROM Orders) as total_orders, "
                   + "(SELECT COUNT(*) FROM TestDriveBookings WHERE status = 'PENDING') as pending_bookings, "
                   + "(SELECT COUNT(*) FROM Users WHERE role_id = 2) as total_customers, "
                   + "(SELECT COUNT(*) FROM Cars WHERE status = 1) as active_cars";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.put("totalRevenue", rs.getDouble("total_revenue"));
                stats.put("totalOrders", rs.getInt("total_orders"));
                stats.put("pendingBookings", rs.getInt("pending_bookings"));
                stats.put("totalCustomers", rs.getInt("total_customers"));
                stats.put("activeCars", rs.getInt("active_cars"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stats;
    }

    private Order extractOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
        o.setOrderCode(rs.getString("order_code"));
        o.setUserId(rs.getInt("user_id"));
        o.setTotalAmount(rs.getDouble("total_amount"));
        o.setDepositAmount(rs.getDouble("deposit_amount"));
        o.setCouponCode(rs.getString("coupon_code"));
        o.setDiscountAmount(rs.getDouble("discount_amount"));
        o.setStatus(rs.getString("status"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setDeliveryAddress(rs.getString("delivery_address"));
        o.setPhone(rs.getString("phone"));
        o.setNote(rs.getString("note"));
        o.setOrderDate(rs.getTimestamp("order_date"));
        
        try {
            o.setCustomerName(rs.getString("customer_name"));
            o.setCustomerEmail(rs.getString("customer_email"));
        } catch (SQLException ignored) {}
        
        return o;
    }
}
