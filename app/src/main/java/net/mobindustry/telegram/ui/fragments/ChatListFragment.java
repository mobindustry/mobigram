package net.mobindustry.telegram.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.ApiHelper;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.ChatHandler;
import net.mobindustry.telegram.core.handlers.ChatsHandler;
import net.mobindustry.telegram.core.handlers.OkHandler;
import net.mobindustry.telegram.model.holder.MessagesFragmentHolder;
import net.mobindustry.telegram.model.holder.UserInfoHolder;
import net.mobindustry.telegram.ui.activity.ChatActivity;
import net.mobindustry.telegram.ui.activity.TransparentActivity;
import net.mobindustry.telegram.ui.adapters.ChatListAdapter;
import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.List;

public class ChatListFragment extends ListFragment {

    boolean dualPane;
    private static int currentCheckPosition = 0;
    private ChatListAdapter adapter;
    private ProgressBar progressBar;
    private static TdApi.Chats chats;
    private LinearLayout layout;
    private ProgressDialog mProgressDialog;
    private boolean fragmentStopped = false;

    private TextView noChatsMessage;

    private long clickedId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new ChatListAdapter(getActivity());
        layout = (LinearLayout) getActivity().findViewById(R.id.fragment_layout);
        noChatsMessage = (TextView) getActivity().findViewById(R.id.no_chats);
        return inflater.inflate(R.layout.chat_list_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        noChatsMessage = (TextView) getActivity().findViewById(R.id.no_chats);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.findFragmentById(R.id.messages);
        if (manager.findFragmentById(R.id.messages) != null) {
            layout.setVisibility(View.INVISIBLE);
        } else {
            layout.setVisibility(View.VISIBLE);
        }

        progressBar = (ProgressBar) getActivity().findViewById(R.id.chat_list_progress_bar);

        getChatsList(Const.CHATS_LIST_OFFSET, Const.CHATS_LIST_LIMIT);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.attachToListView(getListView());
        fab.setColorPressedResId(R.color.background_floating_button_pressed);
        fab.setColorNormalResId(R.color.background_floating_button);
        fab.setShadow(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TransparentActivity.class);
                intent.putExtra("choice", Const.CONTACT_LIST_FRAGMENT);
                intent.putExtra("destination", getActivity().getString(R.string.chat_list));
                startActivityForResult(intent, Const.REQUEST_CODE_NEW_MESSAGE);
            }
        });
        setListAdapter(adapter);

        View detailsFrame = getActivity().findViewById(R.id.messages);
        dualPane = detailsFrame != null
                && detailsFrame.getVisibility() == View.VISIBLE;
        if (savedState != null) {
            currentCheckPosition = savedState.getInt("curChoice", 0);
        }
        if (dualPane) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }

        long id = ((ChatActivity) getActivity()).getIntentChatId();
        if (id != 0) {
            clickedId = id;
            int position = getChatPosition(id);
            showMessages(position);
        }
    }

    public void setChatsList(final TdApi.Chats chats1) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        chats = chats1;
        adapter.clear();
        adapter.addAll(chats.chats);
    }

    public void getChatsList(int offset, int limit) {
        new ApiClient<>(new TdApi.GetChats(offset, limit), new ChatsHandler(), new ApiClient.OnApiResultHandler() {
            @Override
            public void onApiResult(BaseHandler output) {
                if (output == null) {
                    getChatsList(0, 200);
                } else {
                    if (output.getHandlerId() == ChatsHandler.HANDLER_ID) {
                        TdApi.Chats receivedChats = (TdApi.Chats) output.getResponse();
                        if (receivedChats.chats.length == 0) {
                            noChatsMessage.setVisibility(View.VISIBLE);
                        } else {
                            noChatsMessage.setVisibility(View.GONE);
                            setChatsList(receivedChats);
                            UserInfoHolder.addUsersToMap(receivedChats);
                            MessagesFragmentHolder.setChats(receivedChats);
                        }
                    }
                }
            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public TdApi.Chat getChat(long id) {
        if (chats == null || chats.chats.length == 0) {
            chats = MessagesFragmentHolder.getChats();
        }
        if (id != 0) {
            clickedId = id;
            getListView().setSelection(getChatPosition(clickedId));
        }
        for (int i = 0; i < chats.chats.length; i++) {
            if (chats.chats[i].id == clickedId) {
                return chats.chats[i];
            }
        }
        if (chats.chats.length == 1) {
            return chats.chats[0];
        } else {
            return chats.chats[currentCheckPosition];
        }
    }

    public int getChatPosition(long id) {
        if (chats == null) {
            chats = MessagesFragmentHolder.getChats();
        }
        for (int i = 0; i < chats.chats.length; i++) {
            if (chats.chats[i].id == id) {
                return i;
            }
        }
        return Const.CHAT_NOT_FOUND;
    }

    public void setAdapterFilter(String filter) {
        if (chats != null) {
            if (filter.isEmpty()) {
                adapter.clear();
                adapter.addAll(chats.chats);
            } else {
                List<TdApi.Chat> list = new ArrayList<>();
                for (int i = 0; i < chats.chats.length; i++) {
                    String name;
                    String messageText = "";
                    TdApi.ChatInfo info = chats.chats[i].type;
                    if (info.getConstructor() == TdApi.PrivateChatInfo.CONSTRUCTOR) {
                        TdApi.PrivateChatInfo privateInfo = (TdApi.PrivateChatInfo) info;
                        name = privateInfo.user.firstName + " " + privateInfo.user.lastName;
                    } else {
                        TdApi.GroupChatInfo groupInfo = (TdApi.GroupChatInfo) info;
                        name = groupInfo.groupChat.title;
                    }
                    TdApi.MessageContent message = chats.chats[i].topMessage.message;
                    if (message.getConstructor() == TdApi.MessageText.CONSTRUCTOR) {
                        TdApi.MessageText textMessage = (TdApi.MessageText) message;
                        messageText = textMessage.text;
                    }
                    if (name.toLowerCase().contains(filter.toLowerCase()) || messageText.toLowerCase().contains(filter.toLowerCase())) {
                        list.add(chats.chats[i]);
                    }
                    adapter.clear();
                    adapter.addAll(list);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", currentCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        TdApi.Chat selectedItem = adapter.getItem(pos);
        clickedId = selectedItem.id;
        showMessages(pos);
    }

    void showMessages(int index) {
        currentCheckPosition = index;
        if (getFragmentManager() != null) {
            FragmentTransaction ft
                    = getFragmentManager().beginTransaction();
            getListView().setItemChecked(index, true);
            MessagesFragment messagesFragment = new MessagesFragment();
            if (!fragmentStopped) {
                ft.replace(R.id.messages, messagesFragment);
                ft.commit();
            }
            layout.setVisibility(View.INVISIBLE);
        }
    }

    public void openChat(long resultId) {
        clickedId = resultId;
        int position = getChatPosition(resultId);
        if (position == Const.CHAT_NOT_FOUND) {
            newPrivateChat(resultId);
        } else {
            showMessages(position);
        }
    }

    private void newPrivateChat(final long userId) {
        getActivity().setRequestedOrientation(getResources().getConfiguration().orientation);
        mProgressDialog = ProgressDialog.show(getActivity(), getActivity().getString(R.string.loading),
                getActivity().getString(R.string.please_wait), true, false);
        ApiHelper.createPrivateChat(userId);
        new ApiClient<>(new TdApi.GetChat(userId), new ChatHandler(), new ApiClient.OnApiResultHandler() {
            @Override
            public void onApiResult(BaseHandler output) {
                if (output.getHandlerId() == ChatHandler.HANDLER_ID) {
                    TdApi.Chat chat = (TdApi.Chat) output.getResponse();
                    clickedId = chat.id;
                    addChatToChatsArray(chat);
                    adapter.clear();
                    adapter.addAll(chats.chats);
                    showMessages(adapter.getPosition(chat));
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        try {
                            mProgressDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                }
            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    private void addChatToChatsArray(TdApi.Chat chat) {
        TdApi.Chat[] newChatArray = new TdApi.Chat[chats.chats.length + 1];
        newChatArray[0] = chat;
        System.arraycopy(chats.chats, 0, newChatArray, 1, chats.chats.length);
        chats = new TdApi.Chats(newChatArray);
    }

    public void updateChat(long chatId, int unread, int lastRead) {
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            TdApi.Chat chat = adapter.getItem(i);
            if (chat.id == chatId) {
                adapter.getItem(i).unreadCount = unread;
                adapter.getItem(i).lastReadInboxMessageId = lastRead;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_CODE_NEW_MESSAGE && resultCode == Activity.RESULT_OK) {
            long resultId = data.getLongExtra("id", 0);
            openChat(resultId);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        fragmentStopped = true;
    }

    @Override
    public void onResume() {
        fragmentStopped = false;
        if (adapter.getCount() == 0) {
            getChatsList(Const.CHATS_LIST_OFFSET, Const.CHATS_LIST_LIMIT);
        }
        super.onResume();
    }
}
