package net.mobindustry.telegram.core.handlers;

import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class UserHandler extends BaseHandler<TdApi.User>{

    public static final int HANDLER_ID = Const.USER_HANDLER_ID;
    @Override
    public TdApi.User resultHandler(TdApi.TLObject object) {
        if (object.getConstructor() == TdApi.User.CONSTRUCTOR) {
            return (TdApi.User) object;
        }
        return null;
    }

    @Override
    public int getHandlerId() {
        return HANDLER_ID;
    }
}
