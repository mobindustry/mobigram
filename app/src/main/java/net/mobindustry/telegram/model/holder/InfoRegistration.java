package net.mobindustry.telegram.model.holder;

public class InfoRegistration {

    private static InfoRegistration instance;

    private String countryName = "";
    private String codeCountry = "+";
    private String phone = "";
    private String codePlusPhone="";
    private String firstName="";
    private String lastName="";

    public static synchronized InfoRegistration getInstance() {
        if (instance == null) {
            instance = new InfoRegistration();
        }
        return instance;
    }

    private InfoRegistration() {
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
