import javax.swing.*;
import java.awt.*;
import java.util.*;

class Email {
    String recipient;
    String message;
    String status;

    Email(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;
        this.status = "Pending";
    }
}

public class EmailSystem extends JFrame {

    DefaultListModel<String> inboxModel = new DefaultListModel<>();
    java.util.List<Email> emails = new ArrayList<>();

    JTextField recipientField;
    JTextArea messageArea;
    JLabel statusBar;
    JLabel statsLabel;

    boolean demoMode = true; // toggle between demo and real simulation

    public EmailSystem() {

        setTitle("MailFlow - Intelligent Email Delivery System");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== SIDEBAR (Inbox) =====
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(300, 0));

        JLabel inboxLabel = new JLabel("Inbox");
        inboxLabel.setFont(new Font("Arial", Font.BOLD, 16));
        inboxLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JList<String> inboxList = new JList<>(inboxModel);
        inboxList.setFont(new Font("Arial", Font.PLAIN, 13));

        sidebar.add(inboxLabel, BorderLayout.NORTH);
        sidebar.add(new JScrollPane(inboxList), BorderLayout.CENTER);

        // ===== RIGHT PANEL =====
        JPanel rightPanel = new JPanel(new BorderLayout());

        JPanel composePanel = new JPanel(new BorderLayout());
        composePanel.setBorder(BorderFactory.createTitledBorder("Compose Email"));

        JPanel fields = new JPanel(new GridLayout(2,1,5,5));

        recipientField = new JTextField();
        messageArea = new JTextArea(8, 20);

        fields.add(new JLabel("Recipient Email"));
        fields.add(recipientField);

        composePanel.add(fields, BorderLayout.NORTH);
        composePanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        // ===== BUTTONS =====
        JPanel buttons = new JPanel();

        JButton addBtn = new JButton("Save Draft");
        JButton sendBtn = new JButton("Send Emails");
        JButton modeBtn = new JButton("Switch Mode");

        addBtn.setBackground(new Color(66, 133, 244));
        addBtn.setForeground(Color.WHITE);

        sendBtn.setBackground(new Color(52, 168, 83));
        sendBtn.setForeground(Color.WHITE);

        modeBtn.setBackground(Color.GRAY);
        modeBtn.setForeground(Color.WHITE);

        buttons.add(addBtn);
        buttons.add(sendBtn);
        buttons.add(modeBtn);

        composePanel.add(buttons, BorderLayout.SOUTH);

        // ===== DASHBOARD =====
        JPanel dashboard = new JPanel();
        dashboard.setBorder(BorderFactory.createTitledBorder("System Overview"));

        statsLabel = new JLabel("Total: 0 | Sent: 0 | Pending: 0");
        dashboard.add(statsLabel);

        rightPanel.add(composePanel, BorderLayout.CENTER);
        rightPanel.add(dashboard, BorderLayout.SOUTH);

        // ===== STATUS BAR =====
        statusBar = new JLabel(" Welcome to MailFlow");
        statusBar.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

        add(sidebar, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        // ===== ACTIONS =====
        addBtn.addActionListener(e -> addEmail());
        sendBtn.addActionListener(e -> processEmails());
        modeBtn.addActionListener(e -> toggleMode());

        setVisible(true);
    }

    void addEmail() {
        String recipient = recipientField.getText();
        String message = messageArea.getText();

        if (recipient.isEmpty() || message.isEmpty()) {
            statusBar.setText(" Please fill in all fields before saving.");
            return;
        }

        Email email = new Email(recipient, message);
        emails.add(email);

        inboxModel.addElement("Pending  |  " + recipient);

        recipientField.setText("");
        messageArea.setText("");

        updateStats();
        statusBar.setText(" Email saved as draft.");
    }

    void processEmails() {

        inboxModel.clear();
        Random rand = new Random();

        for (Email email : emails) {

            if (demoMode) {
                email.status = "Sent";
            } else {
                boolean success = rand.nextBoolean();
                email.status = success ? "Sent" : "Failed";
            }

            inboxModel.addElement(email.status + "  |  " + email.recipient);
        }

        updateStats();

        if (demoMode) {
            statusBar.setText(" All emails sent successfully (Demo Mode).");
        } else {
            statusBar.setText(" Emails processed with real simulation.");
        }
    }

    void updateStats() {
        int sent = 0, pending = 0;

        for (Email e : emails) {
            if (e.status.equals("Sent")) sent++;
            else if (e.status.equals("Pending")) pending++;
        }

        statsLabel.setText("Total: " + emails.size() + 
                           " | Sent: " + sent + 
                           " | Pending: " + pending);
    }

    void toggleMode() {
        demoMode = !demoMode;

        if (demoMode) {
            statusBar.setText(" Switched to Demo Mode (All emails succeed).");
        } else {
            statusBar.setText(" Switched to Real Mode (Simulated failures).");
        }
    }

    public static void main(String[] args) {
        new EmailSystem();
    }
}