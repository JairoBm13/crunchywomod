// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

public class t extends x
{
    b o;
    long p;
    int q;
    int r;
    int s;
    o t;
    
    public t(final o o) {
        super(o);
        this.o = b.a;
        this.p = 0L;
        this.q = 0;
        this.r = 0;
        this.s = 0;
        this.t = null;
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
        else if (this.o == b.d) {
            n2 = a2.c;
        }
        return this.g.g().b(n2);
    }
    
    @Override
    public void a(final m m, final long n) {
        if (this.o == b.b) {
            this.p = Math.min(this.t.c() - 1L, this.p + n);
            if (this.p == this.t.c() - 1L) {
                m.b(super.a(com.tremorvideo.sdk.android.richmedia.h.c.j));
            }
        }
        else if (this.o == b.c) {
            this.p = Math.min(this.t.c() - 1L, this.p + n);
            if (this.p == this.t.c() - 1L) {
                this.o = b.a;
                m.b(super.a(com.tremorvideo.sdk.android.richmedia.h.c.l));
            }
        }
        else {
            if (this.o != b.d) {
                super.a(m, n);
                return;
            }
            this.p = Math.min(this.t.c() - 1L, this.p + n);
            if (this.p == this.t.c() - 1L) {
                this.o = b.a;
                m.b(super.a(com.tremorvideo.sdk.android.richmedia.h.c.k));
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
        if (this.o == b.d) {
            return this.p;
        }
        return n % this.g.g().b(a.e).c();
    }
    
    @Override
    public h.a b(final boolean b) {
        if ((this.o == com.tremorvideo.sdk.android.richmedia.t.b.a || this.o == com.tremorvideo.sdk.android.richmedia.t.b.c) && b) {
            this.o = com.tremorvideo.sdk.android.richmedia.t.b.b;
            this.t = this.g.g().b(this.q);
            this.p = 0L;
        }
        else if (this.o == com.tremorvideo.sdk.android.richmedia.t.b.b && !b) {
            this.o = com.tremorvideo.sdk.android.richmedia.t.b.c;
            this.t = this.g.g().b(this.r);
            this.p = 0L;
        }
        return null;
    }
    
    @Override
    public void b() {
        this.a = 0.0f;
        this.b = 0.0f;
        this.o = b.a;
        this.p = 0L;
        final a a = this.f.get(0);
        this.q = a.a;
        this.r = a.b;
        this.s = a.c;
    }
    
    @Override
    public h.a c(final boolean b) {
        if (this.o == com.tremorvideo.sdk.android.richmedia.t.b.b) {
            if (b) {
                this.o = com.tremorvideo.sdk.android.richmedia.t.b.d;
                this.t = this.g.g().b(this.s);
                this.p = 0L;
            }
            else {
                this.o = com.tremorvideo.sdk.android.richmedia.t.b.c;
                this.t = this.g.g().b(this.r);
                this.p = 0L;
            }
        }
        return null;
    }
    
    @Override
    protected x.a c() {
        return new a();
    }
    
    @Override
    public boolean k() {
        return true;
    }
    
    private class a extends x.a
    {
        public int a;
        public int b;
        public int c;
        
        @Override
        public void a(final e e) {
            ((x.a)this).b(e);
            try {
                this.e = e.b();
                this.a = e.b();
                this.c = e.b();
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
        c, 
        d;
    }
}
