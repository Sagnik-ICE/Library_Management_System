package login;

import database.DBConnection;
import utils.PasswordUtil;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame
{
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JCheckBox showPasswordCheck;

    public LoginPage() {
        setTitle("Library Management System");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main container with gradient background
        JPanel mainContainer = new JPanel(new GridLayout(1, 2, 0, 0));
        
        // Left Panel - Branding
        JPanel leftPanel = createBrandingPanel();
        
        // Right Panel - Login Form
        JPanel rightPanel = createLoginPanel();
        
        mainContainer.add(leftPanel);
        mainContainer.add(rightPanel);
        
        add(mainContainer);
        setVisible(true);
        
        // Focus on username field
        SwingUtilities.invokeLater(() -> usernameField.requestFocus());
    }
    
    private JPanel createBrandingPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                
                // Gradient background
                GradientPaint gp = new GradientPaint(0, 0, new Color(41, 128, 185), w, h, new Color(109, 213, 250));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 40, 10, 40);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Icon/Logo placeholder
        JLabel logoLabel = new JLabel("LMS");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 72));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.WHITE, 3, true),
            BorderFactory.createEmptyBorder(20, 40, 20, 40)
        ));
        panel.add(logoLabel, gbc);
        
        // Title
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(30, 40, 5, 40);
        panel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Efficient. Organized. Smart.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        gbc.insets = new Insets(5, 40, 40, 40);
        panel.add(subtitleLabel, gbc);
        
        return panel;
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 50, 10, 50);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(44, 62, 80));
        gbc.insets = new Insets(40, 50, 5, 50);
        panel.add(welcomeLabel, gbc);
        
        JLabel instructionLabel = new JLabel("Sign in to continue");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instructionLabel.setForeground(new Color(127, 140, 141));
        gbc.insets = new Insets(0, 50, 30, 50);
        panel.add(instructionLabel, gbc);
        
        // Username field
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userLabel.setForeground(new Color(52, 73, 94));
        gbc.insets = new Insets(10, 50, 5, 50);
        panel.add(userLabel, gbc);
        
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(300, 40));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        usernameField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                usernameField.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(52, 152, 219), 2, true),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
            public void focusLost(FocusEvent e) {
                usernameField.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(189, 195, 199), 1, true),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
        });
        gbc.insets = new Insets(0, 50, 15, 50);
        panel.add(usernameField, gbc);
        
        // Password field
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passLabel.setForeground(new Color(52, 73, 94));
        gbc.insets = new Insets(10, 50, 5, 50);
        panel.add(passLabel, gbc);
        
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        passwordField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(52, 152, 219), 2, true),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
            public void focusLost(FocusEvent e) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(189, 195, 199), 1, true),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
        });
        passwordField.addActionListener(e -> login());
        gbc.insets = new Insets(0, 50, 10, 50);
        panel.add(passwordField, gbc);
        
        // Show password checkbox
        showPasswordCheck = new JCheckBox("Show Password");
        showPasswordCheck.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPasswordCheck.setForeground(new Color(127, 140, 141));
        showPasswordCheck.setBackground(Color.WHITE);
        showPasswordCheck.setFocusPainted(false);
        showPasswordCheck.addActionListener(e -> {
            if (showPasswordCheck.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });
        gbc.insets = new Insets(0, 50, 20, 50);
        panel.add(showPasswordCheck, gbc);
        
        // Login button
        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setPreferredSize(new Dimension(300, 45));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(52, 152, 219));
            }
        });
        loginButton.addActionListener(e -> login());
        gbc.insets = new Insets(10, 50, 10, 50);
        panel.add(loginButton, gbc);
        
        // Cancel button
        cancelButton = new JButton("CANCEL");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cancelButton.setForeground(new Color(127, 140, 141));
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setPreferredSize(new Dimension(300, 40));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(new LineBorder(new Color(189, 195, 199), 1, true));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                cancelButton.setBackground(new Color(245, 245, 245));
            }
            public void mouseExited(MouseEvent e) {
                cancelButton.setBackground(Color.WHITE);
            }
        });
        cancelButton.addActionListener(e -> System.exit(0));
        gbc.insets = new Insets(5, 50, 40, 50);
        panel.add(cancelButton, gbc);
        
        return panel;
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Password are required.");
            return;
        }

        String selectSql = "SELECT password, role, status FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSql)) {

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    String role = rs.getString("role");
                    String status = rs.getString("status");

                    if (status != null && status.equalsIgnoreCase("inactive")) {
                        JOptionPane.showMessageDialog(this, "Account is inactive. Contact administrator.");
                        return;
                    }

                    boolean valid = false;
                    if (storedPassword != null && storedPassword.startsWith("$2")) {
                        valid = PasswordUtil.verifyPassword(password, storedPassword);
                    } else {
                        valid = storedPassword != null && storedPassword.equals(password);
                        if (valid) {
                            String newHash = PasswordUtil.hashPassword(password);
                            String updateSql = "UPDATE users SET password = ? WHERE username = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                                updateStmt.setString(1, newHash);
                                updateStmt.setString(2, username);
                                updateStmt.executeUpdate();
                            }
                        }
                    }

                    if (valid) {
                        dispose();
                        if ("admin".equalsIgnoreCase(role)) {
                            new AdminMenu();
                        } else {
                            new UserMenu(username);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid username or password.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
