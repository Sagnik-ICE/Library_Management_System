package com.librarymgmt.ui.admin;

import com.librarymgmt.ui.auth.LoginPage;
import javax.swing.*;
import java.awt.*;

public class AdminMenu extends JFrame {

    public AdminMenu() {
        setTitle("Admin Menu");
        setSize(420, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- Content Panel with absolute layout ---
        JPanel contentPanel = new JPanel(null);
        contentPanel.setPreferredSize(new Dimension(400, 700)); // vertical space only

        // --- Title ---
        JLabel title = new JLabel("Library Admin Dashboard");
        title.setBounds(110, 20, 200, 30);
        contentPanel.add(title);

        // --- Book Management ---
        int y = 60;
        int spacing = 40;

        JButton addBookBtn = new JButton("Add Book");
        addBookBtn.setBounds(120, y, 150, 30);
        contentPanel.add(addBookBtn);

        JButton searchBookBtn = new JButton("Search Book");
        searchBookBtn.setBounds(120, y += spacing, 150, 30);
        contentPanel.add(searchBookBtn);

        JButton viewBooksBtn = new JButton("View All Books");
        viewBooksBtn.setBounds(120, y += spacing, 150, 30);
        contentPanel.add(viewBooksBtn);

        JButton updateBookBtn = new JButton("Update Book");
        updateBookBtn.setBounds(120, y += spacing, 150, 30);
        contentPanel.add(updateBookBtn);

        JButton deleteBookBtn = new JButton("Delete Book");
        deleteBookBtn.setBounds(120, y += spacing, 150, 30);
        contentPanel.add(deleteBookBtn);

        // --- Member Management ---
        JButton addMemberBtn = new JButton("Add Member");
        addMemberBtn.setBounds(120, y += spacing, 150, 30);
        contentPanel.add(addMemberBtn);

        JButton viewMembersBtn = new JButton("View Members");
        viewMembersBtn.setBounds(120, y += spacing, 150, 30);
        contentPanel.add(viewMembersBtn);

        JButton searchMemberBtn = new JButton("Search Member");
        searchMemberBtn.setBounds(120, y += spacing, 150, 30);
        contentPanel.add(searchMemberBtn);

        JButton updateMemberBtn = new JButton("Update Member");
        updateMemberBtn.setBounds(120, y += spacing, 150, 30);
        contentPanel.add(updateMemberBtn);

        JButton deactivateMemberBtn = new JButton("Deactivate Member");
        deactivateMemberBtn.setBounds(120, y += spacing, 150, 30);
        contentPanel.add(deactivateMemberBtn);

        JButton reactivateMemberBtn = new JButton("Reactivate Member");
        reactivateMemberBtn.setBounds(120, y += spacing, 150, 30);
        contentPanel.add(reactivateMemberBtn);

        // --- Book Issue / Return / Fines ---
        JButton issueBookBtn = new JButton("Issue Book");
        issueBookBtn.setBounds(20, y += spacing, 150, 30);
        contentPanel.add(issueBookBtn);

        JButton returnBookBtn = new JButton("Return Book");
        returnBookBtn.setBounds(220, y, 150, 30);
        contentPanel.add(returnBookBtn);

        JButton viewFinesBtn = new JButton("View Fines");
        viewFinesBtn.setBounds(20, y += spacing, 150, 30);
        contentPanel.add(viewFinesBtn);

        JButton viewIssuedBtn = new JButton("View Issued Books");
        viewIssuedBtn.setBounds(220, y, 150, 30);
        contentPanel.add(viewIssuedBtn);

        // --- Logout ---
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(120, y += spacing + 20, 150, 30);
        contentPanel.add(logoutBtn);

        // --- Scroll Pane (vertical only) ---
        JScrollPane scrollPane = new JScrollPane(contentPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);

        // --- Action Listeners ---
        addBookBtn.addActionListener(e -> new AddBook());
        searchBookBtn.addActionListener(e -> new SearchBook());
        viewBooksBtn.addActionListener(e -> new ViewBooks());
        updateBookBtn.addActionListener(e -> new UpdateBook());
        deleteBookBtn.addActionListener(e -> new DeleteBook());

        addMemberBtn.addActionListener(e -> new AddMember());
        viewMembersBtn.addActionListener(e -> new ViewMembers());
        searchMemberBtn.addActionListener(e -> new SearchMember());
        updateMemberBtn.addActionListener(e -> new UpdateMember());
        deactivateMemberBtn.addActionListener(e -> new DeactivateMember());
        reactivateMemberBtn.addActionListener(e -> new ReactivateMember());

        issueBookBtn.addActionListener(e -> new IssueBook());
        returnBookBtn.addActionListener(e -> new ReturnBook());
        viewFinesBtn.addActionListener(e -> new ViewFines());
        viewIssuedBtn.addActionListener(e -> new ViewIssuedBooks());

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new AdminMenu();
    }
}
