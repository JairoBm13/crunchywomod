// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.Collection;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import java.text.ParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.util.LinkedNode;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.TimeZone;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.util.Locale;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.impl.TypeWrappedDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.introspect.Annotated;
import java.util.Calendar;
import java.util.Date;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import java.text.DateFormat;
import com.fasterxml.jackson.databind.deser.DeserializerCache;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import java.io.Serializable;

public abstract class DeserializationContext extends DatabindContext implements Serializable
{
    protected transient ArrayBuilders _arrayBuilders;
    protected final DeserializerCache _cache;
    protected final DeserializationConfig _config;
    protected transient DateFormat _dateFormat;
    protected final DeserializerFactory _factory;
    protected final int _featureFlags;
    protected final InjectableValues _injectableValues;
    protected transient ObjectBuffer _objectBuffer;
    protected transient JsonParser _parser;
    protected final Class<?> _view;
    
    protected DeserializationContext(final DeserializationContext deserializationContext, final DeserializationConfig config, final JsonParser parser, final InjectableValues injectableValues) {
        this._cache = deserializationContext._cache;
        this._factory = deserializationContext._factory;
        this._config = config;
        this._featureFlags = config.getDeserializationFeatures();
        this._view = config.getActiveView();
        this._parser = parser;
        this._injectableValues = injectableValues;
    }
    
    protected DeserializationContext(final DeserializerFactory factory, final DeserializerCache deserializerCache) {
        if (factory == null) {
            throw new IllegalArgumentException("Can not pass null DeserializerFactory");
        }
        this._factory = factory;
        DeserializerCache cache;
        if ((cache = deserializerCache) == null) {
            cache = new DeserializerCache();
        }
        this._cache = cache;
        this._featureFlags = 0;
        this._config = null;
        this._injectableValues = null;
        this._view = null;
    }
    
    protected String _calcName(final Class<?> clazz) {
        if (clazz.isArray()) {
            return this._calcName(clazz.getComponentType()) + "[]";
        }
        return clazz.getName();
    }
    
    protected String _desc(final String s) {
        String string = s;
        if (s.length() > 500) {
            string = s.substring(0, 500) + "]...[" + s.substring(s.length() - 500);
        }
        return string;
    }
    
    protected String _valueDesc() {
        try {
            return this._desc(this._parser.getText());
        }
        catch (Exception ex) {
            return "[N/A]";
        }
    }
    
    public Calendar constructCalendar(final Date time) {
        final Calendar instance = Calendar.getInstance(this.getTimeZone());
        instance.setTime(time);
        return instance;
    }
    
    public final JavaType constructType(final Class<?> clazz) {
        return this._config.constructType(clazz);
    }
    
    public abstract JsonDeserializer<Object> deserializerInstance(final Annotated p0, final Object p1) throws JsonMappingException;
    
    public JsonMappingException endOfInputException(final Class<?> clazz) {
        return JsonMappingException.from(this._parser, "Unexpected end-of-input when trying to deserialize a " + clazz.getName());
    }
    
    public Class<?> findClass(final String s) throws ClassNotFoundException {
        return ClassUtil.findClass(s);
    }
    
    public final JsonDeserializer<Object> findContextualValueDeserializer(final JavaType javaType, final BeanProperty beanProperty) throws JsonMappingException {
        JsonDeserializer<?> jsonDeserializer2;
        final JsonDeserializer<?> jsonDeserializer = jsonDeserializer2 = this._cache.findValueDeserializer(this, this._factory, javaType);
        if (jsonDeserializer != null) {
            jsonDeserializer2 = jsonDeserializer;
            if (jsonDeserializer instanceof ContextualDeserializer) {
                jsonDeserializer2 = ((ContextualDeserializer)jsonDeserializer).createContextual(this, beanProperty);
            }
        }
        return (JsonDeserializer<Object>)jsonDeserializer2;
    }
    
    public final Object findInjectableValue(final Object o, final BeanProperty beanProperty, final Object o2) {
        if (this._injectableValues == null) {
            throw new IllegalStateException("No 'injectableValues' configured, can not inject value with id [" + o + "]");
        }
        return this._injectableValues.findInjectableValue(o, this, beanProperty, o2);
    }
    
    public final KeyDeserializer findKeyDeserializer(final JavaType javaType, final BeanProperty beanProperty) throws JsonMappingException {
        KeyDeserializer keyDeserializer2;
        final KeyDeserializer keyDeserializer = keyDeserializer2 = this._cache.findKeyDeserializer(this, this._factory, javaType);
        if (keyDeserializer instanceof ContextualKeyDeserializer) {
            keyDeserializer2 = ((ContextualKeyDeserializer)keyDeserializer).createContextual(this, beanProperty);
        }
        return keyDeserializer2;
    }
    
    public abstract ReadableObjectId findObjectId(final Object p0, final ObjectIdGenerator<?> p1);
    
    public final JsonDeserializer<Object> findRootValueDeserializer(final JavaType javaType) throws JsonMappingException {
        final JsonDeserializer<Object> valueDeserializer = this._cache.findValueDeserializer(this, this._factory, javaType);
        JsonDeserializer<?> jsonDeserializer;
        if (valueDeserializer == null) {
            jsonDeserializer = null;
        }
        else {
            JsonDeserializer<?> contextual = valueDeserializer;
            if (valueDeserializer instanceof ContextualDeserializer) {
                contextual = ((ContextualDeserializer)valueDeserializer).createContextual(this, null);
            }
            final TypeDeserializer typeDeserializer = this._factory.findTypeDeserializer(this._config, javaType);
            jsonDeserializer = contextual;
            if (typeDeserializer != null) {
                return new TypeWrappedDeserializer(typeDeserializer.forProperty(null), (JsonDeserializer<Object>)contextual);
            }
        }
        return (JsonDeserializer<Object>)jsonDeserializer;
    }
    
    public final Class<?> getActiveView() {
        return this._view;
    }
    
    public final AnnotationIntrospector getAnnotationIntrospector() {
        return this._config.getAnnotationIntrospector();
    }
    
    public final ArrayBuilders getArrayBuilders() {
        if (this._arrayBuilders == null) {
            this._arrayBuilders = new ArrayBuilders();
        }
        return this._arrayBuilders;
    }
    
    public final Base64Variant getBase64Variant() {
        return this._config.getBase64Variant();
    }
    
    @Override
    public DeserializationConfig getConfig() {
        return this._config;
    }
    
    protected DateFormat getDateFormat() {
        if (this._dateFormat != null) {
            return this._dateFormat;
        }
        return this._dateFormat = (DateFormat)this._config.getDateFormat().clone();
    }
    
    public Locale getLocale() {
        return this._config.getLocale();
    }
    
    public final JsonNodeFactory getNodeFactory() {
        return this._config.getNodeFactory();
    }
    
    public final JsonParser getParser() {
        return this._parser;
    }
    
    public TimeZone getTimeZone() {
        return this._config.getTimeZone();
    }
    
    @Override
    public final TypeFactory getTypeFactory() {
        return this._config.getTypeFactory();
    }
    
    public boolean handleUnknownProperty(final JsonParser jsonParser, final JsonDeserializer<?> jsonDeserializer, final Object o, final String s) throws IOException, JsonProcessingException {
        LinkedNode<DeserializationProblemHandler> linkedNode = this._config.getProblemHandlers();
        if (linkedNode != null) {
            while (linkedNode != null) {
                if (linkedNode.value().handleUnknownProperty(this, jsonParser, jsonDeserializer, o, s)) {
                    return true;
                }
                linkedNode = linkedNode.next();
            }
        }
        return false;
    }
    
    public JsonMappingException instantiationException(final Class<?> clazz, final String s) {
        return JsonMappingException.from(this._parser, "Can not construct instance of " + clazz.getName() + ", problem: " + s);
    }
    
    public JsonMappingException instantiationException(final Class<?> clazz, final Throwable t) {
        return JsonMappingException.from(this._parser, "Can not construct instance of " + clazz.getName() + ", problem: " + t.getMessage(), t);
    }
    
    public final boolean isEnabled(final DeserializationFeature deserializationFeature) {
        return (this._featureFlags & deserializationFeature.getMask()) != 0x0;
    }
    
    public abstract KeyDeserializer keyDeserializerInstance(final Annotated p0, final Object p1) throws JsonMappingException;
    
    public final ObjectBuffer leaseObjectBuffer() {
        final ObjectBuffer objectBuffer = this._objectBuffer;
        if (objectBuffer == null) {
            return new ObjectBuffer();
        }
        this._objectBuffer = null;
        return objectBuffer;
    }
    
    public JsonMappingException mappingException(final Class<?> clazz) {
        return this.mappingException(clazz, this._parser.getCurrentToken());
    }
    
    public JsonMappingException mappingException(final Class<?> clazz, final JsonToken jsonToken) {
        return JsonMappingException.from(this._parser, "Can not deserialize instance of " + this._calcName(clazz) + " out of " + jsonToken + " token");
    }
    
    public JsonMappingException mappingException(final String s) {
        return JsonMappingException.from(this.getParser(), s);
    }
    
    public Date parseDate(final String s) throws IllegalArgumentException {
        try {
            return this.getDateFormat().parse(s);
        }
        catch (ParseException ex) {
            throw new IllegalArgumentException("Failed to parse Date value '" + s + "': " + ex.getMessage());
        }
    }
    
    public void reportUnknownProperty(final Object o, final String s, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        if (!this.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
            return;
        }
        Collection<Object> knownPropertyNames;
        if (jsonDeserializer == null) {
            knownPropertyNames = null;
        }
        else {
            knownPropertyNames = (Collection<Object>)jsonDeserializer.getKnownPropertyNames();
        }
        throw UnrecognizedPropertyException.from(this._parser, o, s, knownPropertyNames);
    }
    
    public final void returnObjectBuffer(final ObjectBuffer objectBuffer) {
        if (this._objectBuffer == null || objectBuffer.initialCapacity() >= this._objectBuffer.initialCapacity()) {
            this._objectBuffer = objectBuffer;
        }
    }
    
    public JsonMappingException unknownTypeException(final JavaType javaType, final String s) {
        return JsonMappingException.from(this._parser, "Could not resolve type id '" + s + "' into a subtype of " + javaType);
    }
    
    public JsonMappingException weirdKeyException(final Class<?> clazz, final String s, final String s2) {
        return InvalidFormatException.from(this._parser, "Can not construct Map key of type " + clazz.getName() + " from String \"" + this._desc(s) + "\": " + s2, s, clazz);
    }
    
    public JsonMappingException weirdNumberException(final Number n, final Class<?> clazz, final String s) {
        return InvalidFormatException.from(this._parser, "Can not construct instance of " + clazz.getName() + " from number value (" + this._valueDesc() + "): " + s, null, clazz);
    }
    
    public JsonMappingException weirdStringException(final String s, final Class<?> clazz, final String s2) {
        return InvalidFormatException.from(this._parser, "Can not construct instance of " + clazz.getName() + " from String value '" + this._valueDesc() + "': " + s2, s, clazz);
    }
    
    public JsonMappingException wrongTokenException(final JsonParser jsonParser, final JsonToken jsonToken, final String s) {
        return JsonMappingException.from(jsonParser, "Unexpected token (" + jsonParser.getCurrentToken() + "), expected " + jsonToken + ": " + s);
    }
}
