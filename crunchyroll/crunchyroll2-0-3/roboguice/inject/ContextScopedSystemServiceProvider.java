// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import android.content.Context;
import com.google.inject.Provider;

public class ContextScopedSystemServiceProvider<T> implements Provider<T>
{
    protected Provider<Context> contextProvider;
    protected String serviceName;
    
    public ContextScopedSystemServiceProvider(final Provider<Context> contextProvider, final String serviceName) {
        this.contextProvider = contextProvider;
        this.serviceName = serviceName;
    }
    
    @Override
    public T get() {
        return (T)this.contextProvider.get().getSystemService(this.serviceName);
    }
}
