// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import java.math.BigInteger;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.math.BigDecimal;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.JsonSerializer;
import java.util.Map;

public class NumberSerializers
{
    public static void addAll(final Map<String, JsonSerializer<?>> map) {
        final IntegerSerializer integerSerializer = new IntegerSerializer();
        map.put(Integer.class.getName(), integerSerializer);
        map.put(Integer.TYPE.getName(), integerSerializer);
        map.put(Long.class.getName(), LongSerializer.instance);
        map.put(Long.TYPE.getName(), LongSerializer.instance);
        map.put(Byte.class.getName(), IntLikeSerializer.instance);
        map.put(Byte.TYPE.getName(), IntLikeSerializer.instance);
        map.put(Short.class.getName(), ShortSerializer.instance);
        map.put(Short.TYPE.getName(), ShortSerializer.instance);
        map.put(Float.class.getName(), FloatSerializer.instance);
        map.put(Float.TYPE.getName(), FloatSerializer.instance);
        map.put(Double.class.getName(), DoubleSerializer.instance);
        map.put(Double.TYPE.getName(), DoubleSerializer.instance);
    }
    
    @JacksonStdImpl
    public static final class DoubleSerializer extends NonTypedScalarSerializerBase<Double>
    {
        static final DoubleSerializer instance;
        
        static {
            instance = new DoubleSerializer();
        }
        
        public DoubleSerializer() {
            super(Double.class);
        }
        
        @Override
        public void serialize(final Double n, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            jsonGenerator.writeNumber(n);
        }
    }
    
    @JacksonStdImpl
    public static final class FloatSerializer extends StdScalarSerializer<Float>
    {
        static final FloatSerializer instance;
        
        static {
            instance = new FloatSerializer();
        }
        
        public FloatSerializer() {
            super(Float.class);
        }
        
        @Override
        public void serialize(final Float n, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            jsonGenerator.writeNumber(n);
        }
    }
    
    @JacksonStdImpl
    public static final class IntLikeSerializer extends StdScalarSerializer<Number>
    {
        static final IntLikeSerializer instance;
        
        static {
            instance = new IntLikeSerializer();
        }
        
        public IntLikeSerializer() {
            super(Number.class);
        }
        
        @Override
        public void serialize(final Number n, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            jsonGenerator.writeNumber(n.intValue());
        }
    }
    
    @JacksonStdImpl
    public static final class IntegerSerializer extends NonTypedScalarSerializerBase<Integer>
    {
        public IntegerSerializer() {
            super(Integer.class);
        }
        
        @Override
        public void serialize(final Integer n, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            jsonGenerator.writeNumber(n);
        }
    }
    
    @JacksonStdImpl
    public static final class LongSerializer extends StdScalarSerializer<Long>
    {
        static final LongSerializer instance;
        
        static {
            instance = new LongSerializer();
        }
        
        public LongSerializer() {
            super(Long.class);
        }
        
        @Override
        public void serialize(final Long n, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            jsonGenerator.writeNumber(n);
        }
    }
    
    @JacksonStdImpl
    public static final class NumberSerializer extends StdScalarSerializer<Number>
    {
        public static final NumberSerializer instance;
        
        static {
            instance = new NumberSerializer();
        }
        
        public NumberSerializer() {
            super(Number.class);
        }
        
        @Override
        public void serialize(final Number n, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            if (n instanceof BigDecimal) {
                if (serializerProvider.isEnabled(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN) && !(jsonGenerator instanceof TokenBuffer)) {
                    jsonGenerator.writeNumber(((BigDecimal)n).toPlainString());
                    return;
                }
                jsonGenerator.writeNumber((BigDecimal)n);
            }
            else {
                if (n instanceof BigInteger) {
                    jsonGenerator.writeNumber((BigInteger)n);
                    return;
                }
                if (n instanceof Integer) {
                    jsonGenerator.writeNumber(n.intValue());
                    return;
                }
                if (n instanceof Long) {
                    jsonGenerator.writeNumber(n.longValue());
                    return;
                }
                if (n instanceof Double) {
                    jsonGenerator.writeNumber(n.doubleValue());
                    return;
                }
                if (n instanceof Float) {
                    jsonGenerator.writeNumber(n.floatValue());
                    return;
                }
                if (n instanceof Byte || n instanceof Short) {
                    jsonGenerator.writeNumber(n.intValue());
                    return;
                }
                jsonGenerator.writeNumber(n.toString());
            }
        }
    }
    
    @JacksonStdImpl
    public static final class ShortSerializer extends StdScalarSerializer<Short>
    {
        static final ShortSerializer instance;
        
        static {
            instance = new ShortSerializer();
        }
        
        public ShortSerializer() {
            super(Short.class);
        }
        
        @Override
        public void serialize(final Short n, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            jsonGenerator.writeNumber(n);
        }
    }
}
