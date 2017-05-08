// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.ima;

import java.util.Iterator;
import android.view.SurfaceHolder;
import android.media.MediaPlayer;
import java.util.ArrayList;
import android.content.Context;
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
import java.util.List;
import android.media.MediaPlayer$OnErrorListener;
import android.media.MediaPlayer$OnCompletionListener;
import android.widget.VideoView;

public class TrackingVideoView extends VideoView implements MediaPlayer$OnCompletionListener, MediaPlayer$OnErrorListener
{
    final List<VideoAdPlayer.VideoAdPlayerCallback> mAdCallbacks;
    CompleteCallback mCompleteCallback;
    PlaybackState mState;
    
    public TrackingVideoView(final Context context) {
        super(context);
        this.mAdCallbacks = new ArrayList<VideoAdPlayer.VideoAdPlayerCallback>(1);
        super.setOnCompletionListener((MediaPlayer$OnCompletionListener)this);
        super.setOnErrorListener((MediaPlayer$OnErrorListener)this);
        this.mState = PlaybackState.Stopped;
    }
    
    private void onStop() {
        this.mState = PlaybackState.Stopped;
    }
    
    public void addCallback(final VideoAdPlayer.VideoAdPlayerCallback videoAdPlayerCallback) {
        this.mAdCallbacks.add(videoAdPlayerCallback);
    }
    
    public void onCompletion(final MediaPlayer mediaPlayer) {
        mediaPlayer.setDisplay((SurfaceHolder)null);
        mediaPlayer.reset();
        mediaPlayer.setDisplay(this.getHolder());
        this.onStop();
        final Iterator<VideoAdPlayer.VideoAdPlayerCallback> iterator = this.mAdCallbacks.iterator();
        while (iterator.hasNext()) {
            iterator.next().onEnded();
        }
        this.mCompleteCallback.onComplete();
    }
    
    public boolean onError(final MediaPlayer mediaPlayer, final int n, final int n2) {
        final Iterator<VideoAdPlayer.VideoAdPlayerCallback> iterator = this.mAdCallbacks.iterator();
        while (iterator.hasNext()) {
            iterator.next().onError();
        }
        this.onStop();
        return true;
    }
    
    public void pause() {
        super.pause();
        final Iterator<VideoAdPlayer.VideoAdPlayerCallback> iterator = this.mAdCallbacks.iterator();
        while (iterator.hasNext()) {
            iterator.next().onPause();
        }
        this.mState = PlaybackState.Paused;
    }
    
    public void removeCallback(final VideoAdPlayer.VideoAdPlayerCallback videoAdPlayerCallback) {
        this.mAdCallbacks.remove(videoAdPlayerCallback);
    }
    
    public void setCompleteCallback(final CompleteCallback mCompleteCallback) {
        this.mCompleteCallback = mCompleteCallback;
    }
    
    public void start() {
        super.start();
        switch (this.mState) {
            case Stopped: {
                final Iterator<VideoAdPlayer.VideoAdPlayerCallback> iterator = this.mAdCallbacks.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onPlay();
                }
                break;
            }
            case Paused: {
                final Iterator<VideoAdPlayer.VideoAdPlayerCallback> iterator2 = this.mAdCallbacks.iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().onResume();
                }
                break;
            }
        }
        this.mState = PlaybackState.Playing;
    }
    
    public void stopPlayback() {
        super.stopPlayback();
        this.onStop();
    }
    
    public interface CompleteCallback
    {
        void onComplete();
    }
    
    private enum PlaybackState
    {
        Paused, 
        Playing, 
        Stopped;
    }
}
