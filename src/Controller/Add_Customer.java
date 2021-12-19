package Controller;
import DBConnection.DBCountries;
import DBConnection.DBCustomers;
import DBConnection.DBDivisions;
import Model.Countries;
import Model.Customers;
import Model.First_Level_Divisions;
import Model.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Add Customer controller class and variables from FXML
 */
public class Add_Customer implements Initializable {
    Stage stage;
    Parent scene;
    @FXML
    private TextField CustomerIDTxt;
    @FXML
    private ComboBox<First_Level_Divisions> DivisionCombo;
    @FXML
    private TextField CustomerAddressTxt;
    @FXML
    private TextField CustomerNameTxt;
    @FXML
    private TextField CustomerPhoneNumberTxt;
    @FXML
    private TextField CustomerPostalCodeTxt;
    @FXML
    private ComboBox<Countries> CountryCombo;
    /**
     * Observable Lists to populate the Country and Division Combo Boxes
     */
    public ObservableList<First_Level_Divisions> DivisionData = FXCollections.observableArrayList();
    public ObservableList<Countries> CountryData = DBCountries.getAllCountries();

    /**
     * Cancels out of Add Customer form and back to Main Form
     * @param event Press the Cancel Button on the Add Customer class
     * @throws IOException
     */
    @FXML
    void CancelAddCustomer(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "New Customer will not been saved, would you like to continue?");

        Optional<ButtonType> results = alert.showAndWait();
        if (results.isPresent() && results.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Main_Form.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Saves new customer as long as all fields are entered correctly
     * @param event Press the Save button triggers this
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    void SaveNewCustomer(ActionEvent event) throws SQLException, IOException {
        try {
            PreparedStatement PS = JDBC.getConnection().prepareStatement("INSERT INTO client_schedule.customers (Customer_Name, Address, Postal_code, Phone, Division_ID)" +
                    "VALUES (?, ?, ?, ?, ?)");
                PS.setString(1, CustomerNameTxt.getText());
                PS.setString(2, CustomerAddressTxt.getText());
                PS.setString(3, CustomerPostalCodeTxt.getText());
                PS.setString(4, CustomerPhoneNumberTxt.getText());
                PS.setInt(5, DivisionCombo.getValue().getDivision_ID());
            int result = PS.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Main_Form.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Clears and populates the division ComboBox based on Country choice
     * @param mouseEvent
     */
    public void DivisionComboBox(MouseEvent mouseEvent) {
        try {
            DivisionData.clear();
            int C_ID = CountryCombo.getSelectionModel().getSelectedItem().getCountryId();
            for (First_Level_Divisions Divisions : DBDivisions.getAllDivisions()) {
                if (Divisions.getCountry_ID() == C_ID) {
                    DivisionData.add(Divisions);
                }
            }
            DivisionCombo.setItems(DivisionData);
        } catch (NullPointerException e) {

        }
    }

    /**
     * Populates the Country Combo Box
     * @param mouseEvent
     * @throws SQLException
     */
    public void CountryComboBox(MouseEvent mouseEvent) throws SQLException {
        String sql = "SELECT * from Countries";
        PreparedStatement PS = JDBC.getConnection().prepareStatement(sql);
        ResultSet RS = PS.executeQuery();
        while (RS.next()) {
            Countries countries = new Countries();
            countries.setCountry(RS.getString("Country"));
            CountryCombo.setItems(CountryData);
            DivisionData.clear();
        }
    }

    /**
     * Disables Customer ID text field
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CustomerIDTxt.setDisable(true);

    }

}