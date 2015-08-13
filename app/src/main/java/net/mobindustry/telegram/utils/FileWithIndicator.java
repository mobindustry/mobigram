package net.mobindustry.telegram.utils;

import java.io.File;

public class FileWithIndicator {

    private File file;
    private boolean check;
    private String thumbPhoto = "";

    public String getThumbPhoto() {
        return thumbPhoto;
    }

    public void setThumbPhoto(String thumbPhoto) {
        this.thumbPhoto = thumbPhoto;
    }

    public FileWithIndicator() {
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
