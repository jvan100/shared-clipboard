package org.jvan100.sharedclipboard.service;

import org.jvan100.sharedclipboard.util.Connection;
import org.jvan100.sharedclipboard.util.ConnectionsList;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnectionService implements Runnable {

    private final int port;
    private final ConnectionsList connectionsList;
    private final ClipboardService clipboardService;

    private ServerSocket serverSocket;

    private volatile boolean shutdown;

    public ServerConnectionService(int port, ConnectionsList connectionsList, ClipboardService clipboardService) {
        this.port = port;
        this.connectionsList = connectionsList;
        this.clipboardService = clipboardService;
        this.shutdown = false;
    }

    public void run() {
        System.out.println("Server connection service running...");

        try {
            serverSocket = new ServerSocket(port);

            Socket clientSocket;

            while (!shutdown) {
                clientSocket = serverSocket.accept();

                final Connection connection = new Connection(clientSocket);
                connectionsList.addConnection(connection);

                new Thread(new ReceiveService(connection, clipboardService)).start();

                System.out.printf("Server connection to (%s) established.\n", clientSocket.getInetAddress().getHostAddress());
            }

            serverSocket.close();
        } catch (IOException ignored) {
            System.out.println("Server connection service terminated.");
        }
    }

    public void shutdown() {
        shutdown = true;

        try {
            serverSocket.close();
        } catch (IOException ignored) {}
    }

}
