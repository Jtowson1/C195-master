package DBConnection;
import Model.First_Level_Divisions;
import Model.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DBDivisions class for connection with the SQL database
 */
public class DBDivisions {
    public static ObservableList<First_Level_Divisions> getAllDivisions(){
        ObservableList<First_Level_Divisions> DivisionsList = FXCollections.observableArrayList();
        try{

            String sql = "SELECT * FROM client_schedule.first_level_divisions;";
            PreparedStatement PS = JDBC.getConnection().prepareStatement(sql);
            ResultSet RS = PS.executeQuery();
            while(RS.next()){
                int Division_ID = RS.getInt("Division_ID");
                String Division = RS.getString("Division");
                int Country_ID = RS.getInt("Country_ID");
                First_Level_Divisions divisions = new First_Level_Divisions(Division_ID, Division, Country_ID);
                DivisionsList.add(divisions);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return DivisionsList;
    }

}

