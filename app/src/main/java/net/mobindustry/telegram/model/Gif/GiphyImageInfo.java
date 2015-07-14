package net.mobindustry.telegram.model.Gif;

import java.io.Serializable;

public class GiphyImageInfo implements Serializable{

    private String url;
    private String width;
    private String height;
    private String size;

    public String getUrl() {
        return url;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public String getSize() {
        return size;
    }

    public GiphyImageInfo() {

    }
}
