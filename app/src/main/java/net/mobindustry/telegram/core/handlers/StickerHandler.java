package net.mobindustry.telegram.core.handlers;

import android.util.Log;

import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class StickerHandler extends BaseHandler<TdApi.Stickers> {
    public static final int HANDLER_ID = Const.STICKER_HANDLER_ID;
    @Override
    public TdApi.Stickers resultHandler(TdApi.TLObject object) {
        Log.i("Log", "Log handler: " + object);
        return null;
    }

    @Override
    public int getHandlerId() {
        return HANDLER_ID;
    }
}
