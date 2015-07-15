package net.mobindustry.telegram.ui.fragments;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.MessageHandler;
import net.mobindustry.telegram.core.service.CreateGalleryThumbs;
import net.mobindustry.telegram.core.service.SendGif;
import net.mobindustry.telegram.model.Gif.Giphy;
import net.mobindustry.telegram.model.Gif.GiphyInfo;
import net.mobindustry.telegram.model.holder.ListFoldersHolder;
import net.mobindustry.telegram.ui.activity.ChatActivity;
import net.mobindustry.telegram.ui.activity.TransparentActivity;
import net.mobindustry.telegram.ui.adapters.GifAdapter;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.GiphyObject;
import net.mobindustry.telegram.utils.ImagesObject;
import net.mobindustry.telegram.utils.widget.LoadMoreListView;

import org.apache.http.util.ByteArrayBuffer;
import org.drinkless.td.libcore.telegram.TdApi;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GifFragment extends Fragment {
    private LoadMoreListView gifsList;
    private List<GiphyObject> giphyObjectList;
    private GifAdapter gifAdapter;
    private ProgressBar progressBar;
    private int page = 0;
    private Toolbar toolbar;
    private FragmentTransaction fragmentTransaction;
    private String search;
    private TextView textNoResult;
    private FrameLayout send;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gif_fragment, container, false);
        gifsList = (LoadMoreListView) view.findViewById(R.id.loadMoreList);
        progressBar = (ProgressBar) view.findViewById(R.id.gif_progress_bar);
        textNoResult = (TextView) view.findViewById(R.id.gif_no_result);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_gif);
        send = (FrameLayout) view.findViewById(R.id.buttonSendGif);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gifsList.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        try {
                            TimeUnit.SECONDS.sleep(2);
                            page += 10;
                            AndroidHttpClient httpClient = new AndroidHttpClient(Const.URL_GIF);
                            httpClient.setMaxRetries(5);
                            ParameterMap param = httpClient.newParams()
                                    .add("q", search)
                                    .add("api_key", Const.API_KEY_GIF)
                                    .add("offset", String.valueOf(page));
                            HttpResponse httpResponse = httpClient.get("/v1/gifs/search", param);
                            Log.e("Log", "LINK " + httpResponse.getBodyAsString());

                            if (httpResponse.getBodyAsString() != null) {
                                return httpResponse.getBodyAsString();
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        Gson gson = new GsonBuilder().create();

                        Giphy giphy = gson.fromJson(s, Giphy.class);

                        for (GiphyInfo info : giphy.getData()) {
                            GiphyObject giphyObject = new GiphyObject();
                            giphyObject.setCheck(false);
                            giphyObject.setPath(info.getImages().getOriginal().getUrl());
                            giphyObjectList.add(giphyObject);
                        }
                        ListFoldersHolder.setGiphyObjectList(giphyObjectList);
                        gifsList.deferNotifyDataSetChanged();
                        gifsList.onLoadMoreComplete();
                    }
                }.execute();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ListFoldersHolder.getListForSending() != null) {
                    for (int i = 0; i < ListFoldersHolder.getListForSending().size(); i++) {
                        if (ListFoldersHolder.getListForSending().get(i) instanceof ImagesObject) {
                            ((TransparentActivity) getActivity()).sendPhotoMessage(ListFoldersHolder.getChatID(),
                                    ((ImagesObject) ListFoldersHolder.getListForSending().get(i)).getPath());

                        }
                        if (ListFoldersHolder.getListForSending().get(i) instanceof GiphyObject) {
                            if (ListFoldersHolder.getListGif() == null) {
                                ListFoldersHolder.setListGif(new ArrayList<String>());
                            }
                            String link = ((GiphyObject) ListFoldersHolder.getListForSending().get(i)).getPath();
                            ListFoldersHolder.getListGif().add(link);
                        }
                    }

                }
                getActivity().startService(new Intent(getActivity(), SendGif.class));
                ListFoldersHolder.setListForSending(null);
                getActivity().finish();
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.inflateMenu(R.menu.search_gif);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SearchView sv = new SearchView(getActivity());
                MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                MenuItemCompat.setActionView(item, sv);
                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        search = query;
                        LoadGifs loadGifs = new LoadGifs();
                        loadGifs.execute();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        Log.e("Log", "onQueryTextChange " + newText);
                        return false;
                    }
                });
                return false;
            }
        });
        toolbar.setTitleTextColor(getResources().getColor(R.color.background_activity));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryFragment galleryFragment;
                galleryFragment = new GalleryFragment();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
                fragmentTransaction.replace(R.id.transparent_content, galleryFragment);
                fragmentTransaction.commit();
            }
        });

    }


    public class LoadGifs extends AsyncTask<Void, Void, String> implements Serializable {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textNoResult.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(2);

                AndroidHttpClient httpClient = new AndroidHttpClient(Const.URL_GIF);
                httpClient.setMaxRetries(5);
                ParameterMap param = httpClient.newParams()
                        .add("q", search)
                        .add("api_key", Const.API_KEY_GIF)
                        .add("offset", String.valueOf(page));
                HttpResponse httpResponse = httpClient.get("/v1/gifs/search", param);
                if (httpResponse.getBodyAsString() != null) {
                    return httpResponse.getBodyAsString();
                }
                return httpResponse.getBodyAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String parseJson) {
            super.onPostExecute(parseJson);
            progressBar.setVisibility(View.GONE);
            Gson gson = new GsonBuilder().create();

            Giphy giphy = gson.fromJson(parseJson, Giphy.class);

            giphyObjectList = new ArrayList<>();
            if (giphy.getPagination().getTotal_count() > 0) {
                for (GiphyInfo info : giphy.getData()) {
                    GiphyObject giphyObject = new GiphyObject();
                    giphyObject.setCheck(false);
                    giphyObject.setPath(info.getImages().getOriginal().getUrl());
                    giphyObjectList.add(giphyObject);
                }
                gifAdapter = new GifAdapter(getActivity(), giphyObjectList);
                gifsList.setAdapter(gifAdapter);
                ListFoldersHolder.setGiphyObjectList(giphyObjectList);
            } else {
                textNoResult.setVisibility(View.VISIBLE);
            }
        }
    }
}
