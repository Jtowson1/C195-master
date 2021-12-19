package Controller;

import DBConnection.DBAppointments;
import DBConnection.DBContacts;
import DBConnection.DBCustomers;
import DBConnection.DBUsers;
import Model.*;
import javafx.collections.ObservableList;
import javafx.css.converter.StringConverter;
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
import java.time.format.FormatStyle;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Modify Appointment Controller class and variables
 */
public class Modify_Appointment implements Initializable {
    Stage stage;
    Parent scene;
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
    /**
     * Observable List to populate all the combo boxes on the application
      */
    public ObservableList<Users> usersData = DBUsers.getAllUsers();
    public ObservableList<Customers> customersData = DBCustomers.getAllCustomers();
    public ObservableList<Contacts> contactsData = DBContacts.getAllContacts();
    public ObservableList<Appointments> appointmentsData = DBAppointments.getAllAppointments();
    @FXML
    private ComboBox<Customers> CustomerCombo;
    @FXML
    private ComboBox<Contacts> ContactCombo;
    @FXML
    private ComboBox<Users> UserCombo;

    /**
     * Method to receive the selected appointment from the main form screen
     * @param appointments
     */
    public void getModAppointment(Appointments appointments){
        LocalTime Startlt = appointments.getStart().toLocalDateTime().toLocalTime();
        LocalDate Startld = appointments.getStart().toLocalDateTime().toLocalDate();

        LocalTime Endlt = appointments.getEnd().toLocalDateTime().toLocalTime();
        LocalDate Endld = appointments.getEnd().toLocalDateTime().toLocalDate();

        ZoneId zoneID = ZoneId.of("UTC");
        ZoneId zoneId = ZoneId.of(TimeZone.getDefault().getID());

        ZonedDateTime Startzdt = ZonedDateTime.of(Startld, Startlt, zoneID).withZoneSameInstant(zoneId);
        ZonedDateTime Endzdt = ZonedDateTime.of(Endld, Endlt, zoneID).withZoneSameInstant(zoneId);


        AppointmentIDTxt.setText(String.valueOf(appointments.getAppointment_ID()));
        AppointmentTitleTxt.setText(appointments.getTitle());
        DescriptionTxt.setText(appointments.getDescription());
        LocationTxt.setText(appointments.getLocation());
        TypeTxt.setText(appointments.getType());
        StartDateMenu.setValue(Startld);
        StartDateTimeTxt.setText(String.valueOf(Startlt));
        EndDateMenu.setValue(Endld);
        EndDateTimeTxt.setText(String.valueOf(Endlt));
        for(Customers C : customersData){
            if(appointments.getCustomer_ID() == C.getCustomer_ID()){
                CustomerCombo.setValue(C);
            }
        }
        for(Users U : usersData) {
            if (appointments.getUser_ID() == U.getUserId()) {
                UserCombo.setValue(U);
            }
        }
        for(Contacts C : contactsData) {
            if (appointments.getContact_ID() == C.getContactId()) {
                ContactCombo.setValue(C);
            }
        }
    }

    /**
     * method to populate the user combo box
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
     * method to populate the customer combo box
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
     * method to populate the contact combo box
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
     * Sends user back to the Main Form screen
     * @param event Pressing the cancel button triggers this event
     * @throws IOException
     */
    @FXML
    void CancelAddAppointment(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment Updates have not been saved, would you like to continue?");

        Optional<ButtonType> results = alert.showAndWait();
        if (results.isPresent() && results.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Main_Form.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Saves the changes made to the selected appointment and converts the time back to UTC for storage in the database
     * @param event Pressing the save button triggers this event
     * @throws IOException
     */
    @FXML
    void SaveNewAppointment(ActionEvent event) throws IOException {
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
        if (ZDT.toLocalTime().isBefore(LocalTime.from(StartVerify)) || zDT.toLocalTime().isAfter(LocalTime.from(EndVerify))) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Appointment start time must be within business hours of 8AM - 10PM EST!");
            alert.showAndWait();
            return;
        }

        for (Appointments A : appointmentsData)
            if (A.getCustomer_ID() == CustomerCombo.getSelectionModel().getSelectedItem().getCustomer_ID()) {
                if (A.getStart().toLocalDateTime().isBefore(end) && A.getStart().toLocalDateTime().isAfter(start) || A.getEnd().toLocalDateTime().isBefore(end) && A.getEnd().toLocalDateTime().isAfter(start)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setContentText("Overlapping appointment set for Customer!");
                    alert.showAndWait();
                    return;
                }
            }
        if (start.equals(end)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Appointment Start and End are at the same time!");
            alert.showAndWait();
            return;
        }

        try {
                    PreparedStatement PS = JDBC.getConnection().prepareStatement("UPDATE client_schedule.appointments SET Title= ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?");
                    PS.setString(1, AppointmentTitleTxt.getText());
                    PS.setString(2, DescriptionTxt.getText());
                    PS.setString(3, LocationTxt.getText());
                    PS.setString(4, TypeTxt.getText());
                    PS.setTimestamp(5, StartZDT);
                    PS.setTimestamp(6, EndZDT);
                    PS.setInt(7, CustomerCombo.getValue().getCustomer_ID());
                    PS.setInt(8, UserCombo.getValue().getUserId());
                    PS.setInt(9, ContactCombo.getValue().getContactId());
                    PS.setInt(10, Integer.parseInt(AppointmentIDTxt.getText()));
                    PS.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setContentText("Please make sure all text fields are filled in correctly!");
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
     * Disables the appointment ID text field and populates the three combo boxes
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AppointmentIDTxt.setDisable(true);
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
