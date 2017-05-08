// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast;

import android.media.MediaPlayer;
import java.io.Serializable;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout;
import android.text.TextUtils;
import android.view.View$OnClickListener;
import com.secondtv.android.ads.R;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import java.util.concurrent.Executors;
import android.content.Context;
import java.util.concurrent.TimeUnit;
import android.os.Bundle;
import android.net.Uri;
import android.media.MediaPlayer$OnErrorListener;
import android.media.MediaPlayer$OnCompletionListener;
import android.media.MediaPlayer$OnPreparedListener;
import java.util.Iterator;
import com.secondtv.android.ads.vast.types.MediaFile;
import java.util.Collection;
import com.secondtv.android.ads.vast.types.Playlist;
import com.secondtv.android.ads.vast.widget.VastVideoView;
import com.secondtv.android.ads.AdTrackListener;
import android.widget.Button;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import android.support.v4.app.Fragment;

public class VastFragment extends Fragment implements OnLoadVastListener
{
    private static final String TAG = "VastFragment";
    private ScheduledFuture<?> adPingTask;
    private AdPingTrigger adPingTrigger;
    private int currentLinearAdIndex;
    private ScheduledExecutorService executor;
    private LinearAd linearAd;
    private Button mClickthroughButton;
    private boolean mDidDispatchStartPing;
    private String mInfoString;
    private OnStatusUpdateListener mListener;
    private MediaPlayerListener mMediaPlayerListener;
    private int mResumePlayhead;
    private AdTrackListener mTrackListener;
    private VastVideoView mVideoView;
    private Playlist playlist;
    
    public VastFragment() {
        this.mDidDispatchStartPing = false;
        this.mMediaPlayerListener = new MediaPlayerListener();
        this.mResumePlayhead = -1;
    }
    
    private void dispatchErrorPing() {
        if (this.linearAd != null) {
            new Pinger().ping(this.linearAd.getErrorUrls());
        }
    }
    
    private LinearAd getCurrentAd() {
        try {
            return this.playlist.getLinearAd(this.currentLinearAdIndex);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    private MediaFile getMostSuitableMediaFile(final Collection<MediaFile> collection) {
        if (collection == null) {
            return null;
        }
        for (final MediaFile mediaFile : collection) {
            if ("video/mp4".equals(mediaFile.getMimeType())) {
                return mediaFile;
            }
        }
        return null;
    }
    
    private void initVideoView() {
        this.mVideoView.setKeepScreenOn(true);
        this.mVideoView.setOnPreparedListener((MediaPlayer$OnPreparedListener)this.mMediaPlayerListener);
        this.mVideoView.setOnCompletionListener((MediaPlayer$OnCompletionListener)this.mMediaPlayerListener);
        this.mVideoView.setOnErrorListener((MediaPlayer$OnErrorListener)this.mMediaPlayerListener);
        this.mVideoView.setZOrderMediaOverlay(true);
    }
    
    public static VastFragment newInstance(final OnStatusUpdateListener onStatusUpdateListener, final String infoString, final AdTrackListener trackListener) {
        final VastFragment vastFragment = new VastFragment();
        vastFragment.setOnStatusUpdateListener(onStatusUpdateListener);
        vastFragment.setInfoString(infoString);
        vastFragment.setTrackListener(trackListener);
        return vastFragment;
    }
    
    private void pauseTriggers() {
        if (this.adPingTask != null) {
            this.adPingTask.cancel(true);
            this.adPingTask = null;
        }
    }
    
    private void playNextLinearAd() {
        ++this.currentLinearAdIndex;
        if (this.currentLinearAdIndex < this.playlist.size()) {
            this.linearAd = this.getCurrentAd();
            final Collection<MediaFile> mediaFiles = this.getCurrentAd().getMediaFiles();
            if (mediaFiles != null && !mediaFiles.isEmpty()) {
                this.mClickthroughButton.setVisibility(4);
                final MediaFile mostSuitableMediaFile = this.getMostSuitableMediaFile(mediaFiles);
                this.mVideoView.setVisibility(0);
                this.mVideoView.setVideoURI(Uri.parse(mostSuitableMediaFile.getUrl()));
                this.mListener.onPrepared();
                return;
            }
        }
        this.mListener.onError(null);
    }
    
    private void resetTriggers(final Bundle bundle) {
        this.pauseTriggers();
        this.adPingTrigger = new AdPingTrigger(this, bundle);
    }
    
    private void scheduleTriggers() {
        this.adPingTask = this.executor.scheduleAtFixedRate(this.adPingTrigger, 0L, 1000L, TimeUnit.MILLISECONDS);
    }
    
    private void setTrackListener(final AdTrackListener mTrackListener) {
        this.mTrackListener = mTrackListener;
    }
    
    public void clear() {
        if (this.mVideoView != null) {
            this.mVideoView.stopPlayback();
            this.mVideoView.setVisibility(4);
            this.mVideoView.setVideoURI(Uri.EMPTY);
        }
    }
    
    public void dispatchImpressionPing() {
        new Pinger().ping(this.linearAd.getImpressionUrls());
    }
    
    public void dispatchTrackingPing(final TrackingType trackingType) {
        new Pinger().ping(this.linearAd.getTrackingUrls(trackingType));
    }
    
    public int getDuration() {
        return this.mVideoView.getDuration();
    }
    
    public int getPlayhead() {
        return this.mVideoView.getCurrentPosition();
    }
    
    public boolean isPlaying() {
        return this.mVideoView.isPlaying();
    }
    
    public void load(final Context context, final String s) {
        this.resetTriggers(null);
        this.currentLinearAdIndex = -1;
        final LoadVastResourceAsyncTask loadVastResourceAsyncTask = new LoadVastResourceAsyncTask(context, s, this.mTrackListener);
        loadVastResourceAsyncTask.setOnVastLoaderListener((LoadVastResourceAsyncTask.OnLoadVastListener)this);
        loadVastResourceAsyncTask.execute();
    }
    
    @Override
    public void onActivityCreated(final Bundle bundle) {
        super.onActivityCreated(bundle);
        this.resetTriggers(bundle);
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.executor = Executors.newScheduledThreadPool(2);
        this.currentLinearAdIndex = -1;
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(R.layout.fragment_vast, viewGroup, false);
        (this.mVideoView = (VastVideoView)inflate.findViewById(R.id.video_view)).setZOrderMediaOverlay(true);
        this.initVideoView();
        (this.mClickthroughButton = (Button)inflate.findViewById(R.id.clickthrough_button)).setText((CharSequence)this.mInfoString);
        this.mClickthroughButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final LinearAd access$100 = VastFragment.this.getCurrentAd();
                if (access$100 != null) {
                    final String clickThroughUrl = access$100.getClickThroughUrl();
                    VastFragment.this.mClickthroughButton.setVisibility(8);
                    if (!TextUtils.isEmpty((CharSequence)clickThroughUrl)) {
                        new Pinger().ping(access$100.getClickTrackingUrls());
                        VastFragment.this.mListener.onClickthrough(clickThroughUrl);
                    }
                }
            }
        });
        return inflate;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.executor.shutdownNow();
        this.executor = null;
    }
    
    @Override
    public void onPause() {
        super.onPause();
        this.pauseTriggers();
        this.pause();
    }
    
    @Override
    public void onPlaylistLoadException(final Exception ex) {
        this.mListener.onError(ex);
    }
    
    @Override
    public void onPlaylistLoadSuccess(final Playlist playlist) {
        this.playlist = playlist;
        final ViewGroup$LayoutParams layoutParams = this.mVideoView.getLayoutParams();
        final RelativeLayout relativeLayout = (RelativeLayout)this.mVideoView.getParent();
        relativeLayout.removeView((View)this.mVideoView);
        this.mVideoView = new VastVideoView((Context)this.getActivity());
        this.initVideoView();
        relativeLayout.addView((View)this.mVideoView, layoutParams);
        this.mClickthroughButton.bringToFront();
        this.scheduleTriggers();
        this.playNextLinearAd();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        this.scheduleTriggers();
        this.resume();
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.adPingTrigger.saveInstanceState(bundle);
        if (this.playlist != null) {
            bundle.putSerializable("playlist", (Serializable)this.playlist);
        }
    }
    
    public void pause() {
        if (this.mVideoView != null) {
            this.mResumePlayhead = Math.max(this.mResumePlayhead, this.mVideoView.getCurrentPosition());
            this.mVideoView.pause();
        }
    }
    
    public void play() {
        this.mVideoView.start();
        if (!this.mDidDispatchStartPing) {
            this.mDidDispatchStartPing = true;
            this.dispatchTrackingPing(TrackingType.START);
        }
    }
    
    public void resume() {
        if (this.mResumePlayhead > 0) {
            this.mVideoView.seekTo(this.mResumePlayhead);
            this.mVideoView.resume();
            return;
        }
        this.mVideoView.resume();
    }
    
    void setInfoString(final String mInfoString) {
        this.mInfoString = mInfoString;
    }
    
    public void setOnStatusUpdateListener(final OnStatusUpdateListener mListener) {
        this.mListener = mListener;
    }
    
    public void start() {
        if (this.mVideoView != null) {
            this.mVideoView.start();
        }
    }
    
    public void startFrom(final int n) {
        if (n == 0) {
            throw new IllegalArgumentException("Can't resume VAST video from position > 0.");
        }
        this.mVideoView.start();
    }
    
    private class MediaPlayerListener implements MediaPlayer$OnCompletionListener, MediaPlayer$OnErrorListener, MediaPlayer$OnPreparedListener
    {
        public void onCompletion(final MediaPlayer mediaPlayer) {
            VastFragment.this.dispatchTrackingPing(TrackingType.COMPLETE);
            VastFragment.this.mListener.onComplete(45242);
        }
        
        public boolean onError(final MediaPlayer mediaPlayer, final int n, final int n2) {
            VastFragment.this.dispatchErrorPing();
            VastFragment.this.playNextLinearAd();
            return true;
        }
        
        public void onPrepared(final MediaPlayer mediaPlayer) {
            if (TextUtils.isEmpty((CharSequence)VastFragment.this.getCurrentAd().getClickThroughUrl())) {
                VastFragment.this.mClickthroughButton.setVisibility(4);
            }
            else {
                VastFragment.this.mClickthroughButton.setVisibility(0);
            }
            VastFragment.this.mVideoView.start();
        }
    }
    
    public interface OnStatusUpdateListener
    {
        void onClickthrough(final String p0);
        
        void onComplete(final int p0);
        
        void onError(final Exception p0);
        
        void onPrepared();
    }
}
