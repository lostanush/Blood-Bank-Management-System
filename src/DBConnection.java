import java.sql.*;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/bloodbank";
        String user = "root";
        String pass = "1234";
        return DriverManager.getConnection(url, user, pass);
    }
}