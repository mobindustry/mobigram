package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
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
        }

        Contact item = getItem(position);
        TextView icon = (TextView) convertView.findViewById(R.id.message_icon_text);
        TextView firstLastName = (TextView) convertView.findViewById(R.id.firstLastName);
        TextView lastMessage = (TextView) convertView.findViewById(R.id.lastMessage);

        ShapeDrawable circle = new ShapeDrawable(new OvalShape());
        circle.setIntrinsicHeight(60);
        circle.setIntrinsicWidth(60);
        circle.getPaint().setColor(item.getColor());

        icon.setBackground(circle);
        icon.setText(item.getInitials());
        firstLastName.setText(item.getFirstName() + " " + getItem(position).getLastName());
        lastMessage.setText(item.getLastMessage());

        return convertView;
    }
}
