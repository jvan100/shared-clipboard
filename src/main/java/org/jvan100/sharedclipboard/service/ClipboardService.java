package org.jvan100.sharedclipboard.service;

import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

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

    public synchronized String getUpdate() throws InterruptedException, ExecutionException {
        final FutureTask<String> task = new FutureTask<>(() -> {
            if (clipboard.hasString()) {
                final String newContent = clipboard.getString();

                if (!oldContent.equals(newContent)) {
                    oldContent = newContent;
                    return oldContent;
                }
            }

            return null;
        });

        Platform.runLater(task);

        return task.get();
    }

}
