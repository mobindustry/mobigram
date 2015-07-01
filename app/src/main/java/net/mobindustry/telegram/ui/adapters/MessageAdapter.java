package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.holder.MessagesFragmentHolder;
import net.mobindustry.telegram.ui.activity.TransparentActivity;
import net.mobindustry.telegram.utils.ImageLoaderHelper;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.Utils;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.Date;

public class MessageAdapter extends ArrayAdapter<TdApi.Message> {

    private final LayoutInflater inflater;
    private int typeCount = 6;
    private long myId;

    private View.OnClickListener onClickListener;

    private LoadMore loadMore;

    public MessageAdapter(final Context context, long myId, LoadMore loadMore) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
        this.myId = myId;
        this.loadMore = loadMore;

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TdApi.Message message = (TdApi.Message) v.getTag();

                switch (message.message.getConstructor()) {
                    case TdApi.MessageAudio.CONSTRUCTOR:
                        TdApi.MessageAudio audio = (TdApi.MessageAudio) message.message;
                        break;
                    case TdApi.MessageContact.CONSTRUCTOR:
                        TdApi.MessageContact contact = (TdApi.MessageContact) message.message;
                        break;
                    case TdApi.MessageDocument.CONSTRUCTOR:
                        TdApi.MessageDocument document = (TdApi.MessageDocument) message.message;
                        break;
                    case TdApi.MessageGeoPoint.CONSTRUCTOR:
                        TdApi.MessageGeoPoint geoPoint = (TdApi.MessageGeoPoint) message.message;

                        if (!MessagesFragmentHolder.isMapCalled()) {
                            MessagesFragmentHolder.mapCalled();
                            Intent intentMap = new Intent(context, TransparentActivity.class);
                            intentMap.putExtra("choice", Const.SELECTED_MAP_FRAGMENT);
                            intentMap.putExtra("lng", geoPoint.geoPoint.longitude);
                            intentMap.putExtra("lat", geoPoint.geoPoint.latitude);
                            context.startActivity(intentMap);
                        }
                        break;
                    case TdApi.MessagePhoto.CONSTRUCTOR:
                        TdApi.MessagePhoto photo = (TdApi.MessagePhoto) message.message;
                        break;
                    case TdApi.MessageVideo.CONSTRUCTOR:
                        TdApi.MessageVideo video = (TdApi.MessageVideo) message.message;
                        break;
                }
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        TdApi.Message message = getItem(position);
        if (message.fromId == myId) {
            if (message.message instanceof TdApi.MessageText) {
                return Const.OUT_MESSAGE;
            } else {
                if (message.message instanceof TdApi.MessageSticker) {
                    return Const.OUT_STICKER;
                }
                return Const.OUT_CONTENT_MESSAGE;
            }
        } else {
            if (message.message instanceof TdApi.MessageText) {
                return Const.IN_MESSAGE;
            } else {
                if (message.message instanceof TdApi.MessageSticker) {
                    return Const.IN_STICKER;
                }
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
        if(position == Const.LIST_PRELOAD_POSITION) {
            loadMore.load();
        }

        if (convertView == null) {
            switch (getItemViewType(position)) {
                case Const.IN_MESSAGE:
                    convertView = inflater.inflate(R.layout.in_message, parent, false);
                    break;
                case Const.IN_CONTENT_MESSAGE:
                    convertView = inflater.inflate(R.layout.in_content_message, parent, false);
                    break;
                case Const.IN_STICKER:
                    convertView = inflater.inflate(R.layout.in_sticker_message, parent, false);
                    break;
                case Const.OUT_MESSAGE:
                    convertView = inflater.inflate(R.layout.out_message, parent, false);
                    break;
                case Const.OUT_CONTENT_MESSAGE:
                    convertView = inflater.inflate(R.layout.out_content_message, parent, false);
                    break;
                case Const.OUT_STICKER:
                    convertView = inflater.inflate(R.layout.out_sticker_message, parent, false);
                    break;
            }
        }

        TdApi.Message item = getItem(position);

        FrameLayout layout = new FrameLayout(getContext());

        if (item.message instanceof TdApi.MessagePhoto) {

            TdApi.MessagePhoto messagePhoto = (TdApi.MessagePhoto) item.message;

            final ImageView photo = new ImageView(getContext());

            for (int i = 0; i < messagePhoto.photo.photos.length; i++) {
                if (messagePhoto.photo.photos[i].type.equals("m")) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(messagePhoto.photo.photos[i].width, messagePhoto.photo.photos[i].height);
                    photo.setLayoutParams(layoutParams);

                    Utils.fileCheckerAndLoader(messagePhoto.photo.photos[i].photo, photo);
                }
            }
            layout.addView(photo);
        }
        if (item.message instanceof TdApi.MessageAudio) {
            //Log.i("Message", "Audio " + item.message);
            TextView audio = new TextView(getContext());
            audio.setText("Audio");

            layout.addView(audio);
        }
        if (item.message instanceof TdApi.MessageContact) {
            //Log.i("Message", "Contact " + item.message);
            TextView contact = new TextView(getContext());
            contact.setText("Contact");

            layout.addView(contact);
        }
        if (item.message instanceof TdApi.MessageDocument) {
            //Log.i("Message", "Document " + item.message);
            TextView document = new TextView(getContext());
            document.setText("Document");

            layout.addView(document);
        }
        if (item.message instanceof TdApi.MessageGeoPoint) {
            TdApi.MessageGeoPoint point = (TdApi.MessageGeoPoint) item.message;
            int height = 100;
            int width = 200;

            //Fucking hardcode!!!

            String url = "https://maps.googleapis.com/maps/api/staticmap?center=" +
                    point.geoPoint.latitude + "," + point.geoPoint.longitude +
                    "&zoom=13&size=" + width + "x" + height + "&maptype=roadmap&scale=1&markers=color:red|size:big|" +
                    point.geoPoint.latitude + "," + point.geoPoint.longitude + "&sensor=false";

            ImageView map = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            map.setLayoutParams(layoutParams);

            ImageLoaderHelper.displayImage(url, map);

            layout.addView(map);
        }
        if (item.message instanceof TdApi.MessageSticker) {
            TdApi.Sticker sticker = ((TdApi.MessageSticker) item.message).sticker;

            final ImageView stickerImage = new ImageView(getContext());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 300);
            stickerImage.setLayoutParams(layoutParams);

            Utils.fileCheckerAndLoader(sticker.sticker, stickerImage);
            layout.addView(stickerImage);
        }
        if (item.message instanceof TdApi.MessageVideo) {
            //Log.i("Message", "Video " + item.message);

            TdApi.MessageVideo messageVideo = (TdApi.MessageVideo) item.message;
            if (messageVideo.video.thumb.photo instanceof TdApi.FileLocal) {
                TdApi.FileLocal file = (TdApi.FileLocal) messageVideo.video.thumb.photo;
                ImageView video = new ImageView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                video.setLayoutParams(layoutParams);
                video.setImageURI(Uri.parse(file.path));
                layout.addView(video);
            }
        }
        if (item.message instanceof TdApi.MessageUnsupported) {
            Log.i("Message", "Unsupported " + item.message);
            TextView unsupported = new TextView(getContext());
            unsupported.setText("Unsupported");

            layout.addView(unsupported);
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

                inContent.setOnClickListener(onClickListener);
                inContent.setTag(getItem(position));

                TextView inContentTime = (TextView) convertView.findViewById(R.id.in_content_msg_time);

                inContent.addView(layout);
                inContentTime.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));
                break;
            case Const.IN_STICKER:
                FrameLayout inSticker = (FrameLayout) convertView.findViewById(R.id.in_sticker);
                inSticker.removeAllViews();

                TextView inStickerTime = (TextView) convertView.findViewById(R.id.in_sticker_msg_time);

                inSticker.addView(layout);
                inStickerTime.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));
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

                outContent.setOnClickListener(onClickListener);
                outContent.setTag(getItem(position));

                TextView outContentTime = (TextView) convertView.findViewById(R.id.out_content_msg_time);

                outContent.addView(layout);
                outContentTime.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));
                break;
            case Const.OUT_STICKER:
                FrameLayout outSticker = (FrameLayout) convertView.findViewById(R.id.out_sticker);
                outSticker.removeAllViews();

                TextView outStickerTime = (TextView) convertView.findViewById(R.id.out_sticker_msg_time);

                outSticker.addView(layout);
                outStickerTime.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));
                break;
        }
        return convertView;
    }

    public interface LoadMore {
        void load();
    }
}
