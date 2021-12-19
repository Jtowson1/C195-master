package Controller;
import Model.JDBC;
import Utility.Log;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Login Screen Controller Class Variables
 */
public class Login_Screen implements Initializable {
    public Label UserNameLabel;
    public Label PassWordLabel;
    public Label WelcomeLbl;
    public Button SubmitBtn;
    public Button ExitBtn;
    public Label CurrentZoneID;
    Stage stage;
    Parent scene;

    ResourceBundle RB = ResourceBundle.getBundle("Properties/lang", Locale.getDefault());

    @FXML
    private TextField LoginScreenUserNameTxt;

    @FXML
    private TextField LoginScreenPassWordTxt;

    /**
     * Exits from Login Screen and out of the application
     * @param event triggers by clicking the exit button
     */
    @FXML
    void LoginScreenExit(ActionEvent event) {

        System.exit(0);
    }

    /**
     * Sends user to the Main_Form as long as a valid Username and Password have been entered
     * @param event triggers by clicking the submit button
     * @throws IOException
     */
    @FXML
    void LoginScreenSubmit(ActionEvent event) throws IOException {
        /**
         * Logs a timestamp and if the login was successful or not
         */
        Log log = new Log("login_activity.txt");
        log.logger.setLevel(Level.INFO);

        if(LoginScreenUserNameTxt.getText().isBlank() || LoginScreenPassWordTxt.getText().isBlank()){
            Alert alert3 = new Alert(Alert.AlertType.ERROR);
            alert3.setTitle("Error Dialog");
            if (Locale.getDefault().getLanguage().equals("fr")) {
                alert3.setContentText(RB.getString("NoName"));
            } else{
                alert3.setContentText("Please enter a Username and Password!");
            }
            alert3.showAndWait();
            return;
        }
        if (!LoginScreenUserNameTxt.getText().isBlank() || !LoginScreenPassWordTxt.getText().isBlank()) {
            try {
                String sql = "Select count(1) from client_schedule.users Where User_name = '" + LoginScreenUserNameTxt.getText() + "' AND Password = '" + LoginScreenPassWordTxt.getText() + "'";
                PreparedStatement PS = JDBC.getConnection().prepareStatement(sql);
                ResultSet RS = PS.executeQuery();
                while (RS.next()) {
                    try {
                        if (RS.getInt(1) == 1) {
                            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Main_Form.fxml")));
                            stage.setScene(new Scene(scene));
                            stage.show();
                            log.logger.info("Login attempt was successful!");
                        } else{
                            Alert alert2 = new Alert(Alert.AlertType.ERROR);
                            alert2.setTitle("Error Dialog");
                            if (Locale.getDefault().getLanguage().equals("fr")) {
                                alert2.setContentText(RB.getString("Invalid"));
                            } else{
                                alert2.setContentText("Please enter a valid Username and/or Password!");
                            }
                            alert2.showAndWait();
                            log.logger.info("Login attempt was not successful!");
                            return;
                        }
                    } catch(SQLException e){
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.setTitle("Error Dialog");
                        if (Locale.getDefault().getLanguage().equals("fr")) {
                            alert2.setContentText(RB.getString("Invalid"));
                        } else{
                            alert2.setContentText("Please enter a valid Username and/or Password!");
                        }
                        alert2.showAndWait();
                        log.logger.info("Login attempt was not successful!");
                        return;
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * converts all the text on the login screen to french if it detects that as the default language and displays the current time zone
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CurrentZoneID.setText(ZoneId.systemDefault().getId());

        try {
            if (Locale.getDefault().getLanguage().equals("fr")) {
                WelcomeLbl.setText(RB.getString("Welcome"));
                UserNameLabel.setText(RB.getString("Username"));
                PassWordLabel.setText(RB.getString("Password"));
                SubmitBtn.setText(RB.getString("Submit"));
                ExitBtn.setText(RB.getString("Exit"));
                CurrentZoneID.setText(RB.getString("ZoneId"));
            } else {
                Locale.getDefault().getLanguage().equals("en");
            }
        } catch (MissingResourceException e) {

        }
    }
}
