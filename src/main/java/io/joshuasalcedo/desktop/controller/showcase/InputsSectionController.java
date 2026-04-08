package io.joshuasalcedo.desktop.controller.showcase;

import io.joshuasalcedo.desktop.controller.showcase.ShowcaseUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
public class InputsSectionController {

    @FXML private Label sectionTitle;
    @FXML private VBox demoBox;
    @FXML private ToggleButton codeToggle;
    @FXML private VBox codeBox;

    private static final String CODE = """
            var text = new TextField();
            text.setPromptText("Text field...");

            var password = new PasswordField();
            password.setPromptText("Password...");

            var area = new TextArea();
            area.setPromptText("TextArea — multiline input...");
            area.setPrefRowCount(3);
            """;

    @FXML
    public void initialize() {
        sectionTitle.setText("Text Inputs");

        var container = new VBox(8);

        var text = new TextField();
        text.setPromptText("Text field...");

        var password = new PasswordField();
        password.setPromptText("Password...");

        var row = new HBox(8, text, password);

        var area = new TextArea();
        area.setPromptText("TextArea — multiline input...");
        area.setPrefRowCount(3);
        area.setPrefWidth(400);

        container.getChildren().addAll(row, area);
        demoBox.getChildren().add(container);

        codeBox.getChildren().add(ShowcaseUtils.createCodeArea(CODE));
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
