// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b.a;

import java.io.IOException;
import com.google.ads.interactivemedia.v3.a.c.a;
import com.google.ads.interactivemedia.v3.a.d.c;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Type;
import com.google.ads.interactivemedia.v3.a.f;
import com.google.ads.interactivemedia.v3.a.w;

final class k<T> extends w<T>
{
    private final f a;
    private final w<T> b;
    private final Type c;
    
    k(final f a, final w<T> b, final Type c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    private Type a(final Type type, final Object o) {
        Type class1 = type;
        if (o != null) {
            if (type != Object.class && !(type instanceof TypeVariable)) {
                class1 = type;
                if (!(type instanceof Class)) {
                    return class1;
                }
            }
            class1 = o.getClass();
        }
        return class1;
    }
    
    @Override
    public void a(final c c, final T t) throws IOException {
        w<?> w = this.b;
        final Type a = this.a(this.c, t);
        if (a != this.c) {
            w = this.a.a(com.google.ads.interactivemedia.v3.a.c.a.a(a));
            if (w instanceof h.a && !(this.b instanceof h.a)) {
                w = this.b;
            }
        }
        w.a(c, t);
    }
    
    @Override
    public T b(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
        return this.b.b(a);
    }
}
