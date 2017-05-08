// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import java.io.Serializable;
import com.fasterxml.jackson.databind.ser.FilterProvider;

public class SimpleFilterProvider extends FilterProvider implements Serializable
{
    protected boolean _cfgFailOnUnknownId;
    protected BeanPropertyFilter _defaultFilter;
    protected final Map<String, BeanPropertyFilter> _filtersById;
    
    public SimpleFilterProvider() {
        this(new HashMap<String, BeanPropertyFilter>());
    }
    
    public SimpleFilterProvider(final Map<String, BeanPropertyFilter> filtersById) {
        this._cfgFailOnUnknownId = true;
        this._filtersById = filtersById;
    }
    
    @Override
    public BeanPropertyFilter findFilter(final Object o) {
        BeanPropertyFilter beanPropertyFilter;
        if ((beanPropertyFilter = this._filtersById.get(o)) == null) {
            final BeanPropertyFilter defaultFilter = this._defaultFilter;
            if ((beanPropertyFilter = defaultFilter) == null) {
                beanPropertyFilter = defaultFilter;
                if (this._cfgFailOnUnknownId) {
                    throw new IllegalArgumentException("No filter configured with id '" + o + "' (type " + o.getClass().getName() + ")");
                }
            }
        }
        return beanPropertyFilter;
    }
}
