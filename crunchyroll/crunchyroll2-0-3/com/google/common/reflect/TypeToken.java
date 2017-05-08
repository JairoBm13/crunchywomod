// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.reflect;

import com.google.common.collect.FluentIterable;
import java.util.Set;
import java.util.Collection;
import com.google.common.collect.ForwardingSet;
import com.google.common.base.Predicate;
import java.util.HashMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken$com.google.common.reflect.TypeToken$TypeSet;
import java.lang.reflect.WildcardType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.base.Preconditions;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Type;
import java.io.Serializable;

public abstract class TypeToken<T> extends TypeCapture<T> implements Serializable
{
    private final Type runtimeType;
    private transient TypeResolver typeResolver;
    
    protected TypeToken() {
        this.runtimeType = this.capture();
        Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", this.runtimeType);
    }
    
    protected TypeToken(final Class<?> clazz) {
        final Type capture = super.capture();
        if (capture instanceof Class) {
            this.runtimeType = capture;
            return;
        }
        this.runtimeType = of(clazz).resolveType(capture).runtimeType;
    }
    
    private TypeToken(final Type type) {
        this.runtimeType = Preconditions.checkNotNull(type);
    }
    
    private TypeToken<? super T> boundAsSuperclass(final Type type) {
        final TypeToken<?> of = of(type);
        if (of.getRawType().isInterface()) {
            return null;
        }
        return (TypeToken<? super T>)of;
    }
    
    private ImmutableList<TypeToken<? super T>> boundsAsInterfaces(final Type[] array) {
        final ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
        for (int length = array.length, i = 0; i < length; ++i) {
            final TypeToken<?> of = of(array[i]);
            if (of.getRawType().isInterface()) {
                builder.add((TypeToken<? super T>)of);
            }
        }
        return builder.build();
    }
    
    private TypeToken<? extends T> getArraySubtype(final Class<?> clazz) {
        return (TypeToken<? extends T>)of(newArrayClassOrGenericArrayType(this.getComponentType().getSubtype(clazz.getComponentType()).runtimeType));
    }
    
    private TypeToken<? super T> getArraySupertype(final Class<? super T> clazz) {
        return (TypeToken<? super T>)of(newArrayClassOrGenericArrayType(Preconditions.checkNotNull(this.getComponentType(), "%s isn't a super type of %s", clazz, this).getSupertype(clazz.getComponentType()).runtimeType));
    }
    
    private ImmutableSet<Class<? super T>> getImmediateRawTypes() {
        return (ImmutableSet<Class<? super T>>)getRawTypes(this.runtimeType);
    }
    
    static Class<?> getRawType(final Type type) {
        return getRawTypes(type).iterator().next();
    }
    
    static ImmutableSet<Class<?>> getRawTypes(final Type type) {
        if (type instanceof Class) {
            return ImmutableSet.of(type);
        }
        if (type instanceof ParameterizedType) {
            return ImmutableSet.of(((ParameterizedType)type).getRawType());
        }
        if (type instanceof GenericArrayType) {
            return ImmutableSet.of(Types.getArrayClass(getRawType(((GenericArrayType)type).getGenericComponentType())));
        }
        if (type instanceof TypeVariable) {
            return getRawTypes(((TypeVariable)type).getBounds());
        }
        if (type instanceof WildcardType) {
            return getRawTypes(((WildcardType)type).getUpperBounds());
        }
        throw new AssertionError((Object)(type + " unsupported"));
    }
    
    private static ImmutableSet<Class<?>> getRawTypes(final Type[] array) {
        final ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();
        for (int length = array.length, i = 0; i < length; ++i) {
            builder.addAll(getRawTypes(array[i]));
        }
        return builder.build();
    }
    
    private TypeToken<? extends T> getSubtypeFromLowerBounds(final Class<?> clazz, final Type[] array) {
        if (array.length < 0) {
            return (TypeToken<? extends T>)of(array[0]).getSubtype(clazz);
        }
        throw new IllegalArgumentException(clazz + " isn't a subclass of " + this);
    }
    
    private TypeToken<? super T> getSupertypeFromUpperBounds(final Class<? super T> clazz, final Type[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final TypeToken<?> of = of(array[i]);
            if (of(clazz).isAssignableFrom(of)) {
                return (TypeToken<? super T>)of.getSupertype(clazz);
            }
        }
        throw new IllegalArgumentException(clazz + " isn't a super type of " + this);
    }
    
    private static boolean isAssignable(final Type type, final Type type2) {
        if (type2.equals(type)) {
            return true;
        }
        if (type2 instanceof WildcardType) {
            return isAssignableToWildcardType(type, (WildcardType)type2);
        }
        if (type instanceof TypeVariable) {
            return isAssignableFromAny(((TypeVariable)type).getBounds(), type2);
        }
        if (type instanceof WildcardType) {
            return isAssignableFromAny(((WildcardType)type).getUpperBounds(), type2);
        }
        if (type instanceof GenericArrayType) {
            return isAssignableFromGenericArrayType((GenericArrayType)type, type2);
        }
        if (type2 instanceof Class) {
            return isAssignableToClass(type, (Class<?>)type2);
        }
        if (type2 instanceof ParameterizedType) {
            return isAssignableToParameterizedType(type, (ParameterizedType)type2);
        }
        return type2 instanceof GenericArrayType && isAssignableToGenericArrayType(type, (GenericArrayType)type2);
    }
    
    private static boolean isAssignableBySubtypeBound(Type subtypeBound, final WildcardType wildcardType) {
        final Type subtypeBound2 = subtypeBound(wildcardType);
        if (subtypeBound2 == null) {
            return true;
        }
        subtypeBound = subtypeBound(subtypeBound);
        return subtypeBound != null && isAssignable(subtypeBound2, subtypeBound);
    }
    
    private static boolean isAssignableFromAny(final Type[] array, final Type type) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (isAssignable(array[i], type)) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean isAssignableFromGenericArrayType(final GenericArrayType genericArrayType, final Type type) {
        boolean b = false;
        if (type instanceof Class) {
            final Class clazz = (Class)type;
            if (clazz.isArray()) {
                return isAssignable(genericArrayType.getGenericComponentType(), clazz.getComponentType());
            }
            if (clazz == Object.class) {
                b = true;
            }
        }
        else if (type instanceof GenericArrayType) {
            return isAssignable(genericArrayType.getGenericComponentType(), ((GenericArrayType)type).getGenericComponentType());
        }
        return b;
    }
    
    private static boolean isAssignableToClass(final Type type, final Class<?> clazz) {
        return clazz.isAssignableFrom(getRawType(type));
    }
    
    private static boolean isAssignableToGenericArrayType(final Type type, final GenericArrayType genericArrayType) {
        if (type instanceof Class) {
            final Class clazz = (Class)type;
            if (clazz.isArray()) {
                return isAssignable(clazz.getComponentType(), genericArrayType.getGenericComponentType());
            }
        }
        else if (type instanceof GenericArrayType) {
            return isAssignable(((GenericArrayType)type).getGenericComponentType(), genericArrayType.getGenericComponentType());
        }
        return false;
    }
    
    private static boolean isAssignableToParameterizedType(final Type type, final ParameterizedType parameterizedType) {
        final Class<?> rawType = getRawType(parameterizedType);
        if (rawType.isAssignableFrom(getRawType(type))) {
            final TypeVariable<Class<?>>[] typeParameters = rawType.getTypeParameters();
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            final TypeToken<?> of = of(type);
            for (int i = 0; i < typeParameters.length; ++i) {
                if (!matchTypeArgument(of.resolveType(typeParameters[i]).runtimeType, actualTypeArguments[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    private static boolean isAssignableToWildcardType(final Type type, final WildcardType wildcardType) {
        return isAssignable(type, supertypeBound(wildcardType)) && isAssignableBySubtypeBound(type, wildcardType);
    }
    
    private static boolean matchTypeArgument(final Type type, final Type type2) {
        return type.equals(type2) || (type2 instanceof WildcardType && isAssignableToWildcardType(type, (WildcardType)type2));
    }
    
    private static Type newArrayClassOrGenericArrayType(final Type type) {
        return Types.JavaVersion.JAVA7.newArrayType(type);
    }
    
    public static <T> TypeToken<T> of(final Class<T> clazz) {
        return new SimpleTypeToken<T>((Type)clazz);
    }
    
    public static TypeToken<?> of(final Type type) {
        return new SimpleTypeToken<Object>(type);
    }
    
    private TypeToken<?> resolveSupertype(final Type type) {
        final TypeToken<?> resolveType = this.resolveType(type);
        resolveType.typeResolver = this.typeResolver;
        return resolveType;
    }
    
    private Type resolveTypeArgsForSubclass(final Class<?> clazz) {
        if (this.runtimeType instanceof Class) {
            return clazz;
        }
        final TypeToken<?> genericType = toGenericType(clazz);
        return new TypeResolver().where(genericType.getSupertype(this.getRawType()).runtimeType, this.runtimeType).resolveType(genericType.runtimeType);
    }
    
    private static Type subtypeBound(final Type type) {
        Type subtypeBound = type;
        if (type instanceof WildcardType) {
            subtypeBound = subtypeBound((WildcardType)type);
        }
        return subtypeBound;
    }
    
    private static Type subtypeBound(final WildcardType wildcardType) {
        final Type[] lowerBounds = wildcardType.getLowerBounds();
        if (lowerBounds.length == 1) {
            return subtypeBound(lowerBounds[0]);
        }
        if (lowerBounds.length == 0) {
            return null;
        }
        throw new AssertionError((Object)("Wildcard should have at most one lower bound: " + wildcardType));
    }
    
    private static Type supertypeBound(final Type type) {
        Type supertypeBound = type;
        if (type instanceof WildcardType) {
            supertypeBound = supertypeBound((WildcardType)type);
        }
        return supertypeBound;
    }
    
    private static Type supertypeBound(final WildcardType wildcardType) {
        final Type[] upperBounds = wildcardType.getUpperBounds();
        if (upperBounds.length == 1) {
            return supertypeBound(upperBounds[0]);
        }
        if (upperBounds.length == 0) {
            return Object.class;
        }
        throw new AssertionError((Object)("There should be at most one upper bound for wildcard type: " + wildcardType));
    }
    
    static <T> TypeToken<? extends T> toGenericType(final Class<T> clazz) {
        if (clazz.isArray()) {
            return (TypeToken<? extends T>)of(Types.newArrayType(toGenericType(clazz.getComponentType()).runtimeType));
        }
        final TypeVariable<Class<? extends T>>[] typeParameters = clazz.getTypeParameters();
        if (typeParameters.length > 0) {
            return (TypeToken<? extends T>)of(Types.newParameterizedType(clazz, (Type[])typeParameters));
        }
        return of((Class<? extends T>)clazz);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof TypeToken && this.runtimeType.equals(((TypeToken)o).runtimeType);
    }
    
    public final TypeToken<?> getComponentType() {
        final Type componentType = Types.getComponentType(this.runtimeType);
        if (componentType == null) {
            return null;
        }
        return of(componentType);
    }
    
    final ImmutableList<TypeToken<? super T>> getGenericInterfaces() {
        if (this.runtimeType instanceof TypeVariable) {
            return this.boundsAsInterfaces(((TypeVariable)this.runtimeType).getBounds());
        }
        if (this.runtimeType instanceof WildcardType) {
            return this.boundsAsInterfaces(((WildcardType)this.runtimeType).getUpperBounds());
        }
        final ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
        final Type[] genericInterfaces = this.getRawType().getGenericInterfaces();
        for (int length = genericInterfaces.length, i = 0; i < length; ++i) {
            builder.add((TypeToken<? super T>)this.resolveSupertype(genericInterfaces[i]));
        }
        return builder.build();
    }
    
    final TypeToken<? super T> getGenericSuperclass() {
        if (this.runtimeType instanceof TypeVariable) {
            return this.boundAsSuperclass(((TypeVariable)this.runtimeType).getBounds()[0]);
        }
        if (this.runtimeType instanceof WildcardType) {
            return this.boundAsSuperclass(((WildcardType)this.runtimeType).getUpperBounds()[0]);
        }
        final Type genericSuperclass = this.getRawType().getGenericSuperclass();
        if (genericSuperclass == null) {
            return null;
        }
        return (TypeToken<? super T>)this.resolveSupertype(genericSuperclass);
    }
    
    public final Class<? super T> getRawType() {
        return (Class<? super T>)getRawType(this.runtimeType);
    }
    
    public final TypeToken<? extends T> getSubtype(final Class<?> clazz) {
        Preconditions.checkArgument(!(this.runtimeType instanceof TypeVariable), "Cannot get subtype of type variable <%s>", this);
        if (this.runtimeType instanceof WildcardType) {
            return this.getSubtypeFromLowerBounds(clazz, ((WildcardType)this.runtimeType).getLowerBounds());
        }
        Preconditions.checkArgument(this.getRawType().isAssignableFrom(clazz), "%s isn't a subclass of %s", clazz, this);
        if (this.isArray()) {
            return this.getArraySubtype(clazz);
        }
        return (TypeToken<? extends T>)of(this.resolveTypeArgsForSubclass(clazz));
    }
    
    public final TypeToken<? super T> getSupertype(final Class<? super T> clazz) {
        Preconditions.checkArgument(clazz.isAssignableFrom(this.getRawType()), "%s is not a super class of %s", clazz, this);
        if (this.runtimeType instanceof TypeVariable) {
            return this.getSupertypeFromUpperBounds(clazz, ((TypeVariable)this.runtimeType).getBounds());
        }
        if (this.runtimeType instanceof WildcardType) {
            return this.getSupertypeFromUpperBounds(clazz, ((WildcardType)this.runtimeType).getUpperBounds());
        }
        if (clazz.isArray()) {
            return this.getArraySupertype(clazz);
        }
        return (TypeToken<? super T>)this.resolveSupertype(toGenericType(clazz).runtimeType);
    }
    
    public final Type getType() {
        return this.runtimeType;
    }
    
    public final TypeToken$TypeSet getTypes() {
        return (TypeToken$TypeSet)new TypeSet();
    }
    
    @Override
    public int hashCode() {
        return this.runtimeType.hashCode();
    }
    
    public final boolean isArray() {
        return this.getComponentType() != null;
    }
    
    public final boolean isAssignableFrom(final TypeToken<?> typeToken) {
        return this.isAssignableFrom(typeToken.runtimeType);
    }
    
    public final boolean isAssignableFrom(final Type type) {
        return isAssignable(Preconditions.checkNotNull(type), this.runtimeType);
    }
    
    final TypeToken<T> rejectTypeVariables() {
        Preconditions.checkArgument(!Types.containsTypeVariable(this.runtimeType), (Object)"%s contains a type variable and is not safe for the operation");
        return this;
    }
    
    public final TypeToken<?> resolveType(final Type type) {
        Preconditions.checkNotNull(type);
        TypeResolver typeResolver;
        if ((typeResolver = this.typeResolver) == null) {
            typeResolver = TypeResolver.accordingTo(this.runtimeType);
            this.typeResolver = typeResolver;
        }
        return of(typeResolver.resolveType(type));
    }
    
    @Override
    public String toString() {
        return Types.toString(this.runtimeType);
    }
    
    public final <X> TypeToken<T> where(final TypeParameter<X> typeParameter, final TypeToken<X> typeToken) {
        return new SimpleTypeToken<T>(new TypeResolver().where(ImmutableMap.of(typeParameter.typeVariable, typeToken.runtimeType)).resolveType(this.runtimeType));
    }
    
    public final <X> TypeToken<T> where(final TypeParameter<X> typeParameter, final Class<X> clazz) {
        return this.where(typeParameter, (TypeToken<X>)of((Class<X>)clazz));
    }
    
    protected Object writeReplace() {
        return of(new TypeResolver().resolveType(this.runtimeType));
    }
    
    private static final class SimpleTypeToken<T> extends TypeToken<T>
    {
        SimpleTypeToken(final Type type) {
            super(type, null);
        }
    }
    
    private abstract static class TypeCollector<K>
    {
        static final TypeCollector<TypeToken<?>> FOR_GENERIC_TYPE;
        static final TypeCollector<Class<?>> FOR_RAW_TYPE;
        
        static {
            FOR_GENERIC_TYPE = new TypeCollector<TypeToken<?>>() {
                @Override
                Iterable<? extends TypeToken<?>> getInterfaces(final TypeToken<?> typeToken) {
                    return typeToken.getGenericInterfaces();
                }
                
                @Override
                Class<?> getRawType(final TypeToken<?> typeToken) {
                    return typeToken.getRawType();
                }
                
                @Override
                TypeToken<?> getSuperclass(final TypeToken<?> typeToken) {
                    return typeToken.getGenericSuperclass();
                }
            };
            FOR_RAW_TYPE = new TypeCollector<Class<?>>() {
                @Override
                Iterable<? extends Class<?>> getInterfaces(final Class<?> clazz) {
                    return Arrays.asList((Class<?>[])clazz.getInterfaces());
                }
                
                @Override
                Class<?> getRawType(final Class<?> clazz) {
                    return clazz;
                }
                
                @Override
                Class<?> getSuperclass(final Class<?> clazz) {
                    return clazz.getSuperclass();
                }
            };
        }
        
        private int collectTypes(final K k, final Map<? super K, Integer> map) {
            final Integer n = map.get(this);
            if (n != null) {
                return n;
            }
            int max;
            if (this.getRawType(k).isInterface()) {
                max = 1;
            }
            else {
                max = 0;
            }
            final Iterator<? extends K> iterator = this.getInterfaces(k).iterator();
            while (iterator.hasNext()) {
                max = Math.max(max, this.collectTypes(iterator.next(), map));
            }
            final Object superclass = this.getSuperclass(k);
            int max2 = max;
            if (superclass != null) {
                max2 = Math.max(max, this.collectTypes((K)superclass, map));
            }
            map.put(k, max2 + 1);
            return max2 + 1;
        }
        
        private static <K, V> ImmutableList<K> sortKeysByValue(final Map<K, V> map, final Comparator<? super V> comparator) {
            return new Ordering<K>() {
                @Override
                public int compare(final K k, final K i) {
                    return comparator.compare(map.get(k), map.get(i));
                }
            }.immutableSortedCopy(map.keySet());
        }
        
        ImmutableList<K> collectTypes(final Iterable<? extends K> iterable) {
            final HashMap<Object, Object> hashMap = Maps.newHashMap();
            final Iterator<? extends K> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                this.collectTypes(iterator.next(), (Map<? super K, Integer>)hashMap);
            }
            return sortKeysByValue((Map<K, Object>)hashMap, Ordering.natural().reverse());
        }
        
        final ImmutableList<K> collectTypes(final K k) {
            return this.collectTypes((Iterable<? extends K>)ImmutableList.of(k));
        }
        
        abstract Iterable<? extends K> getInterfaces(final K p0);
        
        abstract Class<?> getRawType(final K p0);
        
        abstract K getSuperclass(final K p0);
    }
    
    private enum TypeFilter implements Predicate<TypeToken<?>>
    {
        IGNORE_TYPE_VARIABLE_OR_WILDCARD {
            @Override
            public boolean apply(final TypeToken<?> typeToken) {
                return !(((TypeToken<Object>)typeToken).runtimeType instanceof TypeVariable) && !(((TypeToken<Object>)typeToken).runtimeType instanceof WildcardType);
            }
        }, 
        INTERFACE_ONLY {
            @Override
            public boolean apply(final TypeToken<?> typeToken) {
                return typeToken.getRawType().isInterface();
            }
        };
    }
    
    public class TypeSet extends ForwardingSet<TypeToken<? super T>> implements Serializable
    {
        private transient ImmutableSet<TypeToken<? super T>> types;
        
        @Override
        protected Set<TypeToken<? super T>> delegate() {
            ImmutableSet<TypeToken<? super T>> types;
            if ((types = this.types) == null) {
                types = FluentIterable.from((Iterable<TypeToken<? super T>>)TypeCollector.FOR_GENERIC_TYPE.collectTypes(TypeToken.this)).filter(TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toImmutableSet();
                this.types = types;
            }
            return types;
        }
    }
}
