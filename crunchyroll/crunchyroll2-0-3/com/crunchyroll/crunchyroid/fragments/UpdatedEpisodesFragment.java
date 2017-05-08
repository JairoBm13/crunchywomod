// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import android.support.v7.widget.GridLayoutManager;
import de.greenrobot.event.EventBus;
import com.crunchyroll.crunchyroid.events.LoadMore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.LayoutInflater;
import android.content.res.Configuration;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;
import com.crunchyroll.crunchyroid.app.Extras;
import android.os.Bundle;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import java.util.Collection;
import android.content.Context;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import com.crunchyroll.android.api.tasks.BaseListener;
import android.app.Activity;
import com.crunchyroll.android.api.ApiManager;
import com.google.common.collect.Lists;
import com.crunchyroll.android.util.LoggerFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import com.crunchyroll.crunchyroid.adapters.UpdatedEpisodesAdapter;
import com.crunchyroll.crunchyroid.widget.CustomSwipeRefreshLayout;
import android.content.BroadcastReceiver;
import android.view.ViewGroup;
import com.crunchyroll.android.api.models.UpdatedEpisode;
import java.util.List;
import android.support.v7.widget.RecyclerView;
import com.crunchyroll.android.util.Logger;
import com.crunchyroll.cast.CastHandler;

public class UpdatedEpisodesFragment extends EpisodeListBaseFragment implements PlayheadListener
{
    private static final Logger log;
    private boolean mIsNewObject;
    private boolean mIsTablet;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<UpdatedEpisode> mMediaList;
    private ViewGroup mParent;
    private BroadcastReceiver mQueueUpdatedReceiver;
    private RecyclerView mRecyclerView;
    private SpanSizeLookup mSpanSizeLookup;
    private CustomSwipeRefreshLayout mSwipeRefresh;
    private UpdatedEpisodesAdapter mUpdatedEpisodesAdapter;
    private int numCols;
    private AtomicBoolean readyForMore;
    private boolean reloadOnResume;
    
    static {
        log = LoggerFactory.getLogger(UpdatedEpisodesFragment.class);
    }
    
    public UpdatedEpisodesFragment() {
        this.mMediaList = (List<UpdatedEpisode>)Lists.newArrayList();
        this.readyForMore = new AtomicBoolean(false);
        this.mIsTablet = false;
        this.numCols = 2;
        this.mIsNewObject = true;
    }
    
    private void loadItemsWithOffset(final int n) {
        ApiManager.getInstance(this.getActivity()).getUpdatedEpisodes("anime", "updated", n, 20, new BaseListener<List<UpdatedEpisode>>() {
            @Override
            public void onException(final Exception ex) {
                if (ex instanceof ApiNetworkException) {
                    UpdatedEpisodesFragment.this.mUpdatedEpisodesAdapter.showLoadingError(LocalizedStrings.ERROR_NETWORK.get());
                    return;
                }
                UpdatedEpisodesFragment.this.mUpdatedEpisodesAdapter.showLoadingError(LocalizedStrings.ERROR_LOADING_EPISODES.get());
            }
            
            @Override
            public void onFinally() {
                super.onFinally();
                Util.hideProgressBar((Context)UpdatedEpisodesFragment.this.getActivity(), UpdatedEpisodesFragment.this.mParent);
                UpdatedEpisodesFragment.this.mSwipeRefresh.setRefreshing(false);
                UpdatedEpisodesFragment.this.enableLoading();
            }
            
            @Override
            public void onPreExecute() {
                super.onPreExecute();
                if (n == 0) {
                    UpdatedEpisodesFragment.this.mMediaList.clear();
                }
                UpdatedEpisodesFragment.this.mUpdatedEpisodesAdapter.showLoadingProgress();
                if (!UpdatedEpisodesFragment.this.mSwipeRefresh.isRefreshing() && n == 0) {
                    Util.showProgressBar((Context)UpdatedEpisodesFragment.this.getActivity(), UpdatedEpisodesFragment.this.mParent, UpdatedEpisodesFragment.this.getResources().getColor(17170445));
                }
            }
            
            @Override
            public void onSuccess(final List<UpdatedEpisode> list) {
                if (list != null && !list.isEmpty()) {
                    UpdatedEpisodesFragment.this.mMediaList.addAll(list);
                    UpdatedEpisodesFragment.this.mSpanSizeLookup.setUpdatedEpisodeEntries(UpdatedEpisodesFragment.this.mUpdatedEpisodesAdapter.notifyEpisodesChanged(UpdatedEpisodesFragment.this.numCols));
                    UpdatedEpisodesFragment.this.enableLoading();
                }
                if (UpdatedEpisodesFragment.this.mMediaList.size() < n + 20) {
                    UpdatedEpisodesFragment.log.info("LOADING EXHAUSTED", new Object[0]);
                    UpdatedEpisodesFragment.this.mUpdatedEpisodesAdapter.hideLoading();
                }
                ((RecyclerView.Adapter)UpdatedEpisodesFragment.this.mUpdatedEpisodesAdapter).notifyDataSetChanged();
            }
        });
    }
    
    private void loadMoreItems() {
        synchronized (this.readyForMore) {
            if (this.readyForMore.compareAndSet(true, false)) {
                this.loadItemsWithOffset(this.mMediaList.size());
            }
        }
    }
    
    public static UpdatedEpisodesFragment newInstance() {
        final UpdatedEpisodesFragment updatedEpisodesFragment = new UpdatedEpisodesFragment();
        updatedEpisodesFragment.setArguments(new Bundle());
        return updatedEpisodesFragment;
    }
    
    public void enableLoading() {
        this.readyForMore.set(true);
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mIsTablet = (this.getResources().getInteger(2131427330) != 0);
        final Configuration configuration = this.getResources().getConfiguration();
        if (configuration.orientation == 2) {
            this.numCols = 4;
        }
        else if (configuration.orientation == 1) {
            this.numCols = 3;
        }
        if (!this.mIsTablet) {
            this.numCols = 2;
        }
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
            this.mMediaList = Extras.getList(bundle, "list", UpdatedEpisode.class).get();
        }
        this.mQueueUpdatedReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("QUEUE_UPDATED")) {
                    UpdatedEpisodesFragment.this.reloadOnResume = true;
                }
            }
        };
        LocalBroadcastManager.getInstance((Context)this.getActivity()).registerReceiver(this.mQueueUpdatedReceiver, new IntentFilter("QUEUE_UPDATED"));
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2130903128, viewGroup, false);
        (this.mRecyclerView = (RecyclerView)inflate.findViewById(2131624242)).setHasFixedSize(true);
        (this.mSwipeRefresh = (CustomSwipeRefreshLayout)inflate.findViewById(2131624187)).setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener)this);
        this.mParent = (ViewGroup)inflate.findViewById(2131624060);
        return inflate;
    }
    
    @Override
    public void onDestroy() {
        CastHandler.get().setPlayheadListener(null);
        LocalBroadcastManager.getInstance((Context)this.getActivity()).unregisterReceiver(this.mQueueUpdatedReceiver);
        super.onDestroy();
    }
    
    public void onEvent(final LoadMore.UpdatedEpisodesEvent updatedEpisodesEvent) {
        this.loadMoreItems();
    }
    
    @Override
    protected void onLoadImagesSettingChanged() {
        if (this.mUpdatedEpisodesAdapter != null) {
            ((RecyclerView.Adapter)this.mUpdatedEpisodesAdapter).notifyDataSetChanged();
        }
    }
    
    @Override
    public void onPlaybackStop() {
        if (this.reloadOnResume) {
            this.reloadOnResume = false;
            this.onRefresh();
        }
    }
    
    @Override
    public void onRefresh() {
        this.loadItemsWithOffset(0);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (this.reloadOnResume) {
            this.reloadOnResume = false;
            this.loadItemsWithOffset(0);
        }
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Extras.putList(bundle, "list", this.mMediaList);
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
    public void onThreshold() {
        this.reloadOnResume = true;
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        this.mUpdatedEpisodesAdapter = new UpdatedEpisodesAdapter(this.getActivity(), this.mMediaList, this.numCols);
        this.mLayoutManager = new GridLayoutManager((Context)this.getActivity(), 100);
        (this.mSpanSizeLookup = new SpanSizeLookup()).setUpdatedEpisodeEntries(this.mUpdatedEpisodesAdapter.getUpdatedEpisodeEntries());
        ((GridLayoutManager)this.mLayoutManager).setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)this.mSpanSizeLookup);
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mRecyclerView.setAdapter((RecyclerView.Adapter)this.mUpdatedEpisodesAdapter);
    }
    
    private class SpanSizeLookup extends GridLayoutManager.SpanSizeLookup
    {
        List<UpdatedEpisodesAdapter.UpdatedEpisodeEntry> updatedEpisodeEntries;
        
        private SpanSizeLookup() {
            this.updatedEpisodeEntries = null;
        }
        
        @Override
        public int getSpanSize(final int n) {
            if (this.updatedEpisodeEntries != null && n < this.updatedEpisodeEntries.size()) {
                final UpdatedEpisodesAdapter.UpdatedEpisodeEntry updatedEpisodeEntry = this.updatedEpisodeEntries.get(n);
                if (updatedEpisodeEntry.getType() != 2) {
                    if (updatedEpisodeEntry.getType() == 3) {
                        if (UpdatedEpisodesFragment.this.numCols == 2) {
                            return 15;
                        }
                        if (UpdatedEpisodesFragment.this.numCols == 3) {
                            return 8;
                        }
                        if (UpdatedEpisodesFragment.this.numCols == 4) {
                            return 4;
                        }
                    }
                    if (UpdatedEpisodesFragment.this.numCols == 2) {
                        return 85;
                    }
                    if (UpdatedEpisodesFragment.this.numCols == 3) {
                        return 46;
                    }
                    if (UpdatedEpisodesFragment.this.numCols == 4) {
                        return 32;
                    }
                }
            }
            return 100;
        }
        
        public void setUpdatedEpisodeEntries(final List<UpdatedEpisodesAdapter.UpdatedEpisodeEntry> updatedEpisodeEntries) {
            this.updatedEpisodeEntries = updatedEpisodeEntries;
        }
    }
}
