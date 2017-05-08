// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.TreeNode;
import java.util.Iterator;
import com.fasterxml.jackson.core.JsonToken;
import java.util.LinkedHashMap;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;

public final class ObjectNode extends ContainerNode<ObjectNode>
{
    private final Map<String, JsonNode> _children;
    
    public ObjectNode(final JsonNodeFactory jsonNodeFactory) {
        super(jsonNodeFactory);
        this._children = new LinkedHashMap<String, JsonNode>();
    }
    
    @Override
    public JsonToken asToken() {
        return JsonToken.START_OBJECT;
    }
    
    @Override
    public Iterator<JsonNode> elements() {
        return this._children.values().iterator();
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        boolean b2;
        if (o == this) {
            b2 = true;
        }
        else {
            b2 = b;
            if (o != null) {
                b2 = b;
                if (this.getClass() == o.getClass()) {
                    return this._children.equals(((ObjectNode)o)._children);
                }
            }
        }
        return b2;
    }
    
    @Override
    public Iterator<Map.Entry<String, JsonNode>> fields() {
        return this._children.entrySet().iterator();
    }
    
    @Override
    public JsonNode get(final String s) {
        return this._children.get(s);
    }
    
    @Override
    public JsonNodeType getNodeType() {
        return JsonNodeType.OBJECT;
    }
    
    @Override
    public int hashCode() {
        return this._children.hashCode();
    }
    
    @Override
    public JsonNode path(final int n) {
        return MissingNode.getInstance();
    }
    
    @Override
    public JsonNode path(final String s) {
        final JsonNode jsonNode = this._children.get(s);
        if (jsonNode != null) {
            return jsonNode;
        }
        return MissingNode.getInstance();
    }
    
    public JsonNode replace(final String s, final JsonNode jsonNode) {
        JsonNode nullNode = jsonNode;
        if (jsonNode == null) {
            nullNode = this.nullNode();
        }
        return this._children.put(s, nullNode);
    }
    
    @Override
    public void serialize(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        for (final Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
            jsonGenerator.writeFieldName(entry.getKey());
            entry.getValue().serialize(jsonGenerator, serializerProvider);
        }
        jsonGenerator.writeEndObject();
    }
    
    @Override
    public void serializeWithType(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonProcessingException {
        typeSerializer.writeTypePrefixForObject(this, jsonGenerator);
        for (final Map.Entry<String, JsonNode> entry : this._children.entrySet()) {
            jsonGenerator.writeFieldName(entry.getKey());
            entry.getValue().serialize(jsonGenerator, serializerProvider);
        }
        typeSerializer.writeTypeSuffixForObject(this, jsonGenerator);
    }
    
    @Override
    public int size() {
        return this._children.size();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder((this.size() << 4) + 32);
        sb.append("{");
        final Iterator<Map.Entry<String, JsonNode>> iterator = this._children.entrySet().iterator();
        int n = 0;
        while (iterator.hasNext()) {
            final Map.Entry<String, JsonNode> entry = iterator.next();
            if (n > 0) {
                sb.append(",");
            }
            TextNode.appendQuoted(sb, entry.getKey());
            sb.append(':');
            sb.append(entry.getValue().toString());
            ++n;
        }
        sb.append("}");
        return sb.toString();
    }
}
