// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import java.util.Collection;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import java.util.Iterator;
import com.crunchyroll.android.api.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.crunchyroll.android.api.ApiService;
import com.crunchyroll.android.api.tasks.LoadQueueTask;
import com.crunchyroll.android.api.requests.QueueRequest;
import com.crunchyroll.android.api.models.QueueEntry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.AutoCompleteRequest;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.android.util.SafeAsyncTask;
import android.content.res.Configuration;
import android.view.ViewTreeObserver;
import android.os.Build$VERSION;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import com.crunchyroll.crunchyroid.events.LoadMore;
import com.crunchyroll.crunchyroid.events.CardTypeEvent;
import android.view.View;
import android.view.LayoutInflater;
import com.google.common.base.Optional;
import de.greenrobot.event.EventBus;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import java.util.Set;
import android.app.Activity;
import com.google.common.collect.Lists;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import java.io.Serializable;
import android.os.Bundle;
import com.crunchyroll.crunchyroid.app.Extras;
import android.support.v7.widget.LinearLayoutManager;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import com.crunchyroll.android.util.LoggerFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import com.crunchyroll.crunchyroid.widget.CustomSwipeRefreshLayout;
import android.content.SharedPreferences$OnSharedPreferenceChangeListener;
import com.crunchyroll.android.api.models.Series;
import java.util.List;
import com.crunchyroll.crunchyroid.adapters.SeriesEntryAdapter;
import java.util.HashSet;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import com.crunchyroll.android.util.Logger;
import android.support.v4.widget.SwipeRefreshLayout;

public class SearchFragment extends BaseFragment implements OnRefreshListener
{
    private static final Logger log;
    private boolean mIsPortrait;
    private boolean mIsTablet;
    private RecyclerView.LayoutManager mLayoutManager;
    private AutoCompleteTask mLoadTask;
    private int mNumColumns;
    private ViewGroup mParent;
    private HashSet<Long> mQueueEntries;
    private int mRecycleViewWidth;
    private RecyclerView mRecyclerView;
    private String mSearchString;
    private SeriesEntryAdapter mSeriesAdapter;
    private List<Series> mSeriesItems;
    private SharedPreferences$OnSharedPreferenceChangeListener mSettingsListener;
    protected CustomSwipeRefreshLayout mSwipeRefresh;
    private AtomicBoolean readyForMore;
    
    static {
        log = LoggerFactory.getLogger(SearchFragment.class);
    }
    
    public SearchFragment() {
        this.mIsTablet = false;
        this.readyForMore = new AtomicBoolean(false);
        this.mLoadTask = null;
        this.mQueueEntries = new HashSet<Long>();
        this.mIsPortrait = true;
        this.mRecycleViewWidth = -1;
        this.mNumColumns = 1;
    }
    
    private void cardTypeChange(int dimensionPixelSize) {
        final int n = 4;
        this.mSeriesAdapter.setCardType(dimensionPixelSize);
        if (this.mIsTablet) {
            this.mSeriesAdapter.setCardType(2);
            if (this.mRecycleViewWidth > 0) {
                dimensionPixelSize = this.getResources().getDimensionPixelSize(2131230734);
                this.mNumColumns = Math.max(4, Math.round(this.mRecycleViewWidth / dimensionPixelSize));
            }
            else {
                if (this.mIsPortrait) {
                    dimensionPixelSize = n;
                }
                else {
                    dimensionPixelSize = 5;
                }
                this.mNumColumns = dimensionPixelSize;
            }
            this.mLayoutManager = new GridLayoutManager((Context)this.getActivity(), this.mNumColumns);
            ((GridLayoutManager)this.mLayoutManager).setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(final int n) {
                    if (SearchFragment.this.mSeriesAdapter.getItemViewType(n) == 1) {
                        return 1;
                    }
                    return SearchFragment.this.mNumColumns;
                }
            });
            this.mSeriesAdapter.setThumbWidth(this.mRecycleViewWidth / this.mNumColumns);
        }
        else if (dimensionPixelSize == 1) {
            this.mLayoutManager = new LinearLayoutManager((Context)this.getActivity());
            this.mSeriesAdapter.setThumbWidth(this.getResources().getDimensionPixelSize(2131230734));
        }
        else if (dimensionPixelSize == 2) {
            this.mLayoutManager = new GridLayoutManager((Context)this.getActivity(), 3);
            ((GridLayoutManager)this.mLayoutManager).setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(final int n) {
                    if (SearchFragment.this.mSeriesAdapter.getItemViewType(n) == 1) {
                        return 1;
                    }
                    return 3;
                }
            });
            this.mSeriesAdapter.setThumbWidth(this.mRecycleViewWidth / 3);
        }
        else if (dimensionPixelSize == 3) {
            this.mLayoutManager = new GridLayoutManager((Context)this.getActivity(), 2);
            ((GridLayoutManager)this.mLayoutManager).setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(final int n) {
                    if (SearchFragment.this.mSeriesAdapter.getItemViewType(n) == 1) {
                        return 1;
                    }
                    return 2;
                }
            });
            this.mSeriesAdapter.setThumbWidth(this.mRecycleViewWidth / 2);
        }
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
    }
    
    private void dispatchLoadMoreItemsTask(int max) {
        final String s = Extras.getString(this.getArguments(), "mediaType").get();
        final String s2 = Extras.getString(this.getArguments(), "filter").orNull();
        max = Math.max(Extras.getInt(this.getArguments(), "offset").get(), max);
        if (!this.mSearchString.isEmpty()) {
            (this.mLoadTask = new AutoCompleteTask(this.mSearchString, s, max, 50)).execute();
        }
        else {
            this.readyForMore.set(true);
            if (this.mSwipeRefresh != null) {
                this.mSwipeRefresh.setRefreshing(false);
            }
        }
    }
    
    private boolean isLoading() {
        return this.mLoadTask != null;
    }
    
    private void loadMoreItems() {
        synchronized (this.readyForMore) {
            if (this.readyForMore.compareAndSet(true, false)) {
                this.dispatchLoadMoreItemsTask(this.mSeriesItems.size());
            }
        }
    }
    
    public static SearchFragment newInstance(final String s, final int n, final String s2, final List<Series> list) {
        final SearchFragment searchFragment = new SearchFragment();
        int size;
        if (list != null) {
            size = list.size();
        }
        else {
            size = 0;
        }
        final Bundle arguments = new Bundle();
        Extras.putString(arguments, "searchString", s.trim());
        Extras.putInt(arguments, "fragmentContentsId", Integer.valueOf(n));
        Extras.putString(arguments, "mediaType", s2);
        Extras.putInt(arguments, "offset", Integer.valueOf(size));
        Extras.putList(arguments, "list", (List<Serializable>)list);
        searchFragment.setArguments(arguments);
        return searchFragment;
    }
    
    private void trackSearchResultSelection(final Series series) {
        Tracker.searchResultSelection((Context)this.getActivity(), this.mSearchString, series);
    }
    
    public void enableLoading() {
        this.readyForMore.set(true);
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mIsTablet = (this.getResources().getInteger(2131427330) != 0);
        this.mSearchString = Extras.getString(this.getArguments(), "searchString").get();
        final Optional<List<Series>> list = Extras.getList(this.getArguments(), "list", Series.class);
        if (bundle != null) {
            this.mSeriesItems = Extras.getList(bundle, "list", Series.class).get();
            this.mSearchString = bundle.getString("searchString");
            this.enableLoading();
        }
        else if (list.isPresent()) {
            this.mSeriesItems = list.get();
            this.enableLoading();
        }
        else {
            this.mSeriesItems = (List<Series>)Lists.newArrayList();
            this.enableLoading();
            this.dispatchLoadMoreItemsTask(this.mSeriesItems.size());
        }
        (this.mSeriesAdapter = new SeriesEntryAdapter(true, this.getActivity(), Extras.getString(this.getArguments(), "mediaType").get(), Extras.getString(this.getArguments(), "filter").orNull(), this.mQueueEntries, this.mSeriesItems, false, false, SeriesListFragment.Type.SEARCH)).setSearchString(this.mSearchString);
        if (this.mSeriesItems.isEmpty()) {
            this.mSeriesAdapter.setEmptyHints(LocalizedStrings.EMPTY_SEARCH.get(), LocalizedStrings.TRY_AGAIN.get());
            if (this.mSearchString != null && !this.mSearchString.isEmpty()) {
                this.mSeriesAdapter.setEmptyState();
            }
        }
        this.mSettingsListener = (SharedPreferences$OnSharedPreferenceChangeListener)new SharedPreferences$OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String s) {
                if (s.equals("imageLoadingEnabled") && SearchFragment.this.mSeriesAdapter != null) {
                    ((RecyclerView.Adapter)SearchFragment.this.mSeriesAdapter).notifyDataSetChanged();
                }
            }
        };
        PreferenceManager.getDefaultSharedPreferences((Context)this.getActivity()).registerOnSharedPreferenceChangeListener(this.mSettingsListener);
        Tracker.swrveScreenView("search");
        EventBus.getDefault().register(this);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2130903117, viewGroup, false);
        this.mParent = (ViewGroup)inflate.findViewById(2131624060);
        this.mRecyclerView = (RecyclerView)inflate.findViewById(2131624188);
        this.mSwipeRefresh = (CustomSwipeRefreshLayout)inflate.findViewById(2131624187);
        return inflate;
    }
    
    @Override
    public void onDestroy() {
        if (this.mLoadTask != null) {
            this.mLoadTask.cancel(true);
        }
        PreferenceManager.getDefaultSharedPreferences((Context)this.getActivity()).unregisterOnSharedPreferenceChangeListener(this.mSettingsListener);
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    
    public void onEvent(final CardTypeEvent cardTypeEvent) {
        this.cardTypeChange(cardTypeEvent.getType());
    }
    
    public void onEvent(final LoadMore.SeriesEvent seriesEvent) {
        if (this.isLoading()) {
            return;
        }
        synchronized (this.readyForMore) {
            if (this.readyForMore.compareAndSet(true, false)) {
                this.dispatchLoadMoreItemsTask(this.mSeriesItems.size());
            }
        }
    }
    
    @Override
    public void onRefresh() {
        synchronized (this.readyForMore) {
            if (this.readyForMore.compareAndSet(true, false)) {
                this.dispatchLoadMoreItemsTask(0);
            }
            else {
                this.mSwipeRefresh.setRefreshing(false);
            }
        }
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("list", (Serializable)this.mSeriesItems);
        bundle.putSerializable("searchString", (Serializable)this.mSearchString.trim());
        bundle.putSerializable("offset", (Serializable)this.mSeriesItems.size());
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mSwipeRefresh.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener)this);
        this.mRecyclerView.setAdapter((RecyclerView.Adapter)this.mSeriesAdapter);
        this.mRecyclerView.requestFocus();
        final Configuration configuration = this.getResources().getConfiguration();
        if (configuration.orientation == 2) {
            this.mIsPortrait = false;
        }
        else if (configuration.orientation == 1) {
            this.mIsPortrait = true;
        }
        this.mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                final ViewTreeObserver viewTreeObserver = SearchFragment.this.mRecyclerView.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    if (Build$VERSION.SDK_INT >= 16) {
                        viewTreeObserver.removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                    }
                    else {
                        viewTreeObserver.removeGlobalOnLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                    }
                }
                if (SearchFragment.this.mSeriesAdapter != null) {
                    SearchFragment.this.mRecycleViewWidth = SearchFragment.this.mRecyclerView.getWidth();
                    SearchFragment.this.cardTypeChange(SearchFragment.this.getApplicationState().getCardType());
                }
            }
        });
    }
    
    public void search(final String mSearchString) {
        this.mSearchString = mSearchString;
        synchronized (this.readyForMore) {
            if (this.readyForMore.compareAndSet(true, false)) {
                this.mSeriesItems.clear();
                this.dispatchLoadMoreItemsTask(this.mSeriesItems.size());
            }
        }
    }
    
    private class AutoCompleteTask extends SafeAsyncTask<List<Series>>
    {
        protected final Integer limit;
        protected final String mediaType;
        protected final Integer offset;
        protected final String searchString;
        
        public AutoCompleteTask(final String searchString, final String mediaType, final Integer offset, final Integer limit) {
            this.searchString = searchString;
            this.mediaType = mediaType;
            this.offset = offset;
            this.limit = limit;
        }
        
        @Override
        public List<Series> call() throws Exception {
            final CrunchyrollApplication crunchyrollApplication = (CrunchyrollApplication)SearchFragment.this.getActivity().getApplication();
            final ApiService apiService = crunchyrollApplication.getApiService();
            final ObjectMapper objectMapper = crunchyrollApplication.getObjectMapper();
            final ApiResponse run = apiService.run(new AutoCompleteRequest(this.searchString, this.mediaType, this.offset, this.limit));
            final List<Series> list = objectMapper.readValue(run.body.asParser(objectMapper).readValueAsTree().path("data").traverse(), new TypeReference<List<Series>>() {});
            run.cache();
            if (SearchFragment.this.getApplicationState().hasLoggedInUser() && SearchFragment.this.mQueueEntries.isEmpty()) {
                final List<QueueEntry> list2 = objectMapper.readValue(crunchyrollApplication.getApiService().run(new QueueRequest("anime|drama"), LoadQueueTask.fields).body.asParser(objectMapper).readValueAsTree().path("data").traverse(), new TypeReference<List<QueueEntry>>() {});
                SearchFragment.this.mQueueEntries.clear();
                final Iterator<QueueEntry> iterator = list2.iterator();
                while (iterator.hasNext()) {
                    SearchFragment.this.mQueueEntries.add(iterator.next().getSeries().getSeriesId());
                }
            }
            return list;
        }
        
        @Override
        protected void onException(final Exception ex) throws RuntimeException {
            if (ex instanceof ApiNetworkException) {
                SearchFragment.this.mSeriesAdapter.showLoadingError(LocalizedStrings.ERROR_NETWORK.get(), 17);
                return;
            }
            SearchFragment.this.mSeriesAdapter.showLoadingError(LocalizedStrings.ERROR_LOADING_SERIES.get(), 17);
        }
        
        @Override
        protected void onFinally() throws RuntimeException {
            SearchFragment.this.mSwipeRefresh.setRefreshing(false);
            Util.hideProgressBar((Context)SearchFragment.this.getActivity(), SearchFragment.this.mParent);
            SearchFragment.this.mLoadTask = null;
            SearchFragment.this.enableLoading();
            if (!this.searchString.equals(SearchFragment.this.mSearchString) && !SearchFragment.this.mSearchString.isEmpty()) {
                synchronized (SearchFragment.this.readyForMore) {
                    if (SearchFragment.this.readyForMore.compareAndSet(true, false)) {
                        SearchFragment.this.mSeriesItems.clear();
                        SearchFragment.this.dispatchLoadMoreItemsTask(SearchFragment.this.mSeriesItems.size());
                    }
                }
            }
        }
        
        @Override
        protected void onPreExecute() throws Exception {
            SearchFragment.this.mSeriesAdapter.setSearchString(this.searchString);
            SearchFragment.this.mSeriesAdapter.setLoadingStart(17);
            if (!SearchFragment.this.mSwipeRefresh.isRefreshing() && this.offset == 0) {
                Util.showProgressBar((Context)SearchFragment.this.getActivity(), SearchFragment.this.mParent, SearchFragment.this.getResources().getColor(17170445));
            }
        }
        
        @Override
        protected void onSuccess(final List<Series> list) throws Exception {
            if (this.isCancelled()) {
                return;
            }
            if (this.offset == 0) {
                SearchFragment.this.mSeriesItems.clear();
            }
            if (list != null && !list.isEmpty()) {
                SearchFragment.this.mSeriesItems.addAll(list);
            }
            if (SearchFragment.this.mSeriesItems.size() < this.offset + this.limit) {
                SearchFragment.log.info("LOADING EXHAUSTED", new Object[0]);
                SearchFragment.this.mSeriesAdapter.hideLoading();
            }
            if (list != null && list.isEmpty()) {
                SearchFragment.log.info("NO SERIES", new Object[0]);
                SearchFragment.this.mSeriesAdapter.showLoadingError(LocalizedStrings.NO_SERIES.get(), 17);
            }
            if (SearchFragment.this.mSeriesItems.isEmpty() && list != null && list.isEmpty()) {
                SearchFragment.log.info("Search EMPTY", new Object[0]);
                SearchFragment.this.mSeriesAdapter.setEmptyState();
            }
            ((RecyclerView.Adapter)SearchFragment.this.mSeriesAdapter).notifyDataSetChanged();
        }
    }
}
