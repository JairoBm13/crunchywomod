// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.d;

import java.util.Iterator;
import android.os.Process;
import android.app.ActivityManager$RunningAppProcessInfo;
import android.app.ActivityManager;
import java.util.TimerTask;
import android.app.KeyguardManager;
import java.util.Calendar;
import com.tremorvideo.sdk.android.videoad.ac;
import com.tremorvideo.sdk.android.videoad.bx;
import android.content.Intent;
import android.net.Uri;
import com.tremorvideo.sdk.android.videoad.n;
import android.app.Activity;
import java.util.Timer;

public class a extends a
{
    Timer a;
    private boolean b;
    private boolean e;
    private boolean f;
    private boolean g;
    private long h;
    private long i;
    
    public a(final a a, final Activity activity, final n n) {
        super(a, activity);
        this.e = false;
        this.g = false;
        this.h = -1L;
        this.i = -1L;
        this.b = false;
    }
    
    private void r() {
        synchronized (this) {
            if (!this.b) {
                this.b = true;
                this.d.a(this);
            }
        }
    }
    
    @Override
    public void a() {
        if (!this.e) {
            this.e = true;
            this.f = false;
        }
    }
    
    public void a(final String s) {
        synchronized (this) {
            try {
                this.c.startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse(s)), 12);
                bx.c();
            }
            catch (Exception ex) {
                ac.e("TremorLog_error::AdChoice::adchoices-open " + ex.getMessage());
            }
        }
    }
    
    @Override
    public void b() {
        if (this.a != null) {
            this.i += Calendar.getInstance().getTimeInMillis() - this.h;
            this.g = true;
            this.a.cancel();
            this.a.purge();
            this.a = null;
        }
        super.b();
    }
    
    @Override
    public void c() {
        if (this.g && !((KeyguardManager)this.c.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
            final long n = 10000L - this.i;
            if (n > 0L) {
                (this.a = new Timer()).schedule(new a(), n);
                this.h = Calendar.getInstance().getTimeInMillis();
            }
            this.g = false;
        }
        super.c();
    }
    
    @Override
    public void d() {
        if (!this.g) {
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
        synchronized (this) {
            if (!this.b) {
                this.h();
                this.b = true;
                this.d.a(this);
            }
        }
    }
    
    public void f() {
        if (!this.f) {
            this.h = Calendar.getInstance().getTimeInMillis();
            this.i = 0L;
            (this.a = new Timer()).schedule(new a(), 10000L);
            this.f = true;
        }
    }
    
    public void g() {
        if (this.a != null) {
            this.a.cancel();
            this.a.purge();
            this.a = null;
            this.f = false;
            this.f();
        }
    }
    
    public void h() {
        this.f = true;
        if (this.a != null) {
            this.a.cancel();
            this.a.purge();
            this.a = null;
        }
    }
    
    public void i() {
        if (this.a != null) {
            this.a.cancel();
            this.a.purge();
            this.a = null;
        }
    }
    
    class a extends TimerTask
    {
        @Override
        public void run() {
            com.tremorvideo.sdk.android.d.a.this.a.cancel();
            com.tremorvideo.sdk.android.d.a.this.a.purge();
            com.tremorvideo.sdk.android.d.a.this.a = null;
            com.tremorvideo.sdk.android.d.a.this.r();
        }
    }
}
