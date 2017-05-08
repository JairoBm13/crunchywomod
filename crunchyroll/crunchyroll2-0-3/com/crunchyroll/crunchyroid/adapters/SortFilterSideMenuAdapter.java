// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.adapters;

import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.ArrayList;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import de.greenrobot.event.EventBus;
import com.crunchyroll.crunchyroid.events.RefreshEvent;
import android.view.View;
import android.view.View$OnClickListener;
import com.crunchyroll.android.api.models.Category;
import com.crunchyroll.android.api.Filters;
import com.crunchyroll.android.api.models.Categories;
import android.support.v7.widget.RecyclerView;

public class SortFilterSideMenuAdapter extends Adapter<ViewHolder>
{
    public static final int NUM_HEADERS = 5;
    public static final int VIEW_TYPE_A_TO_Z = 3;
    public static final int VIEW_TYPE_GENRE_ITEM = 6;
    public static final int VIEW_TYPE_HEADER_GENRE = 5;
    public static final int VIEW_TYPE_HEADER_SORT_BY = 1;
    public static final int VIEW_TYPE_POPULAR = 2;
    public static final int VIEW_TYPE_SEASONS = 4;
    private final Categories mCategories;
    private final String mMediaType;
    private int mSelectedPosition;
    
    public SortFilterSideMenuAdapter(final String mMediaType, final Categories mCategories, final String s) {
        this.mMediaType = mMediaType;
        this.mCategories = mCategories;
        this.mSelectedPosition = -1;
        if ("popular".equals(s)) {
            this.mSelectedPosition = 1;
        }
        else {
            if ("alpha".equals(s)) {
                this.mSelectedPosition = 2;
                return;
            }
            if (Filters.removeTag(s).contains("season")) {
                this.mSelectedPosition = 3;
                return;
            }
            for (int i = 0; i < this.mCategories.getGenres().size(); ++i) {
                if (this.mCategories.getGenres().get(i).getTag().equals(Filters.removeTag(s))) {
                    this.mSelectedPosition = i + 5;
                }
            }
        }
    }
    
    @Override
    public int getItemCount() {
        return this.mCategories.getGenres().size() + 5;
    }
    
    @Override
    public int getItemViewType(final int n) {
        switch (n) {
            default: {
                return 6;
            }
            case 0: {
                return 1;
            }
            case 1: {
                return 2;
            }
            case 2: {
                return 3;
            }
            case 3: {
                return 4;
            }
            case 4: {
                return 5;
            }
        }
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
        final SortFilterItemViewHolder sortFilterItemViewHolder = (SortFilterItemViewHolder)viewHolder;
        if (!sortFilterItemViewHolder.itemView.getContext().getResources().getString(2131165434).equals(sortFilterItemViewHolder.itemView.getTag())) {
            if (this.mSelectedPosition == n) {
                sortFilterItemViewHolder.itemView.setBackgroundResource(2131558477);
                sortFilterItemViewHolder.sortFilterItem.setTextColor(sortFilterItemViewHolder.sortFilterItem.getContext().getResources().getColor(2131558537));
            }
            else {
                sortFilterItemViewHolder.itemView.setBackgroundResource(2131558536);
                sortFilterItemViewHolder.sortFilterItem.setTextColor(sortFilterItemViewHolder.sortFilterItem.getContext().getResources().getColor(2131558423));
            }
        }
        switch (this.getItemViewType(n)) {
            default: {
                final Category category = this.mCategories.getGenres().get(n - 5);
                sortFilterItemViewHolder.sortFilterItem.setText((CharSequence)category.getLabel());
                sortFilterItemViewHolder.sortFilterItem.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        SortFilterSideMenuAdapter.this.mSelectedPosition = n;
                        ((RecyclerView.Adapter)SortFilterSideMenuAdapter.this).notifyDataSetChanged();
                        final RefreshEvent refreshEvent = new RefreshEvent(SortFilterSideMenuAdapter.this.mMediaType, Filters.addTag(category.getTag()));
                        refreshEvent.setGAViewTrackingTag(SortFilterSideMenuAdapter.this.mMediaType + "_sort_genre_" + category.getTag());
                        EventBus.getDefault().post(refreshEvent);
                    }
                });
            }
            case 1: {
                sortFilterItemViewHolder.sortFilterItem.setText((CharSequence)LocalizedStrings.SORT_BY.get());
            }
            case 2: {
                sortFilterItemViewHolder.sortFilterItem.setText((CharSequence)LocalizedStrings.POPULAR.get());
                sortFilterItemViewHolder.sortFilterItem.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        SortFilterSideMenuAdapter.this.mSelectedPosition = n;
                        ((RecyclerView.Adapter)SortFilterSideMenuAdapter.this).notifyDataSetChanged();
                        if (SortFilterSideMenuAdapter.this.mMediaType.equals("drama") || SortFilterSideMenuAdapter.this.mMediaType.equals("anime")) {
                            final RefreshEvent refreshEvent = new RefreshEvent(SortFilterSideMenuAdapter.this.mMediaType, "popular");
                            refreshEvent.setGAViewTrackingTag(SortFilterSideMenuAdapter.this.mMediaType + "_sort_popular");
                            EventBus.getDefault().post(refreshEvent);
                        }
                    }
                });
            }
            case 3: {
                sortFilterItemViewHolder.sortFilterItem.setText((CharSequence)LocalizedStrings.A_TO_Z.get());
                sortFilterItemViewHolder.sortFilterItem.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        SortFilterSideMenuAdapter.this.mSelectedPosition = n;
                        ((RecyclerView.Adapter)SortFilterSideMenuAdapter.this).notifyDataSetChanged();
                        final RefreshEvent refreshEvent = new RefreshEvent(SortFilterSideMenuAdapter.this.mMediaType, "alpha");
                        refreshEvent.setGAViewTrackingTag(SortFilterSideMenuAdapter.this.mMediaType + "_sort_a_to_z");
                        EventBus.getDefault().post(refreshEvent);
                    }
                });
            }
            case 4: {
                sortFilterItemViewHolder.sortFilterItem.setText((CharSequence)LocalizedStrings.SEASONS.get());
                sortFilterItemViewHolder.sortFilterItem.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        SortFilterSideMenuAdapter.this.mSelectedPosition = n;
                        ((RecyclerView.Adapter)SortFilterSideMenuAdapter.this).notifyDataSetChanged();
                        final ArrayList<Category> seasons = SortFilterSideMenuAdapter.this.mCategories.getSeasons();
                        if (seasons != null && !seasons.isEmpty()) {
                            final Category category = seasons.get(0);
                            final RefreshEvent refreshEvent = new RefreshEvent(SortFilterSideMenuAdapter.this.mMediaType, Filters.addTag(category.getTag()), seasons);
                            refreshEvent.setGAViewTrackingTag(SortFilterSideMenuAdapter.this.mMediaType + "_sort_season_" + category.getLabel());
                            EventBus.getDefault().post(refreshEvent);
                        }
                    }
                });
            }
            case 5: {
                sortFilterItemViewHolder.sortFilterItem.setText((CharSequence)LocalizedStrings.GENRE.get());
            }
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        switch (n) {
            default: {
                return new SortFilterItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903149, viewGroup, false));
            }
            case 1:
            case 5: {
                return new SortFilterItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903148, viewGroup, false));
            }
        }
    }
    
    public static class SortFilterItemViewHolder extends ViewHolder
    {
        public final TextView sortFilterItem;
        
        public SortFilterItemViewHolder(final View view) {
            super(view);
            this.sortFilterItem = (TextView)view.findViewById(2131624286);
        }
    }
}
