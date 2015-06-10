package net.mobindustry.telegram.ui.activity;

import android.content.res.Configuration;
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
import net.mobindustry.telegram.ui.adapters.NavigationDrawerAdapter;
import net.mobindustry.telegram.model.NavigationItem;
import net.mobindustry.telegram.ui.fragments.ChatListFragment;
import net.mobindustry.telegram.ui.fragments.MessagesFragment;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements ClientReqest {

    private DrawerLayout drawerLayout;
    private ListView drawerList;

    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;

    private Client client;
    private Client.ResultHandler resultHandler;

    private TdApi.User userMe;
    private TdApi.Chats chats;
    private FragmentManager fm;

    @Override
    public void getMe() {
        client.send(new TdApi.GetMe(), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                if (object instanceof TdApi.User) {
                    userMe = (TdApi.User) object;

                    LayoutInflater inflater = getLayoutInflater();
                    ViewGroup header = (ViewGroup) inflater.inflate(R.layout.navigation_drawer_header, drawerList, false);
                    TextView firstLastNameView = (TextView) header.findViewById(R.id.drawer_header_first_last_name);
                    TextView phoneView = (TextView) header.findViewById(R.id.drawer_header_phone);
                    firstLastNameView.setText(userMe.firstName + " " + userMe.lastName);
                    phoneView.setText(userMe.phoneNumber);

                    drawerList.addHeaderView(header, null, false);
                }
            }
        });
    }

    @Override
    public void getUser(long id) {
        client.send(new TdApi.GetUser((int) id), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                System.out.println("User object " + object.toString());
                if (object instanceof TdApi.User) {
                    getMessageFragment().setUser((TdApi.User) object);
                }
            }
        });
    }

    @Override
    public void getChats(int offset, int limit) {
        client.send(new TdApi.GetChats(offset, limit), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                if (object instanceof TdApi.Chats) {
                    chats = (TdApi.Chats) object;
                    Log.i("LOG", "Get chats " + chats.toString());
                    fm = getSupportFragmentManager();
                    ChatListFragment chatListFragment = (ChatListFragment) fm.findFragmentById(R.id.titles);
                    chatListFragment.setChatsList(chats);
                }
            }
        });
    }

    @Override
    public void getChatHistory(final long id, final int messageId, final int offset, final int limit) {
        client.send(new TdApi.GetChatHistory(id, messageId, offset, limit), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                if (object instanceof TdApi.Messages) {
                    getMessageFragment().setChatHistory((TdApi.Messages) object);
                }
            }
        });
    }

    @Override
    public void sendMessage(long chatId, String message) {
        client.send(new TdApi.SendMessage(chatId, new TdApi.InputMessageText(message)), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                if (object instanceof TdApi.Messages) {
                    getMessageFragment().setChatHistory((TdApi.Messages) object);
                }
            }
        });
    }

    public long getMyId() {
        return userMe.id;
    }

    public MessagesFragment getMessageFragment() {
        fm = getSupportFragmentManager();
        return (MessagesFragment) fm.findFragmentById(R.id.messages);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        client = TG.getClientInstance();

        resultHandler = new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                Log.i("LOG", "Updates result : " + object);
                if (object instanceof TdApi.UpdateChatReadInbox || object instanceof TdApi.UpdateChatReadOutbox) {
                    getChats(0, 200);
                }
            }
        };

        TG.setDir(this.getFilesDir().getPath());
        TG.setUpdatesHandler(resultHandler);

        getChats(0, 200);

        Toolbar toolbar = (Toolbar) findViewById(R.id.contacts_toolbar);
        setSupportActionBar(toolbar);

        title = drawerTitle = getTitle();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        getMe(); //Get info for NavigationDrawer Header

        List<NavigationItem> drawerItemsList = new ArrayList<>();
        drawerItemsList.add(new NavigationItem(getString(R.string.logout_navigation_item), R.drawable.ic_logout));

        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(ChatActivity.this);
        drawerList.setAdapter(adapter);
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


                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //TODO search
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
                Toast.makeText(ChatActivity.this, R.string.logout_navigation_item, Toast.LENGTH_LONG).show();
                finish();
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

}