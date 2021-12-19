package Controller;

import DBConnection.DBAppointments;
import DBConnection.DBContacts;
import DBConnection.DBCustomers;
import DBConnection.DBUsers;
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Controller Class for the Add Appointment page
 */
public class Add_Appointment implements Initializable {
    Parent scene;
    Stage stage;

    @FXML
    private TextField AppointmentIDTxt;

    @FXML
    private TextField AppointmentTitleTxt;

    @FXML
    private TextField DescriptionTxt;

    @FXML
    private DatePicker EndDateMenu;

    @FXML
    private TextField EndDateTimeTxt;

    @FXML
    private TextField LocationTxt;

    @FXML
    private DatePicker StartDateMenu;

    @FXML
    private TextField StartDateTimeTxt;

    @FXML
    private TextField TypeTxt;
    public ComboBox<Customers> CustomerCombo;
    public ComboBox<Users> UserCombo;
    public ComboBox<Contacts> ContactCombo;

    @FXML
    private TextField UserIDTxt;
    /**
     * Observable lists for populating combo boxes
     */
    public ObservableList<Users> usersData = DBUsers.getAllUsers();
    public ObservableList<Customers> customersData = DBCustomers.getAllCustomers();
    public ObservableList<Contacts> contactsData = DBContacts.getAllContacts();
    public ObservableList<Appointments> appointmentsData = DBAppointments.getAllAppointments();

    /**
     * Method to populate User Combo box
     * @throws SQLException
     */
    public void populateUser() throws SQLException {
        String sql = "SELECT * from client_schedule.users";
        PreparedStatement PS = JDBC.getConnection().prepareStatement(sql);
        ResultSet RS = PS.executeQuery();
        while (RS.next()) {
            Users users = new Users();
            users.setUserId(RS.getInt("User_ID"));
            UserCombo.setItems(usersData);
        }
    }

    /**
     * Method to populate Customer Combo box
     * @throws SQLException
     */
    public void populateCustomer() throws SQLException {
        String sql = "SELECT * from client_schedule.customers";
        PreparedStatement PS = JDBC.getConnection().prepareStatement(sql);
        ResultSet RS = PS.executeQuery();
        while (RS.next()) {
            Customers customers = new Customers();
            customers.setCustomer_ID(RS.getInt("Customer_ID"));
            CustomerCombo.setItems(customersData);
        }
    }

    /**
     * Method to populate Contact Combo box
     * @throws SQLException
     */
    public void populateContact() throws SQLException {
        String sql = "SELECT * from client_schedule.contacts";
        PreparedStatement PS = JDBC.getConnection().prepareStatement(sql);
        ResultSet RS = PS.executeQuery();
        while (RS.next()) {
            Contacts contacts = new Contacts();
            contacts.setContactId(RS.getInt("Contact_ID"));
            ContactCombo.setItems(contactsData);
        }
    }

    /**
     * Method to cancel out of Main Form and go back to log in screen
     * @param event Click Cancel on the Add Appointment Screen
     * @throws IOException
     */
    @FXML
    void CancelAddAppointment(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "New Appointment will not been saved, would you like to continue?");

        Optional<ButtonType> results = alert.showAndWait();
        if (results.isPresent() && results.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Main_Form.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Saves new appointments made in the add appointment screen
     * @param event Click the save button on add appointment screen
     * @throws IOException
     */
    @FXML
    void SaveNewAppointment(ActionEvent event) throws IOException {
        /**
         * variables to convert from string to timestamp to local time to zone time and back to timestamp
         */
        ZoneId zoneId = ZoneId.of(TimeZone.getDefault().getID());
        ZoneId businessHours = ZoneId.of("America/New_York");

        LocalDate StartLD = StartDateMenu.getValue();
        LocalTime StartLT = LocalTime.parse(StartDateTimeTxt.getText());
        LocalTime startVerify = LocalTime.of(8, 0);
        LocalDate EndLD = EndDateMenu.getValue();
        LocalTime EndLT = LocalTime.parse(EndDateTimeTxt.getText());
        LocalTime endVerify = LocalTime.of(22, 0);
        LocalDateTime start = LocalDateTime.of(StartLD,StartLT);
        Timestamp StartZDT = Timestamp.valueOf(start);
        LocalDateTime end = LocalDateTime.of(EndLD, EndLT);
        Timestamp EndZDT = Timestamp.valueOf(end);


        ZonedDateTime ZDT = ZonedDateTime.of(StartLD, StartLT, zoneId);
        ZonedDateTime zDT = ZonedDateTime.of(EndLD, EndLT, zoneId);
        ZonedDateTime StartVerify = ZonedDateTime.of(StartLD, startVerify, businessHours);
        ZonedDateTime EndVerify = ZonedDateTime.of(EndLD, endVerify, businessHours);


/**
 * input validation checks and time overlap checks
 */
        if (AppointmentTitleTxt.getText().isBlank() || DescriptionTxt.getText().isBlank() || LocationTxt.getText().isBlank() || TypeTxt.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please make sure all text fields are filled in!");
            alert.showAndWait();
            return;
        }
        if (CustomerCombo.getSelectionModel().isEmpty() || UserCombo.getSelectionModel().isEmpty() || ContactCombo.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please make sure all text fields are filled in!");
            alert.showAndWait();
            return;
        }
        if (start.isAfter(end)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Appointment start date/time must be before the end date/time!");
            alert.showAndWait();
            return;
        }
        if (ZDT.toLocalDateTime().toLocalTime().isBefore(LocalTime.from(StartVerify)) || (zDT.toLocalDateTime().toLocalTime().isAfter(LocalTime.from(EndVerify)))) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Appointment Start and End time must be within business hours of 8AM - 10PM EST!");
            alert.showAndWait();
            return;
        }
        if (start.equals(end)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Appointment Start and End are at the same time!");
            alert.showAndWait();
            return;
        }
        for (Appointments A : appointmentsData)
            if (A.getCustomer_ID() == CustomerCombo.getSelectionModel().getSelectedItem().getCustomer_ID()) {
                if (A.getStart().toLocalDateTime().isBefore(end) && A.getStart().toLocalDateTime().isAfter(start) || A.getStart().toLocalDateTime().equals(start)
                || A.getEnd().toLocalDateTime().isBefore(end) && A.getEnd().toLocalDateTime().isAfter(start)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setContentText("Overlapping appointment set for Customer!");
                    alert.showAndWait();
                    return;
                }
            }

        /**
         * Save statement converting to sql for database entry
         */
             try {
                PreparedStatement PS = JDBC.getConnection().prepareStatement("INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                PS.setString(1, AppointmentTitleTxt.getText());
                PS.setString(2, DescriptionTxt.getText());
                PS.setString(3, LocationTxt.getText());
                PS.setString(4, TypeTxt.getText());
                PS.setTimestamp(5, StartZDT);
                PS.setTimestamp(6, EndZDT);
                PS.setInt(7, CustomerCombo.getValue().getCustomer_ID());
                PS.setInt(8, UserCombo.getValue().getUserId());
                PS.setInt(9, ContactCombo.getValue().getContactId());
                PS.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Please make sure all text fields are filled in correctly!");
                alert.showAndWait();
            } catch (DateTimeParseException g){
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("Error Dialog");
                 alert.setContentText("Please enter valid time format: HH:mm Ex: 8:00 = 08:00");
                 alert.showAndWait();
             } catch (DateTimeException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Please enter valid time: 0-23 hours and 0-59 minutes");
                alert.showAndWait();
            }
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Main_Form.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Disables Appointment ID field, Sets start date/end date, and populates the ID combo boxes
     * @param location
     * @param resources
     */
        @Override
    public void initialize(URL location, ResourceBundle resources) {
        AppointmentIDTxt.setDisable(true);
        StartDateMenu.setValue(LocalDate.now());
        EndDateMenu.setValue(LocalDate.now());
            try {
                populateUser();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                populateCustomer();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                populateContact();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
}
