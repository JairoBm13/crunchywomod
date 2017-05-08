// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a;

import java.io.IOException;
import com.google.ads.interactivemedia.v3.a.b.j;
import com.google.ads.interactivemedia.v3.a.d.c;
import com.google.ads.interactivemedia.v3.a.c.a;

final class v<T> extends w<T>
{
    private final s<T> a;
    private final k<T> b;
    private final f c;
    private final com.google.ads.interactivemedia.v3.a.c.a<T> d;
    private final x e;
    private w<T> f;
    
    private v(final s<T> a, final k<T> b, final f c, final com.google.ads.interactivemedia.v3.a.c.a<T> d, final x e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }
    
    private w<T> a() {
        final w<T> f = this.f;
        if (f != null) {
            return f;
        }
        return this.f = this.c.a(this.e, this.d);
    }
    
    public static x a(final com.google.ads.interactivemedia.v3.a.c.a<?> a, final Object o) {
        return new a(o, (com.google.ads.interactivemedia.v3.a.c.a)a, false, (Class)null);
    }
    
    public static x b(final com.google.ads.interactivemedia.v3.a.c.a<?> a, final Object o) {
        return new a(o, (com.google.ads.interactivemedia.v3.a.c.a)a, a.b() == a.a(), (Class)null);
    }
    
    @Override
    public void a(final c c, final T t) throws IOException {
        if (this.a == null) {
            this.a().a(c, t);
            return;
        }
        if (t == null) {
            c.f();
            return;
        }
        j.a(this.a.a(t, this.d.b(), this.c.b), c);
    }
    
    @Override
    public T b(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
        if (this.b == null) {
            return this.a().b(a);
        }
        final l a2 = j.a(a);
        if (a2.j()) {
            return null;
        }
        try {
            return this.b.b(a2, this.d.b(), this.c.a);
        }
        catch (p p) {
            throw p;
        }
        catch (Exception ex) {
            throw new p(ex);
        }
    }
    
    private static class a implements x
    {
        private final com.google.ads.interactivemedia.v3.a.c.a<?> a;
        private final boolean b;
        private final Class<?> c;
        private final s<?> d;
        private final k<?> e;
        
        private a(final Object o, final com.google.ads.interactivemedia.v3.a.c.a<?> a, final boolean b, final Class<?> c) {
            s<?> d;
            if (o instanceof s) {
                d = (s<?>)o;
            }
            else {
                d = null;
            }
            this.d = d;
            k<?> e;
            if (o instanceof k) {
                e = (k<?>)o;
            }
            else {
                e = null;
            }
            this.e = e;
            com.google.ads.interactivemedia.v3.a.b.a.a(this.d != null || this.e != null);
            this.a = a;
            this.b = b;
            this.c = c;
        }
        
        @Override
        public <T> w<T> a(final f f, final com.google.ads.interactivemedia.v3.a.c.a<T> a) {
            int assignable;
            if (this.a != null) {
                if (this.a.equals(a) || (this.b && this.a.b() == a.a())) {
                    assignable = 1;
                }
                else {
                    assignable = 0;
                }
            }
            else {
                assignable = (this.c.isAssignableFrom(a.a()) ? 1 : 0);
            }
            if (assignable != 0) {
                return new v<T>(this.d, this.e, f, a, this, null);
            }
            return null;
        }
    }
}
