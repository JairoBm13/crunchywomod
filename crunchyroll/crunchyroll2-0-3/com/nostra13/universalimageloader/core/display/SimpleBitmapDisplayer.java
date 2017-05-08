// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.display;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import android.graphics.Bitmap;

public final class SimpleBitmapDisplayer implements BitmapDisplayer
{
    @Override
    public void display(final Bitmap imageBitmap, final ImageAware imageAware, final LoadedFrom loadedFrom) {
        imageAware.setImageBitmap(imageBitmap);
    }
}
