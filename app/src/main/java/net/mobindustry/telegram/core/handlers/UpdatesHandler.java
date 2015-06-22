package net.mobindustry.telegram.core.handlers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.holder.DownloadFileHolder;
import net.mobindustry.telegram.ui.fragments.MessagesFragment;
import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class UpdatesHandler extends BaseHandler<UpdatesHandler> {
    private Context context;

    public UpdatesHandler(Context context) {
        this.context = context;
    }

    @Override
    public UpdatesHandler resultHandler(TdApi.TLObject object) {
        Log.d("Log", "UpdateHandler: " + object.toString());
        switch (object.getConstructor()) {
            case TdApi.UpdateMessageId.CONSTRUCTOR: {
                TdApi.UpdateMessageId message = (TdApi.UpdateMessageId) object;
                Intent intent = new Intent(Const.NEW_MESSAGE_INTENT_FILTER);
                intent.putExtra("message_id", message.newId);
                intent.putExtra("chatId", message.chatId);
                context.sendBroadcast(intent);
                break;
            }
            case TdApi.UpdateNewMessage.CONSTRUCTOR: {
                TdApi.UpdateNewMessage message = (TdApi.UpdateNewMessage) object;
                Intent intent = new Intent(Const.NEW_MESSAGE_INTENT_FILTER);
                intent.putExtra("message_id", message.message.id);
                intent.putExtra("chatId", message.message.chatId);
                context.sendBroadcast(intent);
                break;
            }
            case TdApi.UpdateFile.CONSTRUCTOR: {
                TdApi.UpdateFile file = (TdApi.UpdateFile) object;
                DownloadFileHolder.addFile(file);
                break;
            }
        }
        return null;
    }
}
