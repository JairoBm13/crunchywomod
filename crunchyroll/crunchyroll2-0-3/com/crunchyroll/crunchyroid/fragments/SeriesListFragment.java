// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import java.util.Collection;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import com.crunchyroll.android.api.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.ListSeriesRequest;
import com.google.common.collect.ImmutableSet;
import com.crunchyroll.android.util.SafeAsyncTask;
import com.crunchyroll.android.api.Filters;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import java.util.Iterator;
import com.crunchyroll.android.api.models.QueueEntry;
import android.view.ViewTreeObserver;
import android.os.Build$VERSION;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import java.io.Serializable;
import android.support.v4.app.Fragment;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.crunchyroid.events.RefreshEvent;
import com.crunchyroll.crunchyroid.events.LoadMore;
import com.crunchyroll.crunchyroid.events.CardTypeEvent;
import android.view.View;
import android.view.LayoutInflater;
import de.greenrobot.event.EventBus;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import java.util.List;
import java.util.Set;
import android.app.Activity;
import com.crunchyroll.crunchyroid.itemdecorations.SeriesListTabletItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import com.google.common.collect.Lists;
import com.crunchyroll.android.util.LoggerFactory;
import com.crunchyroll.crunchyroid.widget.CustomSwipeRefreshLayout;
import android.content.SharedPreferences$OnSharedPreferenceChangeListener;
import com.crunchyroll.android.api.models.Series;
import com.crunchyroll.crunchyroid.adapters.SeriesEntryAdapter;
import com.crunchyroll.android.api.models.Category;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.HashSet;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.CoordinatorLayout;
import com.crunchyroll.android.util.Logger;
import android.support.v4.widget.SwipeRefreshLayout;

public abstract class SeriesListFragment extends BaseFragment implements OnRefreshListener
{
    private static final Logger log;
    protected CoordinatorLayout mCoordinatorLayout;
    protected String mFilter;
    protected int mInitialLoadLimit;
    private boolean mIsNewObject;
    protected boolean mIsShowTitle;
    protected boolean mIsTablet;
    private RecyclerView.ItemDecoration mItemDecoration;
    private RecyclerView.LayoutManager mLayoutManager;
    protected LoadMoreSeriesTask mLoadTask;
    protected String mMediaType;
    private int mNumColumns;
    protected ViewGroup mParent;
    private HashSet<Long> mQueueSeriesIds;
    private AtomicBoolean mReadyForMore;
    private int mRecycleViewWidth;
    protected RecyclerView mRecyclerView;
    private boolean mReloadOnResume;
    protected ArrayList<Category> mSeasons;
    protected SeriesEntryAdapter mSeriesAdapter;
    private ArrayList<Series> mSeriesItems;
    private SharedPreferences$OnSharedPreferenceChangeListener mSettingsListener;
    protected boolean mShouldLoadMoreInSeriesDetailViewPager;
    protected ViewGroup mSortFilterSideMenu;
    protected CustomSwipeRefreshLayout mSwipeRefresh;
    protected Type mType;
    
    static {
        log = LoggerFactory.getLogger(SeriesListFragment.class);
    }
    
    public SeriesListFragment() {
        this.mSeriesItems = Lists.newArrayList();
        this.mRecycleViewWidth = -1;
        this.mReloadOnResume = false;
        this.mReadyForMore = new AtomicBoolean(false);
        this.mLoadTask = null;
        this.mNumColumns = 1;
        this.mInitialLoadLimit = 50;
        this.mIsNewObject = true;
    }
    
    private void loadMoreItems(final int n, final int n2) {
        synchronized (this.mReadyForMore) {
            if (this.mReadyForMore.compareAndSet(true, false) || this.mSeriesItems.isEmpty()) {
                this.loadItemsWithOffset(n, n2);
            }
        }
    }
    
    protected void cardTypeChange(int cardType) {
        this.mSeriesAdapter.setCardType(cardType);
        final int dimensionPixelSize = this.getResources().getDimensionPixelSize(2131230734);
        int thumbWidth;
        if (cardType == 1) {
            this.mLayoutManager = new LinearLayoutManager((Context)this.getActivity());
            thumbWidth = dimensionPixelSize;
        }
        else if (cardType == 2) {
            if (this.mRecycleViewWidth > 0 && this.mIsTablet) {
                this.mNumColumns = Math.max(3, Math.round(this.mRecycleViewWidth / dimensionPixelSize));
                thumbWidth = dimensionPixelSize;
            }
            else {
                this.mNumColumns = 3;
                thumbWidth = Math.round(this.mRecycleViewWidth / this.mNumColumns);
            }
            this.mLayoutManager = new GridLayoutManager((Context)this.getActivity(), this.mNumColumns);
            ((GridLayoutManager)this.mLayoutManager).setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(final int n) {
                    if (SeriesListFragment.this.mSeriesAdapter.getItemViewType(n) == 1) {
                        return 1;
                    }
                    return SeriesListFragment.this.mNumColumns;
                }
            });
        }
        else {
            thumbWidth = dimensionPixelSize;
            if (cardType == 3) {
                thumbWidth = dimensionPixelSize * 2;
                if (this.mRecycleViewWidth > 0 && this.mIsTablet) {
                    this.mNumColumns = Math.max(2, Math.round(this.mRecycleViewWidth / thumbWidth));
                }
                else {
                    this.mNumColumns = 2;
                    thumbWidth = Math.round(this.mRecycleViewWidth / this.mNumColumns);
                }
                this.mLayoutManager = new GridLayoutManager((Context)this.getActivity(), this.mNumColumns);
                ((GridLayoutManager)this.mLayoutManager).setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(final int n) {
                        if (SeriesListFragment.this.mSeriesAdapter.getItemViewType(n) == 1) {
                            return 1;
                        }
                        return SeriesListFragment.this.mNumColumns;
                    }
                });
            }
        }
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        if (this.mIsTablet) {
            if (this.mItemDecoration != null) {
                this.mRecyclerView.removeItemDecoration(this.mItemDecoration);
            }
            if (cardType != 1) {
                if (this.mSeriesAdapter.showTitle()) {
                    cardType = 1;
                }
                else {
                    cardType = 0;
                }
                this.mItemDecoration = new SeriesListTabletItemDecoration(cardType);
                this.mRecyclerView.addItemDecoration(this.mItemDecoration);
            }
        }
        this.mSeriesAdapter.setThumbWidth(thumbWidth);
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
    }
    
    protected void createNewAdapter() {
        this.mSeriesAdapter = new SeriesEntryAdapter(false, this.getActivity(), this.mMediaType, this.mFilter, this.mQueueSeriesIds, this.mSeriesItems, this.mShouldLoadMoreInSeriesDetailViewPager, this.mIsShowTitle, this.mType);
    }
    
    protected void enableLoading() {
        this.mReadyForMore.set(true);
    }
    
    protected void loadItemsWithOffset(final Integer n, final int n2) {
        if (this.mMediaType != null && this.mFilter != null) {
            (this.mLoadTask = new LoadMoreSeriesTask(this.mMediaType, this.mFilter, n, n2)).execute();
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mIsTablet = (this.getResources().getInteger(2131427330) != 0);
        if (bundle == null) {
            if (this.mIsNewObject) {
                this.mIsNewObject = false;
                this.mReloadOnResume = true;
            }
            else {
                this.mReloadOnResume = false;
            }
        }
        else {
            this.mSeriesItems = (ArrayList<Series>)bundle.getSerializable("list");
            if (this.mSeriesItems != null && !this.mSeriesItems.isEmpty()) {
                this.mReloadOnResume = false;
                this.enableLoading();
                this.mQueueSeriesIds = (HashSet<Long>)bundle.getSerializable("seriesId");
                this.mSeasons = (ArrayList<Category>)bundle.getSerializable("seasons");
                this.mFilter = bundle.getString("filter");
            }
            else {
                this.mReloadOnResume = true;
            }
        }
        this.mIsShowTitle = true;
        this.mSettingsListener = (SharedPreferences$OnSharedPreferenceChangeListener)new SharedPreferences$OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String s) {
                if (s.equals("imageLoadingEnabled") && SeriesListFragment.this.mSeriesAdapter != null) {
                    ((RecyclerView.Adapter)SeriesListFragment.this.mSeriesAdapter).notifyDataSetChanged();
                }
            }
        };
        PreferenceManager.getDefaultSharedPreferences((Context)this.getActivity()).registerOnSharedPreferenceChangeListener(this.mSettingsListener);
        EventBus.getDefault().register(this);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2130903128, viewGroup, false);
        this.mRecyclerView = (RecyclerView)inflate.findViewById(2131624242);
        (this.mSwipeRefresh = (CustomSwipeRefreshLayout)inflate.findViewById(2131624187)).setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener)this);
        this.mParent = (ViewGroup)inflate.findViewById(2131624060);
        this.mCoordinatorLayout = (CoordinatorLayout)inflate.findViewById(2131624186);
        return inflate;
    }
    
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (this.mLoadTask != null) {
            this.mLoadTask.cancel(true);
        }
        PreferenceManager.getDefaultSharedPreferences((Context)this.getActivity()).unregisterOnSharedPreferenceChangeListener(this.mSettingsListener);
        super.onDestroy();
    }
    
    public void onEvent(final CardTypeEvent cardTypeEvent) {
        this.cardTypeChange(cardTypeEvent.getType());
    }
    
    public void onEvent(final LoadMore.SeriesEvent seriesEvent) {
        if ((this.mMediaType == null || this.mMediaType.equals(seriesEvent.getMediaType())) && (this.mFilter == null || this.mFilter.equals(seriesEvent.getFilter())) && this.mReadyForMore.get()) {
            this.loadMoreItems(this.mSeriesItems.size(), 50);
        }
    }
    
    public void onEvent(final RefreshEvent refreshEvent) {
        if ((refreshEvent.getMediaType() == null || this.mMediaType.equals(refreshEvent.getMediaType())) && !this.mFilter.equals(refreshEvent.getFilter())) {
            this.mFilter = refreshEvent.getFilter();
            if (refreshEvent.getGATrackingTag() != null && !refreshEvent.getGATrackingTag().isEmpty()) {
                this.trackViewExtras(refreshEvent.getGATrackingTag());
            }
            else {
                this.trackView();
            }
            this.mSeasons = CrunchyrollApplication.getApp(this).getCategories(this.mMediaType).getSeasons();
            this.mSeriesAdapter.setMediaType(this.mMediaType);
            this.mSeriesAdapter.setFilter(this.mFilter);
            this.swrveScreenView();
            this.onRefresh();
        }
    }
    
    @Override
    public void onRefresh() {
        this.loadMoreItems(0, this.mInitialLoadLimit);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (this.mReloadOnResume) {
            this.mReloadOnResume = false;
            this.loadItemsWithOffset(0, this.mInitialLoadLimit);
        }
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("list", (Serializable)this.mSeriesItems);
        bundle.putSerializable("seriesId", (Serializable)this.mQueueSeriesIds);
        bundle.putSerializable("seasons", (Serializable)this.mSeasons);
        bundle.putString("filter", this.mFilter);
    }
    
    @Override
    public void onStart() {
        super.onStart();
    }
    
    @Override
    public void onStop() {
        super.onStop();
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mQueueSeriesIds = new HashSet<Long>();
        this.createNewAdapter();
        this.mSeriesAdapter.setEmptyHints(LocalizedStrings.EMPTY_GENRE_TEXT.get(), LocalizedStrings.EMPTY_GENRE_DESCRIPTION.get());
        if (this.mSeriesItems.isEmpty()) {
            this.mSeriesAdapter.setEmptyState();
        }
        this.mRecyclerView.setAdapter((RecyclerView.Adapter)this.mSeriesAdapter);
        this.mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                final ViewTreeObserver viewTreeObserver = SeriesListFragment.this.mRecyclerView.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    if (Build$VERSION.SDK_INT >= 16) {
                        viewTreeObserver.removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                    }
                    else {
                        viewTreeObserver.removeGlobalOnLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                    }
                }
                if (SeriesListFragment.this.mSeriesAdapter != null) {
                    SeriesListFragment.this.mRecycleViewWidth = SeriesListFragment.this.mRecyclerView.getWidth();
                    SeriesListFragment.this.cardTypeChange(SeriesListFragment.this.getApplicationState().getCardType());
                }
            }
        });
    }
    
    public SeriesListFragment setParameters(final String mMediaType, final String mFilter, final ArrayList<Series> mSeriesItems, final List<QueueEntry> list) {
        this.mMediaType = mMediaType;
        this.mFilter = mFilter;
        this.mSeasons = CrunchyrollApplication.getApp(this).getCategories(mMediaType).getSeasons();
        if (this.mSeriesItems != null) {
            this.mSeriesItems.clear();
            if (mSeriesItems != null) {
                final Iterator<Series> iterator = mSeriesItems.iterator();
                while (iterator.hasNext()) {
                    this.mSeriesItems.add(iterator.next());
                }
            }
        }
        else {
            this.mSeriesItems = mSeriesItems;
        }
        if (this.mQueueSeriesIds == null) {
            this.mQueueSeriesIds = new HashSet<Long>();
        }
        if (this.mQueueSeriesIds != null && list != null) {
            final Iterator<QueueEntry> iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                this.mQueueSeriesIds.add(iterator2.next().getSeries().getSeriesId());
            }
        }
        return this;
    }
    
    public void swrveScreenView() {
        if (this.mMediaType != null && this.mFilter != null) {
            switch (this.mType) {
                case ANIME: {
                    if (this.mFilter.equalsIgnoreCase("popular")) {
                        Tracker.swrveScreenView("anime-sort-popular");
                    }
                    if (this.mFilter.equalsIgnoreCase("alpha")) {
                        Tracker.swrveScreenView("anime-sort-alpha");
                    }
                    if (!Filters.isTag(this.mFilter)) {
                        break;
                    }
                    if (Filters.isSeason(this.mFilter)) {
                        Tracker.swrveScreenView("anime-sort-seasons-" + Filters.removeSeason(Filters.removeTag(this.mFilter)));
                        return;
                    }
                    Tracker.swrveScreenView("anime-sort-genres-" + Filters.removeTag(this.mFilter));
                }
                case DRAMA: {
                    if (this.mFilter.equalsIgnoreCase("popular")) {
                        Tracker.swrveScreenView("drama-sort-popular");
                    }
                    if (this.mFilter.equalsIgnoreCase("alpha")) {
                        Tracker.swrveScreenView("drama-sort-alpha");
                    }
                    if (!Filters.isTag(this.mFilter)) {
                        break;
                    }
                    if (Filters.isSeason(this.mFilter)) {
                        Tracker.swrveScreenView("drama-sort-seasons-" + Filters.removeSeason(Filters.removeTag(this.mFilter)));
                        return;
                    }
                    Tracker.swrveScreenView("drama-sort-genres-" + Filters.removeTag(this.mFilter));
                }
            }
        }
    }
    
    @Override
    public void trackView() {
        this.trackViewExtras(this.mMediaType + "_sort", Filters.getTitle((Context)this.getActivity(), this.mMediaType, this.mFilter));
        this.swrveScreenView();
    }
    
    protected class LoadMoreSeriesTask extends SafeAsyncTask<List<Series>>
    {
        private final Set<String> fields;
        protected final String filter;
        protected final Integer limit;
        protected final String mediaType;
        protected final Integer offset;
        
        public LoadMoreSeriesTask(final String mediaType, final String filter, final Integer offset, final Integer limit) {
            this.fields = ImmutableSet.of("series.series_id", "series.name", "series.portrait_image", "series.media_count", "media.media_id", "media.name", "series.media_type", "media.episode_number", "series.url", "series.description", "image.medium_url", "image.large_url", "image.full_url");
            this.mediaType = mediaType;
            this.filter = filter;
            this.offset = offset;
            this.limit = limit;
        }
        
        @Override
        public List<Series> call() throws Exception {
            final CrunchyrollApplication crunchyrollApplication = (CrunchyrollApplication)SeriesListFragment.this.getActivity().getApplication();
            final ObjectMapper objectMapper = crunchyrollApplication.getObjectMapper();
            final ApiResponse run = crunchyrollApplication.getApiService().run(new ListSeriesRequest(this.mediaType, this.filter, this.offset, this.limit), this.fields);
            final List<Series> list = objectMapper.readValue(run.body.asParser(objectMapper).readValueAsTree().path("data").traverse(), new TypeReference<List<Series>>() {});
            run.cache();
            return list;
        }
        
        @Override
        protected void onException(final Exception ex) throws RuntimeException {
            SeriesListFragment.log.error("Error loading more series", ex);
            if (ex instanceof ApiNetworkException) {
                SeriesListFragment.this.mSeriesAdapter.showLoadingError(LocalizedStrings.ERROR_NETWORK.get(), 3);
            }
            else if (!(ex instanceof InterruptedException)) {
                SeriesListFragment.this.mSeriesAdapter.showLoadingError(LocalizedStrings.ERROR_LOADING_SERIES.get(), 3);
            }
        }
        
        @Override
        protected void onFinally() throws RuntimeException {
            SeriesListFragment.this.mLoadTask = null;
            SeriesListFragment.this.mSwipeRefresh.setRefreshing(false);
            Util.hideProgressBar((Context)SeriesListFragment.this.getActivity(), SeriesListFragment.this.mParent);
            SeriesListFragment.this.enableLoading();
        }
        
        public void onPreExecute() {
            SeriesListFragment.this.mSeriesAdapter.setLoadingStart(3);
            if (!SeriesListFragment.this.mSwipeRefresh.isRefreshing() && this.offset == 0) {
                Util.showProgressBar((Context)SeriesListFragment.this.getActivity(), SeriesListFragment.this.mParent, SeriesListFragment.this.getResources().getColor(17170445));
            }
        }
        
        @Override
        protected void onSuccess(final List<Series> list) throws Exception {
            if (this.isCancelled()) {
                return;
            }
            if (this.offset == 0) {
                SeriesListFragment.this.mSeriesItems.clear();
            }
            if (list != null && !list.isEmpty()) {
                SeriesListFragment.this.mSeriesItems.addAll(list);
            }
            if (SeriesListFragment.this.mSeriesItems.size() < this.offset + this.limit) {
                SeriesListFragment.log.info("LOADING EXHAUSTED", new Object[0]);
                SeriesListFragment.this.mSeriesAdapter.hideLoading();
            }
            if (SeriesListFragment.this.mSeriesItems.isEmpty() && list != null && list.isEmpty()) {
                SeriesListFragment.log.info("EMPTY", new Object[0]);
                SeriesListFragment.this.mSeriesAdapter.setEmptyState();
            }
            ((RecyclerView.Adapter)SeriesListFragment.this.mSeriesAdapter).notifyDataSetChanged();
        }
    }
    
    public enum Type
    {
        ANIME, 
        DRAMA, 
        SEARCH, 
        THIS_SEASON;
    }
}
