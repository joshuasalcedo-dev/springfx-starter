package io.joshuasalcedo.desktop.controller.showcase;

import io.joshuasalcedo.desktop.controller.showcase.ShowcaseUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
public class ProgressSectionController {

    @FXML private Label sectionTitle;
    @FXML private VBox demoBox;
    @FXML private ToggleButton codeToggle;
    @FXML private VBox codeBox;

    private static final String CODE = """
            var pb = new ProgressBar(0.65);
            pb.setPrefWidth(250);

            var indeterminate = new ProgressBar(); // -1 by default

            var spinner = new ProgressIndicator();
            spinner.setPrefSize(32, 32);
            """;

    @FXML
    public void initialize() {
        sectionTitle.setText("ProgressBar & Spinner");

        var container = new VBox(8);

        var pb = new ProgressBar(0.65);
        pb.setPrefWidth(250);
        var pbLabel = new Label("65%");
        var pbRow = new HBox(8, pb, pbLabel);
        pbRow.setAlignment(Pos.CENTER_LEFT);

        var indeterminateLabel = new Label("Indeterminate:");
        var indeterminate = new ProgressBar();
        indeterminate.setPrefWidth(250);
        var indeterminateRow = new HBox(8, indeterminateLabel, indeterminate);
        indeterminateRow.setAlignment(Pos.CENTER_LEFT);

        var spinnerLabel = new Label("Spinner:");
        var spinner = new ProgressIndicator();
        spinner.setPrefSize(32, 32);
        var doneSpinner = new ProgressIndicator(1.0);
        doneSpinner.setPrefSize(32, 32);
        var spinnerRow = new HBox(12, spinnerLabel, spinner, doneSpinner);
        spinnerRow.setAlignment(Pos.CENTER_LEFT);

        container.getChildren().addAll(pbRow, indeterminateRow, spinnerRow);
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
