// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.platforms;

import java.util.concurrent.TimeUnit;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class AndroidApi implements PlatformApi
{
    private ScheduledThreadPoolExecutor _pool;
    private ScheduledFuture<?> _scheduledPollTask;
    private ScheduledFuture<?> _scheduledTask;
    
    public AndroidApi(final Object o) {
        this._pool = new ScheduledThreadPoolExecutor(2);
    }
    
    @Override
    public void cleanup() {
        if (this._scheduledTask != null) {
            this._scheduledTask.cancel(false);
        }
        if (this._scheduledPollTask != null) {
            this._scheduledPollTask.cancel(false);
        }
    }
    
    @Override
    public void createPollTask(final TimerTask timerTask, final int n) {
        if (this._scheduledPollTask != null) {
            this._scheduledPollTask.cancel(false);
        }
        this._scheduledPollTask = this._pool.scheduleAtFixedRate(timerTask, 0L, n, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void createTimer(final TimerTask timerTask, final int n, final int n2, final String s) {
        if (this._scheduledTask != null) {
            this._scheduledTask.cancel(false);
        }
        this._scheduledTask = this._pool.scheduleAtFixedRate(timerTask, n, n2, TimeUnit.MILLISECONDS);
    }
}
