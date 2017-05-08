// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.triggers;

import com.crunchyroll.crunchyroid.app.Extras;
import com.crunchyroll.crunchyroid.tracking.FacebookTracker;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import android.os.Bundle;
import com.crunchyroll.video.fragments.AbstractVideoPlayerFragment;
import android.content.Context;

public class VideoViewTrigger extends AbstractVideoTrigger
{
    public static final String BUNDLE_NAME = "VideoViewTrigger";
    public static final String EXTRA_CURRENT_SECONDS = "currentSeconds";
    Context mContext;
    int mCurrentSecondsViewed;
    int mTotalSeconds;
    
    public VideoViewTrigger(final AbstractVideoPlayerFragment abstractVideoPlayerFragment, final Context mContext, final Bundle bundle, final int mTotalSeconds) {
        super(abstractVideoPlayerFragment);
        this.mContext = mContext;
        this.mCurrentSecondsViewed = 0;
        this.mTotalSeconds = mTotalSeconds;
        if (bundle != null) {
            this.mCurrentSecondsViewed = bundle.getBundle("VideoViewTrigger").getInt("currentSeconds");
        }
    }
    
    @Override
    protected void runTrigger(final AbstractVideoPlayerFragment abstractVideoPlayerFragment, final int n) throws Exception {
        ++this.mCurrentSecondsViewed;
        if (this.mCurrentSecondsViewed == this.mTotalSeconds * 9 / 10) {
            ApplicationState.get(this.mContext);
            FacebookTracker.videoView(this.mContext);
        }
    }
    
    @Override
    public void saveInstanceState(final Bundle bundle) {
        if (bundle == null) {
            return;
        }
        final Bundle bundle2 = new Bundle();
        Extras.putInt(bundle2, "currentSeconds", Integer.valueOf(this.mCurrentSecondsViewed));
        bundle.putBundle("VideoViewTrigger", bundle2);
    }
}
