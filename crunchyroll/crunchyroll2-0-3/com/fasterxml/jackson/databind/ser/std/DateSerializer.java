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
import java.util.Date;

@JacksonStdImpl
public class DateSerializer extends DateTimeSerializerBase<Date>
{
    public static final DateSerializer instance;
    
    static {
        instance = new DateSerializer();
    }
    
    public DateSerializer() {
        this(false, null);
    }
    
    public DateSerializer(final boolean b, final DateFormat dateFormat) {
        super(Date.class, b, dateFormat);
    }
    
    @Override
    protected long _timestamp(final Date date) {
        if (date == null) {
            return 0L;
        }
        return date.getTime();
    }
    
    @Override
    public void serialize(final Date date, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (this._useTimestamp) {
            jsonGenerator.writeNumber(this._timestamp(date));
            return;
        }
        if (this._customFormat != null) {
            synchronized (this._customFormat) {
                jsonGenerator.writeString(this._customFormat.format(date));
                return;
            }
        }
        serializerProvider.defaultSerializeDateValue(date, jsonGenerator);
    }
    
    @Override
    public DateSerializer withFormat(final boolean b, final DateFormat dateFormat) {
        if (b) {
            return new DateSerializer(true, null);
        }
        return new DateSerializer(false, dateFormat);
    }
}
