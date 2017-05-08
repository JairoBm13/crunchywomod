// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.introspect.Annotated;
import java.util.TimeZone;
import java.util.Locale;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.impl.TypeWrappedSerializer;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.Date;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import com.fasterxml.jackson.databind.ser.impl.UnknownSerializer;
import com.fasterxml.jackson.databind.ser.impl.FailingSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerCache;
import com.fasterxml.jackson.databind.util.RootNameLookup;
import com.fasterxml.jackson.databind.ser.impl.ReadOnlyClassToSerializerMap;
import java.text.DateFormat;

public abstract class SerializerProvider extends DatabindContext
{
    public static final JsonSerializer<Object> DEFAULT_NULL_KEY_SERIALIZER;
    public static final JsonSerializer<Object> DEFAULT_UNKNOWN_SERIALIZER;
    protected static final JavaType TYPE_OBJECT;
    protected final SerializationConfig _config;
    protected DateFormat _dateFormat;
    protected JsonSerializer<Object> _keySerializer;
    protected final ReadOnlyClassToSerializerMap _knownSerializers;
    protected JsonSerializer<Object> _nullKeySerializer;
    protected JsonSerializer<Object> _nullValueSerializer;
    protected final RootNameLookup _rootNames;
    protected final Class<?> _serializationView;
    protected final SerializerCache _serializerCache;
    protected final SerializerFactory _serializerFactory;
    protected JsonSerializer<Object> _unknownTypeSerializer;
    
    static {
        TYPE_OBJECT = TypeFactory.defaultInstance().uncheckedSimpleType(Object.class);
        DEFAULT_NULL_KEY_SERIALIZER = new FailingSerializer("Null key for a Map not allowed in JSON (use a converting NullKeySerializer?)");
        DEFAULT_UNKNOWN_SERIALIZER = new UnknownSerializer();
    }
    
    public SerializerProvider() {
        this._unknownTypeSerializer = SerializerProvider.DEFAULT_UNKNOWN_SERIALIZER;
        this._nullValueSerializer = NullSerializer.instance;
        this._nullKeySerializer = SerializerProvider.DEFAULT_NULL_KEY_SERIALIZER;
        this._config = null;
        this._serializerFactory = null;
        this._serializerCache = new SerializerCache();
        this._knownSerializers = null;
        this._rootNames = new RootNameLookup();
        this._serializationView = null;
    }
    
    protected SerializerProvider(final SerializerProvider serializerProvider, final SerializationConfig config, final SerializerFactory serializerFactory) {
        this._unknownTypeSerializer = SerializerProvider.DEFAULT_UNKNOWN_SERIALIZER;
        this._nullValueSerializer = NullSerializer.instance;
        this._nullKeySerializer = SerializerProvider.DEFAULT_NULL_KEY_SERIALIZER;
        if (config == null) {
            throw new NullPointerException();
        }
        this._serializerFactory = serializerFactory;
        this._config = config;
        this._serializerCache = serializerProvider._serializerCache;
        this._unknownTypeSerializer = serializerProvider._unknownTypeSerializer;
        this._keySerializer = serializerProvider._keySerializer;
        this._nullValueSerializer = serializerProvider._nullValueSerializer;
        this._nullKeySerializer = serializerProvider._nullKeySerializer;
        this._rootNames = serializerProvider._rootNames;
        this._knownSerializers = this._serializerCache.getReadOnlyLookupMap();
        this._serializationView = config.getActiveView();
    }
    
    protected JsonSerializer<Object> _createAndCacheUntypedSerializer(final JavaType javaType) throws JsonMappingException {
        try {
            final JsonSerializer<Object> createUntypedSerializer = this._createUntypedSerializer(javaType);
            if (createUntypedSerializer != null) {
                this._serializerCache.addAndResolveNonTypedSerializer(javaType, createUntypedSerializer, this);
            }
            return createUntypedSerializer;
        }
        catch (IllegalArgumentException ex) {
            throw new JsonMappingException(ex.getMessage(), null, ex);
        }
    }
    
    protected JsonSerializer<Object> _createAndCacheUntypedSerializer(final Class<?> clazz) throws JsonMappingException {
        try {
            final JsonSerializer<Object> createUntypedSerializer = this._createUntypedSerializer(this._config.constructType(clazz));
            if (createUntypedSerializer != null) {
                this._serializerCache.addAndResolveNonTypedSerializer(clazz, createUntypedSerializer, this);
            }
            return createUntypedSerializer;
        }
        catch (IllegalArgumentException ex) {
            throw new JsonMappingException(ex.getMessage(), null, ex);
        }
    }
    
    protected JsonSerializer<Object> _createUntypedSerializer(final JavaType javaType) throws JsonMappingException {
        return this._serializerFactory.createSerializer(this, javaType);
    }
    
    protected final DateFormat _dateFormat() {
        if (this._dateFormat != null) {
            return this._dateFormat;
        }
        return this._dateFormat = (DateFormat)this._config.getDateFormat().clone();
    }
    
    protected JsonSerializer<Object> _handleContextual(final JsonSerializer<?> jsonSerializer, final BeanProperty beanProperty) throws JsonMappingException {
        JsonSerializer<?> contextual = jsonSerializer;
        if (jsonSerializer instanceof ContextualSerializer) {
            contextual = ((ContextualSerializer)jsonSerializer).createContextual(this, beanProperty);
        }
        return (JsonSerializer<Object>)contextual;
    }
    
    protected JsonSerializer<Object> _handleContextualResolvable(final JsonSerializer<?> jsonSerializer, final BeanProperty beanProperty) throws JsonMappingException {
        if (jsonSerializer instanceof ResolvableSerializer) {
            ((ResolvableSerializer)jsonSerializer).resolve(this);
        }
        return this._handleContextual(jsonSerializer, beanProperty);
    }
    
    protected JsonSerializer<Object> _handleResolvable(final JsonSerializer<?> jsonSerializer) throws JsonMappingException {
        if (jsonSerializer instanceof ResolvableSerializer) {
            ((ResolvableSerializer)jsonSerializer).resolve(this);
        }
        return (JsonSerializer<Object>)jsonSerializer;
    }
    
    public void defaultSerializeDateKey(final long n, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        if (this.isEnabled(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)) {
            jsonGenerator.writeFieldName(String.valueOf(n));
            return;
        }
        jsonGenerator.writeFieldName(this._dateFormat().format(new Date(n)));
    }
    
    public void defaultSerializeDateKey(final Date date, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        if (this.isEnabled(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)) {
            jsonGenerator.writeFieldName(String.valueOf(date.getTime()));
            return;
        }
        jsonGenerator.writeFieldName(this._dateFormat().format(date));
    }
    
    public final void defaultSerializeDateValue(final Date date, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        if (this.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)) {
            jsonGenerator.writeNumber(date.getTime());
            return;
        }
        jsonGenerator.writeString(this._dateFormat().format(date));
    }
    
    public final void defaultSerializeNull(final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        this.getDefaultNullValueSerializer().serialize(null, jsonGenerator, this);
    }
    
    public final void defaultSerializeValue(final Object o, final JsonGenerator jsonGenerator) throws IOException, JsonProcessingException {
        if (o == null) {
            this.getDefaultNullValueSerializer().serialize(null, jsonGenerator, this);
            return;
        }
        this.findTypedValueSerializer(o.getClass(), true, null).serialize(o, jsonGenerator, this);
    }
    
    public JsonSerializer<Object> findKeySerializer(final JavaType javaType, final BeanProperty beanProperty) throws JsonMappingException {
        return this._handleContextualResolvable(this._serializerFactory.createKeySerializer(this._config, javaType, this._keySerializer), beanProperty);
    }
    
    public JsonSerializer<Object> findNullKeySerializer(final JavaType javaType, final BeanProperty beanProperty) throws JsonMappingException {
        return this.getDefaultNullKeySerializer();
    }
    
    public JsonSerializer<Object> findNullValueSerializer(final BeanProperty beanProperty) throws JsonMappingException {
        return this.getDefaultNullValueSerializer();
    }
    
    public abstract WritableObjectId findObjectId(final Object p0, final ObjectIdGenerator<?> p1);
    
    public JsonSerializer<Object> findTypedValueSerializer(final JavaType javaType, final boolean b, final BeanProperty beanProperty) throws JsonMappingException {
        JsonSerializer<Object> jsonSerializer = this._knownSerializers.typedValueSerializer(javaType);
        if (jsonSerializer == null && (jsonSerializer = this._serializerCache.typedValueSerializer(javaType)) == null) {
            final JsonSerializer<Object> valueSerializer = this.findValueSerializer(javaType, beanProperty);
            final TypeSerializer typeSerializer = this._serializerFactory.createTypeSerializer(this._config, javaType);
            JsonSerializer<Object> jsonSerializer2;
            if (typeSerializer != null) {
                jsonSerializer2 = new TypeWrappedSerializer(typeSerializer.forProperty(beanProperty), valueSerializer);
            }
            else {
                jsonSerializer2 = valueSerializer;
            }
            jsonSerializer = jsonSerializer2;
            if (b) {
                this._serializerCache.addTypedSerializer(javaType, jsonSerializer2);
                return jsonSerializer2;
            }
        }
        return jsonSerializer;
    }
    
    public JsonSerializer<Object> findTypedValueSerializer(final Class<?> clazz, final boolean b, final BeanProperty beanProperty) throws JsonMappingException {
        JsonSerializer<Object> jsonSerializer = this._knownSerializers.typedValueSerializer(clazz);
        if (jsonSerializer == null && (jsonSerializer = this._serializerCache.typedValueSerializer(clazz)) == null) {
            final JsonSerializer<Object> valueSerializer = this.findValueSerializer(clazz, beanProperty);
            final TypeSerializer typeSerializer = this._serializerFactory.createTypeSerializer(this._config, this._config.constructType(clazz));
            JsonSerializer<Object> jsonSerializer2;
            if (typeSerializer != null) {
                jsonSerializer2 = new TypeWrappedSerializer(typeSerializer.forProperty(beanProperty), valueSerializer);
            }
            else {
                jsonSerializer2 = valueSerializer;
            }
            jsonSerializer = jsonSerializer2;
            if (b) {
                this._serializerCache.addTypedSerializer(clazz, jsonSerializer2);
                return jsonSerializer2;
            }
        }
        return jsonSerializer;
    }
    
    public JsonSerializer<Object> findValueSerializer(final JavaType javaType, final BeanProperty beanProperty) throws JsonMappingException {
        JsonSerializer<Object> jsonSerializer;
        if ((jsonSerializer = this._knownSerializers.untypedValueSerializer(javaType)) == null && (jsonSerializer = this._serializerCache.untypedValueSerializer(javaType)) == null && (jsonSerializer = this._createAndCacheUntypedSerializer(javaType)) == null) {
            return this.getUnknownTypeSerializer(javaType.getRawClass());
        }
        return this._handleContextual(jsonSerializer, beanProperty);
    }
    
    public JsonSerializer<Object> findValueSerializer(final Class<?> clazz, final BeanProperty beanProperty) throws JsonMappingException {
        JsonSerializer<Object> jsonSerializer;
        if ((jsonSerializer = this._knownSerializers.untypedValueSerializer(clazz)) == null && (jsonSerializer = this._serializerCache.untypedValueSerializer(clazz)) == null && (jsonSerializer = this._serializerCache.untypedValueSerializer(this._config.constructType(clazz))) == null && (jsonSerializer = this._createAndCacheUntypedSerializer(clazz)) == null) {
            return this.getUnknownTypeSerializer(clazz);
        }
        return this._handleContextual(jsonSerializer, beanProperty);
    }
    
    public final Class<?> getActiveView() {
        return this._serializationView;
    }
    
    public final AnnotationIntrospector getAnnotationIntrospector() {
        return this._config.getAnnotationIntrospector();
    }
    
    @Override
    public final SerializationConfig getConfig() {
        return this._config;
    }
    
    public JsonSerializer<Object> getDefaultNullKeySerializer() {
        return this._nullKeySerializer;
    }
    
    public JsonSerializer<Object> getDefaultNullValueSerializer() {
        return this._nullValueSerializer;
    }
    
    public final FilterProvider getFilterProvider() {
        return this._config.getFilterProvider();
    }
    
    public Locale getLocale() {
        return this._config.getLocale();
    }
    
    public TimeZone getTimeZone() {
        return this._config.getTimeZone();
    }
    
    @Override
    public final TypeFactory getTypeFactory() {
        return this._config.getTypeFactory();
    }
    
    public JsonSerializer<Object> getUnknownTypeSerializer(final Class<?> clazz) {
        return this._unknownTypeSerializer;
    }
    
    public final boolean isEnabled(final SerializationFeature serializationFeature) {
        return this._config.isEnabled(serializationFeature);
    }
    
    public abstract JsonSerializer<Object> serializerInstance(final Annotated p0, final Object p1) throws JsonMappingException;
}
