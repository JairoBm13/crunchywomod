// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.content;

import roboguice.RoboGuice;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class RoboAsyncTaskLoader<D> extends AsyncTaskLoader<D>
{
    public RoboAsyncTaskLoader(final Context context) {
        super(context);
        RoboGuice.injectMembers(context, this);
    }
}
