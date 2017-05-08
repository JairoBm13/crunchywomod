// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.util;

import java.util.concurrent.CancellationException;
import android.os.Looper;
import android.text.TextUtils;
import android.os.SystemClock;
import com.google.android.exoplayer.upstream.UriDataSource;
import java.io.IOException;
import android.os.Handler;
import com.google.android.exoplayer.upstream.UriLoadable;
import com.google.android.exoplayer.upstream.Loader;

public class ManifestFetcher<T> implements Callback
{
    private long currentLoadStartTimestamp;
    private UriLoadable<T> currentLoadable;
    private final Handler eventHandler;
    private final EventListener eventListener;
    private IOException loadException;
    private int loadExceptionCount;
    private long loadExceptionTimestamp;
    private volatile T manifest;
    private volatile long manifestLoadCompleteTimestamp;
    private volatile long manifestLoadStartTimestamp;
    volatile String manifestUri;
    private final UriLoadable.Parser<T> parser;
    private final UriDataSource uriDataSource;
    
    public ManifestFetcher(final String s, final UriDataSource uriDataSource, final UriLoadable.Parser<T> parser) {
        this(s, uriDataSource, parser, null, null);
    }
    
    public ManifestFetcher(final String manifestUri, final UriDataSource uriDataSource, final UriLoadable.Parser<T> parser, final Handler eventHandler, final EventListener eventListener) {
        this.parser = parser;
        this.manifestUri = manifestUri;
        this.uriDataSource = uriDataSource;
        this.eventHandler = eventHandler;
        this.eventListener = eventListener;
    }
    
    private void notifyManifestError(final IOException ex) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    ManifestFetcher.this.eventListener.onManifestError(ex);
                }
            });
        }
    }
    
    private void notifyManifestRefreshed() {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    ManifestFetcher.this.eventListener.onManifestRefreshed();
                }
            });
        }
    }
    
    @Override
    public void onLoadCanceled(final Loadable loadable) {
    }
    
    @Override
    public void onLoadCompleted(final Loadable loadable) {
        if (this.currentLoadable != loadable) {
            return;
        }
        this.manifest = this.currentLoadable.getResult();
        this.manifestLoadStartTimestamp = this.currentLoadStartTimestamp;
        this.manifestLoadCompleteTimestamp = SystemClock.elapsedRealtime();
        this.loadExceptionCount = 0;
        this.loadException = null;
        if (this.manifest instanceof RedirectingManifest) {
            final String nextManifestUri = ((RedirectingManifest)this.manifest).getNextManifestUri();
            if (!TextUtils.isEmpty((CharSequence)nextManifestUri)) {
                this.manifestUri = nextManifestUri;
            }
        }
        this.notifyManifestRefreshed();
    }
    
    @Override
    public void onLoadError(final Loadable loadable, final IOException ex) {
        if (this.currentLoadable != loadable) {
            return;
        }
        ++this.loadExceptionCount;
        this.loadExceptionTimestamp = SystemClock.elapsedRealtime();
        this.notifyManifestError(this.loadException = new IOException(ex));
    }
    
    void onSingleFetchCompleted(final T manifest, final long manifestLoadStartTimestamp) {
        this.manifest = manifest;
        this.manifestLoadStartTimestamp = manifestLoadStartTimestamp;
        this.manifestLoadCompleteTimestamp = SystemClock.elapsedRealtime();
    }
    
    public void singleLoad(final Looper looper, final ManifestCallback<T> manifestCallback) {
        new SingleFetchHelper(new UriLoadable<T>(this.manifestUri, this.uriDataSource, this.parser), looper, manifestCallback).startLoading();
    }
    
    public interface EventListener
    {
        void onManifestError(final IOException p0);
        
        void onManifestRefreshed();
    }
    
    public interface ManifestCallback<T>
    {
        void onSingleManifest(final T p0);
        
        void onSingleManifestError(final IOException p0);
    }
    
    public interface RedirectingManifest
    {
        String getNextManifestUri();
    }
    
    private class SingleFetchHelper implements Callback
    {
        private final Looper callbackLooper;
        private long loadStartTimestamp;
        private final UriLoadable<T> singleUseLoadable;
        private final Loader singleUseLoader;
        private final ManifestCallback<T> wrappedCallback;
        
        public SingleFetchHelper(final UriLoadable<T> singleUseLoadable, final Looper callbackLooper, final ManifestCallback<T> wrappedCallback) {
            this.singleUseLoadable = singleUseLoadable;
            this.callbackLooper = callbackLooper;
            this.wrappedCallback = wrappedCallback;
            this.singleUseLoader = new Loader("manifestLoader:single");
        }
        
        private void releaseLoader() {
            this.singleUseLoader.release();
        }
        
        @Override
        public void onLoadCanceled(final Loadable loadable) {
            try {
                this.wrappedCallback.onSingleManifestError(new IOException("Load cancelled", new CancellationException()));
            }
            finally {
                this.releaseLoader();
            }
        }
        
        @Override
        public void onLoadCompleted(final Loadable loadable) {
            try {
                final T result = this.singleUseLoadable.getResult();
                ManifestFetcher.this.onSingleFetchCompleted(result, this.loadStartTimestamp);
                this.wrappedCallback.onSingleManifest(result);
            }
            finally {
                this.releaseLoader();
            }
        }
        
        @Override
        public void onLoadError(final Loadable loadable, final IOException ex) {
            try {
                this.wrappedCallback.onSingleManifestError(ex);
            }
            finally {
                this.releaseLoader();
            }
        }
        
        public void startLoading() {
            this.loadStartTimestamp = SystemClock.elapsedRealtime();
            this.singleUseLoader.startLoading(this.callbackLooper, (Loader.Loadable)this.singleUseLoadable, (Loader.Callback)this);
        }
    }
}
