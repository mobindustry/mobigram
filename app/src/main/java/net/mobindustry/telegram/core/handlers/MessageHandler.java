package net.mobindustry.telegram.core.handlers;

import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class MessageHandler extends BaseHandler<TdApi.Message> {
    public static final int HANDLER_ID = Const.MESSAGE_HANDLER_ID;

    @Override
    public TdApi.Message resultHandler(TdApi.TLObject object) {
        if (object.getConstructor() == TdApi.Message.CONSTRUCTOR) {
            return (TdApi.Message) object;
        }
        return null;
    }

    @Override
    public int getHandlerId() {
        return HANDLER_ID;
    }
}
