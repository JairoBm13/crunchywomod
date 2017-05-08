// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.Locale;
import java.util.TimeZone;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.text.DateFormat;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public abstract class DateTimeSerializerBase<T> extends StdScalarSerializer<T> implements ContextualSerializer
{
    protected final DateFormat _customFormat;
    protected final boolean _useTimestamp;
    
    protected DateTimeSerializerBase(final Class<T> clazz, final boolean useTimestamp, final DateFormat customFormat) {
        super(clazz);
        this._useTimestamp = useTimestamp;
        this._customFormat = customFormat;
    }
    
    protected abstract long _timestamp(final T p0);
    
    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
        DateTimeSerializerBase withFormat = this;
        if (beanProperty != null) {
            final JsonFormat.Value format = serializerProvider.getAnnotationIntrospector().findFormat((Annotated)beanProperty.getMember());
            withFormat = this;
            if (format != null) {
                if (format.getShape().isNumeric()) {
                    withFormat = this.withFormat(true, null);
                }
                else {
                    final TimeZone timeZone = format.getTimeZone();
                    final String pattern = format.getPattern();
                    if (pattern.length() > 0) {
                        Locale locale;
                        if ((locale = format.getLocale()) == null) {
                            locale = serializerProvider.getLocale();
                        }
                        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
                        TimeZone timeZone2;
                        if (timeZone == null) {
                            timeZone2 = serializerProvider.getTimeZone();
                        }
                        else {
                            timeZone2 = timeZone;
                        }
                        simpleDateFormat.setTimeZone(timeZone2);
                        return this.withFormat(false, simpleDateFormat);
                    }
                    withFormat = this;
                    if (timeZone != null) {
                        final DateFormat dateFormat = serializerProvider.getConfig().getDateFormat();
                        DateFormat iso8601Format;
                        if (dateFormat.getClass() == StdDateFormat.class) {
                            iso8601Format = StdDateFormat.getISO8601Format(timeZone);
                        }
                        else {
                            iso8601Format = (DateFormat)dateFormat.clone();
                            iso8601Format.setTimeZone(timeZone);
                        }
                        return this.withFormat(false, iso8601Format);
                    }
                }
            }
        }
        return withFormat;
    }
    
    @Override
    public boolean isEmpty(final T t) {
        return t == null || this._timestamp(t) == 0L;
    }
    
    public abstract DateTimeSerializerBase<T> withFormat(final boolean p0, final DateFormat p1);
}
