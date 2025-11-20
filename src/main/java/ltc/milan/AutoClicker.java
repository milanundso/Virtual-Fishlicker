package ltc.milan;

import ltc.milan.WindowUtil;

import java.awt.*;
import java.awt.event.InputEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AutoClicker {

    private Robot robot;
    private Timer timer;
    private boolean running;
    private long intervalMs;
    private String priorityWindow;
    private ConsoleWindow consoleWindow;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");

    public AutoClicker(ConsoleWindow consoleWindow) {
        try {
            this.robot = new Robot();
            this.running = false;
            this.consoleWindow = consoleWindow;
        } catch (AWTException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not create robot", e);
        }
    }

    public void start(double intervalSeconds, String priorityWindow) {
        if (running) {
            stop();
        }

        double adjustedInterval = intervalSeconds + 0.3;
        this.intervalMs = (long) (adjustedInterval * 1000);
        this.priorityWindow = priorityWindow;
        this.running = true;

        timer = new Timer("AutoClicker-Timer", true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                performClick();
            }
        }, 5000, intervalMs);

    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        running = false;
    }

    private void performClick() {
        try {
            if (priorityWindow != null && !priorityWindow.trim().isEmpty()) {
                if (!WindowUtil.isWindowActive(priorityWindow)) {
                    return;
                }
            }

            Point mousePos = MouseInfo.getPointerInfo().getLocation();

            robot.mousePress(InputEvent.BUTTON1_MASK);

            Thread.sleep(10);

            robot.mouseRelease(InputEvent.BUTTON1_MASK);

            String activeWindow = WindowUtil.getActiveWindowName();
            consoleWindow.log("Successfully clicked in the window " + activeWindow + "!");

        } catch (Exception e) {
            consoleWindow.logError(e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setPriorityWindow(String priorityWindow) {
        this.priorityWindow = priorityWindow;
    }

    public String getPriorityWindow() {
        return priorityWindow;
    }
}