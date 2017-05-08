// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.core.JsonParseException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.util.RootNameLookup;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import java.io.Serializable;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.core.ObjectCodec;

public class ObjectReader extends ObjectCodec implements Versioned, Serializable
{
    private static final JavaType JSON_NODE_TYPE;
    protected final DeserializationConfig _config;
    protected final DefaultDeserializationContext _context;
    protected final InjectableValues _injectableValues;
    protected final JsonFactory _jsonFactory;
    protected final JsonDeserializer<Object> _rootDeserializer;
    protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
    protected final RootNameLookup _rootNames;
    protected final boolean _unwrapRoot;
    protected final Object _valueToUpdate;
    
    static {
        JSON_NODE_TYPE = SimpleType.constructUnsafe(JsonNode.class);
    }
    
    protected static JsonToken _initForReading(final JsonParser jsonParser) throws IOException, JsonParseException, JsonMappingException {
        JsonToken jsonToken;
        if ((jsonToken = jsonParser.getCurrentToken()) == null && (jsonToken = jsonParser.nextToken()) == null) {
            throw JsonMappingException.from(jsonParser, "No content to map due to end-of-input");
        }
        return jsonToken;
    }
    
    protected JsonNode _bindAsTree(final JsonParser jsonParser) throws IOException, JsonParseException, JsonMappingException {
        final JsonToken initForReading = _initForReading(jsonParser);
        JsonNode instance;
        if (initForReading == JsonToken.VALUE_NULL || initForReading == JsonToken.END_ARRAY || initForReading == JsonToken.END_OBJECT) {
            instance = NullNode.instance;
        }
        else {
            final DefaultDeserializationContext deserializationContext = this.createDeserializationContext(jsonParser, this._config);
            final JsonDeserializer<Object> findRootDeserializer = this._findRootDeserializer(deserializationContext, ObjectReader.JSON_NODE_TYPE);
            if (this._unwrapRoot) {
                instance = (JsonNode)this._unwrapAndDeserialize(jsonParser, deserializationContext, ObjectReader.JSON_NODE_TYPE, findRootDeserializer);
            }
            else {
                instance = (JsonNode)findRootDeserializer.deserialize(jsonParser, deserializationContext);
            }
        }
        jsonParser.clearCurrentToken();
        return instance;
    }
    
    protected JsonDeserializer<Object> _findRootDeserializer(final DeserializationContext deserializationContext, final JavaType javaType) throws JsonMappingException {
        JsonDeserializer<Object> rootDeserializer;
        if (this._rootDeserializer != null) {
            rootDeserializer = this._rootDeserializer;
        }
        else {
            if (javaType == null) {
                throw new JsonMappingException("No value type configured for ObjectReader");
            }
            if ((rootDeserializer = this._rootDeserializers.get(javaType)) == null) {
                final JsonDeserializer<Object> rootValueDeserializer = deserializationContext.findRootValueDeserializer(javaType);
                if (rootValueDeserializer == null) {
                    throw new JsonMappingException("Can not find a deserializer for type " + javaType);
                }
                this._rootDeserializers.put(javaType, rootValueDeserializer);
                return rootValueDeserializer;
            }
        }
        return rootDeserializer;
    }
    
    protected Object _unwrapAndDeserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext, final JavaType javaType, final JsonDeserializer<Object> jsonDeserializer) throws IOException, JsonParseException, JsonMappingException {
        String s;
        if ((s = this._config.getRootName()) == null) {
            s = this._rootNames.findRootName(javaType, this._config).getValue();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            throw JsonMappingException.from(jsonParser, "Current token not START_OBJECT (needed to unwrap root name '" + s + "'), but " + jsonParser.getCurrentToken());
        }
        if (jsonParser.nextToken() != JsonToken.FIELD_NAME) {
            throw JsonMappingException.from(jsonParser, "Current token not FIELD_NAME (to contain expected root name '" + s + "'), but " + jsonParser.getCurrentToken());
        }
        final String currentName = jsonParser.getCurrentName();
        if (!s.equals(currentName)) {
            throw JsonMappingException.from(jsonParser, "Root name '" + currentName + "' does not match expected ('" + s + "') for type " + javaType);
        }
        jsonParser.nextToken();
        Object o;
        if (this._valueToUpdate == null) {
            o = jsonDeserializer.deserialize(jsonParser, deserializationContext);
        }
        else {
            jsonDeserializer.deserialize(jsonParser, deserializationContext, this._valueToUpdate);
            o = this._valueToUpdate;
        }
        if (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            throw JsonMappingException.from(jsonParser, "Current token not END_OBJECT (to match wrapper object with root name '" + s + "'), but " + jsonParser.getCurrentToken());
        }
        return o;
    }
    
    protected DefaultDeserializationContext createDeserializationContext(final JsonParser jsonParser, final DeserializationConfig deserializationConfig) {
        return this._context.createInstance(deserializationConfig, jsonParser, this._injectableValues);
    }
    
    @Override
    public JsonFactory getFactory() {
        return this._jsonFactory;
    }
    
    @Deprecated
    @Override
    public JsonFactory getJsonFactory() {
        return this._jsonFactory;
    }
    
    @Override
    public <T extends TreeNode> T readTree(final JsonParser jsonParser) throws IOException, JsonProcessingException {
        return (T)this._bindAsTree(jsonParser);
    }
    
    @Override
    public Version version() {
        return PackageVersion.VERSION;
    }
    
    @Override
    public void writeValue(final JsonGenerator jsonGenerator, final Object o) throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException("Not implemented for ObjectReader");
    }
}
