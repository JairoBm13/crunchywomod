// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.ima;

import android.view.ViewGroup;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.RelativeLayout$LayoutParams;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.FrameLayout;
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
import android.widget.RelativeLayout;

public class IMAPlayer extends RelativeLayout implements VideoAdPlayer
{
    FrameLayout adUiContainer;
    int savedPosition;
    TrackingVideoView video;
    
    public IMAPlayer(final Context context) {
        super(context);
        this.init();
    }
    
    public IMAPlayer(final Context context, final AttributeSet set) {
        super(context, set);
        this.init();
    }
    
    public IMAPlayer(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.init();
    }
    
    private void init() {
        this.savedPosition = 0;
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(-1, -1);
        relativeLayout$LayoutParams.addRule(13);
        this.addView((View)(this.video = new TrackingVideoView(this.getContext())), (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        this.addView((View)(this.adUiContainer = new FrameLayout(this.getContext())), -1);
    }
    
    public void addCallback(final VideoAdPlayerCallback videoAdPlayerCallback) {
        this.video.addCallback(videoAdPlayerCallback);
    }
    
    public VideoProgressUpdate getProgress() {
        final int duration = this.video.getDuration();
        if (duration <= 0) {
            return VideoProgressUpdate.VIDEO_TIME_NOT_READY;
        }
        return new VideoProgressUpdate(this.video.getCurrentPosition(), duration);
    }
    
    public ViewGroup getUiContainer() {
        return (ViewGroup)this.adUiContainer;
    }
    
    public void loadAd(final String videoPath) {
        this.video.setVideoPath(videoPath);
    }
    
    public void pauseAd() {
        this.video.pause();
    }
    
    public void playAd() {
        this.video.start();
    }
    
    public void removeCallback(final VideoAdPlayerCallback videoAdPlayerCallback) {
        this.video.removeCallback(videoAdPlayerCallback);
    }
    
    public void restorePosition() {
        this.video.seekTo(this.savedPosition);
    }
    
    public void resumeAd() {
        this.video.start();
    }
    
    public void savePosition() {
        this.savedPosition = this.video.getCurrentPosition();
    }
    
    public void setCompleteCallback(final TrackingVideoView.CompleteCallback completeCallback) {
        this.video.setCompleteCallback(completeCallback);
    }
    
    public void stopAd() {
        this.video.stopPlayback();
    }
}
