package io.joshuasalcedo.desktop.controller.showcase;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.stereotype.Component;

@Component
public class WebViewSectionController {

    @FXML private Label sectionTitle;
    @FXML private VBox demoBox;
    @FXML private ToggleButton codeToggle;
    @FXML private VBox codeBox;

    @FXML
    public void initialize() {
        sectionTitle.setText("WebView");

        var webView = new WebView();
        webView.setPrefHeight(350);
        WebEngine engine = webView.getEngine();

        // URL bar
        var urlField = new TextField("https://docs.oracle.com/en/java/javase/21/docs/api/");
        urlField.setPromptText("Enter URL...");
        HBox.setHgrow(urlField, Priority.ALWAYS);

        var goBtn = new Button(null, FontIcon.of(Feather.ARROW_RIGHT, 14));
        goBtn.setOnAction(e -> engine.load(urlField.getText().trim()));

        var backBtn = new Button(null, FontIcon.of(Feather.ARROW_LEFT, 14));
        backBtn.setOnAction(e -> {
            var history = engine.getHistory();
            if (history.getCurrentIndex() > 0) {
                history.go(-1);
            }
        });

        var forwardBtn = new Button(null, FontIcon.of(Feather.ARROW_RIGHT, 14));
        forwardBtn.setOnAction(e -> {
            var history = engine.getHistory();
            if (history.getCurrentIndex() < history.getEntries().size() - 1) {
                history.go(1);
            }
        });

        var reloadBtn = new Button(null, FontIcon.of(Feather.REFRESH_CW, 14));
        reloadBtn.setOnAction(e -> engine.reload());

        urlField.setOnAction(e -> engine.load(urlField.getText().trim()));

        // Update URL field on navigation
        engine.locationProperty().addListener((obs, old, loc) -> {
            if (loc != null) urlField.setText(loc);
        });

        // Loading indicator
        var progress = new ProgressBar();
        progress.setPrefHeight(3);
        progress.setMaxWidth(Double.MAX_VALUE);
        progress.progressProperty().bind(engine.getLoadWorker().progressProperty());
        progress.visibleProperty().bind(engine.getLoadWorker().runningProperty());
        progress.managedProperty().bind(progress.visibleProperty());

        var navBar = new HBox(6, backBtn, forwardBtn, reloadBtn, urlField, goBtn);
        navBar.setAlignment(Pos.CENTER_LEFT);

        // Render HTML button
        var htmlBtn = new Button("Load Sample HTML");
        htmlBtn.setOnAction(e -> engine.loadContent(SAMPLE_HTML));

        var toolbar = new HBox(8, navBar, htmlBtn);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(navBar, Priority.ALWAYS);

        demoBox.setSpacing(8);
        demoBox.getChildren().addAll(toolbar, progress, webView);

        // Load sample HTML by default
        engine.loadContent(SAMPLE_HTML);

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

    private static final String SAMPLE_HTML = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: system-ui, sans-serif; padding: 24px;
                           background: #1e1e2e; color: #cdd6f4; }
                    h1 { color: #cba6f7; }
                    code { background: #313244; padding: 2px 6px; border-radius: 4px;
                           font-family: monospace; color: #a6e3a1; }
                    a { color: #89b4fa; }
                    .card { background: #313244; border-radius: 8px; padding: 16px;
                            margin: 12px 0; }
                </style>
            </head>
            <body>
                <h1>WebView Demo</h1>
                <p>This is rendered HTML inside a JavaFX <code>WebView</code>.</p>
                <div class="card">
                    <strong>Features:</strong>
                    <ul>
                        <li>Full HTML5 / CSS3 / JavaScript support</li>
                        <li>Bi-directional Java ↔ JS communication</li>
                        <li>Load URLs or inline HTML content</li>
                        <li>Useful for rich text, dashboards, reports</li>
                    </ul>
                </div>
                <p>Try entering a URL in the bar above and clicking Go.</p>
            </body>
            </html>
            """;

    private static final String CODE = """
            var webView = new WebView();
            webView.setPrefHeight(400);
            WebEngine engine = webView.getEngine();

            // Load a URL
            engine.load("https://example.com");

            // Or load inline HTML
            engine.loadContent("<h1>Hello</h1><p>Rendered in WebView</p>");

            // Listen for location changes
            engine.locationProperty().addListener((obs, old, loc) ->
                    System.out.println("Navigated to: " + loc));

            // Java → JavaScript
            engine.executeScript("document.title");

            // JavaScript → Java (via JSObject)
            // JSObject window = (JSObject) engine.executeScript("window");
            // window.setMember("app", myJavaObject);
            """;
}
