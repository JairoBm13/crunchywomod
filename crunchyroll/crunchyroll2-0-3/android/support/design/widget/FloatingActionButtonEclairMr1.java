// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.widget;

import android.view.animation.Transformation;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.graphics.PorterDuff$Mode;
import android.graphics.Rect;
import android.content.res.ColorStateList;
import android.view.animation.Animation;
import android.view.View;
import android.graphics.drawable.Drawable;

class FloatingActionButtonEclairMr1 extends FloatingActionButtonImpl
{
    private int mAnimationDuration;
    private Drawable mBorderDrawable;
    private float mElevation;
    private float mPressedTranslationZ;
    private Drawable mRippleDrawable;
    ShadowDrawableWrapper mShadowDrawable;
    private Drawable mShapeDrawable;
    private StateListAnimator mStateListAnimator;
    
    FloatingActionButtonEclairMr1(final View target, final ShadowViewDelegate shadowViewDelegate) {
        super(target, shadowViewDelegate);
        this.mAnimationDuration = target.getResources().getInteger(17694720);
        (this.mStateListAnimator = new StateListAnimator()).setTarget(target);
        this.mStateListAnimator.addState(FloatingActionButtonEclairMr1.PRESSED_ENABLED_STATE_SET, this.setupAnimation(new ElevateToTranslationZAnimation()));
        this.mStateListAnimator.addState(FloatingActionButtonEclairMr1.FOCUSED_ENABLED_STATE_SET, this.setupAnimation(new ElevateToTranslationZAnimation()));
        this.mStateListAnimator.addState(FloatingActionButtonEclairMr1.EMPTY_STATE_SET, this.setupAnimation(new ResetElevationAnimation()));
    }
    
    private static ColorStateList createColorStateList(int n) {
        final int[][] array = new int[3][];
        final int[] array2 = new int[3];
        array[0] = FloatingActionButtonEclairMr1.FOCUSED_ENABLED_STATE_SET;
        array2[0] = n;
        final int n2 = 0 + 1;
        array[n2] = FloatingActionButtonEclairMr1.PRESSED_ENABLED_STATE_SET;
        array2[n2] = n;
        n = n2 + 1;
        array[n] = new int[0];
        array2[n] = 0;
        return new ColorStateList(array, array2);
    }
    
    private Animation setupAnimation(final Animation animation) {
        animation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animation.setDuration((long)this.mAnimationDuration);
        return animation;
    }
    
    private void updatePadding() {
        final Rect rect = new Rect();
        this.mShadowDrawable.getPadding(rect);
        this.mShadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }
    
    @Override
    void jumpDrawableToCurrentState() {
        this.mStateListAnimator.jumpToCurrentState();
    }
    
    @Override
    void onDrawableStateChanged(final int[] state) {
        this.mStateListAnimator.setState(state);
    }
    
    @Override
    void setBackgroundDrawable(final Drawable drawable, final ColorStateList list, final PorterDuff$Mode porterDuff$Mode, final int n, final int n2) {
        DrawableCompat.setTintList(this.mShapeDrawable = DrawableCompat.wrap(drawable), list);
        if (porterDuff$Mode != null) {
            DrawableCompat.setTintMode(this.mShapeDrawable, porterDuff$Mode);
        }
        final GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(1);
        gradientDrawable.setColor(-1);
        gradientDrawable.setCornerRadius(this.mShadowViewDelegate.getRadius());
        DrawableCompat.setTintList(this.mRippleDrawable = DrawableCompat.wrap((Drawable)gradientDrawable), createColorStateList(n));
        DrawableCompat.setTintMode(this.mRippleDrawable, PorterDuff$Mode.MULTIPLY);
        Drawable[] array;
        if (n2 > 0) {
            this.mBorderDrawable = this.createBorderDrawable(n2, list);
            array = new Drawable[] { this.mBorderDrawable, this.mShapeDrawable, this.mRippleDrawable };
        }
        else {
            this.mBorderDrawable = null;
            array = new Drawable[] { this.mShapeDrawable, this.mRippleDrawable };
        }
        (this.mShadowDrawable = new ShadowDrawableWrapper(this.mView.getResources(), (Drawable)new LayerDrawable(array), this.mShadowViewDelegate.getRadius(), this.mElevation, this.mElevation + this.mPressedTranslationZ)).setAddPaddingForCorners(false);
        this.mShadowViewDelegate.setBackgroundDrawable(this.mShadowDrawable);
        this.updatePadding();
    }
    
    @Override
    void setBackgroundTintList(final ColorStateList list) {
        DrawableCompat.setTintList(this.mShapeDrawable, list);
        if (this.mBorderDrawable != null) {
            DrawableCompat.setTintList(this.mBorderDrawable, list);
        }
    }
    
    @Override
    void setBackgroundTintMode(final PorterDuff$Mode porterDuff$Mode) {
        DrawableCompat.setTintMode(this.mShapeDrawable, porterDuff$Mode);
    }
    
    @Override
    void setElevation(final float mElevation) {
        if (this.mElevation != mElevation && this.mShadowDrawable != null) {
            this.mShadowDrawable.setShadowSize(mElevation, this.mPressedTranslationZ + mElevation);
            this.mElevation = mElevation;
            this.updatePadding();
        }
    }
    
    @Override
    void setPressedTranslationZ(final float mPressedTranslationZ) {
        if (this.mPressedTranslationZ != mPressedTranslationZ && this.mShadowDrawable != null) {
            this.mPressedTranslationZ = mPressedTranslationZ;
            this.mShadowDrawable.setMaxShadowSize(this.mElevation + mPressedTranslationZ);
            this.updatePadding();
        }
    }
    
    @Override
    void setRippleColor(final int n) {
        DrawableCompat.setTint(this.mRippleDrawable, n);
    }
    
    private abstract class BaseShadowAnimation extends Animation
    {
        private float mShadowSizeDiff;
        private float mShadowSizeStart;
        
        protected void applyTransformation(final float n, final Transformation transformation) {
            FloatingActionButtonEclairMr1.this.mShadowDrawable.setShadowSize(this.mShadowSizeStart + this.mShadowSizeDiff * n);
        }
        
        protected abstract float getTargetShadowSize();
        
        public void reset() {
            super.reset();
            this.mShadowSizeStart = FloatingActionButtonEclairMr1.this.mShadowDrawable.getShadowSize();
            this.mShadowSizeDiff = this.getTargetShadowSize() - this.mShadowSizeStart;
        }
    }
    
    private class ElevateToTranslationZAnimation extends BaseShadowAnimation
    {
        @Override
        protected float getTargetShadowSize() {
            return FloatingActionButtonEclairMr1.this.mElevation + FloatingActionButtonEclairMr1.this.mPressedTranslationZ;
        }
    }
    
    private class ResetElevationAnimation extends BaseShadowAnimation
    {
        @Override
        protected float getTargetShadowSize() {
            return FloatingActionButtonEclairMr1.this.mElevation;
        }
    }
}
