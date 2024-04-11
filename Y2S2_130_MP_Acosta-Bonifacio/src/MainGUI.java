import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        URL fxmlUrl = getClass().getResource("MainGUI");
        System.out.println("URL" + fxmlUrl);
        if (fxmlUrl == null) {
            throw new IOException("FXML file not found");
        }
        Parent root = FXMLLoader.load(fxmlUrl);

        Scene scene = new Scene(root);
        stage.setTitle("Quine-McCluskey Calculator");
        Image appicon = new Image(getClass().getResourceAsStream("resources/placeholder.jpg"));
        //Image appicon = new Image("/imageassets/app_icon.png");
        stage.getIcons().add(appicon);
        stage.setScene(scene);
        stage.show();
    }
}