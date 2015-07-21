package net.mobindustry.telegram.model.gif;

import java.io.Serializable;

public class GiphyImage implements Serializable{

    private GiphyImageInfo fixed_height;
    private GiphyImageInfo fixed_width;
    private GiphyImageInfo original;
    private GiphyImageInfo fixed_height_downsampled;

    public GiphyImageInfo getFixed_height() {
        return fixed_height;
    }

    public GiphyImageInfo getFixed_width() {
        return fixed_width;
    }

    public GiphyImageInfo getOriginal() {
        return original;
    }

    public GiphyImageInfo getFixed_height_downsampled() {
        return fixed_height_downsampled;
    }

    public GiphyImage() {

    }
}
