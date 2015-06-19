package net.mobindustry.telegram.core.handlers;

import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class StickersHandler extends BaseHandler<TdApi.Stickers> {
    public static final int HANDLER_ID = Const.STICKERS_HANDLER_ID;
    @Override
    public TdApi.Stickers resultHandler(TdApi.TLObject object) {
        if (object.getConstructor() == TdApi.Stickers.CONSTRUCTOR) {
            return (TdApi.Stickers) object;
        }
        return null;
    }

    @Override
    public int getHandlerId() {
        return HANDLER_ID;
    }
}
