// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.util;

import java.util.concurrent.Executor;
import android.os.Handler;
import roboguice.RoboGuice;
import android.content.Context;

public abstract class RoboAsyncTask<ResultT> extends SafeAsyncTask<ResultT>
{
    protected Context context;
    
    protected RoboAsyncTask(final Context context) {
        this.context = context;
        RoboGuice.getInjector(context).injectMembers(this);
    }
    
    protected RoboAsyncTask(final Context context, final Handler handler) {
        super(handler);
        this.context = context;
        RoboGuice.getInjector(context).injectMembers(this);
    }
    
    protected RoboAsyncTask(final Context context, final Handler handler, final Executor executor) {
        super(handler, executor);
        this.context = context;
        RoboGuice.getInjector(context).injectMembers(this);
    }
    
    protected RoboAsyncTask(final Context context, final Executor executor) {
        super(executor);
        this.context = context;
        RoboGuice.getInjector(context).injectMembers(this);
    }
    
    public Context getContext() {
        return this.context;
    }
}
