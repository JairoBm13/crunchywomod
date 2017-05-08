// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

public class u
{
    private final long a;
    private final r.a b;
    
    u(final long a, final r.a b) {
        this.a = a;
        this.b = b;
    }
    
    public long a() {
        return this.a;
    }
    
    public r.a b() {
        return this.b;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final u u = (u)o;
            if (this.a != u.a) {
                return false;
            }
            if (this.b != u.b) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return (int)this.a * 31 + this.b.hashCode();
    }
    
    @Override
    public String toString() {
        return "NativeBridgeConfig [adTimeUpdateMs=" + this.a + ", adUiStyle=" + this.b + "]";
    }
}
