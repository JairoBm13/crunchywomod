// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import android.widget.VideoView;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RoundRectShape;
import com.tremorvideo.sdk.android.videoad.ax;
import com.tremorvideo.sdk.android.videoad.aw;
import android.view.MotionEvent;
import android.media.MediaPlayer$OnErrorListener;
import android.media.MediaPlayer$OnPreparedListener;
import com.tremorvideo.sdk.android.videoad.ac;
import android.media.MediaPlayer;
import android.media.MediaPlayer$OnCompletionListener;
import android.util.AttributeSet;
import android.net.Uri;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.RelativeLayout$LayoutParams;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.content.Context;
import android.widget.RelativeLayout;

public class af
{
    private int A;
    private int B;
    private int C;
    a a;
    RelativeLayout b;
    m c;
    boolean d;
    b e;
    int f;
    int g;
    Context h;
    long i;
    long j;
    ProgressBar k;
    volatile boolean l;
    volatile l m;
    public l n;
    public l o;
    public int p;
    public int q;
    public int r;
    private com.tremorvideo.sdk.android.richmedia.a s;
    private boolean t;
    private boolean u;
    private boolean v;
    private ag w;
    private TextView x;
    private RelativeLayout$LayoutParams y;
    private int z;
    
    public af(final Context h, final m c, final com.tremorvideo.sdk.android.richmedia.a s) {
        this.f = 0;
        this.g = 0;
        this.i = 0L;
        this.j = 0L;
        this.t = false;
        this.u = false;
        this.v = false;
        this.l = false;
        this.m = null;
        this.n = null;
        this.o = null;
        this.p = -1;
        this.q = -1;
        this.r = 0;
        this.z = -1;
        this.A = 0;
        this.C = -1;
        this.s = s;
        this.h = h;
        this.c = c;
        this.d = false;
        this.b = new RelativeLayout(h);
        this.o();
    }
    
    private void a(final l l) {
        (this.w = new ag(this.h, this)).a(l.a.b(), l.e());
        this.l = false;
        this.v = false;
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(-1, -1);
        relativeLayout$LayoutParams.addRule(10);
        relativeLayout$LayoutParams.addRule(12);
        relativeLayout$LayoutParams.addRule(11);
        relativeLayout$LayoutParams.addRule(9);
        this.b.removeAllViews();
        this.b.addView((View)this.w, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
    }
    
    private void a(final boolean d) {
        synchronized (this) {
            this.d = d;
        }
    }
    
    private void b(final l n) {
        this.l = true;
        this.j = 0L;
        if (this.n != null && this.n.h() != null) {
            this.m();
        }
        if (n.h() != null) {
            this.a(n);
        }
        else {
            this.o();
            this.q();
            if (n.f()) {
                this.a.setVideoURI(Uri.parse(n.e()));
                this.k = new ProgressBar(this.a.getContext(), (AttributeSet)null, 16842874);
                final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(100, 100);
                relativeLayout$LayoutParams.addRule(13);
                this.b.addView((View)this.k, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
                this.t = false;
                this.u = true;
                this.v = false;
            }
            else {
                this.a.setVideoPath(n.e());
                this.t = false;
                this.u = false;
                this.v = false;
            }
        }
        this.n = n;
        this.f = 0;
        this.g = this.a.getDuration();
        this.a(true);
    }
    
    private void o() {
        (this.a = new a(this.h)).setBackgroundColor(0);
        this.a.setId(48879);
        this.p();
        this.A = 0;
        this.a.setOnCompletionListener((MediaPlayer$OnCompletionListener)new MediaPlayer$OnCompletionListener() {
            public void onCompletion(final MediaPlayer mediaPlayer) {
                af.this.f = af.this.g;
                if (af.this.A > 0) {
                    return;
                }
                af.this.A++;
                if ((af.this.a != null && af.this.a.a) || af.this.v) {
                    ac.e("Video completed with error");
                }
                else {
                    if (af.this.c != null) {
                        af.this.c.a(com.tremorvideo.sdk.android.richmedia.h.c.c);
                    }
                    if (af.this.e != null) {
                        af.this.e.q();
                    }
                }
                af.this.a(false);
                af.this.t = false;
            }
        });
        this.a.setOnPreparedListener((MediaPlayer$OnPreparedListener)new MediaPlayer$OnPreparedListener() {
            public void onPrepared(final MediaPlayer mediaPlayer) {
                if (af.this.a != null) {
                    if (af.this.m != null) {
                        af.this.a.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                final l m = af.this.m;
                                af.this.m = null;
                                af.this.b(m);
                            }
                        });
                        return;
                    }
                    if (!af.this.v) {
                        af.this.i = 200L;
                        af.this.l = false;
                        af.this.g = af.this.a.getDuration();
                        if (af.this.d) {
                            final boolean b = af.this.e == null || af.this.e.a(af.this);
                            af.this.q();
                            if (b) {
                                af.this.a.start();
                                if (af.this.u) {
                                    af.this.t = true;
                                    af.this.B = 0;
                                    af.this.C = 0;
                                }
                                if (af.this.k != null && af.this.k.isShown()) {
                                    af.this.k.setVisibility(8);
                                }
                                if (af.this.z != -1) {
                                    af.this.a.seekTo(af.this.z);
                                    af.this.z = -1;
                                }
                            }
                            ac.f("[Video]::[Started index: " + af.this.n.d() + "]::[ Progress: " + af.this.h() + "]");
                        }
                    }
                }
            }
        });
        this.a.setOnErrorListener((MediaPlayer$OnErrorListener)new MediaPlayer$OnErrorListener() {
            public boolean onError(final MediaPlayer mediaPlayer, final int n, final int n2) {
                ac.e("Media Player Error: " + n + " Extra: " + n2);
                if (!af.this.a.a) {
                    if (af.this.u) {
                        af.this.l();
                    }
                    else {
                        af.this.a.a = true;
                        if (af.this.e != null) {
                            af.this.e.p();
                            return true;
                        }
                    }
                }
                return true;
            }
        });
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(-1, -1);
        relativeLayout$LayoutParams.addRule(10);
        relativeLayout$LayoutParams.addRule(12);
        relativeLayout$LayoutParams.addRule(11);
        relativeLayout$LayoutParams.addRule(9);
        this.b.removeAllViews();
        this.b.addView((View)this.a, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
    }
    
    private void p() {
        this.a.setVisibility(8);
    }
    
    private void q() {
        this.a.setVisibility(0);
    }
    
    public void a(final int n) {
        if (this.d) {
            this.a.postDelayed((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (!af.this.u) {
                        final int f = af.this.f;
                        af.this.a.pause();
                        af.this.a.setVisibility(4);
                        af.this.a.setVisibility(0);
                        af.this.a.seekTo(f);
                        af.this.A = 0;
                        af.this.a.start();
                    }
                }
            }, (long)n);
        }
    }
    
    public void a(final int n, final int n2) {
        this.a.a(n, n2);
        this.a.getHolder().setFixedSize(n, n2);
    }
    
    public void a(final long n) {
        this.i = Math.max(0L, this.i - n);
        if (this.l) {
            final long j = this.j;
            this.j += n;
            if (this.u) {
                if (this.j >= ac.g) {
                    this.l();
                }
            }
            else if (j < 10000L && this.j >= 10000L) {
                ac.e("Unknown error while loading video...");
                this.e.p();
            }
        }
        else if (this.u && this.d && this.B > -1) {
            if (this.g() == this.B) {
                this.C += (int)n;
            }
            else {
                this.C = 0;
            }
            this.B = this.g();
            if (this.C > ac.g) {
                this.l();
            }
        }
        if (this.n != null && this.n.h() != null && this.w != null) {
            this.w.d();
            this.f = this.w.getProgress();
        }
    }
    
    public void a(MotionEvent obtain) {
        if (this.w != null && this.b != null) {
            final RelativeLayout$LayoutParams relativeLayout$LayoutParams = (RelativeLayout$LayoutParams)this.b.getLayoutParams();
            if (relativeLayout$LayoutParams != null) {
                final float n = obtain.getX() - relativeLayout$LayoutParams.leftMargin;
                final float n2 = obtain.getY() - relativeLayout$LayoutParams.topMargin;
                if (n > 0.0f && n < relativeLayout$LayoutParams.width && n2 > 0.0f && n2 < relativeLayout$LayoutParams.height) {
                    obtain = MotionEvent.obtain(obtain);
                    obtain.setLocation(n, n2);
                    this.w.dispatchTouchEvent(obtain);
                    obtain.recycle();
                }
            }
        }
    }
    
    public void a(final b e) {
        this.e = e;
    }
    
    public void a(final l m, final int z, final int p3) {
        this.z = z;
        if (this.l) {
            this.m = m;
            return;
        }
        this.p = p3;
        this.b(m);
    }
    
    public void a(final aw.b b) {
        if (this.e != null) {
            this.e.a(b);
        }
    }
    
    public void a(final String s) {
        if (this.s != null) {
            final ax c = this.s.c();
            if (c != null) {
                c.c(s);
            }
        }
    }
    
    public boolean a() {
        synchronized (this) {
            return this.d;
        }
    }
    
    public boolean b() {
        return this.i > 0L || (!this.u && this.l);
    }
    
    public boolean b(final int n) {
        if (this.t) {
            final int duration = this.a.getDuration();
            if (duration >= 1) {
                final int currentPosition = this.a.getCurrentPosition();
                final int n2 = duration / 4;
                switch (n) {
                    default: {
                        return false;
                    }
                    case 1: {
                        if (currentPosition > n2) {
                            return true;
                        }
                        break;
                    }
                    case 2: {
                        if (currentPosition > n2 * 2) {
                            return true;
                        }
                        break;
                    }
                    case 3: {
                        if (currentPosition > n2 * 3) {
                            return true;
                        }
                        break;
                    }
                }
            }
        }
        return false;
    }
    
    public void c() {
        this.p();
        if (this.n != null && this.n.h() != null) {
            this.m();
            this.n = null;
        }
        else {
            this.a.stopPlayback();
        }
        if (this.k != null && this.k.isShown()) {
            this.k.setVisibility(8);
        }
        this.a(this.l = false);
        this.B = 0;
        this.C = 0;
        this.f = 0;
        this.g = 0;
    }
    
    public void d() {
        if (this.n != null && this.n.h() != null && this.w != null) {
            this.w.b();
        }
        else if (this.a != null) {
            this.a.pause();
        }
        this.a(false);
    }
    
    public void e() {
        this.r = this.a.getCurrentPosition();
        this.o = this.n;
        this.q = this.p;
        if (this.e != null) {
            this.e.b(this.n.d(), this.r);
        }
        this.d();
    }
    
    public void f() {
        if (this.d) {
            this.g();
        }
    }
    
    public int g() {
        int currentPosition;
        final int n = currentPosition = 0;
        while (true) {
            try {
                if (this.a != null) {
                    currentPosition = n;
                    if (this.n != null) {
                        currentPosition = n;
                        if (this.n.h() == null) {
                            currentPosition = this.a.getCurrentPosition();
                        }
                    }
                }
                if (currentPosition > this.f) {
                    this.f = currentPosition;
                }
                return this.f;
            }
            catch (Exception ex) {
                currentPosition = n;
                continue;
            }
            break;
        }
    }
    
    public int h() {
        if (this.a != null) {
            return this.a.getCurrentPosition();
        }
        return -1;
    }
    
    public int i() {
        if (this.g < 1 && this.a != null) {
            this.g = this.a.getDuration();
        }
        return this.g;
    }
    
    public void j() {
        if (!this.v) {
            if (this.n != null && this.n.h() != null) {
                if (this.w != null) {
                    this.w.c();
                    this.a(true);
                }
            }
            else if (this.a != null && !this.a.isPlaying()) {
                final int currentPosition = this.a.getCurrentPosition();
                final int i = this.i();
                if (currentPosition != this.f && this.f < i) {
                    this.a.seekTo(this.f);
                }
                this.A = 0;
                this.a.start();
            }
            if (this.k != null && this.k.isShown()) {
                this.k.setVisibility(8);
            }
            this.a(true);
        }
    }
    
    public View k() {
        return (View)this.b;
    }
    
    public void l() {
        if (this.b != null && !this.v) {
            this.l = false;
            this.v = true;
            this.t = false;
            this.B = 0;
            this.C = 0;
            (this.x = new TextView(this.a.getContext(), (AttributeSet)null, 16842817)).setText((CharSequence)"Playback Error!");
            (this.y = new RelativeLayout$LayoutParams(-2, -2)).addRule(13);
            this.x.setPadding(10, 5, 10, 5);
            final ShapeDrawable backgroundDrawable = new ShapeDrawable((Shape)new RoundRectShape(new float[] { 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f }, (RectF)null, (float[])null));
            backgroundDrawable.getPaint().setColor(-12303292);
            this.x.setBackgroundDrawable((Drawable)backgroundDrawable);
            this.x.setTextColor(-1);
            ((Activity)this.b.getContext()).runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (af.this.k != null && af.this.k.isShown()) {
                        af.this.k.setVisibility(8);
                    }
                    af.this.b.addView((View)af.this.x, (ViewGroup$LayoutParams)af.this.y);
                    if (af.this.a != null && af.this.a.isPlaying()) {
                        af.this.a.stopPlayback();
                    }
                }
            });
        }
    }
    
    public void m() {
        this.w.stopLoading();
        this.w.a();
        if (this.w.getParent() == this.b) {
            this.b.removeView((View)this.w);
        }
        this.w.loadData("", "text/html", "UTF-8");
        this.w = null;
    }
    
    public void n() {
        ((Activity)this.b.getContext()).runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                if (af.this.w != null) {
                    af.this.m();
                }
                if (af.this.a != null) {
                    if (af.this.a.isPlaying()) {
                        af.this.a.stopPlayback();
                    }
                    af.this.a.destroyDrawingCache();
                    af.this.a = null;
                }
            }
        });
    }
    
    public class a extends VideoView
    {
        public boolean a;
        private int c;
        private int d;
        
        a(final Context context) {
            super(context);
            this.c = 0;
            this.d = 0;
            this.a = false;
        }
        
        public void a(final int c, final int d) {
            this.c = c;
            this.d = d;
        }
        
        public int getId() {
            return 48879;
        }
        
        protected void onMeasure(final int n, final int n2) {
            if (this.c == 0 || this.d == 0) {
                super.onMeasure(n, n2);
                return;
            }
            this.setMeasuredDimension(this.c, this.d);
        }
    }
    
    public interface b
    {
        void a(final aw.b p0);
        
        boolean a(final af p0);
        
        void b(final int p0, final int p1);
        
        void p();
        
        void q();
    }
}
