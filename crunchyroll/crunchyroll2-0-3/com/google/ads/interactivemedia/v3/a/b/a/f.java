// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b.a;

import java.util.Iterator;
import com.google.ads.interactivemedia.v3.a.b.j;
import java.util.ArrayList;
import java.io.IOException;
import com.google.ads.interactivemedia.v3.a.b.e;
import com.google.ads.interactivemedia.v3.a.t;
import com.google.ads.interactivemedia.v3.a.q;
import com.google.ads.interactivemedia.v3.a.b.h;
import com.google.ads.interactivemedia.v3.a.b.b;
import java.util.Map;
import com.google.ads.interactivemedia.v3.a.c.a;
import com.google.ads.interactivemedia.v3.a.w;
import java.lang.reflect.Type;
import com.google.ads.interactivemedia.v3.a.b.c;
import com.google.ads.interactivemedia.v3.a.x;

public final class f implements x
{
    private final c a;
    private final boolean b;
    
    public f(final c a, final boolean b) {
        this.a = a;
        this.b = b;
    }
    
    private w<?> a(final com.google.ads.interactivemedia.v3.a.f f, final Type type) {
        if (type == Boolean.TYPE || type == Boolean.class) {
            return l.f;
        }
        return f.a(com.google.ads.interactivemedia.v3.a.c.a.a(type));
    }
    
    @Override
    public <T> w<T> a(final com.google.ads.interactivemedia.v3.a.f f, final com.google.ads.interactivemedia.v3.a.c.a<T> a) {
        final Type b = a.b();
        if (!Map.class.isAssignableFrom(a.a())) {
            return null;
        }
        final Type[] b2 = com.google.ads.interactivemedia.v3.a.b.b.b(b, com.google.ads.interactivemedia.v3.a.b.b.e(b));
        return (w<T>)new a(f, b2[0], (w<Object>)this.a(f, b2[0]), b2[1], (w<Object>)f.a(a.a(b2[1])), (h<? extends Map<Object, Object>>)this.a.a(a));
    }
    
    private final class a<K, V> extends w<Map<K, V>>
    {
        private final w<K> b;
        private final w<V> c;
        private final h<? extends Map<K, V>> d;
        
        public a(final com.google.ads.interactivemedia.v3.a.f f, final Type type, final w<K> w, final Type type2, final w<V> w2, final h<? extends Map<K, V>> d) {
            this.b = new k<K>(f, w, type);
            this.c = new k<V>(f, w2, type2);
            this.d = d;
        }
        
        private String a(final com.google.ads.interactivemedia.v3.a.l l) {
            if (l.i()) {
                final q m = l.m();
                if (m.p()) {
                    return String.valueOf(m.a());
                }
                if (m.o()) {
                    return Boolean.toString(m.f());
                }
                if (m.q()) {
                    return m.b();
                }
                throw new AssertionError();
            }
            else {
                if (l.j()) {
                    return "null";
                }
                throw new AssertionError();
            }
        }
        
        public Map<K, V> a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
            final com.google.ads.interactivemedia.v3.a.d.b f = a.f();
            if (f == com.google.ads.interactivemedia.v3.a.d.b.i) {
                a.j();
                return null;
            }
            final Map map = (Map)this.d.a();
            if (f == com.google.ads.interactivemedia.v3.a.d.b.a) {
                a.a();
                while (a.e()) {
                    a.a();
                    final K b = this.b.b(a);
                    if (map.put(b, this.c.b(a)) != null) {
                        throw new t("duplicate key: " + b);
                    }
                    a.b();
                }
                a.b();
                return (Map<K, V>)map;
            }
            a.c();
            while (a.e()) {
                e.a.a(a);
                final K b2 = this.b.b(a);
                if (map.put(b2, this.c.b(a)) != null) {
                    throw new t("duplicate key: " + b2);
                }
            }
            a.d();
            return (Map<K, V>)map;
        }
        
        @Override
        public void a(final com.google.ads.interactivemedia.v3.a.d.c c, final Map<K, V> map) throws IOException {
            final int n = 0;
            final int n2 = 0;
            if (map == null) {
                c.f();
                return;
            }
            if (!f.this.b) {
                c.d();
                for (final Map.Entry<K, V> entry : map.entrySet()) {
                    c.a(String.valueOf(entry.getKey()));
                    this.c.a(c, entry.getValue());
                }
                c.e();
                return;
            }
            final ArrayList<com.google.ads.interactivemedia.v3.a.l> list = new ArrayList<com.google.ads.interactivemedia.v3.a.l>(map.size());
            final ArrayList<V> list2 = new ArrayList<V>(map.size());
            final Iterator<Map.Entry<K, V>> iterator2 = map.entrySet().iterator();
            boolean b = false;
            while (iterator2.hasNext()) {
                final Map.Entry<K, V> entry2 = iterator2.next();
                final com.google.ads.interactivemedia.v3.a.l a = this.b.a(entry2.getKey());
                list.add(a);
                list2.add(entry2.getValue());
                boolean b2;
                if (a.g() || a.h()) {
                    b2 = true;
                }
                else {
                    b2 = false;
                }
                b |= b2;
            }
            if (b) {
                c.b();
                for (int i = n2; i < list.size(); ++i) {
                    c.b();
                    j.a((com.google.ads.interactivemedia.v3.a.l)list.get(i), c);
                    this.c.a(c, (V)list2.get(i));
                    c.c();
                }
                c.c();
                return;
            }
            c.d();
            for (int j = n; j < list.size(); ++j) {
                c.a(this.a((com.google.ads.interactivemedia.v3.a.l)list.get(j)));
                this.c.a(c, (V)list2.get(j));
            }
            c.e();
        }
    }
}
