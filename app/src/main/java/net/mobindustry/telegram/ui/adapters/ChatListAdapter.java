package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.graphics.Color;
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
import net.mobindustry.telegram.model.holder.MessagesFragmentHolder;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.ImageLoaderHelper;
import net.mobindustry.telegram.utils.Utils;
import net.mobindustry.telegram.utils.emoji.DpCalculator;
import net.mobindustry.telegram.utils.emoji.Emoji;
import net.mobindustry.telegram.utils.emoji.EmojiParser;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.Date;

public class ChatListAdapter extends ArrayAdapter<TdApi.Chat> {

    private final LayoutInflater inflater;
    private Emoji emoji;
    private EmojiParser emojiParser;

    public ChatListAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
        emoji = MessagesFragmentHolder.getInstance().getEmoji();
        emojiParser = new EmojiParser(emoji);
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
            emojiParser.parse(text);

            lastMessage.setTextColor(Color.BLACK);
            lastMessage.setText(text.textWithSmilesAndUserRefs);
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

        if (info instanceof TdApi.PrivateChatInfo) { //TODO verify;
            privateChatInfo = (TdApi.PrivateChatInfo) info;
        }
        TdApi.User user = privateChatInfo.user;

        if (item.unreadCount != 0) {
            notify.setText(String.valueOf(item.unreadCount));
            notify.setBackground(Utils.getShapeDrawable(R.dimen.chat_list_item_notification_size, getContext().getResources().getColor(R.color.message_notify)));
        } else {
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                notify.setBackgroundDrawable(null);
            } else {
                notify.setBackground(null);
            }
            notify.setText("");
        }

        if (user.photoBig instanceof TdApi.FileEmpty) {
            final TdApi.FileEmpty file = (TdApi.FileEmpty) user.photoBig;
            if (file.id != 0) {
                new ApiClient<>(new TdApi.DownloadFile(file.id), new DownloadFileHandler(), new ApiClient.OnApiResultHandler() {
                    @Override
                    public void onApiResult(BaseHandler output) {
                        if (output.getHandlerId() == DownloadFileHandler.HANDLER_ID) {
                            ImageLoaderHelper.displayImageList(String.valueOf(file.id), imageIcon);
                        }
                    }
                }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else {
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    icon.setBackgroundDrawable(Utils.getShapeDrawable(R.dimen.chat_list_item_icon_size, -user.id));
                } else {
                    icon.setBackground(Utils.getShapeDrawable(R.dimen.chat_list_item_icon_size, -user.id));
                }
                icon.setText(Utils.getInitials(user.firstName, user.lastName));
            }

        } else if (user.photoBig instanceof TdApi.FileLocal) {
            TdApi.FileLocal file = (TdApi.FileLocal) user.photoBig;
            ImageLoaderHelper.displayImage(Const.IMAGE_LOADER_PATH_PREFIX + file.path, imageIcon);
        }

        firstLastName.setText(user.firstName + " " + user.lastName);
        time.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));

        return convertView;
    }
}
