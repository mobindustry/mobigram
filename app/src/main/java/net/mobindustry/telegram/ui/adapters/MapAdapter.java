package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.mobindustry.telegram.R;

public class MapAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater;

    public MapAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.map_list_item, parent, false);
        }

        TextView text = (TextView) convertView.findViewById(R.id.map_item);
        text.setText("Item 1");


        return convertView;
    }
}
