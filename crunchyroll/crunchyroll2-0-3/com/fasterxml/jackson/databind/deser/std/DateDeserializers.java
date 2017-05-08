// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import java.util.Locale;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.databind.introspect.Annotated;
import java.text.ParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.text.DateFormat;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.TimeZone;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.HashSet;

public class DateDeserializers
{
    private static final HashSet<String> _classNames;
    
    static {
        int i = 0;
        _classNames = new HashSet<String>();
        for (Class[] array = { Calendar.class, GregorianCalendar.class, Date.class, java.util.Date.class, Timestamp.class, TimeZone.class }; i < array.length; ++i) {
            DateDeserializers._classNames.add(array[i].getName());
        }
    }
    
    public static JsonDeserializer<?> find(final Class<?> clazz, final String s) {
        if (!DateDeserializers._classNames.contains(s)) {
            return null;
        }
        if (clazz == Calendar.class) {
            return CalendarDeserializer.instance;
        }
        if (clazz == java.util.Date.class) {
            return DateDeserializer.instance;
        }
        if (clazz == Date.class) {
            return SqlDateDeserializer.instance;
        }
        if (clazz == Timestamp.class) {
            return TimestampDeserializer.instance;
        }
        if (clazz == TimeZone.class) {
            return TimeZoneDeserializer.instance;
        }
        if (clazz == GregorianCalendar.class) {
            return CalendarDeserializer.gregorianInstance;
        }
        throw new IllegalArgumentException("Internal error: can't find deserializer for " + s);
    }
    
    @JacksonStdImpl
    public static class CalendarDeserializer extends DateBasedDeserializer<Calendar>
    {
        public static final CalendarDeserializer gregorianInstance;
        public static final CalendarDeserializer instance;
        protected final Class<? extends Calendar> _calendarClass;
        
        static {
            instance = new CalendarDeserializer();
            gregorianInstance = new CalendarDeserializer(GregorianCalendar.class);
        }
        
        public CalendarDeserializer() {
            super(Calendar.class);
            this._calendarClass = null;
        }
        
        public CalendarDeserializer(final CalendarDeserializer calendarDeserializer, final DateFormat dateFormat, final String s) {
            super((DateBasedDeserializer)calendarDeserializer, dateFormat, s);
            this._calendarClass = calendarDeserializer._calendarClass;
        }
        
        public CalendarDeserializer(final Class<? extends Calendar> calendarClass) {
            super(calendarClass);
            this._calendarClass = calendarClass;
        }
        
        @Override
        public Calendar deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            final java.util.Date parseDate = ((DateBasedDeserializer)this)._parseDate(jsonParser, deserializationContext);
            Calendar calendar;
            if (parseDate == null) {
                calendar = null;
            }
            else {
                if (this._calendarClass == null) {
                    return deserializationContext.constructCalendar(parseDate);
                }
                try {
                    final Calendar calendar2 = (Calendar)this._calendarClass.newInstance();
                    calendar2.setTimeInMillis(parseDate.getTime());
                    final TimeZone timeZone = deserializationContext.getTimeZone();
                    calendar = calendar2;
                    if (timeZone != null) {
                        calendar2.setTimeZone(timeZone);
                        return calendar2;
                    }
                }
                catch (Exception ex) {
                    throw deserializationContext.instantiationException(this._calendarClass, ex);
                }
            }
            return calendar;
        }
        
        protected CalendarDeserializer withDateFormat(final DateFormat dateFormat, final String s) {
            return new CalendarDeserializer(this, dateFormat, s);
        }
    }
    
    protected abstract static class DateBasedDeserializer<T> extends StdScalarDeserializer<T> implements ContextualDeserializer
    {
        protected final DateFormat _customFormat;
        protected final String _formatString;
        
        protected DateBasedDeserializer(final DateBasedDeserializer<T> dateBasedDeserializer, final DateFormat customFormat, final String formatString) {
            super(dateBasedDeserializer._valueClass);
            this._customFormat = customFormat;
            this._formatString = formatString;
        }
        
        protected DateBasedDeserializer(final Class<?> clazz) {
            super(clazz);
            this._customFormat = null;
            this._formatString = null;
        }
        
        @Override
        protected java.util.Date _parseDate(JsonParser customFormat, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            if (this._customFormat != null && customFormat.getCurrentToken() == JsonToken.VALUE_STRING) {
                final String trim = customFormat.getText().trim();
                if (trim.length() == 0) {
                    return this.getEmptyValue();
                }
                customFormat = (JsonParser)this._customFormat;
                // monitorenter(customFormat)
                try {
                    try {
                        return this._customFormat.parse(trim);
                    }
                    finally {
                    }
                    // monitorexit(customFormat)
                }
                catch (ParseException ex) {
                    throw new IllegalArgumentException("Failed to parse Date value '" + (String)deserializationContext + "' (format: \"" + this._formatString + "\"): " + ex.getMessage());
                }
            }
            return super._parseDate(customFormat, deserializationContext);
        }
        
        @Override
        public JsonDeserializer<?> createContextual(final DeserializationContext deserializationContext, final BeanProperty beanProperty) throws JsonMappingException {
            DateBasedDeserializer withDateFormat = this;
            if (beanProperty != null) {
                final JsonFormat.Value format = deserializationContext.getAnnotationIntrospector().findFormat((Annotated)beanProperty.getMember());
                withDateFormat = this;
                if (format != null) {
                    final TimeZone timeZone = format.getTimeZone();
                    final String pattern = format.getPattern();
                    if (pattern.length() > 0) {
                        Locale locale;
                        if ((locale = format.getLocale()) == null) {
                            locale = deserializationContext.getLocale();
                        }
                        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
                        TimeZone timeZone2;
                        if (timeZone == null) {
                            timeZone2 = deserializationContext.getTimeZone();
                        }
                        else {
                            timeZone2 = timeZone;
                        }
                        simpleDateFormat.setTimeZone(timeZone2);
                        withDateFormat = this.withDateFormat(simpleDateFormat, pattern);
                    }
                    else {
                        withDateFormat = this;
                        if (timeZone != null) {
                            final DateFormat dateFormat = deserializationContext.getConfig().getDateFormat();
                            DateFormat withTimeZone;
                            if (((StdDateFormat)dateFormat).getClass() == StdDateFormat.class) {
                                withTimeZone = ((StdDateFormat)dateFormat).withTimeZone(timeZone);
                            }
                            else {
                                withTimeZone = (DateFormat)dateFormat.clone();
                                withTimeZone.setTimeZone(timeZone);
                            }
                            return this.withDateFormat(withTimeZone, pattern);
                        }
                    }
                }
            }
            return withDateFormat;
        }
        
        protected abstract DateBasedDeserializer<T> withDateFormat(final DateFormat p0, final String p1);
    }
    
    public static class DateDeserializer extends DateBasedDeserializer<java.util.Date>
    {
        public static final DateDeserializer instance;
        
        static {
            instance = new DateDeserializer();
        }
        
        public DateDeserializer() {
            super(java.util.Date.class);
        }
        
        public DateDeserializer(final DateDeserializer dateDeserializer, final DateFormat dateFormat, final String s) {
            super((DateBasedDeserializer)dateDeserializer, dateFormat, s);
        }
        
        @Override
        public java.util.Date deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return ((DateBasedDeserializer)this)._parseDate(jsonParser, deserializationContext);
        }
        
        protected DateDeserializer withDateFormat(final DateFormat dateFormat, final String s) {
            return new DateDeserializer(this, dateFormat, s);
        }
    }
    
    public static class SqlDateDeserializer extends DateBasedDeserializer<Date>
    {
        public static final SqlDateDeserializer instance;
        
        static {
            instance = new SqlDateDeserializer();
        }
        
        public SqlDateDeserializer() {
            super(Date.class);
        }
        
        public SqlDateDeserializer(final SqlDateDeserializer sqlDateDeserializer, final DateFormat dateFormat, final String s) {
            super((DateBasedDeserializer)sqlDateDeserializer, dateFormat, s);
        }
        
        @Override
        public Date deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            final java.util.Date parseDate = ((DateBasedDeserializer)this)._parseDate(jsonParser, deserializationContext);
            if (parseDate == null) {
                return null;
            }
            return new Date(parseDate.getTime());
        }
        
        protected SqlDateDeserializer withDateFormat(final DateFormat dateFormat, final String s) {
            return new SqlDateDeserializer(this, dateFormat, s);
        }
    }
    
    protected static class TimeZoneDeserializer extends FromStringDeserializer<TimeZone>
    {
        public static final TimeZoneDeserializer instance;
        
        static {
            instance = new TimeZoneDeserializer();
        }
        
        public TimeZoneDeserializer() {
            super(TimeZone.class);
        }
        
        @Override
        protected TimeZone _deserialize(final String s, final DeserializationContext deserializationContext) throws IOException {
            return TimeZone.getTimeZone(s);
        }
    }
    
    public static class TimestampDeserializer extends DateBasedDeserializer<Timestamp>
    {
        public static final TimestampDeserializer instance;
        
        static {
            instance = new TimestampDeserializer();
        }
        
        public TimestampDeserializer() {
            super(Timestamp.class);
        }
        
        public TimestampDeserializer(final TimestampDeserializer timestampDeserializer, final DateFormat dateFormat, final String s) {
            super((DateBasedDeserializer)timestampDeserializer, dateFormat, s);
        }
        
        @Override
        public Timestamp deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return new Timestamp(((DateBasedDeserializer)this)._parseDate(jsonParser, deserializationContext).getTime());
        }
        
        protected TimestampDeserializer withDateFormat(final DateFormat dateFormat, final String s) {
            return new TimestampDeserializer(this, dateFormat, s);
        }
    }
}
