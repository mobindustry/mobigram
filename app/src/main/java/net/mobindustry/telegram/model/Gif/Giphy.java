package net.mobindustry.telegram.model.Gif;

import java.io.Serializable;
import java.util.List;

public class Giphy implements Serializable {

    private List<GiphyInfo> data;
    private GiphyPagination pagination;

    public Giphy() {
    }

    public List<GiphyInfo> getData() {
        return data;
    }

    public GiphyPagination getPagination() {
        return pagination;
    }
}
