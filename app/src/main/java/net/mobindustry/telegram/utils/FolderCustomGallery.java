package net.mobindustry.telegram.utils;


import java.io.Serializable;
import java.util.List;

public class FolderCustomGallery implements Serializable {

    private String name;
    private String path;
    private String photosQuantity;
    private List<FileWithIndicator> photosInFolder;
    private String firstPhoto;
    private String firstThumb;

    public String getFirstThumb() {
        return firstThumb;
    }

    public void setFirstThumb(String firstThumb) {
        this.firstThumb = firstThumb;
    }

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

    public List<FileWithIndicator> getPhotosInFolder() {
        return photosInFolder;
    }

    public void setPhotosInFolder(List<FileWithIndicator> photosInFolder) {
        this.photosInFolder = photosInFolder;
    }
}
