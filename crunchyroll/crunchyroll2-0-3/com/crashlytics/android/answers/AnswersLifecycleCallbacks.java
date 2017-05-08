// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import android.os.Bundle;
import android.app.Activity;
import io.fabric.sdk.android.ActivityLifecycleManager;

class AnswersLifecycleCallbacks extends Callbacks
{
    private final SessionAnalyticsManager analyticsManager;
    
    public AnswersLifecycleCallbacks(final SessionAnalyticsManager analyticsManager) {
        this.analyticsManager = analyticsManager;
    }
    
    @Override
    public void onActivityCreated(final Activity activity, final Bundle bundle) {
        this.analyticsManager.onLifecycle(activity, SessionEvent.Type.CREATE);
    }
    
    @Override
    public void onActivityDestroyed(final Activity activity) {
        this.analyticsManager.onLifecycle(activity, SessionEvent.Type.DESTROY);
    }
    
    @Override
    public void onActivityPaused(final Activity activity) {
        this.analyticsManager.onLifecycle(activity, SessionEvent.Type.PAUSE);
    }
    
    @Override
    public void onActivityResumed(final Activity activity) {
        this.analyticsManager.onLifecycle(activity, SessionEvent.Type.RESUME);
    }
    
    @Override
    public void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {
        this.analyticsManager.onLifecycle(activity, SessionEvent.Type.SAVE_INSTANCE_STATE);
    }
    
    @Override
    public void onActivityStarted(final Activity activity) {
        this.analyticsManager.onLifecycle(activity, SessionEvent.Type.START);
    }
    
    @Override
    public void onActivityStopped(final Activity activity) {
        this.analyticsManager.onLifecycle(activity, SessionEvent.Type.STOP);
    }
}
