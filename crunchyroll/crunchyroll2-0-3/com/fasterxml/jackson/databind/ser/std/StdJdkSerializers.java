// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.File;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.UUID;
import java.util.Currency;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class StdJdkSerializers
{
    public static Collection<Map.Entry<Class<?>, Object>> all() {
        final HashMap<Class<URL>, ToStringSerializer> hashMap = new HashMap<Class<URL>, ToStringSerializer>();
        final ToStringSerializer instance = ToStringSerializer.instance;
        hashMap.put(URL.class, instance);
        hashMap.put((Class<URL>)URI.class, instance);
        hashMap.put((Class<URL>)Currency.class, instance);
        hashMap.put((Class<URL>)UUID.class, instance);
        hashMap.put((Class<URL>)Pattern.class, instance);
        hashMap.put((Class<URL>)Locale.class, instance);
        hashMap.put((Class<URL>)Locale.class, instance);
        hashMap.put((Class<URL>)AtomicReference.class, (ToStringSerializer)AtomicReferenceSerializer.class);
        hashMap.put((Class<URL>)AtomicBoolean.class, (ToStringSerializer)AtomicBooleanSerializer.class);
        hashMap.put((Class<URL>)AtomicInteger.class, (ToStringSerializer)AtomicIntegerSerializer.class);
        hashMap.put((Class<URL>)AtomicLong.class, (ToStringSerializer)AtomicLongSerializer.class);
        hashMap.put((Class<URL>)File.class, (ToStringSerializer)FileSerializer.class);
        hashMap.put((Class<URL>)Class.class, (ToStringSerializer)ClassSerializer.class);
        hashMap.put((Class<URL>)Void.TYPE, (ToStringSerializer)NullSerializer.class);
        return (Collection<Map.Entry<Class<?>, Object>>)hashMap.entrySet();
    }
    
    public static final class AtomicBooleanSerializer extends StdScalarSerializer<AtomicBoolean>
    {
        public AtomicBooleanSerializer() {
            super(AtomicBoolean.class, false);
        }
        
        @Override
        public void serialize(final AtomicBoolean atomicBoolean, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            jsonGenerator.writeBoolean(atomicBoolean.get());
        }
    }
    
    public static final class AtomicIntegerSerializer extends StdScalarSerializer<AtomicInteger>
    {
        public AtomicIntegerSerializer() {
            super(AtomicInteger.class, false);
        }
        
        @Override
        public void serialize(final AtomicInteger atomicInteger, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            jsonGenerator.writeNumber(atomicInteger.get());
        }
    }
    
    public static final class AtomicLongSerializer extends StdScalarSerializer<AtomicLong>
    {
        public AtomicLongSerializer() {
            super(AtomicLong.class, false);
        }
        
        @Override
        public void serialize(final AtomicLong atomicLong, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            jsonGenerator.writeNumber(atomicLong.get());
        }
    }
    
    public static final class AtomicReferenceSerializer extends StdSerializer<AtomicReference<?>>
    {
        public AtomicReferenceSerializer() {
            super(AtomicReference.class, false);
        }
        
        @Override
        public void serialize(final AtomicReference<?> atomicReference, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            serializerProvider.defaultSerializeValue(atomicReference.get(), jsonGenerator);
        }
    }
    
    public static final class ClassSerializer extends StdScalarSerializer<Class<?>>
    {
        public ClassSerializer() {
            super(Class.class, false);
        }
        
        @Override
        public void serialize(final Class<?> clazz, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            jsonGenerator.writeString(clazz.getName());
        }
    }
    
    public static final class FileSerializer extends StdScalarSerializer<File>
    {
        public FileSerializer() {
            super(File.class);
        }
        
        @Override
        public void serialize(final File file, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            jsonGenerator.writeString(file.getAbsolutePath());
        }
    }
}
