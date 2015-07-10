package net.mobindustry.telegram.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.MessageHandler;
import net.mobindustry.telegram.model.holder.ListFoldersHolder;
import net.mobindustry.telegram.ui.fragments.FolderFragment;
import net.mobindustry.telegram.ui.fragments.MessagesFragment;
import net.mobindustry.telegram.ui.fragments.PageFragment;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.Utils;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewPagerActivity extends FragmentActivity {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private Toolbar toolbar;
    private TextView numberPhotos;
    private int photos = ListFoldersHolder.getList().size();
    private ImageView image;
    private int photoNumber;
    private FrameLayout send;
    private FrameLayout cancel;
    private List<String> listForHolder = new ArrayList<>();

    public int getPhotoNumber() {
        return photoNumber;
    }

    public void setPhotoNumber(int photoNumber) {
        this.photoNumber = photoNumber;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PhotoViewPagerActivity.this, TransparentActivity.class);
        intent.putExtra("choice", Const.SELECTED_FOLDER_FRAGMENT);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view_pager_activity);
        pager = (ViewPager) findViewById(R.id.pager);
        send = (FrameLayout) findViewById(R.id.buttonSendPhoto);
        cancel = (FrameLayout) findViewById(R.id.buttonCancelPhoto);
        toolbar = (Toolbar) findViewById(R.id.toolbar_photo);
        image = (ImageView) findViewById(R.id.photoBig);
        numberPhotos = (TextView) findViewById(R.id.numberPhotosInPhoto);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(ListFoldersHolder.getCurrentSelectedPhoto());
        pager.setOffscreenPageLimit(4);
        setPhotoNumber(ListFoldersHolder.getCurrentSelectedPhoto());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListFoldersHolder.setListForSending(null);
                Intent intent = new Intent();
                intent.putExtra("choice", Const.SEND_FOLDER_FRAGMENT);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ListFoldersHolder.getListForSending() != null) {
                    for (int i = 0; i < ListFoldersHolder.getListForSending().size(); i++) {
                        sendPhotoMessage(ListFoldersHolder.getChatID(), ListFoldersHolder.getListForSending().get(i));
                    }
                    Intent intent = new Intent();
                    intent.putExtra("choice", Const.SEND_FOLDER_FRAGMENT);
                    setResult(RESULT_OK, intent);
                    ListFoldersHolder.setListForSending(null);
                    finish();
                }
            }
        });
        if (!ListFoldersHolder.getList().get(getPhotoNumber()).isCheck()) {
            image.setImageResource(R.drawable.ic_circle);
        } else {
            image.setImageResource(R.drawable.ic_attach_check);
        }
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                //Todo
                int positionForUser = position + 1;
                setPhotoNumber(position);
                toolbar.setTitle(positionForUser + " of " + photos);
                if (!ListFoldersHolder.getList().get(getPhotoNumber()).isCheck()) {
                    image.setImageResource(R.drawable.ic_circle);
                } else {
                    image.setImageResource(R.drawable.ic_attach_check);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        if (Utils.isTablet(this)) {
            if (ListFoldersHolder.getCheckQuantity() != 0) {
                numberPhotos.setVisibility(View.VISIBLE);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) numberPhotos.getLayoutParams();
                params.leftMargin = 65;
                numberPhotos.setLayoutParams(params);
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    numberPhotos.setBackgroundDrawable(Utils.getShapeDrawable(60, getResources().getColor(R.color.message_notify)));
                } else {
                    numberPhotos.setBackground(Utils.getShapeDrawable(60, getResources().getColor(R.color.message_notify)));
                }

                numberPhotos.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
            } else {
                numberPhotos.setVisibility(View.GONE);
            }
        } else {
            if (ListFoldersHolder.getCheckQuantity() != 0) {
                numberPhotos.setVisibility(View.VISIBLE);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) numberPhotos.getLayoutParams();
                params.leftMargin = 60;
                numberPhotos.setLayoutParams(params);
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    numberPhotos.setBackgroundDrawable(Utils.getShapeDrawable(60, getResources().getColor(R.color.message_notify)));
                } else {
                    numberPhotos.setBackground(Utils.getShapeDrawable(60, getResources().getColor(R.color.message_notify)));
                }

                numberPhotos.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
            } else {
                numberPhotos.setVisibility(View.GONE);
            }
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        int positionForUser = ListFoldersHolder.getCurrentSelectedPhoto() + 1;
        toolbar.setTitle(positionForUser + " of " + photos);
        toolbar.setTitleTextColor(getResources().getColor(R.color.background_activity));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoViewPagerActivity.this, TransparentActivity.class);
                intent.putExtra("choice", Const.SELECTED_FOLDER_FRAGMENT);
                finish();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ListFoldersHolder.getList().get(getPhotoNumber()).isCheck()) {
                    image.setImageResource(R.drawable.ic_attach_check);
                    ListFoldersHolder.getList().get(getPhotoNumber()).setCheck(true);
                    //listForHolder.remove(ListFoldersHolder.getList().get(getPhotoNumber()).getFile().getAbsolutePath());
                    if (ListFoldersHolder.getListForSending() == null) {
                        ListFoldersHolder.setListForSending(new ArrayList<String>());
                        String path = ListFoldersHolder.getList().get(getPhotoNumber()).getFile().getAbsolutePath();
                        ListFoldersHolder.getListForSending().add(path);
                    } else {
                        String path = ListFoldersHolder.getList().get(getPhotoNumber()).getFile().getAbsolutePath();
                        List<String> list = ListFoldersHolder.getListForSending();
                        list.add(path);
                        ListFoldersHolder.setListForSending(list);
                    }
                    ListFoldersHolder.setCheckQuantity(ListFoldersHolder.getListForSending().size());
                } else {
                    if (ListFoldersHolder.getCheckQuantity() < 10) {
                        image.setImageResource(R.drawable.ic_circle);
                        ListFoldersHolder.getList().get(getPhotoNumber()).setCheck(false);
                        if (ListFoldersHolder.getListForSending() == null) {
                            ListFoldersHolder.setListForSending(new ArrayList<String>());
                            String path = ListFoldersHolder.getList().get(getPhotoNumber()).getFile().getAbsolutePath();
                            ListFoldersHolder.getListForSending().remove(path);
                        } else {
                            String path = ListFoldersHolder.getList().get(getPhotoNumber()).getFile().getAbsolutePath();
                            List<String> list = ListFoldersHolder.getListForSending();
                            list.remove(path);
                            ListFoldersHolder.setListForSending(list);
                        }
                        ListFoldersHolder.setCheckQuantity(ListFoldersHolder.getListForSending().size());
                    }
                }

                if (Utils.isTablet(PhotoViewPagerActivity.this)) {
                    if (ListFoldersHolder.getCheckQuantity() != 0) {
                        numberPhotos.setVisibility(View.VISIBLE);
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) numberPhotos.getLayoutParams();
                        params.leftMargin = 65;
                        numberPhotos.setLayoutParams(params);
                        int sdk = android.os.Build.VERSION.SDK_INT;
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            numberPhotos.setBackgroundDrawable(Utils.getShapeDrawable(60, getResources().getColor(R.color.message_notify)));
                        } else {
                            numberPhotos.setBackground(Utils.getShapeDrawable(60, getResources().getColor(R.color.message_notify)));
                        }

                        numberPhotos.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
                    } else {
                        numberPhotos.setVisibility(View.GONE);
                    }
                } else {
                    if (ListFoldersHolder.getCheckQuantity() != 0) {
                        numberPhotos.setVisibility(View.VISIBLE);
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) numberPhotos.getLayoutParams();
                        params.leftMargin = 60;
                        numberPhotos.setLayoutParams(params);
                        int sdk = android.os.Build.VERSION.SDK_INT;
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            numberPhotos.setBackgroundDrawable(Utils.getShapeDrawable(60, getResources().getColor(R.color.message_notify)));
                        } else {
                            numberPhotos.setBackground(Utils.getShapeDrawable(60, getResources().getColor(R.color.message_notify)));
                        }

                        numberPhotos.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
                    } else {
                        numberPhotos.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position, ListFoldersHolder.getList().get(position).getFile().getAbsolutePath());
        }

        @Override
        public int getCount() {
            return ListFoldersHolder.getList().size();
        }
    }

    public void sendPhotoMessage(long chatId, String path) {
        new ApiClient<>(new TdApi.SendMessage(chatId, new TdApi.InputMessagePhoto(path)), new MessageHandler(), new ApiClient.OnApiResultHandler() {
            @Override
            public void onApiResult(BaseHandler output) {

            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }


}
