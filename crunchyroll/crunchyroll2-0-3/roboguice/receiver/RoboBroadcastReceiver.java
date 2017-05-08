// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.receiver;

import roboguice.RoboGuice;
import android.app.Application;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public abstract class RoboBroadcastReceiver extends BroadcastReceiver
{
    protected void handleReceive(final Context context, final Intent intent) {
    }
    
    public final void onReceive(final Context context, final Intent intent) {
        RoboGuice.getBaseApplicationInjector((Application)context.getApplicationContext()).injectMembers(this);
        this.handleReceive(context, intent);
    }
}
