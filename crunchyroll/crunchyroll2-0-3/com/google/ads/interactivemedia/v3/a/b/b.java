// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Properties;
import java.util.Arrays;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.TypeVariable;
import java.util.NoSuchElementException;
import java.lang.reflect.Type;

public final class b
{
    static final Type[] a;
    
    static {
        a = new Type[0];
    }
    
    private static int a(final Object[] array, final Object o) {
        for (int i = 0; i < array.length; ++i) {
            if (o.equals(array[i])) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }
    
    private static Class<?> a(final TypeVariable<?> typeVariable) {
        final Object genericDeclaration = typeVariable.getGenericDeclaration();
        if (genericDeclaration instanceof Class) {
            return (Class<?>)genericDeclaration;
        }
        return null;
    }
    
    public static GenericArrayType a(final Type type) {
        return new a(type);
    }
    
    public static ParameterizedType a(final Type type, final Type type2, final Type... array) {
        return new b(type, type2, array);
    }
    
    public static Type a(Type b, final Class<?> clazz) {
        final Type type = b = b(b, clazz, Collection.class);
        if (type instanceof WildcardType) {
            b = ((WildcardType)type).getUpperBounds()[0];
        }
        if (b instanceof ParameterizedType) {
            return ((ParameterizedType)b).getActualTypeArguments()[0];
        }
        return Object.class;
    }
    
    static Type a(final Type type, Class<?> clazz, final Class<?> clazz2) {
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
                    return a(clazz.getGenericInterfaces()[i], interfaces[i], clazz2);
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
                    return a(clazz.getGenericSuperclass(), superclass, clazz2);
                }
                clazz = (Class<Object>)superclass;
            }
        }
        return clazz2;
    }
    
    public static Type a(Type type, final Class<?> clazz, Type type2) {
        Type type3;
        for (type3 = type2; type3 instanceof TypeVariable; type3 = type2) {
            final TypeVariable<?> typeVariable = (TypeVariable<?>)type3;
            type2 = a(type, clazz, typeVariable);
            if (type2 == typeVariable) {
                return type2;
            }
        }
        if (type3 instanceof Class && ((Class)type3).isArray()) {
            type2 = type3;
            final Class<?> componentType = ((Class)type2).getComponentType();
            type = a(type, clazz, (Type)componentType);
            if (componentType != type) {
                return a(type);
            }
            return type2;
        }
        else if (type3 instanceof GenericArrayType) {
            type2 = type3;
            final Type genericComponentType = ((GenericArrayType)type2).getGenericComponentType();
            type = a(type, clazz, genericComponentType);
            if (genericComponentType != type) {
                return a(type);
            }
            return type2;
        }
        else if (type3 instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)type3;
            type2 = parameterizedType.getOwnerType();
            final Type a = a(type, clazz, type2);
            int n;
            if (a != type2) {
                n = 1;
            }
            else {
                n = 0;
            }
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Type[] array;
            int n2;
            for (int length = actualTypeArguments.length, i = 0; i < length; ++i, actualTypeArguments = array, n = n2) {
                final Type a2 = a(type, clazz, actualTypeArguments[i]);
                array = actualTypeArguments;
                n2 = n;
                if (a2 != actualTypeArguments[i]) {
                    array = actualTypeArguments;
                    if ((n2 = n) == 0) {
                        array = actualTypeArguments.clone();
                        n2 = 1;
                    }
                    array[i] = a2;
                }
            }
            type2 = parameterizedType;
            if (n != 0) {
                return a(a, parameterizedType.getRawType(), actualTypeArguments);
            }
            return type2;
        }
        else {
            type2 = type3;
            if (!(type3 instanceof WildcardType)) {
                return type2;
            }
            final WildcardType wildcardType = (WildcardType)type3;
            final Type[] lowerBounds = wildcardType.getLowerBounds();
            final Type[] upperBounds = wildcardType.getUpperBounds();
            if (lowerBounds.length == 1) {
                type = a(type, clazz, lowerBounds[0]);
                type2 = wildcardType;
                if (type != lowerBounds[0]) {
                    return c(type);
                }
                return type2;
            }
            else {
                type2 = wildcardType;
                if (upperBounds.length != 1) {
                    return type2;
                }
                type = a(type, clazz, upperBounds[0]);
                type2 = wildcardType;
                if (type != upperBounds[0]) {
                    return b(type);
                }
                return type2;
            }
        }
    }
    
    static Type a(Type a, final Class<?> clazz, final TypeVariable<?> typeVariable) {
        final Class<?> a2 = a(typeVariable);
        if (a2 != null) {
            a = a(a, clazz, a2);
            if (a instanceof ParameterizedType) {
                return ((ParameterizedType)a).getActualTypeArguments()[a(a2.getTypeParameters(), typeVariable)];
            }
        }
        return typeVariable;
    }
    
    static boolean a(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    public static boolean a(final Type type, final Type type2) {
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
                    return a(parameterizedType.getOwnerType(), (Object)parameterizedType2.getOwnerType()) && parameterizedType.getRawType().equals(parameterizedType2.getRawType()) && Arrays.equals(parameterizedType.getActualTypeArguments(), parameterizedType2.getActualTypeArguments()) && b3;
                }
            }
            else if (type instanceof GenericArrayType) {
                b5 = b4;
                if (type2 instanceof GenericArrayType) {
                    return a(((GenericArrayType)type).getGenericComponentType(), ((GenericArrayType)type2).getGenericComponentType());
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
    
    private static int b(final Object o) {
        if (o != null) {
            return o.hashCode();
        }
        return 0;
    }
    
    static Type b(final Type type, final Class<?> clazz, final Class<?> clazz2) {
        com.google.ads.interactivemedia.v3.a.b.a.a(clazz2.isAssignableFrom(clazz));
        return a(type, clazz, a(type, clazz, clazz2));
    }
    
    public static WildcardType b(final Type type) {
        return new c(new Type[] { type }, b.a);
    }
    
    public static Type[] b(Type b, final Class<?> clazz) {
        if (b == Properties.class) {
            return new Type[] { String.class, String.class };
        }
        b = b(b, clazz, Map.class);
        if (b instanceof ParameterizedType) {
            return ((ParameterizedType)b).getActualTypeArguments();
        }
        return new Type[] { Object.class, Object.class };
    }
    
    public static WildcardType c(final Type type) {
        return new c(new Type[] { Object.class }, new Type[] { type });
    }
    
    public static Type d(final Type type) {
        Object o;
        if (type instanceof Class) {
            final Class clazz = (Class)(o = type);
            if (clazz.isArray()) {
                o = new a(d(clazz.getComponentType()));
            }
        }
        else {
            if (type instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType)type;
                return new b(parameterizedType.getOwnerType(), parameterizedType.getRawType(), parameterizedType.getActualTypeArguments());
            }
            if (type instanceof GenericArrayType) {
                return new a(((GenericArrayType)type).getGenericComponentType());
            }
            o = type;
            if (type instanceof WildcardType) {
                final WildcardType wildcardType = (WildcardType)type;
                return new c(wildcardType.getUpperBounds(), wildcardType.getLowerBounds());
            }
        }
        return (Type)o;
    }
    
    public static Class<?> e(Type rawType) {
        if (rawType instanceof Class) {
            return (Class<?>)rawType;
        }
        if (rawType instanceof ParameterizedType) {
            rawType = ((ParameterizedType)rawType).getRawType();
            com.google.ads.interactivemedia.v3.a.b.a.a(rawType instanceof Class);
            return (Class<?>)rawType;
        }
        if (rawType instanceof GenericArrayType) {
            return Array.newInstance(e(((GenericArrayType)rawType).getGenericComponentType()), 0).getClass();
        }
        if (rawType instanceof TypeVariable) {
            return Object.class;
        }
        if (rawType instanceof WildcardType) {
            return e(((WildcardType)rawType).getUpperBounds()[0]);
        }
        String name;
        if (rawType == null) {
            name = "null";
        }
        else {
            name = rawType.getClass().getName();
        }
        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + rawType + "> is of type " + name);
    }
    
    public static String f(final Type type) {
        if (type instanceof Class) {
            return ((Class)type).getName();
        }
        return type.toString();
    }
    
    public static Type g(final Type type) {
        if (type instanceof GenericArrayType) {
            return ((GenericArrayType)type).getGenericComponentType();
        }
        return ((Class)type).getComponentType();
    }
    
    private static void i(final Type type) {
        com.google.ads.interactivemedia.v3.a.b.a.a(!(type instanceof Class) || !((Class)type).isPrimitive());
    }
    
    private static final class a implements Serializable, GenericArrayType
    {
        private final Type a;
        
        public a(final Type type) {
            this.a = b.d(type);
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof GenericArrayType && b.a(this, (Type)o);
        }
        
        @Override
        public Type getGenericComponentType() {
            return this.a;
        }
        
        @Override
        public int hashCode() {
            return this.a.hashCode();
        }
        
        @Override
        public String toString() {
            return b.f(this.a) + "[]";
        }
    }
    
    private static final class b implements Serializable, ParameterizedType
    {
        private final Type a;
        private final Type b;
        private final Type[] c;
        
        public b(Type d, final Type type, final Type... array) {
            final boolean b = true;
            int i = 0;
            if (type instanceof Class) {
                final Class clazz = (Class)type;
                com.google.ads.interactivemedia.v3.a.b.a.a(d != null || clazz.getEnclosingClass() == null);
                boolean b2 = b;
                if (d != null) {
                    b2 = (clazz.getEnclosingClass() != null && b);
                }
                com.google.ads.interactivemedia.v3.a.b.a.a(b2);
            }
            if (d == null) {
                d = null;
            }
            else {
                d = com.google.ads.interactivemedia.v3.a.b.b.d(d);
            }
            this.a = d;
            this.b = com.google.ads.interactivemedia.v3.a.b.b.d(type);
            this.c = array.clone();
            while (i < this.c.length) {
                com.google.ads.interactivemedia.v3.a.b.a.a(this.c[i]);
                i(this.c[i]);
                this.c[i] = com.google.ads.interactivemedia.v3.a.b.b.d(this.c[i]);
                ++i;
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof ParameterizedType && com.google.ads.interactivemedia.v3.a.b.b.a(this, (Type)o);
        }
        
        @Override
        public Type[] getActualTypeArguments() {
            return this.c.clone();
        }
        
        @Override
        public Type getOwnerType() {
            return this.a;
        }
        
        @Override
        public Type getRawType() {
            return this.b;
        }
        
        @Override
        public int hashCode() {
            return Arrays.hashCode(this.c) ^ this.b.hashCode() ^ b((Object)this.a);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder((this.c.length + 1) * 30);
            sb.append(com.google.ads.interactivemedia.v3.a.b.b.f(this.b));
            if (this.c.length == 0) {
                return sb.toString();
            }
            sb.append("<").append(com.google.ads.interactivemedia.v3.a.b.b.f(this.c[0]));
            for (int i = 1; i < this.c.length; ++i) {
                sb.append(", ").append(com.google.ads.interactivemedia.v3.a.b.b.f(this.c[i]));
            }
            return sb.append(">").toString();
        }
    }
    
    private static final class c implements Serializable, WildcardType
    {
        private final Type a;
        private final Type b;
        
        public c(final Type[] array, final Type[] array2) {
            final boolean b = true;
            com.google.ads.interactivemedia.v3.a.b.a.a(array2.length <= 1);
            com.google.ads.interactivemedia.v3.a.b.a.a(array.length == 1);
            if (array2.length == 1) {
                com.google.ads.interactivemedia.v3.a.b.a.a(array2[0]);
                i(array2[0]);
                com.google.ads.interactivemedia.v3.a.b.a.a(array[0] == Object.class && b);
                this.b = com.google.ads.interactivemedia.v3.a.b.b.d(array2[0]);
                this.a = Object.class;
                return;
            }
            com.google.ads.interactivemedia.v3.a.b.a.a(array[0]);
            i(array[0]);
            this.b = null;
            this.a = com.google.ads.interactivemedia.v3.a.b.b.d(array[0]);
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof WildcardType && com.google.ads.interactivemedia.v3.a.b.b.a(this, (Type)o);
        }
        
        @Override
        public Type[] getLowerBounds() {
            if (this.b != null) {
                return new Type[] { this.b };
            }
            return com.google.ads.interactivemedia.v3.a.b.b.a;
        }
        
        @Override
        public Type[] getUpperBounds() {
            return new Type[] { this.a };
        }
        
        @Override
        public int hashCode() {
            int n;
            if (this.b != null) {
                n = this.b.hashCode() + 31;
            }
            else {
                n = 1;
            }
            return n ^ this.a.hashCode() + 31;
        }
        
        @Override
        public String toString() {
            if (this.b != null) {
                return "? super " + com.google.ads.interactivemedia.v3.a.b.b.f(this.b);
            }
            if (this.a == Object.class) {
                return "?";
            }
            return "? extends " + com.google.ads.interactivemedia.v3.a.b.b.f(this.a);
        }
    }
}
