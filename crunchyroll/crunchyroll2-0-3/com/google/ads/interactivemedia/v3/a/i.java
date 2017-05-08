// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public final class i extends l implements Iterable<l>
{
    private final List<l> a;
    
    public i() {
        this.a = new ArrayList<l>();
    }
    
    @Override
    public Number a() {
        if (this.a.size() == 1) {
            return this.a.get(0).a();
        }
        throw new IllegalStateException();
    }
    
    public void a(final l l) {
        l a = l;
        if (l == null) {
            a = n.a;
        }
        this.a.add(a);
    }
    
    @Override
    public String b() {
        if (this.a.size() == 1) {
            return this.a.get(0).b();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public double c() {
        if (this.a.size() == 1) {
            return this.a.get(0).c();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public long d() {
        if (this.a.size() == 1) {
            return this.a.get(0).d();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public int e() {
        if (this.a.size() == 1) {
            return this.a.get(0).e();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof i && ((i)o).a.equals(this.a));
    }
    
    @Override
    public boolean f() {
        if (this.a.size() == 1) {
            return this.a.get(0).f();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public int hashCode() {
        return this.a.hashCode();
    }
    
    @Override
    public Iterator<l> iterator() {
        return this.a.iterator();
    }
}
