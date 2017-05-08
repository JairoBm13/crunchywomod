// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import java.util.Iterator;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.JavaType;
import java.util.Collection;

public class CollectionSerializer extends AsArraySerializerBase<Collection<?>>
{
    public CollectionSerializer(final JavaType javaType, final boolean b, final TypeSerializer typeSerializer, final BeanProperty beanProperty, final JsonSerializer<Object> jsonSerializer) {
        super(Collection.class, javaType, b, typeSerializer, beanProperty, jsonSerializer);
    }
    
    public CollectionSerializer(final CollectionSerializer collectionSerializer, final BeanProperty beanProperty, final TypeSerializer typeSerializer, final JsonSerializer<?> jsonSerializer) {
        super(collectionSerializer, beanProperty, typeSerializer, jsonSerializer);
    }
    
    public ContainerSerializer<?> _withValueTypeSerializer(final TypeSerializer typeSerializer) {
        return new CollectionSerializer(this._elementType, this._staticTyping, typeSerializer, this._property, this._elementSerializer);
    }
    
    @Override
    public boolean hasSingleElement(final Collection<?> collection) {
        final Iterator<?> iterator = collection.iterator();
        if (iterator.hasNext()) {
            iterator.next();
            if (!iterator.hasNext()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    
    public void serializeContents(final Collection<?> collection, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (this._elementSerializer != null) {
            this.serializeContentsUsing(collection, jsonGenerator, serializerProvider, this._elementSerializer);
        }
        else {
            final Iterator<?> iterator = collection.iterator();
            if (iterator.hasNext()) {
                PropertySerializerMap dynamicSerializers = this._dynamicSerializers;
                final TypeSerializer valueTypeSerializer = this._valueTypeSerializer;
                int n = 0;
                int n2;
                Object next = null;
                Class<?> class1 = null;
                JsonSerializer<Object> serializer;
                PropertySerializerMap dynamicSerializers2 = null;
                JsonSerializer<Object> jsonSerializer = null;
                JsonSerializer<Object> jsonSerializer2;
                Label_0174_Outer:Label_0079_Outer:
                while (true) {
                    n2 = n;
                    while (true) {
                    Label_0242:
                        while (true) {
                            Label_0224: {
                                try {
                                    next = iterator.next();
                                    if (next == null) {
                                        n2 = n;
                                        serializerProvider.defaultSerializeNull(jsonGenerator);
                                    }
                                    else {
                                        n2 = n;
                                        class1 = next.getClass();
                                        n2 = n;
                                        serializer = dynamicSerializers.serializerFor(class1);
                                        dynamicSerializers2 = dynamicSerializers;
                                        if ((jsonSerializer = serializer) == null) {
                                            n2 = n;
                                            if (!this._elementType.hasGenericTypes()) {
                                                break Label_0224;
                                            }
                                            n2 = n;
                                            jsonSerializer2 = this._findAndAddDynamic(dynamicSerializers, serializerProvider.constructSpecializedType(this._elementType, class1), serializerProvider);
                                            n2 = n;
                                            dynamicSerializers2 = this._dynamicSerializers;
                                            jsonSerializer = jsonSerializer2;
                                        }
                                        if (valueTypeSerializer != null) {
                                            break Label_0242;
                                        }
                                        n2 = n;
                                        jsonSerializer.serialize(next, jsonGenerator, serializerProvider);
                                        dynamicSerializers = dynamicSerializers2;
                                    }
                                    n2 = ++n;
                                    if (!iterator.hasNext()) {
                                        return;
                                    }
                                    continue Label_0174_Outer;
                                }
                                catch (Exception ex) {
                                    this.wrapAndThrow(serializerProvider, ex, collection, n2);
                                    return;
                                }
                            }
                            n2 = n;
                            jsonSerializer2 = this._findAndAddDynamic(dynamicSerializers, class1, serializerProvider);
                            continue Label_0079_Outer;
                        }
                        n2 = n;
                        jsonSerializer.serializeWithType(next, jsonGenerator, serializerProvider, valueTypeSerializer);
                        dynamicSerializers = dynamicSerializers2;
                        continue;
                    }
                }
            }
        }
    }
    
    public void serializeContentsUsing(final Collection<?> collection, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final JsonSerializer<Object> jsonSerializer) throws IOException, JsonGenerationException {
        final Iterator<?> iterator = collection.iterator();
        while (true) {
            while (true) {
                Label_0027: {
                    if (iterator.hasNext()) {
                        final TypeSerializer valueTypeSerializer = this._valueTypeSerializer;
                        final int n = 0;
                        break Label_0027;
                    }
                    return;
                    while (true) {
                    Label_0052_Outer:
                        while (true) {
                            final Object next;
                            while (true) {
                                try {
                                    serializerProvider.defaultSerializeNull(jsonGenerator);
                                    final int n = n + 1;
                                    if (!iterator.hasNext()) {
                                        return;
                                    }
                                    break;
                                    while (true) {
                                        jsonSerializer.serialize(next, jsonGenerator, serializerProvider);
                                        continue Label_0052_Outer;
                                        continue;
                                    }
                                }
                                // iftrue(Label_0095:, valueTypeSerializer != null)
                                catch (Exception ex) {
                                    final int n;
                                    this.wrapAndThrow(serializerProvider, ex, collection, n);
                                    continue;
                                }
                                break;
                            }
                            Label_0095: {
                                final TypeSerializer valueTypeSerializer;
                                jsonSerializer.serializeWithType(next, jsonGenerator, serializerProvider, valueTypeSerializer);
                            }
                            continue;
                        }
                    }
                }
                final Object next = iterator.next();
                if (next == null) {
                    continue;
                }
                break;
            }
            continue;
        }
    }
    
    @Override
    public CollectionSerializer withResolved(final BeanProperty beanProperty, final TypeSerializer typeSerializer, final JsonSerializer<?> jsonSerializer) {
        return new CollectionSerializer(this, beanProperty, typeSerializer, jsonSerializer);
    }
}
