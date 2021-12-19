package DBConnection;
import Model.Customers;
import Model.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DBCustomers class for connection with the database
 */
public class DBCustomers {
    public static ObservableList<Customers> CustomersList = FXCollections.observableArrayList();

    public static ObservableList<Customers> getAllCustomers() {
        return CustomersList;
    }

    /**
     * Method to populate customers list
     */
    public static void populateCustomerList() {
        try {
            CustomersList.clear();
            String sql = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID FROM Customers";
            PreparedStatement PS = JDBC.getConnection().prepareStatement(sql);
            ResultSet RS = PS.executeQuery();
            while (RS.next()) {
                int Customer_ID = RS.getInt("Customer_ID");
                String Customer_Name = RS.getString("Customer_Name");
                String Address = RS.getString("Address");
                String Postal_Code = RS.getString("Postal_Code");
                String Phone = RS.getString("Phone");
                int Division_ID = RS.getInt("Division_ID");
                Customers customers = new Customers(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID);
                CustomersList.add(customers);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
