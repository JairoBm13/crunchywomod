// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.res.TypedArray;
import com.crunchyroll.crunchyroid.activities.SeriesDetailActivity;
import com.crunchyroll.crunchyroid.activities.PopupActivity;
import com.crunchyroll.crunchyroid.util.ActionsUtil;
import android.view.MenuItem;
import android.widget.PopupMenu$OnMenuItemClickListener;
import android.widget.PopupMenu;
import com.crunchyroll.crunchyroid.util.Functional;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import com.crunchyroll.crunchyroid.fragments.EpisodeListBaseFragment;
import com.google.common.base.Optional;
import com.crunchyroll.crunchyroid.app.WatchStatus;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.crunchyroid.events.ErrorEvent;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import com.crunchyroll.crunchyroid.app.PrepareToWatch;
import com.crunchyroll.android.api.tasks.BaseListener;
import android.app.Activity;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.view.View;
import com.crunchyroll.android.api.models.Series;
import com.crunchyroll.android.api.models.Media;
import android.view.View$OnClickListener;
import com.crunchyroll.crunchyroid.events.LoadMore;
import de.greenrobot.event.EventBus;
import com.crunchyroll.crunchyroid.viewHolders.ListItemLoadingViewHolder;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import com.crunchyroll.android.api.models.RecentlyWatchedItem;
import java.util.List;
import android.support.v7.widget.GridLayoutManager;
import android.support.v4.app.FragmentActivity;

public class HistoryAdapter extends MediaCardAdapter
{
    private FragmentActivity mActivity;
    private GridLayoutManager mGridLayoutManager;
    private List<RecentlyWatchedItem> mHistoryItems;
    
    public HistoryAdapter(final FragmentActivity mActivity, final List<RecentlyWatchedItem> mHistoryItems, final int n) {
        this.mActivity = mActivity;
        this.mHistoryItems = mHistoryItems;
        (this.mGridLayoutManager = new GridLayoutManager((Context)this.mActivity, n)).setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(final int n) {
                if (n == HistoryAdapter.this.mHistoryItems.size()) {
                    return HistoryAdapter.this.mGridLayoutManager.getSpanCount();
                }
                return 1;
            }
        });
    }
    
    public GridLayoutManager getGridLayoutManager() {
        return this.mGridLayoutManager;
    }
    
    @Override
    public int getItemCount() {
        if (this.mHistoryItems.isEmpty()) {
            return 0;
        }
        if (this.mLoadingState != State.STATE_NO_LOADING) {
            return this.mHistoryItems.size() + 1;
        }
        return this.mHistoryItems.size();
    }
    
    @Override
    public int getItemViewType(final int n) {
        if (n == this.mHistoryItems.size()) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int resourceId) {
        if (viewHolder instanceof ListItemLoadingViewHolder) {
            if (this.mLoadingState == State.STATE_LOADING) {
                this.mListItemLoadingViewHolder.setShake(false);
                EventBus.getDefault().post(new LoadMore.HistoryEvent());
            }
        }
        else {
            final RecentlyWatchedItem recentlyWatchedItem = this.mHistoryItems.get(resourceId);
            if (recentlyWatchedItem != null) {
                final Series series = recentlyWatchedItem.getSeries();
                final Media media = recentlyWatchedItem.getMedia();
                viewHolder.itemView.setFocusable(true);
                final TypedArray obtainStyledAttributes = this.mActivity.obtainStyledAttributes(new int[] { 2130772199 });
                resourceId = obtainStyledAttributes.getResourceId(0, 0);
                viewHolder.itemView.setBackgroundResource(resourceId);
                obtainStyledAttributes.recycle();
                viewHolder.itemView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        boolean b = false;
                        if (!CrunchyrollApplication.getApp((Context)HistoryAdapter.this.mActivity).isPrepareToWatchLoading()) {
                            final PrepareToWatch prepareToWatch = CrunchyrollApplication.getApp((Context)HistoryAdapter.this.mActivity).prepareToWatch(HistoryAdapter.this.mActivity, media, false, 0);
                            prepareToWatch.prepare(new BaseListener<Void>() {
                                @Override
                                public void onException(final Exception ex) {
                                    if (ex instanceof ApiNetworkException) {
                                        EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_NETWORK.get()));
                                        return;
                                    }
                                    EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_UNKNOWN.get()));
                                }
                                
                                @Override
                                public void onFinally() {
                                    Util.hideProgressBar(HistoryAdapter.this.mActivity);
                                }
                                
                                @Override
                                public void onPreExecute() {
                                    Util.showProgressBar(HistoryAdapter.this.mActivity, HistoryAdapter.this.mActivity.getResources().getColor(2131558518));
                                }
                                
                                @Override
                                public void onSuccess(final Void void1) {
                                    prepareToWatch.go(PrepareToWatch.Event.NONE);
                                }
                            });
                            Tracker.screenEpisodeSelected("home-history", series.getName(), "episode-" + media.getEpisodeNumber());
                            final String name = series.getName();
                            final String string = "episode-" + media.getEpisodeNumber();
                            if (WatchStatus.getVideoType(media) == 2) {
                                b = true;
                            }
                            Tracker.episodeSelected(name, string, b);
                        }
                    }
                });
                EpisodeListBaseFragment.loadImageIntoImageView((Context)this.mActivity, Optional.of(media), ((NormalViewHolder)viewHolder).image, this.mThumbwidth);
                resourceId = (int)(media.getPercentWatched() * 100.0);
                if (resourceId == 0) {
                    ((NormalViewHolder)viewHolder).mediaProgress.setVisibility(8);
                }
                else {
                    ((NormalViewHolder)viewHolder).mediaProgress.setVisibility(0);
                    ((NormalViewHolder)viewHolder).mediaProgressPercent.setLayoutParams((ViewGroup$LayoutParams)new LinearLayout$LayoutParams(0, -1, (float)resourceId));
                    ((NormalViewHolder)viewHolder).mediaProgressRemainder.setLayoutParams((ViewGroup$LayoutParams)new LinearLayout$LayoutParams(0, -1, (float)(100 - resourceId)));
                }
                final TextView seriesTitle = ((NormalViewHolder)viewHolder).seriesTitle;
                String name;
                if (series != null) {
                    name = series.getName();
                }
                else {
                    name = null;
                }
                seriesTitle.setText((CharSequence)name);
                ((NormalViewHolder)viewHolder).mediaTitle.setText((CharSequence)Functional.Media.getEpisodeSubtitle(media));
                if (WatchStatus.getVideoType(media) == 2) {
                    ((NormalViewHolder)viewHolder).premiumIcon.setVisibility(0);
                }
                else {
                    ((NormalViewHolder)viewHolder).premiumIcon.setVisibility(8);
                }
                ((NormalViewHolder)viewHolder).menu.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        final PopupMenu popupMenu = new PopupMenu((Context)HistoryAdapter.this.mActivity, view);
                        popupMenu.getMenuInflater().inflate(2131689472, popupMenu.getMenu());
                        popupMenu.getMenu().getItem(0).setTitle((CharSequence)LocalizedStrings.PLAY.get());
                        popupMenu.getMenu().getItem(1).setTitle((CharSequence)LocalizedStrings.SHARE.get());
                        popupMenu.getMenu().getItem(2).setTitle((CharSequence)LocalizedStrings.INFORMATION.get());
                        popupMenu.getMenu().getItem(3).setTitle((CharSequence)LocalizedStrings.VIDEOS.get());
                        popupMenu.setOnMenuItemClickListener((PopupMenu$OnMenuItemClickListener)new PopupMenu$OnMenuItemClickListener() {
                            public boolean onMenuItemClick(final MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case 2131624350: {
                                        if (!CrunchyrollApplication.getApp((Context)HistoryAdapter.this.mActivity).isPrepareToWatchLoading()) {
                                            final PrepareToWatch prepareToWatch = CrunchyrollApplication.getApp((Context)HistoryAdapter.this.mActivity).prepareToWatch(HistoryAdapter.this.mActivity, media, false, 0);
                                            prepareToWatch.prepare(new BaseListener<Void>() {
                                                @Override
                                                public void onException(final Exception ex) {
                                                    if (ex instanceof ApiNetworkException) {
                                                        EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_NETWORK.get()));
                                                        return;
                                                    }
                                                    EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_UNKNOWN.get()));
                                                }
                                                
                                                @Override
                                                public void onFinally() {
                                                    Util.hideProgressBar(HistoryAdapter.this.mActivity);
                                                }
                                                
                                                @Override
                                                public void onPreExecute() {
                                                    Util.showProgressBar(HistoryAdapter.this.mActivity, HistoryAdapter.this.mActivity.getResources().getColor(2131558518));
                                                }
                                                
                                                @Override
                                                public void onSuccess(final Void void1) {
                                                    prepareToWatch.go(PrepareToWatch.Event.NONE);
                                                }
                                            });
                                            Tracker.dropdownMenu("home-history", "play", series.getName(), "episode-" + media.getEpisodeNumber());
                                            break;
                                        }
                                        break;
                                    }
                                    case 2131624351: {
                                        ActionsUtil.share((Context)HistoryAdapter.this.mActivity, series.getName(), media.getEpisodeNumber(), media.getUrl());
                                        Tracker.dropdownMenu("home-history", "share", series.getName(), "episode-" + media.getEpisodeNumber());
                                        break;
                                    }
                                    case 2131624352: {
                                        PopupActivity.startMediaInfo(HistoryAdapter.this.mActivity, media.getMediaId());
                                        Tracker.dropdownMenu("home-history", "information", series.getName(), "episode-" + media.getEpisodeNumber());
                                        break;
                                    }
                                    case 2131624353: {
                                        SeriesDetailActivity.start((Context)HistoryAdapter.this.mActivity, series.getSeriesId(), 0, false);
                                        Tracker.dropdownMenu("home-history", "videos", series.getName(), "episode-" + media.getEpisodeNumber());
                                        break;
                                    }
                                }
                                return true;
                            }
                        });
                        popupMenu.show();
                    }
                });
            }
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        if (n == 0) {
            return new NormalViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903080, viewGroup, false));
        }
        if (this.mListItemLoadingViewHolder == null) {
            this.mListItemLoadingViewHolder = new ListItemLoadingViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903154, viewGroup, false));
            this.mListItemLoadingViewHolder.mRetry.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    HistoryAdapter.this.mListItemLoadingViewHolder.setShake(true);
                    EventBus.getDefault().post(new LoadMore.HistoryEvent());
                }
            });
        }
        return this.mListItemLoadingViewHolder;
    }
}
