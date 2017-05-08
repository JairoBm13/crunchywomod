// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.widget;

import android.view.MotionEvent;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

public class ScrollRecyclerView extends RecyclerView
{
    public ScrollRecyclerView(final Context context) {
        super(context);
    }
    
    public ScrollRecyclerView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public ScrollRecyclerView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public int getHorizontalOffset() {
        return super.computeHorizontalScrollOffset();
    }
    
    public int getVerticalOffset() {
        return super.computeVerticalScrollOffset();
    }
    
    @Override
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        try {
            return super.onInterceptTouchEvent(motionEvent);
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        try {
            return super.onTouchEvent(motionEvent);
        }
        catch (Exception ex) {
            return false;
        }
    }
}
