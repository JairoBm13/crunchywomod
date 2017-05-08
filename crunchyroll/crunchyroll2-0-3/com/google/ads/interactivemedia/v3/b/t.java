// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import android.util.Log;
import android.webkit.ValueCallback;
import android.os.Build$VERSION;
import android.graphics.Bitmap;
import android.webkit.WebViewClient;
import android.content.Context;
import android.webkit.WebView;

public class t
{
    private final a a;
    private final WebView b;
    
    public t(final Context context, final a a) {
        this(new WebView(context), a);
    }
    
    public t(final WebView b, final a a) {
        this.a = a;
        (this.b = b).setBackgroundColor(0);
        b.getSettings().setJavaScriptEnabled(true);
        b.setWebViewClient((WebViewClient)new WebViewClient() {
            public void onPageFinished(final WebView webView, final String s) {
                t.c("Finished " + s);
            }
            
            public void onPageStarted(final WebView webView, final String s, final Bitmap bitmap) {
                t.c("Started " + s);
            }
            
            public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
                t.c("Error: " + n + " " + s + " " + s2);
            }
            
            public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                if (!s.startsWith("gmsg://")) {
                    return false;
                }
                t.this.b(s);
                return true;
            }
        });
    }
    
    static final void c(final String s) {
    }
    
    public WebView a() {
        return this.b;
    }
    
    public void a(final r r) {
        final String e = r.e();
        c("Sending javascript msg: " + r + " as URL " + e);
        if (Build$VERSION.SDK_INT >= 19) {
            this.b.evaluateJavascript(e, (ValueCallback)null);
            return;
        }
        this.b.loadUrl(e);
    }
    
    public void a(final String s) {
        this.b.loadUrl(s);
    }
    
    protected void b(final String s) {
        try {
            final r a = r.a(s);
            c("Received msg: " + a + " from URL " + s);
            this.a.a(a);
        }
        catch (IllegalArgumentException ex2) {
            Log.w("IMASDK", "Invalid internal message, ignoring. Please make sure the Google IMA SDK library is up to date. Message: " + s);
        }
        catch (Exception ex) {
            Log.e("IMASDK", "An internal error occured parsing message from javascript.  Message to be parsed: " + s, (Throwable)ex);
        }
    }
    
    public interface a
    {
        void a(final r p0);
    }
}
