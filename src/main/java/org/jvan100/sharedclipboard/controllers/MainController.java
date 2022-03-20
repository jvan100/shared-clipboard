package org.jvan100.sharedclipboard.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.jvan100.sharedclipboard.ServiceList;
import org.jvan100.sharedclipboard.service.ClientService;
import org.jvan100.sharedclipboard.service.ClipboardService;

import java.io.IOException;
import java.net.InetAddress;

public class MainController {

    private final ClipboardService clipboardService;

    private final ServiceList<ClientService> serviceList;

    @FXML
    private Label hostAddressLabel;

    @FXML
    private TextField addressField;

    public MainController() {
        this.clipboardService = new ClipboardService();
        this.serviceList = new ServiceList<>();
    }

    public void initialize() throws IOException {
        hostAddressLabel.setText(InetAddress.getLocalHost().getHostAddress());
    }

    @FXML
    private void connectToTarget() {
        final ClientService clientService = new ClientService(addressField.getText(), 16000, serviceList, clipboardService);
        final Thread clientServiceThread = new Thread(clientService);
        clientServiceThread.start();
    }

    public ClipboardService getClipboardService() {
        return clipboardService;
    }

    public void shutdownServices() {
        synchronized (serviceList) {
            for (final ClientService clientService : serviceList.getServices())
                clientService.shutdown();
        }
    }

}
