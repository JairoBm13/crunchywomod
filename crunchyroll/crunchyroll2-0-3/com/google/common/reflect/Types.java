// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.reflect;

import java.util.ListIterator;
import com.google.common.collect.AbstractIndexedListIterator;
import com.google.common.collect.UnmodifiableListIterator;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.Lists;
import com.google.common.collect.EmptyImmutableList;
import com.google.common.collect.Collections2;
import com.google.common.collect.RegularImmutableList;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.SingletonImmutableList;
import java.util.RandomAccess;
import java.util.List;
import com.google.common.collect.ImmutableCollection;
import java.util.Iterator;
import java.util.Arrays;
import com.google.common.collect.ImmutableList;
import com.google.common.base.Objects;
import java.io.Serializable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Array;
import com.google.common.collect.Iterables;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Preconditions;
import java.lang.reflect.WildcardType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.lang.reflect.Type;
import com.google.common.base.Function;
import com.google.common.base.Joiner;

final class Types
{
    private static final Joiner COMMA_JOINER;
    private static final Function<Type, String> TYPE_TO_STRING;
    
    static {
        TYPE_TO_STRING = new Function<Type, String>() {
            @Override
            public String apply(final Type type) {
                return Types.toString(type);
            }
        };
        COMMA_JOINER = Joiner.on(", ").useForNull("null");
    }
    
    static boolean containsTypeVariable(final Type type) {
        final boolean b = false;
        boolean b2;
        if (type instanceof TypeVariable) {
            b2 = true;
        }
        else {
            if (type instanceof GenericArrayType) {
                return containsTypeVariable(((GenericArrayType)type).getGenericComponentType());
            }
            if (type instanceof ParameterizedType) {
                return containsTypeVariable(((ParameterizedType)type).getActualTypeArguments());
            }
            b2 = b;
            if (type instanceof WildcardType) {
                final WildcardType wildcardType = (WildcardType)type;
                if (!containsTypeVariable(wildcardType.getUpperBounds())) {
                    b2 = b;
                    if (!containsTypeVariable(wildcardType.getLowerBounds())) {
                        return b2;
                    }
                }
                return true;
            }
        }
        return b2;
    }
    
    private static boolean containsTypeVariable(final Type[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (containsTypeVariable(array[i])) {
                return true;
            }
        }
        return false;
    }
    
    private static void disallowPrimitiveType(final Type[] array, final String s) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final Type type = array[i];
            if (type instanceof Class) {
                final Class clazz = (Class)type;
                Preconditions.checkArgument(!clazz.isPrimitive(), "Primitive type '%s' used as %s", clazz, s);
            }
        }
    }
    
    private static Iterable<Type> filterUpperBounds(final Iterable<Type> iterable) {
        return Iterables.filter(iterable, (Predicate<? super Type>)Predicates.not((Predicate<? super T>)Predicates.equalTo((T)Object.class)));
    }
    
    static Class<?> getArrayClass(final Class<?> clazz) {
        return Array.newInstance(clazz, 0).getClass();
    }
    
    static Type getComponentType(final Type type) {
        Preconditions.checkNotNull(type);
        if (type instanceof Class) {
            return ((Class)type).getComponentType();
        }
        if (type instanceof GenericArrayType) {
            return ((GenericArrayType)type).getGenericComponentType();
        }
        if (type instanceof WildcardType) {
            return subtypeOfComponentType(((WildcardType)type).getUpperBounds());
        }
        if (type instanceof TypeVariable) {
            return subtypeOfComponentType(((TypeVariable)type).getBounds());
        }
        return null;
    }
    
    static Type newArrayType(final Type type) {
        final boolean b = true;
        if (!(type instanceof WildcardType)) {
            return JavaVersion.CURRENT.newArrayType(type);
        }
        final WildcardType wildcardType = (WildcardType)type;
        final Type[] lowerBounds = wildcardType.getLowerBounds();
        Preconditions.checkArgument(lowerBounds.length <= 1, (Object)"Wildcard cannot have more than one lower bounds.");
        if (lowerBounds.length == 1) {
            return supertypeOf(newArrayType(lowerBounds[0]));
        }
        final Type[] upperBounds = wildcardType.getUpperBounds();
        Preconditions.checkArgument(upperBounds.length == 1 && b, (Object)"Wildcard should have only one upper bound.");
        return subtypeOf(newArrayType(upperBounds[0]));
    }
    
    static ParameterizedType newParameterizedType(final Class<?> clazz, final Type... array) {
        return new ParameterizedTypeImpl(ClassOwnership.JVM_BEHAVIOR.getOwnerType(clazz), clazz, array);
    }
    
    static ParameterizedType newParameterizedTypeWithOwner(final Type type, final Class<?> clazz, final Type... array) {
        if (type == null) {
            return newParameterizedType(clazz, array);
        }
        Preconditions.checkNotNull(array);
        Preconditions.checkArgument(clazz.getEnclosingClass() != null, "Owner type for unenclosed %s", clazz);
        return new ParameterizedTypeImpl(type, clazz, array);
    }
    
    static <D extends GenericDeclaration> TypeVariable<D> newTypeVariable(final D n, final String s, final Type... array) {
        Type[] array2 = array;
        if (array.length == 0) {
            array2 = new Type[] { Object.class };
        }
        return new TypeVariableImpl<D>(n, s, array2);
    }
    
    static WildcardType subtypeOf(final Type type) {
        return new WildcardTypeImpl(new Type[0], new Type[] { type });
    }
    
    private static Type subtypeOfComponentType(final Type[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final Type componentType = getComponentType(array[i]);
            if (componentType != null) {
                if (componentType instanceof Class) {
                    final Class clazz = (Class)componentType;
                    if (clazz.isPrimitive()) {
                        return clazz;
                    }
                }
                return subtypeOf(componentType);
            }
        }
        return null;
    }
    
    static WildcardType supertypeOf(final Type type) {
        return new WildcardTypeImpl(new Type[] { type }, new Type[] { Object.class });
    }
    
    private static Type[] toArray(final Collection<Type> collection) {
        return collection.toArray(new Type[collection.size()]);
    }
    
    static String toString(final Type type) {
        if (type instanceof Class) {
            return ((Class)type).getName();
        }
        return type.toString();
    }
    
    private enum ClassOwnership
    {
        static final ClassOwnership JVM_BEHAVIOR;
        
        LOCAL_CLASS_HAS_NO_OWNER {
            @Override
            Class<?> getOwnerType(final Class<?> clazz) {
                if (clazz.isLocalClass()) {
                    return null;
                }
                return (Class<?>)clazz.getEnclosingClass();
            }
        }, 
        OWNED_BY_ENCLOSING_CLASS {
            @Override
            Class<?> getOwnerType(final Class<?> clazz) {
                return (Class<?>)clazz.getEnclosingClass();
            }
        };
        
        static {
            JVM_BEHAVIOR = detectJvmBehavior();
        }
        
        private static ClassOwnership detectJvmBehavior() {
            final ParameterizedType parameterizedType = (ParameterizedType)new LocalClass<String>() {}.getClass().getGenericSuperclass();
            final ClassOwnership[] values = values();
            for (int length = values.length, i = 0; i < length; ++i) {
                final ClassOwnership classOwnership = values[i];
                if (classOwnership.getOwnerType(LocalClass.class) == parameterizedType.getOwnerType()) {
                    return classOwnership;
                }
            }
            throw new AssertionError();
        }
        
        abstract Class<?> getOwnerType(final Class<?> p0);
        
        class LocalClass<T>
        {
        }
    }
    
    private static final class GenericArrayTypeImpl implements Serializable, GenericArrayType
    {
        private final Type componentType;
        
        GenericArrayTypeImpl(final Type type) {
            this.componentType = JavaVersion.CURRENT.usedInGenericType(type);
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof GenericArrayType && Objects.equal(this.getGenericComponentType(), ((GenericArrayType)o).getGenericComponentType());
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
        public String toString() {
            return Types.toString(this.componentType) + "[]";
        }
    }
    
    enum JavaVersion
    {
        static final JavaVersion CURRENT;
        
        JAVA6 {
            @Override
            GenericArrayType newArrayType(final Type type) {
                return new GenericArrayTypeImpl(type);
            }
            
            @Override
            Type usedInGenericType(final Type type) {
                Preconditions.checkNotNull(type);
                Type type2 = type;
                if (type instanceof Class) {
                    final Class clazz = (Class)type;
                    type2 = type;
                    if (clazz.isArray()) {
                        type2 = new GenericArrayTypeImpl(clazz.getComponentType());
                    }
                }
                return type2;
            }
        }, 
        JAVA7 {
            @Override
            Type newArrayType(final Type type) {
                if (type instanceof Class) {
                    return Types.getArrayClass((Class<?>)type);
                }
                return new GenericArrayTypeImpl(type);
            }
            
            @Override
            Type usedInGenericType(final Type type) {
                return Preconditions.checkNotNull(type);
            }
        };
        
        static {
            JavaVersion current;
            if (new TypeCapture<int[]>() {}.capture() instanceof Class) {
                current = JavaVersion.JAVA7;
            }
            else {
                current = JavaVersion.JAVA6;
            }
            CURRENT = current;
        }
        
        abstract Type newArrayType(final Type p0);
        
        final ImmutableList<Type> usedInGenericType(final Type[] array) {
            final ImmutableList.Builder<Type> builder = ImmutableList.builder();
            for (int length = array.length, i = 0; i < length; ++i) {
                builder.add(this.usedInGenericType(array[i]));
            }
            return builder.build();
        }
        
        abstract Type usedInGenericType(final Type p0);
    }
    
    private static final class ParameterizedTypeImpl implements Serializable, ParameterizedType
    {
        private final ImmutableList<Type> argumentsList;
        private final Type ownerType;
        private final Class<?> rawType;
        
        ParameterizedTypeImpl(final Type ownerType, final Class<?> rawType, final Type[] array) {
            Preconditions.checkNotNull(rawType);
            Preconditions.checkArgument(array.length == rawType.getTypeParameters().length);
            disallowPrimitiveType(array, "type parameter");
            this.ownerType = ownerType;
            this.rawType = rawType;
            this.argumentsList = JavaVersion.CURRENT.usedInGenericType(array);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType)o;
                if (this.getRawType().equals(parameterizedType.getRawType()) && Objects.equal(this.getOwnerType(), parameterizedType.getOwnerType()) && Arrays.equals(this.getActualTypeArguments(), parameterizedType.getActualTypeArguments())) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public Type[] getActualTypeArguments() {
            return toArray(this.argumentsList);
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
            int hashCode;
            if (this.ownerType == null) {
                hashCode = 0;
            }
            else {
                hashCode = this.ownerType.hashCode();
            }
            return hashCode ^ this.argumentsList.hashCode() ^ this.rawType.hashCode();
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            if (this.ownerType != null) {
                sb.append(Types.toString(this.ownerType)).append('.');
            }
            sb.append(this.rawType.getName()).append('<').append(Types.COMMA_JOINER.join(Iterables.transform((Iterable<Type>)this.argumentsList, (Function<? super Type, ?>)Types.TYPE_TO_STRING))).append('>');
            return sb.toString();
        }
    }
    
    private static final class TypeVariableImpl<D extends GenericDeclaration> implements TypeVariable<D>
    {
        private final ImmutableList<Type> bounds;
        private final D genericDeclaration;
        private final String name;
        
        TypeVariableImpl(final D n, final String s, final Type[] array) {
            disallowPrimitiveType(array, "bound for type variable");
            this.genericDeclaration = Preconditions.checkNotNull(n);
            this.name = Preconditions.checkNotNull(s);
            this.bounds = ImmutableList.copyOf(array);
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b2;
            final boolean b = b2 = false;
            if (o instanceof TypeVariable) {
                final TypeVariable typeVariable = (TypeVariable)o;
                b2 = b;
                if (this.name.equals(typeVariable.getName())) {
                    b2 = b;
                    if (this.genericDeclaration.equals(typeVariable.getGenericDeclaration())) {
                        b2 = true;
                    }
                }
            }
            return b2;
        }
        
        @Override
        public Type[] getBounds() {
            return toArray(this.bounds);
        }
        
        @Override
        public D getGenericDeclaration() {
            return this.genericDeclaration;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        @Override
        public int hashCode() {
            return this.genericDeclaration.hashCode() ^ this.name.hashCode();
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
    
    static final class WildcardTypeImpl implements Serializable, WildcardType
    {
        private final ImmutableList<Type> lowerBounds;
        private final ImmutableList<Type> upperBounds;
        
        WildcardTypeImpl(final Type[] array, final Type[] array2) {
            disallowPrimitiveType(array, "lower bound for wildcard");
            disallowPrimitiveType(array2, "upper bound for wildcard");
            this.lowerBounds = JavaVersion.CURRENT.usedInGenericType(array);
            this.upperBounds = JavaVersion.CURRENT.usedInGenericType(array2);
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b2;
            final boolean b = b2 = false;
            if (o instanceof WildcardType) {
                final WildcardType wildcardType = (WildcardType)o;
                b2 = b;
                if (this.lowerBounds.equals(Arrays.asList(wildcardType.getLowerBounds()))) {
                    b2 = b;
                    if (this.upperBounds.equals(Arrays.asList(wildcardType.getUpperBounds()))) {
                        b2 = true;
                    }
                }
            }
            return b2;
        }
        
        @Override
        public Type[] getLowerBounds() {
            return toArray(this.lowerBounds);
        }
        
        @Override
        public Type[] getUpperBounds() {
            return toArray(this.upperBounds);
        }
        
        @Override
        public int hashCode() {
            return this.lowerBounds.hashCode() ^ this.upperBounds.hashCode();
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("?");
            final Iterator iterator = this.lowerBounds.iterator();
            while (iterator.hasNext()) {
                sb.append(" super ").append(Types.toString(iterator.next()));
            }
            final Iterator<Type> iterator2 = filterUpperBounds(this.upperBounds).iterator();
            while (iterator2.hasNext()) {
                sb.append(" extends ").append(Types.toString(iterator2.next()));
            }
            return sb.toString();
        }
    }
}
