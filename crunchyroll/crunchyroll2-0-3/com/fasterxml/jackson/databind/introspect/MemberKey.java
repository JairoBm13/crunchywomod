// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

public final class MemberKey
{
    static final Class<?>[] NO_CLASSES;
    final Class<?>[] _argTypes;
    final String _name;
    
    static {
        NO_CLASSES = new Class[0];
    }
    
    public MemberKey(final String name, final Class<?>[] array) {
        this._name = name;
        Class<?>[] no_CLASSES = array;
        if (array == null) {
            no_CLASSES = MemberKey.NO_CLASSES;
        }
        this._argTypes = no_CLASSES;
    }
    
    public MemberKey(final Constructor<?> constructor) {
        this("", constructor.getParameterTypes());
    }
    
    public MemberKey(final Method method) {
        this(method.getName(), method.getParameterTypes());
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
            final MemberKey memberKey = (MemberKey)o;
            if (!this._name.equals(memberKey._name)) {
                return false;
            }
            final Class<?>[] argTypes = memberKey._argTypes;
            final int length = this._argTypes.length;
            if (argTypes.length != length) {
                return false;
            }
            for (int i = 0; i < length; ++i) {
                final Class<?> clazz = argTypes[i];
                final Class<?> clazz2 = this._argTypes[i];
                if (clazz != clazz2 && !clazz.isAssignableFrom(clazz2) && !clazz2.isAssignableFrom(clazz)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return this._name.hashCode() + this._argTypes.length;
    }
    
    @Override
    public String toString() {
        return this._name + "(" + this._argTypes.length + "-args)";
    }
}
