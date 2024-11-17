import java.awt.*;
import javax.swing.*;
import java.sql.*;

public class Login extends JFrame {
    JLabel idLabel;
    JLabel passLabel;
    JLabel headerLabel;
    JTextField idField;
    JPasswordField passField;
    JButton submitButton;

    public Login() {
        setTitle("Blood Bank Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLayout(null); // Use absolute positioning
        setLocationRelativeTo(null); // Center the window

        init(); // Initialize UI components
        setVisible(true);
    }

    public void init() {
        headerLabel = new JLabel("Blood Bank Management System", JLabel.CENTER);
        headerLabel.setBounds(150, 20, 400, 50);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(Color.RED);
        add(headerLabel);

        idLabel = new JLabel("Username:");
        idLabel.setBounds(190, 110, 100, 30);
        idLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(idLabel);

        passLabel = new JLabel("Password:");
        passLabel.setBounds(190, 160, 100, 30);
        passLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(passLabel);

        idField = new JTextField();
        idField.setBounds(300, 110, 200, 30);
        add(idField);

        passField = new JPasswordField();
        passField.setBounds(300, 160, 200, 30);
        add(passField);

        submitButton = new JButton("Login");
        submitButton.setBounds(300, 220, 100, 30);
        submitButton.addActionListener(this::submitActionPerformed);
        add(submitButton);
    }

    public void submitActionPerformed(java.awt.event.ActionEvent evt) {
        String login = idField.getText();
        String pass = new String(passField.getPassword());

        String query = "SELECT * FROM admin WHERE login_id = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, login);
            pstmt.setString(2, pass);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    this.dispose(); // Close the login window
                    new Home();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
    public static void main(String[] args) {
        new Login();
    }
}
