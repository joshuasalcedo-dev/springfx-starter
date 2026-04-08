package io.joshuasalcedo.desktop.controller.showcase;

import io.joshuasalcedo.desktop.controller.showcase.ShowcaseUtils;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.fxmisc.richtext.CodeArea;
import org.springframework.stereotype.Component;

@Component
public class SliderSectionController {

    @FXML private Label sectionTitle;
    @FXML private VBox demoBox;
    @FXML private ToggleButton codeToggle;
    @FXML private VBox codeBox;

    private static final String CODE = """
            var slider = new Slider(0, 100, 40);
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(true);
            slider.setMajorTickUnit(25);

            slider.valueProperty().addListener((obs, old, val) ->
                    label.setText(String.valueOf(val.intValue())));""";

    @FXML
    public void initialize() {
        sectionTitle.setText("Slider & Separator");

        var container = new VBox(8);

        var slider = new Slider(0, 100, 40);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(25);
        slider.setPrefWidth(300);

        var valueLabel = new Label("40");
        slider.valueProperty().addListener((obs, old, val) ->
                valueLabel.setText(String.valueOf(val.intValue())));

        var sliderRow = new HBox(12, slider, valueLabel);

        var sepLabel = new Label("Separator:");
        var separator = new Separator(Orientation.HORIZONTAL);

        container.getChildren().addAll(sliderRow, sepLabel, separator);
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
