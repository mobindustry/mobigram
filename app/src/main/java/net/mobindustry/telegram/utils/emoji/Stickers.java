package net.mobindustry.telegram.utils.emoji;

import net.mobindustry.telegram.model.holder.MessagesFragmentHolder;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.List;


public class Stickers {
    private List<TdApi.Sticker> ss = new ArrayList<>();

    public Stickers() {

        TdApi.Stickers stickers = MessagesFragmentHolder.getStickers();
        for (int i = 0; i < stickers.stickers.length; i++) {
            ss.add(stickers.stickers[i]);
        }
    }

    public List<TdApi.Sticker> getStickers() {
        return ss;
    }
}
