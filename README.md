# AtlantaFX Desktop Starter

A JavaFX + Spring Boot desktop application template with AtlantaFX theming, designed as a starting point for building productivity-oriented desktop apps.

## Tech Stack

| Technology | Version | Role |
|-----------|---------|------|
| Java | 25 | Runtime |
| JavaFX | 25 | UI framework |
| Spring Boot | 3.5.5 | Dependency injection, data, config |
| AtlantaFX | 2.1.0 | Modern theme engine (Dracula default) |
| Ikonli | 12.4.0 | Icon packs (Feather, FontAwesome, Material, etc.) |
| ControlsFX | 11.2.3 | Extended controls |
| RichTextFX | 0.11.7 | Code/text editor component |
| H2 / SQLite | -- | Embedded databases |
| Flyway | 11.8.2 | Database migrations |
| OkHttp + Retrofit | 5.x / 3.x | HTTP client |
| Lombok | 1.18.44 | Boilerplate reduction |
| TestFX | 4.0.18 | UI testing |

## Project Structure

```
src/main/
  java/io/joshuasalcedo/desktop/
    Launcher.java               Application entry point (JavaFX + Spring Boot)
    controller/
      MainController.java       Main window controller
      SettingsController.java   Settings dialog controller
    view/
      FxmlViewManager.java      FXML loading, dialogs, view swapping
      ViewHolder.java           Record holding root node + controller
    config/                     Spring configuration classes
  resources/
    fxml/
      main.fxml                 Main window layout
      settings.fxml             Settings dialog
    assets/
      index.css                 Font loading
      desktop-overrides.css     Design system (colors, spacing, components)
      fonts/                    Bundled fonts
      icons/                    App icons
    logback-spring.xml          Logging config
    application.properties      App metadata
```

## Layout

```
+--------------------------------------------------+
|  SIDEBAR  |   CENTER CONTENT     |  RIGHT PANEL   |
|  (nav)    |   (swappable views)  |  (detail/      |
|           |                      |   inspector)   |
|           |                      |  [toggle]      |
+--------------------------------------------------+
|  STATUS BAR (message + progress + version)        |
+--------------------------------------------------+
```

## Getting Started

### Prerequisites

- JDK 25+ (GraalVM CE or Oracle)
- Maven 3.9+

### Run in Development

```bash
mvn javafx:run
```

### Debug Mode

```bash
mvn javafx:run@debug
# Then attach a remote debugger to port 5005
```

## Packaging

### Tier 1: Fat JAR

```bash
mvn clean package -DskipTests
java -jar target/fx-1.1.0.jar
```

### Tier 2a: App Image (self-contained .exe + bundled JRE)

```bash
mvn clean package -Pinstaller -DskipTests
```

Output: `target/installer/AtlantaFX Starter/AtlantaFX Starter.exe`

Zip the folder for distribution. No Java install needed on the target machine.

### Tier 2b: MSI Installer (requires WiX Toolset)

```bash
mvn clean package -Pinstaller-msi -DskipTests
```

Output: `target/installer/*.msi`

### Tier 3: GraalVM Native Image (experimental)

```bash
mvn clean package -Pnative -DskipTests
```

Requires GraalVM with `native-image` and Visual Studio Build Tools on Windows.

## Design System

The design system is defined in `src/main/resources/assets/desktop-overrides.css` and loaded after the AtlantaFX base theme.

**Key decisions:**
- 13px base font (desktop standard, not 16px web default)
- 4px spacing rhythm
- Muted color palette (no pure black/white)
- Borders for structure, shadows only for floating elements
- Max border-radius: 6px
- Component heights: 28px default, 24px compact

**Theme switching:** The Settings dialog (gear icon in sidebar) allows switching between AtlantaFX themes at runtime (Dracula, Nord Dark, Cupertino, Primer).

## Customizing This Template

1. **Rename the package** from `io.joshuasalcedo.desktop` to your own
2. **Update `pom.xml`** -- `groupId`, `artifactId`, `app.name`, `description`
3. **Add nav items** -- Edit `main.fxml` and wire icons in `MainController.initIcons()`
4. **Add views** -- Create FXML + Controller, load via `FxmlViewManager.load()`
5. **Swap content** -- Call `contentPane.getChildren().setAll(newView)` from the controller
6. **Add database migrations** -- Place SQL files in `src/main/resources/db/migration/`

## Logging

Logs write to `~/AtlantaFX-Starter/logs/app.log` with daily rotation (7 day retention, 50MB cap). A crash log at `~/AtlantaFX-Starter/logs/crash.log` catches errors before logback initializes.

## License

MIT
