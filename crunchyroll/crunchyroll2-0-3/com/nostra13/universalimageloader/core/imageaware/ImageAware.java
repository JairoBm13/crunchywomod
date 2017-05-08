// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.imageaware;

import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.view.View;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;

public interface ImageAware
{
    int getHeight();
    
    int getId();
    
    ViewScaleType getScaleType();
    
    int getWidth();
    
    View getWrappedView();
    
    boolean isCollected();
    
    boolean setImageBitmap(final Bitmap p0);
    
    boolean setImageDrawable(final Drawable p0);
}
