package com.efunhub.ticktok.activity;

import androidx.recyclerview.widget.PagerSnapHelper;

public class CustomSnapHelper extends PagerSnapHelper {
    private boolean isScrollEnabled = true;

    public void setScrollEnabled(boolean scrollEnabled) {
        isScrollEnabled = scrollEnabled;
    }

    @Override
    public boolean onFling(int velocityX, int velocityY) {
        if (!isScrollEnabled) {
            return false;
        }
        return super.onFling(velocityX, velocityY);
    }
}

