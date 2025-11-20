package ltc.milan;

import javax.swing.*;
import java.awt.*;

import ltc.milan.AutoClicker;

public class AutoClickerGUI extends JFrame {

    private final AutoClicker autoClicker;
    private final ConsoleWindow consoleWindow;
    private JTextField intervalField;
    private JTextField windowField;
    private JButton startStopButton;
    private JButton consoleButton;
    private JLabel statusLabel;

    public AutoClickerGUI() {
        this.consoleWindow = new ConsoleWindow();
        this.autoClicker = new AutoClicker(consoleWindow);

        setTitle("Virtual Fishlicker by Milan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 280);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        JLabel intervalLabel = new JLabel("Interval (Seconds):");
        intervalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        intervalField = new JTextField("3.5");
        intervalField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        intervalField.setToolTipText("Enter the cooldown you have on virtual fisher");

        JLabel windowLabel = new JLabel("Priority Window:");
        windowLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        windowField = new JTextField("");
        windowField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        windowField.setToolTipText("leave empty if you want it to click everywhere, or type\nfor example 'discord.exe' so it only clicks if discord is prioritized");
        inputPanel.add(intervalLabel);
        inputPanel.add(intervalField);
        inputPanel.add(windowLabel);
        inputPanel.add(windowField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        startStopButton = new JButton("Start");
        startStopButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        startStopButton.setPreferredSize(new Dimension(150, 40));
        startStopButton.setFocusPainted(false);
        startStopButton.addActionListener(e -> toggleAutoClicker());
        buttonPanel.add(startStopButton);

        consoleButton = new JButton("Show Console");
        consoleButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        consoleButton.setPreferredSize(new Dimension(150, 40));
        consoleButton.setFocusPainted(false);
        consoleButton.addActionListener(e -> toggleConsole());
        buttonPanel.add(consoleButton);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusLabel = new JLabel("Status: Stopped");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(Color.RED);
        statusPanel.add(statusLabel);
        mainPanel.add(statusPanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }


    private void toggleAutoClicker() {
        if (!autoClicker.isRunning()) {
            startAutoClicker();
        } else {
            stopAutoClicker();
        }
    }

    private void startAutoClicker() {
        try {
            String intervalText = intervalField.getText().trim().replace(",", ".");
            double interval = Double.parseDouble(intervalText);

            if (interval <= 0) {
                showError("Interval has to be bigger than 0");
                return;
            }

            String priorityWindow = windowField.getText().trim();
            autoClicker.start(interval, priorityWindow.isEmpty() ? null : priorityWindow);

            startStopButton.setText("Stop");
            statusLabel.setText("State: Running");
            statusLabel.setForeground(Color.GREEN);

            intervalField.setEnabled(false);
            windowField.setEnabled(false);

        } catch (NumberFormatException e) {
            showError("Invalid interval, example of a correct one: '3.5'");
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void stopAutoClicker() {
        autoClicker.stop();

        startStopButton.setText("Start");
        statusLabel.setText("State: Stopped");
        statusLabel.setForeground(Color.RED);

        intervalField.setEnabled(true);
        windowField.setEnabled(true);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void toggleConsole() {
        if (consoleWindow.isVisible()) {
            consoleWindow.setVisible(false);
            consoleButton.setText("Show Console");
        } else {
            consoleWindow.setVisible(true);
            consoleButton.setText("Hide Console");
        }
    }
}
