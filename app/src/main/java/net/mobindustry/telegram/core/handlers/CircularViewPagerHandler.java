package net.mobindustry.telegram.core.handlers;

import android.support.v4.view.ViewPager;

public class CircularViewPagerHandler implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private int currentPosition;
    private int scrollState;

    public CircularViewPagerHandler(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(final int position) {
        currentPosition=position;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public void onPageScrollStateChanged(final int state) {
        handleScrollState(state);
        scrollState=state;

    }

    private void handleScrollState(final int state){
        if (state==ViewPager.SCROLL_STATE_IDLE){
            setNextItem();
        }
    }

    private void setNextItem() {
        if (!isScrollStateSettling()){
            handleSetNextItem();
        }
    }

    private boolean isScrollStateSettling() {
        return scrollState==ViewPager.SCROLL_STATE_SETTLING;
    }

    private void handleSetNextItem() {
        final int lastPosition=viewPager.getAdapter().getCount()-1;
        if (currentPosition==0){
            viewPager.setCurrentItem(lastPosition,false);
        } else {
            if (currentPosition==lastPosition){
                viewPager.setCurrentItem(0,false);
            }
        }
    }
}
