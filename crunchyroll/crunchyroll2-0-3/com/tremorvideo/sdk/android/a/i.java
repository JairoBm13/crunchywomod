// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.a;

import android.net.Uri;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import java.util.HashMap;
import org.apache.http.client.utils.URLEncodedUtils;
import java.net.URI;
import android.content.Context;
import android.os.Handler;
import android.webkit.WebView;

public class i extends WebView
{
    public b a;
    private a b;
    private c c;
    private boolean d;
    private Handler e;
    private Runnable f;
    
    public i(final Context context, final b a) {
        super(context);
        this.d = false;
        this.b();
        this.a = a;
    }
    
    private boolean a(final URI uri) {
        final String host = uri.getHost();
        final List parse = URLEncodedUtils.parse(uri, "UTF-8");
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        for (final NameValuePair nameValuePair : parse) {
            hashMap.put(nameValuePair.getName(), nameValuePair.getValue());
        }
        final d a = com.tremorvideo.sdk.android.a.f.a(host, hashMap, this);
        if (a == null) {
            return false;
        }
        a.a();
        return true;
    }
    
    private void b() {
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setAllowFileAccess(true);
        this.setWebChromeClient((WebChromeClient)(this.b = new a()));
        this.setWebViewClient((WebViewClient)(this.c = new c()));
        this.e = new Handler();
        this.f = new Runnable() {
            @Override
            public void run() {
                i.this.a.a(false, true);
            }
        };
        this.e.postDelayed(this.f, 8000L);
    }
    
    public void a() {
        if (!this.d) {
            this.d = true;
            super.loadUrl("javascript:validateXML()");
        }
    }
    
    public void a(final Map<String, String> map) {
        if (this.e != null) {
            this.e.removeCallbacks(this.f);
            this.e = null;
            this.f = null;
        }
        if (map.get("valid").equals("true")) {
            this.a.a(true, false);
            return;
        }
        this.a.a(false, false);
    }
    
    public void destroy() {
        if (this.e != null) {
            this.e.removeCallbacks(this.f);
            this.e = null;
            this.f = null;
        }
        this.clearCache(false);
        this.destroyDrawingCache();
    }
    
    private class a extends WebChromeClient
    {
    }
    
    public interface b
    {
        void a(final boolean p0, final boolean p1);
    }
    
    private class c extends WebViewClient
    {
        public void onPageFinished(final WebView webView, final String s) {
            if (webView instanceof i) {
                ((i)webView).a();
            }
        }
        
        public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
            if (Uri.parse(s).getScheme().equals("tremorvideo")) {
                i.this.a(URI.create(s));
                return true;
            }
            return false;
        }
    }
}
