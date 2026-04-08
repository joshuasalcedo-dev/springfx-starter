package io.joshuasalcedo.desktop.controller;

import atlantafx.base.theme.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SettingsController {

    private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);

    @FXML private ComboBox<String> themeComboBox;
    @FXML private ComboBox<String> fontSizeComboBox;

    @FXML
    public void initialize() {
        themeComboBox.setItems(FXCollections.observableArrayList(
                "Dracula", "Nord Dark", "Cupertino Dark", "Cupertino Light", "Primer Light", "Primer Dark"
        ));
        themeComboBox.setValue("Dracula");

        fontSizeComboBox.setItems(FXCollections.observableArrayList(
                "11px", "12px", "13px", "14px", "15px"
        ));
        fontSizeComboBox.setValue("13px");
    }

    @FXML
    private void onApply() {
        String selected = themeComboBox.getValue();
        Theme theme = switch (selected) {
            case "Nord Dark" -> new NordDark();
            case "Cupertino Dark" -> new CupertinoDark();
            case "Cupertino Light" -> new CupertinoLight();
            case "Primer Light" -> new PrimerLight();
            case "Primer Dark" -> new PrimerDark();
            default -> new Dracula();
        };
        Application.setUserAgentStylesheet(theme.getUserAgentStylesheet());

        String fontSize = fontSizeComboBox.getValue();
        themeComboBox.getScene().getRoot().setStyle("-fx-font-size: " + fontSize + ";");

        logger.info("Applied theme: {}, font size: {}", selected, fontSize);
        closeDialog();
    }

    @FXML
    private void onCancel() {
        closeDialog();
    }

    private void closeDialog() {
        ((Stage) themeComboBox.getScene().getWindow()).close();
    }
}
