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
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE,
                    "JDBC Driver not found! Check mssql-jdbc JAR in WEB-INF/lib.", ex);
            throw new RuntimeException("JDBC Driver not found!", ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE,
                    "Cannot connect to SQL Server [" + SERVER_NAME + ":" + PORT_NUMBER + "/" + DB_NAME + "]. "
                    + "Check: 1) SQL Server is running, 2) Database '" + DB_NAME + "' exists, "
                    + "3) Username/password is correct.", ex);
            throw new RuntimeException("Database connection failed: " + ex.getMessage(), ex);
        }
        return conn;
    }
}
