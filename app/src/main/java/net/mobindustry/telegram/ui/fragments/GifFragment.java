package net.mobindustry.telegram.ui.fragments;

import android.app.SearchManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.Gif.Giphy;
import net.mobindustry.telegram.model.Gif.GiphyInfo;
import net.mobindustry.telegram.ui.adapters.GifAdapter;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.widget.LoadMoreListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GifFragment extends Fragment {
    private LoadMoreListView gifsList;
    private List<String> urls;
    private GifAdapter gifAdapter;
    private ProgressBar progressBar;
    private int page = 0;
    private Toolbar toolbar;
    private FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gif_fragment, container, false);
        gifsList = (LoadMoreListView) view.findViewById(R.id.loadMoreList);
        progressBar = (ProgressBar) view.findViewById(R.id.gif_progress_bar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_gif);
        LoadGifs loadGifs = new LoadGifs();
        loadGifs.execute();
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
                                    .add("q", "funny cat")
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
                            urls.add(info.getImages().getOriginal().getUrl());
                        }
                        gifsList.deferNotifyDataSetChanged();
                        gifsList.onLoadMoreComplete();
                    }
                }.execute();
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.inflateMenu(R.menu.search_gif);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SearchView sv = new SearchView(getActivity());
                MenuItemCompat.setShowAsAction(item,MenuItemCompat.SHOW_AS_ACTION_IF_ROOM | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                MenuItemCompat.setActionView(item, sv);
                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Log.e("Log", " " + query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        Log.e("Log", " " + newText);
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
        protected String doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(2);
                AndroidHttpClient httpClient = new AndroidHttpClient(Const.URL_GIF);
                httpClient.setMaxRetries(5);
                ParameterMap param = httpClient.newParams()
                        .add("q", "cat")
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
            Gson gson = new GsonBuilder().create();

            Giphy giphy = gson.fromJson(parseJson, Giphy.class);

            urls = new ArrayList<String>();
            for (GiphyInfo info : giphy.getData()) {
                urls.add(info.getImages().getOriginal().getUrl());
            }
            gifAdapter = new GifAdapter(getActivity(), urls);
            progressBar.setVisibility(View.GONE);
            gifsList.setAdapter(gifAdapter);
        }
    }


}
