// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.a;

import android.net.Uri;
import android.webkit.WebSettings;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.os.Build$VERSION;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import java.util.HashMap;
import org.apache.http.client.utils.URLEncodedUtils;
import java.net.URI;
import android.content.Context;
import android.webkit.WebView;

public class j extends WebView
{
    private com.tremorvideo.sdk.android.a.b a;
    private b b;
    private a c;
    private com.tremorvideo.sdk.android.a.a d;
    private boolean e;
    
    public j(final Context context, final com.tremorvideo.sdk.android.a.b a) {
        super(context);
        this.a = a;
        this.c();
    }
    
    private boolean a(final URI uri) {
        final String host = uri.getHost();
        final List parse = URLEncodedUtils.parse(uri, "UTF-8");
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        for (final NameValuePair nameValuePair : parse) {
            hashMap.put(nameValuePair.getName(), nameValuePair.getValue());
        }
        final d a = f.a(host, hashMap, this);
        if (a == null) {
            this.a(host);
            return false;
        }
        a.a();
        this.a(host);
        return true;
    }
    
    private void c() {
        this.setScrollContainer(false);
        this.setBackgroundColor(0);
        this.setHorizontalScrollBarEnabled(false);
        this.setHorizontalScrollbarOverlay(false);
        this.setVerticalScrollBarEnabled(false);
        this.setVerticalScrollbarOverlay(false);
        final WebSettings settings = this.getSettings();
        settings.setSupportZoom(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        if (Build$VERSION.SDK_INT >= 16) {
            settings.setAllowFileAccessFromFileURLs(true);
        }
        this.d = this.a.k();
        this.setWebViewClient((WebViewClient)(this.b = new b()));
        this.setWebChromeClient((WebChromeClient)(this.c = new a()));
        this.e = false;
    }
    
    protected void a() {
        this.b("tremorcore.fireEvent('viewableChange','true');");
    }
    
    protected void a(final String s) {
        this.b("tremorcore.nativeCallComplete('" + s + "');");
    }
    
    public void a(final Map<String, String> map) {
        this.d.a(map);
    }
    
    public void b() {
        this.d.e();
    }
    
    protected void b(final String s) {
        if (s != null) {
            super.loadUrl("javascript:" + s);
        }
    }
    
    public void destroy() {
        this.clearCache(false);
        this.destroyDrawingCache();
    }
    
    private class a extends WebChromeClient
    {
    }
    
    private class b extends WebViewClient
    {
        public void onPageFinished(final WebView webView, final String s) {
            if (!j.this.e) {
                j.this.a();
            }
        }
        
        public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
            super.onReceivedError(webView, n, s, s2);
        }
        
        public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
            if (Uri.parse(s).getScheme().equals("tremorvideo")) {
                j.this.a(URI.create(s));
                return true;
            }
            return false;
        }
    }
}
