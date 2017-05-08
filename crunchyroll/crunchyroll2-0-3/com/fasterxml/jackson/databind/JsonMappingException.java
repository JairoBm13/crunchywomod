// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import java.io.Serializable;
import java.util.Iterator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonLocation;
import java.util.LinkedList;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonMappingException extends JsonProcessingException
{
    protected LinkedList<Reference> _path;
    
    public JsonMappingException(final String s) {
        super(s);
    }
    
    public JsonMappingException(final String s, final JsonLocation jsonLocation) {
        super(s, jsonLocation);
    }
    
    public JsonMappingException(final String s, final JsonLocation jsonLocation, final Throwable t) {
        super(s, jsonLocation, t);
    }
    
    public JsonMappingException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public static JsonMappingException from(final JsonParser jsonParser, final String s) {
        JsonLocation tokenLocation;
        if (jsonParser == null) {
            tokenLocation = null;
        }
        else {
            tokenLocation = jsonParser.getTokenLocation();
        }
        return new JsonMappingException(s, tokenLocation);
    }
    
    public static JsonMappingException from(final JsonParser jsonParser, final String s, final Throwable t) {
        JsonLocation tokenLocation;
        if (jsonParser == null) {
            tokenLocation = null;
        }
        else {
            tokenLocation = jsonParser.getTokenLocation();
        }
        return new JsonMappingException(s, tokenLocation, t);
    }
    
    public static JsonMappingException wrapWithPath(final Throwable t, final Reference reference) {
        JsonMappingException ex;
        if (t instanceof JsonMappingException) {
            ex = (JsonMappingException)t;
        }
        else {
            final String message = t.getMessage();
            String string = null;
            Label_0068: {
                if (message != null) {
                    string = message;
                    if (message.length() != 0) {
                        break Label_0068;
                    }
                }
                string = "(was " + t.getClass().getName() + ")";
            }
            ex = new JsonMappingException(string, null, t);
        }
        ex.prependPath(reference);
        return ex;
    }
    
    public static JsonMappingException wrapWithPath(final Throwable t, final Object o, final int n) {
        return wrapWithPath(t, new Reference(o, n));
    }
    
    public static JsonMappingException wrapWithPath(final Throwable t, final Object o, final String s) {
        return wrapWithPath(t, new Reference(o, s));
    }
    
    protected void _appendPathDesc(final StringBuilder sb) {
        if (this._path != null) {
            final Iterator<Reference> iterator = (Iterator<Reference>)this._path.iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next().toString());
                if (iterator.hasNext()) {
                    sb.append("->");
                }
            }
        }
    }
    
    protected String _buildMessage() {
        final String message = super.getMessage();
        if (this._path == null) {
            return message;
        }
        StringBuilder sb;
        if (message == null) {
            sb = new StringBuilder();
        }
        else {
            sb = new StringBuilder(message);
        }
        sb.append(" (through reference chain: ");
        final StringBuilder pathReference = this.getPathReference(sb);
        pathReference.append(')');
        return pathReference.toString();
    }
    
    @Override
    public String getLocalizedMessage() {
        return this._buildMessage();
    }
    
    @Override
    public String getMessage() {
        return this._buildMessage();
    }
    
    public StringBuilder getPathReference(final StringBuilder sb) {
        this._appendPathDesc(sb);
        return sb;
    }
    
    public void prependPath(final Reference reference) {
        if (this._path == null) {
            this._path = new LinkedList<Reference>();
        }
        if (this._path.size() < 1000) {
            this._path.addFirst(reference);
        }
    }
    
    public void prependPath(final Object o, final String s) {
        this.prependPath(new Reference(o, s));
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + ": " + this.getMessage();
    }
    
    public static class Reference implements Serializable
    {
        protected String _fieldName;
        protected Object _from;
        protected int _index;
        
        protected Reference() {
            this._index = -1;
        }
        
        public Reference(final Object from, final int index) {
            this._index = -1;
            this._from = from;
            this._index = index;
        }
        
        public Reference(final Object from, final String fieldName) {
            this._index = -1;
            this._from = from;
            if (fieldName == null) {
                throw new NullPointerException("Can not pass null fieldName");
            }
            this._fieldName = fieldName;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            Class<?> class1;
            if (this._from instanceof Class) {
                class1 = (Class<?>)this._from;
            }
            else {
                class1 = this._from.getClass();
            }
            final Package package1 = class1.getPackage();
            if (package1 != null) {
                sb.append(package1.getName());
                sb.append('.');
            }
            sb.append(class1.getSimpleName());
            sb.append('[');
            if (this._fieldName != null) {
                sb.append('\"');
                sb.append(this._fieldName);
                sb.append('\"');
            }
            else if (this._index >= 0) {
                sb.append(this._index);
            }
            else {
                sb.append('?');
            }
            sb.append(']');
            return sb.toString();
        }
    }
}
