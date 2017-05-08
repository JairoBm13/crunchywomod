// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.cast;

import android.app.NotificationManager;
import java.io.Serializable;
import android.content.Intent;
import com.crunchyroll.cast.model.CastInfo;
import android.content.Context;

public class NotificationHelper
{
    private final Context mContext;
    
    public NotificationHelper(final Context mContext) {
        this.mContext = mContext;
    }
    
    public void publishNotification(final CastHandler castHandler) {
        final CastInfo castInfo = castHandler.getCurrentCastInfo().orNull();
        if (castInfo == null) {
            throw new IllegalStateException("Cannot add notification when episodeInfo is null");
        }
        final Intent intent = new Intent(this.mContext.getApplicationContext(), (Class)NotificationService.class);
        intent.putExtra("castInfo", (Serializable)castInfo);
        this.mContext.startService(intent);
    }
    
    public void removeNotification() {
        this.mContext.stopService(new Intent(this.mContext, (Class)NotificationService.class));
        ((NotificationManager)this.mContext.getSystemService("notification")).cancel(1);
    }
}
