// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.widget;

import android.graphics.drawable.Drawable;
import android.annotation.SuppressLint;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.graphics.Bitmap;
import android.widget.SeekBar$OnSeekBarChangeListener;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.view.View$OnClickListener;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.KeyEvent;
import android.view.View$OnKeyListener;
import android.util.AttributeSet;
import com.crunchyroll.android.util.LoggerFactory;
import android.content.Context;
import java.lang.ref.WeakReference;
import android.widget.SeekBar;
import android.widget.ProgressBar;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.os.Handler;
import android.widget.ImageButton;
import com.crunchyroll.android.api.models.BIFFile;
import com.crunchyroll.android.util.Logger;
import android.widget.FrameLayout;

public class CustomMediaController extends FrameLayout
{
    public static final float DISABLED_ALPHA = 0.5f;
    public static final float ENABLED_ALPHA = 1.0f;
    private static final int HIDE_UI_DELAY_MSEC = 3000;
    public static final int SKIP_INTERVAL_MSEC = 10000;
    private static final int UPDATE_UI_INTERVAL_MSEC = 250;
    private final Logger log;
    private BIFFile mBifFile;
    private ImageButton mForwardButton;
    private Handler mHandler;
    private Runnable mHideUIRunnable;
    private boolean mIsPlaying;
    protected boolean mIsSeeking;
    private boolean mIsShowing;
    private ImageButton mNextEpisodeButton;
    private View mPanel;
    private TextView mPastText;
    private ImageButton mPlayPauseButton;
    private ImageView mPreviewImg;
    private TextView mPreviewTime;
    private ProgressBar mProgressBar;
    private TextView mRemainText;
    private ImageButton mRevButton;
    private SeekBar mSeekBar;
    private OnShowHideListener mShowHideListener;
    private Runnable mUpdateUIRunnable;
    private WeakReference<AbstractVideoView> mVideoView;
    
    public CustomMediaController(final Context context) {
        super(context);
        this.log = LoggerFactory.getLogger(this.getClass());
        this.mVideoView = new WeakReference<AbstractVideoView>(null);
        this.mHandler = new Handler();
        this.init(context);
    }
    
    public CustomMediaController(final Context context, final AttributeSet set) {
        super(context, set);
        this.log = LoggerFactory.getLogger(this.getClass());
        this.mVideoView = new WeakReference<AbstractVideoView>(null);
        this.mHandler = new Handler();
        this.init(context);
    }
    
    public CustomMediaController(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.log = LoggerFactory.getLogger(this.getClass());
        this.mVideoView = new WeakReference<AbstractVideoView>(null);
        this.mHandler = new Handler();
        this.init(context);
    }
    
    private void cancelHideUITimer() {
        this.log.verbose("cancelHideUITimer", new Object[0]);
        this.mHandler.removeCallbacks(this.mHideUIRunnable);
    }
    
    private void init(final Context context) {
        this.mIsShowing = true;
        this.setOnKeyListener((View$OnKeyListener)new View$OnKeyListener() {
            public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
                if (keyEvent.getAction() == 0) {
                    switch (keyEvent.getKeyCode()) {
                        case 4: {
                            if (CustomMediaController.this.mShowHideListener != null) {
                                CustomMediaController.this.mShowHideListener.onBackPressed();
                            }
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        this.mUpdateUIRunnable = new Runnable() {
            @Override
            public void run() {
                if (CustomMediaController.this.mVideoView.get() != null) {
                    CustomMediaController.this.updatePlaybackState();
                    CustomMediaController.this.mHandler.postDelayed(CustomMediaController.this.mUpdateUIRunnable, 250L);
                }
            }
        };
        this.mHideUIRunnable = new Runnable() {
            @Override
            public void run() {
                CustomMediaController.this.hide();
            }
        };
        final View inflate = ((LayoutInflater)this.getContext().getSystemService("layout_inflater")).inflate(2130903156, (ViewGroup)this, true);
        inflate.findViewById(2131624299).setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    CustomMediaController.this.log.verbose("Tap background", new Object[0]);
                    CustomMediaController.this.togglePanelVisibility();
                }
                return true;
            }
        });
        (this.mPanel = inflate.findViewById(2131624300)).setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    CustomMediaController.this.cancelHideUITimer();
                }
                else if (motionEvent.getAction() == 1) {
                    CustomMediaController.this.show();
                }
                return false;
            }
        });
        (this.mRevButton = (ImageButton)inflate.findViewById(2131624301)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.rewindVideo(CustomMediaController.this.getContext());
                final int max = Math.max(0, ((AbstractVideoView)CustomMediaController.this.mVideoView.get()).getCurrentPosition() - 10000);
                CustomMediaController.this.log.debug("Seek to %d", max);
                ((AbstractVideoView)CustomMediaController.this.mVideoView.get()).seekTo(max);
                CustomMediaController.this.show();
            }
        });
        (this.mForwardButton = (ImageButton)inflate.findViewById(2131624161)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.forwardVideo(CustomMediaController.this.getContext());
                final int max = Math.max(0, ((AbstractVideoView)CustomMediaController.this.mVideoView.get()).getCurrentPosition() + 10000);
                CustomMediaController.this.log.debug("Seek to %d", max);
                ((AbstractVideoView)CustomMediaController.this.mVideoView.get()).seekTo(max);
                CustomMediaController.this.show();
            }
        });
        this.mProgressBar = (ProgressBar)inflate.findViewById(2131624264);
        (this.mPlayPauseButton = (ImageButton)inflate.findViewById(2131624135)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                CustomMediaController.this.togglePlayPause();
                CustomMediaController.this.show();
            }
        });
        (this.mNextEpisodeButton = (ImageButton)inflate.findViewById(2131624304)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final AbstractVideoView abstractVideoView = (AbstractVideoView)CustomMediaController.this.mVideoView.get();
                if (abstractVideoView != null) {
                    CustomMediaController.this.log.debug("Next episode", new Object[0]);
                    abstractVideoView.next();
                    CustomMediaController.this.show();
                }
            }
        });
        this.mPastText = (TextView)inflate.findViewById(2131624302);
        this.mRemainText = (TextView)inflate.findViewById(2131624303);
        (this.mSeekBar = (SeekBar)inflate.findViewById(2131624164)).setOnSeekBarChangeListener((SeekBar$OnSeekBarChangeListener)new SeekBar$OnSeekBarChangeListener() {
            public void onProgressChanged(final SeekBar seekBar, final int n, final boolean b) {
                if (CustomMediaController.this.mPreviewTime != null) {
                    if (CustomMediaController.this.isShowing()) {
                        CustomMediaController.this.mPreviewTime.setText((CharSequence)CustomMediaController.this.timeToString(n));
                        CustomMediaController.this.setPreviewTimePosition();
                    }
                    else {
                        CustomMediaController.this.mPreviewTime.setVisibility(4);
                    }
                }
                if (b) {
                    CustomMediaController.this.log.verbose("Scrub Changed", new Object[0]);
                    if (CustomMediaController.this.mBifFile != null) {
                        CustomMediaController.this.mPreviewImg.setImageBitmap((Bitmap)null);
                        final Bitmap imageAtSeconds = CustomMediaController.this.mBifFile.getImageAtSeconds(n / 1000);
                        if (imageAtSeconds != null) {
                            CustomMediaController.this.mPreviewImg.setImageBitmap(imageAtSeconds);
                            CustomMediaController.this.mPreviewImg.setVisibility(0);
                        }
                        else {
                            CustomMediaController.this.mPreviewImg.setVisibility(4);
                        }
                    }
                    CustomMediaController.this.mPastText.setText((CharSequence)CustomMediaController.this.timeToString(Math.max(n, 0)));
                    CustomMediaController.this.mRemainText.setText((CharSequence)CustomMediaController.this.timeToString(Math.max(((AbstractVideoView)CustomMediaController.this.mVideoView.get()).getDuration() - n, 0)));
                    ((AbstractVideoView)CustomMediaController.this.mVideoView.get()).seekTo(n);
                }
            }
            
            public void onStartTrackingTouch(final SeekBar seekBar) {
                CustomMediaController.this.log.verbose("Scrub Start", new Object[0]);
                CustomMediaController.this.mIsSeeking = true;
                CustomMediaController.this.cancelHideUITimer();
            }
            
            public void onStopTrackingTouch(final SeekBar seekBar) {
                CustomMediaController.this.log.verbose("Scrub Stop", new Object[0]);
                CustomMediaController.this.mIsSeeking = false;
                CustomMediaController.this.show();
            }
        });
    }
    
    private void scheduleHideUITimer() {
        this.log.verbose("scheduleHideUITimer", new Object[0]);
        this.mHandler.postDelayed(this.mHideUIRunnable, 3000L);
    }
    
    private void setPreviewTimePosition() {
        if (this.mSeekBar.getMax() > 0) {
            final int[] array = new int[2];
            this.mSeekBar.getLocationOnScreen(array);
            final RelativeLayout$LayoutParams layoutParams = new RelativeLayout$LayoutParams(-2, -2);
            layoutParams.setMargins(array[0] + (this.mSeekBar.getPaddingLeft() + this.mSeekBar.getProgress() * (this.mSeekBar.getWidth() - this.mSeekBar.getPaddingLeft() - this.mSeekBar.getPaddingRight()) / this.mSeekBar.getMax()) - this.mPreviewTime.getWidth() / 2, this.mPanel.getTop() - this.mPreviewTime.getHeight() - 40, 0, 0);
            this.mPreviewTime.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            this.mPreviewTime.setVisibility(0);
            return;
        }
        this.mPreviewTime.setVisibility(4);
    }
    
    private void startPeriodicUpdates() {
        this.log.verbose("stopPeriodicUpdates", new Object[0]);
        this.mUpdateUIRunnable.run();
    }
    
    private void stopPeriodicUpdates() {
        this.log.verbose("stopPeriodicUpdates", new Object[0]);
        this.mHandler.removeCallbacks(this.mUpdateUIRunnable);
    }
    
    @SuppressLint({ "DefaultLocale" })
    private String timeToString(int n) {
        final int n2 = n / 1000;
        n = n2 / 3600;
        final int n3 = n2 / 60 % 60;
        final int n4 = n2 % 60;
        if (n > 0) {
            return String.format("%d:%02d:%02d", n, n3, n4);
        }
        return String.format("%d:%02d", n3, n4);
    }
    
    private void togglePanelVisibility() {
        if (this.mIsShowing) {
            this.hide();
            return;
        }
        this.show();
    }
    
    private void togglePlayPause() {
        if (this.mVideoView.get().isPlaying()) {
            this.log.verbose("Pause", new Object[0]);
            this.mVideoView.get().pause();
            return;
        }
        this.log.verbose("Resume", new Object[0]);
        this.mVideoView.get().start();
    }
    
    private void updatePlaybackState() {
        final int currentPosition = this.mVideoView.get().getCurrentPosition();
        final int duration = this.mVideoView.get().getDuration();
        final String timeToString = this.timeToString(Math.max(currentPosition, 0));
        final String timeToString2 = this.timeToString(Math.max(duration - currentPosition, 0));
        this.mPastText.setText((CharSequence)timeToString);
        this.mRemainText.setText((CharSequence)timeToString2);
        this.mSeekBar.setMax(duration);
        this.mSeekBar.setSecondaryProgress((int)Math.round(this.mVideoView.get().getBufferPercentage() / 100.0 * this.mSeekBar.getMax()));
        if (!this.mIsSeeking) {
            this.mPastText.setText((CharSequence)timeToString);
            this.mRemainText.setText((CharSequence)timeToString2);
            this.mSeekBar.setProgress(currentPosition);
            final boolean playing = this.mVideoView.get().isPlaying();
            if (playing != this.mIsPlaying) {
                this.mIsPlaying = playing;
                if (!this.mIsPlaying) {
                    this.mPlayPauseButton.setImageResource(2130837593);
                    return;
                }
                this.mPlayPauseButton.setImageResource(2130837583);
            }
        }
    }
    
    public void hide() {
        this.log.verbose("Hide panel", new Object[0]);
        this.cancelHideUITimer();
        this.mIsShowing = false;
        this.mPanel.setVisibility(8);
        this.mPanel.invalidate();
        if (this.mShowHideListener != null) {
            this.mShowHideListener.onHide();
        }
        if (this.mPreviewTime != null) {
            this.mPreviewTime.setVisibility(4);
        }
    }
    
    public void hidePreview() {
        if (this.mPreviewImg != null) {
            this.mPreviewImg.setImageBitmap((Bitmap)null);
            this.mPreviewImg.setVisibility(4);
            this.mPreviewTime.setVisibility(4);
        }
    }
    
    public void hideProgress() {
        this.mProgressBar.setVisibility(4);
    }
    
    public boolean isShowing() {
        return this.mIsShowing;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.log.verbose("onAttachedToWindow", new Object[0]);
        this.startPeriodicUpdates();
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.log.verbose("onDetachedFromWindow", new Object[0]);
        this.stopPeriodicUpdates();
        this.cancelHideUITimer();
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        this.log.debug("onKeyDown", new Object[0]);
        switch (n) {
            default: {
                return false;
            }
            case 62:
            case 79:
            case 85: {
                this.togglePlayPause();
                this.show();
                return true;
            }
        }
    }
    
    public void setBIFFile(final BIFFile mBifFile) {
        this.mBifFile = mBifFile;
    }
    
    public void setCustomSeekBarDrawable(final Drawable progressDrawable) {
        if (this.mSeekBar != null) {
            this.mSeekBar.setProgressDrawable(progressDrawable);
        }
    }
    
    public void setNextEnabled(final boolean enabled) {
        if (this.mNextEpisodeButton != null) {
            this.mNextEpisodeButton.setEnabled(enabled);
            if (!enabled) {
                this.mNextEpisodeButton.setAlpha(0.5f);
                return;
            }
            this.mNextEpisodeButton.setAlpha(1.0f);
        }
    }
    
    public void setOnShowHideListener(final OnShowHideListener mShowHideListener) {
        this.mShowHideListener = mShowHideListener;
    }
    
    public void setPreviewImg(final ImageView mPreviewImg) {
        this.mPreviewImg = mPreviewImg;
    }
    
    public void setPreviewTime(final TextView mPreviewTime) {
        this.mPreviewTime = mPreviewTime;
    }
    
    public void setVideoView(final AbstractVideoView abstractVideoView) {
        this.mVideoView = new WeakReference<AbstractVideoView>(abstractVideoView);
        this.startPeriodicUpdates();
    }
    
    public void show() {
        this.log.verbose("Show panel", new Object[0]);
        this.cancelHideUITimer();
        this.mIsShowing = true;
        this.mPanel.setVisibility(0);
        this.mPanel.invalidate();
        if (this.mShowHideListener != null) {
            this.mShowHideListener.onShow();
        }
        this.scheduleHideUITimer();
        if (this.mPreviewTime != null) {
            this.mPreviewTime.setVisibility(this.mPreviewImg.getVisibility());
        }
    }
    
    public void showProgress() {
        this.mProgressBar.setVisibility(0);
    }
    
    public abstract static class OnShowHideListener
    {
        public abstract void onBackPressed();
        
        public abstract void onHide();
        
        public abstract void onShow();
    }
}
