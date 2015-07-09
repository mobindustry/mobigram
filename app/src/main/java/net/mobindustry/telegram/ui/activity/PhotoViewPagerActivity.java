package net.mobindustry.telegram.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.handlers.CircularViewPagerHandler;
import net.mobindustry.telegram.model.holder.ListFoldersHolder;
import net.mobindustry.telegram.ui.fragments.PageFragment;
import net.mobindustry.telegram.utils.Const;

public class PhotoViewPagerActivity extends FragmentActivity {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private int lastPositionInPager;
    private CircularViewPagerHandler circularViewPagerHandler;

    @Override
    public void onBackPressed() {
        lastPositionInPager = circularViewPagerHandler.getCurrentPosition();
        Intent intent = new Intent(PhotoViewPagerActivity.this, TransparentActivity.class);
        intent.putExtra("choice", Const.SELECTED_FOLDER_FRAGMENT);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view_pager_activity);
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(getIntent().getIntExtra("number in list", RESULT_OK));
        circularViewPagerHandler = new CircularViewPagerHandler(pager);
        pager.setOnPageChangeListener(circularViewPagerHandler);
        pager.setOffscreenPageLimit(4);

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


}
