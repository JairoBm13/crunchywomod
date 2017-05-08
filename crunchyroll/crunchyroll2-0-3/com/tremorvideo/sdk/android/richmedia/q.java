// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import java.util.ArrayList;
import java.util.Iterator;
import android.graphics.RectF;
import com.tremorvideo.sdk.android.richmedia.a.i;
import com.tremorvideo.sdk.android.richmedia.b.a;
import java.util.List;

public abstract class q
{
    public float a;
    public float b;
    protected com.tremorvideo.sdk.android.richmedia.b c;
    protected h d;
    protected int e;
    protected List<j> f;
    protected o g;
    protected boolean h;
    protected boolean i;
    protected int j;
    protected boolean k;
    protected b l;
    protected int m;
    protected boolean n;
    
    public q(final o g) {
        this.c = new com.tremorvideo.sdk.android.richmedia.b(this);
        this.j = 0;
        this.k = true;
        this.l = q.b.a;
        this.m = 0;
        this.n = false;
        this.g = g;
        this.d = new h();
    }
    
    public static q a(final o o, final int n) throws Exception {
        if (n == 308075814) {
            return new w(o);
        }
        if (n == 1441378760) {
            return new ab(o);
        }
        if (n == 1430499683) {
            return new s(o);
        }
        if (n == 1440879594) {
            return new u(o);
        }
        if (n == 319580241) {
            return new x(o);
        }
        if (n == 1312804287) {
            return new r(o);
        }
        if (n == 323352250) {
            return new ac(o);
        }
        if (n == 2057701471) {
            return new t(o);
        }
        if (n == 1654336090) {
            return new aa(o);
        }
        if (n == 964050621) {
            return new com.tremorvideo.sdk.android.richmedia.b.a(o);
        }
        if (n == 2756143) {
            return new i(o);
        }
        if (n == 14608462) {
            return new z(o);
        }
        if (n == 2570398) {
            return new i(o);
        }
        if (n == 193493196) {
            return new v(o);
        }
        throw new Exception("Loading Scene Object: Unknown type: " + n);
    }
    
    protected RectF a(final p p3, final long n, final RectF rectF) {
        if (this.c.a(0) != null) {
            final com.tremorvideo.sdk.android.richmedia.b.a a = this.c.a(0);
            rectF.left = com.tremorvideo.sdk.android.richmedia.b.a(a.a(this.g).c(p3, n), a.a()).x;
        }
        if (this.c.a(1) != null) {
            final com.tremorvideo.sdk.android.richmedia.b.a a2 = this.c.a(1);
            rectF.right = com.tremorvideo.sdk.android.richmedia.b.a(a2.a(this.g).c(p3, n), a2.a()).x + rectF.left - rectF.left;
        }
        if (this.c.a(2) != null) {
            final com.tremorvideo.sdk.android.richmedia.b.a a3 = this.c.a(2);
            rectF.top = com.tremorvideo.sdk.android.richmedia.b.a(a3.a(this.g).c(p3, n), a3.a()).y;
        }
        if (this.c.a(3) != null) {
            final com.tremorvideo.sdk.android.richmedia.b.a a4 = this.c.a(3);
            rectF.bottom = com.tremorvideo.sdk.android.richmedia.b.a(a4.a(this.g).c(p3, n), a4.a()).y + rectF.top - rectF.top;
        }
        return rectF;
    }
    
    public h.a a(final h.c c) {
        return this.d.a(c);
    }
    
    public h.a a(final boolean b) {
        if (b) {
            return this.d.a(com.tremorvideo.sdk.android.richmedia.h.c.e);
        }
        return null;
    }
    
    protected a a(final long n) {
        final a a = new a();
        final Iterator<j> iterator = this.f.iterator();
        long n2 = Long.MIN_VALUE;
        long a2 = Long.MAX_VALUE;
        while (iterator.hasNext()) {
            final j j = iterator.next();
            long a3 = n2;
            if (j.a() <= n) {
                a3 = n2;
                if (j.a() >= n2) {
                    a.a = j;
                    a3 = j.a();
                }
            }
            if (j.a() >= n && j.a() <= a2) {
                a.b = j;
                a2 = j.a();
            }
            n2 = a3;
        }
        if (a.b == null) {
            a.b = a.a;
        }
        if (a.a != null && a.b != null) {
            final float n3 = a.b.a() - a.a.a();
            if (n3 == 0.0f) {
                a.c = 0.0f;
            }
            else {
                a.c = (n - a.a.a()) / n3;
            }
            return a;
        }
        return null;
    }
    
    public void a() {
    }
    
    public void a(final int j) {
        this.j = j;
    }
    
    public void a(final e e) {
        int i = 0;
        try {
            this.e = e.a();
            if (this.g.g().g() > 1) {
                this.l = q.b.values()[e.b()];
            }
            if (this.g.g().g() > 2) {
                this.m = e.a();
                if ((this.m & 0x1) == 0x1) {
                    this.n = true;
                }
                else {
                    this.n = false;
                }
            }
            this.c.a(e);
            this.d.a(e, this.g.g().g());
            final int b = e.b();
            this.f = new ArrayList<j>(b);
            while (i < b) {
                final j e2 = this.e();
                e2.a(e);
                this.f.add(e2);
                ++i;
            }
            if (this.d.a(com.tremorvideo.sdk.android.richmedia.h.c.d) != null) {
                this.h = true;
            }
            else {
                this.h = false;
            }
            if (this.d.a(com.tremorvideo.sdk.android.richmedia.h.c.k) != null) {
                this.i = true;
                return;
            }
            this.i = false;
        }
        catch (Exception ex) {}
    }
    
    public void a(final m m, final long n) {
    }
    
    public void a(final p p2, final long n) {
    }
    
    public boolean a(final int n, final int n2) {
        return false;
    }
    
    public h.a b(final boolean b) {
        return this.d.a(com.tremorvideo.sdk.android.richmedia.h.c.j);
    }
    
    protected k b(final p p2, final long n) {
        return com.tremorvideo.sdk.android.richmedia.k.h;
    }
    
    public void b() {
        this.a = 0.0f;
        this.b = 0.0f;
    }
    
    public h.a c(final boolean b) {
        return this.d.a(com.tremorvideo.sdk.android.richmedia.h.c.k);
    }
    
    public k c(final p p2, final long n) {
        final k b = this.b(p2, n);
        b.a += this.a;
        b.b += this.b;
        return b;
    }
    
    protected boolean d(boolean b) {
        if (this.j == -1) {
            b = false;
        }
        else if (this.j == 1) {
            return true;
        }
        return b;
    }
    
    protected abstract j e();
    
    public boolean f() {
        return this.d.a();
    }
    
    public boolean g() {
        return this.d.b();
    }
    
    public int h() {
        return this.e;
    }
    
    public h i() {
        return this.d;
    }
    
    public o j() {
        return this.g;
    }
    
    public boolean k() {
        return this.h;
    }
    
    public boolean l() {
        return this.i;
    }
    
    public h.a m() {
        return this.d.a(com.tremorvideo.sdk.android.richmedia.h.c.d);
    }
    
    public boolean n() {
        return !this.n || this.g.g().b;
    }
    
    protected class a
    {
        public j a;
        public j b;
        public float c;
    }
    
    public enum b
    {
        a, 
        b, 
        c;
    }
}
