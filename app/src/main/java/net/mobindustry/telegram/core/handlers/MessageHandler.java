package net.mobindustry.telegram.core.handlers;

import android.util.Log;

import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class MessageHandler extends BaseHandler<TdApi.Message> {
    public static final int HANDLER_ID = Const.MESSAGE_HANDLER_ID;
    @Override
    public TdApi.Message resultHandler(TdApi.TLObject object) {
        Log.e("Log", "MessageResult " + object.toString());
        if (object.getConstructor() == TdApi.Message.CONSTRUCTOR) {
            return (TdApi.Message) object;
        }
        return null;
    }

    @Override
    public int GetHandlerId() {
        return HANDLER_ID;
    }
}
