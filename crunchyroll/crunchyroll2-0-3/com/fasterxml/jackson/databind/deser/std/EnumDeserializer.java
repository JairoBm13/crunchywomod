// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import java.io.Serializable;
import java.lang.reflect.Method;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import java.lang.reflect.Member;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class EnumDeserializer extends StdScalarDeserializer<Enum<?>>
{
    protected final EnumResolver<?> _resolver;
    
    public EnumDeserializer(final EnumResolver<?> resolver) {
        super(Enum.class);
        this._resolver = resolver;
    }
    
    public static JsonDeserializer<?> deserializerForCreator(final DeserializationConfig deserializationConfig, final Class<?> clazz, final AnnotatedMethod annotatedMethod) {
        final Class<?> rawParameterType = annotatedMethod.getRawParameterType(0);
        Class<?> clazz2;
        if (rawParameterType == String.class) {
            clazz2 = null;
        }
        else if (rawParameterType == Integer.TYPE || rawParameterType == Integer.class) {
            clazz2 = Integer.class;
        }
        else {
            if (rawParameterType != Long.TYPE && rawParameterType != Long.class) {
                throw new IllegalArgumentException("Parameter #0 type for factory method (" + annotatedMethod + ") not suitable, must be java.lang.String or int/Integer/long/Long");
            }
            clazz2 = Long.class;
        }
        if (deserializationConfig.canOverrideAccessModifiers()) {
            ClassUtil.checkAndFixAccess(annotatedMethod.getMember());
        }
        return new FactoryBasedDeserializer(clazz, annotatedMethod, clazz2);
    }
    
    @Override
    public Enum<?> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonToken currentToken = jsonParser.getCurrentToken();
        Object o;
        if (currentToken == JsonToken.VALUE_STRING || currentToken == JsonToken.FIELD_NAME) {
            final String text = jsonParser.getText();
            final Object enum1 = this._resolver.findEnum(text);
            if ((o = enum1) == null) {
                if (deserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT) && (text.length() == 0 || text.trim().length() == 0)) {
                    o = null;
                }
                else {
                    o = enum1;
                    if (!deserializationContext.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
                        throw deserializationContext.weirdStringException(text, this._resolver.getEnumClass(), "value not one of declared Enum instance names");
                    }
                }
            }
        }
        else {
            if (currentToken != JsonToken.VALUE_NUMBER_INT) {
                throw deserializationContext.mappingException(this._resolver.getEnumClass());
            }
            if (deserializationContext.isEnabled(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)) {
                throw deserializationContext.mappingException("Not allowed to deserialize Enum value out of JSON number (disable DeserializationConfig.DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS to allow)");
            }
            final int intValue = jsonParser.getIntValue();
            final Object enum2 = this._resolver.getEnum(intValue);
            if ((o = enum2) == null) {
                o = enum2;
                if (!deserializationContext.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
                    throw deserializationContext.weirdNumberException(intValue, this._resolver.getEnumClass(), "index value outside legal index range [0.." + this._resolver.lastValidIndex() + "]");
                }
            }
        }
        return (Enum<?>)o;
    }
    
    @Override
    public boolean isCachable() {
        return true;
    }
    
    protected static class FactoryBasedDeserializer extends StdScalarDeserializer<Object>
    {
        protected final Class<?> _enumClass;
        protected final Method _factory;
        protected final Class<?> _inputType;
        
        public FactoryBasedDeserializer(final Class<?> enumClass, final AnnotatedMethod annotatedMethod, final Class<?> inputType) {
            super(Enum.class);
            this._enumClass = enumClass;
            this._factory = annotatedMethod.getAnnotated();
            this._inputType = inputType;
        }
        
        @Override
        public Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            Label_0034: {
                if (this._inputType != null) {
                    break Label_0034;
                }
                Serializable s = jsonParser.getText();
                try {
                    return this._factory.invoke(this._enumClass, s);
                    // iftrue(Label_0074:, this._inputType != Long.class)
                    while (true) {
                        while (true) {
                            s = jsonParser.getValueAsLong();
                            return this._factory.invoke(this._enumClass, s);
                            s = jsonParser.getValueAsInt();
                            return this._factory.invoke(this._enumClass, s);
                            Label_0054: {
                                continue;
                            }
                        }
                        Label_0074: {
                            throw deserializationContext.mappingException(this._enumClass);
                        }
                        continue;
                    }
                }
                // iftrue(Label_0054:, this._inputType != Integer.class)
                catch (Exception ex) {
                    ClassUtil.unwrapAndThrowAsIAE(ex);
                    return null;
                }
            }
        }
    }
}
