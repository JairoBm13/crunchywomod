// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.receivers;

import com.crunchyroll.crunchyroid.util.Util;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.BroadcastReceiver;

public class CastBarReciever extends BroadcastReceiver
{
    private Activity mActivity;
    private ViewGroup mCastBar;
    
    public CastBarReciever(final Activity mActivity, final ViewGroup mCastBar) {
        this.mActivity = mActivity;
        this.mCastBar = mCastBar;
    }
    
    public static IntentFilter getIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("CAST_SESSION_STARTING_EVENT");
        intentFilter.addAction("CAST_SESSION_STARTED_EVENT");
        intentFilter.addAction("CAST_SESSION_ENDED_EVENT");
        intentFilter.addAction("CAST_SESSION_LOST_EVENT");
        intentFilter.addAction("CAST_VIDEO_STARTED_EVENT");
        intentFilter.addAction("CAST_VIDEO_COMPLETION_EVENT");
        return intentFilter;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        Util.updateCastBar(this.mActivity, this.mCastBar);
    }
}
