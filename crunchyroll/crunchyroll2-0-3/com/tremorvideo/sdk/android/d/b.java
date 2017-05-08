// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.d;

import android.content.res.Resources;
import android.view.KeyEvent;
import android.content.IntentFilter;
import android.view.View;
import java.util.TimerTask;
import android.widget.LinearLayout;
import java.io.File;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import com.tremorvideo.sdk.android.videoad.ac;
import android.content.res.Configuration;
import org.apache.http.NameValuePair;
import java.util.List;
import com.tremorvideo.sdk.android.videoad.aw;
import android.view.WindowManager;
import android.content.Intent;
import android.content.Context;
import android.webkit.WebView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import java.util.Timer;
import com.tremorvideo.sdk.android.videoad.n;
import com.tremorvideo.sdk.android.videoad.l;
import com.tremorvideo.sdk.android.videoad.a;

public class b implements com.tremorvideo.sdk.android.videoad.a.a, l
{
    public static n a;
    public static com.tremorvideo.sdk.android.videoad.a.a b;
    boolean c;
    int d;
    Timer e;
    BroadcastReceiver f;
    BroadcastReceiver g;
    BroadcastReceiver h;
    private com.tremorvideo.sdk.android.d.a i;
    private Activity j;
    private WebView k;
    private com.tremorvideo.sdk.android.videoad.a.a l;
    
    public b() {
        this.c = true;
        this.d = -1;
        this.f = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                com.tremorvideo.sdk.android.d.b.this.c = false;
                com.tremorvideo.sdk.android.d.b.this.i.l();
            }
        };
        this.g = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                com.tremorvideo.sdk.android.d.b.this.c = true;
                com.tremorvideo.sdk.android.d.b.this.i.k();
            }
        };
        this.h = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (com.tremorvideo.sdk.android.d.b.this.c) {
                    com.tremorvideo.sdk.android.d.b.this.i.d();
                }
            }
        };
    }
    
    private Double m() {
        return new Double(((WindowManager)this.j.getSystemService("window")).getDefaultDisplay().getWidth()) / new Double(440.0);
    }
    
    @Override
    public int a(final aw aw) {
        return this.l.a(aw, -1);
    }
    
    @Override
    public int a(final aw aw, final int n) {
        return this.l.a(aw, n);
    }
    
    @Override
    public int a(final aw aw, final int n, final List<NameValuePair> list) {
        return this.l.a(aw, n, list);
    }
    
    @Override
    public void a() {
        this.i.a();
    }
    
    @Override
    public void a(final float n, final float n2, final int n3) {
    }
    
    @Override
    public void a(final int n) {
        this.l.a(n);
    }
    
    @Override
    public void a(final int n, final int n2, final Intent intent) {
    }
    
    @Override
    public void a(final Configuration configuration) {
        ac.e("ActivityAdChoice : onConfigurationChanged");
        if (this.m() >= 1.0) {
            final DisplayMetrics displayMetrics = this.j.getResources().getDisplayMetrics();
            final float density = this.j.getResources().getDisplayMetrics().density;
            int widthPixels = displayMetrics.widthPixels;
            int heightPixels = displayMetrics.heightPixels;
            final int n = (int)(440 * density);
            final int n2 = (int)(320 * density);
            if (n <= widthPixels) {
                widthPixels = n;
            }
            if (n2 <= heightPixels) {
                heightPixels = n2;
            }
            final LinearLayout$LayoutParams layoutParams = new LinearLayout$LayoutParams(widthPixels, heightPixels);
            layoutParams.gravity = 17;
            this.k.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        }
    }
    
    @Override
    public void a(final Bundle bundle, final Activity j) {
        Thread.setDefaultUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread thread, final Throwable t) {
                ac.a(t.getStackTrace());
                ac.e(t.getMessage());
                com.tremorvideo.sdk.android.d.b.this.j.finish();
            }
        });
        ac.e("TremorLog_info::AdChoice::ActivityAdChoices onCreate");
        this.j = j;
        this.i = new com.tremorvideo.sdk.android.d.a(this, this.j, com.tremorvideo.sdk.android.d.b.a);
        this.j.requestWindowFeature(1);
        this.j.getWindow().setFlags(1024, 1024);
        this.k = new c((Context)this.j, this);
        final String string = this.j.getIntent().getExtras().getString("tremorVideoURL");
        ac.e("TremorLog_info::AdChoice::ActivityAdChoices file url=" + string);
        final File file = new File(string);
        final Double m = this.m();
        this.k.setPadding(0, 0, 0, 0);
        if (m >= 1.0) {
            final DisplayMetrics displayMetrics = this.j.getResources().getDisplayMetrics();
            final float density = this.j.getResources().getDisplayMetrics().density;
            int widthPixels = displayMetrics.widthPixels;
            int heightPixels = displayMetrics.heightPixels;
            final int n = (int)(440 * density);
            final int n2 = (int)(320 * density);
            if (n <= widthPixels) {
                widthPixels = n;
            }
            if (n2 <= heightPixels) {
                heightPixels = n2;
            }
            final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(widthPixels, heightPixels);
            linearLayout$LayoutParams.gravity = 17;
            final LinearLayout contentView = new LinearLayout((Context)this.j);
            contentView.setGravity(17);
            (this.e = new Timer()).schedule(new a("file://" + file.getAbsolutePath()), 100L);
            contentView.addView((View)this.k, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
            this.j.setContentView((View)contentView);
        }
        else {
            ac.M();
            (this.e = new Timer()).schedule(new a("file://" + file.getAbsolutePath()), 100L);
            this.j.setContentView((View)this.k);
        }
        this.l = com.tremorvideo.sdk.android.d.b.b;
        this.j.registerReceiver(this.f, new IntentFilter("android.intent.action.SCREEN_OFF"));
        this.j.registerReceiver(this.g, new IntentFilter("android.intent.action.SCREEN_ON"));
        this.j.registerReceiver(this.h, new IntentFilter("android.intent.action.USER_PRESENT"));
        if (this.l != null) {
            ac.a(this.j, this.l.j());
        }
    }
    
    @Override
    public void a(final com.tremorvideo.sdk.android.videoad.a a) {
        this.j.setResult(100, new Intent());
        this.j.finish();
    }
    
    @Override
    public boolean a(final int n, final KeyEvent keyEvent) {
        return false;
    }
    
    @Override
    public void b() {
        this.i.b();
    }
    
    @Override
    public void b(final int d) {
        this.d = d;
    }
    
    @Override
    public void c() {
        this.i.c();
    }
    
    @Override
    public boolean d() {
        return false;
    }
    
    @Override
    public void e() {
    }
    
    @Override
    public void f() {
        ac.e("AdChoiceWebView - onDestroy");
        if (this.i != null) {
            this.i.i();
            this.i = null;
        }
        this.j.unregisterReceiver(this.f);
        this.j.unregisterReceiver(this.g);
        this.j.unregisterReceiver(this.h);
        System.gc();
    }
    
    @Override
    public n g() {
        return com.tremorvideo.sdk.android.d.b.a;
    }
    
    @Override
    public Context h() {
        return (Context)this.j;
    }
    
    @Override
    public int i() {
        if (Resources.getSystem().getConfiguration().orientation == 2) {
            return 0;
        }
        return 1;
    }
    
    @Override
    public int j() {
        return this.d;
    }
    
    public com.tremorvideo.sdk.android.d.a k() {
        return this.i;
    }
    
    @Override
    public void l() {
    }
    
    class a extends TimerTask
    {
        String a;
        
        public a(final String a) {
            this.a = a;
        }
        
        @Override
        public void run() {
            com.tremorvideo.sdk.android.d.b.this.e.purge();
            com.tremorvideo.sdk.android.d.b.this.e.cancel();
            com.tremorvideo.sdk.android.d.b.this.e = null;
            com.tremorvideo.sdk.android.d.b.this.j.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    com.tremorvideo.sdk.android.d.b.this.k.loadUrl(com.tremorvideo.sdk.android.d.b.a.this.a);
                }
            });
        }
    }
}
