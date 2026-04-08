package io.joshuasalcedo.desktop.controller.showcase;

import io.joshuasalcedo.desktop.controller.showcase.ShowcaseUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.stereotype.Component;

@Component
public class ButtonsSectionController {

    @FXML private Label sectionTitle;
    @FXML private VBox demoBox;
    @FXML private ToggleButton codeToggle;
    @FXML private VBox codeBox;

    private static final String CODE = """
            var def = new Button("Default");

            var accent = new Button("Accent");
            accent.setDefaultButton(true);

            var danger = new Button("Danger");
            danger.setStyle("-fx-base: -color-danger;");

            var withIcon = new Button("Save", FontIcon.of(Feather.SAVE, 14));
            """;

    @FXML
    public void initialize() {
        sectionTitle.setText("Buttons");

        var flow = new FlowPane(8, 8);

        var def = new Button("Default");

        var accent = new Button("Accent");
        accent.setDefaultButton(true);

        var danger = new Button("Danger");
        danger.setStyle("-fx-base: -color-danger;");

        var outlined = new Button("Outlined");
        outlined.getStyleClass().add("outlined");

        var flat = new Button("Flat");
        flat.getStyleClass().add("flat");

        var disabled = new Button("Disabled");
        disabled.setDisable(true);

        var withIcon = new Button("Save", FontIcon.of(Feather.SAVE, 14));

        flow.getChildren().addAll(def, accent, danger, outlined, flat, disabled, withIcon);
        demoBox.getChildren().add(flow);

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
