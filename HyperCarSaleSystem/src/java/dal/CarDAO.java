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
import model.Car;

/**
 * Lớp truy xuất dữ liệu danh mục siêu xe (Cars) trong cơ sở dữ liệu.
 * Hỗ trợ tìm kiếm động đa tiêu chí, phân trang SQL Server OFFSET/FETCH và tìm kiếm AJAX Live Search.
 */
public class CarDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(CarDAO.class.getName());

    /**
     * Lấy danh sách siêu xe nổi bật hiển thị ở Trang Chủ (Ưu tiên theo Giá và Mã lực).
     */
    public List<Car> getFeaturedCars(int limit) {
        List<Car> list = new ArrayList<Car>();
        String sql = "SELECT TOP (?) c.*, b.brand_name, b.country AS brand_country, b.logo_url AS brand_logo_url, cat.category_name "
                   + "FROM Cars c "
                   + "JOIN Brands b ON c.brand_id = b.brand_id "
                   + "JOIN Categories cat ON c.category_id = cat.category_id "
                   + "WHERE c.status = 1 "
                   + "ORDER BY c.price DESC, c.horsepower DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCar(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy danh sách siêu xe nổi bật", ex);
        }
        return list;
    }

    /**
     * Lấy danh sách siêu xe mới cập nhật gần đây nhất.
     */
    public List<Car> getLatestCars(int limit) {
        List<Car> list = new ArrayList<Car>();
        String sql = "SELECT TOP (?) c.*, b.brand_name, b.country AS brand_country, b.logo_url AS brand_logo_url, cat.category_name "
                   + "FROM Cars c "
                   + "JOIN Brands b ON c.brand_id = b.brand_id "
                   + "JOIN Categories cat ON c.category_id = cat.category_id "
                   + "WHERE c.status = 1 "
                   + "ORDER BY c.created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCar(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy danh sách siêu xe mới nhất", ex);
        }
        return list;
    }

    /**
     * Lấy thông tin chi tiết một siêu xe theo ID (kèm thông tin Hãng và Phân khúc).
     */
    public Car getCarById(int carId) {
        String sql = "SELECT c.*, b.brand_name, b.country AS brand_country, b.logo_url AS brand_logo_url, cat.category_name "
                   + "FROM Cars c "
                   + "JOIN Brands b ON c.brand_id = b.brand_id "
                   + "JOIN Categories cat ON c.category_id = cat.category_id "
                   + "WHERE c.car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCar(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy thông tin siêu xe theo ID: " + carId, ex);
        }
        return null;
    }

    /**
     * Tìm kiếm và Lọc siêu xe đa tiêu chí kết hợp Phân trang động (OFFSET ... FETCH).
     * 
     * @param keyword Từ khóa tìm kiếm theo Tên xe, Động cơ hoặc Xuất xứ
     * @param brandId Lọc theo Hãng (null nếu chọn Tất cả)
     * @param categoryId Lọc theo Phân khúc (null nếu chọn Tất cả)
     * @param minPrice Giá thấp nhất USD
     * @param maxPrice Giá cao nhất USD
     * @param sortBy Tiêu chí sắp xếp: price_asc, price_desc, hp_desc, speed_desc, newest
     * @param page Số trang hiện tại (bắt đầu từ 1)
     * @param pageSize Số lượng xe hiển thị trên 1 trang (ví dụ: 6 hoặc 9)
     */
    public List<Car> searchCars(String keyword, Integer brandId, Integer categoryId, 
                               BigDecimal minPrice, BigDecimal maxPrice, String sortBy, 
                               int page, int pageSize) {
        List<Car> list = new ArrayList<Car>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.*, b.brand_name, b.country AS brand_country, b.logo_url AS brand_logo_url, cat.category_name ");
        sql.append("FROM Cars c ");
        sql.append("JOIN Brands b ON c.brand_id = b.brand_id ");
        sql.append("JOIN Categories cat ON c.category_id = cat.category_id ");
        sql.append("WHERE c.status = 1 ");

        List<Object> params = new ArrayList<Object>();

        // 1. Lọc theo từ khóa
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(c.model_name) LIKE ? OR LOWER(b.brand_name) LIKE ? OR LOWER(c.engine_spec) LIKE ?) ");
            String kw = "%" + keyword.trim().toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }

        // 2. Lọc theo thương hiệu
        if (brandId != null && brandId > 0) {
            sql.append("AND c.brand_id = ? ");
            params.add(brandId);
        }

        // 3. Lọc theo phân khúc
        if (categoryId != null && categoryId > 0) {
            sql.append("AND c.category_id = ? ");
            params.add(categoryId);
        }

        // 4. Lọc theo khoảng giá
        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) > 0) {
            sql.append("AND c.price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) > 0) {
            sql.append("AND c.price <= ? ");
            params.add(maxPrice);
        }

        // 5. Sắp xếp kết quả (Sorting)
        if ("price_asc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY c.price ASC ");
        } else if ("price_desc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY c.price DESC ");
        } else if ("hp_desc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY c.horsepower DESC ");
        } else if ("speed_desc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY c.top_speed DESC ");
        } else {
            sql.append("ORDER BY c.created_at DESC "); // Mặc định: Mới nhất
        }

        // 6. Phân trang SQL Server chuẩn (OFFSET ... FETCH)
        int offset = (page - 1) * pageSize;
        if (offset < 0) offset = 0;
        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(pageSize);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCar(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi tìm kiếm và lọc danh sách siêu xe", ex);
        }
        return list;
    }

    /**
     * Đếm tổng số lượng siêu xe thỏa mãn bộ lọc (để tính tổng số trang phân trang).
     */
    public int countSearchCars(String keyword, Integer brandId, Integer categoryId, 
                               BigDecimal minPrice, BigDecimal maxPrice) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) ");
        sql.append("FROM Cars c ");
        sql.append("JOIN Brands b ON c.brand_id = b.brand_id ");
        sql.append("JOIN Categories cat ON c.category_id = cat.category_id ");
        sql.append("WHERE c.status = 1 ");

        List<Object> params = new ArrayList<Object>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(c.model_name) LIKE ? OR LOWER(b.brand_name) LIKE ? OR LOWER(c.engine_spec) LIKE ?) ");
            String kw = "%" + keyword.trim().toLowerCase() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }
        if (brandId != null && brandId > 0) {
            sql.append("AND c.brand_id = ? ");
            params.add(brandId);
        }
        if (categoryId != null && categoryId > 0) {
            sql.append("AND c.category_id = ? ");
            params.add(categoryId);
        }
        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) > 0) {
            sql.append("AND c.price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) > 0) {
            sql.append("AND c.price <= ? ");
            params.add(maxPrice);
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi đếm số lượng xe theo bộ lọc", ex);
        }
        return 0;
    }

    /**
     * Tìm kiếm nhanh siêu xe phục vụ thanh tìm kiếm Live Search AJAX (Dropdown realtime).
     */
    public List<Car> searchLive(String keyword, int limit) {
        List<Car> list = new ArrayList<Car>();
        String sql = "SELECT TOP (?) c.car_id, c.model_name, c.price, c.thumbnail_url, c.horsepower, "
                   + "b.brand_name, b.country AS brand_country, b.logo_url AS brand_logo_url, cat.category_name, "
                   + "c.brand_id, c.category_id, c.deposit_rate, c.year, c.acceleration_0_100, c.top_speed, "
                   + "c.stock_quantity, c.color_options, c.engine_spec, c.description, c.status, c.created_at "
                   + "FROM Cars c "
                   + "JOIN Brands b ON c.brand_id = b.brand_id "
                   + "JOIN Categories cat ON c.category_id = cat.category_id "
                   + "WHERE c.status = 1 "
                   + "AND (LOWER(c.model_name) LIKE ? OR LOWER(b.brand_name) LIKE ?) "
                   + "ORDER BY c.price DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            String kw = "%" + keyword.trim().toLowerCase() + "%";
            ps.setString(2, kw);
            ps.setString(3, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCar(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi tìm kiếm live search", ex);
        }
        return list;
    }

    /**
     * Lấy toàn bộ danh sách xe (bao gồm cả xe tạm ngừng kinh doanh) phục vụ Bàn Quản Trị (Admin).
     */
    public List<Car> getAllCarsAdmin() {
        List<Car> list = new ArrayList<Car>();
        String sql = "SELECT c.*, b.brand_name, b.country AS brand_country, b.logo_url AS brand_logo_url, cat.category_name "
                   + "FROM Cars c "
                   + "JOIN Brands b ON c.brand_id = b.brand_id "
                   + "JOIN Categories cat ON c.category_id = cat.category_id "
                   + "ORDER BY c.car_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapCar(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy toàn bộ danh sách xe cho Admin", ex);
        }
        return list;
    }

    /**
     * Thêm mới một mẫu siêu xe (Admin).
     */
    public int insertCar(Car car) {
        String sql = "INSERT INTO Cars (model_name, brand_id, category_id, price, deposit_rate, "
                   + "year, horsepower, acceleration_0_100, top_speed, stock_quantity, thumbnail_url, "
                   + "color_options, engine_spec, description, status, created_at) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, car.getModelName());
            ps.setInt(2, car.getBrandId());
            ps.setInt(3, car.getCategoryId());
            ps.setBigDecimal(4, car.getPrice());
            ps.setBigDecimal(5, car.getDepositRate() != null ? car.getDepositRate() : new BigDecimal("10.0"));
            ps.setInt(6, car.getYear());
            ps.setInt(7, car.getHorsepower());
            ps.setDouble(8, car.getAcceleration0100());
            ps.setInt(9, car.getTopSpeed());
            ps.setInt(10, car.getStockQuantity());
            ps.setString(11, car.getThumbnailUrl());
            ps.setString(12, car.getColorOptions());
            ps.setString(13, car.getEngineSpec());
            ps.setString(14, car.getDescription());
            ps.setInt(15, car.getStatus());
            
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi thêm siêu xe mới: " + car.getModelName(), ex);
        }
        return -1;
    }

    /**
     * Cập nhật thông tin chi tiết siêu xe (Admin).
     */
    public boolean updateCar(Car car) {
        String sql = "UPDATE Cars SET model_name = ?, brand_id = ?, category_id = ?, price = ?, "
                   + "deposit_rate = ?, year = ?, horsepower = ?, acceleration_0_100 = ?, top_speed = ?, "
                   + "stock_quantity = ?, thumbnail_url = ?, color_options = ?, engine_spec = ?, "
                   + "description = ?, status = ? "
                   + "WHERE car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, car.getModelName());
            ps.setInt(2, car.getBrandId());
            ps.setInt(3, car.getCategoryId());
            ps.setBigDecimal(4, car.getPrice());
            ps.setBigDecimal(5, car.getDepositRate() != null ? car.getDepositRate() : new BigDecimal("10.0"));
            ps.setInt(6, car.getYear());
            ps.setInt(7, car.getHorsepower());
            ps.setDouble(8, car.getAcceleration0100());
            ps.setInt(9, car.getTopSpeed());
            ps.setInt(10, car.getStockQuantity());
            ps.setString(11, car.getThumbnailUrl());
            ps.setString(12, car.getColorOptions());
            ps.setString(13, car.getEngineSpec());
            ps.setString(14, car.getDescription());
            ps.setInt(15, car.getStatus());
            ps.setInt(16, car.getCarId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi cập nhật siêu xe ID: " + car.getCarId(), ex);
        }
        return false;
    }

    /**
     * Bật hoặc Tắt trạng thái mở bán siêu xe (Admin).
     */
    public boolean updateStatus(int carId, int status) {
        String sql = "UPDATE Cars SET status = ? WHERE car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setInt(2, carId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi cập nhật trạng thái bán xe ID: " + carId, ex);
        }
        return false;
    }

    /**
     * Xóa siêu xe theo ID (Admin).
     */
    public boolean deleteCar(int carId) {
        String sql = "DELETE FROM Cars WHERE car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi xóa siêu xe ID: " + carId, ex);
        }
        return false;
    }

    /**
     * Đếm tổng số lượng siêu xe đang sẵn sàng giao dịch trong hệ thống (dùng cho Admin Dashboard).
     */
    public int countTotalAvailableCars() {
        String sql = "SELECT COUNT(*) FROM Cars WHERE status = 1 AND stock_quantity > 0";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi đếm tổng số xe sẵn có", ex);
        }
        return 0;
    }

    /**
     * Ánh xạ một dòng ResultSet sang đối tượng Car.
     */
    private Car mapCar(ResultSet rs) throws SQLException {
        Car c = new Car();
        c.setCarId(rs.getInt("car_id"));
        c.setModelName(rs.getString("model_name"));
        c.setBrandId(rs.getInt("brand_id"));
        c.setBrandName(rs.getString("brand_name"));
        c.setBrandCountry(rs.getString("brand_country"));
        c.setBrandLogoUrl(rs.getString("brand_logo_url"));
        c.setCategoryId(rs.getInt("category_id"));
        c.setCategoryName(rs.getString("category_name"));
        c.setPrice(rs.getBigDecimal("price"));
        c.setDepositRate(rs.getBigDecimal("deposit_rate"));
        c.setYear(rs.getInt("year"));
        c.setHorsepower(rs.getInt("horsepower"));
        c.setAcceleration0100(rs.getDouble("acceleration_0_100"));
        c.setTopSpeed(rs.getInt("top_speed"));
        c.setStockQuantity(rs.getInt("stock_quantity"));
        c.setThumbnailUrl(rs.getString("thumbnail_url"));
        c.setColorOptions(rs.getString("color_options"));
        c.setEngineSpec(rs.getString("engine_spec"));
        c.setDescription(rs.getString("description"));
        c.setStatus(rs.getInt("status"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }
}
