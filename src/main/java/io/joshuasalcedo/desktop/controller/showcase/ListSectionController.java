package io.joshuasalcedo.desktop.controller.showcase;

import io.joshuasalcedo.desktop.controller.showcase.ShowcaseUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import org.fxmisc.richtext.CodeArea;
import org.springframework.stereotype.Component;

@Component
public class ListSectionController {

    @FXML private Label sectionTitle;
    @FXML private VBox demoBox;
    @FXML private ToggleButton codeToggle;
    @FXML private VBox codeBox;

    private static final String CODE = """
            var list = new ListView<>(FXCollections.observableArrayList(
                    "Dashboard", "Analytics", "Reports", "Settings"));
            list.setPrefHeight(180);
            list.getSelectionModel().selectFirst();""";

    @FXML
    public void initialize() {
        sectionTitle.setText("ListView");

        var list = new ListView<>(FXCollections.observableArrayList(
                "Dashboard", "Analytics", "Reports", "Team",
                "Settings", "Notifications", "Integrations", "Billing"));
        list.setPrefHeight(180);
        list.getSelectionModel().selectFirst();

        demoBox.getChildren().add(list);

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
