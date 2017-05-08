// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.app;

import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.content.Context;
import com.secondtv.android.ads.AdTrackListener;

public class AdsListener extends AdTrackListener
{
    @Override
    public void onAdEnd(final Context context, final String s) {
        Tracker.logAd(context, "ad_end", s);
        this.onPostAdResume(context, null);
    }
    
    @Override
    public void onAdRequested(final Context context, final String s) {
        Tracker.logAd(context, "ad_requested", s);
    }
    
    @Override
    public void onAdSlotStart(final Context context) {
        Tracker.logAd(context, "ad_slot_start", null);
    }
    
    @Override
    public void onAdStart(final Context context, final String s) {
        Tracker.logAd(context, "ad_start", s);
    }
    
    @Override
    public void onAdUnfulfilled(final Context context, final String s) {
        Tracker.logAd(context, "ad_unfulfilled", s);
    }
    
    @Override
    public void onIMAPlayed(final Context context) {
        Tracker.adServed("ima");
    }
    
    @Override
    public void onIMARequest(final Context context) {
    }
    
    @Override
    public void onPostAdResume(final Context context, final String s) {
        Tracker.logAd(context, "post_ad_resume", s);
    }
    
    @Override
    public void onTremorAdReady(final Context context, final boolean b) {
        Tracker.tremorIsAdReady(b);
    }
    
    @Override
    public void onTremorLoadAd(final Context context) {
        Tracker.tremorLoadAd();
    }
    
    @Override
    public void onTremorPlayed(final Context context) {
        Tracker.adServed("tremor");
    }
    
    @Override
    public void onTremorRequestFail(final Context context) {
        Tracker.tremorShowAd(false);
    }
    
    @Override
    public void onTremorRequestSuccess(final Context context) {
        Tracker.tremorShowAd(true);
    }
    
    @Override
    public void onVastAdParseComplete(final Context context) {
        Tracker.logAd(context, "ad_parse_complete", "vast");
    }
    
    @Override
    public void onVastAdParseStart(final Context context) {
        Tracker.logAd(context, "ad_parse_start", "vast");
    }
    
    @Override
    public void onVastPlayed(final Context context) {
        Tracker.adServed("vast");
    }
    
    @Override
    public void onVastRequest(final Context context) {
    }
}
