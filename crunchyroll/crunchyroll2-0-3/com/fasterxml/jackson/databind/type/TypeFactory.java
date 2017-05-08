// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.WildcardType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.fasterxml.jackson.databind.util.LRUMap;
import com.fasterxml.jackson.databind.JavaType;
import java.io.Serializable;

public final class TypeFactory implements Serializable
{
    protected static final SimpleType CORE_TYPE_BOOL;
    protected static final SimpleType CORE_TYPE_INT;
    protected static final SimpleType CORE_TYPE_LONG;
    protected static final SimpleType CORE_TYPE_STRING;
    private static final JavaType[] NO_TYPES;
    protected static final TypeFactory instance;
    protected transient HierarchicType _cachedArrayListType;
    protected transient HierarchicType _cachedHashMapType;
    protected final TypeModifier[] _modifiers;
    protected final TypeParser _parser;
    protected final LRUMap<ClassKey, JavaType> _typeCache;
    
    static {
        NO_TYPES = new JavaType[0];
        instance = new TypeFactory();
        CORE_TYPE_STRING = new SimpleType(String.class);
        CORE_TYPE_BOOL = new SimpleType(Boolean.TYPE);
        CORE_TYPE_INT = new SimpleType(Integer.TYPE);
        CORE_TYPE_LONG = new SimpleType(Long.TYPE);
    }
    
    private TypeFactory() {
        this._typeCache = new LRUMap<ClassKey, JavaType>(16, 100);
        this._parser = new TypeParser(this);
        this._modifiers = null;
    }
    
    private JavaType _collectionType(final Class<?> clazz) {
        final JavaType[] typeParameters = this.findTypeParameters(clazz, Collection.class);
        if (typeParameters == null) {
            return CollectionType.construct(clazz, this._unknownType());
        }
        if (typeParameters.length != 1) {
            throw new IllegalArgumentException("Strange Collection type " + clazz.getName() + ": can not determine type parameters");
        }
        return CollectionType.construct(clazz, typeParameters[0]);
    }
    
    private JavaType _mapType(final Class<?> clazz) {
        final JavaType[] typeParameters = this.findTypeParameters(clazz, Map.class);
        if (typeParameters == null) {
            return MapType.construct(clazz, this._unknownType(), this._unknownType());
        }
        if (typeParameters.length != 2) {
            throw new IllegalArgumentException("Strange Map type " + clazz.getName() + ": can not determine type parameters");
        }
        return MapType.construct(clazz, typeParameters[0], typeParameters[1]);
    }
    
    public static TypeFactory defaultInstance() {
        return TypeFactory.instance;
    }
    
    public static JavaType unknownType() {
        return defaultInstance()._unknownType();
    }
    
    protected HierarchicType _arrayListSuperInterfaceChain(final HierarchicType subType) {
        synchronized (this) {
            if (this._cachedArrayListType == null) {
                final HierarchicType deepCloneWithoutSubtype = subType.deepCloneWithoutSubtype();
                this._doFindSuperInterfaceChain(deepCloneWithoutSubtype, List.class);
                this._cachedArrayListType = deepCloneWithoutSubtype.getSuperType();
            }
            final HierarchicType deepCloneWithoutSubtype2 = this._cachedArrayListType.deepCloneWithoutSubtype();
            subType.setSuperType(deepCloneWithoutSubtype2);
            deepCloneWithoutSubtype2.setSubType(subType);
            return subType;
        }
    }
    
    protected JavaType _constructType(final Type type, final TypeBindings typeBindings) {
        JavaType javaType;
        if (type instanceof Class) {
            javaType = this._fromClass((Class<?>)type, typeBindings);
        }
        else if (type instanceof ParameterizedType) {
            javaType = this._fromParamType((ParameterizedType)type, typeBindings);
        }
        else {
            if (type instanceof JavaType) {
                return (JavaType)type;
            }
            if (type instanceof GenericArrayType) {
                javaType = this._fromArrayType((GenericArrayType)type, typeBindings);
            }
            else if (type instanceof TypeVariable) {
                javaType = this._fromVariable((TypeVariable<?>)type, typeBindings);
            }
            else {
                if (!(type instanceof WildcardType)) {
                    final StringBuilder append = new StringBuilder().append("Unrecognized Type: ");
                    String string;
                    if (type == null) {
                        string = "[null]";
                    }
                    else {
                        string = type.toString();
                    }
                    throw new IllegalArgumentException(append.append(string).toString());
                }
                javaType = this._fromWildcard((WildcardType)type, typeBindings);
            }
        }
        JavaType javaType2 = javaType;
        if (this._modifiers != null) {
            javaType2 = javaType;
            if (!javaType.isContainerType()) {
                final TypeModifier[] modifiers = this._modifiers;
                final int length = modifiers.length;
                int n = 0;
                while (true) {
                    javaType2 = javaType;
                    if (n >= length) {
                        break;
                    }
                    javaType = modifiers[n].modifyType(javaType, type, typeBindings, this);
                    ++n;
                }
            }
        }
        return javaType2;
    }
    
    protected HierarchicType _doFindSuperInterfaceChain(final HierarchicType hierarchicType, final Class<?> clazz) {
        final Class<?> rawClass = hierarchicType.getRawClass();
        final Type[] genericInterfaces = rawClass.getGenericInterfaces();
        if (genericInterfaces != null) {
            for (int length = genericInterfaces.length, i = 0; i < length; ++i) {
                final HierarchicType findSuperInterfaceChain = this._findSuperInterfaceChain(genericInterfaces[i], clazz);
                if (findSuperInterfaceChain != null) {
                    findSuperInterfaceChain.setSubType(hierarchicType);
                    hierarchicType.setSuperType(findSuperInterfaceChain);
                    return hierarchicType;
                }
            }
        }
        final Type genericSuperclass = rawClass.getGenericSuperclass();
        if (genericSuperclass != null) {
            final HierarchicType findSuperInterfaceChain2 = this._findSuperInterfaceChain(genericSuperclass, clazz);
            if (findSuperInterfaceChain2 != null) {
                findSuperInterfaceChain2.setSubType(hierarchicType);
                hierarchicType.setSuperType(findSuperInterfaceChain2);
                return hierarchicType;
            }
        }
        return null;
    }
    
    protected HierarchicType _findSuperClassChain(final Type type, final Class<?> clazz) {
        final HierarchicType subType = new HierarchicType(type);
        final Class<?> rawClass = subType.getRawClass();
        if (rawClass == clazz) {
            return subType;
        }
        final Type genericSuperclass = rawClass.getGenericSuperclass();
        if (genericSuperclass != null) {
            final HierarchicType findSuperClassChain = this._findSuperClassChain(genericSuperclass, clazz);
            if (findSuperClassChain != null) {
                findSuperClassChain.setSubType(subType);
                subType.setSuperType(findSuperClassChain);
                return subType;
            }
        }
        return null;
    }
    
    protected HierarchicType _findSuperInterfaceChain(final Type type, final Class<?> clazz) {
        final HierarchicType hierarchicType = new HierarchicType(type);
        final Class<?> rawClass = hierarchicType.getRawClass();
        if (rawClass == clazz) {
            return new HierarchicType(type);
        }
        if (rawClass == HashMap.class && clazz == Map.class) {
            return this._hashMapSuperInterfaceChain(hierarchicType);
        }
        if (rawClass == ArrayList.class && clazz == List.class) {
            return this._arrayListSuperInterfaceChain(hierarchicType);
        }
        return this._doFindSuperInterfaceChain(hierarchicType, clazz);
    }
    
    protected HierarchicType _findSuperTypeChain(final Class<?> clazz, final Class<?> clazz2) {
        if (clazz2.isInterface()) {
            return this._findSuperInterfaceChain(clazz, clazz2);
        }
        return this._findSuperClassChain(clazz, clazz2);
    }
    
    protected JavaType _fromArrayType(final GenericArrayType genericArrayType, final TypeBindings typeBindings) {
        return ArrayType.construct(this._constructType(genericArrayType.getGenericComponentType(), typeBindings), null, null);
    }
    
    protected JavaType _fromClass(final Class<?> clazz, TypeBindings typeBindings) {
        if (clazz == String.class) {
            typeBindings = (TypeBindings)TypeFactory.CORE_TYPE_STRING;
        }
        else {
            if (clazz == Boolean.TYPE) {
                return TypeFactory.CORE_TYPE_BOOL;
            }
            if (clazz == Integer.TYPE) {
                return TypeFactory.CORE_TYPE_INT;
            }
            if (clazz == Long.TYPE) {
                return TypeFactory.CORE_TYPE_LONG;
            }
            while (true) {
                final ClassKey classKey = new ClassKey(clazz);
                while (true) {
                    synchronized (this._typeCache) {
                        final Object o = this._typeCache.get(classKey);
                        // monitorexit(this._typeCache)
                        typeBindings = (TypeBindings)o;
                        if (o != null) {
                            break;
                        }
                        if (clazz.isArray()) {
                            final JavaType javaType = ArrayType.construct(this._constructType(clazz.getComponentType(), null), null, null);
                            typeBindings = (TypeBindings)this._typeCache;
                            synchronized (this._typeCache) {
                                this._typeCache.put(classKey, javaType);
                                return javaType;
                            }
                        }
                    }
                    final Class<?> clazz2;
                    if (clazz2.isEnum()) {
                        final JavaType javaType = new SimpleType(clazz2);
                        continue;
                    }
                    if (Map.class.isAssignableFrom(clazz2)) {
                        final JavaType javaType = this._mapType(clazz2);
                        continue;
                    }
                    if (Collection.class.isAssignableFrom(clazz2)) {
                        final JavaType javaType = this._collectionType(clazz2);
                        continue;
                    }
                    final JavaType javaType = new SimpleType(clazz2);
                    continue;
                }
            }
        }
        return (JavaType)typeBindings;
    }
    
    protected JavaType _fromParamType(final ParameterizedType parameterizedType, final TypeBindings typeBindings) {
        final Class clazz = (Class)parameterizedType.getRawType();
        final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        int length;
        if (actualTypeArguments == null) {
            length = 0;
        }
        else {
            length = actualTypeArguments.length;
        }
        JavaType[] no_TYPES;
        if (length == 0) {
            no_TYPES = TypeFactory.NO_TYPES;
        }
        else {
            final JavaType[] array = new JavaType[length];
            int n = 0;
            while (true) {
                no_TYPES = array;
                if (n >= length) {
                    break;
                }
                array[n] = this._constructType(actualTypeArguments[n], typeBindings);
                ++n;
            }
        }
        if (Map.class.isAssignableFrom(clazz)) {
            final JavaType[] typeParameters = this.findTypeParameters(this.constructSimpleType(clazz, no_TYPES), Map.class);
            if (typeParameters.length != 2) {
                throw new IllegalArgumentException("Could not find 2 type parameters for Map class " + clazz.getName() + " (found " + typeParameters.length + ")");
            }
            return MapType.construct(clazz, typeParameters[0], typeParameters[1]);
        }
        else if (Collection.class.isAssignableFrom(clazz)) {
            final JavaType[] typeParameters2 = this.findTypeParameters(this.constructSimpleType(clazz, no_TYPES), Collection.class);
            if (typeParameters2.length != 1) {
                throw new IllegalArgumentException("Could not find 1 type parameter for Collection class " + clazz.getName() + " (found " + typeParameters2.length + ")");
            }
            return CollectionType.construct(clazz, typeParameters2[0]);
        }
        else {
            if (length == 0) {
                return new SimpleType(clazz);
            }
            return this.constructSimpleType(clazz, no_TYPES);
        }
    }
    
    protected JavaType _fromParameterizedClass(final Class<?> clazz, final List<JavaType> list) {
        if (clazz.isArray()) {
            return ArrayType.construct(this._constructType(clazz.getComponentType(), null), null, null);
        }
        if (clazz.isEnum()) {
            return new SimpleType(clazz);
        }
        if (Map.class.isAssignableFrom(clazz)) {
            if (list.size() > 0) {
                final JavaType javaType = list.get(0);
                JavaType unknownType;
                if (list.size() >= 2) {
                    unknownType = list.get(1);
                }
                else {
                    unknownType = this._unknownType();
                }
                return MapType.construct(clazz, javaType, unknownType);
            }
            return this._mapType(clazz);
        }
        else if (Collection.class.isAssignableFrom(clazz)) {
            if (list.size() >= 1) {
                return CollectionType.construct(clazz, list.get(0));
            }
            return this._collectionType(clazz);
        }
        else {
            if (list.size() == 0) {
                return new SimpleType(clazz);
            }
            return this.constructSimpleType(clazz, list.toArray(new JavaType[list.size()]));
        }
    }
    
    protected JavaType _fromVariable(final TypeVariable<?> typeVariable, final TypeBindings typeBindings) {
        JavaType javaType;
        if (typeBindings == null) {
            javaType = this._unknownType();
        }
        else {
            final String name = typeVariable.getName();
            if ((javaType = typeBindings.findType(name)) == null) {
                final Type[] bounds = typeVariable.getBounds();
                typeBindings._addPlaceholder(name);
                return this._constructType(bounds[0], typeBindings);
            }
        }
        return javaType;
    }
    
    protected JavaType _fromWildcard(final WildcardType wildcardType, final TypeBindings typeBindings) {
        return this._constructType(wildcardType.getUpperBounds()[0], typeBindings);
    }
    
    protected HierarchicType _hashMapSuperInterfaceChain(final HierarchicType subType) {
        synchronized (this) {
            if (this._cachedHashMapType == null) {
                final HierarchicType deepCloneWithoutSubtype = subType.deepCloneWithoutSubtype();
                this._doFindSuperInterfaceChain(deepCloneWithoutSubtype, Map.class);
                this._cachedHashMapType = deepCloneWithoutSubtype.getSuperType();
            }
            final HierarchicType deepCloneWithoutSubtype2 = this._cachedHashMapType.deepCloneWithoutSubtype();
            subType.setSuperType(deepCloneWithoutSubtype2);
            deepCloneWithoutSubtype2.setSubType(subType);
            return subType;
        }
    }
    
    protected JavaType _unknownType() {
        return new SimpleType(Object.class);
    }
    
    public CollectionType constructCollectionType(final Class<? extends Collection> clazz, final JavaType javaType) {
        return CollectionType.construct(clazz, javaType);
    }
    
    public CollectionType constructCollectionType(final Class<? extends Collection> clazz, final Class<?> clazz2) {
        return CollectionType.construct(clazz, this.constructType(clazz2));
    }
    
    public JavaType constructFromCanonical(final String s) throws IllegalArgumentException {
        return this._parser.parse(s);
    }
    
    public MapType constructMapType(final Class<? extends Map> clazz, final Class<?> clazz2, final Class<?> clazz3) {
        return MapType.construct(clazz, this.constructType(clazz2), this.constructType(clazz3));
    }
    
    public JavaType constructSimpleType(final Class<?> clazz, final JavaType[] array) {
        final TypeVariable<Class<?>>[] typeParameters = clazz.getTypeParameters();
        if (typeParameters.length != array.length) {
            throw new IllegalArgumentException("Parameter type mismatch for " + clazz.getName() + ": expected " + typeParameters.length + " parameters, was given " + array.length);
        }
        final String[] array2 = new String[typeParameters.length];
        for (int length = typeParameters.length, i = 0; i < length; ++i) {
            array2[i] = typeParameters[i].getName();
        }
        return new SimpleType(clazz, array2, array, null, null, false);
    }
    
    public JavaType constructSpecializedType(JavaType withTypeHandler, final Class<?> clazz) {
        if (!(withTypeHandler instanceof SimpleType) || (!clazz.isArray() && !Map.class.isAssignableFrom(clazz) && !Collection.class.isAssignableFrom(clazz))) {
            return withTypeHandler.narrowBy(clazz);
        }
        if (!withTypeHandler.getRawClass().isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Class " + clazz.getClass().getName() + " not subtype of " + withTypeHandler);
        }
        final JavaType fromClass = this._fromClass(clazz, new TypeBindings(this, withTypeHandler.getRawClass()));
        final Object valueHandler = withTypeHandler.getValueHandler();
        JavaType withValueHandler = fromClass;
        if (valueHandler != null) {
            withValueHandler = fromClass.withValueHandler(valueHandler);
        }
        final Object typeHandler = withTypeHandler.getTypeHandler();
        withTypeHandler = withValueHandler;
        if (typeHandler != null) {
            withTypeHandler = withValueHandler.withTypeHandler(typeHandler);
        }
        return withTypeHandler;
    }
    
    public JavaType constructType(final TypeReference<?> typeReference) {
        return this._constructType(typeReference.getType(), null);
    }
    
    public JavaType constructType(final Type type) {
        return this._constructType(type, null);
    }
    
    public JavaType constructType(final Type type, final TypeBindings typeBindings) {
        return this._constructType(type, typeBindings);
    }
    
    public JavaType[] findTypeParameters(final JavaType javaType, final Class<?> clazz) {
        final Class<?> rawClass = javaType.getRawClass();
        if (rawClass == clazz) {
            final int containedTypeCount = javaType.containedTypeCount();
            JavaType[] array;
            if (containedTypeCount == 0) {
                array = null;
            }
            else {
                final JavaType[] array2 = new JavaType[containedTypeCount];
                int n = 0;
                while (true) {
                    array = array2;
                    if (n >= containedTypeCount) {
                        break;
                    }
                    array2[n] = javaType.containedType(n);
                    ++n;
                }
            }
            return array;
        }
        return this.findTypeParameters(rawClass, clazz, new TypeBindings(this, javaType));
    }
    
    public JavaType[] findTypeParameters(final Class<?> clazz, final Class<?> clazz2) {
        return this.findTypeParameters(clazz, clazz2, new TypeBindings(this, clazz));
    }
    
    public JavaType[] findTypeParameters(final Class<?> clazz, final Class<?> clazz2, TypeBindings typeBindings) {
        HierarchicType hierarchicType;
        if ((hierarchicType = this._findSuperTypeChain(clazz, clazz2)) == null) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not a subtype of " + clazz2.getName());
        }
        while (hierarchicType.getSuperType() != null) {
            hierarchicType = hierarchicType.getSuperType();
            final Class<?> rawClass = hierarchicType.getRawClass();
            final TypeBindings typeBindings2 = new TypeBindings(this, rawClass);
            if (hierarchicType.isGeneric()) {
                final Type[] actualTypeArguments = hierarchicType.asGeneric().getActualTypeArguments();
                final TypeVariable<Class<?>>[] typeParameters = rawClass.getTypeParameters();
                for (int length = actualTypeArguments.length, i = 0; i < length; ++i) {
                    typeBindings2.addBinding(typeParameters[i].getName(), this._constructType(actualTypeArguments[i], typeBindings));
                }
            }
            typeBindings = typeBindings2;
        }
        if (!hierarchicType.isGeneric()) {
            return null;
        }
        return typeBindings.typesAsArray();
    }
    
    public JavaType uncheckedSimpleType(final Class<?> clazz) {
        return new SimpleType(clazz);
    }
}
