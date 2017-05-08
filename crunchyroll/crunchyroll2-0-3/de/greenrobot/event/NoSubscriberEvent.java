// 
// Decompiled by Procyon v0.5.30
// 

package de.greenrobot.event;

public final class NoSubscriberEvent
{
    public final EventBus eventBus;
    public final Object originalEvent;
    
    public NoSubscriberEvent(final EventBus eventBus, final Object originalEvent) {
        this.eventBus = eventBus;
        this.originalEvent = originalEvent;
    }
}
