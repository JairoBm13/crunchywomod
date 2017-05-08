// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.display;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import android.graphics.Bitmap;

public interface BitmapDisplayer
{
    void display(final Bitmap p0, final ImageAware p1, final LoadedFrom p2);
}
