package org.jvan100.sharedclipboard.service;

import org.jvan100.sharedclipboard.interfaces.ConnectionCallback;
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
    private final ConnectionCallback callback;

    private final int timeout = 2000;

    public ClientConnectionService(int port, ConnectionsList connectionsList, ClipboardService clipboardService, ConnectionCallback callback) {
        this.port = port;
        this.connectionsList = connectionsList;
        this.clipboardService = clipboardService;
        this.callback = callback;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void run() {
        try {
            final Socket socket = new Socket();
            socket.connect(new InetSocketAddress(address, port), timeout);

            final Connection connection = new Connection(socket);
            connectionsList.addConnection(connection);

            new Thread(new ReceiveService(connection, clipboardService, callback)).start();

            callback.execute("Connection successful.");
        } catch (IOException ignored) {
            callback.execute("Connection failed.");
        }
    }

}
