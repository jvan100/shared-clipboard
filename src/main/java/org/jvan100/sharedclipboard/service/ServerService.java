package org.jvan100.sharedclipboard.service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerService implements Runnable {

    private final int port;
    private final List<Socket> sockets;

    private ServerSocket serverSocket;

    private volatile boolean shutdown;

    public ServerService(int port) {
        this.port = port;
        this.sockets = Collections.synchronizedList(new ArrayList<>());
        this.shutdown = false;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(port);

            Socket clientSocket;

            while (!shutdown) {
                System.out.println("Listening for incoming connections...");

                clientSocket = serverSocket.accept();

                System.out.printf("--> Connection to client (%s) has been established.\n", clientSocket.getInetAddress().getHostAddress());

                synchronized (sockets) {
                    sockets.add(clientSocket);
                }
            }

            serverSocket.close();
        } catch (IOException ignored) {
        } finally {
            try {
                for (final Socket socket : sockets) {
                    if (!socket.isClosed()) {
                        socket.close();
                    }
                }
            } catch (IOException ignored) {}
        }
        System.out.println("here");
    }

    public void sendMessage(String message) {
        System.out.println("Sending message: " + message);

        try {
            synchronized (sockets) {
                for (final Socket socket : sockets) {
                    if (!socket.isClosed()) {
                        final ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(message);
                        oos.flush();
                    }
                }
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
