// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.widget;

import com.crunchyroll.crunchyroid.util.Util;
import android.media.MediaPlayer;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import com.crunchyroll.android.util.LoggerFactory;
import android.net.Uri;
import android.media.MediaPlayer$OnVideoSizeChangedListener;
import android.media.MediaPlayer$OnSeekCompleteListener;
import com.crunchyroll.android.util.Logger;
import android.media.MediaPlayer$OnPreparedListener;
import android.widget.VideoView;

public class CustomVideoView extends VideoView implements MediaPlayer$OnPreparedListener, AbstractVideoView
{
    private static final Logger logger;
    private MediaPlayer$OnPreparedListener mExternalOnPreparedListener;
    private onBufferingListener mOnBufferingListener;
    private OnProgressListener mOnProgressListener;
    private MediaPlayer$OnSeekCompleteListener mOnSeekCompleteListener;
    private OnSeekStartListener mOnSeekStartListener;
    private MediaPlayer$OnVideoSizeChangedListener mOnVideoSizeChangedListener;
    private OnPlayActionListener mPlayListener;
    private Uri mVideoUri;
    
    static {
        logger = LoggerFactory.getLogger(CustomVideoView.class);
    }
    
    public CustomVideoView(final Context context) {
        super(context);
    }
    
    public CustomVideoView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public CustomVideoView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public Uri getVideoUri() {
        return this.mVideoUri;
    }
    
    public View getView() {
        return (View)this;
    }
    
    public void init() {
        super.setOnPreparedListener((MediaPlayer$OnPreparedListener)this);
    }
    
    public void init(final String s) {
        this.init();
    }
    
    public void next() {
        if (this.mPlayListener != null) {
            this.mPlayListener.onNext();
        }
    }
    
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        super.stopPlayback();
    }
    
    public void onPrepared(final MediaPlayer mediaPlayer) {
        this.mExternalOnPreparedListener.onPrepared(mediaPlayer);
        if (mediaPlayer != null) {
            mediaPlayer.setOnVideoSizeChangedListener((MediaPlayer$OnVideoSizeChangedListener)new MediaPlayer$OnVideoSizeChangedListener() {
                public void onVideoSizeChanged(final MediaPlayer mediaPlayer, final int n, final int n2) {
                    CustomVideoView.this.mOnVideoSizeChangedListener.onVideoSizeChanged(mediaPlayer, n, n2);
                }
            });
            mediaPlayer.setOnSeekCompleteListener((MediaPlayer$OnSeekCompleteListener)new MediaPlayer$OnSeekCompleteListener() {
                public void onSeekComplete(final MediaPlayer mediaPlayer) {
                    CustomVideoView.this.mOnSeekCompleteListener.onSeekComplete(mediaPlayer);
                }
            });
        }
        if (this.mOnProgressListener != null) {
            this.mOnProgressListener.onFirstProgressUpdate();
        }
    }
    
    public void pause() {
        super.pause();
        final int currentPosition = this.getCurrentPosition();
        CustomVideoView.logger.debug("Pause video at %s (%dmsec)", Util.stringFromDuration(currentPosition), currentPosition);
        if (this.mPlayListener != null) {
            this.mPlayListener.onPause(currentPosition);
        }
    }
    
    public void seekTo(final int n) {
        CustomVideoView.logger.debug("Seek to %s (%d)", Util.stringFromDuration(n), n);
        super.seekTo(n);
        if (this.mOnSeekStartListener != null) {
            this.mOnSeekStartListener.onSeekStart();
        }
    }
    
    public void setAutoplay(final boolean b) {
        if (this.mPlayListener != null) {
            this.mPlayListener.onAutoplayChanged(b);
        }
    }
    
    public void setExternalOnPreparedListener(final MediaPlayer$OnPreparedListener mExternalOnPreparedListener) {
        this.mExternalOnPreparedListener = mExternalOnPreparedListener;
    }
    
    public void setOnBufferingListener(final onBufferingListener mOnBufferingListener) {
        this.mOnBufferingListener = mOnBufferingListener;
    }
    
    public void setOnPlayActionListener(final OnPlayActionListener mPlayListener) {
        this.mPlayListener = mPlayListener;
    }
    
    public void setOnSeekCompleteListener(final MediaPlayer$OnSeekCompleteListener mOnSeekCompleteListener) {
        this.mOnSeekCompleteListener = mOnSeekCompleteListener;
    }
    
    public void setOnVideoSizeChangedListener(final MediaPlayer$OnVideoSizeChangedListener mOnVideoSizeChangedListener) {
        this.mOnVideoSizeChangedListener = mOnVideoSizeChangedListener;
    }
    
    public void setProgressListener(final OnProgressListener mOnProgressListener) {
        this.mOnProgressListener = mOnProgressListener;
    }
    
    public void setSeekStartListener(final OnSeekStartListener mOnSeekStartListener) {
        this.mOnSeekStartListener = mOnSeekStartListener;
    }
    
    public void setVideoURI(final Uri uri) {
        super.setVideoURI(uri);
        this.mVideoUri = uri;
    }
    
    public void setVideoUri(final Uri videoURI) {
        super.setVideoURI(videoURI);
    }
    
    public void start() {
        super.start();
        CustomVideoView.logger.debug("Start video", new Object[0]);
        if (this.mPlayListener != null) {
            this.mPlayListener.onPlay();
        }
    }
    
    public void stopPlayback() {
        CustomVideoView.logger.debug("Stop video", new Object[0]);
        super.stopPlayback();
        if (this.mPlayListener != null) {
            this.mPlayListener.onStop();
        }
    }
}
