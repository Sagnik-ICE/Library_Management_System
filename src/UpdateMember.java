import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdateMember extends JFrame {
    private JTextField idField, nameField, emailField, contactField, usernameField;
    private JPasswordField passwordField;
    private JButton searchButton, updateButton;

    public UpdateMember() {
        setTitle("Update Member");
        setSize(450, 450);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel idLabel = new JLabel("Enter Member ID:");
        idLabel.setBounds(30, 30, 120, 25);
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(160, 30, 200, 25);
        add(idField);

        searchButton = new JButton("Search");
        searchButton.setBounds(160, 65, 100, 30);
        add(searchButton);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(30, 110, 100, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(160, 110, 200, 25);
        add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 150, 100, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(160, 150, 200, 25);
        add(emailField);

        JLabel contactLabel = new JLabel("Contact:");
        contactLabel.setBounds(30, 190, 100, 25);
        add(contactLabel);

        contactField = new JTextField();
        contactField.setBounds(160, 190, 200, 25);
        add(contactField);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 230, 100, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(160, 230, 200, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 270, 100, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 270, 200, 25);
        add(passwordField);

        updateButton = new JButton("Update Member");
        updateButton.setBounds(140, 320, 160, 35);
        add(updateButton);

        // Disable fields initially
        setFieldsEnabled(false);

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchMember();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateMember();
            }
        });

        setVisible(true);
    }

    private void setFieldsEnabled(boolean state) {
        nameField.setEnabled(state);
        emailField.setEnabled(state);
        contactField.setEnabled(state);
        usernameField.setEnabled(state);
        passwordField.setEnabled(state);
        updateButton.setEnabled(state);
    }

    private void searchMember() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT m.name, m.email, m.contact, u.username, u.password " +
                    "FROM members m LEFT JOIN users u ON m.id = u.member_id WHERE m.id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(idField.getText()));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                emailField.setText(rs.getString("email"));
                contactField.setText(rs.getString("contact"));

                String username = rs.getString("username");
                String password = rs.getString("password");

                usernameField.setText(username != null ? username : "");
                passwordField.setText(password != null ? password : "");

                setFieldsEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Member not found.");
                setFieldsEnabled(false);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }


    private void updateMember() {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // transaction start

            String updateMemberSql = "UPDATE members SET name = ?, email = ?, contact = ? WHERE id = ?";
            PreparedStatement ps1 = conn.prepareStatement(updateMemberSql);
            ps1.setString(1, nameField.getText());
            ps1.setString(2, emailField.getText());
            ps1.setString(3, contactField.getText());
            ps1.setInt(4, Integer.parseInt(idField.getText()));
            ps1.executeUpdate();

            String updateUserSql = "UPDATE users SET username = ?, password = ? WHERE member_id = ?";
            PreparedStatement ps2 = conn.prepareStatement(updateUserSql);
            ps2.setString(1, usernameField.getText());
            ps2.setString(2, new String(passwordField.getPassword()));
            ps2.setInt(3, Integer.parseInt(idField.getText()));
            ps2.executeUpdate();

            conn.commit(); // commit transaction

            JOptionPane.showMessageDialog(this, "Member and login details updated successfully!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new UpdateMember();
    }
}
