package net.mobindustry.telegram.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.holder.MessagesFragmentHolder;
import net.mobindustry.telegram.model.holder.UserInfoHolder;
import net.mobindustry.telegram.ui.activity.PhotoViewerActivity;
import net.mobindustry.telegram.ui.activity.TransparentActivity;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.ImageLoaderHelper;
import net.mobindustry.telegram.utils.Utils;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.Date;

public class MessageAdapter extends ArrayAdapter<TdApi.Message> {

    private final LayoutInflater inflater;
    private int typeCount = 6;
    private long myId;
    private TdApi.ChatInfo info;

    private View.OnClickListener onClickListener;

    private Loader loader;

    public MessageAdapter(final Activity context, long myId, final Loader loader, TdApi.ChatInfo info) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
        this.myId = myId;
        this.loader = loader;
        this.info = info;

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TdApi.Message message = (TdApi.Message) v.getTag();

                switch (message.message.getConstructor()) {
                    case TdApi.MessageAudio.CONSTRUCTOR: {
                        TdApi.MessageAudio audio = (TdApi.MessageAudio) message.message;
                        ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.download_document_progress_bar);
                        progressBar.setVisibility(View.VISIBLE);
                        TdApi.File file = audio.audio.audio;
                        if (file.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
                            TdApi.FileLocal fileLocal = (TdApi.FileLocal) file;
                            loader.openFile(fileLocal.path, progressBar);
                        } else {
                            TdApi.FileEmpty fileEmpty = (TdApi.FileEmpty) file;
                            loader.loadFile(fileEmpty.id, progressBar);
                        }
                        break;
                    }
                    case TdApi.MessageContact.CONSTRUCTOR: {
                        TdApi.MessageContact contact = (TdApi.MessageContact) message.message;
                        loader.openContact(contact.userId);
                        break;
                    }
                    case TdApi.MessageDocument.CONSTRUCTOR: {
                        TdApi.MessageDocument document = (TdApi.MessageDocument) message.message;
                        if (document.document.mimeType.contains("gif")) {
                            v.findViewById(R.id.document_load_icon).setVisibility(View.GONE);
                            v.findViewById(R.id.gif_blend).setVisibility(View.GONE);
                            TdApi.File documentFile = document.document.document;
                            ImageView icon = (ImageView) v.findViewById(R.id.document_icon);
                            icon.setImageBitmap(null);
                            Utils.gifFileCheckerAndLoader(documentFile, context, icon);
                        } else {
                            ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.download_document_progress_bar);
                            progressBar.setVisibility(View.VISIBLE);
                            TdApi.File file = document.document.document;
                            if (file.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
                                TdApi.FileLocal fileLocal = (TdApi.FileLocal) file;
                                loader.openFile(fileLocal.path, progressBar);
                            } else {
                                TdApi.FileEmpty fileEmpty = (TdApi.FileEmpty) file;
                                loader.loadFile(fileEmpty.id, progressBar);
                            }
                        }
                        break;
                    }
                    case TdApi.MessageGeoPoint.CONSTRUCTOR: {
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
                    }
                    case TdApi.MessagePhoto.CONSTRUCTOR: {
                        TdApi.MessagePhoto photo = (TdApi.MessagePhoto) message.message;
                        TdApi.File file = findCorrectSizeImageFile(photo);
                        Intent intent = new Intent(context, PhotoViewerActivity.class);
                        if (file.getConstructor() == TdApi.FileEmpty.CONSTRUCTOR) {
                            TdApi.FileEmpty fileEmpty = (TdApi.FileEmpty) file;
                            intent.putExtra("file_id", fileEmpty.id);
                        }
                        if (file.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
                            TdApi.FileLocal fileLocal = (TdApi.FileLocal) file;
                            intent.putExtra("file_path", fileLocal.path);
                        }
                        context.startActivity(intent);
                        context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    }
                    case TdApi.MessageVideo.CONSTRUCTOR: {
                        TdApi.MessageVideo video = (TdApi.MessageVideo) message.message;
                        ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.download_video_progress_bar);
                        progressBar.setVisibility(View.VISIBLE);
                        TdApi.File file = video.video.video;
                        if (file.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
                            TdApi.FileLocal fileLocal = (TdApi.FileLocal) file;
                            loader.openFile(fileLocal.path, progressBar);
                        } else {
                            TdApi.FileEmpty fileEmpty = (TdApi.FileEmpty) file;
                            loader.loadFile(fileEmpty.id, progressBar);
                        }
                        break;
                    }
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
        if (position == Const.LIST_PRELOAD_POSITION) {
            loader.loadMore();
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
        if (item.selected) {
            convertView.setBackgroundResource(R.drawable.msg_selected);
        } else {
            Utils.verifySetBackground(convertView, null);
        }

        FrameLayout layout = new FrameLayout(getContext());
        FrameLayout forwarded_from = (FrameLayout) convertView.findViewById(R.id.forwarded_from_layout);
        forwarded_from.removeAllViews();

        if (item.forwardFromId != 0) {
            forwarded_from = (FrameLayout) convertView.findViewById(R.id.forwarded_from_layout);
            forwarded_from.removeAllViews();

            View textView = View.inflate(getContext(), R.layout.chat_user_name_layout, null);
            TdApi.User user = UserInfoHolder.getUser(item.forwardFromId);
            TextView forwardedFromName = ((TextView) textView.findViewById(R.id.chat_user_name_text_view));
            forwardedFromName.setTextColor(getContext().getResources().getColor(R.color.content_text_color));
            if (user != null) {
                forwardedFromName.setText(getContext().getString(R.string.forwarded_message_from) + user.firstName + " " + user.lastName);
            } else {
                forwardedFromName.setText(getContext().getString(R.string.forwarded_message_from_id) + item.forwardFromId);
            }
            forwarded_from.addView(textView);
        }

        if (info.getConstructor() == TdApi.GroupChatInfo.CONSTRUCTOR &&
                (getItemViewType(position) != Const.OUT_MESSAGE && getItemViewType(position)
                        != Const.OUT_CONTENT_MESSAGE && getItemViewType(position) != Const.OUT_STICKER)) {
            FrameLayout base_layout = (FrameLayout) convertView.findViewById(R.id.base_layout);
            base_layout.removeAllViews();

            View textView = View.inflate(getContext(), R.layout.chat_user_name_layout, null);
            TdApi.User user = UserInfoHolder.getUser(item.fromId);
            if (user != null) {
                ((TextView) textView.findViewById(R.id.chat_user_name_text_view)).setText(user.firstName + " " + user.lastName);
            } else {
                ((TextView) textView.findViewById(R.id.chat_user_name_text_view)).setText("ID: " + item.fromId);
            }
            base_layout.addView(textView);
        }

        if (item.message instanceof TdApi.MessagePhoto) {
            TdApi.MessagePhoto messagePhoto = (TdApi.MessagePhoto) item.message;
            final ImageView photo = new ImageView(getContext());
            String photoSize;
            if (havePhotoSizeM(messagePhoto.photo.photos)) {
                photoSize = "m";
            } else {
                photoSize = "s";
            }
            for (int i = 0; i < messagePhoto.photo.photos.length; i++) {
                if (messagePhoto.photo.photos[i].type.equals(photoSize)) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(messagePhoto.photo.photos[i].width, messagePhoto.photo.photos[i].height);
                    photo.setLayoutParams(layoutParams);
                    Utils.photoFileCheckerAndLoader(messagePhoto.photo.photos[i].photo, photo, (Activity) getContext());
                }
            }
            layout.addView(photo);
        }
        if (item.message instanceof TdApi.MessageAudio) {
            //Log.i("Message", "Audio " + item.message);
            TdApi.MessageAudio audio = (TdApi.MessageAudio) item.message;
            TdApi.Audio file = audio.audio;

            View view = inflater.inflate(R.layout.document_view_layout, null);
            ImageView icon = (ImageView) view.findViewById(R.id.document_icon);
            TextView name = (TextView) view.findViewById(R.id.document_name);
            TextView size = (TextView) view.findViewById(R.id.document_size);
            if (file.audio.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
                TdApi.FileLocal fileLocal = (TdApi.FileLocal) file.audio;
                size.setText(Utils.formatFileSize(fileLocal.size));
                icon.setImageResource(R.drawable.photocheck);
            } else {
                TdApi.FileEmpty fileEmpty = (TdApi.FileEmpty) file.audio;
                size.setText(Utils.formatFileSize(fileEmpty.size));
                icon.setImageResource(R.drawable.photoload);
            }
            name.setText(file.mimeType + getContext().getString(R.string.duration) + Utils.getDateFormat(Const.TIME_PATTERN).format(file.duration));
            layout.addView(view);
        }
        if (item.message instanceof TdApi.MessageContact) {
            //Log.i("Message", "Contact " + item.message);
            TdApi.MessageContact contact = (TdApi.MessageContact) item.message;

            View contactView = inflater.inflate(R.layout.contact_message, null);
            TextView name = (TextView) contactView.findViewById(R.id.contact_message_name);
            TextView phone = (TextView) contactView.findViewById(R.id.contact_message_phone);
            TextView icon = (TextView) contactView.findViewById(R.id.contact_message_icon);
            Utils.verifySetBackground(icon, Utils.getShapeDrawable(R.dimen.toolbar_icon_size, -contact.userId));
            icon.setText(Utils.getInitials(contact.firstName, contact.lastName));
            phone.setText(contact.phoneNumber);
            name.setText(contact.firstName + " " + contact.lastName);
            layout.addView(contactView);
        }
        if (item.message instanceof TdApi.MessageDocument) {
            //Log.e("Message", "Document " + item.message);
            TdApi.MessageDocument doc = (TdApi.MessageDocument) item.message;
            if (doc.document.mimeType.contains("gif")) {
                TdApi.PhotoSize thumb = doc.document.thumb;
                View gifView = inflater.inflate(R.layout.gif_document_view_layout, null);
                ImageView icon = (ImageView) gifView.findViewById(R.id.document_icon);
                if (thumb.width != 0) {
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(thumb.width * 3, thumb.height * 3);
                    icon.setLayoutParams(layoutParams);
                }
                TdApi.File documentFile = doc.document.document;
                Utils.photoFileCheckerAndLoader(documentFile, icon, (Activity) getContext());

                layout.addView(gifView);
            } else {
                View view = inflater.inflate(R.layout.document_view_layout, null);
                ImageView icon = (ImageView) view.findViewById(R.id.document_icon);
                TextView name = (TextView) view.findViewById(R.id.document_name);
                TextView size = (TextView) view.findViewById(R.id.document_size);
                if (doc.document.document.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
                    TdApi.FileLocal fileLocal = (TdApi.FileLocal) doc.document.document;
                    size.setText(Utils.formatFileSize(fileLocal.size));
                    icon.setImageResource(R.drawable.photocheck);
                } else {
                    TdApi.FileEmpty fileEmpty = (TdApi.FileEmpty) doc.document.document;
                    size.setText(Utils.formatFileSize(fileEmpty.size));
                    icon.setImageResource(R.drawable.photoload);
                }
                name.setText(doc.document.fileName);
                layout.addView(view);
            }
        }
        if (item.message instanceof TdApi.MessageGeoPoint) {
            TdApi.MessageGeoPoint point = (TdApi.MessageGeoPoint) item.message;
            int height = 150;
            int width = 250;

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
            Utils.photoFileCheckerAndLoader(sticker.sticker, stickerImage, (Activity) getContext());
            layout.addView(stickerImage);
        }
        if (item.message instanceof TdApi.MessageVideo) {
            //Log.e("Message", "Video " + item.message);
            TdApi.MessageVideo messageVideo = (TdApi.MessageVideo) item.message;
            View view = inflater.inflate(R.layout.video_message_view, null);
            ImageView icon = (ImageView) view.findViewById(R.id.video_icon);
            ImageView loadIcon = (ImageView) view.findViewById(R.id.video_load_icon);

            TdApi.File file = messageVideo.video.thumb.photo;
            TdApi.PhotoSize photo = messageVideo.video.thumb;
            if (file.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(photo.width * 3, photo.height * 3);
                icon.setLayoutParams(params);
                TdApi.FileLocal fileLocal = (TdApi.FileLocal) file;
                icon.setImageURI(Uri.parse(fileLocal.path));
            } else {
                TdApi.FileEmpty fileEmpty = (TdApi.FileEmpty) file;
                if (fileEmpty.id == 0) {
                    icon.setImageResource(R.drawable.ic_netelegram_placeholder);
                } else {
                    Utils.photoFileCheckerAndLoader(fileEmpty, icon, (Activity) getContext());
                }
            }
            if (messageVideo.video.video.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
                loadIcon.setImageResource(R.drawable.photocheck);
            } else {
                loadIcon.setImageResource(R.drawable.photoload);
            }
            layout.addView(view);
        }
        if (item.message instanceof TdApi.MessageUnsupported) {
            //Log.i("Message", "Unsupported " + item.message);
            TextView unsupported = new TextView(getContext());
            unsupported.setText(R.string.unsupported);

            layout.addView(unsupported);
        }

        long timeInMls = item.date;
        Date date = new Date(timeInMls * 1000);

        switch (getItemViewType(position)) {
            case Const.IN_MESSAGE:
                TdApi.MessageText inText = (TdApi.MessageText) item.message;

                TextView inMessage = (TextView) convertView.findViewById(R.id.in_msg);
                TextView inTime = (TextView) convertView.findViewById(R.id.in_msg_time);
                inMessage.setAutoLinkMask(Linkify.WEB_URLS | Linkify.PHONE_NUMBERS);

                inMessage.setText(inText.textWithSmilesAndUserRefs);
                inTime.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));
                break;
            case Const.OUT_MESSAGE:
                TdApi.MessageText outText = (TdApi.MessageText) item.message;

                TextView outMessage = (TextView) convertView.findViewById(R.id.out_msg);
                TextView outTime = (TextView) convertView.findViewById(R.id.out_msg_time);
                outMessage.setAutoLinkMask(Linkify.WEB_URLS | Linkify.PHONE_NUMBERS);

                outMessage.setText(outText.textWithSmilesAndUserRefs);
                outTime.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));
                break;
            case Const.IN_CONTENT_MESSAGE:
                FrameLayout inContent = (FrameLayout) convertView.findViewById(R.id.in_content);

                inContent.removeAllViews();
                TextView inContentTime = (TextView) convertView.findViewById(R.id.in_content_msg_time);

                inContent.setOnClickListener(onClickListener);
                inContent.setTag(getItem(position));

                inContent.addView(layout);
                inContentTime.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));
                break;
            case Const.OUT_CONTENT_MESSAGE:
                FrameLayout outContent = (FrameLayout) convertView.findViewById(R.id.out_content);

                outContent.removeAllViews();
                TextView outContentTime = (TextView) convertView.findViewById(R.id.out_content_msg_time);

                outContent.setOnClickListener(onClickListener);
                outContent.setTag(getItem(position));

                outContent.addView(layout);
                outContentTime.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));
                break;
            case Const.IN_STICKER:
                FrameLayout inSticker = (FrameLayout) convertView.findViewById(R.id.in_sticker);
                inSticker.removeAllViews();

                TextView inStickerTime = (TextView) convertView.findViewById(R.id.in_sticker_msg_time);

                inSticker.addView(layout);
                inStickerTime.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));
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

    private boolean havePhotoSizeM(TdApi.PhotoSize[] sizes) {
        for (int i = 0; i < sizes.length; i++) {
            if (sizes[i].type.equals("m")) {
                return true;
            }
        }
        return false;
    }

    private TdApi.File findCorrectSizeImageFile(TdApi.MessagePhoto photo) {
        for (int i = photo.photo.photos.length - 1; i > 0; i--) {
            if (photo.photo.photos[i].height > 2048 || photo.photo.photos[i].height > 2048) {
                continue;
            } else {
                return photo.photo.photos[i].photo;
            }
        }
        return photo.photo.photos[0].photo;
    }

    public interface Loader {
        void loadMore();

        void loadFile(int id, View v);

        void openFile(String path, View v);

        void openContact(long id);
    }
}
