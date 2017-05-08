// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.event.eventListener;

import android.os.Handler;
import roboguice.util.SafeAsyncTask;

public class RunnableAsyncTaskAdaptor extends SafeAsyncTask<Void>
{
    protected Runnable runnable;
    
    public RunnableAsyncTaskAdaptor(final Handler handler, final Runnable runnable) {
        super(handler);
        this.runnable = runnable;
    }
    
    public RunnableAsyncTaskAdaptor(final Runnable runnable) {
        this.runnable = runnable;
    }
    
    @Override
    public Void call() throws Exception {
        this.runnable.run();
        return null;
    }
}
