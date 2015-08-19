package net.mobindustry.mobigram.core.handlers;

import net.mobindustry.mobigram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class UserFullHandler extends BaseHandler<TdApi.UserFull> {
    public static final int HANDLER_ID = Const.USER_FULL_HANDLER_ID;

    @Override
    public TdApi.UserFull resultHandler(TdApi.TLObject object) {
        return (TdApi.UserFull) object;
    }

    @Override
    public int getHandlerId() {
        return HANDLER_ID;
    }
}
