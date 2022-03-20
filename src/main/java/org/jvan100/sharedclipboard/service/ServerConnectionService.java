package org.jvan100.sharedclipboard.service;

import org.jvan100.sharedclipboard.interfaces.ConnectionCallback;
import org.jvan100.sharedclipboard.util.Connection;
import org.jvan100.sharedclipboard.util.ConnectionsList;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnectionService implements Runnable {

    private final int port;
    private final ConnectionsList connectionsList;
    private final ClipboardService clipboardService;
    private final ConnectionCallback callback;

    private ServerSocket serverSocket;

    private volatile boolean shutdown;

    public ServerConnectionService(int port, ConnectionsList connectionsList, ClipboardService clipboardService, ConnectionCallback callback) {
        this.port = port;
        this.connectionsList = connectionsList;
        this.clipboardService = clipboardService;
        this.callback = callback;
        this.shutdown = false;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(port);

            Socket clientSocket;

            while (!shutdown) {
                clientSocket = serverSocket.accept();

                final Connection connection = new Connection(clientSocket);
                connectionsList.addConnection(connection);

                new Thread(new ReceiveService(connection, clipboardService, callback)).start();

                callback.execute(String.format("Connected to (%s)", clientSocket.getInetAddress().getHostAddress()));
            }

            serverSocket.close();
        } catch (IOException ignored) {}
    }

    public void shutdown() {
        shutdown = true;

        try {
            serverSocket.close();
        } catch (IOException ignored) {}
    }

}
