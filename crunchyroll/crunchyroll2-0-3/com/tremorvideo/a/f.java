// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.a;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.view.KeyEvent;
import android.webkit.WebViewClient;
import android.content.Context;

public class f extends d
{
    protected f(final Context context, final String s, final b.a a) {
        super(context, s, a);
    }
    
    @Override
    protected WebViewClient b() {
        return new a();
    }
    
    public boolean onKeyLongPress(final int n, final KeyEvent keyEvent) {
        return n == 84 || super.onKeyLongPress(n, keyEvent);
    }
    
    private class a extends d.a
    {
        public void onReceivedSslError(final WebView webView, final SslErrorHandler sslErrorHandler, final SslError sslError) {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
            f.this.d.a(new com.tremorvideo.a.a("An SSL Error Occured.", -1, ""));
            super.a();
            webView.clearView();
            webView.loadUrl("about:blank");
        }
    }
}
