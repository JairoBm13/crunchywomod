// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer;

import java.util.Arrays;
import java.util.Iterator;
import android.os.Message;
import android.util.Log;
import java.util.concurrent.CopyOnWriteArraySet;
import android.os.Handler;

final class ExoPlayerImpl implements ExoPlayer
{
    private final Handler eventHandler;
    private final ExoPlayerImplInternal internalPlayer;
    private final CopyOnWriteArraySet<Listener> listeners;
    private int pendingPlayWhenReadyAcks;
    private boolean playWhenReady;
    private int playbackState;
    private final int[] selectedTrackIndices;
    private final MediaFormat[][] trackFormats;
    
    public ExoPlayerImpl(final int n, final int n2, final int n3) {
        Log.i("ExoPlayerImpl", "Init 1.5.0");
        this.playWhenReady = false;
        this.playbackState = 1;
        this.listeners = new CopyOnWriteArraySet<Listener>();
        this.trackFormats = new MediaFormat[n][];
        this.selectedTrackIndices = new int[n];
        this.eventHandler = new Handler() {
            public void handleMessage(final Message message) {
                ExoPlayerImpl.this.handleEvent(message);
            }
        };
        this.internalPlayer = new ExoPlayerImplInternal(this.eventHandler, this.playWhenReady, this.selectedTrackIndices, n2, n3);
    }
    
    @Override
    public void addListener(final Listener listener) {
        this.listeners.add(listener);
    }
    
    @Override
    public long getCurrentPosition() {
        return this.internalPlayer.getCurrentPosition();
    }
    
    @Override
    public long getDuration() {
        return this.internalPlayer.getDuration();
    }
    
    @Override
    public boolean getPlayWhenReady() {
        return this.playWhenReady;
    }
    
    @Override
    public int getPlaybackState() {
        return this.playbackState;
    }
    
    void handleEvent(final Message message) {
        switch (message.what) {
            case 1: {
                System.arraycopy(message.obj, 0, this.trackFormats, 0, this.trackFormats.length);
                this.playbackState = message.arg1;
                final Iterator<Listener> iterator = this.listeners.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onPlayerStateChanged(this.playWhenReady, this.playbackState);
                }
                break;
            }
            case 2: {
                this.playbackState = message.arg1;
                final Iterator<Listener> iterator2 = this.listeners.iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().onPlayerStateChanged(this.playWhenReady, this.playbackState);
                }
                break;
            }
            case 3: {
                --this.pendingPlayWhenReadyAcks;
                if (this.pendingPlayWhenReadyAcks == 0) {
                    final Iterator<Listener> iterator3 = this.listeners.iterator();
                    while (iterator3.hasNext()) {
                        iterator3.next().onPlayWhenReadyCommitted();
                    }
                    break;
                }
                break;
            }
            case 4: {
                final ExoPlaybackException ex = (ExoPlaybackException)message.obj;
                final Iterator<Listener> iterator4 = this.listeners.iterator();
                while (iterator4.hasNext()) {
                    iterator4.next().onPlayerError(ex);
                }
                break;
            }
        }
    }
    
    @Override
    public void prepare(final TrackRenderer... array) {
        Arrays.fill(this.trackFormats, null);
        this.internalPlayer.prepare(array);
    }
    
    @Override
    public void release() {
        this.internalPlayer.release();
        this.eventHandler.removeCallbacksAndMessages((Object)null);
    }
    
    @Override
    public void removeListener(final Listener listener) {
        this.listeners.remove(listener);
    }
    
    @Override
    public void seekTo(final long n) {
        this.internalPlayer.seekTo(n);
    }
    
    @Override
    public void sendMessage(final ExoPlayerComponent exoPlayerComponent, final int n, final Object o) {
        this.internalPlayer.sendMessage(exoPlayerComponent, n, o);
    }
    
    @Override
    public void setPlayWhenReady(final boolean b) {
        if (this.playWhenReady != b) {
            this.playWhenReady = b;
            ++this.pendingPlayWhenReadyAcks;
            this.internalPlayer.setPlayWhenReady(b);
            final Iterator<Listener> iterator = this.listeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onPlayerStateChanged(b, this.playbackState);
            }
        }
    }
    
    @Override
    public void stop() {
        this.internalPlayer.stop();
    }
}
