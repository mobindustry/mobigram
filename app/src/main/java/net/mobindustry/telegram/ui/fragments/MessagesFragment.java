package net.mobindustry.telegram.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuPopupHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.ui.adapters.AttachAdapter;
import net.mobindustry.telegram.ui.activity.ChatActivity;
import net.mobindustry.telegram.ui.adapters.MessageAdapter;
import net.mobindustry.telegram.model.Contact;
import net.mobindustry.telegram.model.NeTelegramMessage;
import net.mobindustry.telegram.utils.Utils;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.Date;
import java.util.Arrays;
import java.util.List;

public class MessagesFragment extends Fragment implements Serializable {

    private MessageAdapter adapter;
    private static List<TdApi.Message> messageList = new ArrayList<>();

    private String initials;
    private String firstLastName;
    private String lastSeen;
    private int color;
    private ImageView attach;
    private ImageView smiles;
    private File tempTakePhotoFile;
    public static final int REQUEST_CODE_TAKE_PHOTO = 101;
    private TextView icon;
    private TextView name;
    private TextView lastSeenText;

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

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    public void setChatHistory(TdApi.Messages messages) {

        adapter.addAll(Utils.reverseMessages(messages.messages));
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
        adapter.addAll(messageList);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ChatActivity activity = (ChatActivity) getActivity();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.messageFragmentToolbar);
        if (toolbar != null) {

            attach = (ImageView) getActivity().findViewById(R.id.attach);
            smiles = (ImageView) getActivity().findViewById(R.id.smiles);
            TextView icon = (TextView) getActivity().findViewById(R.id.toolbar_text_icon);
            TextView name = (TextView) getActivity().findViewById(R.id.toolbar_text_name);
            TextView lastSeenText = (TextView) getActivity().findViewById(R.id.toolbar_text_last_seen);
            icon = (TextView) getActivity().findViewById(R.id.toolbar_text_icon);
            name = (TextView) getActivity().findViewById(R.id.toolbar_text_name);
            lastSeenText = (TextView) getActivity().findViewById(R.id.toolbar_text_last_seen);

            attach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Handler().postDelayed(new Runnable() {

                        public void run() {
                            showPopupMenu(attach);
                        }

                    }, 100L);
                }
            });


            name.setText(firstLastName);
            lastSeenText.setText(lastSeen);
            icon.setText(initials);
            ChatListFragment fragment = (ChatListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.titles);
            chat = fragment.getChat();

            TdApi.PrivateChatInfo privateChatInfo = (TdApi.PrivateChatInfo) chat.type; //TODO verify;
            TdApi.User chatUser = privateChatInfo.user;
//            TdApi.UserStatusOffline status = (TdApi.UserStatusOffline) chatUser.status;

            name.setText(chatUser.firstName + " " + chatUser.lastName);
            lastSeenText.setText("lastSeen"); //TODO
            icon.setText(Utils.getInitials(chatUser.firstName, chatUser.lastName));
            icon.setBackground(getBackground());
            activity.getChatHistory(chat.id, chat.topMessage.id, -1, 30);

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

    public ShapeDrawable getBackground() {
        ShapeDrawable circle = new ShapeDrawable(new OvalShape());
        circle.setIntrinsicHeight(60);
        circle.setIntrinsicWidth(60);
        circle.getPaint().setColor(Color.rgb(100, 100, 100));

        return circle;
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
                            case R.id.gallery:
                                Toast.makeText(getActivity(),
                                        "gallery", Toast.LENGTH_LONG).show();
                            case R.id.video:
                                Toast.makeText(getActivity(),
                                        "video", Toast.LENGTH_LONG).show();
                            case R.id.file:
                                Toast.makeText(getActivity(),
                                        "file", Toast.LENGTH_LONG).show();
                            case R.id.location:
                                Toast.makeText(getActivity(),
                                        "location", Toast.LENGTH_LONG).show();
                        }

                        return true;
                    }
                });

        popupMenu.show();

    }

    private void makePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        tempTakePhotoFile = getOutputMediaFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempTakePhotoFile));
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    public static File getExternalStoragePublicPictureDir() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return path;
    }

    public static File getOutputMediaFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "IMG_" + dateFormat.format(new Date()) + ".jpg";
        return new File(getExternalStoragePublicPictureDir(), fileName);
    }


}
