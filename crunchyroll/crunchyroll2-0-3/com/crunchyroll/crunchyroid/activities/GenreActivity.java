// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.activities;

import android.view.Menu;
import java.util.Iterator;
import com.crunchyroll.android.api.models.Categories;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import java.util.HashMap;
import com.crunchyroll.crunchyroid.fragments.FilteredSeriesFragment;
import android.support.v7.widget.Toolbar;
import com.crunchyroll.android.api.Filters;
import com.crunchyroll.android.api.models.Category;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.Fragment;

public final class GenreActivity extends TrackedActivity
{
    private String mFilter;
    private Fragment mFilteredSeriesFragment;
    private String mMediaType;
    private String mTitle;
    
    public static void start(final Context context, final String s, final String s2) {
        final Intent intent = new Intent(context, (Class)GenreActivity.class);
        intent.putExtra("mediaType", s);
        intent.putExtra("filter", s2);
        context.startActivity(intent);
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130903068);
        this.mMediaType = this.getIntent().getStringExtra("mediaType");
        this.mFilter = this.getIntent().getStringExtra("filter");
        if (bundle == null) {
            this.mTitle = "";
            final Categories categories = CrunchyrollApplication.getApp((Context)this).getCategories(this.mMediaType);
            if (categories == null || this.mFilter == null) {
                this.mTitle = LocalizedStrings.POPULAR.get();
            }
            else {
                for (final Category category : categories.getGenres()) {
                    if (Filters.removeTag(this.mFilter).equals(category.getTag())) {
                        this.mTitle = category.getLabel();
                    }
                }
            }
        }
        else {
            this.mTitle = bundle.getString("title");
        }
        final Toolbar supportActionBar = (Toolbar)this.findViewById(2131624056);
        supportActionBar.setTitle(this.mTitle.toUpperCase());
        this.setSupportActionBar(supportActionBar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mFilteredSeriesFragment = this.getSupportFragmentManager().findFragmentByTag(FilteredSeriesFragment.class.getSimpleName());
        if (this.mFilteredSeriesFragment == null) {
            this.mFilteredSeriesFragment = FilteredSeriesFragment.newInstance(this.mMediaType, this.mFilter, false, true);
            this.getSupportFragmentManager().beginTransaction().replace(2131624055, this.mFilteredSeriesFragment, FilteredSeriesFragment.class.getSimpleName()).commit();
        }
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("genre", Filters.removeTag(this.mFilter));
        if ("anime".equalsIgnoreCase(this.mMediaType)) {
            Tracker.swrveScreenView("anime-series-genre", hashMap);
            return;
        }
        Tracker.swrveScreenView("drama-series-genre", hashMap);
    }
    
    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        this.mMediaType = intent.getStringExtra("mediaType");
        this.mFilter = intent.getStringExtra("filter");
        ((Toolbar)this.findViewById(2131624056)).setTitle(Filters.getTitle((Context)this, this.mMediaType, this.mFilter).toUpperCase());
        this.mFilteredSeriesFragment = FilteredSeriesFragment.newInstance(this.mMediaType, this.mFilter, false, true);
        this.getSupportFragmentManager().beginTransaction().replace(2131624055, this.mFilteredSeriesFragment, FilteredSeriesFragment.class.getSimpleName()).commit();
    }
    
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);
        return true;
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("title", this.mTitle);
    }
    
    @Override
    protected void trackView() {
        Tracker.trackView((Context)this, this.mMediaType + "_series_genre_" + Filters.removeTag(this.mFilter));
    }
}
