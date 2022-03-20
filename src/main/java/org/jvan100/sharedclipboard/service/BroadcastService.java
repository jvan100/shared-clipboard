package org.jvan100.sharedclipboard.service;

import org.jvan100.sharedclipboard.util.Connection;
import org.jvan100.sharedclipboard.util.ConnectionsList;

import java.io.IOException;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class BroadcastService extends TimerTask {

    private final ConnectionsList connectionsList;
    private final ClipboardService clipboardService;

    public BroadcastService(ConnectionsList connectionsList, ClipboardService clipboardService) {
        this.connectionsList = connectionsList;
        this.clipboardService = clipboardService;
    }

    public void run() {
        String message = null;

        try {
            message = clipboardService.getUpdate();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (message != null) {
            try {
                synchronized (connectionsList) {
                    for (final Connection connection : connectionsList.getConnections()) {
                        if (!connection.isClosed())
                            connection.writeMessage(message);
                    }
                }
            } catch (IOException ignored) {
                System.out.println("Broadcast service unable to send message.");
            }
        }
    }

}
