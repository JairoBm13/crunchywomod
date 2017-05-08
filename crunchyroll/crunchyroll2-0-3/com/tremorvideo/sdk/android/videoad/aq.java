// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.graphics.Bitmap;
import com.tremorvideo.sdk.android.logger.TestAppLogger;
import android.webkit.WebView;
import android.os.Build$VERSION;
import android.webkit.WebViewClient;
import android.content.Context;
import android.widget.RelativeLayout;

public abstract class aq extends RelativeLayout
{
    protected d a;
    protected c b;
    
    public aq(final Context context) {
        super(context);
    }
    
    public void a(final d a) {
        this.a = a;
    }
    
    public abstract void a(final String p0);
    
    public abstract boolean a();
    
    public abstract void b();
    
    public abstract void c();
    
    public abstract void d();
    
    public abstract void e();
    
    public abstract void f();
    
    public abstract void g();
    
    protected WebViewClient h() {
        if (Integer.parseInt(Build$VERSION.SDK) >= 8) {
            return new b();
        }
        return new a();
    }
    
    protected class a extends WebViewClient
    {
        public void onPageFinished(final WebView webView, final String s) {
            super.onPageFinished(webView, s);
            try {
                if (ac.r) {
                    TestAppLogger.getInstance().logClickout("Page Loaded", "webpage_url:" + s, "info");
                }
            }
            catch (Exception ex) {
                ac.e("Exception clickout onPageFinished " + ex);
            }
        }
        
        public void onPageStarted(final WebView webView, final String s, final Bitmap bitmap) {
            super.onPageStarted(webView, s, bitmap);
        }
        
        public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
            super.onReceivedError(webView, n, s, s2);
            while (true) {
                try {
                    if (ac.r) {
                        TestAppLogger.getInstance().logClickout("error_code=" + n, "webpage_url:" + s2, "fail");
                    }
                    ac.e("HTML5 error: " + s2);
                    ac.e("HTML5 error: " + s);
                    if (aq.this.b != null) {
                        aq.this.b.a(s2 + " - " + s);
                    }
                }
                catch (Exception ex) {
                    ac.e("Exception clickout onReceivedError " + ex);
                    continue;
                }
                break;
            }
        }
        
        public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
            if (aq.this.a != null && s.startsWith("event://tremorvideo.com/")) {
                aq.this.a.a(s.replace("event://tremorvideo.com/", ""));
                return true;
            }
            return false;
        }
    }
    
    protected class b extends a
    {
        public void onReceivedSslError(final WebView webView, final SslErrorHandler sslErrorHandler, final SslError sslError) {
            ac.e("SSL Error");
            sslErrorHandler.proceed();
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
        }
    }
    
    public interface c
    {
        void a();
        
        void a(final String p0);
    }
    
    public interface d
    {
        boolean a(final String p0);
    }
}
