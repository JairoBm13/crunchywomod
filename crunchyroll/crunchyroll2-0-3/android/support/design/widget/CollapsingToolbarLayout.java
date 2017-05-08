// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.widget;

import android.view.ViewGroup$MarginLayoutParams;
import android.support.v4.content.ContextCompat;
import android.support.annotation.DrawableRes;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable$Callback;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout$LayoutParams;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.content.res.TypedArray;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.design.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.graphics.Rect;
import android.support.v4.view.WindowInsetsCompat;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;

public class CollapsingToolbarLayout extends FrameLayout
{
    private static final int SCRIM_ANIMATION_DURATION = 600;
    private final CollapsingTextHelper mCollapsingTextHelper;
    private Drawable mContentScrim;
    private int mCurrentOffset;
    private View mDummyView;
    private int mExpandedMarginBottom;
    private int mExpandedMarginLeft;
    private int mExpandedMarginRight;
    private int mExpandedMarginTop;
    private WindowInsetsCompat mLastInsets;
    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener;
    private boolean mRefreshToolbar;
    private int mScrimAlpha;
    private ValueAnimatorCompat mScrimAnimator;
    private boolean mScrimsAreShown;
    private Drawable mStatusBarScrim;
    private final Rect mTmpRect;
    private Toolbar mToolbar;
    private int mToolbarId;
    
    public CollapsingToolbarLayout(final Context context) {
        this(context, null);
    }
    
    public CollapsingToolbarLayout(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public CollapsingToolbarLayout(final Context context, final AttributeSet set, int n) {
        final int n2 = 1;
        super(context, set, n);
        this.mRefreshToolbar = true;
        this.mTmpRect = new Rect();
        (this.mCollapsingTextHelper = new CollapsingTextHelper((View)this)).setExpandedTextVerticalGravity(80);
        this.mCollapsingTextHelper.setTextSizeInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.CollapsingToolbarLayout, n, R.style.Widget_Design_CollapsingToolbar);
        n = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMargin, 0);
        this.mExpandedMarginBottom = n;
        this.mExpandedMarginRight = n;
        this.mExpandedMarginTop = n;
        this.mExpandedMarginLeft = n;
        if (ViewCompat.getLayoutDirection((View)this) == 1) {
            n = n2;
        }
        else {
            n = 0;
        }
        if (obtainStyledAttributes.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart)) {
            final int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart, 0);
            if (n != 0) {
                this.mExpandedMarginRight = dimensionPixelSize;
            }
            else {
                this.mExpandedMarginLeft = dimensionPixelSize;
            }
        }
        if (obtainStyledAttributes.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd)) {
            final int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd, 0);
            if (n != 0) {
                this.mExpandedMarginLeft = dimensionPixelSize2;
            }
            else {
                this.mExpandedMarginRight = dimensionPixelSize2;
            }
        }
        if (obtainStyledAttributes.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop)) {
            this.mExpandedMarginTop = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop, 0);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom)) {
            this.mExpandedMarginBottom = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom, 0);
        }
        n = obtainStyledAttributes.getResourceId(R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance, R.style.TextAppearance_AppCompat_Title);
        this.mCollapsingTextHelper.setExpandedTextAppearance(n);
        n = obtainStyledAttributes.getResourceId(R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance, R.style.TextAppearance_AppCompat_Widget_ActionBar_Title);
        this.mCollapsingTextHelper.setCollapsedTextAppearance(n);
        this.setContentScrim(obtainStyledAttributes.getDrawable(R.styleable.CollapsingToolbarLayout_contentScrim));
        this.setStatusBarScrim(obtainStyledAttributes.getDrawable(R.styleable.CollapsingToolbarLayout_statusBarScrim));
        this.mToolbarId = obtainStyledAttributes.getResourceId(R.styleable.CollapsingToolbarLayout_toolbarId, -1);
        obtainStyledAttributes.recycle();
        this.setWillNotDraw(false);
        ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
                CollapsingToolbarLayout.this.mLastInsets = windowInsetsCompat;
                CollapsingToolbarLayout.this.requestLayout();
                return windowInsetsCompat.consumeSystemWindowInsets();
            }
        });
    }
    
    private void animateScrim(final int n) {
        this.ensureToolbar();
        if (this.mScrimAnimator == null) {
            (this.mScrimAnimator = ViewUtils.createAnimator()).setDuration(600);
            this.mScrimAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.mScrimAnimator.setUpdateListener((ValueAnimatorCompat.AnimatorUpdateListener)new ValueAnimatorCompat.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(final ValueAnimatorCompat valueAnimatorCompat) {
                    final int animatedIntValue = valueAnimatorCompat.getAnimatedIntValue();
                    if (animatedIntValue != CollapsingToolbarLayout.this.mScrimAlpha) {
                        if (CollapsingToolbarLayout.this.mContentScrim != null && CollapsingToolbarLayout.this.mToolbar != null) {
                            ViewCompat.postInvalidateOnAnimation((View)CollapsingToolbarLayout.this.mToolbar);
                        }
                        CollapsingToolbarLayout.this.mScrimAlpha = animatedIntValue;
                        ViewCompat.postInvalidateOnAnimation((View)CollapsingToolbarLayout.this);
                    }
                }
            });
        }
        else if (this.mScrimAnimator.isRunning()) {
            this.mScrimAnimator.cancel();
        }
        this.mScrimAnimator.setIntValues(this.mScrimAlpha, n);
        this.mScrimAnimator.start();
    }
    
    private void ensureToolbar() {
        if (!this.mRefreshToolbar) {
            return;
        }
        Toolbar toolbar = null;
        final Toolbar toolbar2 = null;
        int n = 0;
        final int childCount = this.getChildCount();
        while (true) {
            View child = null;
            Label_0153: {
                Toolbar toolbar3;
                while (true) {
                    toolbar3 = toolbar2;
                    if (n >= childCount) {
                        break;
                    }
                    child = this.getChildAt(n);
                    Toolbar toolbar4 = toolbar;
                    if (child instanceof Toolbar) {
                        if (this.mToolbarId == -1) {
                            break Label_0153;
                        }
                        if (this.mToolbarId == child.getId()) {
                            toolbar3 = (Toolbar)child;
                            break;
                        }
                        if ((toolbar4 = toolbar) == null) {
                            toolbar4 = (Toolbar)child;
                        }
                    }
                    ++n;
                    toolbar = toolbar4;
                }
                Toolbar mToolbar;
                if ((mToolbar = toolbar3) == null) {
                    mToolbar = toolbar;
                }
                if (mToolbar != null) {
                    this.mToolbar = mToolbar;
                    this.mDummyView = new View(this.getContext());
                    this.mToolbar.addView(this.mDummyView, -1, -1);
                }
                else {
                    this.mToolbar = null;
                    this.mDummyView = null;
                }
                this.mRefreshToolbar = false;
                return;
            }
            Toolbar toolbar3 = (Toolbar)child;
            continue;
        }
    }
    
    private static ViewOffsetHelper getViewOffsetHelper(final View view) {
        ViewOffsetHelper viewOffsetHelper;
        if ((viewOffsetHelper = (ViewOffsetHelper)view.getTag(R.id.view_offset_helper)) == null) {
            viewOffsetHelper = new ViewOffsetHelper(view);
            view.setTag(R.id.view_offset_helper, (Object)viewOffsetHelper);
        }
        return viewOffsetHelper;
    }
    
    private void hideScrim() {
        if (this.mScrimsAreShown) {
            this.animateScrim(0);
            this.mScrimsAreShown = false;
        }
    }
    
    private void showScrim() {
        if (!this.mScrimsAreShown) {
            this.animateScrim(255);
            this.mScrimsAreShown = true;
        }
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        this.ensureToolbar();
        if (this.mToolbar == null && this.mContentScrim != null && this.mScrimAlpha > 0) {
            this.mContentScrim.mutate().setAlpha(this.mScrimAlpha);
            this.mContentScrim.draw(canvas);
        }
        this.mCollapsingTextHelper.draw(canvas);
        if (this.mStatusBarScrim != null && this.mScrimAlpha > 0) {
            int systemWindowInsetTop;
            if (this.mLastInsets != null) {
                systemWindowInsetTop = this.mLastInsets.getSystemWindowInsetTop();
            }
            else {
                systemWindowInsetTop = 0;
            }
            if (systemWindowInsetTop > 0) {
                this.mStatusBarScrim.setBounds(0, -this.mCurrentOffset, this.getWidth(), systemWindowInsetTop - this.mCurrentOffset);
                this.mStatusBarScrim.mutate().setAlpha(this.mScrimAlpha);
                this.mStatusBarScrim.draw(canvas);
            }
        }
    }
    
    protected boolean drawChild(final Canvas canvas, final View view, final long n) {
        this.ensureToolbar();
        if (view == this.mToolbar && this.mContentScrim != null && this.mScrimAlpha > 0) {
            this.mContentScrim.mutate().setAlpha(this.mScrimAlpha);
            this.mContentScrim.draw(canvas);
        }
        return super.drawChild(canvas, view, n);
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(super.generateDefaultLayoutParams());
    }
    
    public FrameLayout$LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected FrameLayout$LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return new LayoutParams(viewGroup$LayoutParams);
    }
    
    public Drawable getContentScrim() {
        return this.mContentScrim;
    }
    
    final int getScrimTriggerOffset() {
        return ViewCompat.getMinimumHeight((View)this) * 2;
    }
    
    public Drawable getStatusBarScrim() {
        return this.mStatusBarScrim;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        final ViewParent parent = this.getParent();
        if (parent instanceof AppBarLayout) {
            if (this.mOnOffsetChangedListener == null) {
                this.mOnOffsetChangedListener = new OffsetUpdateListener();
            }
            ((AppBarLayout)parent).addOnOffsetChangedListener(this.mOnOffsetChangedListener);
        }
    }
    
    protected void onDetachedFromWindow() {
        final ViewParent parent = this.getParent();
        if (this.mOnOffsetChangedListener != null && parent instanceof AppBarLayout) {
            ((AppBarLayout)parent).removeOnOffsetChangedListener(this.mOnOffsetChangedListener);
        }
        super.onDetachedFromWindow();
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        for (int i = 0; i < this.getChildCount(); ++i) {
            final View child = this.getChildAt(i);
            if (this.mLastInsets != null && !ViewCompat.getFitsSystemWindows(child)) {
                final int systemWindowInsetTop = this.mLastInsets.getSystemWindowInsetTop();
                if (child.getTop() < systemWindowInsetTop) {
                    child.offsetTopAndBottom(systemWindowInsetTop);
                }
            }
            getViewOffsetHelper(child).onViewLayout();
        }
        this.mCollapsingTextHelper.onLayout(b, n, n2, n3, n4);
        this.ensureToolbar();
        if (this.mDummyView != null) {
            ViewGroupUtils.getDescendantRect((ViewGroup)this, this.mDummyView, this.mTmpRect);
            this.mCollapsingTextHelper.setCollapsedBounds(this.mTmpRect.left, n4 - this.mTmpRect.height(), this.mTmpRect.right, n4);
            this.mCollapsingTextHelper.setExpandedBounds(this.mExpandedMarginLeft + n, this.mTmpRect.bottom + this.mExpandedMarginTop, n3 - this.mExpandedMarginRight, n4 - this.mExpandedMarginBottom);
        }
        if (this.mToolbar != null) {
            this.setMinimumHeight(this.mToolbar.getHeight());
        }
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (this.mContentScrim != null) {
            this.mContentScrim.setBounds(0, 0, n, n2);
        }
    }
    
    public void setCollapsedTitleTextAppearance(final int collapsedTextAppearance) {
        this.mCollapsingTextHelper.setCollapsedTextAppearance(collapsedTextAppearance);
    }
    
    public void setCollapsedTitleTextColor(final int collapsedTextColor) {
        this.mCollapsingTextHelper.setCollapsedTextColor(collapsedTextColor);
    }
    
    public void setContentScrim(@Nullable final Drawable mContentScrim) {
        if (this.mContentScrim != mContentScrim) {
            if (this.mContentScrim != null) {
                this.mContentScrim.setCallback((Drawable$Callback)null);
            }
            (this.mContentScrim = mContentScrim).setBounds(0, 0, this.getWidth(), this.getHeight());
            mContentScrim.setCallback((Drawable$Callback)this);
            mContentScrim.mutate().setAlpha(this.mScrimAlpha);
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    public void setContentScrimColor(final int n) {
        this.setContentScrim((Drawable)new ColorDrawable(n));
    }
    
    public void setContentScrimResource(@DrawableRes final int n) {
        this.setContentScrim(ContextCompat.getDrawable(this.getContext(), n));
    }
    
    public void setExpandedTitleColor(final int expandedTextColor) {
        this.mCollapsingTextHelper.setExpandedTextColor(expandedTextColor);
    }
    
    public void setExpandedTitleTextAppearance(final int expandedTextAppearance) {
        this.mCollapsingTextHelper.setExpandedTextAppearance(expandedTextAppearance);
    }
    
    public void setStatusBarScrim(@Nullable final Drawable mStatusBarScrim) {
        if (this.mStatusBarScrim != mStatusBarScrim) {
            if (this.mStatusBarScrim != null) {
                this.mStatusBarScrim.setCallback((Drawable$Callback)null);
            }
            (this.mStatusBarScrim = mStatusBarScrim).setCallback((Drawable$Callback)this);
            mStatusBarScrim.mutate().setAlpha(this.mScrimAlpha);
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    public void setStatusBarScrimColor(final int n) {
        this.setStatusBarScrim((Drawable)new ColorDrawable(n));
    }
    
    public void setStatusBarScrimResource(@DrawableRes final int n) {
        this.setStatusBarScrim(ContextCompat.getDrawable(this.getContext(), n));
    }
    
    public void setTitle(final CharSequence text) {
        this.mCollapsingTextHelper.setText(text);
    }
    
    public static class LayoutParams extends FrameLayout$LayoutParams
    {
        public static final int COLLAPSE_MODE_OFF = 0;
        public static final int COLLAPSE_MODE_PARALLAX = 2;
        public static final int COLLAPSE_MODE_PIN = 1;
        private static final float DEFAULT_PARALLAX_MULTIPLIER = 0.5f;
        int mCollapseMode;
        float mParallaxMult;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.mCollapseMode = 0;
            this.mParallaxMult = 0.5f;
        }
        
        public LayoutParams(final int n, final int n2, final int n3) {
            super(n, n2, n3);
            this.mCollapseMode = 0;
            this.mParallaxMult = 0.5f;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.mCollapseMode = 0;
            this.mParallaxMult = 0.5f;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.CollapsingAppBarLayout_LayoutParams);
            this.mCollapseMode = obtainStyledAttributes.getInt(R.styleable.CollapsingAppBarLayout_LayoutParams_layout_collapseMode, 0);
            this.setParallaxMultiplier(obtainStyledAttributes.getFloat(R.styleable.CollapsingAppBarLayout_LayoutParams_layout_collapseParallaxMultiplier, 0.5f));
            obtainStyledAttributes.recycle();
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.mCollapseMode = 0;
            this.mParallaxMult = 0.5f;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.mCollapseMode = 0;
            this.mParallaxMult = 0.5f;
        }
        
        public LayoutParams(final FrameLayout$LayoutParams frameLayout$LayoutParams) {
            super(frameLayout$LayoutParams);
            this.mCollapseMode = 0;
            this.mParallaxMult = 0.5f;
        }
        
        public int getCollapseMode() {
            return this.mCollapseMode;
        }
        
        public float getParallaxMultiplier() {
            return this.mParallaxMult;
        }
        
        public void setCollapseMode(final int mCollapseMode) {
            this.mCollapseMode = mCollapseMode;
        }
        
        public void setParallaxMultiplier(final float mParallaxMult) {
            this.mParallaxMult = mParallaxMult;
        }
    }
    
    private class OffsetUpdateListener implements OnOffsetChangedListener
    {
        @Override
        public void onOffsetChanged(final AppBarLayout appBarLayout, final int n) {
            CollapsingToolbarLayout.this.mCurrentOffset = n;
            int systemWindowInsetTop;
            if (CollapsingToolbarLayout.this.mLastInsets != null) {
                systemWindowInsetTop = CollapsingToolbarLayout.this.mLastInsets.getSystemWindowInsetTop();
            }
            else {
                systemWindowInsetTop = 0;
            }
            final int totalScrollRange = appBarLayout.getTotalScrollRange();
            for (int i = 0; i < CollapsingToolbarLayout.this.getChildCount(); ++i) {
                final View child = CollapsingToolbarLayout.this.getChildAt(i);
                final CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams)child.getLayoutParams();
                final ViewOffsetHelper access$600 = getViewOffsetHelper(child);
                switch (layoutParams.mCollapseMode) {
                    case 1: {
                        if (CollapsingToolbarLayout.this.getHeight() - systemWindowInsetTop + n >= child.getHeight()) {
                            access$600.setTopAndBottomOffset(-n);
                            break;
                        }
                        break;
                    }
                    case 2: {
                        access$600.setTopAndBottomOffset(Math.round(-n * layoutParams.mParallaxMult));
                        break;
                    }
                }
            }
            if (CollapsingToolbarLayout.this.mContentScrim != null || CollapsingToolbarLayout.this.mStatusBarScrim != null) {
                if (CollapsingToolbarLayout.this.getHeight() + n < CollapsingToolbarLayout.this.getScrimTriggerOffset() + systemWindowInsetTop) {
                    CollapsingToolbarLayout.this.showScrim();
                }
                else {
                    CollapsingToolbarLayout.this.hideScrim();
                }
            }
            if (CollapsingToolbarLayout.this.mStatusBarScrim != null && systemWindowInsetTop > 0) {
                ViewCompat.postInvalidateOnAnimation((View)CollapsingToolbarLayout.this);
            }
            CollapsingToolbarLayout.this.mCollapsingTextHelper.setExpansionFraction(Math.abs(n) / (CollapsingToolbarLayout.this.getHeight() - ViewCompat.getMinimumHeight((View)CollapsingToolbarLayout.this) - systemWindowInsetTop));
            if (Math.abs(n) == totalScrollRange) {
                ViewCompat.setElevation((View)appBarLayout, appBarLayout.getTargetElevation());
                return;
            }
            ViewCompat.setElevation((View)appBarLayout, 0.0f);
        }
    }
}
