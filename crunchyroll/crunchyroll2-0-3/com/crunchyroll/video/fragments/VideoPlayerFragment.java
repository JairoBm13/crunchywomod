// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.fragments;

import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import android.widget.LinearLayout;
import com.crunchyroll.android.api.models.Media;
import com.crunchyroll.android.api.ApiManager;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import com.crunchyroll.android.api.models.BIFFile;
import com.crunchyroll.android.api.tasks.BaseListener;
import com.crunchyroll.android.api.tasks.ProcessBifFileTask;
import android.content.BroadcastReceiver;
import com.crunchyroll.video.widget.CustomProgressDrawable;
import android.widget.CompoundButton;
import android.widget.CompoundButton$OnCheckedChangeListener;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.support.v7.app.MediaRouteDialogFactory;
import com.crunchyroll.video.util.CustomRouteDialogFactory;
import com.crunchyroll.cast.CastHandler;
import com.crunchyroll.crunchyroid.util.Functional;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.secondtv.android.ads.DeepLinker;
import com.secondtv.android.ads.AdTrackListener;
import android.app.Activity;
import com.crunchyroll.crunchyroid.app.AdsListener;
import com.swrve.sdk.SwrveSDKBase;
import java.util.concurrent.Executors;
import com.crunchyroll.crunchyroid.app.Extras;
import android.os.Bundle;
import android.media.MediaPlayer;
import com.crunchyroll.video.activities.VideoPlayerActivity;
import java.util.Iterator;
import java.util.List;
import android.support.v4.app.Fragment;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import java.util.concurrent.TimeUnit;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.android.api.models.EpisodeInfo;
import android.app.KeyguardManager;
import tv.ouya.console.api.OuyaFacade;
import tv.ouya.console.api.OuyaController;
import android.view.KeyEvent;
import android.view.View$OnKeyListener;
import com.crunchyroll.video.widget.VideoInfoView;
import com.crunchyroll.video.util.MediaManager;
import android.view.View$OnSystemUiVisibilityChangeListener;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import com.crunchyroll.android.util.LoggerFactory;
import com.crunchyroll.video.widget.CustomMediaController;
import com.crunchyroll.video.triggers.VideoViewTrigger;
import android.net.Uri;
import com.crunchyroll.android.api.models.StreamData;
import com.crunchyroll.video.triggers.SavePositionTrigger;
import android.support.v7.app.MediaRouteButton;
import com.crunchyroll.video.triggers.PingTrigger;
import android.os.Handler;
import java.util.concurrent.ScheduledExecutorService;
import com.crunchyroll.crunchyroid.tracking.ConvivaTracker;
import android.view.View;
import android.widget.Switch;
import com.secondtv.android.ads.AdTrigger;
import java.util.concurrent.ScheduledFuture;
import com.crunchyroll.android.api.models.Stream;
import com.crunchyroll.android.util.Logger;
import com.google.common.base.Optional;
import android.content.IntentFilter;
import com.secondtv.android.ads.AdTriggerProvider;
import com.crunchyroll.video.widget.AbstractVideoView;
import android.media.MediaPlayer$OnVideoSizeChangedListener;
import android.media.MediaPlayer$OnSeekCompleteListener;
import android.media.MediaPlayer$OnPreparedListener;
import android.media.MediaPlayer$OnErrorListener;
import android.media.MediaPlayer$OnCompletionListener;

public class VideoPlayerFragment extends AbstractVideoPlayerFragment implements MediaPlayer$OnCompletionListener, MediaPlayer$OnErrorListener, MediaPlayer$OnPreparedListener, MediaPlayer$OnSeekCompleteListener, MediaPlayer$OnVideoSizeChangedListener, OnProgressListener, OnSeekStartListener, AdTriggerProvider
{
    private static final int DELAYED_SHUTDOWN_POST_SEEK_DELAY_MSEC = 500;
    private static final int HIDE_UI_DELAY = 3000;
    public static final IntentFilter VIDEO_FILTER;
    private boolean didReceiveInitialSeekComplete;
    private boolean executeDelayedStop;
    private int lastKnownPlayhead;
    private Optional<Integer> lastSeekTime;
    private final Logger log;
    private Stream mActiveStream;
    private ScheduledFuture<?> mAdTask;
    private AdTrigger mAdTrigger;
    private Switch mAutoplayButton;
    private View mCastBar;
    private CastReceiver mCastReceiver;
    private ConvivaTracker mConvivaTracker;
    private ScheduledExecutorService mExecutor;
    private Handler mHandler;
    private boolean mIsHLS;
    private ScheduledFuture<?> mPingTask;
    private PingTrigger mPingTrigger;
    private MediaRouteButton mRouteButton;
    private SavePositionTrigger mSavePositionTrigger;
    private boolean mShowPreroll;
    private StreamData mStreamData;
    private UnlockReceiver mUnlockReceiver;
    private Uri mUri;
    private AbstractVideoView mVideoView;
    private ScheduledFuture<?> mVideoViewTask;
    private VideoViewTrigger mVideoViewTrigger;
    private View mView;
    private CustomMediaController mediaController;
    private ScheduledFuture<?> savePositionTask;
    
    static {
        (VIDEO_FILTER = new IntentFilter()).addAction("videoErrored");
        VideoPlayerFragment.VIDEO_FILTER.addAction("videoFinished");
        VideoPlayerFragment.VIDEO_FILTER.addAction("videoStarted");
    }
    
    public VideoPlayerFragment() {
        this.log = LoggerFactory.getLogger(this.getClass());
        this.lastSeekTime = Optional.absent();
        this.lastKnownPlayhead = 0;
        this.didReceiveInitialSeekComplete = false;
        this.executeDelayedStop = false;
    }
    
    private void cancelTimingTasks() {
        if (this.savePositionTask != null) {
            this.savePositionTask.cancel(true);
            this.savePositionTask = null;
        }
        if (this.mPingTask != null) {
            this.mPingTask.cancel(true);
            this.mPingTask = null;
        }
        if (this.mVideoViewTask != null) {
            this.mVideoViewTask.cancel(true);
            this.mVideoViewTask = null;
        }
        if (this.mAdTask != null) {
            this.mAdTask.cancel(true);
            this.mAdTask = null;
        }
    }
    
    private boolean deviceSupportsSystemUI() {
        return !this.isOuyaHardware();
    }
    
    private void finishPlayback() {
        this.lastKnownPlayhead = this.getPlayhead();
        LocalBroadcastManager.getInstance((Context)this.getActivity()).sendBroadcast(new Intent("videoFinished"));
    }
    
    private void forceShowMediaController(final View view) {
        view.setOnSystemUiVisibilityChangeListener((View$OnSystemUiVisibilityChangeListener)new View$OnSystemUiVisibilityChangeListener() {
            public void onSystemUiVisibilityChange(final int n) {
                VideoPlayerFragment.this.handleVisibilityChange(n);
            }
        });
    }
    
    private int getHiddenNavigationFlags() {
        return 2051;
    }
    
    private IntentFilter getUnlockIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        return intentFilter;
    }
    
    private void handleResumingVideo() {
        if (!this.mShowPreroll) {
            this.restorePlayhead();
        }
        if (!this.isScreenLocked() && this.mAutoPlayback) {
            this.mAutoPlayback = false;
            if (!this.mShowPreroll) {
                this.mVideoView.start();
            }
        }
        if (this.deviceSupportsSystemUI()) {
            this.scheduleHideNavigation();
        }
        this.mediaController.show();
    }
    
    private void handleVisibilityChange(final int n) {
        if (n == 2049) {
            this.mediaController.setOnShowHideListener(null);
            this.mediaController.show();
            this.showCastBar();
            this.setMediaListener();
        }
    }
    
    private void hideCastBar() {
        this.log.verbose("Hide cast button", new Object[0]);
        this.mCastBar.setVisibility(8);
    }
    
    private void hideNavigation() {
        final View view = this.getView();
        if (view != null) {
            view.setSystemUiVisibility(this.getHiddenNavigationFlags());
        }
    }
    
    private void hideProgressMessage() {
        this.log.debug("Hide progress message", new Object[0]);
        this.mediaController.hideProgress();
    }
    
    private void hideRouteButton() {
        this.mRouteButton.setVisibility(8);
    }
    
    private void initMediaController(final View view) {
        this.mediaController = (CustomMediaController)view.findViewById(2131624156);
        final int n = (int)this.getResources().getDimension(2131230730);
        this.mediaController.setPadding(n, 0, n, (int)this.getResources().getDimension(2131230731));
        this.setAutoplay(this.getApplicationState().getAutoplay());
        this.setMediaListener();
        if (!MediaManager.getInstance().hasMoreMedia()) {
            this.disableNext();
        }
    }
    
    private void initVideoView(final View view) {
        this.mVideoView.setKeepScreenOn(true);
        this.mVideoView.setExternalOnPreparedListener((MediaPlayer$OnPreparedListener)this);
        this.mVideoView.setOnVideoSizeChangedListener((MediaPlayer$OnVideoSizeChangedListener)this);
        this.mVideoView.setOnSeekCompleteListener((MediaPlayer$OnSeekCompleteListener)this);
        this.mVideoView.setOnErrorListener((MediaPlayer$OnErrorListener)this);
        this.mVideoView.setOnCompletionListener((MediaPlayer$OnCompletionListener)this);
        this.mVideoView.setProgressListener((AbstractVideoView.OnProgressListener)this);
        this.mVideoView.setSeekStartListener((AbstractVideoView.OnSeekStartListener)this);
        this.mVideoView.setOnKeyListener((View$OnKeyListener)new View$OnKeyListener() {
            final /* synthetic */ VideoInfoView val$infoView = (VideoInfoView)view.findViewById(2131624259);
            
            public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
                switch (keyEvent.getAction()) {
                    default: {
                        return true;
                    }
                    case 0: {
                        OuyaController.onKeyDown(n, keyEvent);
                        if (keyEvent.getAction() == 0 && VideoPlayerFragment.this.mediaController.onKeyDown(keyEvent.getKeyCode(), keyEvent)) {
                            return true;
                        }
                        switch (n) {
                            default: {
                                return false;
                            }
                            case 97: {
                                VideoPlayerFragment.this.cancelTimingTasks();
                                VideoPlayerFragment.this.lastKnownPlayhead = VideoPlayerFragment.this.mVideoView.getCurrentPosition();
                                if (VideoPlayerFragment.this.isOuyaHardware() && !VideoPlayerFragment.this.didReceiveInitialSeekComplete) {
                                    if (!VideoPlayerFragment.this.executeDelayedStop) {
                                        VideoPlayerFragment.this.executeDelayedStop = true;
                                        VideoPlayerFragment.this.showStopVideoMessage();
                                    }
                                }
                                else {
                                    VideoPlayerFragment.this.mVideoView.stopPlayback();
                                }
                                return true;
                            }
                            case 96: {
                                if (VideoPlayerFragment.this.mVideoView.isPlaying()) {
                                    VideoPlayerFragment.this.mVideoView.pause();
                                    this.val$infoView.showPause();
                                }
                                else {
                                    VideoPlayerFragment.this.mVideoView.start();
                                    this.val$infoView.showPlay();
                                }
                                return true;
                            }
                            case 21: {
                                VideoPlayerFragment.this.seekWithOffset(-10000);
                                return true;
                            }
                            case 102: {
                                VideoPlayerFragment.this.seekWithOffset(-20000);
                                return true;
                            }
                            case 104: {
                                VideoPlayerFragment.this.seekWithOffset(-30000);
                                return true;
                            }
                            case 22: {
                                VideoPlayerFragment.this.seekWithOffset(10000);
                                return true;
                            }
                            case 103: {
                                VideoPlayerFragment.this.seekWithOffset(20000);
                                return true;
                            }
                            case 105: {
                                VideoPlayerFragment.this.seekWithOffset(30000);
                                return true;
                            }
                        }
                        break;
                    }
                    case 1: {
                        return OuyaController.onKeyUp(n, keyEvent);
                    }
                }
            }
        });
        this.mVideoView.setOnPlayActionListener((AbstractVideoView.OnPlayActionListener)new OnPlayActionListener() {
            @Override
            public void onAutoplayChanged(final boolean autoplay) {
                VideoPlayerFragment.this.setAutoplay(autoplay);
            }
            
            @Override
            public void onNext() {
                VideoPlayerFragment.this.next();
            }
            
            @Override
            public void onPause(final int n) {
                VideoPlayerFragment.this.cancelTimingTasks();
                VideoPlayerFragment.this.lastKnownPlayhead = n;
            }
            
            @Override
            public void onPlay() {
                VideoPlayerFragment.this.scheduleTimingTasks();
            }
            
            @Override
            public void onStop() {
                VideoPlayerFragment.this.finishPlayback();
            }
        });
        this.mVideoView.setOnBufferingListener((AbstractVideoView.onBufferingListener)new onBufferingListener() {
            @Override
            public void onVideoStartBuff() {
                VideoPlayerFragment.this.showProgressMessage();
            }
            
            @Override
            public void onVideoStopBuff() {
                VideoPlayerFragment.this.hideProgressMessage();
            }
        });
    }
    
    private boolean isOuyaHardware() {
        return OuyaFacade.getInstance().isRunningOnOUYAHardware();
    }
    
    private boolean isScreenLocked() {
        return !this.isOuyaHardware() && ((KeyguardManager)this.getActivity().getSystemService("keyguard")).inKeyguardRestrictedInputMode();
    }
    
    public static VideoPlayerFragment newInstance(final EpisodeInfo episodeInfo, final boolean b, final boolean b2, final boolean b3) {
        final VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        videoPlayerFragment.initAndSetBundle(episodeInfo, b, b2, b3);
        return videoPlayerFragment;
    }
    
    private void restorePlayhead() {
        final Integer value = this.mSavePositionTrigger.getPosition();
        this.log.debug("Restore playhead to %s (%dmsec)", Util.stringFromDuration(value), value);
        this.mVideoView.seekTo(value);
        if (value == 0) {
            this.didReceiveInitialSeekComplete = true;
        }
    }
    
    private void scheduleHideNavigation() {
        this.mHandler.removeCallbacksAndMessages((Object)null);
        this.mHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                if (Thread.currentThread().isInterrupted()) {}
            }
        }, 3000L);
    }
    
    private void scheduleTimingTasks() {
        if (this.savePositionTask == null) {
            this.savePositionTask = this.mExecutor.scheduleAtFixedRate(this.mSavePositionTrigger, 0L, 1000L, TimeUnit.MILLISECONDS);
        }
        if (this.mPingTask == null) {
            this.mPingTask = this.mExecutor.scheduleAtFixedRate(this.mPingTrigger, 0L, 1000L, TimeUnit.MILLISECONDS);
        }
        if (this.mVideoViewTask == null) {
            this.mVideoViewTask = this.mExecutor.scheduleAtFixedRate(this.mVideoViewTrigger, 0L, 1000L, TimeUnit.MILLISECONDS);
        }
        if (this.mShowAds && this.mAdTask == null) {
            this.mAdTask = this.mExecutor.scheduleAtFixedRate(this.mAdTrigger, 0L, 1000L, TimeUnit.MILLISECONDS);
        }
    }
    
    private void seekWithOffset(final int n) {
        final VideoInfoView videoInfoView = (VideoInfoView)this.mView.findViewById(2131624259);
        final int duration = this.mVideoView.getDuration();
        if (duration == 0) {
            this.log.debug("Duration == 0, cancelling seek", new Object[0]);
            return;
        }
        int n2;
        if (this.lastSeekTime.isPresent()) {
            n2 = this.lastSeekTime.get();
        }
        else {
            n2 = this.mVideoView.getCurrentPosition();
        }
        final int max = Math.max(0, Math.min(duration, n2 + n));
        if (this.mVideoView.isPlaying()) {
            this.mAutoPlayback = true;
            this.mVideoView.pause();
        }
        this.seekTo(this.lastKnownPlayhead = max);
        if (n > 0) {
            videoInfoView.showForward(max);
            return;
        }
        videoInfoView.showReverse(max);
    }
    
    private void setMediaListener() {
        this.mediaController.setOnShowHideListener((CustomMediaController.OnShowHideListener)new CustomMediaController.OnShowHideListener() {
            @Override
            public void onBackPressed() {
                VideoPlayerFragment.this.getActivity().finish();
            }
            
            @Override
            public void onHide() {
                if (VideoPlayerFragment.this.deviceSupportsSystemUI()) {
                    VideoPlayerFragment.this.mHandler.removeCallbacksAndMessages((Object)null);
                    VideoPlayerFragment.this.hideNavigation();
                }
                VideoPlayerFragment.this.hideCastBar();
            }
            
            @Override
            public void onShow() {
                VideoPlayerFragment.this.showCastBar();
                if (VideoPlayerFragment.this.deviceSupportsSystemUI()) {
                    VideoPlayerFragment.this.showNavigation();
                }
            }
        });
    }
    
    private void showCastBar() {
        this.log.verbose("Show cast button", new Object[0]);
        this.mCastBar.setVisibility(0);
    }
    
    private void showNavigation() {
        this.mView.setSystemUiVisibility(0);
    }
    
    private void showProgressMessage() {
        this.mediaController.showProgress();
    }
    
    private void showRouteButton() {
        this.mRouteButton.setVisibility(0);
    }
    
    private void showStopVideoMessage() {
        this.log.debug("Show stop message", new Object[0]);
        this.showProgressMessage();
    }
    
    @Override
    public void disableNext() {
        if (this.mediaController != null) {
            this.mediaController.setNextEnabled(false);
        }
    }
    
    @Override
    public Stream getActiveStream() {
        return this.mActiveStream;
    }
    
    @Override
    public int getLastKnownPlayhead() {
        return this.lastKnownPlayhead;
    }
    
    @Override
    public int getPlayhead() {
        if (this.mVideoView == null) {
            return 0;
        }
        return this.mVideoView.getCurrentPosition();
    }
    
    @Override
    public Stream getPreferredStream() {
        final List<Stream> streams = this.mStreamData.getStreams();
        final ApplicationState value = ApplicationState.get((Context)this.getActivity());
        final String videoQualityPreference = value.getVideoQualityPreference(false);
        if (!CrunchyrollApplication.getApp(this).getApplicationState().getUseHls()) {
            return streams.get(0);
        }
        for (final Stream stream : streams) {
            if (videoQualityPreference.equals(stream.getQuality())) {
                return stream;
            }
        }
        if (value.isAnyPremium()) {
            return streams.get(0);
        }
        return streams.get(streams.size() - 1);
    }
    
    @Override
    public Uri getVideoViewUri() {
        if (this.mVideoView != null) {
            return this.mVideoView.getVideoUri();
        }
        return null;
    }
    
    @Override
    public boolean isPlaying() {
        return this.mVideoView != null && this.mVideoView.isPlaying();
    }
    
    public boolean isReady() {
        return this.isVisible();
    }
    
    @Override
    public void next() {
        super.next();
        this.mediaController.hide();
        ((VideoPlayerActivity)this.getActivity()).next(false);
    }
    
    @Override
    public void onAdFinish() {
        if (this.mShowPreroll) {
            this.mShowPreroll = false;
            if (this.mUri != null) {
                this.mVideoView.setVideoUri(this.mUri);
                this.showProgressMessage();
            }
        }
        if (!this.isPlaying()) {
            this.start();
        }
    }
    
    public void onAdStart() {
        if (this.isPlaying()) {
            this.pause();
        }
    }
    
    public void onCompletion(final MediaPlayer mediaPlayer) {
        this.cancelTimingTasks();
        this.finishPlayback();
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mShowPreroll = Extras.getBoolean(this.getArguments(), "preroll").or(Boolean.valueOf(false));
        this.mIsHLS = CrunchyrollApplication.getApp((Context)this.getActivity()).getApplicationState().getUseHls();
        this.mStreamData = this.mEpisodeInfo.getMedia().getStreamData().get();
        this.mActiveStream = this.getPreferredStream();
        this.mHandler = new Handler();
        this.mExecutor = Executors.newScheduledThreadPool(3);
        final Long mediaId = this.mEpisodeInfo.getMedia().getMediaId();
        this.mSavePositionTrigger = new SavePositionTrigger(this, bundle, Extras.getInt(this.getArguments(), "playhead"));
        this.mPingTrigger = new PingTrigger(this, bundle, mediaId, this.mEpisodeInfo.getMedia().getDuration());
        this.mVideoViewTrigger = new VideoViewTrigger(this, (Context)this.getActivity(), bundle, this.mEpisodeInfo.getMedia().getDuration().get());
        this.mConvivaTracker = new ConvivaTracker((Context)this.getActivity(), this.mEpisodeInfo.getMedia(), this.mEpisodeInfo.getSeries());
        if (!this.mShowAds) {
            return;
        }
        int attributeAsInt = 1;
        while (true) {
            try {
                attributeAsInt = SwrveSDKBase.getResourceManager().getAttributeAsInt("tremor", "implementation", 1);
                (this.mAdTrigger = new AdTrigger(this.getActivity(), this, bundle, this.mEpisodeInfo.getAdSlots(), new AdsListener(), this.mEpisodeInfo.getMaxAdStartSeconds(), this.mShowPreroll, CrunchyrollApplication.getApp((Context)this.getActivity()).getDeepLinker(), attributeAsInt)).setClosestAdRollToShown(this.mEpisodeInfo.getMedia().getPlayhead().or(Integer.valueOf(0)) * 1000);
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.mView = layoutInflater.inflate(2130903134, viewGroup, false);
        (this.mVideoView = (AbstractVideoView)this.mView.findViewById(2131624251)).init();
        final ImageView previewImg = (ImageView)this.mView.findViewById(2131624254);
        final TextView previewTime = (TextView)this.mView.findViewById(2131624255);
        this.mCastBar = this.mView.findViewById(2131624092);
        ((TextView)this.mView.findViewById(2131624256)).setText((CharSequence)Functional.Media.getShortEpisodeSubtitle(this.mEpisodeInfo.getMedia()));
        this.mRouteButton = (MediaRouteButton)this.mCastBar.findViewById(2131624257);
        if (CastHandler.isCastSupported((Context)this.getActivity())) {
            CastHandler.get().setRouteSelector(this.mRouteButton, new CustomRouteDialogFactory((Context)this.getActivity()));
        }
        (this.mAutoplayButton = (Switch)this.mView.findViewById(2131624258)).setText((CharSequence)LocalizedStrings.AUTOPLAY.get());
        this.mAutoplayButton.setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)new CompoundButton$OnCheckedChangeListener() {
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean autoplay) {
                final AbstractVideoView access$300 = VideoPlayerFragment.this.mVideoView;
                if (access$300 != null) {
                    VideoPlayerFragment.this.log.debug("Autoplay set to " + autoplay, new Object[0]);
                    access$300.setAutoplay(autoplay);
                    VideoPlayerFragment.this.mediaController.show();
                }
            }
        });
        if (CastHandler.isCastSupported((Context)this.getActivity())) {
            this.showRouteButton();
        }
        else {
            this.hideRouteButton();
        }
        this.initMediaController(this.mView);
        if (this.deviceSupportsSystemUI()) {
            this.forceShowMediaController(this.mView);
        }
        this.initVideoView(this.mView);
        this.mediaController.setVideoView(this.mVideoView);
        this.mediaController.setPreviewImg(previewImg);
        this.mediaController.setPreviewTime(previewTime);
        ((TextView)this.mView.findViewById(2131624265)).setText((CharSequence)LocalizedStrings.STOPPING_VIDEO.get());
        List<Double> adsPlayHeads = null;
        if (this.mShowAds) {
            adsPlayHeads = this.mEpisodeInfo.getAdsPlayHeads();
        }
        this.mediaController.setCustomSeekBarDrawable(CustomProgressDrawable.createDrawable((Context)this.getActivity(), adsPlayHeads, this.mEpisodeInfo.getMedia().getDuration().get()));
        return this.mView;
    }
    
    public void onDestroy() {
        super.onDestroy();
        this.mExecutor.shutdownNow();
        this.mExecutor = null;
        this.mHandler.removeCallbacksAndMessages((Object)null);
    }
    
    public void onDestroyView() {
        super.onDestroyView();
        if (this.mConvivaTracker != null) {
            this.mConvivaTracker.endSession();
        }
    }
    
    public boolean onError(final MediaPlayer mediaPlayer, final int n, final int n2) {
        if (this.mIsHLS) {
            this.mConvivaTracker.reportError(n);
            this.mConvivaTracker.endSession();
        }
        this.log.error("Video error: %s", Logger.errorStringFromIntegers(n, n2));
        final Intent intent = new Intent("videoErrored");
        Extras.putInt(intent, "what", Integer.valueOf(n));
        Extras.putInt(intent, "extra", Integer.valueOf(n2));
        LocalBroadcastManager.getInstance((Context)this.getActivity()).sendBroadcast(intent);
        return true;
    }
    
    public void onFirstProgressUpdate() {
        this.log.debug("VideoView/MediaPlayer onFirstProgressUpdate()", new Object[0]);
        this.hideProgressMessage();
    }
    
    public void onHiddenChanged(final boolean b) {
        super.onHiddenChanged(b);
    }
    
    public void onPause() {
        this.mVideoView.setKeepScreenOn(false);
        if (this.deviceSupportsSystemUI()) {
            this.showNavigation();
        }
        if (this.mVideoView.isPlaying()) {
            this.mVideoView.pause();
            this.mAutoPlayback = true;
        }
        super.onPause();
    }
    
    public void onPrepared(final MediaPlayer mediaPlayer) {
        this.handleResumingVideo();
        LocalBroadcastManager.getInstance((Context)this.getActivity()).sendBroadcast(new Intent("videoStarted"));
    }
    
    public void onResume() {
        super.onResume();
        this.mVideoView.setKeepScreenOn(true);
        this.handleResumingVideo();
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.mSavePositionTrigger.saveInstanceState(bundle);
        this.mPingTrigger.saveInstanceState(bundle);
        this.mVideoViewTrigger.saveInstanceState(bundle);
        if (this.mAdTrigger != null) {
            this.mAdTrigger.saveInstanceState(bundle);
        }
    }
    
    public void onSeekComplete(final MediaPlayer mediaPlayer) {
        this.didReceiveInitialSeekComplete = true;
        final int currentPosition = this.mVideoView.getCurrentPosition();
        this.log.debug(String.format("Seek complete, current position: %s (%d)", Util.stringFromDuration(currentPosition), currentPosition), new Object[0]);
        this.hideProgressMessage();
        if (this.mAutoPlayback) {
            this.mAutoPlayback = false;
            this.mVideoView.start();
        }
        if (this.executeDelayedStop) {
            this.mView.postDelayed((Runnable)new Runnable() {
                @Override
                public void run() {
                    VideoPlayerFragment.this.mVideoView.stopPlayback();
                    VideoPlayerFragment.this.finishPlayback();
                }
            }, 500L);
        }
        this.mediaController.hidePreview();
    }
    
    public void onSeekStart() {
        this.showProgressMessage();
    }
    
    public void onStart() {
        super.onStart();
        this.mUnlockReceiver = new UnlockReceiver();
        this.getActivity().registerReceiver((BroadcastReceiver)this.mUnlockReceiver, this.getUnlockIntentFilter());
        this.mCastReceiver = new CastReceiver();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("CAST_NO_ROUTES_AVAILABLE_EVENT");
        intentFilter.addAction("CAST_ROUTES_AVAILABLE_EVENT");
        LocalBroadcastManager.getInstance((Context)this.getActivity()).registerReceiver(this.mCastReceiver, intentFilter);
        final ProcessBifFileTask processBifFileTask = new ProcessBifFileTask((Context)this.getActivity(), this.mEpisodeInfo.getMedia().getBIFUrl());
        processBifFileTask.setTaskListener(new BaseListener<BIFFile>() {
            @Override
            public void onPreExecute() {
                VideoPlayerFragment.this.mediaController.setBIFFile(null);
            }
            
            @Override
            public void onSuccess(final BIFFile bifFile) {
                VideoPlayerFragment.this.mediaController.setBIFFile(bifFile);
            }
        });
        processBifFileTask.execute();
    }
    
    public void onStop() {
        super.onStop();
        this.getActivity().unregisterReceiver((BroadcastReceiver)this.mUnlockReceiver);
        LocalBroadcastManager.getInstance((Context)this.getActivity()).unregisterReceiver(this.mCastReceiver);
        if (!this.getActivity().isFinishing() || this.lastKnownPlayhead <= 0) {
            return;
        }
        try {
            if (this.mPingTrigger.getNumberOfPings() > 0L) {
                this.mPingTrigger.runTrigger(this, this.lastKnownPlayhead, true);
                final Intent intent = new Intent("VIDEO_PROGRESS_UPDATED");
                Extras.putLong(intent, "seriesId", Long.valueOf(this.mEpisodeInfo.getMedia().getSeriesId()));
                Extras.putLong(intent, "mediaId", this.mEpisodeInfo.getMedia().getMediaId());
                Extras.putInt(intent, "playhead", Integer.valueOf(this.lastKnownPlayhead / 1000));
                LocalBroadcastManager.getInstance((Context)this.getActivity()).sendBroadcast(intent);
                ApiManager.getInstance(this.getActivity()).invalidateCache(Media.class, Optional.of(this.mEpisodeInfo.getMedia().getSeriesId()));
                ApiManager.getInstance(this.getActivity()).invalidateCache(Media.class, Optional.of(this.mEpisodeInfo.getMedia().getCollectionId()));
            }
        }
        catch (Exception ex) {
            this.log.error("Error running trigger!", ex);
        }
    }
    
    public void onVideoSizeChanged(final MediaPlayer mediaPlayer, final int n, final int n2) {
        if (this.lastSeekTime.isPresent()) {
            this.lastSeekTime = Optional.absent();
            this.log.debug("Video size change reported, removing seek time", new Object[0]);
        }
    }
    
    public void onViewCreated(final View view, final Bundle bundle) {
        final LinearLayout linearLayout = (LinearLayout)view.findViewById(2131624263);
        linearLayout.setVisibility(4);
        linearLayout.requestLayout();
        linearLayout.invalidate();
        this.mConvivaTracker.startSession(this.mVideoView);
        if (this.mActiveStream.getUrl() != null) {
            this.mUri = Uri.parse(this.mActiveStream.getUrl());
            if (this.mUri == null) {
                this.mConvivaTracker.reportError(1);
                final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.getActivity());
                alertDialog$Builder.setMessage((CharSequence)LocalizedStrings.ERROR_LOADING_MEDIA.get());
                alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.OK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        VideoPlayerFragment.this.getActivity().finish();
                    }
                });
                alertDialog$Builder.show();
            }
            else {
                if (this.mShowPreroll) {
                    this.mAdTrigger.playPreRoll();
                    return;
                }
                this.mVideoView.setVideoUri(this.mUri);
                this.showProgressMessage();
            }
        }
    }
    
    @Override
    public void pause() {
        if (this.mVideoView != null) {
            this.mVideoView.pause();
            this.mHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    VideoPlayerFragment.this.lastKnownPlayhead = VideoPlayerFragment.this.mVideoView.getCurrentPosition();
                    VideoPlayerFragment.this.mVideoView.setVisibility(4);
                    if (VideoPlayerFragment.this.mediaController.getVisibility() == 0) {
                        VideoPlayerFragment.this.mediaController.setVisibility(8);
                    }
                }
            });
        }
    }
    
    public void seekTo(final int n) {
        if (this.mVideoView != null) {
            this.lastSeekTime = Optional.of(n);
            this.mVideoView.seekTo(n);
        }
    }
    
    @Override
    public void setAutoplay(final boolean b) {
        super.setAutoplay(b);
        this.mAutoplayButton.setChecked(b);
    }
    
    @Override
    public void start() {
        if (this.mVideoView != null) {
            if (this.mediaController.getVisibility() == 8) {
                this.mediaController.setVisibility(0);
            }
            this.mVideoView.setVisibility(0);
            if (this.lastSeekTime.isPresent()) {
                this.mVideoView.seekTo(this.lastSeekTime.get());
            }
            else {
                this.mVideoView.seekTo(this.lastKnownPlayhead);
            }
            this.mVideoView.start();
        }
    }
    
    @Override
    public void startFrom(final int n) {
        this.lastKnownPlayhead = n;
        this.mSavePositionTrigger.setPosition(n);
        this.start();
    }
    
    private class CastReceiver extends BroadcastReceiver
    {
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction().equals("CAST_ROUTES_AVAILABLE_EVENT")) {
                VideoPlayerFragment.this.showRouteButton();
            }
            else if (intent.getAction().equals("CAST_NO_ROUTES_AVAILABLE_EVENT")) {
                VideoPlayerFragment.this.hideRouteButton();
            }
        }
    }
    
    private class UnlockReceiver extends BroadcastReceiver
    {
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction().equals("android.intent.action.USER_PRESENT") && VideoPlayerFragment.this.mAutoPlayback) {
                VideoPlayerFragment.this.mAutoPlayback = false;
                VideoPlayerFragment.this.mVideoView.start();
            }
        }
    }
}
