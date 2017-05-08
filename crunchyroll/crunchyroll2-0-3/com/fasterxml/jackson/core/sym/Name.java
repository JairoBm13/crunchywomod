// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.sym;

public abstract class Name
{
    protected final int _hashCode;
    protected final String _name;
    
    protected Name(final String name, final int hashCode) {
        this._name = name;
        this._hashCode = hashCode;
    }
    
    public abstract boolean equals(final int p0);
    
    public abstract boolean equals(final int p0, final int p1);
    
    @Override
    public boolean equals(final Object o) {
        return o == this;
    }
    
    public abstract boolean equals(final int[] p0, final int p1);
    
    public String getName() {
        return this._name;
    }
    
    @Override
    public final int hashCode() {
        return this._hashCode;
    }
    
    @Override
    public String toString() {
        return this._name;
    }
}
