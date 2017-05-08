// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

public class r extends x
{
    b o;
    long p;
    int q;
    int r;
    o s;
    boolean t;
    
    public r(final o o) {
        super(o);
        this.o = b.a;
        this.p = 0L;
        this.q = 0;
        this.r = 0;
        this.s = null;
        this.t = false;
    }
    
    @Override
    public h.a a(final boolean t) {
        if (this.o == b.b) {
            this.o = b.c;
            this.s = this.g.g().b(this.r);
            this.p = 0L;
        }
        this.t = t;
        return null;
    }
    
    public o a(final x.a a, final long n) {
        final a a2 = (a)a;
        int n2 = a.e;
        if (this.o == b.b) {
            n2 = a2.a;
        }
        else if (this.o == b.c) {
            n2 = a2.b;
        }
        return this.g.g().b(n2);
    }
    
    @Override
    public void a(final m m, final long n) {
        if (this.o == b.b) {
            this.p = Math.min(this.s.c() - 1L, this.p + n);
        }
        else {
            if (this.o != b.c) {
                super.a(m, n);
                return;
            }
            this.p = Math.min(this.s.c() - 1L, this.p + n);
            if (this.p == this.s.c() - 1L) {
                final boolean t = this.t;
                this.t = false;
                this.o = b.a;
                if (t) {
                    m.b(super.a(com.tremorvideo.sdk.android.richmedia.h.c.d));
                    m.b(super.a(com.tremorvideo.sdk.android.richmedia.h.c.e));
                }
            }
        }
    }
    
    @Override
    protected long b(final x.a a, final long n) {
        if (this.o == b.b) {
            return this.p;
        }
        if (this.o == b.c) {
            return this.p;
        }
        return n % this.g.g().b(a.e).c();
    }
    
    @Override
    public void b() {
        this.a = 0.0f;
        this.b = 0.0f;
        this.o = b.a;
        this.p = 0L;
        this.t = false;
        final a a = this.f.get(0);
        this.q = a.a;
        this.r = a.b;
    }
    
    @Override
    protected x.a c() {
        return new a();
    }
    
    @Override
    public boolean k() {
        return true;
    }
    
    @Override
    public h.a m() {
        if (this.o == b.a) {
            this.o = b.b;
            this.s = this.g.g().b(this.q);
            this.p = 0L;
        }
        return null;
    }
    
    private class a extends x.a
    {
        public int a;
        public int b;
        
        @Override
        public void a(final e e) {
            ((x.a)this).b(e);
            try {
                this.e = e.b();
                this.a = e.b();
                this.b = e.b();
                this.f = e.d();
                this.g = e.b();
                this.h = e.d();
                this.i = e.b();
                this.j = 0;
                this.k = e.d();
                this.l = e.b();
                this.m = e.d();
                this.n = e.b();
                this.o = e.d();
                this.p = e.b();
                this.q = e.d();
                this.r = e.b();
                this.s = e.f();
            }
            catch (Exception ex) {}
        }
    }
    
    private enum b
    {
        a, 
        b, 
        c;
    }
}
