package org.jvan100.sharedclipboard.service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerService implements Runnable {

    private final int port;
    private volatile List<ServerConnection> connections;

    private ServerSocket serverSocket;

    private volatile boolean shutdown;

    public ServerService(int port) {
        this.port = port;
        this.connections = new ArrayList<>();
        this.shutdown = false;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(port);

            System.out.println("Listening for incoming connections...");

            Socket clientSocket;

            while (!shutdown) {
                clientSocket = serverSocket.accept();

                System.out.printf("--> Connection to client (%s) has been established.\n", clientSocket.getInetAddress().getHostAddress());

                synchronized (this) {
                    connections.add(new ServerConnection(clientSocket));
                }
            }

            serverSocket.close();
        } catch (IOException ignored) {

        } finally {
            try {
                for (final ServerConnection connection : connections)
                    connection.close();
            } catch (IOException ignored) {}
        }
    }

    public synchronized void sendMessage(String message) {
        try {
            for (final ServerConnection connection : connections) {
                final ObjectOutputStream oos = connection.getOos();
                oos.writeObject(message);
                oos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        shutdown = true;

        try {
            serverSocket.close();
        } catch (IOException ignored) {}
    }

}
