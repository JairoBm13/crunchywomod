// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.widget;

import android.graphics.Canvas;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

public class CustomSwipeRefreshLayout extends SwipeRefreshLayout
{
    public CustomSwipeRefreshLayout(final Context context) {
        super(context);
    }
    
    public CustomSwipeRefreshLayout(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public void onDraw(final Canvas canvas) {
        this.setColorSchemeResources(2131558531, 2131558500, 2131558531, 2131558500);
        super.onDraw(canvas);
    }
}
