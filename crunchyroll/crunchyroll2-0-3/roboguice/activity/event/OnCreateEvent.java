// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.activity.event;

import android.os.Bundle;

public class OnCreateEvent
{
    protected Bundle savedInstanceState;
    
    public OnCreateEvent(final Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
    }
    
    public Bundle getSavedInstanceState() {
        return this.savedInstanceState;
    }
}
