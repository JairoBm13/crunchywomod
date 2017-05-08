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
import java.util.ArrayList;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public final class ArrayNode extends ContainerNode<ArrayNode>
{
    private final List<JsonNode> _children;
    
    public ArrayNode(final JsonNodeFactory jsonNodeFactory) {
        super(jsonNodeFactory);
        this._children = new ArrayList<JsonNode>();
    }
    
    private ArrayNode _add(final JsonNode jsonNode) {
        this._children.add(jsonNode);
        return this;
    }
    
    public ArrayNode add(final JsonNode jsonNode) {
        JsonNode nullNode = jsonNode;
        if (jsonNode == null) {
            nullNode = this.nullNode();
        }
        this._add(nullNode);
        return this;
    }
    
    @Override
    public JsonToken asToken() {
        return JsonToken.START_ARRAY;
    }
    
    @Override
    public Iterator<JsonNode> elements() {
        return this._children.iterator();
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
                    return this._children.equals(((ArrayNode)o)._children);
                }
            }
        }
        return b2;
    }
    
    @Override
    public JsonNode get(final String s) {
        return null;
    }
    
    @Override
    public JsonNodeType getNodeType() {
        return JsonNodeType.ARRAY;
    }
    
    @Override
    public int hashCode() {
        return this._children.hashCode();
    }
    
    @Override
    public JsonNode path(final int n) {
        if (n >= 0 && n < this._children.size()) {
            return this._children.get(n);
        }
        return MissingNode.getInstance();
    }
    
    @Override
    public JsonNode path(final String s) {
        return MissingNode.getInstance();
    }
    
    @Override
    public void serialize(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();
        final Iterator<JsonNode> iterator = this._children.iterator();
        while (iterator.hasNext()) {
            ((BaseJsonNode)iterator.next()).serialize(jsonGenerator, serializerProvider);
        }
        jsonGenerator.writeEndArray();
    }
    
    @Override
    public void serializeWithType(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonProcessingException {
        typeSerializer.writeTypePrefixForArray(this, jsonGenerator);
        final Iterator<JsonNode> iterator = this._children.iterator();
        while (iterator.hasNext()) {
            ((BaseJsonNode)iterator.next()).serialize(jsonGenerator, serializerProvider);
        }
        typeSerializer.writeTypeSuffixForArray(this, jsonGenerator);
    }
    
    @Override
    public int size() {
        return this._children.size();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder((this.size() << 4) + 16);
        sb.append('[');
        for (int size = this._children.size(), i = 0; i < size; ++i) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(this._children.get(i).toString());
        }
        sb.append(']');
        return sb.toString();
    }
}
