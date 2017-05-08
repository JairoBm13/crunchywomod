// 
// Decompiled by Procyon v0.5.30
// 

package com.nineoldandroids.animation;

import android.os.Message;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.util.AndroidRuntimeException;
import android.os.Looper;
import android.view.animation.AccelerateDecelerateInterpolator;
import java.util.HashMap;
import android.view.animation.Interpolator;
import java.util.ArrayList;

public class ValueAnimator extends Animator
{
    private static ThreadLocal<AnimationHandler> sAnimationHandler;
    private static final ThreadLocal<ArrayList<ValueAnimator>> sAnimations;
    private static final Interpolator sDefaultInterpolator;
    private static final ThreadLocal<ArrayList<ValueAnimator>> sDelayedAnims;
    private static final ThreadLocal<ArrayList<ValueAnimator>> sEndingAnims;
    private static final TypeEvaluator sFloatEvaluator;
    private static long sFrameDelay;
    private static final TypeEvaluator sIntEvaluator;
    private static final ThreadLocal<ArrayList<ValueAnimator>> sPendingAnimations;
    private static final ThreadLocal<ArrayList<ValueAnimator>> sReadyAnims;
    private float mCurrentFraction;
    private int mCurrentIteration;
    private long mDelayStartTime;
    private long mDuration;
    boolean mInitialized;
    private Interpolator mInterpolator;
    private boolean mPlayingBackwards;
    int mPlayingState;
    private int mRepeatCount;
    private int mRepeatMode;
    private boolean mRunning;
    long mSeekTime;
    private long mStartDelay;
    long mStartTime;
    private boolean mStarted;
    private boolean mStartedDelay;
    private ArrayList<AnimatorUpdateListener> mUpdateListeners;
    PropertyValuesHolder[] mValues;
    HashMap<String, PropertyValuesHolder> mValuesMap;
    
    static {
        ValueAnimator.sAnimationHandler = new ThreadLocal<AnimationHandler>();
        sAnimations = new ThreadLocal<ArrayList<ValueAnimator>>() {
            @Override
            protected ArrayList<ValueAnimator> initialValue() {
                return new ArrayList<ValueAnimator>();
            }
        };
        sPendingAnimations = new ThreadLocal<ArrayList<ValueAnimator>>() {
            @Override
            protected ArrayList<ValueAnimator> initialValue() {
                return new ArrayList<ValueAnimator>();
            }
        };
        sDelayedAnims = new ThreadLocal<ArrayList<ValueAnimator>>() {
            @Override
            protected ArrayList<ValueAnimator> initialValue() {
                return new ArrayList<ValueAnimator>();
            }
        };
        sEndingAnims = new ThreadLocal<ArrayList<ValueAnimator>>() {
            @Override
            protected ArrayList<ValueAnimator> initialValue() {
                return new ArrayList<ValueAnimator>();
            }
        };
        sReadyAnims = new ThreadLocal<ArrayList<ValueAnimator>>() {
            @Override
            protected ArrayList<ValueAnimator> initialValue() {
                return new ArrayList<ValueAnimator>();
            }
        };
        sDefaultInterpolator = (Interpolator)new AccelerateDecelerateInterpolator();
        sIntEvaluator = new IntEvaluator();
        sFloatEvaluator = new FloatEvaluator();
        ValueAnimator.sFrameDelay = 10L;
    }
    
    public ValueAnimator() {
        this.mSeekTime = -1L;
        this.mPlayingBackwards = false;
        this.mCurrentIteration = 0;
        this.mCurrentFraction = 0.0f;
        this.mStartedDelay = false;
        this.mPlayingState = 0;
        this.mRunning = false;
        this.mStarted = false;
        this.mInitialized = false;
        this.mDuration = 300L;
        this.mStartDelay = 0L;
        this.mRepeatCount = 0;
        this.mRepeatMode = 1;
        this.mInterpolator = ValueAnimator.sDefaultInterpolator;
        this.mUpdateListeners = null;
    }
    
    private boolean delayedAnimationFrame(final long mDelayStartTime) {
        if (!this.mStartedDelay) {
            this.mStartedDelay = true;
            this.mDelayStartTime = mDelayStartTime;
        }
        else {
            final long n = mDelayStartTime - this.mDelayStartTime;
            if (n > this.mStartDelay) {
                this.mStartTime = mDelayStartTime - (n - this.mStartDelay);
                this.mPlayingState = 1;
                return true;
            }
        }
        return false;
    }
    
    private void endAnimation() {
        ValueAnimator.sAnimations.get().remove(this);
        ValueAnimator.sPendingAnimations.get().remove(this);
        ValueAnimator.sDelayedAnims.get().remove(this);
        this.mPlayingState = 0;
        if (this.mRunning && this.mListeners != null) {
            final ArrayList list = (ArrayList)this.mListeners.clone();
            for (int size = list.size(), i = 0; i < size; ++i) {
                list.get(i).onAnimationEnd(this);
            }
        }
        this.mRunning = false;
        this.mStarted = false;
    }
    
    public static ValueAnimator ofFloat(final float... floatValues) {
        final ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(floatValues);
        return valueAnimator;
    }
    
    private void start(final boolean mPlayingBackwards) {
        if (Looper.myLooper() == null) {
            throw new AndroidRuntimeException("Animators may only be run on Looper threads");
        }
        this.mPlayingBackwards = mPlayingBackwards;
        this.mCurrentIteration = 0;
        this.mPlayingState = 0;
        this.mStarted = true;
        this.mStartedDelay = false;
        ValueAnimator.sPendingAnimations.get().add(this);
        if (this.mStartDelay == 0L) {
            this.setCurrentPlayTime(this.getCurrentPlayTime());
            this.mPlayingState = 0;
            this.mRunning = true;
            if (this.mListeners != null) {
                final ArrayList list = (ArrayList)this.mListeners.clone();
                for (int size = list.size(), i = 0; i < size; ++i) {
                    list.get(i).onAnimationStart(this);
                }
            }
        }
        AnimationHandler animationHandler;
        if ((animationHandler = ValueAnimator.sAnimationHandler.get()) == null) {
            animationHandler = new AnimationHandler();
            ValueAnimator.sAnimationHandler.set(animationHandler);
        }
        animationHandler.sendEmptyMessage(0);
    }
    
    private void startAnimation() {
        this.initAnimation();
        ValueAnimator.sAnimations.get().add(this);
        if (this.mStartDelay > 0L && this.mListeners != null) {
            final ArrayList list = (ArrayList)this.mListeners.clone();
            for (int size = list.size(), i = 0; i < size; ++i) {
                list.get(i).onAnimationStart(this);
            }
        }
    }
    
    void animateValue(float interpolation) {
        interpolation = this.mInterpolator.getInterpolation(interpolation);
        this.mCurrentFraction = interpolation;
        for (int length = this.mValues.length, i = 0; i < length; ++i) {
            this.mValues[i].calculateValue(interpolation);
        }
        if (this.mUpdateListeners != null) {
            for (int size = this.mUpdateListeners.size(), j = 0; j < size; ++j) {
                this.mUpdateListeners.get(j).onAnimationUpdate(this);
            }
        }
    }
    
    boolean animationFrame(final long mStartTime) {
        final boolean b = false;
        if (this.mPlayingState == 0) {
            this.mPlayingState = 1;
            if (this.mSeekTime < 0L) {
                this.mStartTime = mStartTime;
            }
            else {
                this.mStartTime = mStartTime - this.mSeekTime;
                this.mSeekTime = -1L;
            }
        }
        switch (this.mPlayingState) {
            default: {
                return false;
            }
            case 1:
            case 2: {
                float n;
                if (this.mDuration > 0L) {
                    n = (mStartTime - this.mStartTime) / this.mDuration;
                }
                else {
                    n = 1.0f;
                }
                boolean b2 = b;
                float min = n;
                if (n >= 1.0f) {
                    if (this.mCurrentIteration < this.mRepeatCount || this.mRepeatCount == -1) {
                        if (this.mListeners != null) {
                            for (int size = this.mListeners.size(), i = 0; i < size; ++i) {
                                ((AnimatorListener)this.mListeners.get(i)).onAnimationRepeat(this);
                            }
                        }
                        if (this.mRepeatMode == 2) {
                            this.mPlayingBackwards = !this.mPlayingBackwards;
                        }
                        this.mCurrentIteration += (int)n;
                        min = n % 1.0f;
                        this.mStartTime += this.mDuration;
                        b2 = b;
                    }
                    else {
                        b2 = true;
                        min = Math.min(n, 1.0f);
                    }
                }
                float n2 = min;
                if (this.mPlayingBackwards) {
                    n2 = 1.0f - min;
                }
                this.animateValue(n2);
                return b2;
            }
        }
    }
    
    @Override
    public ValueAnimator clone() {
        final ValueAnimator valueAnimator = (ValueAnimator)super.clone();
        if (this.mUpdateListeners != null) {
            final ArrayList<AnimatorUpdateListener> mUpdateListeners = this.mUpdateListeners;
            valueAnimator.mUpdateListeners = new ArrayList<AnimatorUpdateListener>();
            for (int size = mUpdateListeners.size(), i = 0; i < size; ++i) {
                valueAnimator.mUpdateListeners.add(mUpdateListeners.get(i));
            }
        }
        valueAnimator.mSeekTime = -1L;
        valueAnimator.mPlayingBackwards = false;
        valueAnimator.mCurrentIteration = 0;
        valueAnimator.mInitialized = false;
        valueAnimator.mPlayingState = 0;
        valueAnimator.mStartedDelay = false;
        final PropertyValuesHolder[] mValues = this.mValues;
        if (mValues != null) {
            final int length = mValues.length;
            valueAnimator.mValues = new PropertyValuesHolder[length];
            valueAnimator.mValuesMap = new HashMap<String, PropertyValuesHolder>(length);
            for (int j = 0; j < length; ++j) {
                final PropertyValuesHolder clone = mValues[j].clone();
                valueAnimator.mValues[j] = clone;
                valueAnimator.mValuesMap.put(clone.getPropertyName(), clone);
            }
        }
        return valueAnimator;
    }
    
    public long getCurrentPlayTime() {
        if (!this.mInitialized || this.mPlayingState == 0) {
            return 0L;
        }
        return AnimationUtils.currentAnimationTimeMillis() - this.mStartTime;
    }
    
    void initAnimation() {
        if (!this.mInitialized) {
            for (int length = this.mValues.length, i = 0; i < length; ++i) {
                this.mValues[i].init();
            }
            this.mInitialized = true;
        }
    }
    
    public void setCurrentPlayTime(final long mSeekTime) {
        this.initAnimation();
        final long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
        if (this.mPlayingState != 1) {
            this.mSeekTime = mSeekTime;
            this.mPlayingState = 2;
        }
        this.mStartTime = currentAnimationTimeMillis - mSeekTime;
        this.animationFrame(currentAnimationTimeMillis);
    }
    
    public ValueAnimator setDuration(final long mDuration) {
        if (mDuration < 0L) {
            throw new IllegalArgumentException("Animators cannot have negative duration: " + mDuration);
        }
        this.mDuration = mDuration;
        return this;
    }
    
    public void setFloatValues(final float... floatValues) {
        if (floatValues == null || floatValues.length == 0) {
            return;
        }
        if (this.mValues == null || this.mValues.length == 0) {
            this.setValues(PropertyValuesHolder.ofFloat("", floatValues));
        }
        else {
            this.mValues[0].setFloatValues(floatValues);
        }
        this.mInitialized = false;
    }
    
    public void setValues(final PropertyValuesHolder... mValues) {
        final int length = mValues.length;
        this.mValues = mValues;
        this.mValuesMap = new HashMap<String, PropertyValuesHolder>(length);
        for (int i = 0; i < length; ++i) {
            final PropertyValuesHolder propertyValuesHolder = mValues[i];
            this.mValuesMap.put(propertyValuesHolder.getPropertyName(), propertyValuesHolder);
        }
        this.mInitialized = false;
    }
    
    @Override
    public void start() {
        this.start(false);
    }
    
    @Override
    public String toString() {
        String string2;
        String string = string2 = "ValueAnimator@" + Integer.toHexString(this.hashCode());
        if (this.mValues != null) {
            int n = 0;
            while (true) {
                string2 = string;
                if (n >= this.mValues.length) {
                    break;
                }
                string = string + "\n    " + this.mValues[n].toString();
                ++n;
            }
        }
        return string2;
    }
    
    private static class AnimationHandler extends Handler
    {
        public void handleMessage(final Message message) {
            int n = 1;
            int n2 = 1;
            final ArrayList<ValueAnimator> list = ValueAnimator.sAnimations.get();
            final ArrayList<ValueAnimator> list2 = ValueAnimator.sDelayedAnims.get();
            Label_0162: {
                switch (message.what) {
                    case 0: {
                        final ArrayList list3 = ValueAnimator.sPendingAnimations.get();
                        if (list.size() > 0 || list2.size() > 0) {
                            n2 = 0;
                        }
                        while (true) {
                            n = n2;
                            if (list3.size() <= 0) {
                                break Label_0162;
                            }
                            final ArrayList list4 = (ArrayList)list3.clone();
                            list3.clear();
                            for (int size = list4.size(), i = 0; i < size; ++i) {
                                final ValueAnimator valueAnimator = list4.get(i);
                                if (valueAnimator.mStartDelay == 0L) {
                                    valueAnimator.startAnimation();
                                }
                                else {
                                    list2.add(valueAnimator);
                                }
                            }
                        }
                        break;
                    }
                    case 1: {
                        final long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
                        final ArrayList<ValueAnimator> list5 = ValueAnimator.sReadyAnims.get();
                        final ArrayList<ValueAnimator> list6 = ValueAnimator.sEndingAnims.get();
                        for (int size2 = list2.size(), j = 0; j < size2; ++j) {
                            final ValueAnimator valueAnimator2 = list2.get(j);
                            if (valueAnimator2.delayedAnimationFrame(currentAnimationTimeMillis)) {
                                list5.add(valueAnimator2);
                            }
                        }
                        final int size3 = list5.size();
                        if (size3 > 0) {
                            for (int k = 0; k < size3; ++k) {
                                final ValueAnimator valueAnimator3 = list5.get(k);
                                valueAnimator3.startAnimation();
                                valueAnimator3.mRunning = true;
                                list2.remove(valueAnimator3);
                            }
                            list5.clear();
                        }
                        int size4 = list.size();
                        int l = 0;
                        while (l < size4) {
                            final ValueAnimator valueAnimator4 = list.get(l);
                            if (valueAnimator4.animationFrame(currentAnimationTimeMillis)) {
                                list6.add(valueAnimator4);
                            }
                            if (list.size() == size4) {
                                ++l;
                            }
                            else {
                                --size4;
                                list6.remove(valueAnimator4);
                            }
                        }
                        if (list6.size() > 0) {
                            for (int n3 = 0; n3 < list6.size(); ++n3) {
                                list6.get(n3).endAnimation();
                            }
                            list6.clear();
                        }
                        if (n != 0 && (!list.isEmpty() || !list2.isEmpty())) {
                            this.sendEmptyMessageDelayed(1, Math.max(0L, ValueAnimator.sFrameDelay - (AnimationUtils.currentAnimationTimeMillis() - currentAnimationTimeMillis)));
                            return;
                        }
                        break;
                    }
                }
            }
        }
    }
    
    public interface AnimatorUpdateListener
    {
        void onAnimationUpdate(final ValueAnimator p0);
    }
}
