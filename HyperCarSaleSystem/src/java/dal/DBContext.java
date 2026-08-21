package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lớp quản lý kết nối CSDL Microsoft SQL Server cho hệ thống
 * HyperCarSaleSystem.
 * Đặt trong package dal để đóng gói toàn bộ logic truy xuất dữ liệu trong Data
 * Access Layer.
 */
public class DBContext {

    private static final String SERVER_NAME = "localhost";
    private static final String DB_NAME = "HyperCarDB";
    private static final String PORT_NUMBER = "1433";
    private static final String USER_ID = "sa";
    private static final String PASSWORD = "12345";

    /**
     * Mở và trả về kết nối Connection tới SQL Server.
     * Ném RuntimeException kèm thông điệp chi tiết nếu kết nối thất bại để lập
     * trình viên dễ debug.
     * 
     * @return Connection đối tượng kết nối CSDL
     */
    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://" + SERVER_NAME + ":" + PORT_NUMBER
                    + ";databaseName=" + DB_NAME
                    + ";encrypt=true;trustServerCertificate=true;";
            conn = DriverManager.getConnection(url, USER_ID, PASSWORD);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE,
                    "Không tìm thấy JDBC Driver! Hãy kiểm tra file mssql-jdbc JAR trong thư mục WEB-INF/lib.", ex);
            throw new RuntimeException("JDBC Driver not found!", ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE,
                    "Không thể kết nối tới SQL Server [" + SERVER_NAME + ":" + PORT_NUMBER + "/" + DB_NAME + "]. "
                            + "Kkiểm tra: 1) SQL Server đã Start chưa, 2) Đã chạy script tạo database 'HyperCarDB' chưa, "
                            + "3) Tài khoản sa và mật khẩu có đúng không.",
                    ex);
            throw new RuntimeException("Database connection failed: " + ex.getMessage(), ex);
        }
        return conn;
    }
}
