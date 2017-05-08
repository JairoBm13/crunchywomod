// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.util;

import android.view.ViewGroup;
import android.content.Context;
import android.app.Activity;

public class ProgressBarManager
{
    private Activity mActivity;
    private int mBgColor;
    private Context mContext;
    private int mCount;
    private ViewGroup mViewGroup;
    
    public ProgressBarManager(final Activity mActivity, final int mBgColor) {
        this.mActivity = mActivity;
        this.mBgColor = mBgColor;
        this.mCount = 0;
    }
    
    public ProgressBarManager(final Context mContext, final ViewGroup mViewGroup, final int mBgColor) {
        this.mContext = mContext;
        this.mViewGroup = mViewGroup;
        this.mBgColor = mBgColor;
        this.mCount = 0;
    }
    
    public void hide() {
        synchronized (this) {
            --this.mCount;
            if (this.mCount == 0) {
                if (this.mViewGroup == null) {
                    Util.hideProgressBar(this.mActivity);
                }
                else {
                    Util.hideProgressBar(this.mContext, this.mViewGroup);
                }
            }
            if (this.mCount < 0) {
                this.mCount = 0;
            }
        }
    }
    
    public void show() {
        synchronized (this) {
            if (this.mCount == 0) {
                if (this.mViewGroup == null) {
                    Util.showProgressBar(this.mActivity, this.mBgColor);
                }
                else {
                    Util.showProgressBar(this.mContext, this.mViewGroup, this.mBgColor);
                }
            }
            ++this.mCount;
        }
    }
}
