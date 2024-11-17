import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DeleteDonor {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JPanel controlPanel;
    private JTextField nameField, bloodGroupField;
    private JButton deleteButton, cancelButton;

    public DeleteDonor() {
        prepareGUI();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("<html><u>Delete Donor</u></html>");
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

        // Background image
        JLabel background = new JLabel(new ImageIcon(getClass().getResource("/images/Anime-4k-Wallpaper.jpg")));
        background.setBounds(0, 0, 700, 400);
        mainFrame.add(background);

        // Header label
        headerLabel = new JLabel("Delete Donor", JLabel.CENTER);
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

        // Delete button
        deleteButton = new JButton("Delete");
        deleteButton.setBounds(200, 250, 100, 30);
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.addActionListener(this::deleteDonor);
        background.add(deleteButton);

        // Cancel button
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(350, 250, 100, 30);
        cancelButton.setBackground(new Color(255, 99, 71));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.addActionListener(e -> mainFrame.dispose());
        background.add(cancelButton);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void deleteDonor(ActionEvent evt) {
        String name = nameField.getText();
        String bloodGroup = bloodGroupField.getText();

        if (name.isEmpty() || bloodGroup.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Please fill all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Query to check if donor exists with the given name and blood group
            String query = "SELECT * FROM bloodbank WHERE name = ? AND bloodgroup = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, bloodGroup);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // If donor exists, ask for confirmation before deleting
                int confirmation = JOptionPane.showConfirmDialog(mainFrame, "Do you want to delete this donor?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    // Proceed to delete the donor
                    String deleteQuery = "DELETE FROM bloodbank WHERE name = ? AND bloodgroup = ?";
                    PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                    deleteStmt.setString(1, name);
                    deleteStmt.setString(2, bloodGroup);

                    int rowsDeleted = deleteStmt.executeUpdate();
                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(mainFrame, "Donor deleted successfully!");
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Failed to delete donor.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "No donor found with the provided details.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new DeleteDonor();
    }
}
