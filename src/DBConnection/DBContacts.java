package DBConnection;
import Model.Contacts;
import Model.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DBContacts class for connection with the SQL database
 */
public class DBContacts {
    /**
     * method to receive all info from Contacts
     * @return
     */
    public static ObservableList<Contacts> getAllContacts(){
        ObservableList<Contacts> ContactsList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * FROM Contacts";
            PreparedStatement PS = JDBC.getConnection().prepareStatement(sql);
            ResultSet RS = PS.executeQuery();
            while(RS.next()){
                int ContactId = RS.getInt("Contact_ID");
                String ContactName = RS.getString("Contact_Name");
                String Email = RS.getString("Email");
                Contacts contacts= new Contacts(ContactId, ContactName, Email);
                ContactsList.add(contacts);
            }        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ContactsList;    }}

