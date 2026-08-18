package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Brand;

public class BrandDAO extends DBContext {

    public List<Brand> getAllBrands() {
        List<Brand> list = new ArrayList<Brand>();
        String sql = "SELECT * FROM Brands ORDER BY brand_name ASC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Brand b = new Brand();
                b.setBrandId(rs.getInt("brand_id"));
                b.setBrandName(rs.getString("brand_name"));
                b.setCountry(rs.getString("country"));
                b.setLogoUrl(rs.getString("logo_url"));
                b.setDescription(rs.getString("description"));
                list.add(b);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BrandDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public Brand getBrandById(int brandId) {
        String sql = "SELECT * FROM Brands WHERE brand_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, brandId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Brand b = new Brand();
                    b.setBrandId(rs.getInt("brand_id"));
                    b.setBrandName(rs.getString("brand_name"));
                    b.setCountry(rs.getString("country"));
                    b.setLogoUrl(rs.getString("logo_url"));
                    b.setDescription(rs.getString("description"));
                    return b;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(BrandDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean insertBrand(Brand brand) {
        String sql = "INSERT INTO Brands (brand_name, country, logo_url, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, brand.getBrandName());
            ps.setString(2, brand.getCountry());
            ps.setString(3, brand.getLogoUrl());
            ps.setString(4, brand.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(BrandDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

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
            Logger.getLogger(BrandDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteBrand(int brandId) {
        String sql = "DELETE FROM Brands WHERE brand_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, brandId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(BrandDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
