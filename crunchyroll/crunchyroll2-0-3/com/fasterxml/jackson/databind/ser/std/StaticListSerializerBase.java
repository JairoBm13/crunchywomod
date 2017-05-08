// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import java.util.Collection;

public abstract class StaticListSerializerBase<T extends Collection<?>> extends StdSerializer<T>
{
    protected StaticListSerializerBase(final Class<?> clazz) {
        super(clazz, false);
    }
    
    @Override
    public boolean isEmpty(final T t) {
        return t == null || t.size() == 0;
    }
}
