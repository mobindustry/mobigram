package net.mobindustry.telegram.model.foursquare;

import android.os.Parcel;
import android.os.Parcelable;

public class FoursquareCategoryIcon implements Parcelable {
    public static final String ICON = "64";

    private String prefix;
    private String suffix;

    public FoursquareCategoryIcon() {
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getIconUrl() {
        return prefix + ICON + suffix;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.prefix);
        dest.writeString(this.suffix);
    }


    protected FoursquareCategoryIcon(Parcel parcel) {
        this.prefix = parcel.readString();
        this.suffix = parcel.readString();
    }

    public static Creator<FoursquareCategoryIcon> CREATOR = new Creator<FoursquareCategoryIcon>() {

        @Override
        public FoursquareCategoryIcon createFromParcel(Parcel source) {
            return new FoursquareCategoryIcon(source);
        }

        @Override
        public FoursquareCategoryIcon[] newArray(int size) {
            return new FoursquareCategoryIcon[size];
        }

    };
}
