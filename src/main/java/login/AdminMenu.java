package login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminMenu extends JFrame
{
    public AdminMenu()
    {
        setTitle("Admin Dashboard - Library Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(false);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(13, 110, 253));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel headerLabel = new JLabel("Admin Dashboard");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerPanel.add(headerLabel, BorderLayout.WEST);

        JLabel subtitleLabel = new JLabel("Library Management System");
        subtitleLabel.setForeground(new Color(220, 220, 220));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(240, 242, 245));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Book Management Section
        contentPanel.add(createSectionPanel("Book Management", 
            new String[]{"Add Book", "Update Book", "View Books", "Delete Book", "Search Book"},
            new String[]{"book.AddBook", "book.UpdateBook", "book.ViewBooks", "book.DeleteBook", "book.SearchBook"},
            new Color(52, 152, 219)));

        contentPanel.add(Box.createVerticalStrut(15));

        // Member Management Section
        contentPanel.add(createSectionPanel("Member Management",
            new String[]{"Add Member", "Update Member", "View Members", "Search Member", "Deactivate Member", "Reactivate Member"},
            new String[]{"member.AddMember", "member.UpdateMember", "member.ViewMembers", "member.SearchMember", "member.DeactivateMember", "member.ReactivateMember"},
            new Color(46, 184, 92)));

        contentPanel.add(Box.createVerticalStrut(15));

        // Transaction Management Section
        contentPanel.add(createSectionPanel("Transaction Management",
            new String[]{"Issue Book", "Return Book", "View Issued Books", "View Returned Books", "View Fines"},
            new String[]{"transaction.IssueBook", "transaction.ReturnBook", "transaction.ViewIssuedBooks", "transaction.ViewReturnedBooks", "transaction.ViewFines"},
            new Color(155, 89, 182)));

        // Scroll pane for content
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(new Color(240, 242, 245));
        scrollPane.getViewport().setBackground(new Color(240, 242, 245));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(248, 249, 250));
        footerPanel.setPreferredSize(new Dimension(0, 50));
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(206, 212, 218)));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setPreferredSize(new Dimension(120, 35));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginPage();
        });
        footerPanel.add(logoutBtn);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createSectionPanel(String title, String[] buttonLabels, String[] classNames, Color sectionColor) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.WHITE);
        sectionPanel.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218), 1));
        sectionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(sectionColor);
        titlePanel.setPreferredSize(new Dimension(0, 40));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        sectionPanel.add(titlePanel);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton btn = new JButton(buttonLabels[i]);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            btn.setForeground(Color.WHITE);
            btn.setBackground(sectionColor);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setPreferredSize(new Dimension(140, 35));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            final String className = classNames[i];
            btn.addActionListener(e -> launchPage(className));
            buttonsPanel.add(btn);
        }

        sectionPanel.add(buttonsPanel);
        return sectionPanel;
    }

    private void launchPage(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            if (instance instanceof JFrame) {
                JFrame frame = (JFrame) instance;
                frame.setLocationRelativeTo(this);
                frame.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error opening page: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminMenu::new);
    }
}