package org.jvan100.sharedclipboard.service;

import org.jvan100.sharedclipboard.interfaces.ConnectionCallback;
import org.jvan100.sharedclipboard.util.Connection;

import java.io.IOException;

public class ReceiveService implements Runnable {

    private final Connection connection;
    private final ClipboardService clipboardService;
    private final ConnectionCallback callback;

    public ReceiveService(Connection connection, ClipboardService clipboardService, ConnectionCallback callback) {
        this.connection = connection;
        this.clipboardService = clipboardService;
        this.callback = callback;
    }

    public void run() {
        try {
            String message;

            while (!connection.isClosed()) {
                message = connection.readMessage();
                clipboardService.setClipboard(message);
            }
        } catch (IOException | ClassNotFoundException ignored) {
            callback.execute(String.format("Connection to (%s) lost.", connection.getSocket().getInetAddress().getHostAddress()));
        }
    }

}
