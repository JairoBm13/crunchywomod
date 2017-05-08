// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.a;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.widget.LinearLayout$LayoutParams;
import android.os.Bundle;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import com.tremorvideo.sdk.android.videoad.bw;
import com.tremorvideo.sdk.android.e.s;
import com.tremorvideo.sdk.android.videoad.ac;
import android.content.DialogInterface;
import android.content.DialogInterface$OnCancelListener;
import com.tremorvideo.sdk.android.videoad.an;
import android.content.Context;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.webkit.WebView;
import android.app.ProgressDialog;
import android.view.ViewGroup$LayoutParams;
import android.app.Dialog;

public class d extends Dialog
{
    static final float[] a;
    static final float[] b;
    static final ViewGroup$LayoutParams c;
    protected b.a d;
    private String e;
    private ProgressDialog f;
    private WebView g;
    private LinearLayout h;
    private TextView i;
    private boolean j;
    private boolean k;
    
    static {
        a = new float[] { 20.0f, 60.0f };
        b = new float[] { 40.0f, 60.0f };
        c = new ViewGroup$LayoutParams(-2, -2);
    }
    
    protected d(final Context context, final String e, final b.a d) {
        super(context);
        this.e = e;
        this.d = d;
        this.j = true;
        this.k = false;
        (this.f = an.a(this.getContext())).setMessage((CharSequence)"Loading...");
        this.f.setCancelable(false);
        this.setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
            public void onCancel(final DialogInterface dialogInterface) {
                com.tremorvideo.a.d.this.d.a();
            }
        });
    }
    
    public static d a(final Context context, final String s, final b.a a) {
        final int r = ac.r();
        if (r < 5) {
            return new d(context, s, a);
        }
        if (r < 7) {
            return new e(context, s, a);
        }
        return new f(context, s, a);
    }
    
    private void c() {
        this.requestWindowFeature(1);
        final BitmapDrawable bitmapDrawable = new BitmapDrawable(bw.a(s.a));
        (this.i = new TextView(this.getContext())).setText((CharSequence)"Facebook");
        this.i.setTextColor(-1);
        this.i.setTypeface(Typeface.DEFAULT_BOLD);
        this.i.setBackgroundColor(-9599820);
        this.i.setPadding(6, 4, 4, 4);
        this.i.setCompoundDrawablePadding(6);
        this.i.setCompoundDrawablesWithIntrinsicBounds((Drawable)bitmapDrawable, (Drawable)null, (Drawable)null, (Drawable)null);
        this.h.addView((View)this.i);
    }
    
    private void d() {
        this.h.addView((View)this.g);
    }
    
    public void a() {
        this.j = true;
        (this.g = new WebView(this.getContext())).setVerticalScrollBarEnabled(false);
        this.g.setHorizontalScrollBarEnabled(false);
        this.g.setWebViewClient(this.b());
        this.g.getSettings().setJavaScriptEnabled(true);
        this.g.setWebChromeClient(new WebChromeClient());
        this.g.loadUrl(this.e);
        this.g.setLayoutParams(com.tremorvideo.a.d.c);
        this.g.getSettings().setSaveFormData(false);
        this.g.getSettings().setSavePassword(false);
        this.getWindow().setFlags(1024, 1024);
        this.show();
        this.f.show();
    }
    
    protected WebViewClient b() {
        return new a();
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        (this.h = new LinearLayout(this.getContext())).setOrientation(1);
        this.c();
        this.d();
        this.addContentView((View)this.h, (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-2, -2));
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        return n == 84 || super.onKeyDown(n, keyEvent);
    }
    
    protected void onStart() {
        super.onStart();
    }
    
    protected void onStop() {
        super.onStop();
    }
    
    protected class a extends WebViewClient
    {
        protected void a() {
            com.tremorvideo.a.d.this.j = false;
            if (com.tremorvideo.a.d.this.isShowing()) {
                com.tremorvideo.a.d.this.dismiss();
            }
        }
        
        public void onPageFinished(final WebView webView, String title) {
            super.onPageFinished(webView, title);
            if (com.tremorvideo.a.d.this.j && !com.tremorvideo.a.d.this.k) {
                com.tremorvideo.a.d.this.j = false;
                com.tremorvideo.a.d.this.getWindow().setFlags(1024, 1024);
                com.tremorvideo.a.d.this.show();
                title = com.tremorvideo.a.d.this.g.getTitle();
                if (title != null && title.length() > 0) {
                    com.tremorvideo.a.d.this.i.setText((CharSequence)title);
                }
                webView.forceLayout();
            }
            if (com.tremorvideo.a.d.this.f != null && com.tremorvideo.a.d.this.f.isShowing()) {
                com.tremorvideo.a.d.this.f.dismiss();
            }
        }
        
        public void onPageStarted(final WebView webView, final String s, final Bitmap bitmap) {
            Log.d("Facebook-WebView", "Webview loading URL: " + s);
            super.onPageStarted(webView, s, bitmap);
            if (!com.tremorvideo.a.d.this.f.isShowing()) {
                com.tremorvideo.a.d.this.f.show();
            }
        }
        
        public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
            super.onReceivedError(webView, n, s, s2);
            com.tremorvideo.a.d.this.d.a(new com.tremorvideo.a.a(s, n, s2));
            this.a();
        }
        
        public boolean shouldOverrideUrlLoading(final WebView webView, String string) {
            Log.d("Facebook-WebView", "Redirect URL: " + string);
            if (string.startsWith("fbconnect://success")) {
                final Bundle b = com.tremorvideo.a.g.b(string);
                string = b.getString("error");
                String string2;
                if ((string2 = string) == null) {
                    string2 = b.getString("error_type");
                }
                if (string2 == null) {
                    com.tremorvideo.a.d.this.d.a(b);
                }
                else if (string2.equals("access_denied") || string2.equals("OAuthAccessDeniedException")) {
                    com.tremorvideo.a.d.this.d.a();
                }
                else {
                    com.tremorvideo.a.d.this.d.a(new c(string2));
                }
                com.tremorvideo.a.d.this.k = true;
                com.tremorvideo.a.d.this.dismiss();
                return true;
            }
            if (string.startsWith("fbconnect://cancel")) {
                com.tremorvideo.a.d.this.d.a();
                com.tremorvideo.a.d.this.dismiss();
                return true;
            }
            if (string.contains("touch")) {
                return false;
            }
            com.tremorvideo.a.d.this.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(string)));
            return true;
        }
    }
}
