package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.DownloadFileHandler;
import net.mobindustry.telegram.model.holder.MessagesFragmentHolder;
import net.mobindustry.telegram.model.holder.UserInfoHolder;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.ImageLoaderHelper;
import net.mobindustry.telegram.utils.Utils;
import net.mobindustry.telegram.ui.emoji.Emoji;
import net.mobindustry.telegram.ui.emoji.EmojiParser;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.Date;

public class ChatListAdapter extends ArrayAdapter<TdApi.Chat> {

    private final LayoutInflater inflater;
    private Emoji emoji;
    private EmojiParser emojiParser;

    public ChatListAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
        emoji = MessagesFragmentHolder.getInstance().getEmoji();
        emojiParser = new EmojiParser(emoji);
    }

    //TODO correct show avatar

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chat_item, parent, false);
        }

        TextView icon = (TextView) convertView.findViewById(R.id.message_icon_text);
        TextView firstLastName = (TextView) convertView.findViewById(R.id.firstLastName);
        TextView lastMessage = (TextView) convertView.findViewById(R.id.lastMessage);
        TextView time = (TextView) convertView.findViewById(R.id.contactItemTime);
        TextView notify = (TextView) convertView.findViewById(R.id.chat_notification);

        final ImageView imageIcon = (ImageView) convertView.findViewById(R.id.message_icon_image);

        imageIcon.setImageBitmap(null);
        lastMessage.setText("");

        TdApi.Chat item = getItem(position);
        TdApi.ChatInfo info = item.type;

        TdApi.MessageText text = null;
        TdApi.Message message = item.topMessage;

        long timeMls = (long) message.date;
        Date date = new Date(timeMls * 1000);

        if (message.message instanceof TdApi.MessageText) {
            text = (TdApi.MessageText) message.message;
            emojiParser.parse(text);

            lastMessage.setTextColor(Color.BLACK);
            lastMessage.setText(text.textWithSmilesAndUserRefs);
        } else {
            lastMessage.setTextColor(getContext().getResources().getColor(R.color.content_text_color));
            if (message.message instanceof TdApi.MessagePhoto) {
                lastMessage.setText("Photo");
            }
            if (message.message instanceof TdApi.MessageAudio) {
                lastMessage.setText("Audio");
            }
            if (message.message instanceof TdApi.MessageContact) {
                lastMessage.setText("Contact");
            }
            if (message.message instanceof TdApi.MessageDocument) {
                lastMessage.setText("Document");
            }
            if (message.message instanceof TdApi.MessageGeoPoint) {
                lastMessage.setText("GeoPoint");
            }
            if (message.message instanceof TdApi.MessageSticker) {
                lastMessage.setText("Sticker");
            }
            if (message.message instanceof TdApi.MessageVideo) {
                lastMessage.setText("Video");
            }
            if (message.message instanceof TdApi.MessageUnsupported) {
                lastMessage.setText("Unknown");
            }
        }

        TdApi.File file = null;
        long chatId = item.id;
        String userFirstName = "";
        String userLastName = "";

        if (info.getConstructor() == TdApi.PrivateChatInfo.CONSTRUCTOR) {
            TdApi.PrivateChatInfo privateChatInfo = (TdApi.PrivateChatInfo) info;
            TdApi.User chatUser = privateChatInfo.user;
            file = chatUser.photoBig;
            userFirstName = privateChatInfo.user.firstName;
            userLastName = privateChatInfo.user.lastName;
        }
        if (info.getConstructor() == TdApi.GroupChatInfo.CONSTRUCTOR) {
            TdApi.GroupChatInfo groupChatInfo = (TdApi.GroupChatInfo) info;
            file = groupChatInfo.groupChat.photoBig;
            userFirstName = groupChatInfo.groupChat.title;
            userLastName = "";
        }

        if (item.unreadCount != 0) {
            notify.setText(String.valueOf(item.unreadCount));
            notify.setBackground(Utils.getShapeDrawable(R.dimen.chat_list_item_notification_size, getContext().getResources().getColor(R.color.message_notify)));
        } else {
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                notify.setBackgroundDrawable(null);
            } else {
                notify.setBackground(null);
            }
            notify.setText("");
        }

        if (file != null) {
            if (file.getConstructor() == TdApi.FileEmpty.CONSTRUCTOR) {
                final TdApi.FileEmpty fileEmpty = (TdApi.FileEmpty) file;
                if (fileEmpty.id != 0) {
                    new ApiClient<>(new TdApi.DownloadFile(fileEmpty.id), new DownloadFileHandler(), new ApiClient.OnApiResultHandler() {
                        @Override
                        public void onApiResult(BaseHandler output) {
                            if (output.getHandlerId() == DownloadFileHandler.HANDLER_ID) {
                                ImageLoaderHelper.displayImageList(String.valueOf(fileEmpty.id), imageIcon);
                            }
                        }
                    }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                } else {
                    icon.setVisibility(View.VISIBLE);

                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        if(chatId < 0) {
                            icon.setBackgroundDrawable(Utils.getShapeDrawable(R.dimen.toolbar_icon_size, (int) chatId));
                        } else {
                            icon.setBackgroundDrawable(Utils.getShapeDrawable(R.dimen.toolbar_icon_size, (int) -chatId));
                        }

                    } else {
                        if(chatId < 0) {
                            icon.setBackground(Utils.getShapeDrawable(R.dimen.toolbar_icon_size, (int) chatId));
                        } else {
                            icon.setBackground(Utils.getShapeDrawable(R.dimen.toolbar_icon_size, (int) -chatId));
                        }
                    }
                    icon.setText(Utils.getInitials(userFirstName, userLastName));
                }
            }
            if (file.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
                imageIcon.setVisibility(View.VISIBLE);
                TdApi.FileLocal fileLocal = (TdApi.FileLocal) file;
                ImageLoaderHelper.displayImageList(Const.IMAGE_LOADER_PATH_PREFIX + fileLocal.path, imageIcon);
            }
        }

        firstLastName.setText(userFirstName + " " + userLastName);
        time.setText(Utils.getDateFormat(Const.TIME_PATTERN).format(date));

        return convertView;
    }
}
