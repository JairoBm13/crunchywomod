// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import java.util.HashMap;
import com.tremorvideo.sdk.android.c.a;
import android.os.AsyncTask;
import android.net.Uri;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.webkit.WebViewClient;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface$OnCancelListener;
import android.content.Context;
import android.app.Activity;

public class ca
{
    public static void a(final Activity activity, final aw aw, final e e) {
        a(activity, aw, e, 0, "");
    }
    
    public static void a(final Activity activity, final aw aw, final e e, final int n, final String s) {
        final an a = an.a((Context)activity);
        a.setMessage((CharSequence)"Loading...");
        a.setCancelable(false);
        a.show();
        new d().execute((Object[])new Void[0]);
    }
    
    private static void b(final Activity activity, final aw aw, final String s, final an an, final String s2, final e e, final int n, final String s3) {
        final c c = new c(activity);
        final ai a = ai.a((Context)activity);
        a.requestWindowFeature(1);
        a.getWindow().setFlags(1024, 1024);
        a.setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
            public void onCancel(final DialogInterface dialogInterface) {
                c.a = true;
                e.a(-1, "");
            }
        });
        if (ac.r() < 7) {
            c.setWebViewClient((WebViewClient)new a());
        }
        else {
            c.setWebViewClient((WebViewClient)new b());
        }
        an.dismiss();
        a.setContentView((View)c, new ViewGroup$LayoutParams(-2, -2));
        a.show();
        an.show();
        c.loadUrl(s);
    }
    
    class a extends WebViewClient
    {
        final /* synthetic */ c a;
        final /* synthetic */ an b;
        final /* synthetic */ Dialog c;
        final /* synthetic */ Activity d;
        final /* synthetic */ e e;
        final /* synthetic */ String f;
        final /* synthetic */ int g;
        final /* synthetic */ String h;
        final /* synthetic */ aw i;
        
        public a(final c a, final an b, final Dialog c, final Activity d, final e e, final String f, final int g, final String h, final aw i) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.h = h;
            this.i = i;
        }
        
        public void onPageFinished(final WebView webView, final String s) {
            if (!this.a.a && s.compareTo("https://api.twitter.com/oauth/authorize") != 0) {
                if (this.b.isShowing()) {
                    this.b.dismiss();
                }
                if (!this.c.isShowing()) {
                    this.c.setContentView((View)this.a, new ViewGroup$LayoutParams(-2, -2));
                    this.c.show();
                }
            }
        }
        
        public void onPageStarted(final WebView webView, final String s, final Bitmap bitmap) {
            super.onPageStarted(webView, s, bitmap);
        }
        
        public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
            if (this.a.a) {
                return;
            }
            this.d.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    ca.a.this.b.dismiss();
                    ca.a.this.c.dismiss();
                    ca.a.this.e.a(0, "");
                }
            });
        }
        
        public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
            if (this.a.a || this.a.b) {
                return true;
            }
            if (!s.startsWith("oauth")) {
                return super.shouldOverrideUrlLoading(webView, s);
            }
            this.a.b = true;
            final Uri parse = Uri.parse(s);
            if (parse.getQueryParameter("denied") != null) {
                this.e.a(-1, "");
                this.d.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        ca.a.this.c.dismiss();
                        ca.a.this.b.dismiss();
                    }
                });
                return true;
            }
            this.d.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    ca.a.this.c.dismiss();
                    ca.a.this.b.show();
                }
            });
            new ca.a.a().execute((Object[])new Void[0]);
            return true;
        }
        
        class a extends AsyncTask<Void, Void, Boolean>
        {
            String a;
            final /* synthetic */ Uri b;
            
            a(final Uri b) {
                this.b = b;
                this.a = "";
            }
            
            protected Boolean a(final Void... array) {
                try {
                    final String queryParameter = this.b.getQueryParameter("oauth_verifier");
                    final com.tremorvideo.sdk.android.c.a a = new com.tremorvideo.sdk.android.c.a("zSB18SDCMETDn2BH50xg", "Khb9CLQxkT62an9PUUpvBtRyh1Jluvwi7FGAiJkuJY&", ca.a.this.f, "POST", "https://api.twitter.com/oauth/access_token");
                    a.a("oauth_verifier", queryParameter);
                    a.a();
                    final HashMap<String, String> b = a.b();
                    final String s = b.get("oauth_token");
                    final String string = "Khb9CLQxkT62an9PUUpvBtRyh1Jluvwi7FGAiJkuJY&" + b.get("oauth_token_secret");
                    String string2;
                    if (ca.a.this.g == 0) {
                        string2 = "https://api.twitter.com/1.1/statuses/update.json";
                    }
                    else {
                        string2 = "https://api.twitter.com/1.1/statuses/retweet/" + ca.a.this.h + ".json";
                    }
                    final com.tremorvideo.sdk.android.c.a a2 = new com.tremorvideo.sdk.android.c.a("zSB18SDCMETDn2BH50xg", string, s, "POST", string2);
                    if (ca.a.this.g == 0) {
                        a2.a("status", ca.a.this.i.a("message", "Tweet"));
                    }
                    final boolean a3 = a2.a();
                    ac.e(a2.c());
                    return a3;
                }
                catch (Exception ex) {
                    ac.a(ex);
                    return false;
                }
            }
            
            protected void a(final Boolean b) {
                ca.a.this.b.dismiss();
                if (b) {
                    ca.a.this.e.a(1, "");
                    return;
                }
                ca.a.this.e.a(0, this.a);
            }
        }
    }
    
    class b extends ca.a
    {
        final /* synthetic */ aw j;
        final /* synthetic */ String k;
        final /* synthetic */ int l;
        final /* synthetic */ String m;
        final /* synthetic */ e n;
        final /* synthetic */ Activity o;
        final /* synthetic */ Dialog p;
        final /* synthetic */ an q;
        final /* synthetic */ c r;
        
        b(final aw j, final String k, final int l, final String m, final e n, final Activity o, final Dialog p9, final an q, final c r) {
            this.j = j;
            this.k = k;
            this.l = l;
            this.m = m;
            this.n = n;
            this.o = o;
            this.p = p9;
            this.q = q;
            this.r = r;
        }
        
        public void onReceivedSslError(final WebView webView, final SslErrorHandler sslErrorHandler, final SslError sslError) {
            ac.e("SSL Error");
            sslErrorHandler.proceed();
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
        }
    }
    
    class c extends WebView
    {
        public boolean a;
        public boolean b;
        
        c(final Activity activity) {
            super((Context)activity);
            this.a = false;
            this.b = false;
            this.getSettings().setJavaScriptEnabled(true);
            this.getSettings().setSaveFormData(false);
            this.getSettings().setSavePassword(false);
        }
    }
    
    class d extends AsyncTask<Void, Void, Boolean>
    {
        String a;
        String b;
        final /* synthetic */ Activity c;
        final /* synthetic */ aw d;
        final /* synthetic */ an e;
        final /* synthetic */ e f;
        final /* synthetic */ int g;
        final /* synthetic */ String h;
        
        d(final Activity c, final aw d, final an e, final e f, final int g, final String h) {
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.h = h;
        }
        
        protected Boolean a(final Void... array) {
            try {
                final a a = new a("zSB18SDCMETDn2BH50xg", "Khb9CLQxkT62an9PUUpvBtRyh1Jluvwi7FGAiJkuJY&", "POST", "https://api.twitter.com/oauth/request_token");
                a.a("oauth_callback", "oauth://twitter.com");
                final boolean a2 = a.a();
                this.a = a.b().get("oauth_token");
                this.b = "https://api.twitter.com/oauth/authorize?oauth_token=" + this.a;
                if (this.a == null || this.b == null) {
                    return false;
                }
                return a2;
            }
            catch (Exception ex) {
                ac.a(ex);
                return false;
            }
        }
        
        protected void a(final Boolean b) {
            if (b) {
                b(this.c, this.d, this.b, this.e, this.a, this.f, this.g, this.h);
                return;
            }
            this.e.dismiss();
            this.f.a(0, "");
        }
    }
    
    public interface e
    {
        void a(final int p0, final String p1);
    }
}
