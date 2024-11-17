import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Home {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JPanel controlPanel;

    public Home() {
        prepareGUI();
        showButtonDemo();
    }

    private void prepareGUI() {

        mainFrame = new JFrame("Blood Bank Management System");
        mainFrame.setSize(1100, 700);
        mainFrame.setLayout(null); // Absolute layout for precise positioning
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Background image
        JLabel background = new JLabel(new ImageIcon(getClass().getResource("/images/Anime-4k-Wallpaper.jpg")));
        background.setBounds(0, 0, 1100, 700);
        mainFrame.add(background);

        // Header label
        headerLabel = new JLabel("<html><u>Blood Bank Management System !</u></html>", JLabel.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 32)); // Changed font to Serif
        headerLabel.setForeground(Color.BLACK); // Darker font color for visibility
        headerLabel.setBounds(50, 20, 1000, 50); // Adjusted position and size
        background.add(headerLabel);

        // Control panel
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 5, 20, 0)); // Buttons with spacing
        controlPanel.setBounds(100, 150, 500, 60);
        controlPanel.setOpaque(false); // Transparent background
        background.add(controlPanel);

        JButton cancelButton = createStyledButton("Logout");
        controlPanel.add(cancelButton);
        cancelButton.setBounds(350, 280, 100, 30);
        cancelButton.setBackground(new Color(100, 20, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new Login();
            }
        });
        background.add(cancelButton);

        mainFrame.setLocationRelativeTo(null); // Center the window
        mainFrame.setVisible(true);
    }

    private void showButtonDemo() {
        // Custom button style
        JButton[] buttons = {
                createStyledButton("Donors"),
                createStyledButton("Add Donor"),
                createStyledButton("Blood Donations"),
                createStyledButton("Update Donor"),
                createStyledButton("Delete Donor")
        };

        // Add buttons to panel
        for (JButton button : buttons) {
            controlPanel.add(button);
        }



        // Assign actions
        buttons[0].addActionListener(this::openDonorsForm);
        buttons[1].addActionListener(this::openAddDonorForm);
        buttons[2].addActionListener(this::openBloodDonationsForm);
        buttons[3].addActionListener(this::openUpdateDonorForm);
        buttons[4].addActionListener(this::openDeleteDonorForm);
    }

    // Custom button creation method
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(220, 20, 60)); // Crimson red
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 69, 0)); // Lighter red on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 20, 60)); // Original color
            }
        });

        return button;
    }

    // Action methods
    private void openDonorsForm(ActionEvent evt) {
        new Donors();
    }

    private void openBloodDonationsForm(ActionEvent evt) {
        new BloodDonated();
    }

    private void openAddDonorForm(ActionEvent evt) {
        new AddDonor();
    }

    private void openUpdateDonorForm(ActionEvent evt) {
        new UpdateDonor();
    }

    private void openDeleteDonorForm(ActionEvent evt) {
        new DeleteDonor();
    }

    public static void main(String[] args) {
        new Home();
    }
}
