package Controller;
import DBConnection.DBAppointments;
import DBConnection.DBCustomers;
import DBConnection.DBUsers;
import Model.Appointments;
import Model.Customers;
import Model.JDBC;
import Model.Users;
import com.mysql.cj.jdbc.JdbcConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Variables for the main form class which is the Main Page of this application
 */
public class Main_Form implements Initializable {
    public Button Reports;
    Stage stage;
    Parent scene;
    @FXML
    private TableColumn<Appointments, Integer> Appointment_ID;
    @FXML
    private TableColumn<Appointments, String> AppointmentTitle;
    @FXML
    private TableColumn<Appointments, String> Description;
    @FXML
    private TableColumn<Appointments, String> Location;
    @FXML
    private TableColumn<Appointments, Integer> Contact_ID;
    @FXML
    private TableColumn<Appointments, String> Type;
    @FXML
    private TableColumn<Appointments, String> Start;
    @FXML
    private TableColumn<Appointments, String> End;
    @FXML
    private TableColumn<Appointments, Integer> Customer_ID;
    @FXML
    private TableColumn<Appointments, Integer> User_ID;
    @FXML
    private TableView<Appointments> AppointmentsTableView;
    @FXML
    private TableColumn<Customers, String> Address;
    @FXML
    private TableColumn<Customers, Integer> Division_ID;
    @FXML
    private TableColumn<Customers, Integer> Ccustomer_ID;
    @FXML
    private TableColumn<Customers, String> Customer_Name;
    @FXML
    private TableColumn<Customers, Integer> Phone;
    @FXML
    private TableColumn<Customers, Integer> Postal_Code;
    @FXML
    private TableView<Customers> CustomersTableView;

    /**
     * Sends user to the add customer interface
     * @param event Pressing the add customer button triggers this event
     * @throws IOException
     */
    @FXML
    void AddCustomer(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Add_Customer.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Sends user to the add appointment screen of the application
     * @param event Pressing the add appointment button triggers this event
     * @throws IOException
     */
    @FXML
    void CreateAppointment(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Add_Appointment.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Deletes selected appointment from the table
     * @param event Pressing the delete button appointment while selecting an appointment triggers this event
     * @throws SQLException
     */
    @FXML
    void DeleteAppointment(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the selected appointment, do you want to continue?");

        Optional<ButtonType> results = alert.showAndWait();
        if (results.isPresent() && results.get() == ButtonType.OK) {
            int A_ID = AppointmentsTableView.getSelectionModel().getSelectedItem().getAppointment_ID();
            PreparedStatement PS = JDBC.getConnection().prepareStatement("DELETE FROM client_schedule.appointments WHERE Appointment_ID = ?");
            PS.setInt(1, A_ID);
            int result = PS.executeUpdate();
            DBAppointments.populateAppointments();
        }
    }

    /**
     * Method to delete all related appointments when deleting a customer
     * @throws SQLException
     */
    public void deleteCustomerAppointments() throws SQLException {
        PreparedStatement PS = JDBC.getConnection().prepareStatement("DELETE FROM client_schedule.appointments WHERE Customer_Id = ?");
        PS.setString(1, String.valueOf(CustomersTableView.getSelectionModel().getSelectedItem().getCustomer_ID()));
        int result = PS.executeUpdate();
    }


    /**
     * Deletes a selected customer from the customer table
     * @param event Pressing the delete customer button while selecting a customer triggers this event
     * @throws SQLException
     */
    @FXML
    void DeleteCustomer(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete selected customer and all related appointments, do you want to continue?");

        Optional<ButtonType> results = alert.showAndWait();
        if (results.isPresent() && results.get() == ButtonType.OK) {
            int C_ID = CustomersTableView.getSelectionModel().getSelectedItem().getCustomer_ID();
            PreparedStatement PS = JDBC.getConnection().prepareStatement("DELETE FROM client_schedule.customers WHERE Customer_ID = ?");
            PS.setInt(1, C_ID);
            int result = PS.executeUpdate();
            deleteCustomerAppointments();
            DBCustomers.populateCustomerList();
            DBAppointments.populateAppointments();
        }
    }

    @FXML
    public void Reports(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Reports.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Sends user back to the login screen
     * @param event Pressing the logout button triggers this event
     * @throws IOException
     */
    @FXML
    void Logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will send you back to the login screen, do you want to continue?");

        Optional<ButtonType> results = alert.showAndWait();
        if (results.isPresent() && results.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Login_Screen.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Sends user to the update appointment controller screen
     * @param event Pressing the update appointment button with an appointment selected triggers this event
     * @throws IOException
     */
    @FXML
    void ModifyAppointment(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/Modify_Appointment.fxml"));
            loader.load();

            Modify_Appointment ModAppointmentControl = loader.getController();
            ModAppointmentControl.getModAppointment(AppointmentsTableView.getSelectionModel().getSelectedItem());
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.showAndWait();
        } catch (IllegalStateException e) {

        } catch (NullPointerException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please Make A Selection!");
            alert.showAndWait();
        }
    }

    /**
     * Sends user to the update customer controller screen
     * @param event Pressing the update customer with a customer selected triggers this event
     * @throws IOException
     */
    @FXML
    void UpdateCustomer(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/Modify_Customer.fxml"));
            loader.load();

            Modify_Customer ModCustomerControl = loader.getController();
            ModCustomerControl.getModCustomer(CustomersTableView.getSelectionModel().getSelectedItem());
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.showAndWait();
        } catch (IllegalStateException e) {

        } catch (NullPointerException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please Make A Selection!");
            alert.showAndWait();
        }
    }

    /**
     * Method to check if there is an upcoming appointments within 15 minutes of the time of arrival at the Main Form screen
     * @return
     */
    private boolean upcomingAppointments() {
        for (Appointments A : DBAppointments.getAllAppointments()) {
            LocalDate appointmentStartD = A.getStart().toLocalDateTime().toLocalDate();
            LocalTime appointmentStartT = A.getStart().toLocalDateTime().toLocalTime();
            ZoneId zoneId = ZoneId.of(TimeZone.getDefault().getID());
            ZonedDateTime appointmentConv = ZonedDateTime.of(appointmentStartD, appointmentStartT, zoneId).withZoneSameInstant(zoneId);


            LocalTime currentTime = LocalTime.now();
            LocalDate currentDate = LocalDate.now();
            LocalDateTime current = LocalDateTime.of(currentDate, currentTime);

            LocalTime searchAppointments = LocalTime.now().plusMinutes(15);
            LocalDateTime search = LocalDateTime.of(currentDate, searchAppointments);
            if (appointmentConv.toLocalDateTime().isBefore(search) && appointmentConv.toLocalDateTime().isAfter(current)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Announcement");
                alert.setContentText("Appointment approaching within 15 minutes" + "\n" + "Appointment ID:" + A.getAppointment_ID() + "\n" + "Start Date/Time:" + A.getStart() + "\n" + "End Date/Time:" + A.getEnd());
                alert.showAndWait();
                return true;
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Announcement");
        alert.setContentText("There are no upcoming appointments within the next 15 minutes!");
        alert.showAndWait();
        return false;
    }

    /**
     * Populates the customer and appointments table and checks to see about any upcoming appointments
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBCustomers.populateCustomerList();
        DBAppointments.populateAppointments();
        CustomersTableView.setItems(DBCustomers.getAllCustomers());
        Customer_Name.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
        Ccustomer_ID.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        Address.setCellValueFactory(new PropertyValueFactory<>("Address"));
        Phone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        Postal_Code.setCellValueFactory(new PropertyValueFactory<>("Postal_Code"));
        Division_ID.setCellValueFactory(new PropertyValueFactory<>("Division_ID"));

        AppointmentsTableView.setItems(DBAppointments.getAllAppointments());
        Appointment_ID.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        AppointmentTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        Description.setCellValueFactory(new PropertyValueFactory<>("Description"));
        Location.setCellValueFactory(new PropertyValueFactory<>("Location"));
        Contact_ID.setCellValueFactory(new PropertyValueFactory<>("Contact_ID"));
        Type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        Start.setCellValueFactory(new PropertyValueFactory<>("Start"));
        End.setCellValueFactory(new PropertyValueFactory<>("End"));
        Customer_ID.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        User_ID.setCellValueFactory(new PropertyValueFactory<>("User_ID"));

        upcomingAppointments();
    }


}
