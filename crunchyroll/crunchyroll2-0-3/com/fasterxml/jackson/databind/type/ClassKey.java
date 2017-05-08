// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.type;

import java.io.Serializable;

public final class ClassKey implements Serializable, Comparable<ClassKey>
{
    private Class<?> _class;
    private String _className;
    private int _hashCode;
    
    public ClassKey() {
        this._class = null;
        this._className = null;
        this._hashCode = 0;
    }
    
    public ClassKey(final Class<?> class1) {
        this._class = class1;
        this._className = class1.getName();
        this._hashCode = this._className.hashCode();
    }
    
    @Override
    public int compareTo(final ClassKey classKey) {
        return this._className.compareTo(classKey._className);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (o == null) {
                return false;
            }
            if (o.getClass() != this.getClass()) {
                return false;
            }
            if (((ClassKey)o)._class != this._class) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return this._hashCode;
    }
    
    public void reset(final Class<?> class1) {
        this._class = class1;
        this._className = class1.getName();
        this._hashCode = this._className.hashCode();
    }
    
    @Override
    public String toString() {
        return this._className;
    }
}
