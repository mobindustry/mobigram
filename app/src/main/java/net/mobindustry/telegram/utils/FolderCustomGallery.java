package net.mobindustry.telegram.utils;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class FolderCustomGallery implements Serializable{
    private String name;
    private String path;
    private String photosQuantity;
    private List<File>photosInFolder;
    private File firstPhoto;
    private Bitmap bitmapPhoto;

    public Bitmap getBitmapPhoto() {
        return bitmapPhoto;
    }

    public void setBitmapPhoto(Bitmap bitmapPhoto) {
        this.bitmapPhoto = bitmapPhoto;
    }

    public FolderCustomGallery() {
    }

    public File getFirstPhoto() {
        return firstPhoto;
    }

    public void setFirstPhoto(File firstPhoto) {
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

    public List<File> getPhotosInFolder() {
        return photosInFolder;
    }

    public void setPhotosInFolder(List<File> photosInFolder) {
        this.photosInFolder = photosInFolder;
    }
}
