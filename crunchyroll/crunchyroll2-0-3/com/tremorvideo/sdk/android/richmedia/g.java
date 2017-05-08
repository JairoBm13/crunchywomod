// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

public class g
{
    public boolean a;
    private q b;
    private int c;
    private int d;
    private long e;
    private boolean f;
    private float g;
    private float h;
    private float i;
    private float j;
    
    public g(final q b) {
        this.a = false;
        this.b = b;
        this.f = false;
        this.e = 0L;
    }
    
    private int a(final p p3, final k k, final int n) {
        final int round = Math.round(k.a - this.b.a + n - this.g + this.i);
        final int round2 = Math.round(round + k.f);
        int n2;
        if (round < p3.d() / -2.0f) {
            n2 = n + ((int)(p3.d() / -2.0f) - round);
        }
        else {
            n2 = n;
            if (round2 > p3.d() / 2.0f) {
                return n + ((int)(p3.d() / 2.0f) - round2);
            }
        }
        return n2;
    }
    
    private int b(final p p3, final k k, final int n) {
        final int round = Math.round(k.b - this.b.b + n - this.h + this.j);
        final int round2 = Math.round(round + k.g);
        int n2;
        if (round < p3.e() / -2.0f) {
            n2 = n + ((int)(p3.e() / -2.0f) - round);
        }
        else {
            n2 = n;
            if (round2 > p3.e() / 2.0f) {
                return n + ((int)(p3.e() / 2.0f) - round2);
            }
        }
        return n2;
    }
    
    private void d(final p p3, final int c, final int d) {
        this.c = c;
        this.d = d;
        this.b.a = this.c - this.g + this.i;
        this.b.b = this.d - this.h + this.j;
    }
    
    public void a() {
        this.f = false;
        this.e = 0L;
        this.i = 0.0f;
        this.j = 0.0f;
        this.g = 0.0f;
        this.h = 0.0f;
        this.c = 0;
        this.d = 0;
        this.a = false;
        this.b.a = 0.0f;
        this.b.b = 0.0f;
    }
    
    public void a(final long n) {
        if (this.b instanceof x) {
            this.e = (this.e + n) % ((x)this.b).j().c();
        }
    }
    
    public void a(final p p) {
        p.c().save();
        if (this.f) {
            p.c().translate(-this.g, -this.h);
            p.c().translate(this.i, this.j);
            p.c().translate((float)this.c, (float)this.d);
        }
        this.b.a(p, this.e);
        p.c().restore();
    }
    
    public void a(final p p3, final int n, final int n2) {
        final k c = this.b.c(p3, this.e);
        this.d(p3, this.a(p3, c, n), this.b(p3, c, n2));
    }
    
    public void a(final p p4, final int n, final int n2, final long e) {
        final k c = this.b.c(p4, e);
        if (!this.f) {
            this.f = true;
            this.e = e;
            this.g = c.a;
            this.h = c.b;
            this.i = c.a - n;
            this.j = c.b - n2;
        }
        else {
            this.i = c.a - n;
            this.j = c.b - n2;
        }
        this.d(p4, n, n2);
    }
    
    public q b() {
        return this.b;
    }
    
    public void b(final p p3, final int n, final int n2) {
        final k c = this.b.c(p3, this.e);
        this.d(p3, this.a(p3, c, n), this.b(p3, c, n2));
    }
    
    public boolean c(final p p3, final int n, final int n2) {
        final k c = this.b.c(p3, this.e);
        return n >= c.a && n2 >= c.b && n < c.a + c.f && n2 < c.g + c.b;
    }
}
