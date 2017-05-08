// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.widget;

import android.view.MotionEvent;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.view.ViewPager;

public class SafeViewPager extends ViewPager
{
    public SafeViewPager(final Context context) {
        super(context);
    }
    
    public SafeViewPager(final Context context, final AttributeSet set) {
        super(context, set);
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
}
