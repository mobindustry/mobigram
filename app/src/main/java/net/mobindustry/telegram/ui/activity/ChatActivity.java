package net.mobindustry.telegram.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.DownloadFileHandler;
import net.mobindustry.telegram.core.handlers.MessageHandler;
import net.mobindustry.telegram.core.handlers.StickerHandler;
import net.mobindustry.telegram.core.handlers.StickersHandler;
import net.mobindustry.telegram.core.handlers.UserHandler;
import net.mobindustry.telegram.model.Enums;
import net.mobindustry.telegram.model.NavigationItem;
import net.mobindustry.telegram.model.holder.DataHolder;
import net.mobindustry.telegram.model.holder.MessagesFragmentHolder;
import net.mobindustry.telegram.model.holder.UserInfoHolder;
import net.mobindustry.telegram.ui.adapters.NavigationDrawerAdapter;
import net.mobindustry.telegram.ui.fragments.ChatListFragment;
import net.mobindustry.telegram.ui.fragments.MessagesFragment;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.ImageLoaderHelper;
import net.mobindustry.telegram.utils.Utils;

import org.drinkless.td.libcore.telegram.TdApi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements ApiClient.OnApiResultHandler {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private BroadcastReceiver receiver;
    private MenuItem itemToCollapse;

    private IntentFilter filter = new IntentFilter();

    private final int NEW_MESSAGE_LOAD_LIMIT = 1;
    private final int NEW_MESSAGE_LOAD_OFFSET = -1;

    private FragmentManager fragmentManager;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;

    private NavigationDrawerAdapter adapter;

    private UserInfoHolder holder = UserInfoHolder.getInstance();
    private UserInfoHolder userInfoHolder;
    private ChatListFragment chatListFragment;

    public void logOut() {
        Utils.deleteRecursive(new File(Const.PATH_TO_NETELEGRAM));
        Toast.makeText(ChatActivity.this, R.string.logout_navigation_item, Toast.LENGTH_LONG).show();
        DataHolder.setIsLoggedIn(false);
        finish();
        new ApiClient<>(new TdApi.AuthReset(), new StickerHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public long getMyId() {
        return holder.getUser().id;
    }

    public MessagesFragment getMessageFragment() {
        fragmentManager = getSupportFragmentManager();
        return (MessagesFragment) fragmentManager.findFragmentById(R.id.messages);
    }

    public ChatListFragment getChatListFragment() {
        fragmentManager = getSupportFragmentManager();
        return (ChatListFragment) fragmentManager.findFragmentById(R.id.chat_list);
    }

    public void getUserMe() {
        new ApiClient<>(new TdApi.GetMe(), new UserHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void getStickers() {
        new ApiClient<>(new TdApi.GetStickers(), new StickersHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public void onApiResult(BaseHandler output) {
        if (output == null) {
            getUserMe();
        } else {
            if (output.getHandlerId() == UserHandler.HANDLER_ID) {
                userInfoHolder.setUser((TdApi.User) output.getResponse());
                setHeader(userInfoHolder.getUser());
            }
        }

        if (output.getHandlerId() == StickersHandler.HANDLER_ID) {
            TdApi.Stickers stickers = (TdApi.Stickers) output.getResponse();
            if (stickers.stickers.length == 0) {
                getStickers();
            } else {
                MessagesFragmentHolder.setStickers((TdApi.Stickers) output.getResponse());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        DataHolder.setIsLoggedIn(true);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Const.NEW_MESSAGE_ACTION) || intent.getAction().equals(Const.NEW_MESSAGE_ACTION_ID)) {
                    MessagesFragment fragment = getMessageFragment();
                    if (fragment != null) {
                        int id = intent.getIntExtra("message_id", 0);
                        long chat_id = intent.getLongExtra("chatId", 0);
                        if (chat_id == fragment.getShownChatId()) {
                            fragment.getChatHistory(chat_id, id, NEW_MESSAGE_LOAD_OFFSET, NEW_MESSAGE_LOAD_LIMIT, Enums.MessageAddType.NEW);
                        }
                    }
                    getChatListFragment().getChatsList(Const.CHATS_LIST_OFFSET, Const.CHATS_LIST_LIMIT);
                }
                if (intent.getAction().equals(Const.READ_INBOX_ACTION)) {
                    long chatId = intent.getLongExtra("chat_id", 0);
                    int unread = intent.getIntExtra("unread_count", 0);
                    int lastRead = intent.getIntExtra("last_read", 0);
                    getChatListFragment().updateChat(chatId, unread, lastRead);
                }
            }
        };

        filter.addAction(Const.NEW_MESSAGE_ACTION);
        filter.addAction(Const.NEW_MESSAGE_ACTION_ID);
        filter.addAction(Const.READ_INBOX_ACTION);
        registerReceiver(receiver, filter);

        chatListFragment = new ChatListFragment();
        FragmentTransaction ft
                = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.chat_list, chatListFragment);
        ft.commit();

        adapter = new NavigationDrawerAdapter(ChatActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.chats_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        title = drawerTitle = getTitle();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        List<NavigationItem> drawerItemsList = new ArrayList<>();
        drawerItemsList.add(new NavigationItem(getString(R.string.logout_navigation_item), R.drawable.ic_logout));

        userInfoHolder = UserInfoHolder.getInstance();
        if (userInfoHolder.getUser() == null) {
            getUserMe();
        } else {
            setHeader(userInfoHolder.getUser());
        }

        getStickers();

        adapter.addAll(drawerItemsList);

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

    public long getIntentChatId() {
        return getIntent().getLongExtra("chatId", 0);
    }

    public void setHeader(TdApi.User userMe) {
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.navigation_drawer_header, drawerList, false);
        TextView firstLastNameView = (TextView) header.findViewById(R.id.drawer_header_first_last_name);
        TextView phoneView = (TextView) header.findViewById(R.id.drawer_header_phone);
        firstLastNameView.setText(userMe.firstName + " " + userMe.lastName);
        phoneView.setText(userMe.phoneNumber);

        TextView icon = (TextView) header.findViewById(R.id.header_text_icon);
        final RoundedImageView imageIcon = (RoundedImageView) header.findViewById(R.id.header_image_icon);

        if (userMe.photoBig instanceof TdApi.FileEmpty) {
            final TdApi.FileEmpty file = (TdApi.FileEmpty) userMe.photoBig;
            if (file.id != 0) {
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

                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    icon.setBackgroundDrawable(Utils.getShapeDrawable(R.dimen.header_icon_size, -userMe.id));
                } else {
                    icon.setBackground(Utils.getShapeDrawable(R.dimen.header_icon_size, -userMe.id));
                }
                icon.setText(Utils.getInitials(userMe.firstName, userMe.lastName));
            }
        }
        if (userMe.photoBig instanceof TdApi.FileLocal) {
            imageIcon.setVisibility(View.VISIBLE);
            TdApi.FileLocal file = (TdApi.FileLocal) userMe.photoBig;
            ImageLoaderHelper.displayImage(Const.IMAGE_LOADER_PATH_PREFIX + file.path, imageIcon);
        }

        drawerList.addHeaderView(header, null, false);
        drawerList.setAdapter(adapter);
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
                itemToCollapse = item;

                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
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

    public void clearSearch() {
        if (itemToCollapse != null) {
            chatListFragment.setAdapterFilter("");
            MenuItemCompat.collapseActionView(itemToCollapse);
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        if (getMessageFragment() != null) {
            if (getMessageFragment().isEmojiAttached()) {
                getMessageFragment().dissmissEmojiPopup();
                return;
            }
        }
        if (!(Utils.isTablet(this) && getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) && getMessageFragment() != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            LinearLayout layout = (LinearLayout) findViewById(R.id.fragment_layout);
            layout.setVisibility(View.VISIBLE);
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
            fragmentTransaction.remove(getMessageFragment()).commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataHolder.setActive(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataHolder.setActive(false);

    }
}
