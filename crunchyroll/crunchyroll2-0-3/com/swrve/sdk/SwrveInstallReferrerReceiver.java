// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class SwrveInstallReferrerReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, Intent stringExtra) {
        stringExtra = (Intent)stringExtra.getStringExtra("referrer");
        Log.i("SwrveSDK", "Received INSTALL_REFERRER broadcast with referrer:" + (String)stringExtra);
        if (SwrveHelper.isNullOrEmpty((String)stringExtra)) {
            return;
        }
        try {
            context.getSharedPreferences("swrve_prefs", 0).edit().putString("swrve.referrer_id", URLDecoder.decode((String)stringExtra, "UTF-8")).commit();
        }
        catch (UnsupportedEncodingException ex) {
            Log.e("SwrveSDK", "Error decoding the referrer:" + (String)stringExtra, (Throwable)ex);
        }
    }
}
