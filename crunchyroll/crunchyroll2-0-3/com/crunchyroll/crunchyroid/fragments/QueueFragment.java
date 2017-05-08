// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import android.view.ViewTreeObserver;
import android.os.Build$VERSION;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import com.crunchyroll.crunchyroid.itemdecorations.MediaCardItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import java.util.List;
import java.io.Serializable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.LayoutInflater;
import java.util.Iterator;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.models.Media;
import com.crunchyroll.crunchyroid.app.Extras;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import java.util.Collection;
import com.crunchyroll.crunchyroid.events.Empty;
import com.crunchyroll.crunchyroid.events.ErrorEvent;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import de.greenrobot.event.EventBus;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import android.content.Context;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.android.api.tasks.BaseListener;
import android.app.Activity;
import com.crunchyroll.android.api.ApiManager;
import android.os.Bundle;
import com.google.common.collect.Lists;
import com.crunchyroll.crunchyroid.widget.CustomSwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import com.crunchyroll.android.api.models.QueueEntry;
import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.view.ViewGroup;
import com.crunchyroll.crunchyroid.adapters.QueueAdapter;
import com.crunchyroll.cast.CastHandler;

public class QueueFragment extends EpisodeListBaseFragment implements PlayheadListener
{
    private QueueAdapter mAdapter;
    private boolean mIsPortrait;
    private boolean mIsReloadOnCreate;
    private boolean mIsTablet;
    private ViewGroup mParent;
    private BroadcastReceiver mPlayheadUpdatedReceiver;
    private ArrayList<QueueEntry> mQueueList;
    private BroadcastReceiver mQueueUpdatedReceiver;
    private RecyclerView mRecyclerView;
    private CustomSwipeRefreshLayout mSwipeRefresh;
    private int numColumns;
    private boolean reloadOnResume;
    
    public QueueFragment() {
        this.mQueueList = Lists.newArrayList();
        this.mIsReloadOnCreate = true;
    }
    
    public static QueueFragment newInstance() {
        final QueueFragment queueFragment = new QueueFragment();
        queueFragment.setArguments(new Bundle());
        return queueFragment;
    }
    
    private void reloadQueueItems(final boolean b) {
        if (!b) {
            this.mQueueList.clear();
            ((RecyclerView.Adapter)this.mAdapter).notifyDataSetChanged();
        }
        ApiManager.getInstance(this.getActivity()).getQueueData("anime|drama", new BaseListener<ArrayList<QueueEntry>>() {
            @Override
            public void onException(final Exception ex) {
                Util.hideProgressBar((Context)QueueFragment.this.getActivity(), QueueFragment.this.mParent);
                if (ex instanceof ApiNetworkException) {
                    EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_NETWORK.get()));
                    return;
                }
                EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_LOADING_QUEUE.get()));
            }
            
            @Override
            public void onFinally() {
                super.onFinally();
                QueueFragment.this.mSwipeRefresh.setRefreshing(false);
            }
            
            @Override
            public void onPreExecute() {
                super.onPreExecute();
                if (!QueueFragment.this.mSwipeRefresh.isRefreshing()) {
                    Util.showProgressBar((Context)QueueFragment.this.getActivity(), QueueFragment.this.mParent, QueueFragment.this.getResources().getColor(17170445));
                }
            }
            
            @Override
            public void onSuccess(final ArrayList<QueueEntry> list) {
                if (list == null || list.size() == 0) {
                    EventBus.getDefault().post(new Empty.QueueEvent());
                    return;
                }
                Util.hideProgressBar((Context)QueueFragment.this.getActivity(), QueueFragment.this.mParent);
                QueueFragment.this.mQueueList.clear();
                QueueFragment.this.mQueueList.addAll(list);
                ((RecyclerView.Adapter)QueueFragment.this.mAdapter).notifyDataSetChanged();
            }
        });
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
        if (bundle == null) {
            if (this.mIsReloadOnCreate) {
                this.mIsReloadOnCreate = false;
                this.reloadOnResume = true;
            }
            else {
                this.reloadOnResume = false;
            }
        }
        else {
            this.reloadOnResume = false;
            this.mQueueList = (ArrayList<QueueEntry>)bundle.getSerializable("queue_list");
        }
        this.mQueueUpdatedReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("QUEUE_UPDATED")) {
                    QueueFragment.this.reloadOnResume = true;
                }
            }
        };
        LocalBroadcastManager.getInstance((Context)this.getActivity()).registerReceiver(this.mQueueUpdatedReceiver, new IntentFilter("QUEUE_UPDATED"));
        this.mPlayheadUpdatedReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final Optional<Integer> int1 = Extras.getInt(intent, "playhead");
                final Optional<Long> long1 = Extras.getLong(intent, "mediaId");
                if (int1.isPresent()) {
                    for (final QueueEntry queueEntry : QueueFragment.this.mQueueList) {
                        if (queueEntry.getMostLikelyMedia().isPresent()) {
                            final Media media = queueEntry.getMostLikelyMedia().get();
                            if (!media.getMediaId().equals(long1.get())) {
                                continue;
                            }
                            media.setPlayhead(int1.get());
                            ((RecyclerView.Adapter)QueueFragment.this.mAdapter).notifyDataSetChanged();
                        }
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance((Context)this.getActivity()).registerReceiver(this.mPlayheadUpdatedReceiver, new IntentFilter("VIDEO_PROGRESS_UPDATED"));
        CastHandler.get().setThreshold(0.81f);
        CastHandler.get().setPlayheadListener((CastHandler.PlayheadListener)this);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2130903128, viewGroup, false);
        this.mParent = (ViewGroup)inflate.findViewById(2131624060);
        (this.mRecyclerView = (RecyclerView)inflate.findViewById(2131624242)).setHasFixedSize(true);
        (this.mSwipeRefresh = (CustomSwipeRefreshLayout)inflate.findViewById(2131624187)).setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener)this);
        return inflate;
    }
    
    @Override
    public void onDestroy() {
        CastHandler.get().setPlayheadListener(null);
        LocalBroadcastManager.getInstance((Context)this.getActivity()).unregisterReceiver(this.mQueueUpdatedReceiver);
        LocalBroadcastManager.getInstance((Context)this.getActivity()).unregisterReceiver(this.mPlayheadUpdatedReceiver);
        super.onDestroy();
    }
    
    @Override
    protected void onLoadImagesSettingChanged() {
        if (this.mAdapter != null) {
            ((RecyclerView.Adapter)this.mAdapter).notifyDataSetChanged();
        }
    }
    
    @Override
    public void onPlaybackStop() {
        if (this.reloadOnResume) {
            this.reloadOnResume = false;
            this.reloadQueueItems(true);
        }
    }
    
    @Override
    public void onRefresh() {
        this.reloadQueueItems(true);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (this.reloadOnResume) {
            this.reloadQueueItems(this.reloadOnResume = false);
        }
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("queue_list", (Serializable)this.mQueueList);
    }
    
    @Override
    public void onThreshold() {
        this.reloadOnResume = true;
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        this.mAdapter = new QueueAdapter(this.getActivity(), this.mQueueList);
        this.mRecyclerView.setLayoutManager((RecyclerView.LayoutManager)new GridLayoutManager((Context)this.getActivity(), this.numColumns));
        this.mRecyclerView.addItemDecoration((RecyclerView.ItemDecoration)new MediaCardItemDecoration(this.numColumns));
        this.mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                final ViewTreeObserver viewTreeObserver = QueueFragment.this.mRecyclerView.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    if (Build$VERSION.SDK_INT >= 16) {
                        viewTreeObserver.removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                    }
                    else {
                        viewTreeObserver.removeGlobalOnLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                    }
                }
                if (QueueFragment.this.mAdapter != null) {
                    QueueFragment.this.mAdapter.setThumbwidth(QueueFragment.this.mRecyclerView.getWidth() / QueueFragment.this.numColumns);
                    QueueFragment.this.mRecyclerView.setAdapter((RecyclerView.Adapter)QueueFragment.this.mAdapter);
                }
            }
        });
    }
    
    public void setReloadAfterAttach() {
        this.mIsReloadOnCreate = true;
    }
}
