import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class AddDonor {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JPanel controlPanel;
    private JTextField nameField, bloodGroupField, quantityField;
    private JButton saveButton, cancelButton;

    public AddDonor() {
        prepareGUI();
    }
//
    private void prepareGUI() {
        mainFrame = new JFrame("Add Donor");
        mainFrame.setSize(700, 400);
        mainFrame.setLayout(null);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Close operation to return to Home page
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                mainFrame.dispose();
            }
        });

        // Background
        JLabel background = new JLabel(new ImageIcon(getClass().getResource("/images/Anime-4k-Wallpaper.jpg")));
        background.setBounds(0, 0, 700, 400);
        mainFrame.add(background);

        // Header label
        headerLabel = new JLabel("<html><u>Add Donor</u></html>", JLabel.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 28));
        headerLabel.setForeground(Color.BLACK);
        headerLabel.setBounds(150, 20, 300, 50);
        background.add(headerLabel);

        // Name label and field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setBounds(100, 100, 100, 25);
        background.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(200, 100, 250, 30);
        background.add(nameField);

        // Blood group label and field
        JLabel bloodGroupLabel = new JLabel("Blood Group:");
        bloodGroupLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bloodGroupLabel.setBounds(100, 150, 100, 25);
        background.add(bloodGroupLabel);

        bloodGroupField = new JTextField();
        bloodGroupField.setBounds(200, 150, 250, 30);
        background.add(bloodGroupField);

        // Quantity label and field
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 16));
        quantityLabel.setBounds(100, 200, 100, 25);
        background.add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(200, 200, 250, 30);
        background.add(quantityField);

        // Save button
        saveButton = new JButton("Save");
        saveButton.setBounds(200, 280, 100, 30);
        saveButton.setBackground(new Color(0, 128, 0));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.addActionListener(this::saveDonorDetails);
        background.add(saveButton);

        // Cancel button
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(350, 280, 100, 30);
        cancelButton.setBackground(new Color(220, 20, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
            }
        });
        background.add(cancelButton);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void saveDonorDetails(ActionEvent evt) {
        String name = nameField.getText();
        String bloodGroup = bloodGroupField.getText();
        String quantityText = quantityField.getText();

        if (name.isEmpty() || bloodGroup.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Please fill all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);

            // Get database connection from DBConnection class
            try (Connection conn = DBConnection.getConnection()) {
                // First, check if a donor with the same name and blood group already exists
                String checkQuery = "SELECT quantity FROM bloodbank WHERE name = ? AND bloodgroup = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setString(1, name);
                checkStmt.setString(2, bloodGroup);

                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    // Donor exists with the same name and blood group, show confirmation dialog
                    int existingQuantity = rs.getInt("quantity");
                    int newQuantity = existingQuantity + quantity;

                    int response = JOptionPane.showConfirmDialog(mainFrame, "Donor exists with the same name and blood group. Do you want to update the quantity?",
                            "Update Donor", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        // Redirect to the UpdateDonor page with existing quantity for update
                        String donorName = name; // Store donor name to pass
                        String donorBloodGroup = bloodGroup; // Store blood group to pass
                        String donorQuantity = String.valueOf(existingQuantity); // Store existing quantity to pass

                        mainFrame.dispose(); // Close current AddDonor window
                        new UpdateDonor(); // Open the UpdateDonor page with existing data
                    }
                } else {
                    // If the name does not exist, insert new donor
                    String insertQuery = "INSERT INTO bloodbank (name, bloodgroup, quantity) VALUES (?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                    insertStmt.setString(1, name);
                    insertStmt.setString(2, bloodGroup);
                    insertStmt.setInt(3, quantity);

                    int rowsInserted = insertStmt.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(mainFrame, "New donor added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Failed to add donor. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(mainFrame, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainFrame, "Quantity must be a number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        new AddDonor();
    }
}
