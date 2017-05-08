// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject;

import com.google.inject.internal.util.$Preconditions;
import java.util.Collection;
import com.google.inject.internal.Errors;
import com.google.inject.spi.Message;
import com.google.inject.internal.util.$ImmutableSet;

public final class ConfigurationException extends RuntimeException
{
    private final $ImmutableSet<Message> messages;
    private Object partialValue;
    
    public ConfigurationException(final Iterable<Message> iterable) {
        this.partialValue = null;
        this.messages = $ImmutableSet.copyOf((Iterable<? extends Message>)iterable);
        this.initCause(Errors.getOnlyCause(this.messages));
    }
    
    public Collection<Message> getErrorMessages() {
        return this.messages;
    }
    
    @Override
    public String getMessage() {
        return Errors.format("Guice configuration errors", this.messages);
    }
    
    public <E> E getPartialValue() {
        return (E)this.partialValue;
    }
    
    public ConfigurationException withPartialValue(final Object partialValue) {
        $Preconditions.checkState(this.partialValue == null, "Can't clobber existing partial value %s with %s", this.partialValue, partialValue);
        final ConfigurationException ex = new ConfigurationException(this.messages);
        ex.partialValue = partialValue;
        return ex;
    }
}
