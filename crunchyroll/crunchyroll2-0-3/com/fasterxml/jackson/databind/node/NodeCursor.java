// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.node;

import java.util.Map;
import java.util.Iterator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonStreamContext;

abstract class NodeCursor extends JsonStreamContext
{
    protected String _currentName;
    protected final NodeCursor _parent;
    
    public NodeCursor(final int type, final NodeCursor parent) {
        this._type = type;
        this._index = -1;
        this._parent = parent;
    }
    
    public abstract boolean currentHasChildren();
    
    public abstract JsonNode currentNode();
    
    public abstract JsonToken endToken();
    
    public final String getCurrentName() {
        return this._currentName;
    }
    
    public final NodeCursor getParent() {
        return this._parent;
    }
    
    public final NodeCursor iterateChildren() {
        final JsonNode currentNode = this.currentNode();
        if (currentNode == null) {
            throw new IllegalStateException("No current node");
        }
        if (currentNode.isArray()) {
            return new Array(currentNode, this);
        }
        if (currentNode.isObject()) {
            return new Object(currentNode, this);
        }
        throw new IllegalStateException("Current node of type " + currentNode.getClass().getName());
    }
    
    public abstract JsonToken nextToken();
    
    protected static final class Array extends NodeCursor
    {
        protected Iterator<JsonNode> _contents;
        protected JsonNode _currentNode;
        
        public Array(final JsonNode jsonNode, final NodeCursor nodeCursor) {
            super(1, nodeCursor);
            this._contents = jsonNode.elements();
        }
        
        @Override
        public boolean currentHasChildren() {
            return ((ContainerNode)this.currentNode()).size() > 0;
        }
        
        @Override
        public JsonNode currentNode() {
            return this._currentNode;
        }
        
        @Override
        public JsonToken endToken() {
            return JsonToken.END_ARRAY;
        }
        
        @Override
        public JsonToken nextToken() {
            if (!this._contents.hasNext()) {
                this._currentNode = null;
                return null;
            }
            this._currentNode = this._contents.next();
            return this._currentNode.asToken();
        }
    }
    
    protected static final class Object extends NodeCursor
    {
        protected Iterator<Map.Entry<String, JsonNode>> _contents;
        protected Map.Entry<String, JsonNode> _current;
        protected boolean _needEntry;
        
        public Object(final JsonNode jsonNode, final NodeCursor nodeCursor) {
            super(2, nodeCursor);
            this._contents = ((ObjectNode)jsonNode).fields();
            this._needEntry = true;
        }
        
        @Override
        public boolean currentHasChildren() {
            return ((ContainerNode)this.currentNode()).size() > 0;
        }
        
        @Override
        public JsonNode currentNode() {
            if (this._current == null) {
                return null;
            }
            return this._current.getValue();
        }
        
        @Override
        public JsonToken endToken() {
            return JsonToken.END_OBJECT;
        }
        
        @Override
        public JsonToken nextToken() {
            if (!this._needEntry) {
                this._needEntry = true;
                return this._current.getValue().asToken();
            }
            if (!this._contents.hasNext()) {
                this._currentName = null;
                this._current = null;
                return null;
            }
            this._needEntry = false;
            this._current = this._contents.next();
            String currentName;
            if (this._current == null) {
                currentName = null;
            }
            else {
                currentName = this._current.getKey();
            }
            this._currentName = currentName;
            return JsonToken.FIELD_NAME;
        }
    }
    
    protected static final class RootValue extends NodeCursor
    {
        protected boolean _done;
        protected JsonNode _node;
        
        public RootValue(final JsonNode node, final NodeCursor nodeCursor) {
            super(0, nodeCursor);
            this._done = false;
            this._node = node;
        }
        
        @Override
        public boolean currentHasChildren() {
            return false;
        }
        
        @Override
        public JsonNode currentNode() {
            return this._node;
        }
        
        @Override
        public JsonToken endToken() {
            return null;
        }
        
        @Override
        public JsonToken nextToken() {
            if (!this._done) {
                this._done = true;
                return this._node.asToken();
            }
            this._node = null;
            return null;
        }
    }
}
