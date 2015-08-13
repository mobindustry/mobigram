package net.mobindustry.telegram.core.handlers;

import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class OkHandler extends BaseHandler<TdApi.Ok> {
    public static final int HANDLER_ID = Const.OK_HANDLER_ID;

    @Override
    public TdApi.Ok resultHandler(TdApi.TLObject object) {
        return (TdApi.Ok) object;
    }

    @Override
    public int getHandlerId() {
        return HANDLER_ID;
    }
}
