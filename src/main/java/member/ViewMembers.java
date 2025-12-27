package member;

import database.DBConnection;
import utils.Constants;
import models.Member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewMembers extends JFrame {
    private JTextField searchField;
    private JComboBox<String> statusFilterCombo;
    private JTable membersTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JButton refreshButton;
    
    private final String[] columnNames = {"ID", "Unique ID", "First Name", "Last Name", "Email", "Contact", "Gender", "DOB", "Status"};

    public ViewMembers() {
        setTitle("View Members - Library Management System");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        createHeader();
        createSearchPanel();
        createTablePanel();
        createFooter();
        
        loadMembers();
        setVisible(true);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 135, 84)); // Green for members
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("View Members");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Browse and manage library members");
        subtitleLabel.setForeground(new Color(200, 255, 220));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(subtitleLabel);
        
        headerPanel.add(textPanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        searchPanel.setBackground(new Color(248, 249, 250));
        searchPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(222, 226, 230)));
        
        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchPanel.add(searchLabel);
        
        searchField = new JTextField(30);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setToolTipText("Search by name, email, or contact...");
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterMembers();
            }
        });
        searchPanel.add(searchField);
        
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchPanel.add(statusLabel);
        
        String[] statusOptions = {"All", "Active", "Inactive"};
        statusFilterCombo = new JComboBox<>(statusOptions);
        statusFilterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusFilterCombo.setPreferredSize(new Dimension(120, 30));
        statusFilterCombo.addActionListener(e -> filterMembers());
        searchPanel.add(statusFilterCombo);
        
        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(25, 135, 84));
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> loadMembers());
        searchPanel.add(refreshButton);
        
        add(searchPanel, BorderLayout.NORTH);
        
        JPanel combinedPanel = new JPanel(new BorderLayout());
        JPanel header = (JPanel) getContentPane().getComponent(0);
        combinedPanel.add(header, BorderLayout.NORTH);
        combinedPanel.add(searchPanel, BorderLayout.SOUTH);
        add(combinedPanel, BorderLayout.NORTH);
    }

    private void createTablePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        membersTable = new JTable(tableModel);
        membersTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        membersTable.setRowHeight(25);
        membersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        membersTable.setGridColor(new Color(222, 226, 230));
        membersTable.setShowGrid(true);
        
        JTableHeader header = membersTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(25, 135, 84));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        
        membersTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        membersTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        membersTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        membersTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        membersTable.getColumnModel().getColumn(4).setPreferredWidth(180);
        membersTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        membersTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        membersTable.getColumnModel().getColumn(7).setPreferredWidth(100);
        membersTable.getColumnModel().getColumn(8).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(membersTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }

    private void createFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(248, 249, 250));
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(222, 226, 230)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        statusLabel = new JLabel("Loading members...");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(108, 117, 125));
        footerPanel.add(statusLabel, BorderLayout.WEST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton viewDetailsButton = new JButton("📋 View Details");
        viewDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewDetailsButton.setForeground(Color.WHITE);
        viewDetailsButton.setBackground(new Color(13, 110, 253));
        viewDetailsButton.setFocusPainted(false);
        viewDetailsButton.setBorderPainted(false);
        viewDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewDetailsButton.addActionListener(e -> viewMemberDetails());
        buttonPanel.add(viewDetailsButton);
        
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(108, 117, 125));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        footerPanel.add(buttonPanel, BorderLayout.EAST);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void loadMembers() {
        SwingWorker<List<Object[]>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Object[]> doInBackground() {
                List<Object[]> members = new ArrayList<>();
                try (Connection conn = DBConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM members ORDER BY first_name, last_name")) {
                    
                    while (rs.next()) {
                        members.add(new Object[]{
                                rs.getInt("id"),
                                rs.getString("unique_member_id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("email"),
                                rs.getString("contact_number"),
                                rs.getString("gender"),
                                rs.getDate("date_of_birth"),
                                rs.getString("status")
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return members;
            }
            
            @Override
            protected void done() {
                try {
                    List<Object[]> members = get();
                    tableModel.setRowCount(0);
                    for (Object[] member : members) {
                        tableModel.addRow(member);
                    }
                    statusLabel.setText(members.size() + " member(s) found");
                } catch (Exception e) {
                    e.printStackTrace();
                    statusLabel.setText("Error loading members");
                }
            }
        };
        worker.execute();
    }

    private void filterMembers() {
        String searchText = searchField.getText().toLowerCase().trim();
        String statusFilter = (String) statusFilterCombo.getSelectedItem();
        
        SwingWorker<List<Object[]>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Object[]> doInBackground() {
                List<Object[]> members = new ArrayList<>();
                StringBuilder query = new StringBuilder("SELECT * FROM members WHERE 1=1");
                
                if (!"All".equals(statusFilter)) {
                    query.append(" AND status = '").append(statusFilter.toLowerCase()).append("'");
                }
                
                query.append(" ORDER BY first_name, last_name");
                
                try (Connection conn = DBConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(query.toString())) {
                    
                    while (rs.next()) {
                        if (searchText.isEmpty() || 
                            rs.getString("first_name").toLowerCase().contains(searchText) ||
                            rs.getString("last_name").toLowerCase().contains(searchText) ||
                            rs.getString("email").toLowerCase().contains(searchText) ||
                            rs.getString("contact_number").contains(searchText)) {
                            
                            members.add(new Object[]{
                                    rs.getInt("id"),
                                    rs.getString("unique_member_id"),
                                    rs.getString("first_name"),
                                    rs.getString("last_name"),
                                    rs.getString("email"),
                                    rs.getString("contact_number"),
                                    rs.getString("gender"),
                                    rs.getDate("date_of_birth"),
                                    rs.getString("status")
                            });
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return members;
            }
            
            @Override
            protected void done() {
                try {
                    List<Object[]> members = get();
                    tableModel.setRowCount(0);
                    for (Object[] member : members) {
                        tableModel.addRow(member);
                    }
                    statusLabel.setText(members.size() + " member(s) found");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void viewMemberDetails() {
        int selectedRow = membersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member to view details", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int memberId = (int) tableModel.getValueAt(selectedRow, 0);
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM members WHERE id = ?")) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    StringBuilder details = new StringBuilder();
                    details.append("═══════════════════════════════════════\n");
                    details.append("          MEMBER DETAILS\n");
                    details.append("═══════════════════════════════════════\n\n");
                    details.append("👤 Member ID: ").append(rs.getInt("id")).append("\n");
                    details.append("🔖 Unique ID: ").append(rs.getString("unique_member_id")).append("\n");
                    details.append("👨 First Name: ").append(rs.getString("first_name")).append("\n");
                    details.append("👨 Last Name: ").append(rs.getString("last_name")).append("\n");
                    details.append("📧 Email: ").append(rs.getString("email")).append("\n");
                    details.append("📱 Contact: ").append(rs.getString("contact_number")).append("\n");
                    details.append("🏠 Address: ").append(rs.getString("address")).append("\n");
                    details.append("⚧ Gender: ").append(rs.getString("gender")).append("\n");
                    details.append("🎂 DOB: ").append(rs.getDate("date_of_birth")).append("\n");
                    details.append("📊 Status: ").append(rs.getString("status").toUpperCase()).append("\n");
                    
                    JTextArea textArea = new JTextArea(details.toString());
                    textArea.setEditable(false);
                    textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(400, 300));
                    
                    JOptionPane.showMessageDialog(this, scrollPane, "Member Details", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading member details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewMembers());
    }
}
