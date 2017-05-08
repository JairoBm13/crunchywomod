// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.util;

import java.lang.reflect.Field;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.EnumSet;
import java.util.EnumMap;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.Collection;

public final class ClassUtil
{
    private static void _addSuperTypes(final Class<?> clazz, final Class<?> clazz2, final Collection<Class<?>> collection, final boolean b) {
        if (clazz != clazz2 && clazz != null && clazz != Object.class) {
            if (b) {
                if (collection.contains(clazz)) {
                    return;
                }
                collection.add(clazz);
            }
            final Class<?>[] interfaces = clazz.getInterfaces();
            for (int length = interfaces.length, i = 0; i < length; ++i) {
                _addSuperTypes(interfaces[i], clazz2, collection, true);
            }
            _addSuperTypes(clazz.getSuperclass(), clazz2, collection, true);
        }
    }
    
    public static String canBeABeanType(final Class<?> clazz) {
        if (clazz.isAnnotation()) {
            return "annotation";
        }
        if (clazz.isArray()) {
            return "array";
        }
        if (clazz.isEnum()) {
            return "enum";
        }
        if (clazz.isPrimitive()) {
            return "primitive";
        }
        return null;
    }
    
    public static void checkAndFixAccess(final Member member) {
        final AccessibleObject accessibleObject = (AccessibleObject)member;
        try {
            accessibleObject.setAccessible(true);
        }
        catch (SecurityException ex) {
            if (!accessibleObject.isAccessible()) {
                throw new IllegalArgumentException("Can not access " + member + " (from class " + member.getDeclaringClass().getName() + "; failed to set access: " + ex.getMessage());
            }
        }
    }
    
    public static <T> T createInstance(final Class<T> clazz, final boolean b) throws IllegalArgumentException {
        final Constructor<T> constructor = findConstructor(clazz, b);
        if (constructor == null) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " has no default (no arg) constructor");
        }
        try {
            return constructor.newInstance(new Object[0]);
        }
        catch (Exception ex) {
            unwrapAndThrowAsIAE(ex, "Failed to instantiate class " + clazz.getName() + ", problem: " + ex.getMessage());
            return null;
        }
    }
    
    public static Object defaultValue(final Class<?> clazz) {
        if (clazz == Integer.TYPE) {
            return 0;
        }
        if (clazz == Long.TYPE) {
            return 0L;
        }
        if (clazz == Boolean.TYPE) {
            return Boolean.FALSE;
        }
        if (clazz == Double.TYPE) {
            return 0.0;
        }
        if (clazz == Float.TYPE) {
            return 0.0f;
        }
        if (clazz == Byte.TYPE) {
            return 0;
        }
        if (clazz == Short.TYPE) {
            return 0;
        }
        if (clazz == Character.TYPE) {
            return '\0';
        }
        throw new IllegalArgumentException("Class " + clazz.getName() + " is not a primitive type");
    }
    
    public static Class<?> findClass(final String s) throws ClassNotFoundException {
        if (s.indexOf(46) < 0) {
            if ("int".equals(s)) {
                return Integer.TYPE;
            }
            if ("long".equals(s)) {
                return Long.TYPE;
            }
            if ("float".equals(s)) {
                return Float.TYPE;
            }
            if ("double".equals(s)) {
                return Double.TYPE;
            }
            if ("boolean".equals(s)) {
                return Boolean.TYPE;
            }
            if ("byte".equals(s)) {
                return Byte.TYPE;
            }
            if ("char".equals(s)) {
                return Character.TYPE;
            }
            if ("short".equals(s)) {
                return Short.TYPE;
            }
            if ("void".equals(s)) {
                return Void.TYPE;
            }
        }
        Throwable rootCause = null;
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            try {
                return Class.forName(s, true, contextClassLoader);
            }
            catch (Exception ex) {
                rootCause = getRootCause(ex);
            }
        }
        try {
            return Class.forName(s);
        }
        catch (Exception ex2) {
            Throwable rootCause2 = rootCause;
            if (rootCause == null) {
                rootCause2 = getRootCause(ex2);
            }
            if (rootCause2 instanceof RuntimeException) {
                throw (RuntimeException)rootCause2;
            }
            throw new ClassNotFoundException(rootCause2.getMessage(), rootCause2);
        }
    }
    
    public static <T> Constructor<T> findConstructor(final Class<T> clazz, final boolean b) throws IllegalArgumentException {
        Constructor<T> declaredConstructor = null;
        try {
            declaredConstructor = clazz.getDeclaredConstructor((Class<?>[])new Class[0]);
            if (b) {
                checkAndFixAccess(declaredConstructor);
                return declaredConstructor;
            }
            if (!Modifier.isPublic(declaredConstructor.getModifiers())) {
                throw new IllegalArgumentException("Default constructor for " + clazz.getName() + " is not accessible (non-public?): not allowed to try modify access via Reflection: can not instantiate type");
            }
        }
        catch (NoSuchMethodException ex2) {}
        catch (Exception ex) {
            unwrapAndThrowAsIAE(ex, "Failed to find default constructor of class " + clazz.getName() + ", problem: " + ex.getMessage());
            goto Label_0067;
        }
        return declaredConstructor;
    }
    
    public static Class<? extends Enum<?>> findEnumType(final Class<?> clazz) {
        Serializable superclass = clazz;
        if (clazz.getSuperclass() != Enum.class) {
            superclass = clazz.getSuperclass();
        }
        return (Class<? extends Enum<?>>)superclass;
    }
    
    public static Class<? extends Enum<?>> findEnumType(final Enum<?> enum1) {
        Serializable s;
        final Class<? extends Enum> clazz = (Class<? extends Enum>)(s = enum1.getClass());
        if (clazz.getSuperclass() != Enum.class) {
            s = clazz.getSuperclass();
        }
        return (Class<? extends Enum<?>>)s;
    }
    
    public static Class<? extends Enum<?>> findEnumType(final EnumMap<?, ?> enumMap) {
        if (!enumMap.isEmpty()) {
            return findEnumType((Enum<?>)enumMap.keySet().iterator().next());
        }
        return EnumTypeLocator.instance.enumTypeFor(enumMap);
    }
    
    public static Class<? extends Enum<?>> findEnumType(final EnumSet<?> set) {
        if (!set.isEmpty()) {
            return findEnumType(set.iterator().next());
        }
        return EnumTypeLocator.instance.enumTypeFor(set);
    }
    
    public static List<Class<?>> findSuperTypes(final Class<?> clazz, final Class<?> clazz2) {
        return findSuperTypes(clazz, clazz2, new ArrayList<Class<?>>(8));
    }
    
    public static List<Class<?>> findSuperTypes(final Class<?> clazz, final Class<?> clazz2, final List<Class<?>> list) {
        _addSuperTypes(clazz, clazz2, list, false);
        return list;
    }
    
    public static Class<?> getOuterClass(final Class<?> clazz) {
        try {
            if (clazz.getEnclosingMethod() != null) {
                return null;
            }
            if (!Modifier.isStatic(clazz.getModifiers())) {
                return clazz.getEnclosingClass();
            }
        }
        catch (NullPointerException ex) {
            return null;
        }
        catch (SecurityException ex2) {}
        return null;
    }
    
    public static Throwable getRootCause(Throwable cause) {
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause;
    }
    
    public static boolean isCollectionMapOrArray(final Class<?> clazz) {
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz);
    }
    
    public static boolean isConcrete(final Class<?> clazz) {
        return (clazz.getModifiers() & 0x600) == 0x0;
    }
    
    public static boolean isJacksonStdImpl(final Class<?> clazz) {
        return clazz.getAnnotation(JacksonStdImpl.class) != null;
    }
    
    public static boolean isJacksonStdImpl(final Object o) {
        return o != null && isJacksonStdImpl(o.getClass());
    }
    
    public static String isLocalType(final Class<?> clazz, final boolean b) {
        try {
            if (clazz.getEnclosingMethod() != null) {
                return "local/anonymous";
            }
            if (!b && clazz.getEnclosingClass() != null && !Modifier.isStatic(clazz.getModifiers())) {
                return "non-static member class";
            }
            goto Label_0037;
        }
        catch (NullPointerException ex) {}
        catch (SecurityException ex2) {
            goto Label_0037;
        }
    }
    
    public static boolean isProxyType(final Class<?> clazz) {
        final String name = clazz.getName();
        return name.startsWith("net.sf.cglib.proxy.") || name.startsWith("org.hibernate.proxy.");
    }
    
    public static void throwAsIAE(final Throwable t) {
        throwAsIAE(t, t.getMessage());
    }
    
    public static void throwAsIAE(final Throwable t, final String s) {
        if (t instanceof RuntimeException) {
            throw (RuntimeException)t;
        }
        if (t instanceof Error) {
            throw (Error)t;
        }
        throw new IllegalArgumentException(s, t);
    }
    
    public static void unwrapAndThrowAsIAE(final Throwable t) {
        throwAsIAE(getRootCause(t));
    }
    
    public static void unwrapAndThrowAsIAE(final Throwable t, final String s) {
        throwAsIAE(getRootCause(t), s);
    }
    
    public static Class<?> wrapperType(final Class<?> clazz) {
        if (clazz == Integer.TYPE) {
            return Integer.class;
        }
        if (clazz == Long.TYPE) {
            return Long.class;
        }
        if (clazz == Boolean.TYPE) {
            return Boolean.class;
        }
        if (clazz == Double.TYPE) {
            return Double.class;
        }
        if (clazz == Float.TYPE) {
            return Float.class;
        }
        if (clazz == Byte.TYPE) {
            return Byte.class;
        }
        if (clazz == Short.TYPE) {
            return Short.class;
        }
        if (clazz == Character.TYPE) {
            return Character.class;
        }
        throw new IllegalArgumentException("Class " + clazz.getName() + " is not a primitive type");
    }
    
    private static class EnumTypeLocator
    {
        static final EnumTypeLocator instance;
        private final Field enumMapTypeField;
        private final Field enumSetTypeField;
        
        static {
            instance = new EnumTypeLocator();
        }
        
        private EnumTypeLocator() {
            this.enumSetTypeField = locateField(EnumSet.class, "elementType", Class.class);
            this.enumMapTypeField = locateField(EnumMap.class, "elementType", Class.class);
        }
        
        private Object get(Object value, final Field field) {
            try {
                value = field.get(value);
                return value;
            }
            catch (Exception ex) {
                throw new IllegalArgumentException(ex);
            }
        }
        
        private static Field locateField(final Class<?> clazz, String o, final Class<?> clazz2) {
            final Field[] declaredFields = clazz.getDeclaredFields();
            final int length = declaredFields.length;
            int i = 0;
            while (true) {
                while (i < length) {
                    Field field = declaredFields[i];
                    if (((String)o).equals(field.getName()) && field.getType() == clazz2) {
                        if ((o = field) == null) {
                            Field field2;
                            for (int length2 = declaredFields.length, j = 0; j < length2; ++j, field = field2) {
                                field2 = declaredFields[j];
                                if (field2.getType() == clazz2) {
                                    if (field != null) {
                                        final Object o2 = null;
                                        return (Field)o2;
                                    }
                                }
                                else {
                                    field2 = field;
                                }
                            }
                            o = field;
                        }
                        Label_0103: {
                            break Label_0103;
                        }
                        Object o2;
                        if ((o2 = o) != null) {
                            try {
                                ((AccessibleObject)o).setAccessible(true);
                                return (Field)o;
                            }
                            catch (Throwable t) {
                                return (Field)o;
                            }
                            break;
                        }
                        return (Field)o2;
                    }
                    else {
                        ++i;
                    }
                }
                Field field = null;
                continue;
            }
        }
        
        public Class<? extends Enum<?>> enumTypeFor(final EnumMap<?, ?> enumMap) {
            if (this.enumMapTypeField != null) {
                return (Class<? extends Enum<?>>)this.get(enumMap, this.enumMapTypeField);
            }
            throw new IllegalStateException("Can not figure out type for EnumMap (odd JDK platform?)");
        }
        
        public Class<? extends Enum<?>> enumTypeFor(final EnumSet<?> set) {
            if (this.enumSetTypeField != null) {
                return (Class<? extends Enum<?>>)this.get(set, this.enumSetTypeField);
            }
            throw new IllegalStateException("Can not figure out type for EnumSet (odd JDK platform?)");
        }
    }
}
