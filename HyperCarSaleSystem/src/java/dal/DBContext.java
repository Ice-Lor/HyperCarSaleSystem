package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lớp quản lý kết nối CSDL Microsoft SQL Server chuẩn JDBC thuần
 */
public class DBContext {
    private static final String SERVER_NAME = "localhost";
    private static final String PORT_NUMBER = "1433";
    private static final String DATABASE_NAME = "HyperCarDB";
    private static final String USER_ID = "sa";
    private static final String PASSWORD = "123"; // Đại ca có thể chỉnh sửa mật khẩu sa ở đây

    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://" + SERVER_NAME + ":" + PORT_NUMBER + 
                         ";databaseName=" + DATABASE_NAME + 
                         ";encrypt=true;trustServerCertificate=true;characterEncoding=UTF-8";
            conn = DriverManager.getConnection(url, USER_ID, PASSWORD);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, "Không tìm thấy Driver SQL Server!", ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, "Lỗi kết nối CSDL SQL Server!", ex);
        }
        return conn;
    }

    public static void main(String[] args) {
        DBContext db = new DBContext();
        Connection c = db.getConnection();
        if (c != null) {
            System.out.println("Kết nối cơ sở dữ liệu HyperCarDB thành công!");
        } else {
            System.out.println("Kết nối thất bại. Vui lòng kiểm tra lại cấu hình tài khoản sa!");
        }
    }
}
