package Model;

/**
 * Variables for the Users class
 */
public class Users {
    private int UserId;
    private String UserName;
    private String Password;

    /**
     * Constructor for the Users class
     * @param userId
     * @param userName
     * @param password
     */
    public Users(int userId, String userName, String password) {
        UserId = userId;
        UserName = userName;
        Password = password;
    }

    public Users() {

    }

    /**
     * Setter and Getter for User ID
     * @return
     */
    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    /**
     * Setter and Getter for User Name
     * @return
     */
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    /**
     * Setter and Getter for User Password
     * @return
     */
    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    /**
     * Method to convert data into readable content for combo boxes
     * @return
     */
    @Override
    public String toString(){

        return Integer.toString(getUserId()) + " " + getUserName();
    }
}


