package io.joshuasalcedo.desktop.controller.showcase;

import io.joshuasalcedo.desktop.controller.showcase.ShowcaseUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import org.fxmisc.richtext.CodeArea;
import org.springframework.stereotype.Component;

@Component
public class TypographySectionController {

    @FXML private Label sectionTitle;
    @FXML private VBox demoBox;
    @FXML private ToggleButton codeToggle;
    @FXML private VBox codeBox;

    private static final String CODE = """
            var title = new Label("Page Title");
            title.setStyle("-fx-font-size: 18px; -fx-font-weight: 600;");

            var body = new Label("Body text at 13px");
            body.setStyle("-fx-font-size: 13px;");

            var caption = new Label("Caption at 11px");
            caption.setStyle("-fx-font-size: 11px; -fx-text-fill: -color-fg-subtle;");""";

    @FXML
    public void initialize() {
        sectionTitle.setText("Labels & Typography");

        var container = new VBox(6);

        var pageTitle = new Label("Page Title (18px SemiBold)");
        pageTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: 600;");

        var sectionHeader = new Label("Section Header (15px Medium)");
        sectionHeader.setStyle("-fx-font-size: 15px; -fx-font-weight: 500;");

        var body = new Label("Body text at 13px \u2014 the desktop default. Readable and compact.");
        body.setStyle("-fx-font-size: 13px;");

        var secondary = new Label("Secondary text at 12px for less important info.");
        secondary.setStyle("-fx-font-size: 12px; -fx-text-fill: -color-fg-muted;");

        var caption = new Label("Caption / hint text at 11px.");
        caption.setStyle("-fx-font-size: 11px; -fx-text-fill: -color-fg-subtle;");

        container.getChildren().addAll(pageTitle, sectionHeader, body, secondary, caption);
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
