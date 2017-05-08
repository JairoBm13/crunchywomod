// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b.a;

import com.google.ads.interactivemedia.v3.a.t;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import com.google.ads.interactivemedia.v3.a.a.b;
import java.io.IOException;
import com.google.ads.interactivemedia.v3.a.w;
import java.lang.reflect.Type;
import com.google.ads.interactivemedia.v3.a.b.i;
import com.google.ads.interactivemedia.v3.a.c.a;
import java.lang.reflect.Field;
import com.google.ads.interactivemedia.v3.a.f;
import com.google.ads.interactivemedia.v3.a.b.d;
import com.google.ads.interactivemedia.v3.a.e;
import com.google.ads.interactivemedia.v3.a.b.c;
import com.google.ads.interactivemedia.v3.a.x;

public final class h implements x
{
    private final c a;
    private final e b;
    private final d c;
    
    public h(final c a, final e b, final d c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    private b a(final f f, final Field field, final String s, final com.google.ads.interactivemedia.v3.a.c.a<?> a, final boolean b, final boolean b2) {
        return (b)new b(s, b, b2) {
            final w<?> a = f.a(a);
            final /* synthetic */ boolean e = com.google.ads.interactivemedia.v3.a.b.i.a((Type)a.a());
            
            @Override
            void a(final com.google.ads.interactivemedia.v3.a.d.a a, final Object o) throws IOException, IllegalAccessException {
                final Object b = this.a.b(a);
                if (b != null || !this.e) {
                    field.set(o, b);
                }
            }
            
            @Override
            void a(final com.google.ads.interactivemedia.v3.a.d.c c, Object value) throws IOException, IllegalAccessException {
                value = field.get(value);
                new k<Object>(f, this.a, a.b()).a(c, value);
            }
        };
    }
    
    private String a(final Field field) {
        final com.google.ads.interactivemedia.v3.a.a.b b = field.getAnnotation(com.google.ads.interactivemedia.v3.a.a.b.class);
        if (b == null) {
            return this.b.a(field);
        }
        return b.a();
    }
    
    private Map<String, b> a(final f f, com.google.ads.interactivemedia.v3.a.c.a<?> a, Class<?> a2) {
        final LinkedHashMap<String, b> linkedHashMap = new LinkedHashMap<String, b>();
        if (a2.isInterface()) {
            return linkedHashMap;
        }
        final Type b = a.b();
        while (a2 != Object.class) {
            final Field[] declaredFields = a2.getDeclaredFields();
            for (int length = declaredFields.length, i = 0; i < length; ++i) {
                final Field field = declaredFields[i];
                final boolean a3 = this.a(field, true);
                final boolean a4 = this.a(field, false);
                if (a3 || a4) {
                    field.setAccessible(true);
                    final b a5 = this.a(f, field, this.a(field), a.a(com.google.ads.interactivemedia.v3.a.b.b.a(a.b(), a2, field.getGenericType())), a3, a4);
                    final b b2 = linkedHashMap.put(a5.g, a5);
                    if (b2 != null) {
                        throw new IllegalArgumentException(b + " declares multiple JSON fields named " + b2.g);
                    }
                }
            }
            a = a.a(com.google.ads.interactivemedia.v3.a.b.b.a(a.b(), a2, a2.getGenericSuperclass()));
            a2 = a.a();
        }
        return linkedHashMap;
    }
    
    @Override
    public <T> w<T> a(final f f, final com.google.ads.interactivemedia.v3.a.c.a<T> a) {
        final Class<? super T> a2 = a.a();
        if (!Object.class.isAssignableFrom(a2)) {
            return null;
        }
        return new a<T>((com.google.ads.interactivemedia.v3.a.b.h)this.a.a(a), (Map)this.a(f, a, a2));
    }
    
    public boolean a(final Field field, final boolean b) {
        return !this.c.a(field.getType(), b) && !this.c.a(field, b);
    }
    
    public static final class a<T> extends w<T>
    {
        private final com.google.ads.interactivemedia.v3.a.b.h<T> a;
        private final Map<String, b> b;
        
        private a(final com.google.ads.interactivemedia.v3.a.b.h<T> a, final Map<String, b> b) {
            this.a = a;
            this.b = b;
        }
        
        @Override
        public void a(final com.google.ads.interactivemedia.v3.a.d.c c, final T t) throws IOException {
            if (t == null) {
                c.f();
                return;
            }
            c.d();
            try {
                for (final b b : this.b.values()) {
                    if (b.h) {
                        c.a(b.g);
                        b.a(c, t);
                    }
                }
            }
            catch (IllegalAccessException ex) {
                throw new AssertionError();
            }
            c.e();
        }
        
        @Override
        public T b(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
            if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                a.j();
                return null;
            }
            final T a2 = this.a.a();
            try {
                a.c();
                while (a.e()) {
                    final b b = this.b.get(a.g());
                    if (b != null && b.i) {
                        goto Label_0084;
                    }
                    a.n();
                }
            }
            catch (IllegalStateException ex) {
                throw new t(ex);
            }
            catch (IllegalAccessException ex2) {
                throw new AssertionError((Object)ex2);
            }
            a.d();
            return a2;
        }
    }
    
    abstract static class b
    {
        final String g;
        final boolean h;
        final boolean i;
        
        protected b(final String g, final boolean h, final boolean i) {
            this.g = g;
            this.h = h;
            this.i = i;
        }
        
        abstract void a(final com.google.ads.interactivemedia.v3.a.d.a p0, final Object p1) throws IOException, IllegalAccessException;
        
        abstract void a(final com.google.ads.interactivemedia.v3.a.d.c p0, final Object p1) throws IOException, IllegalAccessException;
    }
}
