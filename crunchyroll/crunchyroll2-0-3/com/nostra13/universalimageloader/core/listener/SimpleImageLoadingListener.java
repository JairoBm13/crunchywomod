// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.listener;

import com.nostra13.universalimageloader.core.assist.FailReason;
import android.graphics.Bitmap;
import android.view.View;

public class SimpleImageLoadingListener implements ImageLoadingListener
{
    @Override
    public void onLoadingCancelled(final String s, final View view) {
    }
    
    @Override
    public void onLoadingComplete(final String s, final View view, final Bitmap bitmap) {
    }
    
    @Override
    public void onLoadingFailed(final String s, final View view, final FailReason failReason) {
    }
    
    @Override
    public void onLoadingStarted(final String s, final View view) {
    }
}
