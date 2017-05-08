// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.util;

import android.widget.ListView;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.view.ViewAnimationUtils;
import android.annotation.TargetApi;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation$AnimationListener;
import android.view.animation.AnimationUtils;
import android.os.Build$VERSION;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;
import android.content.Context;

public class AnimationUtil
{
    public static void animateImageTransitionWithRotation(final Context context, final ImageView imageView, @DrawableRes final int imageResource) {
        if (Build$VERSION.SDK_INT >= 21) {
            final Animation loadAnimation = AnimationUtils.loadAnimation(context, 2130968590);
            loadAnimation.setAnimationListener((Animation$AnimationListener)new Animation$AnimationListener() {
                public void onAnimationEnd(Animation loadAnimation) {
                    imageView.setImageResource(imageResource);
                    loadAnimation = AnimationUtils.loadAnimation(context, 2130968591);
                    imageView.startAnimation(loadAnimation);
                }
                
                public void onAnimationRepeat(final Animation animation) {
                }
                
                public void onAnimationStart(final Animation animation) {
                }
            });
            imageView.startAnimation(loadAnimation);
            return;
        }
        imageView.setImageResource(imageResource);
    }
    
    @TargetApi(21)
    public static void hideViewWithCircularAnimation(final View view, final int n, final int n2) {
        hideViewWithCircularAnimation(view, n, n2, null);
    }
    
    public static void hideViewWithCircularAnimation(final View view, final int n, final int n2, final AnimatorListenerAdapter animatorListenerAdapter) {
        if (Build$VERSION.SDK_INT >= 21) {
            final Animator circularReveal = ViewAnimationUtils.createCircularReveal(view, n, n2, (float)Math.max(view.getWidth(), view.getHeight()), 0.0f);
            circularReveal.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    super.onAnimationEnd(animator);
                    view.setVisibility(4);
                    if (animatorListenerAdapter != null) {
                        animatorListenerAdapter.onAnimationEnd(animator);
                    }
                }
            });
            circularReveal.start();
        }
        else {
            view.setVisibility(4);
            if (animatorListenerAdapter != null) {
                animatorListenerAdapter.onAnimationEnd((Animator)null);
            }
        }
    }
    
    public static void revealListViewItems(final Context context, final ListView listView) {
        for (int n = 0, i = listView.getFirstVisiblePosition(); i <= listView.getLastVisiblePosition(); ++i, ++n) {
            final Animation loadAnimation = AnimationUtils.loadAnimation(context, 2130968589);
            loadAnimation.setStartOffset((long)(n * 20));
            final View child = listView.getChildAt(i);
            if (child != null) {
                child.startAnimation(loadAnimation);
            }
        }
    }
    
    @TargetApi(21)
    public static void revealViewWithCircularAnimation(final View view, final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 21) {
            final Animator circularReveal = ViewAnimationUtils.createCircularReveal(view, n, n2, 0.0f, (float)Math.max(view.getWidth(), view.getHeight()));
            view.setVisibility(0);
            circularReveal.start();
            return;
        }
        view.setVisibility(0);
    }
}
