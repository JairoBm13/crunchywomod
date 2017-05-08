// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.adapters;

import android.widget.ArrayAdapter;
import java.util.Iterator;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.text.style.StyleSpan;
import android.text.style.ForegroundColorSpan;
import android.text.SpannableStringBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.widget.RelativeLayout;
import android.content.res.TypedArray;
import com.crunchyroll.crunchyroid.events.RefreshEvent;
import android.widget.AdapterView;
import java.util.ArrayList;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.crunchyroll.android.api.models.Category;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.widget.PopupWindow;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.android.api.Filters;
import com.crunchyroll.crunchyroid.viewHolders.TitleViewHolder;
import com.crunchyroll.crunchyroid.events.LoadMore;
import de.greenrobot.event.EventBus;
import com.crunchyroll.crunchyroid.activities.SeriesDetailActivity;
import android.content.Context;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.view.View;
import android.view.View$OnClickListener;
import com.crunchyroll.crunchyroid.viewHolders.SeriesCardViewHolder;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.models.ImageSet;
import com.crunchyroll.android.api.models.Series;
import java.util.List;
import java.util.Set;
import com.crunchyroll.crunchyroid.viewHolders.ListItemLoadingViewHolder;
import com.crunchyroll.crunchyroid.fragments.SeriesListFragment;
import com.crunchyroll.crunchyroid.viewHolders.EmptyViewHolder;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;

public class SeriesEntryAdapter extends Adapter<ViewHolder>
{
    private static final int DESCRIPTION_ELLIPSIZE_LENGTH = 150;
    public static final int STATE_LOADING_EMPTY = 3;
    public static final int STATE_LOADING_END = 0;
    public static final int STATE_LOADING_ERROR = 2;
    public static final int STATE_LOADING_START = 1;
    public static final int VIEW_TYPE_EMPTY = 3;
    public static final int VIEW_TYPE_LOADING = 2;
    public static final int VIEW_TYPE_SERIES = 1;
    public static final int VIEW_TYPE_TITLE = 0;
    private Activity mActivity;
    private int mCardType;
    private String mEmptyDescription;
    private String mEmptyTitle;
    private EmptyViewHolder mEmptyViewHolder;
    private String mFilter;
    private SeriesListFragment.Type mFragmentType;
    private final boolean mIsSearchAdapter;
    private final boolean mIsShowTitle;
    private ListItemLoadingViewHolder mListItemLoadingViewHolder;
    private int mLoadingState;
    protected String mMediaType;
    private Set<Long> mQueueSeriesIds;
    private String mSearchString;
    private List<Series> mSeriesItems;
    private final boolean mShouldLoadMoreInSeriesDetailViewPager;
    private int mThumbWidth;
    
    public SeriesEntryAdapter(final boolean mIsSearchAdapter, final Activity mActivity, final String mMediaType, final String mFilter, final Set<Long> mQueueSeriesIds, final List<Series> mSeriesItems, final boolean mShouldLoadMoreInSeriesDetailViewPager, final boolean mIsShowTitle, final SeriesListFragment.Type mFragmentType) {
        this.mCardType = 1;
        this.mSearchString = null;
        this.mLoadingState = 0;
        this.mActivity = mActivity;
        this.mQueueSeriesIds = mQueueSeriesIds;
        this.mThumbWidth = mActivity.getResources().getDimensionPixelSize(2131230734);
        this.mSeriesItems = mSeriesItems;
        this.mFilter = mFilter;
        this.mMediaType = mMediaType;
        this.mIsSearchAdapter = mIsSearchAdapter;
        this.mShouldLoadMoreInSeriesDetailViewPager = mShouldLoadMoreInSeriesDetailViewPager;
        this.mIsShowTitle = mIsShowTitle;
        this.mFragmentType = mFragmentType;
    }
    
    private String getOptimalImageUrlFromImageSet(final ImageSet set, final int n) {
        if (n >= 560 && set.getFullUrl().isPresent()) {
            return set.getFullUrl().get();
        }
        if (n >= 240 && set.getLargeUrl().isPresent()) {
            return set.getLargeUrl().get();
        }
        if (n >= 200 && set.getThumbUrl().isPresent()) {
            return set.getThumbUrl().get();
        }
        if (n >= 140 && set.getMediumUrl().isPresent()) {
            return set.getMediumUrl().get();
        }
        if (n >= 100 && set.getSmallUrl().isPresent()) {
            return set.getSmallUrl().get();
        }
        return set.getSmallUrl().or(set.getMediumUrl()).or(set.getThumbUrl().or(set.getLargeUrl())).orNull();
    }
    
    public Series getItem(final int n) {
        return this.mSeriesItems.get(n);
    }
    
    @Override
    public int getItemCount() {
        final int size = this.mSeriesItems.size();
        int n = 0;
        Label_0044: {
            if (this.mLoadingState != 3 && (size <= 0 || this.mLoadingState != 1)) {
                n = size;
                if (this.mLoadingState != 2) {
                    break Label_0044;
                }
            }
            n = size + 1;
        }
        int n2 = n;
        if (this.mIsShowTitle) {
            n2 = n + 1;
        }
        return n2;
    }
    
    @Override
    public int getItemViewType(int n) {
        final int n2 = 3;
        if (this.showTitle() && n == 0) {
            n = 0;
        }
        else {
            int size = this.mSeriesItems.size();
            if (this.showTitle()) {
                ++size;
            }
            if (n < size) {
                return 1;
            }
            n = n2;
            if (this.mLoadingState != 3) {
                return 2;
            }
        }
        return n;
    }
    
    public void hideLoading() {
        this.mLoadingState = 0;
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int resourceId) {
        if (viewHolder instanceof SeriesCardViewHolder) {
            final SeriesCardViewHolder seriesCardViewHolder = (SeriesCardViewHolder)viewHolder;
            int n = resourceId;
            if (this.mIsShowTitle) {
                n = resourceId - 1;
            }
            final Series series = this.mSeriesItems.get(n);
            seriesCardViewHolder.itemView.setFocusable(true);
            final TypedArray obtainStyledAttributes = this.mActivity.obtainStyledAttributes(new int[] { 2130772199 });
            resourceId = obtainStyledAttributes.getResourceId(0, 0);
            seriesCardViewHolder.itemView.setBackgroundResource(resourceId);
            obtainStyledAttributes.recycle();
            seriesCardViewHolder.itemView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    if (SeriesEntryAdapter.this.mIsSearchAdapter) {
                        if (SeriesEntryAdapter.this.mSearchString != null) {
                            Tracker.searchResultSelection((Context)SeriesEntryAdapter.this.mActivity, SeriesEntryAdapter.this.mSearchString, series);
                        }
                    }
                    else {
                        Tracker.browseSeries((Context)SeriesEntryAdapter.this.mActivity, series.getName(), series.getMediaType().or(""), SeriesEntryAdapter.this.mFilter);
                        if (SeriesEntryAdapter.this.mFragmentType == SeriesListFragment.Type.THIS_SEASON) {
                            Tracker.seriesSelected("main-new", series.getName());
                        }
                    }
                    SeriesDetailActivity.start((Context)SeriesEntryAdapter.this.mActivity, series.getSeriesId(), SeriesEntryAdapter.this.mMediaType, SeriesEntryAdapter.this.mFilter, SeriesEntryAdapter.this.mSeriesItems, SeriesEntryAdapter.this.mShouldLoadMoreInSeriesDetailViewPager, 0, false);
                }
            });
            this.seriesCardOnBind(seriesCardViewHolder, this.mActivity, this.mQueueSeriesIds, series, this.mThumbWidth, this.mSearchString);
        }
        else if (viewHolder instanceof ListItemLoadingViewHolder) {
            if (this.mLoadingState == 1) {
                this.mListItemLoadingViewHolder.setShake(false);
                EventBus.getDefault().post(new LoadMore.SeriesEvent(this.mMediaType, this.mFilter));
            }
        }
        else {
            if (viewHolder instanceof EmptyViewHolder) {
                ((EmptyViewHolder)viewHolder).mEmptyText.setText((CharSequence)this.mEmptyTitle);
                ((EmptyViewHolder)viewHolder).mEmptyDescription.setText((CharSequence)this.mEmptyDescription);
                return;
            }
            if (viewHolder instanceof TitleViewHolder && this.mIsShowTitle) {
                final String title = Filters.getTitle((Context)this.mActivity, this.mMediaType, this.mFilter);
                ((TitleViewHolder)viewHolder).getTitle().setText((CharSequence)title.toUpperCase());
                ((TitleViewHolder)viewHolder).getTitle().setTextColor(this.mActivity.getResources().getColor(2131558417));
                if (this.mFilter != null && Filters.isSeason(this.mFilter)) {
                    ((TitleViewHolder)viewHolder).showDropDownIcon();
                }
                else {
                    ((TitleViewHolder)viewHolder).hideDropDownIcon();
                }
                viewHolder.itemView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        final ArrayList<Category> seasons = CrunchyrollApplication.getApp((Context)SeriesEntryAdapter.this.mActivity).getCategories(SeriesEntryAdapter.this.mMediaType).getSeasons();
                        if (seasons == null || seasons.isEmpty() || !Filters.isSeason(SeriesEntryAdapter.this.mFilter)) {
                            return;
                        }
                        final PopupWindow popupWindow = new PopupWindow((Context)SeriesEntryAdapter.this.mActivity);
                        popupWindow.setWidth(-2);
                        popupWindow.setHeight(-2);
                        popupWindow.setBackgroundDrawable((Drawable)new BitmapDrawable());
                        popupWindow.setOutsideTouchable(true);
                        popupWindow.setFocusable(true);
                        final SeasonAdapter adapter = new SeasonAdapter((Context)SeriesEntryAdapter.this.mActivity, seasons);
                        adapter.PADDING_LEFT = (int)view.getResources().getDimension(2131230769);
                        final LayoutInflater from = LayoutInflater.from((Context)SeriesEntryAdapter.this.mActivity);
                        final View inflate = from.inflate(2130903152, (ViewGroup)null);
                        final ListView listView = (ListView)inflate.findViewById(2131624294);
                        final View inflate2 = from.inflate(2130903145, (ViewGroup)null);
                        final TextView textView = (TextView)inflate2.findViewById(2131624020);
                        textView.setText((CharSequence)title.toUpperCase());
                        textView.setPadding(adapter.PADDING_LEFT, adapter.PADDING_TOP_BOTTOM, 0, adapter.PADDING_TOP_BOTTOM);
                        listView.addHeaderView(inflate2, (Object)null, false);
                        inflate2.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                            public void onClick(final View view) {
                                popupWindow.dismiss();
                            }
                        });
                        listView.setAdapter((ListAdapter)adapter);
                        listView.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
                            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                                if (seasons != null && !seasons.isEmpty()) {
                                    final Category category = seasons.get(n - 1);
                                    Tracker.browseCategory((Context)SeriesEntryAdapter.this.mActivity, SeriesEntryAdapter.this.mMediaType, category.getLabel(), Filters.addTag(category.getTag()));
                                    final RefreshEvent refreshEvent = new RefreshEvent(SeriesEntryAdapter.this.mMediaType, Filters.addTag(category.getTag()));
                                    refreshEvent.setGAViewTrackingTag(SeriesEntryAdapter.this.mMediaType + "_sort_season_" + category.getLabel());
                                    EventBus.getDefault().post(refreshEvent);
                                    popupWindow.dismiss();
                                }
                            }
                        });
                        popupWindow.setContentView(inflate);
                        popupWindow.setAnimationStyle(16973824);
                        popupWindow.showAsDropDown(view, 0, -view.getHeight());
                    }
                });
            }
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        if (n == 1) {
            final View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(2130903146, viewGroup, false);
            final View inflate2 = LayoutInflater.from(viewGroup.getContext()).inflate(2130903082, viewGroup, false);
            final View inflate3 = LayoutInflater.from(viewGroup.getContext()).inflate(2130903081, viewGroup, false);
            final RelativeLayout relativeLayout = new RelativeLayout((Context)this.mActivity);
            relativeLayout.setLayoutParams((ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-2, -2));
            switch (this.mCardType) {
                default: {
                    relativeLayout.addView(inflate3);
                    break;
                }
                case 1: {
                    relativeLayout.addView(inflate);
                    break;
                }
                case 2: {
                    relativeLayout.addView(inflate2);
                    break;
                }
            }
            return new SeriesCardViewHolder(relativeLayout, inflate, inflate2, inflate3);
        }
        if (n == 0) {
            return new TitleViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903145, viewGroup, false));
        }
        if (n == 3) {
            if (this.mEmptyViewHolder == null) {
                this.mEmptyViewHolder = new EmptyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903144, viewGroup, false));
            }
            return this.mEmptyViewHolder;
        }
        if (this.mListItemLoadingViewHolder == null) {
            this.mListItemLoadingViewHolder = new ListItemLoadingViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(2130903154, viewGroup, false));
            this.mListItemLoadingViewHolder.mRetry.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    SeriesEntryAdapter.this.mListItemLoadingViewHolder.setShake(true);
                    EventBus.getDefault().post(new LoadMore.SeriesEvent(SeriesEntryAdapter.this.mMediaType, SeriesEntryAdapter.this.mFilter));
                }
            });
        }
        return this.mListItemLoadingViewHolder;
    }
    
    public void seriesCardOnBind(final SeriesCardViewHolder seriesCardViewHolder, final Activity activity, final Set<Long> set, final Series series, int i, final String s) {
        if (seriesCardViewHolder.getCardSize() != this.mCardType) {
            seriesCardViewHolder.switchView(this.mCardType);
        }
        final boolean imageLoadingEnabled = CrunchyrollApplication.getApp((Context)activity).getApplicationState().isImageLoadingEnabled();
        final String optimalImageUrlFromImageSet = this.getOptimalImageUrlFromImageSet(series.getPortraitImage(), i);
        if (imageLoadingEnabled && optimalImageUrlFromImageSet != null) {
            ImageLoader.getInstance().displayImage(optimalImageUrlFromImageSet, seriesCardViewHolder.image, CrunchyrollApplication.getDisplayImageOptionsPlaceholderPortrait());
        }
        else {
            seriesCardViewHolder.image.setImageResource(2130837818);
        }
        String name;
        if (series != null) {
            name = series.getName();
        }
        else {
            name = "";
        }
        final ArrayList<Integer> list = new ArrayList<Integer>();
        if (s != null) {
            for (i = name.toLowerCase().indexOf(s.toLowerCase(), 0); i >= 0; i = name.toLowerCase().indexOf(s.toLowerCase(), i + s.length())) {
                list.add(i);
            }
        }
        if (!list.isEmpty()) {
            final SpannableStringBuilder text = new SpannableStringBuilder((CharSequence)name.toUpperCase());
            final Iterator<Integer> iterator = list.iterator();
            while (iterator.hasNext()) {
                i = iterator.next();
                text.setSpan((Object)new ForegroundColorSpan(activity.getResources().getColor(2131558477)), i, s.length() + i, 17);
                text.setSpan((Object)new StyleSpan(1), i, s.length() + i, 17);
            }
            seriesCardViewHolder.title.setText((CharSequence)text);
        }
        else {
            seriesCardViewHolder.title.setText((CharSequence)name.toUpperCase());
        }
        final Optional<Integer> mediaCount = series.getMediaCount();
        if (mediaCount.isPresent()) {
            seriesCardViewHolder.subtitle.setText((CharSequence)LocalizedStrings.VIDEOS_COUNT.get(mediaCount.get()));
        }
        else {
            seriesCardViewHolder.subtitle.setText((CharSequence)"");
        }
        if (seriesCardViewHolder.seriesDescription != null && series.getDescription() != null) {
            if (series.getDescription().length() <= 150) {
                seriesCardViewHolder.seriesDescription.setText((CharSequence)series.getDescription());
                seriesCardViewHolder.more.setVisibility(8);
                return;
            }
            seriesCardViewHolder.seriesDescription.setText((CharSequence)(series.getDescription().substring(0, 147) + "..."));
            seriesCardViewHolder.more.setText((CharSequence)LocalizedStrings.MORE.get());
            seriesCardViewHolder.more.setCompoundDrawablesWithIntrinsicBounds(0, 0, 2130837710, 0);
            seriesCardViewHolder.more.setVisibility(0);
            seriesCardViewHolder.more.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    if (seriesCardViewHolder.seriesDescription.getText().equals(series.getDescription())) {
                        seriesCardViewHolder.seriesDescription.setText((CharSequence)(series.getDescription().substring(0, 147) + "..."));
                        seriesCardViewHolder.more.setText((CharSequence)LocalizedStrings.MORE.get());
                        seriesCardViewHolder.more.setCompoundDrawablesWithIntrinsicBounds(0, 0, 2130837710, 0);
                        return;
                    }
                    seriesCardViewHolder.seriesDescription.setText((CharSequence)series.getDescription());
                    seriesCardViewHolder.more.setText((CharSequence)LocalizedStrings.LESS.get());
                    seriesCardViewHolder.more.setCompoundDrawablesWithIntrinsicBounds(0, 0, 2130837715, 0);
                }
            });
        }
    }
    
    public void setCardType(final int mCardType) {
        this.mCardType = mCardType;
    }
    
    public void setEmptyHints(final String mEmptyTitle, final String mEmptyDescription) {
        this.mEmptyTitle = mEmptyTitle;
        this.mEmptyDescription = mEmptyDescription;
    }
    
    public void setEmptyState() {
        this.mLoadingState = 3;
    }
    
    public void setFilter(final String mFilter) {
        this.mFilter = mFilter;
    }
    
    public void setLoadingStart(final int layoutGravity) {
        this.mLoadingState = 1;
        if (this.mListItemLoadingViewHolder != null) {
            this.mListItemLoadingViewHolder.setLayoutGravity(layoutGravity);
            this.mListItemLoadingViewHolder.showProgress();
        }
    }
    
    public void setMediaType(final String mMediaType) {
        this.mMediaType = mMediaType;
    }
    
    public void setSearchString(final String mSearchString) {
        this.mSearchString = mSearchString;
    }
    
    public void setThumbWidth(final int mThumbWidth) {
        this.mThumbWidth = mThumbWidth;
    }
    
    public void showLoadingError(final String s, final int layoutGravity) {
        this.mLoadingState = 2;
        if (this.mListItemLoadingViewHolder != null) {
            this.mListItemLoadingViewHolder.setLayoutGravity(layoutGravity);
            this.mListItemLoadingViewHolder.showError(s);
        }
    }
    
    public boolean showTitle() {
        return this.mIsShowTitle;
    }
    
    public class SeasonAdapter extends ArrayAdapter<Category>
    {
        public int PADDING_LEFT;
        public int PADDING_TOP_BOTTOM;
        
        public SeasonAdapter(final Context context, final List<Category> list) {
            super(context, 2130903145, (List)list);
            this.PADDING_LEFT = 25;
            this.PADDING_TOP_BOTTOM = 20;
        }
        
        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            Object o = view;
            if (view == null) {
                o = new TextView((Context)SeriesEntryAdapter.this.mActivity);
                ((View)o).setPadding(this.PADDING_LEFT, this.PADDING_TOP_BOTTOM, 0, this.PADDING_TOP_BOTTOM);
                ((TextView)o).setTextSize(16.0f);
                ((TextView)o).setTextColor(SeriesEntryAdapter.this.mActivity.getResources().getColor(2131558417));
            }
            final String label = CrunchyrollApplication.getApp((Context)SeriesEntryAdapter.this.mActivity).getCategories(SeriesEntryAdapter.this.mMediaType).getSeasons().get(n).getLabel();
            ((TextView)o).setText((CharSequence)label);
            if (label.equalsIgnoreCase(Filters.getTitle((Context)SeriesEntryAdapter.this.mActivity, SeriesEntryAdapter.this.mMediaType, SeriesEntryAdapter.this.mFilter))) {
                ((View)o).setBackgroundColor(SeriesEntryAdapter.this.mActivity.getResources().getColor(2131558422));
                return (View)o;
            }
            ((View)o).setBackgroundColor(SeriesEntryAdapter.this.mActivity.getResources().getColor(2131558536));
            return (View)o;
        }
    }
}
