// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.imageaware;

import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.view.View;
import android.text.TextUtils;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;

public class NonViewAware implements ImageAware
{
    protected final ImageSize imageSize;
    protected final String imageUri;
    protected final ViewScaleType scaleType;
    
    public NonViewAware(final ImageSize imageSize, final ViewScaleType viewScaleType) {
        this(null, imageSize, viewScaleType);
    }
    
    public NonViewAware(final String imageUri, final ImageSize imageSize, final ViewScaleType scaleType) {
        if (imageSize == null) {
            throw new IllegalArgumentException("imageSize must not be null");
        }
        if (scaleType == null) {
            throw new IllegalArgumentException("scaleType must not be null");
        }
        this.imageUri = imageUri;
        this.imageSize = imageSize;
        this.scaleType = scaleType;
    }
    
    @Override
    public int getHeight() {
        return this.imageSize.getHeight();
    }
    
    @Override
    public int getId() {
        if (TextUtils.isEmpty((CharSequence)this.imageUri)) {
            return super.hashCode();
        }
        return this.imageUri.hashCode();
    }
    
    @Override
    public ViewScaleType getScaleType() {
        return this.scaleType;
    }
    
    @Override
    public int getWidth() {
        return this.imageSize.getWidth();
    }
    
    @Override
    public View getWrappedView() {
        return null;
    }
    
    @Override
    public boolean isCollected() {
        return false;
    }
    
    @Override
    public boolean setImageBitmap(final Bitmap bitmap) {
        return true;
    }
    
    @Override
    public boolean setImageDrawable(final Drawable drawable) {
        return true;
    }
}
