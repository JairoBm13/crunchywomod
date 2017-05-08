// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.HashSet;

public class NumberDeserializers
{
    private static final HashSet<String> _classNames;
    
    static {
        int i = 0;
        _classNames = new HashSet<String>();
        for (Class[] array = { Boolean.class, Byte.class, Short.class, Character.class, Integer.class, Long.class, Float.class, Double.class, Number.class, BigDecimal.class, BigInteger.class }; i < array.length; ++i) {
            NumberDeserializers._classNames.add(array[i].getName());
        }
    }
    
    public static JsonDeserializer<?> find(final Class<?> clazz, final String s) {
        if (clazz.isPrimitive()) {
            if (clazz == Integer.TYPE) {
                return IntegerDeserializer.primitiveInstance;
            }
            if (clazz == Boolean.TYPE) {
                return BooleanDeserializer.primitiveInstance;
            }
            if (clazz == Long.TYPE) {
                return LongDeserializer.primitiveInstance;
            }
            if (clazz == Double.TYPE) {
                return DoubleDeserializer.primitiveInstance;
            }
            if (clazz == Character.TYPE) {
                return CharacterDeserializer.primitiveInstance;
            }
            if (clazz == Byte.TYPE) {
                return ByteDeserializer.primitiveInstance;
            }
            if (clazz == Short.TYPE) {
                return ShortDeserializer.primitiveInstance;
            }
            if (clazz == Float.TYPE) {
                return FloatDeserializer.primitiveInstance;
            }
        }
        else {
            if (!NumberDeserializers._classNames.contains(s)) {
                return null;
            }
            if (clazz == Integer.class) {
                return IntegerDeserializer.wrapperInstance;
            }
            if (clazz == Boolean.class) {
                return BooleanDeserializer.wrapperInstance;
            }
            if (clazz == Long.class) {
                return LongDeserializer.wrapperInstance;
            }
            if (clazz == Double.class) {
                return DoubleDeserializer.wrapperInstance;
            }
            if (clazz == Character.class) {
                return CharacterDeserializer.wrapperInstance;
            }
            if (clazz == Byte.class) {
                return ByteDeserializer.wrapperInstance;
            }
            if (clazz == Short.class) {
                return ShortDeserializer.wrapperInstance;
            }
            if (clazz == Float.class) {
                return FloatDeserializer.wrapperInstance;
            }
            if (clazz == Number.class) {
                return NumberDeserializer.instance;
            }
            if (clazz == BigDecimal.class) {
                return BigDecimalDeserializer.instance;
            }
            if (clazz == BigInteger.class) {
                return BigIntegerDeserializer.instance;
            }
        }
        throw new IllegalArgumentException("Internal error: can't find deserializer for " + clazz.getName());
    }
    
    @JacksonStdImpl
    public static class BigDecimalDeserializer extends StdScalarDeserializer<BigDecimal>
    {
        public static final BigDecimalDeserializer instance;
        
        static {
            instance = new BigDecimalDeserializer();
        }
        
        public BigDecimalDeserializer() {
            super(BigDecimal.class);
        }
        
        @Override
        public BigDecimal deserialize(JsonParser trim, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            final JsonToken currentToken = trim.getCurrentToken();
            if (currentToken == JsonToken.VALUE_NUMBER_INT || currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
                return trim.getDecimalValue();
            }
            if (currentToken == JsonToken.VALUE_STRING) {
                trim = (JsonParser)trim.getText().trim();
                if (((String)trim).length() == 0) {
                    return null;
                }
                try {
                    return new BigDecimal((String)trim);
                }
                catch (IllegalArgumentException ex) {
                    throw deserializationContext.weirdStringException((String)trim, this._valueClass, "not a valid representation");
                }
            }
            throw deserializationContext.mappingException(this._valueClass, currentToken);
        }
    }
    
    @JacksonStdImpl
    public static class BigIntegerDeserializer extends StdScalarDeserializer<BigInteger>
    {
        public static final BigIntegerDeserializer instance;
        
        static {
            instance = new BigIntegerDeserializer();
        }
        
        public BigIntegerDeserializer() {
            super(BigInteger.class);
        }
        
        @Override
        public BigInteger deserialize(JsonParser trim, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            final JsonToken currentToken = trim.getCurrentToken();
            if (currentToken == JsonToken.VALUE_NUMBER_INT) {
                switch (trim.getNumberType()) {
                    case INT:
                    case LONG: {
                        return BigInteger.valueOf(trim.getLongValue());
                    }
                }
            }
            else {
                if (currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
                    return trim.getDecimalValue().toBigInteger();
                }
                if (currentToken != JsonToken.VALUE_STRING) {
                    throw deserializationContext.mappingException(this._valueClass, currentToken);
                }
            }
            trim = (JsonParser)trim.getText().trim();
            if (((String)trim).length() == 0) {
                return null;
            }
            try {
                return new BigInteger((String)trim);
            }
            catch (IllegalArgumentException ex) {
                throw deserializationContext.weirdStringException((String)trim, this._valueClass, "not a valid representation");
            }
        }
    }
    
    @JacksonStdImpl
    public static final class BooleanDeserializer extends PrimitiveOrWrapperDeserializer<Boolean>
    {
        private static final BooleanDeserializer primitiveInstance;
        private static final BooleanDeserializer wrapperInstance;
        
        static {
            primitiveInstance = new BooleanDeserializer(Boolean.class, Boolean.FALSE);
            wrapperInstance = new BooleanDeserializer(Boolean.TYPE, null);
        }
        
        public BooleanDeserializer(final Class<Boolean> clazz, final Boolean b) {
            super(clazz, b);
        }
        
        @Override
        public Boolean deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return this._parseBoolean(jsonParser, deserializationContext);
        }
        
        @Override
        public Boolean deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
            return this._parseBoolean(jsonParser, deserializationContext);
        }
    }
    
    @JacksonStdImpl
    public static final class ByteDeserializer extends PrimitiveOrWrapperDeserializer<Byte>
    {
        private static final ByteDeserializer primitiveInstance;
        private static final ByteDeserializer wrapperInstance;
        
        static {
            primitiveInstance = new ByteDeserializer(Byte.TYPE, Byte.valueOf((byte)0));
            wrapperInstance = new ByteDeserializer(Byte.class, null);
        }
        
        public ByteDeserializer(final Class<Byte> clazz, final Byte b) {
            super(clazz, b);
        }
        
        @Override
        public Byte deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return this._parseByte(jsonParser, deserializationContext);
        }
    }
    
    @JacksonStdImpl
    public static final class CharacterDeserializer extends PrimitiveOrWrapperDeserializer<Character>
    {
        private static final CharacterDeserializer primitiveInstance;
        private static final CharacterDeserializer wrapperInstance;
        
        static {
            primitiveInstance = new CharacterDeserializer(Character.class, Character.valueOf('\0'));
            wrapperInstance = new CharacterDeserializer(Character.TYPE, null);
        }
        
        public CharacterDeserializer(final Class<Character> clazz, final Character c) {
            super(clazz, c);
        }
        
        @Override
        public Character deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            final JsonToken currentToken = jsonParser.getCurrentToken();
            if (currentToken == JsonToken.VALUE_NUMBER_INT) {
                final int intValue = jsonParser.getIntValue();
                if (intValue >= 0 && intValue <= 65535) {
                    return (char)intValue;
                }
            }
            else if (currentToken == JsonToken.VALUE_STRING) {
                final String text = jsonParser.getText();
                if (text.length() == 1) {
                    return text.charAt(0);
                }
                if (text.length() == 0) {
                    return this.getEmptyValue();
                }
            }
            throw deserializationContext.mappingException(this._valueClass, currentToken);
        }
    }
    
    @JacksonStdImpl
    public static final class DoubleDeserializer extends PrimitiveOrWrapperDeserializer<Double>
    {
        private static final DoubleDeserializer primitiveInstance;
        private static final DoubleDeserializer wrapperInstance;
        
        static {
            primitiveInstance = new DoubleDeserializer(Double.class, Double.valueOf(0.0));
            wrapperInstance = new DoubleDeserializer(Double.TYPE, null);
        }
        
        public DoubleDeserializer(final Class<Double> clazz, final Double n) {
            super(clazz, n);
        }
        
        @Override
        public Double deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return this._parseDouble(jsonParser, deserializationContext);
        }
        
        @Override
        public Double deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
            return this._parseDouble(jsonParser, deserializationContext);
        }
    }
    
    @JacksonStdImpl
    public static final class FloatDeserializer extends PrimitiveOrWrapperDeserializer<Float>
    {
        private static final FloatDeserializer primitiveInstance;
        private static final FloatDeserializer wrapperInstance;
        
        static {
            primitiveInstance = new FloatDeserializer(Float.class, Float.valueOf(0.0f));
            wrapperInstance = new FloatDeserializer(Float.TYPE, null);
        }
        
        public FloatDeserializer(final Class<Float> clazz, final Float n) {
            super(clazz, n);
        }
        
        @Override
        public Float deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return this._parseFloat(jsonParser, deserializationContext);
        }
    }
    
    @JacksonStdImpl
    public static final class IntegerDeserializer extends PrimitiveOrWrapperDeserializer<Integer>
    {
        private static final IntegerDeserializer primitiveInstance;
        private static final IntegerDeserializer wrapperInstance;
        
        static {
            primitiveInstance = new IntegerDeserializer(Integer.class, Integer.valueOf(0));
            wrapperInstance = new IntegerDeserializer(Integer.TYPE, null);
        }
        
        public IntegerDeserializer(final Class<Integer> clazz, final Integer n) {
            super(clazz, n);
        }
        
        @Override
        public Integer deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return this._parseInteger(jsonParser, deserializationContext);
        }
        
        @Override
        public Integer deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
            return this._parseInteger(jsonParser, deserializationContext);
        }
    }
    
    @JacksonStdImpl
    public static final class LongDeserializer extends PrimitiveOrWrapperDeserializer<Long>
    {
        private static final LongDeserializer primitiveInstance;
        private static final LongDeserializer wrapperInstance;
        
        static {
            primitiveInstance = new LongDeserializer(Long.class, Long.valueOf(0L));
            wrapperInstance = new LongDeserializer(Long.TYPE, null);
        }
        
        public LongDeserializer(final Class<Long> clazz, final Long n) {
            super(clazz, n);
        }
        
        @Override
        public Long deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return this._parseLong(jsonParser, deserializationContext);
        }
    }
    
    @JacksonStdImpl
    public static final class NumberDeserializer extends StdScalarDeserializer<Number>
    {
        public static final NumberDeserializer instance;
        
        static {
            instance = new NumberDeserializer();
        }
        
        public NumberDeserializer() {
            super(Number.class);
        }
        
        @Override
        public Number deserialize(JsonParser trim, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            final JsonToken currentToken = trim.getCurrentToken();
            if (currentToken == JsonToken.VALUE_NUMBER_INT) {
                if (deserializationContext.isEnabled(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)) {
                    return trim.getBigIntegerValue();
                }
                return trim.getNumberValue();
            }
            else if (currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
                if (deserializationContext.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
                    return trim.getDecimalValue();
                }
                return trim.getDoubleValue();
            }
            else {
                if (currentToken != JsonToken.VALUE_STRING) {
                    throw deserializationContext.mappingException(this._valueClass, currentToken);
                }
                trim = (JsonParser)trim.getText().trim();
                Label_0136: {
                    try {
                        if (((String)trim).indexOf(46) < 0) {
                            break Label_0136;
                        }
                        if (deserializationContext.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
                            return new BigDecimal((String)trim);
                        }
                    }
                    catch (IllegalArgumentException ex) {
                        throw deserializationContext.weirdStringException((String)trim, this._valueClass, "not a valid number");
                    }
                    return new Double((String)trim);
                }
                if (deserializationContext.isEnabled(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)) {
                    return new BigInteger((String)trim);
                }
                final long long1 = Long.parseLong((String)trim);
                if (long1 <= 2147483647L && long1 >= -2147483648L) {
                    return (int)long1;
                }
                return long1;
            }
        }
        
        @Override
        public Object deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
            switch (jsonParser.getCurrentToken()) {
                default: {
                    return typeDeserializer.deserializeTypedFromScalar(jsonParser, deserializationContext);
                }
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT:
                case VALUE_STRING: {
                    return this.deserialize(jsonParser, deserializationContext);
                }
            }
        }
    }
    
    protected abstract static class PrimitiveOrWrapperDeserializer<T> extends StdScalarDeserializer<T>
    {
        protected final T _nullValue;
        
        protected PrimitiveOrWrapperDeserializer(final Class<T> clazz, final T nullValue) {
            super(clazz);
            this._nullValue = nullValue;
        }
        
        @Override
        public final T getNullValue() {
            return this._nullValue;
        }
    }
    
    @JacksonStdImpl
    public static final class ShortDeserializer extends PrimitiveOrWrapperDeserializer<Short>
    {
        private static final ShortDeserializer primitiveInstance;
        private static final ShortDeserializer wrapperInstance;
        
        static {
            primitiveInstance = new ShortDeserializer(Short.class, Short.valueOf((short)0));
            wrapperInstance = new ShortDeserializer(Short.TYPE, null);
        }
        
        public ShortDeserializer(final Class<Short> clazz, final Short n) {
            super(clazz, n);
        }
        
        @Override
        public Short deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return this._parseShort(jsonParser, deserializationContext);
        }
    }
}
