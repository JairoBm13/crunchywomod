// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

final class Present<T> extends Optional<T>
{
    private final T reference;
    
    Present(final T reference) {
        this.reference = reference;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof Present && this.reference.equals(((Present)o).reference);
    }
    
    @Override
    public T get() {
        return this.reference;
    }
    
    @Override
    public int hashCode() {
        return 1502476572 + this.reference.hashCode();
    }
    
    @Override
    public boolean isPresent() {
        return true;
    }
    
    @Override
    public Optional<T> or(final Optional<? extends T> optional) {
        Preconditions.checkNotNull(optional);
        return this;
    }
    
    @Override
    public T or(final T t) {
        Preconditions.checkNotNull(t, (Object)"use orNull() instead of or(null)");
        return this.reference;
    }
    
    @Override
    public T orNull() {
        return this.reference;
    }
    
    @Override
    public String toString() {
        return "Optional.of(" + this.reference + ")";
    }
    
    @Override
    public <V> Optional<V> transform(final Function<? super T, V> function) {
        return new Present<V>(Preconditions.checkNotNull(function.apply(this.reference), (Object)"Transformation function cannot return null."));
    }
}
