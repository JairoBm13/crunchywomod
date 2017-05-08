// 
// Decompiled by Procyon v0.5.30
// 

package de.greenrobot.event.util;

import de.greenrobot.event.EventBus;

public class ErrorDialogConfig
{
    EventBus eventBus;
    
    EventBus getEventBus() {
        if (this.eventBus != null) {
            return this.eventBus;
        }
        return EventBus.getDefault();
    }
}
