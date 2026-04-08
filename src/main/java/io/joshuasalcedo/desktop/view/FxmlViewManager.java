package io.joshuasalcedo.desktop.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Component
public class FxmlViewManager {

    private static final Logger log = LoggerFactory.getLogger(FxmlViewManager.class);
    private static final String ASSETS_DIR = "/assets/";

    private final ApplicationContext applicationContext;
    private final List<String> globalStylesheets;
    private final Image appIcon;

    public FxmlViewManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.globalStylesheets = List.of(ASSETS_DIR + "index.css", ASSETS_DIR + "desktop-overrides.css");
        this.appIcon = new Image(Objects.requireNonNull(
                getClass().getResource(ASSETS_DIR + "icons/app-icon.png")
        ).toExternalForm());
    }

    // ─────────────────────────────────────────────
    // LOAD FXML
    // ─────────────────────────────────────────────

    /**
     * Loads an FXML file with Spring-managed controller injection.
     *
     * @param fxmlPath       classpath path, e.g. "/fxml/settings.fxml"
     * @param controllerType the expected controller class for type safety
     */
    public <C> ViewHolder<C> load(String fxmlPath, Class<C> controllerType) throws IOException {
        URL fxmlUrl = Objects.requireNonNull(
                getClass().getResource(fxmlPath),
                "FXML not found: " + fxmlPath
        );
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        loader.setControllerFactory(applicationContext::getBean);
        Parent root = loader.load();
        C controller = loader.getController();
        log.debug("Loaded FXML: {} with controller: {}", fxmlPath, controllerType.getSimpleName());
        return new ViewHolder<>(root, controller);
    }

    /**
     * Loads FXML when you don't need typed controller access.
     */
    public Parent loadView(String fxmlPath) throws IOException {
        return load(fxmlPath, Object.class).root();
    }

    // ─────────────────────────────────────────────
    // OPEN NEW WINDOWS
    // ─────────────────────────────────────────────

    /**
     * Opens a new window. The configurator callback runs after FXML is loaded
     * but before the stage is shown — use it to pass data to the controller.
     *
     * @param fxmlPath       classpath path to the FXML
     * @param controllerType controller class
     * @param title          window title
     * @param owner          owner window (null for no owner)
     * @param modality       Modality.NONE, APPLICATION_MODAL, or WINDOW_MODAL
     * @param configurator   callback to configure the controller before showing (nullable)
     * @return the ViewHolder so the caller can read results after the dialog closes
     */
    public <C> ViewHolder<C> openWindow(
            String fxmlPath,
            Class<C> controllerType,
            String title,
            Window owner,
            Modality modality,
            Consumer<C> configurator
    ) throws IOException {
        ViewHolder<C> holder = load(fxmlPath, controllerType);

        if (configurator != null) {
            configurator.accept(holder.controller());
        }

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.getIcons().add(appIcon);
        stage.initModality(modality);
        if (owner != null) {
            stage.initOwner(owner);
        }

        Scene scene = new Scene(holder.root());
        globalStylesheets.forEach(scene.getStylesheets()::add);
        stage.setScene(scene);

        if (modality != Modality.NONE) {
            stage.showAndWait();
        } else {
            stage.show();
        }

        return holder;
    }

    /**
     * Opens a modal dialog owned by the given window.
     */
    public <C> ViewHolder<C> showDialog(
            String fxmlPath,
            Class<C> controllerType,
            String title,
            Window owner,
            Consumer<C> configurator
    ) throws IOException {
        return openWindow(fxmlPath, controllerType, title, owner,
                Modality.WINDOW_MODAL, configurator);
    }

    /**
     * Opens a modal dialog with no data to pass in.
     */
    public <C> ViewHolder<C> showDialog(
            String fxmlPath,
            Class<C> controllerType,
            String title,
            Window owner
    ) throws IOException {
        return showDialog(fxmlPath, controllerType, title, owner, null);
    }

    // ─────────────────────────────────────────────
    // SWAP VIEWS (same stage, replace root)
    // ─────────────────────────────────────────────

    /**
     * Replaces the root of the given stage's scene with a new FXML view.
     * Preserves the existing scene's stylesheets and size.
     */
    public <C> ViewHolder<C> swapView(
            Stage stage,
            String fxmlPath,
            Class<C> controllerType
    ) throws IOException {
        ViewHolder<C> holder = load(fxmlPath, controllerType);
        stage.getScene().setRoot(holder.root());
        return holder;
    }

    /**
     * Swaps view with data passing to the new controller.
     */
    public <C> ViewHolder<C> swapView(
            Stage stage,
            String fxmlPath,
            Class<C> controllerType,
            Consumer<C> configurator
    ) throws IOException {
        ViewHolder<C> holder = load(fxmlPath, controllerType);
        if (configurator != null) {
            configurator.accept(holder.controller());
        }
        stage.getScene().setRoot(holder.root());
        return holder;
    }
}
