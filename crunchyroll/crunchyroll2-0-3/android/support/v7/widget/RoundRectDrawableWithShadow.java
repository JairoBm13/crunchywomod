// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.graphics.ColorFilter;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.RadialGradient;
import android.graphics.Shader$TileMode;
import android.graphics.Path$FillType;
import android.graphics.Rect;
import android.graphics.Paint$Style;
import android.support.v7.cardview.R;
import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

class RoundRectDrawableWithShadow extends Drawable
{
    static final double COS_45;
    static final float SHADOW_MULTIPLIER = 1.5f;
    static RoundRectHelper sRoundRectHelper;
    private boolean mAddPaddingForCorners;
    final RectF mCardBounds;
    float mCornerRadius;
    Paint mCornerShadowPaint;
    Path mCornerShadowPath;
    private boolean mDirty;
    Paint mEdgeShadowPaint;
    final int mInsetShadow;
    float mMaxShadowSize;
    Paint mPaint;
    private boolean mPrintedShadowClipWarning;
    float mRawMaxShadowSize;
    float mRawShadowSize;
    private final int mShadowEndColor;
    float mShadowSize;
    private final int mShadowStartColor;
    
    static {
        COS_45 = Math.cos(Math.toRadians(45.0));
    }
    
    RoundRectDrawableWithShadow(final Resources resources, final int color, final float n, final float n2, final float n3) {
        this.mDirty = true;
        this.mAddPaddingForCorners = true;
        this.mPrintedShadowClipWarning = false;
        this.mShadowStartColor = resources.getColor(R.color.cardview_shadow_start_color);
        this.mShadowEndColor = resources.getColor(R.color.cardview_shadow_end_color);
        this.mInsetShadow = resources.getDimensionPixelSize(R.dimen.cardview_compat_inset_shadow);
        (this.mPaint = new Paint(5)).setColor(color);
        (this.mCornerShadowPaint = new Paint(5)).setStyle(Paint$Style.FILL);
        this.mCornerRadius = (int)(0.5f + n);
        this.mCardBounds = new RectF();
        (this.mEdgeShadowPaint = new Paint(this.mCornerShadowPaint)).setAntiAlias(false);
        this.setShadowSize(n2, n3);
    }
    
    private void buildComponents(final Rect rect) {
        final float n = this.mRawMaxShadowSize * 1.5f;
        this.mCardBounds.set(rect.left + this.mRawMaxShadowSize, rect.top + n, rect.right - this.mRawMaxShadowSize, rect.bottom - n);
        this.buildShadowCorners();
    }
    
    private void buildShadowCorners() {
        final RectF rectF = new RectF(-this.mCornerRadius, -this.mCornerRadius, this.mCornerRadius, this.mCornerRadius);
        final RectF rectF2 = new RectF(rectF);
        rectF2.inset(-this.mShadowSize, -this.mShadowSize);
        if (this.mCornerShadowPath == null) {
            this.mCornerShadowPath = new Path();
        }
        else {
            this.mCornerShadowPath.reset();
        }
        this.mCornerShadowPath.setFillType(Path$FillType.EVEN_ODD);
        this.mCornerShadowPath.moveTo(-this.mCornerRadius, 0.0f);
        this.mCornerShadowPath.rLineTo(-this.mShadowSize, 0.0f);
        this.mCornerShadowPath.arcTo(rectF2, 180.0f, 90.0f, false);
        this.mCornerShadowPath.arcTo(rectF, 270.0f, -90.0f, false);
        this.mCornerShadowPath.close();
        this.mCornerShadowPaint.setShader((Shader)new RadialGradient(0.0f, 0.0f, this.mCornerRadius + this.mShadowSize, new int[] { this.mShadowStartColor, this.mShadowStartColor, this.mShadowEndColor }, new float[] { 0.0f, this.mCornerRadius / (this.mCornerRadius + this.mShadowSize), 1.0f }, Shader$TileMode.CLAMP));
        this.mEdgeShadowPaint.setShader((Shader)new LinearGradient(0.0f, -this.mCornerRadius + this.mShadowSize, 0.0f, -this.mCornerRadius - this.mShadowSize, new int[] { this.mShadowStartColor, this.mShadowStartColor, this.mShadowEndColor }, new float[] { 0.0f, 0.5f, 1.0f }, Shader$TileMode.CLAMP));
        this.mEdgeShadowPaint.setAntiAlias(false);
    }
    
    static float calculateHorizontalPadding(final float n, final float n2, final boolean b) {
        float n3 = n;
        if (b) {
            n3 = (float)(n + (1.0 - RoundRectDrawableWithShadow.COS_45) * n2);
        }
        return n3;
    }
    
    static float calculateVerticalPadding(final float n, final float n2, final boolean b) {
        if (b) {
            return (float)(1.5f * n + (1.0 - RoundRectDrawableWithShadow.COS_45) * n2);
        }
        return 1.5f * n;
    }
    
    private void drawShadow(final Canvas canvas) {
        final float n = -this.mCornerRadius - this.mShadowSize;
        final float n2 = this.mCornerRadius + this.mInsetShadow + this.mRawShadowSize / 2.0f;
        boolean b;
        if (this.mCardBounds.width() - 2.0f * n2 > 0.0f) {
            b = true;
        }
        else {
            b = false;
        }
        boolean b2;
        if (this.mCardBounds.height() - 2.0f * n2 > 0.0f) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        final int save = canvas.save();
        canvas.translate(this.mCardBounds.left + n2, this.mCardBounds.top + n2);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (b) {
            canvas.drawRect(0.0f, n, this.mCardBounds.width() - 2.0f * n2, -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(save);
        final int save2 = canvas.save();
        canvas.translate(this.mCardBounds.right - n2, this.mCardBounds.bottom - n2);
        canvas.rotate(180.0f);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (b) {
            canvas.drawRect(0.0f, n, this.mCardBounds.width() - 2.0f * n2, this.mShadowSize + -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(save2);
        final int save3 = canvas.save();
        canvas.translate(this.mCardBounds.left + n2, this.mCardBounds.bottom - n2);
        canvas.rotate(270.0f);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (b2) {
            canvas.drawRect(0.0f, n, this.mCardBounds.height() - 2.0f * n2, -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(save3);
        final int save4 = canvas.save();
        canvas.translate(this.mCardBounds.right - n2, this.mCardBounds.top + n2);
        canvas.rotate(90.0f);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (b2) {
            canvas.drawRect(0.0f, n, this.mCardBounds.height() - 2.0f * n2, -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(save4);
    }
    
    private int toEven(final float n) {
        int n3;
        final int n2 = n3 = (int)(0.5f + n);
        if (n2 % 2 == 1) {
            n3 = n2 - 1;
        }
        return n3;
    }
    
    public void draw(final Canvas canvas) {
        if (this.mDirty) {
            this.buildComponents(this.getBounds());
            this.mDirty = false;
        }
        canvas.translate(0.0f, this.mRawShadowSize / 2.0f);
        this.drawShadow(canvas);
        canvas.translate(0.0f, -this.mRawShadowSize / 2.0f);
        RoundRectDrawableWithShadow.sRoundRectHelper.drawRoundRect(canvas, this.mCardBounds, this.mCornerRadius, this.mPaint);
    }
    
    float getCornerRadius() {
        return this.mCornerRadius;
    }
    
    void getMaxShadowAndCornerPadding(final Rect rect) {
        this.getPadding(rect);
    }
    
    float getMaxShadowSize() {
        return this.mRawMaxShadowSize;
    }
    
    float getMinHeight() {
        return (this.mRawMaxShadowSize * 1.5f + this.mInsetShadow) * 2.0f + 2.0f * Math.max(this.mRawMaxShadowSize, this.mCornerRadius + this.mInsetShadow + this.mRawMaxShadowSize * 1.5f / 2.0f);
    }
    
    float getMinWidth() {
        return (this.mRawMaxShadowSize + this.mInsetShadow) * 2.0f + 2.0f * Math.max(this.mRawMaxShadowSize, this.mCornerRadius + this.mInsetShadow + this.mRawMaxShadowSize / 2.0f);
    }
    
    public int getOpacity() {
        return -3;
    }
    
    public boolean getPadding(final Rect rect) {
        final int n = (int)Math.ceil(calculateVerticalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
        final int n2 = (int)Math.ceil(calculateHorizontalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
        rect.set(n2, n, n2, n);
        return true;
    }
    
    float getShadowSize() {
        return this.mRawShadowSize;
    }
    
    protected void onBoundsChange(final Rect rect) {
        super.onBoundsChange(rect);
        this.mDirty = true;
    }
    
    public void setAddPaddingForCorners(final boolean mAddPaddingForCorners) {
        this.mAddPaddingForCorners = mAddPaddingForCorners;
        this.invalidateSelf();
    }
    
    public void setAlpha(final int alpha) {
        this.mPaint.setAlpha(alpha);
        this.mCornerShadowPaint.setAlpha(alpha);
        this.mEdgeShadowPaint.setAlpha(alpha);
    }
    
    public void setColor(final int color) {
        this.mPaint.setColor(color);
        this.invalidateSelf();
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
        this.mCornerShadowPaint.setColorFilter(colorFilter);
        this.mEdgeShadowPaint.setColorFilter(colorFilter);
    }
    
    void setCornerRadius(float mCornerRadius) {
        mCornerRadius = (int)(0.5f + mCornerRadius);
        if (this.mCornerRadius == mCornerRadius) {
            return;
        }
        this.mCornerRadius = mCornerRadius;
        this.mDirty = true;
        this.invalidateSelf();
    }
    
    void setMaxShadowSize(final float n) {
        this.setShadowSize(this.mRawShadowSize, n);
    }
    
    void setShadowSize(final float n) {
        this.setShadowSize(n, this.mRawMaxShadowSize);
    }
    
    void setShadowSize(float mRawShadowSize, float n) {
        if (mRawShadowSize < 0.0f || n < 0.0f) {
            throw new IllegalArgumentException("invalid shadow size");
        }
        final float n2 = this.toEven(mRawShadowSize);
        final float mRawMaxShadowSize = this.toEven(n);
        mRawShadowSize = n2;
        if (n2 > mRawMaxShadowSize) {
            n = (mRawShadowSize = mRawMaxShadowSize);
            if (!this.mPrintedShadowClipWarning) {
                this.mPrintedShadowClipWarning = true;
                mRawShadowSize = n;
            }
        }
        if (this.mRawShadowSize == mRawShadowSize && this.mRawMaxShadowSize == mRawMaxShadowSize) {
            return;
        }
        this.mRawShadowSize = mRawShadowSize;
        this.mRawMaxShadowSize = mRawMaxShadowSize;
        this.mShadowSize = (int)(1.5f * mRawShadowSize + this.mInsetShadow + 0.5f);
        this.mMaxShadowSize = this.mInsetShadow + mRawMaxShadowSize;
        this.mDirty = true;
        this.invalidateSelf();
    }
    
    interface RoundRectHelper
    {
        void drawRoundRect(final Canvas p0, final RectF p1, final float p2, final Paint p3);
    }
}
