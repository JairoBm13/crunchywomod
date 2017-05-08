// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android;

import io.fabric.sdk.android.services.concurrency.UnmetDependencyException;
import io.fabric.sdk.android.services.concurrency.Priority;
import io.fabric.sdk.android.services.common.TimingMetric;
import io.fabric.sdk.android.services.concurrency.PriorityAsyncTask;

class InitializationTask<Result> extends PriorityAsyncTask<Void, Void, Result>
{
    final Kit<Result> kit;
    
    public InitializationTask(final Kit<Result> kit) {
        this.kit = kit;
    }
    
    private TimingMetric createAndStartTimingMetric(final String s) {
        final TimingMetric timingMetric = new TimingMetric(this.kit.getIdentifier() + "." + s, "KitInitialization");
        timingMetric.startMeasuring();
        return timingMetric;
    }
    
    @Override
    protected Result doInBackground(final Void... array) {
        final TimingMetric andStartTimingMetric = this.createAndStartTimingMetric("doInBackground");
        Result doInBackground = null;
        if (!this.isCancelled()) {
            doInBackground = this.kit.doInBackground();
        }
        andStartTimingMetric.stopMeasuring();
        return doInBackground;
    }
    
    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }
    
    @Override
    protected void onCancelled(final Result result) {
        this.kit.onCancelled(result);
        this.kit.initializationCallback.failure(new InitializationException(this.kit.getIdentifier() + " Initialization was cancelled"));
    }
    
    @Override
    protected void onPostExecute(final Result result) {
        this.kit.onPostExecute(result);
        this.kit.initializationCallback.success(result);
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        final TimingMetric andStartTimingMetric = this.createAndStartTimingMetric("onPreExecute");
        try {
            final boolean onPreExecute = this.kit.onPreExecute();
            andStartTimingMetric.stopMeasuring();
            if (!onPreExecute) {
                this.cancel(true);
            }
        }
        catch (UnmetDependencyException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            Fabric.getLogger().e("Fabric", "Failure onPreExecute()", ex2);
        }
        finally {
            andStartTimingMetric.stopMeasuring();
            if (!false) {
                this.cancel(true);
            }
        }
    }
}
