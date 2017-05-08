// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.tagmanager;

import com.google.android.gms.analytics.CampaignTrackingService;
import com.google.android.gms.analytics.CampaignTrackingReceiver;

public final class InstallReferrerReceiver extends CampaignTrackingReceiver
{
    @Override
    protected void zzaL(final String s) {
        zzax.zzex(s);
    }
    
    @Override
    protected Class<? extends CampaignTrackingService> zzhf() {
        return InstallReferrerService.class;
    }
}
