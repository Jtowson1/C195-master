package Main;

import Model.*;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Main Class
 */
public class Main extends Application {
    /**
     * Starts the application and opens up the Login Screen
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {

            Parent root = FXMLLoader.load(getClass().getResource("/View/Login_Screen.fxml"));
            stage.setTitle("Login Screen");
            stage.setScene(new Scene(root, 600, 600));
            stage.show();
        }

    /**
     * opens up the connection with the SQL database
     * @param args
     */
    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);

    }
}
