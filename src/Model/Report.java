package Model;

public class Report {
    private String Month;
    private String Type;
    private String Total;

    public Report(String month, String type, String total, int contact_ID, int customer_ID) {
        Month = month;
        Type = type;
        Total = total;
        Contact_ID = contact_ID;
        Customer_ID = customer_ID;
    }

    private int Contact_ID;
    private int Customer_ID;

    public Report(String month, String type, String total) {
    }


    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }
    public int getContact_ID() {
        return Contact_ID;
    }

    public void setContact_ID(int contact_ID) {
        Contact_ID = contact_ID;
    }

    public int getCustomer_ID() {
        return Customer_ID;
    }

    public void setCustomer_ID(int customer_ID) {
        Customer_ID = customer_ID;
    }
}
