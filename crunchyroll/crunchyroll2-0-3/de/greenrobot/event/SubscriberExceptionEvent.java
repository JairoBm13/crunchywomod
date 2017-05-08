// 
// Decompiled by Procyon v0.5.30
// 

package de.greenrobot.event;

public final class SubscriberExceptionEvent
{
    public final Object causingEvent;
    public final Object causingSubscriber;
    public final EventBus eventBus;
    public final Throwable throwable;
    
    public SubscriberExceptionEvent(final EventBus eventBus, final Throwable throwable, final Object causingEvent, final Object causingSubscriber) {
        this.eventBus = eventBus;
        this.throwable = throwable;
        this.causingEvent = causingEvent;
        this.causingSubscriber = causingSubscriber;
    }
}
