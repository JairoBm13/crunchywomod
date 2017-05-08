// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.listener;

import com.nostra13.universalimageloader.core.assist.FailReason;
import android.graphics.Bitmap;
import android.view.View;

public interface ImageLoadingListener
{
    void onLoadingCancelled(final String p0, final View p1);
    
    void onLoadingComplete(final String p0, final View p1, final Bitmap p2);
    
    void onLoadingFailed(final String p0, final View p1, final FailReason p2);
    
    void onLoadingStarted(final String p0, final View p1);
}
