// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import com.google.inject.Inject;
import android.content.Context;
import android.content.res.AssetManager;
import com.google.inject.Provider;

public class AssetManagerProvider implements Provider<AssetManager>
{
    @Inject
    protected Context context;
    
    @Override
    public AssetManager get() {
        return this.context.getAssets();
    }
}
