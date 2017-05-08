// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer;

import android.view.View$MeasureSpec;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.FrameLayout;

public final class AspectRatioFrameLayout extends FrameLayout
{
    private float videoAspectRatio;
    
    public AspectRatioFrameLayout(final Context context) {
        super(context);
    }
    
    public AspectRatioFrameLayout(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    protected void onMeasure(int measuredHeight, int measuredWidth) {
        super.onMeasure(measuredHeight, measuredWidth);
        if (this.videoAspectRatio != 0.0f) {
            measuredWidth = this.getMeasuredWidth();
            measuredHeight = this.getMeasuredHeight();
            final float n = this.videoAspectRatio / (measuredWidth / measuredHeight) - 1.0f;
            if (Math.abs(n) > 0.01f) {
                if (n > 0.0f) {
                    measuredHeight = (int)(measuredWidth / this.videoAspectRatio);
                }
                else {
                    measuredWidth = (int)(measuredHeight * this.videoAspectRatio);
                }
                super.onMeasure(View$MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824), View$MeasureSpec.makeMeasureSpec(measuredHeight, 1073741824));
            }
        }
    }
    
    public void setAspectRatio(final float videoAspectRatio) {
        if (this.videoAspectRatio != videoAspectRatio) {
            this.videoAspectRatio = videoAspectRatio;
            this.requestLayout();
        }
    }
}
