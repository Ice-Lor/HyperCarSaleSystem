package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lớp quản lý kết nối CSDL Microsoft SQL Server cho hệ thống HyperCarSaleSystem
 */
public class DBContext {

    private static final String SERVER_NAME = "localhost";
    private static final String DB_NAME = "HyperCarDB";
    private static final String PORT_NUMBER = "1433";
    private static final String USER_ID = "sa";
    private static final String PASSWORD = "123";

    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://" + SERVER_NAME + ":" + PORT_NUMBER
                    + ";databaseName=" + DB_NAME
                    + ";encrypt=true;trustServerCertificate=true;";
            conn = DriverManager.getConnection(url, USER_ID, PASSWORD);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, "Lỗi kết nối CSDL SQL Server!", ex);
        }
        return conn;
    }
}
