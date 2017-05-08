// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.widget;

import android.view.ViewParent;
import android.text.TextUtils$TruncateAt;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.widget.Toast;
import android.view.accessibility.AccessibilityNodeInfo;
import android.annotation.TargetApi;
import android.support.v7.app.ActionBar;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View$OnLongClickListener;
import java.lang.ref.WeakReference;
import android.support.v7.internal.widget.TintManager;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.LinearLayout;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;
import java.util.Iterator;
import android.view.View$MeasureSpec;
import android.widget.LinearLayout$LayoutParams;
import android.support.v4.view.ViewCompat;
import android.view.ViewGroup$LayoutParams;
import android.view.animation.Animation;
import android.content.res.TypedArray;
import android.support.design.R;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import java.util.ArrayList;
import android.content.res.ColorStateList;
import android.view.View$OnClickListener;
import android.widget.HorizontalScrollView;

public class TabLayout extends HorizontalScrollView
{
    private static final int ANIMATION_DURATION = 300;
    private static final int DEFAULT_HEIGHT = 48;
    private static final int FIXED_WRAP_GUTTER_MIN = 16;
    public static final int GRAVITY_CENTER = 1;
    public static final int GRAVITY_FILL = 0;
    private static final int MAX_TAB_TEXT_LINES = 2;
    public static final int MODE_FIXED = 1;
    public static final int MODE_SCROLLABLE = 0;
    private static final int MOTION_NON_ADJACENT_OFFSET = 24;
    private static final int TAB_MIN_WIDTH_MARGIN = 56;
    private int mContentInsetStart;
    private int mMode;
    private OnTabSelectedListener mOnTabSelectedListener;
    private final int mRequestedTabMaxWidth;
    private Tab mSelectedTab;
    private final int mTabBackgroundResId;
    private View$OnClickListener mTabClickListener;
    private int mTabGravity;
    private int mTabMaxWidth;
    private final int mTabMinWidth;
    private int mTabPaddingBottom;
    private int mTabPaddingEnd;
    private int mTabPaddingStart;
    private int mTabPaddingTop;
    private final SlidingTabStrip mTabStrip;
    private int mTabTextAppearance;
    private ColorStateList mTabTextColors;
    private final ArrayList<Tab> mTabs;
    
    public TabLayout(final Context context) {
        this(context, null);
    }
    
    public TabLayout(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public TabLayout(final Context context, final AttributeSet set, int n) {
        super(context, set, n);
        this.mTabs = new ArrayList<Tab>();
        this.setHorizontalScrollBarEnabled(false);
        this.setFillViewport(true);
        this.addView((View)(this.mTabStrip = new SlidingTabStrip(context)), -2, -1);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.TabLayout, n, R.style.Widget_Design_TabLayout);
        this.mTabStrip.setSelectedIndicatorHeight(obtainStyledAttributes.getDimensionPixelSize(R.styleable.TabLayout_tabIndicatorHeight, 0));
        this.mTabStrip.setSelectedIndicatorColor(obtainStyledAttributes.getColor(R.styleable.TabLayout_tabIndicatorColor, 0));
        this.mTabTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.TabLayout_tabTextAppearance, R.style.TextAppearance_Design_Tab);
        n = obtainStyledAttributes.getDimensionPixelSize(R.styleable.TabLayout_tabPadding, 0);
        this.mTabPaddingBottom = n;
        this.mTabPaddingEnd = n;
        this.mTabPaddingTop = n;
        this.mTabPaddingStart = n;
        this.mTabPaddingStart = obtainStyledAttributes.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingStart, this.mTabPaddingStart);
        this.mTabPaddingTop = obtainStyledAttributes.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingTop, this.mTabPaddingTop);
        this.mTabPaddingEnd = obtainStyledAttributes.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingEnd, this.mTabPaddingEnd);
        this.mTabPaddingBottom = obtainStyledAttributes.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingBottom, this.mTabPaddingBottom);
        this.mTabTextColors = this.loadTextColorFromTextAppearance(this.mTabTextAppearance);
        if (obtainStyledAttributes.hasValue(R.styleable.TabLayout_tabTextColor)) {
            this.mTabTextColors = obtainStyledAttributes.getColorStateList(R.styleable.TabLayout_tabTextColor);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.TabLayout_tabSelectedTextColor)) {
            n = obtainStyledAttributes.getColor(R.styleable.TabLayout_tabSelectedTextColor, 0);
            this.mTabTextColors = createColorStateList(this.mTabTextColors.getDefaultColor(), n);
        }
        this.mTabMinWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.TabLayout_tabMinWidth, 0);
        this.mRequestedTabMaxWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.TabLayout_tabMaxWidth, 0);
        this.mTabBackgroundResId = obtainStyledAttributes.getResourceId(R.styleable.TabLayout_tabBackground, 0);
        this.mContentInsetStart = obtainStyledAttributes.getDimensionPixelSize(R.styleable.TabLayout_tabContentStart, 0);
        this.mMode = obtainStyledAttributes.getInt(R.styleable.TabLayout_tabMode, 1);
        this.mTabGravity = obtainStyledAttributes.getInt(R.styleable.TabLayout_tabGravity, 0);
        obtainStyledAttributes.recycle();
        this.applyModeAndGravity();
    }
    
    private void addTabView(final Tab tab, final int n, final boolean b) {
        final TabView tabView = this.createTabView(tab);
        this.mTabStrip.addView((View)tabView, n, (ViewGroup$LayoutParams)this.createLayoutParamsForTabs());
        if (b) {
            tabView.setSelected(true);
        }
    }
    
    private void addTabView(final Tab tab, final boolean b) {
        final TabView tabView = this.createTabView(tab);
        this.mTabStrip.addView((View)tabView, (ViewGroup$LayoutParams)this.createLayoutParamsForTabs());
        if (b) {
            tabView.setSelected(true);
        }
    }
    
    private void animateToTab(final int n) {
        this.clearAnimation();
        if (n == -1) {
            return;
        }
        if (this.getWindowToken() == null || !ViewCompat.isLaidOut((View)this)) {
            this.setScrollPosition(n, 0.0f, true);
            return;
        }
        final int scrollX = this.getScrollX();
        final int calculateScrollXForTab = this.calculateScrollXForTab(n, 0.0f);
        if (scrollX != calculateScrollXForTab) {
            final ValueAnimatorCompat animator = ViewUtils.createAnimator();
            animator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            animator.setDuration(300);
            animator.setIntValues(scrollX, calculateScrollXForTab);
            animator.setUpdateListener((ValueAnimatorCompat.AnimatorUpdateListener)new ValueAnimatorCompat.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(final ValueAnimatorCompat valueAnimatorCompat) {
                    TabLayout.this.scrollTo(valueAnimatorCompat.getAnimatedIntValue(), 0);
                }
            });
            animator.start();
        }
        this.mTabStrip.animateIndicatorToPosition(n, 300);
    }
    
    private void applyModeAndGravity() {
        int max = 0;
        if (this.mMode == 0) {
            max = Math.max(0, this.mContentInsetStart - this.mTabPaddingStart);
        }
        ViewCompat.setPaddingRelative((View)this.mTabStrip, max, 0, 0, 0);
        switch (this.mMode) {
            case 1: {
                this.mTabStrip.setGravity(1);
                break;
            }
            case 0: {
                this.mTabStrip.setGravity(8388611);
                break;
            }
        }
        this.updateTabViewsLayoutParams();
    }
    
    private int calculateScrollXForTab(int width, final float n) {
        int n2 = 0;
        final int n3 = 0;
        if (this.mMode == 0) {
            final View child = this.mTabStrip.getChildAt(width);
            View child2;
            if (width + 1 < this.mTabStrip.getChildCount()) {
                child2 = this.mTabStrip.getChildAt(width + 1);
            }
            else {
                child2 = null;
            }
            if (child != null) {
                width = child.getWidth();
            }
            else {
                width = 0;
            }
            int width2 = n3;
            if (child2 != null) {
                width2 = child2.getWidth();
            }
            n2 = (int)(child.getLeft() + (width + width2) * n * 0.5f + child.getWidth() * 0.5f - this.getWidth() * 0.5f);
        }
        return n2;
    }
    
    private void configureTab(final Tab tab, int i) {
        tab.setPosition(i);
        this.mTabs.add(i, tab);
        int size;
        for (size = this.mTabs.size(), ++i; i < size; ++i) {
            this.mTabs.get(i).setPosition(i);
        }
    }
    
    private static ColorStateList createColorStateList(final int n, int n2) {
        final int[][] array = new int[2][];
        final int[] array2 = new int[2];
        array[0] = TabLayout.SELECTED_STATE_SET;
        array2[0] = n2;
        n2 = 0 + 1;
        array[n2] = TabLayout.EMPTY_STATE_SET;
        array2[n2] = n;
        return new ColorStateList(array, array2);
    }
    
    private LinearLayout$LayoutParams createLayoutParamsForTabs() {
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(-2, -1);
        this.updateTabViewLayoutParams(linearLayout$LayoutParams);
        return linearLayout$LayoutParams;
    }
    
    private TabView createTabView(final Tab tab) {
        final TabView tabView = new TabView(this.getContext(), tab);
        tabView.setFocusable(true);
        if (this.mTabClickListener == null) {
            this.mTabClickListener = (View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    ((TabView)view).getTab().select();
                }
            };
        }
        tabView.setOnClickListener(this.mTabClickListener);
        return tabView;
    }
    
    private int dpToPx(final int n) {
        return Math.round(this.getResources().getDisplayMetrics().density * n);
    }
    
    private static boolean isAnimationRunning(final Animation animation) {
        return animation != null && animation.hasStarted() && !animation.hasEnded();
    }
    
    private ColorStateList loadTextColorFromTextAppearance(final int n) {
        final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(n, R.styleable.TextAppearance);
        try {
            return obtainStyledAttributes.getColorStateList(R.styleable.TextAppearance_android_textColor);
        }
        finally {
            obtainStyledAttributes.recycle();
        }
    }
    
    private void removeTabViewAt(final int n) {
        this.mTabStrip.removeViewAt(n);
        this.requestLayout();
    }
    
    private void setSelectedTabView(final int n) {
        for (int childCount = this.mTabStrip.getChildCount(), i = 0; i < childCount; ++i) {
            this.mTabStrip.getChildAt(i).setSelected(i == n);
        }
    }
    
    private void updateAllTabs() {
        for (int i = 0; i < this.mTabStrip.getChildCount(); ++i) {
            this.updateTab(i);
        }
    }
    
    private void updateTab(final int n) {
        final TabView tabView = (TabView)this.mTabStrip.getChildAt(n);
        if (tabView != null) {
            tabView.update();
        }
    }
    
    private void updateTabViewLayoutParams(final LinearLayout$LayoutParams linearLayout$LayoutParams) {
        if (this.mMode == 1 && this.mTabGravity == 0) {
            linearLayout$LayoutParams.width = 0;
            linearLayout$LayoutParams.weight = 1.0f;
            return;
        }
        linearLayout$LayoutParams.width = -2;
        linearLayout$LayoutParams.weight = 0.0f;
    }
    
    private void updateTabViewsLayoutParams() {
        for (int i = 0; i < this.mTabStrip.getChildCount(); ++i) {
            final View child = this.mTabStrip.getChildAt(i);
            this.updateTabViewLayoutParams((LinearLayout$LayoutParams)child.getLayoutParams());
            child.requestLayout();
        }
    }
    
    public void addTab(final Tab tab) {
        this.addTab(tab, this.mTabs.isEmpty());
    }
    
    public void addTab(final Tab tab, final int n) {
        this.addTab(tab, n, this.mTabs.isEmpty());
    }
    
    public void addTab(final Tab tab, final int n, final boolean b) {
        if (tab.mParent != this) {
            throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
        }
        this.addTabView(tab, n, b);
        this.configureTab(tab, n);
        if (b) {
            tab.select();
        }
    }
    
    public void addTab(final Tab tab, final boolean b) {
        if (tab.mParent != this) {
            throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
        }
        this.addTabView(tab, b);
        this.configureTab(tab, this.mTabs.size());
        if (b) {
            tab.select();
        }
    }
    
    public Tab getTabAt(final int n) {
        return this.mTabs.get(n);
    }
    
    public int getTabCount() {
        return this.mTabs.size();
    }
    
    public int getTabGravity() {
        return this.mTabGravity;
    }
    
    public int getTabMode() {
        return this.mMode;
    }
    
    public ColorStateList getTabTextColors() {
        return this.mTabTextColors;
    }
    
    public Tab newTab() {
        return new Tab(this);
    }
    
    protected void onMeasure(int measuredWidth, int n) {
        switch (View$MeasureSpec.getMode(n)) {
            case Integer.MIN_VALUE: {
                n = View$MeasureSpec.makeMeasureSpec(Math.min(this.dpToPx(48), View$MeasureSpec.getSize(n)), 1073741824);
                break;
            }
            case 0: {
                n = View$MeasureSpec.makeMeasureSpec(this.dpToPx(48), 1073741824);
                break;
            }
        }
        super.onMeasure(measuredWidth, n);
        if (this.mMode == 1 && this.getChildCount() == 1) {
            final View child = this.getChildAt(0);
            measuredWidth = this.getMeasuredWidth();
            if (child.getMeasuredWidth() > measuredWidth) {
                n = getChildMeasureSpec(n, this.getPaddingTop() + this.getPaddingBottom(), child.getLayoutParams().height);
                child.measure(View$MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824), n);
            }
        }
        n = this.mRequestedTabMaxWidth;
        final int n2 = this.getMeasuredWidth() - this.dpToPx(56);
        if (n == 0 || (measuredWidth = n) > n2) {
            measuredWidth = n2;
        }
        this.mTabMaxWidth = measuredWidth;
    }
    
    public void removeAllTabs() {
        this.mTabStrip.removeAllViews();
        final Iterator<Tab> iterator = this.mTabs.iterator();
        while (iterator.hasNext()) {
            iterator.next().setPosition(-1);
            iterator.remove();
        }
    }
    
    public void removeTab(final Tab tab) {
        if (tab.mParent != this) {
            throw new IllegalArgumentException("Tab does not belong to this TabLayout.");
        }
        this.removeTabAt(tab.getPosition());
    }
    
    public void removeTabAt(final int n) {
        int position;
        if (this.mSelectedTab != null) {
            position = this.mSelectedTab.getPosition();
        }
        else {
            position = 0;
        }
        this.removeTabViewAt(n);
        final Tab tab = this.mTabs.remove(n);
        if (tab != null) {
            tab.setPosition(-1);
        }
        for (int size = this.mTabs.size(), i = n; i < size; ++i) {
            this.mTabs.get(i).setPosition(i);
        }
        if (position == n) {
            Tab tab2;
            if (this.mTabs.isEmpty()) {
                tab2 = null;
            }
            else {
                tab2 = this.mTabs.get(Math.max(0, n - 1));
            }
            this.selectTab(tab2);
        }
    }
    
    void selectTab(final Tab mSelectedTab) {
        if (this.mSelectedTab == mSelectedTab) {
            if (this.mSelectedTab != null) {
                if (this.mOnTabSelectedListener != null) {
                    this.mOnTabSelectedListener.onTabReselected(this.mSelectedTab);
                }
                this.animateToTab(mSelectedTab.getPosition());
            }
        }
        else {
            int position;
            if (mSelectedTab != null) {
                position = mSelectedTab.getPosition();
            }
            else {
                position = -1;
            }
            this.setSelectedTabView(position);
            if ((this.mSelectedTab == null || this.mSelectedTab.getPosition() == -1) && position != -1) {
                this.setScrollPosition(position, 0.0f, true);
            }
            else {
                this.animateToTab(position);
            }
            if (this.mSelectedTab != null && this.mOnTabSelectedListener != null) {
                this.mOnTabSelectedListener.onTabUnselected(this.mSelectedTab);
            }
            this.mSelectedTab = mSelectedTab;
            if (this.mSelectedTab != null && this.mOnTabSelectedListener != null) {
                this.mOnTabSelectedListener.onTabSelected(this.mSelectedTab);
            }
        }
    }
    
    public void setOnTabSelectedListener(final OnTabSelectedListener mOnTabSelectedListener) {
        this.mOnTabSelectedListener = mOnTabSelectedListener;
    }
    
    public void setScrollPosition(final int n, final float n2, final boolean b) {
        if (!isAnimationRunning(this.getAnimation()) && n >= 0 && n < this.mTabStrip.getChildCount()) {
            this.mTabStrip.setIndicatorPositionFromTabPosition(n, n2);
            this.scrollTo(this.calculateScrollXForTab(n, n2), 0);
            if (b) {
                this.setSelectedTabView(Math.round(n + n2));
            }
        }
    }
    
    public void setTabGravity(final int mTabGravity) {
        if (this.mTabGravity != mTabGravity) {
            this.mTabGravity = mTabGravity;
            this.applyModeAndGravity();
        }
    }
    
    public void setTabMode(final int mMode) {
        if (mMode != this.mMode) {
            this.mMode = mMode;
            this.applyModeAndGravity();
        }
    }
    
    public void setTabTextColors(final int n, final int n2) {
        this.setTabTextColors(createColorStateList(n, n2));
    }
    
    public void setTabTextColors(final ColorStateList mTabTextColors) {
        if (this.mTabTextColors != mTabTextColors) {
            this.mTabTextColors = mTabTextColors;
            this.updateAllTabs();
        }
    }
    
    public void setTabsFromPagerAdapter(final PagerAdapter pagerAdapter) {
        this.removeAllTabs();
        for (int i = 0; i < pagerAdapter.getCount(); ++i) {
            this.addTab(this.newTab().setText(pagerAdapter.getPageTitle(i)));
        }
    }
    
    public void setupWithViewPager(final ViewPager viewPager) {
        final PagerAdapter adapter = viewPager.getAdapter();
        if (adapter == null) {
            throw new IllegalArgumentException("ViewPager does not have a PagerAdapter set");
        }
        this.setTabsFromPagerAdapter(adapter);
        viewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener)new TabLayoutOnPageChangeListener(this));
        this.setOnTabSelectedListener((OnTabSelectedListener)new ViewPagerOnTabSelectedListener(viewPager));
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }
    
    public interface OnTabSelectedListener
    {
        void onTabReselected(final Tab p0);
        
        void onTabSelected(final Tab p0);
        
        void onTabUnselected(final Tab p0);
    }
    
    private class SlidingTabStrip extends LinearLayout
    {
        private int mIndicatorLeft;
        private int mIndicatorRight;
        private int mSelectedIndicatorHeight;
        private final Paint mSelectedIndicatorPaint;
        private int mSelectedPosition;
        private float mSelectionOffset;
        
        SlidingTabStrip(final Context context) {
            super(context);
            this.mSelectedPosition = -1;
            this.mIndicatorLeft = -1;
            this.mIndicatorRight = -1;
            this.setWillNotDraw(false);
            this.mSelectedIndicatorPaint = new Paint();
        }
        
        private void setIndicatorPosition(final int mIndicatorLeft, final int mIndicatorRight) {
            if (mIndicatorLeft != this.mIndicatorLeft || mIndicatorRight != this.mIndicatorRight) {
                this.mIndicatorLeft = mIndicatorLeft;
                this.mIndicatorRight = mIndicatorRight;
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
        }
        
        private void updateIndicatorPosition() {
            final View child = this.getChildAt(this.mSelectedPosition);
            int n;
            int n2;
            if (child != null && child.getWidth() > 0) {
                final int left = child.getLeft();
                final int right = child.getRight();
                n = left;
                n2 = right;
                if (this.mSelectionOffset > 0.0f) {
                    n = left;
                    n2 = right;
                    if (this.mSelectedPosition < this.getChildCount() - 1) {
                        final View child2 = this.getChildAt(this.mSelectedPosition + 1);
                        n = (int)(this.mSelectionOffset * child2.getLeft() + (1.0f - this.mSelectionOffset) * left);
                        n2 = (int)(this.mSelectionOffset * child2.getRight() + (1.0f - this.mSelectionOffset) * right);
                    }
                }
            }
            else {
                n2 = -1;
                n = -1;
            }
            this.setIndicatorPosition(n, n2);
        }
        
        void animateIndicatorToPosition(final int n, final int duration) {
            final boolean b = ViewCompat.getLayoutDirection((View)this) == 1;
            final View child = this.getChildAt(n);
            final int left = child.getLeft();
            final int right = child.getRight();
            int mIndicatorLeft;
            int mIndicatorRight;
            if (Math.abs(n - this.mSelectedPosition) <= 1) {
                mIndicatorLeft = this.mIndicatorLeft;
                mIndicatorRight = this.mIndicatorRight;
            }
            else {
                final int access$1400 = TabLayout.this.dpToPx(24);
                if (n < this.mSelectedPosition) {
                    if (b) {
                        mIndicatorRight = (mIndicatorLeft = left - access$1400);
                    }
                    else {
                        mIndicatorRight = (mIndicatorLeft = right + access$1400);
                    }
                }
                else if (b) {
                    mIndicatorRight = (mIndicatorLeft = right + access$1400);
                }
                else {
                    mIndicatorRight = (mIndicatorLeft = left - access$1400);
                }
            }
            if (mIndicatorLeft != left || mIndicatorRight != right) {
                final ValueAnimatorCompat animator = ViewUtils.createAnimator();
                animator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                animator.setDuration(duration);
                animator.setFloatValues(0.0f, 1.0f);
                animator.setUpdateListener((ValueAnimatorCompat.AnimatorUpdateListener)new ValueAnimatorCompat.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(final ValueAnimatorCompat valueAnimatorCompat) {
                        final float animatedFraction = valueAnimatorCompat.getAnimatedFraction();
                        SlidingTabStrip.this.setIndicatorPosition(AnimationUtils.lerp(mIndicatorLeft, left, animatedFraction), AnimationUtils.lerp(mIndicatorRight, right, animatedFraction));
                    }
                });
                animator.setListener((ValueAnimatorCompat.AnimatorListener)new ValueAnimatorCompat.AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(final ValueAnimatorCompat valueAnimatorCompat) {
                        SlidingTabStrip.this.mSelectedPosition = n;
                        SlidingTabStrip.this.mSelectionOffset = 0.0f;
                    }
                    
                    @Override
                    public void onAnimationEnd(final ValueAnimatorCompat valueAnimatorCompat) {
                        SlidingTabStrip.this.mSelectedPosition = n;
                        SlidingTabStrip.this.mSelectionOffset = 0.0f;
                    }
                });
                animator.start();
            }
        }
        
        protected void onDraw(final Canvas canvas) {
            if (this.mIndicatorLeft >= 0 && this.mIndicatorRight > this.mIndicatorLeft) {
                canvas.drawRect((float)this.mIndicatorLeft, (float)(this.getHeight() - this.mSelectedIndicatorHeight), (float)this.mIndicatorRight, (float)this.getHeight(), this.mSelectedIndicatorPaint);
            }
        }
        
        protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
            super.onLayout(b, n, n2, n3, n4);
            if (!isAnimationRunning(this.getAnimation())) {
                this.updateIndicatorPosition();
            }
        }
        
        protected void onMeasure(final int n, final int n2) {
            super.onMeasure(n, n2);
            if (View$MeasureSpec.getMode(n) == 1073741824 && TabLayout.this.mMode == 1 && TabLayout.this.mTabGravity == 1) {
                final int childCount = this.getChildCount();
                final int measureSpec = View$MeasureSpec.makeMeasureSpec(0, 0);
                int max = 0;
                for (int i = 0; i < childCount; ++i) {
                    final View child = this.getChildAt(i);
                    child.measure(measureSpec, n2);
                    max = Math.max(max, child.getMeasuredWidth());
                }
                if (max > 0) {
                    if (max * childCount <= this.getMeasuredWidth() - TabLayout.this.dpToPx(16) * 2) {
                        for (int j = 0; j < childCount; ++j) {
                            final LinearLayout$LayoutParams linearLayout$LayoutParams = (LinearLayout$LayoutParams)this.getChildAt(j).getLayoutParams();
                            linearLayout$LayoutParams.width = max;
                            linearLayout$LayoutParams.weight = 0.0f;
                        }
                    }
                    else {
                        TabLayout.this.mTabGravity = 0;
                        TabLayout.this.updateTabViewsLayoutParams();
                    }
                    super.onMeasure(n, n2);
                }
            }
        }
        
        void setIndicatorPositionFromTabPosition(final int mSelectedPosition, final float mSelectionOffset) {
            if (isAnimationRunning(this.getAnimation())) {
                return;
            }
            this.mSelectedPosition = mSelectedPosition;
            this.mSelectionOffset = mSelectionOffset;
            this.updateIndicatorPosition();
        }
        
        void setSelectedIndicatorColor(final int color) {
            this.mSelectedIndicatorPaint.setColor(color);
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
        
        void setSelectedIndicatorHeight(final int mSelectedIndicatorHeight) {
            this.mSelectedIndicatorHeight = mSelectedIndicatorHeight;
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    public static final class Tab
    {
        public static final int INVALID_POSITION = -1;
        private CharSequence mContentDesc;
        private View mCustomView;
        private Drawable mIcon;
        private final TabLayout mParent;
        private int mPosition;
        private Object mTag;
        private CharSequence mText;
        
        Tab(final TabLayout mParent) {
            this.mPosition = -1;
            this.mParent = mParent;
        }
        
        public CharSequence getContentDescription() {
            return this.mContentDesc;
        }
        
        View getCustomView() {
            return this.mCustomView;
        }
        
        public Drawable getIcon() {
            return this.mIcon;
        }
        
        public int getPosition() {
            return this.mPosition;
        }
        
        public Object getTag() {
            return this.mTag;
        }
        
        public CharSequence getText() {
            return this.mText;
        }
        
        public void select() {
            this.mParent.selectTab(this);
        }
        
        public Tab setContentDescription(final int n) {
            return this.setContentDescription(this.mParent.getResources().getText(n));
        }
        
        public Tab setContentDescription(final CharSequence mContentDesc) {
            this.mContentDesc = mContentDesc;
            if (this.mPosition >= 0) {
                this.mParent.updateTab(this.mPosition);
            }
            return this;
        }
        
        public Tab setCustomView(final int n) {
            return this.setCustomView(LayoutInflater.from(this.mParent.getContext()).inflate(n, (ViewGroup)null));
        }
        
        public Tab setCustomView(final View mCustomView) {
            this.mCustomView = mCustomView;
            if (this.mPosition >= 0) {
                this.mParent.updateTab(this.mPosition);
            }
            return this;
        }
        
        public Tab setIcon(final int n) {
            return this.setIcon(TintManager.getDrawable(this.mParent.getContext(), n));
        }
        
        public Tab setIcon(final Drawable mIcon) {
            this.mIcon = mIcon;
            if (this.mPosition >= 0) {
                this.mParent.updateTab(this.mPosition);
            }
            return this;
        }
        
        void setPosition(final int mPosition) {
            this.mPosition = mPosition;
        }
        
        public Tab setTag(final Object mTag) {
            this.mTag = mTag;
            return this;
        }
        
        public Tab setText(final int n) {
            return this.setText(this.mParent.getResources().getText(n));
        }
        
        public Tab setText(final CharSequence mText) {
            this.mText = mText;
            if (this.mPosition >= 0) {
                this.mParent.updateTab(this.mPosition);
            }
            return this;
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface TabGravity {
    }
    
    public static class TabLayoutOnPageChangeListener implements OnPageChangeListener
    {
        private int mScrollState;
        private final WeakReference<TabLayout> mTabLayoutRef;
        
        public TabLayoutOnPageChangeListener(final TabLayout tabLayout) {
            this.mTabLayoutRef = new WeakReference<TabLayout>(tabLayout);
        }
        
        @Override
        public void onPageScrollStateChanged(final int mScrollState) {
            this.mScrollState = mScrollState;
        }
        
        @Override
        public void onPageScrolled(final int n, final float n2, final int n3) {
            boolean b = true;
            final TabLayout tabLayout = this.mTabLayoutRef.get();
            if (tabLayout != null) {
                if (this.mScrollState != 1) {
                    b = false;
                }
                tabLayout.setScrollPosition(n, n2, b);
            }
        }
        
        @Override
        public void onPageSelected(final int n) {
            final TabLayout tabLayout = this.mTabLayoutRef.get();
            if (tabLayout != null) {
                tabLayout.getTabAt(n).select();
            }
        }
    }
    
    class TabView extends LinearLayout implements View$OnLongClickListener
    {
        private View mCustomView;
        private ImageView mIconView;
        private final Tab mTab;
        private TextView mTextView;
        
        public TabView(final Context context, final Tab mTab) {
            super(context);
            this.mTab = mTab;
            if (TabLayout.this.mTabBackgroundResId != 0) {
                this.setBackgroundDrawable(TintManager.getDrawable(context, TabLayout.this.mTabBackgroundResId));
            }
            ViewCompat.setPaddingRelative((View)this, TabLayout.this.mTabPaddingStart, TabLayout.this.mTabPaddingTop, TabLayout.this.mTabPaddingEnd, TabLayout.this.mTabPaddingBottom);
            this.setGravity(17);
            this.update();
        }
        
        public Tab getTab() {
            return this.mTab;
        }
        
        @TargetApi(14)
        public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)ActionBar.Tab.class.getName());
        }
        
        @TargetApi(14)
        public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName((CharSequence)ActionBar.Tab.class.getName());
        }
        
        public boolean onLongClick(final View view) {
            final int[] array = new int[2];
            this.getLocationOnScreen(array);
            final Context context = this.getContext();
            final int width = this.getWidth();
            final int height = this.getHeight();
            final int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            final Toast text = Toast.makeText(context, this.mTab.getContentDescription(), 0);
            text.setGravity(49, array[0] + width / 2 - widthPixels / 2, height);
            text.show();
            return true;
        }
        
        public void onMeasure(final int n, final int n2) {
            super.onMeasure(n, n2);
            if (TabLayout.this.mTabMaxWidth != 0 && this.getMeasuredWidth() > TabLayout.this.mTabMaxWidth) {
                super.onMeasure(View$MeasureSpec.makeMeasureSpec(TabLayout.this.mTabMaxWidth, 1073741824), n2);
            }
            else if (TabLayout.this.mTabMinWidth > 0 && this.getMeasuredHeight() < TabLayout.this.mTabMinWidth) {
                super.onMeasure(View$MeasureSpec.makeMeasureSpec(TabLayout.this.mTabMinWidth, 1073741824), n2);
            }
        }
        
        public void setSelected(final boolean selected) {
            boolean b;
            if (this.isSelected() != selected) {
                b = true;
            }
            else {
                b = false;
            }
            super.setSelected(selected);
            if (b && selected) {
                this.sendAccessibilityEvent(4);
                if (this.mTextView != null) {
                    this.mTextView.setSelected(selected);
                }
                if (this.mIconView != null) {
                    this.mIconView.setSelected(selected);
                }
            }
        }
        
        final void update() {
            final Tab mTab = this.mTab;
            final View customView = mTab.getCustomView();
            if (customView != null) {
                final ViewParent parent = customView.getParent();
                if (parent != this) {
                    if (parent != null) {
                        ((ViewGroup)parent).removeView(customView);
                    }
                    this.addView(customView);
                }
                this.mCustomView = customView;
                if (this.mTextView != null) {
                    this.mTextView.setVisibility(8);
                }
                if (this.mIconView != null) {
                    this.mIconView.setVisibility(8);
                    this.mIconView.setImageDrawable((Drawable)null);
                }
                return;
            }
            if (this.mCustomView != null) {
                this.removeView(this.mCustomView);
                this.mCustomView = null;
            }
            final Drawable icon = mTab.getIcon();
            final CharSequence text = mTab.getText();
            if (icon != null) {
                if (this.mIconView == null) {
                    final ImageView mIconView = new ImageView(this.getContext());
                    final LinearLayout$LayoutParams layoutParams = new LinearLayout$LayoutParams(-2, -2);
                    layoutParams.gravity = 16;
                    mIconView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                    this.addView((View)mIconView, 0);
                    this.mIconView = mIconView;
                }
                this.mIconView.setImageDrawable(icon);
                this.mIconView.setVisibility(0);
            }
            else if (this.mIconView != null) {
                this.mIconView.setVisibility(8);
                this.mIconView.setImageDrawable((Drawable)null);
            }
            boolean b;
            if (!TextUtils.isEmpty(text)) {
                b = true;
            }
            else {
                b = false;
            }
            if (b) {
                if (this.mTextView == null) {
                    final AppCompatTextView mTextView = new AppCompatTextView(this.getContext());
                    mTextView.setTextAppearance(this.getContext(), TabLayout.this.mTabTextAppearance);
                    mTextView.setMaxLines(2);
                    mTextView.setEllipsize(TextUtils$TruncateAt.END);
                    mTextView.setGravity(17);
                    if (TabLayout.this.mTabTextColors != null) {
                        mTextView.setTextColor(TabLayout.this.mTabTextColors);
                    }
                    this.addView((View)mTextView, -2, -2);
                    this.mTextView = mTextView;
                }
                this.mTextView.setText(text);
                this.mTextView.setContentDescription(mTab.getContentDescription());
                this.mTextView.setVisibility(0);
            }
            else if (this.mTextView != null) {
                this.mTextView.setVisibility(8);
                this.mTextView.setText((CharSequence)null);
            }
            if (this.mIconView != null) {
                this.mIconView.setContentDescription(mTab.getContentDescription());
            }
            if (!b && !TextUtils.isEmpty(mTab.getContentDescription())) {
                this.setOnLongClickListener((View$OnLongClickListener)this);
                return;
            }
            this.setOnLongClickListener((View$OnLongClickListener)null);
            this.setLongClickable(false);
        }
    }
    
    public static class ViewPagerOnTabSelectedListener implements OnTabSelectedListener
    {
        private final ViewPager mViewPager;
        
        public ViewPagerOnTabSelectedListener(final ViewPager mViewPager) {
            this.mViewPager = mViewPager;
        }
        
        @Override
        public void onTabReselected(final Tab tab) {
        }
        
        @Override
        public void onTabSelected(final Tab tab) {
            this.mViewPager.setCurrentItem(tab.getPosition());
        }
        
        @Override
        public void onTabUnselected(final Tab tab) {
        }
    }
}
