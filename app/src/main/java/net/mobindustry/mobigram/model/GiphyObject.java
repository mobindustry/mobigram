package net.mobindustry.mobigram.model;

import java.io.File;

public class GiphyObject extends MediaGallery {

    private String path;
    private boolean check;
    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public GiphyObject() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
