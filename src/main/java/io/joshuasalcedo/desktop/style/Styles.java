package io.joshuasalcedo.desktop.style;

import javafx.scene.Node;

/**
 * Type-safe Java representation of the design system defined in
 * {@code desktop-overrides.css}. Every enum constant has a {@code value}
 * field and its {@code toString()} returns that value, so you can use it
 * directly in style expressions.
 *
 * <pre>{@code
 * // Add a CSS class
 * Styles.addClass(node, StyleClass.SIDEBAR, StyleClass.NAV_ITEM);
 *
 * // Set inline typography
 * label.setStyle(Text.PAGE_TITLE.value);
 *
 * // Use color tokens in inline styles
 * label.setStyle("-fx-text-fill: " + Color.FG_MUTED);
 *
 * // Compose inline styles
 * node.setStyle(Styles.of(Text.CAPTION, "-fx-padding: 4"));
 * }</pre>
 */
public final class Styles {

    private Styles() {}

    // ─────────────────────────────────────────────
    // CSS class names — node.getStyleClass().add(...)
    // ─────────────────────────────────────────────

    public enum StyleClass {
        // Layout
        SIDEBAR("sidebar"),
        TOP_BAR("top-bar"),
        STATUS_BAR("status-bar"),
        RIGHT_PANEL("right-panel"),
        CONTENT_SCROLL("content-scroll"),
        CONTENT_PANE("content-pane"),

        // Navigation
        NAV_ITEM("nav-item"),
        NAV_ITEM_ACTIVE("nav-item active"),
        NAV_ITEM_ICON("nav-item-icon"),
        NAV_ITEM_LABEL("nav-item-label"),
        NAV_SECTION_LABEL("nav-section-label"),

        // Branding
        APP_ICON("app-icon"),
        APP_NAME("app-name"),

        // Typography
        PAGE_TITLE("page-title"),
        TOP_BAR_ACTION("top-bar-action-label"),

        // Right panel
        RIGHT_PANEL_TITLE("right-panel-title"),
        RIGHT_PANEL_CLOSE("right-panel-close"),

        // Status bar
        STATUS_TEXT("status-text"),
        STATUS_PROGRESS("status-progress"),

        // Text contexts
        MONO("mono"),
        CODE("code"),
        LOG_VIEW("log-view"),

        // Button variants
        SM("sm"),
        OUTLINED("outlined"),
        FLAT("flat"),

        // Welcome
        WELCOME("welcome"),

        // Showcase
        SHOWCASE_CODE("showcase-code"),
        SHOWCASE_SECTION_TITLE("showcase-section-title"),
        SHOWCASE_DEMO("showcase-demo");

        public final String value;

        StyleClass(String value) { this.value = value; }

        @Override
        public String toString() { return value; }
    }

    // ─────────────────────────────────────────────
    // CSS color tokens — reference in inline styles
    // ─────────────────────────────────────────────

    public enum Color {
        // Backgrounds
        BG_DEFAULT("-color-bg-default"),
        BG_SUBTLE("-color-bg-subtle"),
        BG_MUTED("-color-bg-muted"),
        BG_INSET("-color-bg-inset"),

        // Surfaces
        SURFACE_SIDEBAR("-color-surface-sidebar"),
        SURFACE_CARD("-color-surface-card"),
        SURFACE_HOVER("-color-surface-hover"),
        SURFACE_ACTIVE("-color-surface-active"),

        // Foreground / text
        FG_DEFAULT("-color-fg-default"),
        FG_MUTED("-color-fg-muted"),
        FG_SUBTLE("-color-fg-subtle"),

        // Borders
        BORDER_DEFAULT("-color-border-default"),
        BORDER_MUTED("-color-border-muted"),

        // Accent
        ACCENT("-color-accent-default"),
        ACCENT_HOVER("-color-accent-hover"),
        ACCENT_MUTED("-color-accent-muted"),

        // Status
        SUCCESS("-color-success"),
        WARNING("-color-warning"),
        DANGER("-color-danger"),
        INFO("-color-info");

        public final String value;

        Color(String value) { this.value = value; }

        @Override
        public String toString() { return value; }

        /** e.g. {@code "-fx-text-fill: " + Color.FG_MUTED.fill()} → "-fx-text-fill: -color-fg-muted;" */
        public String fill() { return "-fx-text-fill: " + value + ";"; }

        /** e.g. {@code Color.BG_DEFAULT.bg()} → "-fx-background-color: -color-bg-default;" */
        public String bg() { return "-fx-background-color: " + value + ";"; }

        /** e.g. {@code Color.BORDER_DEFAULT.border()} → "-fx-border-color: -color-border-default;" */
        public String border() { return "-fx-border-color: " + value + ";"; }

        /** e.g. {@code Color.DANGER.base()} → "-fx-base: -color-danger;" (for tinting buttons) */
        public String base() { return "-fx-base: " + value + ";"; }
    }

    // ─────────────────────────────────────────────
    // Typography presets — inline style strings
    // ─────────────────────────────────────────────

    public enum Text {
        PAGE_TITLE("-fx-font-size: 18px; -fx-font-weight: 600;"),
        SECTION_HEADER("-fx-font-size: 15px; -fx-font-weight: 600;"),
        SECTION_HEADER_MEDIUM("-fx-font-size: 15px; -fx-font-weight: 500;"),
        BODY("-fx-font-size: 13px;"),
        SECONDARY("-fx-font-size: 12px; -fx-text-fill: -color-fg-muted;"),
        CAPTION("-fx-font-size: 11px; -fx-text-fill: -color-fg-subtle;"),
        LABEL_BOLD("-fx-font-size: 13px; -fx-font-weight: 600;"),
        SMALL("-fx-font-size: 11px;"),
        MONO("-fx-font-family: 'Cascadia Code','JetBrains Mono','Consolas','SF Mono',monospace; -fx-font-size: 12px;");

        public final String value;

        Text(String value) { this.value = value; }

        @Override
        public String toString() { return value; }
    }

    // ─────────────────────────────────────────────
    // Radius tokens
    // ─────────────────────────────────────────────

    public enum Radius {
        SM("-radius-sm"),
        MD("-radius-md"),
        LG("-radius-lg");

        public final String value;

        Radius(String value) { this.value = value; }

        @Override
        public String toString() { return value; }

        public String background() { return "-fx-background-radius: " + value + ";"; }
        public String borderRadius() { return "-fx-border-radius: " + value + ";"; }
        public String both() { return background() + " " + borderRadius(); }
    }

    // ─────────────────────────────────────────────
    // Spacing presets (consistent padding/gaps)
    // ─────────────────────────────────────────────

    public enum Spacing {
        XS("2"),
        SM("4"),
        MD("8"),
        LG("12"),
        XL("16"),
        XXL("24");

        public final String value;

        Spacing(String value) { this.value = value; }

        @Override
        public String toString() { return value; }

        /** All sides: "-fx-padding: 8;" */
        public String padding() { return "-fx-padding: " + value + ";"; }

        /** Horizontal + vertical: "-fx-padding: 4 12;" */
        public static String padding(Spacing vertical, Spacing horizontal) {
            return "-fx-padding: " + vertical.value + " " + horizontal.value + ";";
        }
    }

    // ─────────────────────────────────────────────
    // Helper methods
    // ─────────────────────────────────────────────

    /** Add one or more style classes to a node. */
    public static void addClass(Node node, StyleClass... classes) {
        for (StyleClass sc : classes) {
            // Handle compound classes like "nav-item active"
            for (String part : sc.value.split(" ")) {
                if (!node.getStyleClass().contains(part)) {
                    node.getStyleClass().add(part);
                }
            }
        }
    }

    /** Remove one or more style classes from a node. */
    public static void removeClass(Node node, StyleClass... classes) {
        for (StyleClass sc : classes) {
            for (String part : sc.value.split(" ")) {
                node.getStyleClass().remove(part);
            }
        }
    }

    /** Toggle a style class on/off. */
    public static void toggleClass(Node node, StyleClass styleClass, boolean on) {
        if (on) {
            addClass(node, styleClass);
        } else {
            removeClass(node, styleClass);
        }
    }

    /** Compose multiple inline style fragments into one string. */
    public static String of(Object... parts) {
        var sb = new StringBuilder();
        for (Object part : parts) {
            String s = part.toString().strip();
            sb.append(s);
            if (!s.endsWith(";")) sb.append(';');
            sb.append(' ');
        }
        return sb.toString().strip();
    }
}
