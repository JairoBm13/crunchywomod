// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.widget;

import android.content.res.TypedArray;
import android.support.design.R;
import android.text.TextUtils$TruncateAt;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.text.TextUtils;
import android.support.v4.view.ViewCompat;
import android.graphics.Color;
import android.os.Build$VERSION;
import android.view.View;
import android.text.TextPaint;
import android.view.animation.Interpolator;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Paint;

final class CollapsingTextHelper
{
    private static final boolean DEBUG_DRAW = false;
    private static final Paint DEBUG_DRAW_PAINT;
    private static final boolean USE_SCALING_TEXTURE;
    private final Rect mCollapsedBounds;
    private int mCollapsedTextColor;
    private float mCollapsedTextSize;
    private int mCollapsedTextVerticalGravity;
    private float mCollapsedTop;
    private float mCurrentLeft;
    private float mCurrentRight;
    private float mCurrentTextSize;
    private float mCurrentTop;
    private final Rect mExpandedBounds;
    private float mExpandedFraction;
    private int mExpandedTextColor;
    private float mExpandedTextSize;
    private int mExpandedTextVerticalGravity;
    private Bitmap mExpandedTitleTexture;
    private float mExpandedTop;
    private Interpolator mPositionInterpolator;
    private float mScale;
    private CharSequence mText;
    private final TextPaint mTextPaint;
    private Interpolator mTextSizeInterpolator;
    private CharSequence mTextToDraw;
    private float mTextWidth;
    private float mTextureAscent;
    private float mTextureDescent;
    private Paint mTexturePaint;
    private boolean mUseTexture;
    private final View mView;
    
    static {
        USE_SCALING_TEXTURE = (Build$VERSION.SDK_INT < 18);
        DEBUG_DRAW_PAINT = null;
        if (CollapsingTextHelper.DEBUG_DRAW_PAINT != null) {
            CollapsingTextHelper.DEBUG_DRAW_PAINT.setAntiAlias(true);
            CollapsingTextHelper.DEBUG_DRAW_PAINT.setColor(-65281);
        }
    }
    
    public CollapsingTextHelper(final View mView) {
        this.mExpandedTextVerticalGravity = 16;
        this.mCollapsedTextVerticalGravity = 16;
        this.mView = mView;
        (this.mTextPaint = new TextPaint()).setAntiAlias(true);
        this.mCollapsedBounds = new Rect();
        this.mExpandedBounds = new Rect();
    }
    
    private static int blendColors(final int n, final int n2, final float n3) {
        final float n4 = 1.0f - n3;
        return Color.argb((int)(Color.alpha(n) * n4 + Color.alpha(n2) * n3), (int)(Color.red(n) * n4 + Color.red(n2) * n3), (int)(Color.green(n) * n4 + Color.green(n2) * n3), (int)(Color.blue(n) * n4 + Color.blue(n2) * n3));
    }
    
    private void calculateBaselines() {
        this.mTextPaint.setTextSize(this.mCollapsedTextSize);
        switch (this.mCollapsedTextVerticalGravity) {
            default: {
                this.mCollapsedTop = this.mCollapsedBounds.centerY() + ((this.mTextPaint.descent() - this.mTextPaint.ascent()) / 2.0f - this.mTextPaint.descent());
                break;
            }
            case 80: {
                this.mCollapsedTop = this.mCollapsedBounds.bottom;
                break;
            }
            case 48: {
                this.mCollapsedTop = this.mCollapsedBounds.top - this.mTextPaint.ascent();
                break;
            }
        }
        this.mTextPaint.setTextSize(this.mExpandedTextSize);
        switch (this.mExpandedTextVerticalGravity) {
            default: {
                this.mExpandedTop = this.mExpandedBounds.centerY() + ((this.mTextPaint.descent() - this.mTextPaint.ascent()) / 2.0f - this.mTextPaint.descent());
                break;
            }
            case 80: {
                this.mExpandedTop = this.mExpandedBounds.bottom;
                break;
            }
            case 48: {
                this.mExpandedTop = this.mExpandedBounds.top - this.mTextPaint.ascent();
                break;
            }
        }
        this.mTextureAscent = this.mTextPaint.ascent();
        this.mTextureDescent = this.mTextPaint.descent();
        this.clearTexture();
    }
    
    private void calculateOffsets() {
        final float mExpandedFraction = this.mExpandedFraction;
        this.mCurrentLeft = interpolate(this.mExpandedBounds.left, this.mCollapsedBounds.left, mExpandedFraction, this.mPositionInterpolator);
        this.mCurrentTop = interpolate(this.mExpandedTop, this.mCollapsedTop, mExpandedFraction, this.mPositionInterpolator);
        this.mCurrentRight = interpolate(this.mExpandedBounds.right, this.mCollapsedBounds.right, mExpandedFraction, this.mPositionInterpolator);
        this.setInterpolatedTextSize(interpolate(this.mExpandedTextSize, this.mCollapsedTextSize, mExpandedFraction, this.mTextSizeInterpolator));
        if (this.mCollapsedTextColor != this.mExpandedTextColor) {
            this.mTextPaint.setColor(blendColors(this.mExpandedTextColor, this.mCollapsedTextColor, mExpandedFraction));
        }
        else {
            this.mTextPaint.setColor(this.mCollapsedTextColor);
        }
        ViewCompat.postInvalidateOnAnimation(this.mView);
    }
    
    private void clearTexture() {
        if (this.mExpandedTitleTexture != null) {
            this.mExpandedTitleTexture.recycle();
            this.mExpandedTitleTexture = null;
        }
    }
    
    private void ensureExpandedTexture() {
        if (this.mExpandedTitleTexture == null && !this.mExpandedBounds.isEmpty() && !TextUtils.isEmpty(this.mTextToDraw)) {
            this.mTextPaint.setTextSize(this.mExpandedTextSize);
            this.mTextPaint.setColor(this.mExpandedTextColor);
            final int round = Math.round(this.mTextPaint.measureText(this.mTextToDraw, 0, this.mTextToDraw.length()));
            final int round2 = Math.round(this.mTextPaint.descent() - this.mTextPaint.ascent());
            this.mTextWidth = round;
            if (round > 0 || round2 > 0) {
                this.mExpandedTitleTexture = Bitmap.createBitmap(round, round2, Bitmap$Config.ARGB_8888);
                new Canvas(this.mExpandedTitleTexture).drawText(this.mTextToDraw, 0, this.mTextToDraw.length(), 0.0f, round2 - this.mTextPaint.descent(), (Paint)this.mTextPaint);
                if (this.mTexturePaint == null) {
                    (this.mTexturePaint = new Paint()).setAntiAlias(true);
                    this.mTexturePaint.setFilterBitmap(true);
                }
            }
        }
    }
    
    private static float interpolate(final float n, final float n2, final float n3, final Interpolator interpolator) {
        float interpolation = n3;
        if (interpolator != null) {
            interpolation = interpolator.getInterpolation(n3);
        }
        return AnimationUtils.lerp(n, n2, interpolation);
    }
    
    private static boolean isClose(final float n, final float n2) {
        return Math.abs(n - n2) < 0.001f;
    }
    
    private void recalculate() {
        if (ViewCompat.isLaidOut(this.mView)) {
            this.calculateBaselines();
            this.calculateOffsets();
        }
    }
    
    private void setInterpolatedTextSize(float n) {
        boolean mUseTexture = true;
        if (this.mText == null) {
            return;
        }
        boolean b = false;
        float mCurrentTextSize;
        if (isClose(n, this.mCollapsedTextSize)) {
            n = this.mCollapsedBounds.width();
            mCurrentTextSize = this.mCollapsedTextSize;
            this.mScale = 1.0f;
        }
        else {
            final float n2 = this.mExpandedBounds.width();
            mCurrentTextSize = this.mExpandedTextSize;
            if (isClose(n, this.mExpandedTextSize)) {
                this.mScale = 1.0f;
                n = n2;
            }
            else {
                this.mScale = n / this.mExpandedTextSize;
                n = n2;
            }
        }
        if (n > 0.0f) {
            if (this.mCurrentTextSize != mCurrentTextSize) {
                b = true;
            }
            else {
                b = false;
            }
            this.mCurrentTextSize = mCurrentTextSize;
        }
        if (this.mTextToDraw == null || b) {
            this.mTextPaint.setTextSize(this.mCurrentTextSize);
            final CharSequence ellipsize = TextUtils.ellipsize(this.mText, this.mTextPaint, n, TextUtils$TruncateAt.END);
            if (this.mTextToDraw == null || !this.mTextToDraw.equals(ellipsize)) {
                this.mTextToDraw = ellipsize;
            }
            this.mTextWidth = this.mTextPaint.measureText(this.mTextToDraw, 0, this.mTextToDraw.length());
        }
        if (!CollapsingTextHelper.USE_SCALING_TEXTURE || this.mScale == 1.0f) {
            mUseTexture = false;
        }
        this.mUseTexture = mUseTexture;
        if (this.mUseTexture) {
            this.ensureExpandedTexture();
        }
        ViewCompat.postInvalidateOnAnimation(this.mView);
    }
    
    public void draw(final Canvas canvas) {
        boolean b = true;
        final int save = canvas.save();
        if (this.mTextToDraw != null) {
            boolean b2;
            if (ViewCompat.getLayoutDirection(this.mView) == 1) {
                b2 = true;
            }
            else {
                b2 = false;
            }
            float n;
            if (b2) {
                n = this.mCurrentRight;
            }
            else {
                n = this.mCurrentLeft;
            }
            final float mCurrentTop = this.mCurrentTop;
            if (!this.mUseTexture || this.mExpandedTitleTexture == null) {
                b = false;
            }
            this.mTextPaint.setTextSize(this.mCurrentTextSize);
            float n2;
            if (b) {
                n2 = this.mTextureAscent * this.mScale;
                final float mTextureDescent = this.mTextureDescent;
                final float mScale = this.mScale;
            }
            else {
                n2 = this.mTextPaint.ascent() * this.mScale;
                this.mTextPaint.descent();
                final float mScale2 = this.mScale;
            }
            float n3 = mCurrentTop;
            if (b) {
                n3 = mCurrentTop + n2;
            }
            if (this.mScale != 1.0f) {
                canvas.scale(this.mScale, this.mScale, n, n3);
            }
            float n4 = n;
            if (b2) {
                n4 = n - this.mTextWidth;
            }
            if (b) {
                canvas.drawBitmap(this.mExpandedTitleTexture, n4, n3, this.mTexturePaint);
            }
            else {
                canvas.drawText(this.mTextToDraw, 0, this.mTextToDraw.length(), n4, n3, (Paint)this.mTextPaint);
            }
        }
        canvas.restoreToCount(save);
    }
    
    int getCollapsedTextColor() {
        return this.mCollapsedTextColor;
    }
    
    float getCollapsedTextSize() {
        return this.mCollapsedTextSize;
    }
    
    int getExpandedTextColor() {
        return this.mExpandedTextColor;
    }
    
    float getExpandedTextSize() {
        return this.mExpandedTextSize;
    }
    
    float getExpansionFraction() {
        return this.mExpandedFraction;
    }
    
    CharSequence getText() {
        return this.mText;
    }
    
    public void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        this.recalculate();
    }
    
    void setCollapsedBounds(final int n, final int n2, final int n3, final int n4) {
        this.mCollapsedBounds.set(n, n2, n3, n4);
        this.recalculate();
    }
    
    void setCollapsedTextAppearance(final int n) {
        final TypedArray obtainStyledAttributes = this.mView.getContext().obtainStyledAttributes(n, R.styleable.TextAppearance);
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textColor)) {
            this.mCollapsedTextColor = obtainStyledAttributes.getColor(R.styleable.TextAppearance_android_textColor, 0);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textSize)) {
            this.mCollapsedTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
        }
        obtainStyledAttributes.recycle();
        this.recalculate();
    }
    
    void setCollapsedTextColor(final int mCollapsedTextColor) {
        if (this.mCollapsedTextColor != mCollapsedTextColor) {
            this.mCollapsedTextColor = mCollapsedTextColor;
            this.recalculate();
        }
    }
    
    void setCollapsedTextSize(final float mCollapsedTextSize) {
        if (this.mCollapsedTextSize != mCollapsedTextSize) {
            this.mCollapsedTextSize = mCollapsedTextSize;
            this.recalculate();
        }
    }
    
    void setCollapsedTextVerticalGravity(int mCollapsedTextVerticalGravity) {
        mCollapsedTextVerticalGravity &= 0x70;
        if (this.mCollapsedTextVerticalGravity != mCollapsedTextVerticalGravity) {
            this.mCollapsedTextVerticalGravity = mCollapsedTextVerticalGravity;
            this.recalculate();
        }
    }
    
    void setExpandedBounds(final int n, final int n2, final int n3, final int n4) {
        this.mExpandedBounds.set(n, n2, n3, n4);
        this.recalculate();
    }
    
    void setExpandedTextAppearance(final int n) {
        final TypedArray obtainStyledAttributes = this.mView.getContext().obtainStyledAttributes(n, R.styleable.TextAppearance);
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textColor)) {
            this.mExpandedTextColor = obtainStyledAttributes.getColor(R.styleable.TextAppearance_android_textColor, 0);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textSize)) {
            this.mExpandedTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
        }
        obtainStyledAttributes.recycle();
        this.recalculate();
    }
    
    void setExpandedTextColor(final int mExpandedTextColor) {
        if (this.mExpandedTextColor != mExpandedTextColor) {
            this.mExpandedTextColor = mExpandedTextColor;
            this.recalculate();
        }
    }
    
    void setExpandedTextSize(final float mExpandedTextSize) {
        if (this.mExpandedTextSize != mExpandedTextSize) {
            this.mExpandedTextSize = mExpandedTextSize;
            this.recalculate();
        }
    }
    
    void setExpandedTextVerticalGravity(int mExpandedTextVerticalGravity) {
        mExpandedTextVerticalGravity &= 0x70;
        if (this.mExpandedTextVerticalGravity != mExpandedTextVerticalGravity) {
            this.mExpandedTextVerticalGravity = mExpandedTextVerticalGravity;
            this.recalculate();
        }
    }
    
    void setExpansionFraction(float constrain) {
        constrain = MathUtils.constrain(constrain, 0.0f, 1.0f);
        if (constrain != this.mExpandedFraction) {
            this.mExpandedFraction = constrain;
            this.calculateOffsets();
        }
    }
    
    void setPositionInterpolator(final Interpolator mPositionInterpolator) {
        this.mPositionInterpolator = mPositionInterpolator;
        this.recalculate();
    }
    
    void setText(final CharSequence mText) {
        if (mText == null || !mText.equals(this.mText)) {
            this.mText = mText;
            this.clearTexture();
            this.recalculate();
        }
    }
    
    void setTextSizeInterpolator(final Interpolator mTextSizeInterpolator) {
        this.mTextSizeInterpolator = mTextSizeInterpolator;
        this.recalculate();
    }
}
