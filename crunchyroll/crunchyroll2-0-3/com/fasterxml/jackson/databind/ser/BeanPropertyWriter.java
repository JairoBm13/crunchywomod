// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanPropertyWriter;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.util.NameTransformer;
import java.lang.reflect.Type;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.util.Map;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import java.util.HashMap;
import java.lang.reflect.Field;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.JavaType;
import java.lang.reflect.Method;
import com.fasterxml.jackson.databind.BeanProperty;

public class BeanPropertyWriter implements BeanProperty
{
    public static final Object MARKER_FOR_EMPTY;
    protected final Method _accessorMethod;
    protected final JavaType _cfgSerializationType;
    protected final Annotations _contextAnnotations;
    protected final JavaType _declaredType;
    protected PropertySerializerMap _dynamicSerializers;
    protected final Field _field;
    protected final Class<?>[] _includeInViews;
    protected HashMap<Object, Object> _internalSettings;
    protected final boolean _isRequired;
    protected final AnnotatedMember _member;
    protected final SerializedString _name;
    protected JavaType _nonTrivialBaseType;
    protected JsonSerializer<Object> _nullSerializer;
    protected JsonSerializer<Object> _serializer;
    protected final boolean _suppressNulls;
    protected final Object _suppressableValue;
    protected TypeSerializer _typeSerializer;
    protected final PropertyName _wrapperName;
    
    static {
        MARKER_FOR_EMPTY = new Object();
    }
    
    public BeanPropertyWriter(final BeanPropertyDefinition beanPropertyDefinition, final AnnotatedMember member, final Annotations contextAnnotations, final JavaType declaredType, final JsonSerializer<?> serializer, final TypeSerializer typeSerializer, final JavaType cfgSerializationType, final boolean suppressNulls, final Object suppressableValue) {
        this._member = member;
        this._contextAnnotations = contextAnnotations;
        this._name = new SerializedString(beanPropertyDefinition.getName());
        this._wrapperName = beanPropertyDefinition.getWrapperName();
        this._declaredType = declaredType;
        this._serializer = (JsonSerializer<Object>)serializer;
        PropertySerializerMap emptyMap;
        if (serializer == null) {
            emptyMap = PropertySerializerMap.emptyMap();
        }
        else {
            emptyMap = null;
        }
        this._dynamicSerializers = emptyMap;
        this._typeSerializer = typeSerializer;
        this._cfgSerializationType = cfgSerializationType;
        this._isRequired = beanPropertyDefinition.isRequired();
        if (member instanceof AnnotatedField) {
            this._accessorMethod = null;
            this._field = (Field)member.getMember();
        }
        else {
            if (!(member instanceof AnnotatedMethod)) {
                throw new IllegalArgumentException("Can not pass member of type " + member.getClass().getName());
            }
            this._accessorMethod = (Method)member.getMember();
            this._field = null;
        }
        this._suppressNulls = suppressNulls;
        this._suppressableValue = suppressableValue;
        this._includeInViews = beanPropertyDefinition.findViews();
        this._nullSerializer = null;
    }
    
    protected BeanPropertyWriter(final BeanPropertyWriter beanPropertyWriter) {
        this(beanPropertyWriter, beanPropertyWriter._name);
    }
    
    protected BeanPropertyWriter(final BeanPropertyWriter beanPropertyWriter, final SerializedString name) {
        this._name = name;
        this._wrapperName = beanPropertyWriter._wrapperName;
        this._member = beanPropertyWriter._member;
        this._contextAnnotations = beanPropertyWriter._contextAnnotations;
        this._declaredType = beanPropertyWriter._declaredType;
        this._accessorMethod = beanPropertyWriter._accessorMethod;
        this._field = beanPropertyWriter._field;
        this._serializer = beanPropertyWriter._serializer;
        this._nullSerializer = beanPropertyWriter._nullSerializer;
        if (beanPropertyWriter._internalSettings != null) {
            this._internalSettings = new HashMap<Object, Object>(beanPropertyWriter._internalSettings);
        }
        this._cfgSerializationType = beanPropertyWriter._cfgSerializationType;
        this._dynamicSerializers = beanPropertyWriter._dynamicSerializers;
        this._suppressNulls = beanPropertyWriter._suppressNulls;
        this._suppressableValue = beanPropertyWriter._suppressableValue;
        this._includeInViews = beanPropertyWriter._includeInViews;
        this._typeSerializer = beanPropertyWriter._typeSerializer;
        this._nonTrivialBaseType = beanPropertyWriter._nonTrivialBaseType;
        this._isRequired = beanPropertyWriter._isRequired;
    }
    
    protected JsonSerializer<Object> _findAndAddDynamic(final PropertySerializerMap propertySerializerMap, final Class<?> clazz, final SerializerProvider serializerProvider) throws JsonMappingException {
        PropertySerializerMap.SerializerAndMapResult serializerAndMapResult;
        if (this._nonTrivialBaseType != null) {
            serializerAndMapResult = propertySerializerMap.findAndAddSerializer(serializerProvider.constructSpecializedType(this._nonTrivialBaseType, clazz), serializerProvider, this);
        }
        else {
            serializerAndMapResult = propertySerializerMap.findAndAddSerializer(clazz, serializerProvider, this);
        }
        if (propertySerializerMap != serializerAndMapResult.map) {
            this._dynamicSerializers = serializerAndMapResult.map;
        }
        return serializerAndMapResult.serializer;
    }
    
    protected void _handleSelfReference(final Object o, final JsonSerializer<?> jsonSerializer) throws JsonMappingException {
        if (jsonSerializer.usesObjectId()) {
            return;
        }
        throw new JsonMappingException("Direct self-reference leading to cycle");
    }
    
    public void assignNullSerializer(final JsonSerializer<Object> nullSerializer) {
        if (this._nullSerializer != null && this._nullSerializer != nullSerializer) {
            throw new IllegalStateException("Can not override null serializer");
        }
        this._nullSerializer = nullSerializer;
    }
    
    public void assignSerializer(final JsonSerializer<Object> serializer) {
        if (this._serializer != null && this._serializer != serializer) {
            throw new IllegalStateException("Can not override serializer");
        }
        this._serializer = serializer;
    }
    
    public final Object get(final Object o) throws Exception {
        if (this._accessorMethod != null) {
            return this._accessorMethod.invoke(o, new Object[0]);
        }
        return this._field.get(o);
    }
    
    public Type getGenericPropertyType() {
        if (this._accessorMethod != null) {
            return this._accessorMethod.getGenericReturnType();
        }
        return this._field.getGenericType();
    }
    
    @Override
    public AnnotatedMember getMember() {
        return this._member;
    }
    
    @Override
    public String getName() {
        return this._name.getValue();
    }
    
    public JavaType getSerializationType() {
        return this._cfgSerializationType;
    }
    
    @Override
    public JavaType getType() {
        return this._declaredType;
    }
    
    public Class<?>[] getViews() {
        return this._includeInViews;
    }
    
    public boolean hasNullSerializer() {
        return this._nullSerializer != null;
    }
    
    public boolean hasSerializer() {
        return this._serializer != null;
    }
    
    public BeanPropertyWriter rename(final NameTransformer nameTransformer) {
        final String transform = nameTransformer.transform(this._name.getValue());
        if (transform.equals(this._name.toString())) {
            return this;
        }
        return new BeanPropertyWriter(this, new SerializedString(transform));
    }
    
    public void serializeAsColumn(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws Exception {
        final Object value = this.get(o);
        if (value == null) {
            if (this._nullSerializer != null) {
                this._nullSerializer.serialize(null, jsonGenerator, serializerProvider);
                return;
            }
            jsonGenerator.writeNull();
        }
        else {
            JsonSerializer<Object> jsonSerializer;
            if ((jsonSerializer = this._serializer) == null) {
                final Class<?> class1 = value.getClass();
                final PropertySerializerMap dynamicSerializers = this._dynamicSerializers;
                if ((jsonSerializer = dynamicSerializers.serializerFor(class1)) == null) {
                    jsonSerializer = this._findAndAddDynamic(dynamicSerializers, class1, serializerProvider);
                }
            }
            if (this._suppressableValue != null) {
                if (BeanPropertyWriter.MARKER_FOR_EMPTY == this._suppressableValue) {
                    if (jsonSerializer.isEmpty(value)) {
                        this.serializeAsPlaceholder(o, jsonGenerator, serializerProvider);
                        return;
                    }
                }
                else if (this._suppressableValue.equals(value)) {
                    this.serializeAsPlaceholder(o, jsonGenerator, serializerProvider);
                    return;
                }
            }
            if (value == o) {
                this._handleSelfReference(o, jsonSerializer);
            }
            if (this._typeSerializer == null) {
                jsonSerializer.serialize(value, jsonGenerator, serializerProvider);
                return;
            }
            jsonSerializer.serializeWithType(value, jsonGenerator, serializerProvider, this._typeSerializer);
        }
    }
    
    public void serializeAsField(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws Exception {
        final Object value = this.get(o);
        if (value == null) {
            if (this._nullSerializer != null) {
                jsonGenerator.writeFieldName(this._name);
                this._nullSerializer.serialize(null, jsonGenerator, serializerProvider);
            }
        }
        else {
            JsonSerializer<Object> jsonSerializer;
            if ((jsonSerializer = this._serializer) == null) {
                final Class<?> class1 = value.getClass();
                final PropertySerializerMap dynamicSerializers = this._dynamicSerializers;
                if ((jsonSerializer = dynamicSerializers.serializerFor(class1)) == null) {
                    jsonSerializer = this._findAndAddDynamic(dynamicSerializers, class1, serializerProvider);
                }
            }
            if (this._suppressableValue != null) {
                if (BeanPropertyWriter.MARKER_FOR_EMPTY == this._suppressableValue) {
                    if (jsonSerializer.isEmpty(value)) {
                        return;
                    }
                }
                else if (this._suppressableValue.equals(value)) {
                    return;
                }
            }
            if (value == o) {
                this._handleSelfReference(o, jsonSerializer);
            }
            jsonGenerator.writeFieldName(this._name);
            if (this._typeSerializer == null) {
                jsonSerializer.serialize(value, jsonGenerator, serializerProvider);
                return;
            }
            jsonSerializer.serializeWithType(value, jsonGenerator, serializerProvider, this._typeSerializer);
        }
    }
    
    public void serializeAsPlaceholder(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws Exception {
        if (this._nullSerializer != null) {
            this._nullSerializer.serialize(null, jsonGenerator, serializerProvider);
            return;
        }
        jsonGenerator.writeNull();
    }
    
    public void setNonTrivialBaseType(final JavaType nonTrivialBaseType) {
        this._nonTrivialBaseType = nonTrivialBaseType;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(40);
        sb.append("property '").append(this.getName()).append("' (");
        if (this._accessorMethod != null) {
            sb.append("via method ").append(this._accessorMethod.getDeclaringClass().getName()).append("#").append(this._accessorMethod.getName());
        }
        else {
            sb.append("field \"").append(this._field.getDeclaringClass().getName()).append("#").append(this._field.getName());
        }
        if (this._serializer == null) {
            sb.append(", no static serializer");
        }
        else {
            sb.append(", static serializer of type " + this._serializer.getClass().getName());
        }
        sb.append(')');
        return sb.toString();
    }
    
    public BeanPropertyWriter unwrappingWriter(final NameTransformer nameTransformer) {
        return new UnwrappingBeanPropertyWriter(this, nameTransformer);
    }
    
    public boolean willSuppressNulls() {
        return this._suppressNulls;
    }
}
