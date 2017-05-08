// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.widget;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.view.animation.DecelerateInterpolator;
import android.graphics.Paint$Style;
import com.crunchyroll.crunchyroid.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Paint;
import android.view.animation.Interpolator;
import android.view.View;

public class AnimatedCircleView extends View
{
    private Interpolator mAlphaInterpolator;
    private int mColorId;
    private final int mDurationMs;
    private final int mFrameDurationMs;
    private boolean mIsAnimating;
    private Paint mPaint;
    private int mRadius;
    private Interpolator mRadiusInterpolator;
    private int mTimeElapsedMs;
    private int mX;
    private int mY;
    
    public AnimatedCircleView(final Context context) {
        this(context, null);
    }
    
    public AnimatedCircleView(Context obtainStyledAttributes, final AttributeSet set) {
        super(obtainStyledAttributes, set);
        obtainStyledAttributes = (Context)obtainStyledAttributes.obtainStyledAttributes(set, R.styleable.AnimatedCircleView, 0, 0);
        try {
            this.mFrameDurationMs = ((TypedArray)obtainStyledAttributes).getInt(0, 12);
            this.mDurationMs = ((TypedArray)obtainStyledAttributes).getInt(0, 200);
            ((TypedArray)obtainStyledAttributes).recycle();
            this.mX = 0;
            this.mY = 0;
            this.mColorId = 17170443;
            this.mIsAnimating = false;
            (this.mPaint = new Paint()).setAntiAlias(true);
            this.mPaint.setStyle(Paint$Style.FILL);
            this.mPaint.setColor(this.getResources().getColor(this.mColorId));
        }
        finally {
            ((TypedArray)obtainStyledAttributes).recycle();
        }
    }
    
    public void animateCircle(final int n, final int n2, final int n3, final int n4) {
        this.animateCircle(n, n2, n3, n4, (Interpolator)new DecelerateInterpolator(1.2f), (Interpolator)new DecelerateInterpolator());
    }
    
    public void animateCircle(final int mx, final int my, final int mRadius, final int mColorId, final Interpolator mAlphaInterpolator, final Interpolator mRadiusInterpolator) {
        if (!this.mIsAnimating) {
            this.mX = mx;
            this.mY = my;
            this.mRadius = mRadius;
            this.mColorId = mColorId;
            this.mAlphaInterpolator = mAlphaInterpolator;
            this.mRadiusInterpolator = mRadiusInterpolator;
            this.post((Runnable)new AnimateCircleRunnable());
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.mIsAnimating) {
            final float n = this.mTimeElapsedMs / this.mDurationMs;
            final int round = Math.round(this.mAlphaInterpolator.getInterpolation(n) * 255.0f);
            final float interpolation = this.mRadiusInterpolator.getInterpolation(n);
            final float n2 = this.mRadius;
            this.mPaint.setColor(this.getResources().getColor(this.mColorId));
            this.mPaint.setAlpha(255 - round);
            canvas.drawCircle((float)this.mX, (float)this.mY, interpolation * n2, this.mPaint);
        }
    }
    
    class AnimateCircleRunnable implements Runnable
    {
        public AnimateCircleRunnable() {
            AnimatedCircleView.this.mTimeElapsedMs = -AnimatedCircleView.this.mFrameDurationMs;
        }
        
        @Override
        public void run() {
            AnimatedCircleView.this.mTimeElapsedMs += AnimatedCircleView.this.mFrameDurationMs;
            if (AnimatedCircleView.this.mTimeElapsedMs < AnimatedCircleView.this.mDurationMs) {
                AnimatedCircleView.this.mIsAnimating = true;
                AnimatedCircleView.this.postDelayed((Runnable)this, (long)AnimatedCircleView.this.mFrameDurationMs);
            }
            else {
                AnimatedCircleView.this.mIsAnimating = false;
            }
            AnimatedCircleView.this.invalidate();
        }
    }
}
