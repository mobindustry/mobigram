package net.mobindustry.telegram.model.flickr;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PhotoFlickr implements Parcelable {
    @SerializedName("photo id")
    private String photoId;
    private String owner;
    private String secret;
    private String server;
    private String farm;
    private boolean check;
    private String link;
    private String sendLinkLarge;



    public PhotoFlickr() {
    }


    public PhotoFlickr(Parcel source) {

        Bundle data = source.readBundle();
        photoId = data.getString("photo id");
        owner = data.getString("owner");
        secret = data.getString("secret");
        server = data.getString("server");
        farm = data.getString("farm");

    }

    public String getSendLinkLarge() {
        return sendLinkLarge;
    }

    public void setSendLinkLarge(String sendLinkLarge) {
        this.sendLinkLarge = sendLinkLarge;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle data = new Bundle();
        photoId = data.getString("photo id");
        owner = data.getString("owner");
        secret = data.getString("secret");
        server = data.getString("server");
        farm = data.getString("farm");
        dest.writeBundle(data);


    }

    public static final Creator<PhotoFlickr> CREATOR = new Creator<PhotoFlickr>() {
        public PhotoFlickr createFromParcel(Parcel data) {
            return new PhotoFlickr(data);
        }

        public PhotoFlickr[] newArray(int size) {
            return new PhotoFlickr[size];
        }
    };
}
