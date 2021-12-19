package Controller;

import DBConnection.DBAppointments;
import DBConnection.DBContacts;
import DBConnection.DBCustomers;
import DBConnection.DBReport;
import Model.Appointments;
import Model.Contacts;
import Model.Report;
import Model.Customers;
import Model.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Reports page, showing various reports on the data and variables
 */
public class Reports implements Initializable {


    Stage stage;
    Parent scene;
    @FXML
    public TableView<Report> CustomerTableView;
    @FXML
    private TableColumn<Appointments, Integer> AppointmentID;
    @FXML
    private TableColumn<Report, String> CustomerTotal;
    @FXML
    private ComboBox<Contacts> ContactCombo;

    @FXML
    private TableView<Appointments> ContactTableView;

    @FXML
    private ComboBox<Customers> CustomerCombo;

    @FXML
    private TableColumn<Appointments, Integer> CustomerID;

    @FXML
    private TableColumn<Report, String> CustomerMonth;

    @FXML
    private TableColumn<Report, String> CustomerType;

    @FXML
    private TableColumn<Appointments, String> Description;

    @FXML
    private TableColumn<Appointments, String> End;

    @FXML
    private TableView<Appointments> PastAppointments;

    @FXML
    private TableColumn<Appointments, String> PastEnd;

    @FXML
    private TableColumn<Appointments, String> PastStart;

    @FXML
    private TableColumn<Appointments, String> PastType;

    @FXML
    private TableColumn<Appointments, String> Start;

    @FXML
    private TableColumn<Appointments, String> Title;

    @FXML
    private TableColumn<Appointments, String> Type;
    Appointments appointments;
    public ObservableList<Contacts> contactsData = DBContacts.getAllContacts();
    public ObservableList<Customers> customersData = DBCustomers.getAllCustomers();
    public ObservableList<Appointments> appointmentData = DBAppointments.getAllAppointments();
    public ObservableList<Appointments> pastAppointments = FXCollections.observableArrayList();
    public ObservableList<Report> customerReports = DBReport.getAllReports();
    public ObservableList<Report> monthAppointment = FXCollections.observableArrayList();

    /**
     * Method to populate the Customer Reports Page with appointmet stats
     */
    private void monthAppointments(){
    try{
        PreparedStatement PS = JDBC.getConnection().prepareStatement("select Type, monthname(start) as 'Month', count(*) as 'Total' From client_schedule.appointments group by Type, MONTH(start);");
        ResultSet RS = PS.executeQuery();
        while (RS.next()) {
            String Month = RS.getString("Month");
            String Type = RS.getString("Type");
            String Total = RS.getString("Total");
            monthAppointment.add(new Report(Month, Type, Total));
            CustomerTableView.setItems(monthAppointment);
        }
        } catch (SQLException throwables) {
        throwables.printStackTrace();
        }
    }
    /**
     * method to populate contacts combo box on reports page
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
     * method to populate Customer combo box on reports page
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
     * 1st lambda expression to populate tables based on which contact is chosen out of a combo box
     * @param mouseEvent click on contact combo box sets this expression
     */
    public void populateComboBox(MouseEvent mouseEvent) {
        FilteredList<Appointments> filteredAppointments = new FilteredList<>(appointmentData, b -> true);

        ContactCombo.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            filteredAppointments.setPredicate(Appointment ->{

                if( newValue == null){
                    return true;
                }

                int searchID = ContactCombo.getSelectionModel().getSelectedItem().getContactId();

                if(searchID == Appointment.getContact_ID()){
                    return true;
                } else
                    return false;
            });
        });
        SortedList<Appointments> sortedAppointments = new SortedList<>(filteredAppointments);

        sortedAppointments.comparatorProperty().bind(ContactTableView.comparatorProperty());

        ContactTableView.setItems(sortedAppointments);
    }

    /**
     * Populate the report table on all appointments that have past
     */
    public void populatePastAppointments(){
        LocalDate LD = LocalDate.now();
        LocalTime LT = LocalTime.now();
        LocalDateTime LDT = LocalDateTime.of(LD, LT);
        for(Appointments A : DBAppointments.getAllAppointments()){
            if(A.getStart().toLocalDateTime().isBefore(LDT)){
                pastAppointments.add(A);
            }
        }
    }

    /**
     * 2nd lambda expression to filted the Customer report table by customer choice
     * @param mouseEvent Selecting an option from the Customer Combo triggers this event
     */
    public void PopulateCustomerTable(MouseEvent mouseEvent) throws SQLException {
        FilteredList<Report> filteredAppointments = new FilteredList<>(customerReports, b -> true);

        CustomerCombo.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            filteredAppointments.setPredicate(Report -> {

                if (newValue == null) {
                    return true;
                }

                int searchID = CustomerCombo.getSelectionModel().getSelectedItem().getCustomer_ID();

                if (searchID == Report.getCustomer_ID()) {
                    return true;
                } else
                    return false;
            });
        });
        SortedList<Report> sortedAppointments = new SortedList<>(filteredAppointments);

        sortedAppointments.comparatorProperty().bind(CustomerTableView.comparatorProperty());

        CustomerTableView.setItems(sortedAppointments);
    }

    /**
     * Exits out back to the main form screen
     * @param event press exit button to trigger event
     * @throws IOException
     */
    @FXML
    public void Exit(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Main_Form.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Sets all the tables on the page and populates all the combo boxes
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        monthAppointments();
        DBReport.populateReport();
        populatePastAppointments();
        try {
            populateContact();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            populateCustomer();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /**
         * Setting the three tableview and columns
         */

        ContactTableView.setItems(appointmentData);
        AppointmentID.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        Title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        Type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        Description.setCellValueFactory(new PropertyValueFactory<>("Description"));
        Start.setCellValueFactory(new PropertyValueFactory<>("Start"));
        End.setCellValueFactory(new PropertyValueFactory<>("End"));
        CustomerID.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));

        PastAppointments.setItems(pastAppointments);
        PastType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        PastStart.setCellValueFactory(new PropertyValueFactory<>("Start"));
        PastEnd.setCellValueFactory(new PropertyValueFactory<>("End"));

        CustomerTableView.setItems(customerReports);
        CustomerType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        CustomerMonth.setCellValueFactory(new PropertyValueFactory<>("Month"));
        CustomerTotal.setCellValueFactory(new PropertyValueFactory<>("Total"));
        /**
         * Select default start values for combo boxes
         */
        CustomerCombo.getSelectionModel().select(0);
        ContactCombo.getSelectionModel().select(0);
    }

}
