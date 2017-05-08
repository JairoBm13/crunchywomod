// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.JsonSerializer;
import java.util.HashMap;

public class StdArraySerializers
{
    protected static final HashMap<String, JsonSerializer<?>> _arraySerializers;
    
    static {
        (_arraySerializers = new HashMap<String, JsonSerializer<?>>()).put(boolean[].class.getName(), new BooleanArraySerializer());
        StdArraySerializers._arraySerializers.put(byte[].class.getName(), new ByteArraySerializer());
        StdArraySerializers._arraySerializers.put(char[].class.getName(), new CharArraySerializer());
        StdArraySerializers._arraySerializers.put(short[].class.getName(), new ShortArraySerializer());
        StdArraySerializers._arraySerializers.put(int[].class.getName(), new IntArraySerializer());
        StdArraySerializers._arraySerializers.put(long[].class.getName(), new LongArraySerializer());
        StdArraySerializers._arraySerializers.put(float[].class.getName(), new FloatArraySerializer());
        StdArraySerializers._arraySerializers.put(double[].class.getName(), new DoubleArraySerializer());
    }
    
    public static JsonSerializer<?> findStandardImpl(final Class<?> clazz) {
        return StdArraySerializers._arraySerializers.get(clazz.getName());
    }
    
    @JacksonStdImpl
    public static final class BooleanArraySerializer extends ArraySerializerBase<boolean[]>
    {
        private static final JavaType VALUE_TYPE;
        
        static {
            VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Boolean.class);
        }
        
        public BooleanArraySerializer() {
            super(boolean[].class, null);
        }
        
        public ContainerSerializer<?> _withValueTypeSerializer(final TypeSerializer typeSerializer) {
            return this;
        }
        
        @Override
        public boolean hasSingleElement(final boolean[] array) {
            return array.length == 1;
        }
        
        @Override
        public boolean isEmpty(final boolean[] array) {
            return array == null || array.length == 0;
        }
        
        public void serializeContents(final boolean[] array, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            for (int i = 0; i < array.length; ++i) {
                jsonGenerator.writeBoolean(array[i]);
            }
        }
    }
    
    @JacksonStdImpl
    public static final class ByteArraySerializer extends StdSerializer<byte[]>
    {
        public ByteArraySerializer() {
            super(byte[].class);
        }
        
        @Override
        public boolean isEmpty(final byte[] array) {
            return array == null || array.length == 0;
        }
        
        @Override
        public void serialize(final byte[] array, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            jsonGenerator.writeBinary(serializerProvider.getConfig().getBase64Variant(), array, 0, array.length);
        }
        
        @Override
        public void serializeWithType(final byte[] array, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
            typeSerializer.writeTypePrefixForScalar(array, jsonGenerator);
            jsonGenerator.writeBinary(serializerProvider.getConfig().getBase64Variant(), array, 0, array.length);
            typeSerializer.writeTypeSuffixForScalar(array, jsonGenerator);
        }
    }
    
    @JacksonStdImpl
    public static final class CharArraySerializer extends StdSerializer<char[]>
    {
        public CharArraySerializer() {
            super(char[].class);
        }
        
        private final void _writeArrayContents(final JsonGenerator jsonGenerator, final char[] array) throws IOException, JsonGenerationException {
            for (int i = 0; i < array.length; ++i) {
                jsonGenerator.writeString(array, i, 1);
            }
        }
        
        @Override
        public boolean isEmpty(final char[] array) {
            return array == null || array.length == 0;
        }
        
        @Override
        public void serialize(final char[] array, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            if (serializerProvider.isEnabled(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS)) {
                jsonGenerator.writeStartArray();
                this._writeArrayContents(jsonGenerator, array);
                jsonGenerator.writeEndArray();
                return;
            }
            jsonGenerator.writeString(array, 0, array.length);
        }
        
        @Override
        public void serializeWithType(final char[] array, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
            if (serializerProvider.isEnabled(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS)) {
                typeSerializer.writeTypePrefixForArray(array, jsonGenerator);
                this._writeArrayContents(jsonGenerator, array);
                typeSerializer.writeTypeSuffixForArray(array, jsonGenerator);
                return;
            }
            typeSerializer.writeTypePrefixForScalar(array, jsonGenerator);
            jsonGenerator.writeString(array, 0, array.length);
            typeSerializer.writeTypeSuffixForScalar(array, jsonGenerator);
        }
    }
    
    @JacksonStdImpl
    public static final class DoubleArraySerializer extends ArraySerializerBase<double[]>
    {
        private static final JavaType VALUE_TYPE;
        
        static {
            VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Double.TYPE);
        }
        
        public DoubleArraySerializer() {
            super(double[].class, null);
        }
        
        public ContainerSerializer<?> _withValueTypeSerializer(final TypeSerializer typeSerializer) {
            return this;
        }
        
        @Override
        public boolean hasSingleElement(final double[] array) {
            return array.length == 1;
        }
        
        @Override
        public boolean isEmpty(final double[] array) {
            return array == null || array.length == 0;
        }
        
        public void serializeContents(final double[] array, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            for (int i = 0; i < array.length; ++i) {
                jsonGenerator.writeNumber(array[i]);
            }
        }
    }
    
    @JacksonStdImpl
    public static final class FloatArraySerializer extends TypedPrimitiveArraySerializer<float[]>
    {
        private static final JavaType VALUE_TYPE;
        
        static {
            VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Float.TYPE);
        }
        
        public FloatArraySerializer() {
            super(float[].class);
        }
        
        public FloatArraySerializer(final FloatArraySerializer floatArraySerializer, final BeanProperty beanProperty, final TypeSerializer typeSerializer) {
            super((TypedPrimitiveArraySerializer)floatArraySerializer, beanProperty, typeSerializer);
        }
        
        public ContainerSerializer<?> _withValueTypeSerializer(final TypeSerializer typeSerializer) {
            return new FloatArraySerializer(this, this._property, typeSerializer);
        }
        
        @Override
        public boolean hasSingleElement(final float[] array) {
            return array.length == 1;
        }
        
        @Override
        public boolean isEmpty(final float[] array) {
            return array == null || array.length == 0;
        }
        
        public void serializeContents(final float[] array, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            final int n = 0;
            int i = 0;
            if (this._valueTypeSerializer != null) {
                while (i < array.length) {
                    this._valueTypeSerializer.writeTypePrefixForScalar(null, jsonGenerator, Float.TYPE);
                    jsonGenerator.writeNumber(array[i]);
                    this._valueTypeSerializer.writeTypeSuffixForScalar(null, jsonGenerator);
                    ++i;
                }
            }
            else {
                for (int length = array.length, j = n; j < length; ++j) {
                    jsonGenerator.writeNumber(array[j]);
                }
            }
        }
    }
    
    @JacksonStdImpl
    public static final class IntArraySerializer extends ArraySerializerBase<int[]>
    {
        private static final JavaType VALUE_TYPE;
        
        static {
            VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Integer.TYPE);
        }
        
        public IntArraySerializer() {
            super(int[].class, null);
        }
        
        public ContainerSerializer<?> _withValueTypeSerializer(final TypeSerializer typeSerializer) {
            return this;
        }
        
        @Override
        public boolean hasSingleElement(final int[] array) {
            return array.length == 1;
        }
        
        @Override
        public boolean isEmpty(final int[] array) {
            return array == null || array.length == 0;
        }
        
        public void serializeContents(final int[] array, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            for (int i = 0; i < array.length; ++i) {
                jsonGenerator.writeNumber(array[i]);
            }
        }
    }
    
    @JacksonStdImpl
    public static final class LongArraySerializer extends TypedPrimitiveArraySerializer<long[]>
    {
        private static final JavaType VALUE_TYPE;
        
        static {
            VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Long.TYPE);
        }
        
        public LongArraySerializer() {
            super(long[].class);
        }
        
        public LongArraySerializer(final LongArraySerializer longArraySerializer, final BeanProperty beanProperty, final TypeSerializer typeSerializer) {
            super((TypedPrimitiveArraySerializer)longArraySerializer, beanProperty, typeSerializer);
        }
        
        public ContainerSerializer<?> _withValueTypeSerializer(final TypeSerializer typeSerializer) {
            return new LongArraySerializer(this, this._property, typeSerializer);
        }
        
        @Override
        public boolean hasSingleElement(final long[] array) {
            return array.length == 1;
        }
        
        @Override
        public boolean isEmpty(final long[] array) {
            return array == null || array.length == 0;
        }
        
        public void serializeContents(final long[] array, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            final int n = 0;
            int i = 0;
            if (this._valueTypeSerializer != null) {
                while (i < array.length) {
                    this._valueTypeSerializer.writeTypePrefixForScalar(null, jsonGenerator, Long.TYPE);
                    jsonGenerator.writeNumber(array[i]);
                    this._valueTypeSerializer.writeTypeSuffixForScalar(null, jsonGenerator);
                    ++i;
                }
            }
            else {
                for (int length = array.length, j = n; j < length; ++j) {
                    jsonGenerator.writeNumber(array[j]);
                }
            }
        }
    }
    
    @JacksonStdImpl
    public static final class ShortArraySerializer extends TypedPrimitiveArraySerializer<short[]>
    {
        private static final JavaType VALUE_TYPE;
        
        static {
            VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Short.TYPE);
        }
        
        public ShortArraySerializer() {
            super(short[].class);
        }
        
        public ShortArraySerializer(final ShortArraySerializer shortArraySerializer, final BeanProperty beanProperty, final TypeSerializer typeSerializer) {
            super((TypedPrimitiveArraySerializer)shortArraySerializer, beanProperty, typeSerializer);
        }
        
        public ContainerSerializer<?> _withValueTypeSerializer(final TypeSerializer typeSerializer) {
            return new ShortArraySerializer(this, this._property, typeSerializer);
        }
        
        @Override
        public boolean hasSingleElement(final short[] array) {
            return array.length == 1;
        }
        
        @Override
        public boolean isEmpty(final short[] array) {
            return array == null || array.length == 0;
        }
        
        public void serializeContents(final short[] array, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            final int n = 0;
            int i = 0;
            if (this._valueTypeSerializer != null) {
                while (i < array.length) {
                    this._valueTypeSerializer.writeTypePrefixForScalar(null, jsonGenerator, Short.TYPE);
                    jsonGenerator.writeNumber(array[i]);
                    this._valueTypeSerializer.writeTypeSuffixForScalar(null, jsonGenerator);
                    ++i;
                }
            }
            else {
                for (int length = array.length, j = n; j < length; ++j) {
                    jsonGenerator.writeNumber((int)array[j]);
                }
            }
        }
    }
    
    protected abstract static class TypedPrimitiveArraySerializer<T> extends ArraySerializerBase<T>
    {
        protected final TypeSerializer _valueTypeSerializer;
        
        protected TypedPrimitiveArraySerializer(final TypedPrimitiveArraySerializer<T> typedPrimitiveArraySerializer, final BeanProperty beanProperty, final TypeSerializer valueTypeSerializer) {
            super(typedPrimitiveArraySerializer, beanProperty);
            this._valueTypeSerializer = valueTypeSerializer;
        }
        
        protected TypedPrimitiveArraySerializer(final Class<T> clazz) {
            super(clazz);
            this._valueTypeSerializer = null;
        }
    }
}
