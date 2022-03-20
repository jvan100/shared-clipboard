package org.jvan100.sharedclipboard.service;

import org.jvan100.sharedclipboard.util.Connection;
import org.jvan100.sharedclipboard.util.ConnectionsList;

import java.io.IOException;
import java.util.TimerTask;

public class BroadcastService extends TimerTask {

    private final ConnectionsList connectionsList;
    private final ClipboardService clipboardService;

    public BroadcastService(ConnectionsList connectionsList, ClipboardService clipboardService) {
        this.connectionsList = connectionsList;
        this.clipboardService = clipboardService;
    }

    public void run() {
        final String message = clipboardService.getUpdate();

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
