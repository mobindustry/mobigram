package net.mobindustry.telegram.ui.fragments;

import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.service.SendGif;
import net.mobindustry.telegram.model.flickr.PhotosFlickr;
import net.mobindustry.telegram.model.flickr.XmlReader;
import net.mobindustry.telegram.model.holder.ListFoldersHolder;
import net.mobindustry.telegram.ui.activity.TransparentActivity;
import net.mobindustry.telegram.ui.adapters.ImagesAdapter;
import net.mobindustry.telegram.utils.GiphyObject;
import net.mobindustry.telegram.utils.ImagesObject;
import net.mobindustry.telegram.utils.Utils;

import java.net.URL;
import java.util.ArrayList;

public class ImagesFragment extends Fragment {
    private ListView imagesList;
    private ImagesAdapter imagesAdapter;
    private ProgressBar progressBar;
    private FragmentTransaction fragmentTransaction;
    private String search;
    private TextView textNoResult;
    private FrameLayout send;
    private TextView number;
    private FrameLayout cancel;
    private Toolbar toolbar;
    public static String FLICKR_URL = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.images_fragment, container, false);
        imagesList = (ListView) view.findViewById(R.id.listImages);
        progressBar = (ProgressBar) view.findViewById(R.id.images_progress_bar);
        textNoResult = (TextView) view.findViewById(R.id.images_no_result);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_images);
        send = (FrameLayout) view.findViewById(R.id.buttonSendImages);
        number = (TextView) view.findViewById(R.id.numberImages);
        cancel = (FrameLayout) view.findViewById(R.id.buttonCancelImages);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Utils.isTablet(getActivity())) {
            if (ListFoldersHolder.getCheckQuantity() != 0) {
                Log.e("Log", "TABLET");
                number.setVisibility(View.VISIBLE);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) number.getLayoutParams();
                params.leftMargin = 65;
                number.setLayoutParams(params);
                int sdk = Build.VERSION.SDK_INT;
                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    number.setBackgroundDrawable(Utils.getShapeDrawable(40, getActivity().getResources().getColor(R.color.message_notify)));
                } else {
                    number.setBackground(Utils.getShapeDrawable(40, getActivity().getResources().getColor(R.color.message_notify)));
                }

                number.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
            } else {
                number.setVisibility(View.GONE);
            }
        } else {
            if (ListFoldersHolder.getCheckQuantity() != 0) {
                number.setVisibility(View.VISIBLE);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) number.getLayoutParams();
                params.leftMargin = 60;
                number.setLayoutParams(params);
                int sdk = Build.VERSION.SDK_INT;
                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    number.setBackgroundDrawable(Utils.getShapeDrawable(60, getActivity().getResources().getColor(R.color.message_notify)));
                } else {
                    number.setBackground(Utils.getShapeDrawable(60, getActivity().getResources().getColor(R.color.message_notify)));
                }

                number.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
            } else {
                number.setVisibility(View.GONE);
            }
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ListFoldersHolder.getListForSending() != null) {
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
        toolbar.inflateMenu(R.menu.search_image);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SearchView sv = new SearchView(getActivity());
                MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                MenuItemCompat.setActionView(item, sv);
                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if (imagesAdapter != null) {
                            imagesAdapter.clear();
                        }
                        search = query;
                        FLICKR_URL = "https://www.flickr.com/services/rest/?method=flickr.photos.search&text="
                                + search
                                + "&api_key=c8d349e8bc5be538e22c275a9600de25&privacy_filter=1&content_type=1";
                        doXmlParse();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
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

    private void doXmlParse() {
        new AsyncTask<Void, Void, PhotosFlickr>() {

            @Override
            protected void onPreExecute() {
                textNoResult.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected PhotosFlickr doInBackground(Void... params) {

                try {
                    URL url = new URL(FLICKR_URL);
                    PhotosFlickr feed = XmlReader.read(url);
                    return feed;
                } catch (Exception e) {
                    Log.e("Log", "Can't parse", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(PhotosFlickr photosFlickr) {
                super.onPostExecute(photosFlickr);
                progressBar.setVisibility(View.GONE);
                if (photosFlickr != null && photosFlickr.getPhotos().size() > 0) {
                    imagesAdapter = new ImagesAdapter(getActivity(),photosFlickr.getPhotos(), new ImagesAdapter.LoadPhotos() {
                        @Override
                        public void load() {
                            InputMethodManager imm = (InputMethodManager) getActivity()
                                    .getSystemService(getActivity().INPUT_METHOD_SERVICE);
                            if (Utils.isTablet(getActivity())) {
                                if (ListFoldersHolder.getCheckQuantity() != 0) {
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
                                    number.setVisibility(View.GONE);
                                }
                            } else {
                                if (ListFoldersHolder.getCheckQuantity() != 0) {
                                    number.setVisibility(View.VISIBLE);
                                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) number.getLayoutParams();
                                    params.leftMargin = 60;
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
                                    number.setVisibility(View.GONE);
                                }

                            }
                        }
                    });
                    imagesList.setAdapter(imagesAdapter);
                } else {
                    textNoResult.setVisibility(View.VISIBLE);
                }
            }
        }.execute();
    }

}
