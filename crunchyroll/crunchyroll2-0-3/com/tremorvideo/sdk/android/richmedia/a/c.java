// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia.a;

import android.net.Uri;
import android.webkit.ConsoleMessage;
import android.util.Log;
import android.graphics.Paint;
import android.webkit.WebSettings;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.webkit.WebSettings$PluginState;
import android.os.Build$VERSION;
import java.util.Iterator;
import java.util.List;
import com.tremorvideo.sdk.android.videoad.ac;
import org.apache.http.message.BasicNameValuePair;
import java.util.Map;
import android.app.Activity;
import org.apache.http.NameValuePair;
import java.util.HashMap;
import org.apache.http.client.utils.URLEncodedUtils;
import java.net.URI;
import android.content.Context;
import org.json.JSONObject;
import java.util.ArrayList;
import android.webkit.WebView;

public class c extends WebView implements j
{
    private com.tremorvideo.sdk.android.richmedia.a.b a;
    private b b;
    private a c;
    private com.tremorvideo.sdk.android.richmedia.a.a d;
    private ArrayList<g> e;
    private boolean f;
    private JSONObject g;
    
    public c(final Context context, final com.tremorvideo.sdk.android.richmedia.a.b a) {
        super(context);
        this.a = a;
        this.c();
    }
    
    private void a(final URI uri) {
        final String host = uri.getHost();
        final List parse = URLEncodedUtils.parse(uri, "UTF-8");
        final HashMap<Object, String> hashMap = new HashMap<Object, String>();
        for (final NameValuePair nameValuePair : parse) {
            hashMap.put(nameValuePair.getName(), nameValuePair.getValue());
        }
        if (host.equals("url-request")) {
            ((Activity)this.getContext()).runOnUiThread((Runnable)new c((Map<String, String>)hashMap, this));
            this.b("url-request");
            return;
        }
        if (host.equals("fire-tracking")) {
            while (true) {
                String s = null;
                ArrayList<NameValuePair> list = null;
                Label_0257: {
                    try {
                        s = hashMap.get("event_tag");
                        if (s != null) {
                            list = new ArrayList<NameValuePair>();
                            for (final Map.Entry<Object, Object> entry : hashMap.entrySet()) {
                                if (!entry.getKey().equals("event_tag")) {
                                    list.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
                                }
                            }
                            break Label_0257;
                        }
                    }
                    catch (Exception ex) {
                        ac.e("firetrackingEvent Exception:" + ex);
                    }
                    this.b("fire-tracking");
                    return;
                }
                this.d.a(s, list);
                continue;
            }
        }
        if (host.equals("trigger-close")) {
            this.d.e();
            this.b("trigger-close");
            return;
        }
        if (host.equals("trigger-error")) {
            ac.e("EmbedPlayer Error - " + (String)hashMap.get("error_code") + ": " + (String)hashMap.get("description"));
            this.b("trigger-error");
            return;
        }
        if (host.equals("cancel-request")) {
            if (this.e != null) {
                for (final g g : this.e) {
                    g.cancel(true);
                    g.a();
                }
            }
            this.b("cancel-request");
            return;
        }
        this.b(host);
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
        if (Build$VERSION.SDK_INT >= 8) {
            settings.setPluginState(WebSettings$PluginState.ON);
        }
        settings.setAllowFileAccess(true);
        if (Build$VERSION.SDK_INT >= 16) {
            settings.setAllowFileAccessFromFileURLs(true);
        }
        this.d = this.a.k();
        this.setWebViewClient((WebViewClient)(this.b = new b()));
        this.setWebChromeClient((WebChromeClient)(this.c = new a()));
        this.f = false;
        this.e = new ArrayList<g>();
    }
    
    protected void a() {
        this.c("TMWI.fireEvent('viewableChange','true');");
    }
    
    public void a(final g g) {
        if (this.e != null && this.e.contains(g)) {
            this.e.remove(g);
        }
    }
    
    public void a(final String s) {
        if (s == null) {
            return;
        }
        try {
            this.g = new JSONObject(s);
        }
        catch (Exception ex) {}
    }
    
    public void b() {
        this.setBackgroundColor(0);
        if (ac.r() > 10) {
            this.setLayerType(1, (Paint)null);
        }
        this.f = true;
    }
    
    protected void b(final String s) {
        this.c("TMWI.nativeCallComplete('" + s + "');");
    }
    
    protected void c(final String s) {
        if (s != null) {
            super.loadUrl("javascript:" + s);
        }
    }
    
    public void destroy() {
        final Iterator<g> iterator = this.e.iterator();
        while (iterator.hasNext()) {
            iterator.next().cancel(true);
        }
        this.e.clear();
        this.e = null;
        this.clearCache(false);
        this.destroyDrawingCache();
    }
    
    private class a extends WebChromeClient
    {
        public void onConsoleMessage(final String s, final int n, final String s2) {
            Log.d("ClickoutWebView", "MSG: " + s + "; Line: " + n + "; Source: " + s2);
        }
        
        public boolean onConsoleMessage(final ConsoleMessage consoleMessage) {
            Log.d("ClickoutWebView", "MSG: " + consoleMessage.message() + "; Line: " + consoleMessage.lineNumber() + "; Source: " + consoleMessage.sourceId());
            return true;
        }
    }
    
    private class b extends WebViewClient
    {
        public void onPageFinished(final WebView webView, final String s) {
            if (!com.tremorvideo.sdk.android.richmedia.a.c.this.f) {
                com.tremorvideo.sdk.android.richmedia.a.c.this.b();
                final Iterator<g> iterator = com.tremorvideo.sdk.android.richmedia.a.c.this.e.iterator();
                while (iterator.hasNext()) {
                    iterator.next().cancel(true);
                }
                com.tremorvideo.sdk.android.richmedia.a.c.this.e.clear();
                com.tremorvideo.sdk.android.richmedia.a.c.this.a();
                com.tremorvideo.sdk.android.richmedia.a.c.this.c("TMWI.fireEvent('init'," + com.tremorvideo.sdk.android.richmedia.a.c.this.g + ");");
            }
        }
        
        public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
            super.onReceivedError(webView, n, s, s2);
        }
        
        public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
            if (Uri.parse(s).getScheme().equals("tmwi")) {
                com.tremorvideo.sdk.android.richmedia.a.c.this.a(URI.create(s));
                return true;
            }
            return false;
        }
    }
    
    public class c implements Runnable
    {
        Map<String, String> a;
        com.tremorvideo.sdk.android.richmedia.a.c b;
        
        public c(final Map<String, String> a, final com.tremorvideo.sdk.android.richmedia.a.c b) {
            this.a = a;
            this.b = b;
        }
        
        @Override
        public void run() {
            final g g = new g(this.b);
            com.tremorvideo.sdk.android.richmedia.a.c.this.e.add(g);
            if (this.a.containsKey("data")) {
                g.execute((Object[])new String[] { this.a.get("url"), this.a.get("type"), this.a.get("header"), this.a.get("data") });
                return;
            }
            g.execute((Object[])new String[] { this.a.get("url"), this.a.get("type"), this.a.get("header") });
        }
    }
}
