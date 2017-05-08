// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging.view;

import android.view.MotionEvent;
import android.content.Context;
import android.widget.ImageView;

public class SwrveImageView extends ImageView
{
    public SwrveImageView(final Context context) {
        super(context);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return true;
    }
}
