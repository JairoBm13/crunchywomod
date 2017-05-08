// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public abstract class InjectableValues
{
    public abstract Object findInjectableValue(final Object p0, final DeserializationContext p1, final BeanProperty p2, final Object p3);
    
    public static class Std extends InjectableValues implements Serializable
    {
        protected final Map<String, Object> _values;
        
        public Std() {
            this(new HashMap<String, Object>());
        }
        
        public Std(final Map<String, Object> values) {
            this._values = values;
        }
        
        @Override
        public Object findInjectableValue(final Object o, final DeserializationContext deserializationContext, final BeanProperty beanProperty, final Object o2) {
            if (!(o instanceof String)) {
                String name;
                if (o == null) {
                    name = "[null]";
                }
                else {
                    name = o.getClass().getName();
                }
                throw new IllegalArgumentException("Unrecognized inject value id type (" + name + "), expecting String");
            }
            final String s = (String)o;
            final Object value = this._values.get(s);
            if (value == null && !this._values.containsKey(s)) {
                throw new IllegalArgumentException("No injectable id with value '" + s + "' found (for property '" + beanProperty.getName() + "')");
            }
            return value;
        }
    }
}
