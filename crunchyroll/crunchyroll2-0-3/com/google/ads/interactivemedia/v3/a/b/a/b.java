// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b.a;

import java.util.Iterator;
import java.io.IOException;
import java.lang.reflect.Type;
import com.google.ads.interactivemedia.v3.a.b.h;
import java.util.Collection;
import com.google.ads.interactivemedia.v3.a.w;
import com.google.ads.interactivemedia.v3.a.c.a;
import com.google.ads.interactivemedia.v3.a.f;
import com.google.ads.interactivemedia.v3.a.b.c;
import com.google.ads.interactivemedia.v3.a.x;

public final class b implements x
{
    private final c a;
    
    public b(final c a) {
        this.a = a;
    }
    
    @Override
    public <T> w<T> a(final f f, final com.google.ads.interactivemedia.v3.a.c.a<T> a) {
        final Type b = a.b();
        final Class<? super T> a2 = a.a();
        if (!Collection.class.isAssignableFrom(a2)) {
            return null;
        }
        final Type a3 = com.google.ads.interactivemedia.v3.a.b.b.a(b, a2);
        return (w<T>)new a(f, a3, (w<Object>)f.a(a.a(a3)), (h<? extends Collection<Object>>)this.a.a((com.google.ads.interactivemedia.v3.a.c.a<? extends Collection<E>>)a));
    }
    
    private static final class a<E> extends w<Collection<E>>
    {
        private final w<E> a;
        private final h<? extends Collection<E>> b;
        
        public a(final f f, final Type type, final w<E> w, final h<? extends Collection<E>> b) {
            this.a = new k<E>(f, w, type);
            this.b = b;
        }
        
        public Collection<E> a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
            if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                a.j();
                return null;
            }
            final Collection collection = (Collection)this.b.a();
            a.a();
            while (a.e()) {
                collection.add(this.a.b(a));
            }
            a.b();
            return (Collection<E>)collection;
        }
        
        @Override
        public void a(final com.google.ads.interactivemedia.v3.a.d.c c, final Collection<E> collection) throws IOException {
            if (collection == null) {
                c.f();
                return;
            }
            c.b();
            final Iterator<E> iterator = collection.iterator();
            while (iterator.hasNext()) {
                this.a.a(c, iterator.next());
            }
            c.c();
        }
    }
}
