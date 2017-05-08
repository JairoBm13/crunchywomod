// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.reflect;

import com.google.common.base.Joiner;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.Iterator;
import java.util.HashMap;
import com.google.common.collect.Maps;
import java.lang.reflect.WildcardType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import com.google.common.base.Preconditions;
import java.util.Map;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import com.google.common.collect.ImmutableMap;

class TypeResolver
{
    private final ImmutableMap<TypeVariable<?>, Type> typeTable;
    
    public TypeResolver() {
        this.typeTable = ImmutableMap.of();
    }
    
    private TypeResolver(final ImmutableMap<TypeVariable<?>, Type> typeTable) {
        this.typeTable = typeTable;
    }
    
    static TypeResolver accordingTo(final Type type) {
        return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(type));
    }
    
    private static <T> T checkNonNullArgument(final T t, final String s, final Object... array) {
        Preconditions.checkArgument(t != null, s, array);
        return t;
    }
    
    private static <T> T expectArgument(final Class<T> clazz, final Object o) {
        try {
            return clazz.cast(o);
        }
        catch (ClassCastException ex) {
            throw new IllegalArgumentException(o + " is not a " + clazz.getSimpleName());
        }
    }
    
    private static void populateTypeMappings(final Map<TypeVariable<?>, Type> map, final Type type, final Type type2) {
        if (!type.equals(type2)) {
            if (type instanceof TypeVariable) {
                map.put((TypeVariable<?>)type, type2);
                return;
            }
            if (type instanceof GenericArrayType) {
                populateTypeMappings(map, ((GenericArrayType)type).getGenericComponentType(), checkNonNullArgument(Types.getComponentType(type2), "%s is not an array type.", type2));
                return;
            }
            if (type instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType)type;
                final ParameterizedType parameterizedType2 = expectArgument(ParameterizedType.class, type2);
                Preconditions.checkArgument(parameterizedType.getRawType().equals(parameterizedType2.getRawType()), "Inconsistent raw type: %s vs. %s", type, type2);
                final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                final Type[] actualTypeArguments2 = parameterizedType2.getActualTypeArguments();
                Preconditions.checkArgument(actualTypeArguments.length == actualTypeArguments2.length);
                for (int i = 0; i < actualTypeArguments.length; ++i) {
                    populateTypeMappings(map, actualTypeArguments[i], actualTypeArguments2[i]);
                }
            }
            else {
                if (!(type instanceof WildcardType)) {
                    throw new IllegalArgumentException("No type mapping from " + type);
                }
                final WildcardType wildcardType = (WildcardType)type;
                final WildcardType wildcardType2 = expectArgument(WildcardType.class, type2);
                final Type[] upperBounds = wildcardType.getUpperBounds();
                final Type[] upperBounds2 = wildcardType2.getUpperBounds();
                final Type[] lowerBounds = wildcardType.getLowerBounds();
                final Type[] lowerBounds2 = wildcardType2.getLowerBounds();
                Preconditions.checkArgument(upperBounds.length == upperBounds2.length && lowerBounds.length == lowerBounds2.length, "Incompatible type: %s vs. %s", type, type2);
                for (int j = 0; j < upperBounds.length; ++j) {
                    populateTypeMappings(map, upperBounds[j], upperBounds2[j]);
                }
                for (int k = 0; k < lowerBounds.length; ++k) {
                    populateTypeMappings(map, lowerBounds[k], lowerBounds2[k]);
                }
            }
        }
    }
    
    private Type resolveGenericArrayType(final GenericArrayType genericArrayType) {
        return Types.newArrayType(this.resolveType(genericArrayType.getGenericComponentType()));
    }
    
    private ParameterizedType resolveParameterizedType(final ParameterizedType parameterizedType) {
        final Type ownerType = parameterizedType.getOwnerType();
        Type resolveType;
        if (ownerType == null) {
            resolveType = null;
        }
        else {
            resolveType = this.resolveType(ownerType);
        }
        final Type resolveType2 = this.resolveType(parameterizedType.getRawType());
        final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        final Type[] array = new Type[actualTypeArguments.length];
        for (int i = 0; i < actualTypeArguments.length; ++i) {
            array[i] = this.resolveType(actualTypeArguments[i]);
        }
        return Types.newParameterizedTypeWithOwner(resolveType, (Class<?>)resolveType2, array);
    }
    
    private Type resolveTypeVariable(final TypeVariable<?> typeVariable) {
        return this.resolveTypeVariable(typeVariable, new TypeResolver(this.typeTable) {
            @Override
            Type resolveTypeVariable(final TypeVariable<?> typeVariable, final TypeResolver typeResolver) {
                if (typeVariable.getGenericDeclaration().equals(typeVariable.getGenericDeclaration())) {
                    return typeVariable;
                }
                return TypeResolver.this.resolveTypeVariable(typeVariable, typeResolver);
            }
        });
    }
    
    private Type[] resolveTypes(final Type[] array) {
        final Type[] array2 = new Type[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = this.resolveType(array[i]);
        }
        return array2;
    }
    
    public final Type resolveType(final Type type) {
        Type resolveTypeVariable;
        if (type instanceof TypeVariable) {
            resolveTypeVariable = this.resolveTypeVariable((TypeVariable<?>)type);
        }
        else {
            if (type instanceof ParameterizedType) {
                return this.resolveParameterizedType((ParameterizedType)type);
            }
            if (type instanceof GenericArrayType) {
                return this.resolveGenericArrayType((GenericArrayType)type);
            }
            resolveTypeVariable = type;
            if (type instanceof WildcardType) {
                final WildcardType wildcardType = (WildcardType)type;
                return new Types.WildcardTypeImpl(this.resolveTypes(wildcardType.getLowerBounds()), this.resolveTypes(wildcardType.getUpperBounds()));
            }
        }
        return resolveTypeVariable;
    }
    
    Type resolveTypeVariable(final TypeVariable<?> typeVariable, final TypeResolver typeResolver) {
        final Type type = this.typeTable.get(typeVariable);
        if (type != null) {
            return typeResolver.resolveType(type);
        }
        final Type[] bounds = typeVariable.getBounds();
        if (bounds.length == 0) {
            return typeVariable;
        }
        return Types.newTypeVariable(typeVariable.getGenericDeclaration(), typeVariable.getName(), typeResolver.resolveTypes(bounds));
    }
    
    public final TypeResolver where(final Type type, final Type type2) {
        final HashMap<TypeVariable<?>, Type> hashMap = Maps.newHashMap();
        populateTypeMappings(hashMap, type, type2);
        return this.where(hashMap);
    }
    
    final TypeResolver where(final Map<? extends TypeVariable<?>, ? extends Type> map) {
        final ImmutableMap.Builder<TypeVariable<?>, Type> builder = ImmutableMap.builder();
        builder.putAll(this.typeTable);
        for (final Map.Entry<? extends TypeVariable<?>, ? extends Type> entry : map.entrySet()) {
            final TypeVariable<?> typeVariable = (TypeVariable<?>)entry.getKey();
            final Type type = (Type)entry.getValue();
            Preconditions.checkArgument(!typeVariable.equals(type), "Type variable %s bound to itself", typeVariable);
            builder.put(typeVariable, type);
        }
        return new TypeResolver(builder.build());
    }
    
    private static final class TypeMappingIntrospector
    {
        private static final WildcardCapturer wildcardCapturer;
        private final Set<Type> introspectedTypes;
        private final Map<TypeVariable<?>, Type> mappings;
        
        static {
            wildcardCapturer = new WildcardCapturer();
        }
        
        private TypeMappingIntrospector() {
            this.mappings = (Map<TypeVariable<?>, Type>)Maps.newHashMap();
            this.introspectedTypes = (Set<Type>)Sets.newHashSet();
        }
        
        static ImmutableMap<TypeVariable<?>, Type> getTypeMappings(final Type type) {
            final TypeMappingIntrospector typeMappingIntrospector = new TypeMappingIntrospector();
            typeMappingIntrospector.introspect(TypeMappingIntrospector.wildcardCapturer.capture(type));
            return (ImmutableMap<TypeVariable<?>, Type>)ImmutableMap.copyOf((Map<?, ?>)typeMappingIntrospector.mappings);
        }
        
        private void introspect(final Type type) {
            if (this.introspectedTypes.add(type)) {
                if (type instanceof ParameterizedType) {
                    this.introspectParameterizedType((ParameterizedType)type);
                    return;
                }
                if (type instanceof Class) {
                    this.introspectClass((Class<?>)type);
                    return;
                }
                if (type instanceof TypeVariable) {
                    final Type[] bounds = ((TypeVariable)type).getBounds();
                    for (int length = bounds.length, i = 0; i < length; ++i) {
                        this.introspect(bounds[i]);
                    }
                }
                else if (type instanceof WildcardType) {
                    final Type[] upperBounds = ((WildcardType)type).getUpperBounds();
                    for (int length2 = upperBounds.length, j = 0; j < length2; ++j) {
                        this.introspect(upperBounds[j]);
                    }
                }
            }
        }
        
        private void introspectClass(final Class<?> clazz) {
            this.introspect(clazz.getGenericSuperclass());
            final Type[] genericInterfaces = clazz.getGenericInterfaces();
            for (int length = genericInterfaces.length, i = 0; i < length; ++i) {
                this.introspect(genericInterfaces[i]);
            }
        }
        
        private void introspectParameterizedType(final ParameterizedType parameterizedType) {
            final Class clazz = (Class)parameterizedType.getRawType();
            final TypeVariable<Class<?>>[] typeParameters = clazz.getTypeParameters();
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Preconditions.checkState(typeParameters.length == actualTypeArguments.length);
            for (int i = 0; i < typeParameters.length; ++i) {
                this.map(typeParameters[i], actualTypeArguments[i]);
            }
            this.introspectClass(clazz);
            this.introspect(parameterizedType.getOwnerType());
        }
        
        private void map(final TypeVariable<?> typeVariable, Type type) {
            if (!this.mappings.containsKey(typeVariable)) {
                for (Type type2 = type; type2 != null; type2 = this.mappings.get(type2)) {
                    if (typeVariable.equals(type2)) {
                        while (type != null) {
                            type = this.mappings.remove(type);
                        }
                        return;
                    }
                }
                this.mappings.put(typeVariable, type);
            }
        }
    }
    
    private static final class WildcardCapturer
    {
        private final AtomicInteger id;
        
        private WildcardCapturer() {
            this.id = new AtomicInteger();
        }
        
        private Type[] capture(final Type[] array) {
            final Type[] array2 = new Type[array.length];
            for (int i = 0; i < array.length; ++i) {
                array2[i] = this.capture(array[i]);
            }
            return array2;
        }
        
        private Type captureNullable(final Type type) {
            if (type == null) {
                return null;
            }
            return this.capture(type);
        }
        
        Type capture(final Type type) {
            Preconditions.checkNotNull(type);
            if (!(type instanceof Class) && !(type instanceof TypeVariable)) {
                if (type instanceof GenericArrayType) {
                    return Types.newArrayType(this.capture(((GenericArrayType)type).getGenericComponentType()));
                }
                if (type instanceof ParameterizedType) {
                    final ParameterizedType parameterizedType = (ParameterizedType)type;
                    return Types.newParameterizedTypeWithOwner(this.captureNullable(parameterizedType.getOwnerType()), (Class<?>)parameterizedType.getRawType(), this.capture(parameterizedType.getActualTypeArguments()));
                }
                if (!(type instanceof WildcardType)) {
                    throw new AssertionError((Object)"must have been one of the known types");
                }
                final WildcardType wildcardType = (WildcardType)type;
                if (wildcardType.getLowerBounds().length == 0) {
                    return Types.newTypeVariable(WildcardCapturer.class, "capture#" + this.id.incrementAndGet() + "-of ? extends " + Joiner.on('&').join(wildcardType.getUpperBounds()), wildcardType.getUpperBounds());
                }
            }
            return type;
        }
    }
}
