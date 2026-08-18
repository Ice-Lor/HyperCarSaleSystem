package dal;

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

public class CarDAO extends DBContext {

    public List<Car> getFeaturedCars(int limit) {
        List<Car> list = new ArrayList<Car>();
        String sql = "SELECT TOP (?) c.*, b.brand_name, b.logo_url as brand_logo, cat.category_name "
                   + "FROM Cars c "
                   + "JOIN Brands b ON c.brand_id = b.brand_id "
                   + "JOIN Categories cat ON c.category_id = cat.category_id "
                   + "WHERE c.status = 1 "
                   + "ORDER BY c.price DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractCar(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Car> filterCars(String keyword, Integer brandId, Integer categoryId, 
                                Double minPrice, Double maxPrice, Integer minHp, 
                                String sortBy, int page, int pageSize) {
        List<Car> list = new ArrayList<Car>();
        StringBuilder sql = new StringBuilder(
            "SELECT c.*, b.brand_name, b.logo_url as brand_logo, cat.category_name "
          + "FROM Cars c "
          + "JOIN Brands b ON c.brand_id = b.brand_id "
          + "JOIN Categories cat ON c.category_id = cat.category_id "
          + "WHERE c.status = 1 "
        );
        List<Object> params = new ArrayList<Object>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (c.model_name LIKE ? OR b.brand_name LIKE ? OR c.description LIKE ?) ");
            String kw = "%" + keyword.trim() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }
        if (brandId != null && brandId > 0) {
            sql.append(" AND c.brand_id = ? ");
            params.add(brandId);
        }
        if (categoryId != null && categoryId > 0) {
            sql.append(" AND c.category_id = ? ");
            params.add(categoryId);
        }
        if (minPrice != null && minPrice >= 0) {
            sql.append(" AND c.price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null && maxPrice > 0) {
            sql.append(" AND c.price <= ? ");
            params.add(maxPrice);
        }
        if (minHp != null && minHp > 0) {
            sql.append(" AND c.horsepower >= ? ");
            params.add(minHp);
        }

        // Sorting
        if ("price_asc".equalsIgnoreCase(sortBy)) {
            sql.append(" ORDER BY c.price ASC ");
        } else if ("price_desc".equalsIgnoreCase(sortBy)) {
            sql.append(" ORDER BY c.price DESC ");
        } else if ("hp_desc".equalsIgnoreCase(sortBy)) {
            sql.append(" ORDER BY c.horsepower DESC ");
        } else if ("speed_desc".equalsIgnoreCase(sortBy)) {
            sql.append(" ORDER BY c.top_speed DESC ");
        } else if ("newest".equalsIgnoreCase(sortBy)) {
            sql.append(" ORDER BY c.year DESC, c.car_id DESC ");
        } else {
            sql.append(" ORDER BY c.car_id DESC ");
        }

        // Pagination (MSSQL OFFSET...FETCH)
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");
        int offset = (page - 1) * pageSize;
        if (offset < 0) offset = 0;
        params.add(offset);
        params.add(pageSize);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractCar(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int countFilteredCars(String keyword, Integer brandId, Integer categoryId, 
                                 Double minPrice, Double maxPrice, Integer minHp) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) FROM Cars c "
          + "JOIN Brands b ON c.brand_id = b.brand_id "
          + "WHERE c.status = 1 "
        );
        List<Object> params = new ArrayList<Object>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (c.model_name LIKE ? OR b.brand_name LIKE ? OR c.description LIKE ?) ");
            String kw = "%" + keyword.trim() + "%";
            params.add(kw);
            params.add(kw);
            params.add(kw);
        }
        if (brandId != null && brandId > 0) {
            sql.append(" AND c.brand_id = ? ");
            params.add(brandId);
        }
        if (categoryId != null && categoryId > 0) {
            sql.append(" AND c.category_id = ? ");
            params.add(categoryId);
        }
        if (minPrice != null && minPrice >= 0) {
            sql.append(" AND c.price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null && maxPrice > 0) {
            sql.append(" AND c.price <= ? ");
            params.add(maxPrice);
        }
        if (minHp != null && minHp > 0) {
            sql.append(" AND c.horsepower >= ? ");
            params.add(minHp);
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
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Car> searchCarsByKeyword(String keyword, int limit) {
        List<Car> list = new ArrayList<Car>();
        String sql = "SELECT TOP (?) c.*, b.brand_name, b.logo_url as brand_logo, cat.category_name "
                   + "FROM Cars c "
                   + "JOIN Brands b ON c.brand_id = b.brand_id "
                   + "JOIN Categories cat ON c.category_id = cat.category_id "
                   + "WHERE c.status = 1 AND (c.model_name LIKE ? OR b.brand_name LIKE ?) "
                   + "ORDER BY c.model_name ASC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            String kw = "%" + keyword.trim() + "%";
            ps.setString(2, kw);
            ps.setString(3, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractCar(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public Car getCarById(int carId) {
        String sql = "SELECT c.*, b.brand_name, b.logo_url as brand_logo, cat.category_name "
                   + "FROM Cars c "
                   + "JOIN Brands b ON c.brand_id = b.brand_id "
                   + "JOIN Categories cat ON c.category_id = cat.category_id "
                   + "WHERE c.car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCar(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Car> getAllCarsForAdmin() {
        List<Car> list = new ArrayList<Car>();
        String sql = "SELECT c.*, b.brand_name, b.logo_url as brand_logo, cat.category_name "
                   + "FROM Cars c "
                   + "JOIN Brands b ON c.brand_id = b.brand_id "
                   + "JOIN Categories cat ON c.category_id = cat.category_id "
                   + "ORDER BY c.car_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractCar(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean insertCar(Car car) {
        String sql = "INSERT INTO Cars (model_name, brand_id, category_id, price, deposit_rate, year, horsepower, "
                   + "acceleration_0_100, top_speed, stock_quantity, thumbnail_url, color_options, engine_spec, description, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setCarStatementParams(ps, car);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        car.setCarId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean updateCar(Car car) {
        String sql = "UPDATE Cars SET model_name = ?, brand_id = ?, category_id = ?, price = ?, deposit_rate = ?, "
                   + "year = ?, horsepower = ?, acceleration_0_100 = ?, top_speed = ?, stock_quantity = ?, "
                   + "thumbnail_url = ?, color_options = ?, engine_spec = ?, description = ?, status = ? "
                   + "WHERE car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setCarStatementParams(ps, car);
            ps.setInt(16, car.getCarId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteCar(int carId) {
        String sql = "UPDATE Cars SET status = 0 WHERE car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean reduceStock(Connection conn, int carId, int quantity) throws SQLException {
        String sql = "UPDATE Cars SET stock_quantity = stock_quantity - ? WHERE car_id = ? AND stock_quantity >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, carId);
            ps.setInt(3, quantity);
            return ps.executeUpdate() > 0;
        }
    }

    private void setCarStatementParams(PreparedStatement ps, Car car) throws SQLException {
        ps.setString(1, car.getModelName());
        ps.setInt(2, car.getBrandId());
        ps.setInt(3, car.getCategoryId());
        ps.setDouble(4, car.getPrice());
        ps.setDouble(5, car.getDepositRate() > 0 ? car.getDepositRate() : 10.0);
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
    }

    private Car extractCar(ResultSet rs) throws SQLException {
        Car c = new Car();
        c.setCarId(rs.getInt("car_id"));
        c.setModelName(rs.getString("model_name"));
        c.setBrandId(rs.getInt("brand_id"));
        c.setCategoryId(rs.getInt("category_id"));
        c.setPrice(rs.getDouble("price"));
        c.setDepositRate(rs.getDouble("deposit_rate"));
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
        
        try {
            c.setBrandName(rs.getString("brand_name"));
            c.setBrandLogo(rs.getString("brand_logo"));
            c.setCategoryName(rs.getString("category_name"));
        } catch (SQLException ignored) {}
        
        return c;
    }
}
