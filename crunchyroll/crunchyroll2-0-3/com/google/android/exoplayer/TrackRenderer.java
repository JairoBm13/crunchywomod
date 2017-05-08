// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer;

import com.google.android.exoplayer.util.Assertions;

public abstract class TrackRenderer implements ExoPlayerComponent
{
    public static final long END_OF_TRACK_US = -3L;
    public static final long MATCH_LONGEST_US = -2L;
    protected static final int STATE_ENABLED = 2;
    protected static final int STATE_PREPARED = 1;
    protected static final int STATE_RELEASED = -1;
    protected static final int STATE_STARTED = 3;
    protected static final int STATE_UNPREPARED = 0;
    public static final long UNKNOWN_TIME_US = -1L;
    private int state;
    
    final void disable() throws ExoPlaybackException {
        Assertions.checkState(this.state == 2);
        this.state = 1;
        this.onDisabled();
    }
    
    protected abstract boolean doPrepare(final long p0) throws ExoPlaybackException;
    
    protected abstract void doSomeWork(final long p0, final long p1) throws ExoPlaybackException;
    
    final void enable(final int n, final long n2, final boolean b) throws ExoPlaybackException {
        boolean b2 = true;
        if (this.state != 1) {
            b2 = false;
        }
        Assertions.checkState(b2);
        this.state = 2;
        this.onEnabled(n, n2, b);
    }
    
    protected abstract long getBufferedPositionUs();
    
    protected abstract long getDurationUs();
    
    protected abstract MediaFormat getFormat(final int p0);
    
    protected MediaClock getMediaClock() {
        return null;
    }
    
    protected final int getState() {
        return this.state;
    }
    
    protected abstract int getTrackCount();
    
    @Override
    public void handleMessage(final int n, final Object o) throws ExoPlaybackException {
    }
    
    protected abstract boolean isEnded();
    
    protected abstract boolean isReady();
    
    protected abstract void maybeThrowError() throws ExoPlaybackException;
    
    protected void onDisabled() throws ExoPlaybackException {
    }
    
    protected void onEnabled(final int n, final long n2, final boolean b) throws ExoPlaybackException {
    }
    
    protected void onReleased() throws ExoPlaybackException {
    }
    
    protected void onStarted() throws ExoPlaybackException {
    }
    
    protected void onStopped() throws ExoPlaybackException {
    }
    
    final int prepare(final long n) throws ExoPlaybackException {
        boolean state = true;
        Assertions.checkState(this.state == 0);
        if (!this.doPrepare(n)) {
            state = false;
        }
        return this.state = (state ? 1 : 0);
    }
    
    final void release() throws ExoPlaybackException {
        Assertions.checkState(this.state != 2 && this.state != 3 && this.state != -1);
        this.state = -1;
        this.onReleased();
    }
    
    protected abstract void seekTo(final long p0) throws ExoPlaybackException;
    
    final void start() throws ExoPlaybackException {
        Assertions.checkState(this.state == 2);
        this.state = 3;
        this.onStarted();
    }
    
    final void stop() throws ExoPlaybackException {
        Assertions.checkState(this.state == 3);
        this.state = 2;
        this.onStopped();
    }
}
