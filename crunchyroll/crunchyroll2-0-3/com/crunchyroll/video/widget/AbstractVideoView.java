// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.widget;

import android.media.MediaPlayer$OnVideoSizeChangedListener;
import android.media.MediaPlayer$OnSeekCompleteListener;
import android.view.View$OnKeyListener;
import android.media.MediaPlayer$OnErrorListener;
import android.media.MediaPlayer$OnCompletionListener;
import android.media.MediaPlayer$OnPreparedListener;
import android.view.View;
import android.net.Uri;

public interface AbstractVideoView
{
    int getBufferPercentage();
    
    int getCurrentPosition();
    
    int getDuration();
    
    Uri getVideoUri();
    
    View getView();
    
    void init();
    
    void init(final String p0);
    
    boolean isPlaying();
    
    void next();
    
    void pause();
    
    void seekTo(final int p0);
    
    void setAutoplay(final boolean p0);
    
    void setExternalOnPreparedListener(final MediaPlayer$OnPreparedListener p0);
    
    void setKeepScreenOn(final boolean p0);
    
    void setOnBufferingListener(final onBufferingListener p0);
    
    void setOnCompletionListener(final MediaPlayer$OnCompletionListener p0);
    
    void setOnErrorListener(final MediaPlayer$OnErrorListener p0);
    
    void setOnKeyListener(final View$OnKeyListener p0);
    
    void setOnPlayActionListener(final OnPlayActionListener p0);
    
    void setOnPreparedListener(final MediaPlayer$OnPreparedListener p0);
    
    void setOnSeekCompleteListener(final MediaPlayer$OnSeekCompleteListener p0);
    
    void setOnVideoSizeChangedListener(final MediaPlayer$OnVideoSizeChangedListener p0);
    
    void setProgressListener(final OnProgressListener p0);
    
    void setSeekStartListener(final OnSeekStartListener p0);
    
    void setVideoUri(final Uri p0);
    
    void setVisibility(final int p0);
    
    void start();
    
    void stopPlayback();
    
    public abstract static class OnPlayActionListener
    {
        public abstract void onAutoplayChanged(final boolean p0);
        
        public abstract void onNext();
        
        public abstract void onPause(final int p0);
        
        public abstract void onPlay();
        
        public abstract void onStop();
    }
    
    public interface OnProgressListener
    {
        void onFirstProgressUpdate();
    }
    
    public interface OnSeekStartListener
    {
        void onSeekStart();
    }
    
    public interface onBufferingListener
    {
        void onVideoStartBuff();
        
        void onVideoStopBuff();
    }
}
