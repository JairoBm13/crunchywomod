// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.tremor;

import android.content.Context;
import com.tremorvideo.sdk.android.videoad.Settings;
import com.secondtv.android.ads.AdTrackListener;
import android.app.Activity;
import com.tremorvideo.sdk.android.videoad.TremorVideo;

public class Tremor
{
    public static void tremorDestroy() {
        TremorVideo.destroy();
    }
    
    public static void tremorInit(final Activity activity, final String s, final AdTrackListener adTrackListener) {
        final Settings settings = new Settings();
        settings.preferredOrientation = Settings.PreferredOrientation.Landscape;
        try {
            TremorVideo.initialize((Context)activity, s);
            TremorVideo.updateSettings(settings);
            adTrackListener.onTremorLoadAd((Context)activity);
            TremorVideo.loadAd();
        }
        catch (Exception ex) {}
    }
}
