// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads;

import android.content.Context;
import java.io.Serializable;

public abstract class AdTrackListener implements Serializable
{
    private static final long serialVersionUID = -2027476842685257923L;
    
    public abstract void onAdEnd(final Context p0, final String p1);
    
    public abstract void onAdRequested(final Context p0, final String p1);
    
    public abstract void onAdSlotStart(final Context p0);
    
    public abstract void onAdStart(final Context p0, final String p1);
    
    public abstract void onAdUnfulfilled(final Context p0, final String p1);
    
    public abstract void onIMAPlayed(final Context p0);
    
    public abstract void onIMARequest(final Context p0);
    
    public abstract void onPostAdResume(final Context p0, final String p1);
    
    public abstract void onTremorAdReady(final Context p0, final boolean p1);
    
    public abstract void onTremorLoadAd(final Context p0);
    
    public abstract void onTremorPlayed(final Context p0);
    
    public abstract void onTremorRequestFail(final Context p0);
    
    public abstract void onTremorRequestSuccess(final Context p0);
    
    public abstract void onVastAdParseComplete(final Context p0);
    
    public abstract void onVastAdParseStart(final Context p0);
    
    public abstract void onVastPlayed(final Context p0);
    
    public abstract void onVastRequest(final Context p0);
}
