package ltc.milan;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;

public class WindowUtil {

    private static final User32 user32 = User32.INSTANCE;
    private static final Kernel32 kernel32 = Kernel32.INSTANCE;

    public static String getActiveWindowName() {
        try {
            HWND hwnd = user32.GetForegroundWindow();

            if (hwnd == null) {
                return "Unknown";
            }

            char[] buffer = new char[512];
            user32.GetWindowText(hwnd, buffer, buffer.length);
            String windowTitle = Native.toString(buffer);

            IntByReference processId = new IntByReference();
            user32.GetWindowThreadProcessId(hwnd, processId);

            String processName = getProcessName(processId.getValue());

            return processName != null ? processName : windowTitle;

        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    private static String getProcessName(int processId) {
        try {
            HANDLE process = kernel32.OpenProcess(
                    0x0400 | 0x0010,
                    false,
                    processId
            );

            if (process == null) {
                return null;
            }

            char[] exePathBuffer = new char[512];
            IntByReference size = new IntByReference(exePathBuffer.length);

            if (kernel32.QueryFullProcessImageName(process, 0, exePathBuffer, size)) {
                String fullPath = Native.toString(exePathBuffer);
                String[] parts = fullPath.split("\\\\");
                kernel32.CloseHandle(process);
                return parts[parts.length - 1];
            }

            kernel32.CloseHandle(process);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isWindowActive(String windowName) {
        if (windowName == null || windowName.isEmpty()) {
            return true;
        }

        String activeWindow = getActiveWindowName();
        return activeWindow.toLowerCase().contains(windowName.toLowerCase());
    }
}