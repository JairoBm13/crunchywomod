// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

public class s extends ab
{
    public s(final o o) {
        super(o);
    }
    
    private String a(final long n, final long n2, final int n3) {
        final float n4 = n / 1000.0f;
        if (n4 < 0.1) {
            return "0";
        }
        return Integer.toString((int)Math.ceil(n4) + n3);
    }
    
    @Override
    protected String a(final p p3, final ab.a a, final long n) {
        if (this.p != null) {
            return this.p;
        }
        final com.tremorvideo.sdk.android.richmedia.a g = this.g.g();
        final a a2 = (a)a;
        final String a3 = g.a(a2.e);
        final String a4 = g.a(a2.a);
        final String s = "##";
        final b b = com.tremorvideo.sdk.android.richmedia.s.b.values()[a2.b];
        final m.a b2 = p3.b();
        String s2;
        if (b == com.tremorvideo.sdk.android.richmedia.s.b.d) {
            s2 = this.a(n, this.g.c(), a2.c);
        }
        else if (b == com.tremorvideo.sdk.android.richmedia.s.b.c) {
            s2 = this.a(this.g.c() - n, this.g.c(), a2.c);
        }
        else if (b == com.tremorvideo.sdk.android.richmedia.s.b.a) {
            s2 = s;
            if (b2 != null) {
                final int b3 = b2.b();
                final int a5 = b2.a();
                if (b3 != -1 && a5 != -1) {
                    s2 = this.a(b3 - a5, b3, a2.c);
                }
                else {
                    s2 = this.a(0L, 0L, a2.c);
                }
            }
        }
        else {
            s2 = s;
            if (b == com.tremorvideo.sdk.android.richmedia.s.b.b) {
                s2 = s;
                if (b2 != null) {
                    final int b4 = b2.b();
                    final int a6 = b2.a();
                    if (b4 != -1 && a6 != -1) {
                        s2 = this.a(a6, b4, a2.c);
                    }
                    else {
                        s2 = this.a(0L, 0L, a2.c);
                    }
                }
            }
        }
        return a3 + s2 + a4;
    }
    
    @Override
    public void a(final p p2, final long n) {
        super.a(p2, n);
    }
    
    @Override
    protected ab.a c() {
        return new a();
    }
    
    private class a extends ab.a
    {
        public int a;
        public int b;
        public int c;
        
        @Override
        public void a(final e e) {
            ((ab.a)this).b(e);
            try {
                this.e = e.b();
                this.a = e.b();
                this.b = e.b();
                this.c = e.a();
                this.f = e.b();
                this.g = e.b();
                this.h = e.d();
                this.i = e.b();
                this.j = e.d();
                this.k = e.b();
                this.l = e.d();
                this.m = e.b();
                this.n = e.d();
                this.o = e.b();
                this.p = e.d();
                this.q = e.b();
                this.r = e.d();
                this.s = e.b();
                this.t = e.d();
                this.u = e.b();
                this.v = e.d();
                this.w = e.b();
                this.x = e.d();
                this.y = e.b();
                this.z = e.f();
            }
            catch (Exception ex) {}
        }
    }
    
    public enum b
    {
        a, 
        b, 
        c, 
        d;
    }
}
