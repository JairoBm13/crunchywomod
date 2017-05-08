// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

final class Absent extends Optional<Object>
{
    static final Absent INSTANCE;
    
    static {
        INSTANCE = new Absent();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this;
    }
    
    @Override
    public Object get() {
        throw new IllegalStateException("value is absent");
    }
    
    @Override
    public int hashCode() {
        return 1502476572;
    }
    
    @Override
    public boolean isPresent() {
        return false;
    }
    
    @Override
    public Optional<Object> or(final Optional<?> optional) {
        return Preconditions.checkNotNull(optional);
    }
    
    @Override
    public Object or(final Object o) {
        return Preconditions.checkNotNull(o, (Object)"use orNull() instead of or(null)");
    }
    
    @Override
    public Object orNull() {
        return null;
    }
    
    @Override
    public String toString() {
        return "Optional.absent()";
    }
    
    @Override
    public <V> Optional<V> transform(final Function<Object, V> function) {
        Preconditions.checkNotNull(function);
        return Optional.absent();
    }
}
