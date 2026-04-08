package io.joshuasalcedo.desktop.view;

import javafx.scene.Parent;

/**
 * Holds a loaded FXML view and its Spring-managed controller.
 */
public record ViewHolder<C>(Parent root, C controller) {
}
