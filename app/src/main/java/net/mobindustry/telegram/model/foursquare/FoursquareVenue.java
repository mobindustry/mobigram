package net.mobindustry.telegram.model.foursquare;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FoursquareVenue implements Parcelable {
    private String id;
    private String name;

    @SerializedName("location")
    private FoursquareLocation foursquareLocation;

    @SerializedName("categories")
    private ArrayList<FoursquareCategory> foursquareCategories;


    public FoursquareVenue() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoursquareLocation getFoursquareLocation() {
        return foursquareLocation;
    }

    public void setFoursquareLocation(FoursquareLocation foursquareLocation) {
        this.foursquareLocation = foursquareLocation;
    }

    public ArrayList<FoursquareCategory> getFoursquareCategories() {
        return foursquareCategories;
    }

    public void setFoursquareCategories(ArrayList<FoursquareCategory> foursquareCategories) {
        this.foursquareCategories = foursquareCategories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.foursquareLocation, flags);
        dest.writeList(foursquareCategories);
    }


    protected FoursquareVenue(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.foursquareLocation = in.readParcelable(FoursquareLocation.class.getClassLoader());
        this.foursquareCategories = in.readArrayList(FoursquareCategory.class.getClassLoader());
    }

    public static Creator<FoursquareVenue> CREATOR = new Creator<FoursquareVenue>() {

        @Override
        public FoursquareVenue createFromParcel(Parcel source) {
            return new FoursquareVenue(source);
        }

        @Override
        public FoursquareVenue[] newArray(int size) {
            return new FoursquareVenue[size];
        }

    };

}
