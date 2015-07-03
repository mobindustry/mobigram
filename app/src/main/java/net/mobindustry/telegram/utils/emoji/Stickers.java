package net.mobindustry.telegram.utils.emoji;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.List;


public class Stickers {
    private List<TdApi.Sticker> ss = new ArrayList<>();

    public Stickers() {


    }

    public List<TdApi.Sticker> getStickers() {
        return ss;
    }
}
