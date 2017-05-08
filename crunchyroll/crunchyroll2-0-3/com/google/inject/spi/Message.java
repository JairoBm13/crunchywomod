// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.internal.Errors;
import com.google.inject.internal.util.$SourceProvider;
import com.google.inject.internal.util.$Objects;
import com.google.inject.Binder;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.internal.util.$ImmutableList;
import java.util.List;
import java.io.Serializable;

public final class Message implements Element, Serializable
{
    private final Throwable cause;
    private final String message;
    private final List<Object> sources;
    
    public Message(final Object o, final String s) {
        this($ImmutableList.of(o), s, null);
    }
    
    public Message(final String s) {
        this($ImmutableList.of(), s, null);
    }
    
    public Message(final List<Object> list, final String s, final Throwable cause) {
        this.sources = $ImmutableList.copyOf((Iterable<?>)list);
        this.message = $Preconditions.checkNotNull(s, "message");
        this.cause = cause;
    }
    
    @Override
    public <T> T acceptVisitor(final ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        binder.withSource(this.getSource()).addError(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof Message) {
            final Message message = (Message)o;
            if (this.message.equals(message.message) && $Objects.equal(this.cause, message.cause) && this.sources.equals(message.sources)) {
                return true;
            }
        }
        return false;
    }
    
    public Throwable getCause() {
        return this.cause;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    @Override
    public String getSource() {
        if (this.sources.isEmpty()) {
            return $SourceProvider.UNKNOWN_SOURCE.toString();
        }
        return Errors.convert(this.sources.get(this.sources.size() - 1)).toString();
    }
    
    public List<Object> getSources() {
        return this.sources;
    }
    
    @Override
    public int hashCode() {
        return this.message.hashCode();
    }
    
    @Override
    public String toString() {
        return this.message;
    }
}
