package org.jvan100.sharedclipboard.service;

import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ClipboardService {

    private volatile Clipboard clipboard;
    private String oldContent;

    public ClipboardService() {
        this.clipboard = Clipboard.getSystemClipboard();
        this.oldContent = clipboard.hasString() ? clipboard.getString() : "";
    }

    public synchronized void setClipboard(String message) {
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(message);

        Platform.runLater(() -> clipboard.setContent(clipboardContent));

        oldContent = message;
    }

    public synchronized String getUpdate() {
        if (clipboard.hasString()) {
            final String newContent = clipboard.getString();

            return (!oldContent.equals(newContent)) ? newContent : null;
        }

        return null;
    }

}
