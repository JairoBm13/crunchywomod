// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.receivers;

import java.util.Iterator;
import android.content.Intent;
import android.content.ComponentName;
import android.os.Bundle;
import android.content.Context;
import android.content.BroadcastReceiver;

public class ForwardingReceiver extends BroadcastReceiver
{
    private Bundle getMetaData(final Context context) {
        try {
            return context.getPackageManager().getReceiverInfo(new ComponentName(context, (Class)ForwardingReceiver.class), 128).metaData;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new Bundle();
        }
    }
    
    private BroadcastReceiver makeReceiver(final String s) {
        try {
            return (BroadcastReceiver)Class.forName(s).newInstance();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
    public void onReceive(final Context context, final Intent intent) {
        final Bundle metaData = this.getMetaData(context);
        final Iterator iterator = metaData.keySet().iterator();
        while (iterator.hasNext()) {
            this.makeReceiver(metaData.getString((String)iterator.next())).onReceive(context, intent);
        }
    }
}
