// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.activities;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.crunchyroll.android.api.models.Categories;
import android.annotation.TargetApi;
import android.view.View$OnLayoutChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.view.ViewGroup$LayoutParams;
import android.widget.AbsListView$LayoutParams;
import android.widget.ListAdapter;
import com.crunchyroll.android.api.Filters;
import de.greenrobot.event.EventBus;
import com.crunchyroll.crunchyroid.events.RefreshEvent;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.widget.TextView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View$OnClickListener;
import android.os.Bundle;
import android.content.Intent;
import android.app.Activity;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.content.Context;
import android.view.View;
import com.crunchyroll.crunchyroid.util.AnimationUtil;
import java.util.ArrayList;
import android.widget.ListView;
import com.crunchyroll.android.api.models.Category;
import java.util.List;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;

public class SortFilterMenuActivity extends TrackedActivity
{
    public static String FILTER_TYPE;
    public static String MEDIA_TYPE;
    private CategoryAdapter categoryAdapter;
    private String filterType;
    private ImageView mAtoz;
    private FloatingActionButton mClose;
    private ImageView mGenre;
    private List<Category> mGenreItems;
    private ListView mGenreMenu;
    private ImageView mPopular;
    private ArrayList<Category> mSeasonItems;
    private ImageView mSeasons;
    private SortType mSortType;
    private String mediaType;
    
    static {
        SortFilterMenuActivity.MEDIA_TYPE = "media_type";
        SortFilterMenuActivity.FILTER_TYPE = "filter_type";
    }
    
    public SortFilterMenuActivity() {
        this.mSortType = SortType.SORT_TYPE_POPULAR;
    }
    
    private void selectSortType(final SortType mSortType) {
        this.mSortType = mSortType;
        this.mPopular.setSelected(false);
        this.mSeasons.setSelected(false);
        this.mAtoz.setSelected(false);
        this.mGenre.setSelected(false);
        switch (this.mSortType) {
            default: {}
            case SORT_TYPE_POPULAR: {
                this.mPopular.setSelected(true);
            }
            case SORT_TYPE_SEASONS: {
                this.mSeasons.setSelected(true);
            }
            case SORT_TYPE_ATOZ: {
                this.mAtoz.setSelected(true);
            }
            case SORT_TYPE_GENRE: {
                this.mGenre.setSelected(true);
            }
        }
    }
    
    private void showGenreMenu() {
        final int[] array = new int[2];
        final int[] array2 = new int[2];
        this.mGenreMenu.getLocationOnScreen(array2);
        this.mGenre.getLocationOnScreen(array);
        AnimationUtil.revealViewWithCircularAnimation((View)this.mGenreMenu, array[0] - array2[0] + this.mGenre.getWidth() / 2, array[1] - array2[1] + this.mGenre.getHeight() / 2);
        AnimationUtil.revealListViewItems((Context)this, this.mGenreMenu);
        AnimationUtil.animateImageTransitionWithRotation((Context)this, this.mClose, 2130837714);
        if (this.mediaType.equalsIgnoreCase("anime")) {
            Tracker.swrveScreenView("anime-sort-menu-genres");
            return;
        }
        Tracker.swrveScreenView("drama-sort-menu-genres");
    }
    
    private void showSortMenu() {
        AnimationUtil.animateImageTransitionWithRotation((Context)this, this.mClose, 2130837743);
        final int[] array = new int[2];
        final int[] array2 = new int[2];
        if (this.mGenreMenu.getVisibility() == 0) {
            this.mGenreMenu.getLocationOnScreen(array2);
            this.mGenre.getLocationOnScreen(array);
            AnimationUtil.hideViewWithCircularAnimation((View)this.mGenreMenu, array[0] - array2[0] + this.mGenre.getWidth() / 2, array[1] - array2[1] + this.mGenre.getHeight() / 2);
        }
        if (this.mediaType.equalsIgnoreCase("anime")) {
            Tracker.swrveScreenView("anime-sort-menu");
            return;
        }
        Tracker.swrveScreenView("drama-sort-menu");
    }
    
    public static void start(final Activity activity, final String s, final String s2) {
        final Intent intent = new Intent((Context)activity, (Class)SortFilterMenuActivity.class);
        intent.addFlags(65536);
        intent.putExtra(SortFilterMenuActivity.MEDIA_TYPE, s);
        intent.putExtra(SortFilterMenuActivity.FILTER_TYPE, s2);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }
    
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, 0);
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130903076);
        (this.mClose = (FloatingActionButton)this.findViewById(2131624081)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(View viewById) {
                if (SortFilterMenuActivity.this.mGenreMenu.getVisibility() == 0) {
                    SortFilterMenuActivity.this.showSortMenu();
                    return;
                }
                viewById = SortFilterMenuActivity.this.findViewById(2131624070);
                final int[] array = new int[2];
                final int[] array2 = new int[2];
                viewById.getLocationOnScreen(array);
                SortFilterMenuActivity.this.mClose.getLocationOnScreen(array2);
                AnimationUtil.hideViewWithCircularAnimation(viewById, array2[0] - array[0] + SortFilterMenuActivity.this.mClose.getWidth() / 2, array2[1] - array[1] + SortFilterMenuActivity.this.mClose.getHeight() / 2, new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        SortFilterMenuActivity.this.finish();
                    }
                });
            }
        });
        this.mPopular = (ImageView)this.findViewById(2131624073);
        this.mSeasons = (ImageView)this.findViewById(2131624075);
        this.mAtoz = (ImageView)this.findViewById(2131624077);
        this.mGenre = (ImageView)this.findViewById(2131624079);
        ((TextView)this.findViewById(2131624072)).setText((CharSequence)LocalizedStrings.POPULAR.get());
        ((TextView)this.findViewById(2131624074)).setText((CharSequence)LocalizedStrings.SEASONS.get());
        ((TextView)this.findViewById(2131624076)).setText((CharSequence)LocalizedStrings.A_TO_Z.get());
        ((TextView)this.findViewById(2131624078)).setText((CharSequence)LocalizedStrings.GENRE.get());
        this.mGenreMenu = (ListView)this.findViewById(2131624080);
        final Intent intent = this.getIntent();
        this.mediaType = intent.getStringExtra(SortFilterMenuActivity.MEDIA_TYPE);
        this.filterType = intent.getStringExtra(SortFilterMenuActivity.FILTER_TYPE);
        final Categories categories = CrunchyrollApplication.getApp((Context)this).getCategories(this.mediaType);
        if ("popular".equals(this.filterType)) {
            this.mSortType = SortType.SORT_TYPE_POPULAR;
        }
        else if ("alpha".equals(this.filterType)) {
            this.mSortType = SortType.SORT_TYPE_ATOZ;
        }
        else if (this.filterType.contains("season")) {
            this.mSortType = SortType.SORT_TYPE_SEASONS;
        }
        else if (this.filterType.startsWith("tag:")) {
            this.mSortType = SortType.SORT_TYPE_GENRE;
        }
        this.selectSortType(this.mSortType);
        this.mPopular.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                SortFilterMenuActivity.this.selectSortType(SortType.SORT_TYPE_POPULAR);
                Tracker.browseCategory((Context)SortFilterMenuActivity.this, SortFilterMenuActivity.this.mediaType, "popular", "popular");
                if (SortFilterMenuActivity.this.mediaType.equals("drama")) {
                    final RefreshEvent refreshEvent = new RefreshEvent(SortFilterMenuActivity.this.mediaType, "popular");
                    refreshEvent.setGAViewTrackingTag("drama_sort_popular");
                    EventBus.getDefault().post(refreshEvent);
                }
                else if (SortFilterMenuActivity.this.mediaType.equals("anime")) {
                    final RefreshEvent refreshEvent2 = new RefreshEvent(SortFilterMenuActivity.this.mediaType, "popular");
                    refreshEvent2.setGAViewTrackingTag("anime_sort_popular");
                    EventBus.getDefault().post(refreshEvent2);
                }
                SortFilterMenuActivity.this.finish();
            }
        });
        this.mSeasons.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                SortFilterMenuActivity.this.selectSortType(SortType.SORT_TYPE_SEASONS);
                if (SortFilterMenuActivity.this.mSeasonItems != null && !SortFilterMenuActivity.this.mSeasonItems.isEmpty()) {
                    final Category category = SortFilterMenuActivity.this.mSeasonItems.get(0);
                    final RefreshEvent refreshEvent = new RefreshEvent(SortFilterMenuActivity.this.mediaType, Filters.addTag(category.getTag()), SortFilterMenuActivity.this.mSeasonItems);
                    refreshEvent.setGAViewTrackingTag(SortFilterMenuActivity.this.mediaType + "_sort_season_" + category.getLabel());
                    Tracker.browseCategory((Context)SortFilterMenuActivity.this, SortFilterMenuActivity.this.mediaType, "seasons", Filters.addTag(category.getTag()));
                    EventBus.getDefault().post(refreshEvent);
                    SortFilterMenuActivity.this.finish();
                }
            }
        });
        this.mAtoz.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                SortFilterMenuActivity.this.selectSortType(SortType.SORT_TYPE_ATOZ);
                Tracker.browseCategory((Context)SortFilterMenuActivity.this, SortFilterMenuActivity.this.mediaType, "a to z", "alpha");
                final RefreshEvent refreshEvent = new RefreshEvent(SortFilterMenuActivity.this.mediaType, "alpha");
                refreshEvent.setGAViewTrackingTag(SortFilterMenuActivity.this.mediaType + "_sort_a_to_z");
                EventBus.getDefault().post(refreshEvent);
                SortFilterMenuActivity.this.finish();
            }
        });
        this.mGenre.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.trackView((Context)SortFilterMenuActivity.this, Tracker.Screen.SORT_MENU_GENRES);
                SortFilterMenuActivity.this.selectSortType(SortType.SORT_TYPE_GENRE);
                Tracker.browseCategory((Context)SortFilterMenuActivity.this, SortFilterMenuActivity.this.mediaType, "genre", null);
                SortFilterMenuActivity.this.showGenreMenu();
            }
        });
        this.mGenreItems = categories.getGenres();
        this.mSeasonItems = categories.getSeasons();
        if (this.categoryAdapter == null) {
            this.categoryAdapter = new CategoryAdapter((Context)this, this.mGenreItems);
        }
        this.mGenreMenu.setAdapter((ListAdapter)this.categoryAdapter);
        final View view = new View((Context)this);
        view.setLayoutParams((ViewGroup$LayoutParams)new AbsListView$LayoutParams(-1, Math.round(this.getResources().getDimension(2131230888))));
        this.mGenreMenu.addFooterView(view);
        this.mGenreMenu.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                final Category category = (Category)adapterView.getItemAtPosition(n);
                if (category != null) {
                    Tracker.browseCategory((Context)SortFilterMenuActivity.this, SortFilterMenuActivity.this.mediaType, category.getLabel(), Filters.addTag(category.getTag()));
                    final RefreshEvent refreshEvent = new RefreshEvent(SortFilterMenuActivity.this.mediaType, Filters.addTag(category.getTag()));
                    refreshEvent.setGAViewTrackingTag(SortFilterMenuActivity.this.mediaType + "_sort_genre_" + category.getTag());
                    EventBus.getDefault().post(refreshEvent);
                    SortFilterMenuActivity.this.finish();
                }
            }
        });
        final View viewById = this.findViewById(2131624070);
        viewById.addOnLayoutChangeListener((View$OnLayoutChangeListener)new View$OnLayoutChangeListener() {
            @TargetApi(21)
            public void onLayoutChange(final View view, int n, int n2, int n3, int n4, int n5, int n6, final int n7, final int n8) {
                view.removeOnLayoutChangeListener((View$OnLayoutChangeListener)this);
                final int[] array = new int[2];
                final int[] array2 = new int[2];
                viewById.getLocationOnScreen(array);
                SortFilterMenuActivity.this.mClose.getLocationOnScreen(array2);
                n = array2[0];
                n2 = array[0];
                n3 = SortFilterMenuActivity.this.mClose.getWidth() / 2;
                n4 = array2[1];
                n5 = array[1];
                n6 = SortFilterMenuActivity.this.mClose.getHeight() / 2;
                AnimationUtil.revealViewWithCircularAnimation(viewById, n - n2 + n3, n4 - n5 + n6);
            }
        });
        if (this.mediaType.equalsIgnoreCase("anime")) {
            Tracker.swrveScreenView("anime-sort-menu");
            return;
        }
        Tracker.swrveScreenView("drama-sort-menu");
    }
    
    private final class CategoryAdapter extends ArrayAdapter<Category>
    {
        public CategoryAdapter(final Context context, final List<Category> list) {
            super(context, 0, (List)list);
        }
        
        public View getView(final int n, View inflate, final ViewGroup viewGroup) {
            if (inflate == null) {
                inflate = ((LayoutInflater)SortFilterMenuActivity.this.getSystemService("layout_inflater")).inflate(2130903147, (ViewGroup)null);
                ((TextView)inflate.findViewById(2131624285)).setTextColor(SortFilterMenuActivity.this.getResources().getColor(2131558537));
            }
            final Category category = (Category)this.getItem(n);
            ((TextView)inflate.findViewById(2131624285)).setText((CharSequence)category.getLabel());
            if (SortFilterMenuActivity.this.filterType.equals(Filters.addTag(category.getTag()))) {
                inflate.setBackgroundColor(SortFilterMenuActivity.this.getResources().getColor(2131558478));
                return inflate;
            }
            inflate.setBackgroundColor(SortFilterMenuActivity.this.getResources().getColor(2131558477));
            return inflate;
        }
    }
    
    public enum SortType
    {
        SORT_TYPE_ATOZ, 
        SORT_TYPE_GENRE, 
        SORT_TYPE_POPULAR, 
        SORT_TYPE_SEASONS;
    }
}
