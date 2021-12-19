package Model;

/**
 * Variables for the Countries class
 */
public class Countries {
    private int CountryId;
    private String Country;

    /**
     * Constructor for the Countries class
     * @param countryId
     * @param country
     */
    public Countries(int countryId, String country) {
        CountryId = countryId;
        Country = country;
    }

    public Countries() {

    }

    /**
     * setter and getter for Country ID
     * @return
     */
    public int getCountryId() {
        return CountryId;
    }

    public void setCountryId(int countryId) {
        CountryId = countryId;
    }

    /**
     * setter and getter for Country Name
     * @return
     */
    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    /**
     * Method to convert data into readable content for combo boxes
     * @return
     */
    @Override
    public String toString(){

        return (getCountry());
    }
}




