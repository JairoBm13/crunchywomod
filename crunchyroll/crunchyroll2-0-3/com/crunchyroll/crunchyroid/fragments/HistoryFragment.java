// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import android.view.ViewTreeObserver;
import android.os.Build$VERSION;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import com.crunchyroll.crunchyroid.itemdecorations.MediaCardItemDecoration;
import com.crunchyroll.crunchyroid.events.LoadMore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.LayoutInflater;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.crunchyroll.android.api.models.Media;
import java.util.Iterator;
import com.google.common.base.Optional;
import android.content.Intent;
import com.crunchyroll.crunchyroid.app.Extras;
import android.os.Bundle;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import android.app.Activity;
import com.crunchyroll.android.api.ApiManager;
import java.util.Collection;
import com.crunchyroll.crunchyroid.events.Empty;
import de.greenrobot.event.EventBus;
import android.content.Context;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import com.google.common.collect.Lists;
import com.crunchyroll.android.util.LoggerFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import android.os.Handler;
import com.crunchyroll.crunchyroid.widget.CustomSwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.content.BroadcastReceiver;
import android.view.ViewGroup;
import com.crunchyroll.android.api.tasks.BaseListener;
import com.crunchyroll.crunchyroid.adapters.HistoryAdapter;
import com.crunchyroll.android.api.models.RecentlyWatchedItem;
import java.util.List;
import com.crunchyroll.android.util.Logger;

public class HistoryFragment extends EpisodeListBaseFragment
{
    private static final Logger log;
    private Runnable deferredProgressUpdate;
    private List<RecentlyWatchedItem> historyItems;
    private Integer lastOffset;
    private HistoryAdapter mAdapter;
    private boolean mIsLoadingExhausted;
    private boolean mIsNewObject;
    private boolean mIsPortrait;
    private boolean mIsTablet;
    private BaseListener<List<RecentlyWatchedItem>> mLoadHistoryListener;
    private ViewGroup mParent;
    private BroadcastReceiver mProgressUpdateReceiver;
    private RecyclerView mRecyclerView;
    private CustomSwipeRefreshLayout mSwipeRefresh;
    private int numColumns;
    private Handler progressHandler;
    private AtomicBoolean readyForMore;
    private boolean reloadOnResume;
    private BroadcastReceiver reloadReceiver;
    
    static {
        log = LoggerFactory.getLogger(HistoryFragment.class);
    }
    
    public HistoryFragment() {
        this.historyItems = (List<RecentlyWatchedItem>)Lists.newArrayList();
        this.readyForMore = new AtomicBoolean(false);
        this.lastOffset = 0;
        this.mProgressUpdateReceiver = null;
        this.progressHandler = new Handler();
        this.deferredProgressUpdate = null;
        this.mLoadHistoryListener = new BaseListener<List<RecentlyWatchedItem>>() {
            @Override
            public void onException(final Exception ex) {
                HistoryFragment.log.error("Error loading more histories", ex);
                if (ex instanceof ApiNetworkException) {
                    HistoryFragment.this.mAdapter.showLoadingError(LocalizedStrings.ERROR_NETWORK.get());
                    return;
                }
                HistoryFragment.this.mAdapter.showLoadingError(LocalizedStrings.ERROR_LOADING_HISTORY.get());
            }
            
            @Override
            public void onFinally() {
                HistoryFragment.this.mSwipeRefresh.setRefreshing(false);
                Util.hideProgressBar((Context)HistoryFragment.this.getActivity(), HistoryFragment.this.mParent);
                HistoryFragment.this.enableLoading();
            }
            
            @Override
            public void onPreExecute() {
                if (!HistoryFragment.this.mSwipeRefresh.isRefreshing() && HistoryFragment.this.historyItems.isEmpty()) {
                    Util.showProgressBar((Context)HistoryFragment.this.getActivity(), HistoryFragment.this.mParent, HistoryFragment.this.getResources().getColor(17170445));
                }
                HistoryFragment.this.mAdapter.showLoadingProgress();
                HistoryFragment.this.readyForMore.set(false);
            }
            
            @Override
            public void onSuccess(final List<RecentlyWatchedItem> list) {
                if (HistoryFragment.this.mSwipeRefresh.isRefreshing()) {
                    HistoryFragment.this.historyItems.clear();
                }
                if (list == null || list.size() == 0) {
                    if (HistoryFragment.this.lastOffset == 0) {
                        EventBus.getDefault().post(new Empty.HistoryEvent());
                    }
                }
                else {
                    Util.hideProgressBar((Context)HistoryFragment.this.getActivity(), HistoryFragment.this.mParent);
                    HistoryFragment.this.historyItems.addAll(list);
                    ((RecyclerView.Adapter)HistoryFragment.this.mAdapter).notifyDataSetChanged();
                    if (HistoryFragment.this.deferredProgressUpdate != null) {
                        HistoryFragment.this.progressHandler.post(HistoryFragment.this.deferredProgressUpdate);
                    }
                }
                if (HistoryFragment.this.historyItems.size() < HistoryFragment.this.lastOffset + 50) {
                    HistoryFragment.log.info("LOADING EXHAUSTED", new Object[0]);
                    HistoryFragment.this.mIsLoadingExhausted = true;
                    HistoryFragment.this.mAdapter.hideLoading();
                }
                ((RecyclerView.Adapter)HistoryFragment.this.mAdapter).notifyDataSetChanged();
            }
        };
        this.mIsNewObject = true;
    }
    
    private void loadItemsWithOffset(final int n) {
        ApiManager.getInstance(this.getActivity()).getHistoryData("anime|drama", n, 50, this.mLoadHistoryListener);
    }
    
    private void loadMoreItems() {
        synchronized (this.readyForMore) {
            if (this.readyForMore.compareAndSet(true, false)) {
                this.lastOffset = this.historyItems.size();
                this.loadItemsWithOffset(this.lastOffset);
            }
        }
    }
    
    public static HistoryFragment newInstance() {
        final HistoryFragment historyFragment = new HistoryFragment();
        historyFragment.setArguments(new Bundle());
        return historyFragment;
    }
    
    private void reloadItems() {
        this.historyItems.clear();
        this.enableLoading();
        this.lastOffset = 0;
        this.loadItemsWithOffset(0);
    }
    
    public void enableLoading() {
        this.readyForMore.set(true);
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mIsTablet = (this.getResources().getInteger(2131427330) != 0);
        this.mIsPortrait = (this.getResources().getConfiguration().orientation == 1);
        if (this.mIsTablet) {
            if (this.mIsPortrait) {
                this.numColumns = 3;
            }
            else {
                this.numColumns = 4;
            }
        }
        else {
            this.numColumns = 2;
        }
        this.mIsLoadingExhausted = false;
        if (bundle == null) {
            if (this.mIsNewObject) {
                this.mIsNewObject = false;
                this.reloadOnResume = true;
            }
            else {
                this.reloadOnResume = false;
            }
        }
        else {
            this.reloadOnResume = false;
            this.historyItems = Extras.getList(bundle, "list", RecentlyWatchedItem.class).get();
            if (!(this.mIsLoadingExhausted = Extras.getBoolean(bundle, "isExhausted").get())) {
                this.enableLoading();
            }
        }
        this.reloadReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                HistoryFragment.this.reloadOnResume = true;
                HistoryFragment.log.debug("Received history update ping, will update on next resume.", new Object[0]);
            }
        };
        this.mProgressUpdateReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                HistoryFragment.this.deferredProgressUpdate = new Runnable() {
                    @Override
                    public void run() {
                        HistoryFragment.log.debug("Updating progress bar", new Object[0]);
                        final Optional<Integer> int1 = Extras.getInt(intent, "playhead");
                        final Optional<Long> long1 = Extras.getLong(intent, "mediaId");
                        if (int1.isPresent()) {
                            final Iterator<RecentlyWatchedItem> iterator = HistoryFragment.this.historyItems.iterator();
                            while (iterator.hasNext()) {
                                final Media media = iterator.next().getMedia();
                                if (media.getMediaId().equals(long1.get())) {
                                    HistoryFragment.log.debug("PROGRESS PLAYHEAD: video stopped - playhead at " + int1.get() + " / " + media.getDuration().get(), new Object[0]);
                                    media.setPlayhead(int1.get());
                                    ((RecyclerView.Adapter)HistoryFragment.this.mAdapter).notifyDataSetChanged();
                                }
                            }
                        }
                        HistoryFragment.this.deferredProgressUpdate = null;
                    }
                };
            }
        };
        LocalBroadcastManager.getInstance((Context)this.getActivity()).registerReceiver(this.reloadReceiver, new IntentFilter("VIDEO_STARTED"));
        LocalBroadcastManager.getInstance((Context)this.getActivity()).registerReceiver(this.mProgressUpdateReceiver, new IntentFilter("VIDEO_PROGRESS_UPDATED"));
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2130903128, viewGroup, false);
        this.mParent = (ViewGroup)inflate.findViewById(2131624060);
        this.mRecyclerView = (RecyclerView)inflate.findViewById(2131624242);
        (this.mSwipeRefresh = (CustomSwipeRefreshLayout)inflate.findViewById(2131624187)).setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener)this);
        return inflate;
    }
    
    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance((Context)this.getActivity()).unregisterReceiver(this.reloadReceiver);
        LocalBroadcastManager.getInstance((Context)this.getActivity()).unregisterReceiver(this.mProgressUpdateReceiver);
        this.progressHandler.removeCallbacks((Runnable)null);
        super.onDestroy();
    }
    
    public void onEvent(final LoadMore.HistoryEvent historyEvent) {
        if (this.readyForMore.get()) {
            this.loadMoreItems();
        }
    }
    
    @Override
    protected void onLoadImagesSettingChanged() {
        if (this.mAdapter != null) {
            ((RecyclerView.Adapter)this.mAdapter).notifyDataSetChanged();
        }
    }
    
    @Override
    public void onRefresh() {
        this.lastOffset = 0;
        this.loadItemsWithOffset(0);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (this.reloadOnResume) {
            this.reloadOnResume = false;
            this.reloadItems();
        }
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Extras.putList(bundle, "list", this.historyItems);
        Extras.putBoolean(bundle, "isExhausted", this.mIsLoadingExhausted);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        this.mAdapter = new HistoryAdapter(this.getActivity(), this.historyItems, this.numColumns);
        if (this.mIsLoadingExhausted) {
            this.mAdapter.hideLoading();
        }
        this.mRecyclerView.setLayoutManager((RecyclerView.LayoutManager)this.mAdapter.getGridLayoutManager());
        this.mRecyclerView.addItemDecoration((RecyclerView.ItemDecoration)new MediaCardItemDecoration(this.numColumns));
        this.mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                final ViewTreeObserver viewTreeObserver = HistoryFragment.this.mRecyclerView.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    if (Build$VERSION.SDK_INT >= 16) {
                        viewTreeObserver.removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                    }
                    else {
                        viewTreeObserver.removeGlobalOnLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                    }
                }
                if (HistoryFragment.this.mAdapter != null) {
                    HistoryFragment.this.mAdapter.setThumbwidth(HistoryFragment.this.mRecyclerView.getWidth() / HistoryFragment.this.numColumns);
                    HistoryFragment.this.mRecyclerView.setAdapter((RecyclerView.Adapter)HistoryFragment.this.mAdapter);
                }
            }
        });
    }
    
    public void setReloadAfterAttach() {
        this.mIsNewObject = true;
    }
}
