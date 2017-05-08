// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.d;

import android.net.Uri;
import android.webkit.ConsoleMessage;
import android.util.Log;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import android.webkit.JavascriptInterface;
import android.app.Activity;
import org.json.JSONObject;
import android.graphics.Paint;
import com.tremorvideo.sdk.android.videoad.ac;
import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.webkit.WebSettings$PluginState;
import android.os.Build$VERSION;
import java.util.Iterator;
import java.util.List;
import org.apache.http.NameValuePair;
import java.util.HashMap;
import org.apache.http.client.utils.URLEncodedUtils;
import java.net.URI;
import android.content.Context;
import android.webkit.WebView;

public class c extends WebView
{
    private com.tremorvideo.sdk.android.d.b a;
    private b b;
    private a c;
    private com.tremorvideo.sdk.android.d.a d;
    private boolean e;
    private Context f;
    
    public c(final Context f, final com.tremorvideo.sdk.android.d.b a) {
        super(f);
        this.f = f;
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
        if (host.equals("adchoices-open")) {
            final String s = hashMap.get("url");
            if (s != null && s.length() > 0) {
                this.a("adchoices-open");
                this.d.a(s);
            }
        }
        else {
            if (host.equals("adchoices-close")) {
                this.a("adchoices-close");
                this.d.e();
                return;
            }
            if (host.equals("adchoices-reset")) {
                this.a("adchoices-reset");
                this.d.g();
            }
        }
    }
    
    @SuppressLint({ "NewApi" })
    private void c() {
        this.setScrollContainer(false);
        this.setBackgroundColor(0);
        this.setHorizontalScrollBarEnabled(false);
        this.setHorizontalScrollbarOverlay(false);
        this.setVerticalScrollBarEnabled(false);
        this.setVerticalScrollbarOverlay(false);
        final WebSettings settings = this.getSettings();
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
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
        this.e = false;
        this.addJavascriptInterface((Object)this, "AndroidDevice");
    }
    
    @SuppressLint({ "NewApi" })
    public void a() {
        this.setBackgroundColor(0);
        if (ac.r() > 10) {
            this.setLayerType(1, (Paint)null);
        }
        this.onSizeChanged(this.getWidth(), this.getHeight(), 0, 0);
        if (this.d != null) {
            this.d.f();
        }
    }
    
    protected void a(final String s) {
        this.b("TMWI.nativeCallComplete('" + s + "');");
    }
    
    protected void b() {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("AndroidVersion", ac.r());
            jsonObject.put("AndroidTargetVersion", ac.s());
            this.b("TMWI.fireEvent('init'," + jsonObject + ");");
        }
        catch (Exception ex) {
            ac.e("Failed to add additional parameters");
        }
    }
    
    protected void b(final String s) {
        if (s != null) {
            super.loadUrl("javascript:" + s);
        }
    }
    
    protected void c(final String s) {
        this.b("TMWI.fireEvent('frameChange'," + s + ");");
    }
    
    @SuppressLint({ "NewApi" })
    public void destroy() {
        this.clearCache(false);
        this.destroyDrawingCache();
    }
    
    @JavascriptInterface
    public void executeSDKCall(final String s) {
        ((Activity)this.f).runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                com.tremorvideo.sdk.android.d.c.this.a(URI.create(s));
            }
        });
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        Label_0201: {
            if (!this.e) {
                break Label_0201;
            }
            final JSONObject jsonObject = new JSONObject();
            while (true) {
                try {
                    final ViewGroup$LayoutParams layoutParams = this.getLayoutParams();
                    if (layoutParams instanceof LinearLayout$LayoutParams) {
                        jsonObject.put("x", ((LinearLayout$LayoutParams)layoutParams).leftMargin);
                        jsonObject.put("y", ((LinearLayout$LayoutParams)layoutParams).topMargin);
                        jsonObject.put("width", layoutParams.width);
                        jsonObject.put("height", layoutParams.height);
                    }
                    if (layoutParams instanceof RelativeLayout$LayoutParams) {
                        jsonObject.put("x", ((RelativeLayout$LayoutParams)layoutParams).leftMargin);
                        jsonObject.put("y", ((RelativeLayout$LayoutParams)layoutParams).topMargin);
                        jsonObject.put("width", ((RelativeLayout$LayoutParams)layoutParams).width);
                        jsonObject.put("height", ((RelativeLayout$LayoutParams)layoutParams).height);
                    }
                    jsonObject.put("AndroidVersion", ac.r());
                    jsonObject.put("AndroidTargetVersion", ac.s());
                    this.c(jsonObject.toString());
                    super.onSizeChanged(n, n2, n3, n4);
                }
                catch (Exception ex) {
                    ac.a(ex);
                    continue;
                }
                break;
            }
        }
    }
    
    private class a extends WebChromeClient
    {
        public void onConsoleMessage(final String s, final int n, final String s2) {
            Log.d("AdChoices", "MSG: " + s + "; Line: " + n + "; Source: " + s2);
        }
        
        public boolean onConsoleMessage(final ConsoleMessage consoleMessage) {
            Log.d("AdChoices", "MSG: " + consoleMessage.message() + "; Line: " + consoleMessage.lineNumber() + "; Source: " + consoleMessage.sourceId());
            return true;
        }
    }
    
    private class b extends WebViewClient
    {
        public void onPageFinished(WebView webView, final String s) {
            Label_0207: {
                if (com.tremorvideo.sdk.android.d.c.this.e) {
                    break Label_0207;
                }
                com.tremorvideo.sdk.android.d.c.this.e = true;
                com.tremorvideo.sdk.android.d.c.this.b();
                webView = (WebView)new JSONObject();
                while (true) {
                    try {
                        final ViewGroup$LayoutParams layoutParams = com.tremorvideo.sdk.android.d.c.this.getLayoutParams();
                        if (layoutParams instanceof LinearLayout$LayoutParams) {
                            ((JSONObject)webView).put("x", ((LinearLayout$LayoutParams)layoutParams).leftMargin);
                            ((JSONObject)webView).put("y", ((LinearLayout$LayoutParams)layoutParams).topMargin);
                            ((JSONObject)webView).put("width", layoutParams.width);
                            ((JSONObject)webView).put("height", layoutParams.height);
                            ((JSONObject)webView).put("AndroidVersion", ac.r());
                            ((JSONObject)webView).put("AndroidTargetVersion", ac.s());
                        }
                        if (layoutParams instanceof RelativeLayout$LayoutParams) {
                            ((JSONObject)webView).put("x", ((RelativeLayout$LayoutParams)layoutParams).leftMargin);
                            ((JSONObject)webView).put("y", ((RelativeLayout$LayoutParams)layoutParams).topMargin);
                            ((JSONObject)webView).put("width", layoutParams.width);
                            ((JSONObject)webView).put("height", layoutParams.height);
                            ((JSONObject)webView).put("AndroidVersion", ac.r());
                            ((JSONObject)webView).put("AndroidTargetVersion", ac.s());
                        }
                        com.tremorvideo.sdk.android.d.c.this.c(((JSONObject)webView).toString());
                        com.tremorvideo.sdk.android.d.c.this.a();
                    }
                    catch (Exception ex) {
                        ac.a(ex);
                        continue;
                    }
                    break;
                }
            }
        }
        
        public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
            super.onReceivedError(webView, n, s, s2);
        }
        
        public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
            if (Uri.parse(s).getScheme().equals("tmwi")) {
                com.tremorvideo.sdk.android.d.c.this.a(URI.create(s));
                return true;
            }
            return false;
        }
    }
}
