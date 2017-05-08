// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.exoplayer;

public interface VideoControllerListener
{
    void onFastForwardPressed(final int p0);
    
    void onNextPressed();
    
    void onPlayPausePressed();
    
    void onRewindPressed(final int p0);
    
    void onSeekProgressChanged(final int p0);
    
    void onStartSeekScrub();
    
    void onStopPressed();
    
    void onStopSeekScrub(final int p0);
}
