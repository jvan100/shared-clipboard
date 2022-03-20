package org.jvan100.sharedclipboard.service;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ClipboardService {

    private volatile Clipboard clipboard;

    public ClipboardService() {
        this.clipboard = Clipboard.getSystemClipboard();
    }

    public synchronized void setClipboard(String message) {
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(message);
        clipboard.setContent(clipboardContent);
    }

    public Clipboard getClipboard() {
        return clipboard;
    }

}
