package io.joshuasalcedo.desktop.controller.showcase;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class IconsSectionController {

    @FXML private Label sectionTitle;
    @FXML private VBox demoBox;
    @FXML private ToggleButton codeToggle;
    @FXML private VBox codeBox;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> packFilter;
    @FXML private Label countLabel;
    @FXML private FlowPane iconsGrid;
    @FXML private Button prevBtn;
    @FXML private Button nextBtn;
    @FXML private Label pageLabel;

    private static final int PAGE_SIZE = 120;
    private int currentPage = 0;

    private final Map<String, List<Ikon>> iconPacks = new LinkedHashMap<>();
    private List<Ikon> filteredIcons = new ArrayList<>();

    @FXML
    public void initialize() {
        sectionTitle.setText("Icons (Ikonli)");
        loadIconPacks();
        setupFilters();
        applyFilter();

        codeBox.getChildren().add(ShowcaseUtils.createCodeArea(CODE));
        codeBox.setVisible(false);
        codeBox.setManaged(false);
    }

    private void loadIconPacks() {
        // Feather — always available
        tryLoadPack("Feather", "org.kordamp.ikonli.feather.Feather");

        // Codicons
        tryLoadPack("Codicons", "org.kordamp.ikonli.codicons.Codicons");

        // Bootstrap Icons
        tryLoadPack("Bootstrap", "org.kordamp.ikonli.bootstrapicons.BootstrapIcons");

        // Devicons
        tryLoadPack("Devicons", "org.kordamp.ikonli.devicons.Devicons");

        // Material Design 2 (A–Z sub-enums)
        for (char c = 'A'; c <= 'Z'; c++) {
            tryLoadPack("Material " + c,
                    "org.kordamp.ikonli.materialdesign2.MaterialDesign" + c);
        }

        // FontAwesome 6
        tryLoadPack("FA Brands", "org.kordamp.ikonli.fontawesome6.FontAwesomeBrands");
        tryLoadPack("FA Regular", "org.kordamp.ikonli.fontawesome6.FontAwesomeRegular");
        tryLoadPack("FA Solid", "org.kordamp.ikonli.fontawesome6.FontAwesomeSolid");

        // Fluent UI
        tryLoadPack("Fluent Filled A-L", "org.kordamp.ikonli.fluentui.FluentUiFilledAL");
        tryLoadPack("Fluent Filled M-Z", "org.kordamp.ikonli.fluentui.FluentUiFilledMZ");
        tryLoadPack("Fluent Regular A-L", "org.kordamp.ikonli.fluentui.FluentUiRegularAL");
        tryLoadPack("Fluent Regular M-Z", "org.kordamp.ikonli.fluentui.FluentUiRegularMZ");
    }

    @SuppressWarnings("unchecked")
    private void tryLoadPack(String displayName, String className) {
        try {
            Class<?> cls = Class.forName(className);
            if (cls.isEnum() && Ikon.class.isAssignableFrom(cls)) {
                List<Ikon> icons = Arrays.asList((Ikon[]) cls.getEnumConstants());
                if (!icons.isEmpty()) {
                    iconPacks.put(displayName, icons);
                }
            }
        } catch (ClassNotFoundException ignored) {
            // pack not on classpath — skip silently
        }
    }

    private void setupFilters() {
        // Pack filter combo
        packFilter.getItems().add("All Packs");
        packFilter.getItems().addAll(iconPacks.keySet());
        packFilter.setValue("All Packs");
        packFilter.setOnAction(e -> { currentPage = 0; applyFilter(); });

        // Search field — filter on each keystroke
        searchField.textProperty().addListener((obs, old, val) -> {
            currentPage = 0;
            applyFilter();
        });
    }

    private void applyFilter() {
        String search = searchField.getText() == null ? "" : searchField.getText().toLowerCase().trim();
        String pack = packFilter.getValue();

        // Collect matching icons
        List<Ikon> source;
        if ("All Packs".equals(pack)) {
            source = iconPacks.values().stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        } else {
            source = iconPacks.getOrDefault(pack, List.of());
        }

        if (!search.isEmpty()) {
            filteredIcons = source.stream()
                    .filter(icon -> icon.getDescription().toLowerCase().contains(search))
                    .collect(Collectors.toList());
        } else {
            filteredIcons = new ArrayList<>(source);
        }

        renderPage();
    }

    private void renderPage() {
        iconsGrid.getChildren().clear();

        int totalPages = Math.max(1, (int) Math.ceil((double) filteredIcons.size() / PAGE_SIZE));
        if (currentPage >= totalPages) currentPage = totalPages - 1;
        if (currentPage < 0) currentPage = 0;

        int from = currentPage * PAGE_SIZE;
        int to = Math.min(from + PAGE_SIZE, filteredIcons.size());

        List<Ikon> pageIcons = filteredIcons.subList(from, to);

        for (Ikon icon : pageIcons) {
            iconsGrid.getChildren().add(createIconCell(icon));
        }

        // Update pagination controls
        pageLabel.setText("Page " + (currentPage + 1) + " / " + totalPages);
        prevBtn.setDisable(currentPage == 0);
        nextBtn.setDisable(currentPage >= totalPages - 1);
        countLabel.setText(filteredIcons.size() + " icons");
    }

    private VBox createIconCell(Ikon icon) {
        FontIcon fi = new FontIcon(icon);
        fi.setIconSize(18);

        String name = icon.getDescription();
        String shortName = name.contains("-") ? name.substring(name.indexOf('-') + 1) : name;
        if (shortName.length() > 14) {
            shortName = shortName.substring(0, 12) + "..";
        }

        var label = new Label(shortName);
        label.setStyle("-fx-font-size: 9px; -fx-text-fill: -color-fg-subtle;");

        var cell = new VBox(3, fi, label);
        cell.setAlignment(Pos.CENTER);
        cell.setPrefWidth(80);
        cell.setPrefHeight(50);
        cell.setStyle("-fx-cursor: hand; -fx-background-radius: 4;");

        cell.setOnMouseEntered(e ->
                cell.setStyle("-fx-cursor: hand; -fx-background-radius: 4; -fx-background-color: -color-surface-hover;"));
        cell.setOnMouseExited(e ->
                cell.setStyle("-fx-cursor: hand; -fx-background-radius: 4;"));

        Tooltip.install(cell, new Tooltip(name));
        cell.setOnMouseClicked(e -> showIconPopover(icon, cell));

        return cell;
    }

    private void showIconPopover(Ikon icon, Node owner) {
        // Derive enum class + constant name for the Java snippet
        String enumClass = "?";
        String enumConstant = "?";
        if (icon instanceof Enum<?> enumVal) {
            enumClass = enumVal.getDeclaringClass().getSimpleName();
            enumConstant = enumVal.name();
        }
        String literal = icon.getDescription();
        String javaCode = "FontIcon.of(" + enumClass + "." + enumConstant + ", 16)";
        String fxmlCode = "<FontIcon iconLiteral=\"" + literal + "\" iconSize=\"16\"/>";
        String javaImport = "import org.kordamp.ikonli.javafx.FontIcon;\n"
                + "import " + icon.getClass().getName().replace('$', '.') + ";";

        // Large preview
        var preview = new FontIcon(icon);
        preview.setIconSize(48);

        var nameLabel = new Label(literal);
        nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600;");
        var packLabel = new Label(enumClass + "." + enumConstant);
        packLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: -color-fg-muted;");

        var header = new VBox(4, preview, nameLabel, packLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 8, 0));

        // Code snippets
        var javaSection = codeRow("Java", javaCode);
        var fxmlSection = codeRow("FXML", fxmlCode);
        var importSection = codeRow("Import", javaImport);

        var content = new VBox(10, header, new Separator(), importSection, javaSection, fxmlSection);
        content.setPadding(new Insets(16));
        content.setPrefWidth(380);

        var popOver = new PopOver(content);
        popOver.setTitle("Icon Detail");
        popOver.setHeaderAlwaysVisible(false);
        popOver.setDetachable(false);
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popOver.show(owner);
    }

    private HBox codeRow(String label, String code) {
        var lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 11px; -fx-font-weight: 600; -fx-min-width: 46;");

        var codeField = new TextField(code);
        codeField.setEditable(false);
        codeField.setStyle("-fx-font-family: 'Cascadia Code','JetBrains Mono','Consolas',monospace; "
                + "-fx-font-size: 11px;");
        HBox.setHgrow(codeField, Priority.ALWAYS);

        var copyBtn = new Button(null, FontIcon.of(
                org.kordamp.ikonli.feather.Feather.COPY, 12));
        copyBtn.setStyle("-fx-padding: 4;");
        copyBtn.setOnAction(e -> {
            var cc = new ClipboardContent();
            cc.putString(code);
            Clipboard.getSystemClipboard().setContent(cc);
            copyBtn.setGraphic(FontIcon.of(org.kordamp.ikonli.feather.Feather.CHECK, 12));
            var pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.2));
            pause.setOnFinished(ev -> copyBtn.setGraphic(FontIcon.of(
                    org.kordamp.ikonli.feather.Feather.COPY, 12)));
            pause.play();
        });

        var row = new HBox(6, lbl, codeField, copyBtn);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    @FXML
    private void prevPage() {
        if (currentPage > 0) {
            currentPage--;
            renderPage();
        }
    }

    @FXML
    private void nextPage() {
        int totalPages = (int) Math.ceil((double) filteredIcons.size() / PAGE_SIZE);
        if (currentPage < totalPages - 1) {
            currentPage++;
            renderPage();
        }
    }

    @FXML
    private void toggleCode() {
        boolean show = codeToggle.isSelected();
        codeBox.setVisible(show);
        codeBox.setManaged(show);
        codeToggle.setText(show ? "Hide Code" : "Show Code");
    }

    private static final String CODE = """
            // Add ikonli-feather-pack (or any other pack) dependency
            import org.kordamp.ikonli.feather.Feather;
            import org.kordamp.ikonli.javafx.FontIcon;

            // Create an icon
            var icon = FontIcon.of(Feather.HOME, 18);

            // Use as button graphic
            var btn = new Button("Save", FontIcon.of(Feather.SAVE, 14));

            // Use as label graphic
            label.setGraphic(FontIcon.of(Feather.SETTINGS, 16));

            // Available packs: Feather, Codicons, Bootstrap,
            //   Material Design 2, FontAwesome 6, Fluent UI, Devicons
            """;
}
