package DBConnection;
import Model.JDBC;
import Model.Countries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * DBCountries class for connection with the database
 */
public class DBCountries {
    public static ObservableList<Countries>getAllCountries(){
        ObservableList<Countries> CountryList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * FROM Countries";
            PreparedStatement PS = JDBC.getConnection().prepareStatement(sql);
            ResultSet RS = PS.executeQuery();
            while(RS.next()){
                int CountryId = RS.getInt("Country_ID");
                String CountryName = RS.getString("Country");
                Countries countries = new Countries(CountryId, CountryName);
                CountryList.add(countries);
            }        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return CountryList;    }}

