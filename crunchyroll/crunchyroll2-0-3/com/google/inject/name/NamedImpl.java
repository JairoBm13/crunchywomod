// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.name;

import java.lang.annotation.Annotation;
import com.google.inject.internal.util.$Preconditions;
import java.io.Serializable;

class NamedImpl implements Named, Serializable
{
    private final String value;
    
    public NamedImpl(final String s) {
        this.value = $Preconditions.checkNotNull(s, "name");
    }
    
    @Override
    public Class<? extends Annotation> annotationType() {
        return Named.class;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof Named && this.value.equals(((Named)o).value());
    }
    
    @Override
    public int hashCode() {
        return "value".hashCode() * 127 ^ this.value.hashCode();
    }
    
    @Override
    public String toString() {
        return "@" + Named.class.getName() + "(value=" + this.value + ")";
    }
    
    @Override
    public String value() {
        return this.value;
    }
}
