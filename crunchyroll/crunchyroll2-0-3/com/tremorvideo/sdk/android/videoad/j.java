// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.content.Context;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.content.res.Configuration;
import android.content.Intent;
import android.os.Handler;
import android.app.Activity;

public class j implements c, l
{
    Activity a;
    int b;
    int c;
    long d;
    boolean e;
    n f;
    boolean g;
    boolean h;
    Handler i;
    au j;
    Runnable k;
    
    public j() {
        this.b = -1;
        this.c = 0;
        this.e = true;
        this.i = new Handler();
        this.j = ac.C();
        this.k = new Runnable() {
            @Override
            public void run() {
                ac.e("ASSET DOWNLOAD TIMEOUT");
                if (com.tremorvideo.sdk.android.videoad.j.this.f == null) {
                    com.tremorvideo.sdk.android.videoad.j.this.j.d();
                    com.tremorvideo.sdk.android.videoad.j.this.i();
                    return;
                }
                if (!(com.tremorvideo.sdk.android.videoad.j.this.f instanceof r) && (com.tremorvideo.sdk.android.videoad.j.this.f instanceof t || com.tremorvideo.sdk.android.videoad.j.this.f.r) && (!(com.tremorvideo.sdk.android.videoad.j.this.f instanceof s) || com.tremorvideo.sdk.android.videoad.j.this.f.r)) {
                    com.tremorvideo.sdk.android.videoad.j.this.j.d();
                    com.tremorvideo.sdk.android.videoad.j.this.i();
                    return;
                }
                if (com.tremorvideo.sdk.android.videoad.j.this.c > 1) {
                    com.tremorvideo.sdk.android.videoad.j.this.j.d();
                    com.tremorvideo.sdk.android.videoad.j.this.i();
                    return;
                }
                com.tremorvideo.sdk.android.videoad.j.this.j.d();
                com.tremorvideo.sdk.android.videoad.j.this.f = null;
                com.tremorvideo.sdk.android.videoad.j.this.g();
            }
        };
    }
    
    @Override
    public void a() {
        ac.e("ActiveAdProgress onStart");
        if (!this.g) {
            this.g();
        }
        this.g = false;
    }
    
    @Override
    public void a(final float n, final float n2, final int n3) {
    }
    
    @Override
    public void a(final int n, final int n2, final Intent intent) {
    }
    
    @Override
    public void a(final Configuration configuration) {
        this.a.getWindow().setFlags(1024, 1024);
    }
    
    @Override
    public void a(final Bundle bundle, final Activity a) {
        ac.e("ActivityAdProgress onCreate");
        this.a = a;
        this.c = 0;
        Thread.setDefaultUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread thread, final Throwable t) {
                ac.a(t.getStackTrace());
                ac.e(t.getMessage());
                com.tremorvideo.sdk.android.videoad.j.this.a.finish();
            }
        });
        this.a.requestWindowFeature(1);
        this.a.getWindow().setFlags(1024, 1024);
        final LinearLayout contentView = new LinearLayout((Context)this.a);
        final ProgressBar progressBar = new ProgressBar((Context)this.a, (AttributeSet)null, 16842874);
        progressBar.setLayoutParams(new ViewGroup$LayoutParams(-2, -2));
        contentView.setGravity(17);
        contentView.addView((View)progressBar);
        if (ac.r() >= 10) {
            this.a.setRequestedOrientation(10);
        }
        else {
            this.a.setRequestedOrientation(4);
        }
        this.a.setContentView((View)contentView);
    }
    
    @Override
    public void a(final n f) {
        ac.e("onCurrentAdDownloadFlowStart");
        this.f = f;
        if (this.f instanceof r) {
            ac.e("onCurrentAdDownloadFlowStart : AdRichMedia");
        }
        else if (this.f instanceof s) {
            ac.e("onCurrentAdDownloadFlowStart : AdVAST");
        }
    }
    
    @Override
    public boolean a(final int n, final KeyEvent keyEvent) {
        return n == 4;
    }
    
    @Override
    public void b() {
        this.g = true;
        ac.e("ActiveAdProgress onPause");
    }
    
    @Override
    public void c() {
        this.g = false;
        if (this.h) {
            this.h = false;
            this.k();
        }
        ac.e("ActiveAdProgress onResume");
    }
    
    @Override
    public boolean d() {
        return false;
    }
    
    @Override
    public void e() {
        ac.e("ActiveAdProgress onStop");
    }
    
    @Override
    public void f() {
        ac.e("ActiveAdProgress onDestroy");
    }
    
    public void g() {
        this.f = null;
        if (this.i != null) {
            this.i.removeCallbacks(this.k);
        }
        Label_0188: {
            if (this.c != 0 && this.c != 1) {
                break Label_0188;
            }
            this.d = SystemClock.elapsedRealtime();
            Label_0129: {
                if (this.c != 1) {
                    break Label_0129;
                }
                Label_0103_Outer:Block_5_Outer:
                while (true) {
                    try {
                        Thread.sleep(1000L);
                        ac.e("downloadManager.start call streaming true");
                        this.j.a(true, (au.c)this);
                        ++this.c;
                        this.i.postDelayed(this.k, ac.e + ac.f);
                        while (true) {
                            while (true) {
                                ac.e("attempt=" + this.c);
                                return;
                                Label_0176: {
                                    this.j.a(false, (au.c)this);
                                }
                                continue Label_0103_Outer;
                                ac.e("download happening in background");
                                this.j.a((au.c)this);
                                continue Label_0103_Outer;
                                this.i();
                                continue Block_5_Outer;
                            }
                            ac.e("downloadManager.start call streaming false");
                            this.j.c = null;
                            this.f = this.j.g();
                            continue;
                        }
                    }
                    // iftrue(Label_0176:, this.f == null)
                    catch (Exception ex) {
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public void h() {
        ac.e("onRequestDownloadComplete");
    }
    
    public void i() {
        if (this.i != null) {
            this.i.removeCallbacks(this.k);
        }
        bx.a(bx.a.b, false, 0);
        this.a.finish();
    }
    
    @Override
    public void j() {
        ac.e("onRequestDownloadTimeout");
        this.i();
    }
    
    public void k() {
        synchronized (this) {
            if (this.e) {
                ac.e("onAsssetDownloadComplete showAd loop");
                if (!ac.C().e(ac.p())) {
                    ac.e("Progress: Failed to Start the Ad");
                    this.i();
                }
                else {
                    this.e = false;
                    final Intent intent = new Intent((Context)this.a, (Class)Playvideo.class);
                    intent.putExtra("tremorVideoType", "ad");
                    bx.a(bx.a.a, new Object[0]);
                    this.a.startActivityForResult(intent, 131);
                    this.a.finish();
                }
            }
        }
    }
    
    @Override
    public void l() {
        ac.e("onAsssetDownloadComplete");
        this.i.removeCallbacks(this.k);
        if (this.g) {
            this.h = true;
            return;
        }
        this.k();
    }
}
