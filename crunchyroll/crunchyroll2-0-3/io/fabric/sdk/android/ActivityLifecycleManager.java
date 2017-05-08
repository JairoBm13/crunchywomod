// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android;

import android.os.Bundle;
import android.app.Activity;
import android.annotation.TargetApi;
import java.util.Iterator;
import java.util.HashSet;
import android.app.Application$ActivityLifecycleCallbacks;
import java.util.Set;
import android.os.Build$VERSION;
import android.content.Context;
import android.app.Application;

public class ActivityLifecycleManager
{
    private final Application application;
    private ActivityLifecycleCallbacksWrapper callbacksWrapper;
    
    public ActivityLifecycleManager(final Context context) {
        this.application = (Application)context.getApplicationContext();
        if (Build$VERSION.SDK_INT >= 14) {
            this.callbacksWrapper = new ActivityLifecycleCallbacksWrapper(this.application);
        }
    }
    
    public boolean registerCallbacks(final Callbacks callbacks) {
        return this.callbacksWrapper != null && this.callbacksWrapper.registerLifecycleCallbacks(callbacks);
    }
    
    public void resetCallbacks() {
        if (this.callbacksWrapper != null) {
            this.callbacksWrapper.clearCallbacks();
        }
    }
    
    private static class ActivityLifecycleCallbacksWrapper
    {
        private final Application application;
        private final Set<Application$ActivityLifecycleCallbacks> registeredCallbacks;
        
        ActivityLifecycleCallbacksWrapper(final Application application) {
            this.registeredCallbacks = new HashSet<Application$ActivityLifecycleCallbacks>();
            this.application = application;
        }
        
        @TargetApi(14)
        private void clearCallbacks() {
            final Iterator<Application$ActivityLifecycleCallbacks> iterator = this.registeredCallbacks.iterator();
            while (iterator.hasNext()) {
                this.application.unregisterActivityLifecycleCallbacks((Application$ActivityLifecycleCallbacks)iterator.next());
            }
        }
        
        @TargetApi(14)
        private boolean registerLifecycleCallbacks(final Callbacks callbacks) {
            if (this.application != null) {
                final Application$ActivityLifecycleCallbacks application$ActivityLifecycleCallbacks = (Application$ActivityLifecycleCallbacks)new Application$ActivityLifecycleCallbacks() {
                    public void onActivityCreated(final Activity activity, final Bundle bundle) {
                        callbacks.onActivityCreated(activity, bundle);
                    }
                    
                    public void onActivityDestroyed(final Activity activity) {
                        callbacks.onActivityDestroyed(activity);
                    }
                    
                    public void onActivityPaused(final Activity activity) {
                        callbacks.onActivityPaused(activity);
                    }
                    
                    public void onActivityResumed(final Activity activity) {
                        callbacks.onActivityResumed(activity);
                    }
                    
                    public void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {
                        callbacks.onActivitySaveInstanceState(activity, bundle);
                    }
                    
                    public void onActivityStarted(final Activity activity) {
                        callbacks.onActivityStarted(activity);
                    }
                    
                    public void onActivityStopped(final Activity activity) {
                        callbacks.onActivityStopped(activity);
                    }
                };
                this.application.registerActivityLifecycleCallbacks((Application$ActivityLifecycleCallbacks)application$ActivityLifecycleCallbacks);
                this.registeredCallbacks.add((Application$ActivityLifecycleCallbacks)application$ActivityLifecycleCallbacks);
                return true;
            }
            return false;
        }
    }
    
    public abstract static class Callbacks
    {
        public void onActivityCreated(final Activity activity, final Bundle bundle) {
        }
        
        public void onActivityDestroyed(final Activity activity) {
        }
        
        public void onActivityPaused(final Activity activity) {
        }
        
        public void onActivityResumed(final Activity activity) {
        }
        
        public void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {
        }
        
        public void onActivityStarted(final Activity activity) {
        }
        
        public void onActivityStopped(final Activity activity) {
        }
    }
}
