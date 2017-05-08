// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b.a;

import java.util.Map;
import com.google.ads.interactivemedia.v3.a.n;
import com.google.ads.interactivemedia.v3.a.q;
import java.util.Iterator;
import com.google.ads.interactivemedia.v3.a.o;
import com.google.ads.interactivemedia.v3.a.i;
import com.google.ads.interactivemedia.v3.a.d.b;
import java.io.IOException;
import java.util.List;
import java.io.Reader;
import com.google.ads.interactivemedia.v3.a.d.a;

public final class d extends a
{
    private static final Reader a;
    private static final Object b;
    private final List<Object> c;
    
    static {
        a = new Reader() {
            @Override
            public void close() throws IOException {
                throw new AssertionError();
            }
            
            @Override
            public int read(final char[] array, final int n, final int n2) throws IOException {
                throw new AssertionError();
            }
        };
        b = new Object();
    }
    
    private void a(final b b) throws IOException {
        if (this.f() != b) {
            throw new IllegalStateException("Expected " + b + " but was " + this.f());
        }
    }
    
    private Object q() {
        return this.c.get(this.c.size() - 1);
    }
    
    private Object r() {
        return this.c.remove(this.c.size() - 1);
    }
    
    @Override
    public void a() throws IOException {
        this.a(com.google.ads.interactivemedia.v3.a.d.b.a);
        this.c.add(((i)this.q()).iterator());
    }
    
    @Override
    public void b() throws IOException {
        this.a(com.google.ads.interactivemedia.v3.a.d.b.b);
        this.r();
        this.r();
    }
    
    @Override
    public void c() throws IOException {
        this.a(com.google.ads.interactivemedia.v3.a.d.b.c);
        this.c.add(((o)this.q()).o().iterator());
    }
    
    @Override
    public void close() throws IOException {
        this.c.clear();
        this.c.add(d.b);
    }
    
    @Override
    public void d() throws IOException {
        this.a(com.google.ads.interactivemedia.v3.a.d.b.d);
        this.r();
        this.r();
    }
    
    @Override
    public boolean e() throws IOException {
        final b f = this.f();
        return f != com.google.ads.interactivemedia.v3.a.d.b.d && f != com.google.ads.interactivemedia.v3.a.d.b.b;
    }
    
    @Override
    public b f() throws IOException {
        if (this.c.isEmpty()) {
            return com.google.ads.interactivemedia.v3.a.d.b.j;
        }
        final Object q = this.q();
        if (q instanceof Iterator) {
            final boolean b = this.c.get(this.c.size() - 2) instanceof o;
            final Iterator<Object> iterator = (Iterator<Object>)q;
            if (iterator.hasNext()) {
                if (b) {
                    return com.google.ads.interactivemedia.v3.a.d.b.e;
                }
                this.c.add(iterator.next());
                return this.f();
            }
            else {
                if (b) {
                    return com.google.ads.interactivemedia.v3.a.d.b.d;
                }
                return com.google.ads.interactivemedia.v3.a.d.b.b;
            }
        }
        else {
            if (q instanceof o) {
                return com.google.ads.interactivemedia.v3.a.d.b.c;
            }
            if (q instanceof i) {
                return com.google.ads.interactivemedia.v3.a.d.b.a;
            }
            if (q instanceof q) {
                final q q2 = (q)q;
                if (q2.q()) {
                    return com.google.ads.interactivemedia.v3.a.d.b.f;
                }
                if (q2.o()) {
                    return com.google.ads.interactivemedia.v3.a.d.b.h;
                }
                if (q2.p()) {
                    return com.google.ads.interactivemedia.v3.a.d.b.g;
                }
                throw new AssertionError();
            }
            else {
                if (q instanceof n) {
                    return com.google.ads.interactivemedia.v3.a.d.b.i;
                }
                if (q == d.b) {
                    throw new IllegalStateException("JsonReader is closed");
                }
                throw new AssertionError();
            }
        }
    }
    
    @Override
    public String g() throws IOException {
        this.a(com.google.ads.interactivemedia.v3.a.d.b.e);
        final Map.Entry<K, Object> entry = ((Iterator)this.q()).next();
        this.c.add(entry.getValue());
        return (String)entry.getKey();
    }
    
    @Override
    public String h() throws IOException {
        final b f = this.f();
        if (f != com.google.ads.interactivemedia.v3.a.d.b.f && f != com.google.ads.interactivemedia.v3.a.d.b.g) {
            throw new IllegalStateException("Expected " + com.google.ads.interactivemedia.v3.a.d.b.f + " but was " + f);
        }
        return ((q)this.r()).b();
    }
    
    @Override
    public boolean i() throws IOException {
        this.a(com.google.ads.interactivemedia.v3.a.d.b.h);
        return ((q)this.r()).f();
    }
    
    @Override
    public void j() throws IOException {
        this.a(com.google.ads.interactivemedia.v3.a.d.b.i);
        this.r();
    }
    
    @Override
    public double k() throws IOException {
        final b f = this.f();
        if (f != com.google.ads.interactivemedia.v3.a.d.b.g && f != com.google.ads.interactivemedia.v3.a.d.b.f) {
            throw new IllegalStateException("Expected " + com.google.ads.interactivemedia.v3.a.d.b.g + " but was " + f);
        }
        final double c = ((q)this.q()).c();
        if (!this.p() && (Double.isNaN(c) || Double.isInfinite(c))) {
            throw new NumberFormatException("JSON forbids NaN and infinities: " + c);
        }
        this.r();
        return c;
    }
    
    @Override
    public long l() throws IOException {
        final b f = this.f();
        if (f != com.google.ads.interactivemedia.v3.a.d.b.g && f != com.google.ads.interactivemedia.v3.a.d.b.f) {
            throw new IllegalStateException("Expected " + com.google.ads.interactivemedia.v3.a.d.b.g + " but was " + f);
        }
        final long d = ((q)this.q()).d();
        this.r();
        return d;
    }
    
    @Override
    public int m() throws IOException {
        final b f = this.f();
        if (f != com.google.ads.interactivemedia.v3.a.d.b.g && f != com.google.ads.interactivemedia.v3.a.d.b.f) {
            throw new IllegalStateException("Expected " + com.google.ads.interactivemedia.v3.a.d.b.g + " but was " + f);
        }
        final int e = ((q)this.q()).e();
        this.r();
        return e;
    }
    
    @Override
    public void n() throws IOException {
        if (this.f() == com.google.ads.interactivemedia.v3.a.d.b.e) {
            this.g();
            return;
        }
        this.r();
    }
    
    public void o() throws IOException {
        this.a(com.google.ads.interactivemedia.v3.a.d.b.e);
        final Map.Entry<K, Object> entry = ((Iterator)this.q()).next();
        this.c.add(entry.getValue());
        this.c.add(new q((String)entry.getKey()));
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
