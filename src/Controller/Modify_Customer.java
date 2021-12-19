package Controller;

import DBConnection.DBCountries;
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
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
 * Modify Customer Controller class and variables
 */
public class Modify_Customer implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private ComboBox<Countries> CountryCombo;

    @FXML
    private TextField CustomerAddressTxt;

    @FXML
    private TextField CustomerIDTxt;

    @FXML
    private TextField CustomerNameTxt;

    @FXML
    private TextField CustomerPhoneNumberTxt;

    @FXML
    private TextField CustomerPostalCodeTxt;

    @FXML
    private ComboBox<First_Level_Divisions> FirstLevelDivisionCombo;
    public ObservableList<First_Level_Divisions> DivisionData = FXCollections.observableArrayList();
    public ObservableList<Countries> CountryData = DBCountries.getAllCountries();

    /**
     * Method to receive the selected customer from the main form page
     * @param customers
     */
    public void getModCustomer(Customers customers) {
        CustomerNameTxt.setText(customers.getCustomer_Name());
        CustomerAddressTxt.setText(customers.getAddress());
        CustomerPostalCodeTxt.setText(customers.getPostal_Code());
        CustomerPhoneNumberTxt.setText(customers.getPhone());
        CustomerIDTxt.setText(String.valueOf((customers.getCustomer_ID())));
        DivisionData = DBDivisions.getAllDivisions();
        for (First_Level_Divisions D : DivisionData) {
            if (customers.getDivision_ID() == D.getDivision_ID()) {
                FirstLevelDivisionCombo.setValue(D);
                for (Countries C : CountryData) {
                    if (D.getCountry_ID() == C.getCountryId()) {
                        CountryCombo.setValue(C);
                    }
                }
            }
        }
    }

    /**
     * cancels out of the modify customer page and sends user back to the main form
     * @param event pressing the cancel button triggers this event
     * @throws IOException
     */
    @FXML
    void CancelAddCustomer(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Customer Updates have not been saved, would you like to continue?");

        Optional<ButtonType> results = alert.showAndWait();
        if (results.isPresent() && results.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Main_Form.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * populates the division combo box based on the choice of country
     * @param mouseEvent click on the combo box triggers this event
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
            FirstLevelDivisionCombo.setItems(DivisionData);
        } catch (NullPointerException e) {

        }
    }

    /**
     * Populates the country combo box selection
     * @param mouseEvent clicking on the country combo box triggers this event
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
            FirstLevelDivisionCombo.setValue(null);
        }
    }

    /**
     * Saves the changes made in the modify customer fields
     * @param event clicked save triggers this event
     * @throws IOException
     */
    @FXML
    void SaveNewCustomer(ActionEvent event) throws IOException {
        try {
            PreparedStatement PS = JDBC.getConnection().prepareStatement("UPDATE client_schedule.customers SET customer_Name = ?, Address = ?, Postal_code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?");
                PS.setString(1, CustomerNameTxt.getText());
                PS.setString(2, CustomerAddressTxt.getText());
                PS.setString(3, CustomerPostalCodeTxt.getText());
                PS.setString(4, CustomerPhoneNumberTxt.getText());
                PS.setInt(5, FirstLevelDivisionCombo.getValue().getDivision_ID());
                PS.setInt(6, Integer.parseInt(CustomerIDTxt.getText()));
                int result = PS.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Main_Form.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * disables the customer id text field
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CustomerIDTxt.setDisable(true);

    }
}

