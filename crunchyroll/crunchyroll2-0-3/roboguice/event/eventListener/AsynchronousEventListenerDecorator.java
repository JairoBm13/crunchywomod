// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.event.eventListener;

import android.os.Handler;
import roboguice.event.EventListener;

public class AsynchronousEventListenerDecorator<T> implements EventListener<T>
{
    protected EventListener<T> eventListener;
    protected Handler handler;
    
    public AsynchronousEventListenerDecorator(final Handler handler, final EventListener<T> eventListener) {
        this.handler = handler;
        this.eventListener = eventListener;
    }
    
    public AsynchronousEventListenerDecorator(final EventListener<T> eventListener) {
        this.eventListener = eventListener;
    }
    
    @Override
    public void onEvent(final T t) {
        new RunnableAsyncTaskAdaptor(this.handler, new EventListenerRunnable<Object>(t, this.eventListener)).execute();
    }
}
