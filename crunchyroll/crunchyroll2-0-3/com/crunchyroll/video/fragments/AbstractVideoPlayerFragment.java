// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.fragments;

import android.support.v4.app.FragmentActivity;
import android.content.Context;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.google.common.base.Optional;
import com.crunchyroll.crunchyroid.app.Extras;
import android.os.Bundle;
import android.net.Uri;
import com.crunchyroll.android.api.models.Stream;
import com.crunchyroll.android.api.models.EpisodeInfo;
import com.crunchyroll.crunchyroid.fragments.BaseFragment;

public abstract class AbstractVideoPlayerFragment extends BaseFragment
{
    protected boolean mAutoPlayback;
    protected OnClickthroughListener mClickthroughListener;
    protected EpisodeInfo mEpisodeInfo;
    protected boolean mShowAds;
    
    public abstract void disableNext();
    
    public abstract Stream getActiveStream();
    
    public abstract int getLastKnownPlayhead();
    
    public abstract int getPlayhead();
    
    public abstract Stream getPreferredStream();
    
    public abstract Uri getVideoViewUri();
    
    public void initAndSetBundle(final EpisodeInfo episodeInfo, final boolean b, final boolean b2, final boolean b3) {
        final Bundle arguments = new Bundle();
        Extras.putSerializable(arguments, "episodeInfo", episodeInfo);
        Extras.putBoolean(arguments, "autoPlayback", b);
        Extras.putBoolean(arguments, "shouldShowAds", b2);
        Extras.putBoolean(arguments, "preroll", b3);
        final Optional<Integer> playhead = episodeInfo.getMedia().getPlayhead();
        if (playhead.isPresent()) {
            Extras.putInt(arguments, "playhead", playhead.get());
        }
        this.setArguments(arguments);
    }
    
    public abstract boolean isPlaying();
    
    public void next() {
        Tracker.playNextVideo((Context)this.getActivity(), this.mEpisodeInfo.getMedia());
        Tracker.videoPlay("play-next", this.mEpisodeInfo.getMedia().getSeriesName().or(""), "episode-" + this.mEpisodeInfo.getMedia().getEpisodeNumber());
    }
    
    public abstract void onAdFinish();
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mAutoPlayback = Extras.getBoolean(this.getArguments(), "autoPlayback").get();
        this.mShowAds = Extras.getBoolean(this.getArguments(), "shouldShowAds").get();
        this.mEpisodeInfo = Extras.getSerializable(this.getArguments(), "episodeInfo", EpisodeInfo.class).get();
    }
    
    public abstract void pause();
    
    public void setAutoplay(final boolean autoplay) {
        this.getApplicationState().setAutoplay(autoplay);
        final FragmentActivity activity = this.getActivity();
        String s;
        if (autoplay) {
            s = "click-on";
        }
        else {
            s = "click-off";
        }
        Tracker.autoNextToggle((Context)activity, s);
    }
    
    public void setOnClickthroughListener(final OnClickthroughListener mClickthroughListener) {
        this.mClickthroughListener = mClickthroughListener;
    }
    
    public abstract void start();
    
    public abstract void startFrom(final int p0);
    
    public interface OnClickthroughListener
    {
        void onClickthrough(final String p0);
    }
}
