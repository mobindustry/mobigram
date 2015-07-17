package net.mobindustry.telegram.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.DownloadFileHandler;
import net.mobindustry.telegram.core.handlers.GroupChatFullHandler;
import net.mobindustry.telegram.core.handlers.OkHandler;
import net.mobindustry.telegram.core.handlers.UserFullHandler;
import net.mobindustry.telegram.ui.activity.TransparentActivity;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.ImageLoaderHelper;
import net.mobindustry.telegram.utils.Utils;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.List;

public class UserInfoFragment extends Fragment {

    private long id;
    private String type;
    private TextView icon;
    private TextView name;
    private TextView lastSeenText;
    private RoundedImageView imageIcon;
    private LinearLayout content;
    private ImageView writeMessage;
    private TdApi.User user;
    private TdApi.GroupChatFull groupChatFull;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_info_fragment, container, false);
        icon = (TextView) view.findViewById(R.id.toolbar_user_info_icon);
        name = (TextView) view.findViewById(R.id.toolbar_user_info_text_name);
        lastSeenText = (TextView) view.findViewById(R.id.toolbar_text_user_info_last_seen);
        imageIcon = (RoundedImageView) view.findViewById(R.id.toolbar_user_info_image_icon);
        content = (LinearLayout) view.findViewById(R.id.user_info_content);
        writeMessage = (ImageView) view.findViewById(R.id.write_message_button);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (type.equals("private")) {
            getUserFull();
        } else {
            getChatFull();
        }

        writeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.userInfoToolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.background_activity));
        toolbar.inflateMenu(R.menu.user_info);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share_user:
                        Intent intent = new Intent(getActivity(), TransparentActivity.class);
                        intent.putExtra("choice", Const.CONTACT_LIST_FRAGMENT);
                        intent.putExtra("destination", "userInfo");
                        startActivityForResult(intent, Const.REQUEST_CODE_NEW_MESSAGE);
                        break;
                    case R.id.block_user:
                        Log.e("Log", "Block user");
                        break;
                    case R.id.delete_user:
                        Log.e("Log", "Delete user");
                        break;
                }
                return false;
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    public void setInfo(long id, String type) {
        this.id = id;
        this.type = type;
    }

    private void getUserFull() {
        new ApiClient<>(new TdApi.GetUserFull((int) id), new UserFullHandler(), new ApiClient.OnApiResultHandler() {
            @Override
            public void onApiResult(BaseHandler output) {
                if (output.getHandlerId() == UserFullHandler.HANDLER_ID) {
                    TdApi.UserFull userFull = (TdApi.UserFull) output.getResponse();
                    setUserFullInfo(userFull);
                }
            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    private void getChatFull() {
        new ApiClient<>(new TdApi.GetGroupChatFull((int) id), new GroupChatFullHandler(), new ApiClient.OnApiResultHandler() {
            @Override
            public void onApiResult(BaseHandler output) {
                if (output.getHandlerId() == GroupChatFullHandler.HANDLER_ID) {
                    TdApi.GroupChatFull groupChatFull = (TdApi.GroupChatFull) output.getResponse();
                    setGroupChatFullInfo(groupChatFull);
                }
            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    private void setGroupChatFullInfo(TdApi.GroupChatFull groupChatFull) {
        Log.e("Log", groupChatFull.toString());
        TextView text = new TextView(getActivity());
        text.setText("Displays this information is coming soon");
        content.addView(text);
        long chatId = groupChatFull.groupChat.id;

        name.setText(groupChatFull.groupChat.title);

        lastSeenText.setText(groupChatFull.groupChat.participantsCount + " members");
        TdApi.File file = groupChatFull.groupChat.photoBig;

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
                    icon.setText(Utils.getInitials(groupChatFull.groupChat.title, ""));
                }
            }
            if (file.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
                imageIcon.setVisibility(View.VISIBLE);
                TdApi.FileLocal fileLocal = (TdApi.FileLocal) file;
                ImageLoaderHelper.displayImageList(Const.IMAGE_LOADER_PATH_PREFIX + fileLocal.path, imageIcon);
            }
        }

        List<TdApi.User> list = new ArrayList<>();
        for (int i = 0; i < groupChatFull.participants.length; i++) {
            list.add(groupChatFull.participants[i].user);
        }

        Log.e("Log", list.toString());
    }

    private void setUserFullInfo(TdApi.UserFull userFullInfo) {
        user = userFullInfo.user;
        TdApi.UserStatus status = user.status;
        TdApi.File file = user.photoBig;

        name.setText(userFullInfo.realFirstName + " " + userFullInfo.realLastName);
        lastSeenText.setText(Utils.getUserStatusString(status));

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
                        icon.setBackgroundDrawable(Utils.getShapeDrawable(R.dimen.toolbar_icon_size, -user.id));
                    } else {
                        icon.setBackground(Utils.getShapeDrawable(R.dimen.toolbar_icon_size, -user.id));
                    }
                    icon.setText(Utils.getInitials(userFullInfo.realFirstName, userFullInfo.realLastName));
                }
            }
            if (file.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
                imageIcon.setVisibility(View.VISIBLE);
                TdApi.FileLocal fileLocal = (TdApi.FileLocal) file;
                ImageLoaderHelper.displayImageList(Const.IMAGE_LOADER_PATH_PREFIX + fileLocal.path, imageIcon);
            }
        }

        View userPhoneNumberView = View.inflate(getActivity(), R.layout.user_phone_layout, null);
        ((TextView)userPhoneNumberView.findViewById(R.id.user_phone_number)).setText(user.phoneNumber);
        content.addView(userPhoneNumberView);
        if(!user.username.equals("")) {
            View userNickname = View.inflate(getActivity(), R.layout.user_nickname_layout, null);
            ((TextView)userNickname.findViewById(R.id.user_nickname)).setText("@" + user.username);
            content.addView(userNickname);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.REQUEST_CODE_NEW_MESSAGE && resultCode == Activity.RESULT_OK) {
            long id = data.getLongExtra("id", 0);

            new ApiClient<>(new TdApi.SendMessage(id, new TdApi.InputMessageContact(user.phoneNumber, user.firstName, user.lastName)),
                    new OkHandler(), new ApiClient.OnApiResultHandler() {
                @Override
                public void onApiResult(BaseHandler output) {
                    getActivity().finish();
                }
            }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    }
}
