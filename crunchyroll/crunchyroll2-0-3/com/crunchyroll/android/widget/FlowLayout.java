// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.widget;

import android.content.res.TypedArray;
import android.view.View$MeasureSpec;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import com.crunchyroll.crunchyroid.R;
import android.util.AttributeSet;
import android.content.Context;
import java.util.ArrayList;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup
{
    public final int LeftToRightDirection;
    public final int RightToLeftDirection;
    private int mContentWidth;
    int mFlowDirection;
    int mGravity;
    int mSpacing;
    ArrayList<Integer> rowWidths;
    
    public FlowLayout(final Context context) {
        super(context);
        this.LeftToRightDirection = 0;
        this.RightToLeftDirection = 1;
        this.mSpacing = 0;
        this.mGravity = 3;
        this.rowWidths = new ArrayList<Integer>();
    }
    
    public FlowLayout(Context obtainStyledAttributes, final AttributeSet set) {
        super(obtainStyledAttributes, set);
        this.LeftToRightDirection = 0;
        this.RightToLeftDirection = 1;
        this.mSpacing = 0;
        this.mGravity = 3;
        this.rowWidths = new ArrayList<Integer>();
        if (!this.isInEditMode()) {
            this.mGravity = set.getAttributeUnsignedIntValue("http://schemas.android.com/apk/res/android", "gravity", this.mGravity);
        }
        obtainStyledAttributes = (Context)obtainStyledAttributes.obtainStyledAttributes(set, R.styleable.FlowLayout);
        try {
            this.mSpacing = ((TypedArray)obtainStyledAttributes).getDimensionPixelSize(0, 0);
            this.mFlowDirection = ((TypedArray)obtainStyledAttributes).getInt(1, 0);
        }
        finally {
            ((TypedArray)obtainStyledAttributes).recycle();
        }
    }
    
    public FlowLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.LeftToRightDirection = 0;
        this.RightToLeftDirection = 1;
        this.mSpacing = 0;
        this.mGravity = 3;
        this.rowWidths = new ArrayList<Integer>();
    }
    
    private void centerChildren(final ArrayList<Integer> list) {
        final int measuredWidth = this.getMeasuredWidth();
        final int childCount = this.getChildCount();
        int n = this.getPaddingTop();
        int n2 = 0;
        int n3 = Math.round((measuredWidth - list.get(0)) / 2);
        int n4;
        for (int i = 0; i < childCount; ++i, n2 = n4) {
            final View child = this.getChildAt(i);
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            n4 = n2;
            if (layoutParams.y != n) {
                n4 = n2 + 1;
                n3 = Math.round((measuredWidth - list.get(n4)) / 2);
            }
            layoutParams.x += n3;
            child.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            n = layoutParams.y;
        }
    }
    
    private void mirrorChildren(final ArrayList<Integer> list) {
        final int childCount = this.getChildCount();
        int n = this.getPaddingTop();
        int n2 = 0;
        int n3 = list.get(0);
        int n4;
        for (int i = 0; i < childCount; ++i, n2 = n4) {
            final View child = this.getChildAt(i);
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            n4 = n2;
            if (layoutParams.y != n) {
                n4 = n2 + 1;
                n3 = list.get(n4);
            }
            layoutParams.x = n3 - layoutParams.x - child.getMeasuredWidth();
            child.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            n = layoutParams.y;
        }
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return new LayoutParams(viewGroup$LayoutParams.width, viewGroup$LayoutParams.height);
    }
    
    public void onLayout(final boolean b, int i, int childCount, final int n, final int n2) {
        View child;
        LayoutParams layoutParams;
        for (childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            child = this.getChildAt(i);
            layoutParams = (LayoutParams)child.getLayoutParams();
            child.layout(layoutParams.x, layoutParams.y, layoutParams.x + child.getMeasuredWidth(), layoutParams.y + child.getMeasuredHeight());
        }
    }
    
    public void onMeasure(final int n, final int n2) {
        final int size = View$MeasureSpec.getSize(n);
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        this.mContentWidth = 0;
        int n3 = this.getPaddingTop();
        int paddingLeft2 = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();
        int n4;
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i, paddingTop = n4) {
            final View child = this.getChildAt(i);
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            this.measureChild(child, n, n2);
            final int measuredWidth = child.getMeasuredWidth();
            int paddingLeft3;
            if (paddingLeft2 > this.getPaddingLeft() && this.mSpacing + paddingLeft2 + measuredWidth > size - paddingLeft - paddingRight) {
                this.rowWidths.add(this.getPaddingRight() + paddingLeft2);
                n4 = n3 + this.mSpacing;
                paddingLeft3 = this.getPaddingLeft();
            }
            else {
                paddingLeft3 = paddingLeft2;
                n4 = paddingTop;
                if (paddingLeft2 > this.getPaddingLeft()) {
                    paddingLeft3 = paddingLeft2 + this.mSpacing;
                    n4 = paddingTop;
                }
            }
            layoutParams.x = paddingLeft3;
            layoutParams.y = n4;
            paddingLeft2 = layoutParams.x + measuredWidth;
            this.mContentWidth = Math.max(this.mContentWidth, paddingLeft2);
            n3 = Math.max(n3, child.getMeasuredHeight() + n4);
        }
        this.mContentWidth += this.getPaddingRight();
        final int paddingBottom = this.getPaddingBottom();
        this.rowWidths.add(this.getPaddingRight() + paddingLeft2);
        this.setMeasuredDimension(resolveSize(this.mContentWidth, n), resolveSize(n3 + paddingBottom, n2));
        if (this.mFlowDirection == 1) {
            this.mirrorChildren(this.rowWidths);
        }
        if (this.mGravity == 17 || this.mGravity == 1) {
            this.centerChildren(this.rowWidths);
        }
        this.rowWidths.clear();
    }
    
    public static class LayoutParams extends ViewGroup$LayoutParams
    {
        private int x;
        private int y;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
        }
    }
}
