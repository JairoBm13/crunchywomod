// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.adapters;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Button;
import android.view.ViewGroup;
import com.crunchyroll.crunchyroid.util.Functional;
import android.widget.LinearLayout$LayoutParams;
import com.crunchyroll.crunchyroid.fragments.EpisodeListBaseFragment;
import com.google.common.base.Optional;
import com.crunchyroll.crunchyroid.activities.PopupActivity;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import com.crunchyroll.android.api.ApiManager;
import android.widget.AdapterView;
import com.crunchyroll.android.api.models.Series;
import android.widget.AdapterView$OnItemSelectedListener;
import android.widget.SpinnerAdapter;
import android.widget.RelativeLayout$LayoutParams;
import com.crunchyroll.crunchyroid.activities.GenreActivity;
import com.crunchyroll.android.api.Filters;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.crunchyroll.crunchyroid.app.WatchStatus;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.crunchyroid.events.ErrorEvent;
import de.greenrobot.event.EventBus;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import com.crunchyroll.crunchyroid.app.PrepareToWatch;
import com.crunchyroll.android.api.tasks.BaseListener;
import android.app.Activity;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.view.View$OnClickListener;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation$AnimationListener;
import android.view.animation.AnimationUtils;
import java.util.Iterator;
import com.crunchyroll.android.api.models.Category;
import android.content.Context;
import com.crunchyroll.android.api.models.Categories;
import com.crunchyroll.android.api.models.SeriesInfoWithMedia;
import com.crunchyroll.crunchyroid.fragments.SeriesDetailFragment;
import java.util.HashMap;
import android.support.v7.widget.GridLayoutManager;
import com.crunchyroll.android.api.models.Media;
import com.crunchyroll.android.api.models.Collection;
import java.util.List;
import android.view.View;
import android.support.v7.widget.RecyclerView;

public class SeriesDetailAdapter extends Adapter<ViewHolder>
{
    public static final int SORT_TYPE_ASCENDING = 1;
    public static final int SORT_TYPE_DESCENDING = 0;
    public static final int VIEW_AVAILABILITY = 2;
    public static final int VIEW_INFO = 1;
    public static final int VIEW_MEDIA = 4;
    public static final int VIEW_MEDIA_SPINNERS = 3;
    public static final int VIEW_RELOADING = 5;
    private String mAvailabilityNotes;
    private View mBookmark;
    private List<Collection> mCollectionList;
    private Media mContinueWatchingMedia;
    private GridLayoutManager mGridLayoutManager;
    private boolean mHasAddedGenres;
    private boolean mHasAvailabilityNotes;
    private boolean mIsReloadingSeason;
    private HashMap<String, String> mLocalizedGenreMap;
    private MediaSpinnersViewHolder mMediaSpinnersViewHolder;
    private int mNumHeaders;
    private SeasonSortAdapter mSeasonSortAdapter;
    private int mSeasonSortAdapterPosition;
    private SeasonsAdapter mSeasonsAdapter;
    private SeriesDetailFragment mSeriesDetailFragment;
    private Type mType;
    
    public SeriesDetailAdapter(final SeriesDetailFragment mSeriesDetailFragment, final Type mType, final SeriesInfoWithMedia seriesInfoWithMedia, final List<Collection> mCollectionList, final Categories categories, final Media mContinueWatchingMedia) {
        this.mSeriesDetailFragment = mSeriesDetailFragment;
        this.mCollectionList = mCollectionList;
        this.mContinueWatchingMedia = mContinueWatchingMedia;
        this.mIsReloadingSeason = false;
        this.mSeasonSortAdapterPosition = this.mSeriesDetailFragment.getSortType();
        (this.mGridLayoutManager = new GridLayoutManager((Context)this.mSeriesDetailFragment.getActivity(), 1)).setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(final int n) {
                return 1;
            }
        });
        this.mHasAddedGenres = false;
        this.mHasAvailabilityNotes = false;
        if (this.mCollectionList != null && this.mCollectionList.size() > 0 && this.mCollectionList.get(0).getAvailabilityNotes() != null && !this.mCollectionList.get(0).getAvailabilityNotes().isEmpty()) {
            this.mHasAvailabilityNotes = true;
            this.mAvailabilityNotes = this.mCollectionList.get(0).getAvailabilityNotes();
        }
        this.mType = mType;
        switch (this.mType) {
            case TYPE_PHONE: {
                if (this.mHasAvailabilityNotes) {
                    this.mNumHeaders = 3;
                    break;
                }
                this.mNumHeaders = 2;
                break;
            }
            case TYPE_TABLET_POTRAIT_SECONDARY: {
                if (this.mHasAvailabilityNotes) {
                    this.mNumHeaders = 2;
                    break;
                }
                this.mNumHeaders = 1;
                break;
            }
            case TYPE_TABLET_LANDSCAPE_SECONDARY: {
                if (this.mHasAvailabilityNotes) {
                    this.mNumHeaders = 1;
                    break;
                }
                this.mNumHeaders = 0;
                break;
            }
            case TYPE_TABLET_POTRAIT: {
                this.mNumHeaders = 1;
                break;
            }
            case TYPE_TABLET_LANDSCAPE: {
                this.mNumHeaders = 2;
                break;
            }
        }
        this.mLocalizedGenreMap = new HashMap<String, String>();
        if (categories != null && categories.getGenres() != null) {
            for (final Category category : categories.getGenres()) {
                this.mLocalizedGenreMap.put(category.getTag(), category.getLabel());
            }
        }
    }
    
    public void animateBookmark(final boolean b) {
        this.mBookmark.setVisibility(0);
        if (b) {
            this.mBookmark.startAnimation(AnimationUtils.loadAnimation(this.mBookmark.getContext(), 2130968593));
            return;
        }
        final Animation loadAnimation = AnimationUtils.loadAnimation(this.mBookmark.getContext(), 2130968588);
        loadAnimation.setAnimationListener((Animation$AnimationListener)new Animation$AnimationListener() {
            public void onAnimationEnd(final Animation animation) {
                SeriesDetailAdapter.this.mBookmark.setVisibility(8);
            }
            
            public void onAnimationRepeat(final Animation animation) {
            }
            
            public void onAnimationStart(final Animation animation) {
            }
        });
        this.mBookmark.startAnimation(loadAnimation);
    }
    
    public GridLayoutManager getGridLayoutManager() {
        return this.mGridLayoutManager;
    }
    
    @Override
    public int getItemCount() {
        switch (this.mType) {
            default: {
                if (this.mSeriesDetailFragment.getSeriesInfo() == null) {
                    return 1;
                }
                if (this.mIsReloadingSeason) {
                    return this.mNumHeaders + 1;
                }
                return this.mNumHeaders + this.mSeriesDetailFragment.getSeriesInfo().getMedias().size();
            }
            case TYPE_TABLET_POTRAIT_SECONDARY:
            case TYPE_TABLET_LANDSCAPE_SECONDARY: {
                return this.mNumHeaders;
            }
        }
    }
    
    @Override
    public int getItemViewType(final int n) {
        int n2 = 1;
        Label_0089: {
            switch (this.mType) {
                default: {
                    if (this.mHasAvailabilityNotes) {
                        switch (n) {
                            default: {
                                if (this.mIsReloadingSeason) {
                                    n2 = 5;
                                    break Label_0089;
                                }
                                return 4;
                            }
                            case 0: {
                                break Label_0089;
                            }
                            case 1: {
                                return 2;
                            }
                            case 2: {
                                return 3;
                            }
                        }
                    }
                    else {
                        switch (n) {
                            case 0: {
                                break Label_0089;
                            }
                            default: {
                                if (this.mIsReloadingSeason) {
                                    return 5;
                                }
                                return 4;
                            }
                            case 1: {
                                return 3;
                            }
                        }
                    }
                    break;
                }
                case TYPE_TABLET_POTRAIT_SECONDARY: {
                    switch (n) {
                        case 0: {
                            break Label_0089;
                        }
                        default: {
                            return 2;
                        }
                    }
                    break;
                }
                case TYPE_TABLET_POTRAIT: {
                    switch (n) {
                        default: {
                            if (this.mIsReloadingSeason) {
                                return 5;
                            }
                            return 4;
                        }
                        case 0: {
                            return 3;
                        }
                    }
                    break;
                }
                case TYPE_TABLET_LANDSCAPE_SECONDARY: {
                    return 2;
                }
                case TYPE_TABLET_LANDSCAPE: {
                    switch (n) {
                        case 0: {
                            break Label_0089;
                        }
                        default: {
                            if (this.mIsReloadingSeason) {
                                return 5;
                            }
                            return 4;
                        }
                        case 1: {
                            return 3;
                        }
                    }
                    break;
                }
            }
        }
        return n2;
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
        switch (this.getItemViewType(n)) {
            case 1: {
                if (this.mSeriesDetailFragment.getSeriesInfo() == null) {
                    break;
                }
                final Series series = this.mSeriesDetailFragment.getSeriesInfo().getSeries();
                final InfoViewHolder infoViewHolder = (InfoViewHolder)viewHolder;
                if (this.mType != Type.TYPE_TABLET_LANDSCAPE) {
                    final FrameLayout$LayoutParams layoutParams = new FrameLayout$LayoutParams(-1, -2);
                    layoutParams.setMargins(0, this.mSeriesDetailFragment.getSeriesImageHeight(), 0, 0);
                    viewHolder.itemView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                }
                if (this.mContinueWatchingMedia != null) {
                    infoViewHolder.watchButton.setText((CharSequence)LocalizedStrings.CONTINUE_WATCHING.get());
                    infoViewHolder.watchButton.setVisibility(0);
                    infoViewHolder.watchButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                        public void onClick(final View view) {
                            boolean b = false;
                            if (SeriesDetailAdapter.this.mContinueWatchingMedia != null && !CrunchyrollApplication.getApp((Context)SeriesDetailAdapter.this.mSeriesDetailFragment.getActivity()).isPrepareToWatchLoading()) {
                                final PrepareToWatch prepareToWatch = CrunchyrollApplication.getApp((Context)SeriesDetailAdapter.this.mSeriesDetailFragment.getActivity()).prepareToWatch(SeriesDetailAdapter.this.mSeriesDetailFragment.getActivity(), SeriesDetailAdapter.this.mContinueWatchingMedia, false, 0);
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
                                        Util.hideProgressBar(SeriesDetailAdapter.this.mSeriesDetailFragment.getActivity());
                                    }
                                    
                                    @Override
                                    public void onPreExecute() {
                                        Util.showProgressBar(SeriesDetailAdapter.this.mSeriesDetailFragment.getActivity(), SeriesDetailAdapter.this.mSeriesDetailFragment.getResources().getColor(2131558518));
                                    }
                                    
                                    @Override
                                    public void onSuccess(final Void void1) {
                                        prepareToWatch.go(PrepareToWatch.Event.NONE);
                                    }
                                });
                                final String name = SeriesDetailAdapter.this.mSeriesDetailFragment.getSeriesInfo().getSeries().getName();
                                final String string = "episode-" + SeriesDetailAdapter.this.mContinueWatchingMedia.getEpisodeNumber();
                                if (WatchStatus.getVideoType(SeriesDetailAdapter.this.mContinueWatchingMedia) == 2) {
                                    b = true;
                                }
                                Tracker.episodeSelected(name, string, b);
                                Tracker.seriesDetailContinueWatching(SeriesDetailAdapter.this.mSeriesDetailFragment.getSeriesInfo().getSeries().getName());
                            }
                        }
                    });
                }
                else {
                    infoViewHolder.watchButton.setVisibility(8);
                }
                infoViewHolder.seriesTitle.setText((CharSequence)series.getName().toUpperCase());
                infoViewHolder.seriesDescription.setText((CharSequence)series.getDescription());
                infoViewHolder.seriesRating.setText((CharSequence)(series.getRating() + "/" + 5.0f));
                if (!this.mHasAddedGenres) {
                    final List<String> genres = this.mSeriesDetailFragment.getSeriesInfo().getSeries().getGenres();
                    if (genres != null) {
                        for (final String s : genres) {
                            final TextView textView = (TextView)LayoutInflater.from(infoViewHolder.flowLayout.getContext()).inflate(2130903079, infoViewHolder.flowLayout, false);
                            String text;
                            if (this.mLocalizedGenreMap.get(s) == null) {
                                text = s;
                            }
                            else {
                                text = this.mLocalizedGenreMap.get(s);
                            }
                            textView.setText((CharSequence)text);
                            textView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                                public void onClick(final View view) {
                                    Tracker.browseGenre(view.getContext(), s);
                                    GenreActivity.start(view.getContext(), SeriesDetailAdapter.this.mSeriesDetailFragment.getMediaType(), Filters.addTag(s));
                                }
                            });
                            infoViewHolder.flowLayout.addView((View)textView, 0);
                        }
                    }
                    this.mHasAddedGenres = true;
                }
                this.mBookmark = infoViewHolder.bookmark;
                if (!CrunchyrollApplication.getApp((Context)this.mSeriesDetailFragment.getActivity()).getApplicationState().hasLoggedInUser()) {
                    this.mBookmark.setVisibility(8);
                    return;
                }
                if (series.getInQueue().get()) {
                    this.mBookmark.setVisibility(0);
                    return;
                }
                this.mBookmark.setVisibility(8);
            }
            case 2: {
                ((AvailabilityViewHolder)viewHolder).availabilityNotes.setText((CharSequence)this.mAvailabilityNotes);
                if (this.mType == Type.TYPE_TABLET_LANDSCAPE_SECONDARY) {
                    viewHolder.itemView.setBackgroundResource(2130837685);
                    final FrameLayout$LayoutParams layoutParams2 = new FrameLayout$LayoutParams(-1, -2);
                    layoutParams2.setMargins(0, this.mSeriesDetailFragment.getSeriesImageHeight(), 0, 0);
                    viewHolder.itemView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
                    return;
                }
                break;
            }
            case 3: {
                if (this.mSeriesDetailFragment.getSeriesInfo() != null) {
                    final Series series2 = this.mSeriesDetailFragment.getSeriesInfo().getSeries();
                    final MediaSpinnersViewHolder mediaSpinnersViewHolder = (MediaSpinnersViewHolder)viewHolder;
                    if (this.mCollectionList.size() > 1) {
                        mediaSpinnersViewHolder.seasonsSpinner.setVisibility(0);
                    }
                    else {
                        mediaSpinnersViewHolder.seasonsSpinner.setVisibility(8);
                        final RelativeLayout$LayoutParams layoutParams3 = new RelativeLayout$LayoutParams(-2, -2);
                        layoutParams3.addRule(11);
                        layoutParams3.setMargins(0, 0, 0, 0);
                        mediaSpinnersViewHolder.seasonSortSpinner.setLayoutParams((ViewGroup$LayoutParams)layoutParams3);
                    }
                    if (this.mSeasonsAdapter == null && this.mSeriesDetailFragment != null && this.mSeriesDetailFragment.getActivity() != null) {
                        this.mSeasonsAdapter = new SeasonsAdapter((Context)this.mSeriesDetailFragment.getActivity(), 2130903184, mediaSpinnersViewHolder.seasonsSpinner, this.mCollectionList);
                        mediaSpinnersViewHolder.seasonsSpinner.setAdapter((SpinnerAdapter)this.mSeasonsAdapter);
                        mediaSpinnersViewHolder.seasonsSpinner.setFocusable(true);
                        mediaSpinnersViewHolder.seasonsSpinner.setOnItemSelectedListener((AdapterView$OnItemSelectedListener)new AdapterView$OnItemSelectedListener() {
                            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                                Tracker.seasonsSelect((Context)SeriesDetailAdapter.this.mSeriesDetailFragment.getActivity(), SeriesDetailAdapter.this.mCollectionList.get(n), series2.getName());
                                if (SeriesDetailAdapter.this.mSeriesDetailFragment.getSeriesInfoIndex() != n) {
                                    ApiManager.getInstance(SeriesDetailAdapter.this.mSeriesDetailFragment.getActivity()).getMediaListData(series2.getSeriesId(), SeriesDetailAdapter.this.mCollectionList.get(n).getCollectionId(), new BaseListener<SeriesInfoWithMedia>() {
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
                                            super.onFinally();
                                            SeriesDetailAdapter.this.mIsReloadingSeason = false;
                                            ((RecyclerView.Adapter)SeriesDetailAdapter.this).notifyDataSetChanged();
                                        }
                                        
                                        @Override
                                        public void onPreExecute() {
                                            super.onPreExecute();
                                            SeriesDetailAdapter.this.mIsReloadingSeason = true;
                                            ((RecyclerView.Adapter)SeriesDetailAdapter.this).notifyDataSetChanged();
                                        }
                                        
                                        @Override
                                        public void onSuccess(final SeriesInfoWithMedia seriesInfoWithMedia) {
                                            SeriesDetailAdapter.this.mSeriesDetailFragment.setSeriesInfoIndex(n);
                                            SeriesDetailAdapter.this.mSeriesDetailFragment.setSeriesInfo(seriesInfoWithMedia);
                                            SeriesDetailAdapter.this.mSeriesDetailFragment.setSeriesInfo(seriesInfoWithMedia);
                                        }
                                    });
                                }
                            }
                            
                            public void onNothingSelected(final AdapterView<?> adapterView) {
                            }
                        });
                    }
                    mediaSpinnersViewHolder.seasonsSpinner.setSelection(this.mSeriesDetailFragment.getSeriesInfoIndex());
                    if (this.mSeasonSortAdapter == null && this.mSeriesDetailFragment != null && this.mSeriesDetailFragment.getActivity() != null) {
                        this.mSeasonSortAdapter = new SeasonSortAdapter((Context)this.mSeriesDetailFragment.getActivity(), 2130903184, mediaSpinnersViewHolder.seasonSortSpinner, new String[] { LocalizedStrings.DESCENDING.get(), LocalizedStrings.ASCENDING.get() });
                        mediaSpinnersViewHolder.seasonSortSpinner.setAdapter((SpinnerAdapter)this.mSeasonSortAdapter);
                        mediaSpinnersViewHolder.seasonSortSpinner.setFocusable(true);
                        mediaSpinnersViewHolder.seasonSortSpinner.setOnItemSelectedListener((AdapterView$OnItemSelectedListener)new AdapterView$OnItemSelectedListener() {
                            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int sortType, final long n) {
                                if (SeriesDetailAdapter.this.mSeasonSortAdapterPosition != sortType) {
                                    SeriesDetailAdapter.this.mSeasonSortAdapterPosition = sortType;
                                    SeriesDetailAdapter.this.mSeriesDetailFragment.setSortType(sortType);
                                    Tracker.sortEpisode(view.getContext(), SeriesDetailAdapter.this.mSeriesDetailFragment.getSortType());
                                    ((RecyclerView.Adapter)SeriesDetailAdapter.this).notifyDataSetChanged();
                                }
                            }
                            
                            public void onNothingSelected(final AdapterView<?> adapterView) {
                            }
                        });
                    }
                    mediaSpinnersViewHolder.seasonSortSpinner.setSelection(this.mSeriesDetailFragment.getSortType());
                    return;
                }
                break;
            }
            case 4: {
                if (this.mSeriesDetailFragment.getSeriesInfo() == null || this.mIsReloadingSeason) {
                    break;
                }
                final MediaViewHolder mediaViewHolder = (MediaViewHolder)viewHolder;
                int n2 = n - this.mNumHeaders;
                while (true) {
                Label_1199_Outer:
                    while (true) {
                        Media media = null;
                    Label_1024:
                        while (true) {
                            Label_0920: {
                                switch (this.mSeriesDetailFragment.getSortType()) {
                                    case 1: {
                                        Label_1171: {
                                            break Label_1171;
                                            String availableTimeInfo;
                                            final int n3;
                                            Label_1068_Outer:Label_1089_Outer:Label_1120_Outer:
                                            while (true) {
                                                availableTimeInfo = "";
                                                while (true) {
                                                Label_1372:
                                                    while (true) {
                                                    Label_1285:
                                                        while (true) {
                                                            try {
                                                                availableTimeInfo = Util.createAvailableTimeInfo(media.getAvailableTime());
                                                                if (!media.getEpisodeNumber().isEmpty()) {
                                                                    break Label_1285;
                                                                }
                                                                mediaViewHolder.titleNumber.setVisibility(8);
                                                                if (!media.getName().isEmpty()) {
                                                                    break Label_1372;
                                                                }
                                                                mediaViewHolder.title.setVisibility(8);
                                                                mediaViewHolder.time.setText((CharSequence)availableTimeInfo);
                                                                mediaViewHolder.info.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                                                                    public void onClick(final View view) {
                                                                        Tracker.episodeInfo((Context)SeriesDetailAdapter.this.mSeriesDetailFragment.getActivity(), SeriesDetailAdapter.this.mSeriesDetailFragment.getSeriesInfo().getSeries().getName(), "episode-" + media.getEpisodeNumber());
                                                                        PopupActivity.startMediaInfo(SeriesDetailAdapter.this.mSeriesDetailFragment.getActivity(), media.getMediaId());
                                                                    }
                                                                });
                                                                if (n == this.getItemCount() - 1) {
                                                                    viewHolder.itemView.setPadding(0, 0, 0, Util.dpToPx((Context)this.mSeriesDetailFragment.getActivity(), 90));
                                                                    return;
                                                                }
                                                                break;
                                                                n2 = this.mSeriesDetailFragment.getSeriesInfo().getMedias().size() + this.mNumHeaders - n - 1;
                                                                break Label_0920;
                                                                EpisodeListBaseFragment.loadLargeImageIntoImageView((Context)this.mSeriesDetailFragment.getActivity(), Optional.of(media), mediaViewHolder.image);
                                                                break Label_1024;
                                                                mediaViewHolder.mediaProgress.setVisibility(0);
                                                                mediaViewHolder.mediaProgressPercent.setLayoutParams((ViewGroup$LayoutParams)new LinearLayout$LayoutParams(0, -1, (float)n3));
                                                                mediaViewHolder.mediaProgressRemainder.setLayoutParams((ViewGroup$LayoutParams)new LinearLayout$LayoutParams(0, -1, (float)(100 - n3)));
                                                                continue Label_1068_Outer;
                                                            }
                                                            catch (Exception ex) {
                                                                ex.printStackTrace();
                                                                continue Label_1089_Outer;
                                                            }
                                                            break;
                                                        }
                                                        mediaViewHolder.titleNumber.setText((CharSequence)Functional.Media.getEpisodeNumber(media));
                                                        if (!media.isFreeAvailable().or(Boolean.valueOf(true)) && media.isPremiumAvailable().or(Boolean.valueOf(false))) {
                                                            mediaViewHolder.titleNumber.setCompoundDrawablesWithIntrinsicBounds(2130837745, 0, 0, 0);
                                                            continue Label_1120_Outer;
                                                        }
                                                        mediaViewHolder.titleNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                                        continue Label_1120_Outer;
                                                    }
                                                    mediaViewHolder.title.setText((CharSequence)media.getName());
                                                    if (media.getDuration().isPresent()) {
                                                        mediaViewHolder.time.setText((CharSequence)Util.secondsToString(media.getDuration().get()));
                                                        continue Label_1199_Outer;
                                                    }
                                                    continue Label_1199_Outer;
                                                }
                                            }
                                        }
                                        viewHolder.itemView.setPadding(0, 0, 0, 0);
                                        return;
                                    }
                                }
                            }
                            media = this.mSeriesDetailFragment.getSeriesInfo().getMedias().get(n2);
                            viewHolder.itemView.setFocusable(true);
                            viewHolder.itemView.setBackgroundResource(2130837566);
                            viewHolder.itemView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                                public void onClick(final View view) {
                                    boolean b = false;
                                    if (!CrunchyrollApplication.getApp((Context)SeriesDetailAdapter.this.mSeriesDetailFragment.getActivity()).isPrepareToWatchLoading()) {
                                        Tracker.episodeSelect((Context)SeriesDetailAdapter.this.mSeriesDetailFragment.getActivity(), media);
                                        final PrepareToWatch prepareToWatch = CrunchyrollApplication.getApp((Context)SeriesDetailAdapter.this.mSeriesDetailFragment.getActivity()).prepareToWatch(SeriesDetailAdapter.this.mSeriesDetailFragment.getActivity(), media, false, 0);
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
                                                Util.hideProgressBar(SeriesDetailAdapter.this.mSeriesDetailFragment.getActivity());
                                            }
                                            
                                            @Override
                                            public void onPreExecute() {
                                                Util.showProgressBar(SeriesDetailAdapter.this.mSeriesDetailFragment.getActivity(), SeriesDetailAdapter.this.mSeriesDetailFragment.getResources().getColor(2131558518));
                                            }
                                            
                                            @Override
                                            public void onSuccess(final Void void1) {
                                                prepareToWatch.go(PrepareToWatch.Event.NONE);
                                            }
                                        });
                                        final String s = media.getSeriesName().or("");
                                        final String string = "episode-" + media.getEpisodeNumber();
                                        if (WatchStatus.getVideoType(media) == 2) {
                                            b = true;
                                        }
                                        Tracker.episodeSelected(s, string, b);
                                        Tracker.seriesDetailEpisodeSelected(media.getSeriesName().or(""), "episode-" + media.getEpisodeNumber());
                                    }
                                }
                            });
                            switch (this.mType) {
                                default: {
                                    EpisodeListBaseFragment.loadSmallImageIntoImageView((Context)this.mSeriesDetailFragment.getActivity(), Optional.of(media), mediaViewHolder.image);
                                    break;
                                }
                                case TYPE_PHONE: {
                                    continue;
                                }
                            }
                            break;
                        }
                        final int n3 = (int)(media.getPercentWatched() * 100.0);
                        if (n3 == 0) {
                            mediaViewHolder.mediaProgress.setVisibility(8);
                            continue Label_1199_Outer;
                        }
                        break;
                    }
                    continue;
                }
            }
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        switch (n) {
            default: {
                return new LoadingViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903155, viewGroup, false));
            }
            case 1: {
                return new InfoViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903141, viewGroup, false));
            }
            case 2: {
                return new AvailabilityViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903140, viewGroup, false));
            }
            case 3: {
                if (this.mMediaSpinnersViewHolder == null) {
                    this.mMediaSpinnersViewHolder = new MediaSpinnersViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903143, viewGroup, false));
                }
                return this.mMediaSpinnersViewHolder;
            }
            case 4: {
                return new MediaViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903142, viewGroup, false));
            }
        }
    }
    
    public static class AvailabilityViewHolder extends ViewHolder
    {
        public final TextView availabilityNotes;
        
        public AvailabilityViewHolder(final View view) {
            super(view);
            this.availabilityNotes = (TextView)view.findViewById(2131624271);
        }
    }
    
    public static class InfoViewHolder extends ViewHolder
    {
        public final View bookmark;
        public final ViewGroup flowLayout;
        public final TextView seriesDescription;
        public final TextView seriesRating;
        public final TextView seriesTitle;
        public final Button watchButton;
        
        public InfoViewHolder(final View view) {
            super(view);
            this.watchButton = (Button)view.findViewById(2131624272);
            this.seriesTitle = (TextView)view.findViewById(2131624086);
            this.seriesDescription = (TextView)view.findViewById(2131624273);
            this.seriesRating = (TextView)view.findViewById(2131624275);
            this.bookmark = view.findViewById(2131624276);
            this.flowLayout = (ViewGroup)view.findViewById(2131624274);
        }
    }
    
    public static class LoadingViewHolder extends ViewHolder
    {
        public LoadingViewHolder(final View view) {
            super(view);
        }
    }
    
    public static class MediaSpinnersViewHolder extends ViewHolder
    {
        public final Spinner seasonSortSpinner;
        public final Spinner seasonsSpinner;
        
        public MediaSpinnersViewHolder(final View view) {
            super(view);
            this.seasonsSpinner = (Spinner)view.findViewById(2131624281);
            this.seasonSortSpinner = (Spinner)view.findViewById(2131624282);
        }
    }
    
    public static class MediaViewHolder extends ViewHolder
    {
        public final ImageView image;
        public final View info;
        public final View mediaProgress;
        public final View mediaProgressPercent;
        public final View mediaProgressRemainder;
        public final TextView time;
        public final TextView title;
        public final TextView titleNumber;
        
        public MediaViewHolder(final View view) {
            super(view);
            this.image = (ImageView)view.findViewById(2131624082);
            this.mediaProgress = view.findViewById(2131624083);
            this.mediaProgressPercent = view.findViewById(2131624084);
            this.mediaProgressRemainder = view.findViewById(2131624085);
            this.titleNumber = (TextView)view.findViewById(2131624278);
            this.title = (TextView)view.findViewById(2131624088);
            this.time = (TextView)view.findViewById(2131624279);
            this.info = view.findViewById(2131624280);
        }
    }
    
    public enum Type
    {
        TYPE_PHONE, 
        TYPE_TABLET_LANDSCAPE, 
        TYPE_TABLET_LANDSCAPE_SECONDARY, 
        TYPE_TABLET_POTRAIT, 
        TYPE_TABLET_POTRAIT_SECONDARY;
    }
}
