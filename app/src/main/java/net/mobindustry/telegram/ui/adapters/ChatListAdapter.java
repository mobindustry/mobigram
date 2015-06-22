package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.DownloadFileHandler;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.ImageLoaderHelper;
import net.mobindustry.telegram.utils.Utils;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.Date;

public class ChatListAdapter extends ArrayAdapter<TdApi.Chat> {

    private final LayoutInflater inflater;

    public ChatListAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chat_item, parent, false);
        }

        TextView icon = (TextView) convertView.findViewById(R.id.message_icon_text);
        TextView firstLastName = (TextView) convertView.findViewById(R.id.firstLastName);
        TextView lastMessage = (TextView) convertView.findViewById(R.id.lastMessage);
        TextView time = (TextView) convertView.findViewById(R.id.contactItemTime);
        TextView notify = (TextView) convertView.findViewById(R.id.chat_notification);

        final ImageView imageIcon = (ImageView) convertView.findViewById(R.id.message_icon_image);

        TdApi.Chat item = getItem(position);

        TdApi.ChatInfo info = item.type;
        TdApi.PrivateChatInfo privateChatInfo = null;
        TdApi.MessageText text = null;
        TdApi.Message message = item.topMessage;

        long timeMls = (long) message.date;
        Date date = new Date(timeMls * 1000);

        if (message.message instanceof TdApi.MessageText) {
            text = (TdApi.MessageText) message.message;
            lastMessage.setTextColor(Color.BLACK);
            lastMessage.setText(text.text);
        } else {
            lastMessage.setTextColor(getContext().getResources().getColor(R.color.content_text_color));
            if (message.message instanceof TdApi.MessagePhoto) {
                lastMessage.setText("Photo");
            }
            if (message.message instanceof TdApi.MessageAudio) {
                lastMessage.setText("Audio");
            }
            if (message.message instanceof TdApi.MessageContact) {
                lastMessage.setText("Contact");
            }
            if (message.message instanceof TdApi.MessageDocument) {
                lastMessage.setText("Document");
            }
            if (message.message instanceof TdApi.MessageGeoPoint) {
                lastMessage.setText("GeoPoint");
            }
            if (message.message instanceof TdApi.MessageSticker) {
                lastMessage.setText("Sticker");
            }
            if (message.message instanceof TdApi.MessageVideo) {
                lastMessage.setText("Video");
            }
            if (message.message instanceof TdApi.MessageUnsupported) {
                lastMessage.setText("Unknown");
            }
        }

        if (info instanceof TdApi.PrivateChatInfo) {
            privateChatInfo = (TdApi.PrivateChatInfo) info;
        }
        TdApi.User user = privateChatInfo.user;

        //TODO set unread messages;
//        if (item.unreadCount != 0) {
//            notify.setText(String.valueOf(item.unreadCount));
//            notify.setBackground(Utils.getShapeDrawable(25, Color.rgb(255, 145, 0)));
//        } else  {
//            notify.setText("");
//            notify.setBackground(null);
//        }

        if (user.photoBig instanceof TdApi.FileEmpty) {
            final TdApi.FileEmpty file = (TdApi.FileEmpty) user.photoBig;
            if(file.id != 0) {
                new ApiClient<>(new TdApi.DownloadFile(file.id), new DownloadFileHandler(), new ApiClient.OnApiResultHandler() {
                    @Override
                    public void onApiResult(BaseHandler output) {
                        if (output.getHandlerId() == DownloadFileHandler.HANDLER_ID) {
                            ImageLoaderHelper.displayImage(String.valueOf(file.id), imageIcon);
                        }
                    }
                }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else {
                icon.setBackground(Utils.getShapeDrawable(60, -user.id));
                icon.setText(Utils.getInitials(user.firstName, user.lastName));
            }

        }
        if (user.photoBig instanceof TdApi.FileLocal) {
            TdApi.FileLocal file = (TdApi.FileLocal) user.photoBig;
            ImageLoaderHelper.displayImage("file://" + file.path, imageIcon);
        }

        firstLastName.setText(user.firstName + " " + user.lastName);

        time.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));

        return convertView;
    }


}
