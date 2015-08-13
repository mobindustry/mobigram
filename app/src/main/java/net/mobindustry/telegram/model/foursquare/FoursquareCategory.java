package net.mobindustry.telegram.model.foursquare;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FoursquareCategory implements Parcelable {
    private String id;
    private String name;
    private String pluralName;
    private String shortName;

    @SerializedName("icon")
    FoursquareCategoryIcon foursquareCategoryIcon;

    public FoursquareCategory() {
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

    public String getPluralName() {
        return pluralName;
    }

    public void setPluralName(String pluralName) {
        this.pluralName = pluralName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public FoursquareCategoryIcon getFoursquareCategoryIcon() {
        return foursquareCategoryIcon;
    }

    public void setFoursquareCategoryIcon(FoursquareCategoryIcon foursquareCategoryIcon) {
        this.foursquareCategoryIcon = foursquareCategoryIcon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.pluralName);
        dest.writeString(this.shortName);
        dest.writeParcelable(this.foursquareCategoryIcon, flags);
    }

    protected FoursquareCategory(Parcel parcel) {
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.pluralName = parcel.readString();
        this.shortName = parcel.readString();
        this.foursquareCategoryIcon = parcel.readParcelable(FoursquareCategoryIcon.class.getClassLoader());
    }

    public static Creator<FoursquareCategory> CREATOR = new Creator<FoursquareCategory>() {

        @Override
        public FoursquareCategory createFromParcel(Parcel source) {
            return new FoursquareCategory(source);
        }

        @Override
        public FoursquareCategory[] newArray(int size) {
            return new FoursquareCategory[size];
        }
    };
}
