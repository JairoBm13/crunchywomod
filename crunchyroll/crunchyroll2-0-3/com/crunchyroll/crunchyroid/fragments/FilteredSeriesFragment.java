// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.support.v7.widget.LinearLayoutManager;
import android.app.Activity;
import com.crunchyroll.crunchyroid.activities.SortFilterMenuActivity;
import android.content.Context;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.view.View$OnClickListener;
import android.support.v4.widget.SwipeRefreshLayout;
import com.crunchyroll.crunchyroid.widget.CustomSwipeRefreshLayout;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import com.crunchyroll.crunchyroid.adapters.SortFilterSideMenuAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.FloatingActionButton;

public class FilteredSeriesFragment extends SeriesListFragment
{
    private FloatingActionButton mFloatingButton;
    private boolean mIsAllowGenreSelection;
    private RecyclerView mSortFilterRecycler;
    private SortFilterSideMenuAdapter mSortFilterRecyclerAdapter;
    
    public static FilteredSeriesFragment newInstance(final String s, final String s2, final boolean b, final boolean b2) {
        final FilteredSeriesFragment filteredSeriesFragment = new FilteredSeriesFragment();
        final Bundle arguments = new Bundle();
        arguments.putString("mediaType", s);
        arguments.putString("filter", s2);
        arguments.putBoolean("isAllowGenreSelection", b);
        arguments.putBoolean("isShowTitle", b2);
        filteredSeriesFragment.setArguments(arguments);
        return filteredSeriesFragment;
    }
    
    @Override
    protected void loadItemsWithOffset(final Integer n, final int n2) {
        (this.mLoadTask = new LoadMoreSeriesTask(this, this.mMediaType, this.mFilter, n, n2)).execute();
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mMediaType = this.getArguments().getString("mediaType");
        this.mFilter = this.getArguments().getString("filter");
        this.mIsAllowGenreSelection = this.getArguments().getBoolean("isAllowGenreSelection");
        if (this.mFilter == null) {
            this.mFilter = "popular";
        }
        if ("anime".equalsIgnoreCase(this.mMediaType)) {
            this.mType = Type.ANIME;
        }
        else {
            this.mType = Type.DRAMA;
        }
        if (bundle != null) {
            this.mFilter = bundle.getString("filter");
        }
        if (this.getArguments().getBoolean("isShowTitle")) {
            this.mIsShowTitle = false;
        }
        this.mShouldLoadMoreInSeriesDetailViewPager = true;
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2130903129, viewGroup, false);
        this.mParent = (ViewGroup)inflate.findViewById(2131624060);
        this.mCoordinatorLayout = (CoordinatorLayout)inflate.findViewById(2131624186);
        this.registerForContextMenu((View)(this.mRecyclerView = (RecyclerView)inflate.findViewById(2131624242)));
        (this.mSwipeRefresh = (CustomSwipeRefreshLayout)inflate.findViewById(2131624187)).setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener)this);
        this.mFloatingButton = (FloatingActionButton)inflate.findViewById(2131624243);
        if (this.mIsAllowGenreSelection) {
            if (!this.mIsTablet) {
                this.mFloatingButton.setVisibility(0);
                this.mFloatingButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        Tracker.browseCategory((Context)FilteredSeriesFragment.this.getActivity(), FilteredSeriesFragment.this.mMediaType, null, null);
                        SortFilterMenuActivity.start(FilteredSeriesFragment.this.getActivity(), FilteredSeriesFragment.this.mMediaType, FilteredSeriesFragment.this.mFilter);
                    }
                });
                return inflate;
            }
            (this.mSortFilterSideMenu = (ViewGroup)inflate.findViewById(2131624244)).setVisibility(0);
            LayoutInflater.from((Context)this.getActivity()).inflate(2130903132, this.mSortFilterSideMenu, true);
            this.mSortFilterRecycler = (RecyclerView)this.mSortFilterSideMenu.findViewById(2131624242);
            final LinearLayoutManager layoutManager = new LinearLayoutManager((Context)this.getActivity());
            layoutManager.setOrientation(1);
            this.mSortFilterRecycler.setLayoutManager((RecyclerView.LayoutManager)layoutManager);
            if (CrunchyrollApplication.getApp((Context)this.getActivity()).getCategories(this.mMediaType) != null) {
                this.mSortFilterRecyclerAdapter = new SortFilterSideMenuAdapter(this.mMediaType, CrunchyrollApplication.getApp((Context)this.getActivity()).getCategories(this.mMediaType), this.mFilter);
                this.mSortFilterRecycler.setAdapter((RecyclerView.Adapter)this.mSortFilterRecyclerAdapter);
            }
        }
        else if (this.mFloatingButton != null) {
            this.mFloatingButton.setVisibility(8);
            return inflate;
        }
        return inflate;
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("filter", this.mFilter);
    }
}
