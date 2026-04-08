package io.joshuasalcedo.desktop.controller.showcase;

import io.joshuasalcedo.desktop.controller.showcase.ShowcaseUtils;
import io.joshuasalcedo.desktop.service.NotificationService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.fxmisc.richtext.CodeArea;
import org.springframework.stereotype.Component;

@Component
public class NotificationsSectionController {

    @FXML private Label sectionTitle;
    @FXML private VBox demoBox;
    @FXML private ToggleButton codeToggle;
    @FXML private VBox codeBox;

    private final NotificationService notifications;

    public NotificationsSectionController(NotificationService notifications) {
        this.notifications = notifications;
    }

    private static final String CODE = """
            @Autowired NotificationService notifications;

            // In-app toast (ControlsFX, inside your window)
            notifications.info("Title", "Message");
            notifications.warn("Warning", "Something needs attention");
            notifications.error("Error", "Something went wrong");

            // OS-level (Windows toast / Linux notify / macOS banner)
            notifications.osInfo("Title", "Message");

            // Both at once
            notifications.notifyInfo("Title", "Message");""";

    @FXML
    public void initialize() {
        sectionTitle.setText("Notifications");

        var container = new HBox(8);

        var toastBtn = new Button("In-App Toast");
        toastBtn.setOnAction(e -> notifications.info("Showcase", "This is an in-app notification"));

        var osBtn = new Button("OS Notification");
        osBtn.setOnAction(e -> notifications.osInfo("Showcase", "This is a system notification"));

        var warnBtn = new Button("Warning");
        warnBtn.setOnAction(e -> notifications.warn("Warning", "Something needs attention"));

        var errorBtn = new Button("Error");
        errorBtn.setOnAction(e -> notifications.error("Error", "Something went wrong"));

        container.getChildren().addAll(toastBtn, osBtn, warnBtn, errorBtn);
        demoBox.getChildren().add(container);

        CodeArea codeArea = ShowcaseUtils.createCodeArea(CODE);
        codeBox.getChildren().add(codeArea);
        codeBox.setVisible(false);
        codeBox.setManaged(false);
    }

    @FXML
    private void toggleCode() {
        boolean show = codeToggle.isSelected();
        codeBox.setVisible(show);
        codeBox.setManaged(show);
        codeToggle.setText(show ? "Hide Code" : "Show Code");
    }
}
