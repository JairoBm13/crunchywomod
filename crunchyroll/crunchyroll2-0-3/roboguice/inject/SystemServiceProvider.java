// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import com.google.inject.Inject;
import android.app.Application;
import com.google.inject.Provider;

public class SystemServiceProvider<T> implements Provider<T>
{
    @Inject
    protected Application application;
    protected String serviceName;
    
    public SystemServiceProvider(final String serviceName) {
        this.serviceName = serviceName;
    }
    
    @Override
    public T get() {
        return (T)this.application.getSystemService(this.serviceName);
    }
}
