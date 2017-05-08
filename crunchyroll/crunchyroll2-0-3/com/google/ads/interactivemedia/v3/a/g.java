// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a;

import com.google.ads.interactivemedia.v3.a.b.a.l;
import java.util.Collections;
import java.util.Collection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.reflect.Type;
import java.util.Map;
import com.google.ads.interactivemedia.v3.a.b.d;

public final class g
{
    private d a;
    private u b;
    private e c;
    private final Map<Type, h<?>> d;
    private final List<x> e;
    private final List<x> f;
    private boolean g;
    private String h;
    private int i;
    private int j;
    private boolean k;
    private boolean l;
    private boolean m;
    private boolean n;
    private boolean o;
    
    public g() {
        this.a = com.google.ads.interactivemedia.v3.a.b.d.a;
        this.b = u.a;
        this.c = com.google.ads.interactivemedia.v3.a.d.a;
        this.d = new HashMap<Type, h<?>>();
        this.e = new ArrayList<x>();
        this.f = new ArrayList<x>();
        this.i = 2;
        this.j = 2;
        this.m = true;
    }
    
    private void a(final String s, final int n, final int n2, final List<x> list) {
        a a;
        if (s != null && !"".equals(s.trim())) {
            a = new a(s);
        }
        else {
            if (n == 2 || n2 == 2) {
                return;
            }
            a = new a(n, n2);
        }
        list.add(v.a(com.google.ads.interactivemedia.v3.a.c.a.b((Class<?>)Date.class), a));
        list.add(v.a(com.google.ads.interactivemedia.v3.a.c.a.b((Class<?>)Timestamp.class), a));
        list.add(v.a(com.google.ads.interactivemedia.v3.a.c.a.b((Class<?>)java.sql.Date.class), a));
    }
    
    public f a() {
        final ArrayList<Object> list = new ArrayList<Object>();
        list.addAll(this.e);
        Collections.reverse(list);
        list.addAll(this.f);
        this.a(this.h, this.i, this.j, (List<x>)list);
        return new f(this.a, this.c, this.d, this.g, this.k, this.o, this.m, this.n, this.l, this.b, (List<x>)list);
    }
    
    public g a(final Type type, final Object o) {
        com.google.ads.interactivemedia.v3.a.b.a.a(o instanceof s || o instanceof k || o instanceof h || o instanceof w);
        if (o instanceof h) {
            this.d.put(type, (h<?>)o);
        }
        if (o instanceof s || o instanceof k) {
            this.e.add(v.b(com.google.ads.interactivemedia.v3.a.c.a.a(type), o));
        }
        if (o instanceof w) {
            this.e.add(com.google.ads.interactivemedia.v3.a.b.a.l.a(com.google.ads.interactivemedia.v3.a.c.a.a(type), (w<?>)o));
        }
        return this;
    }
}
