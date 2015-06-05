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

public class ContactsListAdapter extends ArrayAdapter<TdApi.Chat> {

    private final LayoutInflater inflater;

    public ContactsListAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_item, parent, false);
        }

        TextView icon = (TextView) convertView.findViewById(R.id.message_icon_text);
        TextView firstLastName = (TextView) convertView.findViewById(R.id.firstLastName);
        TextView lastMessage = (TextView) convertView.findViewById(R.id.lastMessage);
        TextView time = (TextView) convertView.findViewById(R.id.contactItemTime);

        TdApi.Chat item = getItem(position);

        TdApi.ChatInfo info = item.type;
        TdApi.PrivateChatInfo privateChatInfo = null;
        TdApi.MessageText text = null;
        TdApi.Message message = item.topMessage;
        TdApi.MessageContent content = message.message;

        long timeMls = (long) message.date;
        Date date = new Date(timeMls * 1000);

        if (content instanceof TdApi.MessageText) {
            text = (TdApi.MessageText) content;
        }
        if (info instanceof TdApi.PrivateChatInfo) {
            privateChatInfo = (TdApi.PrivateChatInfo) info;
        }
        TdApi.User user = privateChatInfo.user;

        icon.setBackground(getShapeDrawable());
        icon.setText(getInitials(user.firstName, user.lastName));

        if (user.lastName.isEmpty()) {
            firstLastName.setText(user.firstName);
        } else {
            firstLastName.setText(user.firstName + " " + user.lastName);
        }

        lastMessage.setText(text.text);
        time.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));

        return convertView;
    }

    private ShapeDrawable getShapeDrawable() {
        ShapeDrawable circle = new ShapeDrawable(new OvalShape());
        circle.setIntrinsicHeight(60);
        circle.setIntrinsicWidth(60);
        circle.getPaint().setColor(Color.DKGRAY);
        return circle;
    }

    public String getInitials(String firstName, String lastName) {
        if (firstName.isEmpty()) {
            return ":)";
        }
        if (lastName.isEmpty()) {
            char[] iconText = new char[2];
            firstName.getChars(0, 1, iconText, 0);
            firstName.getChars(1, 2, iconText, 1);
            return ("" + iconText[0] + iconText[1]).toUpperCase();
        } else {
            char[] iconText = new char[2];
            firstName.getChars(0, 1, iconText, 0);
            lastName.getChars(0, 1, iconText, 1);
            return ("" + iconText[0] + iconText[1]).toUpperCase();
        }
    }
}
