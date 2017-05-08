// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;

public class ThrowableDeserializer extends BeanDeserializer
{
    public ThrowableDeserializer(final BeanDeserializer beanDeserializer) {
        super(beanDeserializer);
        this._vanillaProcessing = false;
    }
    
    protected ThrowableDeserializer(final BeanDeserializer beanDeserializer, final NameTransformer nameTransformer) {
        super(beanDeserializer, nameTransformer);
    }
    
    @Override
    public Object deserializeFromObject(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final int n = 0;
        Object deserializeUsingPropertyBased;
        if (this._propertyBasedCreator != null) {
            deserializeUsingPropertyBased = this._deserializeUsingPropertyBased(jsonParser, deserializationContext);
        }
        else {
            if (this._delegateDeserializer != null) {
                return this._valueInstantiator.createUsingDelegate(deserializationContext, this._delegateDeserializer.deserialize(jsonParser, deserializationContext));
            }
            if (this._beanType.isAbstract()) {
                throw JsonMappingException.from(jsonParser, "Can not instantiate abstract type " + this._beanType + " (need to add/enable type information?)");
            }
            final boolean canCreateFromString = this._valueInstantiator.canCreateFromString();
            final boolean canCreateUsingDefault = this._valueInstantiator.canCreateUsingDefault();
            if (!canCreateFromString && !canCreateUsingDefault) {
                throw new JsonMappingException("Can not deserialize Throwable of type " + this._beanType + " without having a default contructor, a single-String-arg constructor; or explicit @JsonCreator");
            }
            int n2 = 0;
            Object[] array = null;
            Object fromString = null;
            while (jsonParser.getCurrentToken() != JsonToken.END_OBJECT) {
                final String currentName = jsonParser.getCurrentName();
                final SettableBeanProperty find = this._beanProperties.find(currentName);
                jsonParser.nextToken();
                Object o = null;
                Object[] array3 = null;
                Label_0220: {
                    if (find != null) {
                        if (fromString != null) {
                            find.deserializeAndSet(jsonParser, deserializationContext, fromString);
                            final Object[] array2 = array;
                            o = fromString;
                            array3 = array2;
                        }
                        else {
                            Object[] array4;
                            if ((array4 = array) == null) {
                                final int size = this._beanProperties.size();
                                array4 = new Object[size + size];
                            }
                            final int n3 = n2 + 1;
                            array4[n2] = find;
                            n2 = n3 + 1;
                            array4[n3] = find.deserialize(jsonParser, deserializationContext);
                            o = fromString;
                            array3 = array4;
                        }
                    }
                    else {
                        if ("message".equals(currentName) && canCreateFromString) {
                            final Object o2 = fromString = this._valueInstantiator.createFromString(deserializationContext, jsonParser.getText());
                            if (array != null) {
                                for (int i = 0; i < n2; i += 2) {
                                    ((SettableBeanProperty)array[i]).set(o2, array[i + 1]);
                                }
                                o = o2;
                                array3 = null;
                                break Label_0220;
                            }
                        }
                        else {
                            if (this._ignorableProps != null && this._ignorableProps.contains(currentName)) {
                                jsonParser.skipChildren();
                                final Object o3 = fromString;
                                array3 = array;
                                o = o3;
                                break Label_0220;
                            }
                            if (this._anySetter != null) {
                                this._anySetter.deserializeAndSet(jsonParser, deserializationContext, fromString, currentName);
                                final Object o4 = fromString;
                                array3 = array;
                                o = o4;
                                break Label_0220;
                            }
                            this.handleUnknownProperty(jsonParser, deserializationContext, fromString, currentName);
                        }
                        final Object o5 = fromString;
                        array3 = array;
                        o = o5;
                    }
                }
                jsonParser.nextToken();
                final Object o6 = o;
                array = array3;
                fromString = o6;
            }
            if ((deserializeUsingPropertyBased = fromString) == null) {
                Object o7;
                if (canCreateFromString) {
                    o7 = this._valueInstantiator.createFromString(deserializationContext, null);
                }
                else {
                    o7 = this._valueInstantiator.createUsingDefault(deserializationContext);
                }
                deserializeUsingPropertyBased = o7;
                if (array != null) {
                    int n4 = n;
                    while (true) {
                        deserializeUsingPropertyBased = o7;
                        if (n4 >= n2) {
                            break;
                        }
                        ((SettableBeanProperty)array[n4]).set(o7, array[n4 + 1]);
                        n4 += 2;
                    }
                }
            }
        }
        return deserializeUsingPropertyBased;
    }
    
    @Override
    public JsonDeserializer<Object> unwrappingDeserializer(final NameTransformer nameTransformer) {
        if (this.getClass() != ThrowableDeserializer.class) {
            return this;
        }
        return new ThrowableDeserializer(this, nameTransformer);
    }
}
