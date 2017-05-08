// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

import android.os.Process;

public abstract class BackgroundPriorityRunnable implements Runnable
{
    protected abstract void onRun();
    
    @Override
    public final void run() {
        Process.setThreadPriority(10);
        this.onRun();
    }
}
