// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import java.util.Calendar;
import java.util.Date;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;

public class StdKeySerializers
{
    protected static final JsonSerializer<Object> DEFAULT_KEY_SERIALIZER;
    protected static final JsonSerializer<Object> DEFAULT_STRING_SERIALIZER;
    
    static {
        DEFAULT_KEY_SERIALIZER = new StdKeySerializer();
        DEFAULT_STRING_SERIALIZER = new StringKeySerializer();
    }
    
    public static JsonSerializer<Object> getStdKeySerializer(final JavaType javaType) {
        if (javaType == null) {
            return StdKeySerializers.DEFAULT_KEY_SERIALIZER;
        }
        final Class<?> rawClass = javaType.getRawClass();
        if (rawClass == String.class) {
            return StdKeySerializers.DEFAULT_STRING_SERIALIZER;
        }
        if (rawClass == Object.class) {
            return StdKeySerializers.DEFAULT_KEY_SERIALIZER;
        }
        if (Date.class.isAssignableFrom(rawClass)) {
            return (JsonSerializer<Object>)DateKeySerializer.instance;
        }
        if (Calendar.class.isAssignableFrom(rawClass)) {
            return (JsonSerializer<Object>)CalendarKeySerializer.instance;
        }
        return StdKeySerializers.DEFAULT_KEY_SERIALIZER;
    }
    
    public static class CalendarKeySerializer extends StdSerializer<Calendar>
    {
        protected static final JsonSerializer<?> instance;
        
        static {
            instance = new CalendarKeySerializer();
        }
        
        public CalendarKeySerializer() {
            super(Calendar.class);
        }
        
        @Override
        public void serialize(final Calendar calendar, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            serializerProvider.defaultSerializeDateKey(calendar.getTimeInMillis(), jsonGenerator);
        }
    }
    
    public static class DateKeySerializer extends StdSerializer<Date>
    {
        protected static final JsonSerializer<?> instance;
        
        static {
            instance = new DateKeySerializer();
        }
        
        public DateKeySerializer() {
            super(Date.class);
        }
        
        @Override
        public void serialize(final Date date, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            serializerProvider.defaultSerializeDateKey(date, jsonGenerator);
        }
    }
    
    public static class StringKeySerializer extends StdSerializer<String>
    {
        public StringKeySerializer() {
            super(String.class);
        }
        
        @Override
        public void serialize(final String s, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            jsonGenerator.writeFieldName(s);
        }
    }
}
