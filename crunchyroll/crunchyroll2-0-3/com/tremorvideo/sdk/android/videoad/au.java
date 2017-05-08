// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.os.AsyncTask;
import android.os.SystemClock;
import java.util.Iterator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.ArrayList;
import android.content.Context;
import java.util.Map;
import java.util.List;

public class au implements bf.a
{
    private Thread A;
    bf a;
    String b;
    av c;
    String d;
    av e;
    n f;
    boolean g;
    List<n.a> h;
    int i;
    long j;
    boolean k;
    boolean l;
    boolean m;
    boolean n;
    c o;
    private Map<String, av> p;
    private Map<String, b> q;
    private List<String> r;
    private n s;
    private av t;
    private boolean u;
    private boolean v;
    private boolean w;
    private boolean x;
    private boolean y;
    private Context z;
    
    public au(final Context z, final String b) {
        int i = 0;
        this.w = false;
        this.x = false;
        this.y = false;
        this.j = 0L;
        this.A = null;
        this.b = b;
        this.z = z;
        this.r = new ArrayList<String>();
        this.q = new HashMap<String, b>();
        this.p = new HashMap<String, av>();
        this.u = false;
        this.v = false;
        this.i = 0;
        for (String[] u = ac.u(); i < u.length; ++i) {
            final String s = u[i];
            this.r.add(s);
            this.q.put(s, new b());
        }
        new x(z.getFilesDir()).b();
    }
    
    private void a(final av av) {
        synchronized (this) {
            while (!av.e()) {
                final n f = av.f();
                f.c();
                com.tremorvideo.sdk.android.videoad.x.b(f);
                av.a();
            }
        }
        final av av2;
        if (av2.e()) {
            this.p.remove(av2.h());
        }
    }
    // monitorexit(this)
    
    private void a(final bf a, final boolean b, final boolean b2) {
        ac.e(" ");
        if (b) {
            ac.a(ac.c.b, "Re-Starting Job: " + a.toString());
        }
        else {
            ac.a(ac.c.b, "Starting Job: " + a.toString());
        }
        this.a = a;
        if (b) {
            this.a.d();
            return;
        }
        this.a.b();
    }
    
    private void a(final bs bs) {
        if (this.p.containsKey(this.d)) {
            ac.e("onRequestDownloaded removing request");
            this.p.remove(this.d);
        }
        this.e = new av(bs, this.d);
        this.p.put(this.d, this.e);
        ac.e(" onRequestDownloaded _ActiveChannel=" + this.d + " _Requests.size()=" + this.p.size());
        this.a(this.e.f());
    }
    
    private void a(final n.a a) {
        bf bf;
        if (a.a == com.tremorvideo.sdk.android.videoad.bf.d.a) {
            bf = new bh(this, this.z, a.b, a.c);
        }
        else if (a.a == com.tremorvideo.sdk.android.videoad.bf.d.b) {
            bf = new bl(this, this.z, a.c);
        }
        else if (a.a == com.tremorvideo.sdk.android.videoad.bf.d.e) {
            bf = new bk(this, this.z, a.c);
        }
        else if (a.a == com.tremorvideo.sdk.android.videoad.bf.d.c) {
            bf = new bj(this, this.z, this.f);
        }
        else if (a.a == com.tremorvideo.sdk.android.videoad.bf.d.f) {
            bf = new bm(this, this.z, this.f);
        }
        else if (a.a == com.tremorvideo.sdk.android.videoad.bf.d.g) {
            bf = new bn(this, this.z, this.f, a.c);
        }
        else if (a.a == com.tremorvideo.sdk.android.videoad.bf.d.h) {
            bf = new bo(this, this.z, this.f);
        }
        else if (a.a == com.tremorvideo.sdk.android.videoad.bf.d.i) {
            bf = new bg(this, this.z, this.f);
        }
        else {
            bf = null;
        }
        if (bf != null) {
            this.a(bf, false, false);
            return;
        }
        this.u = false;
        this.a = null;
    }
    
    private void a(final n f) {
        if (this.o != null) {
            this.o.a(f);
        }
        this.h = f.a();
        this.i = 0;
        this.f = f;
        this.g = false;
        if (this.h.size() > 0) {
            this.a(this.h.get(0));
        }
        else {
            this.q();
            if (ac.d == 0) {
                this.f();
            }
        }
    }
    
    private void a(final String s, final av av, final n n) {
        ac.e("Dumping Unusable Ad.");
        n.c();
        this.a(av, n);
        this.f(s);
    }
    
    private boolean b(final n n) {
        final long g = ac.G();
        if (n.w() > 0L && g - n.x() >= n.w()) {
            ac.e("Ad is expired because its lifetime has elapsed.");
            return true;
        }
        if (new GregorianCalendar().after(n.e())) {
            ac.e("Ad is expired because it cache date has elapsed.");
            return true;
        }
        return false;
    }
    
    private boolean g(final String s) {
        final b b = this.q.get(s);
        return b.a >= ac.j && ac.G() - b.b < 60000L;
    }
    
    private boolean h(final String s) {
        if (this.g(s)) {
            ac.e(s + " is closed because of too many calls this minute");
            return false;
        }
        final b b = this.q.get(s);
        if (!b.e) {
            ac.e(s + " is closed because of no ad match");
            return false;
        }
        final long g = ac.G();
        if (g - b.c <= b.d) {
            ac.e(s + " is closed because of built in call delay: " + (b.d - (g - b.c)) + "ms remaining.");
            return false;
        }
        return true;
    }
    
    private String i(final String s) {
        return s + "precached";
    }
    
    private String m() {
        for (int i = 0; i < 2; ++i) {
            for (final String s : this.r) {
                if (i == 1) {
                    return s;
                }
                if (this.q.get(s).f != null) {
                    return s;
                }
            }
        }
        return null;
    }
    
    private String n() {
        for (int i = 0; i < 2; ++i) {
            for (final String s : this.r) {
                if (!this.p.containsKey(s) && this.h(s)) {
                    if (i == 1) {
                        return s;
                    }
                    if (this.q.get(s).f != null) {
                        return s;
                    }
                    continue;
                }
            }
        }
        return null;
    }
    
    private av o() {
        final Iterator<String> iterator = this.r.iterator();
        while (iterator.hasNext()) {
            final av av = this.p.get(iterator.next());
            if (av != null && !av.c()) {
                return av;
            }
        }
        return null;
    }
    
    private void p() {
        (this.A = new a()).setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread thread, final Throwable t) {
                ac.a(t);
            }
        });
        this.A.start();
    }
    
    private void q() {
        this.x = true;
        if (this.r.size() == 1) {
            ac.e("Pre loading ad...");
            this.e.f().a(this.z);
            if (!this.e.f().k) {
                this.a(this.d, this.e, this.e.f());
            }
        }
        final n d = this.e.d();
        if (this.o != null) {
            this.o.l();
        }
        if (ac.d == 1) {
            this.u = false;
        }
        com.tremorvideo.sdk.android.videoad.x.a(d);
        this.e = null;
        this.f = null;
        this.i = 0;
        this.d = null;
        this.h = null;
        this.a = null;
    }
    
    private boolean r() {
        try {
            this.a.a(this.h.get(this.i).b, this.f);
            this.s();
            this.a = null;
            ++this.i;
            if (this.i < this.h.size()) {
                this.a(this.h.get(this.i));
            }
            else {
                this.q();
                if (ac.d == 0) {
                    this.f();
                }
            }
        }
        catch (Exception ex) {
            ac.a(ex);
            return true;
        }
        return false;
    }
    
    private void s() {
        if (!this.g && this.f != null && this.f.m()) {
            this.g = true;
            new x(this.z.getFilesDir().getAbsoluteFile()).d(this.f);
        }
    }
    
    private void t() {
        while (true) {
        Label_0183_Outer:
            while (true) {
                while (true) {
                    int n2 = 0;
                    int r = 0;
                    Label_0023: {
                        if (this.a == null) {
                            ac.e("processCurrentJob:beginNewDownload");
                            this.f();
                            final int n = 0;
                            n2 = 0;
                            r = 0;
                            break Label_0023;
                        }
                        Label_0223: {
                            break Label_0223;
                            int n = 0;
                            Thread a;
                            Label_0499_Outer:Label_0453_Outer:
                            while (true) {
                            Label_0453:
                                while (true) {
                                Label_0499:
                                    while (true) {
                                        synchronized (this.A) {
                                            this.q.get(this.d).d = 10000L;
                                            this.q.get(this.d).e = false;
                                            // monitorexit(this.A)
                                            this.e = null;
                                            this.f = null;
                                            this.a = null;
                                            this.n = true;
                                            if (n != 0) {
                                                ac.e("NEW REQUEST ERROR/TIMEOUT");
                                                this.u = false;
                                                this.o.j();
                                            }
                                            return;
                                        Label_0396_Outer:
                                            while (true) {
                                                n2 = 1;
                                                break Label_0499;
                                                Block_20: {
                                                Block_14_Outer:
                                                    while (true) {
                                                        while (true) {
                                                            this.a(this.a, true, true);
                                                            n = 0;
                                                            n2 = 0;
                                                            r = 0;
                                                            break Label_0023;
                                                            Label_0267: {
                                                                n = 0;
                                                            }
                                                            n2 = 1;
                                                            r = 0;
                                                            break Label_0023;
                                                            r = (this.r() ? 1 : 0);
                                                            n = 0;
                                                            n2 = 0;
                                                            break Label_0023;
                                                            ac.e("_CurrentJob CANCELLED");
                                                            continue Label_0396_Outer;
                                                        }
                                                        Block_15: {
                                                        Label_0434_Outer:
                                                            while (true) {
                                                                break Block_15;
                                                                Block_18: {
                                                                    while (true) {
                                                                        break Block_18;
                                                                        Label_0408: {
                                                                            continue Label_0453_Outer;
                                                                        }
                                                                    }
                                                                    n = n2;
                                                                    r = 1;
                                                                    n2 = 0;
                                                                    break Label_0023;
                                                                }
                                                                break Block_14_Outer;
                                                                Label_0276: {
                                                                    continue Label_0434_Outer;
                                                                }
                                                            }
                                                            Label_0462: {
                                                                n2 = 0;
                                                            }
                                                            continue Label_0453;
                                                            n = n2;
                                                            r = 1;
                                                            n2 = 0;
                                                            break Label_0023;
                                                            Label_0508:
                                                            n2 = 0;
                                                            continue Label_0499;
                                                            Label_0467:
                                                            break Block_20;
                                                        }
                                                        a = this.A;
                                                        synchronized (this.A) {
                                                            if (this.d != null) {
                                                                this.q.get(this.d).d = 2000L;
                                                            }
                                                            // monitorexit(this.A)
                                                            this.a(((bi)this.a).h());
                                                            this.s();
                                                            this.k = true;
                                                            if (this.o != null) {
                                                                this.o.h();
                                                                n = 0;
                                                                n2 = 0;
                                                                r = 0;
                                                                break Label_0023;
                                                            }
                                                            break;
                                                        }
                                                        continue Block_14_Outer;
                                                    }
                                                    n2 = 1;
                                                    continue Label_0453;
                                                }
                                                continue Label_0499_Outer;
                                            }
                                        }
                                        // iftrue(Label_0276:, this.a.a() != bf.b.e)
                                        // iftrue(Label_0267:, ac.d != 0)
                                        // iftrue(Label_0396:, !this.a instanceof bi)
                                        // iftrue(Label_0462:, !this.a instanceof bi)
                                        // iftrue(Label_0467:, this.a.a() != bf.b.d && this.a.a() != bf.b.c)
                                        // iftrue(Label_0540:, ac.d != 1)
                                        // iftrue(Label_0408:, this.a.a() != bf.b.b)
                                        // iftrue(Label_0531:, this.a.a() != bf.b.f)
                                        // iftrue(Label_0508:, !this.a instanceof bi)
                                        // iftrue(Label_0526:, ac.d != 1)
                                        break Label_0183_Outer;
                                        Label_0526: {
                                            n2 = 0;
                                        }
                                        continue Label_0499;
                                    }
                                    Label_0540: {
                                        n2 = 0;
                                    }
                                    continue Label_0453;
                                }
                            }
                        }
                        Label_0531: {
                            final int n = 0;
                        }
                        n2 = 0;
                        r = 0;
                    }
                    if (n2 != 0) {
                        this.a = null;
                        this.e = null;
                        this.f = null;
                        this.n = true;
                    }
                    if (r == 0) {
                        return;
                    }
                    if (this.e == null || this.e.i().b().size() <= 0) {
                        break Label_0183_Outer;
                    }
                    this.e.j();
                    if (this.e.e()) {
                        break Label_0183_Outer;
                    }
                    n2 = 0;
                    if (n2 != 1) {
                        continue;
                    }
                    break;
                }
                if (this.e != null) {
                    this.p.remove(this.e.h());
                }
                continue Label_0183_Outer;
            }
            int n2 = 1;
            continue;
        }
    }
    
    public void a(final c o) {
        this.o = o;
    }
    
    public void a(final av av, final n n) {
        synchronized (this) {
            av.a();
            if (av.e()) {
                ac.e("removeAdFromRequest removing request");
                this.p.remove(av.h());
            }
            // monitorexit(this)
            com.tremorvideo.sdk.android.videoad.x.b(n);
        }
    }
    
    @Override
    public void a(final bf bf) {
        ac.a(ac.c.b, "Job " + bf.a().name() + ": " + bf.toString());
    }
    
    public void a(String i, final String f) {
        i = this.i(i);
        final b b = new b();
        b.f = f;
        this.r.add(i);
        this.q.put(i, b);
    }
    
    public void a(final boolean m) {
        this.m = m;
        this.x = false;
        if (this.A == null) {
            this.u = true;
            this.p();
        }
        else if (!this.u) {
            synchronized (this.A) {
                final Iterator<String> iterator = this.q.keySet().iterator();
                while (iterator.hasNext()) {
                    this.q.get(iterator.next()).e = true;
                }
            }
            this.u = true;
            this.A.notify();
            // monitorexit(thread)
            ac.e("Resuming Download Manager");
        }
    }
    
    public void a(final boolean b, final c o) {
        this.o = o;
        this.a(b);
    }
    
    public boolean a() {
        synchronized (this) {
            return this.y;
        }
    }
    
    public boolean a(final String s) {
        return this.b(s) || this.b(this.i(s));
    }
    
    public boolean b() {
        boolean b = false;
        if (this.A == null) {
            ac.E();
            ac.e("loadAd: start new background thread and start download");
            this.a(false);
        }
        else {
            if (this.u && !this.a()) {
                return b;
            }
            ac.e("loadAd: make new Ad load");
            if (this.a(ac.p())) {
                ac.e("loadAd: Ad already loaded and ready");
                bx.a(bx.a.c, true);
            }
            else {
                ac.e("loadAd: starting new Ad load");
                synchronized (this.A) {
                    this.u = true;
                    this.q.get(ac.p()).e = true;
                    this.A.notify();
                }
            }
        }
        b = true;
        return b;
    }
    
    public boolean b(final String s) {
        synchronized (this) {
            if (this.p.containsKey(s)) {
                final av av = this.p.get(s);
                if (av != null && av.b()) {
                    final n f = av.f();
                    if (!this.b(f)) {
                        return true;
                    }
                    this.a(s, av, f);
                }
            }
            return false;
        }
    }
    
    public void c() {
        if (this.A != null) {
            synchronized (this.A) {
                this.u = false;
                this.w = true;
                if (this.a != null) {
                    this.a.c();
                }
                this.A.notify();
            }
        }
    }
    
    public boolean c(final String s) {
        return this.d(s) || this.d(this.i(s));
    }
    
    public void d() {
        ac.e("Stopping Current Job");
        final String p = ac.p();
        if (this.p.containsKey(p)) {
            final av c = this.p.get(p);
            if (c != null) {
                this.c = c;
            }
        }
        if (this.A != null) {
            synchronized (this.A) {
                if (this.u) {
                    this.u = false;
                    if (this.a != null) {
                        this.a.c();
                    }
                    this.a = null;
                }
                this.A.notify();
            }
        }
    }
    
    public boolean d(final String s) {
        // monitorenter(this)
        String i = s;
        try {
            if (!this.p.containsKey(s)) {
                i = this.i(s);
            }
            if (this.p.containsKey(i)) {
                final av av = this.p.get(i);
                if (av != null && av.b()) {
                    final n f = av.f();
                    if (this.b(f)) {
                        this.a(i, av, f);
                    }
                    else {
                        if (av.f() instanceof t) {
                            return true;
                        }
                        if (av.f() instanceof s) {
                            return true;
                        }
                        this.a(i, av, f);
                    }
                }
            }
            return false;
        }
        finally {
        }
        // monitorexit(this)
    }
    
    public void e() {
        ac.e("Stopping Download Manager");
        final String p = ac.p();
        if (this.p.containsKey(p)) {
            final av av = this.p.get(p);
            if (av != null && av.b()) {
                this.a(av);
            }
        }
        if (this.A != null) {
            synchronized (this.A) {
                if (this.u) {
                    this.u = false;
                    if (this.a != null) {
                        this.a.c();
                    }
                    this.a = null;
                }
            }
        }
    }
    
    public boolean e(String i) {
        if (!this.v) {
            ac.e("Starting ad for " + i);
            int n;
            if (this.b(i)) {
                ac.e("TremorDebug: startAdView - Ad is ready");
                n = 1;
            }
            else if (this.b(this.i(i))) {
                ac.e("TremorDebug: startAdView - Ad is ready pre-cache channel");
                this.f(i);
                i = this.i(i);
                n = 1;
            }
            else {
                n = 0;
            }
            if (n != 0) {
                ac.e("TremorDebug: startAdView - Ad was ready. Check expriry");
                final av t = this.p.get(i);
                final n f = t.f();
                if (!this.b(f)) {
                    ac.e("TremorDebug: startAdView - Ad was good");
                    this.s = f;
                    this.t = t;
                    this.v = true;
                    ac.e("TremorDebug: startAdView - Returning true");
                    return true;
                }
                ac.e("TremorDebug: startAdView - Ad Expired and dumped.");
                this.a(i, t, f);
            }
            else {
                ac.e("TremorDebug: startAdView - Ad was not ready");
            }
        }
        else {
            ac.e("TremorDebug: startAdView - ad view was aleady started!");
        }
        ac.e("TremorDebug: startAdView - returning false");
        return false;
    }
    
    public void f() {
        final long g = ac.G();
        final String n = this.n();
        ac.e("*beginNewDownload streamingAdRequest=" + this.m);
        if (ac.d == 1 && this.m) {
            final String m = this.m();
            this.d = m;
            this.e = null;
            this.y = false;
            final b b = this.q.get(m);
            if (g - b.b >= 60000L) {
                b.b = g;
                b.a = 0;
            }
            ++b.a;
            b.c = g;
            String s = this.b;
            if (b.f != null) {
                s = b.f;
            }
            this.k = false;
            this.l = false;
            this.n = false;
            this.j = SystemClock.elapsedRealtime();
            this.a(new bi(this, m, s, this.m), false, false);
            return;
        }
        if (n != null) {
            this.d = n;
            this.e = null;
            this.y = false;
            final b b2 = this.q.get(n);
            if (g - b2.b >= 60000L) {
                b2.b = g;
                b2.a = 0;
            }
            ++b2.a;
            b2.c = g;
            String s2 = this.b;
            if (b2.f != null) {
                s2 = b2.f;
            }
            this.k = false;
            this.l = false;
            this.n = false;
            this.j = SystemClock.elapsedRealtime();
            this.a(new bi(this, n, s2, this.m), false, false);
            return;
        }
        final av o = this.o();
        if (o == null) {
            this.y = true;
            return;
        }
        final n g2 = o.g();
        if (g2 != null) {
            this.d = o.h();
            this.e = o;
            this.y = false;
            this.a(g2);
            return;
        }
        this.y = true;
    }
    
    public void f(final String s) {
        ac.g().b();
        if (this.A != null) {
            synchronized (this.A) {
                if (ac.d != 2) {
                    this.q.get(s).e = true;
                    this.A.notify();
                }
            }
        }
    }
    
    public n g() {
        return this.f;
    }
    
    public n h() {
        return this.s;
    }
    
    public bs i() {
        if (this.t == null) {
            return null;
        }
        return this.t.i();
    }
    
    public void j() {
        if (this.v) {
            ac.e("onAdViewed");
            ac.g().b();
            ac.h().a();
            this.a(this.t, this.s);
            if (!this.t.e()) {
                final n f = this.t.f();
                if (f instanceof q) {
                    new d(f).execute((Object[])new Void[0]);
                }
            }
            this.s = null;
            this.t = null;
            this.v = false;
            if (ac.d == 1) {
                this.m = false;
                if (this.c != null) {
                    ac.e("preservedRequest !=null");
                    this.p.put(ac.p(), this.c);
                    ac.e(" onRequestDownloaded _ActiveChannel=" + ac.p() + " _Requests.size()=" + this.p.size());
                    this.c = null;
                    this.u = true;
                }
                else {
                    ac.e("preservedRequest is  false");
                    this.u = false;
                }
            }
            if (ac.d == 2) {
                this.u = false;
            }
            else {
                synchronized (this.A) {
                    this.A.notify();
                }
            }
        }
    }
    
    public boolean k() {
        return this.x;
    }
    
    public boolean l() {
        return this.v;
    }
    
    class a extends Thread
    {
        @Override
        public void run() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aload_0        
            //     1: getfield        com/tremorvideo/sdk/android/videoad/au$a.a:Lcom/tremorvideo/sdk/android/videoad/au;
            //     4: invokestatic    com/tremorvideo/sdk/android/videoad/au.a:(Lcom/tremorvideo/sdk/android/videoad/au;)Z
            //     7: ifeq            17
            //    10: aload_0        
            //    11: getfield        com/tremorvideo/sdk/android/videoad/au$a.a:Lcom/tremorvideo/sdk/android/videoad/au;
            //    14: invokestatic    com/tremorvideo/sdk/android/videoad/au.b:(Lcom/tremorvideo/sdk/android/videoad/au;)V
            //    17: aload_0        
            //    18: monitorenter   
            //    19: aload_0        
            //    20: getfield        com/tremorvideo/sdk/android/videoad/au$a.a:Lcom/tremorvideo/sdk/android/videoad/au;
            //    23: invokestatic    com/tremorvideo/sdk/android/videoad/au.c:(Lcom/tremorvideo/sdk/android/videoad/au;)Z
            //    26: ifeq            45
            //    29: aload_0        
            //    30: monitorexit    
            //    31: ldc             "Background thread exited"
            //    33: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
            //    36: return         
            //    37: astore_2       
            //    38: aload_2        
            //    39: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
            //    42: goto            17
            //    45: aload_0        
            //    46: getfield        com/tremorvideo/sdk/android/videoad/au$a.a:Lcom/tremorvideo/sdk/android/videoad/au;
            //    49: invokestatic    com/tremorvideo/sdk/android/videoad/au.a:(Lcom/tremorvideo/sdk/android/videoad/au;)Z
            //    52: ifne            82
            //    55: ldc             "Download manager stopped"
            //    57: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
            //    60: aload_0        
            //    61: invokevirtual   java/lang/Object.wait:()V
            //    64: aload_0        
            //    65: monitorexit    
            //    66: goto            0
            //    69: astore_2       
            //    70: aload_0        
            //    71: monitorexit    
            //    72: aload_2        
            //    73: athrow         
            //    74: astore_2       
            //    75: aload_2        
            //    76: invokestatic    com/tremorvideo/sdk/android/videoad/ac.a:(Ljava/lang/Throwable;)V
            //    79: goto            0
            //    82: aload_0        
            //    83: getfield        com/tremorvideo/sdk/android/videoad/au$a.a:Lcom/tremorvideo/sdk/android/videoad/au;
            //    86: invokestatic    com/tremorvideo/sdk/android/videoad/au.d:(Lcom/tremorvideo/sdk/android/videoad/au;)Z
            //    89: ifeq            64
            //    92: ldc             "Download manager will be idle until next signal"
            //    94: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
            //    97: aload_0        
            //    98: getfield        com/tremorvideo/sdk/android/videoad/au$a.a:Lcom/tremorvideo/sdk/android/videoad/au;
            //   101: invokestatic    com/tremorvideo/sdk/android/videoad/ac.p:()Ljava/lang/String;
            //   104: invokevirtual   com/tremorvideo/sdk/android/videoad/au.a:(Ljava/lang/String;)Z
            //   107: istore_1       
            //   108: iload_1        
            //   109: ifeq            181
            //   112: getstatic       com/tremorvideo/sdk/android/videoad/ac.r:Z
            //   115: ifeq            130
            //   118: invokestatic    com/tremorvideo/sdk/android/logger/TestAppLogger.getInstance:()Lcom/tremorvideo/sdk/android/logger/TestAppLogger;
            //   121: ldc             "Ad is ready"
            //   123: ldc             "Ad is ready , can call showAd API"
            //   125: ldc             "info"
            //   127: invokevirtual   com/tremorvideo/sdk/android/logger/TestAppLogger.logAdReady:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
            //   130: aload_0        
            //   131: getfield        com/tremorvideo/sdk/android/videoad/au$a.a:Lcom/tremorvideo/sdk/android/videoad/au;
            //   134: getfield        com/tremorvideo/sdk/android/videoad/au.n:Z
            //   137: ifne            148
            //   140: aload_0        
            //   141: getfield        com/tremorvideo/sdk/android/videoad/au$a.a:Lcom/tremorvideo/sdk/android/videoad/au;
            //   144: iconst_1       
            //   145: putfield        com/tremorvideo/sdk/android/videoad/au.l:Z
            //   148: aload_0        
            //   149: invokevirtual   java/lang/Object.wait:()V
            //   152: goto            64
            //   155: astore_2       
            //   156: new             Ljava/lang/StringBuilder;
            //   159: dup            
            //   160: invokespecial   java/lang/StringBuilder.<init>:()V
            //   163: ldc             "Error logAdReady"
            //   165: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   168: aload_2        
            //   169: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //   172: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   175: invokestatic    com/tremorvideo/sdk/android/videoad/ac.e:(Ljava/lang/String;)V
            //   178: goto            130
            //   181: getstatic       com/tremorvideo/sdk/android/videoad/bx$a.c:Lcom/tremorvideo/sdk/android/videoad/bx$a;
            //   184: iconst_1       
            //   185: anewarray       Ljava/lang/Object;
            //   188: dup            
            //   189: iconst_0       
            //   190: iconst_0       
            //   191: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
            //   194: aastore        
            //   195: invokestatic    com/tremorvideo/sdk/android/videoad/bx.a:(Lcom/tremorvideo/sdk/android/videoad/bx$a;[Ljava/lang/Object;)V
            //   198: goto            130
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                            
            //  -----  -----  -----  -----  --------------------------------
            //  0      17     37     45     Ljava/lang/Exception;
            //  17     19     74     82     Ljava/lang/InterruptedException;
            //  19     31     69     74     Any
            //  45     64     69     74     Any
            //  64     66     69     74     Any
            //  70     72     69     74     Any
            //  72     74     74     82     Ljava/lang/InterruptedException;
            //  82     108    69     74     Any
            //  112    130    155    181    Ljava/lang/Exception;
            //  112    130    69     74     Any
            //  130    148    69     74     Any
            //  148    152    69     74     Any
            //  156    178    69     74     Any
            //  181    198    69     74     Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0017:
            //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
            //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:556)
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
    }
    
    class b
    {
        int a;
        long b;
        long c;
        long d;
        boolean e;
        String f;
        
        public b() {
            this.c = 0L;
            this.d = 0L;
            this.e = true;
            this.f = null;
        }
    }
    
    public interface c
    {
        void a(final n p0);
        
        void h();
        
        void j();
        
        void l();
    }
    
    class d extends AsyncTask<Void, Void, Boolean>
    {
        private Exception b;
        private n c;
        
        public d(final n c) {
            this.c = c;
        }
        
        protected Boolean a(final Void... array) {
            try {
                if (!this.c.b()) {
                    this.c.a(au.this.z);
                }
                return true;
            }
            catch (Exception b) {
                this.b = b;
                return false;
            }
        }
        
        protected void a(final Boolean b) {
            if (!b || this.b != null) {
                this.c.E();
            }
        }
    }
}
