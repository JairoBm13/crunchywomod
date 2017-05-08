// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.content;

import roboguice.RoboGuice;
import android.content.ContentProvider;

public abstract class RoboContentProvider extends ContentProvider
{
    public boolean onCreate() {
        RoboGuice.getInjector(this.getContext()).injectMembers(this);
        return true;
    }
}
