package Model;

/**
 * Variables for First Level Division class
 */
public class First_Level_Divisions {
    private int Division_ID;
    private String Division;
    private int Country_ID;

    /**
     * constructor for first level division class
     * @param divisionId
     * @param division
     * @param countryId
     */
    public First_Level_Divisions(int divisionId, String division, int countryId) {
        Division_ID = divisionId;
        Division = division;
        Country_ID = countryId;
    }

    public First_Level_Divisions() {

    }

    /**
     * setter and getter for Division ID
     * @return
     */
    public int getDivision_ID() {
        return Division_ID;
    }

    public void setDivision_ID(int division_ID) {
        Division_ID = division_ID;
    }

    /**
     * Setter and Getter for Division name
     * @return
     */
    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
    }

    /**
     * Setter and Getter for Country ID
     * @return
     */
    public int getCountry_ID() {
        return Country_ID;
    }

    public void setCountry_ID(int country_ID) {
        Country_ID = country_ID;
    }

    /**
     * Method for convert data into readable content for combo boxes
     * @return
     */
    @Override
    public String toString(){

        return (getDivision());
    }

}