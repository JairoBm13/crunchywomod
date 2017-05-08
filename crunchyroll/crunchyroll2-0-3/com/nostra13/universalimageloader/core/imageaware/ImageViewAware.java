// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.imageaware;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import java.lang.reflect.Field;
import com.nostra13.universalimageloader.utils.L;
import android.view.View;
import android.widget.ImageView;

public class ImageViewAware extends ViewAware
{
    public ImageViewAware(final ImageView imageView) {
        super((View)imageView);
    }
    
    public ImageViewAware(final ImageView imageView, final boolean b) {
        super((View)imageView, b);
    }
    
    private static int getImageViewFieldValue(final Object o, final String s) {
        final boolean b = false;
        try {
            final Field declaredField = ImageView.class.getDeclaredField(s);
            declaredField.setAccessible(true);
            final int intValue = (int)declaredField.get(o);
            int n = b ? 1 : 0;
            if (intValue > 0) {
                n = (b ? 1 : 0);
                if (intValue < Integer.MAX_VALUE) {
                    n = intValue;
                }
            }
            return n;
        }
        catch (Exception ex) {
            L.e(ex);
            return 0;
        }
    }
    
    @Override
    public int getHeight() {
        int n2;
        final int n = n2 = super.getHeight();
        if (n <= 0) {
            final ImageView imageView = (ImageView)this.viewRef.get();
            n2 = n;
            if (imageView != null) {
                n2 = getImageViewFieldValue(imageView, "mMaxHeight");
            }
        }
        return n2;
    }
    
    @Override
    public ViewScaleType getScaleType() {
        final ImageView imageView = (ImageView)this.viewRef.get();
        if (imageView != null) {
            return ViewScaleType.fromImageView(imageView);
        }
        return super.getScaleType();
    }
    
    @Override
    public int getWidth() {
        int n2;
        final int n = n2 = super.getWidth();
        if (n <= 0) {
            final ImageView imageView = (ImageView)this.viewRef.get();
            n2 = n;
            if (imageView != null) {
                n2 = getImageViewFieldValue(imageView, "mMaxWidth");
            }
        }
        return n2;
    }
    
    public ImageView getWrappedView() {
        return (ImageView)super.getWrappedView();
    }
    
    @Override
    protected void setImageBitmapInto(final Bitmap imageBitmap, final View view) {
        ((ImageView)view).setImageBitmap(imageBitmap);
    }
    
    @Override
    protected void setImageDrawableInto(final Drawable imageDrawable, final View view) {
        ((ImageView)view).setImageDrawable(imageDrawable);
        if (imageDrawable instanceof AnimationDrawable) {
            ((AnimationDrawable)imageDrawable).start();
        }
    }
}
