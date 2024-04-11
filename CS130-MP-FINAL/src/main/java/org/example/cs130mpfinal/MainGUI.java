package org.example.cs130mpfinal;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class MainGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        URL fxmlUrl = getClass().getResource("/org/example/cs130mpfinal/MainGUI.fxml");
        System.out.println("URL" + fxmlUrl);
        if (fxmlUrl == null) {
            throw new IOException("FXML file not found");
        }
        Parent root = FXMLLoader.load(fxmlUrl);

        Scene scene = new Scene(root);
        stage.setTitle("Quine-McCluskey Calculator");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png")));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}