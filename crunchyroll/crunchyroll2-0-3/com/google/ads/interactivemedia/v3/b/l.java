// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import android.webkit.WebViewClient;
import android.webkit.WebView$WebViewTransport;
import android.os.Message;
import android.webkit.WebChromeClient;
import com.google.ads.interactivemedia.v3.b.a.c;
import android.content.Context;
import android.webkit.WebView;

public class l extends WebView
{
    public l(final Context context, final s s, final c c) {
        super(context);
        this.getSettings().setSupportMultipleWindows(true);
        this.setWebChromeClient((WebChromeClient)new WebChromeClient() {
            public boolean onCreateWindow(final WebView webView, final boolean b, final boolean b2, final Message message) {
                final WebView$WebViewTransport webView$WebViewTransport = (WebView$WebViewTransport)message.obj;
                webView$WebViewTransport.setWebView(new WebView(context));
                webView$WebViewTransport.getWebView().setWebViewClient((WebViewClient)new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                        s.b(s);
                        return true;
                    }
                });
                message.sendToTarget();
                return true;
            }
        });
        if (c.type == c.a.Html) {
            this.loadData(c.src, "text/html", (String)null);
            return;
        }
        if (c.type == c.a.IFrame) {
            this.loadUrl(c.src);
            return;
        }
        throw new IllegalArgumentException("Companion type " + c.type + " is not valid for a CompanionWebView");
    }
}
