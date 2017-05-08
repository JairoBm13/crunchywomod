// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.activities;

import java.util.ArrayList;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.Collection;
import android.support.design.widget.Snackbar;
import com.crunchyroll.android.api.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.requests.ListSeriesRequest;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import com.crunchyroll.android.util.SafeAsyncTask;
import android.view.Menu;
import com.crunchyroll.crunchyroid.events.ShareEvent;
import android.view.MenuItem;
import com.crunchyroll.crunchyroid.events.Upsell;
import com.crunchyroll.crunchyroid.events.ShowTopBarEvent;
import com.crunchyroll.crunchyroid.events.HideTopBarEvent;
import com.crunchyroll.crunchyroid.events.ClickEvent;
import com.google.android.gms.appindexing.Action;
import com.crunchyroll.crunchyroid.events.AppIndexEvent;
import android.annotation.TargetApi;
import android.view.Window;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.appindexing.AppIndex;
import android.os.Build$VERSION;
import android.os.Bundle;
import com.crunchyroll.crunchyroid.events.ErrorEvent;
import de.greenrobot.event.EventBus;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import com.crunchyroll.crunchyroid.app.PrepareToWatch;
import com.crunchyroll.android.api.tasks.BaseListener;
import android.app.Activity;
import com.crunchyroll.android.api.models.Media;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import java.util.HashMap;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import java.io.Serializable;
import com.crunchyroll.crunchyroid.app.Extras;
import android.content.Intent;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.app.Fragment;
import android.animation.Animator;
import android.animation.Animator$AnimatorListener;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import com.crunchyroll.android.api.models.Series;
import java.util.List;
import com.crunchyroll.crunchyroid.fragments.SeriesDetailFragment;
import android.content.BroadcastReceiver;
import android.view.View;
import com.google.android.gms.common.api.GoogleApiClient;
import android.net.Uri;

public class SeriesDetailActivity extends TrackedActivity
{
    public static final String APP_URI_BASE = "crunchyroll://series/";
    private static final String SERIES_DETAIL_TAG = "series_detail";
    private int appEntryFrom;
    private boolean hasIndexed;
    private SeriesPagerAdapter mAdapter;
    private Uri mAppUri;
    private GoogleApiClient mClient;
    private int mCurrentIndex;
    private String mFilter;
    private View mFragmentContainer;
    private boolean mHasClickedMedia;
    private boolean mIsPaused;
    private boolean mIsPrepareToWatchOnStart;
    private boolean mIsRefreshForLoginLogout;
    private boolean mIsShowMediaInfoOnStart;
    private boolean mIsTablet;
    private BroadcastReceiver mLoginLogoutReceiver;
    private long mMediaInfoOnStartId;
    private String mMediaType;
    private int mOffset;
    private TrackingPageChangeListener mPageListener;
    private SeriesDetailFragment mSeriesDetailFragment;
    private long mSeriesId;
    private List<Series> mSeriesList;
    private ViewPager mSeriesPager;
    private boolean mShouldLoadMoreSeries;
    private String mTitle;
    private Toolbar mToolbar;
    private Uri mWebUri;
    
    public SeriesDetailActivity() {
        this.mCurrentIndex = -1;
        this.mPageListener = new TrackingPageChangeListener();
    }
    
    public static void fadeIn(final View view) {
        view.clearAnimation();
        if (view.getAlpha() < 1.0f) {
            view.setVisibility(0);
            view.animate().alphaBy(1.0f - view.getAlpha()).setDuration(200L).setListener((Animator$AnimatorListener)new Animator$AnimatorListener() {
                public void onAnimationCancel(final Animator animator) {
                }
                
                public void onAnimationEnd(final Animator animator) {
                }
                
                public void onAnimationRepeat(final Animator animator) {
                }
                
                public void onAnimationStart(final Animator animator) {
                }
            }).start();
        }
    }
    
    public static void fadeOut(final View view) {
        view.clearAnimation();
        if (view.getAlpha() > 0.0f) {
            view.animate().alphaBy(-view.getAlpha()).setDuration(200L).setListener((Animator$AnimatorListener)new Animator$AnimatorListener() {
                public void onAnimationCancel(final Animator animator) {
                }
                
                public void onAnimationEnd(final Animator animator) {
                    view.setVisibility(8);
                }
                
                public void onAnimationRepeat(final Animator animator) {
                }
                
                public void onAnimationStart(final Animator animator) {
                }
            }).start();
        }
    }
    
    private void loadData(final boolean b, final long n, final boolean b2) {
        if (this.mSeriesList == null) {
            this.mSeriesPager.setVisibility(8);
            this.mSeriesDetailFragment = (SeriesDetailFragment)this.getSupportFragmentManager().findFragmentByTag("series_detail");
            if (this.mSeriesDetailFragment == null) {
                this.mSeriesDetailFragment = SeriesDetailFragment.newInstance(-1, this.mSeriesId, b, true, n, b2, true);
                this.getSupportFragmentManager().beginTransaction().replace(2131624055, this.mSeriesDetailFragment, "series_detail").commitAllowingStateLoss();
            }
            return;
        }
        this.mFragmentContainer.setVisibility(8);
        (this.mAdapter = new SeriesPagerAdapter(this.getSupportFragmentManager())).add(this.mSeriesList);
        this.mSeriesPager.setAdapter(this.mAdapter);
        if (this.mCurrentIndex == -1) {
            this.mSeriesPager.setCurrentItem(this.mAdapter.getIndexForSeriesId(this.mSeriesId));
            return;
        }
        this.mSeriesPager.setCurrentItem(this.mCurrentIndex);
    }
    
    public static void start(final Context context, final Long n, final int n2, final boolean b) {
        final Intent intent = new Intent(context, (Class)SeriesDetailActivity.class);
        Extras.putLong(intent, "seriesId", n);
        Extras.putInt(intent, "intent_from", Integer.valueOf(n2));
        Extras.putBoolean(intent, "isPrepareToWatchOnStart", b);
        context.startActivity(intent);
    }
    
    public static void start(final Context context, final Long n, final String s, final String s2, final List<Series> list, final boolean b, final int n2, final boolean b2) {
        final Intent intent = new Intent(context, (Class)SeriesDetailActivity.class);
        Extras.putLong(intent, "seriesId", n);
        Extras.putInt(intent, "intent_from", Integer.valueOf(n2));
        Extras.putBoolean(intent, "isPrepareToWatchOnStart", b2);
        Extras.putString(intent, "mediaType", s);
        Extras.putString(intent, "filter", s2);
        Extras.putList(intent, "series", (List<Serializable>)list);
        Extras.putBoolean(intent, "shouldLoadMore", b);
        context.startActivity(intent);
    }
    
    public static void startMediaInfo(final Context context, final Long n, final int n2, final long n3) {
        final Intent intent = new Intent(context, (Class)SeriesDetailActivity.class);
        Extras.putLong(intent, "seriesId", n);
        Extras.putInt(intent, "intent_from", Integer.valueOf(n2));
        Extras.putLong(intent, "mediaId", Long.valueOf(n3));
        Extras.putBoolean(intent, "isShowMediaInfoOnStart", true);
        context.startActivity(intent);
    }
    
    private void trackPosition(final int n) {
        final Series series = this.mSeriesList.get(n);
        Tracker.trackView((Context)this, "SeriesInfo_" + series.getName());
        if (series.getMediaType().or("").equalsIgnoreCase("anime")) {
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("series", series.getName());
            Tracker.swrveScreenView("anime-series", hashMap);
        }
        else if (series.getMediaType().or("").equalsIgnoreCase("drama")) {
            final HashMap<String, String> hashMap2 = new HashMap<String, String>();
            hashMap2.put("series", series.getName());
            Tracker.swrveScreenView("drama-series", hashMap2);
        }
    }
    
    public void finish() {
        switch (this.appEntryFrom) {
            default: {
                super.finish();
                CrunchyrollApplication.getApp((Context)this).invalidatePrepareToWatch();
            }
            case 1: {
                final Intent intent = new Intent((Context)this, (Class)MainActivity.class);
                intent.setFlags(335544320);
                super.finish();
                CrunchyrollApplication.getApp((Context)this).invalidatePrepareToWatch();
                this.startActivity(intent);
            }
            case 2: {
                if (this.mHasClickedMedia) {
                    final Intent intent2 = new Intent((Context)this, (Class)MainActivity.class);
                    intent2.setFlags(335544320);
                    super.finish();
                    CrunchyrollApplication.getApp((Context)this).invalidatePrepareToWatch();
                    this.startActivity(intent2);
                    return;
                }
                super.finish();
                CrunchyrollApplication.getApp((Context)this).invalidatePrepareToWatch();
            }
        }
    }
    
    protected void loadItemsWithOffset(final Integer n) {
        new LoadMoreSeriesTask(this.mMediaType, this.mFilter, n, 50).execute();
    }
    
    @Override
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        super.onActivityResult(n, n2, intent);
        if (n2 == 22) {
            Util.showErrorPopup((Context)this, LocalizedStrings.ERROR_NOT_AVAILABLE.get());
        }
        if (n2 == 23) {
            final Media media = (Media)intent.getExtras().getSerializable("media");
            if (!CrunchyrollApplication.getApp((Context)this).isPrepareToWatchLoading() && media != null) {
                final PrepareToWatch prepareToWatch = CrunchyrollApplication.getApp((Context)this).prepareToWatch(this, media, false, 0);
                prepareToWatch.prepare(new BaseListener<Void>() {
                    @Override
                    public void onException(final Exception ex) {
                        if (ex instanceof ApiNetworkException) {
                            EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_NETWORK.get()));
                            return;
                        }
                        EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_UNKNOWN.get()));
                    }
                    
                    @Override
                    public void onFinally() {
                        Util.hideProgressBar(SeriesDetailActivity.this);
                    }
                    
                    @Override
                    public void onPreExecute() {
                        Util.showProgressBar(SeriesDetailActivity.this, SeriesDetailActivity.this.getResources().getColor(2131558518));
                    }
                    
                    @Override
                    public void onSuccess(final Void void1) {
                        prepareToWatch.go(PrepareToWatch.Event.NONE);
                    }
                });
            }
        }
    }
    
    @TargetApi(21)
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (Build$VERSION.SDK_INT >= 21) {
            final Window window = this.getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.clearFlags(67108864);
            window.setStatusBarColor(this.getResources().getColor(2131558417));
        }
        this.setContentView(2130903075);
        if (!(this.mIsTablet = (this.getResources().getInteger(2131427330) != 0))) {
            this.setRequestedOrientation(1);
        }
        (this.mToolbar = (Toolbar)this.findViewById(2131624056)).setTitle("");
        this.setSupportActionBar(this.mToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mSeriesId = this.getIntent().getExtras().getLong("seriesId");
        this.mMediaType = this.getIntent().getExtras().getString("mediaType");
        this.mFilter = this.getIntent().getExtras().getString("filter");
        if (Extras.getList(this.getIntent(), "series", Series.class).isPresent()) {
            this.mSeriesList = Extras.getList(this.getIntent(), "series", Series.class).get();
            this.mOffset = this.mSeriesList.size();
            this.mShouldLoadMoreSeries = this.getIntent().getBooleanExtra("shouldLoadMore", false);
        }
        this.appEntryFrom = this.getIntent().getExtras().getInt("intent_from", 0);
        this.mIsPrepareToWatchOnStart = this.getIntent().getExtras().getBoolean("isPrepareToWatchOnStart");
        this.mHasClickedMedia = false;
        this.mMediaInfoOnStartId = this.getIntent().getExtras().getLong("mediaId");
        this.mIsShowMediaInfoOnStart = this.getIntent().getExtras().getBoolean("isShowMediaInfoOnStart");
        this.mClient = new GoogleApiClient.Builder((Context)this).addApi(AppIndex.APP_INDEX_API).build();
        this.hasIndexed = false;
        this.mFragmentContainer = this.findViewById(2131624055);
        (this.mSeriesPager = (ViewPager)this.findViewById(2131624069)).addOnPageChangeListener((ViewPager.OnPageChangeListener)this.mPageListener);
        this.mLoginLogoutReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();
                if (action.equals("LOCALE_CHANGED") || (!action.equals("USER_LOGGED_IN") && !action.equals("USER_LOGGED_OUT"))) {
                    return;
                }
                if (SeriesDetailActivity.this.mIsPaused) {
                    SeriesDetailActivity.this.mIsRefreshForLoginLogout = true;
                    return;
                }
                SeriesDetailActivity.this.loadData(false, 0L, false);
            }
        };
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("LOCALE_CHANGED");
        intentFilter.addAction("USER_LOGGED_IN");
        intentFilter.addAction("USER_LOGGED_OUT");
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mLoginLogoutReceiver, new IntentFilter(intentFilter));
        if (bundle == null) {
            this.loadData(this.mIsPrepareToWatchOnStart, this.mMediaInfoOnStartId, this.mIsShowMediaInfoOnStart);
            return;
        }
        this.mSeriesList = Extras.getList(bundle, "series", Series.class).orNull();
        this.mOffset = Extras.getInt(bundle, "offset").orNull();
        this.mCurrentIndex = Extras.getInt(bundle, "currentIndex").get();
        this.mMediaType = Extras.getString(bundle, "mediaType").orNull();
        this.mFilter = Extras.getString(bundle, "filter").orNull();
        this.loadData(this.mIsPrepareToWatchOnStart, this.mMediaInfoOnStartId, this.mIsShowMediaInfoOnStart);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mSeriesPager.removeOnPageChangeListener((ViewPager.OnPageChangeListener)this.mPageListener);
        LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.mLoginLogoutReceiver);
    }
    
    public void onEvent(final AppIndexEvent appIndexEvent) {
        if (this.mTitle != null) {
            return;
        }
        try {
            this.mTitle = appIndexEvent.title;
            this.mWebUri = appIndexEvent.webUri;
            this.mAppUri = appIndexEvent.appUri;
            this.mClient.connect();
            AppIndex.AppIndexApi.start(this.mClient, Action.newAction("http://schema.org/ViewAction", this.mTitle, this.mWebUri, this.mAppUri));
        }
        catch (Exception ex) {}
    }
    
    public void onEvent(final ClickEvent clickEvent) {
        this.mHasClickedMedia = true;
    }
    
    public void onEvent(final HideTopBarEvent hideTopBarEvent) {
        if (!this.mIsTablet) {
            fadeOut((View)this.mToolbar);
        }
    }
    
    public void onEvent(final ShowTopBarEvent showTopBarEvent) {
        if (!this.mIsTablet) {
            fadeIn((View)this.mToolbar);
        }
    }
    
    public void onEvent(final Upsell.MediaNotAvailableEvent mediaNotAvailableEvent) {
        Util.showErrorPopup((Context)this, mediaNotAvailableEvent.message);
    }
    
    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        if (menuItem.getItemId() == 2131624351) {
            EventBus.getDefault().post(new ShareEvent(this.mSeriesPager.getCurrentItem()));
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        this.mIsPaused = true;
    }
    
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);
        menu.getItem(4).setVisible(false);
        return true;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        this.mIsPaused = false;
        if (this.mIsRefreshForLoginLogout) {
            this.loadData(false, 0L, false);
        }
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Extras.putList(bundle, "series", this.mSeriesList);
        Extras.putInt(bundle, "offset", Integer.valueOf(this.mOffset));
        Extras.putInt(bundle, "currentIndex", Integer.valueOf(this.mSeriesPager.getCurrentItem()));
        Extras.putString(bundle, "mediaType", this.mMediaType);
        Extras.putString(bundle, "filter", this.mFilter);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        if (!this.hasIndexed && this.mTitle != null) {
            AppIndex.AppIndexApi.end(this.mClient, Action.newAction("http://schema.org/ViewAction", this.mTitle, this.mWebUri, this.mAppUri));
            this.mClient.disconnect();
            this.hasIndexed = true;
        }
    }
    
    protected class LoadMoreSeriesTask extends SafeAsyncTask<List<Series>>
    {
        private final Set<String> fields;
        protected final String filter;
        protected final Integer limit;
        protected final String mediaType;
        protected final Integer offset;
        
        public LoadMoreSeriesTask(final String mediaType, final String filter, final Integer offset, final Integer limit) {
            this.fields = ImmutableSet.of("series.series_id", "series.name", "series.portrait_image", "series.media_count", "media.media_id", "media.name", "media.episode_number", "series.url", "series.description", "image.medium_url", "image.large_url", "image.full_url");
            this.mediaType = mediaType;
            this.filter = filter;
            this.offset = offset;
            this.limit = limit;
        }
        
        @Override
        public List<Series> call() throws Exception {
            final CrunchyrollApplication crunchyrollApplication = (CrunchyrollApplication)SeriesDetailActivity.this.getApplication();
            final ObjectMapper objectMapper = crunchyrollApplication.getObjectMapper();
            final ApiResponse run = crunchyrollApplication.getApiService().run(new ListSeriesRequest(this.mediaType, this.filter, this.offset, this.limit), this.fields);
            final List<Series> list = objectMapper.readValue(run.body.asParser(objectMapper).readValueAsTree().path("data").traverse(), new TypeReference<List<Series>>() {});
            run.cache();
            return list;
        }
        
        @Override
        protected void onException(final Exception ex) throws RuntimeException {
            SeriesDetailActivity.this.log.error("Error loading more series", ex);
            if (ex instanceof ApiNetworkException) {
                Snackbar.make(SeriesDetailActivity.this.mFragmentContainer, LocalizedStrings.ERROR_NETWORK.get(), 0).show();
                return;
            }
            Snackbar.make(SeriesDetailActivity.this.mFragmentContainer, LocalizedStrings.ERROR_LOADING_SERIES.get(), 0).show();
        }
        
        @Override
        protected void onFinally() throws RuntimeException {
        }
        
        @Override
        protected void onSuccess(final List<Series> list) throws Exception {
            if (!this.isCancelled()) {
                if (list != null && !list.isEmpty()) {
                    SeriesDetailActivity.this.mSeriesList.addAll(list);
                    SeriesDetailActivity.this.mAdapter.add(list);
                    SeriesDetailActivity.this.mOffset += this.limit;
                }
                if (list.size() == 0) {
                    Snackbar.make(SeriesDetailActivity.this.mFragmentContainer, LocalizedStrings.NO_SERIES.get(), 0).show();
                }
                if (list.size() < this.offset + this.limit) {
                    SeriesDetailActivity.this.log.info("LOADING EXHAUSTED", new Object[0]);
                }
            }
        }
    }
    
    private class SeriesPagerAdapter extends FragmentStatePagerAdapter
    {
        private boolean mGotFirstView;
        private List<Series> mSeriesList;
        
        public SeriesPagerAdapter(final FragmentManager fragmentManager) {
            super(fragmentManager);
            this.mSeriesList = new ArrayList<Series>();
            this.mGotFirstView = false;
        }
        
        public void add(final List<Series> list) {
            this.mSeriesList.addAll(list);
            this.notifyDataSetChanged();
        }
        
        @Override
        public int getCount() {
            return this.mSeriesList.size();
        }
        
        public int getIndexForSeriesId(final Long n) {
            for (int i = 0; i < this.mSeriesList.size(); ++i) {
                if (this.mSeriesList.get(i).getSeriesId().equals(n)) {
                    return i;
                }
            }
            return 0;
        }
        
        @Override
        public Fragment getItem(final int n) {
            final Series series = this.mSeriesList.get(n);
            final SeriesDetailFragment instance = SeriesDetailFragment.newInstance(n, this.mSeriesList.get(n).getSeriesId(), SeriesDetailActivity.this.mIsPrepareToWatchOnStart, !this.mGotFirstView, SeriesDetailActivity.this.mMediaInfoOnStartId, SeriesDetailActivity.this.mIsShowMediaInfoOnStart, false);
            if (series.getSeriesId().equals(SeriesDetailActivity.this.mSeriesId)) {
                this.mGotFirstView = true;
            }
            if (SeriesDetailActivity.this.mShouldLoadMoreSeries && n == this.mSeriesList.size() - 1) {
                SeriesDetailActivity.this.loadItemsWithOffset(this.mSeriesList.size());
            }
            return instance;
        }
    }
    
    private class TrackingPageChangeListener implements OnPageChangeListener
    {
        @Override
        public void onPageScrollStateChanged(final int n) {
        }
        
        @Override
        public void onPageScrolled(final int n, final float n2, final int n3) {
        }
        
        @Override
        public void onPageSelected(final int n) {
            SeriesDetailActivity.this.trackPosition(n);
        }
    }
}
