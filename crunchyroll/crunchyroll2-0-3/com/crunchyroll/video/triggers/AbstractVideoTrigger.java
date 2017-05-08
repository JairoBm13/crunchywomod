// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.triggers;

import android.os.Bundle;
import com.crunchyroll.android.util.LoggerFactory;
import com.crunchyroll.video.fragments.AbstractVideoPlayerFragment;
import com.crunchyroll.android.util.Logger;

public abstract class AbstractVideoTrigger implements Runnable
{
    private static final Logger log;
    private final AbstractVideoPlayerFragment videoPlayerFragment;
    
    static {
        log = LoggerFactory.getLogger(AbstractVideoTrigger.class);
    }
    
    public AbstractVideoTrigger(final AbstractVideoPlayerFragment videoPlayerFragment) {
        this.videoPlayerFragment = videoPlayerFragment;
    }
    
    @Override
    public final void run() {
        try {
            if (this.videoPlayerFragment == null) {
                return;
            }
            if (this.videoPlayerFragment.isVisible() && this.videoPlayerFragment.isPlaying()) {
                this.runTrigger(this.videoPlayerFragment, this.videoPlayerFragment.getPlayhead());
            }
        }
        catch (Exception ex) {
            AbstractVideoTrigger.log.error("Error running trigger!", ex);
        }
    }
    
    protected abstract void runTrigger(final AbstractVideoPlayerFragment p0, final int p1) throws Exception;
    
    public void saveInstanceState(final Bundle bundle) {
    }
}
