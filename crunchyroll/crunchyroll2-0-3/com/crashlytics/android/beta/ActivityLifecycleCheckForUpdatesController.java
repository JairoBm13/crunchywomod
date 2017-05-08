// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.beta;

import android.app.Activity;
import java.util.concurrent.ExecutorService;
import io.fabric.sdk.android.ActivityLifecycleManager;
import android.annotation.TargetApi;

@TargetApi(14)
class ActivityLifecycleCheckForUpdatesController extends AbstractCheckForUpdatesController
{
    private final ActivityLifecycleManager.Callbacks callbacks;
    private final ExecutorService executorService;
    
    public ActivityLifecycleCheckForUpdatesController(final ActivityLifecycleManager activityLifecycleManager, final ExecutorService executorService) {
        this.callbacks = new ActivityLifecycleManager.Callbacks() {
            @Override
            public void onActivityStarted(final Activity activity) {
                if (ActivityLifecycleCheckForUpdatesController.this.signalExternallyReady()) {
                    ActivityLifecycleCheckForUpdatesController.this.executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            ActivityLifecycleCheckForUpdatesController.this.checkForUpdates();
                        }
                    });
                }
            }
        };
        this.executorService = executorService;
        activityLifecycleManager.registerCallbacks(this.callbacks);
    }
}
