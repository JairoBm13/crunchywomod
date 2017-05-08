// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a;

import java.io.StringWriter;
import java.io.StringReader;
import java.io.Reader;
import java.io.EOFException;
import java.util.Iterator;
import java.io.IOException;
import java.io.Writer;
import com.google.ads.interactivemedia.v3.a.b.a.i;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Collection;
import com.google.ads.interactivemedia.v3.a.b.a.g;
import com.google.ads.interactivemedia.v3.a.b.a.b;
import com.google.ads.interactivemedia.v3.a.b.a.l;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.Type;
import java.util.Collections;
import com.google.ads.interactivemedia.v3.a.b.d;
import com.google.ads.interactivemedia.v3.a.b.c;
import java.util.List;
import com.google.ads.interactivemedia.v3.a.c.a;
import java.util.Map;

public final class f
{
    final j a;
    final r b;
    private final ThreadLocal<Map<com.google.ads.interactivemedia.v3.a.c.a<?>, a<?>>> c;
    private final Map<com.google.ads.interactivemedia.v3.a.c.a<?>, w<?>> d;
    private final List<x> e;
    private final c f;
    private final boolean g;
    private final boolean h;
    private final boolean i;
    private final boolean j;
    
    public f() {
        this(com.google.ads.interactivemedia.v3.a.b.d.a, com.google.ads.interactivemedia.v3.a.d.a, Collections.emptyMap(), false, false, false, true, false, false, u.a, Collections.emptyList());
    }
    
    f(final d d, final e e, final Map<Type, h<?>> map, final boolean g, final boolean b, final boolean i, final boolean h, final boolean j, final boolean b2, final u u, final List<x> list) {
        this.c = new ThreadLocal<Map<com.google.ads.interactivemedia.v3.a.c.a<?>, a<?>>>();
        this.d = Collections.synchronizedMap(new HashMap<com.google.ads.interactivemedia.v3.a.c.a<?>, w<?>>());
        this.a = new j() {};
        this.b = new r() {};
        this.f = new c(map);
        this.g = g;
        this.i = i;
        this.h = h;
        this.j = j;
        final ArrayList<x> list2 = new ArrayList<x>();
        list2.add(l.Q);
        list2.add(com.google.ads.interactivemedia.v3.a.b.a.g.a);
        list2.add(d);
        list2.addAll((Collection<?>)list);
        list2.add(l.x);
        list2.add(l.m);
        list2.add(l.g);
        list2.add(l.i);
        list2.add(l.k);
        list2.add(l.a(Long.TYPE, Long.class, this.a(u)));
        list2.add(l.a(Double.TYPE, Double.class, this.a(b2)));
        list2.add(l.a(Float.TYPE, Float.class, this.b(b2)));
        list2.add(l.r);
        list2.add(l.t);
        list2.add(l.z);
        list2.add(l.B);
        list2.add(l.a(BigDecimal.class, l.v));
        list2.add(l.a(BigInteger.class, l.w));
        list2.add(l.D);
        list2.add(l.F);
        list2.add(l.J);
        list2.add(l.O);
        list2.add(l.H);
        list2.add(l.d);
        list2.add(com.google.ads.interactivemedia.v3.a.b.a.c.a);
        list2.add(l.M);
        list2.add(com.google.ads.interactivemedia.v3.a.b.a.j.a);
        list2.add(com.google.ads.interactivemedia.v3.a.b.a.i.a);
        list2.add(l.K);
        list2.add(com.google.ads.interactivemedia.v3.a.b.a.a.a);
        list2.add(l.R);
        list2.add(l.b);
        list2.add(new b(this.f));
        list2.add(new com.google.ads.interactivemedia.v3.a.b.a.f(this.f, b));
        list2.add(new com.google.ads.interactivemedia.v3.a.b.a.h(this.f, e, d));
        this.e = (List<x>)Collections.unmodifiableList((List<?>)list2);
    }
    
    private com.google.ads.interactivemedia.v3.a.d.c a(final Writer writer) throws IOException {
        if (this.i) {
            writer.write(")]}'\n");
        }
        final com.google.ads.interactivemedia.v3.a.d.c c = new com.google.ads.interactivemedia.v3.a.d.c(writer);
        if (this.j) {
            c.c("  ");
        }
        c.d(this.g);
        return c;
    }
    
    private w<Number> a(final u u) {
        if (u == u.a) {
            return l.n;
        }
        return new w<Number>() {
            public Number a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                return a.l();
            }
            
            @Override
            public void a(final com.google.ads.interactivemedia.v3.a.d.c c, final Number n) throws IOException {
                if (n == null) {
                    c.f();
                    return;
                }
                c.b(n.toString());
            }
        };
    }
    
    private w<Number> a(final boolean b) {
        if (b) {
            return l.p;
        }
        return new w<Number>() {
            public Double a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                return a.k();
            }
            
            @Override
            public void a(final com.google.ads.interactivemedia.v3.a.d.c c, final Number n) throws IOException {
                if (n == null) {
                    c.f();
                    return;
                }
                com.google.ads.interactivemedia.v3.a.f.this.a(n.doubleValue());
                c.a(n);
            }
        };
    }
    
    private void a(final double n) {
        if (Double.isNaN(n) || Double.isInfinite(n)) {
            throw new IllegalArgumentException(n + " is not a valid double value as per JSON specification. To override this" + " behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method.");
        }
    }
    
    private static void a(final Object o, final com.google.ads.interactivemedia.v3.a.d.a a) {
        if (o != null) {
            try {
                if (a.f() != com.google.ads.interactivemedia.v3.a.d.b.j) {
                    throw new m("JSON document was not fully consumed.");
                }
            }
            catch (com.google.ads.interactivemedia.v3.a.d.d d) {
                throw new t(d);
            }
            catch (IOException ex) {
                throw new m(ex);
            }
        }
    }
    
    private w<Number> b(final boolean b) {
        if (b) {
            return l.o;
        }
        return new w<Number>() {
            public Float a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                return (float)a.k();
            }
            
            @Override
            public void a(final com.google.ads.interactivemedia.v3.a.d.c c, final Number n) throws IOException {
                if (n == null) {
                    c.f();
                    return;
                }
                com.google.ads.interactivemedia.v3.a.f.this.a(n.floatValue());
                c.a(n);
            }
        };
    }
    
    public <T> w<T> a(final com.google.ads.interactivemedia.v3.a.c.a<T> a) {
        w<?> w = this.d.get(a);
        if (w == null) {
            Object o = this.c.get();
            boolean b = false;
            if (o == null) {
                o = new HashMap<com.google.ads.interactivemedia.v3.a.c.a<T>, a<Object>>();
                this.c.set((Map<com.google.ads.interactivemedia.v3.a.c.a<?>, a<?>>)o);
                b = true;
            }
            while (true) {
                if ((w = ((Map<com.google.ads.interactivemedia.v3.a.c.a<T>, a<Object>>)o).get(a)) != null) {
                    return (w<T>)w;
                }
                try {
                    final a<Object> a2 = new a<Object>();
                    ((Map<com.google.ads.interactivemedia.v3.a.c.a<T>, a<Object>>)o).put(a, a2);
                    final Iterator<x> iterator = this.e.iterator();
                    while (iterator.hasNext()) {
                        final w<?> a3 = iterator.next().a(this, (com.google.ads.interactivemedia.v3.a.c.a<?>)a);
                        if (a3 != null) {
                            a2.a(a3);
                            this.d.put(a, a3);
                            ((Map<com.google.ads.interactivemedia.v3.a.c.a<T>, a<Object>>)o).remove(a);
                            if (b) {
                                this.c.remove();
                                return (w<T>)a3;
                            }
                            return (w<T>)w;
                        }
                    }
                    throw new IllegalArgumentException("GSON cannot handle " + a);
                }
                finally {
                    ((Map<com.google.ads.interactivemedia.v3.a.c.a<T>, a<Object>>)o).remove(a);
                    if (b) {
                        this.c.remove();
                    }
                }
                continue;
            }
        }
        return (w<T>)w;
    }
    
    public <T> w<T> a(final x x, final com.google.ads.interactivemedia.v3.a.c.a<T> a) {
        final Iterator<x> iterator = this.e.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            final x x2 = iterator.next();
            if (n == 0) {
                if (x2 != x) {
                    continue;
                }
                n = 1;
            }
            else {
                final w<T> a2 = x2.a(this, a);
                if (a2 != null) {
                    return a2;
                }
                continue;
            }
        }
        throw new IllegalArgumentException("GSON cannot serialize " + a);
    }
    
    public <T> w<T> a(final Class<T> clazz) {
        return this.a((com.google.ads.interactivemedia.v3.a.c.a<T>)com.google.ads.interactivemedia.v3.a.c.a.b((Class<T>)clazz));
    }
    
    public <T> T a(final com.google.ads.interactivemedia.v3.a.d.a a, final Type type) throws m, t {
        boolean b = true;
        a.p();
        a.a(true);
        try {
            a.f();
            b = false;
            return (T)this.a(a.a(type)).b(a);
        }
        catch (EOFException ex) {
            if (b) {
                return null;
            }
            throw new t(ex);
        }
        catch (IllegalStateException ex3) {}
        catch (IOException ex4) {
            final IOException ex2;
            throw new t(ex2);
        }
    }
    
    public <T> T a(final Reader reader, final Type type) throws m, t {
        final com.google.ads.interactivemedia.v3.a.d.a a = new com.google.ads.interactivemedia.v3.a.d.a(reader);
        final Object a2 = this.a(a, type);
        a(a2, a);
        return (T)a2;
    }
    
    public <T> T a(final String s, final Class<T> clazz) throws t {
        return com.google.ads.interactivemedia.v3.a.b.i.a(clazz).cast(this.a(s, (Type)clazz));
    }
    
    public <T> T a(final String s, final Type type) throws t {
        if (s == null) {
            return null;
        }
        return this.a(new StringReader(s), type);
    }
    
    public String a(final com.google.ads.interactivemedia.v3.a.l l) {
        final StringWriter stringWriter = new StringWriter();
        this.a(l, stringWriter);
        return stringWriter.toString();
    }
    
    public String a(final Object o) {
        if (o == null) {
            return this.a(n.a);
        }
        return this.a(o, o.getClass());
    }
    
    public String a(final Object o, final Type type) {
        final StringWriter stringWriter = new StringWriter();
        this.a(o, type, stringWriter);
        return stringWriter.toString();
    }
    
    public void a(final com.google.ads.interactivemedia.v3.a.l l, final com.google.ads.interactivemedia.v3.a.d.c c) throws m {
        final boolean g = c.g();
        c.b(true);
        final boolean h = c.h();
        c.c(this.h);
        final boolean i = c.i();
        c.d(this.g);
        try {
            com.google.ads.interactivemedia.v3.a.b.j.a(l, c);
        }
        catch (IOException ex) {
            throw new m(ex);
        }
        finally {
            c.b(g);
            c.c(h);
            c.d(i);
        }
    }
    
    public void a(final com.google.ads.interactivemedia.v3.a.l l, final Appendable appendable) throws m {
        try {
            this.a(l, this.a(com.google.ads.interactivemedia.v3.a.b.j.a(appendable)));
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void a(final Object o, final Type type, final com.google.ads.interactivemedia.v3.a.d.c c) throws m {
        final w<?> a = this.a(com.google.ads.interactivemedia.v3.a.c.a.a(type));
        final boolean g = c.g();
        c.b(true);
        final boolean h = c.h();
        c.c(this.h);
        final boolean i = c.i();
        c.d(this.g);
        try {
            a.a(c, o);
        }
        catch (IOException ex) {
            throw new m(ex);
        }
        finally {
            c.b(g);
            c.c(h);
            c.d(i);
        }
    }
    
    public void a(final Object o, final Type type, final Appendable appendable) throws m {
        try {
            this.a(o, type, this.a(com.google.ads.interactivemedia.v3.a.b.j.a(appendable)));
        }
        catch (IOException ex) {
            throw new m(ex);
        }
    }
    
    @Override
    public String toString() {
        return "{serializeNulls:" + this.g + "factories:" + this.e + ",instanceCreators:" + this.f + "}";
    }
    
    static class a<T> extends w<T>
    {
        private w<T> a;
        
        @Override
        public void a(final com.google.ads.interactivemedia.v3.a.d.c c, final T t) throws IOException {
            if (this.a == null) {
                throw new IllegalStateException();
            }
            this.a.a(c, t);
        }
        
        public void a(final w<T> a) {
            if (this.a != null) {
                throw new AssertionError();
            }
            this.a = a;
        }
        
        @Override
        public T b(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
            if (this.a == null) {
                throw new IllegalStateException();
            }
            return this.a.b(a);
        }
    }
}
