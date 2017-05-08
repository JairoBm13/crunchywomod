// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.monitor;

import java.util.Map;
import com.conviva.StreamerError;

public interface IMonitorNotifier
{
    void Log(final String p0);
    
    void OnError(final StreamerError p0);
    
    void OnMetadata(final Map<String, String> p0);
    
    void SetPlayingState(final int p0);
    
    void SetStream(final int p0, final String p1, final String p2);
}
