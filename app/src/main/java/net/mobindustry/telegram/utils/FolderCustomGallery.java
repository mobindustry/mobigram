package net.mobindustry.telegram.utils;

import java.io.Serializable;
import java.util.List;

public class FolderCustomGallery implements Serializable{
    private String name;
    private String path;
    private String photosQuantity;
    private List<java.io.File>photosInFolder;
    private String firstPhoto;


    public FolderCustomGallery() {
    }

    public String getFirstPhoto() {
        return firstPhoto;
    }

    public void setFirstPhoto(String firstPhoto) {
        this.firstPhoto = firstPhoto;
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

    public List<java.io.File> getPhotosInFolder() {
        return photosInFolder;
    }

    public void setPhotosInFolder(List<java.io.File> photosInFolder) {
        this.photosInFolder = photosInFolder;
    }
}
