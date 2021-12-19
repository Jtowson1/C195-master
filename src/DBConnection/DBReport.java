package DBConnection;
import Model.Appointments;
import Model.JDBC;
import Model.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * DBAppointments class to receive data from the SQL database
 */
public class DBReport {
    public static ObservableList<Report> ReportList = FXCollections.observableArrayList();

    /**
     * method to call when trying to receive all of the appointment data
     * @return
     */
    public static ObservableList<Report> getAllReports() {
        return ReportList;
    }

    /**
     * method to populate the appointments class with all of the information from the database
     */
    public static void populateReport() {
        try {
            ReportList.clear();
            String sql = "select Customer_ID, Contact_ID, Type, monthname(start) as 'Month', count(*) as 'Total' From client_schedule.appointments group by Type, MONTH(start), Customer_ID;";
            PreparedStatement PS = JDBC.getConnection().prepareStatement(sql);
            ResultSet RS = PS.executeQuery();
            while (RS.next()) {
                String Month = RS.getString("Month");
                String Type = RS.getString("Type");
                String Total = RS.getString("Total");
                int Customer_ID = RS.getInt("Customer_ID");
                int Contact_ID = RS.getInt("Contact_ID");
                Report report = new Report(Month, Type, Total, Contact_ID, Customer_ID);
                ReportList.add(report);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}



