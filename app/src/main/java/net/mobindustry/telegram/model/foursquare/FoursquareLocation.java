package net.mobindustry.telegram.model.foursquare;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FoursquareLocation implements Parcelable {
    @SerializedName("lat")
    private double latitude;

    @SerializedName("lng")
    private double longitude;

    private long distance;

    @SerializedName("cc")
    private String countryCode;

    private String country;
    private String address;


    private ArrayList<String> formattedAddress;

    public FoursquareLocation() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ArrayList<String> getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(ArrayList<String> formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeLong(this.distance);
        dest.writeString(this.countryCode);
        dest.writeString(this.country);
        dest.writeString(this.address);
        dest.writeList(this.formattedAddress);

    }


    protected FoursquareLocation(Parcel parcel) {
        this.latitude = parcel.readDouble();
        this.longitude = parcel.readDouble();
        this.distance = parcel.readLong();
        this.countryCode = parcel.readString();
        this.country = parcel.readString();
        this.address = parcel.readString();
        this.formattedAddress = parcel.readArrayList(null);
    }

    public static Creator<FoursquareLocation> CREATOR = new Creator<FoursquareLocation>() {

        @Override
        public FoursquareLocation createFromParcel(Parcel source) {
            return new FoursquareLocation(source);
        }

        @Override
        public FoursquareLocation[] newArray(int size) {
            return new FoursquareLocation[size];
        }

    };
}

