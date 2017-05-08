// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.widget;

import android.support.v4.view.ViewCompat;
import android.os.Build$VERSION;
import android.view.View;

class ViewOffsetHelper
{
    private int mLayoutLeft;
    private int mLayoutTop;
    private int mOffsetLeft;
    private int mOffsetTop;
    private final View mView;
    
    public ViewOffsetHelper(final View mView) {
        this.mView = mView;
    }
    
    private void updateOffsets() {
        if (Build$VERSION.SDK_INT == 22) {
            ViewCompat.setTranslationY(this.mView, this.mOffsetTop);
            ViewCompat.setTranslationX(this.mView, this.mOffsetLeft);
            return;
        }
        ViewCompat.offsetTopAndBottom(this.mView, this.mOffsetTop - this.mView.getTop() - this.mLayoutTop);
        ViewCompat.offsetLeftAndRight(this.mView, this.mOffsetLeft - this.mView.getLeft() - this.mLayoutLeft);
    }
    
    public int getLeftAndRightOffset() {
        return this.mOffsetLeft;
    }
    
    public int getTopAndBottomOffset() {
        return this.mOffsetTop;
    }
    
    public void onViewLayout() {
        this.mLayoutTop = this.mView.getTop();
        this.mLayoutLeft = this.mView.getLeft();
        this.updateOffsets();
    }
    
    public boolean setLeftAndRightOffset(final int mOffsetLeft) {
        if (this.mOffsetLeft != mOffsetLeft) {
            this.mOffsetLeft = mOffsetLeft;
            this.updateOffsets();
            return true;
        }
        return false;
    }
    
    public boolean setTopAndBottomOffset(final int mOffsetTop) {
        if (this.mOffsetTop != mOffsetTop) {
            this.mOffsetTop = mOffsetTop;
            this.updateOffsets();
            return true;
        }
        return false;
    }
}
