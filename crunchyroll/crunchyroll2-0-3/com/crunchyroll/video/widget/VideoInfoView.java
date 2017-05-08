// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.widget;

import com.crunchyroll.crunchyroid.util.Util;
import android.animation.Animator$AnimatorListener;
import android.animation.ObjectAnimator;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import android.animation.AnimatorSet;
import android.animation.Animator;
import android.widget.LinearLayout;

public class VideoInfoView extends LinearLayout
{
    private static final long FADE_OUT_DURATION_IN_MSEC = 500L;
    private static final long HIDE_INFO_TIMEOUT_IN_MSEC = 3000L;
    Animator ffAnimator;
    public Runnable hideInfoTask;
    AnimatorSet mAnimatorSet;
    private ImageView mForwardIcon;
    private ImageView mRewindIcon;
    private TextView mTextView;
    Animator rewAnimator;
    Animator textAnimator;
    
    public VideoInfoView(final Context context) {
        super(context);
        this.rewAnimator = null;
        this.textAnimator = null;
        this.ffAnimator = null;
        this.mAnimatorSet = null;
        this.hideInfoTask = new Runnable() {
            @Override
            public void run() {
                VideoInfoView.this.fadeOutViewCompoments();
            }
        };
        this.init();
    }
    
    public VideoInfoView(final Context context, final AttributeSet set) {
        super(context, set);
        this.rewAnimator = null;
        this.textAnimator = null;
        this.ffAnimator = null;
        this.mAnimatorSet = null;
        this.hideInfoTask = new Runnable() {
            @Override
            public void run() {
                VideoInfoView.this.fadeOutViewCompoments();
            }
        };
        this.init();
    }
    
    public VideoInfoView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.rewAnimator = null;
        this.textAnimator = null;
        this.ffAnimator = null;
        this.mAnimatorSet = null;
        this.hideInfoTask = new Runnable() {
            @Override
            public void run() {
                VideoInfoView.this.fadeOutViewCompoments();
            }
        };
        this.init();
    }
    
    private void cancelScheduledHideInfo() {
        this.removeCallbacks(this.hideInfoTask);
        if (this.mAnimatorSet != null) {
            this.mAnimatorSet.cancel();
        }
        this.mRewindIcon.setAlpha(1.0f);
        this.mTextView.setAlpha(1.0f);
        this.mForwardIcon.setAlpha(1.0f);
    }
    
    private void fadeOutViewCompoments() {
        this.rewAnimator = (Animator)ObjectAnimator.ofFloat((Object)this.mRewindIcon, "alpha", new float[] { 0.0f });
        this.textAnimator = (Animator)ObjectAnimator.ofFloat((Object)this.mTextView, "alpha", new float[] { 0.0f });
        this.ffAnimator = (Animator)ObjectAnimator.ofFloat((Object)this.mForwardIcon, "alpha", new float[] { 0.0f });
        if (this.mAnimatorSet == null) {
            this.mAnimatorSet = new AnimatorSet();
        }
        this.mAnimatorSet.addListener((Animator$AnimatorListener)new Animator$AnimatorListener() {
            public void onAnimationCancel(final Animator animator) {
            }
            
            public void onAnimationEnd(final Animator animator) {
            }
            
            public void onAnimationRepeat(final Animator animator) {
            }
            
            public void onAnimationStart(final Animator animator) {
                VideoInfoView.this.hideViewComponents();
            }
        });
        this.mAnimatorSet.setDuration(500L);
        this.mAnimatorSet.playTogether(new Animator[] { this.rewAnimator, this.textAnimator, this.ffAnimator });
        this.mAnimatorSet.start();
    }
    
    private void hideViewComponents() {
        this.mRewindIcon.setVisibility(4);
        this.mTextView.setVisibility(4);
        this.mForwardIcon.setVisibility(4);
    }
    
    private void init() {
    }
    
    private void scheduleHideInfo() {
        this.postDelayed(this.hideInfoTask, 3000L);
    }
    
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTextView = (TextView)this.findViewById(2131624261);
        this.mRewindIcon = (ImageView)this.findViewById(2131624260);
        this.mForwardIcon = (ImageView)this.findViewById(2131624262);
    }
    
    public void showForward(final int n) {
        this.cancelScheduledHideInfo();
        this.mRewindIcon.setVisibility(8);
        this.mTextView.setText((CharSequence)String.format(Util.stringFromDuration(n), new Object[0]));
        this.mTextView.setVisibility(0);
        this.mForwardIcon.setImageResource(2130837572);
        this.mForwardIcon.setVisibility(0);
        this.scheduleHideInfo();
    }
    
    public void showPause() {
        this.cancelScheduledHideInfo();
        this.mRewindIcon.setVisibility(8);
        this.mTextView.setVisibility(8);
        this.mForwardIcon.setImageResource(2130837583);
        this.mForwardIcon.setVisibility(0);
        this.scheduleHideInfo();
    }
    
    public void showPlay() {
        this.cancelScheduledHideInfo();
        this.mRewindIcon.setVisibility(8);
        this.mTextView.setVisibility(8);
        this.mForwardIcon.setImageResource(2130837593);
        this.mForwardIcon.setVisibility(0);
        this.scheduleHideInfo();
    }
    
    public void showPlayhead(final int n) {
        this.cancelScheduledHideInfo();
        this.mRewindIcon.setVisibility(8);
        this.mTextView.setText((CharSequence)new String(Util.stringFromDuration(n)));
        this.mTextView.setVisibility(0);
        this.mForwardIcon.setVisibility(8);
        this.scheduleHideInfo();
    }
    
    public void showReverse(final int n) {
        this.cancelScheduledHideInfo();
        this.mRewindIcon.setVisibility(0);
        this.mTextView.setText((CharSequence)Util.stringFromDuration(n));
        this.mTextView.setVisibility(0);
        this.mForwardIcon.setVisibility(8);
        this.scheduleHideInfo();
    }
}
