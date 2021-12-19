package DBConnection;
import Model.Appointments;
import Model.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DBAppointments class to receive data from the SQL database
 */
public class DBAppointments {
    public static ObservableList<Appointments> AppointmentsList = FXCollections.observableArrayList();

    /**
     * method to call when trying to receive all of the appointment data
     * @return
     */
    public static ObservableList<Appointments> getAllAppointments() {
        return AppointmentsList;
    }

    /**
     * method to populate the appointments class with all of the information from the database
     */
    public static void populateAppointments() {
        try {
            AppointmentsList.clear();
            String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM Appointments";
            PreparedStatement PS = JDBC.getConnection().prepareStatement(sql);
            ResultSet RS = PS.executeQuery();
            while (RS.next()) {
                int Appointment_ID = RS.getInt("Appointment_ID");
                String Title = RS.getString("Title");
                String Description = RS.getString("Description");
                String Location = RS.getString("Location");
                String Type = RS.getString("Type");
                Timestamp Start = RS.getTimestamp("Start");
                Timestamp End = RS.getTimestamp("End");
                int Customer_ID = RS.getInt("Customer_ID");
                int User_ID = RS.getInt("User_ID");
                int Contact_ID = RS.getInt("Contact_ID");
               /* DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-ddXXX");
                LocalDateTime startFormat = Start.toLocalDateTime();
                Timestamp start = Timestamp.valueOf(startFormat.format(dtf));*/

                Appointments appointments = new Appointments(Appointment_ID, Title, Description, Location, Type,
                        Start, End, Customer_ID, User_ID, Contact_ID);
                AppointmentsList.add(appointments);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}



