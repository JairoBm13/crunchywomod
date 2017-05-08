// 
// Decompiled by Procyon v0.5.30
// 

package com.astuetz;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.view.View$BaseSavedState;
import android.os.Parcelable;
import android.graphics.Canvas;
import android.annotation.SuppressLint;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.os.Build$VERSION;
import android.widget.TextView;
import android.view.View$OnClickListener;
import android.widget.ImageButton;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.graphics.Paint$Style;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import com.astuetz.pagerslidingtabstrip.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.LinearLayout;
import android.graphics.Typeface;
import java.util.Locale;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout$LayoutParams;
import android.widget.HorizontalScrollView;

public class PagerSlidingTabStrip extends HorizontalScrollView
{
    private static final int[] ATTRS;
    private int currentPosition;
    private float currentPositionOffset;
    private LinearLayout$LayoutParams defaultTabLayoutParams;
    public ViewPager.OnPageChangeListener delegatePageListener;
    private int dividerColor;
    private int dividerPadding;
    private Paint dividerPaint;
    private int dividerWidth;
    private LinearLayout$LayoutParams expandedTabLayoutParams;
    private int indicatorColor;
    private int indicatorHeight;
    private int lastScrollX;
    private Locale locale;
    private final PageListener pageListener;
    private ViewPager pager;
    private Paint rectPaint;
    private int scrollOffset;
    private boolean shouldExpand;
    private int tabBackgroundResId;
    private int tabCount;
    private int tabPadding;
    private int tabTextColor;
    private int tabTextSize;
    private Typeface tabTypeface;
    private int tabTypefaceStyle;
    private LinearLayout tabsContainer;
    private boolean textAllCaps;
    private int underlineColor;
    private int underlineHeight;
    
    static {
        ATTRS = new int[] { 16842901, 16842904 };
    }
    
    public PagerSlidingTabStrip(final Context context) {
        this(context, null);
    }
    
    public PagerSlidingTabStrip(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public PagerSlidingTabStrip(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.pageListener = new PageListener();
        this.currentPosition = 0;
        this.currentPositionOffset = 0.0f;
        this.indicatorColor = -10066330;
        this.underlineColor = 436207616;
        this.dividerColor = 436207616;
        this.shouldExpand = false;
        this.textAllCaps = true;
        this.scrollOffset = 52;
        this.indicatorHeight = 8;
        this.underlineHeight = 2;
        this.dividerPadding = 12;
        this.tabPadding = 24;
        this.dividerWidth = 1;
        this.tabTextSize = 12;
        this.tabTextColor = -10066330;
        this.tabTypeface = null;
        this.tabTypefaceStyle = 1;
        this.lastScrollX = 0;
        this.tabBackgroundResId = R.drawable.background_tab;
        this.setFillViewport(true);
        this.setWillNotDraw(false);
        (this.tabsContainer = new LinearLayout(context)).setOrientation(0);
        this.tabsContainer.setLayoutParams((ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
        this.addView((View)this.tabsContainer);
        final DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        this.scrollOffset = (int)TypedValue.applyDimension(1, (float)this.scrollOffset, displayMetrics);
        this.indicatorHeight = (int)TypedValue.applyDimension(1, (float)this.indicatorHeight, displayMetrics);
        this.underlineHeight = (int)TypedValue.applyDimension(1, (float)this.underlineHeight, displayMetrics);
        this.dividerPadding = (int)TypedValue.applyDimension(1, (float)this.dividerPadding, displayMetrics);
        this.tabPadding = (int)TypedValue.applyDimension(1, (float)this.tabPadding, displayMetrics);
        this.dividerWidth = (int)TypedValue.applyDimension(1, (float)this.dividerWidth, displayMetrics);
        this.tabTextSize = (int)TypedValue.applyDimension(2, (float)this.tabTextSize, displayMetrics);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, PagerSlidingTabStrip.ATTRS);
        this.tabTextSize = obtainStyledAttributes.getDimensionPixelSize(0, this.tabTextSize);
        this.tabTextColor = obtainStyledAttributes.getColor(1, this.tabTextColor);
        obtainStyledAttributes.recycle();
        final TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(set, R.styleable.PagerSlidingTabStrip);
        this.indicatorColor = obtainStyledAttributes2.getColor(0, this.indicatorColor);
        this.underlineColor = obtainStyledAttributes2.getColor(1, this.underlineColor);
        this.dividerColor = obtainStyledAttributes2.getColor(2, this.dividerColor);
        this.indicatorHeight = obtainStyledAttributes2.getDimensionPixelSize(3, this.indicatorHeight);
        this.underlineHeight = obtainStyledAttributes2.getDimensionPixelSize(4, this.underlineHeight);
        this.dividerPadding = obtainStyledAttributes2.getDimensionPixelSize(5, this.dividerPadding);
        this.tabPadding = obtainStyledAttributes2.getDimensionPixelSize(6, this.tabPadding);
        this.tabBackgroundResId = obtainStyledAttributes2.getResourceId(8, this.tabBackgroundResId);
        this.shouldExpand = obtainStyledAttributes2.getBoolean(9, this.shouldExpand);
        this.scrollOffset = obtainStyledAttributes2.getDimensionPixelSize(7, this.scrollOffset);
        this.textAllCaps = obtainStyledAttributes2.getBoolean(10, this.textAllCaps);
        obtainStyledAttributes2.recycle();
        (this.rectPaint = new Paint()).setAntiAlias(true);
        this.rectPaint.setStyle(Paint$Style.FILL);
        (this.dividerPaint = new Paint()).setAntiAlias(true);
        this.dividerPaint.setStrokeWidth((float)this.dividerWidth);
        this.defaultTabLayoutParams = new LinearLayout$LayoutParams(-2, -1);
        this.expandedTabLayoutParams = new LinearLayout$LayoutParams(0, -1, 1.0f);
        if (this.locale == null) {
            this.locale = this.getResources().getConfiguration().locale;
        }
    }
    
    private void addIconTab(final int n, final int imageResource) {
        final ImageButton imageButton = new ImageButton(this.getContext());
        imageButton.setImageResource(imageResource);
        this.addTab(n, (View)imageButton);
    }
    
    private void addTab(final int n, final View view) {
        view.setFocusable(true);
        view.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                PagerSlidingTabStrip.this.pager.setCurrentItem(n);
            }
        });
        view.setPadding(this.tabPadding, 0, this.tabPadding, 0);
        final LinearLayout tabsContainer = this.tabsContainer;
        LinearLayout$LayoutParams linearLayout$LayoutParams;
        if (this.shouldExpand) {
            linearLayout$LayoutParams = this.expandedTabLayoutParams;
        }
        else {
            linearLayout$LayoutParams = this.defaultTabLayoutParams;
        }
        tabsContainer.addView(view, n, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
    }
    
    private void addTextTab(final int n, final String text) {
        final TextView textView = new TextView(this.getContext());
        textView.setText((CharSequence)text);
        textView.setGravity(17);
        textView.setSingleLine();
        this.addTab(n, (View)textView);
    }
    
    private void scrollToChild(int lastScrollX, final int n) {
        if (this.tabCount != 0) {
            final int n2 = this.tabsContainer.getChildAt(lastScrollX).getLeft() + n;
            Label_0039: {
                if (lastScrollX <= 0) {
                    lastScrollX = n2;
                    if (n <= 0) {
                        break Label_0039;
                    }
                }
                lastScrollX = n2 - this.scrollOffset;
            }
            if (lastScrollX != this.lastScrollX) {
                this.scrollTo(this.lastScrollX = lastScrollX, 0);
            }
        }
    }
    
    private void updateTabStyles() {
        for (int i = 0; i < this.tabCount; ++i) {
            final View child = this.tabsContainer.getChildAt(i);
            child.setBackgroundResource(this.tabBackgroundResId);
            if (child instanceof TextView) {
                final TextView textView = (TextView)child;
                textView.setTextSize(0, (float)this.tabTextSize);
                textView.setTypeface(this.tabTypeface, this.tabTypefaceStyle);
                textView.setTextColor(this.tabTextColor);
                if (this.textAllCaps) {
                    if (Build$VERSION.SDK_INT >= 14) {
                        textView.setAllCaps(true);
                    }
                    else {
                        textView.setText((CharSequence)textView.getText().toString().toUpperCase(this.locale));
                    }
                }
            }
        }
    }
    
    public void notifyDataSetChanged() {
        this.tabsContainer.removeAllViews();
        this.tabCount = this.pager.getAdapter().getCount();
        for (int i = 0; i < this.tabCount; ++i) {
            if (this.pager.getAdapter() instanceof IconTabProvider) {
                this.addIconTab(i, ((IconTabProvider)this.pager.getAdapter()).getPageIconResId(i));
            }
            else {
                this.addTextTab(i, this.pager.getAdapter().getPageTitle(i).toString());
            }
        }
        this.updateTabStyles();
        this.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
            @SuppressLint({ "NewApi" })
            public void onGlobalLayout() {
                if (Build$VERSION.SDK_INT < 16) {
                    PagerSlidingTabStrip.this.getViewTreeObserver().removeGlobalOnLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                }
                else {
                    PagerSlidingTabStrip.this.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                }
                PagerSlidingTabStrip.this.currentPosition = PagerSlidingTabStrip.this.pager.getCurrentItem();
                PagerSlidingTabStrip.this.scrollToChild(PagerSlidingTabStrip.this.currentPosition, 0);
            }
        });
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (!this.isInEditMode() && this.tabCount != 0) {
            final int height = this.getHeight();
            this.rectPaint.setColor(this.indicatorColor);
            final View child = this.tabsContainer.getChildAt(this.currentPosition);
            final float n = child.getLeft();
            final float n2 = child.getRight();
            float n3 = n;
            float n4 = n2;
            if (this.currentPositionOffset > 0.0f) {
                n3 = n;
                n4 = n2;
                if (this.currentPosition < this.tabCount - 1) {
                    final View child2 = this.tabsContainer.getChildAt(this.currentPosition + 1);
                    final float n5 = child2.getLeft();
                    final float n6 = child2.getRight();
                    n3 = this.currentPositionOffset * n5 + (1.0f - this.currentPositionOffset) * n;
                    n4 = this.currentPositionOffset * n6 + (1.0f - this.currentPositionOffset) * n2;
                }
            }
            canvas.drawRect(n3, (float)(height - this.indicatorHeight), n4, (float)height, this.rectPaint);
            this.rectPaint.setColor(this.underlineColor);
            canvas.drawRect(0.0f, (float)(height - this.underlineHeight), (float)this.tabsContainer.getWidth(), (float)height, this.rectPaint);
            this.dividerPaint.setColor(this.dividerColor);
            for (int i = 0; i < this.tabCount - 1; ++i) {
                final View child3 = this.tabsContainer.getChildAt(i);
                canvas.drawLine((float)child3.getRight(), (float)this.dividerPadding, (float)child3.getRight(), (float)(height - this.dividerPadding), this.dividerPaint);
            }
        }
    }
    
    public void onRestoreInstanceState(final Parcelable parcelable) {
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.currentPosition = savedState.currentPosition;
        this.requestLayout();
    }
    
    public Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.currentPosition = this.currentPosition;
        return (Parcelable)savedState;
    }
    
    public void setAllCaps(final boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }
    
    public void setDividerColor(final int dividerColor) {
        this.dividerColor = dividerColor;
        this.invalidate();
    }
    
    public void setDividerColorResource(final int n) {
        this.dividerColor = this.getResources().getColor(n);
        this.invalidate();
    }
    
    public void setDividerPadding(final int dividerPadding) {
        this.dividerPadding = dividerPadding;
        this.invalidate();
    }
    
    public void setIndicatorColor(final int indicatorColor) {
        this.indicatorColor = indicatorColor;
        this.invalidate();
    }
    
    public void setIndicatorColorResource(final int n) {
        this.indicatorColor = this.getResources().getColor(n);
        this.invalidate();
    }
    
    public void setIndicatorHeight(final int indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
        this.invalidate();
    }
    
    public void setOnPageChangeListener(final ViewPager.OnPageChangeListener delegatePageListener) {
        this.delegatePageListener = delegatePageListener;
    }
    
    public void setScrollOffset(final int scrollOffset) {
        this.scrollOffset = scrollOffset;
        this.invalidate();
    }
    
    public void setShouldExpand(final boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        this.requestLayout();
    }
    
    public void setTabBackground(final int tabBackgroundResId) {
        this.tabBackgroundResId = tabBackgroundResId;
    }
    
    public void setTabPaddingLeftRight(final int tabPadding) {
        this.tabPadding = tabPadding;
        this.updateTabStyles();
    }
    
    public void setTextColor(final int tabTextColor) {
        this.tabTextColor = tabTextColor;
        this.updateTabStyles();
    }
    
    public void setTextColorResource(final int n) {
        this.tabTextColor = this.getResources().getColor(n);
        this.updateTabStyles();
    }
    
    public void setTextSize(final int tabTextSize) {
        this.tabTextSize = tabTextSize;
        this.updateTabStyles();
    }
    
    public void setTypeface(final Typeface tabTypeface, final int tabTypefaceStyle) {
        this.tabTypeface = tabTypeface;
        this.tabTypefaceStyle = tabTypefaceStyle;
        this.updateTabStyles();
    }
    
    public void setUnderlineColor(final int underlineColor) {
        this.underlineColor = underlineColor;
        this.invalidate();
    }
    
    public void setUnderlineColorResource(final int n) {
        this.underlineColor = this.getResources().getColor(n);
        this.invalidate();
    }
    
    public void setUnderlineHeight(final int underlineHeight) {
        this.underlineHeight = underlineHeight;
        this.invalidate();
    }
    
    public void setViewPager(final ViewPager pager) {
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        pager.setOnPageChangeListener((ViewPager.OnPageChangeListener)this.pageListener);
        this.notifyDataSetChanged();
    }
    
    public interface IconTabProvider
    {
        int getPageIconResId(final int p0);
    }
    
    private class PageListener implements OnPageChangeListener
    {
        @Override
        public void onPageScrollStateChanged(final int n) {
            if (n == 0) {
                PagerSlidingTabStrip.this.scrollToChild(PagerSlidingTabStrip.this.pager.getCurrentItem(), 0);
            }
            if (PagerSlidingTabStrip.this.delegatePageListener != null) {
                PagerSlidingTabStrip.this.delegatePageListener.onPageScrollStateChanged(n);
            }
        }
        
        @Override
        public void onPageScrolled(final int n, final float n2, final int n3) {
            PagerSlidingTabStrip.this.currentPosition = n;
            PagerSlidingTabStrip.this.currentPositionOffset = n2;
            PagerSlidingTabStrip.this.scrollToChild(n, (int)(PagerSlidingTabStrip.this.tabsContainer.getChildAt(n).getWidth() * n2));
            PagerSlidingTabStrip.this.invalidate();
            if (PagerSlidingTabStrip.this.delegatePageListener != null) {
                PagerSlidingTabStrip.this.delegatePageListener.onPageScrolled(n, n2, n3);
            }
        }
        
        @Override
        public void onPageSelected(final int n) {
            if (PagerSlidingTabStrip.this.delegatePageListener != null) {
                PagerSlidingTabStrip.this.delegatePageListener.onPageSelected(n);
            }
        }
    }
    
    static class SavedState extends View$BaseSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        int currentPosition;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        private SavedState(final Parcel parcel) {
            super(parcel);
            this.currentPosition = parcel.readInt();
        }
        
        public SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.currentPosition);
        }
    }
}
