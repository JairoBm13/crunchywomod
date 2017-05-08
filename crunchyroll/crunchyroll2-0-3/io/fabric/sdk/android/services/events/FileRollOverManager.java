// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.events;

import java.io.IOException;

public interface FileRollOverManager
{
    void cancelTimeBasedFileRollOver();
    
    boolean rollFileOver() throws IOException;
}
