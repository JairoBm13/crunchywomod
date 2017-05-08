// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import com.google.ads.interactivemedia.v3.a.b.g$com.google.ads.interactivemedia.v3.a.b.g$c;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.Set;
import java.util.Map;
import com.google.ads.interactivemedia.v3.a.b.g$com.google.ads.interactivemedia.v3.a.b.g$b;
import com.google.ads.interactivemedia.v3.a.b.g$com.google.ads.interactivemedia.v3.a.b.g$a;
import java.util.Comparator;
import java.io.Serializable;
import java.util.AbstractMap;

public final class g<K, V> extends AbstractMap<K, V> implements Serializable
{
    static final /* synthetic */ boolean f;
    private static final Comparator<Comparable> g;
    Comparator<? super K> a;
    d<K, V> b;
    int c;
    int d;
    final d<K, V> e;
    private g$a h;
    private g$b i;
    
    static {
        f = !g.class.desiredAssertionStatus();
        g = new Comparator<Comparable>() {
            public int a(final Comparable comparable, final Comparable comparable2) {
                return comparable.compareTo(comparable2);
            }
        };
    }
    
    public g() {
        this((Comparator)com.google.ads.interactivemedia.v3.a.b.g.g);
    }
    
    public g(Comparator<? super K> g) {
        this.c = 0;
        this.d = 0;
        this.e = new d<K, V>();
        if (g == null) {
            g = com.google.ads.interactivemedia.v3.a.b.g.g;
        }
        this.a = (Comparator<? super K>)g;
    }
    
    private void a(final d<K, V> d) {
        final int n = 0;
        final d<K, V> b = d.b;
        final d<K, V> c = d.c;
        final d<K, V> b2 = c.b;
        final d<K, V> c2 = c.c;
        d.c = b2;
        if (b2 != null) {
            b2.a = d;
        }
        this.a(d, c);
        c.b = d;
        d.a = c;
        int h;
        if (b != null) {
            h = b.h;
        }
        else {
            h = 0;
        }
        int h2;
        if (b2 != null) {
            h2 = b2.h;
        }
        else {
            h2 = 0;
        }
        d.h = Math.max(h, h2) + 1;
        final int h3 = d.h;
        int h4 = n;
        if (c2 != null) {
            h4 = c2.h;
        }
        c.h = Math.max(h3, h4) + 1;
    }
    
    private void a(final d<K, V> d, final d<K, V> c) {
        final d<K, V> a = d.a;
        d.a = null;
        if (c != null) {
            c.a = a;
        }
        if (a == null) {
            this.b = c;
            return;
        }
        if (a.b == d) {
            a.b = c;
            return;
        }
        if (!com.google.ads.interactivemedia.v3.a.b.g.f && a.c != d) {
            throw new AssertionError();
        }
        a.c = c;
    }
    
    private boolean a(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    private void b(final d<K, V> d) {
        final int n = 0;
        final d<K, V> b = d.b;
        final d<K, V> c = d.c;
        final d<K, V> b2 = b.b;
        final d<K, V> c2 = b.c;
        d.b = c2;
        if (c2 != null) {
            c2.a = d;
        }
        this.a(d, b);
        b.c = d;
        d.a = b;
        int h;
        if (c != null) {
            h = c.h;
        }
        else {
            h = 0;
        }
        int h2;
        if (c2 != null) {
            h2 = c2.h;
        }
        else {
            h2 = 0;
        }
        d.h = Math.max(h, h2) + 1;
        final int h3 = d.h;
        int h4 = n;
        if (b2 != null) {
            h4 = b2.h;
        }
        b.h = Math.max(h3, h4) + 1;
    }
    
    private void b(d<K, V> a, final boolean b) {
        while (a != null) {
            final d<K, V> b2 = a.b;
            final d<K, V> c = a.c;
            int h;
            if (b2 != null) {
                h = b2.h;
            }
            else {
                h = 0;
            }
            int h2;
            if (c != null) {
                h2 = c.h;
            }
            else {
                h2 = 0;
            }
            final int n = h - h2;
            if (n == -2) {
                final d<K, V> b3 = c.b;
                final d<K, V> c2 = c.c;
                int h3;
                if (c2 != null) {
                    h3 = c2.h;
                }
                else {
                    h3 = 0;
                }
                int h4;
                if (b3 != null) {
                    h4 = b3.h;
                }
                else {
                    h4 = 0;
                }
                final int n2 = h4 - h3;
                if (n2 == -1 || (n2 == 0 && !b)) {
                    this.a(a);
                }
                else {
                    if (!com.google.ads.interactivemedia.v3.a.b.g.f && n2 != 1) {
                        throw new AssertionError();
                    }
                    this.b(c);
                    this.a(a);
                }
                if (b) {
                    break;
                }
            }
            else if (n == 2) {
                final d<K, V> b4 = b2.b;
                final d<K, V> c3 = b2.c;
                int h5;
                if (c3 != null) {
                    h5 = c3.h;
                }
                else {
                    h5 = 0;
                }
                int h6;
                if (b4 != null) {
                    h6 = b4.h;
                }
                else {
                    h6 = 0;
                }
                final int n3 = h6 - h5;
                if (n3 == 1 || (n3 == 0 && !b)) {
                    this.b(a);
                }
                else {
                    if (!com.google.ads.interactivemedia.v3.a.b.g.f && n3 != -1) {
                        throw new AssertionError();
                    }
                    this.a(b2);
                    this.b(a);
                }
                if (b) {
                    break;
                }
            }
            else if (n == 0) {
                a.h = h + 1;
                if (b) {
                    return;
                }
            }
            else {
                if (!com.google.ads.interactivemedia.v3.a.b.g.f && n != -1 && n != 1) {
                    throw new AssertionError();
                }
                a.h = Math.max(h, h2) + 1;
                if (!b) {
                    return;
                }
            }
            a = a.a;
        }
    }
    
    d<K, V> a(final Object o) {
        d<Object, V> a = null;
        if (o == null) {
            return (d<K, V>)a;
        }
        try {
            a = this.a(o, false);
            return (d<K, V>)a;
        }
        catch (ClassCastException ex) {
            return null;
        }
    }
    
    d<K, V> a(final K k, final boolean b) {
        final d<K, V> d = null;
        final Comparator<? super K> a = this.a;
        Object b2 = this.b;
        int n = 0;
        Label_0102: {
            if (b2 == null) {
                n = 0;
                break Label_0102;
            }
            Comparable<K> comparable;
            if (a == com.google.ads.interactivemedia.v3.a.b.g.g) {
                comparable = (Comparable<K>)k;
            }
            else {
                comparable = null;
            }
            d<K, V> d2;
            while (true) {
                if (comparable != null) {
                    n = comparable.compareTo(((d)b2).f);
                }
                else {
                    n = a.compare(k, (K)((d)b2).f);
                }
                if (n == 0) {
                    d2 = (d<K, V>)b2;
                    break;
                }
                Entry<K, V> entry;
                if (n < 0) {
                    entry = ((d)b2).b;
                }
                else {
                    entry = ((d)b2).c;
                }
                if (entry == null) {
                    break Label_0102;
                }
                b2 = entry;
            }
            return d2;
        }
        d<K, V> d2 = d;
        if (b) {
            final d<K, V> e = this.e;
            d c;
            if (b2 == null) {
                if (a == com.google.ads.interactivemedia.v3.a.b.g.g && !(k instanceof Comparable)) {
                    throw new ClassCastException(k.getClass().getName() + " is not Comparable");
                }
                c = new d<K, V>((d<Object, Object>)b2, k, (d<Object, Object>)e, (d<Object, Object>)e.e);
                this.b = (d<K, V>)c;
            }
            else {
                c = new d<K, V>((d<K, V>)b2, k, e, e.e);
                if (n < 0) {
                    ((d)b2).b = c;
                }
                else {
                    ((d)b2).c = c;
                }
                this.b((d<K, V>)b2, true);
            }
            ++this.c;
            ++this.d;
            return (d<K, V>)c;
        }
        return d2;
    }
    
    d<K, V> a(final Entry<?, ?> entry) {
        final d<K, V> a = this.a(entry.getKey());
        int n;
        if (a != null && this.a(a.g, entry.getValue())) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n != 0) {
            return a;
        }
        return null;
    }
    
    void a(final d<K, V> d, final boolean b) {
        int h = 0;
        if (b) {
            d.e.d = d.d;
            d.d.e = d.e;
        }
        final d<K, V> b2 = d.b;
        final d<K, V> c = d.c;
        final d<K, V> a = d.a;
        if (b2 != null && c != null) {
            d<K, V> d2;
            if (b2.h > c.h) {
                d2 = b2.b();
            }
            else {
                d2 = c.a();
            }
            this.a(d2, false);
            final d<K, V> b3 = d.b;
            int h2;
            if (b3 != null) {
                h2 = b3.h;
                d2.b = b3;
                b3.a = d2;
                d.b = null;
            }
            else {
                h2 = 0;
            }
            final d<K, V> c2 = d.c;
            if (c2 != null) {
                h = c2.h;
                d2.c = c2;
                c2.a = d2;
                d.c = null;
            }
            d2.h = Math.max(h2, h) + 1;
            this.a(d, d2);
            return;
        }
        if (b2 != null) {
            this.a(d, b2);
            d.b = null;
        }
        else if (c != null) {
            this.a(d, c);
            d.c = null;
        }
        else {
            this.a(d, null);
        }
        this.b(a, false);
        --this.c;
        ++this.d;
    }
    
    d<K, V> b(final Object o) {
        final d<K, V> a = this.a(o);
        if (a != null) {
            this.a(a, true);
        }
        return a;
    }
    
    @Override
    public void clear() {
        this.b = null;
        this.c = 0;
        ++this.d;
        final d<K, V> e = this.e;
        e.e = e;
        e.d = e;
    }
    
    @Override
    public boolean containsKey(final Object o) {
        return this.a(o) != null;
    }
    
    @Override
    public Set<Entry<K, V>> entrySet() {
        final a h = this.h;
        if (h != null) {
            return h;
        }
        return this.h = new a();
    }
    
    @Override
    public V get(final Object o) {
        final d<K, V> a = this.a(o);
        if (a != null) {
            return a.g;
        }
        return null;
    }
    
    @Override
    public Set<K> keySet() {
        final b i = this.i;
        if (i != null) {
            return i;
        }
        return this.i = new b();
    }
    
    @Override
    public V put(final K k, final V g) {
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        final d<K, V> a = this.a(k, true);
        final V g2 = a.g;
        a.g = g;
        return g2;
    }
    
    @Override
    public V remove(final Object o) {
        final d<K, V> b = this.b(o);
        if (b != null) {
            return b.g;
        }
        return null;
    }
    
    @Override
    public int size() {
        return this.c;
    }
    
    class a extends AbstractSet<Entry<K, V>>
    {
        @Override
        public void clear() {
            com.google.ads.interactivemedia.v3.a.b.g.this.clear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return o instanceof Entry && com.google.ads.interactivemedia.v3.a.b.g.this.a((Entry<?, ?>)o) != null;
        }
        
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return (Iterator<Entry<K, V>>)new g$c<Entry<K, V>>() {
                public Entry<K, V> a() {
                    return (Entry<K, V>)this.b();
                }
            };
        }
        
        @Override
        public boolean remove(final Object o) {
            if (o instanceof Entry) {
                final d<K, V> a = com.google.ads.interactivemedia.v3.a.b.g.this.a((Entry<?, ?>)o);
                if (a != null) {
                    com.google.ads.interactivemedia.v3.a.b.g.this.a(a, true);
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public int size() {
            return com.google.ads.interactivemedia.v3.a.b.g.this.c;
        }
    }
    
    class b extends AbstractSet<K>
    {
        @Override
        public void clear() {
            com.google.ads.interactivemedia.v3.a.b.g.this.clear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return com.google.ads.interactivemedia.v3.a.b.g.this.containsKey(o);
        }
        
        @Override
        public Iterator<K> iterator() {
            return (Iterator<K>)new g$c<K>() {
                public K next() {
                    return (K)this.b().f;
                }
            };
        }
        
        @Override
        public boolean remove(final Object o) {
            return com.google.ads.interactivemedia.v3.a.b.g.this.b(o) != null;
        }
        
        @Override
        public int size() {
            return com.google.ads.interactivemedia.v3.a.b.g.this.c;
        }
    }
    
    private abstract class c<T> implements Iterator<T>
    {
        d<K, V> b;
        d<K, V> c;
        int d;
        
        private c() {
            this.b = com.google.ads.interactivemedia.v3.a.b.g.this.e.d;
            this.c = null;
            this.d = com.google.ads.interactivemedia.v3.a.b.g.this.d;
        }
        
        final d<K, V> b() {
            final d<K, V> b = this.b;
            if (b == com.google.ads.interactivemedia.v3.a.b.g.this.e) {
                throw new NoSuchElementException();
            }
            if (com.google.ads.interactivemedia.v3.a.b.g.this.d != this.d) {
                throw new ConcurrentModificationException();
            }
            this.b = b.d;
            return this.c = b;
        }
        
        @Override
        public final boolean hasNext() {
            return this.b != com.google.ads.interactivemedia.v3.a.b.g.this.e;
        }
        
        @Override
        public final void remove() {
            if (this.c == null) {
                throw new IllegalStateException();
            }
            com.google.ads.interactivemedia.v3.a.b.g.this.a(this.c, true);
            this.c = null;
            this.d = com.google.ads.interactivemedia.v3.a.b.g.this.d;
        }
    }
    
    static final class d<K, V> implements Entry<K, V>
    {
        d<K, V> a;
        d<K, V> b;
        d<K, V> c;
        d<K, V> d;
        d<K, V> e;
        final K f;
        V g;
        int h;
        
        d() {
            this.f = null;
            this.e = this;
            this.d = this;
        }
        
        d(final d<K, V> a, final K f, final d<K, V> d, final d<K, V> e) {
            this.a = a;
            this.f = f;
            this.h = 1;
            this.d = d;
            this.e = e;
            e.d = this;
            d.e = this;
        }
        
        public d<K, V> a() {
            d<K, V> b = this.b;
            d d = this;
            while (b != null) {
                final d<K, V> b2 = b.b;
                d = b;
                b = b2;
            }
            return (d<K, V>)d;
        }
        
        public d<K, V> b() {
            d<K, V> c = this.c;
            d d = this;
            while (c != null) {
                final d<K, V> c2 = c.c;
                d = c;
                c = c2;
            }
            return (d<K, V>)d;
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b2;
            final boolean b = b2 = false;
            if (o instanceof Entry) {
                final Entry entry = (Entry)o;
                if (this.f == null) {
                    b2 = b;
                    if (entry.getKey() != null) {
                        return b2;
                    }
                }
                else {
                    b2 = b;
                    if (!this.f.equals(entry.getKey())) {
                        return b2;
                    }
                }
                if (this.g == null) {
                    b2 = b;
                    if (entry.getValue() != null) {
                        return b2;
                    }
                }
                else {
                    b2 = b;
                    if (!this.g.equals(entry.getValue())) {
                        return b2;
                    }
                }
                b2 = true;
            }
            return b2;
        }
        
        @Override
        public K getKey() {
            return this.f;
        }
        
        @Override
        public V getValue() {
            return this.g;
        }
        
        @Override
        public int hashCode() {
            int hashCode = 0;
            int hashCode2;
            if (this.f == null) {
                hashCode2 = 0;
            }
            else {
                hashCode2 = this.f.hashCode();
            }
            if (this.g != null) {
                hashCode = this.g.hashCode();
            }
            return hashCode2 ^ hashCode;
        }
        
        @Override
        public V setValue(final V g) {
            final V g2 = this.g;
            this.g = g;
            return g2;
        }
        
        @Override
        public String toString() {
            return this.f + "=" + this.g;
        }
    }
}
