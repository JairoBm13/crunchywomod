// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Iterator;
import com.fasterxml.jackson.core.SerializableString;
import java.util.Map;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.util.EnumValues;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.util.EnumMap;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;

@JacksonStdImpl
public class EnumMapSerializer extends ContainerSerializer<EnumMap<? extends Enum<?>, ?>> implements ContextualSerializer
{
    protected final EnumValues _keyEnums;
    protected final BeanProperty _property;
    protected final boolean _staticTyping;
    protected final JsonSerializer<Object> _valueSerializer;
    protected final JavaType _valueType;
    protected final TypeSerializer _valueTypeSerializer;
    
    public EnumMapSerializer(final JavaType valueType, final boolean b, final EnumValues keyEnums, final TypeSerializer valueTypeSerializer, final JsonSerializer<Object> valueSerializer) {
        final boolean b2 = false;
        super(EnumMap.class, false);
        this._property = null;
        boolean staticTyping = false;
        Label_0038: {
            if (!b) {
                staticTyping = b2;
                if (valueType == null) {
                    break Label_0038;
                }
                staticTyping = b2;
                if (!valueType.isFinal()) {
                    break Label_0038;
                }
            }
            staticTyping = true;
        }
        this._staticTyping = staticTyping;
        this._valueType = valueType;
        this._keyEnums = keyEnums;
        this._valueTypeSerializer = valueTypeSerializer;
        this._valueSerializer = valueSerializer;
    }
    
    public EnumMapSerializer(final EnumMapSerializer enumMapSerializer, final BeanProperty property, final JsonSerializer<?> valueSerializer) {
        super(enumMapSerializer);
        this._property = property;
        this._staticTyping = enumMapSerializer._staticTyping;
        this._valueType = enumMapSerializer._valueType;
        this._keyEnums = enumMapSerializer._keyEnums;
        this._valueTypeSerializer = enumMapSerializer._valueTypeSerializer;
        this._valueSerializer = (JsonSerializer<Object>)valueSerializer;
    }
    
    public EnumMapSerializer _withValueTypeSerializer(final TypeSerializer typeSerializer) {
        return new EnumMapSerializer(this._valueType, this._staticTyping, this._keyEnums, typeSerializer, this._valueSerializer);
    }
    
    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
        JsonSerializer<?> serializerInstance;
        final JsonSerializer<?> jsonSerializer = serializerInstance = null;
        if (beanProperty != null) {
            final AnnotatedMember member = beanProperty.getMember();
            serializerInstance = jsonSerializer;
            if (member != null) {
                final Object contentSerializer = serializerProvider.getAnnotationIntrospector().findContentSerializer(member);
                serializerInstance = jsonSerializer;
                if (contentSerializer != null) {
                    serializerInstance = serializerProvider.serializerInstance(member, contentSerializer);
                }
            }
        }
        JsonSerializer<?> valueSerializer;
        if ((valueSerializer = serializerInstance) == null) {
            valueSerializer = this._valueSerializer;
        }
        final JsonSerializer<?> convertingContentSerializer = this.findConvertingContentSerializer(serializerProvider, beanProperty, valueSerializer);
        JsonSerializer<?> contextual = null;
        Label_0135: {
            final EnumMapSerializer withValueSerializer;
            if (convertingContentSerializer == null) {
                contextual = convertingContentSerializer;
                if (!this._staticTyping) {
                    break Label_0135;
                }
                withValueSerializer = this.withValueSerializer(beanProperty, serializerProvider.findValueSerializer(this._valueType, beanProperty));
            }
            else {
                contextual = convertingContentSerializer;
                if (this._valueSerializer instanceof ContextualSerializer) {
                    contextual = ((ContextualSerializer)convertingContentSerializer).createContextual(serializerProvider, beanProperty);
                }
                break Label_0135;
            }
            return withValueSerializer;
        }
        EnumMapSerializer withValueSerializer = this;
        if (contextual != this._valueSerializer) {
            return this.withValueSerializer(beanProperty, contextual);
        }
        return withValueSerializer;
    }
    
    @Override
    public boolean hasSingleElement(final EnumMap<? extends Enum<?>, ?> enumMap) {
        return enumMap.size() == 1;
    }
    
    @Override
    public boolean isEmpty(final EnumMap<? extends Enum<?>, ?> enumMap) {
        return enumMap == null || enumMap.isEmpty();
    }
    
    @Override
    public void serialize(final EnumMap<? extends Enum<?>, ?> enumMap, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        jsonGenerator.writeStartObject();
        if (!enumMap.isEmpty()) {
            this.serializeContents(enumMap, jsonGenerator, serializerProvider);
        }
        jsonGenerator.writeEndObject();
    }
    
    protected void serializeContents(final EnumMap<? extends Enum<?>, ?> enumMap, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (this._valueSerializer != null) {
            this.serializeContentsUsing(enumMap, jsonGenerator, serializerProvider, this._valueSerializer);
        }
        else {
            EnumValues keyEnums = this._keyEnums;
            boolean b;
            if (!serializerProvider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES)) {
                b = true;
            }
            else {
                b = false;
            }
            final TypeSerializer valueTypeSerializer = this._valueTypeSerializer;
            final Iterator<Map.Entry<? extends Enum<?>, ?>> iterator = enumMap.entrySet().iterator();
            Object valueSerializer = null;
            JsonSerializer<Object> jsonSerializer = null;
        Label_0267_Outer:
            while (iterator.hasNext()) {
                final Map.Entry<? extends Enum<?>, ?> entry = iterator.next();
                final Object value = entry.getValue();
                if (!b || value != null) {
                    final Enum enum1 = (Enum)entry.getKey();
                    EnumValues enumValues;
                    if ((enumValues = keyEnums) == null) {
                        enumValues = ((EnumSerializer)(StdSerializer)serializerProvider.findValueSerializer(enum1.getDeclaringClass(), this._property)).getEnumValues();
                    }
                    jsonGenerator.writeFieldName(enumValues.serializedValueFor(enum1));
                    if (value == null) {
                        serializerProvider.defaultSerializeNull(jsonGenerator);
                        keyEnums = enumValues;
                    }
                    else {
                        final Class<?> class1 = value.getClass();
                        while (true) {
                            while (true) {
                                Label_0211: {
                                    if (class1 == valueSerializer) {
                                        final Class<?> clazz = (Class<?>)valueSerializer;
                                        valueSerializer = jsonSerializer;
                                        final JsonSerializer<Object> jsonSerializer2 = jsonSerializer;
                                        jsonSerializer = (JsonSerializer<Object>)clazz;
                                        break Label_0211;
                                    }
                                    Label_0244: {
                                        break Label_0244;
                                        while (true) {
                                            while (true) {
                                                try {
                                                    final JsonSerializer<Object> jsonSerializer2;
                                                    jsonSerializer2.serialize(value, jsonGenerator, serializerProvider);
                                                    final Class<?> clazz2 = (Class<?>)valueSerializer;
                                                    keyEnums = enumValues;
                                                    valueSerializer = jsonSerializer;
                                                    jsonSerializer = (JsonSerializer<Object>)clazz2;
                                                    continue Label_0267_Outer;
                                                    jsonSerializer2 = (JsonSerializer<Object>)(valueSerializer = serializerProvider.findValueSerializer(class1, this._property));
                                                    jsonSerializer = (JsonSerializer<Object>)class1;
                                                    break;
                                                    jsonSerializer2.serializeWithType(value, jsonGenerator, serializerProvider, valueTypeSerializer);
                                                    continue Label_0267_Outer;
                                                }
                                                catch (Exception ex) {
                                                    this.wrapAndThrow(serializerProvider, ex, enumMap, ((Enum)entry.getKey()).name());
                                                    continue Label_0267_Outer;
                                                }
                                                continue Label_0267_Outer;
                                            }
                                        }
                                    }
                                }
                                if (valueTypeSerializer == null) {
                                    continue Label_0267_Outer;
                                }
                                break;
                            }
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    protected void serializeContentsUsing(final EnumMap<? extends Enum<?>, ?> enumMap, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final JsonSerializer<Object> jsonSerializer) throws IOException, JsonGenerationException {
        EnumValues keyEnums = this._keyEnums;
        boolean b;
        if (!serializerProvider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES)) {
            b = true;
        }
        else {
            b = false;
        }
        final TypeSerializer valueTypeSerializer = this._valueTypeSerializer;
        for (final Map.Entry<? extends Enum<?>, ?> entry : enumMap.entrySet()) {
            final Object value = entry.getValue();
            if (!b || value != null) {
                final Enum enum1 = (Enum)entry.getKey();
                EnumValues enumValues;
                if ((enumValues = keyEnums) == null) {
                    enumValues = ((EnumSerializer)(StdSerializer)serializerProvider.findValueSerializer(enum1.getDeclaringClass(), this._property)).getEnumValues();
                }
                jsonGenerator.writeFieldName(enumValues.serializedValueFor(enum1));
                if (value == null) {
                    serializerProvider.defaultSerializeNull(jsonGenerator);
                    keyEnums = enumValues;
                }
                else if (valueTypeSerializer == null) {
                    try {
                        jsonSerializer.serialize(value, jsonGenerator, serializerProvider);
                        keyEnums = enumValues;
                    }
                    catch (Exception ex) {
                        this.wrapAndThrow(serializerProvider, ex, enumMap, ((Enum)entry.getKey()).name());
                        keyEnums = enumValues;
                    }
                }
                else {
                    jsonSerializer.serializeWithType(value, jsonGenerator, serializerProvider, valueTypeSerializer);
                    keyEnums = enumValues;
                }
            }
        }
    }
    
    @Override
    public void serializeWithType(final EnumMap<? extends Enum<?>, ?> enumMap, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        typeSerializer.writeTypePrefixForObject(enumMap, jsonGenerator);
        if (!enumMap.isEmpty()) {
            this.serializeContents(enumMap, jsonGenerator, serializerProvider);
        }
        typeSerializer.writeTypeSuffixForObject(enumMap, jsonGenerator);
    }
    
    public EnumMapSerializer withValueSerializer(final BeanProperty beanProperty, final JsonSerializer<?> jsonSerializer) {
        if (this._property == beanProperty && jsonSerializer == this._valueSerializer) {
            return this;
        }
        return new EnumMapSerializer(this, beanProperty, jsonSerializer);
    }
}
