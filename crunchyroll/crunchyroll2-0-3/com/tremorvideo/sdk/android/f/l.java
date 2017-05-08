// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.f;

import com.tremorvideo.sdk.android.videoad.ac;
import java.util.ArrayList;
import android.view.Window;
import android.graphics.Rect;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import android.content.IntentFilter;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.os.Handler;

class l extends b
{
    protected float b;
    protected int c;
    protected int d;
    private s.k e;
    private final s.a f;
    private final s.e g;
    private boolean h;
    private boolean i;
    private String j;
    private Runnable k;
    private Handler l;
    private final int m;
    private BroadcastReceiver n;
    private boolean o;
    
    public l(final s s, final s.a f, final s.e g) {
        super(s);
        this.e = s.k.d;
        this.h = true;
        this.j = "none";
        this.k = new Runnable() {
            @Override
            public void run() {
                final boolean e = com.tremorvideo.sdk.android.f.l.this.e();
                if (com.tremorvideo.sdk.android.f.l.this.h != e) {
                    com.tremorvideo.sdk.android.f.l.this.h = e;
                    com.tremorvideo.sdk.android.f.l.this.a().a(t.a(com.tremorvideo.sdk.android.f.l.this.h));
                }
                com.tremorvideo.sdk.android.f.l.this.l.postDelayed((Runnable)this, 3000L);
            }
        };
        this.l = new Handler();
        this.n = new BroadcastReceiver() {
            private int b;
            
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("android.intent.action.CONFIGURATION_CHANGED")) {
                    final int c = com.tremorvideo.sdk.android.f.l.this.i();
                    if (c != this.b) {
                        this.b = c;
                        com.tremorvideo.sdk.android.f.l.this.a(this.b);
                    }
                }
            }
        };
        this.c = -1;
        this.d = -1;
        this.f = f;
        this.g = g;
        this.i = true;
        final Context context = this.a().getContext();
        int requestedOrientation;
        if (context instanceof Activity) {
            requestedOrientation = ((Activity)context).getRequestedOrientation();
        }
        else {
            requestedOrientation = -1;
        }
        this.m = requestedOrientation;
        this.f();
    }
    
    private void a(final int n) {
        this.g();
        this.a().a(p.a(this.c, this.d));
    }
    
    private void f() {
        this.e = s.k.a;
        this.g();
        this.h();
        this.a().getContext().registerReceiver(this.n, new IntentFilter("android.intent.action.CONFIGURATION_CHANGED"));
    }
    
    private void g() {
        int top = 0;
        final Context context = this.a().getContext();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager)context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        this.b = displayMetrics.density;
        int n;
        if (context instanceof Activity) {
            final Window window = ((Activity)context).getWindow();
            final Rect rect = new Rect();
            window.getDecorView().getWindowVisibleDisplayFrame(rect);
            top = rect.top;
            n = window.findViewById(16908290).getTop() - top;
        }
        else {
            n = 0;
        }
        final int widthPixels = displayMetrics.widthPixels;
        final int heightPixels = displayMetrics.heightPixels;
        this.c = (int)(widthPixels / displayMetrics.density);
        this.d = (int)((heightPixels - top - n) / displayMetrics.density);
    }
    
    private void h() {
        this.l.removeCallbacks(this.k);
        this.l.post(this.k);
    }
    
    private int i() {
        return ((WindowManager)this.a().getContext().getSystemService("window")).getDefaultDisplay().getOrientation();
    }
    
    protected void a(final boolean o) {
        this.o = o;
        final s a = this.a();
        final boolean b = !o;
        if (a.j() != null) {
            a.j().a(a, b);
        }
    }
    
    public void b() {
        this.l.removeCallbacks(this.k);
        try {
            this.a().getContext().unregisterReceiver(this.n);
        }
        catch (IllegalArgumentException ex) {
            if (!ex.getMessage().contains("Receiver not registered")) {
                throw ex;
            }
        }
    }
    
    protected void c() {
        this.h = true;
        this.e = s.k.b;
        final ArrayList<r> list = new ArrayList<r>();
        list.add((r)p.a(this.c, this.d));
        list.add((r)t.a(this.h));
        list.add(r.b());
        list.add((r)q.a(this.e));
        this.a().a((ArrayList<o>)list);
    }
    
    protected void d() {
        this.a().setVisibility(4);
        this.e = s.k.d;
        ac.e("TremorLog_info::MRAID::stateChange event - hidden");
        this.a().a(t.a(false));
        if (this.a().h() != null) {
            this.a().h().a(this.a(), this.e);
        }
    }
    
    protected boolean e() {
        return true;
    }
}
