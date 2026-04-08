package io.joshuasalcedo.desktop.controller.showcase;

import io.joshuasalcedo.desktop.controller.showcase.ShowcaseUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
public class ComboSectionController {

    @FXML private Label sectionTitle;
    @FXML private VBox demoBox;
    @FXML private ToggleButton codeToggle;
    @FXML private VBox codeBox;

    private static final String CODE = """
            var combo = new ComboBox<>(FXCollections.observableArrayList(
                    "Option A", "Option B", "Option C"));
            combo.setPromptText("ComboBox...");

            var editable = new ComboBox<>(list);
            editable.setEditable(true);

            var choice = new ChoiceBox<>(FXCollections.observableArrayList(
                    "Small", "Medium", "Large"));
            choice.setValue("Medium");
            """;

    @FXML
    public void initialize() {
        sectionTitle.setText("ComboBox & ChoiceBox");

        var combo = new ComboBox<>(FXCollections.observableArrayList(
                "Option A", "Option B", "Option C"));
        combo.setPromptText("ComboBox...");
        combo.setPrefWidth(160);
        combo.setMinWidth(160);

        var editable = new ComboBox<>(FXCollections.observableArrayList(
                "Red", "Green", "Blue"));
        editable.setEditable(true);
        editable.setPromptText("Editable combo...");
        editable.setPrefWidth(160);

        var choice = new ChoiceBox<>(FXCollections.observableArrayList(
                "Small", "Medium", "Large"));
        choice.setValue("Medium");

        var row = new HBox(8, combo, editable, choice);
        demoBox.getChildren().add(row);

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
