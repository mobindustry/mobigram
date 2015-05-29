package net.mobindustry.telegram.utils;

import java.io.Serializable;

/**
 * Created by alytar on 28.05.15.
 */
public class CountryObject implements Serializable {

    private String countryCode;
    private String countryName;
    private String countryStringCode;
    private String initialLetter;


    public String getInitialLetter() {
        return initialLetter;
    }

    public void setInitialLetter(String initialLetter) {
        this.initialLetter = initialLetter;
    }

    public CountryObject() {

    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryStringCode() {
        return countryStringCode;
    }

    public void setCountryStringCode(String countryStringCode) {
        this.countryStringCode = countryStringCode;
    }
}
