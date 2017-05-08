// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype.impl;

import java.util.Map;
import java.util.EnumMap;
import java.util.Collection;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.util.EnumSet;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

public class ClassNameIdResolver extends TypeIdResolverBase
{
    public ClassNameIdResolver(final JavaType javaType, final TypeFactory typeFactory) {
        super(javaType, typeFactory);
    }
    
    protected final String _idFrom(final Object o, final Class<?> clazz) {
        Class<?> superclass = clazz;
        if (Enum.class.isAssignableFrom(clazz)) {
            superclass = clazz;
            if (!clazz.isEnum()) {
                superclass = clazz.getSuperclass();
            }
        }
        final String name = superclass.getName();
        String canonical;
        if (name.startsWith("java.util")) {
            if (o instanceof EnumSet) {
                canonical = TypeFactory.defaultInstance().constructCollectionType(EnumSet.class, ClassUtil.findEnumType((EnumSet<?>)o)).toCanonical();
            }
            else {
                if (o instanceof EnumMap) {
                    return TypeFactory.defaultInstance().constructMapType(EnumMap.class, ClassUtil.findEnumType((EnumMap<?, ?>)o), Object.class).toCanonical();
                }
                final String substring = name.substring(9);
                if (!substring.startsWith(".Arrays$")) {
                    canonical = name;
                    if (!substring.startsWith(".Collections$")) {
                        return canonical;
                    }
                }
                canonical = name;
                if (name.indexOf("List") >= 0) {
                    return "java.util.ArrayList";
                }
            }
        }
        else {
            canonical = name;
            if (name.indexOf(36) >= 0) {
                canonical = name;
                if (ClassUtil.getOuterClass(superclass) != null) {
                    canonical = name;
                    if (ClassUtil.getOuterClass(this._baseType.getRawClass()) == null) {
                        return this._baseType.getRawClass().getName();
                    }
                }
            }
        }
        return canonical;
    }
    
    @Override
    public String idFromValue(final Object o) {
        return this._idFrom(o, o.getClass());
    }
    
    @Override
    public String idFromValueAndType(final Object o, final Class<?> clazz) {
        return this._idFrom(o, clazz);
    }
    
    @Override
    public JavaType typeFromId(final String s) {
        if (s.indexOf(60) > 0) {
            return this._typeFactory.constructFromCanonical(s);
        }
        try {
            return this._typeFactory.constructSpecializedType(this._baseType, ClassUtil.findClass(s));
        }
        catch (ClassNotFoundException ex2) {
            throw new IllegalArgumentException("Invalid type id '" + s + "' (for id type 'Id.class'): no such class found");
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("Invalid type id '" + s + "' (for id type 'Id.class'): " + ex.getMessage(), ex);
        }
    }
}
