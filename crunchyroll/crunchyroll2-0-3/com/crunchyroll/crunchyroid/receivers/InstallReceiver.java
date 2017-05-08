// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.receivers;

import com.crunchyroll.crunchyroid.app.ApplicationState;
import android.content.Intent;
import android.content.Context;
import com.crunchyroll.android.util.LoggerFactory;
import com.crunchyroll.android.util.Logger;
import android.content.BroadcastReceiver;

public class InstallReceiver extends BroadcastReceiver
{
    private static final Logger log;
    
    static {
        log = LoggerFactory.getLogger(InstallReceiver.class);
    }
    
    public void onReceive(final Context context, final Intent intent) {
        final ApplicationState value = ApplicationState.get(context);
        final String stringExtra = intent.getStringExtra("referrer");
        try {
            value.setInstallReferrer(stringExtra);
        }
        catch (Exception ex) {
            InstallReceiver.log.error("Install referrer could not be set", ex);
        }
    }
}
