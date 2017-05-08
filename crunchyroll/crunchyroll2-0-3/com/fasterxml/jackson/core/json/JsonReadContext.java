// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonStreamContext;

public final class JsonReadContext extends JsonStreamContext
{
    protected JsonReadContext _child;
    protected int _columnNr;
    protected String _currentName;
    protected int _lineNr;
    protected final JsonReadContext _parent;
    
    public JsonReadContext(final JsonReadContext parent, final int type, final int lineNr, final int columnNr) {
        this._child = null;
        this._type = type;
        this._parent = parent;
        this._lineNr = lineNr;
        this._columnNr = columnNr;
        this._index = -1;
    }
    
    public static JsonReadContext createRootContext() {
        return new JsonReadContext(null, 0, 1, 0);
    }
    
    public static JsonReadContext createRootContext(final int n, final int n2) {
        return new JsonReadContext(null, 0, n, n2);
    }
    
    public JsonReadContext createChildArrayContext(final int n, final int n2) {
        final JsonReadContext child = this._child;
        if (child == null) {
            return this._child = new JsonReadContext(this, 1, n, n2);
        }
        child.reset(1, n, n2);
        return child;
    }
    
    public JsonReadContext createChildObjectContext(final int n, final int n2) {
        final JsonReadContext child = this._child;
        if (child == null) {
            return this._child = new JsonReadContext(this, 2, n, n2);
        }
        child.reset(2, n, n2);
        return child;
    }
    
    public boolean expectComma() {
        final int index = this._index + 1;
        this._index = index;
        return this._type != 0 && index > 0;
    }
    
    public String getCurrentName() {
        return this._currentName;
    }
    
    public JsonReadContext getParent() {
        return this._parent;
    }
    
    public JsonLocation getStartLocation(final Object o) {
        return new JsonLocation(o, -1L, this._lineNr, this._columnNr);
    }
    
    protected void reset(final int type, final int lineNr, final int columnNr) {
        this._type = type;
        this._index = -1;
        this._lineNr = lineNr;
        this._columnNr = columnNr;
        this._currentName = null;
    }
    
    public void setCurrentName(final String currentName) {
        this._currentName = currentName;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(64);
        switch (this._type) {
            case 0: {
                sb.append("/");
                break;
            }
            case 1: {
                sb.append('[');
                sb.append(this.getCurrentIndex());
                sb.append(']');
                break;
            }
            case 2: {
                sb.append('{');
                if (this._currentName != null) {
                    sb.append('\"');
                    CharTypes.appendQuoted(sb, this._currentName);
                    sb.append('\"');
                }
                else {
                    sb.append('?');
                }
                sb.append('}');
                break;
            }
        }
        return sb.toString();
    }
}
