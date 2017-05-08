// 
// Decompiled by Procyon v0.5.30
// 

package com.nineoldandroids.util;

public abstract class Property<T, V>
{
    private final String mName;
    private final Class<V> mType;
    
    public Property(final Class<V> mType, final String mName) {
        this.mName = mName;
        this.mType = mType;
    }
    
    public abstract V get(final T p0);
    
    public String getName() {
        return this.mName;
    }
    
    public void set(final T t, final V v) {
        throw new UnsupportedOperationException("Property " + this.getName() + " is read-only");
    }
}
