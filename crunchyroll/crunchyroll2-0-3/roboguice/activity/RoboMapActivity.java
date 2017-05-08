// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.activity;

import roboguice.activity.event.OnStopEvent;
import roboguice.activity.event.OnStartEvent;
import roboguice.activity.event.OnResumeEvent;
import roboguice.activity.event.OnRestartEvent;
import roboguice.activity.event.OnPauseEvent;
import roboguice.activity.event.OnNewIntentEvent;
import roboguice.activity.event.OnDestroyEvent;
import roboguice.inject.RoboInjector;
import roboguice.activity.event.OnCreateEvent;
import android.os.Bundle;
import roboguice.activity.event.OnContentChangedEvent;
import android.app.Activity;
import android.content.Context;
import roboguice.RoboGuice;
import roboguice.activity.event.OnConfigurationChangedEvent;
import android.content.res.Configuration;
import roboguice.activity.event.OnActivityResultEvent;
import android.content.Intent;
import java.util.Map;
import com.google.inject.Key;
import java.util.HashMap;
import com.google.inject.Inject;
import roboguice.inject.ContentViewListener;
import roboguice.event.EventManager;
import roboguice.util.RoboContext;
import com.google.android.maps.MapActivity;

public abstract class RoboMapActivity extends MapActivity implements RoboContext
{
    protected EventManager eventManager;
    @Inject
    ContentViewListener ignored;
    protected HashMap<Key<?>, Object> scopedObjects;
    
    public RoboMapActivity() {
        this.scopedObjects = new HashMap<Key<?>, Object>();
    }
    
    public Map<Key<?>, Object> getScopedObjectMap() {
        return this.scopedObjects;
    }
    
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        super.onActivityResult(n, n2, intent);
        this.eventManager.fire(new OnActivityResultEvent(n, n2, intent));
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        final Configuration configuration2 = this.getResources().getConfiguration();
        super.onConfigurationChanged(configuration);
        this.eventManager.fire(new OnConfigurationChangedEvent(configuration2, configuration));
    }
    
    public void onContentChanged() {
        super.onContentChanged();
        RoboGuice.getInjector((Context)this).injectViewMembers((Activity)this);
        this.eventManager.fire(new OnContentChangedEvent());
    }
    
    protected void onCreate(final Bundle bundle) {
        final RoboInjector injector = RoboGuice.getInjector((Context)this);
        this.eventManager = injector.getInstance(EventManager.class);
        injector.injectMembersWithoutViews(this);
        super.onCreate(bundle);
        this.eventManager.fire(new OnCreateEvent(bundle));
    }
    
    protected void onDestroy() {
        try {
            this.eventManager.fire(new OnDestroyEvent());
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
    
    public void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        this.eventManager.fire(new OnNewIntentEvent());
    }
    
    protected void onPause() {
        super.onPause();
        this.eventManager.fire(new OnPauseEvent());
    }
    
    protected void onRestart() {
        super.onRestart();
        this.eventManager.fire(new OnRestartEvent());
    }
    
    protected void onResume() {
        super.onResume();
        this.eventManager.fire(new OnResumeEvent());
    }
    
    protected void onStart() {
        super.onStart();
        this.eventManager.fire(new OnStartEvent());
    }
    
    protected void onStop() {
        try {
            this.eventManager.fire(new OnStopEvent());
        }
        finally {
            super.onStop();
        }
    }
}
