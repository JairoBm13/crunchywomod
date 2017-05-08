// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.event.eventListener;

import roboguice.event.EventListener;

public class EventListenerRunnable<T> implements Runnable
{
    protected T event;
    protected EventListener<T> eventListener;
    
    public EventListenerRunnable(final T event, final EventListener<T> eventListener) {
        this.event = event;
        this.eventListener = eventListener;
    }
    
    @Override
    public void run() {
        this.eventListener.onEvent(this.event);
    }
}
