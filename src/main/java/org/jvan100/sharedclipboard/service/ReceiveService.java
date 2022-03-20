package org.jvan100.sharedclipboard.service;

import org.jvan100.sharedclipboard.util.Connection;

import java.io.IOException;

public class ReceiveService implements Runnable {

    private final Connection connection;
    private final ClipboardService clipboardService;

    public ReceiveService(Connection connection, ClipboardService clipboardService) {
        this.connection = connection;
        this.clipboardService = clipboardService;
    }

    public void run() {
        System.out.println("Receive service running...");

        try {
            String message;

            while (!connection.isClosed()) {
                message = connection.readMessage();
                clipboardService.setClipboard(message);
            }
        } catch (IOException | ClassNotFoundException ignored) {
            System.out.println("Receive service unable to receive message.");
        }
    }

}
