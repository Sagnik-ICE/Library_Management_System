import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;


public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPage() {
        setTitle("Login Page");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 180, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 90, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 90, 180, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 140, 100, 30);
        add(loginButton);

        // Enter key triggers login
        Action loginAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        };
        usernameField.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);
        loginButton.addActionListener(loginAction);

        setVisible(true);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND status = 'active'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                JOptionPane.showMessageDialog(this, "Login Successful as " + role.toUpperCase());

                dispose(); // Close login window

                if (role.equals("admin")) {
                    new AdminMenu();
                } else {
                    new UserMenu(rs.getString("username")); // ✅ pass string instead of int

                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials or inactive account.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
