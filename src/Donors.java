import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Donors {
    private JFrame mainFrame;
    private JTable donorsTable;

    public Donors() {
        prepareGUI();
        fetchAndDisplayDonors();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("All Donors");
        mainFrame.setSize(700, 400);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("All Donors", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(Color.RED);
        mainFrame.add(headerLabel, BorderLayout.NORTH);

        donorsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(donorsTable);
        mainFrame.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> mainFrame.dispose());
        mainFrame.add(backButton, BorderLayout.SOUTH);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void fetchAndDisplayDonors() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM bloodbank";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Use metadata to dynamically create column names
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Populate table with data
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            while (rs.next()) {
                String[] rowData = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getString(i);
                }
                tableModel.addRow(rowData);
            }

            donorsTable.setModel(tableModel);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new Donors();
    }
}
