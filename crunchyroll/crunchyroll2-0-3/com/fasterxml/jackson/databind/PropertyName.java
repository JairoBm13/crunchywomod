// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import java.io.Serializable;

public class PropertyName implements Serializable
{
    public static final PropertyName NO_NAME;
    public static final PropertyName USE_DEFAULT;
    protected final String _namespace;
    protected final String _simpleName;
    
    static {
        USE_DEFAULT = new PropertyName("", null);
        NO_NAME = new PropertyName(new String("#disabled"), null);
    }
    
    public PropertyName(final String s) {
        this(s, null);
    }
    
    public PropertyName(final String s, final String namespace) {
        String simpleName = s;
        if (s == null) {
            simpleName = "";
        }
        this._simpleName = simpleName;
        this._namespace = namespace;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = true;
        final boolean b2 = false;
        boolean b3;
        if (o == this) {
            b3 = true;
        }
        else {
            b3 = b2;
            if (o != null) {
                b3 = b2;
                if (o.getClass() == this.getClass()) {
                    final PropertyName propertyName = (PropertyName)o;
                    if (this._simpleName == null) {
                        b3 = b2;
                        if (propertyName._simpleName != null) {
                            return b3;
                        }
                    }
                    else if (!this._simpleName.equals(propertyName._simpleName)) {
                        return false;
                    }
                    if (this._namespace == null) {
                        return propertyName._namespace == null && b;
                    }
                    return this._namespace.equals(propertyName._namespace);
                }
            }
        }
        return b3;
    }
    
    public String getSimpleName() {
        return this._simpleName;
    }
    
    public boolean hasSimpleName() {
        return this._simpleName.length() > 0;
    }
    
    @Override
    public int hashCode() {
        if (this._namespace == null) {
            return this._simpleName.hashCode();
        }
        return this._namespace.hashCode() ^ this._simpleName.hashCode();
    }
    
    @Override
    public String toString() {
        if (this._namespace == null) {
            return this._simpleName;
        }
        return "{" + this._namespace + "}" + this._simpleName;
    }
}
