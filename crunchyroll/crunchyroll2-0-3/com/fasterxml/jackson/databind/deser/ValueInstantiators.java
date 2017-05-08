// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;

public interface ValueInstantiators
{
    ValueInstantiator findValueInstantiator(final DeserializationConfig p0, final BeanDescription p1, final ValueInstantiator p2);
    
    public static class Base implements ValueInstantiators
    {
        @Override
        public ValueInstantiator findValueInstantiator(final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final ValueInstantiator valueInstantiator) {
            return valueInstantiator;
        }
    }
}
