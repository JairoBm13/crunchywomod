// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.content;

import roboguice.RoboGuice;
import android.content.Context;
import android.support.v4.content.Loader;

public abstract class RoboLoader<D> extends Loader<D>
{
    public RoboLoader(final Context context) {
        super(context);
        RoboGuice.injectMembers(context, this);
    }
}
