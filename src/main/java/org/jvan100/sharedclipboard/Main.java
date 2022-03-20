package org.jvan100.sharedclipboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jvan100.sharedclipboard.controllers.MainController;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
        final AnchorPane root = loader.load();
        final MainController controller = loader.getController();

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Shared Clipboard");
        primaryStage.setOnCloseRequest(windowEvent -> controller.shutdownServices());
        primaryStage.show();
    }

}
