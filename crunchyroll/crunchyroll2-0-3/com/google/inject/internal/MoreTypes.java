// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.lang.reflect.Array;
import java.util.Arrays;
import com.google.inject.internal.util.$Objects;
import java.lang.reflect.TypeVariable;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.util.Types;
import javax.inject.Provider;
import com.google.inject.spi.Message;
import com.google.inject.ConfigurationException;
import java.lang.reflect.WildcardType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import com.google.inject.internal.util.$ImmutableMap;
import com.google.inject.TypeLiteral;
import java.util.Map;
import java.lang.reflect.Type;

public class MoreTypes
{
    public static final Type[] EMPTY_TYPE_ARRAY;
    private static final Map<TypeLiteral<?>, TypeLiteral<?>> PRIMITIVE_TO_WRAPPER;
    
    static {
        EMPTY_TYPE_ARRAY = new Type[0];
        PRIMITIVE_TO_WRAPPER = new $ImmutableMap.Builder<TypeLiteral<Boolean>, TypeLiteral<Boolean>>().put(TypeLiteral.get(Boolean.TYPE), TypeLiteral.get(Boolean.class)).put(TypeLiteral.get((Class<Boolean>)Byte.TYPE), TypeLiteral.get((Class<Boolean>)Byte.class)).put(TypeLiteral.get((Class<Boolean>)Short.TYPE), TypeLiteral.get((Class<Boolean>)Short.class)).put(TypeLiteral.get((Class<Boolean>)Integer.TYPE), TypeLiteral.get((Class<Boolean>)Integer.class)).put(TypeLiteral.get((Class<Boolean>)Long.TYPE), TypeLiteral.get((Class<Boolean>)Long.class)).put(TypeLiteral.get((Class<Boolean>)Float.TYPE), TypeLiteral.get((Class<Boolean>)Float.class)).put(TypeLiteral.get((Class<Boolean>)Double.TYPE), TypeLiteral.get((Class<Boolean>)Double.class)).put(TypeLiteral.get((Class<Boolean>)Character.TYPE), TypeLiteral.get((Class<Boolean>)Character.class)).put(TypeLiteral.get((Class<Boolean>)Void.TYPE), TypeLiteral.get((Class<Boolean>)Void.class)).build();
    }
    
    public static Type canonicalize(final Type type) {
        Type type2;
        if (type instanceof Class) {
            GenericArrayTypeImpl genericArrayTypeImpl;
            final Class clazz = (Class)(genericArrayTypeImpl = (GenericArrayTypeImpl)type);
            if (clazz.isArray()) {
                genericArrayTypeImpl = new GenericArrayTypeImpl(canonicalize(clazz.getComponentType()));
            }
            type2 = genericArrayTypeImpl;
        }
        else {
            type2 = type;
            if (!(type instanceof CompositeType)) {
                if (type instanceof ParameterizedType) {
                    final ParameterizedType parameterizedType = (ParameterizedType)type;
                    return new ParameterizedTypeImpl(parameterizedType.getOwnerType(), parameterizedType.getRawType(), parameterizedType.getActualTypeArguments());
                }
                if (type instanceof GenericArrayType) {
                    return new GenericArrayTypeImpl(((GenericArrayType)type).getGenericComponentType());
                }
                type2 = type;
                if (type instanceof WildcardType) {
                    final WildcardType wildcardType = (WildcardType)type;
                    return new WildcardTypeImpl(wildcardType.getUpperBounds(), wildcardType.getLowerBounds());
                }
            }
        }
        return type2;
    }
    
    public static <T> TypeLiteral<T> canonicalizeForKey(TypeLiteral<T> typeLiteral) {
        final Type type = typeLiteral.getType();
        if (!isFullySpecified(type)) {
            throw new ConfigurationException(new Errors().keyNotFullySpecified(typeLiteral).getMessages());
        }
        if (typeLiteral.getRawType() == Provider.class) {
            return (TypeLiteral<T>)TypeLiteral.get(Types.providerOf(((ParameterizedType)type).getActualTypeArguments()[0]));
        }
        final TypeLiteral<?> typeLiteral2 = MoreTypes.PRIMITIVE_TO_WRAPPER.get(typeLiteral);
        if (typeLiteral2 != null) {
            typeLiteral = (TypeLiteral<T>)typeLiteral2;
        }
        return typeLiteral;
    }
    
    private static void checkNotPrimitive(final Type type, final String s) {
        $Preconditions.checkArgument(!(type instanceof Class) || !((Class)type).isPrimitive(), "Primitive types are not allowed in %s: %s", s, type);
    }
    
    private static Class<?> declaringClassOf(final TypeVariable typeVariable) {
        final Class<?> genericDeclaration = typeVariable.getGenericDeclaration();
        if (genericDeclaration instanceof Class) {
            return genericDeclaration;
        }
        return null;
    }
    
    public static boolean equals(final Type type, final Type type2) {
        final boolean b = true;
        final boolean b2 = true;
        final boolean b3 = true;
        final boolean b4 = false;
        boolean b5;
        if (type == type2) {
            b5 = true;
        }
        else {
            if (type instanceof Class) {
                return type.equals(type2);
            }
            if (type instanceof ParameterizedType) {
                b5 = b4;
                if (type2 instanceof ParameterizedType) {
                    final ParameterizedType parameterizedType = (ParameterizedType)type;
                    final ParameterizedType parameterizedType2 = (ParameterizedType)type2;
                    return $Objects.equal(parameterizedType.getOwnerType(), parameterizedType2.getOwnerType()) && parameterizedType.getRawType().equals(parameterizedType2.getRawType()) && Arrays.equals(parameterizedType.getActualTypeArguments(), parameterizedType2.getActualTypeArguments()) && b3;
                }
            }
            else if (type instanceof GenericArrayType) {
                b5 = b4;
                if (type2 instanceof GenericArrayType) {
                    return equals(((GenericArrayType)type).getGenericComponentType(), ((GenericArrayType)type2).getGenericComponentType());
                }
            }
            else if (type instanceof WildcardType) {
                b5 = b4;
                if (type2 instanceof WildcardType) {
                    final WildcardType wildcardType = (WildcardType)type;
                    final WildcardType wildcardType2 = (WildcardType)type2;
                    return Arrays.equals(wildcardType.getUpperBounds(), wildcardType2.getUpperBounds()) && Arrays.equals(wildcardType.getLowerBounds(), wildcardType2.getLowerBounds()) && b;
                }
            }
            else {
                b5 = b4;
                if (type instanceof TypeVariable) {
                    b5 = b4;
                    if (type2 instanceof TypeVariable) {
                        final TypeVariable typeVariable = (TypeVariable)type;
                        final TypeVariable typeVariable2 = (TypeVariable)type2;
                        return typeVariable.getGenericDeclaration() == typeVariable2.getGenericDeclaration() && typeVariable.getName().equals(typeVariable2.getName()) && b2;
                    }
                }
            }
        }
        return b5;
    }
    
    public static Type getGenericSupertype(final Type type, Class<?> clazz, final Class<?> clazz2) {
        if (clazz2 == clazz) {
            return type;
        }
        if (clazz2.isInterface()) {
            final Class<?>[] interfaces = clazz.getInterfaces();
            for (int i = 0; i < interfaces.length; ++i) {
                if (interfaces[i] == clazz2) {
                    return clazz.getGenericInterfaces()[i];
                }
                if (clazz2.isAssignableFrom(interfaces[i])) {
                    return getGenericSupertype(clazz.getGenericInterfaces()[i], interfaces[i], clazz2);
                }
            }
        }
        if (!clazz.isInterface()) {
            while (clazz != Object.class) {
                final Class<? super Object> superclass = clazz.getSuperclass();
                if (superclass == clazz2) {
                    return clazz.getGenericSuperclass();
                }
                if (clazz2.isAssignableFrom(superclass)) {
                    return getGenericSupertype(clazz.getGenericSuperclass(), superclass, clazz2);
                }
                clazz = (Class<Object>)superclass;
            }
        }
        return clazz2;
    }
    
    public static Class<?> getRawType(final Type type) {
        if (type instanceof Class) {
            return (Class<?>)type;
        }
        if (type instanceof ParameterizedType) {
            final Type rawType = ((ParameterizedType)type).getRawType();
            $Preconditions.checkArgument(rawType instanceof Class, "Expected a Class, but <%s> is of type %s", type, type.getClass().getName());
            return (Class<?>)rawType;
        }
        if (type instanceof GenericArrayType) {
            return Array.newInstance(getRawType(((GenericArrayType)type).getGenericComponentType()), 0).getClass();
        }
        if (type instanceof TypeVariable) {
            return Object.class;
        }
        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + type.getClass().getName());
    }
    
    private static int hashCodeOrZero(final Object o) {
        if (o != null) {
            return o.hashCode();
        }
        return 0;
    }
    
    private static int indexOf(final Object[] array, final Object o) {
        for (int i = 0; i < array.length; ++i) {
            if (o.equals(array[i])) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }
    
    private static boolean isFullySpecified(final Type type) {
        if (type instanceof Class) {
            return true;
        }
        if (type instanceof CompositeType) {
            return ((CompositeType)type).isFullySpecified();
        }
        return !(type instanceof TypeVariable) && ((CompositeType)canonicalize(type)).isFullySpecified();
    }
    
    public static Type resolveTypeVariable(Type genericSupertype, final Class<?> clazz, final TypeVariable typeVariable) {
        final Class<?> declaringClass = declaringClassOf(typeVariable);
        if (declaringClass != null) {
            genericSupertype = getGenericSupertype(genericSupertype, clazz, declaringClass);
            if (genericSupertype instanceof ParameterizedType) {
                return ((ParameterizedType)genericSupertype).getActualTypeArguments()[indexOf(declaringClass.getTypeParameters(), typeVariable)];
            }
        }
        return typeVariable;
    }
    
    public static String typeToString(final Type type) {
        if (type instanceof Class) {
            return ((Class)type).getName();
        }
        return type.toString();
    }
    
    private interface CompositeType
    {
        boolean isFullySpecified();
    }
    
    public static class GenericArrayTypeImpl implements CompositeType, Serializable, GenericArrayType
    {
        private final Type componentType;
        
        public GenericArrayTypeImpl(final Type type) {
            this.componentType = MoreTypes.canonicalize(type);
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof GenericArrayType && MoreTypes.equals(this, (Type)o);
        }
        
        @Override
        public Type getGenericComponentType() {
            return this.componentType;
        }
        
        @Override
        public int hashCode() {
            return this.componentType.hashCode();
        }
        
        @Override
        public boolean isFullySpecified() {
            return isFullySpecified(this.componentType);
        }
        
        @Override
        public String toString() {
            return MoreTypes.typeToString(this.componentType) + "[]";
        }
    }
    
    public static class ParameterizedTypeImpl implements CompositeType, Serializable, ParameterizedType
    {
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;
        
        public ParameterizedTypeImpl(Type canonicalize, final Type type, final Type... array) {
            if (type instanceof Class) {
                final Class clazz = (Class)type;
                $Preconditions.checkArgument(canonicalize != null || clazz.getEnclosingClass() == null, "No owner type for enclosed %s", type);
                $Preconditions.checkArgument(canonicalize == null || clazz.getEnclosingClass() != null, "Owner type for unenclosed %s", type);
            }
            if (canonicalize == null) {
                canonicalize = null;
            }
            else {
                canonicalize = MoreTypes.canonicalize(canonicalize);
            }
            this.ownerType = canonicalize;
            this.rawType = MoreTypes.canonicalize(type);
            this.typeArguments = array.clone();
            for (int i = 0; i < this.typeArguments.length; ++i) {
                $Preconditions.checkNotNull(this.typeArguments[i], "type parameter");
                checkNotPrimitive(this.typeArguments[i], "type parameters");
                this.typeArguments[i] = MoreTypes.canonicalize(this.typeArguments[i]);
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof ParameterizedType && MoreTypes.equals(this, (Type)o);
        }
        
        @Override
        public Type[] getActualTypeArguments() {
            return this.typeArguments.clone();
        }
        
        @Override
        public Type getOwnerType() {
            return this.ownerType;
        }
        
        @Override
        public Type getRawType() {
            return this.rawType;
        }
        
        @Override
        public int hashCode() {
            return Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode() ^ hashCodeOrZero(this.ownerType);
        }
        
        @Override
        public boolean isFullySpecified() {
            if ((this.ownerType == null || isFullySpecified(this.ownerType)) && isFullySpecified(this.rawType)) {
                final Type[] typeArguments = this.typeArguments;
                for (int length = typeArguments.length, i = 0; i < length; ++i) {
                    if (!isFullySpecified(typeArguments[i])) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder((this.typeArguments.length + 1) * 30);
            sb.append(MoreTypes.typeToString(this.rawType));
            if (this.typeArguments.length == 0) {
                return sb.toString();
            }
            sb.append("<").append(MoreTypes.typeToString(this.typeArguments[0]));
            for (int i = 1; i < this.typeArguments.length; ++i) {
                sb.append(", ").append(MoreTypes.typeToString(this.typeArguments[i]));
            }
            return sb.append(">").toString();
        }
    }
    
    public static class WildcardTypeImpl implements CompositeType, Serializable, WildcardType
    {
        private final Type lowerBound;
        private final Type upperBound;
        
        public WildcardTypeImpl(final Type[] array, final Type[] array2) {
            final boolean b = true;
            $Preconditions.checkArgument(array2.length <= 1, (Object)"Must have at most one lower bound.");
            $Preconditions.checkArgument(array.length == 1, (Object)"Must have exactly one upper bound.");
            if (array2.length == 1) {
                $Preconditions.checkNotNull(array2[0], "lowerBound");
                checkNotPrimitive(array2[0], "wildcard bounds");
                $Preconditions.checkArgument(array[0] == Object.class && b, (Object)"bounded both ways");
                this.lowerBound = MoreTypes.canonicalize(array2[0]);
                this.upperBound = Object.class;
                return;
            }
            $Preconditions.checkNotNull(array[0], "upperBound");
            checkNotPrimitive(array[0], "wildcard bounds");
            this.lowerBound = null;
            this.upperBound = MoreTypes.canonicalize(array[0]);
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof WildcardType && MoreTypes.equals(this, (Type)o);
        }
        
        @Override
        public Type[] getLowerBounds() {
            if (this.lowerBound != null) {
                return new Type[] { this.lowerBound };
            }
            return MoreTypes.EMPTY_TYPE_ARRAY;
        }
        
        @Override
        public Type[] getUpperBounds() {
            return new Type[] { this.upperBound };
        }
        
        @Override
        public int hashCode() {
            int n;
            if (this.lowerBound != null) {
                n = this.lowerBound.hashCode() + 31;
            }
            else {
                n = 1;
            }
            return n ^ this.upperBound.hashCode() + 31;
        }
        
        @Override
        public boolean isFullySpecified() {
            return isFullySpecified(this.upperBound) && (this.lowerBound == null || isFullySpecified(this.lowerBound));
        }
        
        @Override
        public String toString() {
            if (this.lowerBound != null) {
                return "? super " + MoreTypes.typeToString(this.lowerBound);
            }
            if (this.upperBound == Object.class) {
                return "?";
            }
            return "? extends " + MoreTypes.typeToString(this.upperBound);
        }
    }
}
