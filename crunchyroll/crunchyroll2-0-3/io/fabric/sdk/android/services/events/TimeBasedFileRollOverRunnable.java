// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.events;

import io.fabric.sdk.android.services.common.CommonUtils;
import android.content.Context;

public class TimeBasedFileRollOverRunnable implements Runnable
{
    private final Context context;
    private final FileRollOverManager fileRollOverManager;
    
    public TimeBasedFileRollOverRunnable(final Context context, final FileRollOverManager fileRollOverManager) {
        this.context = context;
        this.fileRollOverManager = fileRollOverManager;
    }
    
    @Override
    public void run() {
        try {
            CommonUtils.logControlled(this.context, "Performing time based file roll over.");
            if (!this.fileRollOverManager.rollFileOver()) {
                this.fileRollOverManager.cancelTimeBasedFileRollOver();
            }
        }
        catch (Exception ex) {
            CommonUtils.logControlledError(this.context, "Failed to roll over file", ex);
        }
    }
}
