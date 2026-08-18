package dal;

import java.math.BigDecimal;
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
import model.Car;

public class CarDAO extends DBContext {

    public List<Car> getFeaturedCars(int limit) {
        List<Car> list = new ArrayList<Car>();
        String sql = "SELECT TOP (?) c.*, b.brand_name, b.country as brand_country, cat.category_name "
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
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Car> getLatestCars(int limit) {
        List<Car> list = new ArrayList<Car>();
        String sql = "SELECT TOP (?) c.*, b.brand_name, b.country as brand_country, cat.category_name "
                   + "FROM Cars c "
                   + "JOIN Brands b ON c.brand_id = b.brand_id "
                   + "JOIN Categories cat ON c.category_id = cat.category_id "
                   + "WHERE c.status = 1 "
                   + "ORDER BY c.car_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCar(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public Car getCarById(int carId) {
        String sql = "SELECT c.*, b.brand_name, b.country as brand_country, cat.category_name "
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
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Car> searchCarsByKeyword(String keyword, int limit) {
        List<Car> list = new ArrayList<Car>();
        String sql = "SELECT TOP (?) c.*, b.brand_name, b.country as brand_country, cat.category_name "
                   + "FROM Cars c "
                   + "JOIN Brands b ON c.brand_id = b.brand_id "
                   + "JOIN Categories cat ON c.category_id = cat.category_id "
                   + "WHERE c.status = 1 AND (c.model_name LIKE ? OR b.brand_name LIKE ?) "
                   + "ORDER BY c.car_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCar(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Car> searchCarsDynamic(String keyword, Integer brandId, Integer categoryId,
                                      Double minPrice, Double maxPrice, String sortBy,
                                      int page, int pageSize) {
        List<Car> list = new ArrayList<Car>();
        StringBuilder sql = new StringBuilder(
            "SELECT c.*, b.brand_name, b.country as brand_country, cat.category_name "
          + "FROM Cars c "
          + "JOIN Brands b ON c.brand_id = b.brand_id "
          + "JOIN Categories cat ON c.category_id = cat.category_id "
          + "WHERE c.status = 1 "
        );

        List<Object> params = new ArrayList<Object>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (c.model_name LIKE ? OR b.brand_name LIKE ?) ");
            params.add("%" + keyword.trim() + "%");
            params.add("%" + keyword.trim() + "%");
        }
        if (brandId != null && brandId > 0) {
            sql.append("AND c.brand_id = ? ");
            params.add(brandId);
        }
        if (categoryId != null && categoryId > 0) {
            sql.append("AND c.category_id = ? ");
            params.add(categoryId);
        }
        if (minPrice != null && minPrice >= 0) {
            sql.append("AND c.price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null && maxPrice > 0) {
            sql.append("AND c.price <= ? ");
            params.add(maxPrice);
        }

        if ("price_asc".equals(sortBy)) {
            sql.append("ORDER BY c.price ASC ");
        } else if ("price_desc".equals(sortBy)) {
            sql.append("ORDER BY c.price DESC ");
        } else if ("hp_desc".equals(sortBy)) {
            sql.append("ORDER BY c.horsepower DESC ");
        } else if ("speed_desc".equals(sortBy)) {
            sql.append("ORDER BY c.top_speed DESC ");
        } else {
            sql.append("ORDER BY c.car_id DESC ");
        }

        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add((page - 1) * pageSize);
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
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int countCarsDynamic(String keyword, Integer brandId, Integer categoryId,
                               Double minPrice, Double maxPrice) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) "
          + "FROM Cars c "
          + "JOIN Brands b ON c.brand_id = b.brand_id "
          + "JOIN Categories cat ON c.category_id = cat.category_id "
          + "WHERE c.status = 1 "
        );

        List<Object> params = new ArrayList<Object>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (c.model_name LIKE ? OR b.brand_name LIKE ?) ");
            params.add("%" + keyword.trim() + "%");
            params.add("%" + keyword.trim() + "%");
        }
        if (brandId != null && brandId > 0) {
            sql.append("AND c.brand_id = ? ");
            params.add(brandId);
        }
        if (categoryId != null && categoryId > 0) {
            sql.append("AND c.category_id = ? ");
            params.add(categoryId);
        }
        if (minPrice != null && minPrice >= 0) {
            sql.append("AND c.price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null && maxPrice > 0) {
            sql.append("AND c.price <= ? ");
            params.add(maxPrice);
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Car> getAllCarsAdmin() {
        List<Car> list = new ArrayList<Car>();
        String sql = "SELECT c.*, b.brand_name, b.country as brand_country, cat.category_name "
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
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int insertCar(Car car) {
        String sql = "INSERT INTO Cars (model_name, brand_id, category_id, price, deposit_rate, year, "
                   + "horsepower, acceleration_0_100, top_speed, stock_quantity, thumbnail_url, "
                   + "color_options, engine_spec, description, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public boolean updateCar(Car car) {
        String sql = "UPDATE Cars SET model_name = ?, brand_id = ?, category_id = ?, price = ?, deposit_rate = ?, "
                   + "year = ?, horsepower = ?, acceleration_0_100 = ?, top_speed = ?, stock_quantity = ?, "
                   + "thumbnail_url = ?, color_options = ?, engine_spec = ?, description = ?, status = ? "
                   + "WHERE car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, car.getModelName());
            ps.setInt(2, car.getBrandId());
            ps.setInt(3, car.getCategoryId());
            ps.setBigDecimal(4, car.getPrice());
            ps.setBigDecimal(5, car.getDepositRate());
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
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteCar(int carId) {
        String sql = "DELETE FROM Cars WHERE car_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public int countTotalCars() {
        String sql = "SELECT COUNT(*) FROM Cars WHERE status = 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public Map<String, Integer> countCarsByBrand() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        String sql = "SELECT b.brand_name, COUNT(c.car_id) as total "
                   + "FROM Brands b "
                   + "LEFT JOIN Cars c ON b.brand_id = c.brand_id "
                   + "GROUP BY b.brand_name";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString("brand_name"), rs.getInt("total"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    private Car mapCar(ResultSet rs) throws SQLException {
        Car c = new Car();
        c.setCarId(rs.getInt("car_id"));
        c.setModelName(rs.getString("model_name"));
        c.setBrandId(rs.getInt("brand_id"));
        c.setBrandName(rs.getString("brand_name"));
        c.setBrandCountry(rs.getString("brand_country"));
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
