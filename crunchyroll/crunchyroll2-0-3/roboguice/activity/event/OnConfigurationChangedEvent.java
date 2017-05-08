// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.activity.event;

import android.content.res.Configuration;

public class OnConfigurationChangedEvent
{
    protected Configuration newConfig;
    protected Configuration oldConfig;
    
    public OnConfigurationChangedEvent(final Configuration oldConfig, final Configuration newConfig) {
        this.oldConfig = oldConfig;
        this.newConfig = newConfig;
    }
    
    public Configuration getNewConfig() {
        return this.newConfig;
    }
    
    public Configuration getOldConfig() {
        return this.oldConfig;
    }
}
