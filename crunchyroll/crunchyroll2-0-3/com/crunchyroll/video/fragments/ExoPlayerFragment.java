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
import com.crunchyroll.exoplayer.VideoControllerListener;
import com.google.android.exoplayer.upstream.HttpDataSource;
import com.google.android.exoplayer.ExoPlaybackException;
import java.util.List;
import com.crunchyroll.video.widget.CustomProgressDrawable;
import android.support.v7.app.MediaRouteDialogFactory;
import com.crunchyroll.video.util.CustomRouteDialogFactory;
import com.crunchyroll.cast.CastHandler;
import com.crunchyroll.crunchyroid.util.Functional;
import android.widget.TextView;
import android.widget.ImageView;
import com.crunchyroll.exoplayer.VideoController;
import android.widget.CompoundButton;
import android.widget.CompoundButton$OnCheckedChangeListener;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.view.LayoutInflater;
import com.secondtv.android.ads.DeepLinker;
import com.secondtv.android.ads.AdTrackListener;
import android.app.Activity;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.crunchyroid.app.AdsListener;
import com.swrve.sdk.SwrveSDKBase;
import java.util.concurrent.Executors;
import android.os.Bundle;
import android.media.MediaPlayer;
import com.crunchyroll.video.activities.VideoPlayerActivity;
import com.crunchyroll.crunchyroid.app.Extras;
import com.crunchyroll.video.widget.VideoInfoView;
import java.util.concurrent.TimeUnit;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.android.api.models.EpisodeInfo;
import android.app.KeyguardManager;
import tv.ouya.console.api.OuyaFacade;
import tv.ouya.console.api.OuyaController;
import android.view.KeyEvent;
import android.view.View$OnKeyListener;
import com.crunchyroll.video.util.MediaManager;
import android.view.View$OnSystemUiVisibilityChangeListener;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import com.crunchyroll.android.util.LoggerFactory;
import com.crunchyroll.video.triggers.VideoViewTrigger;
import com.crunchyroll.android.api.models.StreamData;
import com.crunchyroll.video.triggers.SavePositionTrigger;
import com.crunchyroll.video.triggers.PingTrigger;
import com.crunchyroll.exoplayer.ExoPlayerView;
import android.net.Uri;
import android.support.v7.app.MediaRouteButton;
import com.crunchyroll.exoplayer.AbstractPlayerEventListener;
import android.view.ViewGroup;
import android.os.Handler;
import com.crunchyroll.exoplayer.AbstractVideoControllerListener;
import android.view.View;
import android.widget.Switch;
import com.crunchyroll.android.util.Logger;
import com.google.common.base.Optional;
import java.util.concurrent.ScheduledExecutorService;
import com.secondtv.android.ads.AdTrigger;
import java.util.concurrent.ScheduledFuture;
import com.crunchyroll.android.api.models.Stream;
import android.content.IntentFilter;
import com.secondtv.android.ads.AdTriggerProvider;
import com.crunchyroll.video.widget.AbstractVideoView;
import com.crunchyroll.exoplayer.VideoPlayer;
import android.media.MediaPlayer$OnVideoSizeChangedListener;
import android.media.MediaPlayer$OnSeekCompleteListener;
import android.media.MediaPlayer$OnPreparedListener;
import android.media.MediaPlayer$OnErrorListener;
import android.media.MediaPlayer$OnCompletionListener;

public class ExoPlayerFragment extends AbstractVideoPlayerFragment implements MediaPlayer$OnCompletionListener, MediaPlayer$OnErrorListener, MediaPlayer$OnPreparedListener, MediaPlayer$OnSeekCompleteListener, MediaPlayer$OnVideoSizeChangedListener, ErrorListener, StateChangeListener, OnProgressListener, OnSeekStartListener, AdTriggerProvider
{
    private static final int DELAYED_SHUTDOWN_POST_SEEK_DELAY_MSEC = 500;
    private static final int HIDE_UI_DELAY = 3000;
    public static final IntentFilter VIDEO_FILTER;
    private Stream activeStream;
    private ScheduledFuture<?> adTask;
    private AdTrigger adTrigger;
    private ScheduledExecutorService executor;
    private Optional<Boolean> isPlaying;
    private int lastKnownPlayhead;
    private Optional<Integer> lastSeekTime;
    private final Logger log;
    private Switch mAutoplayButton;
    private View mCastBar;
    private CastReceiver mCastReceiver;
    private AbstractVideoControllerListener mControllerListener;
    private Handler mHandler;
    private ViewGroup mMessageView;
    private AbstractPlayerEventListener mPlayerListener;
    private MediaRouteButton mRouteButton;
    private boolean mShowPreroll;
    private Uri mUri;
    private ExoPlayerView mVideoView;
    private ExoPlayerMediaController mediaController;
    private ScheduledFuture<?> pingTask;
    private PingTrigger pingTrigger;
    private ScheduledFuture<?> savePositionTask;
    private SavePositionTrigger savePositionTrigger;
    private StreamData streamData;
    private UnlockReceiver unlockReceiver;
    private ScheduledFuture<?> videoViewTask;
    private VideoViewTrigger videoViewTrigger;
    
    static {
        (VIDEO_FILTER = new IntentFilter()).addAction("videoErrored");
        ExoPlayerFragment.VIDEO_FILTER.addAction("videoFinished");
        ExoPlayerFragment.VIDEO_FILTER.addAction("videoStarted");
    }
    
    public ExoPlayerFragment() {
        this.log = LoggerFactory.getLogger(this.getClass());
        this.lastSeekTime = Optional.absent();
        this.lastKnownPlayhead = 0;
        this.mControllerListener = new MediaControllerListener();
        this.mPlayerListener = new PlayerEventListener();
    }
    
    private void cancelTimingTasks() {
        if (this.savePositionTask != null) {
            this.savePositionTask.cancel(true);
            this.savePositionTask = null;
        }
        if (this.pingTask != null) {
            this.pingTask.cancel(true);
            this.pingTask = null;
        }
        if (this.videoViewTask != null) {
            this.videoViewTask.cancel(true);
            this.videoViewTask = null;
        }
        if (this.adTask != null) {
            this.adTask.cancel(true);
            this.adTask = null;
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
                ExoPlayerFragment.this.handleVisibilityChange(n);
            }
        });
    }
    
    private int getHiddenNavigationFlags() {
        return 2051;
    }
    
    private final String getNameForSystemUIVisiblity(final int n) {
        String string = "";
        if (n == 0) {
            string = "" + "VISIBLE";
        }
        String string2 = string;
        if ((n & 0x4) > 0) {
            string2 = string + " FULLSCREEN";
        }
        String string3 = string2;
        if ((n & 0x2) > 0) {
            string3 = string2 + " HIDE_NAVIGATION";
        }
        String string4 = string3;
        if ((n & 0x400) > 0) {
            string4 = string3 + " LAYOUT_FULLSCREEN";
        }
        String s = string4;
        if ((n & 0x200) > 0) {
            s = " LAYOUT_HIDE_NAVIGATION";
        }
        String string5 = s;
        if ((n & 0x100) > 0) {
            string5 = s + " LAYOUT_STABLE";
        }
        String string6 = string5;
        if ((n & 0x1) > 0) {
            string6 = string5 + " LOW_PROFILE";
        }
        String string7 = string6;
        if ((n & 0x800) > 0) {
            string7 = string6 + " IMMERSIVE";
        }
        return string7;
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
        if (this.mAutoPlayback) {
            this.mAutoPlayback = false;
            if (!this.mShowPreroll) {
                this.mVideoView.init();
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
        this.log.verbose("Hide progress message", new Object[0]);
        this.mediaController.hideProgress();
    }
    
    private void hideRouteButton() {
        this.mRouteButton.setVisibility(8);
    }
    
    private void initMediaController(final View view) {
        this.mediaController = (ExoPlayerMediaController)view.findViewById(2131624156);
        final int n = (int)this.getResources().getDimension(2131230730);
        this.mediaController.setPadding(n, 0, n, (int)this.getResources().getDimension(2131230731));
        this.setAutoplay(this.getApplicationState().getAutoplay());
        this.setMediaListener();
        if (!MediaManager.getInstance().hasMoreMedia()) {
            this.disableNext();
        }
    }
    
    private void initVideoView(final View view) {
        int n = 0;
        final ApplicationState applicationState = this.getApplicationState();
        this.mVideoView.setBufferLengths(10000, 10000, 500, 500);
        this.mVideoView.setErrorListener(this);
        if (applicationState.isAnyPremium()) {
            final String videoQualityPreference = applicationState.getVideoQualityPreference(false);
            Label_0078: {
                switch (videoQualityPreference.hashCode()) {
                    case -1306012042: {
                        if (videoQualityPreference.equals("adaptive")) {
                            break Label_0078;
                        }
                        break;
                    }
                    case 111384492: {
                        if (videoQualityPreference.equals("ultra")) {
                            n = 1;
                            break Label_0078;
                        }
                        break;
                    }
                }
                n = -1;
            }
            switch (n) {
                default: {
                    this.mVideoView.setVideoQuality(VideoQuality.QUALITY_LOW);
                    break;
                }
                case 0: {
                    this.mVideoView.setVideoQuality(VideoQuality.QUALITY_ADAPTIVE);
                    break;
                }
                case 1: {
                    this.mVideoView.setVideoQuality(VideoQuality.QUALITY_HIGH);
                    break;
                }
            }
        }
        else {
            this.mVideoView.setVideoQuality(VideoQuality.QUALITY_ADAPTIVE);
        }
        this.mVideoView.setOnKeyListener((View$OnKeyListener)new View$OnKeyListener() {
            public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
                switch (keyEvent.getAction()) {
                    default: {
                        return true;
                    }
                    case 0: {
                        OuyaController.onKeyDown(n, keyEvent);
                        if (keyEvent.getAction() == 0 && ExoPlayerFragment.this.mediaController.onKeyDown(keyEvent.getKeyCode(), keyEvent)) {
                            return true;
                        }
                        switch (n) {
                            default: {
                                return false;
                            }
                            case 62:
                            case 96: {
                                ExoPlayerFragment.this.mediaController.playPause();
                                return true;
                            }
                            case 21: {
                                ExoPlayerFragment.this.seekWithOffset(-10000);
                                return true;
                            }
                            case 102: {
                                ExoPlayerFragment.this.seekWithOffset(-20000);
                                return true;
                            }
                            case 104: {
                                ExoPlayerFragment.this.seekWithOffset(-30000);
                                return true;
                            }
                            case 22: {
                                ExoPlayerFragment.this.seekWithOffset(10000);
                                return true;
                            }
                            case 103: {
                                ExoPlayerFragment.this.seekWithOffset(20000);
                                return true;
                            }
                            case 105: {
                                ExoPlayerFragment.this.seekWithOffset(30000);
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
    }
    
    private boolean isOuyaHardware() {
        return OuyaFacade.getInstance().isRunningOnOUYAHardware();
    }
    
    private boolean isScreenLocked() {
        return !this.isOuyaHardware() && ((KeyguardManager)this.getActivity().getSystemService("keyguard")).inKeyguardRestrictedInputMode();
    }
    
    public static ExoPlayerFragment newInstance(final EpisodeInfo episodeInfo, final boolean b, final boolean b2, final boolean b3) {
        final ExoPlayerFragment exoPlayerFragment = new ExoPlayerFragment();
        exoPlayerFragment.initAndSetBundle(episodeInfo, b, b2, b3);
        return exoPlayerFragment;
    }
    
    private void restorePlayhead() {
        final Integer value = this.savePositionTrigger.getPosition();
        this.log.debug("Restore playhead to %s (%dmsec)", Util.stringFromDuration(value), value);
        this.mVideoView.setPlayhead(value);
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
            this.savePositionTask = this.executor.scheduleAtFixedRate(this.savePositionTrigger, 0L, 1000L, TimeUnit.MILLISECONDS);
        }
        if (this.pingTask == null) {
            this.pingTask = this.executor.scheduleAtFixedRate(this.pingTrigger, 0L, 1000L, TimeUnit.MILLISECONDS);
        }
        if (this.videoViewTask == null) {
            this.videoViewTask = this.executor.scheduleAtFixedRate(this.videoViewTrigger, 0L, 1000L, TimeUnit.MILLISECONDS);
        }
        if (this.mShowAds && this.adTask == null) {
            this.adTask = this.executor.scheduleAtFixedRate(this.adTrigger, 0L, 1000L, TimeUnit.MILLISECONDS);
        }
    }
    
    private void seekWithOffset(final int n) {
        final VideoInfoView videoInfoView = (VideoInfoView)this.getView().findViewById(2131624259);
        final int duration = this.mVideoView.getDuration();
        if (duration == 0) {
            this.log.debug("Duration == 0, cancelling seek", new Object[0]);
            return;
        }
        int intValue;
        if (this.lastSeekTime.isPresent()) {
            intValue = this.lastSeekTime.get();
        }
        else {
            intValue = (int)this.mVideoView.getVideoPosition();
        }
        final int max = Math.max(0, Math.min(duration, intValue + n));
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
    
    private void sendPlaybackError() {
        this.log.error("Video error: Lost connection to video", new Object[0]);
        final Intent intent = new Intent("videoErrored");
        Extras.putInt(intent, "what", Integer.valueOf(0));
        Extras.putInt(intent, "extra", Integer.valueOf(0));
        LocalBroadcastManager.getInstance((Context)this.getActivity()).sendBroadcast(intent);
    }
    
    private void setMediaListener() {
        this.mediaController.setOnShowHideListener((ExoPlayerMediaController.OnShowHideListener)new ExoPlayerMediaController.OnShowHideListener() {
            @Override
            public void onBackPressed() {
                ExoPlayerFragment.this.getActivity().finish();
            }
            
            @Override
            public void onHide() {
                if (ExoPlayerFragment.this.deviceSupportsSystemUI()) {
                    ExoPlayerFragment.this.mHandler.removeCallbacksAndMessages((Object)null);
                    ExoPlayerFragment.this.hideNavigation();
                }
                ExoPlayerFragment.this.hideCastBar();
            }
            
            @Override
            public void onShow() {
                ExoPlayerFragment.this.showCastBar();
                if (ExoPlayerFragment.this.deviceSupportsSystemUI()) {
                    ExoPlayerFragment.this.showNavigation();
                }
            }
        });
    }
    
    private void showCastBar() {
        this.log.verbose("Show cast button", new Object[0]);
        this.mCastBar.setVisibility(0);
    }
    
    private void showNavigation() {
        this.getView().setSystemUiVisibility(0);
    }
    
    private void showProgressMessage() {
        this.mediaController.showProgress();
    }
    
    private void showRouteButton() {
        this.mRouteButton.setVisibility(0);
    }
    
    @Override
    public void disableNext() {
        if (this.mediaController != null) {
            this.mediaController.setNextEnabled(false);
        }
    }
    
    @Override
    public Stream getActiveStream() {
        return this.activeStream;
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
        return (int)this.mVideoView.getVideoPosition();
    }
    
    @Override
    public Stream getPreferredStream() {
        return this.streamData.getStreams().get(0);
    }
    
    @Override
    public Uri getVideoViewUri() {
        if (this.mVideoView != null) {
            return this.mUri;
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
                this.mVideoView.addVideo(this.mUri.toString());
                this.mVideoView.init();
                this.showProgressMessage();
            }
        }
        else if (this.mVideoView != null) {}
        if (!this.isPlaying()) {
            this.start();
        }
    }
    
    public void onAdStart() {
        if (this.mShowPreroll || this.mVideoView != null) {}
    }
    
    public void onCompletion(final MediaPlayer mediaPlayer) {
        this.cancelTimingTasks();
        this.finishPlayback();
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.executor = Executors.newScheduledThreadPool(3);
        this.mShowPreroll = Extras.getBoolean(this.getArguments(), "preroll").or(Boolean.valueOf(false));
        this.streamData = this.mEpisodeInfo.getMedia().getStreamData().get();
        this.activeStream = this.getPreferredStream();
        final Long mediaId = this.mEpisodeInfo.getMedia().getMediaId();
        this.savePositionTrigger = new SavePositionTrigger(this, bundle, Extras.getInt(this.getArguments(), "playhead"));
        this.pingTrigger = new PingTrigger(this, bundle, mediaId, this.mEpisodeInfo.getMedia().getDuration());
        this.videoViewTrigger = new VideoViewTrigger(this, (Context)this.getActivity(), bundle, this.mEpisodeInfo.getMedia().getDuration().get());
        Label_0276: {
            if (!this.mShowAds) {
                break Label_0276;
            }
            int attributeAsInt = 1;
            while (true) {
                try {
                    attributeAsInt = SwrveSDKBase.getResourceManager().getAttributeAsInt("tremor", "implementation", 1);
                    (this.adTrigger = new AdTrigger(this.getActivity(), this, bundle, this.mEpisodeInfo.getAdSlots(), new AdsListener(), this.mEpisodeInfo.getMaxAdStartSeconds(), this.mShowPreroll, CrunchyrollApplication.getApp((Context)this.getActivity()).getDeepLinker(), attributeAsInt)).setClosestAdRollToShown(this.mEpisodeInfo.getMedia().getPlayhead().or(Integer.valueOf(0)) * 1000);
                    if (this.mShowPreroll) {
                        this.adTrigger.playPreRoll();
                    }
                    this.mHandler = new Handler();
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        Optional<Boolean> isPlaying;
        if (this.isPlaying == null) {
            isPlaying = Optional.absent();
        }
        else {
            isPlaying = this.isPlaying;
        }
        this.isPlaying = isPlaying;
        this.isPlaying = this.isPlaying.or(Extras.getBoolean(bundle, "isPlaying"));
        this.isPlaying = this.isPlaying.or(Extras.getBoolean(this.getArguments(), "isPlaying"));
        final View inflate = layoutInflater.inflate(2130903135, viewGroup, false);
        this.mVideoView = (ExoPlayerView)inflate.findViewById(2131624251);
        (this.mAutoplayButton = (Switch)inflate.findViewById(2131624258)).setText((CharSequence)LocalizedStrings.AUTOPLAY.get());
        this.mAutoplayButton.setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)new CompoundButton$OnCheckedChangeListener() {
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean autoplay) {
                ExoPlayerFragment.this.log.debug("Autoplay set to " + autoplay, new Object[0]);
                ExoPlayerFragment.this.getApplicationState().setAutoplay(autoplay);
                ExoPlayerFragment.this.mediaController.show();
            }
        });
        this.initMediaController(inflate);
        this.mVideoView.setVideoController(this.mediaController);
        this.mVideoView.setStateChangeListener(this);
        this.mVideoView.addPlayerEventListener(this.mPlayerListener);
        this.mMessageView = (ViewGroup)inflate.findViewById(2131624263);
        final ImageView previewImg = (ImageView)inflate.findViewById(2131624254);
        final TextView previewTime = (TextView)inflate.findViewById(2131624255);
        this.mCastBar = inflate.findViewById(2131624092);
        ((TextView)inflate.findViewById(2131624256)).setText((CharSequence)Functional.Media.getShortEpisodeSubtitle(this.mEpisodeInfo.getMedia()));
        this.mRouteButton = (MediaRouteButton)this.mCastBar.findViewById(2131624257);
        if (CastHandler.isCastSupported((Context)this.getActivity())) {
            CastHandler.get().setRouteSelector(this.mRouteButton, new CustomRouteDialogFactory((Context)this.getActivity()));
        }
        if (CastHandler.isCastSupported((Context)this.getActivity())) {
            this.showRouteButton();
        }
        else {
            this.hideRouteButton();
        }
        if (this.deviceSupportsSystemUI()) {
            this.forceShowMediaController(inflate);
        }
        this.initVideoView(inflate);
        this.mediaController.setPreviewImg(previewImg);
        this.mediaController.setPreviewTime(previewTime);
        ((TextView)inflate.findViewById(2131624265)).setText((CharSequence)LocalizedStrings.STOPPING_VIDEO.get());
        List<Double> adsPlayHeads = null;
        if (this.mShowAds) {
            adsPlayHeads = this.mEpisodeInfo.getAdsPlayHeads();
        }
        this.mediaController.setCustomSeekBarDrawable(CustomProgressDrawable.createDrawable((Context)this.getActivity(), adsPlayHeads, this.mEpisodeInfo.getMedia().getDuration().get()));
        return inflate;
    }
    
    public void onDestroy() {
        super.onDestroy();
        this.mVideoView.stop();
        this.cancelTimingTasks();
        this.executor.shutdownNow();
        this.executor = null;
        this.mHandler.removeCallbacksAndMessages((Object)null);
    }
    
    public void onDestroyView() {
        super.onDestroyView();
    }
    
    public void onError(final Exception ex) {
        if (ex instanceof ExoPlaybackException || ex instanceof HttpDataSource.HttpDataSourceException) {
            this.sendPlaybackError();
        }
        ex.printStackTrace();
    }
    
    public boolean onError(final MediaPlayer mediaPlayer, final int n, final int n2) {
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
        if (this.deviceSupportsSystemUI()) {
            this.showNavigation();
        }
        if (this.mVideoView.isPlaying()) {
            this.mAutoPlayback = true;
        }
        this.lastKnownPlayhead = this.getPlayhead();
        this.mVideoView.release();
        super.onPause();
    }
    
    public void onPrepared(final MediaPlayer mediaPlayer) {
        this.handleResumingVideo();
        LocalBroadcastManager.getInstance((Context)this.getActivity()).sendBroadcast(new Intent("videoStarted"));
    }
    
    public void onResume() {
        super.onResume();
        this.mediaController.addVideoControllerListener(this.mControllerListener);
        this.scheduleTimingTasks();
        this.handleResumingVideo();
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.savePositionTrigger.saveInstanceState(bundle);
        this.pingTrigger.saveInstanceState(bundle);
        this.videoViewTrigger.saveInstanceState(bundle);
        if (this.adTrigger != null) {
            this.adTrigger.saveInstanceState(bundle);
        }
    }
    
    public void onSeekComplete(final MediaPlayer mediaPlayer) {
        final int n = (int)this.mVideoView.getVideoPosition();
        this.log.debug(String.format("Seek complete, current position: %s (%d)", Util.stringFromDuration(n), n), new Object[0]);
        this.hideProgressMessage();
        if (this.mAutoPlayback) {
            this.mAutoPlayback = false;
            this.mVideoView.play();
        }
        this.mediaController.hidePreview();
    }
    
    public void onSeekStart() {
        this.showProgressMessage();
    }
    
    public void onStart() {
        super.onStart();
        this.unlockReceiver = new UnlockReceiver();
        this.getActivity().registerReceiver((BroadcastReceiver)this.unlockReceiver, this.getUnlockIntentFilter());
        this.mCastReceiver = new CastReceiver();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("CAST_NO_ROUTES_AVAILABLE_EVENT");
        intentFilter.addAction("CAST_ROUTES_AVAILABLE_EVENT");
        LocalBroadcastManager.getInstance((Context)this.getActivity()).registerReceiver(this.mCastReceiver, intentFilter);
        final ProcessBifFileTask processBifFileTask = new ProcessBifFileTask((Context)this.getActivity(), this.mEpisodeInfo.getMedia().getBIFUrl());
        processBifFileTask.setTaskListener(new BaseListener<BIFFile>() {
            @Override
            public void onPreExecute() {
                ExoPlayerFragment.this.mediaController.setBIFFile(null);
            }
            
            @Override
            public void onSuccess(final BIFFile bifFile) {
                ExoPlayerFragment.this.mediaController.setBIFFile(bifFile);
            }
        });
        processBifFileTask.execute();
    }
    
    public void onStateChange(final PlayerState playerState) {
        switch (playerState) {
            default: {}
            case STATE_BUFFERING: {
                this.showProgressMessage();
            }
            case STATE_ENDED: {
                if (this.getApplicationState().getAutoplay()) {
                    this.next();
                    return;
                }
                this.getActivity().finish();
            }
            case STATE_PREPARING: {
                this.showProgressMessage();
            }
            case STATE_READY: {
                this.hideProgressMessage();
                if (this.isVisible()) {
                    LocalBroadcastManager.getInstance((Context)this.getActivity()).sendBroadcast(new Intent("videoStarted"));
                    return;
                }
                this.mVideoView.pause();
            }
        }
    }
    
    public void onStop() {
        super.onStop();
        this.getActivity().unregisterReceiver((BroadcastReceiver)this.unlockReceiver);
        LocalBroadcastManager.getInstance((Context)this.getActivity()).unregisterReceiver(this.mCastReceiver);
        while (true) {
            if (!this.getActivity().isFinishing() || this.lastKnownPlayhead <= 0) {
                break Label_0208;
            }
            try {
                if (this.pingTrigger.getNumberOfPings() > 0L) {
                    this.pingTrigger.runTrigger(this, this.lastKnownPlayhead, true);
                    final Intent intent = new Intent("VIDEO_PROGRESS_UPDATED");
                    Extras.putLong(intent, "seriesId", Long.valueOf(this.mEpisodeInfo.getMedia().getSeriesId()));
                    Extras.putLong(intent, "mediaId", this.mEpisodeInfo.getMedia().getMediaId());
                    Extras.putInt(intent, "playhead", Integer.valueOf(this.lastKnownPlayhead / 1000));
                    LocalBroadcastManager.getInstance((Context)this.getActivity()).sendBroadcast(intent);
                    ApiManager.getInstance(this.getActivity()).invalidateCache(Media.class, Optional.of(this.mEpisodeInfo.getMedia().getSeriesId()));
                    ApiManager.getInstance(this.getActivity()).invalidateCache(Media.class, Optional.of(this.mEpisodeInfo.getMedia().getCollectionId()));
                }
                this.mVideoView.stop();
            }
            catch (Exception ex) {
                this.log.error("Error running trigger!", ex);
                continue;
            }
            break;
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
        if (this.activeStream.getUrl() != null) {
            this.mUri = Uri.parse(this.activeStream.getUrl());
            if (this.mUri == null) {
                final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.getActivity());
                alertDialog$Builder.setMessage((CharSequence)LocalizedStrings.ERROR_LOADING_MEDIA.get());
                alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.OK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        ExoPlayerFragment.this.getActivity().finish();
                    }
                });
                alertDialog$Builder.show();
            }
            else if (!this.mShowPreroll) {
                this.mVideoView.addVideo(this.mUri.toString());
                this.mVideoView.init();
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
                    ExoPlayerFragment.this.lastKnownPlayhead = (int)ExoPlayerFragment.this.mVideoView.getVideoPosition();
                    ExoPlayerFragment.this.mVideoView.setVisibility(4);
                    if (ExoPlayerFragment.this.mediaController.getVisibility() == 0) {
                        ExoPlayerFragment.this.mediaController.setVisibility(8);
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
            this.mVideoView.release();
            this.mVideoView.init();
        }
    }
    
    @Override
    public void startFrom(final int n) {
        this.lastKnownPlayhead = n;
        this.savePositionTrigger.setPosition(n);
        this.start();
    }
    
    private class CastReceiver extends BroadcastReceiver
    {
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction().equals("CAST_ROUTES_AVAILABLE_EVENT")) {
                ExoPlayerFragment.this.showRouteButton();
            }
            else if (intent.getAction().equals("CAST_NO_ROUTES_AVAILABLE_EVENT")) {
                ExoPlayerFragment.this.hideRouteButton();
            }
        }
    }
    
    private class MediaControllerListener extends AbstractVideoControllerListener
    {
        @Override
        public void onNextPressed() {
            ExoPlayerFragment.this.next();
        }
    }
    
    private class PlayerEventListener extends AbstractPlayerEventListener
    {
        @Override
        public void onVideoSizeChanged(final int n, final int n2) {
        }
    }
    
    private class UnlockReceiver extends BroadcastReceiver
    {
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction().equals("android.intent.action.USER_PRESENT") && ExoPlayerFragment.this.mAutoPlayback) {
                ExoPlayerFragment.this.mAutoPlayback = false;
                ExoPlayerFragment.this.mVideoView.play();
            }
        }
    }
}
