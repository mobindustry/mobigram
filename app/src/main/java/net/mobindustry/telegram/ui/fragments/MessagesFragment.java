package net.mobindustry.telegram.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.LevelListDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.soundcloud.android.crop.Crop;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.activity.ChatActivity;
import net.mobindustry.telegram.ui.adapters.MessageAdapter;
import net.mobindustry.telegram.ui.adapters.TextWatcherAdapter;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.Utils;

import org.drinkless.td.libcore.telegram.TdApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessagesFragment extends Fragment implements Serializable {

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

    private TdApi.User user;
    private TdApi.Chat chat;
    private ChatActivity activity;

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
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                adapter.clear();
                adapter.addAll(Utils.reverseMessages(messages.messages));
            }
        });
    }

    public void setUser(TdApi.User user) {
        this.user = user;
        Log.e("Log", "User " + user.toString());
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

        activity = (ChatActivity) getActivity();

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
                    if (text.length() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                showPopupMenu(attach);
                            }
                        }, 100L);
                    } else {
                        activity.sendMessage(chat.id, input.getText().toString());
                        input.setText("");
                    }
                }
            });

            ChatListFragment fragment = (ChatListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.titles);
            chat = fragment.getChat();

            TdApi.PrivateChatInfo privateChatInfo = (TdApi.PrivateChatInfo) chat.type; //TODO verify;
            TdApi.User chatUser = privateChatInfo.user;

            name.setText(chatUser.firstName + " " + chatUser.lastName);
            lastSeenText.setText("lastSeen"); //TODO
            icon.setText(Utils.getInitials(chatUser.firstName, chatUser.lastName));
            icon.setBackground(Utils.getShapeDrawable(60, -chatUser.id));
            activity.getChatHistory(chat.id, chat.topMessage.id, -1, 200);

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
                                Toast.makeText(getActivity(),
                                        "Take photo", Toast.LENGTH_LONG).show();
                                makePhoto();
                                break;
                            case R.id.gallery:
                                Toast.makeText(getActivity(),
                                        "gallery", Toast.LENGTH_LONG).show();
                                selectPhoto();
                                break;
                            case R.id.video:
                                Toast.makeText(getActivity(),
                                        "video", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.file:
                                Toast.makeText(getActivity(),
                                        "file", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.location:
                                Toast.makeText(getActivity(),
                                        "location", Toast.LENGTH_LONG).show();
                                break;
                        }
                        return true;
                    }
                });

        popupMenu.show();

    }

    private void makePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        tempTakePhotoFile = getOutputMediaFile();
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

    public File getExternalStoragePublicPictureDir() {
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        if (isSDPresent) {
            String dir = Environment.getExternalStorageDirectory() + File.separator + "NeTelegram";
            File path = new File(dir);
            path.mkdirs();
            return path;
        } else {
            File path = Environment.getExternalStoragePublicDirectory("NeTelegram");
            return path;
        }
    }


    public File getOutputMediaFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "IMG_" + dateFormat.format(new Date()) + ".jpg";
        return new File(getExternalStoragePublicPictureDir(), fileName);
    }

    public static String getPathFromURI(Uri contentUri, Activity activity) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_CODE_TAKE_PHOTO) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(tempTakePhotoFile);
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);
            Uri external = Uri.fromFile(tempTakePhotoFile);
            Crop.of(external, external).asSquare().start(getActivity());
            Crop.pickImage(getActivity());
        }

        if (requestCode == Const.REQUEST_CODE_SELECT_IMAGE) {
            try {
                Uri uriImage = data.getData();
                String path = getPathFromURI(uriImage, getActivity());
                if (!TextUtils.isEmpty(path)) {
                    //photoSelected(path);
                } else {
                    Toast.makeText(getActivity(), "File not found", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "File not found", Toast.LENGTH_LONG).show();
            }
        }
    }
}


