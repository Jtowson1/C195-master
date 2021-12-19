package Model;


import java.sql.Timestamp;

/**
 * Variables for the appointments class
 */
public class Appointments {
    private int Appointment_ID;
    private String Title;
    private String Description;
    private String Location;
    private String Type;
    private Timestamp Start;
    private Timestamp End;
    private int Customer_ID;
    private int User_ID;
    private int Contact_ID;

    /**
     * constructor for the appointments class
     * @param appointmentId
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerId
     * @param userId
     * @param contactId
     */
    public Appointments(int appointmentId, String title, String description, String location, String type, Timestamp start, Timestamp end, int customerId, int userId, int contactId) {
        Appointment_ID = appointmentId;
        Title = title;
        Description = description;
        Location = location;
        Type = type;
        Start = start;
        End = end;
        Customer_ID = customerId;
        User_ID = userId;
        Contact_ID = contactId;
    }

    /**
     * setter and getter for the appointment_ID variable
     * @param appointment_ID
     */
    public void setAppointment_ID(int appointment_ID) {
        Appointment_ID = appointment_ID;
    }

    public int getAppointment_ID() {
        return Appointment_ID;
    }

    /**
     * setter and getter for appointment title
     * @return
     */
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    /**
     * setter and getter for appointment description
     * @return
     */
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    /**
     * setter and getter for appointment location
     * @return
     */
    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    /**
     * setter and getter for appointment type
     * @return
     */
    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    /**
     * setter and getter for appointment start date/time
     * @return
     */
    public Timestamp getStart() {
        return Start;
    }

    public void setStart(Timestamp start) {
        Start = start;
    }

    /**
     * setter and getter for appointment end date/time
     * @return
     */
    public Timestamp getEnd() {
        return End;
    }

    public void setEnd(Timestamp end) {
        End = end;
    }

    /**
     * setter and getter for customer ID
     * @return
     */
    public int getCustomer_ID() {
        return Customer_ID;
    }

    public void setCustomer_ID(int customer_ID) {
        Customer_ID = customer_ID;
    }

    /**
     * setter and getter for User ID
     * @return
     */
    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    /**
     * setter and getter for contact ID
     * @return
     */
    public int getContact_ID() {
        return Contact_ID;
    }

    public void setContact_ID(int contact_ID) {
        Contact_ID = contact_ID;
    }


}
