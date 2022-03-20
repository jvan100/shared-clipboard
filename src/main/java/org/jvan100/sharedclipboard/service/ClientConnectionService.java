package org.jvan100.sharedclipboard.service;

import org.jvan100.sharedclipboard.util.Connection;
import org.jvan100.sharedclipboard.util.ConnectionsList;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientConnectionService implements Runnable {

    private String address;
    private final int port;
    private final ConnectionsList connectionsList;
    private final ClipboardService clipboardService;

    private final int timeout = 2000;

    public ClientConnectionService(int port, ConnectionsList connectionsList, ClipboardService clipboardService) {
        this.port = port;
        this.connectionsList = connectionsList;
        this.clipboardService = clipboardService;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void run() {
        System.out.println("Client connection service running...");

        try {
            final Socket socket = new Socket();
            socket.connect(new InetSocketAddress(address, port), timeout);

            final Connection connection = new Connection(socket);
            connectionsList.addConnection(connection);

            new Thread(new ReceiveService(connection, clipboardService)).start();

            System.out.println("Client connection successful.");
        } catch (IOException ignored) {
            System.out.println("Client connection unsuccessful.");
        }
    }

}
