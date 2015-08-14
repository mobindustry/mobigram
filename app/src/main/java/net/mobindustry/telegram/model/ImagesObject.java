package net.mobindustry.telegram.model;

import net.mobindustry.telegram.model.MediaGallery;

public class ImagesObject extends MediaGallery {

    private String path;

    public ImagesObject() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
