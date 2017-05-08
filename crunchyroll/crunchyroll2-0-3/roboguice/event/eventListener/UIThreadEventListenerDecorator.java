// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.event.eventListener;

import android.os.Handler;
import roboguice.event.EventListener;

public class UIThreadEventListenerDecorator<T> implements EventListener<T>
{
    protected EventListener<T> eventListener;
    protected Handler handler;
    
    public UIThreadEventListenerDecorator(final EventListener<T> eventListener, final Handler handler) {
        this.eventListener = eventListener;
        this.handler = handler;
    }
    
    @Override
    public void onEvent(final T t) {
        this.handler.post((Runnable)new EventListenerRunnable<Object>(t, this.eventListener));
    }
}
