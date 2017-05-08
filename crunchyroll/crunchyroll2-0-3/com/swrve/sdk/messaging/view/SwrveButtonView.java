// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging.view;

import android.view.MotionEvent;
import android.graphics.Canvas;
import android.content.Context;
import android.graphics.Color;
import com.swrve.sdk.messaging.SwrveActionType;
import android.widget.ImageView;

public class SwrveButtonView extends ImageView
{
    private static int clickColor;
    private SwrveActionType type;
    
    static {
        SwrveButtonView.clickColor = Color.argb(100, 0, 0, 0);
    }
    
    public SwrveButtonView(final Context context, final SwrveActionType type) {
        super(context);
        this.type = type;
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0: {
                this.setColorFilter(SwrveButtonView.clickColor);
                this.invalidate();
                break;
            }
            case 1: {
                this.clearColorFilter();
                break;
            }
        }
        return super.onTouchEvent(motionEvent);
    }
}
