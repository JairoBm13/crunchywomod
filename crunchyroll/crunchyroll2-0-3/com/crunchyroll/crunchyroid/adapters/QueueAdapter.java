// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.adapters;

import android.widget.TextView;
import android.content.res.TypedArray;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import com.crunchyroll.crunchyroid.events.Empty;
import com.google.common.collect.Iterables;
import com.google.common.base.Predicate;
import com.crunchyroll.android.api.ApiManager;
import com.crunchyroll.crunchyroid.activities.PopupActivity;
import com.crunchyroll.crunchyroid.util.ActionsUtil;
import android.view.MenuItem;
import android.widget.PopupMenu$OnMenuItemClickListener;
import android.widget.PopupMenu;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import com.crunchyroll.crunchyroid.fragments.EpisodeListBaseFragment;
import com.crunchyroll.crunchyroid.activities.SeriesDetailActivity;
import com.crunchyroll.crunchyroid.app.WatchStatus;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.crunchyroid.events.ErrorEvent;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import de.greenrobot.event.EventBus;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import com.crunchyroll.crunchyroid.app.PrepareToWatch;
import com.crunchyroll.android.api.tasks.BaseListener;
import android.app.Activity;
import com.crunchyroll.android.api.models.Media;
import android.content.Context;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.view.View;
import com.crunchyroll.android.api.models.Series;
import com.google.common.base.Optional;
import android.view.View$OnClickListener;
import com.crunchyroll.crunchyroid.util.Functional;
import android.support.v7.widget.RecyclerView;
import com.crunchyroll.android.api.models.QueueEntry;
import java.util.List;
import android.support.v4.app.FragmentActivity;

public class QueueAdapter extends MediaCardAdapter
{
    private FragmentActivity mActivity;
    private List<QueueEntry> mQueueEntries;
    
    public QueueAdapter(final FragmentActivity mActivity, final List<QueueEntry> mQueueEntries) {
        this.mActivity = mActivity;
        this.mQueueEntries = mQueueEntries;
    }
    
    @Override
    public int getItemCount() {
        return this.mQueueEntries.size();
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int resourceId) {
        final QueueEntry queueEntry = this.mQueueEntries.get(resourceId);
        if (queueEntry != null) {
            final Series series = queueEntry.getSeries();
            final Optional<Media> mostLikelyMedia = queueEntry.getMostLikelyMedia();
            String name;
            if (series != null) {
                name = series.getName();
            }
            else {
                name = null;
            }
            final String episodeSubtitle = Functional.Media.getEpisodeSubtitle(mostLikelyMedia);
            viewHolder.itemView.setFocusable(true);
            final TypedArray obtainStyledAttributes = this.mActivity.obtainStyledAttributes(new int[] { 2130772199 });
            resourceId = obtainStyledAttributes.getResourceId(0, 0);
            viewHolder.itemView.setBackgroundResource(resourceId);
            obtainStyledAttributes.recycle();
            ((NormalViewHolder)viewHolder).itemView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    if (mostLikelyMedia.isPresent()) {
                        if (!CrunchyrollApplication.getApp((Context)QueueAdapter.this.mActivity).isPrepareToWatchLoading()) {
                            final PrepareToWatch prepareToWatch = CrunchyrollApplication.getApp((Context)QueueAdapter.this.mActivity).prepareToWatch(QueueAdapter.this.mActivity, mostLikelyMedia.get(), false, 0);
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
                                    Util.hideProgressBar(QueueAdapter.this.mActivity);
                                }
                                
                                @Override
                                public void onPreExecute() {
                                    Util.showProgressBar(QueueAdapter.this.mActivity, QueueAdapter.this.mActivity.getResources().getColor(2131558518));
                                }
                                
                                @Override
                                public void onSuccess(final Void void1) {
                                    prepareToWatch.go(PrepareToWatch.Event.NONE);
                                }
                            });
                            Tracker.screenEpisodeSelected("home-queue", name, "episode-" + mostLikelyMedia.get().getEpisodeNumber());
                            Tracker.episodeSelected(name, "episode-" + mostLikelyMedia.get().getEpisodeNumber(), WatchStatus.getVideoType(mostLikelyMedia.get()) == 2);
                        }
                        return;
                    }
                    SeriesDetailActivity.start((Context)QueueAdapter.this.mActivity, series.getSeriesId(), 0, false);
                }
            });
            EpisodeListBaseFragment.loadImageIntoImageView((Context)this.mActivity, mostLikelyMedia, ((NormalViewHolder)viewHolder).image, this.mThumbwidth);
            int n = 0;
            resourceId = 1;
            if (mostLikelyMedia.isPresent()) {
                n = (int)(mostLikelyMedia.get().getPercentWatched() * 100.0);
                resourceId = 100 - n;
            }
            if (n == 0) {
                ((NormalViewHolder)viewHolder).mediaProgress.setVisibility(8);
            }
            else {
                ((NormalViewHolder)viewHolder).mediaProgress.setVisibility(0);
                ((NormalViewHolder)viewHolder).mediaProgressPercent.setLayoutParams((ViewGroup$LayoutParams)new LinearLayout$LayoutParams(0, -1, (float)n));
                ((NormalViewHolder)viewHolder).mediaProgressRemainder.setLayoutParams((ViewGroup$LayoutParams)new LinearLayout$LayoutParams(0, -1, (float)resourceId));
            }
            final TextView seriesTitle = ((NormalViewHolder)viewHolder).seriesTitle;
            String text;
            if (name != null) {
                text = name;
            }
            else {
                text = "";
            }
            seriesTitle.setText((CharSequence)text);
            final TextView mediaTitle = ((NormalViewHolder)viewHolder).mediaTitle;
            String text2;
            if (episodeSubtitle != null) {
                text2 = episodeSubtitle;
            }
            else {
                text2 = "";
            }
            mediaTitle.setText((CharSequence)text2);
            if (mostLikelyMedia.isPresent() && WatchStatus.getVideoType(mostLikelyMedia.get()) == 2) {
                ((NormalViewHolder)viewHolder).premiumIcon.setVisibility(0);
            }
            else {
                ((NormalViewHolder)viewHolder).premiumIcon.setVisibility(8);
            }
            ((NormalViewHolder)viewHolder).menu.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    final PopupMenu popupMenu = new PopupMenu((Context)QueueAdapter.this.mActivity, view);
                    popupMenu.getMenuInflater().inflate(2131689473, popupMenu.getMenu());
                    popupMenu.getMenu().getItem(0).setTitle((CharSequence)LocalizedStrings.PLAY.get());
                    popupMenu.getMenu().getItem(1).setTitle((CharSequence)LocalizedStrings.SHARE.get());
                    popupMenu.getMenu().getItem(2).setTitle((CharSequence)LocalizedStrings.INFORMATION.get());
                    popupMenu.getMenu().getItem(3).setTitle((CharSequence)LocalizedStrings.VIDEOS.get());
                    popupMenu.getMenu().getItem(4).setTitle((CharSequence)LocalizedStrings.REMOVE_FROM_QUEUE.get());
                    popupMenu.setOnMenuItemClickListener((PopupMenu$OnMenuItemClickListener)new PopupMenu$OnMenuItemClickListener() {
                        public boolean onMenuItemClick(final MenuItem menuItem) {
                            final Media media = mostLikelyMedia.get();
                            switch (menuItem.getItemId()) {
                                case 2131624350: {
                                    if (!CrunchyrollApplication.getApp((Context)QueueAdapter.this.mActivity).isPrepareToWatchLoading()) {
                                        final PrepareToWatch prepareToWatch = CrunchyrollApplication.getApp((Context)QueueAdapter.this.mActivity).prepareToWatch(QueueAdapter.this.mActivity, media, false, 0);
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
                                                Util.hideProgressBar(QueueAdapter.this.mActivity);
                                            }
                                            
                                            @Override
                                            public void onPreExecute() {
                                                Util.showProgressBar(QueueAdapter.this.mActivity, QueueAdapter.this.mActivity.getResources().getColor(2131558518));
                                            }
                                            
                                            @Override
                                            public void onSuccess(final Void void1) {
                                                prepareToWatch.go(PrepareToWatch.Event.NONE);
                                            }
                                        });
                                        Tracker.dropdownMenu("home-queue", "play", name, "episode-" + mostLikelyMedia.get().getEpisodeNumber());
                                        break;
                                    }
                                    break;
                                }
                                case 2131624351: {
                                    ActionsUtil.share((Context)QueueAdapter.this.mActivity, series.getName(), null, series.getUrl());
                                    Tracker.dropdownMenu("home-history", "share", series.getName(), "episode-" + media.getEpisodeNumber());
                                    break;
                                }
                                case 2131624352: {
                                    PopupActivity.startMediaInfo(QueueAdapter.this.mActivity, media.getMediaId());
                                    Tracker.dropdownMenu("home-queue", "information", name, "episode-" + mostLikelyMedia.get().getEpisodeNumber());
                                    break;
                                }
                                case 2131624353: {
                                    SeriesDetailActivity.start((Context)QueueAdapter.this.mActivity, series.getSeriesId(), 0, false);
                                    Tracker.dropdownMenu("home-queue", "videos", name, "episode-" + mostLikelyMedia.get().getEpisodeNumber());
                                    break;
                                }
                                case 2131624354: {
                                    ApiManager.getInstance(QueueAdapter.this.mActivity).removeFromQueue(series, new BaseListener<Void>() {
                                        @Override
                                        public void onException(final Exception ex) throws RuntimeException {
                                            EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_QUEUE_REMOVE.get()));
                                        }
                                        
                                        @Override
                                        public void onSuccess(final Void void1) {
                                            super.onSuccess(void1);
                                            Iterables.removeIf((Iterable<Object>)QueueAdapter.this.mQueueEntries, (Predicate<? super Object>)new Predicate<QueueEntry>() {
                                                @Override
                                                public boolean apply(final QueueEntry queueEntry) {
                                                    return series.getSeriesId().equals(queueEntry.getSeries().getSeriesId());
                                                }
                                            });
                                            if (QueueAdapter.this.mQueueEntries.isEmpty()) {
                                                EventBus.getDefault().post(new Empty.QueueEvent());
                                                return;
                                            }
                                            ((RecyclerView.Adapter)QueueAdapter.this).notifyDataSetChanged();
                                        }
                                    });
                                    Tracker.dropdownMenu("home-queue", "remove-from-queue", name, "episode-" + mostLikelyMedia.get().getEpisodeNumber());
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
