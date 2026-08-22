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
import model.Brand;

/**
 * Lớp truy xuất dữ liệu thương hiệu siêu xe (Brands) trong cơ sở dữ liệu.
 */
public class BrandDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(BrandDAO.class.getName());

    /**
     * Lấy toàn bộ danh sách thương hiệu siêu xe, sắp xếp theo tên A-Z.
     */
    public List<Brand> getAllBrands() {
        List<Brand> list = new ArrayList<Brand>();
        String sql = "SELECT * FROM Brands ORDER BY brand_name ASC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapBrand(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy danh sách toàn bộ thương hiệu", ex);
        }
        return list;
    }

    /**
     * Tìm thương hiệu theo ID.
     */
    public Brand getBrandById(int brandId) {
        String sql = "SELECT * FROM Brands WHERE brand_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, brandId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapBrand(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi tìm thương hiệu theo ID: " + brandId, ex);
        }
        return null;
    }

    /**
     * Kiểm tra tên thương hiệu đã tồn tại hay chưa (tránh trùng lặp khi thêm mới hoặc sửa).
     * 
     * @param brandName Tên thương hiệu cần kiểm tra
     * @param excludeBrandId ID thương hiệu bỏ qua khi cập nhật (truyền 0 nếu là thêm mới)
     */
    public boolean checkBrandNameExists(String brandName, int excludeBrandId) {
        String sql = "SELECT 1 FROM Brands WHERE LOWER(brand_name) = LOWER(?) AND brand_id != ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, brandName.trim());
            ps.setInt(2, excludeBrandId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi kiểm tra trùng tên thương hiệu: " + brandName, ex);
        }
        return false;
    }

    /**
     * Thêm mới một thương hiệu siêu xe (Admin).
     */
    public int insertBrand(Brand brand) {
        String sql = "INSERT INTO Brands (brand_name, country, logo_url, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, brand.getBrandName());
            ps.setString(2, brand.getCountry());
            ps.setString(3, brand.getLogoUrl());
            ps.setString(4, brand.getDescription());
            
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi thêm thương hiệu mới: " + brand.getBrandName(), ex);
        }
        return -1;
    }

    /**
     * Cập nhật thông tin thương hiệu (Admin).
     */
    public boolean updateBrand(Brand brand) {
        String sql = "UPDATE Brands SET brand_name = ?, country = ?, logo_url = ?, description = ? WHERE brand_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, brand.getBrandName());
            ps.setString(2, brand.getCountry());
            ps.setString(3, brand.getLogoUrl());
            ps.setString(4, brand.getDescription());
            ps.setInt(5, brand.getBrandId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi cập nhật thương hiệu ID: " + brand.getBrandId(), ex);
        }
        return false;
    }

    /**
     * Xóa một thương hiệu theo ID (Admin).
     */
    public boolean deleteBrand(int brandId) {
        String sql = "DELETE FROM Brands WHERE brand_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, brandId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi xóa thương hiệu ID: " + brandId, ex);
        }
        return false;
    }

    /**
     * Đếm tổng số lượng thương hiệu siêu xe đang có trong hệ thống (dùng cho Admin Dashboard).
     */
    public int countTotalBrands() {
        String sql = "SELECT COUNT(*) FROM Brands";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi đếm tổng số thương hiệu", ex);
        }
        return 0;
    }

    /**
     * Ánh xạ một dòng ResultSet sang đối tượng Brand.
     */
    private Brand mapBrand(ResultSet rs) throws SQLException {
        Brand b = new Brand();
        b.setBrandId(rs.getInt("brand_id"));
        b.setBrandName(rs.getString("brand_name"));
        b.setCountry(rs.getString("country"));
        b.setLogoUrl(rs.getString("logo_url"));
        b.setDescription(rs.getString("description"));
        return b;
    }
}
