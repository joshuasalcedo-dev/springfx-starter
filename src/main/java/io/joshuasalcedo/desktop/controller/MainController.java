package io.joshuasalcedo.desktop.controller;

import io.joshuasalcedo.desktop.view.FxmlViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final FxmlViewManager viewManager;

    public MainController(FxmlViewManager viewManager) {
        this.viewManager = viewManager;
    }

    @FXML
    private Label appIconLabel;

    @FXML
    private Label appNameLabel;

    @FXML
    private VBox bottomSection;

    @FXML
    private HBox brandingBox;

    @FXML
    private StackPane contentPane;

    @FXML
    private ScrollPane contentScrollPane;

    @FXML
    private HBox navBottomItem;

    @FXML
    private Label navBottomItemIcon;

    @FXML
    private Label navBottomItemLabel;

    @FXML
    private VBox navButtonContainer;

    @FXML
    private HBox navItem1;

    @FXML
    private Label navItem1Icon;

    @FXML
    private Label navItem1Label;

    @FXML
    private HBox navItem2;

    @FXML
    private Label navItem2Icon;

    @FXML
    private Label navItem2Label;

    @FXML
    private HBox navItem3;

    @FXML
    private Label navItem3Icon;

    @FXML
    private Label navItem3Label;

    @FXML
    private HBox navItem4;

    @FXML
    private Label navItem4Icon;

    @FXML
    private Label navItem4Label;

    @FXML
    private HBox navSecondary1;

    @FXML
    private Label navSecondary1Icon;

    @FXML
    private Label navSecondary1Label;

    @FXML
    private HBox navSecondary2;

    @FXML
    private Label navSecondary2Icon;

    @FXML
    private Label navSecondary2Label;

    @FXML
    private Label pageTitle;

    @FXML
    private VBox rightPanel;

    @FXML
    private Label rightPanelClose;

    @FXML
    private StackPane rightPanelContent;

    @FXML
    private HBox rightPanelHeader;

    @FXML
    private ScrollPane rightPanelScrollPane;

    @FXML
    private Label rightPanelTitle;

    @FXML
    private VBox secondaryNavContainer;

    @FXML
    private Label secondarySectionLabel;

    @FXML
    private VBox sidebar;

    @FXML
    private HBox statusBar;

    @FXML
    private Label statusLabel;

    @FXML
    private ProgressBar statusProgressBar;

    @FXML
    private Label statusProgressLabel;

    @FXML
    private Label statusRightLabel;


    @FXML
    public void initialize() {
        logger.debug("MainController initialized");

        initIcons();
        navBottomItem.setOnMouseClicked(event -> openSettings());
    }

    private void initIcons() {
        setIcon(appIconLabel, Feather.BOX, 20);
        setIcon(navItem1Icon, Feather.HOME, 16);
        setIcon(navItem2Icon, Feather.FILE_TEXT, 16);
        setIcon(navItem3Icon, Feather.INBOX, 16);
        setIcon(navItem4Icon, Feather.BAR_CHART_2, 16);
        setIcon(navSecondary1Icon, Feather.USERS, 16);
        setIcon(navSecondary2Icon, Feather.ARCHIVE, 16);
        setIcon(navBottomItemIcon, Feather.SETTINGS, 16);
    }

    private void setIcon(Label label, Feather icon, int size) {
        FontIcon fontIcon = FontIcon.of(icon, size);
        label.setGraphic(fontIcon);
        label.setText(null);
    }

    private void openSettings() {
        try {
            viewManager.showDialog(
                    "/fxml/settings.fxml",
                    SettingsController.class,
                    "Settings",
                    sidebar.getScene().getWindow()
            );
        } catch (IOException e) {
            logger.error("Failed to open settings dialog", e);
        }
    }
}
