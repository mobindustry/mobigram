package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.model.NeTelegramMessage;
import net.mobindustry.telegram.utils.Const;

public class MessageAdapter extends ArrayAdapter<NeTelegramMessage>{

    private final LayoutInflater inflater;
    private int typeCount = 4;

    public MessageAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return typeCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            switch (getItemViewType(position)) {
                case Const.IN_MESSAGE:
                    convertView = inflater.inflate(R.layout.in_message, parent, false);
                    break;
                case Const.IN_CONTENT_MESSAGE:
                    convertView = inflater.inflate(R.layout.in_content_message, parent, false);
                    break;
                case Const.OUT_MESSAGE:
                    convertView = inflater.inflate(R.layout.out_message, parent, false);
                    break;
                case Const.OUT_CONTENT_MESSAGE:
                    convertView = inflater.inflate(R.layout.out_content_message, parent, false);
                    break;
            }
        }

        NeTelegramMessage item = getItem(position);

        switch (getItemViewType(position)) {
            case Const.IN_MESSAGE:
                TextView inMessage = (TextView) convertView.findViewById(R.id.in_msg);
                TextView inTime = (TextView) convertView.findViewById(R.id.in_msg_time);

                inMessage.setText(item.getMessage());
                inTime.setText(item.getDate().toString());
                break;
            case Const.IN_CONTENT_MESSAGE:
                FrameLayout inContent = (FrameLayout) convertView.findViewById(R.id.in_content);
                TextView inContentTime = (TextView) convertView.findViewById(R.id.in_content_msg_time);

                //inContent.addView();
                inContentTime.setText(item.getDate().toString());
                break;
            case Const.OUT_MESSAGE:
                TextView outMessage = (TextView) convertView.findViewById(R.id.out_msg);
                TextView outTime = (TextView) convertView.findViewById(R.id.out_msg_time);

                outMessage.setText(item.getMessage());
                outTime.setText(item.getDate().toString());
                break;
            case Const.OUT_CONTENT_MESSAGE:
                FrameLayout outContent = (FrameLayout) convertView.findViewById(R.id.out_content);
                TextView outContentTime = (TextView) convertView.findViewById(R.id.out_content_msg_time);

                //outContent.addView();
                outContentTime.setText(item.getDate().toString());
                break;
        }
        return convertView;
    }
}
