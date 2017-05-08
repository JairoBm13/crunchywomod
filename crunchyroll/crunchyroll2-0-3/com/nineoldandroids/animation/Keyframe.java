// 
// Decompiled by Procyon v0.5.30
// 

package com.nineoldandroids.animation;

import android.view.animation.Interpolator;

public abstract class Keyframe implements Cloneable
{
    float mFraction;
    boolean mHasValue;
    private Interpolator mInterpolator;
    Class mValueType;
    
    public Keyframe() {
        this.mInterpolator = null;
        this.mHasValue = false;
    }
    
    public static Keyframe ofFloat(final float n) {
        return new FloatKeyframe(n);
    }
    
    public static Keyframe ofFloat(final float n, final float n2) {
        return new FloatKeyframe(n, n2);
    }
    
    public abstract Keyframe clone();
    
    public float getFraction() {
        return this.mFraction;
    }
    
    public Interpolator getInterpolator() {
        return this.mInterpolator;
    }
    
    public abstract Object getValue();
    
    public boolean hasValue() {
        return this.mHasValue;
    }
    
    public void setInterpolator(final Interpolator mInterpolator) {
        this.mInterpolator = mInterpolator;
    }
    
    public abstract void setValue(final Object p0);
    
    static class FloatKeyframe extends Keyframe
    {
        float mValue;
        
        FloatKeyframe(final float mFraction) {
            this.mFraction = mFraction;
            this.mValueType = Float.TYPE;
        }
        
        FloatKeyframe(final float mFraction, final float mValue) {
            this.mFraction = mFraction;
            this.mValue = mValue;
            this.mValueType = Float.TYPE;
            this.mHasValue = true;
        }
        
        @Override
        public FloatKeyframe clone() {
            final FloatKeyframe floatKeyframe = new FloatKeyframe(this.getFraction(), this.mValue);
            floatKeyframe.setInterpolator(this.getInterpolator());
            return floatKeyframe;
        }
        
        public float getFloatValue() {
            return this.mValue;
        }
        
        @Override
        public Object getValue() {
            return this.mValue;
        }
        
        @Override
        public void setValue(final Object o) {
            if (o != null && o.getClass() == Float.class) {
                this.mValue = (float)o;
                this.mHasValue = true;
            }
        }
    }
}
