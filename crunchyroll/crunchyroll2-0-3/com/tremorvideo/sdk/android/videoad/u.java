// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.text.TextPaint;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.KeyEvent;
import android.content.res.Configuration;
import android.content.res.Resources;
import java.util.TimerTask;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.graphics.drawable.Drawable;
import android.view.View$OnClickListener;
import android.graphics.Typeface;
import android.widget.TextView;
import android.view.animation.AlphaAnimation;
import java.util.Collection;
import android.media.MediaPlayer$OnCompletionListener;
import android.media.MediaPlayer$OnPreparedListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer$OnErrorListener;
import android.net.Uri;
import android.content.DialogInterface$OnCancelListener;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import java.util.Iterator;
import java.util.ArrayList;
import android.view.animation.Animation;
import android.view.animation.Animation$AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.view.ViewGroup;
import android.os.SystemClock;
import android.widget.FrameLayout$LayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.LinearLayout;
import android.content.Context;
import java.util.List;
import android.widget.RelativeLayout;
import android.app.AlertDialog;
import java.util.Timer;
import android.app.Activity;
import android.view.View;
import android.widget.VideoView;
import android.os.Handler;
import android.widget.FrameLayout;

public class u extends FrameLayout implements au.c, ax.c
{
    private static String aa;
    private static int ab;
    long A;
    boolean B;
    boolean C;
    boolean D;
    int E;
    int F;
    boolean G;
    Handler H;
    au I;
    Runnable J;
    private t K;
    private ax L;
    private VideoView M;
    private e N;
    private ad O;
    private a P;
    private aq Q;
    private int R;
    private boolean S;
    private boolean T;
    private boolean U;
    private boolean V;
    private boolean W;
    private boolean Z;
    boolean a;
    private int ac;
    private View ad;
    private boolean ae;
    private Activity af;
    private com.tremorvideo.sdk.android.videoad.a.a ag;
    private Timer ah;
    private long ai;
    private long aj;
    private long ak;
    private AlertDialog al;
    private Runnable am;
    int b;
    v c;
    v d;
    boolean e;
    boolean f;
    int g;
    boolean h;
    boolean i;
    bs j;
    int k;
    int l;
    RelativeLayout m;
    boolean n;
    boolean o;
    boolean p;
    boolean q;
    boolean r;
    long s;
    long t;
    List<aw> u;
    int v;
    boolean w;
    n x;
    boolean y;
    boolean z;
    
    static {
        u.aa = "";
        u.ab = 0;
    }
    
    public u(final Activity af, final com.tremorvideo.sdk.android.videoad.a.a ag, final t t, final int e, final int f) {
        super((Context)af);
        this.K = null;
        this.Q = null;
        this.a = false;
        this.b = 0;
        this.e = false;
        this.f = true;
        this.h = false;
        this.i = false;
        this.n = false;
        this.o = false;
        this.p = true;
        this.q = true;
        this.r = false;
        this.s = 0L;
        this.t = 0L;
        this.S = false;
        this.T = false;
        this.U = false;
        this.V = false;
        this.W = false;
        this.Z = false;
        this.ac = -1;
        this.ad = null;
        this.ae = true;
        this.v = 0;
        this.w = true;
        this.B = false;
        this.C = false;
        this.D = false;
        this.ah = null;
        this.ai = 0L;
        this.aj = 0L;
        this.ak = -1L;
        this.al = null;
        this.E = 0;
        this.F = 0;
        this.H = new Handler();
        this.I = com.tremorvideo.sdk.android.videoad.ac.C();
        this.J = new Runnable() {
            @Override
            public void run() {
                com.tremorvideo.sdk.android.videoad.ac.e("ASSET DOWNLOAD TIMEOUT");
                if (com.tremorvideo.sdk.android.videoad.u.this.I != null && com.tremorvideo.sdk.android.videoad.u.this.I.k()) {
                    com.tremorvideo.sdk.android.videoad.ac.e("Ad is already loading :: don't time out");
                    return;
                }
                if (com.tremorvideo.sdk.android.videoad.u.this.x == null) {
                    com.tremorvideo.sdk.android.videoad.u.this.I.d();
                    com.tremorvideo.sdk.android.videoad.u.this.i();
                    return;
                }
                if (!(com.tremorvideo.sdk.android.videoad.u.this.x instanceof r) && (com.tremorvideo.sdk.android.videoad.u.this.x instanceof t || com.tremorvideo.sdk.android.videoad.u.this.x.r) && (!(com.tremorvideo.sdk.android.videoad.u.this.x instanceof s) || com.tremorvideo.sdk.android.videoad.u.this.x.r)) {
                    com.tremorvideo.sdk.android.videoad.u.this.I.d();
                    com.tremorvideo.sdk.android.videoad.u.this.i();
                    return;
                }
                if (com.tremorvideo.sdk.android.videoad.u.this.v > 1) {
                    com.tremorvideo.sdk.android.videoad.u.this.I.d();
                    com.tremorvideo.sdk.android.videoad.u.this.i();
                    return;
                }
                com.tremorvideo.sdk.android.videoad.u.this.I.d();
                com.tremorvideo.sdk.android.videoad.u.this.x = null;
                com.tremorvideo.sdk.android.videoad.u.this.b();
            }
        };
        this.am = new Runnable() {
            @Override
            public void run() {
                if (!com.tremorvideo.sdk.android.videoad.u.this.M.isPlaying()) {
                    if (com.tremorvideo.sdk.android.videoad.u.this.o) {
                        com.tremorvideo.sdk.android.videoad.u.this.D();
                    }
                }
                else {
                    final int currentPosition = com.tremorvideo.sdk.android.videoad.u.this.M.getCurrentPosition();
                    com.tremorvideo.sdk.android.videoad.u.this.e(currentPosition);
                    com.tremorvideo.sdk.android.videoad.u.this.f(currentPosition);
                    com.tremorvideo.sdk.android.videoad.u.this.N.a(com.tremorvideo.sdk.android.videoad.u.this.C());
                }
                com.tremorvideo.sdk.android.videoad.u.this.N.postDelayed((Runnable)this, 500L);
            }
        };
        this.af = af;
        this.ag = ag;
        com.tremorvideo.sdk.android.videoad.u.aa = "invalid session id";
        this.E = e;
        this.F = f;
        if (t != null) {
            this.a(t);
        }
        else if (com.tremorvideo.sdk.android.videoad.ac.d == 1) {
            this.v = 0;
            final LinearLayout linearLayout = new LinearLayout((Context)this.af);
            final ProgressBar progressBar = new ProgressBar((Context)this.af, (AttributeSet)null, 16842874);
            progressBar.setLayoutParams(new ViewGroup$LayoutParams(-2, -2));
            linearLayout.setGravity(17);
            linearLayout.addView((View)progressBar);
            this.addView((View)linearLayout, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
        }
    }
    
    private boolean A() {
        return this.K.N() && this.q && !this.p() && !this.o;
    }
    
    private String B() {
        if (!this.K.Q) {
            return "";
        }
        if (this.b > 0) {
            return "Advertisement: " + this.K.G();
        }
        return "Ads by Tremor Video: " + this.K.G();
    }
    
    private String C() {
        final int currentPosition = this.M.getCurrentPosition();
        if (!this.K.Q) {
            return "";
        }
        if (currentPosition < 5000 && this.b == 0) {
            return "Ads by Tremor Video: " + String.valueOf(this.E());
        }
        return "Advertisement: " + String.valueOf(this.E());
    }
    
    private void D() {
        try {
            final aw t = this.K.T();
            if (t != null) {
                final String s = t.f().get("auto-skip");
                if (s != null) {
                    final long n = Long.parseLong(s) * 1000L;
                    if (this.s < n) {
                        final long g = com.tremorvideo.sdk.android.videoad.ac.G();
                        final long t2 = this.t;
                        this.t = g;
                        if (this.L.c() == -1 && !this.h) {
                            this.s += g - t2;
                            if (this.s >= n) {
                                bx.a(bx.a.b, true, 1);
                                this.m();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            com.tremorvideo.sdk.android.videoad.ac.a(ex);
        }
    }
    
    private int E() {
        if (this.R != 0) {
            final int round = Math.round((this.R - this.M.getCurrentPosition()) / 1000.0f);
            if (round >= 0) {
                return round;
            }
        }
        return 0;
    }
    
    private void F() {
        this.b(false);
    }
    
    private void G() {
        this.I();
        if (!this.C) {
            this.ag.l();
        }
        this.af.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                if (com.tremorvideo.sdk.android.videoad.u.this.al != null && com.tremorvideo.sdk.android.videoad.u.this.al.isShowing()) {
                    com.tremorvideo.sdk.android.videoad.u.this.al.dismiss();
                }
                com.tremorvideo.sdk.android.videoad.u.this.al = null;
                com.tremorvideo.sdk.android.videoad.u.this.M.destroyDrawingCache();
                com.tremorvideo.sdk.android.videoad.u.this.ag.a((com.tremorvideo.sdk.android.videoad.a)null);
            }
        });
    }
    
    private void H() {
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        final long n = elapsedRealtime - this.ai;
        this.ai = elapsedRealtime;
        if (!this.h && !this.o && !this.i && !this.L.c) {
            if (this.B) {
                if (this.aj > -1L) {
                    this.aj += n;
                    if (this.aj > 5000L) {
                        this.i = true;
                        this.aj = -1L;
                        if (this.al != null && this.al.isShowing()) {
                            this.al.dismiss();
                        }
                        this.G();
                    }
                }
            }
            else {
                if (this.ak > -1L) {
                    final long ak = this.M.getCurrentPosition();
                    if (ak == this.ak) {
                        this.aj += n;
                    }
                    else {
                        this.aj = 0L;
                    }
                    this.ak = ak;
                }
                else {
                    this.aj += n;
                }
                if (this.aj > com.tremorvideo.sdk.android.videoad.ac.g) {
                    this.b(true);
                }
            }
        }
    }
    
    private void I() {
        if (this.ah != null) {
            this.ah.cancel();
            this.ah.purge();
            this.ah = null;
        }
    }
    
    private View a(final View view, final t.b b) {
        int e = -1;
        if (this.Q != null) {
            ((ViewGroup)this.Q.getParent()).removeView((View)this.Q);
        }
        else {
            (this.Q = com.tremorvideo.sdk.android.videoad.ac.a((Context)this.af)).a(b.b());
            this.Q.a((aq.d)new aq.d() {
                @Override
                public boolean a(final String s) {
                    final aw c = com.tremorvideo.sdk.android.videoad.u.this.K.c(s);
                    if (c != null) {
                        com.tremorvideo.sdk.android.videoad.u.this.f(c);
                    }
                    return false;
                }
            });
        }
        if (b.g() == com.tremorvideo.sdk.android.videoad.t.c.e) {
            final RelativeLayout relativeLayout = new RelativeLayout((Context)this.af);
            final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(-1, -1);
            final RelativeLayout$LayoutParams relativeLayout$LayoutParams2 = new RelativeLayout$LayoutParams(b.e(), b.f());
            relativeLayout$LayoutParams2.topMargin = b.c();
            relativeLayout$LayoutParams2.leftMargin = b.d();
            relativeLayout.addView(view, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
            relativeLayout.addView((View)this.Q, (ViewGroup$LayoutParams)relativeLayout$LayoutParams2);
            return (View)relativeLayout;
        }
        if (b.g().b()) {
            final LinearLayout linearLayout = new LinearLayout((Context)this.af);
            linearLayout.setOrientation(0);
            final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(0, -1);
            linearLayout$LayoutParams.weight = 1.0f;
            final int e2 = b.e();
            int f;
            if (b.f() == 0) {
                f = -1;
            }
            else {
                f = b.f();
            }
            final LinearLayout$LayoutParams linearLayout$LayoutParams2 = new LinearLayout$LayoutParams(e2, f);
            linearLayout$LayoutParams2.gravity = 16;
            if (b.g() == com.tremorvideo.sdk.android.videoad.t.c.c) {
                linearLayout.addView((View)this.Q, (ViewGroup$LayoutParams)linearLayout$LayoutParams2);
                linearLayout.addView(view, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
            }
            else {
                linearLayout.addView(view, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
                linearLayout.addView((View)this.Q, (ViewGroup$LayoutParams)linearLayout$LayoutParams2);
            }
            return (View)linearLayout;
        }
        if (!b.g().a()) {
            return null;
        }
        final LinearLayout linearLayout2 = new LinearLayout((Context)this.af);
        linearLayout2.setOrientation(1);
        final LinearLayout$LayoutParams linearLayout$LayoutParams3 = new LinearLayout$LayoutParams(-1, 0);
        linearLayout$LayoutParams3.weight = 1.0f;
        if (b.e() != 0) {
            e = b.e();
        }
        final LinearLayout$LayoutParams linearLayout$LayoutParams4 = new LinearLayout$LayoutParams(e, b.f());
        linearLayout$LayoutParams4.gravity = 1;
        if (b.g() == com.tremorvideo.sdk.android.videoad.t.c.b) {
            linearLayout2.addView(view, (ViewGroup$LayoutParams)linearLayout$LayoutParams3);
            linearLayout2.addView((View)this.Q, (ViewGroup$LayoutParams)linearLayout$LayoutParams4);
            return (View)linearLayout2;
        }
        linearLayout2.addView((View)this.Q, (ViewGroup$LayoutParams)linearLayout$LayoutParams4);
        linearLayout2.addView(view, (ViewGroup$LayoutParams)linearLayout$LayoutParams3);
        return (View)linearLayout2;
    }
    
    private void a(final int n, final boolean b) {
        if (this.p) {
            this.N.measure(-1, -1);
            final TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float)(-this.N.getMeasuredHeight()));
            animation.setInterpolator((Interpolator)new DecelerateInterpolator());
            animation.startNow();
            long duration;
            if (b) {
                duration = 500L;
            }
            else {
                duration = 0L;
            }
            animation.setDuration(duration);
            animation.setFillAfter(true);
            animation.setFillBefore(true);
            if (n > 0) {
                animation.setStartTime(AnimationUtils.currentAnimationTimeMillis() + n);
            }
            animation.setAnimationListener((Animation$AnimationListener)new Animation$AnimationListener() {
                public void onAnimationEnd(final Animation animation) {
                    com.tremorvideo.sdk.android.videoad.u.this.N.clearAnimation();
                    com.tremorvideo.sdk.android.videoad.u.this.N.setVisibility(8);
                }
                
                public void onAnimationRepeat(final Animation animation) {
                }
                
                public void onAnimationStart(final Animation animation) {
                }
            });
            this.N.setAnimation((Animation)animation);
            this.p = false;
        }
    }
    
    private void a(final boolean b) {
        if (this.A() != this.p) {
            if (!this.p) {
                this.b(0, b);
                return;
            }
            this.a(0, b);
        }
    }
    
    private void b(final int n) {
        this.ad.measure(-1, -1);
        final TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, (float)(-this.ad.getMeasuredHeight() * 2), 0.0f);
        animation.setDuration(750L);
        animation.setFillAfter(true);
        animation.setFillBefore(true);
        animation.setInterpolator((Interpolator)new DecelerateInterpolator());
        if (n > 0) {
            animation.setStartTime(AnimationUtils.currentAnimationTimeMillis() + n);
        }
        this.ad.setAnimation((Animation)animation);
        this.a(n - 200, true);
    }
    
    private void b(final int n, final boolean b) {
        if (!this.p && this.A()) {
            final TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, (float)(-this.N.getHeight()), 0.0f);
            animation.setInterpolator((Interpolator)new DecelerateInterpolator());
            animation.startNow();
            long duration;
            if (b) {
                duration = 500L;
            }
            else {
                duration = 0L;
            }
            animation.setDuration(duration);
            if (n > 0) {
                animation.setStartTime(AnimationUtils.currentAnimationTimeMillis() + n);
            }
            this.N.setAnimation((Animation)animation);
            this.p = true;
        }
    }
    
    private void b(final t t) {
        this.u = new ArrayList<aw>();
        for (final aw aw : this.K.l()) {
            if (aw.a() == aw.b.j) {
                this.u.add(aw);
            }
            else if (aw.a() == aw.b.x) {
                this.u.add(aw);
            }
            else {
                if (aw.b() < 0) {
                    continue;
                }
                this.u.add(aw);
            }
        }
    }
    
    private void b(final boolean c) {
        if (!this.B) {
            this.aj = -1L;
            this.B = true;
            this.C = c;
            this.af.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (com.tremorvideo.sdk.android.videoad.u.this.al == null) {
                        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)com.tremorvideo.sdk.android.videoad.u.this.af);
                        alertDialog$Builder.setTitle((CharSequence)"Cannot play video");
                        alertDialog$Builder.setMessage((CharSequence)"Sorry, this video cannot be played");
                        alertDialog$Builder.setPositiveButton((CharSequence)"OK", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                            public void onClick(final DialogInterface dialogInterface, final int n) {
                                com.tremorvideo.sdk.android.videoad.u.this.G();
                            }
                        });
                        com.tremorvideo.sdk.android.videoad.u.this.al = alertDialog$Builder.create();
                        com.tremorvideo.sdk.android.videoad.u.this.al.setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
                            public void onCancel(final DialogInterface dialogInterface) {
                                com.tremorvideo.sdk.android.videoad.u.this.G();
                            }
                        });
                    }
                    com.tremorvideo.sdk.android.videoad.u.this.al.show();
                    com.tremorvideo.sdk.android.videoad.u.this.aj = 0L;
                    if (com.tremorvideo.sdk.android.videoad.u.this.M.isPlaying()) {
                        com.tremorvideo.sdk.android.videoad.u.this.M.stopPlayback();
                    }
                    com.tremorvideo.sdk.android.videoad.u.this.M.setVisibility(8);
                }
            });
        }
    }
    
    private void c(final int n) {
        this.u();
        this.q();
        this.d(n);
        if (this.o) {
            this.z();
        }
    }
    
    private void c(final aw aw) {
        synchronized (this) {
            if (!this.i && (!this.M.isPlaying() || this.M.getCurrentPosition() < this.M.getDuration() - 200)) {
                this.f(aw);
            }
        }
    }
    
    private void c(final t k) {
        com.tremorvideo.sdk.android.videoad.u.aa = "";
        this.K = k;
        this.j = k.f();
        this.b(k);
        final boolean a = this.j.a("orientation");
        this.S = a;
        if (a) {
            com.tremorvideo.sdk.android.videoad.ac.e("Compatibity: orientation");
        }
        final boolean a2 = this.j.a("alpha-disable-blending");
        this.U = a2;
        if (a2) {
            com.tremorvideo.sdk.android.videoad.ac.e("Compatibity: alpha-disable-blending");
        }
        final boolean a3 = this.j.a("lock-orientation");
        this.T = a3;
        if (a3) {
            com.tremorvideo.sdk.android.videoad.ac.e("Compatibity: lock-orientation");
        }
        final boolean v = com.tremorvideo.sdk.android.videoad.ac.r() > 18 || this.j.a("manual-measure");
        this.V = v;
        if (v) {
            com.tremorvideo.sdk.android.videoad.ac.e("Compatibity: manual-measure");
        }
        final boolean a4 = this.j.a("pause-bug");
        this.W = a4;
        if (a4) {
            com.tremorvideo.sdk.android.videoad.ac.e("Compatibity: pause-bug");
        }
        final boolean a5 = this.j.a("disable-video-tag-autoplay");
        this.Z = a5;
        if (a5) {
            com.tremorvideo.sdk.android.videoad.ac.e("Compatibity: disable-video-tag-autoplay");
        }
        final String[] split = this.K.c(0).split("-");
        if (split.length > 1) {
            final String[] split2 = split[1].split("x");
            if (split2.length == 2) {
                this.k = Integer.parseInt(split2[0]);
                this.l = Integer.parseInt(split2[1]);
            }
        }
    }
    
    private void d(final int n) {
        this.B = false;
        this.D = false;
        if (this.K.r) {
            this.M.setVideoURI(Uri.parse(this.K.a(0)));
        }
        else {
            this.M.setVideoPath(this.K.a((Context)this.af, 0));
        }
        this.M.requestFocus();
        this.M.setOnErrorListener((MediaPlayer$OnErrorListener)new MediaPlayer$OnErrorListener() {
            public boolean onError(final MediaPlayer mediaPlayer, final int n, final int n2) {
                com.tremorvideo.sdk.android.videoad.ac.e("Media player error: What: " + n + " Extra: " + n2);
                return false;
            }
        });
        this.M.setOnPreparedListener((MediaPlayer$OnPreparedListener)new MediaPlayer$OnPreparedListener() {
            public void onPrepared(final MediaPlayer mediaPlayer) {
                com.tremorvideo.sdk.android.videoad.u.this.D = true;
                if (n > 0 && (com.tremorvideo.sdk.android.videoad.ac.r() >= 19 || !com.tremorvideo.sdk.android.videoad.u.this.K.a(0).endsWith("m3u8"))) {
                    com.tremorvideo.sdk.android.videoad.u.this.M.seekTo(n);
                }
                if (!com.tremorvideo.sdk.android.videoad.u.this.K.r || !com.tremorvideo.sdk.android.videoad.u.this.h) {
                    com.tremorvideo.sdk.android.videoad.u.this.y();
                }
                final int duration = com.tremorvideo.sdk.android.videoad.u.this.M.getDuration();
                if (duration > 0) {
                    com.tremorvideo.sdk.android.videoad.u.this.R = duration;
                }
                else {
                    com.tremorvideo.sdk.android.videoad.u.this.R = com.tremorvideo.sdk.android.videoad.u.this.K.G;
                }
                if (com.tremorvideo.sdk.android.videoad.u.this.K.g(com.tremorvideo.sdk.android.videoad.u.this.R)) {
                    com.tremorvideo.sdk.android.videoad.u.this.b(com.tremorvideo.sdk.android.videoad.u.this.K);
                }
                if (n == 0) {
                    com.tremorvideo.sdk.android.videoad.u.this.N.a(com.tremorvideo.sdk.android.videoad.u.this.B());
                }
                com.tremorvideo.sdk.android.videoad.u.this.N.postDelayed(com.tremorvideo.sdk.android.videoad.u.this.am, 10L);
                com.tremorvideo.sdk.android.videoad.u.this.M.setOnPreparedListener((MediaPlayer$OnPreparedListener)null);
                com.tremorvideo.sdk.android.videoad.u.this.aj = -1L;
                com.tremorvideo.sdk.android.videoad.u.this.ak = n;
            }
        });
        this.M.setOnErrorListener((MediaPlayer$OnErrorListener)new MediaPlayer$OnErrorListener() {
            public boolean onError(final MediaPlayer mediaPlayer, final int n, final int n2) {
                com.tremorvideo.sdk.android.videoad.u.this.F();
                return true;
            }
        });
        this.M.setOnCompletionListener((MediaPlayer$OnCompletionListener)new MediaPlayer$OnCompletionListener() {
            public void onCompletion(final MediaPlayer mediaPlayer) {
                if (!com.tremorvideo.sdk.android.videoad.u.this.B) {
                    com.tremorvideo.sdk.android.videoad.u.this.f(com.tremorvideo.sdk.android.videoad.u.this.R);
                    if (!com.tremorvideo.sdk.android.videoad.u.this.K.R()) {
                        com.tremorvideo.sdk.android.videoad.u.this.I();
                        com.tremorvideo.sdk.android.videoad.u.this.i = true;
                        com.tremorvideo.sdk.android.videoad.u.this.M.stopPlayback();
                        com.tremorvideo.sdk.android.videoad.u.this.m();
                        return;
                    }
                    com.tremorvideo.sdk.android.videoad.u.this.M.pause();
                    com.tremorvideo.sdk.android.videoad.u.this.o = true;
                    final u a = com.tremorvideo.sdk.android.videoad.u.this;
                    ++a.b;
                    com.tremorvideo.sdk.android.videoad.u.this.z();
                }
            }
        });
        this.aj = 0L;
    }
    
    private void d(final aw aw) {
        if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.j) {
            this.L.a(aw, this.b);
        }
        else {
            this.L.a(aw);
        }
        this.s = Long.MAX_VALUE;
    }
    
    private void e(int round) {
        synchronized (this) {
            if (this.O != null) {
                round = Math.round((this.K.Q() - round) / 1000.0f);
                this.O.a(round);
            }
            else if (this.K.P() && !this.a && round >= this.K.Q()) {
                this.a = true;
                this.c.e();
            }
        }
    }
    
    private void e(final aw aw) {
        this.L.b(aw);
    }
    
    private void f(final int n) {
    Label_0036_Outer:
        while (true) {
            ArrayList<aw> list = null;
        Label_0166:
            while (true) {
                aw aw = null;
                Label_0097: {
                    synchronized (this) {
                        list = new ArrayList<aw>(this.u.size());
                        if (!this.B) {
                            final Iterator<aw> iterator = this.u.iterator();
                            while (iterator.hasNext()) {
                                aw = iterator.next();
                                if (aw.a() != com.tremorvideo.sdk.android.videoad.aw.b.j) {
                                    break Label_0097;
                                }
                                if (n < aw.e()) {
                                    continue Label_0036_Outer;
                                }
                                list.add(aw);
                                this.f(aw);
                            }
                            break Label_0166;
                        }
                        break;
                    }
                }
                if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.x) {
                    if (n >= aw.e()) {
                        list.add(aw);
                        this.f(aw);
                        continue;
                    }
                    continue;
                }
                else {
                    if (aw.b() >= 0 && n >= aw.b()) {
                        list.add(aw);
                        this.h(aw);
                        continue;
                    }
                    continue;
                }
                break;
            }
            this.u.removeAll(list);
            break;
        }
    }
    // monitorexit(this)
    
    private void f(final aw aw) {
        synchronized (this) {
            if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.v) {
                this.w();
            }
            else {
                this.L.c(aw);
            }
        }
    }
    
    private View g(final aw aw) {
        final AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500L);
        animation.setFillAfter(true);
        final Context context = this.M.getContext();
        final c c = new c(context);
        final TextView textView = new TextView(context);
        textView.setText((CharSequence)aw.h());
        textView.setTextColor(this.K.q().a(bw.c.a));
        textView.setShadowLayer(6.0f, 0.0f, 0.0f, this.K.q().a(bw.c.b));
        textView.setGravity(1);
        textView.setTypeface(Typeface.create("helvetica", 1));
        textView.setTextSize(2, (float)com.tremorvideo.sdk.android.videoad.ac.L());
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setGravity(17);
        linearLayout.setOrientation(1);
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(-2, -2);
        linearLayout$LayoutParams.gravity = 17;
        linearLayout.addView((View)c, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
        linearLayout.addView((View)textView);
        linearLayout.setAnimation((Animation)animation);
        c.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                com.tremorvideo.sdk.android.videoad.u.this.m.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        com.tremorvideo.sdk.android.videoad.u.this.m.removeView(linearLayout);
                    }
                });
                com.tremorvideo.sdk.android.videoad.u.this.d(aw);
                com.tremorvideo.sdk.android.videoad.u.this.s = Long.MAX_VALUE;
                com.tremorvideo.sdk.android.videoad.u.this.o = false;
                com.tremorvideo.sdk.android.videoad.u.this.b(com.tremorvideo.sdk.android.videoad.u.this.K);
                com.tremorvideo.sdk.android.videoad.u.this.a(true);
                com.tremorvideo.sdk.android.videoad.u.this.d(0);
            }
        });
        return (View)linearLayout;
    }
    
    private void h(final aw aw) {
        if (this.c != null) {
            this.c.a(aw);
        }
        if (this.d != null) {
            this.d.a(aw);
        }
    }
    
    private void n() {
        if (com.tremorvideo.sdk.android.videoad.u.aa != this.j.c()) {
            com.tremorvideo.sdk.android.videoad.ac.e("Playing Video Format: " + this.K.c(0));
            if (!this.K.b()) {
                this.K.a((Context)this.af);
            }
            com.tremorvideo.sdk.android.videoad.u.aa = this.j.c();
            com.tremorvideo.sdk.android.videoad.u.ab = 0;
            this.s = 0L;
            this.u();
            this.q();
        }
    }
    
    private void o() {
        if (this.ad != null && this.ac == -1) {
            if (!this.p()) {
                this.ad.setVisibility(4);
                this.ad.setAnimation((Animation)null);
                this.b(0, true);
                return;
            }
            if (this.ad.getVisibility() != 0) {
                this.ad.setVisibility(0);
                this.b(0);
            }
            if (this.ad instanceof bd) {
                ((bd)this.ad).a();
            }
        }
    }
    
    private boolean p() {
        return this.d == null && this.ac == -1 && this.ad != null;
    }
    
    private void q() {
        if (this.K.H()) {
            int s = 0;
            if (this.T) {
                s = this.s();
            }
            this.ad = this.K.I().a((Context)this.af, s, this.K.q());
        }
        else {
            this.ad = this.r();
        }
        if (this.ad != null) {
            this.m.addView(this.ad, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-2, -2));
            this.o();
            if (this.p()) {
                this.b(2000);
            }
        }
    }
    
    private View r() {
        if (this.K.J() && this.ac == -1 && this.ae) {
            return new bc(this.K.q(), this.K.K(), this.K.L(), this.K.M()).a((Context)this.af, (bc.a)new bc.a() {
                @Override
                public void a(final int n) {
                    if (com.tremorvideo.sdk.android.videoad.u.this.ac == -1) {
                        com.tremorvideo.sdk.android.videoad.u.this.ac = n;
                        final TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float)(-com.tremorvideo.sdk.android.videoad.u.this.ad.getMeasuredHeight() * 2));
                        animation.setDuration(500L);
                        animation.setFillAfter(true);
                        animation.setFillBefore(true);
                        animation.setInterpolator((Interpolator)new DecelerateInterpolator());
                        animation.setStartTime(AnimationUtils.currentAnimationTimeMillis() + 200L);
                        com.tremorvideo.sdk.android.videoad.u.this.ad.setAnimation((Animation)animation);
                        if (!com.tremorvideo.sdk.android.videoad.u.this.o) {
                            com.tremorvideo.sdk.android.videoad.u.this.b(400, true);
                        }
                    }
                }
            });
        }
        return null;
    }
    
    private int s() {
        int t = this.t();
        if (this.K.U() != null) {
            t -= this.K.U().a();
        }
        return t;
    }
    
    private int t() {
        return this.E;
    }
    
    private void u() {
        final View$OnClickListener view$OnClickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                com.tremorvideo.sdk.android.videoad.u.this.c((aw)view.getTag());
            }
        };
        final int s = this.s();
        this.G = (s / com.tremorvideo.sdk.android.videoad.ac.O() <= 320.0f);
        (this.N = new e((Context)this.af, this.K.q().a(bw.c.c), s)).setId(1);
        this.N.setTag((Object)"");
        this.N.setBackgroundDrawable(new d());
        this.N.a(this.B());
        this.q = true;
        this.p = true;
        this.O = null;
        final aw n = this.K.n();
        if (this.K.V()) {
            aw aw;
            if ((aw = n) == null) {
                aw = new aw(com.tremorvideo.sdk.android.videoad.aw.b.v, 0, null, "Skip");
            }
            (this.O = new ad((Context)this.af, (View$OnClickListener)view$OnClickListener, aw, this.K.q())).a(this.K.Q() / 1000);
        }
        this.c = new v((Context)this.af, (View$OnClickListener)view$OnClickListener, this.K, s, true);
        if (this.a) {
            this.c.f();
        }
        if (this.c.a()) {
            this.d = new v((Context)this.af, (View$OnClickListener)view$OnClickListener, this.K, this.c.b(), s, true);
            if (this.d.c() >= s - this.N.a()) {
                this.q = false;
            }
        }
        final LinearLayout linearLayout = new LinearLayout((Context)this.af);
        linearLayout.setGravity(17);
        this.M = new ap((Context)this.af, this.N.getHeight(), this.k, this.l, this.E, this.F);
        if (com.tremorvideo.sdk.android.videoad.ac.w()) {
            this.M.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
                boolean a = false;
                
                public boolean onTouch(final View view, final MotionEvent motionEvent) {
                    final VideoView videoView = (VideoView)view;
                    this.a = !this.a;
                    if (this.a) {
                        videoView.pause();
                        return false;
                    }
                    videoView.start();
                    return false;
                }
            });
        }
        linearLayout.addView((View)this.M);
        final RelativeLayout relativeLayout = new RelativeLayout((Context)this.af);
        relativeLayout.setBackgroundColor(-16777216);
        relativeLayout.addView((View)linearLayout, (ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(-2, -2);
        relativeLayout$LayoutParams.addRule(10);
        relativeLayout$LayoutParams.addRule(11);
        relativeLayout.addView((View)this.N, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        if (!this.K.Q || !this.q) {
            this.a(0, false);
        }
        if (this.O != null) {
            final RelativeLayout$LayoutParams relativeLayout$LayoutParams2 = new RelativeLayout$LayoutParams(-2, -2);
            relativeLayout$LayoutParams2.addRule(11);
            relativeLayout$LayoutParams2.addRule(12);
            relativeLayout$LayoutParams2.bottomMargin = this.O.c;
            relativeLayout.addView((View)this.O, (ViewGroup$LayoutParams)relativeLayout$LayoutParams2);
        }
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams3 = new RelativeLayout$LayoutParams(-2, -2);
        relativeLayout$LayoutParams3.addRule(12);
        relativeLayout$LayoutParams3.addRule(14);
        relativeLayout.addView(this.c.d(), (ViewGroup$LayoutParams)relativeLayout$LayoutParams3);
        if (this.d != null) {
            final RelativeLayout$LayoutParams relativeLayout$LayoutParams4 = new RelativeLayout$LayoutParams(-2, -2);
            relativeLayout$LayoutParams4.addRule(10);
            relativeLayout$LayoutParams4.addRule(5);
            relativeLayout.addView(this.d.d(), (ViewGroup$LayoutParams)relativeLayout$LayoutParams4);
        }
        this.a(false);
        final aw a = this.K.a(aw.b.H);
        if (a != null) {
            relativeLayout.addView((View)(this.P = new a((Context)this.af, a)), (ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
        }
        if (this.K.U() == null) {
            this.m = relativeLayout;
            this.removeAllViews();
            this.addView((View)relativeLayout, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
            return;
        }
        final View a2 = this.a((View)relativeLayout, this.K.U());
        this.m = relativeLayout;
        new LinearLayout$LayoutParams(-1, -1).gravity = 17;
        this.removeAllViews();
        this.addView(a2, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
    }
    
    private void v() {
        new View$OnClickListener() {
            public void onClick(final View view) {
                com.tremorvideo.sdk.android.videoad.u.this.f((aw)view.getTag());
            }
        };
        final RelativeLayout m = this.m;
        this.s();
        if (this.c != null) {
            m.removeView(this.c.d());
        }
        if (this.d != null) {
            m.removeView(this.d.d());
        }
        if (this.P != null) {
            m.removeView((View)this.P);
        }
        if (this.a) {
            this.c.f();
        }
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(-2, -2);
        relativeLayout$LayoutParams.addRule(12);
        relativeLayout$LayoutParams.addRule(14);
        m.addView(this.c.d(), (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        if (this.d != null) {
            final RelativeLayout$LayoutParams relativeLayout$LayoutParams2 = new RelativeLayout$LayoutParams(-2, -2);
            relativeLayout$LayoutParams2.addRule(10);
            relativeLayout$LayoutParams2.addRule(5);
            m.addView(this.d.d(), (ViewGroup$LayoutParams)relativeLayout$LayoutParams2);
        }
        if (this.P != null) {
            m.addView((View)this.P, (ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
        }
    }
    
    private void w() {
        try {
            boolean b = this.a;
            if (this.O != null) {
                b = this.O.b();
            }
            if (b && this.K.P()) {
                this.I();
                final aw n = this.K.n();
                if (n != null) {
                    this.d(n);
                }
                this.n = true;
                this.M.pause();
                this.M.stopPlayback();
                this.M.destroyDrawingCache();
                this.m();
            }
        }
        catch (Exception ex) {
            com.tremorvideo.sdk.android.videoad.ac.a(ex);
        }
    }
    
    private void x() {
        if (this.M != null && this.M.isPlaying()) {
            this.M.pause();
            com.tremorvideo.sdk.android.videoad.u.ab = this.M.getCurrentPosition();
        }
    }
    
    private void y() {
        if (!this.L.d() && !this.o && this.M != null && !this.h && this.D) {
            this.M.start();
        }
    }
    
    private void z() {
        this.m.addView(this.g(this.K.T()), (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
        this.s = 0L;
        this.t = com.tremorvideo.sdk.android.videoad.ac.G();
        this.a(0, true);
        if (!this.a) {
            this.a = true;
            this.c.e();
        }
    }
    
    public void a() {
        if (com.tremorvideo.sdk.android.videoad.ac.d == 1) {
            if (this.K != null) {
                this.af.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        com.tremorvideo.sdk.android.videoad.u.this.n();
                        com.tremorvideo.sdk.android.videoad.u.this.d();
                    }
                });
            }
            else if (!this.z) {
                this.b();
            }
            return;
        }
        this.n();
        this.d();
    }
    
    public void a(final int n) {
        if (this.L.b()) {
            this.L.e();
            this.e((aw)null);
            if (this.a) {
                this.c.f();
            }
        }
        this.af.finishActivity(11);
    }
    
    public void a(final aw aw) {
        this.x();
    }
    
    public void a(final n x) {
        com.tremorvideo.sdk.android.videoad.ac.e("onCurrentAdDownloadFlowStart");
        this.x = x;
        if (this.x instanceof r) {
            com.tremorvideo.sdk.android.videoad.ac.e("onCurrentAdDownloadFlowStart : AdRichMedia");
        }
        else if (this.x instanceof s) {
            com.tremorvideo.sdk.android.videoad.ac.e("onCurrentAdDownloadFlowStart : AdVAST");
        }
    }
    
    public void a(final t k) {
        this.K = k;
        (this.L = new ax(this.af, this.ag, k.q())).a((ax.c)this);
        this.c(k);
        if (this.K.r) {
            this.ah = new Timer();
            this.ai = SystemClock.elapsedRealtime();
            this.ah.scheduleAtFixedRate(new b(), 10L, 100L);
        }
        this.g = Resources.getSystem().getConfiguration().orientation;
        if (this.g == 2) {
            this.g = 0;
        }
        else {
            this.g = 1;
        }
        if (this.T) {
            this.g = 0;
        }
    }
    
    public void b() {
        if (this.v == 0) {
            this.x = this.I.g();
            if (this.x != null && (this.x instanceof r || (this.x instanceof t && !this.x.r) || (this.x instanceof s && !this.x.r))) {
                this.v = 1;
                this.I.d();
                this.x = null;
            }
        }
        if (this.H != null) {
            this.H.removeCallbacks(this.J);
        }
        Label_0236: {
            if (this.v != 0 && this.v != 1) {
                break Label_0236;
            }
            this.A = SystemClock.elapsedRealtime();
            Label_0218: {
                if (this.v != 1) {
                    break Label_0218;
                }
            Label_0191_Outer:
                while (true) {
                    try {
                        Thread.sleep(1000L);
                        com.tremorvideo.sdk.android.videoad.ac.e("downloadManager.start call streaming true");
                        this.I.a(true, (au.c)this);
                        ++this.v;
                        this.H.postDelayed(this.J, com.tremorvideo.sdk.android.videoad.ac.e + com.tremorvideo.sdk.android.videoad.ac.f);
                        while (true) {
                            com.tremorvideo.sdk.android.videoad.ac.e("attempt=" + this.v);
                            return;
                            this.i();
                            continue;
                        }
                        com.tremorvideo.sdk.android.videoad.ac.e("downloadManager.start call streaming false");
                        this.I.a(false, (au.c)this);
                        continue Label_0191_Outer;
                    }
                    catch (Exception ex) {
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    public void b(final aw aw) {
        this.y();
    }
    
    public void c() {
        this.h = true;
        this.z = true;
        if (!this.W) {
            this.x();
        }
    }
    
    public void d() {
        this.h = false;
        this.z = false;
        if (this.y && com.tremorvideo.sdk.android.videoad.ac.d == 1) {
            this.y = false;
            this.k();
        }
        if (!this.o && this.M != null) {
            if (this.S) {
                this.c(com.tremorvideo.sdk.android.videoad.u.ab);
            }
            else if (com.tremorvideo.sdk.android.videoad.u.ab > 0 && com.tremorvideo.sdk.android.videoad.ac.r() < 19 && this.K.a(0).endsWith("m3u8")) {
                try {
                    this.M.resume();
                }
                catch (Exception ex) {
                    this.F();
                }
            }
            else {
                this.d(com.tremorvideo.sdk.android.videoad.u.ab);
            }
            if (!this.M.isPlaying()) {
                this.y();
            }
        }
    }
    
    public void e() {
        if (this.c != null) {
            this.c.g();
        }
        if (this.d != null) {
            this.d.g();
        }
    }
    
    public void f() {
        if (this.S) {
            this.c(com.tremorvideo.sdk.android.videoad.u.ab);
        }
    }
    
    public void g() {
        try {
            if (this.M != null) {
                this.M.pause();
                this.M.stopPlayback();
                this.M.destroyDrawingCache();
            }
            bx.a(bx.a.b, true, 1);
            this.m();
        }
        catch (Exception ex) {
            com.tremorvideo.sdk.android.videoad.ac.e("TremorLog_error::FrameVideo::stopAd " + ex);
        }
    }
    
    public void h() {
        com.tremorvideo.sdk.android.videoad.ac.e("onRequestDownloadComplete");
    }
    
    public void i() {
        if (this.H != null) {
            this.H.removeCallbacks(this.J);
        }
        bx.a(bx.a.b, false, 0);
        this.af.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                com.tremorvideo.sdk.android.videoad.u.this.m();
            }
        });
    }
    
    public void j() {
        com.tremorvideo.sdk.android.videoad.ac.e("onRequestDownloadTimeout");
        this.i();
    }
    
    public void k() {
        while (true) {
            Label_0128: {
                synchronized (this) {
                    if (this.w) {
                        com.tremorvideo.sdk.android.videoad.ac.e("onAsssetDownloadComplete showAd loop");
                        final au c = com.tremorvideo.sdk.android.videoad.ac.C();
                        if (!c.e(com.tremorvideo.sdk.android.videoad.ac.p())) {
                            bx.a(bx.a.b, false, -1);
                            this.i();
                        }
                        else {
                            this.w = false;
                            final n h = c.h();
                            if (!(h instanceof t) && !(h instanceof s)) {
                                break Label_0128;
                            }
                            bx.a(bx.a.a, new Object[0]);
                            com.tremorvideo.sdk.android.videoad.ac.e("Frame Video Suported Ad present ");
                            this.a(this.K = (t)h);
                            this.a();
                        }
                    }
                    return;
                }
            }
            bx.a(bx.a.b, false, 0);
            com.tremorvideo.sdk.android.videoad.ac.e("Frame Video Suported Ad NOT present ");
            this.i();
        }
    }
    
    public void l() {
        com.tremorvideo.sdk.android.videoad.ac.e("onAsssetDownloadComplete");
        this.H.removeCallbacks(this.J);
        if (this.z) {
            this.y = true;
            return;
        }
        this.k();
    }
    
    public void m() {
        this.I();
        if (this.al != null && this.al.isShowing()) {
            this.al.dismiss();
        }
        this.al = null;
        if (this.ag != null) {
            this.ag.a((com.tremorvideo.sdk.android.videoad.a)null);
        }
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        if (!this.T) {
            if (this.g != configuration.orientation) {
                this.g = configuration.orientation;
                if (this.S) {
                    this.c(this.M.getCurrentPosition());
                }
                else {
                    this.v();
                }
            }
            this.a(false);
            this.o();
        }
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        if (n == 84 || n == 82) {
            return true;
        }
        if (n == 4) {
            this.w();
            return true;
        }
        return false;
    }
    
    private class a extends View
    {
        private o b;
        private RectF c;
        private aw d;
        
        public a(final Context context, final aw d) {
            super(context);
            this.b = new o(this.getResources().getDisplayMetrics());
            this.setFocusable(true);
            this.d = d;
        }
        
        public void onDraw(final Canvas canvas) {
            super.onDraw(canvas);
            canvas.save();
            canvas.translate((float)(this.getWidth() / 2), (float)(this.getHeight() / 2));
            String s;
            if ((s = this.d.f().get("location")) == null) {
                s = "0";
            }
            this.c = this.b.a(canvas, this.b.a(s));
            canvas.restore();
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            if (this.c == null || motionEvent.getAction() != 0) {
                return super.onTouchEvent(motionEvent);
            }
            if (this.c.contains((float)(Math.round(motionEvent.getX()) - this.getWidth() / 2), (float)(Math.round(motionEvent.getY()) - this.getHeight() / 2))) {
                com.tremorvideo.sdk.android.videoad.u.this.L.c(this.d);
                return true;
            }
            return super.onTouchEvent(motionEvent);
        }
    }
    
    private class b extends TimerTask
    {
        @Override
        public void run() {
            com.tremorvideo.sdk.android.videoad.u.this.H();
        }
    }
    
    private class c extends Button
    {
        public c(final Context context) {
            super(context);
            final Bitmap a2 = com.tremorvideo.sdk.android.videoad.u.this.K.q().a(bw.d.Q);
            this.setBackgroundDrawable((Drawable)new BitmapDrawable(a2));
            this.setWidth(a2.getWidth());
            this.setHeight(a2.getHeight());
        }
    }
    
    private class d extends Drawable
    {
        Bitmap a;
        Bitmap b;
        
        public d() {
            if (com.tremorvideo.sdk.android.videoad.u.this.G) {
                this.a = com.tremorvideo.sdk.android.videoad.u.this.K.q().a(bw.d.S);
                this.b = com.tremorvideo.sdk.android.videoad.u.this.K.q().a(bw.d.U);
                return;
            }
            this.a = com.tremorvideo.sdk.android.videoad.u.this.K.q().a(bw.d.R);
            this.b = com.tremorvideo.sdk.android.videoad.u.this.K.q().a(bw.d.T);
        }
        
        public void draw(final Canvas canvas) {
            final Rect bounds = this.getBounds();
            canvas.drawBitmap(this.a, (float)bounds.left, (float)bounds.top, (Paint)null);
            final int n = (int)Math.ceil((bounds.width() - this.a.getWidth()) / this.b.getWidth());
            int n2 = this.a.getWidth() + bounds.left;
            for (int i = 0; i < n; ++i) {
                canvas.drawBitmap(this.b, (float)n2, (float)bounds.top, (Paint)null);
                n2 += this.b.getWidth();
            }
        }
        
        public int getMinimumHeight() {
            return this.b.getHeight();
        }
        
        public int getOpacity() {
            return 1;
        }
        
        public void setAlpha(final int n) {
        }
        
        public void setColorFilter(final ColorFilter colorFilter) {
        }
    }
    
    private class e extends View
    {
        int a;
        String b;
        int c;
        int d;
        TextPaint e;
        
        public e(final Context context, final int a, final int n) {
            super(context);
            this.b = "";
            this.a = a;
            this.c = 0;
            this.d = 0;
            (this.e = new TextPaint()).setColor(this.a);
            if (com.tremorvideo.sdk.android.videoad.u.this.G) {
                this.e.setTextSize((float)com.tremorvideo.sdk.android.videoad.ac.a(8));
                this.e.setTypeface(Typeface.create("helvetica", 0));
            }
            else {
                this.e.setTextSize((float)com.tremorvideo.sdk.android.videoad.ac.K());
                this.e.setTypeface(Typeface.create("helvetica", 1));
            }
            this.e.setAntiAlias(true);
            this.c = (int)this.e.measureText("Ads by Tremor Video: 0000");
        }
        
        public int a() {
            return this.c;
        }
        
        public void a(final String b) {
            this.b = b;
            this.invalidate();
        }
        
        protected void onDraw(final Canvas canvas) {
            super.onDraw(canvas);
            final Rect rect = new Rect();
            this.e.getTextBounds(this.b, 0, this.b.length(), rect);
            canvas.drawText(this.b, (float)((this.c - rect.width()) / 2), (this.d - this.e.getTextSize()) / 2.0f + this.e.getTextSize() + this.e.baselineShift, (Paint)this.e);
        }
        
        protected void onMeasure(final int n, final int n2) {
            this.setMeasuredDimension(this.c, this.d);
        }
        
        public void setBackgroundDrawable(final Drawable backgroundDrawable) {
            super.setBackgroundDrawable(backgroundDrawable);
            this.d = backgroundDrawable.getMinimumHeight();
        }
    }
}
