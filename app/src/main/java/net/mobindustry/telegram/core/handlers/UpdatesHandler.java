package net.mobindustry.telegram.core.handlers;

import android.content.Context;
import android.content.Intent;

import net.mobindustry.telegram.model.holder.DownloadFileHolder;
import net.mobindustry.telegram.model.holder.MessagesFragmentHolder;
import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class UpdatesHandler extends BaseHandler<UpdatesHandler> {
    private Context context;

    public UpdatesHandler(Context context) {
        this.context = context;
    }

    @Override
    public UpdatesHandler resultHandler(TdApi.TLObject object) {
        //Log.wtf("Log", "UpdateHandler: " + object.toString());
        switch (object.getConstructor()) {
            case TdApi.UpdateMessageId.CONSTRUCTOR: {
                TdApi.UpdateMessageId message = (TdApi.UpdateMessageId) object;
                Intent intent = new Intent(Const.NEW_MESSAGE_ACTION_ID);
                intent.putExtra("message_id", message.newId);
                intent.putExtra("chatId", message.chatId);
                context.sendBroadcast(intent);
                break;
            }
            case TdApi.UpdateNewMessage.CONSTRUCTOR: {
                TdApi.UpdateNewMessage message = (TdApi.UpdateNewMessage) object;

                Intent intent = new Intent(Const.NEW_MESSAGE_ACTION);
                intent.putExtra("message_id", message.message.id);
                intent.putExtra("chatId", message.message.chatId);

                if (message.getConstructor() == TdApi.MessageText.CONSTRUCTOR) {
                    TdApi.MessageText textMessage = (TdApi.MessageText) message.message.message;
                    intent.putExtra("message", textMessage.text);
                }

                MessagesFragmentHolder.addToMap(message.message.chatId, message.message.id);
                context.sendBroadcast(intent);
                break;
            }
            case TdApi.UpdateChatReadInbox.CONSTRUCTOR: {
                TdApi.UpdateChatReadInbox update = (TdApi.UpdateChatReadInbox) object;
                Intent intent = new Intent(Const.READ_INBOX_ACTION);
                intent.putExtra("chat_id", update.chatId);
                intent.putExtra("unread_count", update.unreadCount);
                intent.putExtra("last_read", update.lastRead);
                context.sendBroadcast(intent);
                break;
            }
            case TdApi.UpdateChatReadOutbox.CONSTRUCTOR:
                break;
            case TdApi.UpdateFile.CONSTRUCTOR:
                TdApi.UpdateFile file = (TdApi.UpdateFile) object;
                DownloadFileHolder.addFile(file);
                break;
        }
        return null;
    }
}
