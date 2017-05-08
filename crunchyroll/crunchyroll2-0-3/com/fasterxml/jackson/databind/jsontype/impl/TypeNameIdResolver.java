// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype.impl;

import java.util.Iterator;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import java.util.Collection;
import com.fasterxml.jackson.databind.JavaType;
import java.util.HashMap;
import com.fasterxml.jackson.databind.cfg.MapperConfig;

public class TypeNameIdResolver extends TypeIdResolverBase
{
    protected final MapperConfig<?> _config;
    protected final HashMap<String, JavaType> _idToType;
    protected final HashMap<String, String> _typeToId;
    
    protected TypeNameIdResolver(final MapperConfig<?> config, final JavaType javaType, final HashMap<String, String> typeToId, final HashMap<String, JavaType> idToType) {
        super(javaType, config.getTypeFactory());
        this._config = config;
        this._typeToId = typeToId;
        this._idToType = idToType;
    }
    
    protected static String _defaultTypeId(final Class<?> clazz) {
        final String name = clazz.getName();
        final int lastIndex = name.lastIndexOf(46);
        if (lastIndex < 0) {
            return name;
        }
        return name.substring(lastIndex + 1);
    }
    
    public static TypeNameIdResolver construct(final MapperConfig<?> mapperConfig, final JavaType javaType, final Collection<NamedType> collection, final boolean b, final boolean b2) {
        if (b == b2) {
            throw new IllegalArgumentException();
        }
        HashMap<String, String> hashMap;
        if (b) {
            hashMap = new HashMap<String, String>();
        }
        else {
            hashMap = null;
        }
        HashMap<String, JavaType> hashMap2;
        if (b2) {
            hashMap2 = new HashMap<String, JavaType>();
        }
        else {
            hashMap2 = null;
        }
        if (collection != null) {
            for (final NamedType namedType : collection) {
                final Class<?> type = namedType.getType();
                String s;
                if (namedType.hasName()) {
                    s = namedType.getName();
                }
                else {
                    s = _defaultTypeId(type);
                }
                if (b) {
                    hashMap.put(type.getName(), s);
                }
                if (b2) {
                    final JavaType javaType2 = hashMap2.get(s);
                    if (javaType2 != null && type.isAssignableFrom(javaType2.getRawClass())) {
                        continue;
                    }
                    hashMap2.put(s, mapperConfig.constructType(type));
                }
            }
        }
        return new TypeNameIdResolver(mapperConfig, javaType, hashMap, hashMap2);
    }
    
    @Override
    public String idFromValue(final Object o) {
        final Class<?> class1 = o.getClass();
        final String name = class1.getName();
        synchronized (this._typeToId) {
            String defaultTypeId;
            String typeName = defaultTypeId = this._typeToId.get(name);
            if (typeName == null) {
                if (this._config.isAnnotationProcessingEnabled()) {
                    typeName = this._config.getAnnotationIntrospector().findTypeName(this._config.introspectClassAnnotations(class1).getClassInfo());
                }
                if ((defaultTypeId = typeName) == null) {
                    defaultTypeId = _defaultTypeId(class1);
                }
                this._typeToId.put(name, defaultTypeId);
            }
            return defaultTypeId;
        }
    }
    
    @Override
    public String idFromValueAndType(final Object o, final Class<?> clazz) {
        if (o == null) {
            return null;
        }
        return this.idFromValue(o);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('[').append(this.getClass().getName());
        sb.append("; id-to-type=").append(this._idToType);
        sb.append(']');
        return sb.toString();
    }
    
    @Override
    public JavaType typeFromId(final String s) throws IllegalArgumentException {
        return this._idToType.get(s);
    }
}
