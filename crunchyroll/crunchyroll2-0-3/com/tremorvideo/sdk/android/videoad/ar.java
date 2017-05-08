// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.webkit.GeolocationPermissions$Callback;
import android.webkit.WebSettings;
import android.widget.FrameLayout$LayoutParams;
import android.widget.ProgressBar;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.os.Build$VERSION;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.content.Context;
import android.widget.VideoView;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.view.View;

public class ar extends aq
{
    private int c;
    private View d;
    private FrameLayout e;
    private WebView f;
    private VideoView g;
    
    ar(final Context context) {
        super(context);
        this.c = 43981;
        (this.f = new WebView(context)).setVerticalScrollBarEnabled(false);
        this.f.setHorizontalScrollBarEnabled(false);
        this.f.setWebViewClient((WebViewClient)new aq.a(this));
        this.f.setWebChromeClient((WebChromeClient)new b());
        this.f.getSettings().setJavaScriptEnabled(true);
        this.f.getSettings().setSaveFormData(false);
        this.f.getSettings().setSavePassword(false);
        this.f.getSettings().setGeolocationDatabasePath(context.getFilesDir().getPath());
        this.f.getSettings().setGeolocationEnabled(true);
        this.f.getSettings().setAppCacheEnabled(true);
        this.f.getSettings().setDatabaseEnabled(true);
        if (Integer.parseInt(Build$VERSION.SDK) >= 5) {
            new a(this.f.getSettings());
        }
        (this.e = new FrameLayout(context)).setVisibility(8);
        this.f();
        System.gc();
        this.addView((View)this.f, (ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
        this.addView((View)this.e, (ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
    }
    
    private void i() {
        if (this.g != null && this.e.findViewById(this.c) == null) {
            final ProgressBar progressBar = new ProgressBar(this.g.getContext());
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
                this.g = null;
                this.e.removeView(this.d);
                this.d = null;
                this.e.setVisibility(8);
                this.f.setVisibility(0);
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
    public void a(final String s) {
        this.f();
        this.f.loadUrl(s);
    }
    
    @Override
    public boolean a() {
        return this.g != null;
    }
    
    @Override
    public void b() {
        if (this.g != null) {
            ac.e("WebView - skipping video");
            this.j();
        }
    }
    
    @Override
    public void c() {
    }
    
    @Override
    public void d() {
        this.i();
    }
    
    @Override
    public void e() {
    }
    
    @Override
    public void f() {
        if (this.f != null) {
            this.f.clearCache(false);
            this.f.destroyDrawingCache();
        }
    }
    
    @Override
    public void g() {
        this.f();
        this.f = null;
    }
    
    public void setBackgroundColor(final int n) {
        super.setBackgroundColor(n);
        this.f.setBackgroundColor(n);
    }
    
    class a
    {
        public a(final WebSettings webSettings) {
            webSettings.setDatabaseEnabled(true);
        }
    }
    
    private class b extends WebChromeClient
    {
        public void onGeolocationPermissionsShowPrompt(final String s, final GeolocationPermissions$Callback geolocationPermissions$Callback) {
            geolocationPermissions$Callback.invoke(s, true, false);
        }
        
        public void onProgressChanged(final WebView webView, final int n) {
            super.onProgressChanged(webView, n);
            if (n == 100 && ar.this.b != null) {
                ar.this.b.a();
            }
        }
    }
}
