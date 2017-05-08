// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import com.crunchyroll.crunchyroid.events.RefreshEvent;
import android.os.Bundle;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import com.crunchyroll.android.api.Filters;
import com.crunchyroll.android.api.models.Category;
import android.content.Context;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.crunchyroid.events.ErrorEvent;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import de.greenrobot.event.EventBus;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import com.crunchyroll.android.api.models.InitialBrowseData;
import com.crunchyroll.android.api.tasks.BaseListener;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.app.Activity;
import com.crunchyroll.android.api.ApiManager;

public class ThisSeasonListFragment extends SeriesListFragment
{
    public static ThisSeasonListFragment newInstance() {
        return new ThisSeasonListFragment();
    }
    
    @Override
    protected void loadItemsWithOffset(final Integer n, final int n2) {
        if (this.mMediaType != null && this.mFilter != null) {
            (this.mLoadTask = new LoadMoreSeriesTask(this, this.mMediaType, this.mFilter, n, n2)).execute();
            return;
        }
        this.mMediaType = "anime";
        ApiManager.getInstance(this.getActivity()).getInitialBrowseData(this.mMediaType, ((CrunchyrollApplication)this.getActivity().getApplicationContext()).getApplicationState().hasLoggedInUser(), new BaseListener<InitialBrowseData>() {
            @Override
            public void onException(final Exception ex) throws RuntimeException {
                if (ex instanceof ApiNetworkException) {
                    EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_NETWORK.get()));
                    ThisSeasonListFragment.this.mSeriesAdapter.showLoadingError(LocalizedStrings.ERROR_NETWORK.get(), 17);
                    return;
                }
                EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_LOADING_SERIES.get()));
                ThisSeasonListFragment.this.mSeriesAdapter.showLoadingError(LocalizedStrings.ERROR_LOADING_SERIES.get(), 17);
            }
            
            @Override
            public void onFinally() throws RuntimeException {
                ThisSeasonListFragment.this.enableLoading();
                ThisSeasonListFragment.this.mSwipeRefresh.setRefreshing(false);
                Util.hideProgressBar((Context)ThisSeasonListFragment.this.getActivity(), ThisSeasonListFragment.this.mParent);
            }
            
            @Override
            public void onPreExecute() {
                if (!ThisSeasonListFragment.this.mSwipeRefresh.isRefreshing() && n == 0) {
                    Util.showProgressBar((Context)ThisSeasonListFragment.this.getActivity(), ThisSeasonListFragment.this.mParent, ThisSeasonListFragment.this.getResources().getColor(17170445));
                }
                ThisSeasonListFragment.this.mSeriesAdapter.setLoadingStart(17);
            }
            
            @Override
            public void onSuccess(final InitialBrowseData initialBrowseData) {
                ThisSeasonListFragment.this.mFilter = Filters.addTag(initialBrowseData.getCategories().getSeasons().get(0).getTag());
                ThisSeasonListFragment.this.loadItemsWithOffset(0, ThisSeasonListFragment.this.mInitialLoadLimit);
            }
        });
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mShouldLoadMoreInSeriesDetailViewPager = false;
        this.mType = Type.THIS_SEASON;
        this.mIsShowTitle = false;
        this.mInitialLoadLimit = 5000;
    }
    
    @Override
    public void onEvent(final RefreshEvent refreshEvent) {
    }
}
