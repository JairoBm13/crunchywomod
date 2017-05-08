// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

class RoundRectDrawable extends Drawable
{
    private final RectF mBoundsF;
    private final Rect mBoundsI;
    private boolean mInsetForPadding;
    private boolean mInsetForRadius;
    private float mPadding;
    private final Paint mPaint;
    private float mRadius;
    
    public RoundRectDrawable(final int color, final float mRadius) {
        this.mInsetForPadding = false;
        this.mInsetForRadius = true;
        this.mRadius = mRadius;
        (this.mPaint = new Paint(5)).setColor(color);
        this.mBoundsF = new RectF();
        this.mBoundsI = new Rect();
    }
    
    private void updateBounds(final Rect rect) {
        Rect bounds = rect;
        if (rect == null) {
            bounds = this.getBounds();
        }
        this.mBoundsF.set((float)bounds.left, (float)bounds.top, (float)bounds.right, (float)bounds.bottom);
        this.mBoundsI.set(bounds);
        if (this.mInsetForPadding) {
            this.mBoundsI.inset((int)Math.ceil(RoundRectDrawableWithShadow.calculateHorizontalPadding(this.mPadding, this.mRadius, this.mInsetForRadius)), (int)Math.ceil(RoundRectDrawableWithShadow.calculateVerticalPadding(this.mPadding, this.mRadius, this.mInsetForRadius)));
            this.mBoundsF.set(this.mBoundsI);
        }
    }
    
    public void draw(final Canvas canvas) {
        canvas.drawRoundRect(this.mBoundsF, this.mRadius, this.mRadius, this.mPaint);
    }
    
    public int getOpacity() {
        return -3;
    }
    
    public void getOutline(final Outline outline) {
        outline.setRoundRect(this.mBoundsI, this.mRadius);
    }
    
    float getPadding() {
        return this.mPadding;
    }
    
    public float getRadius() {
        return this.mRadius;
    }
    
    protected void onBoundsChange(final Rect rect) {
        super.onBoundsChange(rect);
        this.updateBounds(rect);
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColor(final int color) {
        this.mPaint.setColor(color);
        this.invalidateSelf();
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
    
    void setPadding(final float mPadding, final boolean mInsetForPadding, final boolean mInsetForRadius) {
        if (mPadding == this.mPadding && this.mInsetForPadding == mInsetForPadding && this.mInsetForRadius == mInsetForRadius) {
            return;
        }
        this.mPadding = mPadding;
        this.mInsetForPadding = mInsetForPadding;
        this.mInsetForRadius = mInsetForRadius;
        this.updateBounds(null);
        this.invalidateSelf();
    }
    
    void setRadius(final float mRadius) {
        if (mRadius == this.mRadius) {
            return;
        }
        this.mRadius = mRadius;
        this.updateBounds(null);
        this.invalidateSelf();
    }
}
