package net.mobindustry.telegram.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.ChatHistoryHandler;
import net.mobindustry.telegram.core.handlers.ContactsHandler;
import net.mobindustry.telegram.core.handlers.DownloadFileHandler;
import net.mobindustry.telegram.core.handlers.LogHandler;
import net.mobindustry.telegram.core.handlers.MessageHandler;
import net.mobindustry.telegram.core.handlers.StickersHandler;
import net.mobindustry.telegram.model.NavigationItem;
import net.mobindustry.telegram.model.holder.PhotoDownloadHolder;
import net.mobindustry.telegram.ui.adapters.NavigationDrawerAdapter;
import net.mobindustry.telegram.ui.fragments.ChatListFragment;
import net.mobindustry.telegram.ui.fragments.MessagesFragment;
import net.mobindustry.telegram.model.holder.ContactListHolder;
import net.mobindustry.telegram.model.holder.UserMeHolder;
import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements ApiClient.OnApiResultHandler {

    private DrawerLayout drawerLayout;
    private ListView drawerList;

    private FragmentManager fm;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;

    private Client client;
    private NavigationDrawerAdapter adapter;

    private UserMeHolder holder = UserMeHolder.getInstance();

    public void getContacts() {
        new ApiClient<>(new TdApi.GetContacts(), new ContactsHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void sendTextMessage(long chatId, String message) {
        new ApiClient<>(new TdApi.SendMessage(chatId, new TdApi.InputMessageText(message)), new MessageHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void sendPhotoMessage(long chatId, String path) {
        new ApiClient<>(new TdApi.SendMessage(chatId, new TdApi.InputMessagePhoto(path)), new MessageHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void getStickers() {
        new ApiClient<>(new TdApi.GetStickers(), new StickersHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void getChatHistory(final long id, final int messageId, final int offset, final int limit) {
        new ApiClient<>(new TdApi.GetChatHistory(id, messageId, offset, limit), new ChatHistoryHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void downloadFile(int fileId) {
        new ApiClient<>(new TdApi.DownloadFile(fileId), new DownloadFileHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void logOut() {
        Toast.makeText(ChatActivity.this, R.string.logout_navigation_item, Toast.LENGTH_LONG).show();
        new ApiClient<>(new TdApi.AuthReset(), new LogHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        finish(); //TODO crash???
    }

    commit -am"50% rebase client to ApiClient, add country get from gps, many modifications"


    public void getUser(long id) {
        client.send(new TdApi.GetUser((int) id), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                //System.out.println("User object " + object.toString());
                if (object instanceof TdApi.User) {
                    getMessageFragment().setUser((TdApi.User) object);
                }
            }
        });
    }

    public void downloadFile(final int fileId, final int messageId) {
        client.send(new TdApi.DownloadFile(fileId), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                if (object instanceof TdApi.Ok) {
                    client.send(new TdApi.GetChatHistory(getMessageFragment().getShownChatId(), messageId, -1, 1), new Client.ResultHandler() {
                        @Override
                        public void onResult(TdApi.TLObject object) {
                            if (object instanceof TdApi.Messages) {
                                PhotoDownloadHolder holder = PhotoDownloadHolder.getInstance();
                                TdApi.Messages message = (TdApi.Messages) object;
                                TdApi.MessagePhoto photo = (TdApi.MessagePhoto) message.messages[0].message;
                                synchronized (holder.getSync()) {
                                    holder.setPhoto(photo.photo);
                                    holder.getSync().notify();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    public long getMyId() {
        return holder.getUserMe().id;
    }

    public MessagesFragment getMessageFragment() {
        fm = getSupportFragmentManager();
        return (MessagesFragment) fm.findFragmentById(R.id.messages);
    }

    @Override
    public void onApiResult(BaseHandler output) {
        if (output.GetHandlerId() == ContactsHandler.HANDLER_ID) {
            ContactListHolder contactListHolder = ContactListHolder.getInstance();
            contactListHolder.setContacts((TdApi.Contacts) output.getResponse());
        }
        if (output.GetHandlerId() == StickersHandler.HANDLER_ID) {
            TdApi.Stickers stickers = (TdApi.Stickers) output.getResponse();
            if (stickers != null) {
                for (int i = 0; i < stickers.stickers.length; i++) {
                    if (stickers.stickers[i].sticker instanceof TdApi.FileEmpty) {
                        TdApi.FileEmpty file = (TdApi.FileEmpty) stickers.stickers[i].sticker;
                        downloadFile(file.id);
                    }
                }
            } else {
                getStickers();
            }
        }
        if (output.GetHandlerId() == ChatHistoryHandler.HANDLER_ID) {
            TdApi.Messages messages = (TdApi.Messages) output.getResponse();
            if (getMessageFragment().getShownChatId() == messages.messages[0].chatId) {
                getMessageFragment().setChatHistory(messages);
            }
        }
        if (output.GetHandlerId() == MessageHandler.HANDLER_ID) {
            Log.e("Log", "Result photo message " + output.getResponse());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        PhotoDownloadHolder.getInstance().setActivity(this);

        client = TG.getClientInstance();

        Client.ResultHandler resultHandler = new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                //Log.i("LOG", "Updates result : " + object);
                if (object instanceof TdApi.UpdateMessageId) {
                    TdApi.UpdateMessageId updateMessageId = (TdApi.UpdateMessageId) object;
                    fm = getSupportFragmentManager();
                    ChatListFragment chatListFragment = (ChatListFragment) fm.findFragmentById(R.id.titles);
                    chatListFragment.getChatsList(0, 200);
                    getChatHistory(updateMessageId.chatId, updateMessageId.newId, -1, 200);
                }

                if (object instanceof TdApi.UpdateNewMessage) {
                    TdApi.UpdateNewMessage updateMessageId = (TdApi.UpdateNewMessage) object;
                    fm = getSupportFragmentManager();
                    ChatListFragment chatListFragment = (ChatListFragment) fm.findFragmentById(R.id.titles);
                    chatListFragment.getChatsList(0, 200);
                    getChatHistory(updateMessageId.message.chatId, updateMessageId.message.id, -1, 200);
                }

                //if (object instanceof TdApi.UpdateChatReadInbox ||
                //        object instanceof TdApi.UpdateChatReadOutbox) {
                //   getChats(0, 200);
                //}
            }
        };

        adapter = new NavigationDrawerAdapter(ChatActivity.this);

        getContacts();
        getStickers();

        Toolbar toolbar = (Toolbar) findViewById(R.id.contacts_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        title = drawerTitle = getTitle();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        List<NavigationItem> drawerItemsList = new ArrayList<>();
        drawerItemsList.add(new NavigationItem(getString(R.string.logout_navigation_item), R.drawable.ic_logout));

        setHeader(holder.getUserMe());

        adapter.addAll(drawerItemsList);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(title);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                supportInvalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    public void setHeader(TdApi.User userMe) {
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.navigation_drawer_header, drawerList, false);
        TextView firstLastNameView = (TextView) header.findViewById(R.id.drawer_header_first_last_name);
        TextView phoneView = (TextView) header.findViewById(R.id.drawer_header_phone);
        firstLastNameView.setText(userMe.firstName + " " + userMe.lastName);
        phoneView.setText(userMe.phoneNumber);

        drawerList.addHeaderView(header, null, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_search:
                final SearchView sv = new SearchView(getSupportActionBar().getThemedContext());
                MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
                MenuItemCompat.setActionView(item, sv);

                fm = getSupportFragmentManager();
                final ChatListFragment chatListFragment = (ChatListFragment) fm.findFragmentById(R.id.titles);

                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        System.out.println("search query submit " + query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (!newText.isEmpty()) {
                            chatListFragment.setAdapterFilter(newText);
                        } else {
                            chatListFragment.setAdapterFilter("");
                        }
                        return false;
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        switch (position) {
            case 1:
                logOut();
                break;
            default:
                break;
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        getSupportActionBar().setTitle(this.title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.CROP_REQUEST_CODE) {
            sendPhotoMessage(getMessageFragment().getShownChatId(), getMessageFragment().getPhotoPath());
        }
    }
}