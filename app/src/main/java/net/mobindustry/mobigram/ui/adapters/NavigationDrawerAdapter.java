package net.mobindustry.mobigram.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.mobindustry.mobigram.R;
import net.mobindustry.mobigram.model.NavigationItem;

public class NavigationDrawerAdapter extends ArrayAdapter<NavigationItem> {

    private final LayoutInflater inflater;

    public NavigationDrawerAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);
        }

        NavigationItem item = getItem(position);
        ImageView icon = (ImageView) convertView.findViewById(R.id.navigation_item_icon);
        TextView title = (TextView) convertView.findViewById(R.id.navigation_item_title);
        icon.setImageResource(item.getImage());
        title.setText(item.getTitle());

        return convertView;
    }
}
