// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.view.View$MeasureSpec;
import android.content.res.TypedArray;
import android.support.v7.cardview.R;
import android.util.AttributeSet;
import android.content.Context;
import android.os.Build$VERSION;
import android.graphics.Rect;
import android.widget.FrameLayout;

public class CardView extends FrameLayout implements CardViewDelegate
{
    private static final CardViewImpl IMPL;
    private boolean mCompatPadding;
    private final Rect mContentPadding;
    private boolean mPreventCornerOverlap;
    private final Rect mShadowBounds;
    
    static {
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = new CardViewApi21();
        }
        else if (Build$VERSION.SDK_INT >= 17) {
            IMPL = new CardViewJellybeanMr1();
        }
        else {
            IMPL = new CardViewEclairMr1();
        }
        CardView.IMPL.initStatic();
    }
    
    public CardView(final Context context) {
        super(context);
        this.mContentPadding = new Rect();
        this.mShadowBounds = new Rect();
        this.initialize(context, null, 0);
    }
    
    public CardView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mContentPadding = new Rect();
        this.mShadowBounds = new Rect();
        this.initialize(context, set, 0);
    }
    
    public CardView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mContentPadding = new Rect();
        this.mShadowBounds = new Rect();
        this.initialize(context, set, n);
    }
    
    private void initialize(final Context context, final AttributeSet set, int color) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.CardView, color, R.style.CardView_Light);
        color = obtainStyledAttributes.getColor(R.styleable.CardView_cardBackgroundColor, 0);
        final float dimension = obtainStyledAttributes.getDimension(R.styleable.CardView_cardCornerRadius, 0.0f);
        final float dimension2 = obtainStyledAttributes.getDimension(R.styleable.CardView_cardElevation, 0.0f);
        final float dimension3 = obtainStyledAttributes.getDimension(R.styleable.CardView_cardMaxElevation, 0.0f);
        this.mCompatPadding = obtainStyledAttributes.getBoolean(R.styleable.CardView_cardUseCompatPadding, false);
        this.mPreventCornerOverlap = obtainStyledAttributes.getBoolean(R.styleable.CardView_cardPreventCornerOverlap, true);
        final int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CardView_contentPadding, 0);
        this.mContentPadding.left = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CardView_contentPaddingLeft, dimensionPixelSize);
        this.mContentPadding.top = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CardView_contentPaddingTop, dimensionPixelSize);
        this.mContentPadding.right = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CardView_contentPaddingRight, dimensionPixelSize);
        this.mContentPadding.bottom = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CardView_contentPaddingBottom, dimensionPixelSize);
        float n = dimension3;
        if (dimension2 > dimension3) {
            n = dimension2;
        }
        obtainStyledAttributes.recycle();
        CardView.IMPL.initialize(this, context, color, dimension, dimension2, n);
    }
    
    public float getCardElevation() {
        return CardView.IMPL.getElevation(this);
    }
    
    public int getContentPaddingBottom() {
        return this.mContentPadding.bottom;
    }
    
    public int getContentPaddingLeft() {
        return this.mContentPadding.left;
    }
    
    public int getContentPaddingRight() {
        return this.mContentPadding.right;
    }
    
    public int getContentPaddingTop() {
        return this.mContentPadding.top;
    }
    
    public float getMaxCardElevation() {
        return CardView.IMPL.getMaxElevation(this);
    }
    
    public boolean getPreventCornerOverlap() {
        return this.mPreventCornerOverlap;
    }
    
    public float getRadius() {
        return CardView.IMPL.getRadius(this);
    }
    
    public boolean getUseCompatPadding() {
        return this.mCompatPadding;
    }
    
    protected void onMeasure(int measureSpec, int measureSpec2) {
        if (!(CardView.IMPL instanceof CardViewApi21)) {
            final int mode = View$MeasureSpec.getMode(measureSpec);
            switch (mode) {
                case Integer.MIN_VALUE:
                case 1073741824: {
                    measureSpec = View$MeasureSpec.makeMeasureSpec(Math.max((int)Math.ceil(CardView.IMPL.getMinWidth(this)), View$MeasureSpec.getSize(measureSpec)), mode);
                    break;
                }
            }
            final int mode2 = View$MeasureSpec.getMode(measureSpec2);
            switch (mode2) {
                case Integer.MIN_VALUE:
                case 1073741824: {
                    measureSpec2 = View$MeasureSpec.makeMeasureSpec(Math.max((int)Math.ceil(CardView.IMPL.getMinHeight(this)), View$MeasureSpec.getSize(measureSpec2)), mode2);
                    break;
                }
            }
            super.onMeasure(measureSpec, measureSpec2);
            return;
        }
        super.onMeasure(measureSpec, measureSpec2);
    }
    
    public void setCardBackgroundColor(final int n) {
        CardView.IMPL.setBackgroundColor(this, n);
    }
    
    public void setCardElevation(final float n) {
        CardView.IMPL.setElevation(this, n);
    }
    
    public void setContentPadding(final int n, final int n2, final int n3, final int n4) {
        this.mContentPadding.set(n, n2, n3, n4);
        CardView.IMPL.updatePadding(this);
    }
    
    public void setMaxCardElevation(final float n) {
        CardView.IMPL.setMaxElevation(this, n);
    }
    
    public void setPadding(final int n, final int n2, final int n3, final int n4) {
    }
    
    public void setPaddingRelative(final int n, final int n2, final int n3, final int n4) {
    }
    
    public void setPreventCornerOverlap(final boolean mPreventCornerOverlap) {
        if (mPreventCornerOverlap == this.mPreventCornerOverlap) {
            return;
        }
        this.mPreventCornerOverlap = mPreventCornerOverlap;
        CardView.IMPL.onPreventCornerOverlapChanged(this);
    }
    
    public void setRadius(final float n) {
        CardView.IMPL.setRadius(this, n);
    }
    
    public void setShadowPadding(final int n, final int n2, final int n3, final int n4) {
        this.mShadowBounds.set(n, n2, n3, n4);
        super.setPadding(this.mContentPadding.left + n, this.mContentPadding.top + n2, this.mContentPadding.right + n3, this.mContentPadding.bottom + n4);
    }
    
    public void setUseCompatPadding(final boolean mCompatPadding) {
        if (this.mCompatPadding == mCompatPadding) {
            return;
        }
        this.mCompatPadding = mCompatPadding;
        CardView.IMPL.onCompatPaddingChanged(this);
    }
}
