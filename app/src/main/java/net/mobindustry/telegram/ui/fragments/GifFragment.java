package net.mobindustry.telegram.ui.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.service.SendGif;
import net.mobindustry.telegram.model.gif.Giphy;
import net.mobindustry.telegram.model.gif.GiphyInfo;
import net.mobindustry.telegram.model.holder.DataHolder;
import net.mobindustry.telegram.model.holder.ListFoldersHolder;
import net.mobindustry.telegram.ui.activity.PhotoViewerActivity;
import net.mobindustry.telegram.ui.activity.TransparentActivity;
import net.mobindustry.telegram.ui.adapters.GifAdapter;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.GiphyObject;
import net.mobindustry.telegram.utils.ImagesObject;
import net.mobindustry.telegram.utils.Utils;
import net.mobindustry.telegram.utils.widget.LoadMoreListView;

import java.io.Serializable;
import java.util.ArrayList;
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
    private TextView number;
    private FrameLayout cancel;
    private SearchView sv;
    private LinearLayout layoutButtons;
    private LoadGifs loadGifs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gif_fragment, container, false);
        gifsList = (LoadMoreListView) view.findViewById(R.id.loadMoreList);
        progressBar = (ProgressBar) view.findViewById(R.id.gif_progress_bar);
        textNoResult = (TextView) view.findViewById(R.id.gif_no_result);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_gif);
        send = (FrameLayout) view.findViewById(R.id.buttonSendGif);
        number = (TextView) view.findViewById(R.id.numberGif);
        cancel = (FrameLayout) view.findViewById(R.id.buttonCancelGif);
        layoutButtons = (LinearLayout) view.findViewById(R.id.layoutButtonsGif);
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && !Utils.isTablet(getActivity())) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 0.25f);
            layoutButtons.setLayoutParams(param);
        } else {
            LinearLayout.LayoutParams paramButtons = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 0.15f);
            layoutButtons.setLayoutParams(paramButtons);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Utils.isTablet(getActivity())) {
            if (ListFoldersHolder.getCheckQuantity() > 0 && ListFoldersHolder.getListForSending() != null && ListFoldersHolder.getListForSending().size() > 0) {
                send.setEnabled(true);
                Log.e("Log", "TABLET");
                number.setVisibility(View.VISIBLE);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) number.getLayoutParams();
                params.leftMargin = 50;
                number.setLayoutParams(params);
                int sdk = Build.VERSION.SDK_INT;
                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    number.setBackgroundDrawable(Utils.getShapeDrawable(35, getActivity().getResources().getColor(R.color.message_notify)));
                } else {
                    number.setBackground(Utils.getShapeDrawable(35, getActivity().getResources().getColor(R.color.message_notify)));
                }

                number.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
            } else {
                send.setEnabled(false);
                number.setVisibility(View.GONE);
            }
        } else {
            if (ListFoldersHolder.getCheckQuantity() > 0 && ListFoldersHolder.getListForSending() != null && ListFoldersHolder.getListForSending().size() > 0) {
                send.setEnabled(true);
                number.setVisibility(View.VISIBLE);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) number.getLayoutParams();
                if (Utils.getSmallestScreenSize(getActivity()) <= 480) {
                    params.leftMargin = 10;
                } else {
                    params.leftMargin = 60;
                }
                number.setLayoutParams(params);
                int sdk = Build.VERSION.SDK_INT;
                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    number.setBackgroundDrawable(Utils.getShapeDrawable(60, getActivity().getResources().getColor(R.color.message_notify)));
                } else {
                    number.setBackground(Utils.getShapeDrawable(60, getActivity().getResources().getColor(R.color.message_notify)));
                }

                number.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
            } else {
                send.setEnabled(false);
                number.setVisibility(View.GONE);
            }

        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !Utils.isTablet(getActivity())) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 0.25f);
            layoutButtons.setLayoutParams(param);
        } else {
            LinearLayout.LayoutParams paramButtons = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 0.15f);
            layoutButtons.setLayoutParams(paramButtons);
        }

        gifsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PhotoViewerActivity.class);
                intent.putExtra("file_path", giphyObjectList.get(position).getPath());
                intent.putExtra("gif", 1);
                getActivity().startActivity(intent);
            }
        });

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
                        gifsList.deferNotifyDataSetChanged();
                        gifsList.onLoadMoreComplete();
                    }
                }.execute();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ListFoldersHolder.getListForSending() != null && ListFoldersHolder.getListForSending().size() != 0) {
                    for (int i = 0; i < ListFoldersHolder.getListForSending().size(); i++) {
                        if (ListFoldersHolder.getListForSending().get(i) instanceof ImagesObject) {
                            if (((ImagesObject) ListFoldersHolder.getListForSending().get(i)).getPath().contains("http")) {
                                String linkImage = ((ImagesObject) ListFoldersHolder.getListForSending().get(i)).getPath();
                                if (ListFoldersHolder.getListImages() == null) {
                                    ListFoldersHolder.setListImages(new ArrayList<String>());
                                }
                                ListFoldersHolder.getListImages().add(linkImage);
                            } else {
                                ((TransparentActivity) getActivity()).sendPhotoMessage(ListFoldersHolder.getChatID(),
                                        ((ImagesObject) ListFoldersHolder.getListForSending().get(i)).getPath());
                            }
                        }
                        if (ListFoldersHolder.getListForSending().get(i) instanceof GiphyObject) {
                            if (ListFoldersHolder.getListGif() == null) {
                                ListFoldersHolder.setListGif(new ArrayList<String>());
                            }
                            String link = ((GiphyObject) ListFoldersHolder.getListForSending().get(i)).getPath();
                            ListFoldersHolder.getListGif().add(link);
                            Log.e("Log", "Gif " + ListFoldersHolder.getListGif().size());
                        }
                    }
                    getActivity().startService(new Intent(getActivity(), SendGif.class));
                    getActivity().finish();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListFoldersHolder.setListForSending(null);
                getActivity().finish();
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.inflateMenu(R.menu.search_gif);
        sv = new SearchView(DataHolder.getThemedContext());
        final MenuItem menuItem = toolbar.getMenu().findItem(R.id.action_search_gif);
        MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        MenuItemCompat.setActionView(menuItem, sv);
        MenuItemCompat.expandActionView(menuItem);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (gifAdapter != null) {
                    gifAdapter.clear();
                }
                Log.e("Log", "onQueryTextChange " + query);

                search = query;
                loadGifs = new LoadGifs();
                loadGifs.execute();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("Log", "onQueryTextChange " + newText);
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
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                gifAdapter = new GifAdapter(getActivity(), giphyObjectList, new GifAdapter.LoadGif() {
                    @Override
                    public void load() {
                        InputMethodManager imm = (InputMethodManager) getActivity()
                                .getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        if (Utils.isTablet(getActivity())) {
                            if (ListFoldersHolder.getCheckQuantity() > 0 && ListFoldersHolder.getListForSending() != null && ListFoldersHolder.getListForSending().size() > 0) {
                                send.setEnabled(true);
                                Log.e("Log", "TABLET");
                                number.setVisibility(View.VISIBLE);
                                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) number.getLayoutParams();
                                params.leftMargin = 50;
                                number.setLayoutParams(params);
                                int sdk = Build.VERSION.SDK_INT;
                                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                                    number.setBackgroundDrawable(Utils.getShapeDrawable(35, getActivity().getResources().getColor(R.color.message_notify)));
                                } else {
                                    number.setBackground(Utils.getShapeDrawable(35, getActivity().getResources().getColor(R.color.message_notify)));
                                }

                                number.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
                            } else {
                                send.setEnabled(false);
                                number.setVisibility(View.GONE);
                            }
                        } else {
                            if (ListFoldersHolder.getCheckQuantity() > 0 && ListFoldersHolder.getListForSending() != null && ListFoldersHolder.getListForSending().size() > 0) {
                                send.setEnabled(true);
                                number.setVisibility(View.VISIBLE);
                                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) number.getLayoutParams();
                                if (Utils.getSmallestScreenSize(getActivity()) <= 480) {
                                    params.leftMargin = 10;
                                } else {
                                    params.leftMargin = 60;
                                }
                                number.setLayoutParams(params);
                                int sdk = Build.VERSION.SDK_INT;
                                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                                    if (imm.isAcceptingText()) {
                                        number.setBackgroundDrawable(Utils.getShapeDrawable(50, getActivity().getResources().getColor(R.color.message_notify)));
                                    } else {
                                        number.setBackgroundDrawable(Utils.getShapeDrawable(60, getActivity().getResources().getColor(R.color.message_notify)));
                                    }
                                } else {
                                    if (imm.isAcceptingText()) {
                                        number.setBackgroundDrawable(Utils.getShapeDrawable(50, getActivity().getResources().getColor(R.color.message_notify)));
                                    } else {
                                        number.setBackgroundDrawable(Utils.getShapeDrawable(60, getActivity().getResources().getColor(R.color.message_notify)));
                                    }
                                }

                                number.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
                            } else {
                                send.setEnabled(false);
                                number.setVisibility(View.GONE);
                            }

                        }
                    }
                });
                gifsList.setAdapter(gifAdapter);
            } else {
                textNoResult.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (loadGifs != null) {
            loadGifs.cancel(true);
        }
    }
}
