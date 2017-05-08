// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import java.util.UUID;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.Locale;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.Date;
import java.util.Calendar;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.Serializable;
import com.fasterxml.jackson.databind.KeyDeserializer;

public abstract class StdKeyDeserializer extends KeyDeserializer implements Serializable
{
    protected final Class<?> _keyClass;
    
    protected StdKeyDeserializer(final Class<?> keyClass) {
        this._keyClass = keyClass;
    }
    
    protected abstract Object _parse(final String p0, final DeserializationContext p1) throws Exception;
    
    protected double _parseDouble(final String s) throws IllegalArgumentException {
        return NumberInput.parseDouble(s);
    }
    
    protected int _parseInt(final String s) throws IllegalArgumentException {
        return Integer.parseInt(s);
    }
    
    protected long _parseLong(final String s) throws IllegalArgumentException {
        return Long.parseLong(s);
    }
    
    @Override
    public final Object deserializeKey(final String s, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (s != null) {
            try {
                final Object parse = this._parse(s, deserializationContext);
                if (parse != null) {
                    return parse;
                }
            }
            catch (Exception ex) {
                throw deserializationContext.weirdKeyException(this._keyClass, s, "not a valid representation: " + ex.getMessage());
            }
            if (!this._keyClass.isEnum() || !deserializationContext.getConfig().isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
                throw deserializationContext.weirdKeyException(this._keyClass, s, "not a valid representation");
            }
        }
        return null;
    }
    
    @JacksonStdImpl
    static final class BoolKD extends StdKeyDeserializer
    {
        BoolKD() {
            super(Boolean.class);
        }
        
        public Boolean _parse(final String s, final DeserializationContext deserializationContext) throws JsonMappingException {
            if ("true".equals(s)) {
                return Boolean.TRUE;
            }
            if ("false".equals(s)) {
                return Boolean.FALSE;
            }
            throw deserializationContext.weirdKeyException(this._keyClass, s, "value not 'true' or 'false'");
        }
    }
    
    @JacksonStdImpl
    static final class ByteKD extends StdKeyDeserializer
    {
        ByteKD() {
            super(Byte.class);
        }
        
        public Byte _parse(final String s, final DeserializationContext deserializationContext) throws JsonMappingException {
            final int parseInt = this._parseInt(s);
            if (parseInt < -128 || parseInt > 255) {
                throw deserializationContext.weirdKeyException(this._keyClass, s, "overflow, value can not be represented as 8-bit value");
            }
            return (byte)parseInt;
        }
    }
    
    @JacksonStdImpl
    static final class CalendarKD extends StdKeyDeserializer
    {
        protected CalendarKD() {
            super(Calendar.class);
        }
        
        public Object _parse(final String s, final DeserializationContext deserializationContext) throws IllegalArgumentException, JsonMappingException {
            final Date date = deserializationContext.parseDate(s);
            if (date == null) {
                return null;
            }
            return deserializationContext.constructCalendar(date);
        }
    }
    
    @JacksonStdImpl
    static final class CharKD extends StdKeyDeserializer
    {
        CharKD() {
            super(Character.class);
        }
        
        public Character _parse(final String s, final DeserializationContext deserializationContext) throws JsonMappingException {
            if (s.length() == 1) {
                return s.charAt(0);
            }
            throw deserializationContext.weirdKeyException(this._keyClass, s, "can only convert 1-character Strings");
        }
    }
    
    @JacksonStdImpl
    static final class DateKD extends StdKeyDeserializer
    {
        protected DateKD() {
            super(Date.class);
        }
        
        public Object _parse(final String s, final DeserializationContext deserializationContext) throws IllegalArgumentException, JsonMappingException {
            return deserializationContext.parseDate(s);
        }
    }
    
    static final class DelegatingKD extends KeyDeserializer implements Serializable
    {
        protected final JsonDeserializer<?> _delegate;
        protected final Class<?> _keyClass;
        
        protected DelegatingKD(final Class<?> keyClass, final JsonDeserializer<?> delegate) {
            this._keyClass = keyClass;
            this._delegate = delegate;
        }
        
        @Override
        public final Object deserializeKey(final String s, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            Object deserialize;
            if (s == null) {
                deserialize = null;
            }
            else {
                try {
                    if ((deserialize = this._delegate.deserialize(deserializationContext.getParser(), deserializationContext)) == null) {
                        throw deserializationContext.weirdKeyException(this._keyClass, s, "not a valid representation");
                    }
                }
                catch (Exception ex) {
                    throw deserializationContext.weirdKeyException(this._keyClass, s, "not a valid representation: " + ex.getMessage());
                }
            }
            return deserialize;
        }
    }
    
    @JacksonStdImpl
    static final class DoubleKD extends StdKeyDeserializer
    {
        DoubleKD() {
            super(Double.class);
        }
        
        public Double _parse(final String s, final DeserializationContext deserializationContext) throws JsonMappingException {
            return this._parseDouble(s);
        }
    }
    
    @JacksonStdImpl
    static final class EnumKD extends StdKeyDeserializer
    {
        protected final AnnotatedMethod _factory;
        protected final EnumResolver<?> _resolver;
        
        protected EnumKD(final EnumResolver<?> resolver, final AnnotatedMethod factory) {
            super(resolver.getEnumClass());
            this._resolver = resolver;
            this._factory = factory;
        }
        
        public Object _parse(final String s, final DeserializationContext deserializationContext) throws JsonMappingException {
            Label_0023: {
                if (this._factory == null) {
                    break Label_0023;
                }
                try {
                    return this._factory.call1(s);
                }
                catch (Exception ex) {
                    ClassUtil.unwrapAndThrowAsIAE(ex);
                }
            }
            final Object enum1 = this._resolver.findEnum(s);
            Object call1;
            if ((call1 = enum1) != null) {
                return call1;
            }
            call1 = enum1;
            if (!deserializationContext.getConfig().isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
                throw deserializationContext.weirdKeyException(this._keyClass, s, "not one of values for Enum class");
            }
            return call1;
        }
    }
    
    @JacksonStdImpl
    static final class FloatKD extends StdKeyDeserializer
    {
        FloatKD() {
            super(Float.class);
        }
        
        public Float _parse(final String s, final DeserializationContext deserializationContext) throws JsonMappingException {
            return (float)this._parseDouble(s);
        }
    }
    
    @JacksonStdImpl
    static final class IntKD extends StdKeyDeserializer
    {
        IntKD() {
            super(Integer.class);
        }
        
        public Integer _parse(final String s, final DeserializationContext deserializationContext) throws JsonMappingException {
            return this._parseInt(s);
        }
    }
    
    @JacksonStdImpl
    static final class LocaleKD extends StdKeyDeserializer
    {
        protected JdkDeserializers.LocaleDeserializer _localeDeserializer;
        
        LocaleKD() {
            super(Locale.class);
            this._localeDeserializer = new JdkDeserializers.LocaleDeserializer();
        }
        
        @Override
        protected Locale _parse(final String s, final DeserializationContext deserializationContext) throws JsonMappingException {
            try {
                return this._localeDeserializer._deserialize(s, deserializationContext);
            }
            catch (IOException ex) {
                throw deserializationContext.weirdKeyException(this._keyClass, s, "unable to parse key as locale");
            }
        }
    }
    
    @JacksonStdImpl
    static final class LongKD extends StdKeyDeserializer
    {
        LongKD() {
            super(Long.class);
        }
        
        public Long _parse(final String s, final DeserializationContext deserializationContext) throws JsonMappingException {
            return this._parseLong(s);
        }
    }
    
    @JacksonStdImpl
    static final class ShortKD extends StdKeyDeserializer
    {
        ShortKD() {
            super(Integer.class);
        }
        
        public Short _parse(final String s, final DeserializationContext deserializationContext) throws JsonMappingException {
            final int parseInt = this._parseInt(s);
            if (parseInt < -32768 || parseInt > 32767) {
                throw deserializationContext.weirdKeyException(this._keyClass, s, "overflow, value can not be represented as 16-bit value");
            }
            return (short)parseInt;
        }
    }
    
    static final class StringCtorKeyDeserializer extends StdKeyDeserializer
    {
        protected final Constructor<?> _ctor;
        
        public StringCtorKeyDeserializer(final Constructor<?> ctor) {
            super(ctor.getDeclaringClass());
            this._ctor = ctor;
        }
        
        public Object _parse(final String s, final DeserializationContext deserializationContext) throws Exception {
            return this._ctor.newInstance(s);
        }
    }
    
    static final class StringFactoryKeyDeserializer extends StdKeyDeserializer
    {
        final Method _factoryMethod;
        
        public StringFactoryKeyDeserializer(final Method factoryMethod) {
            super(factoryMethod.getDeclaringClass());
            this._factoryMethod = factoryMethod;
        }
        
        public Object _parse(final String s, final DeserializationContext deserializationContext) throws Exception {
            return this._factoryMethod.invoke(null, s);
        }
    }
    
    @JacksonStdImpl
    static final class StringKD extends StdKeyDeserializer
    {
        private static final StringKD sObject;
        private static final StringKD sString;
        
        static {
            sString = new StringKD(String.class);
            sObject = new StringKD(Object.class);
        }
        
        private StringKD(final Class<?> clazz) {
            super(clazz);
        }
        
        public static StringKD forType(final Class<?> clazz) {
            if (clazz == String.class) {
                return StringKD.sString;
            }
            if (clazz == Object.class) {
                return StringKD.sObject;
            }
            return new StringKD(clazz);
        }
        
        public String _parse(final String s, final DeserializationContext deserializationContext) throws JsonMappingException {
            return s;
        }
    }
    
    @JacksonStdImpl
    static final class UuidKD extends StdKeyDeserializer
    {
        protected UuidKD() {
            super(UUID.class);
        }
        
        public Object _parse(final String s, final DeserializationContext deserializationContext) throws IllegalArgumentException, JsonMappingException {
            return UUID.fromString(s);
        }
    }
}
