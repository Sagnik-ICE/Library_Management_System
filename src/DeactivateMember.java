import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class DeactivateMember extends JFrame {
    private JTextField memberIdField;
    private JButton deactivateButton;

    public DeactivateMember() {
        setTitle("Deactivate Member");
        setSize(350, 200);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Member ID:");
        label.setBounds(30, 40, 100, 25);
        add(label);

        memberIdField = new JTextField();
        memberIdField.setBounds(120, 40, 150, 25);
        add(memberIdField);

        deactivateButton = new JButton("Deactivate");
        deactivateButton.setBounds(100, 90, 120, 30);
        add(deactivateButton);

        deactivateButton.addActionListener(e -> deactivateMember());

        setVisible(true);
    }

    private void deactivateMember() {
        String memberId = memberIdField.getText().trim();

        if (memberId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Member ID.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Check if any books are still issued
            String checkIssued = "SELECT COUNT(*) FROM issued_books WHERE member_id = ? AND return_date IS NULL";
            PreparedStatement pstmt1 = conn.prepareStatement(checkIssued);
            pstmt1.setInt(1, Integer.parseInt(memberId));
            ResultSet rs1 = pstmt1.executeQuery();
            rs1.next();
            if (rs1.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Cannot deactivate: Books still issued.");
                return;
            }

            // Check if any fines are pending
            String checkFines = "SELECT SUM(amount) FROM fines WHERE member_id = ? AND paid = FALSE";
            PreparedStatement pstmt2 = conn.prepareStatement(checkFines);
            pstmt2.setInt(1, Integer.parseInt(memberId));
            ResultSet rs2 = pstmt2.executeQuery();
            if (rs2.next() && rs2.getDouble(1) > 0) {
                JOptionPane.showMessageDialog(this, "Cannot deactivate: Fines pending.");
                return;
            }

            // Deactivate member
            String deactivateSQL = "UPDATE members SET status = 'inactive' WHERE id = ?";
            PreparedStatement pstmt3 = conn.prepareStatement(deactivateSQL);
            pstmt3.setInt(1, Integer.parseInt(memberId));
            int rows = pstmt3.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Member deactivated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No matching member found.");
            }

        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new DeactivateMember();
    }
}
