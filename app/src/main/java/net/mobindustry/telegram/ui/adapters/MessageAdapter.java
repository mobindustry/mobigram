package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.NeTelegramMessage;
import net.mobindustry.telegram.ui.activity.ChatActivity;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.PhotoUtils;
import net.mobindustry.telegram.utils.Utils;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.Date;

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

        FrameLayout layout = new FrameLayout(getContext());

        if (item.message instanceof TdApi.MessagePhoto) {

            TdApi.MessagePhoto messagePhoto = (TdApi.MessagePhoto) item.message;
            Log.i("Log", "Message foto " + messagePhoto.toString());

            for (int i = 0; i < messagePhoto.photo.photos.length; i++) {

                if (messagePhoto.photo.photos[i].photo instanceof TdApi.FileLocal){
                    TdApi.FileLocal file = (TdApi.FileLocal) messagePhoto.photo.photos[i].photo;

                    Log.i("Message", "Photo " + item.message);

                    ImageView photo = new ImageView(getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 300);
                    photo.setLayoutParams(layoutParams);
                    photo.setImageURI(Uri.parse(file.path));
                    photo.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    layout.addView(photo);
                }
            }

            for (int i = 0; i < messagePhoto.photo.photos.length; i++) {
                if (messagePhoto.photo.photos[i].type.equals("x")) {
                    if (messagePhoto.photo.photos[i].photo instanceof TdApi.FileEmpty) {
                        TdApi.FileEmpty file = (TdApi.FileEmpty) messagePhoto.photo.photos[i].photo;
                        ((ChatActivity) getContext()).downloadFile(file.id);
                    }
                }
            }
        }
        if (item.message instanceof TdApi.MessageAudio) {
            Log.i("Message", "Audio " + item.message);
            TextView audio = new TextView(getContext());
            audio.setText("Audio");

            layout.addView(audio);
        }
        if (item.message instanceof TdApi.MessageContact) {
            Log.i("Message", "Contact " + item.message);
            TextView contact = new TextView(getContext());
            contact.setText("Contact");

            layout.addView(contact);
        }
        if (item.message instanceof TdApi.MessageDocument) {
            Log.i("Message", "Document " + item.message);
            TextView document = new TextView(getContext());
            document.setText("Document");

            layout.addView(document);
        }
        if (item.message instanceof TdApi.MessageGeoPoint) {
            Log.i("Message", "GeoPoint " + item.message);
            TextView geopoint = new TextView(getContext());
            geopoint.setText(((TdApi.MessageGeoPoint) item.message).geoPoint.toString());

            layout.addView(geopoint);
        }
        if (item.message instanceof TdApi.MessageSticker) {
            Log.i("Message", "Sticker " + item.message);
            TextView stiker = new TextView(getContext());
            stiker.setText("Sticker");

            layout.addView(stiker);
        }
        if (item.message instanceof TdApi.MessageVideo) {
            Log.i("Message", "Video " + item.message);
            TextView video = new TextView(getContext());
            video.setText("Video");

            layout.addView(video);
        }
        if (item.message instanceof TdApi.MessageUnsupported) {
            Log.i("Message", "Unsupported " + item.message);
            TextView unsupport = new TextView(getContext());
            unsupport.setText("Unsupport");

            layout.addView(unsupport);
        }

        long timeInMls = item.date;
        Date date = new Date(timeInMls * 1000);

        switch (getItemViewType(position)) {
            case Const.IN_MESSAGE:
                TextView inMessage = (TextView) convertView.findViewById(R.id.in_msg);
                TextView inTime = (TextView) convertView.findViewById(R.id.in_msg_time);

                TdApi.MessageText inText = (TdApi.MessageText) item.message;

                inMessage.setText(inText.text);
                inTime.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));
                break;
            case Const.IN_CONTENT_MESSAGE:
                FrameLayout inContent = (FrameLayout) convertView.findViewById(R.id.in_content);
                inContent.removeAllViews();

                TextView inContentTime = (TextView) convertView.findViewById(R.id.in_content_msg_time);

                inContent.addView(layout);
                inContentTime.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));
                break;
            case Const.OUT_MESSAGE:
                TextView outMessage = (TextView) convertView.findViewById(R.id.out_msg);
                TextView outTime = (TextView) convertView.findViewById(R.id.out_msg_time);

                TdApi.MessageText outText = (TdApi.MessageText) item.message;

                outMessage.setText(outText.text);
                outTime.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));
                break;
            case Const.OUT_CONTENT_MESSAGE:
                FrameLayout outContent = (FrameLayout) convertView.findViewById(R.id.out_content);
                outContent.removeAllViews();

                TextView outContentTime = (TextView) convertView.findViewById(R.id.out_content_msg_time);

                outContent.addView(layout);
                outContentTime.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));
                break;
        }
        return convertView;
    }
}
