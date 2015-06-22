package net.mobindustry.telegram.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;
import com.soundcloud.android.crop.Crop;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.ChatHistoryHandler;
import net.mobindustry.telegram.core.handlers.DownloadFileHandler;
import net.mobindustry.telegram.core.handlers.MessageHandler;
import net.mobindustry.telegram.model.holder.DownloadFileHolder;
import net.mobindustry.telegram.model.holder.MessagesFragmentHolder;
import net.mobindustry.telegram.ui.activity.ChatActivity;
import net.mobindustry.telegram.ui.activity.TransparentActivity;
import net.mobindustry.telegram.ui.adapters.MessageAdapter;
import net.mobindustry.telegram.ui.adapters.TextWatcherAdapter;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.ImageLoaderHelper;
import net.mobindustry.telegram.utils.Utils;

import org.drinkless.td.libcore.telegram.TdApi;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MessagesFragment extends Fragment implements Serializable, ApiClient.OnApiResultHandler {

    public static final int LEVEL_SEND = 0;
    public static final int LEVEL_ATTACH = 1;
    private static final long SCALE_UP_DURATION = 80;
    private static final long SCALE_DOWN_DURATION = 80;

    private MessageAdapter adapter;

    private ImageView attach;
    private ImageView smiles;
    private File tempTakePhotoFile;
    private TextView icon;
    private TextView name;
    private TextView lastSeenText;

    private AnimatorSet currentAnimation;

    private TdApi.Chat chat;
    private ChatActivity activity;

    private MessagesFragmentHolder holder;
    private BroadcastReceiver receiver;
    private IntentFilter filter = new IntentFilter(Const.NEW_MESSAGE_INTENT_FILTER);

    public static MessagesFragment newInstance(int index) {
        MessagesFragment f = new MessagesFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public long getShownChatId() {
        return chat.id;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    public void setChatHistory(final TdApi.Messages messages) {
        adapter.clear();
        adapter.addAll(Utils.reverseMessages(messages.messages));
    }

    public void getChatHistory(final long id, final int messageId, final int offset, final int limit) {
        new ApiClient<>(new TdApi.GetChatHistory(id, messageId, offset, limit), new ChatHistoryHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void sendTextMessage(long chatId, String message) {
        new ApiClient<>(new TdApi.SendMessage(chatId, new TdApi.InputMessageText(message)), new MessageHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void sendPhotoMessage(long chatId, String path) {
        new ApiClient<>(new TdApi.SendMessage(chatId, new TdApi.InputMessagePhoto(path)), new MessageHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment, container, false);

        ListView messageListView = (ListView) view.findViewById(R.id.messageListView);
        adapter = new MessageAdapter(getActivity(), ((ChatActivity) getActivity()).getMyId());
        messageListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        holder = MessagesFragmentHolder.getInstance();
        activity = (ChatActivity) getActivity();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int id = intent.getIntExtra("message_id", 0);
                long chat_id = intent.getLongExtra("chatId", 0);
                if (chat_id == chat.id) {
                    getChatHistory(chat_id, id, -1, 200);
                }
            }
        };

        activity.registerReceiver(receiver, filter);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.messageFragmentToolbar);
        if (toolbar != null) {

            final EditText input = (EditText) getActivity().findViewById(R.id.message_edit_text);
            input.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        animateLevel(LEVEL_ATTACH);
                    } else {
                        animateLevel(LEVEL_SEND);
                    }
                }
            });

            attach = (ImageView) getActivity().findViewById(R.id.attach);
            attach.setImageLevel(LEVEL_ATTACH);

            smiles = (ImageView) getActivity().findViewById(R.id.smiles);
            icon = (TextView) getActivity().findViewById(R.id.toolbar_text_icon);
            name = (TextView) getActivity().findViewById(R.id.toolbar_text_name);
            lastSeenText = (TextView) getActivity().findViewById(R.id.toolbar_text_last_seen);

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

            ChatListFragment fragment = (ChatListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.titles);
            chat = fragment.getChat();

            holder.setChat(chat);

            TdApi.PrivateChatInfo privateChatInfo = (TdApi.PrivateChatInfo) chat.type; //TODO verify;
            TdApi.User chatUser = privateChatInfo.user;

            name.setText(chatUser.firstName + " " + chatUser.lastName);
            lastSeenText.setText("lastSeen"); //TODO

            final RoundedImageView imageIcon = (RoundedImageView) getActivity().findViewById(R.id.toolbar_image_icon);

            if (chatUser.photoBig instanceof TdApi.FileEmpty) {
                final TdApi.FileEmpty file = (TdApi.FileEmpty) chatUser.photoBig;
                if(file.id != 0) {
                    Log.e("Log", "Download file from message fragment: " + file.id);

                    new ApiClient<>(new TdApi.DownloadFile(file.id), new DownloadFileHandler(), new ApiClient.OnApiResultHandler() {
                        @Override
                        public void onApiResult(BaseHandler output) {
                            if (output.getHandlerId() == DownloadFileHandler.HANDLER_ID) {
                                imageIcon.setVisibility(View.VISIBLE);
                                ImageLoaderHelper.displayImage(String.valueOf(file.id), imageIcon);
                            }
                        }
                    }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                } else {
                    icon.setVisibility(View.VISIBLE);
                    icon.setBackground(Utils.getShapeDrawable(60, -chatUser.id));
                    icon.setText(Utils.getInitials(chatUser.firstName, chatUser.lastName));
                }

            }
            if (chatUser.photoBig instanceof TdApi.FileLocal) {
                imageIcon.setVisibility(View.VISIBLE);
                TdApi.FileLocal file = (TdApi.FileLocal) chatUser.photoBig;
                ImageLoaderHelper.displayImage("file://" + file.path, imageIcon);
            }

            getChatHistory(chat.id, chat.topMessage.id, -1, 200);

            toolbar.inflateMenu(R.menu.message_menu);

            if (getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_PORTRAIT) {
                toolbar.setNavigationIcon(R.drawable.ic_back);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().beginTransaction().remove(MessagesFragment.this).commit();
                    }
                });
            } else {
                toolbar.setNavigationIcon(R.drawable.ic_close_white);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().beginTransaction().remove(MessagesFragment.this).commit();
                    }
                });
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        activity.unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.registerReceiver(receiver, filter);
    }

    private void animateLevel(final int level) {
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
                                selectPhoto();
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
        tempTakePhotoFile = holder.getNewTempPhotoFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempTakePhotoFile));
        startActivityForResult(intent, Const.REQUEST_CODE_TAKE_PHOTO);
        Log.e("LOG", "ACTIVITY " + activity);
        Log.e("LOG", "FILE" + tempTakePhotoFile);
    }

    private void selectPhoto() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), Const.REQUEST_CODE_SELECT_IMAGE);
        } else {
            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), Const.REQUEST_CODE_SELECT_IMAGE);
        }
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

    @Override
    public void onApiResult(BaseHandler output) {
        if (output.getHandlerId() == ChatHistoryHandler.HANDLER_ID) {
            TdApi.Messages messages = (TdApi.Messages) output.getResponse();
            Log.e("Log", "ChatId " + getShownChatId());

            if (chat.id == messages.messages[0].chatId) {
                setChatHistory(messages);
            }
        }
        if (output.getHandlerId() == MessageHandler.HANDLER_ID) {
            Log.e("Log", "Result message " + output.getResponse());
        }
    }
}


