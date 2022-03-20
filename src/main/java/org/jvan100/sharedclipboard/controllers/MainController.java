package org.jvan100.sharedclipboard.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.jvan100.sharedclipboard.util.Connection;
import org.jvan100.sharedclipboard.util.ConnectionsList;
import org.jvan100.sharedclipboard.service.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Timer;

public class MainController {

    private final int port = 16000;
    private final int updatePeriod = 1000;

    private final ConnectionsList connectionsList;

    private final ClipboardService clipboardService;
    private final ServerConnectionService serverConnectionService;
    private final ClientConnectionService clientConnectionService;
    private final BroadcastService broadcastService;

    private final Timer timer;

    @FXML
    private Label hostAddressLabel;

    @FXML
    private TextField addressField;

    @FXML
    private Label notificationLabel;

    public MainController() {
        this.connectionsList = new ConnectionsList();
        this.clipboardService = new ClipboardService();
        this.serverConnectionService = new ServerConnectionService(port, connectionsList, clipboardService, this::setNotification);
        this.clientConnectionService = new ClientConnectionService(port, connectionsList, clipboardService, this::setNotification);
        this.broadcastService = new BroadcastService(connectionsList, clipboardService);

        this.timer = new Timer();

        startServices();
    }

    public void initialize() throws IOException {
        hostAddressLabel.setText(InetAddress.getLocalHost().getHostAddress());
    }

    public void startServices() {
        new Thread(serverConnectionService).start();

        Platform.runLater(() -> timer.schedule(broadcastService, 0, updatePeriod));
    }

    public void setNotification(String notification) {
        Platform.runLater(() -> notificationLabel.setText(notification));
    }

    @FXML
    private void connectToTarget() {
        clientConnectionService.setAddress(addressField.getText());
        new Thread(clientConnectionService).start();
    }

    public void shutdownServices() {
        serverConnectionService.shutdown();
        timer.cancel();

        try {
            synchronized (connectionsList) {
                for (final Connection connection : connectionsList.getConnections())
                    connection.close();
            }
        } catch (IOException ignored) {}
    }

}
