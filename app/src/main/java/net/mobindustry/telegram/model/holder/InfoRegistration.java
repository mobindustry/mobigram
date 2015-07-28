package net.mobindustry.telegram.model.holder;

import net.mobindustry.telegram.utils.CountryObject;
import net.mobindustry.telegram.utils.ListCountryObject;

import java.util.List;

public class InfoRegistration {

    private static InfoRegistration instance;

    private String countryName = "";
    private String codeCountry = "+";
    private String codeCountryLetters = "";
    private String phone = "";
    private String codePlusPhone="";
    private String firstName="";
    private String lastName="";
    private CountryObject countryObject;
    private ListCountryObject listCountryObject;
    private String phoneForServer="";
    private String textFileFromAssets="";
    private int cursorPosition=0;

    public static synchronized InfoRegistration getInstance() {
        if (instance == null) {
            instance = new InfoRegistration();
        }
        return instance;
    }

    private InfoRegistration() {
    }

    public String getTextFileFromAssets() {
        return textFileFromAssets;
    }

    public void setTextFileFromAssets(String textFileFromAssets) {
        this.textFileFromAssets = textFileFromAssets;
    }

    public String getPhoneForServer() {
        return phoneForServer;
    }

    public void setPhoneForServer(String phoneForServer) {
        this.phoneForServer = phoneForServer;
    }

    public ListCountryObject getListCountryObject() {
        return listCountryObject;
    }

    public void setListCountryObject(ListCountryObject listCountryObject) {
        this.listCountryObject = listCountryObject;
    }

    public String getCodeCountryLetters() {
        return codeCountryLetters;
    }

    public void setCodeCountryLetters(String codeCountryLetters) {
        this.codeCountryLetters = codeCountryLetters;
    }

    public CountryObject getCountryObject() {
        return countryObject;
    }

    public void setCountryObject(CountryObject countryObject) {
        this.countryObject = countryObject;
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(int cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCodeCountry() {
        return codeCountry;
    }

    public void setCodeCountry(String codeCountry) {

        this.codeCountry = codeCountry;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCodePlusPhone() {
        return codePlusPhone;
    }

    public void setCodePlusPhone(String codePlusPhone) {
        this.codePlusPhone = codePlusPhone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
