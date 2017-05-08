// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva;

import com.conviva.monitor.IMonitorNotifier;

public interface ConvivaStreamerProxy
{
    public static final int BOOLEAN_FALSE = -1;
    public static final int BOOLEAN_TRUE = 1;
    public static final int BOOLEAN_UNAVAILABLE = 0;
    public static final String BUFFERING = "BUFFERING";
    public static final int CAPABILITY_BITRATE_EXTERNAL = 16;
    public static final int CAPABILITY_BITRATE_METRICS = 4;
    public static final int CAPABILITY_QUALITY_METRICS = 2;
    public static final int CAPABILITY_VIDEO = 1;
    public static final String ERROR = "ERROR";
    public static final String METADATA_DURATION = "duration";
    public static final String METADATA_ENCODED_FRAMERATE = "framerate";
    public static final String PAUSED = "PAUSED";
    public static final String PLAYING = "PLAYING";
    public static final String STOPPED = "STOPPED";
    public static final String UNKNOWN = "UNKNOWN";
    
    void Cleanup();
    
    int GetBufferLengthMs();
    
    int GetCapabilities();
    
    int GetDroppedFrames();
    
    int GetIsLive();
    
    int GetMinBufferLengthMs();
    
    int GetPlayheadTimeMs();
    
    double GetRenderedFrameRate();
    
    String GetServerAddress();
    
    int GetStartingBufferLengthMs();
    
    String GetStreamerType();
    
    String GetStreamerVersion();
    
    void setMonitoringNotifier(final IMonitorNotifier p0);
}
