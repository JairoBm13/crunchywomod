// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b.a;

import java.util.ArrayList;
import java.io.IOException;
import com.google.ads.interactivemedia.v3.a.d.c;
import com.google.ads.interactivemedia.v3.a.c.a;
import com.google.ads.interactivemedia.v3.a.f;
import com.google.ads.interactivemedia.v3.a.x;
import com.google.ads.interactivemedia.v3.a.w;

public final class g extends w<Object>
{
    public static final x a;
    private final f b;
    
    static {
        a = new x() {
            @Override
            public <T> w<T> a(final f f, final a<T> a) {
                if (a.a() == Object.class) {
                    return (w<T>)new g(f, null);
                }
                return null;
            }
        };
    }
    
    private g(final f b) {
        this.b = b;
    }
    
    @Override
    public void a(final c c, final Object o) throws IOException {
        if (o == null) {
            c.f();
            return;
        }
        final w<?> a = this.b.a(o.getClass());
        if (a instanceof g) {
            c.d();
            c.e();
            return;
        }
        a.a(c, o);
    }
    
    @Override
    public Object b(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
        switch (g$2.a[a.f().ordinal()]) {
            default: {
                throw new IllegalStateException();
            }
            case 1: {
                final ArrayList<Object> list = new ArrayList<Object>();
                a.a();
                while (a.e()) {
                    list.add(this.b(a));
                }
                a.b();
                return list;
            }
            case 2: {
                final com.google.ads.interactivemedia.v3.a.b.g<String, Object> g = new com.google.ads.interactivemedia.v3.a.b.g<String, Object>();
                a.c();
                while (a.e()) {
                    g.put(a.g(), this.b(a));
                }
                a.d();
                return g;
            }
            case 3: {
                return a.h();
            }
            case 4: {
                return a.k();
            }
            case 5: {
                return a.i();
            }
            case 6: {
                a.j();
                return null;
            }
        }
    }
}
