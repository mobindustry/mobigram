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
import net.mobindustry.telegram.core.handlers.ContactsHandler;
import net.mobindustry.telegram.core.handlers.DownloadFileHandler;
import net.mobindustry.telegram.core.handlers.LogHandler;
import net.mobindustry.telegram.core.handlers.StickersHandler;
import net.mobindustry.telegram.model.NavigationItem;
import net.mobindustry.telegram.model.holder.ContactListHolder;
import net.mobindustry.telegram.model.holder.UserMeHolder;
import net.mobindustry.telegram.ui.adapters.NavigationDrawerAdapter;
import net.mobindustry.telegram.ui.fragments.ChatListFragment;
import net.mobindustry.telegram.ui.fragments.MessagesFragment;
import net.mobindustry.telegram.utils.Const;

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

    private NavigationDrawerAdapter adapter;

    private UserMeHolder holder = UserMeHolder.getInstance();

    public void getContacts() {
        new ApiClient<>(new TdApi.GetContacts(), new ContactsHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void getStickers() {
        new ApiClient<>(new TdApi.GetStickers(), new StickersHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void downloadFile(int fileId) {
        new ApiClient<>(new TdApi.DownloadFile(fileId), new DownloadFileHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void logOut() {
        Toast.makeText(ChatActivity.this, R.string.logout_navigation_item, Toast.LENGTH_LONG).show();
        new ApiClient<>(new TdApi.AuthReset(), new LogHandler(), this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        finish(); //TODO crash???
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
        if (output.getHandlerId() == ContactsHandler.HANDLER_ID) {
            ContactListHolder contactListHolder = ContactListHolder.getInstance();
            contactListHolder.setContacts((TdApi.Contacts) output.getResponse());
        }
        if (output.getHandlerId() == StickersHandler.HANDLER_ID) {
            TdApi.Stickers stickers = (TdApi.Stickers) output.getResponse();
            for (int i = 0; i < stickers.stickers.length; i++) {
                if (stickers.stickers[i].sticker instanceof TdApi.FileEmpty) {
                    TdApi.FileEmpty file = (TdApi.FileEmpty) stickers.stickers[i].sticker;
                    downloadFile(file.id);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
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
        //TODO if result != ok
        if (requestCode == Const.CROP_REQUEST_CODE) {
            getMessageFragment().sendPhotoMessage(getMessageFragment().getShownChatId(), getMessageFragment().getPhotoPath());
        }
    }
}