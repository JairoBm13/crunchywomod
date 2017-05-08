// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.imageaware;

import android.graphics.drawable.Drawable;
import com.nostra13.universalimageloader.utils.L;
import android.os.Looper;
import android.graphics.Bitmap;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import android.view.ViewGroup$LayoutParams;
import java.lang.ref.WeakReference;
import android.view.View;
import java.lang.ref.Reference;

public abstract class ViewAware implements ImageAware
{
    public static final String WARN_CANT_SET_BITMAP = "Can't set a bitmap into view. You should call ImageLoader on UI thread for it.";
    public static final String WARN_CANT_SET_DRAWABLE = "Can't set a drawable into view. You should call ImageLoader on UI thread for it.";
    protected boolean checkActualViewSize;
    protected Reference<View> viewRef;
    
    public ViewAware(final View view) {
        this(view, true);
    }
    
    public ViewAware(final View view, final boolean checkActualViewSize) {
        if (view == null) {
            throw new IllegalArgumentException("view must not be null");
        }
        this.viewRef = new WeakReference<View>(view);
        this.checkActualViewSize = checkActualViewSize;
    }
    
    @Override
    public int getHeight() {
        final View view = this.viewRef.get();
        if (view != null) {
            final ViewGroup$LayoutParams layoutParams = view.getLayoutParams();
            int height;
            final int n = height = 0;
            if (this.checkActualViewSize) {
                height = n;
                if (layoutParams != null) {
                    height = n;
                    if (layoutParams.height != -2) {
                        height = view.getHeight();
                    }
                }
            }
            int height2;
            if ((height2 = height) <= 0) {
                height2 = height;
                if (layoutParams != null) {
                    height2 = layoutParams.height;
                }
            }
            return height2;
        }
        return 0;
    }
    
    @Override
    public int getId() {
        final View view = this.viewRef.get();
        if (view == null) {
            return super.hashCode();
        }
        return view.hashCode();
    }
    
    @Override
    public ViewScaleType getScaleType() {
        return ViewScaleType.CROP;
    }
    
    @Override
    public int getWidth() {
        final View view = this.viewRef.get();
        if (view != null) {
            final ViewGroup$LayoutParams layoutParams = view.getLayoutParams();
            int width;
            final int n = width = 0;
            if (this.checkActualViewSize) {
                width = n;
                if (layoutParams != null) {
                    width = n;
                    if (layoutParams.width != -2) {
                        width = view.getWidth();
                    }
                }
            }
            int width2;
            if ((width2 = width) <= 0) {
                width2 = width;
                if (layoutParams != null) {
                    width2 = layoutParams.width;
                }
            }
            return width2;
        }
        return 0;
    }
    
    @Override
    public View getWrappedView() {
        return this.viewRef.get();
    }
    
    @Override
    public boolean isCollected() {
        return this.viewRef.get() == null;
    }
    
    @Override
    public boolean setImageBitmap(final Bitmap bitmap) {
        boolean b = false;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            final View view = this.viewRef.get();
            if (view != null) {
                this.setImageBitmapInto(bitmap, view);
                b = true;
            }
            return b;
        }
        L.w("Can't set a bitmap into view. You should call ImageLoader on UI thread for it.", new Object[0]);
        return false;
    }
    
    protected abstract void setImageBitmapInto(final Bitmap p0, final View p1);
    
    @Override
    public boolean setImageDrawable(final Drawable drawable) {
        boolean b = false;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            final View view = this.viewRef.get();
            if (view != null) {
                this.setImageDrawableInto(drawable, view);
                b = true;
            }
            return b;
        }
        L.w("Can't set a drawable into view. You should call ImageLoader on UI thread for it.", new Object[0]);
        return false;
    }
    
    protected abstract void setImageDrawableInto(final Drawable p0, final View p1);
}
