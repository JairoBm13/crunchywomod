// 
// Decompiled by Procyon v0.5.30
// 

package com.nineoldandroids.animation;

import android.view.animation.Interpolator;
import java.util.ArrayList;

class FloatKeyframeSet extends KeyframeSet
{
    private float deltaValue;
    private boolean firstTime;
    private float firstValue;
    private float lastValue;
    
    public FloatKeyframeSet(final Keyframe.FloatKeyframe... array) {
        super((Keyframe[])array);
        this.firstTime = true;
    }
    
    @Override
    public FloatKeyframeSet clone() {
        final ArrayList<Keyframe> mKeyframes = this.mKeyframes;
        final int size = this.mKeyframes.size();
        final Keyframe.FloatKeyframe[] array = new Keyframe.FloatKeyframe[size];
        for (int i = 0; i < size; ++i) {
            array[i] = (Keyframe.FloatKeyframe)mKeyframes.get(i).clone();
        }
        return new FloatKeyframeSet(array);
    }
    
    public float getFloatValue(float n) {
        if (this.mNumKeyframes == 2) {
            if (this.firstTime) {
                this.firstTime = false;
                this.firstValue = ((Keyframe.FloatKeyframe)this.mKeyframes.get(0)).getFloatValue();
                this.lastValue = ((Keyframe.FloatKeyframe)this.mKeyframes.get(1)).getFloatValue();
                this.deltaValue = this.lastValue - this.firstValue;
            }
            float interpolation = n;
            if (this.mInterpolator != null) {
                interpolation = this.mInterpolator.getInterpolation(n);
            }
            if (this.mEvaluator == null) {
                return this.firstValue + this.deltaValue * interpolation;
            }
            return this.mEvaluator.evaluate(interpolation, this.firstValue, this.lastValue).floatValue();
        }
        else if (n <= 0.0f) {
            final Keyframe.FloatKeyframe floatKeyframe = this.mKeyframes.get(0);
            final Keyframe.FloatKeyframe floatKeyframe2 = this.mKeyframes.get(1);
            final float floatValue = floatKeyframe.getFloatValue();
            final float floatValue2 = floatKeyframe2.getFloatValue();
            final float fraction = floatKeyframe.getFraction();
            final float fraction2 = floatKeyframe2.getFraction();
            final Interpolator interpolator = floatKeyframe2.getInterpolator();
            float interpolation2 = n;
            if (interpolator != null) {
                interpolation2 = interpolator.getInterpolation(n);
            }
            n = (interpolation2 - fraction) / (fraction2 - fraction);
            if (this.mEvaluator == null) {
                return (floatValue2 - floatValue) * n + floatValue;
            }
            return this.mEvaluator.evaluate(n, floatValue, floatValue2).floatValue();
        }
        else {
            if (n < 1.0f) {
                Keyframe keyframe = this.mKeyframes.get(0);
                int i = 1;
                while (i < this.mNumKeyframes) {
                    final Keyframe.FloatKeyframe floatKeyframe3 = this.mKeyframes.get(i);
                    if (n < floatKeyframe3.getFraction()) {
                        final Interpolator interpolator2 = floatKeyframe3.getInterpolator();
                        float interpolation3 = n;
                        if (interpolator2 != null) {
                            interpolation3 = interpolator2.getInterpolation(n);
                        }
                        n = (interpolation3 - keyframe.getFraction()) / (floatKeyframe3.getFraction() - keyframe.getFraction());
                        final float floatValue3 = ((Keyframe.FloatKeyframe)keyframe).getFloatValue();
                        final float floatValue4 = floatKeyframe3.getFloatValue();
                        if (this.mEvaluator == null) {
                            return (floatValue4 - floatValue3) * n + floatValue3;
                        }
                        return this.mEvaluator.evaluate(n, floatValue3, floatValue4).floatValue();
                    }
                    else {
                        keyframe = floatKeyframe3;
                        ++i;
                    }
                }
                return ((Number)this.mKeyframes.get(this.mNumKeyframes - 1).getValue()).floatValue();
            }
            final Keyframe.FloatKeyframe floatKeyframe4 = this.mKeyframes.get(this.mNumKeyframes - 2);
            final Keyframe.FloatKeyframe floatKeyframe5 = this.mKeyframes.get(this.mNumKeyframes - 1);
            final float floatValue5 = floatKeyframe4.getFloatValue();
            final float floatValue6 = floatKeyframe5.getFloatValue();
            final float fraction3 = floatKeyframe4.getFraction();
            final float fraction4 = floatKeyframe5.getFraction();
            final Interpolator interpolator3 = floatKeyframe5.getInterpolator();
            float interpolation4 = n;
            if (interpolator3 != null) {
                interpolation4 = interpolator3.getInterpolation(n);
            }
            n = (interpolation4 - fraction3) / (fraction4 - fraction3);
            if (this.mEvaluator == null) {
                return (floatValue6 - floatValue5) * n + floatValue5;
            }
            return this.mEvaluator.evaluate(n, floatValue5, floatValue6).floatValue();
        }
    }
    
    @Override
    public Object getValue(final float n) {
        return this.getFloatValue(n);
    }
}
