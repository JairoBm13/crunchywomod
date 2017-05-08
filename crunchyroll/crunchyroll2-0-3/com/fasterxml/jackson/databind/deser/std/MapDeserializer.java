// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import java.lang.reflect.InvocationTargetException;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import java.util.Collection;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.KeyDeserializer;
import java.util.HashSet;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import java.util.Map;

@JacksonStdImpl
public class MapDeserializer extends ContainerDeserializerBase<Map<Object, Object>> implements ContextualDeserializer, ResolvableDeserializer
{
    protected JsonDeserializer<Object> _delegateDeserializer;
    protected final boolean _hasDefaultCreator;
    protected HashSet<String> _ignorableProperties;
    protected final KeyDeserializer _keyDeserializer;
    protected final JavaType _mapType;
    protected PropertyBasedCreator _propertyBasedCreator;
    protected boolean _standardStringKey;
    protected final JsonDeserializer<Object> _valueDeserializer;
    protected final ValueInstantiator _valueInstantiator;
    protected final TypeDeserializer _valueTypeDeserializer;
    
    public MapDeserializer(final JavaType mapType, final ValueInstantiator valueInstantiator, final KeyDeserializer keyDeserializer, final JsonDeserializer<Object> valueDeserializer, final TypeDeserializer valueTypeDeserializer) {
        super(Map.class);
        this._mapType = mapType;
        this._keyDeserializer = keyDeserializer;
        this._valueDeserializer = valueDeserializer;
        this._valueTypeDeserializer = valueTypeDeserializer;
        this._valueInstantiator = valueInstantiator;
        this._hasDefaultCreator = valueInstantiator.canCreateUsingDefault();
        this._delegateDeserializer = null;
        this._propertyBasedCreator = null;
        this._standardStringKey = this._isStdKeyDeser(mapType, keyDeserializer);
    }
    
    protected MapDeserializer(final MapDeserializer mapDeserializer, final KeyDeserializer keyDeserializer, final JsonDeserializer<Object> valueDeserializer, final TypeDeserializer valueTypeDeserializer, final HashSet<String> ignorableProperties) {
        super(mapDeserializer._valueClass);
        this._mapType = mapDeserializer._mapType;
        this._keyDeserializer = keyDeserializer;
        this._valueDeserializer = valueDeserializer;
        this._valueTypeDeserializer = valueTypeDeserializer;
        this._valueInstantiator = mapDeserializer._valueInstantiator;
        this._propertyBasedCreator = mapDeserializer._propertyBasedCreator;
        this._delegateDeserializer = mapDeserializer._delegateDeserializer;
        this._hasDefaultCreator = mapDeserializer._hasDefaultCreator;
        this._ignorableProperties = ignorableProperties;
        this._standardStringKey = this._isStdKeyDeser(this._mapType, keyDeserializer);
    }
    
    public Map<Object, Object> _deserializeUsingCreator(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final PropertyBasedCreator propertyBasedCreator = this._propertyBasedCreator;
        final PropertyValueBuffer startBuilding = propertyBasedCreator.startBuilding(jsonParser, deserializationContext, null);
        JsonToken jsonToken;
        if ((jsonToken = jsonParser.getCurrentToken()) == JsonToken.START_OBJECT) {
            jsonToken = jsonParser.nextToken();
        }
        final JsonDeserializer<Object> valueDeserializer = this._valueDeserializer;
        final TypeDeserializer valueTypeDeserializer = this._valueTypeDeserializer;
        while (jsonToken == JsonToken.FIELD_NAME) {
            final String currentName = jsonParser.getCurrentName();
            final JsonToken nextToken = jsonParser.nextToken();
            Label_0092: {
                if (this._ignorableProperties != null && this._ignorableProperties.contains(currentName)) {
                    jsonParser.skipChildren();
                }
                else {
                    final SettableBeanProperty creatorProperty = propertyBasedCreator.findCreatorProperty(currentName);
                    if (creatorProperty != null) {
                        if (!startBuilding.assignParameter(creatorProperty.getCreatorIndex(), creatorProperty.deserialize(jsonParser, deserializationContext))) {
                            break Label_0092;
                        }
                        jsonParser.nextToken();
                        try {
                            final Map map = (Map)propertyBasedCreator.build(deserializationContext, startBuilding);
                            this._readAndBind(jsonParser, deserializationContext, map);
                            return (Map<Object, Object>)map;
                        }
                        catch (Exception ex) {
                            this.wrapAndThrow(ex, this._mapType.getRawClass());
                            return null;
                        }
                    }
                    final Object deserializeKey = this._keyDeserializer.deserializeKey(jsonParser.getCurrentName(), deserializationContext);
                    Object o;
                    if (nextToken == JsonToken.VALUE_NULL) {
                        o = null;
                    }
                    else if (valueTypeDeserializer == null) {
                        o = valueDeserializer.deserialize(jsonParser, deserializationContext);
                    }
                    else {
                        o = valueDeserializer.deserializeWithType(jsonParser, deserializationContext, valueTypeDeserializer);
                    }
                    startBuilding.bufferMapProperty(deserializeKey, o);
                }
            }
            jsonToken = jsonParser.nextToken();
        }
        try {
            return (Map<Object, Object>)propertyBasedCreator.build(deserializationContext, startBuilding);
        }
        catch (Exception ex2) {
            this.wrapAndThrow(ex2, this._mapType.getRawClass());
            return null;
        }
    }
    
    protected final boolean _isStdKeyDeser(JavaType keyType, final KeyDeserializer keyDeserializer) {
        if (keyDeserializer != null) {
            keyType = keyType.getKeyType();
            if (keyType != null) {
                final Class<?> rawClass = keyType.getRawClass();
                if ((rawClass != String.class && rawClass != Object.class) || !this.isDefaultKeyDeserializer(keyDeserializer)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    protected final void _readAndBind(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Map<Object, Object> map) throws IOException, JsonProcessingException {
        JsonToken jsonToken;
        if ((jsonToken = jsonParser.getCurrentToken()) == JsonToken.START_OBJECT) {
            jsonToken = jsonParser.nextToken();
        }
        final KeyDeserializer keyDeserializer = this._keyDeserializer;
        final JsonDeserializer<Object> valueDeserializer = this._valueDeserializer;
        final TypeDeserializer valueTypeDeserializer = this._valueTypeDeserializer;
        while (jsonToken == JsonToken.FIELD_NAME) {
            final String currentName = jsonParser.getCurrentName();
            final Object deserializeKey = keyDeserializer.deserializeKey(currentName, deserializationContext);
            final JsonToken nextToken = jsonParser.nextToken();
            if (this._ignorableProperties != null && this._ignorableProperties.contains(currentName)) {
                jsonParser.skipChildren();
            }
            else {
                Object o;
                if (nextToken == JsonToken.VALUE_NULL) {
                    o = null;
                }
                else if (valueTypeDeserializer == null) {
                    o = valueDeserializer.deserialize(jsonParser, deserializationContext);
                }
                else {
                    o = valueDeserializer.deserializeWithType(jsonParser, deserializationContext, valueTypeDeserializer);
                }
                map.put(deserializeKey, o);
            }
            jsonToken = jsonParser.nextToken();
        }
    }
    
    protected final void _readAndBindStringMap(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Map<Object, Object> map) throws IOException, JsonProcessingException {
        JsonToken jsonToken;
        if ((jsonToken = jsonParser.getCurrentToken()) == JsonToken.START_OBJECT) {
            jsonToken = jsonParser.nextToken();
        }
        final JsonDeserializer<Object> valueDeserializer = this._valueDeserializer;
        final TypeDeserializer valueTypeDeserializer = this._valueTypeDeserializer;
        while (jsonToken == JsonToken.FIELD_NAME) {
            final String currentName = jsonParser.getCurrentName();
            final JsonToken nextToken = jsonParser.nextToken();
            if (this._ignorableProperties != null && this._ignorableProperties.contains(currentName)) {
                jsonParser.skipChildren();
            }
            else {
                Object o;
                if (nextToken == JsonToken.VALUE_NULL) {
                    o = null;
                }
                else if (valueTypeDeserializer == null) {
                    o = valueDeserializer.deserialize(jsonParser, deserializationContext);
                }
                else {
                    o = valueDeserializer.deserializeWithType(jsonParser, deserializationContext, valueTypeDeserializer);
                }
                map.put(currentName, o);
            }
            jsonToken = jsonParser.nextToken();
        }
    }
    
    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext deserializationContext, final BeanProperty beanProperty) throws JsonMappingException {
        KeyDeserializer keyDeserializer = this._keyDeserializer;
        if (keyDeserializer == null) {
            keyDeserializer = deserializationContext.findKeyDeserializer(this._mapType.getKeyType(), beanProperty);
        }
        else if (keyDeserializer instanceof ContextualKeyDeserializer) {
            keyDeserializer = ((ContextualKeyDeserializer)keyDeserializer).createContextual(deserializationContext, beanProperty);
        }
        final JsonDeserializer<?> convertingContentDeserializer = this.findConvertingContentDeserializer(deserializationContext, beanProperty, this._valueDeserializer);
        JsonDeserializer<?> jsonDeserializer;
        if (convertingContentDeserializer == null) {
            jsonDeserializer = deserializationContext.findContextualValueDeserializer(this._mapType.getContentType(), beanProperty);
        }
        else {
            jsonDeserializer = convertingContentDeserializer;
            if (convertingContentDeserializer instanceof ContextualDeserializer) {
                jsonDeserializer = ((ContextualDeserializer)convertingContentDeserializer).createContextual(deserializationContext, beanProperty);
            }
        }
        final TypeDeserializer valueTypeDeserializer = this._valueTypeDeserializer;
        TypeDeserializer forProperty;
        if ((forProperty = valueTypeDeserializer) != null) {
            forProperty = valueTypeDeserializer.forProperty(beanProperty);
        }
        final HashSet<String> ignorableProperties = this._ignorableProperties;
        final AnnotationIntrospector annotationIntrospector = deserializationContext.getAnnotationIntrospector();
        if (annotationIntrospector != null && beanProperty != null) {
            final String[] propertiesToIgnore = annotationIntrospector.findPropertiesToIgnore(beanProperty.getMember());
            if (propertiesToIgnore != null) {
                HashSet<String> set;
                if (ignorableProperties == null) {
                    set = new HashSet<String>();
                }
                else {
                    set = new HashSet<String>(ignorableProperties);
                }
                final int length = propertiesToIgnore.length;
                int n = 0;
                while (true) {
                    final HashSet<String> set2 = set;
                    if (n >= length) {
                        return this.withResolved(keyDeserializer, forProperty, jsonDeserializer, set2);
                    }
                    set.add(propertiesToIgnore[n]);
                    ++n;
                }
            }
        }
        final HashSet<String> set2 = ignorableProperties;
        return this.withResolved(keyDeserializer, forProperty, jsonDeserializer, set2);
    }
    
    @Override
    public Map<Object, Object> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (this._propertyBasedCreator != null) {
            return this._deserializeUsingCreator(jsonParser, deserializationContext);
        }
        if (this._delegateDeserializer != null) {
            return (Map<Object, Object>)this._valueInstantiator.createUsingDelegate(deserializationContext, this._delegateDeserializer.deserialize(jsonParser, deserializationContext));
        }
        if (!this._hasDefaultCreator) {
            throw deserializationContext.instantiationException(this.getMapClass(), "No default constructor found");
        }
        final JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken != JsonToken.START_OBJECT && currentToken != JsonToken.FIELD_NAME && currentToken != JsonToken.END_OBJECT) {
            if (currentToken == JsonToken.VALUE_STRING) {
                return (Map<Object, Object>)this._valueInstantiator.createFromString(deserializationContext, jsonParser.getText());
            }
            throw deserializationContext.mappingException(this.getMapClass());
        }
        else {
            final Map map = (Map)this._valueInstantiator.createUsingDefault(deserializationContext);
            if (this._standardStringKey) {
                this._readAndBindStringMap(jsonParser, deserializationContext, map);
                return (Map<Object, Object>)map;
            }
            this._readAndBind(jsonParser, deserializationContext, map);
            return (Map<Object, Object>)map;
        }
    }
    
    @Override
    public Map<Object, Object> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Map<Object, Object> map) throws IOException, JsonProcessingException {
        final JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken != JsonToken.START_OBJECT && currentToken != JsonToken.FIELD_NAME) {
            throw deserializationContext.mappingException(this.getMapClass());
        }
        if (this._standardStringKey) {
            this._readAndBindStringMap(jsonParser, deserializationContext, map);
            return map;
        }
        this._readAndBind(jsonParser, deserializationContext, map);
        return map;
    }
    
    @Override
    public Object deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
    }
    
    @Override
    public JsonDeserializer<Object> getContentDeserializer() {
        return this._valueDeserializer;
    }
    
    public final Class<?> getMapClass() {
        return this._mapType.getRawClass();
    }
    
    @Override
    public void resolve(final DeserializationContext deserializationContext) throws JsonMappingException {
        if (this._valueInstantiator.canCreateUsingDelegate()) {
            final JavaType delegateType = this._valueInstantiator.getDelegateType(deserializationContext.getConfig());
            if (delegateType == null) {
                throw new IllegalArgumentException("Invalid delegate-creator definition for " + this._mapType + ": value instantiator (" + this._valueInstantiator.getClass().getName() + ") returned true for 'canCreateUsingDelegate()', but null for 'getDelegateType()'");
            }
            this._delegateDeserializer = this.findDeserializer(deserializationContext, delegateType, null);
        }
        if (this._valueInstantiator.canCreateFromObjectWith()) {
            this._propertyBasedCreator = PropertyBasedCreator.construct(deserializationContext, this._valueInstantiator, this._valueInstantiator.getFromObjectArguments(deserializationContext.getConfig()));
        }
        this._standardStringKey = this._isStdKeyDeser(this._mapType, this._keyDeserializer);
    }
    
    public void setIgnorableProperties(final String[] array) {
        HashSet<String> arrayToSet;
        if (array == null || array.length == 0) {
            arrayToSet = null;
        }
        else {
            arrayToSet = ArrayBuilders.arrayToSet(array);
        }
        this._ignorableProperties = arrayToSet;
    }
    
    protected MapDeserializer withResolved(final KeyDeserializer keyDeserializer, final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer, final HashSet<String> set) {
        if (this._keyDeserializer == keyDeserializer && this._valueDeserializer == jsonDeserializer && this._valueTypeDeserializer == typeDeserializer && this._ignorableProperties == set) {
            return this;
        }
        return new MapDeserializer(this, keyDeserializer, (JsonDeserializer<Object>)jsonDeserializer, typeDeserializer, set);
    }
    
    protected void wrapAndThrow(Throwable cause, final Object o) throws IOException {
        while (cause instanceof InvocationTargetException && cause.getCause() != null) {
            cause = cause.getCause();
        }
        if (cause instanceof Error) {
            throw (Error)cause;
        }
        if (cause instanceof IOException && !(cause instanceof JsonMappingException)) {
            throw (IOException)cause;
        }
        throw JsonMappingException.wrapWithPath(cause, o, null);
    }
}
