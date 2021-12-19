package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Variables for the Customers Class
 */
public class Customers {
    private int Customer_ID;
    private String Customer_Name;
    private String Address;
    private String Postal_Code;
    private String Phone;
    private int Division_ID;

    /**
     * Constructor for the Customers class
     * @param customerId
     * @param customerName
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @param division_ID
     */
    public Customers(int customerId, String customerName, String address, String postalCode, String phoneNumber, int division_ID) {
        Customer_ID = customerId;
        Customer_Name = customerName;
        Address = address;
        Postal_Code = postalCode;
        Phone = phoneNumber;
        Division_ID = division_ID;
    }

    public Customers() {

    }

    /**
     * setter and getter for Customer ID
     * @return
     */
    public int getCustomer_ID() {
        return Customer_ID;
    }

    public void setCustomer_ID(int customer_ID) {
        Customer_ID = customer_ID;
    }

    /**
     * Setter and Getter for Customer Name
     * @return
     */
    public String getCustomer_Name() {
        return Customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    /**
     * Setter and Getter for Customer Address
     * @return
     */
    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    /**
     * Setter and Getter for Customer Postal Code
     * @return
     */
    public String getPostal_Code() {
        return Postal_Code;
    }

    public void setPostal_Code(String postal_Code) {
        Postal_Code = postal_Code;
    }

    /**
     * Setter and Getter for Customer Phone Number
     * @return
     */
    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    /**
     * Setter and Getter for Customer Division ID
     * @return
     */
    public int getDivision_ID() {
        return Division_ID;
    }

    public void setDivision_ID(int division_ID) {
        Division_ID = division_ID;
    }

    /**
     * Method to convert data into readable content for combo boxes
     * @return
     */
    @Override
    public String toString(){

        return Integer.toString(getCustomer_ID()) + " " + getCustomer_Name();
    }
}

