package net.mobindustry.mobigram.core.handlers;

import net.mobindustry.mobigram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class ChatHistoryHandler extends BaseHandler<TdApi.Messages> {
    public static final int HANDLER_ID = Const.CHAT_HISTORY_HANDLER_ID;

    @Override
    public TdApi.Messages resultHandler(TdApi.TLObject object) {
        if (object.getConstructor() == TdApi.Messages.CONSTRUCTOR) {
            return (TdApi.Messages) object;
        }
        return null;
    }

    @Override
    public int getHandlerId() {
        return HANDLER_ID;
    }
}
