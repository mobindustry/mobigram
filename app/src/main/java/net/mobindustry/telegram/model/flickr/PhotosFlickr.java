package net.mobindustry.telegram.model.flickr;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PhotosFlickr implements Parcelable {
    private List<PhotoFlickr> photos;
    private String page;
    private String pages;
    private String perpage;
    private String total;


    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    void addPhotoFlickr(PhotoFlickr photoFlickr) {
        photos.add(photoFlickr);
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPerpage() {
        return perpage;
    }

    public void setPerpage(String perpage) {
        this.perpage = perpage;
    }

    public PhotosFlickr() {
        photos = new ArrayList<PhotoFlickr>();
    }

    public PhotosFlickr(Parcel source) {

        Bundle data = source.readBundle();
        page = data.getString("page");
        pages = data.getString("pages");
        perpage = data.getString("perpage");
        total = data.getString("total");
        photos = data.getParcelableArrayList("photos");

    }

    public List<PhotoFlickr> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoFlickr> photos) {
        this.photos = photos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle data = new Bundle();
        data.putString("page", page);
        data.putString("pages", pages);
        data.putString("perpage", perpage);
        data.putString("total", total);
        data.putParcelableArrayList("photos", (ArrayList<? extends Parcelable>) photos);
        dest.writeBundle(data);
    }

    public static final Creator<PhotosFlickr> CREATOR = new Creator<PhotosFlickr>() {
        public PhotosFlickr createFromParcel(Parcel data) {
            return new PhotosFlickr(data);
        }

        public PhotosFlickr[] newArray(int size) {
            return new PhotosFlickr[size];
        }
    };
}
