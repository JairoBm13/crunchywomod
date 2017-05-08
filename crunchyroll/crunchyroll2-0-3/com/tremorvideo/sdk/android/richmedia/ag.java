// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import android.net.Uri;
import android.app.Activity;
import java.util.Iterator;
import java.util.List;
import com.tremorvideo.sdk.android.videoad.ac;
import org.apache.http.NameValuePair;
import java.util.HashMap;
import org.apache.http.client.utils.URLEncodedUtils;
import java.net.URI;
import com.tremorvideo.sdk.android.videoad.aw;
import android.webkit.WebSettings;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.webkit.WebSettings$PluginState;
import android.os.Build$VERSION;
import android.content.Context;
import android.webkit.WebView;

public class ag extends WebView
{
    public static int a;
    public static int b;
    public static int c;
    ag d;
    String e;
    String f;
    b g;
    af h;
    boolean i;
    boolean j;
    boolean k;
    public int l;
    public int m;
    WebViewEmbedJSCallHandler n;
    private String o;
    
    static {
        ag.a = -1;
        ag.b = 0;
        ag.c = 1;
    }
    
    public ag(final Context context, final af h) {
        super(context);
        this.l = ag.a;
        this.m = 0;
        this.d = this;
        this.h = h;
        this.setBackgroundColor(0);
        this.l = ag.a;
        final WebSettings settings = this.getSettings();
        settings.setSupportZoom(false);
        settings.setJavaScriptEnabled(true);
        if (Build$VERSION.SDK_INT >= 8) {
            settings.setPluginState(WebSettings$PluginState.ON);
        }
        settings.setAllowFileAccess(true);
        if (Build$VERSION.SDK_INT >= 16) {
            settings.setAllowFileAccessFromFileURLs(true);
        }
        this.addJavascriptInterface((Object)(this.n = new WebViewEmbedJSCallHandler(this)), "Device");
        this.setWebViewClient((WebViewClient)(this.g = new b()));
        this.setWebChromeClient((WebChromeClient)new a());
    }
    
    public void a() {
        if (this.i && this.j && this.k) {
            this.g.a();
        }
    }
    
    public void a(final int m) {
        this.m = m;
    }
    
    public void a(final aw.b b) {
        this.h.a(b);
    }
    
    public void a(final String f, final String e) {
        this.h.l();
        this.i = false;
        this.j = false;
        this.k = false;
        this.e = e;
        this.f = f;
        super.loadUrl("file://" + f + "index.html");
    }
    
    public void a(final URI uri) {
        final String host = uri.getHost();
        final List parse = URLEncodedUtils.parse(uri, "UTF-8");
        final HashMap<Object, String> hashMap = new HashMap<Object, String>();
        for (final NameValuePair nameValuePair : parse) {
            hashMap.put(nameValuePair.getName(), nameValuePair.getValue());
        }
        ac.e("Trying Command: " + host + " / " + (String)hashMap.get("event_type"));
        if (host.equals("trigger-event")) {
            final String s = hashMap.get("event_type");
            if (s.equals("video-ready")) {
                this.j = true;
            }
            else if (s.equals("video-start")) {
                this.k = true;
                this.d.a(aw.b.ag);
            }
            else if (s.equals("video-playing")) {
                this.l = ag.b;
            }
            else if (s.equals("video-paused") && this.l == ag.b) {
                this.c();
            }
            this.d.a(aw.b.a(s));
        }
        else if (host.equals("handle-url")) {
            this.o = (String)hashMap.get("url");
        }
        else if (host.equals("trigger-error")) {
            this.d.b((String)hashMap.get("error_code"), (String)hashMap.get("description"));
        }
        else {
            ac.e("Unknown native call in Embed JS player: " + host);
        }
        this.g.a(host);
    }
    
    public void b() {
        if (this.i && this.j && this.k) {
            this.l = ag.c;
            this.g.b();
        }
    }
    
    public void b(final String s, final String s2) {
        ac.e("EmbedPlayer Error - " + s + ": " + s2);
        this.h.l();
        this.g.a("trigger-error");
    }
    
    public void c() {
        if (this.i && this.j && this.k) {
            this.l = ag.b;
            this.g.c();
        }
    }
    
    public void d() {
        if (this.i && this.j) {
            ((Activity)this.getContext()).runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    ag.this.loadUrl("javascript: tremorvideo.EmbedVideoPlayer.getCurrentTime();");
                }
            });
        }
    }
    
    public void e() {
        this.k = true;
        this.d.a(aw.b.ah);
        this.d.a(aw.b.ag);
    }
    
    public int getProgress() {
        if (this.m > 0) {
            return this.m;
        }
        return 0;
    }
    
    private class a extends WebChromeClient
    {
    }
    
    private class b extends WebViewClient
    {
        public void a() {
            ag.this.d.loadUrl("javascript: tremorvideo.EmbedVideoPlayer.stopVideo();");
        }
        
        protected void a(final String s) {
            ag.this.d.loadUrl("javascript: tremorcore.nativeCallComplete('" + s + "');");
        }
        
        public void b() {
            ag.this.d.loadUrl("javascript: tremorvideo.EmbedVideoPlayer.pauseVideo();");
        }
        
        public void c() {
            ag.this.d.loadUrl("javascript: tremorvideo.EmbedVideoPlayer.resumeVideo();");
        }
        
        public void onPageFinished(final WebView webView, final String s) {
            if (!ag.this.d.i) {
                ag.this.d.j = false;
                ag.this.d.loadUrl("javascript: tremorvideo.EmbedVideoPlayer.setIsAndroid()");
                ag.this.d.loadUrl("javascript: tremorvideo.EmbedVideoPlayer.initVideo('" + ag.this.d.e + "');");
                ag.this.d.i = true;
            }
        }
        
        public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
            ag.this.h.l();
        }
        
        public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
            if (Uri.parse(s).getScheme().equals("tremorvideo")) {
                ag.this.a(URI.create(s));
                return true;
            }
            if (ag.this.o != null && s.startsWith(ag.this.o)) {
                ag.this.h.a(s);
                return true;
            }
            return false;
        }
    }
}
