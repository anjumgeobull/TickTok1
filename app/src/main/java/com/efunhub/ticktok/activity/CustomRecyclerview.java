package com.efunhub.ticktok.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.recyclerview.widget.RecyclerView;

public class CustomRecyclerview extends RecyclerView {

    public CustomRecyclerview(Context context) {
        super(context);
    }

    public CustomRecyclerview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Consume vertical scrolling gestures and prevent them from reaching the PagerSnapHelper
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float deltaX = ev.getX() - ev.getRawX();
            float deltaY = ev.getY() - ev.getRawY();
            if (Math.abs(deltaX) < Math.abs(deltaY)) {
                // Vertical scrolling gesture detected, consume the event
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }
}
