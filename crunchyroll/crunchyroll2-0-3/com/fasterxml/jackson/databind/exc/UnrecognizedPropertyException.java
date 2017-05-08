// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.exc;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonLocation;
import java.util.Collection;
import com.fasterxml.jackson.databind.JsonMappingException;

public class UnrecognizedPropertyException extends JsonMappingException
{
    protected transient String _propertiesAsString;
    protected final Collection<Object> _propertyIds;
    protected final Class<?> _referringClass;
    protected final String _unrecognizedPropertyName;
    
    public UnrecognizedPropertyException(final String s, final JsonLocation jsonLocation, final Class<?> referringClass, final String unrecognizedPropertyName, final Collection<Object> propertyIds) {
        super(s, jsonLocation);
        this._referringClass = referringClass;
        this._unrecognizedPropertyName = unrecognizedPropertyName;
        this._propertyIds = propertyIds;
    }
    
    public static UnrecognizedPropertyException from(final JsonParser jsonParser, final Object o, final String s, final Collection<Object> collection) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
        Class<?> class1;
        if (o instanceof Class) {
            class1 = (Class<?>)o;
        }
        else {
            class1 = o.getClass();
        }
        final UnrecognizedPropertyException ex = new UnrecognizedPropertyException("Unrecognized field \"" + s + "\" (class " + class1.getName() + "), not marked as ignorable", jsonParser.getCurrentLocation(), class1, s, collection);
        ex.prependPath(o, s);
        return ex;
    }
    
    public String getMessageSuffix() {
        String propertiesAsString;
        final String s = propertiesAsString = this._propertiesAsString;
        if (s == null) {
            propertiesAsString = s;
            if (this._propertyIds != null) {
                final StringBuilder sb = new StringBuilder(100);
                final int size = this._propertyIds.size();
                if (size == 1) {
                    sb.append(" (one known property: \"");
                    sb.append(String.valueOf(this._propertyIds.iterator().next()));
                    sb.append('\"');
                }
                else {
                    sb.append(" (").append(size).append(" known properties: ");
                    final Iterator<Object> iterator = this._propertyIds.iterator();
                    while (iterator.hasNext()) {
                        sb.append(", \"");
                        sb.append(String.valueOf(iterator.next()));
                        sb.append('\"');
                        if (sb.length() > 200) {
                            sb.append(" [truncated]");
                            break;
                        }
                    }
                }
                sb.append("])");
                propertiesAsString = sb.toString();
                this._propertiesAsString = propertiesAsString;
            }
        }
        return propertiesAsString;
    }
}
