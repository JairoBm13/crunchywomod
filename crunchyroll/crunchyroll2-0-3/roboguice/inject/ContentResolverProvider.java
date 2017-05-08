// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import com.google.inject.Inject;
import android.content.Context;
import android.content.ContentResolver;
import com.google.inject.Provider;

@ContextSingleton
public class ContentResolverProvider implements Provider<ContentResolver>
{
    @Inject
    protected Context context;
    
    @Override
    public ContentResolver get() {
        return this.context.getContentResolver();
    }
}
