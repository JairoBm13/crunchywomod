// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.event.eventListener.factory;

import roboguice.event.eventListener.AsynchronousEventListenerDecorator;
import roboguice.event.eventListener.UIThreadEventListenerDecorator;
import roboguice.event.EventListener;
import roboguice.event.EventThread;
import com.google.inject.Inject;
import android.os.Handler;
import com.google.inject.Provider;

public class EventListenerThreadingDecorator
{
    @Inject
    protected Provider<Handler> handlerProvider;
    
    public <T> EventListener<T> decorate(final EventThread eventThread, final EventListener<T> eventListener) {
        switch (eventThread) {
            default: {
                return eventListener;
            }
            case UI: {
                return new UIThreadEventListenerDecorator<T>(eventListener, this.handlerProvider.get());
            }
            case BACKGROUND: {
                return new AsynchronousEventListenerDecorator<T>(eventListener);
            }
        }
    }
}
