// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.activities;

import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import com.crunchyroll.android.api.models.Series;
import java.util.List;
import android.view.ViewTreeObserver;
import android.os.Build$VERSION;
import android.view.ViewGroup$LayoutParams;
import android.view.ViewGroup$MarginLayoutParams;
import android.widget.RelativeLayout;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import com.crunchyroll.crunchyroid.fragments.SearchFragment;
import com.crunchyroll.crunchyroid.widget.SearchBox;
import android.os.Handler;

public final class SearchActivity extends TrackedActivity
{
    private Handler mHandler;
    private boolean mIsTablet;
    private SearchBox mSearchBox;
    private SearchFragment mSearchFragment;
    private SearchRunnable mSearchRunnable;
    
    public SearchActivity() {
        this.mIsTablet = false;
    }
    
    public static void start(final Context context) {
        context.startActivity(new Intent(context, (Class)SearchActivity.class));
    }
    
    @Override
    public void onBackPressed() {
        if (this.mSearchBox.isOpen()) {
            this.mSearchBox.closeEdit();
            return;
        }
        this.finish();
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130903074);
        this.mHandler = new Handler();
        if (!(this.mIsTablet = (this.getResources().getInteger(2131427330) != 0))) {
            this.setRequestedOrientation(1);
        }
        (this.mSearchBox = (SearchBox)this.findViewById(2131624068)).setSearchListener((SearchBox.SearchListener)new SearchBox.SearchListener() {
            @Override
            public void onSearch(final String s) {
                SearchActivity.this.mSearchRunnable = new SearchRunnable(s);
                if (s != null && !s.isEmpty()) {
                    SearchActivity.this.mHandler.post((Runnable)SearchActivity.this.mSearchRunnable);
                }
            }
            
            @Override
            public void onSearchCleared() {
            }
            
            @Override
            public void onSearchClosed() {
                SearchActivity.this.finish();
            }
            
            @Override
            public void onSearchOpened() {
            }
            
            @Override
            public void onSearchTermChanged() {
            }
        });
        this.mSearchBox.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                final int n = (((RelativeLayout)SearchActivity.this.findViewById(2131624056)).getHeight() - SearchActivity.this.mSearchBox.getEditBox().getHeight()) / 2;
                if (n > 0) {
                    final ViewGroup$MarginLayoutParams layoutParams = (ViewGroup$MarginLayoutParams)SearchActivity.this.mSearchBox.getLayoutParams();
                    layoutParams.setMargins(n, n, n, n);
                    SearchActivity.this.mSearchBox.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                }
                final ViewTreeObserver viewTreeObserver = SearchActivity.this.mSearchBox.getEditBox().getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    if (Build$VERSION.SDK_INT < 16) {
                        viewTreeObserver.removeGlobalOnLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                        return;
                    }
                    viewTreeObserver.removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                }
            }
        });
        this.mHandler.removeCallbacks((Runnable)this.mSearchRunnable);
        this.mSearchFragment = (SearchFragment)this.getSupportFragmentManager().findFragmentByTag(SearchFragment.class.getSimpleName());
        if (this.mSearchFragment == null) {
            this.mSearchFragment = SearchFragment.newInstance("", 2131623945, "anime|drama", null);
            this.getSupportFragmentManager().beginTransaction().replace(2131624055, this.mSearchFragment, SearchFragment.class.getSimpleName()).commit();
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        return false;
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (this.mSearchRunnable != null) {
            this.mHandler.removeCallbacks((Runnable)this.mSearchRunnable);
            this.mSearchRunnable = null;
        }
    }
    
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);
        menu.getItem(4).setVisible(false);
        return true;
    }
    
    class SearchRunnable implements Runnable
    {
        String mSearchString;
        
        SearchRunnable(final String mSearchString) {
            this.mSearchString = mSearchString;
        }
        
        @Override
        public void run() {
            if (SearchActivity.this.mSearchFragment != null) {
                SearchActivity.this.mSearchFragment.search(this.mSearchString);
            }
        }
    }
}
