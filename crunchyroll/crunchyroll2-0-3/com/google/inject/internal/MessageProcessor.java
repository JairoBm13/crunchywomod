// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.util.logging.Level;
import com.google.inject.spi.Message;
import com.google.inject.Guice;
import java.util.logging.Logger;

final class MessageProcessor extends AbstractProcessor
{
    private static final Logger logger;
    
    static {
        logger = Logger.getLogger(Guice.class.getName());
    }
    
    MessageProcessor(final Errors errors) {
        super(errors);
    }
    
    public static String getRootMessage(final Throwable t) {
        final Throwable cause = t.getCause();
        if (cause == null) {
            return t.toString();
        }
        return getRootMessage(cause);
    }
    
    @Override
    public Boolean visit(final Message message) {
        if (message.getCause() != null) {
            MessageProcessor.logger.log(Level.INFO, "An exception was caught and reported. Message: " + getRootMessage(message.getCause()), message.getCause());
        }
        this.errors.addMessage(message);
        return true;
    }
}
