// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.fragments;

import android.content.IntentFilter;
import com.crunchyroll.video.util.VideoUtil;
import java.util.List;
import com.crunchyroll.video.util.MediaManager;
import com.crunchyroll.cast.CastState;
import android.widget.SeekBar$OnSeekBarChangeListener;
import com.crunchyroll.video.widget.CustomProgressDrawable;
import com.crunchyroll.video.activities.VideoPlayerActivity;
import com.crunchyroll.cast.model.CastInfo;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.view.View$OnClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.crunchyroll.crunchyroid.util.Functional;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.net.Uri;
import com.crunchyroll.android.api.models.Stream;
import java.util.TimerTask;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.view.ViewGroup$LayoutParams;
import com.crunchyroll.crunchyroid.util.Util;
import android.widget.ProgressBar;
import android.widget.RelativeLayout$LayoutParams;
import com.crunchyroll.cast.CastHandler;
import com.crunchyroll.android.api.models.EpisodeInfo;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.view.ViewGroup;
import com.crunchyroll.android.util.LoggerFactory;
import android.widget.ViewSwitcher;
import java.util.Timer;
import android.widget.SeekBar;
import com.google.common.base.Optional;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.content.BroadcastReceiver;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import com.crunchyroll.android.util.Logger;

public class CastPlayerFragment extends AbstractVideoPlayerFragment
{
    private static final int PROGRESS_BAR_ID = 1;
    private static final long UPDATE_SEEK_INTERVAL_MSEC = 1000L;
    private static final long UPDATE_UI_INTERVAL_MSEC = 250L;
    private static Logger log;
    private Button adsButton;
    private ImageView mBackgroundImageView;
    private TextView mCastInfoView;
    private BroadcastReceiver mCastReceiver;
    private RelativeLayout mConnectView;
    private TextView mDescriptionView;
    private TextView mDurationTextView;
    private TextView mEpisodeNameView;
    private ImageButton mForwardButton;
    private Handler mHandler;
    private boolean mIsClickthroughClicked;
    private boolean mIsPlaying;
    private boolean mIsSeeking;
    private Optional<Long> mLastSeekPosition;
    private ImageButton mNextButton;
    private ImageButton mPlayPauseButton;
    private TextView mProgressTextView;
    private ImageButton mRewindButton;
    private SeekBar mSeekBar;
    private Timer mSeekTimer;
    private TextView mSeriesNameView;
    private ImageButton mStopButton;
    private Timer mUpdateUITimer;
    private ViewSwitcher mViewSwitcher;
    
    static {
        CastPlayerFragment.log = LoggerFactory.getLogger(CastPlayerFragment.class);
    }
    
    public CastPlayerFragment() {
        this.mIsPlaying = false;
        this.mIsSeeking = false;
        this.mIsClickthroughClicked = false;
        this.mLastSeekPosition = Optional.absent();
        this.mHandler = new Handler();
    }
    
    private void changeProgress(final int n) {
        if (this.mIsSeeking) {
            this.mProgressTextView.setText((CharSequence)this.getTimeStringFromSec(n));
        }
    }
    
    private String getSessionAuth() {
        return CrunchyrollApplication.getApp(this).getApplicationState().getSession().getAuth().or("");
    }
    
    @SuppressLint({ "DefaultLocale" })
    private String getTimeStringFromSec(final int n) {
        return String.format("%d:%02d", n / 60, n % 60);
    }
    
    private void handleVideoCompletionEvent() {
        LocalBroadcastManager.getInstance((Context)this.getActivity()).sendBroadcast(new Intent("videoFinished"));
    }
    
    private void hideConnectingMessage() {
        this.mViewSwitcher.setVisibility(0);
        if (this.mConnectView != null) {
            ((ViewGroup)this.mConnectView.getParent()).removeView((View)this.mConnectView);
            this.mConnectView = null;
        }
    }
    
    private void initCastReceiver() {
        this.mCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();
                if (action.equals("CAST_SESSION_STARTING_EVENT")) {
                    final ViewGroup viewGroup = (ViewGroup)CastPlayerFragment.this.getView();
                    if (viewGroup != null) {
                        CastPlayerFragment.this.showConnectingMessage(viewGroup);
                    }
                }
                else {
                    if (action.equals("CAST_SESSION_STARTED_EVENT")) {
                        CastPlayerFragment.this.updateDisplayedDeviceName();
                        CastPlayerFragment.this.hideConnectingMessage();
                        return;
                    }
                    if (action.equals("CAST_VIDEO_COMPLETION_EVENT")) {
                        CastPlayerFragment.this.handleVideoCompletionEvent();
                        return;
                    }
                    if (action.equals("CAST_ADS_START_EVENT")) {
                        CastPlayerFragment.this.mIsClickthroughClicked = false;
                        CastPlayerFragment.this.showAdsButton();
                        return;
                    }
                    if (action.equals("CAST_ADS_STOP_EVENT")) {
                        CastPlayerFragment.this.showMediaController();
                    }
                }
            }
        };
    }
    
    public static CastPlayerFragment newInstance(final EpisodeInfo episodeInfo, final boolean b, final boolean b2) {
        final CastPlayerFragment castPlayerFragment = new CastPlayerFragment();
        castPlayerFragment.initAndSetBundle(episodeInfo, b, b2, false);
        return castPlayerFragment;
    }
    
    private void seekToSeekBarPosition() {
        final long n = this.mSeekBar.getProgress() * 1000;
        if (!this.mLastSeekPosition.equals(Optional.of(n))) {
            if (CastHandler.get() != null) {
                CastHandler.get().seekTo(n);
            }
            this.mLastSeekPosition = Optional.of(n);
        }
    }
    
    private void showAdsButton() {
        this.mViewSwitcher.setDisplayedChild(1);
        final Button button = (Button)this.getView().findViewById(2131624166);
        if (CastHandler.get() != null) {
            if (!CastHandler.get().getClickthroughUrl().isPresent()) {
                CastPlayerFragment.log.debug("Url not present - hide click-through button", new Object[0]);
                button.setVisibility(4);
                return;
            }
            CastPlayerFragment.log.debug("Url present - show click-through button", new Object[0]);
            button.setVisibility(0);
        }
    }
    
    private void showConnectingMessage(final ViewGroup viewGroup) {
        if (CastHandler.get() != null && CastHandler.get().getDeviceName() == null) {
            throw new NullPointerException("Cast device name should not be null");
        }
        this.mViewSwitcher.setVisibility(4);
        (this.mConnectView = new RelativeLayout((Context)this.getActivity())).setBackgroundColor(this.getResources().getColor(2131558424));
        this.mConnectView.setGravity(17);
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(-1, -1);
        final ProgressBar progressBar = new ProgressBar((Context)this.getActivity());
        progressBar.setIndeterminate(true);
        progressBar.setId(1);
        final int dpToPx = Util.dpToPx((Context)this.getActivity(), 40);
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams2 = new RelativeLayout$LayoutParams(dpToPx, dpToPx);
        relativeLayout$LayoutParams2.addRule(13);
        this.mConnectView.addView((View)progressBar, (ViewGroup$LayoutParams)relativeLayout$LayoutParams2);
        final TextView textView = new TextView((Context)this.getActivity());
        final LocalizedStrings connecting_TO_W_TEXT = LocalizedStrings.CONNECTING_TO_W_TEXT;
        String deviceName;
        if (CastHandler.get() == null) {
            deviceName = "";
        }
        else {
            deviceName = CastHandler.get().getDeviceName();
        }
        textView.setText((CharSequence)connecting_TO_W_TEXT.get(deviceName));
        textView.setTextColor(this.getResources().getColor(17170443));
        textView.setTextSize(2, 16.0f);
        textView.setPadding(10, 0, 0, 0);
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams3 = new RelativeLayout$LayoutParams(-2, -2);
        relativeLayout$LayoutParams3.addRule(15);
        relativeLayout$LayoutParams3.addRule(1, progressBar.getId());
        this.mConnectView.addView((View)textView, (ViewGroup$LayoutParams)relativeLayout$LayoutParams3);
        viewGroup.addView((View)this.mConnectView, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
    }
    
    private void showMediaController() {
        this.mViewSwitcher.setDisplayedChild(0);
    }
    
    private void startTrackingTouch() {
        this.mIsSeeking = true;
        (this.mSeekTimer = new Timer()).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                CastPlayerFragment.this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        CastPlayerFragment.this.seekToSeekBarPosition();
                    }
                });
            }
        }, 0L, 1000L);
    }
    
    private void startUpdateUITimer() {
        (this.mUpdateUITimer = new Timer()).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                CastPlayerFragment.this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        CastPlayerFragment.this.updateUI();
                    }
                });
            }
        }, 0L, 250L);
    }
    
    private void stopTrackingTouch() {
        this.seekToSeekBarPosition();
        this.mLastSeekPosition = Optional.absent();
        this.mIsSeeking = false;
        this.mSeekTimer.cancel();
    }
    
    private void stopUpdateUITimer() {
        if (this.mUpdateUITimer != null) {
            this.mUpdateUITimer.cancel();
            this.mUpdateUITimer = null;
        }
    }
    
    private void updateDisplayedDeviceName() {
        final String value = LocalizedStrings.CASTING_TO_W_TEXT.get();
        String deviceName;
        if (CastHandler.get() == null) {
            deviceName = "";
        }
        else {
            deviceName = CastHandler.get().getDeviceName();
        }
        this.mCastInfoView.setText((CharSequence)String.format(value, deviceName));
    }
    
    private void updateUI() {
        if (CastHandler.get() != null) {
            final int max = (int)(CastHandler.get().getDuration() / 1000L);
            final int progress = (int)(CastHandler.get().getPlayhead() / 1000L);
            final boolean playing = CastHandler.get().isPlaying();
            if (!CastHandler.get().isPlayingAd()) {
                this.mProgressTextView.setText((CharSequence)this.getTimeStringFromSec(progress));
                this.mDurationTextView.setText((CharSequence)this.getTimeStringFromSec(max));
            }
            if (!this.mIsSeeking) {
                this.mSeekBar.setProgress(progress);
                this.mSeekBar.setMax(max);
            }
            if (playing != this.mIsPlaying) {
                this.mIsPlaying = playing;
                if (!playing) {
                    this.mPlayPauseButton.setImageResource(2130837730);
                    return;
                }
                this.mPlayPauseButton.setImageResource(2130837729);
            }
        }
    }
    
    @Override
    public void disableNext() {
    }
    
    @Override
    public Stream getActiveStream() {
        return null;
    }
    
    @Override
    public int getLastKnownPlayhead() {
        return this.getPlayhead();
    }
    
    @Override
    public int getPlayhead() {
        if (CastHandler.get() == null) {
            return 0;
        }
        return (int)(CastHandler.get().getPlayhead() / 1000L);
    }
    
    @Override
    public Stream getPreferredStream() {
        return null;
    }
    
    @Override
    public Uri getVideoViewUri() {
        return null;
    }
    
    @Override
    public boolean isPlaying() {
        return this.mAutoPlayback || (CastHandler.get() != null && CastHandler.get().isPlaying());
    }
    
    @Override
    public void onAdFinish() {
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final LinearLayout linearLayout = (LinearLayout)layoutInflater.inflate(2130903113, viewGroup, false);
        final Toolbar supportActionBar = (Toolbar)linearLayout.findViewById(2131624056);
        supportActionBar.setTitle("");
        ((AppCompatActivity)this.getActivity()).setSupportActionBar(supportActionBar);
        ((AppCompatActivity)this.getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mBackgroundImageView = (ImageView)linearLayout.findViewById(2131624151);
        this.mViewSwitcher = (ViewSwitcher)linearLayout.findViewById(2131624155);
        final String s = Functional.ImageSet.getFWideUrl(this.mEpisodeInfo.getMedia().getScreenshotImage()).orNull();
        if (s != null) {
            ImageLoader.getInstance().displayImage(s, this.mBackgroundImageView);
        }
        (this.mSeriesNameView = (TextView)linearLayout.findViewById(2131624152)).setText((CharSequence)this.mEpisodeInfo.getMedia().getSeriesName().or(""));
        (this.mEpisodeNameView = (TextView)linearLayout.findViewById(2131624153)).setText((CharSequence)Functional.Media.getEpisodeSubtitle(this.mEpisodeInfo.getMedia()));
        (this.mDescriptionView = (TextView)linearLayout.findViewById(2131624154)).setText((CharSequence)this.mEpisodeInfo.getMedia().getDescription());
        this.mCastInfoView = (TextView)linearLayout.findViewById(2131624150);
        (this.mPlayPauseButton = (ImageButton)linearLayout.findViewById(2131624160)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (CastHandler.get() != null) {
                    if (CastHandler.get().isPlaying()) {
                        CastHandler.get().pause();
                    }
                    else {
                        CastHandler.get().resume();
                    }
                    CastPlayerFragment.this.updateUI();
                }
            }
        });
        (this.mStopButton = (ImageButton)linearLayout.findViewById(2131624159)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                CastPlayerFragment.this.stop();
                CastPlayerFragment.this.getActivity().finish();
            }
        });
        (this.mRewindButton = (ImageButton)linearLayout.findViewById(2131624158)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.rewindChromecast((Context)CastPlayerFragment.this.getActivity(), CastPlayerFragment.this.mEpisodeInfo.getMedia().getSeriesName().or(""), "episode-" + CastPlayerFragment.this.mEpisodeInfo.getMedia().getEpisodeNumber());
                CastHandler.get().seekTo(Math.max(0L, CastHandler.get().getPlayhead() - 10000L));
            }
        });
        (this.mForwardButton = (ImageButton)linearLayout.findViewById(2131624161)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.fastForwardChromecast((Context)CastPlayerFragment.this.getActivity(), CastPlayerFragment.this.mEpisodeInfo.getMedia().getSeriesName().or(""), "episode-" + CastPlayerFragment.this.mEpisodeInfo.getMedia().getEpisodeNumber());
                CastHandler.get().seekTo(Math.min(CastHandler.get().getCurrentCastInfo().get().getDuration() * 1000 - 2000, CastHandler.get().getPlayhead() + 10000L));
            }
        });
        (this.mNextButton = (ImageButton)linearLayout.findViewById(2131624162)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.playNextChromecast((Context)CastPlayerFragment.this.getActivity(), CastPlayerFragment.this.mEpisodeInfo.getMedia());
                ((VideoPlayerActivity)CastPlayerFragment.this.getActivity()).next(false);
            }
        });
        this.mProgressTextView = (TextView)linearLayout.findViewById(2131624163);
        this.mDurationTextView = (TextView)linearLayout.findViewById(2131624165);
        this.mSeekBar = (SeekBar)linearLayout.findViewById(2131624164);
        List<Double> adsPlayHeads = null;
        if (this.mShowAds) {
            adsPlayHeads = this.mEpisodeInfo.getAdsPlayHeads();
        }
        this.mSeekBar.setProgressDrawable(CustomProgressDrawable.createDrawable((Context)this.getActivity(), adsPlayHeads, this.mEpisodeInfo.getMedia().getDuration().get()));
        this.mSeekBar.setOnSeekBarChangeListener((SeekBar$OnSeekBarChangeListener)new SeekBar$OnSeekBarChangeListener() {
            public void onProgressChanged(final SeekBar seekBar, final int n, final boolean b) {
                CastPlayerFragment.this.changeProgress(n);
            }
            
            public void onStartTrackingTouch(final SeekBar seekBar) {
                CastPlayerFragment.this.startTrackingTouch();
            }
            
            public void onStopTrackingTouch(final SeekBar seekBar) {
                CastPlayerFragment.this.stopTrackingTouch();
            }
        });
        this.initCastReceiver();
        (this.adsButton = (Button)linearLayout.findViewById(2131624166)).setText((CharSequence)LocalizedStrings.MORE_ADS_INFO.get());
        this.adsButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (CastHandler.get() != null) {
                    final Optional<String> clickthroughUrl = CastHandler.get().getClickthroughUrl();
                    if (clickthroughUrl.isPresent() && CastPlayerFragment.this.mClickthroughListener != null) {
                        if (!CastPlayerFragment.this.mIsClickthroughClicked) {
                            CastPlayerFragment.this.mIsClickthroughClicked = true;
                            CastHandler.get().trackClickthrough();
                        }
                        CastPlayerFragment.this.mClickthroughListener.onClickthrough(clickthroughUrl.get());
                    }
                }
            }
        });
        if (CastHandler.get() != null && CastHandler.get().getState() == CastState.CONNECTING) {
            this.showConnectingMessage((ViewGroup)linearLayout);
        }
        if (!MediaManager.getInstance().hasMoreMedia()) {
            this.disableNext();
        }
        return (View)linearLayout;
    }
    
    @Override
    public void onPause() {
        this.stopUpdateUITimer();
        super.onPause();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (CastHandler.get() != null) {
            final CastState state = CastHandler.get().getState();
            if (state == CastState.CONNECTING || state == CastState.CONNECTED) {
                this.updateUI();
                if (this.mAutoPlayback) {
                    this.mAutoPlayback = false;
                    final CastHandler value = CastHandler.get();
                    final EpisodeInfo mEpisodeInfo = this.mEpisodeInfo;
                    final String value2 = LocalizedStrings.CASTING_TO_W_TEXT.get();
                    String deviceName;
                    if (CastHandler.get() == null) {
                        deviceName = "";
                    }
                    else {
                        deviceName = CastHandler.get().getDeviceName();
                    }
                    value.playMedia(VideoUtil.toCastInfo(VideoPlayerActivity.class, mEpisodeInfo, String.format(value2, deviceName)), this.getSessionAuth());
                    CrunchyrollApplication.getApp((Context)this.getActivity()).setCurrentlyCastingEpisode(Optional.of(this.mEpisodeInfo));
                }
                this.updateDisplayedDeviceName();
                if (CastHandler.get().isPlayingAd()) {
                    this.showAdsButton();
                }
                else {
                    this.showMediaController();
                }
            }
        }
        this.startUpdateUITimer();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        Tracker.trackView(this);
        final IntentFilter intentFilter = new IntentFilter(CastHandler.INTENT_FILTER);
        intentFilter.addAction("CAST_SESSION_STARTING_EVENT");
        intentFilter.addAction("CAST_SESSION_STARTED_EVENT");
        LocalBroadcastManager.getInstance((Context)this.getActivity()).registerReceiver(this.mCastReceiver, intentFilter);
    }
    
    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance((Context)this.getActivity()).unregisterReceiver(this.mCastReceiver);
        super.onStop();
    }
    
    @Override
    public void pause() {
        if (CastHandler.get() != null) {
            CastHandler.get().pause();
        }
    }
    
    @Override
    public void start() {
        if (CastHandler.get() != null) {
            CastHandler.get().resume();
        }
    }
    
    @Override
    public void startFrom(final int n) {
        if (CastHandler.get() != null) {
            CastPlayerFragment.log.debug("Start from: %d sec", n);
            if (CastHandler.get().isPlayingEpisode(this.mEpisodeInfo.getMedia().getMediaId())) {
                CastPlayerFragment.log.debug("Already playing episode, do noting", new Object[0]);
                return;
            }
            CastPlayerFragment.log.debug("CastService not playing media, calling playMedia()", new Object[0]);
            final CastHandler value = CastHandler.get();
            final EpisodeInfo mEpisodeInfo = this.mEpisodeInfo;
            final String value2 = LocalizedStrings.CASTING_TO_W_TEXT.get();
            String deviceName;
            if (CastHandler.get() == null) {
                deviceName = "";
            }
            else {
                deviceName = CastHandler.get().getDeviceName();
            }
            value.playMedia(VideoUtil.toCastInfo(VideoPlayerActivity.class, mEpisodeInfo, String.format(value2, deviceName)), n, this.getSessionAuth());
            CrunchyrollApplication.getApp((Context)this.getActivity()).setCurrentlyCastingEpisode(Optional.of(this.mEpisodeInfo));
        }
    }
    
    public void stop() {
        if (CastHandler.get() != null) {
            CastHandler.get().stop();
        }
    }
}
