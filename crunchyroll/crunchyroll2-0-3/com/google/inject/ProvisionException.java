// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject;

import java.util.Collection;
import com.google.inject.internal.Errors;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.spi.Message;
import com.google.inject.internal.util.$ImmutableSet;

public final class ProvisionException extends RuntimeException
{
    private final $ImmutableSet<Message> messages;
    
    public ProvisionException(final Iterable<Message> iterable) {
        this.messages = $ImmutableSet.copyOf((Iterable<? extends Message>)iterable);
        $Preconditions.checkArgument(!this.messages.isEmpty());
        this.initCause(Errors.getOnlyCause(this.messages));
    }
    
    public ProvisionException(final String s) {
        this.messages = $ImmutableSet.of(new Message(s));
    }
    
    public Collection<Message> getErrorMessages() {
        return this.messages;
    }
    
    @Override
    public String getMessage() {
        return Errors.format("Guice provision errors", this.messages);
    }
}
