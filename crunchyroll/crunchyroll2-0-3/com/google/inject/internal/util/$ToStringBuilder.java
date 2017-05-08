// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class $ToStringBuilder
{
    final Map<String, Object> map;
    final String name;
    
    public $ToStringBuilder(final Class clazz) {
        this.map = new LinkedHashMap<String, Object>();
        this.name = clazz.getSimpleName();
    }
    
    public $ToStringBuilder add(final String s, final Object o) {
        if (this.map.put(s, o) != null) {
            throw new RuntimeException("Duplicate names: " + s);
        }
        return this;
    }
    
    @Override
    public String toString() {
        return this.name + this.map.toString().replace('{', '[').replace('}', ']');
    }
}
