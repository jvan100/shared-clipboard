package org.jvan100.sharedclipboard.service;

import javafx.scene.input.Clipboard;
import org.jvan100.sharedclipboard.ServiceList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientService implements Runnable {

    private final String address;
    private final int port;
    private final ServiceList<ClientService> serviceList;
    private final ClipboardService clipboardService;

    private Socket socket;
    private volatile boolean shutdown;

    public ClientService(String address, int port, ServiceList<ClientService> serviceList, ClipboardService clipboardService) {
        this.address = address;
        this.port = port;
        this.serviceList = serviceList;
        this.clipboardService = clipboardService;
        this.shutdown = false;
    }

    public void run() {
        try {
            System.out.printf("Trying to connect to: %s\n", address);

            socket = new Socket();
            socket.connect(new InetSocketAddress(address, port), 5000);

            System.out.println("Connected");

            synchronized (serviceList) {
                serviceList.addService(this);
            }

            final ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            while (!shutdown) {
                final String message = (String) ois.readObject();
                clipboardService.setClipboard(message);
            }

            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not connect");
        }
    }

    public void shutdown() {
        shutdown = true;

        try {
            socket.close();
        } catch (IOException ignored) {}
    }

}
