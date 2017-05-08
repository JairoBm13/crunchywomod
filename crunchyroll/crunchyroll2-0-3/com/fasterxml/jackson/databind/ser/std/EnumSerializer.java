// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.util.EnumValues;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

@JacksonStdImpl
public class EnumSerializer extends StdScalarSerializer<Enum<?>> implements ContextualSerializer
{
    protected final Boolean _serializeAsIndex;
    protected final EnumValues _values;
    
    public EnumSerializer(final EnumValues values, final Boolean serializeAsIndex) {
        super(Enum.class, false);
        this._values = values;
        this._serializeAsIndex = serializeAsIndex;
    }
    
    protected static Boolean _isShapeWrittenUsingIndex(final Class<?> clazz, final JsonFormat.Value value, final boolean b) {
        Enum<JsonFormat.Shape> shape;
        if (value == null) {
            shape = null;
        }
        else {
            shape = value.getShape();
        }
        if (shape == null || shape == JsonFormat.Shape.ANY || shape == JsonFormat.Shape.SCALAR) {
            return null;
        }
        if (shape == JsonFormat.Shape.STRING) {
            return Boolean.FALSE;
        }
        if (((JsonFormat.Shape)shape).isNumeric()) {
            return Boolean.TRUE;
        }
        final StringBuilder append = new StringBuilder().append("Unsupported serialization shape (").append(shape).append(") for Enum ").append(clazz.getName()).append(", not supported as ");
        String s;
        if (b) {
            s = "class";
        }
        else {
            s = "property";
        }
        throw new IllegalArgumentException(append.append(s).append(" annotation").toString());
    }
    
    public static EnumSerializer construct(final Class<Enum<?>> clazz, final SerializationConfig serializationConfig, final BeanDescription beanDescription, final JsonFormat.Value value) {
        final AnnotationIntrospector annotationIntrospector = serializationConfig.getAnnotationIntrospector();
        EnumValues enumValues;
        if (serializationConfig.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)) {
            enumValues = EnumValues.constructFromToString(clazz, annotationIntrospector);
        }
        else {
            enumValues = EnumValues.constructFromName(clazz, annotationIntrospector);
        }
        return new EnumSerializer(enumValues, _isShapeWrittenUsingIndex(clazz, value, true));
    }
    
    protected final boolean _serializeAsIndex(final SerializerProvider serializerProvider) {
        if (this._serializeAsIndex != null) {
            return this._serializeAsIndex;
        }
        return serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX);
    }
    
    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
        EnumSerializer enumSerializer = this;
        if (beanProperty != null) {
            final JsonFormat.Value format = serializerProvider.getAnnotationIntrospector().findFormat((Annotated)beanProperty.getMember());
            enumSerializer = this;
            if (format != null) {
                final Boolean isShapeWrittenUsingIndex = _isShapeWrittenUsingIndex(beanProperty.getType().getRawClass(), format, false);
                enumSerializer = this;
                if (isShapeWrittenUsingIndex != this._serializeAsIndex) {
                    enumSerializer = new EnumSerializer(this._values, isShapeWrittenUsingIndex);
                }
            }
        }
        return enumSerializer;
    }
    
    public EnumValues getEnumValues() {
        return this._values;
    }
    
    @Override
    public final void serialize(final Enum<?> enum1, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (this._serializeAsIndex(serializerProvider)) {
            jsonGenerator.writeNumber(enum1.ordinal());
            return;
        }
        jsonGenerator.writeString(this._values.serializedValueFor(enum1));
    }
}
