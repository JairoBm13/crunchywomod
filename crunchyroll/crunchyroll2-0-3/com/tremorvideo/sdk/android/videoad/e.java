// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.text.TextPaint;
import android.os.AsyncTask;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.content.Intent;
import android.app.KeyguardManager;
import android.view.KeyEvent;
import java.util.TimerTask;
import android.content.res.Resources;
import android.content.res.Configuration;
import android.view.View$OnFocusChangeListener;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.WindowManager;
import android.view.View$OnClickListener;
import android.graphics.Typeface;
import android.widget.TextView;
import android.view.animation.AlphaAnimation;
import java.util.Collection;
import android.media.MediaPlayer$OnCompletionListener;
import android.media.MediaPlayer$OnErrorListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer$OnPreparedListener;
import android.net.Uri;
import android.content.DialogInterface$OnCancelListener;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import java.util.Iterator;
import java.util.ArrayList;
import android.graphics.Color;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.view.ViewGroup;
import android.os.SystemClock;
import com.tremorvideo.sdk.android.logger.TestAppLogger;
import android.view.ViewGroup$LayoutParams;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.content.Context;
import android.widget.LinearLayout;
import android.app.Activity;
import java.util.List;
import android.widget.RelativeLayout;
import android.app.AlertDialog;
import java.util.Timer;
import android.widget.VideoView;
import android.view.View;

public class e extends com.tremorvideo.sdk.android.videoad.a implements ax.c
{
    private static String W;
    private static int X;
    boolean A;
    String B;
    int C;
    boolean D;
    b E;
    boolean F;
    int G;
    View H;
    private t I;
    private ax J;
    private VideoView K;
    private g L;
    private ad M;
    private a N;
    private aq O;
    private int P;
    private boolean Q;
    private boolean R;
    private boolean S;
    private boolean T;
    private boolean U;
    private boolean V;
    private int Y;
    private View Z;
    boolean a;
    private boolean aa;
    private Timer ab;
    private long ac;
    private long ad;
    private long ae;
    private AlertDialog af;
    private Runnable ag;
    int b;
    v e;
    v f;
    boolean g;
    boolean h;
    int i;
    boolean j;
    boolean k;
    boolean l;
    boolean m;
    boolean n;
    bs o;
    int p;
    int q;
    RelativeLayout r;
    boolean s;
    boolean t;
    boolean u;
    boolean v;
    boolean w;
    long x;
    long y;
    List<aw> z;
    
    static {
        e.W = "";
        e.X = 0;
    }
    
    public e(final Activity activity, final com.tremorvideo.sdk.android.videoad.a.a a, final t i, final boolean a2, final b e) {
        super(a, activity);
        this.O = null;
        this.a = false;
        this.b = 0;
        this.g = false;
        this.h = true;
        this.j = false;
        this.k = false;
        this.l = false;
        this.m = false;
        this.n = false;
        this.s = false;
        this.t = false;
        this.u = true;
        this.v = true;
        this.w = false;
        this.x = 0L;
        this.y = 0L;
        this.Q = false;
        this.R = false;
        this.S = false;
        this.T = true;
        this.U = false;
        this.V = false;
        this.Y = -1;
        this.Z = null;
        this.aa = true;
        this.ab = null;
        this.ac = 0L;
        this.ad = 0L;
        this.ae = -1L;
        this.af = null;
        this.A = false;
        this.B = null;
        this.C = 0;
        this.D = true;
        this.F = true;
        this.G = 0;
        this.ag = new Runnable() {
            @Override
            public void run() {
                if (!com.tremorvideo.sdk.android.videoad.e.this.K.isPlaying()) {
                    if (com.tremorvideo.sdk.android.videoad.e.this.t) {
                        com.tremorvideo.sdk.android.videoad.e.this.F();
                    }
                }
                else {
                    final int currentPosition = com.tremorvideo.sdk.android.videoad.e.this.K.getCurrentPosition();
                    com.tremorvideo.sdk.android.videoad.e.this.d(currentPosition);
                    com.tremorvideo.sdk.android.videoad.e.this.e(currentPosition);
                    if (com.tremorvideo.sdk.android.videoad.e.this.u) {
                        com.tremorvideo.sdk.android.videoad.e.this.L.a(com.tremorvideo.sdk.android.videoad.e.this.E());
                    }
                }
                com.tremorvideo.sdk.android.videoad.e.this.L.postDelayed((Runnable)this, 500L);
            }
        };
        this.I = i;
        com.tremorvideo.sdk.android.videoad.ac.a(this.c.getWindow());
        this.A = a2;
        this.B = this.c.getIntent().getExtras().getString("vastURL");
        this.G = this.c.getIntent().getExtras().getInt("skipDelaySeconds");
        this.F = this.c.getIntent().getExtras().getBoolean("bWaterMark");
        com.tremorvideo.sdk.android.videoad.e.W = "invalid session id";
        this.c.requestWindowFeature(1);
        this.c.getWindow().setFlags(1024, 1024);
        this.E = e;
        if (i != null) {
            this.a(i);
            return;
        }
        final LinearLayout contentView = new LinearLayout((Context)this.c);
        final ProgressBar progressBar = new ProgressBar((Context)this.c, (AttributeSet)null, 16842874);
        progressBar.setLayoutParams(new ViewGroup$LayoutParams(-2, -2));
        contentView.setGravity(17);
        contentView.addView((View)progressBar);
        if (com.tremorvideo.sdk.android.videoad.ac.r() >= 9) {
            this.c.setRequestedOrientation(6);
        }
        else {
            this.c.setRequestedOrientation(0);
        }
        this.c.setContentView((View)contentView);
    }
    
    private void A() {
        if (this.K != null && !this.J.d() && !this.t && !this.j && this.n) {
            this.K.start();
            try {
                if (com.tremorvideo.sdk.android.videoad.ac.r) {
                    final TestAppLogger instance = TestAppLogger.getInstance();
                    String s;
                    if (this.I.r) {
                        s = "streaming";
                    }
                    else {
                        s = "no-streaming";
                    }
                    instance.logVideoPlaybackStart(s, "playback_url:" + this.I.a(0), "info");
                }
            }
            catch (Exception ex) {
                com.tremorvideo.sdk.android.videoad.ac.e("Error logVideoPlaybackStart" + ex);
            }
        }
    }
    
    private void B() {
        if (this.H == null) {
            this.H = this.g(this.I.T());
            this.r.addView(this.H, new ViewGroup$LayoutParams(-1, -1));
        }
        this.x = 0L;
        this.y = com.tremorvideo.sdk.android.videoad.ac.G();
        this.a(0, true);
        if (!this.a) {
            this.a = true;
            this.e.e();
        }
    }
    
    private boolean C() {
        return this.I != null && this.I.N() && this.v && !this.r() && !this.t;
    }
    
    private String D() {
        if (!this.I.Q) {
            return "";
        }
        if (this.b > 0 || this.A) {
            return "Advertisement: " + this.I.G();
        }
        return "Ads by Tremor Video: " + this.I.G();
    }
    
    private String E() {
        final int currentPosition = this.K.getCurrentPosition();
        if (!this.I.Q) {
            return "";
        }
        if (!this.A && currentPosition < 5000 && this.b == 0) {
            return "Ads by Tremor Video: " + String.valueOf(this.G());
        }
        return "Advertisement: " + String.valueOf(this.G());
    }
    
    private void F() {
        try {
            final aw t = this.I.T();
            if (t != null) {
                final String s = t.f().get("auto-skip");
                if (s != null) {
                    final long n = Long.parseLong(s) * 1000L;
                    if (this.x < n) {
                        final long g = com.tremorvideo.sdk.android.videoad.ac.G();
                        final long y = this.y;
                        this.y = g;
                        if (this.J.c() == -1 && !this.j) {
                            this.x += g - y;
                            if (this.x >= n) {
                                this.d.a(this);
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
    
    private int G() {
        if (this.P != 0) {
            final int round = Math.round((this.P - this.K.getCurrentPosition()) / 1000.0f);
            if (round >= 0) {
                return round;
            }
        }
        return 0;
    }
    
    private void H() {
        this.b(false);
    }
    
    private void I() {
        this.K();
        if (!this.l) {
            this.d.l();
        }
        this.c.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                if (com.tremorvideo.sdk.android.videoad.e.this.af != null && com.tremorvideo.sdk.android.videoad.e.this.af.isShowing()) {
                    com.tremorvideo.sdk.android.videoad.e.this.af.dismiss();
                }
                com.tremorvideo.sdk.android.videoad.e.this.af = null;
                com.tremorvideo.sdk.android.videoad.e.this.K.destroyDrawingCache();
                com.tremorvideo.sdk.android.videoad.e.this.d.a(com.tremorvideo.sdk.android.videoad.e.this);
            }
        });
    }
    
    private void J() {
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        final long n = elapsedRealtime - this.ac;
        this.ac = elapsedRealtime;
        if (!this.j && !this.t && !this.m && !this.J.c) {
            if (this.k) {
                if (this.ad > -1L) {
                    this.ad += n;
                    if (this.ad > 5000L) {
                        this.m = true;
                        this.ad = -1L;
                        if (this.af != null && this.af.isShowing()) {
                            this.af.dismiss();
                        }
                        this.I();
                    }
                }
            }
            else {
                if (this.ae > -1L) {
                    final long ae = this.K.getCurrentPosition();
                    if (ae == this.ae) {
                        this.ad += n;
                    }
                    else {
                        this.ad = 0L;
                    }
                    this.ae = ae;
                }
                else {
                    this.ad += n;
                }
                if (this.ad > com.tremorvideo.sdk.android.videoad.ac.g) {
                    this.b(true);
                }
            }
        }
    }
    
    private void K() {
        if (this.ab != null) {
            this.ab.cancel();
            this.ab.purge();
            this.ab = null;
        }
    }
    
    private View a(final View view, final t.b b) {
        int e = -1;
        if (this.O != null) {
            ((ViewGroup)this.O.getParent()).removeView((View)this.O);
        }
        else {
            (this.O = com.tremorvideo.sdk.android.videoad.ac.a((Context)this.c)).a(b.b());
            this.O.a((aq.d)new aq.d() {
                @Override
                public boolean a(final String s) {
                    final aw c = com.tremorvideo.sdk.android.videoad.e.this.I.c(s);
                    if (c != null) {
                        com.tremorvideo.sdk.android.videoad.e.this.f(c);
                    }
                    return false;
                }
            });
        }
        if (b.g() == com.tremorvideo.sdk.android.videoad.t.c.e) {
            final RelativeLayout relativeLayout = new RelativeLayout((Context)this.c);
            final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(-1, -1);
            final RelativeLayout$LayoutParams relativeLayout$LayoutParams2 = new RelativeLayout$LayoutParams(b.e(), b.f());
            relativeLayout$LayoutParams2.topMargin = b.c();
            relativeLayout$LayoutParams2.leftMargin = b.d();
            relativeLayout.addView(view, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
            relativeLayout.addView((View)this.O, (ViewGroup$LayoutParams)relativeLayout$LayoutParams2);
            return (View)relativeLayout;
        }
        if (b.g().b()) {
            final LinearLayout linearLayout = new LinearLayout((Context)this.c);
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
                linearLayout.addView((View)this.O, (ViewGroup$LayoutParams)linearLayout$LayoutParams2);
                linearLayout.addView(view, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
            }
            else {
                linearLayout.addView(view, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
                linearLayout.addView((View)this.O, (ViewGroup$LayoutParams)linearLayout$LayoutParams2);
            }
            return (View)linearLayout;
        }
        if (!b.g().a()) {
            return null;
        }
        final LinearLayout linearLayout2 = new LinearLayout((Context)this.c);
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
            linearLayout2.addView((View)this.O, (ViewGroup$LayoutParams)linearLayout$LayoutParams4);
            return (View)linearLayout2;
        }
        linearLayout2.addView((View)this.O, (ViewGroup$LayoutParams)linearLayout$LayoutParams4);
        linearLayout2.addView(view, (ViewGroup$LayoutParams)linearLayout$LayoutParams3);
        return (View)linearLayout2;
    }
    
    private void a(final int n) {
        this.Z.measure(-1, -1);
        final TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, (float)(-this.Z.getMeasuredHeight() * 2), 0.0f);
        animation.setDuration(750L);
        animation.setFillAfter(true);
        animation.setFillBefore(true);
        animation.setInterpolator((Interpolator)new DecelerateInterpolator());
        if (n > 0) {
            animation.setStartTime(AnimationUtils.currentAnimationTimeMillis() + n);
        }
        this.Z.setAnimation((Animation)animation);
        this.a(n - 200, true);
    }
    
    private void a(final int n, final boolean b) {
        if (this.u) {
            this.L.measure(-1, -1);
            final TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float)(-this.L.getMeasuredHeight()));
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
            this.L.setAnimation((Animation)animation);
            this.u = false;
        }
    }
    
    private void a(final t i, final boolean b) {
        com.tremorvideo.sdk.android.videoad.e.W = "";
        this.I = i;
        if (b && this.E != null) {
            this.E.a(this.I);
        }
        if (!this.I.b()) {
            this.I.a((Context)this.c);
        }
        com.tremorvideo.sdk.android.videoad.e.X = 0;
        this.x = 0L;
        this.b(i);
        final String[] split = this.I.c(0).split("-");
        if (split.length > 1) {
            final String[] split2 = split[1].split("x");
            if (split2.length == 2) {
                this.p = Integer.parseInt(split2[0]);
                this.q = Integer.parseInt(split2[1]);
            }
        }
        if (!b) {
            this.o = i.f();
            final boolean a = this.o.a("orientation");
            this.Q = a;
            if (a) {
                com.tremorvideo.sdk.android.videoad.ac.e("Compatibity: orientation");
            }
            final boolean a2 = this.o.a("alpha-disable-blending");
            this.S = a2;
            if (a2) {
                com.tremorvideo.sdk.android.videoad.ac.e("Compatibity: alpha-disable-blending");
            }
            final boolean a3 = this.o.a("lock-orientation");
            this.R = a3;
            if (a3) {
                com.tremorvideo.sdk.android.videoad.ac.e("Compatibity: lock-orientation");
            }
            this.T = true;
            if (this.T) {
                com.tremorvideo.sdk.android.videoad.ac.e("Compatibity: manual-measure");
            }
            final boolean a4 = this.o.a("pause-bug");
            this.U = a4;
            if (a4) {
                com.tremorvideo.sdk.android.videoad.ac.e("Compatibity: pause-bug");
            }
            final boolean a5 = this.o.a("disable-video-tag-autoplay");
            this.V = a5;
            if (a5) {
                com.tremorvideo.sdk.android.videoad.ac.e("Compatibity: disable-video-tag-autoplay");
            }
        }
    }
    
    private void a(final boolean b) {
        if (this.C() != this.u) {
            if (!this.u) {
                this.b(0, b);
                return;
            }
            this.a(0, b);
        }
    }
    
    private void b(final int n) {
        final View contentView = new View((Context)this.c);
        contentView.setBackgroundColor(Color.argb(255, 0, 0, 0));
        this.c.setContentView(contentView);
        this.w();
        this.s();
        this.c(n);
        if (this.t) {
            this.B();
        }
    }
    
    private void b(final int n, final boolean b) {
        if (!this.u && this.C() && this.I.Q) {
            final TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, (float)(-this.L.getHeight()), 0.0f);
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
            this.L.setAnimation((Animation)animation);
            this.u = true;
        }
    }
    
    private void b(final t t) {
        this.z = new ArrayList<aw>();
        for (final aw aw : this.I.l()) {
            if (aw.a() == aw.b.j) {
                this.z.add(aw);
            }
            else if (aw.a() == aw.b.x) {
                this.z.add(aw);
            }
            else if (aw.a() == aw.b.aq) {
                this.z.add(aw);
            }
            else {
                if (aw.b() < 0) {
                    continue;
                }
                this.z.add(aw);
            }
        }
    }
    
    private void b(final boolean l) {
        if (!this.k) {
            this.ad = -1L;
            this.k = true;
            this.l = l;
            this.c.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (com.tremorvideo.sdk.android.videoad.e.this.af == null) {
                        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)com.tremorvideo.sdk.android.videoad.e.this.c);
                        alertDialog$Builder.setTitle((CharSequence)"Cannot play video");
                        alertDialog$Builder.setMessage((CharSequence)"Sorry, this video cannot be played");
                        alertDialog$Builder.setPositiveButton((CharSequence)"OK", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                            public void onClick(final DialogInterface dialogInterface, final int n) {
                                com.tremorvideo.sdk.android.videoad.e.this.I();
                            }
                        });
                        com.tremorvideo.sdk.android.videoad.e.this.af = alertDialog$Builder.create();
                        com.tremorvideo.sdk.android.videoad.e.this.af.setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
                            public void onCancel(final DialogInterface dialogInterface) {
                                com.tremorvideo.sdk.android.videoad.e.this.I();
                            }
                        });
                    }
                    com.tremorvideo.sdk.android.videoad.e.this.af.show();
                    com.tremorvideo.sdk.android.videoad.e.this.ad = 0L;
                    if (com.tremorvideo.sdk.android.videoad.e.this.K.isPlaying()) {
                        com.tremorvideo.sdk.android.videoad.e.this.K.stopPlayback();
                    }
                    com.tremorvideo.sdk.android.videoad.e.this.K.setVisibility(8);
                }
            });
        }
    }
    
    private void c(final int n) {
        this.k = false;
        this.n = false;
        if (this.I.r) {
            this.K.setVideoURI(Uri.parse(this.I.a(0)));
        }
        else {
            this.K.setVideoPath(this.I.a((Context)this.c, 0));
        }
        this.K.setOnPreparedListener((MediaPlayer$OnPreparedListener)new MediaPlayer$OnPreparedListener() {
            public void onPrepared(final MediaPlayer mediaPlayer) {
                com.tremorvideo.sdk.android.videoad.e.this.n = true;
                if (n > 0 && (com.tremorvideo.sdk.android.videoad.ac.r() >= 19 || !com.tremorvideo.sdk.android.videoad.e.this.I.a(0).endsWith("m3u8"))) {
                    com.tremorvideo.sdk.android.videoad.e.this.K.seekTo(n);
                }
                if (!com.tremorvideo.sdk.android.videoad.e.this.I.r || !com.tremorvideo.sdk.android.videoad.e.this.j) {
                    com.tremorvideo.sdk.android.videoad.e.this.A();
                }
                final int duration = com.tremorvideo.sdk.android.videoad.e.this.K.getDuration();
                if (duration > 0) {
                    com.tremorvideo.sdk.android.videoad.e.this.P = duration;
                }
                else {
                    com.tremorvideo.sdk.android.videoad.e.this.P = com.tremorvideo.sdk.android.videoad.e.this.I.G;
                }
                if (com.tremorvideo.sdk.android.videoad.e.this.I.g(com.tremorvideo.sdk.android.videoad.e.this.P)) {
                    com.tremorvideo.sdk.android.videoad.e.this.b(com.tremorvideo.sdk.android.videoad.e.this.I);
                }
                if (n == 0) {
                    com.tremorvideo.sdk.android.videoad.e.this.L.a(com.tremorvideo.sdk.android.videoad.e.this.D());
                }
                com.tremorvideo.sdk.android.videoad.e.this.L.postDelayed(com.tremorvideo.sdk.android.videoad.e.this.ag, 10L);
                com.tremorvideo.sdk.android.videoad.e.this.K.setOnPreparedListener((MediaPlayer$OnPreparedListener)null);
                com.tremorvideo.sdk.android.videoad.e.this.ad = -1L;
                com.tremorvideo.sdk.android.videoad.e.this.ae = n;
            }
        });
        this.K.setOnErrorListener((MediaPlayer$OnErrorListener)new MediaPlayer$OnErrorListener() {
            public boolean onError(final MediaPlayer mediaPlayer, final int n, final int n2) {
                while (true) {
                    try {
                        if (com.tremorvideo.sdk.android.videoad.ac.r) {
                            TestAppLogger.getInstance().logVideoPlaybackStatus("Video Playback Failure", "playback_url:" + com.tremorvideo.sdk.android.videoad.e.this.I.a(0), "fail");
                        }
                        com.tremorvideo.sdk.android.videoad.e.this.H();
                        return true;
                    }
                    catch (Exception ex) {
                        com.tremorvideo.sdk.android.videoad.ac.e("Error logVideoPlaybackStatus" + ex);
                        continue;
                    }
                    break;
                }
            }
        });
        this.K.setOnCompletionListener((MediaPlayer$OnCompletionListener)new MediaPlayer$OnCompletionListener() {
            public void onCompletion(final MediaPlayer mediaPlayer) {
                if (!com.tremorvideo.sdk.android.videoad.e.this.k) {
                    com.tremorvideo.sdk.android.videoad.e.this.e(com.tremorvideo.sdk.android.videoad.e.this.P);
                    while (true) {
                        try {
                            if (com.tremorvideo.sdk.android.videoad.ac.r) {
                                TestAppLogger.getInstance().logVideoPlaybackStatus("Video Playback Complete", "playback_url:" + com.tremorvideo.sdk.android.videoad.e.this.I.a(0), "pass");
                            }
                            if (com.tremorvideo.sdk.android.videoad.e.this.I.R()) {
                                com.tremorvideo.sdk.android.videoad.e.this.K.pause();
                                com.tremorvideo.sdk.android.videoad.e.this.t = true;
                                final e a = com.tremorvideo.sdk.android.videoad.e.this;
                                ++a.b;
                                if (!com.tremorvideo.sdk.android.videoad.e.this.I.R) {
                                    com.tremorvideo.sdk.android.videoad.e.this.I.R = true;
                                    final aw c = com.tremorvideo.sdk.android.videoad.e.this.I.C();
                                    if (c != null) {
                                        com.tremorvideo.sdk.android.videoad.e.this.J.a(c);
                                    }
                                }
                                com.tremorvideo.sdk.android.videoad.e.this.B();
                                return;
                            }
                        }
                        catch (Exception ex) {
                            com.tremorvideo.sdk.android.videoad.ac.e("Error logVideoPlaybackStatus" + ex);
                            continue;
                        }
                        break;
                    }
                    com.tremorvideo.sdk.android.videoad.e.this.K();
                    com.tremorvideo.sdk.android.videoad.e.this.m = true;
                    com.tremorvideo.sdk.android.videoad.e.this.I.R = true;
                    final aw c2 = com.tremorvideo.sdk.android.videoad.e.this.I.C();
                    if (c2 != null) {
                        com.tremorvideo.sdk.android.videoad.e.this.J.a(c2);
                    }
                    if (com.tremorvideo.sdk.android.videoad.e.this.K.isPlaying()) {
                        com.tremorvideo.sdk.android.videoad.e.this.K.stopPlayback();
                    }
                    com.tremorvideo.sdk.android.videoad.e.this.K.destroyDrawingCache();
                    com.tremorvideo.sdk.android.videoad.e.this.d.a(com.tremorvideo.sdk.android.videoad.e.this);
                }
            }
        });
        this.ad = 0L;
    }
    
    private void c(final aw aw) {
        synchronized (this) {
            if (!this.m && (!this.K.isPlaying() || this.K.getCurrentPosition() < this.K.getDuration() - 200)) {
                this.f(aw);
            }
        }
    }
    
    private void d(int round) {
        synchronized (this) {
            if (this.M != null) {
                round = Math.round((this.I.Q() - round) / 1000.0f);
                this.M.a(round);
            }
            else if (this.I != null && this.I.P() && !this.a && round >= this.I.Q()) {
                this.a = true;
                this.e.e();
            }
        }
    }
    
    private void d(final aw aw) {
        if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.j) {
            this.J.a(aw, this.b + 1);
        }
        else {
            this.J.a(aw);
        }
        this.x = Long.MAX_VALUE;
    }
    
    private void e(final int n) {
    Label_0036_Outer:
        while (true) {
            ArrayList<aw> list = null;
        Label_0202:
            while (true) {
                aw aw = null;
                Label_0097: {
                    synchronized (this) {
                        list = new ArrayList<aw>(this.z.size());
                        if (!this.k) {
                            final Iterator<aw> iterator = this.z.iterator();
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
                            break Label_0202;
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
                else if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.aq) {
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
            this.z.removeAll(list);
            break;
        }
    }
    // monitorexit(this)
    
    private void e(final aw aw) {
        this.J.b(aw);
    }
    
    private void f(final aw aw) {
        while (true) {
            Label_0052: {
                synchronized (this) {
                    if (aw.a() == com.tremorvideo.sdk.android.videoad.aw.b.v) {
                        this.y();
                    }
                    else {
                        if (aw.a() != com.tremorvideo.sdk.android.videoad.aw.b.j) {
                            break Label_0052;
                        }
                        this.J.a(aw, this.b + 1, (String)null);
                    }
                    return;
                }
            }
            final aw aw2;
            this.J.c(aw2);
        }
    }
    
    private View g(final aw aw) {
        final AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500L);
        animation.setFillAfter(true);
        final Context context = this.K.getContext();
        final d d = new d(context);
        final TextView textView = new TextView(context);
        textView.setText((CharSequence)aw.h());
        textView.setTextColor(this.I.q().a(bw.c.a));
        textView.setShadowLayer(6.0f, 0.0f, 0.0f, this.I.q().a(bw.c.b));
        textView.setGravity(1);
        textView.setTypeface(Typeface.create("helvetica", 1));
        textView.setTextSize(2, (float)com.tremorvideo.sdk.android.videoad.ac.L());
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setGravity(17);
        linearLayout.setOrientation(1);
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(-2, -2);
        linearLayout$LayoutParams.gravity = 17;
        linearLayout.addView((View)d, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
        linearLayout.addView((View)textView);
        linearLayout.setAnimation((Animation)animation);
        d.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                com.tremorvideo.sdk.android.videoad.e.this.r.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (com.tremorvideo.sdk.android.videoad.e.this.H != null) {
                            com.tremorvideo.sdk.android.videoad.e.this.r.removeView(com.tremorvideo.sdk.android.videoad.e.this.H);
                        }
                        com.tremorvideo.sdk.android.videoad.e.this.H = null;
                    }
                });
                com.tremorvideo.sdk.android.videoad.e.this.d(aw);
                com.tremorvideo.sdk.android.videoad.e.this.x = Long.MAX_VALUE;
                com.tremorvideo.sdk.android.videoad.e.this.t = false;
                com.tremorvideo.sdk.android.videoad.e.this.b(com.tremorvideo.sdk.android.videoad.e.this.I);
                com.tremorvideo.sdk.android.videoad.e.this.a(true);
                com.tremorvideo.sdk.android.videoad.e.this.c(0);
            }
        });
        return (View)linearLayout;
    }
    
    private void h(final aw aw) {
        if (this.e != null) {
            this.e.a(aw);
        }
        if (this.f != null) {
            this.f.a(aw);
        }
    }
    
    private void i() {
        if (this.Z != null && this.Y == -1) {
            if (!this.r()) {
                this.Z.setVisibility(4);
                this.Z.setAnimation((Animation)null);
                this.b(0, true);
                return;
            }
            if (this.Z.getVisibility() != 0) {
                this.Z.setVisibility(0);
                this.a(0);
            }
            if (this.Z instanceof bd) {
                ((bd)this.Z).a();
            }
        }
    }
    
    private boolean r() {
        return this.f == null && this.Y == -1 && this.Z != null;
    }
    
    private void s() {
        if (this.I != null && this.I.H()) {
            int u = 0;
            if (this.R) {
                u = this.u();
            }
            this.Z = this.I.I().a((Context)this.c, u, this.I.q());
        }
        else {
            this.Z = this.t();
        }
        if (this.Z != null) {
            this.r.addView(this.Z, new ViewGroup$LayoutParams(-2, -2));
            this.i();
            if (this.r()) {
                this.a(2000);
            }
        }
    }
    
    private View t() {
        if ((this.I == null || this.I.J()) && this.Y == -1 && this.aa) {
            return new bc(this.I.q(), this.I.K(), this.I.L(), this.I.M()).a((Context)this.c, (bc.a)new bc.a() {
                @Override
                public void a(final int n) {
                    if (e.this.Y == -1) {
                        e.this.Y = n;
                        final TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float)(-e.this.Z.getMeasuredHeight() * 2));
                        animation.setDuration(500L);
                        animation.setFillAfter(true);
                        animation.setFillBefore(true);
                        animation.setInterpolator((Interpolator)new DecelerateInterpolator());
                        animation.setStartTime(AnimationUtils.currentAnimationTimeMillis() + 200L);
                        e.this.Z.setAnimation((Animation)animation);
                        if (!e.this.t) {
                            e.this.b(400, true);
                        }
                    }
                }
            });
        }
        return null;
    }
    
    private int u() {
        int v;
        final int n = v = this.v();
        if (this.I != null) {
            v = n;
            if (this.I.U() != null) {
                v = n - this.I.U().a();
            }
        }
        return v;
    }
    
    private int v() {
        final Display defaultDisplay = ((WindowManager)this.c.getSystemService("window")).getDefaultDisplay();
        if (this.R) {
            return Math.max(defaultDisplay.getWidth(), defaultDisplay.getHeight());
        }
        return defaultDisplay.getWidth();
    }
    
    private void w() {
        if (this.r != null) {
            this.r.removeAllViews();
            this.f = null;
        }
        final View$OnClickListener view$OnClickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                com.tremorvideo.sdk.android.videoad.e.this.c((aw)view.getTag());
            }
        };
        final int u = this.u();
        final int round = Math.round(160.0f * com.tremorvideo.sdk.android.videoad.ac.N());
        (this.L = new g((Context)this.c, this.I.q().a(bw.c.c))).setId(1);
        this.L.setTag((Object)"");
        this.L.setBackgroundDrawable(new e());
        this.L.a(round);
        final g l = this.L;
        String d;
        if (com.tremorvideo.sdk.android.videoad.e.X == 0) {
            d = this.D();
        }
        else {
            d = "";
        }
        l.a(d);
        this.v = true;
        this.u = true;
        this.M = null;
        final aw n = this.I.n();
        if (this.I.V()) {
            aw aw;
            if ((aw = n) == null) {
                aw = new aw(com.tremorvideo.sdk.android.videoad.aw.b.v, 0, null, "Skip");
            }
            (this.M = new ad((Context)this.c, (View$OnClickListener)view$OnClickListener, aw, this.I.q())).a(this.I.Q() / 1000);
        }
        this.e = new v((Context)this.c, (View$OnClickListener)view$OnClickListener, this.I, u);
        if (this.a) {
            this.e.f();
        }
        if (this.e.a()) {
            this.f = new v((Context)this.c, (View$OnClickListener)view$OnClickListener, this.I, this.e.b(), u);
            if (this.f.c() >= u - round) {
                this.v = false;
            }
        }
        final LinearLayout linearLayout = new LinearLayout((Context)this.c);
        linearLayout.setGravity(17);
        if (this.T || this.A) {
            this.K = new ap((Context)this.c, this.L.getHeight(), this.p, this.q);
        }
        else {
            (this.K = new VideoView((Context)this.c)).setId(48879);
        }
        this.K.setFocusable(false);
        if (com.tremorvideo.sdk.android.videoad.ac.w()) {
            this.K.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
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
        linearLayout.addView((View)this.K);
        final RelativeLayout r = new RelativeLayout((Context)this.c);
        r.setBackgroundColor(-16777216);
        r.addView((View)linearLayout, (ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(-2, -2);
        relativeLayout$LayoutParams.addRule(10);
        relativeLayout$LayoutParams.addRule(11);
        r.addView((View)this.L, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        if (!this.I.Q) {
            this.a(0, false);
        }
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams2 = new RelativeLayout$LayoutParams(-2, -2);
        relativeLayout$LayoutParams2.addRule(12);
        relativeLayout$LayoutParams2.addRule(14);
        r.addView(this.e.d(), (ViewGroup$LayoutParams)relativeLayout$LayoutParams2);
        if (this.f != null) {
            final RelativeLayout$LayoutParams relativeLayout$LayoutParams3 = new RelativeLayout$LayoutParams(-2, -2);
            relativeLayout$LayoutParams3.addRule(10);
            relativeLayout$LayoutParams3.addRule(5);
            r.addView(this.f.d(), (ViewGroup$LayoutParams)relativeLayout$LayoutParams3);
        }
        this.a(false);
        if (this.M != null) {
            final RelativeLayout$LayoutParams relativeLayout$LayoutParams4 = new RelativeLayout$LayoutParams(-2, -2);
            relativeLayout$LayoutParams4.addRule(11);
            relativeLayout$LayoutParams4.addRule(12);
            relativeLayout$LayoutParams4.bottomMargin = this.M.c;
            r.addView((View)this.M, (ViewGroup$LayoutParams)relativeLayout$LayoutParams4);
        }
        final aw a = this.I.a(aw.b.H);
        if (a != null) {
            r.addView((View)(this.N = new a((Context)this.c, a)), (ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
        }
        if (this.I.U() == null) {
            (this.r = r).setFocusable(true);
            this.r.setOnFocusChangeListener((View$OnFocusChangeListener)new View$OnFocusChangeListener() {
                public void onFocusChange(final View view, final boolean b) {
                }
            });
            this.c.setContentView((View)r);
            return;
        }
        final View a2 = this.a((View)r, this.I.U());
        this.r = r;
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(-1, -1);
        linearLayout$LayoutParams.gravity = 17;
        this.c.setContentView(a2, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
    }
    
    private void x() {
        final View$OnClickListener view$OnClickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                com.tremorvideo.sdk.android.videoad.e.this.f((aw)view.getTag());
            }
        };
        final RelativeLayout r = this.r;
        final int u = this.u();
        this.v = true;
        if (this.e != null) {
            r.removeView(this.e.d());
            this.e = null;
        }
        if (this.f != null) {
            r.removeView(this.f.d());
            this.f = null;
        }
        if (this.N != null) {
            r.removeView((View)this.N);
        }
        if (this.M != null) {
            r.removeView((View)this.M);
        }
        this.e = new v((Context)this.c, (View$OnClickListener)view$OnClickListener, this.I, u);
        if (this.a) {
            this.e.f();
        }
        if (this.e.a()) {
            this.f = new v((Context)this.c, (View$OnClickListener)view$OnClickListener, this.I, this.e.b(), u);
            if (this.f.c() >= u - Math.round(160.0f * com.tremorvideo.sdk.android.videoad.ac.N())) {
                this.v = false;
            }
        }
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(-2, -2);
        relativeLayout$LayoutParams.addRule(12);
        relativeLayout$LayoutParams.addRule(14);
        r.addView(this.e.d(), (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        if (this.f != null) {
            final RelativeLayout$LayoutParams relativeLayout$LayoutParams2 = new RelativeLayout$LayoutParams(-2, -2);
            relativeLayout$LayoutParams2.addRule(10);
            relativeLayout$LayoutParams2.addRule(5);
            r.addView(this.f.d(), (ViewGroup$LayoutParams)relativeLayout$LayoutParams2);
        }
        if (this.M != null) {
            final RelativeLayout$LayoutParams relativeLayout$LayoutParams3 = new RelativeLayout$LayoutParams(-2, -2);
            relativeLayout$LayoutParams3.addRule(11);
            relativeLayout$LayoutParams3.addRule(12);
            relativeLayout$LayoutParams3.bottomMargin = this.M.c;
            r.addView((View)this.M, (ViewGroup$LayoutParams)relativeLayout$LayoutParams3);
        }
        if (this.N != null) {
            r.addView((View)this.N, (ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
        }
    }
    
    private void y() {
        synchronized (this) {
            try {
                boolean b = this.a;
                if (this.M != null) {
                    b = this.M.b();
                }
                if (b && this.I.P()) {
                    this.K();
                    final aw n = this.I.n();
                    if (n != null) {
                        this.d(n);
                    }
                    this.s = true;
                    this.K.pause();
                    this.K.stopPlayback();
                    this.K.destroyDrawingCache();
                    this.d.a(this);
                }
            }
            catch (Exception ex) {
                com.tremorvideo.sdk.android.videoad.ac.a(ex);
            }
        }
    }
    
    private void z() {
        if (this.K != null && this.K.isPlaying()) {
            this.K.pause();
            com.tremorvideo.sdk.android.videoad.e.X = this.K.getCurrentPosition();
        }
    }
    
    @Override
    public void a() {
        if (this.A) {
            if (this.I == null) {
                this.f();
                return;
            }
            if (!this.j) {
                com.tremorvideo.sdk.android.videoad.e.X = 0;
            }
            this.x = 0L;
            this.w();
            this.s();
            this.c();
        }
        else {
            if (this.o.c() != com.tremorvideo.sdk.android.videoad.e.W) {
                com.tremorvideo.sdk.android.videoad.ac.e("Playing Video Format: " + this.I.c(0));
                com.tremorvideo.sdk.android.videoad.e.W = this.o.c();
                com.tremorvideo.sdk.android.videoad.e.X = 0;
                this.x = 0L;
                this.w();
                this.s();
            }
            if (this.J.b()) {
                this.J.e();
                this.e((aw)null);
                if (this.a) {
                    this.e.f();
                }
            }
        }
    }
    
    @Override
    public void a(final Configuration configuration) {
        if (this.I != null && !this.R) {
            if (this.i != configuration.orientation) {
                this.i = configuration.orientation;
                if (this.Q) {
                    this.b(this.K.getCurrentPosition());
                }
                else {
                    this.x();
                }
            }
            this.a(false);
            this.i();
        }
    }
    
    public void a(final com.tremorvideo.sdk.android.videoad.a.a a) {
        com.tremorvideo.sdk.android.videoad.ac.e("onVastTagDownloaded");
        Label_0142: {
            try {
                if (this.I == null) {
                    this.I = (t)com.tremorvideo.sdk.android.videoad.n.a(a, this.G, this.F);
                }
                else {
                    this.I.a("vast", a);
                }
                if (!a.b()) {
                    break Label_0142;
                }
                if (this.C < 100) {
                    new f().execute((Object[])new String[] { a.d(), "wrapper" });
                    ++this.C;
                    return;
                }
            }
            catch (Exception ex) {
                com.tremorvideo.sdk.android.videoad.ac.e("onVastTagDownloaded Exception : " + ex);
                ex.printStackTrace();
                return;
            }
            com.tremorvideo.sdk.android.videoad.ac.e("Too many wrapper");
            this.h();
            return;
        }
        this.a(this.I);
        this.g();
    }
    
    @Override
    public void a(final aw aw) {
        this.z();
    }
    
    public void a(final t t) {
        (this.J = new ax(this.c, this.d, t.q())).a((ax.c)this);
        this.i = Resources.getSystem().getConfiguration().orientation;
        if (this.i == 2) {
            this.i = 0;
        }
        else {
            this.i = 1;
        }
        this.a(t, this.A);
        if (this.R) {
            this.i = 0;
            com.tremorvideo.sdk.android.videoad.ac.c(this.c);
        }
        final aw b = this.I.B();
        if (b != null) {
            this.J.a(b);
        }
        if (this.I.r) {
            this.ab = new Timer();
            this.ac = SystemClock.elapsedRealtime();
            this.ab.scheduleAtFixedRate(new c(), 10L, 100L);
        }
    }
    
    @Override
    public boolean a(final int n, final KeyEvent keyEvent) {
        if (n == 84 || n == 82) {
            return true;
        }
        if (n == 4) {
            this.y();
            return true;
        }
        return false;
    }
    
    @Override
    public void b() {
        this.j = true;
        if (!this.U) {
            this.z();
        }
    }
    
    @Override
    public void b(final aw aw) {
        this.A();
    }
    
    @Override
    public void c() {
        if (this.I != null && !((KeyguardManager)this.c.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
            this.j = false;
            if (!this.t) {
                if (this.Q) {
                    this.b(com.tremorvideo.sdk.android.videoad.e.X);
                }
                else if (com.tremorvideo.sdk.android.videoad.e.X > 0 && com.tremorvideo.sdk.android.videoad.ac.r() < 19 && this.I.a(0).endsWith("m3u8")) {
                    try {
                        this.K.resume();
                    }
                    catch (Exception ex) {
                        this.H();
                    }
                }
                else {
                    this.c(com.tremorvideo.sdk.android.videoad.e.X);
                }
                if (!this.K.isPlaying()) {
                    this.A();
                }
            }
            if (this.r != null) {
                for (int i = 0; i < 3; ++i) {
                    this.r.postDelayed((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            com.tremorvideo.sdk.android.videoad.e.this.r.requestLayout();
                            com.tremorvideo.sdk.android.videoad.e.this.r.invalidate();
                        }
                    }, (long)((i + 1) * 500));
                }
            }
        }
    }
    
    @Override
    public void d() {
        super.d();
        if (this.j) {
            this.c();
        }
        if (this.Q) {
            this.b(com.tremorvideo.sdk.android.videoad.e.X);
        }
    }
    
    public int e() {
        if (this.P == 0) {
            return -1;
        }
        return this.K.getCurrentPosition();
    }
    
    public void f() {
        try {
            new f().execute((Object[])new String[] { this.B });
        }
        catch (Exception ex) {
            com.tremorvideo.sdk.android.videoad.ac.e("VAST Content Exception: " + ex);
            this.h();
        }
    }
    
    public void g() {
        synchronized (this) {
            if (this.D) {
                this.D = false;
                this.a();
            }
        }
    }
    
    public void h() {
        final Intent intent = new Intent();
        intent.putExtra("TREMOR_VAST_RESULT", "Vast Tag Playback Failure");
        this.c.setResult(0, intent);
        this.c.finish();
    }
    
    @Override
    public boolean j() {
        boolean b = true;
        if (this.k) {
            b = false;
        }
        return b;
    }
    
    @Override
    public void l() {
        if (this.e != null) {
            this.e.g();
        }
        if (this.f != null) {
            this.f.g();
        }
    }
    
    @Override
    public boolean m() {
        return this.J != null && !this.J.d();
    }
    
    @Override
    public com.tremorvideo.sdk.android.videoad.a.b n() {
        return com.tremorvideo.sdk.android.videoad.a.b.b;
    }
    
    @Override
    public void o() {
        super.o();
        this.K();
        if (this.af != null && this.af.isShowing()) {
            this.af.dismiss();
        }
        this.af = null;
    }
    
    @Override
    public void q() {
        if (!this.I.R) {
            this.I.R = true;
            final aw c = this.I.C();
            if (c != null) {
                this.J.a(c);
            }
        }
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
                com.tremorvideo.sdk.android.videoad.e.this.J.c(this.d);
                return true;
            }
            return super.onTouchEvent(motionEvent);
        }
    }
    
    public interface b
    {
        void a(final n p0);
    }
    
    private class c extends TimerTask
    {
        @Override
        public void run() {
            com.tremorvideo.sdk.android.videoad.e.this.J();
        }
    }
    
    private class d extends Button
    {
        public d(final Context context) {
            super(context);
            final Bitmap a2 = com.tremorvideo.sdk.android.videoad.e.this.I.q().a(bw.d.Q);
            this.setBackgroundDrawable((Drawable)new BitmapDrawable(a2));
            this.setWidth(a2.getWidth());
            this.setHeight(a2.getHeight());
            this.setFocusable(false);
        }
    }
    
    private class e extends Drawable
    {
        Bitmap a;
        Bitmap b;
        
        public e() {
            this.a = com.tremorvideo.sdk.android.videoad.e.this.I.q().a(bw.d.R);
            this.b = com.tremorvideo.sdk.android.videoad.e.this.I.q().a(bw.d.T);
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
    
    class f extends AsyncTask<String, Void, String>
    {
        String a;
        
        protected String a(final String... array) {
            try {
                this.a = null;
                com.tremorvideo.sdk.android.videoad.e.this.B = array[0];
                String s2;
                final String s = s2 = com.tremorvideo.sdk.android.videoad.e.this.B.replace("|", "%7C");
                if (s.contains(" ")) {
                    s2 = s.replace(" ", "%20");
                }
                final bb a = bb.a(s2, com.tremorvideo.sdk.android.videoad.ac.z());
                a.a();
                return a.b();
            }
            catch (Exception ex) {
                com.tremorvideo.sdk.android.videoad.ac.e("Exception vast tag download:" + ex);
                return null;
            }
        }
        
        protected void a(final String s) {
            while (true) {
                Label_0107: {
                    if (s == null || s.length() <= 0) {
                        break Label_0107;
                    }
                    try {
                        final com.tremorvideo.sdk.android.videoad.a.a a = new com.tremorvideo.sdk.android.videoad.a.a(s);
                        final String[] split = s.split("\n");
                        for (int length = split.length, i = 0; i < length; ++i) {
                            com.tremorvideo.sdk.android.videoad.ac.a(com.tremorvideo.sdk.android.videoad.ac.c.c, split[i]);
                        }
                        com.tremorvideo.sdk.android.videoad.e.this.a(a);
                        this.cancel(true);
                        return;
                    }
                    catch (Exception ex) {
                        com.tremorvideo.sdk.android.videoad.ac.e("Vast Tag Parsing failed " + ex);
                        com.tremorvideo.sdk.android.videoad.e.this.h();
                        continue;
                    }
                }
                com.tremorvideo.sdk.android.videoad.ac.e("Vast Tag Download failed ");
                com.tremorvideo.sdk.android.videoad.e.this.h();
                continue;
            }
        }
        
        protected void b(final String s) {
        }
    }
    
    private class g extends View
    {
        int a;
        String b;
        int c;
        int d;
        TextPaint e;
        
        public g(final Context context, final int a) {
            super(context);
            this.b = "";
            this.a = a;
            this.c = 0;
            this.d = 0;
            (this.e = new TextPaint()).setTextSize((float)com.tremorvideo.sdk.android.videoad.ac.K());
            this.e.setColor(this.a);
            this.e.setTypeface(Typeface.create("helvetica", 1));
            this.e.setAntiAlias(true);
            this.setFocusable(false);
        }
        
        public void a(final int c) {
            this.c = c;
            this.invalidate();
        }
        
        public void a(final String b) {
            if (com.tremorvideo.sdk.android.videoad.e.this.I.Q) {
                this.b = b;
                this.invalidate();
            }
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
