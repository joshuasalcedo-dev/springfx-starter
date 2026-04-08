package io.joshuasalcedo.desktop.controller.showcase;

import io.joshuasalcedo.desktop.controller.showcase.ShowcaseUtils;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
public class ToggleSectionController {

    @FXML private Label sectionTitle;
    @FXML private VBox demoBox;
    @FXML private ToggleButton codeToggle;
    @FXML private VBox codeBox;

    private static final String CODE = """
            var cb = new CheckBox("Enabled");
            cb.setSelected(true);

            var rb1 = new RadioButton("Light");
            var rb2 = new RadioButton("Dark");
            var group = new ToggleGroup();
            rb1.setToggleGroup(group);
            rb2.setToggleGroup(group);

            var toggle = new ToggleButton("Bold");
            """;

    @FXML
    public void initialize() {
        sectionTitle.setText("ToggleButtons & CheckBox");

        var container = new VBox(8);

        var cbEnabled = new CheckBox("Enabled");
        cbEnabled.setSelected(true);
        var cbNotifications = new CheckBox("Notifications");
        var cbDisabled = new CheckBox("Disabled");
        cbDisabled.setDisable(true);
        var checkRow = new HBox(12, cbEnabled, cbNotifications, cbDisabled);

        var group = new ToggleGroup();
        var rbLight = new RadioButton("Light");
        rbLight.setToggleGroup(group);
        var rbDark = new RadioButton("Dark");
        rbDark.setToggleGroup(group);
        rbDark.setSelected(true);
        var rbSystem = new RadioButton("System");
        rbSystem.setToggleGroup(group);
        var radioRow = new HBox(12, rbLight, rbDark, rbSystem);

        var bold = new ToggleButton("Bold");
        var italic = new ToggleButton("Italic");
        var toggleRow = new HBox(8, bold, italic);

        container.getChildren().addAll(checkRow, radioRow, toggleRow);
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
