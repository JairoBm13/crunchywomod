// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import com.tremorvideo.sdk.android.logger.TestAppLogger;
import android.view.KeyEvent;
import android.view.View;
import java.util.TimerTask;
import android.content.Context;
import android.os.Bundle;
import android.content.res.Configuration;
import android.content.Intent;
import java.util.Timer;
import android.app.Activity;

public class m implements l
{
    Activity a;
    aq b;
    String c;
    Timer d;
    
    @Override
    public void a() {
    }
    
    @Override
    public void a(final float n, final float n2, final int n3) {
    }
    
    @Override
    public void a(final int n, final int n2, final Intent intent) {
    }
    
    @Override
    public void a(final Configuration configuration) {
    }
    
    @Override
    public void a(final Bundle bundle, final Activity a) {
        Thread.setDefaultUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread thread, final Throwable t) {
                ac.a(t.getStackTrace());
                ac.e(t.getMessage());
                m.this.a.finish();
            }
        });
        (this.a = a).requestWindowFeature(1);
        this.a.getWindow().setFlags(1024, 1024);
        ac.a(this.a.getWindow());
        this.c = this.a.getIntent().getExtras().getString("tremorVideoURL");
        (this.b = ac.a((Context)this.a)).f();
        (this.d = new Timer()).schedule(new a(), 1500L);
        this.a.setContentView((View)this.b);
    }
    
    @Override
    public boolean a(final int n, final KeyEvent keyEvent) {
        if (n == 4) {
            this.b.c();
            this.b.g();
        }
        return false;
    }
    
    @Override
    public void b() {
        this.b.e();
    }
    
    @Override
    public void c() {
        this.b.d();
    }
    
    @Override
    public boolean d() {
        return false;
    }
    
    @Override
    public void e() {
        this.b.c();
    }
    
    @Override
    public void f() {
        ac.e("ActivityWebView - onDestroy");
        this.b.g();
        this.b = null;
    }
    
    class a extends TimerTask
    {
        @Override
        public void run() {
            m.this.d.purge();
            m.this.d.cancel();
            m.this.d = null;
            m.this.a.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (m.this.b == null) {
                        return;
                    }
                    while (true) {
                        try {
                            if (ac.r) {
                                TestAppLogger.getInstance().logClickoutURL("ClickThrough URL", "click_out_url:" + m.this.c, "info");
                            }
                            m.this.b.a(m.this.c);
                        }
                        catch (Exception ex) {
                            ac.e("Error logClickoutURL" + ex);
                            continue;
                        }
                        break;
                    }
                }
            });
        }
    }
}
