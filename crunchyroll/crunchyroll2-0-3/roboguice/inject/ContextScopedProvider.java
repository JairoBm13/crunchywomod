// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import android.content.Context;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ContextScopedProvider<T>
{
    @Inject
    protected Provider<T> provider;
    @Inject
    protected ContextScope scope;
    
    public T get(final Context context) {
        synchronized (ContextScope.class) {
            this.scope.enter(context);
            try {
                return this.provider.get();
            }
            finally {
                this.scope.exit(context);
            }
        }
    }
}
