// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b.a;

import java.util.ArrayList;
import java.io.IOException;
import java.lang.reflect.Array;
import com.google.ads.interactivemedia.v3.a.d.c;
import java.lang.reflect.Type;
import com.google.ads.interactivemedia.v3.a.b.b;
import java.lang.reflect.GenericArrayType;
import com.google.ads.interactivemedia.v3.a.f;
import com.google.ads.interactivemedia.v3.a.x;
import com.google.ads.interactivemedia.v3.a.w;

public final class a<E> extends w<Object>
{
    public static final x a;
    private final Class<E> b;
    private final w<E> c;
    
    static {
        a = new x() {
            @Override
            public <T> w<T> a(final f f, final com.google.ads.interactivemedia.v3.a.c.a<T> a) {
                final Type b = a.b();
                if (!(b instanceof GenericArrayType) && (!(b instanceof Class) || !((Class)b).isArray())) {
                    return null;
                }
                final Type g = com.google.ads.interactivemedia.v3.a.b.b.g(b);
                return (w<T>)new a(f, (w<Object>)f.a(a.a(g)), (Class<Object>)com.google.ads.interactivemedia.v3.a.b.b.e(g));
            }
        };
    }
    
    public a(final f f, final w<E> w, final Class<E> b) {
        this.c = new k<E>(f, w, b);
        this.b = b;
    }
    
    @Override
    public void a(final c c, final Object o) throws IOException {
        if (o == null) {
            c.f();
            return;
        }
        c.b();
        for (int i = 0; i < Array.getLength(o); ++i) {
            this.c.a(c, (E)Array.get(o, i));
        }
        c.c();
    }
    
    @Override
    public Object b(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
        if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
            a.j();
            return null;
        }
        final ArrayList<E> list = new ArrayList<E>();
        a.a();
        while (a.e()) {
            list.add(this.c.b(a));
        }
        a.b();
        final Object instance = Array.newInstance(this.b, list.size());
        for (int i = 0; i < list.size(); ++i) {
            Array.set(instance, i, list.get(i));
        }
        return instance;
    }
}
