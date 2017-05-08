// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.IOException;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonGenerator;

public abstract class GeneratorBase extends JsonGenerator
{
    protected boolean _cfgNumbersAsStrings;
    protected boolean _closed;
    protected int _features;
    protected ObjectCodec _objectCodec;
    protected JsonWriteContext _writeContext;
    
    protected GeneratorBase(final int features, final ObjectCodec objectCodec) {
        this._features = features;
        this._writeContext = JsonWriteContext.createRootContext();
        this._objectCodec = objectCodec;
        this._cfgNumbersAsStrings = this.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS);
    }
    
    protected abstract void _releaseBuffers();
    
    protected void _reportError(final String s) throws JsonGenerationException {
        throw new JsonGenerationException(s);
    }
    
    protected final void _throwInternal() {
        VersionUtil.throwInternal();
    }
    
    protected abstract void _verifyValueWrite(final String p0) throws IOException, JsonGenerationException;
    
    protected void _writeSimpleObject(final Object o) throws IOException, JsonGenerationException {
        if (o == null) {
            this.writeNull();
            return;
        }
        if (o instanceof String) {
            this.writeString((String)o);
            return;
        }
        if (o instanceof Number) {
            final Number n = (Number)o;
            if (n instanceof Integer) {
                this.writeNumber(n.intValue());
                return;
            }
            if (n instanceof Long) {
                this.writeNumber(n.longValue());
                return;
            }
            if (n instanceof Double) {
                this.writeNumber(n.doubleValue());
                return;
            }
            if (n instanceof Float) {
                this.writeNumber(n.floatValue());
                return;
            }
            if (n instanceof Short) {
                this.writeNumber(n.shortValue());
                return;
            }
            if (n instanceof Byte) {
                this.writeNumber(n.byteValue());
                return;
            }
            if (n instanceof BigInteger) {
                this.writeNumber((BigInteger)n);
                return;
            }
            if (n instanceof BigDecimal) {
                this.writeNumber((BigDecimal)n);
                return;
            }
            if (n instanceof AtomicInteger) {
                this.writeNumber(((AtomicInteger)n).get());
                return;
            }
            if (n instanceof AtomicLong) {
                this.writeNumber(((AtomicLong)n).get());
                return;
            }
        }
        else {
            if (o instanceof byte[]) {
                this.writeBinary((byte[])o);
                return;
            }
            if (o instanceof Boolean) {
                this.writeBoolean((boolean)o);
                return;
            }
            if (o instanceof AtomicBoolean) {
                this.writeBoolean(((AtomicBoolean)o).get());
                return;
            }
        }
        throw new IllegalStateException("No ObjectCodec defined for the generator, can only serialize simple wrapper types (type passed " + o.getClass().getName() + ")");
    }
    
    @Override
    public void close() throws IOException {
        this._closed = true;
    }
    
    public final JsonWriteContext getOutputContext() {
        return this._writeContext;
    }
    
    public final boolean isEnabled(final Feature feature) {
        return (this._features & feature.getMask()) != 0x0;
    }
    
    @Override
    public JsonGenerator useDefaultPrettyPrinter() {
        if (this.getPrettyPrinter() != null) {
            return this;
        }
        return this.setPrettyPrinter(new DefaultPrettyPrinter());
    }
    
    @Override
    public Version version() {
        return VersionUtil.versionFor(this.getClass());
    }
    
    @Override
    public void writeFieldName(final SerializableString serializableString) throws IOException, JsonGenerationException {
        this.writeFieldName(serializableString.getValue());
    }
    
    @Override
    public void writeObject(final Object o) throws IOException, JsonProcessingException {
        if (o == null) {
            this.writeNull();
            return;
        }
        if (this._objectCodec != null) {
            this._objectCodec.writeValue(this, o);
            return;
        }
        this._writeSimpleObject(o);
    }
    
    @Override
    public void writeRawValue(final String s) throws IOException, JsonGenerationException {
        this._verifyValueWrite("write raw value");
        this.writeRaw(s);
    }
    
    @Override
    public void writeString(final SerializableString serializableString) throws IOException, JsonGenerationException {
        this.writeString(serializableString.getValue());
    }
}
