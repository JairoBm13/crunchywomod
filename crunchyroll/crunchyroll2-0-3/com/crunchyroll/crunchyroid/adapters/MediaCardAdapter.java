// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.adapters;

import android.widget.TextView;
import android.view.View;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.crunchyroll.crunchyroid.viewHolders.ListItemLoadingViewHolder;
import android.support.v7.widget.RecyclerView;

public abstract class MediaCardAdapter extends Adapter<ViewHolder>
{
    public static final int VIEW_LOADING = 1;
    public static final int VIEW_NORMAL = 0;
    protected ListItemLoadingViewHolder mListItemLoadingViewHolder;
    protected State mLoadingState;
    protected int mThumbwidth;
    
    public MediaCardAdapter() {
        this.mLoadingState = State.STATE_LOADING;
    }
    
    public void hideLoading() {
        this.mLoadingState = State.STATE_NO_LOADING;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        if (n == 0) {
            return new NormalViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903080, viewGroup, false));
        }
        if (this.mListItemLoadingViewHolder == null) {
            this.mListItemLoadingViewHolder = new ListItemLoadingViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903154, viewGroup, false));
        }
        return this.mListItemLoadingViewHolder;
    }
    
    public void setThumbwidth(final int mThumbwidth) {
        this.mThumbwidth = mThumbwidth;
    }
    
    public void showLoadingError(final String s) {
        this.mLoadingState = State.STATE_ERROR;
        if (this.mListItemLoadingViewHolder != null) {
            this.mListItemLoadingViewHolder.showError(s);
        }
    }
    
    public void showLoadingProgress() {
        this.mLoadingState = State.STATE_LOADING;
        if (this.mListItemLoadingViewHolder != null) {
            this.mListItemLoadingViewHolder.showProgress();
        }
    }
    
    public static class NormalViewHolder extends ViewHolder
    {
        public final ImageView image;
        public final View mediaProgress;
        public final View mediaProgressPercent;
        public final View mediaProgressRemainder;
        public final TextView mediaTitle;
        public final View menu;
        public final ImageView premiumIcon;
        public final TextView seriesTitle;
        
        public NormalViewHolder(final View view) {
            super(view);
            this.image = (ImageView)view.findViewById(2131624082);
            this.seriesTitle = (TextView)view.findViewById(2131624086);
            this.mediaTitle = (TextView)view.findViewById(2131624088);
            this.mediaProgress = view.findViewById(2131624083);
            this.mediaProgressPercent = view.findViewById(2131624084);
            this.mediaProgressRemainder = view.findViewById(2131624085);
            this.premiumIcon = (ImageView)view.findViewById(2131624087);
            this.menu = view.findViewById(2131624089);
        }
    }
    
    public enum State
    {
        STATE_ERROR, 
        STATE_LOADING, 
        STATE_NO_LOADING;
    }
}
