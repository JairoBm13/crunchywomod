// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.Locale;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import com.fasterxml.jackson.databind.BeanDescription;
import java.lang.reflect.Member;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import java.io.Serializable;
import com.fasterxml.jackson.databind.deser.KeyDeserializers;

public class StdKeyDeserializers implements KeyDeserializers, Serializable
{
    public static KeyDeserializer constructDelegatingKeyDeserializer(final DeserializationConfig deserializationConfig, final JavaType javaType, final JsonDeserializer<?> jsonDeserializer) {
        return new StdKeyDeserializer.DelegatingKD(javaType.getRawClass(), jsonDeserializer);
    }
    
    public static KeyDeserializer constructEnumKeyDeserializer(final EnumResolver<?> enumResolver) {
        return new StdKeyDeserializer.EnumKD(enumResolver, null);
    }
    
    public static KeyDeserializer constructEnumKeyDeserializer(final EnumResolver<?> enumResolver, final AnnotatedMethod annotatedMethod) {
        return new StdKeyDeserializer.EnumKD(enumResolver, annotatedMethod);
    }
    
    public static KeyDeserializer findStringBasedKeyDeserializer(final DeserializationConfig deserializationConfig, final JavaType javaType) {
        final BeanDescription introspect = deserializationConfig.introspect(javaType);
        final Constructor<?> singleArgConstructor = introspect.findSingleArgConstructor(String.class);
        if (singleArgConstructor != null) {
            if (deserializationConfig.canOverrideAccessModifiers()) {
                ClassUtil.checkAndFixAccess(singleArgConstructor);
            }
            return new StdKeyDeserializer.StringCtorKeyDeserializer(singleArgConstructor);
        }
        final Method factoryMethod = introspect.findFactoryMethod(String.class);
        if (factoryMethod != null) {
            if (deserializationConfig.canOverrideAccessModifiers()) {
                ClassUtil.checkAndFixAccess(factoryMethod);
            }
            return new StdKeyDeserializer.StringFactoryKeyDeserializer(factoryMethod);
        }
        return null;
    }
    
    @Override
    public KeyDeserializer findKeyDeserializer(final JavaType javaType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription) throws JsonMappingException {
        final Class<?> rawClass = javaType.getRawClass();
        if (rawClass == String.class || rawClass == Object.class) {
            return StdKeyDeserializer.StringKD.forType(rawClass);
        }
        if (rawClass == UUID.class) {
            return new StdKeyDeserializer.UuidKD();
        }
        Class<?> wrapperType = rawClass;
        if (rawClass.isPrimitive()) {
            wrapperType = ClassUtil.wrapperType(rawClass);
        }
        if (wrapperType == Integer.class) {
            return new StdKeyDeserializer.IntKD();
        }
        if (wrapperType == Long.class) {
            return new StdKeyDeserializer.LongKD();
        }
        if (wrapperType == Date.class) {
            return new StdKeyDeserializer.DateKD();
        }
        if (wrapperType == Calendar.class) {
            return new StdKeyDeserializer.CalendarKD();
        }
        if (wrapperType == Boolean.class) {
            return new StdKeyDeserializer.BoolKD();
        }
        if (wrapperType == Byte.class) {
            return new StdKeyDeserializer.ByteKD();
        }
        if (wrapperType == Character.class) {
            return new StdKeyDeserializer.CharKD();
        }
        if (wrapperType == Short.class) {
            return new StdKeyDeserializer.ShortKD();
        }
        if (wrapperType == Float.class) {
            return new StdKeyDeserializer.FloatKD();
        }
        if (wrapperType == Double.class) {
            return new StdKeyDeserializer.DoubleKD();
        }
        if (wrapperType == Locale.class) {
            return new StdKeyDeserializer.LocaleKD();
        }
        return null;
    }
}
