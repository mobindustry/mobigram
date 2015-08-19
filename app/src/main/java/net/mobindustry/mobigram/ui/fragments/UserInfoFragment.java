package net.mobindustry.mobigram.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import net.mobindustry.mobigram.R;
import net.mobindustry.mobigram.core.ApiClient;
import net.mobindustry.mobigram.core.ApiHelper;
import net.mobindustry.mobigram.core.handlers.BaseHandler;
import net.mobindustry.mobigram.core.handlers.GroupChatFullHandler;
import net.mobindustry.mobigram.core.handlers.UserFullHandler;
import net.mobindustry.mobigram.model.holder.UserInfoHolder;
import net.mobindustry.mobigram.ui.activity.TransparentActivity;
import net.mobindustry.mobigram.utils.Const;
import net.mobindustry.mobigram.utils.Utils;

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

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.userInfoToolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.background_activity));
        if (type.equals("private")) {
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
//                        case R.id.block_user:
//                            Log.e("Log", "Block user");
//                            break;
//                        case R.id.delete_user:
//                            Log.e("Log", "Delete user");
//                            break;
                    }
                    return false;
                }
            });
            writeMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MessagesFragment.class);
                    intent.putExtra("id", (long) user.id);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            });
            getUserFull();
        } else {
            writeMessage.setVisibility(View.GONE);
            getChatFull();
        }
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
                    ((TransparentActivity)getActivity()).progressBarGone();
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
                    ((TransparentActivity)getActivity()).progressBarGone();
                }
            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    private void setGroupChatFullInfo(TdApi.GroupChatFull groupChatFull) {
        long chatId = groupChatFull.groupChat.id;

        name.setText(groupChatFull.groupChat.title);

        lastSeenText.setText(groupChatFull.groupChat.participantsCount + getActivity().getString(R.string.members));
        TdApi.File file = groupChatFull.groupChat.photoBig;

        Utils.setIcon(file, (int) chatId, groupChatFull.groupChat.title, "", imageIcon, icon, getActivity());

        List<TdApi.User> list = new ArrayList<>();
        for (int i = 0; i < groupChatFull.participants.length; i++) {
            list.add(groupChatFull.participants[i].user);
        }
        for (int i = 0; i < list.size(); i++) {
            final TdApi.User user = list.get(i);
            View itemView = View.inflate(getActivity(), R.layout.user_info_card, null);
            ImageView imageIcon = (ImageView) itemView.findViewById(R.id.user_info_image_icon);
            TextView icon = (TextView) itemView.findViewById(R.id.user_info_icon);
            TextView name = (TextView) itemView.findViewById(R.id.user_info_text_name);
            TextView lastSeen = (TextView) itemView.findViewById(R.id.text_user_info_last_seen);

            TdApi.File fileImage = user.photoBig;
            Utils.setIcon(fileImage, user.id, user.firstName, user.lastName, imageIcon, icon, getActivity());
            name.setText(user.firstName + " " + user.lastName);
            TdApi.UserStatus status = user.status;
            lastSeen.setText(Utils.getUserStatusString(status));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user.id != UserInfoHolder.getUser().id) {
                        UserInfoFragment userInfoFragment = new UserInfoFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.transparent_content, userInfoFragment);
                        userInfoFragment.setInfo((long) user.id, getActivity().getString(R.string.private_chat));
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            });
            content.addView(itemView);
        }
    }

    private void setUserFullInfo(TdApi.UserFull userFullInfo) {
        user = userFullInfo.user;
        TdApi.UserStatus status = user.status;
        TdApi.File file = user.photoBig;

        name.setText(userFullInfo.realFirstName + " " + userFullInfo.realLastName);
        lastSeenText.setText(Utils.getUserStatusString(status));

        Utils.setIcon(file, user.id, user.firstName, user.lastName, imageIcon, icon, getActivity());

        View userPhoneNumberView = View.inflate(getActivity(), R.layout.user_phone_layout, null);
        ((TextView) userPhoneNumberView.findViewById(R.id.user_phone_number)).setText(user.phoneNumber);
        content.addView(userPhoneNumberView);
        if (!user.username.equals("")) {
            View userNickname = View.inflate(getActivity(), R.layout.user_nickname_layout, null);
            ((TextView) userNickname.findViewById(R.id.user_nickname)).setText("@" + user.username);
            content.addView(userNickname);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.REQUEST_CODE_NEW_MESSAGE && resultCode == Activity.RESULT_OK) {
            long id = data.getLongExtra("id", 0);
            ApiHelper.sendContactMessage(id, user.phoneNumber, user.firstName, user.lastName);
            getActivity().finish();
        }
    }
}
