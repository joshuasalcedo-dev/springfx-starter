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
    Launcher.java                  Application entry point (JavaFX + Spring Boot)
    controller/
      MainController.java          Main window controller
      SettingsController.java      Settings dialog controller
      showcase/                    Component showcase (dev profile only)
        ButtonsSectionController   Buttons demo
        ComboSectionController     ComboBox & ChoiceBox demo
        IconsSectionController     Paginated icon browser (all Ikonli packs)
        InputsSectionController    Text inputs demo
        ListSectionController      ListView demo
        NotificationsSectionController  OS + in-app notifications demo
        ProgressSectionController  ProgressBar & spinner demo
        SliderSectionController    Slider & separator demo
        TableSectionController     TableView demo
        ToggleSectionController    CheckBox, RadioButton, ToggleButton demo
        TypographySectionController  Typography scale demo
        WebViewSectionController   Embedded browser demo
        ShowcaseUtils              RichTextFX code area with syntax highlighting
    service/
      NotificationService.java     OS-level + in-app notifications
      ProcessService.java          Non-blocking process execution
    style/
      Styles.java                  Type-safe design system tokens (enums)
    view/
      FxmlViewManager.java         FXML loading, dialogs, view swapping
      ViewHolder.java              Record holding root node + controller
    config/                        Spring configuration classes
  resources/
    fxml/
      main.fxml                    Main window layout
      settings.fxml                Settings dialog
      showcase.fxml                Showcase entry point (fx:include composition)
      showcase/                    One FXML per showcase section
        buttons.fxml, combo.fxml, icons.fxml, inputs.fxml,
        list.fxml, notifications.fxml, progress.fxml,
        slider.fxml, table.fxml, toggle.fxml, typography.fxml,
        webview.fxml
    assets/
      index.css                    Font loading
      desktop-overrides.css        Design system (colors, spacing, components)
      fonts/                       Bundled fonts
      icons/                       App icons
    logback-spring.xml             Logging config
    application.properties         App metadata
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

Output: `target/installer/atlantafx-starter/atlantafx-starter.exe`

Zip the folder for distribution. No Java install needed on the target machine.

### Tier 2b: MSI Installer (Windows, requires WiX Toolset)

```bash
mvn clean package -Pinstaller-msi -DskipTests
```

Output: `target/installer/*.msi`

### Tier 2c: DEB Installer (Debian/Ubuntu)

```bash
# With desktop shortcut (real Debian desktop)
mvn clean package -Pinstaller-deb -Djavafx.platform=linux -DskipTests

# Without shortcut (WSL or headless)
mvn clean package -Pinstaller-deb -Djavafx.platform=linux -Dlinux.shortcut=false -DskipTests
```

Output: `target/installer/*.deb`

```bash
# Install
sudo dpkg -i target/installer/*.deb

# Run
/opt/atlantafx-starter/bin/atlantafx-starter

# Uninstall
sudo dpkg -r atlantafx-starter
```

Requires `fakeroot` on the build machine (`apt install fakeroot`).

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

### Styles Utility

The `Styles` class (`io.joshuasalcedo.desktop.style.Styles`) provides type-safe Java enums for all design tokens:

```java
import static io.joshuasalcedo.desktop.style.Styles.*;

// CSS class names
Styles.addClass(node, StyleClass.SIDEBAR, StyleClass.NAV_ITEM);

// Typography presets
label.setStyle(Text.PAGE_TITLE.value);     // 18px semibold
caption.setStyle(Text.CAPTION.value);      // 11px subtle

// Color tokens with shorthand methods
btn.setStyle(Color.DANGER.base());         // -fx-base: -color-danger;
label.setStyle(Color.FG_MUTED.fill());     // -fx-text-fill: -color-fg-muted;

// Compose multiple styles
node.setStyle(Styles.of(Text.CAPTION, Color.FG_MUTED.fill(), Spacing.MD.padding()));
```

Available enums: `StyleClass`, `Color`, `Text`, `Radius`, `Spacing`.

### Component Showcase (Dev Only)

When running with the `dev` profile, a Showcase nav item appears in the sidebar. It renders live demos of all built-in components with toggleable Java code snippets (syntax-highlighted via RichTextFX). The Icons section provides a searchable, paginated browser across all Ikonli icon packs with click-to-copy code generation.

## Customizing This Template

1. **Rename the package** from `io.joshuasalcedo.desktop` to your own
2. **Update `pom.xml`** -- `groupId`, `artifactId`, `app.name`, `description`
3. **Add nav items** -- Edit `main.fxml` and wire icons in `MainController.initIcons()`
4. **Add views** -- Create FXML + Controller, load via `FxmlViewManager.load()`
5. **Swap content** -- Call `contentPane.getChildren().setAll(newView)` from the controller
6. **Add database migrations** -- Place SQL files in `src/main/resources/db/migration/`

## Logging

Logs write to `~/<app.dir>/logs/app.log` with daily rotation (7 day retention, 50MB cap). A crash log at `~/<app.dir>/logs/crash.log` catches errors before logback initializes.

## Library Reference

Libraries organized by use case, similar to the Tauri plugin ecosystem. Items marked **[included]** are already in the pom.

### System Tray

| Library | Description |
|---------|-------------|
| **FXTrayIcon** [included] | JavaFX-native system tray icon with menu support |

### File System / Watching

| Library | Description |
|---------|-------------|
| **JavaFX FileChooser** [built-in] | Native OS file/directory dialogs |
| **directory-watcher** [included] | Efficient recursive directory watching using OS-native APIs |
| **Commons IO** [included] | File utilities, filters, comparators |
| **JNA Platform** | Access native OS file dialogs and shell functions via JNA |

### Notifications / Toasts

| Library | Description |
|---------|-------------|
| **two-slices** [included] | Cross-platform native desktop notifications (Win/Mac/Linux) |
| **ControlsFX Notifications** [included] | In-app toast/notification popups via `Notifications.create()` |
| **GemsFX** [included] | InfoCenterPane for notification center-style UI |

### Auto-Update

| Library | Description |
|---------|-------------|
| **update4j** [included] | Java 9+ module-aware auto-update and launcher framework |

### Database / Storage

| Library | Description |
|---------|-------------|
| **SQLite JDBC** [included] | SQLite embedded database driver |
| **H2 Database** [included] | Embeddable Java SQL database (used for tests) |
| **Flyway** [included] | Database migration and versioning |
| **Spring Data JPA** [included] | ORM/repository layer over Hibernate |
| **Typesafe Config** [included] | HOCON/JSON/properties configuration library |
| **MapDB** | Embedded key-value/map database backed by disk |
| **Nitrite (NO2)** | Embedded NoSQL document database |

### HTTP Client

| Library | Description |
|---------|-------------|
| **OkHttp** [included] | HTTP/2, WebSocket, connection pooling |
| **Retrofit** [included] | Type-safe HTTP client with annotation-driven API definitions |
| **Spring WebClient** [included] | Reactive non-blocking HTTP client with SSE/WebSocket |
| **java.net.http.HttpClient** [built-in] | Modern async HTTP/2 client in the JDK |

### Logging / Crash Reporting

| Library | Description |
|---------|-------------|
| **SLF4J + Logback** [included] | Default Spring Boot logging |
| **Sentry** | Crash reporting and error tracking with Logback integration |

### Rich Text / Code Editor

| Library | Description |
|---------|-------------|
| **RichTextFX** [included] | Rich text area with syntax highlighting for JavaFX |

### Charts / Data Visualization

| Library | Description |
|---------|-------------|
| **Hansolo Charts** [included] | Lightweight JavaFX chart library |
| **TilesFX** [included] | Dashboard tile components for JavaFX |
| **JavaFX Charts** [built-in] | Built-in line, bar, pie, area, scatter, bubble charts |
| **FXyz3D** | 3D visualization components |

### PDF

| Library | Description |
|---------|-------------|
| **Apache PDFBox** [included] | PDF creation, manipulation, and text extraction |
| **OpenPDF** | PDF generation (LGPL fork of iText 4) |

### Theming / UI Controls

| Library | Description |
|---------|-------------|
| **AtlantaFX** [included] | Modern flat themes (Primer, Nord, Dracula, Cupertino) |
| **Ikonli** [included] | Icon packs (FontAwesome, Material, Bootstrap, Feather, etc.) |
| **ControlsFX** [included] | 50+ additional UI controls (SearchableComboBox, RangeSlider, etc.) |
| **FormsFX** [included] | Declarative form builder |
| **ValidatorFX** [included] | Form validation with decorations |
| **GemsFX** [included] | Polished controls: PhotoView, TagField, SearchField, etc. |
| **AnimateFX** [included] | 70+ CSS-like animations for JavaFX nodes |
| **MaterialFX** | Material Design 3 controls |
| **PreferencesFX** | Ready-made settings/preferences dialog builder |

### Terminal / Shell

| Library | Description |
|---------|-------------|
| **TerminalFX** | Embedded terminal emulator via WebView |
| **NuProcess** | Low-overhead, non-blocking process execution |
| **ProcessBuilder** [built-in] | JDK built-in process spawning |

### Keyboard Shortcuts

| Library | Description |
|---------|-------------|
| **JavaFX KeyCombination** [built-in] | App-scoped accelerators and key bindings |
| **JNativeHook** | System-wide global keyboard and mouse listeners |

### Image Processing

| Library | Description |
|---------|-------------|
| **TwelveMonkeys ImageIO** [included] | Extended ImageIO: WebP, TIFF, PSD, SVG, etc. |
| **Thumbnailator** | Simple fluent API for image resizing and thumbnails |

### Markdown

| Library | Description |
|---------|-------------|
| **JPro MDFX** [included] | Render Markdown as JavaFX nodes |
| **Flexmark** | Feature-rich Markdown parser with GFM extensions |

### Calendar / Date

| Library | Description |
|---------|-------------|
| **CalendarFX** [included] | Full-featured calendar views (day, week, month, year) |
| **GemsFX** [included] | DateRangePicker, TimePicker, DurationPicker controls |

### Barcode / QR

| Library | Description |
|---------|-------------|
| **ZXing** | Barcode/QR generation and scanning |

### WebView

| Library | Description |
|---------|-------------|
| **JavaFX WebView** [included] | Built-in WebKit-based browser component |
| **JCEF** | Chromium-based browser embed (more capable than WebView) |

### Media

| Library | Description |
|---------|-------------|
| **JavaFX Media** [included] | Audio/video playback (MP3, WAV, AAC, H.264) |
| **VLCJ-JavaFX** | VLC bindings -- plays virtually any format |

### CSS Hot Reload

| Library | Description |
|---------|-------------|
| **CSSFX** [included] | Automatic CSS hot-reload on file save |
| **Scenic View** | Runtime scene graph inspector/debugger (standalone tool) |

### Security

| Library | Description |
|---------|-------------|
| **java.security / javax.crypto** [built-in] | KeyStore, AES, RSA, HMAC, SecureRandom |
| **Bouncy Castle** | Comprehensive cryptography provider |
| **Spring Security Crypto** | Password encoding (BCrypt, Argon2) and encryption |

### Worth Adding

| Priority | Library | Why |
|----------|---------|-----|
| High | `com.github.kwhat:jnativehook` | System-wide global hotkeys -- no JavaFX equivalent |
| High | `com.google.zxing:core` | QR/barcode is a common desktop need |
| Medium | `io.sentry:sentry-logback` | Crash reporting for production apps |
| Medium | `com.dlsc.preferencesfx:preferencesfx-core` | Ready-made settings dialog |
| Medium | `net.coobird:thumbnailator` | Dead-simple image thumbnailing |
| Low | `uk.co.caprica:vlcj-javafx` | Play any media format beyond JavaFX's limited codecs |

## License

MIT
# developer-center
