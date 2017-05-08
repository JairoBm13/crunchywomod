// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.graphics.Path;
import android.graphics.Canvas;
import android.widget.ImageView;

public final class zzki extends ImageView
{
    private int zzZH;
    private zza zzZI;
    private int zzZJ;
    private float zzZK;
    
    protected void onDraw(final Canvas canvas) {
        if (this.zzZI != null) {
            canvas.clipPath(this.zzZI.zzk(this.getWidth(), this.getHeight()));
        }
        super.onDraw(canvas);
        if (this.zzZH != 0) {
            canvas.drawColor(this.zzZH);
        }
    }
    
    protected void onMeasure(int measuredHeight, int measuredWidth) {
        super.onMeasure(measuredHeight, measuredWidth);
        switch (this.zzZJ) {
            default: {
                return;
            }
            case 1: {
                measuredHeight = this.getMeasuredHeight();
                measuredWidth = (int)(measuredHeight * this.zzZK);
                break;
            }
            case 2: {
                measuredWidth = this.getMeasuredWidth();
                measuredHeight = (int)(measuredWidth / this.zzZK);
                break;
            }
        }
        this.setMeasuredDimension(measuredWidth, measuredHeight);
    }
    
    public interface zza
    {
        Path zzk(final int p0, final int p1);
    }
}
