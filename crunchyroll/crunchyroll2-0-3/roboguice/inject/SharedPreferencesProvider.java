// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import android.content.Context;
import android.preference.PreferenceManager;
import java.io.File;
import com.google.inject.Inject;
import android.app.Application;
import android.content.SharedPreferences;
import com.google.inject.Provider;

public class SharedPreferencesProvider implements Provider<SharedPreferences>
{
    protected static final String ROBOGUICE_1_DEFAULT_FILENAME = "default.xml";
    @Inject
    protected Application application;
    protected String preferencesName;
    
    public SharedPreferencesProvider() {
    }
    
    public SharedPreferencesProvider(final String preferencesName) {
        this.preferencesName = preferencesName;
    }
    
    public SharedPreferencesProvider(final PreferencesNameHolder preferencesNameHolder) {
        this.preferencesName = preferencesNameHolder.value;
    }
    
    @Override
    public SharedPreferences get() {
        if (this.preferencesName != null) {
            return this.application.getSharedPreferences(this.preferencesName, 0);
        }
        if (new File("shared_prefs/default.xml").canRead()) {
            return this.application.getSharedPreferences("default.xml", 0);
        }
        return PreferenceManager.getDefaultSharedPreferences((Context)this.application);
    }
    
    public static class PreferencesNameHolder
    {
        @Inject(optional = true)
        @SharedPreferencesName
        protected String value;
    }
}
