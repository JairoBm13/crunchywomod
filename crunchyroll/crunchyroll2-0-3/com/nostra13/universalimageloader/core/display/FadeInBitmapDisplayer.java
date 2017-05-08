// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.display;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import android.graphics.Bitmap;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.View;

public class FadeInBitmapDisplayer implements BitmapDisplayer
{
    private final boolean animateFromDisk;
    private final boolean animateFromMemory;
    private final boolean animateFromNetwork;
    private final int durationMillis;
    
    public FadeInBitmapDisplayer(final int n) {
        this(n, true, true, true);
    }
    
    public FadeInBitmapDisplayer(final int durationMillis, final boolean animateFromNetwork, final boolean animateFromDisk, final boolean animateFromMemory) {
        this.durationMillis = durationMillis;
        this.animateFromNetwork = animateFromNetwork;
        this.animateFromDisk = animateFromDisk;
        this.animateFromMemory = animateFromMemory;
    }
    
    public static void animate(final View view, final int n) {
        if (view != null) {
            final AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration((long)n);
            alphaAnimation.setInterpolator((Interpolator)new DecelerateInterpolator());
            view.startAnimation((Animation)alphaAnimation);
        }
    }
    
    @Override
    public void display(final Bitmap imageBitmap, final ImageAware imageAware, final LoadedFrom loadedFrom) {
        imageAware.setImageBitmap(imageBitmap);
        if ((this.animateFromNetwork && loadedFrom == LoadedFrom.NETWORK) || (this.animateFromDisk && loadedFrom == LoadedFrom.DISC_CACHE) || (this.animateFromMemory && loadedFrom == LoadedFrom.MEMORY_CACHE)) {
            animate(imageAware.getWrappedView(), this.durationMillis);
        }
    }
}
