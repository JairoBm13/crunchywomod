// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.view.View$OnClickListener;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageButton;
import com.tremorvideo.sdk.android.f.c;
import android.view.Display;
import android.util.TypedValue;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import java.util.Iterator;
import android.os.Process;
import android.app.ActivityManager$RunningAppProcessInfo;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.annotation.SuppressLint;
import android.view.KeyEvent;
import android.content.res.Configuration;
import java.io.File;
import java.util.TimerTask;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.RelativeLayout$LayoutParams;
import android.content.Context;
import com.tremorvideo.sdk.android.richmedia.m;
import android.app.Activity;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import com.tremorvideo.sdk.android.f.s;
import java.util.Timer;
import com.tremorvideo.sdk.android.richmedia.n;

public class g extends com.tremorvideo.sdk.android.videoad.a implements n.a, ax.c
{
    ax a;
    int b;
    int e;
    boolean f;
    Timer g;
    boolean h;
    private q i;
    private s j;
    private ImageView k;
    private RelativeLayout l;
    
    public g(final com.tremorvideo.sdk.android.videoad.a.a a, final Activity activity, final q i) {
        super(a, activity);
        this.b = 0;
        this.e = -1;
        this.f = false;
        this.h = false;
        this.i = i;
        (this.a = new ax(activity, a, this.i.q())).a((ax.c)this);
        this.i.I().a(this.a);
        final aw b = this.i.B();
        if (b != null) {
            this.a.a(b);
            this.a.b(b);
        }
        if (!i.K()) {
            this.a(null, true);
            return;
        }
        this.c.requestWindowFeature(1);
        this.c.getWindow().setFlags(1024, 1024);
        ac.a(this.c.getWindow());
        this.j = this.a(this);
        (this.l = new RelativeLayout((Context)this.c)).addView((View)this.j, (ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
        this.c.setContentView((View)this.l);
        (this.g = new Timer()).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    com.tremorvideo.sdk.android.videoad.g.this.v();
                }
                catch (Exception ex) {
                    ac.a(ex);
                }
            }
        }, 10L, 15L);
        this.h = false;
    }
    
    private s a(final g g) {
        (this.j = new s((Context)this.c, com.tremorvideo.sdk.android.f.s.a.a, com.tremorvideo.sdk.android.f.s.e.c, this.i.H(), this.i)).a(g);
        this.j.a((s.i)new s.i() {
            @Override
            public void a(final s s) {
                g.this.s();
            }
        });
        this.j.a((s.f)new s.f() {
            @Override
            public void a(final s s, final boolean b) {
                if (b) {
                    g.this.t();
                    return;
                }
                g.this.u();
            }
        });
        this.j.a((s.g)new s.g() {
            @Override
            public void a(final s s, final k k) {
                g.this.h = true;
            }
        });
        this.j.a(new File(this.i.H() + this.i.v), this.c);
        if (this.i.w == 1) {
            this.e = 0;
            ac.c(this.c);
            this.d.b(this.e);
        }
        else if (this.i.w == 2) {
            this.e = 1;
            ac.b(this.c);
            this.d.b(this.e);
        }
        else {
            this.e = -1;
            this.c.setRequestedOrientation(-1);
            this.d.b(this.e);
        }
        return this.j;
    }
    
    @Override
    public void a() {
        if (this.a.b()) {
            this.a.e();
            this.a.b((aw)null);
        }
    }
    
    @Override
    public void a(final Configuration configuration) {
    }
    
    @Override
    public void a(final m m, final boolean f) {
        this.a.a();
        this.i.I().a((ax)null);
        if (!(this.f = f)) {
            final aw c = this.i.C();
            if (c != null) {
                this.a.a(c);
                this.a.b(c);
            }
        }
        else {
            this.d.l();
        }
        this.c.runOnUiThread((Runnable)new a(this));
    }
    
    @Override
    public void a(final aw aw) {
        if (aw != null) {
            if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.w) {
                ++this.b;
            }
            else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.v) {
                this.d.a(this);
            }
        }
    }
    
    @Override
    public void a(final aw aw, final int n) {
        this.a(aw, n, null);
    }
    
    public void a(final aw aw, final int n, final String s) {
        if (aw != null) {
            if (!aw.m()) {
                final aa aa = new aa((Context)this.c, this.i.q(), com.tremorvideo.sdk.android.videoad.aa.a.a, (aa.b)new aa.b() {
                    @Override
                    public void a(final boolean b) {
                    }
                });
                aa.setCanceledOnTouchOutside(false);
                aa.setTitle("Unsupported Feature");
                aa.a("Sorry, that feature is not supported on your device.");
                aa.a("OK", "");
                aa.show();
                return;
            }
            this.a.a(aw, n, (String)null);
        }
    }
    
    @Override
    public void a(final String s, final int n) {
        aw aw;
        if (s == "adchoices") {
            aw = this.i.a(com.tremorvideo.sdk.android.videoad.aw.b.H);
        }
        else {
            aw = this.i.c(s);
        }
        if (aw != null) {
            this.a(aw, n);
        }
    }
    
    @Override
    public void a(final String s, final aw.b b, final int n, final String s2) {
    }
    
    public void a(final boolean b) {
        if (this.j.a) {
            this.j.p();
            return;
        }
        this.g.purge();
        this.g.cancel();
        this.g = null;
        this.h = false;
        this.a(null, b);
    }
    
    public void a(final boolean b, final String s) {
        if (s.equals("portrait")) {
            ac.b(this.c);
        }
        else {
            if (s.equals("landscape")) {
                ac.c(this.c);
                return;
            }
            if (b && s.equals("none")) {
                ac.a(this.c, -1);
                return;
            }
            if (!b && s.equals("none")) {
                final int orientation = this.c.getResources().getConfiguration().orientation;
                if (orientation == 7 || orientation == 1) {
                    ac.b(this.c);
                    return;
                }
                ac.c(this.c);
            }
        }
    }
    
    @Override
    public boolean a(final int n, final KeyEvent keyEvent) {
        if (n == 4) {
            if (this.j != null && !this.j.b()) {
                final aw n2 = this.i.n();
                if (n2 != null) {
                    this.a.a(n2);
                    this.a.b(n2);
                }
                this.a(false);
            }
            return true;
        }
        return super.a(n, keyEvent);
    }
    
    @SuppressLint({ "NewApi" })
    @Override
    public void b() {
        if (this.j != null) {
            this.j.onPause();
        }
        super.b();
    }
    
    @Override
    public void b(final aw aw) {
    }
    
    @SuppressLint({ "NewApi" })
    @Override
    public void c() {
        super.c();
        final KeyguardManager keyguardManager = (KeyguardManager)this.c.getSystemService("keyguard");
        if (this.j != null && !keyguardManager.inKeyguardRestrictedInputMode()) {
            this.j.a(true);
        }
    }
    
    @SuppressLint({ "NewApi" })
    @Override
    public void d() {
        while (true) {
            super.d();
            while (true) {
                Label_0086: {
                    try {
                        Block_4: {
                            for (final ActivityManager$RunningAppProcessInfo activityManager$RunningAppProcessInfo : ((ActivityManager)this.c.getSystemService("activity")).getRunningAppProcesses()) {
                                if (activityManager$RunningAppProcessInfo.pid == Process.myPid()) {
                                    break Block_4;
                                }
                            }
                            break Label_0086;
                        }
                        final ActivityManager$RunningAppProcessInfo activityManager$RunningAppProcessInfo;
                        if (activityManager$RunningAppProcessInfo.importance == 100) {
                            final int n = 1;
                            if (n != 0) {
                                this.j.a(false);
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
        this.c.setContentView((View)this.l);
    }
    
    public void f() {
        if (this.i.w == 2) {
            ac.b(this.c);
            return;
        }
        ac.c(this.c);
    }
    
    @Override
    public o.a g() {
        final aw a = this.i.a(aw.b.H);
        if (a == null) {
            return com.tremorvideo.sdk.android.videoad.o.a.f;
        }
        final int a2 = a.a("location", -1);
        if (a2 == -1) {
            return com.tremorvideo.sdk.android.videoad.o.a.f;
        }
        return com.tremorvideo.sdk.android.videoad.o.a.values()[a2];
    }
    
    public void h() {
        ac.a(this.c, this.e);
    }
    
    public int i() {
        final Display defaultDisplay = ((WindowManager)this.c.getSystemService("window")).getDefaultDisplay();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return (int)TypedValue.applyDimension(1, 5.0f, displayMetrics);
    }
    
    @Override
    public boolean m() {
        return !this.a.d();
    }
    
    @Override
    public com.tremorvideo.sdk.android.videoad.a.b n() {
        if (this.f) {
            return com.tremorvideo.sdk.android.videoad.a.b.d;
        }
        return com.tremorvideo.sdk.android.videoad.a.b.b;
    }
    
    @Override
    public void o() {
        if (this.j != null) {
            this.j.destroyDrawingCache();
            this.j.e();
            this.l.removeAllViews();
        }
        if (this.i != null) {
            this.i.G();
        }
    }
    
    @Override
    public void p() {
        this.j.loadUrl("about:blank");
        this.j.destroy();
        this.j = null;
    }
    
    public int r() {
        final Display defaultDisplay = ((WindowManager)this.c.getSystemService("window")).getDefaultDisplay();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return (int)TypedValue.applyDimension(1, 25.0f, displayMetrics);
    }
    
    protected void s() {
        if (this.c.getResources().getDisplayMetrics().density < 1.0f) {}
        final int r = this.r();
        final int i = this.i();
        if (this.k == null) {
            final byte[] a = c.a;
            (this.k = (ImageView)new ImageButton((Context)this.c)).setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(a, 0, a.length), r, r, true));
            this.k.setBackgroundDrawable((Drawable)null);
            this.k.setLayoutParams(new ViewGroup$LayoutParams(-1, -1));
            this.k.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    com.tremorvideo.sdk.android.videoad.g.this.a(false);
                }
            });
        }
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(r, r);
        relativeLayout$LayoutParams.addRule(11);
        relativeLayout$LayoutParams.setMargins(i, i, i, i);
        this.l.removeView((View)this.k);
        this.l.addView((View)this.k, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
    }
    
    protected void t() {
        final AlphaAnimation alphaAnimation = new AlphaAnimation(255.0f, 255.0f);
        alphaAnimation.setDuration(0L);
        alphaAnimation.setFillAfter(true);
        this.k.startAnimation((Animation)alphaAnimation);
    }
    
    protected void u() {
        final AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 0.0f);
        alphaAnimation.setDuration(0L);
        alphaAnimation.setFillAfter(true);
        this.k.startAnimation((Animation)alphaAnimation);
    }
    
    public void v() {
        if (this.h) {
            this.g.purge();
            this.g.cancel();
            this.g = null;
            this.a(null, this.h = false);
        }
    }
    
    private class a implements Runnable
    {
        private com.tremorvideo.sdk.android.videoad.a b;
        
        public a(final com.tremorvideo.sdk.android.videoad.a b) {
            this.b = b;
        }
        
        @Override
        public void run() {
            com.tremorvideo.sdk.android.videoad.g.this.d.a(this.b);
        }
    }
}
