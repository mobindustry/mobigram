package net.mobindustry.telegram.core.handlers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.fragments.MessagesFragment;

import org.drinkless.td.libcore.telegram.TdApi;

public class UpdatesHandler extends BaseHandler<UpdatesHandler> {
    public final static String TAG = UpdatesHandler.class.toString();
    public final static String FILE_DOWNLOADED = TAG.concat(":FILE_DOWNLOADED");
    public final static String NEW_MESSAGE = TAG.concat(":NEW_MESSAGE");
    private Context context;

    public UpdatesHandler(Context context) {
        this.context = context;
    }

    @Override
    public UpdatesHandler resultHandler(TdApi.TLObject object) {
        Log.d("Log", "UpdateHandler: " + object.toString());
        switch (object.getConstructor()) {

//            case TdApi.UpdateFile.CONSTRUCTOR: {
//                TdApi.UpdateFile result = (TdApi.UpdateFile) object;
//                Intent intent = new Intent(FILE_DOWNLOADED);
//                intent.putExtra("fileId", result.fileId);
//                intent.putExtra("path", result.path);
//                context.sendBroadcast(intent);
//                break;
//            }
            case TdApi.UpdateMessageId.CONSTRUCTOR: {
                TdApi.UpdateMessageId message = (TdApi.UpdateMessageId) object;
                Intent intent = new Intent("new_message");
                intent.putExtra("message_id", message.newId);
                intent.putExtra("chatId", message.chatId);

//                intent.putExtra("fromId", message.message.fromId);
//                intent.putExtra("message", message.message.message);
//                intent.putExtra("forwardFromId", message.message.forwardFromId);
//                intent.putExtra("forwardDate", message.message.forwardDate);
//                intent.putExtra("date", message.message.date);
//                intent.putExtra("id", message.message.id);
                context.sendBroadcast(intent);
                break;
            }
            case TdApi.UpdateNewMessage.CONSTRUCTOR: {
                TdApi.UpdateNewMessage message = (TdApi.UpdateNewMessage) object;
                Intent intent = new Intent("new_message");
                intent.putExtra("message_id", message.message.id);
                intent.putExtra("chatId", message.message.chatId);

//                intent.putExtra("fromId", message.message.fromId);
//                intent.putExtra("message", message.message.message);
//                intent.putExtra("forwardFromId", message.message.forwardFromId);
//                intent.putExtra("forwardDate", message.message.forwardDate);
//                intent.putExtra("date", message.message.date);
//                intent.putExtra("id", message.message.id);
                context.sendBroadcast(intent);
                break;
            }
        }
        return null;
    }
}
