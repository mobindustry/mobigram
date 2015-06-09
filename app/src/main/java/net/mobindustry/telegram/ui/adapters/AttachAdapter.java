package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.mobindustry.telegram.R;

import java.io.Serializable;
import java.util.List;

public class AttachAdapter extends ArrayAdapter<String> implements Serializable {
    private LayoutInflater inflater;


    public AttachAdapter(Context context, List<String> objects) {
        super(context, 0, objects);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_attach_menu, parent, false);
        }


        return convertView;
    }
}
