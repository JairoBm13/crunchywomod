// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast.widget;

import android.util.AttributeSet;
import android.content.Context;
import android.net.Uri;
import android.widget.VideoView;

public class VastVideoView extends VideoView
{
    private OnPlayActionListener mPlayListener;
    private Uri mVideoUri;
    
    public VastVideoView(final Context context) {
        super(context);
    }
    
    public VastVideoView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public VastVideoView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public Uri getVideoUri() {
        return this.mVideoUri;
    }
    
    public void pause() {
        super.pause();
        final int currentPosition = this.getCurrentPosition();
        if (this.mPlayListener != null) {
            this.mPlayListener.onPause(currentPosition);
        }
    }
    
    public void seekTo(final int n) {
        super.seekTo(n);
    }
    
    public void setOnPlayActionListener(final OnPlayActionListener mPlayListener) {
        this.mPlayListener = mPlayListener;
    }
    
    public void setVideoURI(final Uri uri) {
        super.setVideoURI(uri);
        this.mVideoUri = uri;
    }
    
    public void start() {
        super.start();
        if (this.mPlayListener != null) {
            this.mPlayListener.onPlay();
        }
    }
    
    public void stopPlayback() {
        super.stopPlayback();
        if (this.mPlayListener != null) {
            this.mPlayListener.onStop();
        }
    }
    
    public abstract static class OnPlayActionListener
    {
        public abstract void onPause(final int p0);
        
        public abstract void onPlay();
        
        public abstract void onStop();
    }
}
