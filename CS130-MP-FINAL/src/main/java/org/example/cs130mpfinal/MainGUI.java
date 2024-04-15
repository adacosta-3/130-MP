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

/**
 * The MainGUI class serves as the primary class to execute the Quine-McCluskey Calculator application.
 *
 * Extends the JavaFX Application class and overrides the start method to establish the primary stage.
 *
 * @author Arianne Jayne Acosta
 * @author Christian Jesse Bonifacio
 * @version 1.0
 * @since 2024-04-10
 */
public class MainGUI extends Application {

    /**
     * The Main method is the entry point for the JavaFX application.
     *
     * It calls the launch method, which starts the JavaFX application.
     * @param args the command-line arguments. An array of strings passed to the main method which can be used by the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Method which initializes a JavaFX application by loading an FXML file to create the user interface.
     *
     * It sets up the scene with the loaded UI elements and assigns properties to the stage.
     * Displays the stage to start the application.
     * @param stage main GUI
     * @throws Exception throws exceptions if found (such as IOException if FXML file is not found)
     */
    @Override
    public void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource("/org/example/cs130mpfinal/MainGUI.fxml");
        System.out.println("URL" + fxmlUrl); // Validates that url is found and is not null

        if (fxmlUrl == null) {
            throw new IOException("FXML file not found");
        }

        Parent root = FXMLLoader.load(fxmlUrl);
        Scene scene = new Scene(root);

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png")));
        stage.setTitle("Quine-McCluskey Calculator");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("font.css")).toExternalForm());
        stage.setResizable(false);
        stage.show();
    }
}