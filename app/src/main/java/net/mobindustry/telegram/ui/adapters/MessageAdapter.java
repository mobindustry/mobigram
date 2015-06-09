package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.NeTelegramMessage;
import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class MessageAdapter extends ArrayAdapter<TdApi.Message> {

    private final LayoutInflater inflater;
    private int typeCount = 4;
    private long myId;

    public MessageAdapter(Context context, long myId) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
        this.myId = myId;
    }

    @Override
    public int getItemViewType(int position) {
        TdApi.Message message = getItem(position);
        if (message.fromId == myId) {
            if (message.message instanceof TdApi.MessageText) {
                return Const.OUT_MESSAGE;
            } else {
                return Const.OUT_CONTENT_MESSAGE;
            }
        } else {
            if (message.message instanceof TdApi.MessageText) {
                return Const.IN_MESSAGE;
            } else {
                return Const.IN_CONTENT_MESSAGE;
            }
        }
    }

    @Override
    public int getViewTypeCount() {
        return typeCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
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

        TdApi.Message item = getItem(position);

        switch (getItemViewType(position)) {
            case Const.IN_MESSAGE:
                TextView inMessage = (TextView) convertView.findViewById(R.id.in_msg);
                TextView inTime = (TextView) convertView.findViewById(R.id.in_msg_time);

                TdApi.MessageText inText = (TdApi.MessageText) item.message;

                inMessage.setText(inText.text);
                inTime.setText(String.valueOf(item.date));
                break;
            case Const.IN_CONTENT_MESSAGE:
                //FrameLayout inContent = (FrameLayout) convertView.findViewById(R.id.in_content);
                TextView inContentTime = (TextView) convertView.findViewById(R.id.in_content_msg_time);

                //inContent.addView();
                inContentTime.setText(String.valueOf(item.date));
                break;
            case Const.OUT_MESSAGE:
                TextView outMessage = (TextView) convertView.findViewById(R.id.out_msg);
                TextView outTime = (TextView) convertView.findViewById(R.id.out_msg_time);

                TdApi.MessageText outText = (TdApi.MessageText) item.message;

                outMessage.setText(outText.text);
                outTime.setText(String.valueOf(item.date));
                break;
            case Const.OUT_CONTENT_MESSAGE:
                //FrameLayout outContent = (FrameLayout) convertView.findViewById(R.id.out_content);
                TextView outContentTime = (TextView) convertView.findViewById(R.id.out_content_msg_time);

                //outContent.addView();
                outContentTime.setText(String.valueOf(item.date));
                break;
        }
        return convertView;
    }
}
