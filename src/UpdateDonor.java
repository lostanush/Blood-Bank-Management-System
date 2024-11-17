import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateDonor {
    private JFrame mainFrame;
    private JTextField nameField, bloodGroupField, quantityField;
    private JButton saveButton, cancelButton;

    public UpdateDonor() {
        prepareGUI();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Update Donor");
        mainFrame.setSize(700, 400);
        mainFrame.setLayout(null);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Background image
        JLabel background = new JLabel(new ImageIcon(getClass().getResource("/images/Anime-4k-Wallpaper.jpg")));
        background.setBounds(0, 0, 700, 400);
        mainFrame.add(background);

        // Header label
        JLabel headerLabel = new JLabel("Update Donor", JLabel.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 28));
        headerLabel.setForeground(Color.BLACK);
        headerLabel.setBounds(150, 20, 300, 50);
        background.add(headerLabel);

        // Name label and field
        JLabel nameLabel = new JLabel("Enter Donor Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setBounds(100, 80, 150, 25);
        background.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(250, 80, 250, 30);
        background.add(nameField);

        // Blood group label and field
        JLabel bloodGroupLabel = new JLabel("Enter Blood Group:");
        bloodGroupLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bloodGroupLabel.setBounds(100, 130, 150, 25);
        background.add(bloodGroupLabel);

        bloodGroupField = new JTextField();
        bloodGroupField.setBounds(250, 130, 250, 30);
        background.add(bloodGroupField);

        // Quantity label and field
        JLabel quantityLabel = new JLabel("Enter Quantity:");
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 16));
        quantityLabel.setBounds(100, 180, 150, 25);
        background.add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(250, 180, 250, 30);
        background.add(quantityField);

        // Save button to save updated donor details or add new donor
        saveButton = new JButton("Save");
        saveButton.setBounds(200, 250, 100, 30);
        saveButton.setBackground(new Color(0, 128, 0));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.addActionListener(e -> saveUpdatedDonorDetails());
        background.add(saveButton);

        // Cancel button to exit without saving
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(350, 250, 100, 30);
        cancelButton.setBackground(new Color(220, 20, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.addActionListener(e -> mainFrame.dispose());
        background.add(cancelButton);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void saveUpdatedDonorDetails() {
        String name = nameField.getText();
        String bloodGroup = bloodGroupField.getText();
        String quantityText = quantityField.getText();

        if (name.isEmpty() || bloodGroup.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Please fill all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);

            // Check if the donor exists by name and blood group
            try (Connection conn = DBConnection.getConnection()) {
                String checkQuery = "SELECT * FROM bloodbank WHERE name = ? AND bloodgroup = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setString(1, name);
                checkStmt.setString(2, bloodGroup);

                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    // If donor exists, pre-fill fields and update the quantity
                    String updateQuery = "UPDATE bloodbank SET quantity = ? WHERE name = ? AND bloodgroup = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setInt(1, quantity);
                    updateStmt.setString(2, name);
                    updateStmt.setString(3, bloodGroup);

                    int rowsUpdated = updateStmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(mainFrame, "Donor quantity updated successfully!");
                        mainFrame.dispose(); // Close the current window
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Failed to update donor.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // If donor does not exist, redirect to AddDonor page
                    int response = JOptionPane.showConfirmDialog(mainFrame, "Donor does not exist. Do you want to add a new donor?", "Add New Donor", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        mainFrame.dispose(); // Close the current window
                        new AddDonor(); // Open AddDonor page
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(mainFrame, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainFrame, "Quantity must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // This is the starting point for opening the UpdateDonor page
        new UpdateDonor();
    }
}
