// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.TreeNode;

public abstract class ContainerNode<T extends ContainerNode<T>> extends BaseJsonNode implements JsonNodeCreator
{
    protected final JsonNodeFactory _nodeFactory;
    
    protected ContainerNode(final JsonNodeFactory nodeFactory) {
        this._nodeFactory = nodeFactory;
    }
    
    @Override
    public String asText() {
        return "";
    }
    
    @Override
    public abstract JsonNode get(final String p0);
    
    public final NullNode nullNode() {
        return this._nodeFactory.nullNode();
    }
    
    @Override
    public abstract int size();
}
