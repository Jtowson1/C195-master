package Model;

/**
 * variables for the Contacts class
 */
public class Contacts {
    private int ContactId;
    private String ContactName;
    private String Email;

    /**
     * constructor for the Contacts class
     * @param contactId
     * @param contactName
     * @param email
     */
    public Contacts(int contactId, String contactName, String email) {
        ContactId = contactId;
        ContactName = contactName;
        Email = email;
    }

    public Contacts() {

    }

    /**
     * setter and getter for contact ID
     * @return
     */
    public int getContactId() {
        return ContactId;
    }

    public void setContactId(int contactId) {
        ContactId = contactId;
    }

    /**
     * setter and getter for contact name
     * @return
     */
    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    /**
     * setter and getter for contact email
     * @return
     */
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    /**
     * method to convert this to readable content for combo boxes
     * @return
     */
    @Override
    public String toString(){

        return Integer.toString(getContactId()) + " " + getContactName();
    }
}


