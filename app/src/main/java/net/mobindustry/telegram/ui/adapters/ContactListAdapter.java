package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.util.Log;
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
import java.util.Locale;

public class ContactListAdapter extends ArrayAdapter<TdApi.User> {

    private final LayoutInflater inflater;

    public ContactListAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_item, parent, false);
        }

        TextView icon = (TextView) convertView.findViewById(R.id.message_icon);
        TextView firstLastName = (TextView) convertView.findViewById(R.id.firstLastName);
        TextView lastSeen = (TextView) convertView.findViewById(R.id.lastSeen);

        TdApi.User item = getItem(position);
        TdApi.UserStatus status = item.status;
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            icon.setBackgroundDrawable(Utils.getShapeDrawable(R.dimen.contact_list_item_icon_size, -item.id));
        } else {
            icon.setBackground(Utils.getShapeDrawable(R.dimen.contact_list_item_icon_size, -item.id));
        }

        icon.setText(Utils.getInitials(item.firstName, item.lastName));

        firstLastName.setText(item.firstName + " " + item.lastName);
        lastSeen.setText(Utils.getUserStatusString(status));

        return convertView;
    }


}
