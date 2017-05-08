// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject;

import java.lang.reflect.WildcardType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.TypeVariable;
import com.google.inject.util.Types;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Member;
import com.google.inject.internal.util.$ImmutableList;
import java.util.List;
import java.lang.reflect.ParameterizedType;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.internal.MoreTypes;
import java.lang.reflect.Type;

public class TypeLiteral<T>
{
    final int hashCode;
    final Class<? super T> rawType;
    final Type type;
    
    protected TypeLiteral() {
        this.type = getSuperclassTypeParameter(this.getClass());
        this.rawType = (Class<? super T>)MoreTypes.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }
    
    TypeLiteral(final Type type) {
        this.type = MoreTypes.canonicalize($Preconditions.checkNotNull(type, "type"));
        this.rawType = (Class<? super T>)MoreTypes.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }
    
    static TypeLiteral<?> fromSuperclassTypeParameter(final Class<?> clazz) {
        return new TypeLiteral<Object>(getSuperclassTypeParameter(clazz));
    }
    
    public static <T> TypeLiteral<T> get(final Class<T> clazz) {
        return new TypeLiteral<T>(clazz);
    }
    
    public static TypeLiteral<?> get(final Type type) {
        return new TypeLiteral<Object>(type);
    }
    
    static Type getSuperclassTypeParameter(final Class<?> clazz) {
        final Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        return MoreTypes.canonicalize(((ParameterizedType)genericSuperclass).getActualTypeArguments()[0]);
    }
    
    private List<TypeLiteral<?>> resolveAll(final Type[] array) {
        final TypeLiteral[] array2 = new TypeLiteral[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = this.resolve(array[i]);
        }
        return (List<TypeLiteral<?>>)$ImmutableList.of((TypeLiteral[])array2);
    }
    
    @Override
    public final boolean equals(final Object o) {
        return o instanceof TypeLiteral && MoreTypes.equals(this.type, ((TypeLiteral)o).type);
    }
    
    public List<TypeLiteral<?>> getExceptionTypes(final Member member) {
        Type[] array;
        if (member instanceof Method) {
            final Method method = (Method)member;
            $Preconditions.checkArgument(method.getDeclaringClass().isAssignableFrom(this.rawType), "%s is not defined by a supertype of %s", method, this.type);
            array = method.getGenericExceptionTypes();
        }
        else {
            if (!(member instanceof Constructor)) {
                throw new IllegalArgumentException("Not a method or a constructor: " + member);
            }
            final Constructor constructor = (Constructor)member;
            $Preconditions.checkArgument(constructor.getDeclaringClass().isAssignableFrom(this.rawType), "%s does not construct a supertype of %s", constructor, this.type);
            array = constructor.getGenericExceptionTypes();
        }
        return this.resolveAll(array);
    }
    
    public TypeLiteral<?> getFieldType(final Field field) {
        $Preconditions.checkArgument(field.getDeclaringClass().isAssignableFrom(this.rawType), "%s is not defined by a supertype of %s", field, this.type);
        return this.resolve(field.getGenericType());
    }
    
    public List<TypeLiteral<?>> getParameterTypes(final Member member) {
        Type[] array;
        if (member instanceof Method) {
            final Method method = (Method)member;
            $Preconditions.checkArgument(method.getDeclaringClass().isAssignableFrom(this.rawType), "%s is not defined by a supertype of %s", method, this.type);
            array = method.getGenericParameterTypes();
        }
        else {
            if (!(member instanceof Constructor)) {
                throw new IllegalArgumentException("Not a method or a constructor: " + member);
            }
            final Constructor constructor = (Constructor)member;
            $Preconditions.checkArgument(constructor.getDeclaringClass().isAssignableFrom(this.rawType), "%s does not construct a supertype of %s", constructor, this.type);
            array = constructor.getGenericParameterTypes();
        }
        return this.resolveAll(array);
    }
    
    public final Class<? super T> getRawType() {
        return this.rawType;
    }
    
    public TypeLiteral<?> getReturnType(final Method method) {
        $Preconditions.checkArgument(method.getDeclaringClass().isAssignableFrom(this.rawType), "%s is not defined by a supertype of %s", method, this.type);
        return this.resolve(method.getGenericReturnType());
    }
    
    public TypeLiteral<?> getSupertype(final Class<?> clazz) {
        $Preconditions.checkArgument(clazz.isAssignableFrom(this.rawType), "%s is not a supertype of %s", clazz, this.type);
        return this.resolve(MoreTypes.getGenericSupertype(this.type, this.rawType, clazz));
    }
    
    public final Type getType() {
        return this.type;
    }
    
    @Override
    public final int hashCode() {
        return this.hashCode;
    }
    
    final TypeLiteral<Provider<T>> providerType() {
        return (TypeLiteral<Provider<T>>)get(Types.providerOf(this.getType()));
    }
    
    TypeLiteral<?> resolve(final Type type) {
        return get(this.resolveType(type));
    }
    
    Type resolveType(Type ownerType) {
        while (ownerType instanceof TypeVariable) {
            final TypeVariable typeVariable = (TypeVariable)ownerType;
            final Type resolveTypeVariable = MoreTypes.resolveTypeVariable(this.type, this.rawType, typeVariable);
            if ((ownerType = resolveTypeVariable) == typeVariable) {
                ownerType = resolveTypeVariable;
                return ownerType;
            }
        }
        if (ownerType instanceof GenericArrayType) {
            ownerType = ownerType;
            final Type genericComponentType = ((GenericArrayType)ownerType).getGenericComponentType();
            final Type resolveType = this.resolveType(genericComponentType);
            if (genericComponentType != resolveType) {
                return Types.arrayOf(resolveType);
            }
            return ownerType;
        }
        else if (ownerType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)ownerType;
            ownerType = parameterizedType.getOwnerType();
            final Type resolveType2 = this.resolveType(ownerType);
            int n;
            if (resolveType2 != ownerType) {
                n = 1;
            }
            else {
                n = 0;
            }
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Type[] array;
            int n2;
            for (int i = 0; i < actualTypeArguments.length; ++i, actualTypeArguments = array, n = n2) {
                final Type resolveType3 = this.resolveType(actualTypeArguments[i]);
                array = actualTypeArguments;
                n2 = n;
                if (resolveType3 != actualTypeArguments[i]) {
                    array = actualTypeArguments;
                    if ((n2 = n) == 0) {
                        array = actualTypeArguments.clone();
                        n2 = 1;
                    }
                    array[i] = resolveType3;
                }
            }
            ownerType = parameterizedType;
            if (n != 0) {
                return Types.newParameterizedTypeWithOwner(resolveType2, parameterizedType.getRawType(), actualTypeArguments);
            }
            return ownerType;
        }
        else {
            if (!(ownerType instanceof WildcardType)) {
                return ownerType;
            }
            final WildcardType wildcardType = (WildcardType)ownerType;
            final Type[] lowerBounds = wildcardType.getLowerBounds();
            final Type[] upperBounds = wildcardType.getUpperBounds();
            if (lowerBounds.length == 1) {
                final Type resolveType4 = this.resolveType(lowerBounds[0]);
                ownerType = wildcardType;
                if (resolveType4 != lowerBounds[0]) {
                    return Types.supertypeOf(resolveType4);
                }
                return ownerType;
            }
            else {
                ownerType = wildcardType;
                if (upperBounds.length != 1) {
                    return ownerType;
                }
                final Type resolveType5 = this.resolveType(upperBounds[0]);
                ownerType = wildcardType;
                if (resolveType5 != upperBounds[0]) {
                    return Types.subtypeOf(resolveType5);
                }
                return ownerType;
            }
        }
    }
    
    @Override
    public final String toString() {
        return MoreTypes.typeToString(this.type);
    }
}
