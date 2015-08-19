package net.mobindustry.mobigram.model.foursquare;

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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
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

