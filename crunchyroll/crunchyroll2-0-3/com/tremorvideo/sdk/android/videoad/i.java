// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.content.res.Resources;
import android.view.KeyEvent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.content.res.Configuration;
import android.view.View;
import org.apache.http.NameValuePair;
import java.util.List;
import android.app.AlertDialog$Builder;
import android.content.Intent;
import android.content.Context;
import android.app.Activity;
import java.util.HashMap;
import android.content.BroadcastReceiver;

public class i implements a.a, e.b, l
{
    boolean a;
    boolean b;
    boolean c;
    int d;
    ay.b e;
    boolean f;
    boolean g;
    BroadcastReceiver h;
    BroadcastReceiver i;
    BroadcastReceiver j;
    HashMap<a, Boolean> k;
    private Activity l;
    private a m;
    private n n;
    private ay o;
    
    public i() {
        this.a = false;
        this.b = true;
        this.c = false;
        this.d = -1;
        this.f = false;
        this.g = false;
        this.h = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                com.tremorvideo.sdk.android.videoad.i.this.b = false;
                com.tremorvideo.sdk.android.videoad.i.this.m.l();
            }
        };
        this.i = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                com.tremorvideo.sdk.android.videoad.i.this.b = true;
                com.tremorvideo.sdk.android.videoad.i.this.m.k();
            }
        };
        this.j = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (com.tremorvideo.sdk.android.videoad.i.this.b) {
                    com.tremorvideo.sdk.android.videoad.i.this.m.d();
                }
            }
        };
        this.k = new HashMap<a, Boolean>();
    }
    
    private void a(final a.b b) {
        if (b == com.tremorvideo.sdk.android.videoad.a.b.a) {
            if (!this.n.v()) {
                this.a(com.tremorvideo.sdk.android.videoad.a.b.d);
                return;
            }
            this.b(new b(this, this.l, this.n, true));
            this.m.a();
        }
        else if (b == com.tremorvideo.sdk.android.videoad.a.b.b) {
            if (this.c || !this.m.j()) {
                this.a(com.tremorvideo.sdk.android.videoad.a.b.c);
                return;
            }
            if (this.a(this.n.u())) {
                this.b(new com.tremorvideo.sdk.android.videoad.d(this, this.l, this.n));
                return;
            }
            this.a(com.tremorvideo.sdk.android.videoad.a.b.c);
        }
        else if (b == com.tremorvideo.sdk.android.videoad.a.b.c) {
            if (this.a(this.n.s())) {
                this.b(new com.tremorvideo.sdk.android.videoad.c(this, this.l, this.n));
                return;
            }
            this.a(com.tremorvideo.sdk.android.videoad.a.b.a);
        }
        else if (b == com.tremorvideo.sdk.android.videoad.a.b.d) {
            com.tremorvideo.sdk.android.videoad.ac.e("<<< Ad End");
            if (this.f) {
                final Intent intent = new Intent();
                intent.putExtra("TREMOR_VAST_RESULT", "Vast Tag Playback Success");
                this.l.setResult(-1, intent);
                this.l.finish();
                return;
            }
            if (this.m != null) {
                this.m.p();
            }
            com.tremorvideo.sdk.android.videoad.ac.C().j();
            final Activity l = this.l;
            int n;
            if (this.c) {
                n = 101;
            }
            else {
                n = 100;
            }
            l.setResult(n, new Intent());
            final bx.a b2 = bx.a.b;
            int n2;
            if (this.g) {
                n2 = -1;
            }
            else {
                n2 = 1;
            }
            bx.a(b2, true, n2);
            this.l.finish();
        }
    }
    
    private boolean a(final az az) {
        return az != null && az.d() != null;
    }
    
    private boolean a(final be be) {
        return be != null;
    }
    
    private void b(final a m) {
        this.m = m;
    }
    
    private void c(final int n) {
        final ay.a b = this.o.b(n);
        if (b != null) {
            if (b.c.c().equals("-1")) {
                com.tremorvideo.sdk.android.videoad.ac.e("Event had ID of -1: not fired");
                return;
            }
            b.c.a(b);
        }
    }
    
    private void o() {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.l);
        alertDialog$Builder.setTitle((CharSequence)"Tremor Video Integration Error");
        alertDialog$Builder.setMessage((CharSequence)"You must disable hardware acceleration for the \"com.tremorvideo.sdk.android.videoad.Playvideo\" Activity in AndroidManifest.xml:\n\nPlease consult the supplied documentation for more information.");
        alertDialog$Builder.create().show();
    }
    
    @Override
    public int a(final aw aw) {
        return this.a(aw, -1, null);
    }
    
    @Override
    public int a(final aw aw, final int n) {
        return this.a(aw, n, null);
    }
    
    @Override
    public int a(final aw aw, int n, final List<NameValuePair> list) {
        int a = -1;
        if (aw != null) {
            if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.v) {
                this.c = true;
            }
            com.tremorvideo.sdk.android.videoad.ac.e("Starting Event: " + com.tremorvideo.sdk.android.videoad.ac.G() + "ms " + aw.a().toString() + " " + aw.h() + " " + aw.c());
            n = (a = this.o.a(aw, n, this.e, list));
            if (aw.g()) {
                this.c(n);
                a = n;
            }
        }
        return a;
    }
    
    @Override
    public void a() {
        if (this.m != null) {
            this.m.a();
        }
    }
    
    @Override
    public void a(final float n, final float n2, final int n3) {
        if (this.m.m() && n3 == 0) {
            final View decorView = this.l.getWindow().getDecorView();
            if (decorView != null) {
                this.e = new ay.b(n, n2, decorView.getWidth(), decorView.getHeight());
            }
            if (this.n.o != null) {
                this.a(this.a(this.n.o));
            }
        }
    }
    
    @Override
    public void a(final int n) {
        if (n != -1) {
            this.o.a(n);
            final ay.a b = this.o.b(n);
            final aw c = b.c;
            com.tremorvideo.sdk.android.videoad.ac.e("Ending Event: " + b.e + "ms " + c.a().toString() + " " + c.h() + " " + c.c());
            if (!c.g()) {
                this.c(n);
            }
        }
    }
    
    @Override
    public void a(final int n, final int n2, final Intent intent) {
        com.tremorvideo.sdk.android.videoad.ac.e("activity returned");
        if (intent != null) {
            final int intExtra = intent.getIntExtra("endEventId", -1);
            if (intExtra > -1) {
                this.a(intExtra);
            }
        }
        if (this.m != null) {
            this.m.a(n, n2, intent);
        }
    }
    
    @Override
    public void a(final Configuration configuration) {
        this.m.a(configuration);
    }
    
    @Override
    public void a(final Bundle bundle, final Activity l) {
        com.tremorvideo.sdk.android.videoad.ac.e("ActivityAd - onCreate");
        this.l = l;
        Label_0097: {
            if (this.l.getIntent().getExtras().getString("vastURL") == null) {
                break Label_0097;
            }
            boolean f = true;
            while (true) {
                this.f = f;
                this.g = false;
                Thread.setDefaultUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(final Thread thread, final Throwable t) {
                        com.tremorvideo.sdk.android.videoad.ac.a(t.getStackTrace());
                        com.tremorvideo.sdk.android.videoad.ac.e(t.getMessage());
                        bx.a(bx.a.b, false, -1);
                        com.tremorvideo.sdk.android.videoad.i.this.l.finish();
                    }
                });
                try {
                    if (com.tremorvideo.sdk.android.videoad.ac.x() == null) {
                        com.tremorvideo.sdk.android.videoad.ac.e("Calling application has been force killed, exiting...");
                        bx.a(bx.a.b, false, -1);
                        this.l.finish();
                        return;
                    }
                    break;
                    f = false;
                    continue;
                }
                catch (Exception ex) {
                    com.tremorvideo.sdk.android.videoad.ac.a(ex);
                    bx.a(bx.a.b, false, -1);
                    this.l.finish();
                    return;
                }
                break;
            }
        }
        try {
            this.k();
            this.l.registerReceiver(this.h, new IntentFilter("android.intent.action.SCREEN_OFF"));
            this.l.registerReceiver(this.i, new IntentFilter("android.intent.action.SCREEN_ON"));
            this.l.registerReceiver(this.j, new IntentFilter("android.intent.action.USER_PRESENT"));
        }
        catch (Exception ex2) {
            com.tremorvideo.sdk.android.videoad.ac.e("Exception onAdDownloaded" + ex2.getMessage());
            bx.a(bx.a.b, false, -1);
            this.l.finish();
        }
    }
    
    @Override
    public void a(final a a) {
        synchronized (this) {
            if (this.k != null && !this.k.containsKey(a)) {
                this.k.put(a, true);
                final a.b n = a.n();
                a.o();
                this.a(n);
            }
        }
    }
    
    @Override
    public void a(final n n) {
        this.n = n;
    }
    
    @Override
    public boolean a(final int n, final KeyEvent keyEvent) {
        return n == 84 || n == 82 || this.m.a(n, keyEvent);
    }
    
    @Override
    public void b() {
        if (this.m != null) {
            this.m.b();
        }
    }
    
    @Override
    public void b(final int d) {
        this.d = d;
    }
    
    @Override
    public void c() {
        if (this.m != null) {
            this.m.c();
        }
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
        if (this.n != null) {
            this.n.c();
        }
        if (this.m != null) {
            this.m.q();
        }
        if (this.l.getIntent().getExtras().getString("vastURL") == null && !bx.f()) {
            com.tremorvideo.sdk.android.videoad.ac.C().j();
            bx.a(bx.a.b, false, -1);
        }
        System.gc();
        try {
            this.l.unregisterReceiver(this.h);
            this.l.unregisterReceiver(this.i);
            this.l.unregisterReceiver(this.j);
        }
        catch (Exception ex) {
            com.tremorvideo.sdk.android.videoad.ac.e("UnregisterReceiver: " + ex);
        }
    }
    
    @Override
    public n g() {
        return this.n;
    }
    
    @Override
    public Context h() {
        return (Context)this.l;
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
    
    public boolean k() throws Exception {
        (this.o = new ay()).a();
        final boolean a = com.tremorvideo.sdk.android.videoad.ac.a(this.l);
        com.tremorvideo.sdk.android.videoad.ac.e(">>> Ad Start");
        if (!this.f) {
            this.n = com.tremorvideo.sdk.android.videoad.ac.C().h();
            if (!this.n.F() && !this.n.b()) {
                this.n.a((Context)this.l);
            }
            new x(this.l.getFilesDir()).c(this.n);
        }
        if (this.f) {
            this.b(new e(this.l, this, null, this.f, (e.b)this));
        }
        else if (this.n.g() == com.tremorvideo.sdk.android.videoad.n.b.b || this.n.g() == com.tremorvideo.sdk.android.videoad.n.b.e) {
            bx.a(this);
            this.b(new e(this.l, this, (t)this.n, this.f, null));
        }
        else if (this.n.g() == com.tremorvideo.sdk.android.videoad.n.b.c) {
            bx.a(this);
            this.b(new h(this, this.l, (r)this.n));
        }
        else if (this.n.g() == com.tremorvideo.sdk.android.videoad.n.b.d) {
            this.b(new com.tremorvideo.sdk.android.videoad.f(this, this.l, (p)this.n));
        }
        else if (this.n.g() == com.tremorvideo.sdk.android.videoad.n.b.f) {
            this.b(new com.tremorvideo.sdk.android.videoad.g(this, this.l, (q)this.n));
        }
        if (a) {
            this.o();
        }
        return true;
    }
    
    @Override
    public void l() {
        this.g = true;
    }
    
    public int m() {
        if (this.m != null && this.n != null) {
            if (this.n.g() == com.tremorvideo.sdk.android.videoad.n.b.b || this.n.g() == com.tremorvideo.sdk.android.videoad.n.b.e) {
                return ((e)this.m).e();
            }
            if (this.n.g() == com.tremorvideo.sdk.android.videoad.n.b.c) {
                return ((h)this.m).u();
            }
        }
        return -1;
    }
    
    public int n() {
        if (this.n == null) {
            return -1;
        }
        if (this.m != null && this.n.g() == com.tremorvideo.sdk.android.videoad.n.b.c) {
            return ((h)this.m).v();
        }
        return this.n.A();
    }
}
