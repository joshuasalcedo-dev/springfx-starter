package io.joshuasalcedo.desktop;

import atlantafx.base.theme.Dracula;
import io.joshuasalcedo.desktop.controller.MainController;
import io.joshuasalcedo.desktop.view.FxmlViewManager;
import io.joshuasalcedo.desktop.view.ViewHolder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Launcher extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);
    private static final Path CRASH_LOG = Path.of(
            System.getProperty("user.home"), "AtlantaFX-Starter", "logs", "crash.log"
    );

    private ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        // Global crash handler — catches errors that occur before logback initializes
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
                writeCrashLog("Uncaught exception in thread " + thread.getName(), throwable));

        try {
            Application.launch(Launcher.class, args);
        } catch (Throwable t) {
            writeCrashLog("Fatal error during Application.launch()", t);
            throw t;
        }
    }

    @Override
    public void init() {
        logger.info("=== Application starting ===");
        logger.info("Java version: {}", System.getProperty("java.version"));
        logger.info("JavaFX version: {}", System.getProperty("javafx.version"));
        logger.info("OS: {} {}", System.getProperty("os.name"), System.getProperty("os.arch"));
        logger.info("User dir: {}", System.getProperty("user.dir"));
        logger.info("Java home: {}", System.getProperty("java.home"));
        logger.debug("Initializing Spring context");
        try {
            applicationContext = new SpringApplicationBuilder(AppConfig.class)
                    .headless(false)
                    .run();
            logger.info("Spring context initialized successfully");
        } catch (Exception e) {
            logger.error("Spring context failed to initialize", e);
            writeCrashLog("Spring context initialization failed", e);
            throw e;
        }
    }

    @Override
    public void start(Stage stage) {
        logger.debug("Starting JavaFX stage");
        try {
            Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
            applicationContext.publishEvent(new StageReady(stage));
            logger.info("Application stage shown successfully");
        } catch (Exception e) {
            logger.error("Failed to start JavaFX stage", e);
            writeCrashLog("JavaFX stage start failed", e);
            throw e;
        }
    }

    @Override
    public void stop() {
        logger.info("=== Application stopping ===");
        applicationContext.close();
        Platform.exit();
    }

    /**
     * Writes a crash log entry to ~/AtlantaFX-Starter/logs/crash.log.
     * This works even when logback has not initialized (e.g. early JVM errors).
     */
    private static void writeCrashLog(String message, Throwable throwable) {
        try {
            Files.createDirectories(CRASH_LOG.getParent());
            StringWriter sw = new StringWriter();
            sw.write("=== " + LocalDateTime.now() + " ===\n");
            sw.write(message + "\n");
            if (throwable != null) {
                throwable.printStackTrace(new PrintWriter(sw));
            }
            sw.write("\n");
            Files.writeString(CRASH_LOG, sw.toString(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ignored) {
            // Last resort: nothing we can do if file writing fails
        }
    }

    // ─────────────────────────────────────────────
    // Spring Boot entry point
    // ─────────────────────────────────────────────

    @SpringBootApplication
    public static class AppConfig {
    }

    // ─────────────────────────────────────────────
    // Stage lifecycle event
    // ─────────────────────────────────────────────

    public static class StageReady extends ApplicationEvent {
        public StageReady(Stage stage) {
            super(stage);
        }

        public Stage getStage() {
            return (Stage) getSource();
        }
    }

    // ─────────────────────────────────────────────
    // Stage initializer — wires up the primary stage
    // ─────────────────────────────────────────────

    @Component
    public static class StageInitializer implements ApplicationListener<StageReady> {

        private static final Logger logger = LoggerFactory.getLogger(StageInitializer.class);
        private static final String ASSETS_DIR = "/assets/";
        private static final String APP_PROPERTIES_PATH = "/application.properties";

        private static final String APP_ICON_PATH = Objects.requireNonNull(
                StageInitializer.class.getResource(ASSETS_DIR + "icons/app-icon.png")
        ).toExternalForm();

        private final FxmlViewManager viewManager;

        public StageInitializer(FxmlViewManager viewManager) {
            this.viewManager = viewManager;
        }

        @Override
        public void onApplicationEvent(StageReady event) {
            Stage stage = event.getStage();
            loadApplicationProperties();

            try {
                ViewHolder<MainController> holder =
                        viewManager.load("/fxml/main.fxml", MainController.class);

                var scene = new Scene(holder.root(), 1200, 800);
                scene.getStylesheets().add(ASSETS_DIR + "index.css");
                scene.getStylesheets().add(ASSETS_DIR + "desktop-overrides.css");

                stage.setScene(scene);
                stage.setTitle(System.getProperty("app.name"));
                stage.getIcons().add(new Image(APP_ICON_PATH));
                stage.setOnCloseRequest(t -> Platform.exit());
                stage.setMinWidth(800);
                stage.setMinHeight(600);

                Platform.runLater(() -> {
                    stage.show();
                    stage.requestFocus();
                });
            } catch (IOException e) {
                logger.error("Failed to load main FXML", e);
                throw new RuntimeException(e);
            }
        }

        private void loadApplicationProperties() {
            try {
                Properties properties = new Properties();
                properties.load(new InputStreamReader(
                        Objects.requireNonNull(getClass().getResourceAsStream(APP_PROPERTIES_PATH)),
                        UTF_8
                ));
                properties.forEach((key, value) -> System.setProperty(
                        String.valueOf(key),
                        String.valueOf(value)
                ));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
