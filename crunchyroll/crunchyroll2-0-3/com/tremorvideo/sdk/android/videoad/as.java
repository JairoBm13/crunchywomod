// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.webkit.GeolocationPermissions$Callback;
import android.widget.FrameLayout$LayoutParams;
import android.widget.ProgressBar;
import android.media.MediaPlayer$OnCompletionListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer$OnPreparedListener;
import android.view.ViewGroup;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.webkit.WebChromeClient;
import android.content.Context;
import android.widget.VideoView;
import android.webkit.WebView;
import android.webkit.WebChromeClient$CustomViewCallback;
import android.widget.FrameLayout;
import android.view.View;

public class as extends aq
{
    private int c;
    private View d;
    private FrameLayout e;
    private WebChromeClient$CustomViewCallback f;
    private WebView g;
    private VideoView h;
    
    as(final Context context) {
        super(context);
        this.c = 43981;
        (this.g = new WebView(context)).setVerticalScrollBarEnabled(false);
        this.g.setHorizontalScrollBarEnabled(false);
        this.g.setWebViewClient(this.h());
        this.g.getSettings().setJavaScriptEnabled(true);
        this.g.getSettings().setSaveFormData(false);
        this.g.getSettings().setSavePassword(false);
        this.g.getSettings().setGeolocationDatabasePath(context.getFilesDir().getPath());
        this.g.getSettings().setGeolocationEnabled(true);
        this.g.getSettings().setAppCacheEnabled(true);
        this.g.getSettings().setDatabaseEnabled(true);
        ac.e(this.g.getSettings().getUserAgentString());
        this.g.getSettings().setDatabaseEnabled(true);
        this.g.setWebChromeClient((WebChromeClient)new a());
        this.g.getSettings().setDomStorageEnabled(true);
        this.g.getSettings().setBuiltInZoomControls(true);
        (this.e = new FrameLayout(context)).setVisibility(8);
        this.f();
        System.gc();
        this.addView((View)this.g, (ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
        this.addView((View)this.e, (ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
    }
    
    private VideoView a(final View view) {
        if (view instanceof VideoView) {
            return (VideoView)view;
        }
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)view;
            for (int childCount = viewGroup.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = viewGroup.getChildAt(i);
                if (child instanceof VideoView) {
                    return (VideoView)child;
                }
            }
        }
        return null;
    }
    
    private void a(final VideoView h) {
        (this.h = h).setOnPreparedListener((MediaPlayer$OnPreparedListener)new MediaPlayer$OnPreparedListener() {
            public void onPrepared(final MediaPlayer mediaPlayer) {
                ac.e("WebView - Video Available");
                as.this.k();
                mediaPlayer.start();
            }
        });
        h.setOnCompletionListener((MediaPlayer$OnCompletionListener)new MediaPlayer$OnCompletionListener() {
            public void onCompletion(final MediaPlayer mediaPlayer) {
                ac.e("WebView - Video Complete");
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                as.this.j();
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
            }
        });
    }
    
    private void i() {
        if (this.h != null && !this.h.isPlaying() && this.e.findViewById(this.c) == null) {
            final ProgressBar progressBar = new ProgressBar(this.h.getContext());
            progressBar.setIndeterminate(true);
            progressBar.setId(this.c);
            final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(-2, -2);
            frameLayout$LayoutParams.gravity = 17;
            this.e.addView((View)progressBar, (ViewGroup$LayoutParams)frameLayout$LayoutParams);
        }
    }
    
    private void j() {
        synchronized (this) {
            if (this.d != null) {
                this.k();
                this.d.setVisibility(8);
                this.h = null;
                this.e.removeView(this.d);
                this.d = null;
                this.e.setVisibility(8);
                this.f.onCustomViewHidden();
                this.g.setVisibility(0);
            }
        }
    }
    
    private void k() {
        while (true) {
            final View viewById = this.e.findViewById(this.c);
            if (viewById == null) {
                break;
            }
            this.e.removeView(viewById);
        }
    }
    
    @Override
    public void a(final d a) {
        this.a = a;
    }
    
    @Override
    public void a(final String s) {
        this.f();
        if (this.g != null) {
            this.g.loadUrl(s);
        }
    }
    
    @Override
    public boolean a() {
        return this.h != null;
    }
    
    @Override
    public void b() {
        if (this.h != null) {
            if (this.g != null && this.a != null) {
                this.g.loadUrl("javascript:onVideoExit();");
            }
            ac.e("WebView - skipping video");
            this.j();
        }
    }
    
    @Override
    public void c() {
        if (this.h != null) {
            this.h.stopPlayback();
        }
        this.j();
    }
    
    @Override
    public void d() {
        if (this.h == null) {
            if (this.g != null && this.a != null) {
                this.g.loadUrl("javascript:onResume();");
            }
        }
        else {
            this.h.start();
        }
        this.i();
    }
    
    @Override
    public void e() {
        if (this.h == null) {
            if (this.g != null && this.a != null) {
                this.g.loadUrl("javascript:onPause();");
            }
            return;
        }
        this.h.pause();
    }
    
    @Override
    public void f() {
        if (this.g != null) {
            this.g.clearCache(false);
            this.g.destroyDrawingCache();
        }
    }
    
    @Override
    public void g() {
        if (this.g != null) {
            this.removeView((View)this.g);
            this.f();
            this.g.destroy();
            this.g = null;
        }
    }
    
    public void setBackgroundColor(final int n) {
        super.setBackgroundColor(n);
        this.g.setBackgroundColor(n);
    }
    
    private class a extends WebChromeClient
    {
        public void onGeolocationPermissionsShowPrompt(final String s, final GeolocationPermissions$Callback geolocationPermissions$Callback) {
            geolocationPermissions$Callback.invoke(s, true, false);
        }
        
        public void onHideCustomView() {
            as.this.j();
        }
        
        public void onProgressChanged(final WebView webView, final int n) {
            super.onProgressChanged(webView, n);
            if (n == 100 && as.this.b != null) {
                as.this.b.a();
            }
        }
        
        public void onShowCustomView(final View view, final WebChromeClient$CustomViewCallback webChromeClient$CustomViewCallback) {
            if (as.this.d != null) {
                webChromeClient$CustomViewCallback.onCustomViewHidden();
                return;
            }
            final VideoView a = as.this.a(view);
            if (a != null) {
                as.this.a(a);
            }
            as.this.g.setVisibility(8);
            as.this.e.addView(view, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
            as.this.d = view;
            as.this.f = webChromeClient$CustomViewCallback;
            as.this.e.setVisibility(0);
            as.this.i();
        }
    }
}
