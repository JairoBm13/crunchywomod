// 
// Decompiled by Procyon v0.5.30
// 

package de.greenrobot.event.util;

import de.greenrobot.event.EventBus;
import android.annotation.TargetApi;
import android.app.Fragment;

public class ErrorDialogManager
{
    public static ErrorDialogFragmentFactory<?> factory;
    
    @TargetApi(11)
    public static class HoneycombManagerFragment extends Fragment
    {
        private EventBus eventBus;
        
        public void onPause() {
            this.eventBus.unregister(this);
            super.onPause();
        }
        
        public void onResume() {
            super.onResume();
            (this.eventBus = ErrorDialogManager.factory.config.getEventBus()).register(this);
        }
    }
}
