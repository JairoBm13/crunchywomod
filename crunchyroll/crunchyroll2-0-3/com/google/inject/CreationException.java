// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject;

import com.google.inject.internal.Errors;
import com.google.inject.internal.util.$Preconditions;
import java.util.Collection;
import com.google.inject.spi.Message;
import com.google.inject.internal.util.$ImmutableSet;

public class CreationException extends RuntimeException
{
    private final $ImmutableSet<Message> messages;
    
    public CreationException(final Collection<Message> collection) {
        this.messages = $ImmutableSet.copyOf((Iterable<? extends Message>)collection);
        $Preconditions.checkArgument(!this.messages.isEmpty());
        this.initCause(Errors.getOnlyCause(this.messages));
    }
    
    public Collection<Message> getErrorMessages() {
        return this.messages;
    }
    
    @Override
    public String getMessage() {
        return Errors.format("Guice creation errors", this.messages);
    }
}
