// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class HierarchicType
{
    protected final Type _actualType;
    protected final ParameterizedType _genericType;
    protected final Class<?> _rawClass;
    protected HierarchicType _subType;
    protected HierarchicType _superType;
    
    public HierarchicType(final Type actualType) {
        this._actualType = actualType;
        if (actualType instanceof Class) {
            this._rawClass = (Class<?>)actualType;
            this._genericType = null;
            return;
        }
        if (actualType instanceof ParameterizedType) {
            this._genericType = (ParameterizedType)actualType;
            this._rawClass = (Class<?>)this._genericType.getRawType();
            return;
        }
        throw new IllegalArgumentException("Type " + actualType.getClass().getName() + " can not be used to construct HierarchicType");
    }
    
    private HierarchicType(final Type actualType, final Class<?> rawClass, final ParameterizedType genericType, final HierarchicType superType, final HierarchicType subType) {
        this._actualType = actualType;
        this._rawClass = rawClass;
        this._genericType = genericType;
        this._superType = superType;
        this._subType = subType;
    }
    
    public final ParameterizedType asGeneric() {
        return this._genericType;
    }
    
    public HierarchicType deepCloneWithoutSubtype() {
        HierarchicType deepCloneWithoutSubtype;
        if (this._superType == null) {
            deepCloneWithoutSubtype = null;
        }
        else {
            deepCloneWithoutSubtype = this._superType.deepCloneWithoutSubtype();
        }
        final HierarchicType subType = new HierarchicType(this._actualType, this._rawClass, this._genericType, deepCloneWithoutSubtype, null);
        if (deepCloneWithoutSubtype != null) {
            deepCloneWithoutSubtype.setSubType(subType);
        }
        return subType;
    }
    
    public final Class<?> getRawClass() {
        return this._rawClass;
    }
    
    public final HierarchicType getSuperType() {
        return this._superType;
    }
    
    public final boolean isGeneric() {
        return this._genericType != null;
    }
    
    public void setSubType(final HierarchicType subType) {
        this._subType = subType;
    }
    
    public void setSuperType(final HierarchicType superType) {
        this._superType = superType;
    }
    
    @Override
    public String toString() {
        if (this._genericType != null) {
            return this._genericType.toString();
        }
        return this._rawClass.getName();
    }
}
