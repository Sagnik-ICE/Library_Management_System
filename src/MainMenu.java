import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Library Management - Admin Panel");
        setSize(400, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Welcome, Admin");
        label.setBounds(120, 20, 200, 30);
        add(label);

        String[] buttons = {
                "Add Book", "View Books", "Search Book", "Update Book", "Delete Book",
                "Add Member", "View Members", "Search Member", "Update Member", "Delete Member",
                "Issue Book", "Return Book", "View Issued Books", "View Fines", "Logout"
        };

        int y = 60;
        for (String name : buttons) {
            JButton btn = new JButton(name);
            btn.setBounds(100, y, 200, 30);
            add(btn);

            // Button actions
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    switch (name) {
                        case "Add Book": new AddBook(); break;
                        case "View Books": new ViewBooks(); break;
                        case "Search Book": new SearchBook(); break;
                        case "Update Book": new UpdateBook(); break;
                        case "Delete Book": new DeleteBook(); break;

                        case "Add Member": new AddMember(); break;
                        case "View Members": new ViewMembers(); break;
                        case "Search Member": new SearchMember(); break;
                        case "Update Member": new UpdateMember(); break;
                        case "Deactivate Member": new DeactivateMember(); break;

                        case "Issue Book": new IssueBook(); break;
                        case "Return Book": new ReturnBook(); break;
                        case "View Issued Books": new ViewIssuedBooks(); break;
                        case "View Fines": new ViewFines(); break;

                        case "Logout":
                            dispose(); // Close current window
                            new LoginPage(); // Go back to login
                            break;
                    }
                }
            });

            y += 35;
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainMenu();
    }
}
