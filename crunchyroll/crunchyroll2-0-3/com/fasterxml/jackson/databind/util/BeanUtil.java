// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.util;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

public class BeanUtil
{
    protected static boolean isCglibGetCallbacks(final AnnotatedMethod annotatedMethod) {
        final Class<?> rawType = annotatedMethod.getRawType();
        if (rawType != null && rawType.isArray()) {
            final Package package1 = rawType.getComponentType().getPackage();
            if (package1 != null) {
                final String name = package1.getName();
                if (name.startsWith("net.sf.cglib") || name.startsWith("org.hibernate.repackage.cglib")) {
                    return true;
                }
            }
        }
        return false;
    }
    
    protected static boolean isGroovyMetaClassGetter(final AnnotatedMethod annotatedMethod) {
        final Class<?> rawType = annotatedMethod.getRawType();
        if (rawType != null && !rawType.isArray()) {
            final Package package1 = rawType.getPackage();
            if (package1 != null && package1.getName().startsWith("groovy.lang")) {
                return true;
            }
        }
        return false;
    }
    
    protected static String manglePropertyName(String s) {
        StringBuilder sb = null;
        final int length = s.length();
        if (length == 0) {
            s = null;
        }
        else {
            StringBuilder sb2;
            for (int i = 0; i < length; ++i, sb = sb2) {
                final char char1 = s.charAt(i);
                final char lowerCase = Character.toLowerCase(char1);
                if (char1 == lowerCase) {
                    break;
                }
                if ((sb2 = sb) == null) {
                    sb2 = new StringBuilder(s);
                }
                sb2.setCharAt(i, lowerCase);
            }
            if (sb != null) {
                return sb.toString();
            }
        }
        return s;
    }
    
    public static String okNameForGetter(final AnnotatedMethod annotatedMethod) {
        final String name = annotatedMethod.getName();
        String s;
        if ((s = okNameForIsGetter(annotatedMethod, name)) == null) {
            s = okNameForRegularGetter(annotatedMethod, name);
        }
        return s;
    }
    
    public static String okNameForIsGetter(final AnnotatedMethod annotatedMethod, final String s) {
        if (s.startsWith("is")) {
            final Class<?> rawType = annotatedMethod.getRawType();
            if (rawType == Boolean.class || rawType == Boolean.TYPE) {
                return manglePropertyName(s.substring(2));
            }
        }
        return null;
    }
    
    public static String okNameForMutator(final AnnotatedMethod annotatedMethod, final String s) {
        final String name = annotatedMethod.getName();
        if (name.startsWith(s)) {
            return manglePropertyName(name.substring(s.length()));
        }
        return null;
    }
    
    public static String okNameForRegularGetter(final AnnotatedMethod annotatedMethod, final String s) {
        if (s.startsWith("get")) {
            if ("getCallbacks".equals(s)) {
                if (isCglibGetCallbacks(annotatedMethod)) {
                    return null;
                }
            }
            else if ("getMetaClass".equals(s) && isGroovyMetaClassGetter(annotatedMethod)) {
                return null;
            }
            return manglePropertyName(s.substring(3));
        }
        return null;
    }
}
