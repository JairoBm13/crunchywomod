// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.util;

import java.lang.reflect.WildcardType;
import com.google.inject.Provider;
import java.lang.reflect.ParameterizedType;
import com.google.inject.internal.MoreTypes;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

public final class Types
{
    public static GenericArrayType arrayOf(final Type type) {
        return new MoreTypes.GenericArrayTypeImpl(type);
    }
    
    public static ParameterizedType newParameterizedType(final Type type, final Type... array) {
        return newParameterizedTypeWithOwner(null, type, array);
    }
    
    public static ParameterizedType newParameterizedTypeWithOwner(final Type type, final Type type2, final Type... array) {
        return new MoreTypes.ParameterizedTypeImpl(type, type2, array);
    }
    
    public static ParameterizedType providerOf(final Type type) {
        return newParameterizedType(Provider.class, type);
    }
    
    public static WildcardType subtypeOf(final Type type) {
        return new MoreTypes.WildcardTypeImpl(new Type[] { type }, MoreTypes.EMPTY_TYPE_ARRAY);
    }
    
    public static WildcardType supertypeOf(final Type type) {
        return new MoreTypes.WildcardTypeImpl(new Type[] { Object.class }, new Type[] { type });
    }
}
