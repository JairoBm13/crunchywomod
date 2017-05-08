// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ext;

import java.util.TimeZone;
import java.util.Date;
import java.util.GregorianCalendar;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import com.fasterxml.jackson.databind.deser.Deserializers;

public class CoreXMLDeserializers extends Base
{
    static final DatatypeFactory _dataTypeFactory;
    
    static {
        try {
            _dataTypeFactory = DatatypeFactory.newInstance();
        }
        catch (DatatypeConfigurationException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public JsonDeserializer<?> findBeanDeserializer(final JavaType javaType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription) {
        final Class<?> rawClass = javaType.getRawClass();
        if (rawClass == QName.class) {
            return QNameDeserializer.instance;
        }
        if (rawClass == XMLGregorianCalendar.class) {
            return GregorianCalendarDeserializer.instance;
        }
        if (rawClass == Duration.class) {
            return DurationDeserializer.instance;
        }
        return null;
    }
    
    public static class DurationDeserializer extends FromStringDeserializer<Duration>
    {
        public static final DurationDeserializer instance;
        
        static {
            instance = new DurationDeserializer();
        }
        
        public DurationDeserializer() {
            super(Duration.class);
        }
        
        @Override
        protected Duration _deserialize(final String s, final DeserializationContext deserializationContext) throws IllegalArgumentException {
            return CoreXMLDeserializers._dataTypeFactory.newDuration(s);
        }
    }
    
    public static class GregorianCalendarDeserializer extends StdScalarDeserializer<XMLGregorianCalendar>
    {
        public static final GregorianCalendarDeserializer instance;
        
        static {
            instance = new GregorianCalendarDeserializer();
        }
        
        public GregorianCalendarDeserializer() {
            super(XMLGregorianCalendar.class);
        }
        
        @Override
        public XMLGregorianCalendar deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            final Date parseDate = this._parseDate(jsonParser, deserializationContext);
            if (parseDate == null) {
                return null;
            }
            final GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(parseDate);
            final TimeZone timeZone = deserializationContext.getTimeZone();
            if (timeZone != null) {
                gregorianCalendar.setTimeZone(timeZone);
            }
            return CoreXMLDeserializers._dataTypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        }
    }
    
    public static class QNameDeserializer extends FromStringDeserializer<QName>
    {
        public static final QNameDeserializer instance;
        
        static {
            instance = new QNameDeserializer();
        }
        
        public QNameDeserializer() {
            super(QName.class);
        }
        
        @Override
        protected QName _deserialize(final String qNameAsString, final DeserializationContext deserializationContext) throws IllegalArgumentException {
            return QName.valueOf(qNameAsString);
        }
    }
}
