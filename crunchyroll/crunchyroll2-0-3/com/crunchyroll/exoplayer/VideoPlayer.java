// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.exoplayer;

import android.view.View$OnTouchListener;
import java.util.List;

public interface VideoPlayer extends VideoControllerListener
{
    void addPlayerEventListener(final PlayerEventListener p0);
    
    void addVideo(final String p0);
    
    void addVideos(final List<String> p0);
    
    int getDuration();
    
    long getVideoPosition();
    
    void init();
    
    boolean isPlaying();
    
    boolean isPlayingLastVideo();
    
    void pause();
    
    void play();
    
    void playNext();
    
    void release();
    
    void seekTo(final long p0);
    
    void setBufferLengths(final int p0, final int p1, final int p2, final int p3);
    
    void setErrorListener(final ErrorListener p0);
    
    void setOnTouchListener(final View$OnTouchListener p0);
    
    void setPlayhead(final long p0);
    
    void setStateChangeListener(final StateChangeListener p0);
    
    void setVideoController(final VideoController p0);
    
    void setVideoQuality(final VideoQuality p0);
    
    void stop();
    
    public interface ErrorListener
    {
        void onError(final Exception p0);
    }
    
    public interface PlayerEventListener
    {
        void onFastForward();
        
        void onPause();
        
        void onPlay();
        
        void onPlayNext();
        
        void onRewind();
        
        void onStop();
        
        void onVideoSizeChanged(final int p0, final int p1);
    }
    
    public enum PlayerState
    {
        STATE_BUFFERING, 
        STATE_ENDED, 
        STATE_IDLE, 
        STATE_PREPARING, 
        STATE_READY;
    }
    
    public interface StateChangeListener
    {
        void onStateChange(final PlayerState p0);
    }
    
    public interface VideoProgressChangeListener
    {
        void onProgressChange(final long p0);
    }
    
    public enum VideoQuality
    {
        QUALITY_ADAPTIVE, 
        QUALITY_HIGH, 
        QUALITY_LOW;
    }
}
