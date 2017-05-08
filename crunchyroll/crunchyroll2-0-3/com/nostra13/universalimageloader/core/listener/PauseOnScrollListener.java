// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.listener;

import android.widget.AbsListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.widget.AbsListView$OnScrollListener;

public class PauseOnScrollListener implements AbsListView$OnScrollListener
{
    private final AbsListView$OnScrollListener externalListener;
    private ImageLoader imageLoader;
    private final boolean pauseOnFling;
    private final boolean pauseOnScroll;
    
    public PauseOnScrollListener(final ImageLoader imageLoader, final boolean b, final boolean b2) {
        this(imageLoader, b, b2, null);
    }
    
    public PauseOnScrollListener(final ImageLoader imageLoader, final boolean pauseOnScroll, final boolean pauseOnFling, final AbsListView$OnScrollListener externalListener) {
        this.imageLoader = imageLoader;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
        this.externalListener = externalListener;
    }
    
    public void onScroll(final AbsListView absListView, final int n, final int n2, final int n3) {
        if (this.externalListener != null) {
            this.externalListener.onScroll(absListView, n, n2, n3);
        }
    }
    
    public void onScrollStateChanged(final AbsListView absListView, final int n) {
        switch (n) {
            case 0: {
                this.imageLoader.resume();
                break;
            }
            case 1: {
                if (this.pauseOnScroll) {
                    this.imageLoader.pause();
                    break;
                }
                break;
            }
            case 2: {
                if (this.pauseOnFling) {
                    this.imageLoader.pause();
                    break;
                }
                break;
            }
        }
        if (this.externalListener != null) {
            this.externalListener.onScrollStateChanged(absListView, n);
        }
    }
}
