package org.jvan100.sharedclipboard;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jvan100.sharedclipboard.controllers.MainController;
import org.jvan100.sharedclipboard.service.ClipboardService;
import org.jvan100.sharedclipboard.service.ServerService;

import java.io.IOException;

public class Main extends Application {

    private String prevContent = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
        final AnchorPane root = loader.load();
        final MainController controller = loader.getController();

        final ServerService serverService = new ServerService(16000);

        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(windowEvent -> {
            serverService.shutdown();
            controller.shutdownServices();
        });

        final Thread serverServiceThread = new Thread(serverService);
        serverServiceThread.start();

        final ClipboardService clipboardService = controller.getClipboardService();

        final Timeline clipboardChange = new Timeline(new KeyFrame(Duration.millis(500), actionEvent -> {
            final String message = clipboardService.getUpdate();

            if (message != null)
                serverService.sendMessage(message);
        }));

        clipboardChange.setCycleCount(Animation.INDEFINITE);
        clipboardChange.play();

        primaryStage.show();
    }

}
