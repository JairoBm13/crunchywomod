// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.service;

import roboguice.service.event.OnStartEvent;
import android.content.Intent;
import roboguice.service.event.OnDestroyEvent;
import roboguice.inject.RoboInjector;
import roboguice.service.event.OnCreateEvent;
import android.content.Context;
import roboguice.RoboGuice;
import roboguice.service.event.OnConfigurationChangedEvent;
import android.content.res.Configuration;
import java.util.Map;
import com.google.inject.Key;
import java.util.HashMap;
import roboguice.event.EventManager;
import roboguice.util.RoboContext;
import android.app.Service;

public abstract class RoboService extends Service implements RoboContext
{
    protected EventManager eventManager;
    protected HashMap<Key<?>, Object> scopedObjects;
    
    public RoboService() {
        this.scopedObjects = new HashMap<Key<?>, Object>();
    }
    
    public Map<Key<?>, Object> getScopedObjectMap() {
        return this.scopedObjects;
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        final Configuration configuration2 = this.getResources().getConfiguration();
        super.onConfigurationChanged(configuration);
        this.eventManager.fire(new OnConfigurationChangedEvent(configuration2, configuration));
    }
    
    public void onCreate() {
        final RoboInjector injector = RoboGuice.getInjector((Context)this);
        this.eventManager = injector.getInstance(EventManager.class);
        injector.injectMembers(this);
        super.onCreate();
        this.eventManager.fire(new OnCreateEvent());
    }
    
    public void onDestroy() {
        try {
            if (this.eventManager != null) {
                this.eventManager.fire(new OnDestroyEvent());
            }
            try {
                RoboGuice.destroyInjector((Context)this);
            }
            finally {
                super.onDestroy();
            }
        }
        finally {
            try {
                RoboGuice.destroyInjector((Context)this);
                super.onDestroy();
            }
            finally {
                super.onDestroy();
            }
        }
    }
    
    public void onStart(final Intent intent, final int n) {
        super.onStart(intent, n);
        this.eventManager.fire(new OnStartEvent());
    }
}
