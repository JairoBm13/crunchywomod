// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.os.Build$VERSION;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.app.Activity;
import org.json.JSONObject;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import java.util.Iterator;
import android.view.MotionEvent;
import com.tremorvideo.sdk.android.videoad.ac;
import java.util.TimerTask;
import android.hardware.SensorManager;
import android.os.SystemClock;
import java.util.ArrayList;
import java.util.Timer;
import com.tremorvideo.sdk.android.videoad.aw;
import android.content.Context;
import android.hardware.SensorEventListener;
import android.view.View;

public class n extends View implements SensorEventListener, af.b, m.a
{
    int A;
    long B;
    boolean C;
    long D;
    float E;
    float F;
    float G;
    long H;
    long I;
    float J;
    float K;
    int L;
    boolean M;
    o.b N;
    d O;
    String P;
    boolean Q;
    boolean R;
    boolean S;
    boolean T;
    private int U;
    private boolean V;
    private boolean W;
    private boolean Z;
    boolean a;
    private Context aa;
    private aw ab;
    boolean b;
    m c;
    Timer d;
    long e;
    af f;
    a g;
    boolean h;
    boolean i;
    int j;
    int k;
    public int l;
    ArrayList<aw> m;
    l n;
    l[] o;
    int[] p;
    ArrayList<ArrayList<aw>> q;
    int[] r;
    long s;
    boolean t;
    boolean u;
    int v;
    int w;
    int x;
    int y;
    o z;
    
    public n(final Context aa, final d o, final com.tremorvideo.sdk.android.richmedia.a a, int i, final boolean m, final l[] o2, final aw ab) {
        final int n = 0;
        super(aa);
        this.a = false;
        this.b = false;
        this.h = false;
        this.i = false;
        this.j = 0;
        this.k = 0;
        this.U = 0;
        this.V = true;
        this.l = 0;
        this.m = new ArrayList<aw>(5);
        this.s = 0L;
        this.t = false;
        this.u = false;
        this.v = 0;
        this.w = 0;
        this.x = 0;
        this.y = 0;
        this.z = null;
        this.A = -1;
        this.B = 0L;
        this.C = false;
        this.D = 0L;
        this.H = 0L;
        this.I = 0L;
        this.M = true;
        this.W = false;
        this.Z = false;
        this.Q = false;
        this.R = false;
        this.S = false;
        this.ab = null;
        this.T = false;
        this.aa = aa;
        this.W = false;
        this.e = SystemClock.elapsedRealtime();
        this.L = i;
        this.M = m;
        final boolean b = aa.getResources().getConfiguration().touchscreen == 1;
        this.P = a.h();
        this.o = o2;
        this.r = new int[o2.length];
        this.p = new int[o2.length];
        this.q = new ArrayList<ArrayList<aw>>();
        for (i = n; i < o2.length; ++i) {
            this.q.add(new ArrayList<aw>());
        }
        this.c = new m((m.a)this, a, aa, b);
        (this.f = new af(aa, this.c, a)).a((af.b)this);
        this.O = o;
        final SensorManager sensorManager = (SensorManager)aa.getSystemService("sensor");
        sensorManager.registerListener((SensorEventListener)this, sensorManager.getDefaultSensor(1), 2);
        this.setFocusableInTouchMode(true);
        this.ab = ab;
        (this.d = new Timer()).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    com.tremorvideo.sdk.android.richmedia.n.this.k();
                }
                catch (Exception ex) {
                    ac.a(ex);
                }
            }
        }, 10L, 15L);
    }
    
    private l a(int i) {
        final String string = "video-" + (i + 1);
        final l[] o = this.o;
        int length;
        l l;
        for (length = o.length, i = 0; i < length; ++i) {
            l = o[i];
            if (string.equals(l.c())) {
                return l;
            }
        }
        return null;
    }
    
    private void a(final int n, final int a, final int l) {
        this.z = this.c.b();
        this.B = this.c.j();
        this.A = a;
        this.t = true;
        this.v = 0;
        this.w = 0;
        this.l = l;
        final int[] r = this.r;
        ++r[n];
        this.n = this.a(n);
        this.s = -1L;
        this.m.clear();
        this.q.get(n).clear();
        if (n > 0) {
            this.V = false;
        }
        if (this.W) {
            this.post((Runnable)new c(l, this.n));
            return;
        }
        this.Z = true;
    }
    
    private void a(final MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.J = motionEvent.getX();
        }
        else if (motionEvent.getAction() == 1) {
            if (this.N.ordinal() >= com.tremorvideo.sdk.android.richmedia.o.b.f.ordinal() && this.K > 0.1f) {
                this.N = com.tremorvideo.sdk.android.richmedia.o.b.a;
                this.c.a(com.tremorvideo.sdk.android.richmedia.o.b.a);
            }
        }
        else if (motionEvent.getAction() == 2 && this.N != com.tremorvideo.sdk.android.richmedia.o.b.a && !this.c.b().b) {
            final float x = motionEvent.getX();
            final float n = x - this.J;
            float n2;
            if ((this.N == com.tremorvideo.sdk.android.richmedia.o.b.c || this.N == com.tremorvideo.sdk.android.richmedia.o.b.g) && n > 0.0f) {
                n2 = n;
            }
            else if ((this.N == com.tremorvideo.sdk.android.richmedia.o.b.b || this.N == com.tremorvideo.sdk.android.richmedia.o.b.f) && n < 0.0f) {
                n2 = -n;
            }
            else if (this.N == com.tremorvideo.sdk.android.richmedia.o.b.d || this.N == com.tremorvideo.sdk.android.richmedia.o.b.h) {
                n2 = -n;
            }
            else {
                n2 = n;
                if (this.N != com.tremorvideo.sdk.android.richmedia.o.b.e) {
                    n2 = n;
                    if (this.N != com.tremorvideo.sdk.android.richmedia.o.b.i) {
                        n2 = 0.0f;
                    }
                }
            }
            this.J = x;
            if (n2 != 0.0f) {
                this.K += n2 / this.getWidth() * 1.2f;
                this.K = Math.max(0.0f, Math.min(this.K, 1.0f));
                this.c.b(Math.round(this.c.b().c() * this.K));
                this.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        com.tremorvideo.sdk.android.richmedia.n.this.invalidate();
                    }
                });
            }
        }
    }
    
    private void a(final l n, final int a, final int l, final ArrayList<aw> list) {
        this.z = this.c.b();
        this.B = this.c.j();
        this.A = a;
        this.t = true;
        this.v = 0;
        this.w = 0;
        this.l = l;
        this.n = n;
        this.s = -1L;
        this.m.clear();
        if (list != null) {
            final Iterator<aw> iterator = list.iterator();
            while (iterator.hasNext()) {
                this.m.add(iterator.next());
            }
        }
        else {
            this.q.get(n.d()).clear();
        }
        if (n.d() > 0) {
            this.V = false;
        }
        if (this.W) {
            this.post((Runnable)new c(l, this.n));
            return;
        }
        this.Z = true;
    }
    
    private boolean a(final aw aw) {
        return this.m.contains(aw);
    }
    
    private int b(final aw aw) {
        int i = 0;
        while (true) {
            while (i < this.o.length) {
                if (this.o[i].c().equals(aw.d())) {
                    if (i >= 0) {
                        return this.r[i];
                    }
                    return -1;
                }
                else {
                    ++i;
                }
            }
            i = -1;
            continue;
        }
    }
    
    private int b(final String s) {
        int i = 0;
        while (true) {
            while (i < this.o.length) {
                if (this.o[i].c().equals(s)) {
                    if (i >= 0) {
                        return this.r[i];
                    }
                    return -1;
                }
                else {
                    ++i;
                }
            }
            i = -1;
            continue;
        }
    }
    
    private void b(final h.a a) {
        int n;
        int intValue;
        if (a.c() == com.tremorvideo.sdk.android.richmedia.h.b.W) {
            final int[] array = (int[])a.e();
            n = array[0];
            intValue = array[1];
            ac.a("event-triggered", new String[][] { { "event_action", a.b().name() }, { "event_response", "start-video-index" }, { "internal_tag", Integer.toString(a.a()) } }, new String[][] { { "video_tag", "video-" + (n + 1) }, { "playback_mode", Integer.toString(intValue) } });
        }
        else {
            n = a.c().ordinal() - com.tremorvideo.sdk.android.richmedia.h.b.d.ordinal();
            intValue = (int)a.e();
        }
        this.c(n, intValue);
        if (this.c != null) {
            this.c.a(com.tremorvideo.sdk.android.richmedia.h.c.b);
        }
    }
    
    private int c(final h.a a) {
        int n = -1;
        switch (n$7.a[a.c().ordinal()]) {
            case 1: {
                n = 1;
                break;
            }
            case 2: {
                n = 2;
                break;
            }
            case 3: {
                n = 3;
                break;
            }
            case 4: {
                n = 4;
                break;
            }
            case 5: {
                if (a.e() != null) {
                    n = ((int[])a.e())[0] + 1;
                    break;
                }
                break;
            }
        }
        return this.b("video-" + n);
    }
    
    private void c(final int n, final int n2) {
        this.a(n, n2, -1);
    }
    
    private void t() {
        final int n = 0;
        if (!this.h && !this.b && (this.n == null || this.n.a == null)) {
            boolean b;
            if (this.f.k().getVisibility() == 0 && this.f.a()) {
                b = true;
            }
            else {
                b = false;
            }
            if (b && this.n != null && this.g != null && this.l != this.s) {
                final aw[] g = this.n.g();
                for (int length = g.length, i = n; i < length; ++i) {
                    final aw aw = g[i];
                    if (!this.a(aw)) {
                        final aw.b a = aw.a();
                        if (a == com.tremorvideo.sdk.android.videoad.aw.b.ag) {
                            this.m.add(aw);
                            this.g.a(aw, this.b(aw));
                        }
                        else if (a == com.tremorvideo.sdk.android.videoad.aw.b.ah) {
                            this.m.add(aw);
                            this.g.a(aw, this.b(aw));
                        }
                        else if (a == com.tremorvideo.sdk.android.videoad.aw.b.ai) {
                            if (this.f.b(1)) {
                                this.m.add(aw);
                                this.g.a(aw, this.b(aw));
                            }
                        }
                        else if (a == com.tremorvideo.sdk.android.videoad.aw.b.aj) {
                            if (this.f.b(2)) {
                                this.m.add(aw);
                                this.g.a(aw, this.b(aw));
                            }
                        }
                        else if (a == com.tremorvideo.sdk.android.videoad.aw.b.ak) {
                            if (this.f.b(3)) {
                                this.m.add(aw);
                                this.g.a(aw, this.b(aw));
                            }
                        }
                        else if (a != com.tremorvideo.sdk.android.videoad.aw.b.al && this.s <= aw.e() && this.l > aw.e()) {
                            this.m.add(aw);
                            this.g.a(aw, this.b(aw));
                        }
                    }
                }
                this.s = this.l;
            }
        }
    }
    
    private void u() {
        if (this.n != null && this.g != null) {
            final aw[] g = this.n.g();
            for (int length = g.length, i = 0; i < length; ++i) {
                final aw aw = g[i];
                if (!this.a(aw) && aw.e() >= this.f.i()) {
                    this.m.add(aw);
                    this.g.a(aw, this.b(aw));
                }
            }
        }
    }
    
    private void v() {
        this.post((Runnable)new Runnable() {
            @Override
            public void run() {
                while (true) {
                    while (true) {
                        Label_0113: {
                            try {
                                if (com.tremorvideo.sdk.android.richmedia.n.this.A != -1 && com.tremorvideo.sdk.android.richmedia.n.this.z != null) {
                                    if (com.tremorvideo.sdk.android.richmedia.n.this.A == 255) {
                                        com.tremorvideo.sdk.android.richmedia.n.this.m();
                                    }
                                    else {
                                        if (com.tremorvideo.sdk.android.richmedia.n.this.A != 254 || com.tremorvideo.sdk.android.richmedia.n.this.n == null) {
                                            break Label_0113;
                                        }
                                        com.tremorvideo.sdk.android.richmedia.n.this.o();
                                    }
                                    com.tremorvideo.sdk.android.richmedia.n.this.f.a(0);
                                    return;
                                }
                                break;
                            }
                            catch (Exception ex) {
                                ac.e("TremorLog_error::VideoPlayBack::RefreshVideo " + ex.getMessage());
                                return;
                            }
                        }
                        if (com.tremorvideo.sdk.android.richmedia.n.this.A == 253 && com.tremorvideo.sdk.android.richmedia.n.this.n != null) {
                            com.tremorvideo.sdk.android.richmedia.n.this.n();
                            continue;
                        }
                        if (!com.tremorvideo.sdk.android.richmedia.n.this.c.o()) {
                            com.tremorvideo.sdk.android.richmedia.n.this.t = true;
                            continue;
                        }
                        final k a = com.tremorvideo.sdk.android.richmedia.n.this.c.a(com.tremorvideo.sdk.android.richmedia.n.this.z.a(com.tremorvideo.sdk.android.richmedia.n.this.A), com.tremorvideo.sdk.android.richmedia.n.this.B);
                        com.tremorvideo.sdk.android.richmedia.n.this.a(a.a, a.b, a.f, a.g);
                        continue;
                    }
                }
            }
        });
    }
    
    public int a() {
        return this.f.g();
    }
    
    public void a(final float n, final float n2, final float n3, final float n4) {
        final RelativeLayout$LayoutParams layoutParams = new RelativeLayout$LayoutParams(Math.round(n3), Math.round(n4));
        layoutParams.leftMargin = Math.round(n);
        layoutParams.topMargin = Math.round(n2);
        this.f.a(Math.round(n3), Math.round(n4));
        this.f.k().setLayoutParams((ViewGroup$LayoutParams)layoutParams);
    }
    
    public void a(final int n, final int n2) {
        this.c(n, n2);
    }
    
    public void a(final h.a a) {
        if (!this.b) {
            String s;
            while (true) {
                while (true) {
                    int n;
                    try {
                        this.c.a(a);
                        n = -1;
                        if ((a.c() == com.tremorvideo.sdk.android.richmedia.h.b.u && !this.c()) || (a.c() == com.tremorvideo.sdk.android.richmedia.h.b.l && !this.d())) {
                            return;
                        }
                        if (a.b() == com.tremorvideo.sdk.android.richmedia.h.c.c) {
                            this.u();
                        }
                        if (a.c() == com.tremorvideo.sdk.android.richmedia.h.b.b) {
                            this.a(this.c.b().g().b((int)a.e()));
                            final int n2 = n;
                            if (this.g != null) {
                                this.post((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        com.tremorvideo.sdk.android.richmedia.n.this.g.a(Integer.toString(a.a()), n2);
                                    }
                                });
                            }
                            return;
                        }
                    }
                    catch (Exception ex) {
                        ac.a(ex);
                        return;
                    }
                    if (a.c() == com.tremorvideo.sdk.android.richmedia.h.b.U) {
                        final o b = this.c.b();
                        this.c.a(this.f, this.m);
                        final String name = a.b().name();
                        final String[] array = { "event_response", "pause-and-go-to-scene" };
                        final String[] array2 = { "internal_tag", Integer.toString(a.a()) };
                        final String string = Integer.toString(b.d());
                        final String[] array3 = { "paused_scene", Integer.toString(b.d()) };
                        final String string2 = Long.toString(b.a.b);
                        String string3;
                        if (b.a.e == null) {
                            string3 = "null";
                        }
                        else {
                            string3 = Integer.toString(b.a.e.d());
                        }
                        ac.a("event-triggered", new String[][] { { "event_action", name }, array, array2 }, new String[][] { { "current_scene", string }, array3, { "timeline_paused", string2 }, { "paused_video", string3 }, { "video_paused", Integer.toString(b.a.g) } });
                        if (this.f.d) {
                            this.post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    com.tremorvideo.sdk.android.richmedia.n.this.n = null;
                                    com.tremorvideo.sdk.android.richmedia.n.this.f.c();
                                    com.tremorvideo.sdk.android.richmedia.n.this.a(com.tremorvideo.sdk.android.richmedia.n.this.c.b().g().b((int)a.e()));
                                }
                            });
                            final int n2 = n;
                            continue;
                        }
                        this.a(this.c.b().g().b((int)a.e()));
                        final int n2 = n;
                        continue;
                    }
                    else {
                        if (a.c() == com.tremorvideo.sdk.android.richmedia.h.b.V) {
                            final int intValue = (int)a.e();
                            final o b2 = this.c.b();
                            final o b3 = this.c.b().g().b(intValue);
                            this.a(b3, b3.a.a);
                            this.c.a(b3.a.c, b3.a.d);
                            final o b4 = this.c.b().g().b(intValue);
                            Label_2290: {
                                if (b3.a.e != null) {
                                    this.a(b3.a.e, b3.a.f, b3.a.g, b3.a.h);
                                    ac.a("event-triggered", new String[][] { { "event_action", a.b().name() }, { "event_response", "resume-scene" }, { "internal_tag", Integer.toString(a.a()) } }, new String[][] { { "current_scene", Integer.toString(b2.d()) }, { "resumed_scene", Integer.toString(b4.d()) }, { "timeline_resumed", Long.toString(this.c.j()) }, { "resumed_video", Integer.toString(b3.a.e.d()) }, { "video_resumed", Integer.toString(b3.a.g) } });
                                    final int n2 = -1;
                                    break Label_2290;
                                }
                                if (this.f.n != null) {
                                    int n2 = this.b(this.f.n.c());
                                    if (this.f.n.h() == null) {
                                        this.post((Runnable)new Runnable() {
                                            @Override
                                            public void run() {
                                                com.tremorvideo.sdk.android.richmedia.n.this.f.c();
                                                com.tremorvideo.sdk.android.richmedia.n.this.f.k().setVisibility(4);
                                            }
                                        });
                                        n2 = n2;
                                    }
                                }
                                else {
                                    final int n2 = -1;
                                }
                                ac.a("event-triggered", new String[][] { { "event_action", a.b().name() }, { "event_response", "resume-scene" }, { "internal_tag", Integer.toString(a.a()) } }, new String[][] { { "current_scene", Integer.toString(b2.d()) }, { "resumed_scene", Integer.toString(b4.d()) }, { "timeline_resumed", Long.toString(this.c.j()) }, { "resumed_video", "null" }, { "video_resumed", "-1" } });
                                break Label_2290;
                            }
                            continue;
                        }
                        if (a.c() == com.tremorvideo.sdk.android.richmedia.h.b.c) {
                            final int[] array4 = (int[])a.e();
                            this.a(this.c.b().g().b(array4[0]));
                            this.c.b(array4[1]);
                            this.c.i();
                            final int n2 = n;
                            continue;
                        }
                        if (com.tremorvideo.sdk.android.richmedia.h.a(a.c())) {
                            this.b(a);
                            final int n2 = this.c(a);
                            continue;
                        }
                        if (a.c() == com.tremorvideo.sdk.android.richmedia.h.b.k) {
                            this.post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    com.tremorvideo.sdk.android.richmedia.n.this.n = null;
                                    com.tremorvideo.sdk.android.richmedia.n.this.f.c();
                                }
                            });
                            final int n2 = n;
                            continue;
                        }
                        if (a.c() == com.tremorvideo.sdk.android.richmedia.h.b.h) {
                            int n2 = n;
                            if (this.f.n == null) {
                                continue;
                            }
                            final int n3 = n2 = this.b(this.f.n.c());
                            if (this.f.n.h() == null) {
                                this.post((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        com.tremorvideo.sdk.android.richmedia.n.this.f.e();
                                    }
                                });
                                n2 = n3;
                                continue;
                            }
                            continue;
                        }
                        else if (a.c() == com.tremorvideo.sdk.android.richmedia.h.b.j) {
                            int n2 = n;
                            if (this.f.n == null) {
                                continue;
                            }
                            final int n4 = n2 = this.b(this.f.n.c());
                            if (this.f.n.h() == null) {
                                this.post((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        com.tremorvideo.sdk.android.richmedia.n.this.f.e();
                                        com.tremorvideo.sdk.android.richmedia.n.this.f.k().setVisibility(4);
                                    }
                                });
                                n2 = n4;
                                continue;
                            }
                            continue;
                        }
                        else if (a.c() == com.tremorvideo.sdk.android.richmedia.h.b.i) {
                            this.post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    if (com.tremorvideo.sdk.android.richmedia.n.this.f.o == null || com.tremorvideo.sdk.android.richmedia.n.this.f.o.h() != null) {
                                        return;
                                    }
                                    int n;
                                    if (a.e() == null) {
                                        n = com.tremorvideo.sdk.android.richmedia.n.this.f.q;
                                    }
                                    else {
                                        n = (int)a.e();
                                    }
                                    if (com.tremorvideo.sdk.android.richmedia.n.this.n != null && com.tremorvideo.sdk.android.richmedia.n.this.n == com.tremorvideo.sdk.android.richmedia.n.this.f.o && n == com.tremorvideo.sdk.android.richmedia.n.this.f.q && n == com.tremorvideo.sdk.android.richmedia.n.this.f.p && !com.tremorvideo.sdk.android.richmedia.n.this.f.d) {
                                        com.tremorvideo.sdk.android.richmedia.n.this.f.j();
                                        com.tremorvideo.sdk.android.richmedia.n.this.f.k().setVisibility(0);
                                        return;
                                    }
                                    com.tremorvideo.sdk.android.richmedia.n.this.a(com.tremorvideo.sdk.android.richmedia.n.this.f.o, n, com.tremorvideo.sdk.android.richmedia.n.this.f.r, com.tremorvideo.sdk.android.richmedia.n.this.q.get(com.tremorvideo.sdk.android.richmedia.n.this.f.o.d()));
                                }
                            });
                            int n2 = n;
                            if (this.f.o != null) {
                                n2 = this.b(this.f.o.c());
                                continue;
                            }
                            continue;
                        }
                        else {
                            if (a.c() == com.tremorvideo.sdk.android.richmedia.h.b.T) {
                                final JSONObject jsonObject = new JSONObject((String)a.e());
                                this.post((Runnable)new Runnable() {
                                    final /* synthetic */ int a = jsonObject.getInt("video-index");
                                    final /* synthetic */ int b = Integer.parseInt(jsonObject.getString("playback-mode"));
                                    
                                    @Override
                                    public void run() {
                                        com.tremorvideo.sdk.android.richmedia.n.this.a(com.tremorvideo.sdk.android.richmedia.n.this.a(this.a), this.b, com.tremorvideo.sdk.android.richmedia.n.this.p[this.a], com.tremorvideo.sdk.android.richmedia.n.this.q.get(this.a));
                                    }
                                });
                                final int n2 = n;
                                continue;
                            }
                            if (a.c() == com.tremorvideo.sdk.android.richmedia.h.b.Q) {
                                s = (String)a.e();
                                final int n2 = n;
                                if (this.g != null) {
                                    break;
                                }
                                continue;
                            }
                            else if (a.c() == com.tremorvideo.sdk.android.richmedia.h.b.l) {
                                this.b = true;
                                int n2 = n;
                                if (this.g != null) {
                                    this.g.a(this.c, false);
                                    n2 = n;
                                    continue;
                                }
                                continue;
                            }
                            else {
                                if (a.c() == com.tremorvideo.sdk.android.richmedia.h.b.S) {
                                    this.c.b = false;
                                    this.c.b((int)a.e());
                                    this.c.a(1, true);
                                    final int n2 = n;
                                    continue;
                                }
                                if (a.c() == com.tremorvideo.sdk.android.richmedia.h.b.Y) {
                                    final int[] array5 = (int[])a.e();
                                    ac.a("event-triggered", new String[][] { { "event_action", a.b().name() }, { "event_response", "GotoAndPlayDirectional" } }, new String[][] { { "current_scene", Integer.toString(this.c.b().d()) }, { "current_time", Long.toString(this.c.j()) }, { "play_forward", Boolean.toString(array5[1] == 1) }, { "play_loop", Boolean.toString(array5[2] == 1) }, { "playback_time", Integer.toString(array5[0]) } });
                                    this.c.b = false;
                                    this.c.b(array5[0]);
                                    this.c.a(array5[1], array5[2] == 1);
                                    final int n2 = n;
                                    continue;
                                }
                                if (a.c() == com.tremorvideo.sdk.android.richmedia.h.b.R) {
                                    this.c.b = true;
                                    this.c.b((int)a.e());
                                    final int n2 = n;
                                    continue;
                                }
                                int n2 = n;
                                if (a.c() != com.tremorvideo.sdk.android.richmedia.h.b.X) {
                                    continue;
                                }
                                final boolean[] array6 = (boolean[])a.e();
                                ac.a("event-triggered", new String[][] { { "event_action", a.b().name() }, { "event_response", "play-current-scene" } }, new String[][] { { "current_scene", Integer.toString(this.c.b().d()) }, { "current_time", Long.toString(this.c.j()) }, { "play_forward", Boolean.toString(array6[0]) }, { "play_loop", Boolean.toString(array6[1]) } });
                                this.c.b = false;
                                if (array6[0]) {
                                    this.c.a(1, array6[1]);
                                    n2 = n;
                                    continue;
                                }
                                this.c.a(-1, array6[1]);
                                n2 = n;
                                continue;
                            }
                        }
                    }
                    break;
                }
            }
            this.g.a(Integer.toString(a.a()), aw.b.as, -1, s);
        }
    }
    
    public void a(final a g) {
        this.g = g;
    }
    
    public void a(final o o) {
        this.a(o, false);
    }
    
    public void a(final o o, final boolean b) {
        ((Activity)this.aa).runOnUiThread((Runnable)new b(o, b));
    }
    
    public void a(final aw.b b) {
        final aw[] g = this.n.g();
        for (int length = g.length, i = 0; i < length; ++i) {
            final aw aw = g[i];
            if (!this.a(aw) && aw.a() == b) {
                this.m.add(aw);
                this.g.a(aw, this.b(aw));
            }
        }
        if (b == aw.b.al && this.c != null) {
            this.c.a(com.tremorvideo.sdk.android.richmedia.h.c.c);
        }
    }
    
    public void a(final com.tremorvideo.sdk.android.videoad.h h) {
        this.c.a(h);
    }
    
    public void a(final String s) {
        this.post((Runnable)new Runnable() {
            @Override
            public void run() {
                com.tremorvideo.sdk.android.richmedia.n.this.g.a(s, -1);
            }
        });
    }
    
    public void a(final boolean b) {
        if (!b) {
            this.t = true;
            this.v = this.x;
            this.w = this.y;
        }
        this.c.k();
        ac.e("rotate");
        this.f.a(0);
        this.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (Build$VERSION.SDK_INT < 16) {
                    com.tremorvideo.sdk.android.richmedia.n.this.getViewTreeObserver().removeGlobalOnLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                }
                else {
                    com.tremorvideo.sdk.android.richmedia.n.this.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                }
                com.tremorvideo.sdk.android.richmedia.n.this.t = true;
                com.tremorvideo.sdk.android.richmedia.n.this.u = true;
                com.tremorvideo.sdk.android.richmedia.n.this.v = com.tremorvideo.sdk.android.richmedia.n.this.getWidth();
                com.tremorvideo.sdk.android.richmedia.n.this.w = com.tremorvideo.sdk.android.richmedia.n.this.getHeight();
            }
        });
    }
    
    public boolean a(final af af) {
        return !this.i;
    }
    
    public int b() {
        return this.f.i();
    }
    
    public void b(final int n, final int n2) {
        this.p[n] = n2;
        final ArrayList<aw> list = this.q.get(n);
        list.clear();
        final Iterator<aw> iterator = this.m.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
    }
    
    public void b(final boolean b) {
        if (this.j > 0) {
            if (b) {
                this.j = 1;
            }
            else {
                this.j = 0;
                this.h = false;
                if (this.i) {
                    this.i = false;
                    this.f.j();
                }
            }
        }
    }
    
    public boolean c() {
        return this.M && this.k >= this.L * 1000;
    }
    
    public boolean d() {
        return this.ab == null || this.Q;
    }
    
    public com.tremorvideo.sdk.android.videoad.o.a e() {
        if (this.g != null) {
            return this.g.g();
        }
        return com.tremorvideo.sdk.android.videoad.o.a.f;
    }
    
    public m f() {
        return this.c;
    }
    
    public void g() {
        this.i();
    }
    
    protected int getSuggestedMinimumHeight() {
        return 0;
    }
    
    protected int getSuggestedMinimumWidth() {
        return 0;
    }
    
    public void h() {
        ((SensorManager)this.getContext().getSystemService("sensor")).unregisterListener((SensorEventListener)this);
        this.b = true;
        this.d.cancel();
        this.f.n();
        this.c.a();
    }
    
    public void i() {
        if (this.j == 0) {
            this.h = true;
            if (this.f.a()) {
                this.i = true;
                this.f.d();
            }
        }
        ++this.j;
    }
    
    public void j() {
        if (this.j > 0) {
            --this.j;
            if (this.j == 0) {
                this.h = false;
                if (this.i) {
                    this.i = false;
                    this.f.j();
                }
            }
        }
    }
    
    protected void k() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: iconst_1       
        //     1: istore_1       
        //     2: invokestatic    android/os/SystemClock.elapsedRealtime:()J
        //     5: lstore_3       
        //     6: lload_3        
        //     7: aload_0        
        //     8: getfield        com/tremorvideo/sdk/android/richmedia/n.e:J
        //    11: lsub           
        //    12: lstore          5
        //    14: aload_0        
        //    15: getfield        com/tremorvideo/sdk/android/richmedia/n.h:Z
        //    18: ifne            289
        //    21: aload_0        
        //    22: getfield        com/tremorvideo/sdk/android/richmedia/n.f:Lcom/tremorvideo/sdk/android/richmedia/af;
        //    25: invokevirtual   com/tremorvideo/sdk/android/richmedia/af.b:()Z
        //    28: ifne            289
        //    31: aload_0        
        //    32: lload_3        
        //    33: putfield        com/tremorvideo/sdk/android/richmedia/n.e:J
        //    36: aload_0        
        //    37: aload_0        
        //    38: getfield        com/tremorvideo/sdk/android/richmedia/n.k:I
        //    41: i2l            
        //    42: lload           5
        //    44: ladd           
        //    45: l2i            
        //    46: putfield        com/tremorvideo/sdk/android/richmedia/n.k:I
        //    49: iload_1        
        //    50: ifeq            62
        //    53: aload_0        
        //    54: getfield        com/tremorvideo/sdk/android/richmedia/n.c:Lcom/tremorvideo/sdk/android/richmedia/m;
        //    57: lload           5
        //    59: invokevirtual   com/tremorvideo/sdk/android/richmedia/m.a:(J)V
        //    62: aload_0        
        //    63: getfield        com/tremorvideo/sdk/android/richmedia/n.V:Z
        //    66: ifeq            86
        //    69: iload_1        
        //    70: ifeq            86
        //    73: aload_0        
        //    74: aload_0        
        //    75: getfield        com/tremorvideo/sdk/android/richmedia/n.U:I
        //    78: i2l            
        //    79: lload           5
        //    81: ladd           
        //    82: l2i            
        //    83: putfield        com/tremorvideo/sdk/android/richmedia/n.U:I
        //    86: aload_0        
        //    87: getfield        com/tremorvideo/sdk/android/richmedia/n.f:Lcom/tremorvideo/sdk/android/richmedia/af;
        //    90: invokevirtual   com/tremorvideo/sdk/android/richmedia/af.a:()Z
        //    93: ifeq            109
        //    96: aload_0        
        //    97: aload_0        
        //    98: getfield        com/tremorvideo/sdk/android/richmedia/n.l:I
        //   101: i2l            
        //   102: lload           5
        //   104: ladd           
        //   105: l2i            
        //   106: putfield        com/tremorvideo/sdk/android/richmedia/n.l:I
        //   109: aload_0        
        //   110: getfield        com/tremorvideo/sdk/android/richmedia/n.f:Lcom/tremorvideo/sdk/android/richmedia/af;
        //   113: lload           5
        //   115: invokevirtual   com/tremorvideo/sdk/android/richmedia/af.a:(J)V
        //   118: aload_0        
        //   119: invokespecial   com/tremorvideo/sdk/android/richmedia/n.t:()V
        //   122: aload_0        
        //   123: getfield        com/tremorvideo/sdk/android/richmedia/n.ab:Lcom/tremorvideo/sdk/android/videoad/aw;
        //   126: ifnull          221
        //   129: aload_0        
        //   130: getfield        com/tremorvideo/sdk/android/richmedia/n.Q:Z
        //   133: ifne            221
        //   136: aload_0        
        //   137: getfield        com/tremorvideo/sdk/android/richmedia/n.n:Lcom/tremorvideo/sdk/android/richmedia/l;
        //   140: ifnull          304
        //   143: aload_0        
        //   144: getfield        com/tremorvideo/sdk/android/richmedia/n.n:Lcom/tremorvideo/sdk/android/richmedia/l;
        //   147: invokevirtual   com/tremorvideo/sdk/android/richmedia/l.d:()I
        //   150: ifle            304
        //   153: aload_0        
        //   154: getfield        com/tremorvideo/sdk/android/richmedia/n.f:Lcom/tremorvideo/sdk/android/richmedia/af;
        //   157: getfield        com/tremorvideo/sdk/android/richmedia/af.d:Z
        //   160: ifeq            304
        //   163: aload_0        
        //   164: getfield        com/tremorvideo/sdk/android/richmedia/n.U:I
        //   167: aload_0        
        //   168: getfield        com/tremorvideo/sdk/android/richmedia/n.f:Lcom/tremorvideo/sdk/android/richmedia/af;
        //   171: invokevirtual   com/tremorvideo/sdk/android/richmedia/af.g:()I
        //   174: iadd           
        //   175: i2l            
        //   176: lstore_3       
        //   177: lload_3        
        //   178: aload_0        
        //   179: getfield        com/tremorvideo/sdk/android/richmedia/n.ab:Lcom/tremorvideo/sdk/android/videoad/aw;
        //   182: invokevirtual   com/tremorvideo/sdk/android/videoad/aw.e:()I
        //   185: i2l            
        //   186: lcmp           
        //   187: ifle            221
        //   190: aload_0        
        //   191: getfield        com/tremorvideo/sdk/android/richmedia/n.m:Ljava/util/ArrayList;
        //   194: aload_0        
        //   195: getfield        com/tremorvideo/sdk/android/richmedia/n.ab:Lcom/tremorvideo/sdk/android/videoad/aw;
        //   198: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   201: pop            
        //   202: aload_0        
        //   203: getfield        com/tremorvideo/sdk/android/richmedia/n.g:Lcom/tremorvideo/sdk/android/richmedia/n$a;
        //   206: aload_0        
        //   207: getfield        com/tremorvideo/sdk/android/richmedia/n.ab:Lcom/tremorvideo/sdk/android/videoad/aw;
        //   210: iconst_m1      
        //   211: invokeinterface com/tremorvideo/sdk/android/richmedia/n$a.a:(Lcom/tremorvideo/sdk/android/videoad/aw;I)V
        //   216: aload_0        
        //   217: iconst_1       
        //   218: putfield        com/tremorvideo/sdk/android/richmedia/n.Q:Z
        //   221: aload_0        
        //   222: getfield        com/tremorvideo/sdk/android/richmedia/n.t:Z
        //   225: ifeq            271
        //   228: aload_0        
        //   229: getfield        com/tremorvideo/sdk/android/richmedia/n.u:Z
        //   232: ifne            257
        //   235: aload_0        
        //   236: getfield        com/tremorvideo/sdk/android/richmedia/n.v:I
        //   239: aload_0        
        //   240: getfield        com/tremorvideo/sdk/android/richmedia/n.x:I
        //   243: if_icmpne       257
        //   246: aload_0        
        //   247: getfield        com/tremorvideo/sdk/android/richmedia/n.w:I
        //   250: aload_0        
        //   251: getfield        com/tremorvideo/sdk/android/richmedia/n.y:I
        //   254: if_icmpeq       271
        //   257: aload_0        
        //   258: iconst_0       
        //   259: putfield        com/tremorvideo/sdk/android/richmedia/n.t:Z
        //   262: aload_0        
        //   263: iconst_0       
        //   264: putfield        com/tremorvideo/sdk/android/richmedia/n.u:Z
        //   267: aload_0        
        //   268: invokespecial   com/tremorvideo/sdk/android/richmedia/n.v:()V
        //   271: iload_1        
        //   272: ifeq            288
        //   275: aload_0        
        //   276: new             Lcom/tremorvideo/sdk/android/richmedia/n$9;
        //   279: dup            
        //   280: aload_0        
        //   281: invokespecial   com/tremorvideo/sdk/android/richmedia/n$9.<init>:(Lcom/tremorvideo/sdk/android/richmedia/n;)V
        //   284: invokevirtual   com/tremorvideo/sdk/android/richmedia/n.post:(Ljava/lang/Runnable;)Z
        //   287: pop            
        //   288: return         
        //   289: iconst_0       
        //   290: istore_1       
        //   291: goto            31
        //   294: astore          7
        //   296: aload           7
        //   298: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
        //   301: goto            122
        //   304: aload_0        
        //   305: getfield        com/tremorvideo/sdk/android/richmedia/n.U:I
        //   308: istore_2       
        //   309: iload_2        
        //   310: i2l            
        //   311: lstore_3       
        //   312: goto            177
        //   315: astore          7
        //   317: aload           7
        //   319: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
        //   322: goto            221
        //   325: astore          7
        //   327: aload           7
        //   329: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
        //   332: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  118    122    294    304    Ljava/lang/Exception;
        //  122    177    315    325    Ljava/lang/Exception;
        //  177    221    315    325    Ljava/lang/Exception;
        //  275    288    325    333    Ljava/lang/Exception;
        //  304    309    315    325    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 167, Size: 167
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:653)
        //     at java.util.ArrayList.get(ArrayList.java:429)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3303)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public View l() {
        return this.f.k();
    }
    
    public void m() {
        final RelativeLayout$LayoutParams layoutParams = new RelativeLayout$LayoutParams(-1, -1);
        layoutParams.addRule(10);
        layoutParams.addRule(12);
        layoutParams.addRule(11);
        layoutParams.addRule(9);
        this.f.a(this.getWidth(), this.getHeight());
        this.f.k().setLayoutParams((ViewGroup$LayoutParams)layoutParams);
    }
    
    public void n() {
        final int width = this.getWidth();
        final int height = this.getHeight();
        final int a = this.n.a();
        final int b = this.n.b();
        final View k = this.f.k();
        final float n = a / b;
        int n2;
        int n3;
        if (n < width / height) {
            n2 = (int)(width / n);
            n3 = width;
        }
        else {
            n3 = (int)(n * height);
            n2 = height;
        }
        ac.e("VideoCentered: DeviceScreen_WxH:" + width + "x" + height + " VideoSource_WxH:" + a + "x" + b + " VideoDisplay_WxH:" + n3 + "x" + n2);
        final RelativeLayout$LayoutParams layoutParams = new RelativeLayout$LayoutParams(n3, n2);
        layoutParams.addRule(13);
        this.f.a(n3, n2);
        k.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        k.forceLayout();
    }
    
    public void o() {
        final int width = this.getWidth();
        final int height = this.getHeight();
        final int a = this.n.a();
        final int b = this.n.b();
        final View k = this.f.k();
        final float n = a / b;
        int n2;
        int n3;
        if (n > width / height) {
            n2 = (int)(width / n);
            n3 = width;
        }
        else {
            n3 = (int)(n * height);
            n2 = height;
        }
        ac.e("VideoFit: DeviceScreen_WxH:" + width + "x" + height + " VideoSource_WxH:" + a + "x" + b + " VideoDisplay_WxH:" + n3 + "x" + n2);
        final RelativeLayout$LayoutParams layoutParams = new RelativeLayout$LayoutParams(n3, n2);
        layoutParams.addRule(13);
        this.f.a(n3, n2);
        k.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        k.forceLayout();
    }
    
    public void onAccuracyChanged(final Sensor sensor, final int n) {
    }
    
    protected void onAttachedToWindow() {
        this.W = true;
        if (this.Z) {
            this.Z = false;
            this.post((Runnable)new c(-1, this.n));
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        this.x = canvas.getWidth();
        this.y = canvas.getHeight();
        canvas.save();
        canvas.translate((float)(this.getWidth() / 2), (float)(this.getHeight() / 2));
        while (true) {
            try {
                this.c.b(canvas);
                canvas.restore();
            }
            catch (Exception ex) {
                ac.a(ex);
                continue;
            }
            break;
        }
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        Label_0020: {
            if (n != 21) {
                break Label_0020;
            }
            while (true) {
                while (true) {
                    try {
                        this.c.e();
                        return super.onKeyDown(n, keyEvent);
                        // iftrue(Label_0044:, n != 22)
                        this.c.f();
                        return super.onKeyDown(n, keyEvent);
                    }
                    catch (Exception ex) {
                        ac.a(ex);
                        return super.onKeyDown(n, keyEvent);
                    }
                    continue;
                    Label_0044: {
                        if (n == 19) {
                            this.c.g();
                            continue;
                        }
                    }
                    if (n == 20) {
                        this.c.h();
                        continue;
                    }
                    if (n == 66 || n == 23) {
                        this.c.d();
                        continue;
                    }
                    if (n == 4 && this.c()) {
                        this.c.c();
                        continue;
                    }
                    continue;
                }
            }
        }
    }
    
    public void onSensorChanged(final SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 1) {
            final long currentTimeMillis = System.currentTimeMillis();
            if (this.D == 0L) {
                this.D = currentTimeMillis;
                this.E = sensorEvent.values[0];
                this.F = sensorEvent.values[1];
                this.G = sensorEvent.values[2];
            }
            else if (currentTimeMillis - this.D > 100L) {
                final long n = currentTimeMillis - this.D;
                this.D = currentTimeMillis;
                final float e = sensorEvent.values[0];
                final float f = sensorEvent.values[1];
                final float g = sensorEvent.values[2];
                if (Math.abs(e + f + g - this.E - this.F - this.G) / n * 10000.0f > 800.0f) {
                    this.H += n;
                    this.I = 0L;
                    if (this.H > 300L) {
                        if (!this.f.b()) {
                            this.c.l();
                        }
                        this.H = 0L;
                    }
                }
                else {
                    this.I += n;
                    if (this.I > 150L) {
                        this.H = 0L;
                    }
                }
                this.E = e;
                this.F = f;
                this.G = g;
            }
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        boolean b = false;
        Label_0180: {
            try {
                if (this.N != com.tremorvideo.sdk.android.richmedia.o.b.a) {
                    this.a(motionEvent);
                }
                if (this.f != null) {
                    this.f.a(motionEvent);
                }
                b = this.f.b();
                if (motionEvent.getAction() == 0) {
                    this.S = true;
                    final int round = Math.round(motionEvent.getX());
                    final int n = this.getWidth() / 2;
                    final int round2 = Math.round(motionEvent.getY());
                    final int n2 = this.getHeight() / 2;
                    if (!b) {
                        this.c.c(round - n, round2 - n2);
                        return true;
                    }
                    return true;
                }
                else {
                    if (motionEvent.getAction() != 2) {
                        break Label_0180;
                    }
                    final int round3 = Math.round(motionEvent.getX());
                    final int n3 = this.getWidth() / 2;
                    final int round4 = Math.round(motionEvent.getY());
                    final int n4 = this.getHeight() / 2;
                    if (!b) {
                        this.c.d(round3 - n3, round4 - n4);
                        return true;
                    }
                    return true;
                }
            }
            catch (Exception ex) {
                ac.a(ex);
            }
            return super.onTouchEvent(motionEvent);
        }
        if (motionEvent.getAction() != 3 && motionEvent.getAction() != 1) {
            return super.onTouchEvent(motionEvent);
        }
        this.S = false;
        this.c.b().b = false;
        final int round5 = Math.round(motionEvent.getX());
        final int n5 = this.getWidth() / 2;
        final int round6 = Math.round(motionEvent.getY());
        final int n6 = this.getHeight() / 2;
        if (!b) {
            this.c.e(round5 - n5, round6 - n6);
        }
        return true;
    }
    
    public void p() {
        if (this.T) {
            return;
        }
        while (true) {
            try {
                this.h();
                this.T = true;
                this.b = true;
                if (this.g != null) {
                    this.g.a(this.c, true);
                }
            }
            catch (Exception ex) {
                ac.a(ex);
                continue;
            }
            break;
        }
    }
    
    public void q() {
        final aw[] g = this.n.g();
        for (int length = g.length, i = 0; i < length; ++i) {
            final aw aw = g[i];
            if (!this.a(aw)) {
                final aw.b a = aw.a();
                if (a == com.tremorvideo.sdk.android.videoad.aw.b.ag || a == com.tremorvideo.sdk.android.videoad.aw.b.ah || a == com.tremorvideo.sdk.android.videoad.aw.b.ai || a == com.tremorvideo.sdk.android.videoad.aw.b.aj || a == com.tremorvideo.sdk.android.videoad.aw.b.ak || a == com.tremorvideo.sdk.android.videoad.aw.b.al) {
                    ac.e("Current Video Time: " + this.l);
                    this.m.add(aw);
                    this.g.a(aw, this.b(aw));
                }
                else if (a == com.tremorvideo.sdk.android.videoad.aw.b.j) {
                    this.m.add(aw);
                    this.g.a(aw, this.b(aw));
                }
            }
        }
    }
    
    public boolean r() {
        return this.c.m();
    }
    
    public int s() {
        if (this.f != null) {
            return this.f.h();
        }
        return -1;
    }
    
    public interface a
    {
        void a(final m p0, final boolean p1);
        
        void a(final aw p0, final int p1);
        
        void a(final String p0, final int p1);
        
        void a(final String p0, final aw.b p1, final int p2, final String p3);
        
        com.tremorvideo.sdk.android.videoad.o.a g();
    }
    
    private class b implements Runnable
    {
        o a;
        boolean b;
        
        public b(final o a, final boolean b) {
            this.a = a;
            this.b = b;
        }
        
        @Override
        public void run() {
            com.tremorvideo.sdk.android.richmedia.n.this.c.a(this.a, this.b);
            com.tremorvideo.sdk.android.richmedia.n.this.N = this.a.f();
            com.tremorvideo.sdk.android.richmedia.n.this.K = 0.0f;
            if (com.tremorvideo.sdk.android.richmedia.n.this.N != com.tremorvideo.sdk.android.richmedia.o.b.a && com.tremorvideo.sdk.android.richmedia.n.this.S) {
                this.a.b = true;
            }
        }
    }
    
    public class c implements Runnable
    {
        int a;
        l b;
        
        public c(final int a, final l b) {
            this.a = -1;
            this.a = a;
            this.b = b;
        }
        
        @Override
        public void run() {
            if (com.tremorvideo.sdk.android.richmedia.n.this.n == null) {
                com.tremorvideo.sdk.android.richmedia.n.this.n = this.b;
            }
            while (true) {
                try {
                    com.tremorvideo.sdk.android.richmedia.n.this.f.c();
                    com.tremorvideo.sdk.android.richmedia.n.this.f.a(this.b, this.a, com.tremorvideo.sdk.android.richmedia.n.this.A);
                    com.tremorvideo.sdk.android.richmedia.n.this.f.k().setVisibility(0);
                }
                catch (Exception ex) {
                    ac.a(ex);
                    continue;
                }
                break;
            }
        }
    }
}
