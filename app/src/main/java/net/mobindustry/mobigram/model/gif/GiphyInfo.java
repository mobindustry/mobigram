package net.mobindustry.mobigram.model.gif;

import java.io.Serializable;

public class GiphyInfo implements Serializable {

    private String id;
    private String ember_url;
    private GiphyImage images;

    public String getId() {
        return id;
    }

    public String getEmber_url() {
        return ember_url;
    }

    public GiphyImage getImages() {
        return images;
    }

    public GiphyInfo() {

    }
}
