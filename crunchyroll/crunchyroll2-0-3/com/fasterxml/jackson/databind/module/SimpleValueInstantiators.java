// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.module;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.util.HashMap;
import java.io.Serializable;
import com.fasterxml.jackson.databind.deser.ValueInstantiators;

public class SimpleValueInstantiators extends Base implements Serializable
{
    protected HashMap<ClassKey, ValueInstantiator> _classMappings;
    
    public SimpleValueInstantiators() {
        this._classMappings = new HashMap<ClassKey, ValueInstantiator>();
    }
    
    @Override
    public ValueInstantiator findValueInstantiator(final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final ValueInstantiator valueInstantiator) {
        final ValueInstantiator valueInstantiator2 = this._classMappings.get(new ClassKey(beanDescription.getBeanClass()));
        if (valueInstantiator2 == null) {
            return valueInstantiator;
        }
        return valueInstantiator2;
    }
}
