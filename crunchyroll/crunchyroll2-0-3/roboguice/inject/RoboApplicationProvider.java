// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.Provider;
import android.app.Application;

@Singleton
public class RoboApplicationProvider<T extends Application> implements Provider<T>
{
    @Inject
    protected Application application;
    
    @Override
    public T get() {
        return (T)this.application;
    }
}
