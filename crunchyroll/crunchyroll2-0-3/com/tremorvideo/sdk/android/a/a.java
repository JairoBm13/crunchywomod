// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.a;

import android.content.Context;
import java.util.Iterator;
import android.os.Process;
import android.app.ActivityManager$RunningAppProcessInfo;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import java.util.Map;
import com.tremorvideo.sdk.android.videoad.ac;
import java.util.TimerTask;
import java.util.Calendar;
import android.content.Intent;
import android.net.Uri;
import android.webkit.URLUtil;
import com.tremorvideo.sdk.android.videoad.aw;
import com.tremorvideo.sdk.android.videoad.n;
import android.app.Activity;
import java.util.Timer;
import com.tremorvideo.sdk.android.videoad.bw;

public class a extends a
{
    bw a;
    Timer b;
    private c e;
    private boolean f;
    private boolean g;
    private boolean h;
    private long i;
    private long j;
    
    public a(final a a, final Activity activity, final n n) {
        super(a, activity);
        this.g = false;
        this.h = false;
        this.i = -1L;
        this.j = -1L;
        this.e = n.y();
        this.f = false;
        this.a = n.q();
    }
    
    private void a(final String s, final String s2) {
        final aw a = this.e.a(aw.b.U);
        if (a != null) {
            a.b("dealer_id", s2);
            a.b("click_uri", s2);
            this.d.a(a);
        }
        if (URLUtil.isValidUrl(s2)) {
            ((Context)this.c).startActivity(new Intent("android.intent.action.VIEW", Uri.parse(s2)));
        }
        this.h();
    }
    
    private void g() {
        synchronized (this) {
            if (!this.f) {
                this.f = true;
                final aw a = this.e.a(aw.b.V);
                if (a != null) {
                    this.d.a(a);
                }
                this.d.a(this);
            }
        }
    }
    
    private void h() {
        synchronized (this) {
            if (!this.f) {
                this.f = true;
                final aw a = this.e.a(aw.b.W);
                if (a != null) {
                    this.d.a(a);
                }
                this.d.a(this);
            }
        }
    }
    
    private void i() {
        synchronized (this) {
            if (!this.f) {
                this.f = true;
                final aw a = this.e.a(aw.b.V);
                if (a != null) {
                    this.d.a(a);
                }
                this.d.a(this);
            }
        }
    }
    
    @Override
    public void a() {
        if (this.g) {
            return;
        }
        this.g = true;
        try {
            this.d.a(this.e.a(aw.b.T));
            if (this.e.a() > 0) {
                this.i = Calendar.getInstance().getTimeInMillis();
                this.j = 0L;
                (this.b = new Timer()).schedule(new a(), this.e.a());
            }
        }
        catch (Exception ex) {
            ac.a(ex);
            this.d.a(this);
        }
    }
    
    public void a(final Map<String, String> map) {
        final aw a = this.e.a(map.get("event_type"));
        if (a != null) {
            if (a.a() == aw.b.U) {
                this.a(map.get("dealer_id"), map.get("click_url"));
            }
            else {
                if (a.a() == aw.b.V) {
                    this.g();
                    return;
                }
                if (a.a() == aw.b.W) {
                    this.h();
                }
            }
        }
    }
    
    @Override
    public void b() {
        if (this.b != null) {
            this.j += Calendar.getInstance().getTimeInMillis() - this.i;
            this.h = true;
            this.b.cancel();
            this.b.purge();
            this.b = null;
        }
        super.b();
    }
    
    @Override
    public void c() {
        if (this.h && !((KeyguardManager)this.c.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
            final long n = this.e.a() - this.j;
            if (n > 0L) {
                (this.b = new Timer()).schedule(new a(), n);
                this.i = Calendar.getInstance().getTimeInMillis();
            }
            this.h = false;
        }
        super.c();
    }
    
    @Override
    public void d() {
        if (!this.h) {
            return;
        }
        while (true) {
            super.d();
            while (true) {
                Label_0088: {
                    try {
                        Block_5: {
                            for (final ActivityManager$RunningAppProcessInfo activityManager$RunningAppProcessInfo : ((ActivityManager)this.c.getSystemService("activity")).getRunningAppProcesses()) {
                                if (activityManager$RunningAppProcessInfo.pid == Process.myPid()) {
                                    break Block_5;
                                }
                            }
                            break Label_0088;
                        }
                        final ActivityManager$RunningAppProcessInfo activityManager$RunningAppProcessInfo;
                        if (activityManager$RunningAppProcessInfo.importance == 100) {
                            final int n = 1;
                            if (n != 0) {
                                this.c();
                            }
                            return;
                        }
                    }
                    catch (Exception ex) {
                        ac.a(ex);
                    }
                }
                final int n = 0;
                continue;
            }
        }
    }
    
    public void e() {
        if (this.b != null) {
            this.b.cancel();
            this.b.purge();
            this.b = null;
        }
    }
    
    public void f() {
        if (this.b != null) {
            this.b.cancel();
            this.b.purge();
            this.b = null;
        }
    }
    
    class a extends TimerTask
    {
        @Override
        public void run() {
            com.tremorvideo.sdk.android.a.a.this.b.cancel();
            com.tremorvideo.sdk.android.a.a.this.b.purge();
            com.tremorvideo.sdk.android.a.a.this.b = null;
            com.tremorvideo.sdk.android.a.a.this.i();
        }
    }
}
