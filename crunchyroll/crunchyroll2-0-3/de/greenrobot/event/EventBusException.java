// 
// Decompiled by Procyon v0.5.30
// 

package de.greenrobot.event;

public class EventBusException extends RuntimeException
{
    public EventBusException(final String s) {
        super(s);
    }
    
    public EventBusException(final String s, final Throwable t) {
        super(s, t);
    }
}
