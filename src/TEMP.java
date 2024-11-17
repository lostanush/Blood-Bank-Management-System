import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TEMP {
    public Connection con;
    String url = "jdbc:mysql://localhost:3306/bloodbank"; // Database URL
    String user = "root"; // MySQL username
    String pass = "1234"; // MySQL password

    // Method to establish a database connection
    public Connection mkDataBase() throws SQLException {
        try {
            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the connection
            con = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new SQLException("Driver not found!", ex);
        }
        return con;
    }

    // Method to retrieve and print all data from the bloodbank table
    public void retrieveAndPrintData() {
        String query = "SELECT * FROM bloodbank;";
        try (Connection con = mkDataBase();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Print column headers
            System.out.printf("%-5s %-15s %-10s %-10s%n", "ID", "Name", "Blood Group", "Quantity");
            System.out.println("---------------------------------------------");

            // Iterate through the result set
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String bloodGroup = rs.getString("bloodgroup");
                int quantity = rs.getInt("quantity");

                // Print each row
                System.out.printf("%-5d %-15s %-10s %-10d%n", id, name, bloodGroup, quantity);
            }
        } catch (SQLException ex) {
            System.out.println("Error retrieving data: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("TEMP class is running...");
        TEMP tempInstance = new TEMP(); // Create an instance of TEMP
        tempInstance.retrieveAndPrintData(); // Call the method to retrieve and print data
    }
}
