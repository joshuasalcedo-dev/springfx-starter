package io.joshuasalcedo.desktop.controller.showcase;

import io.joshuasalcedo.desktop.controller.showcase.ShowcaseUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import org.fxmisc.richtext.CodeArea;
import org.springframework.stereotype.Component;

@Component
public class TableSectionController {

    @FXML private Label sectionTitle;
    @FXML private VBox demoBox;
    @FXML private ToggleButton codeToggle;
    @FXML private VBox codeBox;

    private static final String CODE = """
            var nameCol = new TableColumn<>("Name");
            nameCol.setCellValueFactory(cd ->
                    new SimpleStringProperty(cd.getValue()[0]));

            var table = new TableView<String[]>();
            table.getColumns().addAll(nameCol, roleCol, statusCol);
            table.getItems().addAll(
                new String[]{"Alice", "Engineer", "Active"},
                new String[]{"Bob", "Designer", "Away"});
            table.setColumnResizePolicy(
                    TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);""";

    @FXML
    public void initialize() {
        sectionTitle.setText("TableView");

        TableColumn<String[], String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue()[0]));

        TableColumn<String[], String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue()[1]));

        TableColumn<String[], String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue()[2]));

        var table = new TableView<String[]>();
        table.getColumns().addAll(nameCol, roleCol, statusCol);
        table.getItems().addAll(
                new String[]{"Alice", "Engineer", "Active"},
                new String[]{"Bob", "Designer", "Away"},
                new String[]{"Charlie", "Manager", "Active"},
                new String[]{"Diana", "Analyst", "Offline"},
                new String[]{"Eve", "DevOps", "Active"}
        );
        table.setPrefHeight(200);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        demoBox.getChildren().add(table);

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
