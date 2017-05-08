// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import android.webkit.ValueCallback;
import android.webkit.ConsoleMessage;
import android.graphics.Bitmap;
import android.webkit.JavascriptInterface;
import com.tremorvideo.sdk.android.videoad.ac;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.content.Context;
import android.app.Activity;
import android.webkit.WebView;

public class ScriptInterpreter
{
    WebView a;
    m b;
    com.tremorvideo.sdk.android.richmedia.a c;
    private Activity d;
    public boolean scriptLoaded;
    
    public ScriptInterpreter(final Context context) {
        this.d = (Activity)context;
        this.a = new WebView(context);
        this.a.getSettings().setJavaScriptEnabled(true);
        this.a.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.a.addJavascriptInterface((Object)this, "Device");
        this.a.setWebViewClient((WebViewClient)new b());
        this.a.setWebChromeClient((WebChromeClient)new a());
    }
    
    public void callFunction(final String s) {
        if (s != "" && s != null) {
            ac.e("Calling Function: " + s);
            this.a.loadUrl("javascript:" + s + "();");
        }
    }
    
    public void destroy() {
        this.d.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                ScriptInterpreter.this.a.destroy();
                ScriptInterpreter.this.a = null;
            }
        });
    }
    
    @JavascriptInterface
    public void getValue(String s, final String s2) {
        if (s.equals("geo.dma")) {
            s = this.c.a.e;
            s = "javascript:callCallback(" + s2 + ",'" + s + "');";
        }
        else if (s.equals("geo.state")) {
            s = this.c.a.c;
            s = "javascript:callCallback(" + s2 + ",'" + s + "');";
        }
        else if (s.equals("geo.city")) {
            s = this.c.a.a;
            s = "javascript:callCallback(" + s2 + ",'" + s + "');";
        }
        else if (s.equals("geo.country")) {
            s = this.c.a.b;
            s = "javascript:callCallback(" + s2 + ",'" + s + "');";
        }
        else if (s.equals("geo.zip")) {
            s = this.c.a.d;
            s = "javascript:callCallback(" + s2 + ",'" + s + "');";
        }
        else {
            if ((s = ac.h().a(s)) == null) {
                s = "null";
            }
            s = "javascript:callCallback(" + s2 + ",'" + s + "');";
        }
        if (ac.r() < 19) {
            this.a.loadUrl(s);
            return;
        }
        this.d.runOnUiThread((Runnable)new c(s));
    }
    
    public WebView getView() {
        return this.a;
    }
    
    public void load(final m b, final com.tremorvideo.sdk.android.richmedia.a c) {
        this.scriptLoaded = false;
        this.a.loadDataWithBaseURL((String)null, "<html><head><script type=\"text/javascript\">\n" + c.h() + "</script></head></html>", "text/html", "utf-8", (String)null);
        this.c = c;
        this.b = b;
    }
    
    @JavascriptInterface
    public void onError(final String s) {
        ac.e(s);
    }
    
    @JavascriptInterface
    public void playVideo(final int n, final int n2) {
        this.b.a(n, n2);
    }
    
    @JavascriptInterface
    public void setImage(final int n, final int n2) {
        final o b = this.b.b();
        final q a = b.a(n);
        if (a instanceof w) {
            final w w = (w)a;
            if (w != null) {
                final Bitmap a2 = b.g().r().a(n2);
                if (a2 != null) {
                    w.a(a2);
                }
            }
        }
    }
    
    @JavascriptInterface
    public void setOverrideScene(final int n, final int n2) {
        final o b = this.b.b();
        final q a = b.a(n);
        if (a instanceof x) {
            final x x = (x)a;
            if (x != null) {
                x.a(b.g().b(n2));
            }
        }
    }
    
    @JavascriptInterface
    public void setScene(final int n) {
        final o b = this.c.b(n);
        if (b != null) {
            this.b.a(b, false);
        }
    }
    
    @JavascriptInterface
    public void setText(final int n, final String s) {
        final q a = this.b.b().a(n);
        if (a instanceof ab) {
            final ab ab = (ab)a;
            if (ab != null) {
                ab.a(s);
            }
        }
    }
    
    @JavascriptInterface
    public void setValue(final String s, final String s2) {
        ac.h().a(s, s2);
    }
    
    @JavascriptInterface
    public void setVisibility(final int n, final int n2) {
        final q a = this.b.b().a(n);
        if (a != null) {
            a.a(n2);
        }
    }
    
    @JavascriptInterface
    public void startEvent(final String s) {
        this.b.b(String.valueOf(ae.a(s)));
    }
    
    private class a extends WebChromeClient
    {
        public void onConsoleMessage(final String s, final int n, final String s2) {
        }
        
        public boolean onConsoleMessage(final ConsoleMessage consoleMessage) {
            return false;
        }
    }
    
    private class b extends WebViewClient
    {
        public void onPageFinished(final WebView webView, final String s) {
            ScriptInterpreter.this.scriptLoaded = true;
            ScriptInterpreter.this.b.n();
        }
    }
    
    public class c implements Runnable
    {
        String a;
        
        public c(final String a) {
            this.a = a;
        }
        
        @Override
        public void run() {
            ScriptInterpreter.this.a.evaluateJavascript(this.a, (ValueCallback)null);
        }
    }
}
