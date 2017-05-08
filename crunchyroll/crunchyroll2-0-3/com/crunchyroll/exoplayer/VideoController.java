// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.exoplayer;

import android.util.AttributeSet;
import java.util.ArrayList;
import android.content.Context;
import java.util.List;
import android.widget.SeekBar$OnSeekBarChangeListener;
import android.widget.FrameLayout;

public abstract class VideoController extends FrameLayout implements SeekBar$OnSeekBarChangeListener
{
    protected List<VideoControllerListener> mControllerListeners;
    
    public VideoController(final Context context) {
        super(context);
        this.mControllerListeners = new ArrayList<VideoControllerListener>();
    }
    
    public VideoController(final Context context, final AttributeSet set) {
        super(context, set);
        this.mControllerListeners = new ArrayList<VideoControllerListener>();
    }
    
    public VideoController(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mControllerListeners = new ArrayList<VideoControllerListener>();
    }
    
    public void addVideoControllerListener(final VideoControllerListener videoControllerListener) {
        if (!this.mControllerListeners.contains(videoControllerListener)) {
            this.mControllerListeners.add(videoControllerListener);
        }
    }
    
    public abstract void hide();
    
    public abstract boolean isShowing();
    
    public void removeListeners() {
        this.mControllerListeners.clear();
    }
    
    public abstract void setSeekPosition(final long p0);
    
    public abstract void setVideoDuration(final long p0);
    
    public abstract void show();
    
    public abstract void showPauseButton();
    
    public abstract void showPlayButton();
    
    public void toggleShow() {
        if (this.isShowing()) {
            this.hide();
            return;
        }
        this.show();
    }
}
