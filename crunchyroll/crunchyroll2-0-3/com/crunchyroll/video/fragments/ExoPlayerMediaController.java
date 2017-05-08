// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.fragments;

import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.annotation.SuppressLint;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.widget.SeekBar$OnSeekBarChangeListener;
import java.util.Iterator;
import com.crunchyroll.exoplayer.VideoControllerListener;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.view.View$OnClickListener;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.KeyEvent;
import android.view.View$OnKeyListener;
import java.util.List;
import android.util.AttributeSet;
import com.crunchyroll.android.util.LoggerFactory;
import android.content.Context;
import android.widget.SeekBar;
import android.widget.ProgressBar;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.os.Handler;
import android.widget.ImageButton;
import com.crunchyroll.android.api.models.BIFFile;
import com.crunchyroll.android.util.Logger;
import com.crunchyroll.exoplayer.VideoController;

public class ExoPlayerMediaController extends VideoController
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
    
    public ExoPlayerMediaController(final Context context) {
        super(context);
        this.log = LoggerFactory.getLogger(this.getClass());
        this.mHandler = new Handler();
        this.init(context);
    }
    
    public ExoPlayerMediaController(final Context context, final AttributeSet set) {
        super(context, set);
        this.log = LoggerFactory.getLogger(this.getClass());
        this.mHandler = new Handler();
        this.init(context);
    }
    
    public ExoPlayerMediaController(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.log = LoggerFactory.getLogger(this.getClass());
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
                            if (ExoPlayerMediaController.this.mShowHideListener != null) {
                                ExoPlayerMediaController.this.mShowHideListener.onBackPressed();
                            }
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        this.mHideUIRunnable = new Runnable() {
            @Override
            public void run() {
                ExoPlayerMediaController.this.hide();
            }
        };
        final View inflate = ((LayoutInflater)this.getContext().getSystemService("layout_inflater")).inflate(2130903156, (ViewGroup)this, true);
        inflate.findViewById(2131624299).setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    ExoPlayerMediaController.this.log.verbose("Tap background", new Object[0]);
                    ExoPlayerMediaController.this.togglePanelVisibility();
                }
                return true;
            }
        });
        (this.mPanel = inflate.findViewById(2131624300)).setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    ExoPlayerMediaController.this.cancelHideUITimer();
                }
                else if (motionEvent.getAction() == 1) {
                    ExoPlayerMediaController.this.show();
                }
                return false;
            }
        });
        this.mProgressBar = (ProgressBar)inflate.findViewById(2131624264);
        (this.mRevButton = (ImageButton)inflate.findViewById(2131624301)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.rewindVideo(ExoPlayerMediaController.this.getContext());
                ExoPlayerMediaController.this.log.debug("Seek to %d", Math.max(0, ExoPlayerMediaController.this.mSeekBar.getProgress() - 10000));
                final Iterator<VideoControllerListener> iterator = ExoPlayerMediaController.this.mControllerListeners.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onRewindPressed(10000);
                }
                ExoPlayerMediaController.this.show();
            }
        });
        (this.mForwardButton = (ImageButton)inflate.findViewById(2131624161)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.forwardVideo(ExoPlayerMediaController.this.getContext());
                ExoPlayerMediaController.this.log.debug("Seek to %d", Math.max(0, ExoPlayerMediaController.this.mSeekBar.getProgress() + 10000));
                final Iterator<VideoControllerListener> iterator = ExoPlayerMediaController.this.mControllerListeners.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onFastForwardPressed(10000);
                }
                ExoPlayerMediaController.this.show();
            }
        });
        (this.mPlayPauseButton = (ImageButton)inflate.findViewById(2131624135)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final Iterator<VideoControllerListener> iterator = ExoPlayerMediaController.this.mControllerListeners.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onPlayPausePressed();
                }
                ExoPlayerMediaController.this.show();
            }
        });
        (this.mNextEpisodeButton = (ImageButton)inflate.findViewById(2131624304)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final Iterator<VideoControllerListener> iterator = ExoPlayerMediaController.this.mControllerListeners.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onNextPressed();
                }
            }
        });
        this.mPastText = (TextView)inflate.findViewById(2131624302);
        this.mRemainText = (TextView)inflate.findViewById(2131624303);
        (this.mSeekBar = (SeekBar)inflate.findViewById(2131624164)).setOnSeekBarChangeListener((SeekBar$OnSeekBarChangeListener)this);
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
    
    @Override
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
    
    @Override
    public boolean isShowing() {
        return this.mIsShowing;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.log.verbose("onAttachedToWindow", new Object[0]);
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.log.verbose("onDetachedFromWindow", new Object[0]);
        this.cancelHideUITimer();
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        this.log.debug("onKeyDown", new Object[0]);
        switch (n) {
            default: {
                return false;
            }
            case 79:
            case 85: {
                this.show();
                return true;
            }
        }
    }
    
    public void onProgressChanged(final SeekBar seekBar, final int n, final boolean b) {
        final Iterator<VideoControllerListener> iterator = this.mControllerListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onSeekProgressChanged(this.mSeekBar.getProgress());
        }
        if (this.mPreviewTime != null) {
            if (this.isShowing()) {
                this.mPreviewTime.setText((CharSequence)this.timeToString(n));
                this.setPreviewTimePosition();
            }
            else {
                this.mPreviewTime.setVisibility(4);
            }
        }
        if (b) {
            this.log.verbose("Scrub Changed", new Object[0]);
            if (this.mBifFile != null) {
                this.mPreviewImg.setImageBitmap((Bitmap)null);
                final Bitmap imageAtSeconds = this.mBifFile.getImageAtSeconds(n / 1000);
                if (imageAtSeconds != null) {
                    this.mPreviewImg.setImageBitmap(imageAtSeconds);
                    this.mPreviewImg.setVisibility(0);
                }
                else {
                    this.mPreviewImg.setVisibility(4);
                }
            }
            final Iterator<VideoControllerListener> iterator2 = this.mControllerListeners.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().onSeekProgressChanged(n);
            }
        }
        this.mPastText.setText((CharSequence)this.timeToString(Math.max(n, 0)));
        this.mRemainText.setText((CharSequence)this.timeToString(Math.max(this.mSeekBar.getMax() - n, 0)));
    }
    
    public void onStartTrackingTouch(final SeekBar seekBar) {
        final Iterator<VideoControllerListener> iterator = this.mControllerListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onStartSeekScrub();
        }
        this.log.verbose("Scrub Start", new Object[0]);
        this.mIsSeeking = true;
        this.cancelHideUITimer();
    }
    
    public void onStopTrackingTouch(final SeekBar seekBar) {
        final Iterator<VideoControllerListener> iterator = this.mControllerListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onStopSeekScrub(this.mSeekBar.getProgress());
        }
        this.hidePreview();
        this.log.verbose("Scrub Stop", new Object[0]);
        this.mIsSeeking = false;
        this.show();
    }
    
    public void playPause() {
        final Iterator<VideoControllerListener> iterator = this.mControllerListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onPlayPausePressed();
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
    
    @Override
    public void setSeekPosition(final long n) {
        if (!this.mIsSeeking) {
            this.mSeekBar.setProgress((int)n);
        }
    }
    
    @Override
    public void setVideoDuration(final long n) {
        this.mSeekBar.setMax((int)n);
    }
    
    @Override
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
    
    @Override
    public void showPauseButton() {
        this.mPlayPauseButton.setImageResource(2130837583);
    }
    
    @Override
    public void showPlayButton() {
        this.mPlayPauseButton.setImageResource(2130837593);
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
