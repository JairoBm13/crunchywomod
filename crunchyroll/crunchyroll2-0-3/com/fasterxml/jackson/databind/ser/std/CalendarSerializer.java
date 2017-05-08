// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import java.text.DateFormat;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import java.util.Calendar;

@JacksonStdImpl
public class CalendarSerializer extends DateTimeSerializerBase<Calendar>
{
    public static final CalendarSerializer instance;
    
    static {
        instance = new CalendarSerializer();
    }
    
    public CalendarSerializer() {
        this(false, null);
    }
    
    public CalendarSerializer(final boolean b, final DateFormat dateFormat) {
        super(Calendar.class, b, dateFormat);
    }
    
    @Override
    protected long _timestamp(final Calendar calendar) {
        if (calendar == null) {
            return 0L;
        }
        return calendar.getTimeInMillis();
    }
    
    @Override
    public void serialize(final Calendar calendar, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (this._useTimestamp) {
            jsonGenerator.writeNumber(this._timestamp(calendar));
            return;
        }
        if (this._customFormat != null) {
            synchronized (this._customFormat) {
                jsonGenerator.writeString(this._customFormat.format(calendar));
                return;
            }
        }
        serializerProvider.defaultSerializeDateValue(calendar.getTime(), jsonGenerator);
    }
    
    @Override
    public CalendarSerializer withFormat(final boolean b, final DateFormat dateFormat) {
        if (b) {
            return new CalendarSerializer(true, null);
        }
        return new CalendarSerializer(false, dateFormat);
    }
}
