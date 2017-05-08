// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ext;

import java.util.Calendar;
import com.fasterxml.jackson.databind.ser.std.CalendarSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.xml.datatype.XMLGregorianCalendar;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import javax.xml.namespace.QName;
import javax.xml.datatype.Duration;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.Serializers;

public class CoreXMLSerializers extends Base
{
    @Override
    public JsonSerializer<?> findSerializer(final SerializationConfig serializationConfig, final JavaType javaType, final BeanDescription beanDescription) {
        final Class<?> rawClass = javaType.getRawClass();
        if (Duration.class.isAssignableFrom(rawClass) || QName.class.isAssignableFrom(rawClass)) {
            return ToStringSerializer.instance;
        }
        if (XMLGregorianCalendar.class.isAssignableFrom(rawClass)) {
            return XMLGregorianCalendarSerializer.instance;
        }
        return null;
    }
    
    public static class XMLGregorianCalendarSerializer extends StdSerializer<XMLGregorianCalendar>
    {
        public static final XMLGregorianCalendarSerializer instance;
        
        static {
            instance = new XMLGregorianCalendarSerializer();
        }
        
        public XMLGregorianCalendarSerializer() {
            super(XMLGregorianCalendar.class);
        }
        
        @Override
        public void serialize(final XMLGregorianCalendar xmlGregorianCalendar, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            CalendarSerializer.instance.serialize(xmlGregorianCalendar.toGregorianCalendar(), jsonGenerator, serializerProvider);
        }
    }
}
