// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Iterator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import java.util.Collection;
import com.fasterxml.jackson.databind.introspect.Annotated;
import java.util.TreeMap;
import java.util.SortedMap;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import java.util.HashSet;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.util.Map;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;

@JacksonStdImpl
public class MapSerializer extends ContainerSerializer<Map<?, ?>> implements ContextualSerializer
{
    protected static final JavaType UNSPECIFIED_TYPE;
    protected PropertySerializerMap _dynamicValueSerializers;
    protected final HashSet<String> _ignoredEntries;
    protected JsonSerializer<Object> _keySerializer;
    protected final JavaType _keyType;
    protected final BeanProperty _property;
    protected JsonSerializer<Object> _valueSerializer;
    protected final JavaType _valueType;
    protected final boolean _valueTypeIsStatic;
    protected final TypeSerializer _valueTypeSerializer;
    
    static {
        UNSPECIFIED_TYPE = TypeFactory.unknownType();
    }
    
    protected MapSerializer(final MapSerializer mapSerializer, final BeanProperty property, final JsonSerializer<?> keySerializer, final JsonSerializer<?> valueSerializer, final HashSet<String> ignoredEntries) {
        super(Map.class, false);
        this._ignoredEntries = ignoredEntries;
        this._keyType = mapSerializer._keyType;
        this._valueType = mapSerializer._valueType;
        this._valueTypeIsStatic = mapSerializer._valueTypeIsStatic;
        this._valueTypeSerializer = mapSerializer._valueTypeSerializer;
        this._keySerializer = (JsonSerializer<Object>)keySerializer;
        this._valueSerializer = (JsonSerializer<Object>)valueSerializer;
        this._dynamicValueSerializers = mapSerializer._dynamicValueSerializers;
        this._property = property;
    }
    
    protected MapSerializer(final MapSerializer mapSerializer, final TypeSerializer valueTypeSerializer) {
        super(Map.class, false);
        this._ignoredEntries = mapSerializer._ignoredEntries;
        this._keyType = mapSerializer._keyType;
        this._valueType = mapSerializer._valueType;
        this._valueTypeIsStatic = mapSerializer._valueTypeIsStatic;
        this._valueTypeSerializer = valueTypeSerializer;
        this._keySerializer = mapSerializer._keySerializer;
        this._valueSerializer = mapSerializer._valueSerializer;
        this._dynamicValueSerializers = mapSerializer._dynamicValueSerializers;
        this._property = mapSerializer._property;
    }
    
    protected MapSerializer(final HashSet<String> ignoredEntries, final JavaType keyType, final JavaType valueType, final boolean valueTypeIsStatic, final TypeSerializer valueTypeSerializer, final JsonSerializer<?> keySerializer, final JsonSerializer<?> valueSerializer) {
        super(Map.class, false);
        this._ignoredEntries = ignoredEntries;
        this._keyType = keyType;
        this._valueType = valueType;
        this._valueTypeIsStatic = valueTypeIsStatic;
        this._valueTypeSerializer = valueTypeSerializer;
        this._keySerializer = (JsonSerializer<Object>)keySerializer;
        this._valueSerializer = (JsonSerializer<Object>)valueSerializer;
        this._dynamicValueSerializers = PropertySerializerMap.emptyMap();
        this._property = null;
    }
    
    public static MapSerializer construct(final String[] array, JavaType contentType, boolean b, final TypeSerializer typeSerializer, final JsonSerializer<Object> jsonSerializer, final JsonSerializer<Object> jsonSerializer2) {
        final boolean b2 = false;
        final HashSet<String> set = toSet(array);
        JavaType javaType;
        if (contentType == null) {
            contentType = (javaType = MapSerializer.UNSPECIFIED_TYPE);
        }
        else {
            javaType = contentType.getKeyType();
            contentType = contentType.getContentType();
        }
        if (!b) {
            b = b2;
            if (contentType != null) {
                b = b2;
                if (contentType.isFinal()) {
                    b = true;
                }
            }
        }
        else if (contentType.getRawClass() == Object.class) {
            b = false;
        }
        return new MapSerializer(set, javaType, contentType, b, typeSerializer, jsonSerializer, jsonSerializer2);
    }
    
    private static HashSet<String> toSet(final String[] array) {
        HashSet<String> set;
        if (array == null || array.length == 0) {
            set = null;
        }
        else {
            final HashSet<String> set2 = new HashSet<String>(array.length);
            final int length = array.length;
            int n = 0;
            while (true) {
                set = set2;
                if (n >= length) {
                    break;
                }
                set2.add(array[n]);
                ++n;
            }
        }
        return set;
    }
    
    protected final JsonSerializer<Object> _findAndAddDynamic(final PropertySerializerMap propertySerializerMap, final JavaType javaType, final SerializerProvider serializerProvider) throws JsonMappingException {
        final PropertySerializerMap.SerializerAndMapResult andAddSerializer = propertySerializerMap.findAndAddSerializer(javaType, serializerProvider, this._property);
        if (propertySerializerMap != andAddSerializer.map) {
            this._dynamicValueSerializers = andAddSerializer.map;
        }
        return andAddSerializer.serializer;
    }
    
    protected final JsonSerializer<Object> _findAndAddDynamic(final PropertySerializerMap propertySerializerMap, final Class<?> clazz, final SerializerProvider serializerProvider) throws JsonMappingException {
        final PropertySerializerMap.SerializerAndMapResult andAddSerializer = propertySerializerMap.findAndAddSerializer(clazz, serializerProvider, this._property);
        if (propertySerializerMap != andAddSerializer.map) {
            this._dynamicValueSerializers = andAddSerializer.map;
        }
        return andAddSerializer.serializer;
    }
    
    protected Map<?, ?> _orderEntries(final Map<?, ?> map) {
        if (map instanceof SortedMap) {
            return map;
        }
        return new TreeMap<Object, Object>(map);
    }
    
    public MapSerializer _withValueTypeSerializer(final TypeSerializer typeSerializer) {
        return new MapSerializer(this, typeSerializer);
    }
    
    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
        Object serializerInstance = null;
        while (true) {
            Label_0352: {
                if (beanProperty == null) {
                    break Label_0352;
                }
                final AnnotatedMember member = beanProperty.getMember();
                if (member == null) {
                    break Label_0352;
                }
                final AnnotationIntrospector annotationIntrospector = serializerProvider.getAnnotationIntrospector();
                final Object keySerializer = annotationIntrospector.findKeySerializer(member);
                if (keySerializer != null) {
                    serializerInstance = serializerProvider.serializerInstance(member, keySerializer);
                }
                else {
                    serializerInstance = null;
                }
                final Object contentSerializer = annotationIntrospector.findContentSerializer(member);
                JsonSerializer<?> serializerInstance2;
                if (contentSerializer != null) {
                    serializerInstance2 = serializerProvider.serializerInstance(member, contentSerializer);
                }
                else {
                    serializerInstance2 = null;
                }
                JsonSerializer<?> valueSerializer = serializerInstance2;
                if (serializerInstance2 == null) {
                    valueSerializer = this._valueSerializer;
                }
                JsonSerializer<?> jsonSerializer = this.findConvertingContentSerializer(serializerProvider, beanProperty, valueSerializer);
                if (jsonSerializer == null) {
                    if ((this._valueTypeIsStatic && this._valueType.getRawClass() != Object.class) || this.hasContentTypeAnnotation(serializerProvider, beanProperty)) {
                        jsonSerializer = serializerProvider.findValueSerializer(this._valueType, beanProperty);
                    }
                }
                else if (jsonSerializer instanceof ContextualSerializer) {
                    jsonSerializer = ((ContextualSerializer)jsonSerializer).createContextual(serializerProvider, beanProperty);
                }
                while (true) {
                    Object keySerializer2;
                    if (serializerInstance == null) {
                        keySerializer2 = this._keySerializer;
                    }
                    else {
                        keySerializer2 = serializerInstance;
                    }
                    Object o;
                    if (keySerializer2 == null) {
                        o = serializerProvider.findKeySerializer(this._keyType, beanProperty);
                    }
                    else {
                        o = keySerializer2;
                        if (keySerializer2 instanceof ContextualSerializer) {
                            o = ((ContextualSerializer)keySerializer2).createContextual(serializerProvider, beanProperty);
                        }
                    }
                    HashSet<String> ignoredEntries = this._ignoredEntries;
                    final AnnotationIntrospector annotationIntrospector2 = serializerProvider.getAnnotationIntrospector();
                    if (annotationIntrospector2 != null && beanProperty != null) {
                        final String[] propertiesToIgnore = annotationIntrospector2.findPropertiesToIgnore(beanProperty.getMember());
                        if (propertiesToIgnore != null) {
                            HashSet<String> set;
                            if (ignoredEntries == null) {
                                set = new HashSet<String>();
                            }
                            else {
                                set = new HashSet<String>(ignoredEntries);
                            }
                            final int length = propertiesToIgnore.length;
                            int n = 0;
                            while (true) {
                                ignoredEntries = set;
                                if (n >= length) {
                                    break;
                                }
                                set.add(propertiesToIgnore[n]);
                                ++n;
                            }
                        }
                    }
                    return this.withResolved(beanProperty, (JsonSerializer<?>)o, jsonSerializer, ignoredEntries);
                    continue;
                }
            }
            JsonSerializer<?> serializerInstance2 = null;
            continue;
        }
    }
    
    @Override
    public boolean hasSingleElement(final Map<?, ?> map) {
        return map.size() == 1;
    }
    
    @Override
    public boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
    
    @Override
    public void serialize(final Map<?, ?> map, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        jsonGenerator.writeStartObject();
        if (!map.isEmpty()) {
            Map<?, ?> orderEntries = map;
            if (serializerProvider.isEnabled(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)) {
                orderEntries = this._orderEntries(map);
            }
            if (this._valueSerializer != null) {
                this.serializeFieldsUsing(orderEntries, jsonGenerator, serializerProvider, this._valueSerializer);
            }
            else {
                this.serializeFields(orderEntries, jsonGenerator, serializerProvider);
            }
        }
        jsonGenerator.writeEndObject();
    }
    
    public void serializeFields(final Map<?, ?> map, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (this._valueTypeSerializer != null) {
            this.serializeTypedFields(map, jsonGenerator, serializerProvider);
        }
        else {
            final JsonSerializer<Object> keySerializer = this._keySerializer;
            final HashSet<String> ignoredEntries = this._ignoredEntries;
            final boolean b = !serializerProvider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES);
            PropertySerializerMap dynamicValueSerializers = this._dynamicValueSerializers;
            for (final Map.Entry<K, Object> entry : map.entrySet()) {
                final Object value = entry.getValue();
                final K key = entry.getKey();
                if (key == null) {
                    serializerProvider.findNullKeySerializer(this._keyType, this._property).serialize(null, jsonGenerator, serializerProvider);
                }
                else {
                    if ((b && value == null) || (ignoredEntries != null && ignoredEntries.contains(key))) {
                        continue;
                    }
                    keySerializer.serialize(key, jsonGenerator, serializerProvider);
                }
                if (value == null) {
                    serializerProvider.defaultSerializeNull(jsonGenerator);
                }
                else {
                    final Class<?> class1 = value.getClass();
                    JsonSerializer<Object> serializer = dynamicValueSerializers.serializerFor(class1);
                    if (serializer == null) {
                        JsonSerializer<Object> jsonSerializer;
                        if (this._valueType.hasGenericTypes()) {
                            jsonSerializer = this._findAndAddDynamic(dynamicValueSerializers, serializerProvider.constructSpecializedType(this._valueType, class1), serializerProvider);
                        }
                        else {
                            jsonSerializer = this._findAndAddDynamic(dynamicValueSerializers, class1, serializerProvider);
                        }
                        final PropertySerializerMap dynamicValueSerializers2 = this._dynamicValueSerializers;
                        serializer = jsonSerializer;
                        dynamicValueSerializers = dynamicValueSerializers2;
                    }
                    try {
                        serializer.serialize(value, jsonGenerator, serializerProvider);
                    }
                    catch (Exception ex) {
                        this.wrapAndThrow(serializerProvider, ex, map, "" + key);
                    }
                }
            }
        }
    }
    
    protected void serializeFieldsUsing(final Map<?, ?> map, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final JsonSerializer<Object> jsonSerializer) throws IOException, JsonGenerationException {
        final JsonSerializer<Object> keySerializer = this._keySerializer;
        final HashSet<String> ignoredEntries = this._ignoredEntries;
        final TypeSerializer valueTypeSerializer = this._valueTypeSerializer;
        final boolean b = !serializerProvider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES);
        for (final Map.Entry<K, Object> entry : map.entrySet()) {
            final Object value = entry.getValue();
            final K key = entry.getKey();
            if (key == null) {
                serializerProvider.findNullKeySerializer(this._keyType, this._property).serialize(null, jsonGenerator, serializerProvider);
            }
            else {
                if ((b && value == null) || (ignoredEntries != null && ignoredEntries.contains(key))) {
                    continue;
                }
                keySerializer.serialize(key, jsonGenerator, serializerProvider);
            }
            if (value == null) {
                serializerProvider.defaultSerializeNull(jsonGenerator);
            }
            else if (valueTypeSerializer == null) {
                try {
                    jsonSerializer.serialize(value, jsonGenerator, serializerProvider);
                }
                catch (Exception ex) {
                    this.wrapAndThrow(serializerProvider, ex, map, "" + key);
                }
            }
            else {
                jsonSerializer.serializeWithType(value, jsonGenerator, serializerProvider, valueTypeSerializer);
            }
        }
    }
    
    protected void serializeTypedFields(final Map<?, ?> map, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        final JsonSerializer<Object> keySerializer = this._keySerializer;
        final HashSet<String> ignoredEntries = this._ignoredEntries;
        final boolean b = !serializerProvider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES);
        final Iterator<Map.Entry<?, Object>> iterator = (Iterator<Map.Entry<?, Object>>)map.entrySet().iterator();
        Class<?> clazz = null;
        JsonSerializer<Object> jsonSerializer = null;
        while (iterator.hasNext()) {
            final Map.Entry<K, Object> entry = iterator.next();
            final Object value = entry.getValue();
            final K key = entry.getKey();
            if (key == null) {
                serializerProvider.findNullKeySerializer(this._keyType, this._property).serialize(null, jsonGenerator, serializerProvider);
            }
            else {
                if ((b && value == null) || (ignoredEntries != null && ignoredEntries.contains(key))) {
                    continue;
                }
                keySerializer.serialize(key, jsonGenerator, serializerProvider);
            }
            if (value == null) {
                serializerProvider.defaultSerializeNull(jsonGenerator);
            }
            else {
                final Class<?> class1 = value.getClass();
                JsonSerializer<Object> jsonSerializer3;
                if (class1 == clazz) {
                    final JsonSerializer<Object> jsonSerializer2 = jsonSerializer;
                    jsonSerializer3 = jsonSerializer;
                    jsonSerializer = jsonSerializer2;
                }
                else {
                    if (this._valueType.hasGenericTypes()) {
                        jsonSerializer = serializerProvider.findValueSerializer(serializerProvider.constructSpecializedType(this._valueType, class1), this._property);
                    }
                    else {
                        jsonSerializer = serializerProvider.findValueSerializer(class1, this._property);
                    }
                    jsonSerializer3 = jsonSerializer;
                    clazz = class1;
                }
                try {
                    jsonSerializer3.serializeWithType(value, jsonGenerator, serializerProvider, this._valueTypeSerializer);
                }
                catch (Exception ex) {
                    this.wrapAndThrow(serializerProvider, ex, map, "" + key);
                }
            }
        }
    }
    
    @Override
    public void serializeWithType(final Map<?, ?> map, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        typeSerializer.writeTypePrefixForObject(map, jsonGenerator);
        Map<?, ?> orderEntries = map;
        if (!map.isEmpty()) {
            orderEntries = map;
            if (serializerProvider.isEnabled(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)) {
                orderEntries = this._orderEntries(map);
            }
            if (this._valueSerializer != null) {
                this.serializeFieldsUsing(orderEntries, jsonGenerator, serializerProvider, this._valueSerializer);
            }
            else {
                this.serializeFields(orderEntries, jsonGenerator, serializerProvider);
            }
        }
        typeSerializer.writeTypeSuffixForObject(orderEntries, jsonGenerator);
    }
    
    public MapSerializer withResolved(final BeanProperty beanProperty, final JsonSerializer<?> jsonSerializer, final JsonSerializer<?> jsonSerializer2, final HashSet<String> set) {
        return new MapSerializer(this, beanProperty, jsonSerializer, jsonSerializer2, set);
    }
}
