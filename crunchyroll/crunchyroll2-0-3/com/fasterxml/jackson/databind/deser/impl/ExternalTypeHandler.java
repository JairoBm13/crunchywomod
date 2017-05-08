// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import java.util.ArrayList;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import java.util.HashMap;

public class ExternalTypeHandler
{
    private final HashMap<String, Integer> _nameToPropertyIndex;
    private final ExtTypedProperty[] _properties;
    private final TokenBuffer[] _tokens;
    private final String[] _typeIds;
    
    protected ExternalTypeHandler(final ExternalTypeHandler externalTypeHandler) {
        this._properties = externalTypeHandler._properties;
        this._nameToPropertyIndex = externalTypeHandler._nameToPropertyIndex;
        final int length = this._properties.length;
        this._typeIds = new String[length];
        this._tokens = new TokenBuffer[length];
    }
    
    protected ExternalTypeHandler(final ExtTypedProperty[] properties, final HashMap<String, Integer> nameToPropertyIndex, final String[] typeIds, final TokenBuffer[] tokens) {
        this._properties = properties;
        this._nameToPropertyIndex = nameToPropertyIndex;
        this._typeIds = typeIds;
        this._tokens = tokens;
    }
    
    protected final Object _deserialize(JsonParser parser, final DeserializationContext deserializationContext, final int n, final String s) throws IOException, JsonProcessingException {
        final TokenBuffer tokenBuffer = new TokenBuffer(parser.getCodec());
        tokenBuffer.writeStartArray();
        tokenBuffer.writeString(s);
        final JsonParser parser2 = this._tokens[n].asParser(parser);
        parser2.nextToken();
        tokenBuffer.copyCurrentStructure(parser2);
        tokenBuffer.writeEndArray();
        parser = tokenBuffer.asParser(parser);
        parser.nextToken();
        return this._properties[n].getProperty().deserialize(parser, deserializationContext);
    }
    
    protected final void _deserializeAndSet(JsonParser parser, final DeserializationContext deserializationContext, final Object o, final int n, final String s) throws IOException, JsonProcessingException {
        final TokenBuffer tokenBuffer = new TokenBuffer(parser.getCodec());
        tokenBuffer.writeStartArray();
        tokenBuffer.writeString(s);
        final JsonParser parser2 = this._tokens[n].asParser(parser);
        parser2.nextToken();
        tokenBuffer.copyCurrentStructure(parser2);
        tokenBuffer.writeEndArray();
        parser = tokenBuffer.asParser(parser);
        parser.nextToken();
        this._properties[n].getProperty().deserializeAndSet(parser, deserializationContext, o);
    }
    
    public Object complete(final JsonParser jsonParser, final DeserializationContext deserializationContext, final PropertyValueBuffer propertyValueBuffer, final PropertyBasedCreator propertyBasedCreator) throws IOException, JsonProcessingException {
        final int length = this._properties.length;
        final Object[] array = new Object[length];
        int i = 0;
    Label_0048_Outer:
        while (i < length) {
            String defaultTypeId = this._typeIds[i];
            while (true) {
                Label_0120: {
                    if (defaultTypeId == null) {
                        if (this._tokens[i] != null) {
                            if (!this._properties[i].hasDefaultType()) {
                                throw deserializationContext.mappingException("Missing external type id property '" + this._properties[i].getTypePropertyName() + "'");
                            }
                            defaultTypeId = this._properties[i].getDefaultTypeId();
                            break Label_0120;
                        }
                    }
                    else {
                        if (this._tokens[i] == null) {
                            throw deserializationContext.mappingException("Missing property '" + this._properties[i].getProperty().getName() + "' for external type id '" + this._properties[i].getTypePropertyName());
                        }
                        break Label_0120;
                    }
                    ++i;
                    continue Label_0048_Outer;
                }
                array[i] = this._deserialize(jsonParser, deserializationContext, i, defaultTypeId);
                continue;
            }
        }
        for (int j = 0; j < length; ++j) {
            final SettableBeanProperty property = this._properties[j].getProperty();
            if (propertyBasedCreator.findCreatorProperty(property.getName()) != null) {
                propertyValueBuffer.assignParameter(property.getCreatorIndex(), array[j]);
            }
        }
        final Object build = propertyBasedCreator.build(deserializationContext, propertyValueBuffer);
        for (int k = 0; k < length; ++k) {
            final SettableBeanProperty property2 = this._properties[k].getProperty();
            if (propertyBasedCreator.findCreatorProperty(property2.getName()) == null) {
                property2.set(build, array[k]);
            }
        }
        return build;
    }
    
    public Object complete(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
        int i = 0;
    Label_0045_Outer:
        while (i < this._properties.length) {
            final String s = this._typeIds[i];
            while (true) {
                String defaultTypeId = null;
                Label_0200: {
                    if (s == null) {
                        final TokenBuffer tokenBuffer = this._tokens[i];
                        if (tokenBuffer != null) {
                            final JsonToken firstToken = tokenBuffer.firstToken();
                            defaultTypeId = s;
                            if (firstToken == null) {
                                break Label_0200;
                            }
                            defaultTypeId = s;
                            if (!firstToken.isScalarValue()) {
                                break Label_0200;
                            }
                            final JsonParser parser = tokenBuffer.asParser(jsonParser);
                            parser.nextToken();
                            final SettableBeanProperty property = this._properties[i].getProperty();
                            final Object deserializeIfNatural = TypeDeserializer.deserializeIfNatural(parser, deserializationContext, property.getType());
                            if (deserializeIfNatural != null) {
                                property.set(o, deserializeIfNatural);
                            }
                            else {
                                if (!this._properties[i].hasDefaultType()) {
                                    throw deserializationContext.mappingException("Missing external type id property '" + this._properties[i].getTypePropertyName() + "'");
                                }
                                defaultTypeId = this._properties[i].getDefaultTypeId();
                                break Label_0200;
                            }
                        }
                    }
                    else {
                        if (this._tokens[i] == null) {
                            throw deserializationContext.mappingException("Missing property '" + this._properties[i].getProperty().getName() + "' for external type id '" + this._properties[i].getTypePropertyName());
                        }
                        defaultTypeId = s;
                        break Label_0200;
                    }
                    ++i;
                    continue Label_0045_Outer;
                }
                this._deserializeAndSet(jsonParser, deserializationContext, o, i, defaultTypeId);
                continue;
            }
        }
        return o;
    }
    
    public boolean handlePropertyValue(final JsonParser jsonParser, final DeserializationContext deserializationContext, String s, final Object o) throws IOException, JsonProcessingException {
        final boolean b = false;
        final Integer n = this._nameToPropertyIndex.get(s);
        if (n == null) {
            return false;
        }
        final int intValue = n;
        int n2;
        if (this._properties[intValue].hasTypePropertyName(s)) {
            this._typeIds[intValue] = jsonParser.getText();
            jsonParser.skipChildren();
            if (o != null && this._tokens[intValue] != null) {
                n2 = 1;
            }
            else {
                n2 = 0;
            }
        }
        else {
            final TokenBuffer tokenBuffer = new TokenBuffer(jsonParser.getCodec());
            tokenBuffer.copyCurrentStructure(jsonParser);
            this._tokens[intValue] = tokenBuffer;
            n2 = (b ? 1 : 0);
            if (o != null) {
                n2 = (b ? 1 : 0);
                if (this._typeIds[intValue] != null) {
                    n2 = 1;
                }
            }
        }
        if (n2 != 0) {
            s = this._typeIds[intValue];
            this._typeIds[intValue] = null;
            this._deserializeAndSet(jsonParser, deserializationContext, o, intValue, s);
            this._tokens[intValue] = null;
        }
        return true;
    }
    
    public boolean handleTypePropertyValue(final JsonParser jsonParser, final DeserializationContext deserializationContext, String text, final Object o) throws IOException, JsonProcessingException {
        final boolean b = false;
        final Integer n = this._nameToPropertyIndex.get(text);
        if (n == null) {
            return false;
        }
        final int intValue = n;
        if (!this._properties[intValue].hasTypePropertyName(text)) {
            return false;
        }
        text = jsonParser.getText();
        boolean b2 = b;
        if (o != null) {
            b2 = b;
            if (this._tokens[intValue] != null) {
                b2 = true;
            }
        }
        if (b2) {
            this._deserializeAndSet(jsonParser, deserializationContext, o, intValue, text);
            this._tokens[intValue] = null;
        }
        else {
            this._typeIds[intValue] = text;
        }
        return true;
    }
    
    public ExternalTypeHandler start() {
        return new ExternalTypeHandler(this);
    }
    
    public static class Builder
    {
        private final HashMap<String, Integer> _nameToPropertyIndex;
        private final ArrayList<ExtTypedProperty> _properties;
        
        public Builder() {
            this._properties = new ArrayList<ExtTypedProperty>();
            this._nameToPropertyIndex = new HashMap<String, Integer>();
        }
        
        public void addExternal(final SettableBeanProperty settableBeanProperty, final TypeDeserializer typeDeserializer) {
            final Integer value = this._properties.size();
            this._properties.add(new ExtTypedProperty(settableBeanProperty, typeDeserializer));
            this._nameToPropertyIndex.put(settableBeanProperty.getName(), value);
            this._nameToPropertyIndex.put(typeDeserializer.getPropertyName(), value);
        }
        
        public ExternalTypeHandler build() {
            return new ExternalTypeHandler((ExtTypedProperty[])this._properties.toArray(new ExtTypedProperty[this._properties.size()]), this._nameToPropertyIndex, null, null);
        }
    }
    
    private static final class ExtTypedProperty
    {
        private final SettableBeanProperty _property;
        private final TypeDeserializer _typeDeserializer;
        private final String _typePropertyName;
        
        public ExtTypedProperty(final SettableBeanProperty property, final TypeDeserializer typeDeserializer) {
            this._property = property;
            this._typeDeserializer = typeDeserializer;
            this._typePropertyName = typeDeserializer.getPropertyName();
        }
        
        public String getDefaultTypeId() {
            final Class<?> defaultImpl = this._typeDeserializer.getDefaultImpl();
            if (defaultImpl == null) {
                return null;
            }
            return this._typeDeserializer.getTypeIdResolver().idFromValueAndType(null, defaultImpl);
        }
        
        public SettableBeanProperty getProperty() {
            return this._property;
        }
        
        public String getTypePropertyName() {
            return this._typePropertyName;
        }
        
        public boolean hasDefaultType() {
            return this._typeDeserializer.getDefaultImpl() != null;
        }
        
        public boolean hasTypePropertyName(final String s) {
            return s.equals(this._typePropertyName);
        }
    }
}
