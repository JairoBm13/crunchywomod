// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.util.Date;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import java.io.Serializable;
import com.fasterxml.jackson.databind.JsonDeserializer;

public abstract class StdDeserializer<T> extends JsonDeserializer<T> implements Serializable
{
    protected final Class<?> _valueClass;
    
    protected StdDeserializer(final JavaType javaType) {
        Class<?> rawClass;
        if (javaType == null) {
            rawClass = null;
        }
        else {
            rawClass = javaType.getRawClass();
        }
        this._valueClass = rawClass;
    }
    
    protected StdDeserializer(final Class<?> valueClass) {
        this._valueClass = valueClass;
    }
    
    protected static final double parseDouble(final String s) throws NumberFormatException {
        if ("2.2250738585072012e-308".equals(s)) {
            return Double.MIN_VALUE;
        }
        return Double.parseDouble(s);
    }
    
    protected final Boolean _parseBoolean(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken == JsonToken.VALUE_TRUE) {
            return Boolean.TRUE;
        }
        if (currentToken == JsonToken.VALUE_FALSE) {
            return Boolean.FALSE;
        }
        if (currentToken == JsonToken.VALUE_NUMBER_INT) {
            if (jsonParser.getNumberType() != JsonParser.NumberType.INT) {
                return this._parseBooleanFromNumber(jsonParser, deserializationContext);
            }
            if (jsonParser.getIntValue() == 0) {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
        else {
            if (currentToken == JsonToken.VALUE_NULL) {
                return this.getNullValue();
            }
            if (currentToken != JsonToken.VALUE_STRING) {
                throw deserializationContext.mappingException(this._valueClass, currentToken);
            }
            final String trim = jsonParser.getText().trim();
            if ("true".equals(trim)) {
                return Boolean.TRUE;
            }
            if ("false".equals(trim)) {
                return Boolean.FALSE;
            }
            if (trim.length() == 0) {
                return this.getEmptyValue();
            }
            throw deserializationContext.weirdStringException(trim, this._valueClass, "only \"true\" or \"false\" recognized");
        }
    }
    
    protected final boolean _parseBooleanFromNumber(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (jsonParser.getNumberType() == JsonParser.NumberType.LONG) {
            Boolean b;
            if (jsonParser.getLongValue() == 0L) {
                b = Boolean.FALSE;
            }
            else {
                b = Boolean.TRUE;
            }
            return b;
        }
        final String text = jsonParser.getText();
        if ("0.0".equals(text) || "0".equals(text)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
    protected final boolean _parseBooleanPrimitive(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken != JsonToken.VALUE_TRUE) {
            if (currentToken == JsonToken.VALUE_FALSE) {
                return false;
            }
            if (currentToken == JsonToken.VALUE_NULL) {
                return false;
            }
            if (currentToken == JsonToken.VALUE_NUMBER_INT) {
                if (jsonParser.getNumberType() != JsonParser.NumberType.INT) {
                    return this._parseBooleanFromNumber(jsonParser, deserializationContext);
                }
                if (jsonParser.getIntValue() == 0) {
                    return false;
                }
            }
            else {
                if (currentToken != JsonToken.VALUE_STRING) {
                    throw deserializationContext.mappingException(this._valueClass, currentToken);
                }
                final String trim = jsonParser.getText().trim();
                if (!"true".equals(trim)) {
                    if ("false".equals(trim) || trim.length() == 0) {
                        return Boolean.FALSE;
                    }
                    throw deserializationContext.weirdStringException(trim, this._valueClass, "only \"true\" or \"false\" recognized");
                }
            }
        }
        return true;
    }
    
    protected Byte _parseByte(JsonParser trim, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonToken currentToken = trim.getCurrentToken();
        if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
            return trim.getByteValue();
        }
        if (currentToken == JsonToken.VALUE_STRING) {
            trim = (JsonParser)trim.getText().trim();
            int int1;
            try {
                if (((String)trim).length() == 0) {
                    return this.getEmptyValue();
                }
                int1 = NumberInput.parseInt((String)trim);
                if (int1 < -128 || int1 > 255) {
                    throw deserializationContext.weirdStringException((String)trim, this._valueClass, "overflow, value can not be represented as 8-bit value");
                }
            }
            catch (IllegalArgumentException ex) {
                throw deserializationContext.weirdStringException((String)trim, this._valueClass, "not a valid Byte value");
            }
            return (byte)int1;
        }
        if (currentToken == JsonToken.VALUE_NULL) {
            return this.getNullValue();
        }
        throw deserializationContext.mappingException(this._valueClass, currentToken);
    }
    
    protected Date _parseDate(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken == JsonToken.VALUE_NUMBER_INT) {
            return new Date(jsonParser.getLongValue());
        }
        if (currentToken == JsonToken.VALUE_NULL) {
            return this.getNullValue();
        }
        if (currentToken == JsonToken.VALUE_STRING) {
            String trim = null;
            try {
                final String s = trim = jsonParser.getText().trim();
                if (s.length() == 0) {
                    trim = s;
                    return this.getEmptyValue();
                }
                trim = s;
                return deserializationContext.parseDate(s);
            }
            catch (IllegalArgumentException ex) {
                throw deserializationContext.weirdStringException(trim, this._valueClass, "not a valid representation (error: " + ex.getMessage() + ")");
            }
        }
        throw deserializationContext.mappingException(this._valueClass, currentToken);
    }
    
    protected final Double _parseDouble(JsonParser trim, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonToken currentToken = trim.getCurrentToken();
        if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
            return trim.getDoubleValue();
        }
        if (currentToken == JsonToken.VALUE_STRING) {
            trim = (JsonParser)trim.getText().trim();
            if (((String)trim).length() == 0) {
                return this.getEmptyValue();
            }
            Label_0135: {
                switch (((String)trim).charAt(0)) {
                    default: {
                        break Label_0135;
                    }
                    case 'I': {
                        break Label_0135;
                    }
                    case 'N': {
                        break Label_0135;
                    }
                    case '-': {
                        Label_0151: {
                            break Label_0151;
                            try {
                                return parseDouble((String)trim);
                                // iftrue(Label_0100:, !"Infinity".equals((Object)trim) && !"INF".equals((Object)trim))
                                return Double.POSITIVE_INFINITY;
                                // iftrue(Label_0100:, !"NaN".equals((Object)trim))
                                return Double.NaN;
                                // iftrue(Label_0100:, !"-Infinity".equals((Object)trim) && !"-INF".equals((Object)trim))
                                return Double.NEGATIVE_INFINITY;
                            }
                            catch (IllegalArgumentException ex) {
                                throw deserializationContext.weirdStringException((String)trim, this._valueClass, "not a valid Double value");
                            }
                        }
                        break;
                    }
                }
            }
        }
        if (currentToken == JsonToken.VALUE_NULL) {
            return this.getNullValue();
        }
        throw deserializationContext.mappingException(this._valueClass, currentToken);
    }
    
    protected final double _parseDoublePrimitive(JsonParser trim, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        double doubleValue = 0.0;
        final JsonToken currentToken = trim.getCurrentToken();
        if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
            doubleValue = trim.getDoubleValue();
        }
        else {
            if (currentToken == JsonToken.VALUE_STRING) {
                trim = (JsonParser)trim.getText().trim();
                if (((String)trim).length() == 0) {
                    return doubleValue;
                }
                Label_0121: {
                    switch (((String)trim).charAt(0)) {
                        default: {
                            break Label_0121;
                        }
                        case 'I': {
                            break Label_0121;
                        }
                        case 'N': {
                            break Label_0121;
                        }
                        case '-': {
                            Label_0134: {
                                break Label_0134;
                                try {
                                    return parseDouble((String)trim);
                                    // iftrue(Label_0092:, !"-Infinity".equals((Object)trim) && !"-INF".equals((Object)trim))
                                    return Double.NEGATIVE_INFINITY;
                                    // iftrue(Label_0092:, !"Infinity".equals((Object)trim) && !"INF".equals((Object)trim))
                                    return Double.POSITIVE_INFINITY;
                                    // iftrue(Label_0092:, !"NaN".equals((Object)trim))
                                    return Double.NaN;
                                }
                                catch (IllegalArgumentException ex) {
                                    throw deserializationContext.weirdStringException((String)trim, this._valueClass, "not a valid double value");
                                }
                            }
                            break;
                        }
                    }
                }
            }
            if (currentToken != JsonToken.VALUE_NULL) {
                throw deserializationContext.mappingException(this._valueClass, currentToken);
            }
        }
        return doubleValue;
    }
    
    protected final Float _parseFloat(JsonParser trim, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonToken currentToken = trim.getCurrentToken();
        if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
            return trim.getFloatValue();
        }
        if (currentToken == JsonToken.VALUE_STRING) {
            trim = (JsonParser)trim.getText().trim();
            if (((String)trim).length() == 0) {
                return this.getEmptyValue();
            }
            Label_0135: {
                switch (((String)trim).charAt(0)) {
                    default: {
                        break Label_0135;
                    }
                    case 'I': {
                        break Label_0135;
                    }
                    case 'N': {
                        break Label_0135;
                    }
                    case '-': {
                        Label_0151: {
                            break Label_0151;
                            try {
                                return Float.parseFloat((String)trim);
                                // iftrue(Label_0100:, !"-Infinity".equals((Object)trim) && !"-INF".equals((Object)trim))
                                return Float.NEGATIVE_INFINITY;
                                // iftrue(Label_0100:, !"NaN".equals((Object)trim))
                                return Float.NaN;
                                // iftrue(Label_0100:, !"Infinity".equals((Object)trim) && !"INF".equals((Object)trim))
                                return Float.POSITIVE_INFINITY;
                            }
                            catch (IllegalArgumentException ex) {
                                throw deserializationContext.weirdStringException((String)trim, this._valueClass, "not a valid Float value");
                            }
                        }
                        break;
                    }
                }
            }
        }
        if (currentToken == JsonToken.VALUE_NULL) {
            return this.getNullValue();
        }
        throw deserializationContext.mappingException(this._valueClass, currentToken);
    }
    
    protected final float _parseFloatPrimitive(JsonParser trim, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        float floatValue = 0.0f;
        final JsonToken currentToken = trim.getCurrentToken();
        if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
            floatValue = trim.getFloatValue();
        }
        else {
            if (currentToken == JsonToken.VALUE_STRING) {
                trim = (JsonParser)trim.getText().trim();
                if (((String)trim).length() == 0) {
                    return floatValue;
                }
                Label_0121: {
                    switch (((String)trim).charAt(0)) {
                        default: {
                            break Label_0121;
                        }
                        case 'I': {
                            break Label_0121;
                        }
                        case 'N': {
                            break Label_0121;
                        }
                        case '-': {
                            Label_0134: {
                                break Label_0134;
                                try {
                                    return Float.parseFloat((String)trim);
                                    // iftrue(Label_0092:, !"Infinity".equals((Object)trim) && !"INF".equals((Object)trim))
                                    return Float.POSITIVE_INFINITY;
                                    // iftrue(Label_0092:, !"NaN".equals((Object)trim))
                                    return Float.NaN;
                                    // iftrue(Label_0092:, !"-Infinity".equals((Object)trim) && !"-INF".equals((Object)trim))
                                    return Float.NEGATIVE_INFINITY;
                                }
                                catch (IllegalArgumentException ex) {
                                    throw deserializationContext.weirdStringException((String)trim, this._valueClass, "not a valid float value");
                                }
                            }
                            break;
                        }
                    }
                }
            }
            if (currentToken != JsonToken.VALUE_NULL) {
                throw deserializationContext.mappingException(this._valueClass, currentToken);
            }
        }
        return floatValue;
    }
    
    protected final int _parseIntPrimitive(JsonParser trim, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        int intValue = 0;
        final JsonToken currentToken = trim.getCurrentToken();
        if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
            intValue = trim.getIntValue();
        }
        else if (currentToken == JsonToken.VALUE_STRING) {
            trim = (JsonParser)trim.getText().trim();
            int length = 0;
            Label_0162: {
                long long1;
                try {
                    length = ((String)trim).length();
                    if (length <= 9) {
                        break Label_0162;
                    }
                    long1 = Long.parseLong((String)trim);
                    if (long1 < -2147483648L || long1 > 2147483647L) {
                        throw deserializationContext.weirdStringException((String)trim, this._valueClass, "Overflow: numeric value (" + (String)trim + ") out of range of int (" + Integer.MIN_VALUE + " - " + Integer.MAX_VALUE + ")");
                    }
                }
                catch (IllegalArgumentException ex) {
                    throw deserializationContext.weirdStringException((String)trim, this._valueClass, "not a valid int value");
                }
                return (int)long1;
            }
            if (length != 0) {
                return NumberInput.parseInt((String)trim);
            }
        }
        else if (currentToken != JsonToken.VALUE_NULL) {
            throw deserializationContext.mappingException(this._valueClass, currentToken);
        }
        return intValue;
    }
    
    protected final Integer _parseInteger(JsonParser trim, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonToken currentToken = trim.getCurrentToken();
        if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
            return trim.getIntValue();
        }
        if (currentToken == JsonToken.VALUE_STRING) {
            trim = (JsonParser)trim.getText().trim();
            int length = 0;
            Label_0164: {
                long long1;
                try {
                    length = ((String)trim).length();
                    if (length <= 9) {
                        break Label_0164;
                    }
                    long1 = Long.parseLong((String)trim);
                    if (long1 < -2147483648L || long1 > 2147483647L) {
                        throw deserializationContext.weirdStringException((String)trim, this._valueClass, "Overflow: numeric value (" + (String)trim + ") out of range of Integer (" + Integer.MIN_VALUE + " - " + Integer.MAX_VALUE + ")");
                    }
                }
                catch (IllegalArgumentException ex) {
                    throw deserializationContext.weirdStringException((String)trim, this._valueClass, "not a valid Integer value");
                }
                return (int)long1;
            }
            if (length == 0) {
                return this.getEmptyValue();
            }
            return NumberInput.parseInt((String)trim);
        }
        else {
            if (currentToken == JsonToken.VALUE_NULL) {
                return this.getNullValue();
            }
            throw deserializationContext.mappingException(this._valueClass, currentToken);
        }
    }
    
    protected final Long _parseLong(JsonParser trim, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonToken currentToken = trim.getCurrentToken();
        if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
            return trim.getLongValue();
        }
        if (currentToken == JsonToken.VALUE_STRING) {
            trim = (JsonParser)trim.getText().trim();
            if (((String)trim).length() == 0) {
                return this.getEmptyValue();
            }
            try {
                return NumberInput.parseLong((String)trim);
            }
            catch (IllegalArgumentException ex) {
                throw deserializationContext.weirdStringException((String)trim, this._valueClass, "not a valid Long value");
            }
        }
        if (currentToken == JsonToken.VALUE_NULL) {
            return this.getNullValue();
        }
        throw deserializationContext.mappingException(this._valueClass, currentToken);
    }
    
    protected final long _parseLongPrimitive(JsonParser trim, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        long longValue = 0L;
        final JsonToken currentToken = trim.getCurrentToken();
        if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
            longValue = trim.getLongValue();
        }
        else {
            if (currentToken == JsonToken.VALUE_STRING) {
                trim = (JsonParser)trim.getText().trim();
                if (((String)trim).length() == 0) {
                    return longValue;
                }
                try {
                    return NumberInput.parseLong((String)trim);
                }
                catch (IllegalArgumentException ex) {
                    throw deserializationContext.weirdStringException((String)trim, this._valueClass, "not a valid long value");
                }
            }
            if (currentToken != JsonToken.VALUE_NULL) {
                throw deserializationContext.mappingException(this._valueClass, currentToken);
            }
        }
        return longValue;
    }
    
    protected Short _parseShort(JsonParser trim, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonToken currentToken = trim.getCurrentToken();
        if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
            return trim.getShortValue();
        }
        if (currentToken == JsonToken.VALUE_STRING) {
            trim = (JsonParser)trim.getText().trim();
            int int1;
            try {
                if (((String)trim).length() == 0) {
                    return this.getEmptyValue();
                }
                int1 = NumberInput.parseInt((String)trim);
                if (int1 < -32768 || int1 > 32767) {
                    throw deserializationContext.weirdStringException((String)trim, this._valueClass, "overflow, value can not be represented as 16-bit value");
                }
            }
            catch (IllegalArgumentException ex) {
                throw deserializationContext.weirdStringException((String)trim, this._valueClass, "not a valid Short value");
            }
            return (short)int1;
        }
        if (currentToken == JsonToken.VALUE_NULL) {
            return this.getNullValue();
        }
        throw deserializationContext.mappingException(this._valueClass, currentToken);
    }
    
    protected final short _parseShortPrimitive(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final int parseIntPrimitive = this._parseIntPrimitive(jsonParser, deserializationContext);
        if (parseIntPrimitive < -32768 || parseIntPrimitive > 32767) {
            throw deserializationContext.weirdStringException(String.valueOf(parseIntPrimitive), this._valueClass, "overflow, value can not be represented as 16-bit value");
        }
        return (short)parseIntPrimitive;
    }
    
    protected final String _parseString(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final String valueAsString = jsonParser.getValueAsString();
        if (valueAsString != null) {
            return valueAsString;
        }
        throw deserializationContext.mappingException(String.class, jsonParser.getCurrentToken());
    }
    
    @Override
    public Object deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
    }
    
    protected JsonDeserializer<?> findConvertingContentDeserializer(final DeserializationContext deserializationContext, final BeanProperty beanProperty, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        final AnnotationIntrospector annotationIntrospector = deserializationContext.getAnnotationIntrospector();
        JsonDeserializer<?> jsonDeserializer2 = jsonDeserializer;
        if (annotationIntrospector != null) {
            jsonDeserializer2 = jsonDeserializer;
            if (beanProperty != null) {
                final Object deserializationContentConverter = annotationIntrospector.findDeserializationContentConverter(beanProperty.getMember());
                jsonDeserializer2 = jsonDeserializer;
                if (deserializationContentConverter != null) {
                    final Converter<Object, Object> converterInstance = deserializationContext.converterInstance(beanProperty.getMember(), deserializationContentConverter);
                    final JavaType inputType = converterInstance.getInputType(deserializationContext.getTypeFactory());
                    JsonDeserializer<?> contextualValueDeserializer;
                    if ((contextualValueDeserializer = jsonDeserializer) == null) {
                        contextualValueDeserializer = deserializationContext.findContextualValueDeserializer(inputType, beanProperty);
                    }
                    jsonDeserializer2 = new StdDelegatingDeserializer<Object>(converterInstance, inputType, contextualValueDeserializer);
                }
            }
        }
        return jsonDeserializer2;
    }
    
    protected JsonDeserializer<Object> findDeserializer(final DeserializationContext deserializationContext, final JavaType javaType, final BeanProperty beanProperty) throws JsonMappingException {
        return deserializationContext.findContextualValueDeserializer(javaType, beanProperty);
    }
    
    public Class<?> getValueClass() {
        return this._valueClass;
    }
    
    protected void handleUnknownProperty(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o, final String s) throws IOException, JsonProcessingException {
        Object valueClass = o;
        if (o == null) {
            valueClass = this.getValueClass();
        }
        if (deserializationContext.handleUnknownProperty(jsonParser, this, valueClass, s)) {
            return;
        }
        deserializationContext.reportUnknownProperty(valueClass, s, this);
        jsonParser.skipChildren();
    }
    
    protected boolean isDefaultDeserializer(final JsonDeserializer<?> jsonDeserializer) {
        return jsonDeserializer != null && jsonDeserializer.getClass().getAnnotation(JacksonStdImpl.class) != null;
    }
    
    protected boolean isDefaultKeyDeserializer(final KeyDeserializer keyDeserializer) {
        return keyDeserializer != null && keyDeserializer.getClass().getAnnotation(JacksonStdImpl.class) != null;
    }
}
