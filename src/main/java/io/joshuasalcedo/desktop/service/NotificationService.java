package io.joshuasalcedo.desktop.service;

import com.sshtools.twoslices.Toast;
import com.sshtools.twoslices.ToastType;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Unified notification service with two delivery targets:
 * <ul>
 *   <li><b>OS-level</b> — native system notifications via two-slices (Windows toast, Linux notify, macOS banner)</li>
 *   <li><b>In-app</b> — overlay toasts inside the JavaFX window via ControlsFX</li>
 * </ul>
 *
 * All methods are safe to call from any thread.
 */
@Component
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static final Duration DEFAULT_DURATION = Duration.seconds(5);

    private String appName = System.getProperty("app.name", "Application");

    // ─────────────────────────────────────────────
    // OS-level notifications (system tray / notification center)
    // ─────────────────────────────────────────────

    public void osInfo(String title, String message) {
        os(ToastType.INFO, title, message);
    }

    public void osWarn(String title, String message) {
        os(ToastType.WARNING, title, message);
    }

    public void osError(String title, String message) {
        os(ToastType.ERROR, title, message);
    }

    public void osInfo(String message) {
        os(ToastType.INFO, appName, message);
    }

    public void osWarn(String message) {
        os(ToastType.WARNING, appName, message);
    }

    public void osError(String message) {
        os(ToastType.ERROR, appName, message);
    }

    private void os(ToastType type, String title, String message) {
        try {
            Toast.toast(type, title, message);
            logger.debug("OS notification [{}]: {} - {}", type, title, message);
        } catch (Exception e) {
            logger.warn("Failed to show OS notification: {}", e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // In-app toast notifications (ControlsFX overlay)
    // ─────────────────────────────────────────────

    public void info(String title, String message) {
        toast(title, message, Level.INFO);
    }

    public void warn(String title, String message) {
        toast(title, message, Level.WARN);
    }

    public void error(String title, String message) {
        toast(title, message, Level.ERROR);
    }

    public void confirm(String title, String message) {
        toast(title, message, Level.CONFIRM);
    }

    public void info(String message) {
        info(null, message);
    }

    public void warn(String message) {
        warn(null, message);
    }

    public void error(String message) {
        error(null, message);
    }

    public void confirm(String message) {
        confirm(null, message);
    }

    private void toast(String title, String message, Level level) {
        Platform.runLater(() -> {
            // Find the focused window so the toast renders INSIDE the app, not as an OS popup
            Window owner = Window.getWindows().stream()
                    .filter(Window::isFocused)
                    .findFirst()
                    .orElse(null);

            var notification = Notifications.create()
                    .title(title != null ? title : "")
                    .text(message)
                    .position(Pos.BOTTOM_RIGHT)
                    .hideAfter(DEFAULT_DURATION);

            if (owner != null) {
                notification.owner(owner);
            }

            switch (level) {
                case INFO -> notification.showInformation();
                case WARN -> notification.showWarning();
                case ERROR -> notification.showError();
                case CONFIRM -> notification.showConfirm();
            }

            logger.debug("In-app notification [{}]: {} - {}", level, title, message);
        });
    }

    // ─────────────────────────────────────────────
    // Both — OS + in-app at the same time
    // ─────────────────────────────────────────────

    public void notifyInfo(String title, String message) {
        osInfo(title, message);
        info(title, message);
    }

    public void notifyWarn(String title, String message) {
        osWarn(title, message);
        warn(title, message);
    }

    public void notifyError(String title, String message) {
        osError(title, message);
        error(title, message);
    }

    private enum Level { INFO, WARN, ERROR, CONFIRM }
}
