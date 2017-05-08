// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.activities;

import android.util.Base64;
import com.crunchyroll.cast.model.CastInfo;
import com.google.android.gms.appindexing.Action;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentManager;
import com.google.common.base.Optional;
import com.crunchyroll.crunchyroid.tracking.FacebookTracker;
import android.content.IntentFilter;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.appindexing.AppIndex;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.content.res.Configuration;
import java.util.Calendar;
import java.text.ParseException;
import java.io.Serializable;
import java.util.GregorianCalendar;
import com.crunchyroll.video.util.MediaManager;
import com.crunchyroll.android.api.models.Media;
import com.crunchyroll.crunchyroid.activities.MainActivity;
import android.content.DialogInterface$OnCancelListener;
import java.util.HashMap;
import android.app.Activity;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.crunchyroid.app.Extras;
import android.content.Intent;
import com.crunchyroll.video.fragments.ExoPlayerFragment;
import com.secondtv.android.ads.DeepLinker;
import com.crunchyroll.android.api.models.Stream;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.crunchyroll.video.fragments.VideoPlayerFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.crunchyroll.cast.CastState;
import com.crunchyroll.cast.CastHandler;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.content.Context;
import android.app.AlertDialog$Builder;
import com.crunchyroll.android.util.LoggerFactory;
import android.app.AlertDialog;
import android.os.Handler;
import com.google.android.gms.common.api.GoogleApiClient;
import android.content.BroadcastReceiver;
import com.crunchyroll.video.fragments.AbstractVideoPlayerFragment;
import android.net.Uri;
import com.crunchyroll.android.util.Logger;
import com.crunchyroll.android.api.models.EpisodeInfo;
import com.secondtv.android.ads.vast.ClickthroughFragment;
import com.crunchyroll.video.fragments.CastPlayerFragment;
import com.crunchyroll.crunchyroid.activities.TrackedActivity;

public class VideoPlayerActivity extends TrackedActivity
{
    public static final String APP_URI_BASE = "crunchyroll://playmedia/";
    public static final String VIDEO_ERRORED = "videoErrored";
    public static final String VIDEO_FINISHED = "videoFinished";
    public static final int VIDEO_LOAD_TIMEOUT_MS = 45000;
    public static final String VIDEO_STARTED = "videoStarted";
    private int appEntryFrom;
    private CastPlayerFragment castPlayerFragment;
    private ClickthroughFragment clickthroughFragment;
    private ClickthroughFragment.ClickthroughListener clickthroughListener;
    private EpisodeInfo episodeInfo;
    private FragmentAdapter fragmentAdapter;
    private FragmentSwitcher fragmentSwitcher;
    private boolean hasIndexed;
    private final Logger log;
    private Uri mAppUri;
    private AbstractVideoPlayerFragment.OnClickthroughListener mCastClickthroughListener;
    private BroadcastReceiver mCastReceiver;
    private GoogleApiClient mClient;
    private Handler mHandler;
    private boolean mIsTablet;
    private boolean mIsVideoStarted;
    private boolean mShowPreroll;
    private boolean mSkipResumeDialog;
    private String mTitle;
    private AlertDialog mVideoLoadTimoutDialog;
    private Runnable mVideoPreparedTimerRunnable;
    private VideoTarget mVideoTarget;
    private Uri mWebUri;
    private VideoMessageReceiver videoMessageReceiver;
    private AbstractVideoPlayerFragment videoPlayerFragment;
    
    public VideoPlayerActivity() {
        this.mVideoTarget = VideoTarget.Device;
        this.log = LoggerFactory.getLogger(VideoPlayerActivity.class);
        this.fragmentSwitcher = new FragmentSwitcher();
        this.mVideoPreparedTimerRunnable = new Runnable() {
            @Override
            public void run() {
                if (!VideoPlayerActivity.this.mIsVideoStarted && VideoPlayerActivity.this.mVideoTarget != VideoTarget.Cast) {
                    final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)VideoPlayerActivity.this);
                    alertDialog$Builder.setCancelable(false);
                    alertDialog$Builder.setMessage((CharSequence)LocalizedStrings.VIDEO_TAKING_WHILE_TO_LOAD.get());
                    alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.BACK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            VideoPlayerActivity.this.finish();
                            VideoPlayerActivity.this.overridePendingTransition(0, 0);
                        }
                    });
                    alertDialog$Builder.setNegativeButton((CharSequence)LocalizedStrings.KEEP_WAITING.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                        }
                    });
                    VideoPlayerActivity.this.mVideoLoadTimoutDialog = alertDialog$Builder.show();
                }
            }
        };
        this.clickthroughListener = new ClickthroughFragment.ClickthroughListener() {
            @Override
            public void onClickthroughDismissed() {
                VideoPlayerActivity.this.finishClickthrough();
            }
            
            @Override
            public void onClickthroughMarketLinkOpened() {
                VideoPlayerActivity.this.finishClickthrough();
            }
        };
    }
    
    private void checkPlayheadAndShowResumeDialog() {
        final int intValue = this.episodeInfo.getMedia().getPlayhead().or(Integer.valueOf(0));
        if (intValue > 0) {
            this.showResumeDialog(intValue);
            return;
        }
        this.continueAfterResumeDialog(false);
    }
    
    private void continueAfterResumeDialog(final boolean b) {
        final CastState state = CastHandler.get().getState();
        if (state == CastState.CONNECTING || state == CastState.CONNECTED) {
            this.mVideoTarget = VideoTarget.Cast;
        }
        else {
            this.mVideoTarget = VideoTarget.Device;
        }
        this.log.info("Video target selected: " + this.mVideoTarget.name(), new Object[0]);
        if (this.videoPlayerFragment == null) {
            this.videoPlayerFragment = this.createVideoPlayerFragment();
        }
        if (this.castPlayerFragment == null) {
            this.castPlayerFragment = this.createCastPlayerFragment();
        }
        this.initCastListener();
        this.castPlayerFragment.setOnClickthroughListener(this.mCastClickthroughListener);
        if (this.clickthroughFragment == null) {
            this.clickthroughFragment = this.createClickthroughFragment();
        }
        this.fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager());
        this.fragmentSwitcher.setAdapter(this.fragmentAdapter);
        this.setAndSwitchToVideoTarget(this.mVideoTarget);
        this.videoMessageReceiver = new VideoMessageReceiver();
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.videoMessageReceiver, VideoPlayerFragment.VIDEO_FILTER);
        if (b) {
            final Stream preferredStream = this.videoPlayerFragment.getPreferredStream();
            Tracker.playbackProgress((Context)this, this.episodeInfo.getMedia().getMediaId(), preferredStream.getVideoId(), preferredStream.getVideoEncodeId(), 0, 0, 0);
        }
        if (!this.mShowPreroll) {
            this.mHandler.postDelayed(this.mVideoPreparedTimerRunnable, 45000L);
        }
    }
    
    private CastPlayerFragment createCastPlayerFragment() {
        return CastPlayerFragment.newInstance(this.episodeInfo, this.mVideoTarget == VideoTarget.Cast, this.shouldShowAds());
    }
    
    private ClickthroughFragment createClickthroughFragment() {
        return ClickthroughFragment.newInstance(this.clickthroughListener, LocalizedStrings.CLOSE.get(), null);
    }
    
    private AbstractVideoPlayerFragment createVideoPlayerFragment() {
        final boolean b = true;
        boolean b2 = true;
        switch (ApplicationState.get((Context)this).getVideoPlayerType()) {
            default: {
                final EpisodeInfo episodeInfo = this.episodeInfo;
                if (this.mVideoTarget != VideoTarget.Device) {
                    b2 = false;
                }
                return VideoPlayerFragment.newInstance(episodeInfo, b2, this.shouldShowAds(), this.mShowPreroll);
            }
            case 2: {
                return ExoPlayerFragment.newInstance(this.episodeInfo, this.mVideoTarget == VideoTarget.Device && b, this.shouldShowAds(), this.mShowPreroll);
            }
        }
    }
    
    private void finishClickthrough() {
        this.fragmentSwitcher.setCurrentItem(2);
    }
    
    public static Intent getStartIntent(final Context context, final EpisodeInfo episodeInfo, final boolean b, final int n) {
        final Intent intent = new Intent(context, (Class)VideoPlayerActivity.class);
        Extras.putSerializable(intent, "episodeInfo", episodeInfo);
        Extras.putInt(intent, "intent_from", Integer.valueOf(n));
        Extras.putBoolean(intent, "skipResumeDialog", b);
        return intent;
    }
    
    public static Intent getStartIntent(final Context context, final EpisodeInfo episodeInfo, final boolean b, final int n, final Boolean b2) {
        final Intent intent = new Intent(context, (Class)VideoPlayerActivity.class);
        Extras.putSerializable(intent, "episodeInfo", episodeInfo);
        Extras.putInt(intent, "intent_from", Integer.valueOf(n));
        Extras.putBoolean(intent, "skipResumeDialog", b);
        if (b2 != null) {
            Extras.putBoolean(intent, "preroll", b2);
        }
        return intent;
    }
    
    public static Intent getStartIntent(final Context context, final EpisodeInfo episodeInfo, final boolean b, final int n, final boolean b2) {
        final Intent intent = new Intent(context, (Class)VideoPlayerActivity.class);
        Extras.putSerializable(intent, "episodeInfo", episodeInfo);
        Extras.putInt(intent, "intent_from", Integer.valueOf(n));
        Extras.putBoolean(intent, "retryVideo", b2);
        Extras.putBoolean(intent, "skipResumeDialog", b);
        return intent;
    }
    
    private void initCastListener() {
        this.mCastClickthroughListener = new AbstractVideoPlayerFragment.OnClickthroughListener() {
            @Override
            public void onClickthrough(final String s) {
                VideoPlayerActivity.this.showClickthrough(s);
            }
        };
    }
    
    private void setAndSwitchToVideoTarget(final VideoTarget videoTarget) {
        this.setVideoTarget(videoTarget);
        this.fragmentSwitcher.setCurrentItem(this.mVideoTarget.getFragmentIndex());
        switch (this.mVideoTarget) {
            case Cast: {
                if (this.mIsTablet) {
                    Util.setOrientation(this, Util.Orientation.ANY);
                }
                else {
                    Util.setOrientation(this, Util.Orientation.PORTRAIT);
                }
                Tracker.swrveScreenView("chromecast-fullscreen");
            }
            case Device: {
                Util.setOrientation(this, Util.Orientation.LANDSCAPE);
                if (this.episodeInfo.getMedia().getMediaType().or("").equalsIgnoreCase("anime")) {
                    final HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("series", this.episodeInfo.getMedia().getSeriesName().or(""));
                    hashMap.put("series-episode", "episode-" + this.episodeInfo.getMedia().getEpisodeNumber());
                    Tracker.swrveScreenView("anime-video", hashMap);
                    Tracker.videoSeries("anime", this.episodeInfo.getMedia().getSeriesName().or("").replace(".", "-").replace(",", "-").replace("'", "-").replace(":", "-"), this.episodeInfo.getMedia().getSeriesName().or(""), "episode-" + this.episodeInfo.getMedia().getEpisodeNumber());
                    Tracker.videoPlay("anime-plays", this.episodeInfo.getMedia().getSeriesName().or(""), "episode-" + this.episodeInfo.getMedia().getEpisodeNumber());
                    return;
                }
                if (this.episodeInfo.getMedia().getMediaType().or("").equalsIgnoreCase("drama")) {
                    final HashMap<String, String> hashMap2 = new HashMap<String, String>();
                    hashMap2.put("series", this.episodeInfo.getMedia().getSeriesName().or(""));
                    hashMap2.put("series-episode", "episode-" + this.episodeInfo.getMedia().getEpisodeNumber());
                    Tracker.swrveScreenView("drama-video", hashMap2);
                    Tracker.videoSeries("drama", this.episodeInfo.getMedia().getSeriesName().or("").replace(".", "-").replace(",", "-").replace("'", "-").replace(":", "-"), this.episodeInfo.getMedia().getSeriesName().or(""), "episode-" + this.episodeInfo.getMedia().getEpisodeNumber());
                    Tracker.videoPlay("drama-plays", this.episodeInfo.getMedia().getSeriesName().or(""), "episode-" + this.episodeInfo.getMedia().getEpisodeNumber());
                    return;
                }
                break;
            }
        }
    }
    
    private void setVideoTarget(final VideoTarget mVideoTarget) {
        this.mVideoTarget = mVideoTarget;
    }
    
    private boolean shouldShowAds() {
        return this.episodeInfo != null && !this.getApplicationState().isUserPremiumForMediaType(this.episodeInfo.getMedia().getMediaType().get());
    }
    
    private void showClickthrough(final String s) {
        this.fragmentSwitcher.setCurrentItem(1);
        this.clickthroughFragment.load(s);
    }
    
    private void showResumeDialog(final int n) {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
        final String value = LocalizedStrings.RESTART_OR_RESUME_VIDEO.get(Util.stringFromDuration(n * 1000));
        alertDialog$Builder.setTitle((CharSequence)this.episodeInfo.getMedia().getName());
        alertDialog$Builder.setMessage((CharSequence)value);
        alertDialog$Builder.setNegativeButton((CharSequence)LocalizedStrings.RESUME_VIDEO.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                VideoPlayerActivity.this.continueAfterResumeDialog(false);
            }
        });
        alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.RESTART_VIDEO.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                VideoPlayerActivity.this.episodeInfo.getMedia().setPlayhead(0);
                VideoPlayerActivity.this.continueAfterResumeDialog(true);
            }
        });
        alertDialog$Builder.setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
            public void onCancel(final DialogInterface dialogInterface) {
                VideoPlayerActivity.this.finish();
            }
        });
        alertDialog$Builder.create().show();
    }
    
    public static void start(final Activity activity, final EpisodeInfo episodeInfo, final boolean b, final int n) {
        activity.startActivityForResult(getStartIntent((Context)activity, episodeInfo, b, n), 21);
    }
    
    public static void start(final Activity activity, final EpisodeInfo episodeInfo, final boolean b, final int n, final Boolean b2) {
        activity.startActivityForResult(getStartIntent((Context)activity, episodeInfo, b, n, b2), 21);
    }
    
    public static void start(final Activity activity, final EpisodeInfo episodeInfo, final boolean b, final int n, final boolean b2) {
        activity.startActivityForResult(getStartIntent((Context)activity, episodeInfo, b, n, b2), 21);
    }
    
    private void unregisterListeners() {
        if (this.videoMessageReceiver != null) {
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.videoMessageReceiver);
            this.videoMessageReceiver = null;
        }
        if (this.mCastReceiver != null) {
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.mCastReceiver);
            this.mCastReceiver = null;
        }
        this.mHandler.removeCallbacksAndMessages((Object)null);
    }
    
    public void finish() {
        this.unregisterListeners();
        switch (this.appEntryFrom) {
            default: {
                super.finish();
            }
            case 1: {
                final Intent intent = new Intent((Context)this, (Class)MainActivity.class);
                intent.setFlags(335544320);
                super.finish();
                this.startActivity(intent);
            }
        }
    }
    
    String getLongName(final Media media) {
        return media.getCollectionName() + " Episode " + media.getEpisodeNumber() + " - " + media.getName();
    }
    
    public void next(final boolean b) {
        final MediaManager instance = MediaManager.getInstance();
        if (instance.hasMoreMedia()) {
            instance.getNext(this, (MediaManager.MediaManagerListener)new MediaManager.MediaManagerListener() {
                @Override
                public void onException(final Exception ex) {
                    final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)VideoPlayerActivity.this);
                    alertDialog$Builder.setCancelable(false);
                    alertDialog$Builder.setMessage((CharSequence)LocalizedStrings.ERROR_VIDEO.get());
                    alertDialog$Builder.setNegativeButton((CharSequence)LocalizedStrings.BACK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            dialogInterface.dismiss();
                            VideoPlayerActivity.this.finish();
                            VideoPlayerActivity.this.overridePendingTransition(0, 0);
                        }
                    });
                    alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.RETRY.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            dialogInterface.dismiss();
                            VideoPlayerActivity.this.next(false);
                        }
                    });
                    alertDialog$Builder.show();
                }
                
                @Override
                public void onFinally() {
                }
                
                @Override
                public void onNextEpisodeReceived(final EpisodeInfo episodeInfo) {
                    final GregorianCalendar gregorianCalendar = new GregorianCalendar();
                    Calendar calendar2;
                    final Calendar calendar = calendar2 = null;
                    while (true) {
                        if (episodeInfo.getMedia().getAvailableTime() == null) {
                            break Label_0038;
                        }
                        try {
                            calendar2 = Util.toCalendar(episodeInfo.getMedia().getAvailableTime());
                            if (!episodeInfo.getMedia().isFreeAvailable().get() && !VideoPlayerActivity.this.getApplicationState().isUserPremiumForMediaType(episodeInfo.getMedia().getMediaType().get())) {
                                final Intent intent = new Intent();
                                intent.putExtra("media", (Serializable)episodeInfo.getMedia());
                                VideoPlayerActivity.this.setResult(23, intent);
                                VideoPlayerActivity.this.finish();
                                VideoPlayerActivity.this.overridePendingTransition(0, 0);
                                VideoPlayerActivity.this.castPlayerFragment.stop();
                                return;
                            }
                            if (!episodeInfo.getMedia().getStreamData().isPresent() || (calendar2 != null && calendar2.after(gregorianCalendar))) {
                                VideoPlayerActivity.this.setResult(22);
                                VideoPlayerActivity.this.finish();
                                VideoPlayerActivity.this.overridePendingTransition(0, 0);
                                VideoPlayerActivity.this.castPlayerFragment.stop();
                                return;
                            }
                            if (VideoPlayerActivity.this.shouldShowAds()) {
                                int n;
                                if (VideoPlayerActivity.this.mVideoTarget == VideoTarget.Device) {
                                    n = VideoPlayerActivity.this.videoPlayerFragment.getPlayhead() / 1000;
                                }
                                else {
                                    n = VideoPlayerActivity.this.castPlayerFragment.getPlayhead() / 1000;
                                }
                                VideoPlayerActivity.this.finish();
                                VideoPlayerActivity.this.overridePendingTransition(0, 0);
                                VideoPlayerActivity.start(VideoPlayerActivity.this, episodeInfo, true, VideoPlayerActivity.this.appEntryFrom, Boolean.valueOf(n >= 30 || b));
                                return;
                            }
                            VideoPlayerActivity.this.finish();
                            VideoPlayerActivity.this.overridePendingTransition(0, 0);
                            VideoPlayerActivity.start(VideoPlayerActivity.this, episodeInfo, true, VideoPlayerActivity.this.appEntryFrom);
                        }
                        catch (ParseException ex) {
                            calendar2 = calendar;
                            continue;
                        }
                        break;
                    }
                }
            });
            return;
        }
        this.finish();
    }
    
    @Override
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        super.onActivityResult(n, n2, intent);
        if (n == 56026 && n2 != 45245) {
            this.videoPlayerFragment.onAdFinish();
            if (this.mShowPreroll) {
                this.mShowPreroll = false;
                this.mHandler.postDelayed(this.mVideoPreparedTimerRunnable, 45000L);
            }
            return;
        }
        this.finish();
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Tracker.videoPlay("back", this.episodeInfo.getMedia().getSeriesName().or(""), "episode-" + this.episodeInfo.getMedia().getEpisodeNumber());
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.castPlayerFragment != null) {
            final FragmentTransaction beginTransaction = this.getSupportFragmentManager().beginTransaction();
            beginTransaction.detach(this.castPlayerFragment);
            beginTransaction.attach(this.castPlayerFragment);
            beginTransaction.commitAllowingStateLoss();
        }
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130903078);
        this.mIsTablet = (this.getResources().getInteger(2131427330) != 0);
        final Optional<Boolean> boolean1 = Extras.getBoolean(this.getIntent(), "retryVideo");
        this.mHandler = new Handler();
        this.mIsVideoStarted = false;
        final FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        this.videoPlayerFragment = (AbstractVideoPlayerFragment)supportFragmentManager.findFragmentByTag(AbstractVideoPlayerFragment.class.getSimpleName());
        this.castPlayerFragment = (CastPlayerFragment)supportFragmentManager.findFragmentByTag(CastPlayerFragment.class.getSimpleName());
        this.clickthroughFragment = (ClickthroughFragment)supportFragmentManager.findFragmentByTag(ClickthroughFragment.class.getSimpleName());
        this.log.debug();
        boolean b = false;
        final boolean b2 = false;
        this.mClient = new GoogleApiClient.Builder((Context)this).addApi(AppIndex.APP_INDEX_API).build();
        this.hasIndexed = false;
        if (bundle != null) {
            if (VideoTarget.values()[bundle.getInt("fragmentContentsId")].equals(VideoTarget.Cast)) {
                this.finish();
                return;
            }
            this.mSkipResumeDialog = Extras.getBoolean(bundle, "skipResumeDialog").get();
            this.episodeInfo = Extras.getSerializable(bundle, "episodeInfo", EpisodeInfo.class).orNull();
            b = b2;
            if (this.videoPlayerFragment != null) {
                b = b2;
                if (this.castPlayerFragment != null) {
                    b = b2;
                    if (this.clickthroughFragment != null) {
                        b = true;
                        this.setAndSwitchToVideoTarget(VideoTarget.values()[bundle.getInt("fragmentContentsId")]);
                    }
                }
            }
            this.log.debug("Restoring data from savedInstanceState. Active fragment = " + this.mVideoTarget.name(), new Object[0]);
        }
        else if (this.getIntent() != null && this.getIntent().getAction() != null && this.getIntent().getAction().equals("com.crunchyroll.intent.action.CAST")) {
            this.mSkipResumeDialog = Extras.getBoolean(this.getIntent(), "skipResumeDialog").or(Boolean.valueOf(false));
            this.episodeInfo = CrunchyrollApplication.getApp((Context)this).getCurrentlyCastingEpisode().orNull();
            if (this.episodeInfo == null) {
                this.log.error("Cannot start video for cast without episodeInfo", new Object[0]);
                this.finish();
                return;
            }
            this.log.info("Starting for cast", new Object[0]);
            this.setAndSwitchToVideoTarget(VideoTarget.Cast);
        }
        else {
            this.log.info("Starting for local playback", new Object[0]);
            this.episodeInfo = Extras.getSerializable(this.getIntent(), "episodeInfo", EpisodeInfo.class).get();
            this.mSkipResumeDialog = Extras.getBoolean(this.getIntent(), "skipResumeDialog").get();
            if (this.episodeInfo == null) {
                throw new NullPointerException("Can't start without episodeInfo");
            }
        }
        this.mShowPreroll = Extras.getBoolean(this.getIntent(), "preroll").or(Boolean.valueOf(false));
        this.mCastReceiver = new CastReceiver();
        final IntentFilter intentFilter = new IntentFilter(CastHandler.SESSION_INTENT_FILTER);
        intentFilter.addAction("CAST_VIDEO_STARTED_EVENT");
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mCastReceiver, intentFilter);
        if (Extras.getBoolean(this.getIntent(), "autoPlayback").or(Boolean.valueOf(false)) | (CastHandler.isCastSupported((Context)this) && CastHandler.get().isPlayingEpisode(this.episodeInfo.getMedia().getMediaId())) | b) {
            this.continueAfterResumeDialog(false);
        }
        else if (this.mSkipResumeDialog) {
            this.episodeInfo.getMedia().setPlayhead(0);
            this.continueAfterResumeDialog(true);
        }
        else if (!boolean1.isPresent() || (boolean1.isPresent() && !boolean1.get())) {
            final int intValue = this.episodeInfo.getMedia().getPlayhead().or(Integer.valueOf(0));
            final Optional<Integer> duration = this.episodeInfo.getMedia().getDuration();
            if (duration.isPresent()) {
                final double n = intValue / duration.get();
                this.appEntryFrom = this.getIntent().getExtras().getInt("intent_from", 0);
                if (intValue < 30 || n > 0.85) {
                    this.episodeInfo.getMedia().setPlayhead(0);
                }
            }
            this.checkPlayheadAndShowResumeDialog();
        }
        else {
            this.continueAfterResumeDialog(false);
        }
        FacebookTracker.videoStart((Context)this);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterListeners();
    }
    
    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
    
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);
        menu.getItem(4).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (CastHandler.get() != null && this.mVideoTarget == VideoTarget.Cast && !this.castPlayerFragment.isPlaying()) {
            this.log.debug("onResume: Cast stopped playing while in the background - closing video activity", new Object[0]);
            this.finish();
        }
    }
    
    @Override
    protected void onSaveInstanceState(final Bundle bundle) {
        bundle.putInt("fragmentContentsId", this.mVideoTarget.ordinal());
        bundle.putSerializable("episodeInfo", (Serializable)this.episodeInfo);
        bundle.putBoolean("skipResumeDialog", this.mSkipResumeDialog);
        super.onSaveInstanceState(bundle);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        if (this.mTitle != null) {
            return;
        }
        try {
            final Media media = this.episodeInfo.getMedia();
            this.mAppUri = Uri.parse("android-app://" + this.getPackageName() + "/crunchyroll/" + "playmedia" + "/" + media.getMediaId());
            this.mWebUri = Uri.parse(media.getUrl());
            this.mTitle = this.getLongName(media);
            this.mClient.connect();
            AppIndex.AppIndexApi.start(this.mClient, Action.newAction("http://schema.org/ViewAction", this.mTitle, this.mWebUri, this.mAppUri));
        }
        catch (Exception ex) {}
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        if (!this.hasIndexed && this.mTitle != null) {
            AppIndex.AppIndexApi.end(this.mClient, Action.newAction("http://schema.org/ViewAction", this.mTitle, this.mWebUri, this.mAppUri));
            this.mClient.disconnect();
            this.hasIndexed = true;
        }
    }
    
    private class CastReceiver extends BroadcastReceiver
    {
        private boolean episodeStartTracked;
        
        private CastReceiver() {
            this.episodeStartTracked = false;
        }
        
        private void handleSessionEnded(final Intent intent) {
            VideoPlayerActivity.this.mVideoTarget = VideoTarget.Device;
            VideoPlayerActivity.this.log.info("Handling cast session end", new Object[0]);
            if (VideoPlayerActivity.this.videoPlayerFragment != null && VideoPlayerActivity.this.fragmentSwitcher.getCurrentItem() == 2) {
                final CastInfo castInfo = Extras.getSerializable(intent, "castInfo", CastInfo.class).orNull();
                if (castInfo == null) {
                    VideoPlayerActivity.this.log.debug("Intent contains no episodeInfo - closing activity", new Object[0]);
                    VideoPlayerActivity.this.finish();
                    return;
                }
                VideoPlayerActivity.this.log.debug("Intent contains episodeInfo - switch to local player", new Object[0]);
                VideoPlayerActivity.this.setAndSwitchToVideoTarget(VideoPlayerActivity.this.mVideoTarget);
                final int n = Optional.of(castInfo.getPlayhead()).or(Integer.valueOf(0)) * 1000;
                VideoPlayerActivity.this.log.debug("Play from " + n + " after cast session ended", new Object[0]);
                VideoPlayerActivity.this.videoPlayerFragment.startFrom(n);
            }
        }
        
        private void handleSessionStarting() {
            int playhead;
            if (VideoPlayerActivity.this.mVideoTarget == VideoTarget.Device) {
                if (VideoPlayerActivity.this.videoPlayerFragment.isPlaying()) {
                    VideoPlayerActivity.this.videoPlayerFragment.pause();
                }
                playhead = VideoPlayerActivity.this.videoPlayerFragment.getLastKnownPlayhead() / 1000;
                VideoPlayerActivity.this.log.debug("CONTINUE play on cast device from %d s", playhead);
                VideoPlayerActivity.this.setAndSwitchToVideoTarget(VideoTarget.Cast);
            }
            else {
                playhead = VideoPlayerActivity.this.castPlayerFragment.getPlayhead();
                VideoPlayerActivity.this.log.debug("RETRY play on cast device from %d s", playhead);
            }
            VideoPlayerActivity.this.castPlayerFragment.startFrom(playhead);
        }
        
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            VideoPlayerActivity.this.log.debug("Received " + action, new Object[0]);
            if (action.equals("CAST_SESSION_STARTING_EVENT")) {
                this.handleSessionStarting();
            }
            else if (!action.equals("CAST_SESSION_STARTED_EVENT")) {
                if (action.equals("CAST_SESSION_ENDED_EVENT") || action.equals("CAST_SESSION_LOST_EVENT")) {
                    this.handleSessionEnded(intent);
                    return;
                }
                if (action.equals("CAST_VIDEO_STARTED_EVENT") && !this.episodeStartTracked) {
                    Tracker.chromecastStartPlayback((Context)VideoPlayerActivity.this, VideoPlayerActivity.this.episodeInfo.getMedia());
                    this.episodeStartTracked = true;
                }
            }
        }
    }
    
    private class FragmentAdapter extends FragmentPagerAdapter
    {
        public static final int CAST_PLAYER_INDEX = 2;
        public static final int CLICKTHROUGH_INDEX = 1;
        public static final int VIDEO_INDEX = 0;
        
        public FragmentAdapter(final FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        
        @Override
        public int getCount() {
            return 3;
        }
        
        @Override
        public Fragment getItem(final int n) {
            switch (n) {
                default: {
                    throw new IllegalAccessError("There shouldn't be anything here");
                }
                case 0: {
                    return VideoPlayerActivity.this.videoPlayerFragment;
                }
                case 2: {
                    return VideoPlayerActivity.this.castPlayerFragment;
                }
                case 1: {
                    return VideoPlayerActivity.this.clickthroughFragment;
                }
            }
        }
    }
    
    private class FragmentSwitcher
    {
        private FragmentPagerAdapter mAdapter;
        private int mCurrentItem;
        
        private FragmentSwitcher() {
            this.mCurrentItem = -1;
        }
        
        public int getCurrentItem() {
            return this.mCurrentItem;
        }
        
        public void setAdapter(final FragmentPagerAdapter mAdapter) {
            if (!mAdapter.equals(this.mAdapter)) {
                this.mAdapter = mAdapter;
                if (this.mAdapter != null) {
                    final FragmentManager supportFragmentManager = VideoPlayerActivity.this.getSupportFragmentManager();
                    final FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
                    if (supportFragmentManager.findFragmentByTag(AbstractVideoPlayerFragment.class.getSimpleName()) == null) {
                        beginTransaction.add(2131624055, VideoPlayerActivity.this.videoPlayerFragment, AbstractVideoPlayerFragment.class.getSimpleName());
                    }
                    if (supportFragmentManager.findFragmentByTag(CastPlayerFragment.class.getSimpleName()) == null) {
                        beginTransaction.add(2131624055, VideoPlayerActivity.this.castPlayerFragment, CastPlayerFragment.class.getSimpleName());
                    }
                    if (supportFragmentManager.findFragmentByTag(ClickthroughFragment.class.getSimpleName()) == null) {
                        beginTransaction.add(2131624055, VideoPlayerActivity.this.clickthroughFragment, ClickthroughFragment.class.getSimpleName());
                    }
                    beginTransaction.commit();
                }
            }
        }
        
        public void setCurrentItem(final int mCurrentItem) {
            if (this.mAdapter != null && this.mCurrentItem != mCurrentItem) {
                if (mCurrentItem == 2) {
                    VideoPlayerActivity.this.getWindow().clearFlags(1024);
                }
                else {
                    VideoPlayerActivity.this.getWindow().addFlags(1024);
                }
                final FragmentTransaction beginTransaction = VideoPlayerActivity.this.getSupportFragmentManager().beginTransaction();
                for (int i = 0; i < this.mAdapter.getCount(); ++i) {
                    final Fragment item = this.mAdapter.getItem(i);
                    if (mCurrentItem == i) {
                        beginTransaction.show(item);
                    }
                    else {
                        beginTransaction.hide(item);
                    }
                }
                beginTransaction.commitAllowingStateLoss();
                VideoPlayerActivity.this.getSupportFragmentManager().executePendingTransactions();
                this.mCurrentItem = mCurrentItem;
            }
        }
    }
    
    private class VideoMessageReceiver extends BroadcastReceiver
    {
        private boolean episodeStartTracked;
        
        private VideoMessageReceiver() {
            this.episodeStartTracked = false;
        }
        
        private void handleVideoError(final Intent intent) {
            if (VideoPlayerActivity.this.mVideoPreparedTimerRunnable != null) {
                VideoPlayerActivity.this.mHandler.removeCallbacks(VideoPlayerActivity.this.mVideoPreparedTimerRunnable);
            }
            final int intValue = Extras.getInt(intent, "what").get();
            final int intValue2 = Extras.getInt(intent, "extra").get();
            final Uri videoViewUri = VideoPlayerActivity.this.videoPlayerFragment.getVideoViewUri();
            if (videoViewUri != null) {
                VideoPlayerActivity.this.log.error("Video error: what: %s extra: %s, extraextra: %s", intValue, intValue2, Base64.encodeToString(videoViewUri.toString().getBytes(), 0));
            }
            else {
                VideoPlayerActivity.this.log.error("Video error: what: %s extra: %s", intValue, intValue2);
            }
            final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)VideoPlayerActivity.this);
            alertDialog$Builder.setCancelable(false);
            alertDialog$Builder.setMessage((CharSequence)LocalizedStrings.ERROR_VIDEO.get());
            alertDialog$Builder.setNegativeButton((CharSequence)LocalizedStrings.BACK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    dialogInterface.dismiss();
                    VideoPlayerActivity.this.finish();
                    VideoPlayerActivity.this.overridePendingTransition(0, 0);
                }
            });
            alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.RETRY.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    VideoPlayerActivity.this.episodeInfo.getMedia().setPlayhead(VideoPlayerActivity.this.videoPlayerFragment.getPlayhead() / 1000);
                    VideoPlayerActivity.this.finish();
                    VideoPlayerActivity.this.overridePendingTransition(0, 0);
                    VideoPlayerActivity.start(VideoPlayerActivity.this, VideoPlayerActivity.this.episodeInfo, false, VideoPlayerActivity.this.appEntryFrom, true);
                }
            });
            alertDialog$Builder.show();
        }
        
        private void handleVideoFinished() {
            if (VideoPlayerActivity.this.mVideoTarget == VideoTarget.Device && VideoPlayerActivity.this.getApplicationState().getAutoplay()) {
                VideoPlayerActivity.this.next(true);
                return;
            }
            VideoPlayerActivity.this.finish();
            VideoPlayerActivity.this.overridePendingTransition(0, 0);
        }
        
        private void handleVideoStarted() {
            VideoPlayerActivity.this.mIsVideoStarted = true;
            if (VideoPlayerActivity.this.mVideoLoadTimoutDialog != null) {
                VideoPlayerActivity.this.mVideoLoadTimoutDialog.dismiss();
                VideoPlayerActivity.this.mVideoLoadTimoutDialog = null;
            }
            if (!this.episodeStartTracked) {
                Tracker.videoStartPlayback((Context)VideoPlayerActivity.this, VideoPlayerActivity.this.episodeInfo.getMedia());
                Tracker.swrvePropertyLastWatched(VideoPlayerActivity.this.episodeInfo.getMedia().getSeriesName().or(""), VideoPlayerActivity.this.episodeInfo.getMedia().getName());
                this.episodeStartTracked = true;
                LocalBroadcastManager.getInstance((Context)VideoPlayerActivity.this).sendBroadcast(new Intent("VIDEO_STARTED"));
            }
            VideoPlayerActivity.this.fragmentSwitcher.setCurrentItem(0);
        }
        
        public void onReceive(final Context context, final Intent intent) {
            if ("videoStarted".equals(intent.getAction())) {
                this.handleVideoStarted();
            }
            else {
                if ("videoErrored".equals(intent.getAction())) {
                    this.handleVideoError(intent);
                    return;
                }
                if ("videoFinished".equals(intent.getAction())) {
                    this.handleVideoFinished();
                }
            }
        }
    }
    
    private enum VideoTarget
    {
        Cast(2), 
        Device(0);
        
        private final int mFragmentIndex;
        
        private VideoTarget(final int mFragmentIndex) {
            this.mFragmentIndex = mFragmentIndex;
        }
        
        public int getFragmentIndex() {
            return this.mFragmentIndex;
        }
    }
}
