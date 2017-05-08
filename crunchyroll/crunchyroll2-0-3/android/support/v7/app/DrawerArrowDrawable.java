// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import android.graphics.Paint$Cap;
import android.graphics.Paint$Join;
import android.graphics.Paint$Style;
import android.util.AttributeSet;
import android.support.v7.appcompat.R;
import android.content.Context;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

abstract class DrawerArrowDrawable extends Drawable
{
    private static final float ARROW_HEAD_ANGLE;
    private final float mBarGap;
    private final float mBarSize;
    private final float mBarThickness;
    private float mCenterOffset;
    private float mMaxCutForBarSize;
    private final float mMiddleArrowSize;
    private final Paint mPaint;
    private final Path mPath;
    private float mProgress;
    private final int mSize;
    private final boolean mSpin;
    private final float mTopBottomArrowSize;
    private boolean mVerticalMirror;
    
    static {
        ARROW_HEAD_ANGLE = (float)Math.toRadians(45.0);
    }
    
    DrawerArrowDrawable(final Context context) {
        this.mPaint = new Paint();
        this.mPath = new Path();
        this.mVerticalMirror = false;
        final TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes((AttributeSet)null, R.styleable.DrawerArrowToggle, R.attr.drawerArrowStyle, R.style.Base_Widget_AppCompat_DrawerArrowToggle);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(obtainStyledAttributes.getColor(R.styleable.DrawerArrowToggle_color, 0));
        this.mSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.DrawerArrowToggle_drawableSize, 0);
        this.mBarSize = Math.round(obtainStyledAttributes.getDimension(R.styleable.DrawerArrowToggle_barSize, 0.0f));
        this.mTopBottomArrowSize = Math.round(obtainStyledAttributes.getDimension(R.styleable.DrawerArrowToggle_topBottomBarArrowSize, 0.0f));
        this.mBarThickness = obtainStyledAttributes.getDimension(R.styleable.DrawerArrowToggle_thickness, 0.0f);
        this.mBarGap = Math.round(obtainStyledAttributes.getDimension(R.styleable.DrawerArrowToggle_gapBetweenBars, 0.0f));
        this.mSpin = obtainStyledAttributes.getBoolean(R.styleable.DrawerArrowToggle_spinBars, true);
        this.mMiddleArrowSize = obtainStyledAttributes.getDimension(R.styleable.DrawerArrowToggle_middleBarArrowSize, 0.0f);
        this.mCenterOffset = (int)(this.mSize - this.mBarThickness * 3.0f - this.mBarGap * 2.0f) / 4 * 2;
        this.mCenterOffset += (float)(this.mBarThickness * 1.5 + this.mBarGap);
        obtainStyledAttributes.recycle();
        this.mPaint.setStyle(Paint$Style.STROKE);
        this.mPaint.setStrokeJoin(Paint$Join.MITER);
        this.mPaint.setStrokeCap(Paint$Cap.BUTT);
        this.mPaint.setStrokeWidth(this.mBarThickness);
        this.mMaxCutForBarSize = (float)(this.mBarThickness / 2.0f * Math.cos(DrawerArrowDrawable.ARROW_HEAD_ANGLE));
    }
    
    private static float lerp(final float n, final float n2, final float n3) {
        return (n2 - n) * n3 + n;
    }
    
    public void draw(final Canvas canvas) {
        final Rect bounds = this.getBounds();
        final boolean layoutRtl = this.isLayoutRtl();
        final float lerp = lerp(this.mBarSize, this.mTopBottomArrowSize, this.mProgress);
        final float lerp2 = lerp(this.mBarSize, this.mMiddleArrowSize, this.mProgress);
        final float n = Math.round(lerp(0.0f, this.mMaxCutForBarSize, this.mProgress));
        final float lerp3 = lerp(0.0f, DrawerArrowDrawable.ARROW_HEAD_ANGLE, this.mProgress);
        float n2;
        if (layoutRtl) {
            n2 = 0.0f;
        }
        else {
            n2 = -180.0f;
        }
        float n3;
        if (layoutRtl) {
            n3 = 180.0f;
        }
        else {
            n3 = 0.0f;
        }
        final float lerp4 = lerp(n2, n3, this.mProgress);
        final float n4 = Math.round(lerp * Math.cos(lerp3));
        final float n5 = Math.round(lerp * Math.sin(lerp3));
        this.mPath.rewind();
        final float lerp5 = lerp(this.mBarGap + this.mBarThickness, -this.mMaxCutForBarSize, this.mProgress);
        final float n6 = -lerp2 / 2.0f;
        this.mPath.moveTo(n6 + n, 0.0f);
        this.mPath.rLineTo(lerp2 - 2.0f * n, 0.0f);
        this.mPath.moveTo(n6, lerp5);
        this.mPath.rLineTo(n4, n5);
        this.mPath.moveTo(n6, -lerp5);
        this.mPath.rLineTo(n4, -n5);
        this.mPath.close();
        canvas.save();
        canvas.translate((float)bounds.centerX(), this.mCenterOffset);
        if (this.mSpin) {
            int n7;
            if (this.mVerticalMirror ^ layoutRtl) {
                n7 = -1;
            }
            else {
                n7 = 1;
            }
            canvas.rotate(n7 * lerp4);
        }
        else if (layoutRtl) {
            canvas.rotate(180.0f);
        }
        canvas.drawPath(this.mPath, this.mPaint);
        canvas.restore();
    }
    
    public int getIntrinsicHeight() {
        return this.mSize;
    }
    
    public int getIntrinsicWidth() {
        return this.mSize;
    }
    
    public int getOpacity() {
        return -3;
    }
    
    public float getProgress() {
        return this.mProgress;
    }
    
    public boolean isAutoMirrored() {
        return true;
    }
    
    abstract boolean isLayoutRtl();
    
    public void setAlpha(final int alpha) {
        this.mPaint.setAlpha(alpha);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }
    
    public void setProgress(final float mProgress) {
        this.mProgress = mProgress;
        this.invalidateSelf();
    }
    
    protected void setVerticalMirror(final boolean mVerticalMirror) {
        this.mVerticalMirror = mVerticalMirror;
    }
}
