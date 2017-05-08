// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a;

import java.util.Map;
import java.util.Set;
import com.google.ads.interactivemedia.v3.a.b.g;

public final class o extends l
{
    private final g<String, l> a;
    
    public o() {
        this.a = new g<String, l>();
    }
    
    public void a(final String s, final l l) {
        l a = l;
        if (l == null) {
            a = n.a;
        }
        this.a.put(s, a);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof o && ((o)o).a.equals(this.a));
    }
    
    @Override
    public int hashCode() {
        return this.a.hashCode();
    }
    
    public Set<Map.Entry<String, l>> o() {
        return this.a.entrySet();
    }
}
