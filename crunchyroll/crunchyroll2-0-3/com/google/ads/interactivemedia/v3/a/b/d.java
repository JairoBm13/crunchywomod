// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.io.IOException;
import com.google.ads.interactivemedia.v3.a.w;
import com.google.ads.interactivemedia.v3.a.c.a;
import com.google.ads.interactivemedia.v3.a.f;
import com.google.ads.interactivemedia.v3.a.a.c;
import java.util.Collections;
import com.google.ads.interactivemedia.v3.a.b;
import java.util.List;
import com.google.ads.interactivemedia.v3.a.x;

public final class d implements x, Cloneable
{
    public static final d a;
    private double b;
    private int c;
    private boolean d;
    private boolean e;
    private List<b> f;
    private List<b> g;
    
    static {
        a = new d();
    }
    
    public d() {
        this.b = -1.0;
        this.c = 136;
        this.d = true;
        this.f = Collections.emptyList();
        this.g = Collections.emptyList();
    }
    
    private boolean a(final c c) {
        return c == null || c.a() <= this.b;
    }
    
    private boolean a(final c c, final com.google.ads.interactivemedia.v3.a.a.d d) {
        return this.a(c) && this.a(d);
    }
    
    private boolean a(final com.google.ads.interactivemedia.v3.a.a.d d) {
        return d == null || d.a() > this.b;
    }
    
    private boolean a(final Class<?> clazz) {
        return !Enum.class.isAssignableFrom(clazz) && (clazz.isAnonymousClass() || clazz.isLocalClass());
    }
    
    private boolean b(final Class<?> clazz) {
        return clazz.isMemberClass() && !this.c(clazz);
    }
    
    private boolean c(final Class<?> clazz) {
        return (clazz.getModifiers() & 0x8) != 0x0;
    }
    
    protected d a() {
        try {
            return (d)super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
    
    @Override
    public <T> w<T> a(final f f, final a<T> a) {
        final Class<? super T> a2 = a.a();
        final boolean a3 = this.a(a2, true);
        final boolean a4 = this.a(a2, false);
        if (!a3 && !a4) {
            return null;
        }
        return new w<T>() {
            private w<T> f;
            
            private w<T> a() {
                final w<T> f = this.f;
                if (f != null) {
                    return f;
                }
                return this.f = f.a(com.google.ads.interactivemedia.v3.a.b.d.this, a);
            }
            
            @Override
            public void a(final com.google.ads.interactivemedia.v3.a.d.c c, final T t) throws IOException {
                if (a3) {
                    c.f();
                    return;
                }
                this.a().a(c, t);
            }
            
            @Override
            public T b(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a4) {
                    a.n();
                    return null;
                }
                return this.a().b(a);
            }
        };
    }
    
    public boolean a(final Class<?> clazz, final boolean b) {
        if (this.b != -1.0 && !this.a(clazz.getAnnotation(c.class), clazz.getAnnotation(com.google.ads.interactivemedia.v3.a.a.d.class))) {
            return true;
        }
        if (!this.d && this.b(clazz)) {
            return true;
        }
        if (this.a(clazz)) {
            return true;
        }
        List<b> list;
        if (b) {
            list = this.f;
        }
        else {
            list = this.g;
        }
        final Iterator<b> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().a(clazz)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean a(final Field field, final boolean b) {
        if ((this.c & field.getModifiers()) != 0x0) {
            return true;
        }
        if (this.b != -1.0 && !this.a(field.getAnnotation(c.class), field.getAnnotation(com.google.ads.interactivemedia.v3.a.a.d.class))) {
            return true;
        }
        if (field.isSynthetic()) {
            return true;
        }
        Label_0106: {
            if (this.e) {
                final com.google.ads.interactivemedia.v3.a.a.a a = field.getAnnotation(com.google.ads.interactivemedia.v3.a.a.a.class);
                if (a != null) {
                    if (b) {
                        if (a.a()) {
                            break Label_0106;
                        }
                    }
                    else if (a.b()) {
                        break Label_0106;
                    }
                }
                return true;
            }
        }
        if (!this.d && this.b(field.getType())) {
            return true;
        }
        if (this.a(field.getType())) {
            return true;
        }
        List<b> list;
        if (b) {
            list = this.f;
        }
        else {
            list = this.g;
        }
        if (!list.isEmpty()) {
            final com.google.ads.interactivemedia.v3.a.c c = new com.google.ads.interactivemedia.v3.a.c(field);
            final Iterator<b> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().a(c)) {
                    return true;
                }
            }
        }
        return false;
    }
}
