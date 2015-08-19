package net.mobindustry.mobigram.core.handlers;

import net.mobindustry.mobigram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class GroupChatFullHandler extends BaseHandler<TdApi.GroupChatFull> {
    public static final int HANDLER_ID = Const.GROUP_CHAT_FULL_HANDLER_ID;

    @Override
    public TdApi.GroupChatFull resultHandler(TdApi.TLObject object) {
        return (TdApi.GroupChatFull) object;
    }

    @Override
    public int getHandlerId() {
        return HANDLER_ID;
    }
}
