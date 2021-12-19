package DBConnection;
import Model.Users;
import Model.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DBUsers class for connection with the SQL database
 */
public class DBUsers {
    public static ObservableList<Users> getAllUsers(){
        ObservableList<Users> UsersList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * FROM Users";
            PreparedStatement PS = JDBC.getConnection().prepareStatement(sql);
            ResultSet RS = PS.executeQuery();
            while(RS.next()){
                int UserId = RS.getInt("User_ID");
                String UserName = RS.getString("User_Name");
                String Password = RS.getString("Password");
                Users users = new Users(UserId, UserName, Password);
                UsersList.add(users);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return UsersList;    }}
