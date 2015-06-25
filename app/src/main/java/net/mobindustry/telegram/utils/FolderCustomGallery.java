package net.mobindustry.telegram.utils;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class FolderCustomGallery implements Serializable{
    private String name;
    private String path;
    private String photosQuantity;
    private List<File>photosInFolder;
    private String uriFirstPhoto;

    public FolderCustomGallery() {
    }

    public String getUriFirstPhoto() {
        return uriFirstPhoto;
    }

    public void setUriFirstPhoto(String uriFirstPhoto) {
        this.uriFirstPhoto = uriFirstPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPhotosQuantity() {
        return photosQuantity;
    }

    public void setPhotosQuantity(String photosQuantity) {
        this.photosQuantity = photosQuantity;
    }

    public List<File> getPhotosInFolder() {
        return photosInFolder;
    }

    public void setPhotosInFolder(List<File> photosInFolder) {
        this.photosInFolder = photosInFolder;
    }
}
