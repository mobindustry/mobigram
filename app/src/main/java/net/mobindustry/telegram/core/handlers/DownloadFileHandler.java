package net.mobindustry.telegram.core.handlers;

import android.util.Log;

import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class DownloadFileHandler extends BaseHandler<TdApi.Ok> {
    public static final int HANDLER_ID = Const.DOWNLOAD_FILE_HANDLER_ID;
    @Override
    public TdApi.Ok resultHandler(TdApi.TLObject object) {
        if(object.getConstructor() == TdApi.Ok.CONSTRUCTOR) {
            Log.i("Log", "DownloadHandler: " + object);
            return (TdApi.Ok) object;
        }
        return null;
    }

    @Override
    public int getHandlerId() {
        return HANDLER_ID;
    }
}
