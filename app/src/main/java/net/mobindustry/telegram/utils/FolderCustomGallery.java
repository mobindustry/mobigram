package net.mobindustry.telegram.utils;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class FolderCustomGallery implements Serializable{
    private String name;
    private String path;
    private String photosQuantity;
    private List<File>photosInFolder;

    public FolderCustomGallery() {
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
