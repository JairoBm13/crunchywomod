// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.platforms;

import java.util.TimerTask;

public interface PlatformApi
{
    void cleanup();
    
    void createPollTask(final TimerTask p0, final int p1);
    
    void createTimer(final TimerTask p0, final int p1, final int p2, final String p3);
}
