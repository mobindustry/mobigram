package net.mobindustry.mobigram.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.mobindustry.mobigram.R;
import net.mobindustry.mobigram.model.holder.MessagesFragmentHolder;
import net.mobindustry.mobigram.ui.emoji.Emoji;
import net.mobindustry.mobigram.ui.emoji.EmojiParser;
import net.mobindustry.mobigram.utils.Const;
import net.mobindustry.mobigram.utils.Utils;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.Date;

public class ChatListAdapter extends ArrayAdapter<TdApi.Chat> {

    private final LayoutInflater inflater;
    private EmojiParser emojiParser;

    public ChatListAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
        Emoji emoji = MessagesFragmentHolder.getInstance().getEmoji();
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

        imageIcon.setImageResource(R.drawable.ic_netelegram_placeholder);
        lastMessage.setText("");
        icon.setText("");

        Utils.verifySetBackground(icon, null);

        TdApi.Chat item = getItem(position);
        TdApi.ChatInfo info = item.type;

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
                lastMessage.setText(R.string.message_photo);
            }
            if (message.message instanceof TdApi.MessageAudio) {
                lastMessage.setText(R.string.message_audio);
            }
            if (message.message instanceof TdApi.MessageContact) {
                lastMessage.setText(R.string.message_contact);
            }
            if (message.message instanceof TdApi.MessageDocument) {
                lastMessage.setText(R.string.message_document);
            }
            if (message.message instanceof TdApi.MessageGeoPoint) {
                lastMessage.setText(R.string.message_geopoint);
            }
            if (message.message instanceof TdApi.MessageSticker) {
                lastMessage.setText(R.string.message_sticker);
            }
            if (message.message instanceof TdApi.MessageVideo) {
                lastMessage.setText(R.string.message_video);
            }
            if (message.message instanceof TdApi.MessageUnsupported) {
                lastMessage.setText(R.string.message_unknown);
            }
        }

        TdApi.File file = null;
        long chatId = item.id;
        String userFirstName = "";
        String userLastName = "";

        if (info.getConstructor() == TdApi.PrivateChatInfo.CONSTRUCTOR) {
            TdApi.PrivateChatInfo privateChatInfo = (TdApi.PrivateChatInfo) info;
            TdApi.User chatUser = privateChatInfo.user;
            file = chatUser.photoBig;
            userFirstName = privateChatInfo.user.firstName;
            userLastName = privateChatInfo.user.lastName;
        }
        if (info.getConstructor() == TdApi.GroupChatInfo.CONSTRUCTOR) {
            TdApi.GroupChatInfo groupChatInfo = (TdApi.GroupChatInfo) info;
            file = groupChatInfo.groupChat.photoBig;
            userFirstName = groupChatInfo.groupChat.title;
            userLastName = "";
        }

        if (item.unreadCount != 0) {
            notify.setText(String.valueOf(item.unreadCount));
            Utils.verifySetBackground(notify, Utils.getShapeDrawable(R.dimen.chat_list_item_notification_size, getContext().getResources().getColor(R.color.message_notify)));
        } else {
            Utils.verifySetBackground(notify, null);
            notify.setText("");
        }

        Utils.setIcon(file, (int) chatId, userFirstName, userLastName, imageIcon, icon, (Activity) getContext());

        firstLastName.setText(userFirstName + " " + userLastName);
        time.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));

        return convertView;
    }
}
