// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.b;

import java.util.Iterator;
import android.os.Process;
import android.app.ActivityManager$RunningAppProcessInfo;
import android.app.ActivityManager;
import java.util.TimerTask;
import android.app.KeyguardManager;
import java.util.Calendar;
import com.tremorvideo.sdk.android.videoad.bx;
import android.net.Uri;
import android.content.Context;
import android.content.Intent;
import com.tremorvideo.sdk.android.videoad.Playvideo;
import com.tremorvideo.sdk.android.videoad.ac;
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
    private boolean i;
    private long j;
    private long k;
    
    public a(final a a, final Activity activity, final n n) {
        super(a, activity);
        this.g = false;
        this.i = false;
        this.j = -1L;
        this.k = -1L;
        this.e = n.z();
        this.f = false;
        this.a = n.q();
    }
    
    private void r() {
        synchronized (this) {
            if (!this.f) {
                this.f = true;
                final aw a = this.e.a(aw.b.aa);
                if (a != null) {
                    this.d.a(this.d.a(a));
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
        this.h = false;
        try {
            this.d.a(this.d.a(this.e.a(aw.b.Y)));
        }
        catch (Exception ex) {
            ac.a(ex);
            this.d.a(this);
        }
    }
    
    public void a(final aw.b b) {
        final aw a = this.e.a(b);
        if (a != null) {
            this.d.a(this.d.a(a));
        }
    }
    
    public void a(final aw.b b, final String s) {
        synchronized (this) {
            this.a(b);
            final Intent intent = new Intent((Context)this.c, (Class)Playvideo.class);
            intent.putExtra("tremorVideoType", "webview");
            intent.putExtra("tremorVideoURL", s);
            this.c.startActivityForResult(intent, 3232);
        }
    }
    
    public void a(final aw.b b, final String s, final String s2, final String s3) {
        synchronized (this) {
            this.a(b);
            final Double value = Double.parseDouble(s);
            final Double value2 = Double.parseDouble(s2);
            String string;
            if (value2 == 0.0 && value == 0.0) {
                string = "0,0";
            }
            else {
                string = "" + value2 + "," + value;
            }
            final Uri parse = Uri.parse("geo:" + (string + "?z=20" + "&q=" + s3 + "(" + s3 + ")"));
            ac.a(ac.c.b, "Map URL: " + parse.toString());
            final Intent intent = new Intent("android.intent.action.VIEW", parse);
            if (this.c.getPackageManager().resolveActivity(intent, 0) == null) {
                final String string2 = "http://maps.google.com/?q=" + s3 + "&ll=" + value2 + "," + value;
                final Intent intent2 = new Intent((Context)this.c, (Class)Playvideo.class);
                intent2.putExtra("tremorVideoType", "webview");
                intent2.putExtra("tremorVideoURL", string2);
                this.c.startActivityForResult(intent2, 3232);
            }
            else {
                this.c.startActivityForResult(intent, 11);
                bx.c();
            }
        }
    }
    
    @Override
    public void b() {
        if (this.b != null) {
            this.k += Calendar.getInstance().getTimeInMillis() - this.j;
            this.i = true;
            this.b.cancel();
            this.b.purge();
            this.b = null;
        }
        super.b();
    }
    
    @Override
    public void c() {
        if (this.i && !((KeyguardManager)this.c.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
            final long n = this.e.a() - this.k;
            if (n > 0L) {
                (this.b = new Timer()).schedule(new a(), n);
                this.j = Calendar.getInstance().getTimeInMillis();
            }
            this.i = false;
        }
        super.c();
    }
    
    @Override
    public void d() {
        if (!this.i) {
            return;
        }
        while (true) {
            super.d();
            while (true) {
                Label_0089: {
                    try {
                        Block_5: {
                            for (final ActivityManager$RunningAppProcessInfo activityManager$RunningAppProcessInfo : ((ActivityManager)this.c.getSystemService("activity")).getRunningAppProcesses()) {
                                if (activityManager$RunningAppProcessInfo.pid == Process.myPid()) {
                                    break Block_5;
                                }
                            }
                            break Label_0089;
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
            if (!this.f) {
                this.f = true;
                final aw a = this.e.a(aw.b.aa);
                if (a != null) {
                    this.d.a(this.d.a(a));
                }
                this.d.a(this);
            }
        }
    }
    
    public void f() {
        synchronized (this) {
            if (!this.f) {
                this.f = true;
                final aw a = this.e.a(aw.b.Z);
                if (a != null) {
                    this.d.a(this.d.a(a));
                }
                this.d.a(this);
            }
        }
    }
    
    public void g() {
        if (!this.h && this.e.a() > 0) {
            this.j = Calendar.getInstance().getTimeInMillis();
            this.k = 0L;
            (this.b = new Timer()).schedule(new a(), this.e.a());
            this.h = true;
        }
    }
    
    public void h() {
        this.h = true;
        if (this.b != null) {
            this.b.cancel();
            this.b.purge();
            this.b = null;
        }
    }
    
    public void i() {
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
            com.tremorvideo.sdk.android.b.a.this.b.cancel();
            com.tremorvideo.sdk.android.b.a.this.b.purge();
            com.tremorvideo.sdk.android.b.a.this.b = null;
            com.tremorvideo.sdk.android.b.a.this.r();
        }
    }
}
