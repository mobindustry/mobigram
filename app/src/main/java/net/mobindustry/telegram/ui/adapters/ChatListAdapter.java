package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.utils.Const;
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

        TdApi.Chat item = getItem(position);

        TdApi.ChatInfo info = item.type;
        TdApi.PrivateChatInfo privateChatInfo = null;
        TdApi.MessageText text = null;
        TdApi.Message message = item.topMessage;

        long timeMls = (long) message.date;
        Date date = new Date(timeMls * 1000);



        if (message.message instanceof TdApi.MessageText) {
            text = (TdApi.MessageText) message.message;
            lastMessage.setText(text.text);
        } else {
            lastMessage.setText("\"Content\"");
        }

        if (info instanceof TdApi.PrivateChatInfo) {
            privateChatInfo = (TdApi.PrivateChatInfo) info;
        }
        TdApi.User user = privateChatInfo.user;

        if (item.unreadCount != 0) {
            notify.setText(String.valueOf(item.unreadCount));
            notify.setBackground(Utils.getShapeDrawable(25, Color.YELLOW));
        }

        icon.setBackground(Utils.getShapeDrawable(60, -user.id)); //TODO set color
        icon.setText(Utils.getInitials(user.firstName, user.lastName));

        firstLastName.setText(user.firstName + " " + user.lastName);

        time.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));

        return convertView;
    }


}
