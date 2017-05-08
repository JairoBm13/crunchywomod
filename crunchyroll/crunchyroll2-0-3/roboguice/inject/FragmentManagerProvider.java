// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import android.support.v4.app.FragmentActivity;
import com.google.inject.Inject;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import com.google.inject.Provider;

@ContextSingleton
public class FragmentManagerProvider implements Provider<FragmentManager>
{
    @Inject
    protected Activity activity;
    
    @Override
    public FragmentManager get() {
        return ((FragmentActivity)this.activity).getSupportFragmentManager();
    }
}
