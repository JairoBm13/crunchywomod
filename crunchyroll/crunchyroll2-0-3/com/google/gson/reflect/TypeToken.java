// 
// Decompiled by Procyon v0.5.30
// 

package com.google.gson.reflect;

import java.lang.reflect.ParameterizedType;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.Type;

public class TypeToken<T>
{
    final int hashCode;
    final Class<? super T> rawType;
    final Type type;
    
    protected TypeToken() {
        this.type = getSuperclassTypeParameter(this.getClass());
        this.rawType = (Class<? super T>)$Gson$Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }
    
    TypeToken(final Type type) {
        this.type = $Gson$Types.canonicalize($Gson$Preconditions.checkNotNull(type));
        this.rawType = (Class<? super T>)$Gson$Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }
    
    public static <T> TypeToken<T> get(final Class<T> clazz) {
        return new TypeToken<T>(clazz);
    }
    
    public static TypeToken<?> get(final Type type) {
        return new TypeToken<Object>(type);
    }
    
    static Type getSuperclassTypeParameter(final Class<?> clazz) {
        final Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        return $Gson$Types.canonicalize(((ParameterizedType)genericSuperclass).getActualTypeArguments()[0]);
    }
    
    @Override
    public final boolean equals(final Object o) {
        return o instanceof TypeToken && $Gson$Types.equals(this.type, ((TypeToken)o).type);
    }
    
    public final Class<? super T> getRawType() {
        return this.rawType;
    }
    
    public final Type getType() {
        return this.type;
    }
    
    @Override
    public final int hashCode() {
        return this.hashCode;
    }
    
    @Override
    public final String toString() {
        return $Gson$Types.typeToString(this.type);
    }
}
