// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.util;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.io.Serializable;

public class RootNameLookup implements Serializable
{
    protected LRUMap<ClassKey, SerializedString> _rootNames;
    
    public SerializedString findRootName(final JavaType javaType, final MapperConfig<?> mapperConfig) {
        return this.findRootName(javaType.getRawClass(), mapperConfig);
    }
    
    public SerializedString findRootName(final Class<?> clazz, final MapperConfig<?> mapperConfig) {
        synchronized (this) {
            final ClassKey classKey = new ClassKey(clazz);
            if (this._rootNames == null) {
                this._rootNames = new LRUMap<ClassKey, SerializedString>(20, 200);
            }
            else {
                final SerializedString serializedString = this._rootNames.get(classKey);
                if (serializedString != null) {
                    return serializedString;
                }
            }
            final PropertyName rootName = mapperConfig.getAnnotationIntrospector().findRootName(mapperConfig.introspectClassAnnotations(clazz).getClassInfo());
            String s;
            if (rootName == null || !rootName.hasSimpleName()) {
                s = clazz.getSimpleName();
            }
            else {
                s = rootName.getSimpleName();
            }
            final SerializedString serializedString2 = new SerializedString(s);
            this._rootNames.put(classKey, serializedString2);
            return serializedString2;
        }
    }
}
