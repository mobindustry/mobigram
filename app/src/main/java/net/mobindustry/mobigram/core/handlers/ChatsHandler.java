package net.mobindustry.mobigram.core.handlers;

import net.mobindustry.mobigram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class ChatsHandler extends BaseHandler<TdApi.Chats> {
    public static final int HANDLER_ID = Const.CHATS_HANDLER_ID;
    @Override
    public TdApi.Chats resultHandler(TdApi.TLObject object) {
        if (object.getConstructor() == TdApi.Chats.CONSTRUCTOR) {
            return (TdApi.Chats) object;
        }
        return null;
    }

    @Override
    public int getHandlerId() {
        return HANDLER_ID;
    }
}
