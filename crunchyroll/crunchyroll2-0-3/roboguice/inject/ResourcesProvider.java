// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import android.app.Application;
import com.google.inject.Singleton;
import android.content.res.Resources;
import com.google.inject.Provider;

@Singleton
public class ResourcesProvider implements Provider<Resources>
{
    protected Resources resources;
    
    public ResourcesProvider(final Application application) {
        this.resources = application.getResources();
    }
    
    @Override
    public Resources get() {
        return this.resources;
    }
}
