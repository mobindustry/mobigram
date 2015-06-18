package net.mobindustry.telegram.core.handlers;

import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class DownloadFileHandler extends BaseHandler<TdApi.File> {
    public static final int HANDLER_ID = Const.DOWNLOAD_FILE_HANDLER_ID;
    @Override
    public TdApi.File resultHandler(TdApi.TLObject object) {
        if (object.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
            return (TdApi.FileLocal) object;
        }
        if (object.getConstructor() == TdApi.FileEmpty.CONSTRUCTOR) {
            return (TdApi.FileEmpty) object;
        }
        return null;
    }

    @Override
    public int GetHandlerId() {
        return HANDLER_ID;
    }
}
