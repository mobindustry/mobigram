package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.model.Contact;

public class ContactsListAdapter extends ArrayAdapter<Contact> {

    private final LayoutInflater inflater;

    public ContactsListAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_item, parent, false);

            TextView icon = (TextView) convertView.findViewById(R.id.message_icon_text);
            TextView firstLastName = (TextView) convertView.findViewById(R.id.firstLastName);
            TextView lastMessage = (TextView) convertView.findViewById(R.id.lastMessage);

            char[] iconText = new char[2];
            getItem(position).getFirstName().getChars(0, 1, iconText, 0);
            getItem(position).getLastName().getChars(0, 1, iconText, 1);
            String iconTextResult = "" + iconText[0] + iconText[1];

            icon.setText(iconTextResult.toUpperCase());
            firstLastName.setText(getItem(position).getFirstName() + " " + getItem(position).getLastName());
            lastMessage.setText(getItem(position).getLastMessage());
        }

        Contact item = getItem(position);
        TextView icon = (TextView) convertView.findViewById(R.id.message_icon_text);
        TextView firstLastName = (TextView) convertView.findViewById(R.id.firstLastName);
        TextView lastMessage = (TextView) convertView.findViewById(R.id.lastMessage);

        char[] iconText = new char[2];
        item.getFirstName().getChars(0, 1, iconText, 0);
        item.getLastName().getChars(0, 1, iconText, 1);
        String iconTextResult = "" + iconText[0] + iconText[1];

        icon.setText(iconTextResult.toUpperCase());
        firstLastName.setText(item.getFirstName() + " " + getItem(position).getLastName());
        lastMessage.setText(item.getLastMessage());

        return convertView;
    }
}
