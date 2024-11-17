import java.awt.*;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class BloodDonated {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JTable bloodTable;
    private JLabel totalBloodLabel;
    private JPanel controlPanel;
    private DefaultTableModel tableModel;

    public BloodDonated() {
        prepareGUI();
        fetchBloodData();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Blood Donated Summary");
        mainFrame.setSize(700, 400);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.getContentPane().setBackground(Color.WHITE);

        headerLabel = new JLabel("Blood Donated Summary", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(220, 20, 60)); // Crimson red
        mainFrame.add(headerLabel, BorderLayout.NORTH);

        // Table for blood types and their quantities
        String[] columnNames = {"Blood Type", "Quantity (ml)"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bloodTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bloodTable);
        bloodTable.setFillsViewportHeight(true);

        mainFrame.add(scrollPane, BorderLayout.CENTER);

        // Panel for total blood and close button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        totalBloodLabel = new JLabel("Fetching total donated blood...", JLabel.CENTER);
        totalBloodLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bottomPanel.add(totalBloodLabel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.setBackground(new Color(220, 20, 60)); // Crimson red
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> mainFrame.dispose());
        bottomPanel.add(closeButton, BorderLayout.SOUTH);

        mainFrame.add(bottomPanel, BorderLayout.SOUTH);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void fetchBloodData() {
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT bloodgroup, SUM(quantity) AS total_quantity FROM bloodbank GROUP BY bloodgroup";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Populate table with blood group data
            int grandTotal = 0;
            while (rs.next()) {
                String bloodGroup = rs.getString("bloodgroup");
                int quantity = rs.getInt("total_quantity");
                tableModel.addRow(new Object[]{bloodGroup, quantity});
                grandTotal += quantity;
            }

            totalBloodLabel.setText("Total Blood Donated: " + grandTotal + " ml");
            conn.close();
        } catch (SQLException e) {
            totalBloodLabel.setText("Error fetching data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new BloodDonated();
    }
}
