package net.mobindustry.telegram.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.soundcloud.android.crop.Crop;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.ChatHistoryHandler;
import net.mobindustry.telegram.core.handlers.DownloadFileHandler;
import net.mobindustry.telegram.core.handlers.MessageHandler;
import net.mobindustry.telegram.core.handlers.OkHandler;
import net.mobindustry.telegram.model.Enums;
import net.mobindustry.telegram.model.holder.DownloadFileHolder;
import net.mobindustry.telegram.model.holder.ListFoldersHolder;
import net.mobindustry.telegram.model.holder.MessagesFragmentHolder;
import net.mobindustry.telegram.ui.activity.ChatActivity;
import net.mobindustry.telegram.ui.activity.TransparentActivity;
import net.mobindustry.telegram.ui.adapters.MessageAdapter;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.ImageLoaderHelper;
import net.mobindustry.telegram.utils.Utils;
import net.mobindustry.telegram.ui.emoji.Emoji;
import net.mobindustry.telegram.ui.emoji.EmojiKeyboardView;
import net.mobindustry.telegram.ui.emoji.EmojiParser;
import net.mobindustry.telegram.ui.emoji.EmojiPopup;
import net.mobindustry.telegram.ui.emoji.ObservableLinearLayout;

import org.drinkless.td.libcore.telegram.TdApi;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MessagesFragment extends Fragment implements Serializable, ApiClient.OnApiResultHandler {

    public static final int LEVEL_SEND = 0;
    public static final int LEVEL_ATTACH = 1;
    public static final int LEVEL_SMILE = 1;
    public static final int LEVEL_ARROW = 0;
    private static final long SCALE_UP_DURATION = 80;
    private static final long SCALE_DOWN_DURATION = 80;
    private final int FIRST_MESSAGE_LOAD_LIMIT = 60;
    private final int MESSAGE_LOAD_LIMIT = 60;
    private final int MESSAGE_LOAD_OFFSET = 0;
    private final int NEW_MESSAGE_LOAD_OFFSET = -1;
    private int firstVisibleItem = 0;
    private int loadedMessagesCount = 0;
    private int topMessageId;
    private int toScrollLoadMessageId;

    public boolean isMessagesLoading = false;
    public boolean needLoad = true;

    private ChatActivity activity;
    private MessageAdapter adapter;
    private AnimatorSet currentAnimation;
    private MessagesFragmentHolder holder;
    private ListView messageListView;
    private ProgressBar progressBar;
    private EditText input;
    private TextView noMessages;
    private ObservableLinearLayout linearLayout;
    private ImageView attach;
    private ImageView smiles;

    private Emoji emoji;
    private EmojiParser emojiParser;
    @Nullable
    private EmojiPopup emojiPopup;

    private TdApi.Chat chat;

    public static MessagesFragment newInstance(int index) {
        MessagesFragment f = new MessagesFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DownloadFileHolder.clearList();

        View view = inflater.inflate(R.layout.message_fragment, container, false);
        linearLayout = (ObservableLinearLayout) view.findViewById(R.id.observable_layout);
        noMessages = (TextView) view.findViewById(R.id.no_messages_message);

        messageListView = (ListView) view.findViewById(R.id.messageListView);
        messageListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                setFirstVisibleItem(firstVisibleItem);
            }
        });
        adapter = new MessageAdapter(getActivity(), ((ChatActivity) getActivity()).getMyId(), new MessageAdapter.Loader() {
            @Override
            public void loadMore() {
                if (needLoad && !isMessagesLoading) {
                    getChatHistory(chat.id, toScrollLoadMessageId, MESSAGE_LOAD_OFFSET, MESSAGE_LOAD_LIMIT, Enums.MessageAddType.SCROLL);
                    isMessagesLoading = true;
                }
            }

            @Override
            public void loadDocument(int id, String mime) {
                Utils.fileLoader(id);
            }

            @Override
            public void openDocument(String path, String mime) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(path), mime);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("Log", "Бабах!!!");
                }
                //startActivityForResult(intent, ExplorerActivity.FILE_CLOSED_SUCCESSFULLY);
            }
        });
        messageListView.setAdapter(adapter);
        progressBar = (ProgressBar) view.findViewById(R.id.messages_progress_bar);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((ChatActivity) getActivity()).clearSearch();

        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "This function coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        holder = MessagesFragmentHolder.getInstance();
        emoji = holder.getEmoji();
        emojiParser = new EmojiParser(emoji);
        activity = (ChatActivity) getActivity();

        final ChatListFragment fragment = (ChatListFragment) activity.getSupportFragmentManager().findFragmentById(R.id.chat_list);
        chat = fragment.getChat();
        if (MessagesFragmentHolder.getTopMessage(chat.id) != 0) {
            topMessageId = MessagesFragmentHolder.getTopMessage(chat.id);
        } else {
            topMessageId = chat.topMessage.id;
        }

        holder.setChat(chat);

        input = (EditText) getActivity().findViewById(R.id.message_edit_text);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    animateSendAttachButton(LEVEL_ATTACH);
                } else {
                    animateSendAttachButton(LEVEL_SEND);
                }
            }
        });

        attach = (ImageView) getActivity().findViewById(R.id.attach);
        attach.setImageLevel(LEVEL_ATTACH);

        smiles = (ImageView) getActivity().findViewById(R.id.smiles);
        smiles.setImageLevel(LEVEL_SMILE);

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString();
                if (text.isEmpty()) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            showPopupMenu(attach);
                        }
                    }, 100L);
                } else {
                    sendTextMessage(chat.id, input.getText().toString().trim());
                    input.setText("");
                }
            }
        });


        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.messageFragmentToolbar);
        if (toolbar != null) {
            TextView icon = (TextView) getActivity().findViewById(R.id.toolbar_text_icon);
            TextView name = (TextView) getActivity().findViewById(R.id.toolbar_text_name);
            TextView lastSeenText = (TextView) getActivity().findViewById(R.id.toolbar_text_last_seen);

            final RoundedImageView imageIcon = (RoundedImageView) getActivity().findViewById(R.id.toolbar_image_icon);

            String title = "";
            TdApi.File file = null;
            long chatId = chat.id;

            String userFirstName = "";
            String userLastName = "";

            if (chat.type.getConstructor() == TdApi.PrivateChatInfo.CONSTRUCTOR) {
                TdApi.PrivateChatInfo privateChatInfo = (TdApi.PrivateChatInfo) chat.type;
                TdApi.User chatUser = privateChatInfo.user;
                title = chatUser.firstName + " " + chatUser.lastName;
                TdApi.UserStatus status = chatUser.status;
                lastSeenText.setText(Utils.getUserStatusString(status));
                file = chatUser.photoBig;
                userFirstName = privateChatInfo.user.firstName;
                userLastName = privateChatInfo.user.lastName;
            }
            if (chat.type.getConstructor() == TdApi.GroupChatInfo.CONSTRUCTOR) {
                TdApi.GroupChatInfo groupChatInfo = (TdApi.GroupChatInfo) chat.type;
                title = groupChatInfo.groupChat.title;
                file = groupChatInfo.groupChat.photoBig;
                userFirstName = groupChatInfo.groupChat.title;
                userLastName = "";
            }
            if (file != null) {
                if (file.getConstructor() == TdApi.FileEmpty.CONSTRUCTOR) {
                    final TdApi.FileEmpty fileEmpty = (TdApi.FileEmpty) file;
                    if (fileEmpty.id != 0) {
                        new ApiClient<>(new TdApi.DownloadFile(fileEmpty.id), new DownloadFileHandler(), new ApiClient.OnApiResultHandler() {
                            @Override
                            public void onApiResult(BaseHandler output) {
                                if (output.getHandlerId() == DownloadFileHandler.HANDLER_ID) {
                                    imageIcon.setVisibility(View.VISIBLE);
                                    ImageLoaderHelper.displayImageList(String.valueOf(fileEmpty.id), imageIcon);
                                }
                            }
                        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                    } else {
                        icon.setVisibility(View.VISIBLE);

                        int sdk = android.os.Build.VERSION.SDK_INT;
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            icon.setBackgroundDrawable(Utils.getShapeDrawable(R.dimen.toolbar_icon_size, (int) -chatId));
                        } else {
                            icon.setBackground(Utils.getShapeDrawable(R.dimen.toolbar_icon_size, (int) -chatId));
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

            if (title != null) {
                name.setText(title);
            } else {
                name.setText("Title error");
            }

            toolbar.inflateMenu(R.menu.message_menu);

            final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            if (getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_PORTRAIT) {
                toolbar.setNavigationIcon(R.drawable.ic_back);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        destroyFragment(fragmentTransaction);
                    }
                });
            } else {
                toolbar.setNavigationIcon(R.drawable.ic_close_white);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        destroyFragment(fragmentTransaction);
                    }
                });
            }
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.clear_history:
                            Log.e("Log", "ClearChatHistory");
                            new ApiClient<>(new TdApi.DeleteChatHistory(chat.id), new OkHandler(), new ApiClient.OnApiResultHandler() {
                                @Override
                                public void onApiResult(BaseHandler output) {
                                    if (output == null) {
                                        Log.e("Log", "null");
                                    }
                                    Log.e("Log", "History cleared");
                                    destroyFragment(fragmentTransaction);
                                    fragment.deleteChatFromAdapter(chat);
                                }
                            }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                            break;
//                        case R.id.mute_notification:
//                            Log.e("Log", "MuteNotification");
//                            break;
//                        case R.id.delete_chat:
//                            Log.e("Log", "DeleteChat");
//
//                            break;
                    }
                    return false;
                }
            });
        }

        getChatHistory(chat.id, topMessageId, NEW_MESSAGE_LOAD_OFFSET, FIRST_MESSAGE_LOAD_LIMIT, Enums.MessageAddType.ALL);

        smiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emojiPopup != null) {
                    smiles.setImageLevel(LEVEL_SMILE);
                    emojiPopup.dismiss();
                } else {
                    emojiPopup = EmojiPopup.create(getActivity(), linearLayout, new EmojiKeyboardView.CallBack() {
                        @Override
                        public void backspaceClicked() {
                            input.dispatchKeyEvent(new KeyEvent(0, KeyEvent.KEYCODE_DEL));
                        }

                        @Override
                        public void emojiClicked(long code) {
                            String strEmoji = emoji.toString(code);
                            Editable text = input.getText();
                            text.append(emoji.replaceEmoji(strEmoji));
                        }

                        @Override
                        public void stickerCLicked(String stickerFilePath) {
                            sendStickerMessage(getShownChatId(), stickerFilePath);
                        }
                    });
                    emojiPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            smiles.setImageLevel(LEVEL_SMILE);
                            emojiPopup = null;
                        }
                    });
                    smiles.setImageLevel(LEVEL_ARROW);
                    assert emojiPopup != null;
                }
            }
        });
    }

    private void destroyFragment(FragmentTransaction fragmentTransaction) {
        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.fragment_layout);
        layout.setVisibility(View.VISIBLE);
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
        fragmentTransaction.remove(MessagesFragment.this).commit();
        dissmissEmojiPopup();
    }

    public void addNewMessage(final TdApi.Messages messages) {
        adapter.add(parseEmojiMessages(messages.messages[0]));
    }

    public void addLatestMessages(final TdApi.Messages messages) {
        adapter.setNotifyOnChange(false);
        for (int i = 0; i < messages.messages.length; i++) {
            adapter.insert(parseEmojiMessages(messages.messages[i]), 0);
        }
        adapter.setNotifyOnChange(true);
    }

    public void setChatHistory(final TdApi.Messages messages) {
        adapter.setNotifyOnChange(false);
        for (int i = 0; i < messages.messages.length; i++) {
            adapter.insert(parseEmojiMessages(messages.messages[i]), 0);
        }
        adapter.setNotifyOnChange(true);
        adapter.notifyDataSetChanged();
        noMessages.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void setFirstVisibleItem(int firstVisibleItem) {
        this.firstVisibleItem = firstVisibleItem;
    }

    public long getShownChatId() {
        return chat.id;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    public void getChatHistory(final long id, final int messageId, final int offset, final int limit, final Enums.MessageAddType type) {
        new ApiClient<>(new TdApi.GetChatHistory(id, messageId, offset, limit), new ChatHistoryHandler(), new ApiClient.OnApiResultHandler() {
            @Override
            public void onApiResult(BaseHandler output) {
                if (output == null) {
                    progressBar.setVisibility(View.GONE);
                    noMessages.setVisibility(View.VISIBLE);
                }
                if (output.getHandlerId() == ChatHistoryHandler.HANDLER_ID) {
                    TdApi.Messages messages = (TdApi.Messages) output.getResponse();
                    if (messages.messages.length != 0 && chat.id == messages.messages[0].chatId) {
                        switch (type) {
                            case ALL:
                                toScrollLoadMessageId = messages.messages[messages.messages.length - 1].id;
                                setChatHistory(messages);
                                break;
                            case NEW:
                                topMessageId = messages.messages[0].id;
                                addNewMessage(messages);
                                messageListView.setSelection(adapter.getCount() - 1);
                                break;
                            case SCROLL:
                                toScrollLoadMessageId = messages.messages[messages.messages.length - 1].id;
                                loadedMessagesCount = messages.messages.length;
                                addLatestMessages(messages);
                                adapter.notifyDataSetChanged();
                                messageListView.setSelection(loadedMessagesCount + firstVisibleItem);
                                isMessagesLoading = false;
                                break;
                        }
                    } else if (messages.messages.length == 0) {
                        needLoad = false;
                    }
                }
            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    private TdApi.Message parseEmojiMessages(TdApi.Message message1) {
        TdApi.Message message = message1;
        if (message.message.getConstructor() == TdApi.MessageText.CONSTRUCTOR) {
            TdApi.MessageText text = (TdApi.MessageText) message.message;
            emojiParser.parse(text);
        }
        return message;
    }

    public void sendTextMessage(long chatId, String message) {
        new ApiClient<>(new TdApi.SendMessage(chatId, new TdApi.InputMessageText(message)), new MessageHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void sendPhotoMessage(long chatId, String path) {
        new ApiClient<>(new TdApi.SendMessage(chatId, new TdApi.InputMessagePhoto(path)), new MessageHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void sendStickerMessage(long chatId, String path) {
        new ApiClient<>(new TdApi.SendMessage(chatId, new TdApi.InputMessageSticker(path)), new MessageHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        dissmissEmojiPopup();
    }

    @Override
    public void onApiResult(BaseHandler output) {
        if (output.getHandlerId() == MessageHandler.HANDLER_ID) {
        }
    }

    private void animateSendAttachButton(final int level) {
        LevelListDrawable drawable = (LevelListDrawable) attach.getDrawable();
        if (drawable.getLevel() == level) {
            return;
        }
        if (currentAnimation != null) {
            currentAnimation.cancel();
        }

        AnimatorSet scaleDown = new AnimatorSet()
                .setDuration(SCALE_DOWN_DURATION);
        scaleDown.playTogether(
                ObjectAnimator.ofFloat(attach, View.SCALE_X, 1f, 0.1f),
                ObjectAnimator.ofFloat(attach, View.SCALE_Y, 1f, 0.1f));
        scaleDown.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                attach.setImageLevel(level);
            }
        });
        AnimatorSet scaleUp = new AnimatorSet()
                .setDuration(SCALE_UP_DURATION);
        scaleUp.playTogether(
                ObjectAnimator.ofFloat(attach, View.SCALE_X, 0.1f, 1f),
                ObjectAnimator.ofFloat(attach, View.SCALE_Y, 0.1f, 1f));
        currentAnimation = new AnimatorSet();
        currentAnimation.playSequentially(scaleDown, scaleUp);
        currentAnimation.start();
    }

    private void showPopupMenu(View v) {

        final PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {

                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popupMenu.getMenuInflater().inflate(R.menu.attach_menu, popupMenu.getMenu());
        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.take_photo:
                                makePhoto();
                                break;
                            case R.id.gallery:
                                Intent intentGallery = new Intent(getActivity(), TransparentActivity.class);
                                intentGallery.putExtra("choice", Const.GALLERY_FRAGMENT);
                                startActivityForResult(intentGallery, 1);
                                ListFoldersHolder.setCheckQuantity(0);
                                ListFoldersHolder.setListFolders(null);
                                ListFoldersHolder.setList(null);
                                ListFoldersHolder.setChatID(getShownChatId());
                                break;
                            case R.id.video:
                                Toast.makeText(getActivity(),
                                        "video", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.file:
                                Intent intent = new Intent(getActivity(), TransparentActivity.class);
                                intent.putExtra("choice", Const.FILE_CHOOSE_FRAGMENT);
                                startActivityForResult(intent, 1);
                                break;
                            case R.id.location:
                                Intent intentLoc = new Intent(getActivity(), TransparentActivity.class);
                                intentLoc.putExtra("choice", Const.MAP_FRAGMENT);
                                startActivityForResult(intentLoc, 1);
                                break;
                        }
                        return true;
                    }
                });
        popupMenu.show();
    }

    private void makePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File tempTakePhotoFile = holder.getNewTempPhotoFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempTakePhotoFile));
        startActivityForResult(intent, Const.REQUEST_CODE_TAKE_PHOTO);
        Log.e("LOG", "ACTIVITY " + activity);
        Log.e("LOG", "FILE" + tempTakePhotoFile);
    }

    public static String getPathFromURI(Uri contentUri, Activity activity) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public String getPhotoPath() {
        return holder.getTempPhotoFile().getPath();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.REQUEST_CODE_TAKE_PHOTO && resultCode == getActivity().RESULT_OK) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(holder.getTempPhotoFile());
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);
            Uri external = Uri.fromFile(holder.getTempPhotoFile());
            Crop.of(external, external).start(getActivity(), Const.CROP_REQUEST_CODE);
        }

        if (requestCode == Const.REQUEST_CODE_SELECT_IMAGE && resultCode == getActivity().RESULT_OK) {
            try {
                Uri uriImage = data.getData();
                String path = getPathFromURI(uriImage, getActivity());
                if (!TextUtils.isEmpty(path)) {
                    sendPhotoMessage(getShownChatId(), path);
                } else {
                    Toast.makeText(getActivity(), "File not found", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "File not found", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean dissmissEmojiPopup() {
        smiles.setImageLevel(LEVEL_SMILE);
        if (emojiPopup == null) {
            return false;
        }
        emojiPopup.dismiss();
        emojiPopup = null;
        Utils.hideKeyboard(input);
        return true;
    }

    public boolean isEmojiAttached() {
        if (emojiPopup == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDetach() {
        dissmissEmojiPopup();
        super.onDetach();
    }
}


