package ltc.milan;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsoleWindow extends JFrame {

    private JTextArea consoleArea;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");

    public ConsoleWindow() {
        setTitle("Virtual Fishlicker - Console");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        consoleArea.setBackground(Color.BLACK);
        consoleArea.setForeground(Color.CYAN);
        consoleArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(consoleArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(Color.DARK_GRAY);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearConsole());
        buttonPanel.add(clearButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        log("Console started, waiting for licker to start clicking");
    }

    public void log(String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = dateFormat.format(new Date());
            consoleArea.append("[" + timestamp + "] " + message + "\n");
            consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
        });
    }

    public void logError(String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = dateFormat.format(new Date());
            consoleArea.append("[" + timestamp + "] ERROR: " + message + "\n");
            consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
        });
    }

    public void clearConsole() {
        consoleArea.setText("");
        log("Console cleared.");
    }
}
