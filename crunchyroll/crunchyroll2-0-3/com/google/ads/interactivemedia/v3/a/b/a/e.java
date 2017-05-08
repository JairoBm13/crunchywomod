// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b.a;

import com.google.ads.interactivemedia.v3.a.i;
import com.google.ads.interactivemedia.v3.a.o;
import com.google.ads.interactivemedia.v3.a.n;
import java.util.ArrayList;
import java.io.IOException;
import com.google.ads.interactivemedia.v3.a.l;
import java.util.List;
import com.google.ads.interactivemedia.v3.a.q;
import java.io.Writer;
import com.google.ads.interactivemedia.v3.a.d.c;

public final class e extends c
{
    private static final Writer a;
    private static final q b;
    private final List<l> c;
    private String d;
    private l e;
    
    static {
        a = new Writer() {
            @Override
            public void close() throws IOException {
                throw new AssertionError();
            }
            
            @Override
            public void flush() throws IOException {
                throw new AssertionError();
            }
            
            @Override
            public void write(final char[] array, final int n, final int n2) {
                throw new AssertionError();
            }
        };
        b = new q("closed");
    }
    
    public e() {
        super(com.google.ads.interactivemedia.v3.a.b.a.e.a);
        this.c = new ArrayList<l>();
        this.e = n.a;
    }
    
    private void a(final l e) {
        if (this.d != null) {
            if (!e.j() || this.i()) {
                ((o)this.j()).a(this.d, e);
            }
            this.d = null;
            return;
        }
        if (this.c.isEmpty()) {
            this.e = e;
            return;
        }
        final l j = this.j();
        if (j instanceof i) {
            ((i)j).a(e);
            return;
        }
        throw new IllegalStateException();
    }
    
    private l j() {
        return this.c.get(this.c.size() - 1);
    }
    
    @Override
    public c a(final long n) throws IOException {
        this.a(new q(n));
        return this;
    }
    
    @Override
    public c a(final Number n) throws IOException {
        if (n == null) {
            return this.f();
        }
        if (!this.g()) {
            final double doubleValue = n.doubleValue();
            if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
                throw new IllegalArgumentException("JSON forbids NaN and infinities: " + n);
            }
        }
        this.a(new q(n));
        return this;
    }
    
    @Override
    public c a(final String d) throws IOException {
        if (this.c.isEmpty() || this.d != null) {
            throw new IllegalStateException();
        }
        if (this.j() instanceof o) {
            this.d = d;
            return this;
        }
        throw new IllegalStateException();
    }
    
    @Override
    public c a(final boolean b) throws IOException {
        this.a(new q(Boolean.valueOf(b)));
        return this;
    }
    
    public l a() {
        if (!this.c.isEmpty()) {
            throw new IllegalStateException("Expected one JSON element but was " + this.c);
        }
        return this.e;
    }
    
    @Override
    public c b() throws IOException {
        final i i = new i();
        this.a(i);
        this.c.add(i);
        return this;
    }
    
    @Override
    public c b(final String s) throws IOException {
        if (s == null) {
            return this.f();
        }
        this.a(new q(s));
        return this;
    }
    
    @Override
    public c c() throws IOException {
        if (this.c.isEmpty() || this.d != null) {
            throw new IllegalStateException();
        }
        if (this.j() instanceof i) {
            this.c.remove(this.c.size() - 1);
            return this;
        }
        throw new IllegalStateException();
    }
    
    @Override
    public void close() throws IOException {
        if (!this.c.isEmpty()) {
            throw new IOException("Incomplete document");
        }
        this.c.add(com.google.ads.interactivemedia.v3.a.b.a.e.b);
    }
    
    @Override
    public c d() throws IOException {
        final o o = new o();
        this.a(o);
        this.c.add(o);
        return this;
    }
    
    @Override
    public c e() throws IOException {
        if (this.c.isEmpty() || this.d != null) {
            throw new IllegalStateException();
        }
        if (this.j() instanceof o) {
            this.c.remove(this.c.size() - 1);
            return this;
        }
        throw new IllegalStateException();
    }
    
    @Override
    public c f() throws IOException {
        this.a(n.a);
        return this;
    }
    
    @Override
    public void flush() throws IOException {
    }
}
