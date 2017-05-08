// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia.a;

import android.content.res.Resources;
import android.view.KeyEvent;
import android.content.IntentFilter;
import android.view.View;
import java.util.TimerTask;
import com.tremorvideo.sdk.android.videoad.ac;
import android.os.Bundle;
import android.content.res.Configuration;
import org.apache.http.NameValuePair;
import java.util.List;
import com.tremorvideo.sdk.android.videoad.aw;
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
    private com.tremorvideo.sdk.android.richmedia.a.a i;
    private Activity j;
    private WebView k;
    private com.tremorvideo.sdk.android.videoad.a.a l;
    
    public b() {
        this.c = true;
        this.d = -1;
        this.f = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                com.tremorvideo.sdk.android.richmedia.a.b.this.c = false;
                com.tremorvideo.sdk.android.richmedia.a.b.this.i.l();
            }
        };
        this.g = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                com.tremorvideo.sdk.android.richmedia.a.b.this.c = true;
                com.tremorvideo.sdk.android.richmedia.a.b.this.i.k();
            }
        };
        this.h = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (com.tremorvideo.sdk.android.richmedia.a.b.this.c) {
                    com.tremorvideo.sdk.android.richmedia.a.b.this.i.d();
                }
            }
        };
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
    }
    
    @Override
    public void a(final Bundle bundle, final Activity j) {
        Thread.setDefaultUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread thread, final Throwable t) {
                ac.a(t.getStackTrace());
                ac.e(t.getMessage());
                com.tremorvideo.sdk.android.richmedia.a.b.this.j.finish();
            }
        });
        (this.j = j).requestWindowFeature(1);
        this.j.getWindow().setFlags(1024, 1024);
        this.i = new com.tremorvideo.sdk.android.richmedia.a.a(this, this.j, com.tremorvideo.sdk.android.richmedia.a.b.a);
        final String string = this.j.getIntent().getExtras().getString("path");
        final String string2 = this.j.getIntent().getExtras().getString("params");
        this.k = new c((Context)this.j, this);
        ((c)this.k).a(string2);
        final String t = com.tremorvideo.sdk.android.richmedia.a.b.a.t();
        this.k.setPadding(0, 0, 0, 0);
        (this.e = new Timer()).schedule(new a("file://" + t + "/" + string), 1000L);
        this.j.setContentView((View)this.k);
        this.l = com.tremorvideo.sdk.android.richmedia.a.b.b;
        this.j.registerReceiver(this.f, new IntentFilter("android.intent.action.SCREEN_OFF"));
        this.j.registerReceiver(this.g, new IntentFilter("android.intent.action.SCREEN_ON"));
        this.j.registerReceiver(this.h, new IntentFilter("android.intent.action.USER_PRESENT"));
        if (ac.r() >= 9) {
            this.j.setRequestedOrientation(6);
            return;
        }
        this.j.setRequestedOrientation(0);
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
        ac.e("WebView - onDestroy");
        if (this.i != null) {
            this.i.f();
            this.i = null;
        }
        while (true) {
            try {
                this.j.unregisterReceiver(this.f);
                this.j.unregisterReceiver(this.g);
                this.j.unregisterReceiver(this.h);
                System.gc();
            }
            catch (IllegalArgumentException ex) {
                continue;
            }
            break;
        }
    }
    
    @Override
    public n g() {
        return com.tremorvideo.sdk.android.richmedia.a.b.a;
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
    
    public com.tremorvideo.sdk.android.richmedia.a.a k() {
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
            com.tremorvideo.sdk.android.richmedia.a.b.this.e.purge();
            com.tremorvideo.sdk.android.richmedia.a.b.this.e.cancel();
            com.tremorvideo.sdk.android.richmedia.a.b.this.e = null;
            com.tremorvideo.sdk.android.richmedia.a.b.this.j.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    com.tremorvideo.sdk.android.richmedia.a.b.this.k.loadUrl(com.tremorvideo.sdk.android.richmedia.a.b.a.this.a);
                }
            });
        }
    }
}
