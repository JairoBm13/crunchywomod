// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype;

import java.io.Serializable;

public final class NamedType implements Serializable
{
    protected final Class<?> _class;
    protected final int _hashCode;
    protected String _name;
    
    public NamedType(final Class<?> class1, final String name) {
        this._class = class1;
        this._hashCode = class1.getName().hashCode();
        this.setName(name);
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
            if (this._class != ((NamedType)o)._class) {
                return false;
            }
        }
        return true;
    }
    
    public String getName() {
        return this._name;
    }
    
    public Class<?> getType() {
        return this._class;
    }
    
    public boolean hasName() {
        return this._name != null;
    }
    
    @Override
    public int hashCode() {
        return this._hashCode;
    }
    
    public void setName(final String s) {
        String name = null;
        Label_0015: {
            if (s != null) {
                name = s;
                if (s.length() != 0) {
                    break Label_0015;
                }
            }
            name = null;
        }
        this._name = name;
    }
    
    @Override
    public String toString() {
        final StringBuilder append = new StringBuilder().append("[NamedType, class ").append(this._class.getName()).append(", name: ");
        String string;
        if (this._name == null) {
            string = "null";
        }
        else {
            string = "'" + this._name + "'";
        }
        return append.append(string).append("]").toString();
    }
}
