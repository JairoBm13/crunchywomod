// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer;

public interface ExoPlayer
{
    void addListener(final Listener p0);
    
    long getCurrentPosition();
    
    long getDuration();
    
    boolean getPlayWhenReady();
    
    int getPlaybackState();
    
    void prepare(final TrackRenderer... p0);
    
    void release();
    
    void removeListener(final Listener p0);
    
    void seekTo(final long p0);
    
    void sendMessage(final ExoPlayerComponent p0, final int p1, final Object p2);
    
    void setPlayWhenReady(final boolean p0);
    
    void stop();
    
    public interface ExoPlayerComponent
    {
        void handleMessage(final int p0, final Object p1) throws ExoPlaybackException;
    }
    
    public static final class Factory
    {
        public static ExoPlayer newInstance(final int n, final int n2, final int n3) {
            return new ExoPlayerImpl(n, n2, n3);
        }
    }
    
    public interface Listener
    {
        void onPlayWhenReadyCommitted();
        
        void onPlayerError(final ExoPlaybackException p0);
        
        void onPlayerStateChanged(final boolean p0, final int p1);
    }
}
