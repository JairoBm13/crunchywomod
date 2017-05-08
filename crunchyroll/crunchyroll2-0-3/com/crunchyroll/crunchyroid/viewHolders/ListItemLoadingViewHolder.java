// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.viewHolders;

import android.view.animation.AnimationUtils;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.view.View;
import android.widget.ViewSwitcher;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

public class ListItemLoadingViewHolder extends ViewHolder
{
    private TextView mErrorInfo;
    private boolean mIsShake;
    public TextView mRetry;
    private LinearLayout mRetryLayout;
    private ViewSwitcher mViewSwitcher;
    
    public ListItemLoadingViewHolder(final View view) {
        super(view);
        this.mIsShake = false;
        this.mViewSwitcher = (ViewSwitcher)view.findViewById(2131624155);
        this.mErrorInfo = (TextView)view.findViewById(2131624296);
        this.mRetryLayout = (LinearLayout)view.findViewById(2131624297);
        (this.mRetry = (TextView)view.findViewById(2131624298)).setText((CharSequence)LocalizedStrings.RETRY.get().toUpperCase());
    }
    
    private void shake() {
        if (this.mIsShake) {
            this.mIsShake = false;
            this.mRetryLayout.startAnimation(AnimationUtils.loadAnimation(this.mRetryLayout.getContext(), 2130968592));
        }
    }
    
    public void setLayoutGravity(final int gravity) {
        this.mErrorInfo.setGravity(gravity);
    }
    
    public void setShake(final boolean mIsShake) {
        this.mIsShake = mIsShake;
    }
    
    public void showError(final String text) {
        this.mViewSwitcher.setDisplayedChild(1);
        this.mErrorInfo.setText((CharSequence)text);
        this.shake();
    }
    
    public void showProgress() {
        this.mViewSwitcher.setDisplayedChild(0);
    }
}
