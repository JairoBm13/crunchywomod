// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import com.crunchyroll.crunchyroid.receivers.CastBarReciever;
import com.crunchyroll.crunchyroid.activities.PopupActivity;
import java.io.Serializable;
import com.crunchyroll.crunchyroid.util.ActionsUtil;
import com.crunchyroll.crunchyroid.events.ShareEvent;
import android.view.Display;
import com.crunchyroll.crunchyroid.app.PrepareToWatch;
import com.crunchyroll.android.api.models.Series;
import android.view.View$OnClickListener;
import android.annotation.TargetApi;
import android.view.View$OnLayoutChangeListener;
import android.widget.LinearLayout$LayoutParams;
import android.graphics.Point;
import android.support.v7.widget.LinearLayoutManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;
import com.crunchyroll.crunchyroid.events.HideTopBarEvent;
import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import com.crunchyroll.crunchyroid.activities.SeriesDetailActivity;
import com.crunchyroll.crunchyroid.events.ShowTopBarEvent;
import android.animation.Animator;
import android.animation.Animator$AnimatorListener;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.ViewGroup$LayoutParams;
import android.view.View$OnAttachStateChangeListener;
import android.os.Bundle;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import java.util.HashMap;
import com.crunchyroll.crunchyroid.events.AppIndexEvent;
import android.net.Uri;
import java.util.Iterator;
import com.crunchyroll.android.api.models.QueueEntry;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import com.crunchyroll.crunchyroid.events.ErrorEvent;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import de.greenrobot.event.EventBus;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import com.crunchyroll.android.api.tasks.BaseListener;
import android.app.Activity;
import com.crunchyroll.android.api.ApiManager;
import android.view.ViewTreeObserver;
import android.os.Build$VERSION;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import java.util.List;
import android.content.Context;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.crunchyroid.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.support.design.widget.Snackbar;
import com.crunchyroll.android.api.models.SeriesInfoWithMedia;
import android.widget.ImageView;
import android.support.v7.widget.RecyclerView;
import com.crunchyroll.android.widget.ScrollRecyclerView;
import com.crunchyroll.crunchyroid.util.ProgressBarManager;
import android.view.View;
import android.support.design.widget.CoordinatorLayout;
import com.crunchyroll.android.api.models.Media;
import com.crunchyroll.android.api.models.Collection;
import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.view.ViewGroup;
import android.support.design.widget.FloatingActionButton;
import com.crunchyroll.android.widget.AnimatedCircleView;
import com.crunchyroll.crunchyroid.adapters.SeriesDetailAdapter;

public class SeriesDetailFragment extends BaseFragment
{
    private static int ANIMATED_CIRCLE_RADIUS_PX = 0;
    private static int FADE_HEIGHT_PX = 0;
    private static int SERIES_IMAGE_TABLET_LANDSCAPE_EXTRA_DP = 0;
    private static final long SNACKBAR_SHOW_DELAY = 1000L;
    private SeriesDetailAdapter mAdapter;
    private SeriesDetailAdapter mAdapterSecondary;
    private AnimatedCircleView mAnimatedCircleView;
    private FloatingActionButton mButtonFloatQueue;
    private int mButtonFloatQueueX;
    private int mButtonFloatQueueY;
    private ViewGroup mCastBar;
    private BroadcastReceiver mCastReceiver;
    ArrayList<Collection> mCollectionList;
    Media mContinueWatchingMedia;
    private CoordinatorLayout mCoordinatorLayout;
    private boolean mHasCollectionList;
    private boolean mHasContinueWatching;
    private boolean mHasPostedScrollRunnable;
    private boolean mHasSeriesImageHeight;
    private boolean mHasSeriesInfo;
    private int mId;
    private boolean mIsPortrait;
    private boolean mIsPrepareToWatchOnStart;
    private boolean mIsScrollInterrupted;
    private boolean mIsSelfTrack;
    private boolean mIsShowMediaInfoOnStart;
    private boolean mIsTablet;
    private long mMediaInfoOnStartId;
    private View mOverlay;
    private ViewGroup mParent;
    private ProgressBarManager mProgressBarManager;
    private BroadcastReceiver mProgressUpdateReceiver;
    private ScrollRecyclerView mRecyclerView;
    private ScrollRecyclerView mRecyclerViewSecondary;
    private RecyclerView.OnScrollListener mScrollListener;
    private Runnable mScrollRunnable;
    private long mSeriesId;
    private ImageView mSeriesImage;
    private int mSeriesImageHeight;
    SeriesInfoWithMedia mSeriesInfo;
    private int mSeriesInfoIndex;
    private Snackbar mSnackbar;
    private int mSortType;
    
    static {
        SeriesDetailFragment.FADE_HEIGHT_PX = 30;
        SeriesDetailFragment.ANIMATED_CIRCLE_RADIUS_PX = 240;
        SeriesDetailFragment.SERIES_IMAGE_TABLET_LANDSCAPE_EXTRA_DP = 100;
    }
    
    private void finalizeUI() {
        if (this.mHasCollectionList && this.mHasSeriesInfo && this.mHasContinueWatching && this.mHasSeriesImageHeight) {
            ImageLoader.getInstance().displayImage(Util.chooseLargestImage(this.mSeriesInfo.getSeries().getPortraitImage()), this.mSeriesImage, CrunchyrollApplication.getDisplayImageOptions());
            if (this.mIsTablet) {
                this.mButtonFloatQueue.setVisibility(0);
                if (this.mIsPortrait) {
                    this.mAdapter = new SeriesDetailAdapter(this, SeriesDetailAdapter.Type.TYPE_TABLET_POTRAIT, this.mSeriesInfo, this.mCollectionList, CrunchyrollApplication.getApp((Context)this.getActivity()).getCategories(this.mSeriesInfo.getSeries().getMediaType().orNull()), this.mContinueWatchingMedia);
                    this.mAdapterSecondary = new SeriesDetailAdapter(this, SeriesDetailAdapter.Type.TYPE_TABLET_POTRAIT_SECONDARY, this.mSeriesInfo, this.mCollectionList, CrunchyrollApplication.getApp((Context)this.getActivity()).getCategories(this.mSeriesInfo.getSeries().getMediaType().orNull()), this.mContinueWatchingMedia);
                }
                else {
                    this.mAdapter = new SeriesDetailAdapter(this, SeriesDetailAdapter.Type.TYPE_TABLET_LANDSCAPE, this.mSeriesInfo, this.mCollectionList, CrunchyrollApplication.getApp((Context)this.getActivity()).getCategories(this.mSeriesInfo.getSeries().getMediaType().orNull()), this.mContinueWatchingMedia);
                    this.mAdapterSecondary = new SeriesDetailAdapter(this, SeriesDetailAdapter.Type.TYPE_TABLET_LANDSCAPE_SECONDARY, this.mSeriesInfo, this.mCollectionList, CrunchyrollApplication.getApp((Context)this.getActivity()).getCategories(this.mSeriesInfo.getSeries().getMediaType().orNull()), this.mContinueWatchingMedia);
                }
                this.mRecyclerView.setLayoutManager((RecyclerView.LayoutManager)this.mAdapter.getGridLayoutManager());
                this.mRecyclerView.setAdapter((RecyclerView.Adapter)this.mAdapter);
                this.mRecyclerViewSecondary.setLayoutManager((RecyclerView.LayoutManager)this.mAdapterSecondary.getGridLayoutManager());
                this.mRecyclerViewSecondary.setOnScrollListener(this.mScrollListener);
                this.mRecyclerViewSecondary.setAdapter((RecyclerView.Adapter)this.mAdapterSecondary);
            }
            else {
                this.mAdapter = new SeriesDetailAdapter(this, SeriesDetailAdapter.Type.TYPE_PHONE, this.mSeriesInfo, this.mCollectionList, CrunchyrollApplication.getApp((Context)this.getActivity()).getCategories(this.mSeriesInfo.getSeries().getMediaType().orNull()), this.mContinueWatchingMedia);
                this.mRecyclerView.setLayoutManager((RecyclerView.LayoutManager)this.mAdapter.getGridLayoutManager());
                this.mRecyclerView.setOnScrollListener(this.mScrollListener);
                this.mRecyclerView.setAdapter((RecyclerView.Adapter)this.mAdapter);
                final ViewTreeObserver viewTreeObserver = this.mRecyclerView.getViewTreeObserver();
                if (viewTreeObserver != null) {
                    this.mHasPostedScrollRunnable = false;
                    this.mIsScrollInterrupted = false;
                    viewTreeObserver.addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
                        public void onGlobalLayout() {
                            if (!SeriesDetailFragment.this.mHasPostedScrollRunnable) {
                                SeriesDetailFragment.this.mHasPostedScrollRunnable = true;
                                SeriesDetailFragment.this.mParent.post(SeriesDetailFragment.this.mScrollRunnable);
                            }
                            if (SeriesDetailFragment.this.mRecyclerView != null) {
                                final ViewTreeObserver viewTreeObserver = SeriesDetailFragment.this.mRecyclerView.getViewTreeObserver();
                                if (viewTreeObserver != null && viewTreeObserver.isAlive()) {
                                    if (Build$VERSION.SDK_INT < 16) {
                                        viewTreeObserver.removeGlobalOnLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                                        return;
                                    }
                                    viewTreeObserver.removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                                }
                            }
                        }
                    });
                }
            }
            if (!CrunchyrollApplication.getApp((Context)this.getActivity()).getApplicationState().hasLoggedInUser()) {
                this.setFloatingButtonSelected(true);
                return;
            }
            if (!this.mSeriesInfo.getSeries().getInQueue().get()) {
                this.setFloatingButtonSelected(true);
                return;
            }
            this.setFloatingButtonSelected(false);
        }
    }
    
    private void getCollectionList() {
        ApiManager.getInstance(this.getActivity()).getCollectionListData(this.mSeriesId, new BaseListener<ArrayList<Collection>>() {
            @Override
            public void onException(final Exception ex) {
                if (ex instanceof ApiNetworkException) {
                    EventBus.getDefault().post(new ErrorEvent(SeriesDetailFragment.this.mId, LocalizedStrings.ERROR_NETWORK.get()));
                    return;
                }
                EventBus.getDefault().post(new ErrorEvent(SeriesDetailFragment.this.mId, LocalizedStrings.ERROR_UNKNOWN.get()));
            }
            
            @Override
            public void onFinally() {
                SeriesDetailFragment.this.mProgressBarManager.hide();
            }
            
            @Override
            public void onPreExecute() {
                SeriesDetailFragment.this.mProgressBarManager.show();
            }
            
            @Override
            public void onSuccess(final ArrayList<Collection> mCollectionList) {
                SeriesDetailFragment.this.mCollectionList = mCollectionList;
                SeriesDetailFragment.this.mHasCollectionList = true;
                SeriesDetailFragment.this.getSeriesInfoWithMedia(false);
            }
        });
    }
    
    private void getContinueWatching() {
        ApiManager.getInstance(this.getActivity()).getQueueData("anime|drama", new BaseListener<ArrayList<QueueEntry>>() {
            @Override
            public void onException(final Exception ex) {
                SeriesDetailFragment.this.mContinueWatchingMedia = null;
            }
            
            @Override
            public void onFinally() {
                super.onFinally();
                SeriesDetailFragment.this.mHasContinueWatching = true;
                SeriesDetailFragment.this.getSeriesInfoWithMedia(false);
                SeriesDetailFragment.this.mProgressBarManager.hide();
            }
            
            @Override
            public void onPreExecute() {
                SeriesDetailFragment.this.mProgressBarManager.show();
            }
            
            @Override
            public void onSuccess(final ArrayList<QueueEntry> list) {
                if (list != null) {
                    for (final QueueEntry queueEntry : list) {
                        if (queueEntry.getSeries().getSeriesId() == SeriesDetailFragment.this.mSeriesId) {
                            SeriesDetailFragment.this.mContinueWatchingMedia = queueEntry.getMostLikelyMedia().get();
                        }
                    }
                }
            }
        });
    }
    
    private void getSeriesInfoWithMedia(final boolean b) {
        if (this.mHasCollectionList && this.mHasContinueWatching) {
            this.mSeriesInfoIndex = this.mCollectionList.size() - 1;
            this.mSortType = 1;
            if (this.mContinueWatchingMedia != null) {
                this.mSeriesInfoIndex = 0;
                this.mSortType = 0;
            }
            ApiManager.getInstance(this.getActivity()).getMediaListData(this.mSeriesId, this.mCollectionList.get(this.mSeriesInfoIndex).getCollectionId(), new BaseListener<SeriesInfoWithMedia>() {
                @Override
                public void onException(final Exception ex) {
                    if (ex instanceof ApiNetworkException) {
                        EventBus.getDefault().post(new ErrorEvent(SeriesDetailFragment.this.mId, LocalizedStrings.ERROR_NETWORK.get()));
                        return;
                    }
                    EventBus.getDefault().post(new ErrorEvent(SeriesDetailFragment.this.mId, LocalizedStrings.ERROR_UNKNOWN.get()));
                }
                
                @Override
                public void onFinally() throws RuntimeException {
                    SeriesDetailFragment.this.mProgressBarManager.hide();
                }
                
                @Override
                public void onPreExecute() {
                    SeriesDetailFragment.this.mProgressBarManager.show();
                }
                
                @Override
                public void onSuccess(final SeriesInfoWithMedia mSeriesInfo) {
                    SeriesDetailFragment.this.mSeriesInfo = mSeriesInfo;
                    SeriesDetailFragment.this.mHasSeriesInfo = true;
                    if (b) {
                        ((RecyclerView.Adapter)SeriesDetailFragment.this.mAdapter).notifyDataSetChanged();
                    }
                    else {
                        while (true) {
                            try {
                                EventBus.getDefault().post(new AppIndexEvent(SeriesDetailFragment.this.mSeriesInfo.getSeries().getName(), Uri.parse(SeriesDetailFragment.this.mSeriesInfo.getSeries().getUrl()), Uri.parse("android-app://" + SeriesDetailFragment.this.getActivity().getPackageName() + "/crunchyroll/" + "series" + "/" + SeriesDetailFragment.this.mSeriesInfo.getSeries().getSeriesId())));
                                SeriesDetailFragment.this.finalizeUI();
                                if (SeriesDetailFragment.this.mIsSelfTrack) {
                                    if (SeriesDetailFragment.this.mSeriesInfo.getSeries().getMediaType().or("").equalsIgnoreCase("anime")) {
                                        final HashMap<String, String> hashMap = new HashMap<String, String>();
                                        hashMap.put("series", SeriesDetailFragment.this.mSeriesInfo.getSeries().getName());
                                        Tracker.swrveScreenView("anime-series", hashMap);
                                        return;
                                    }
                                    if (SeriesDetailFragment.this.mSeriesInfo.getSeries().getMediaType().or("").equalsIgnoreCase("drama")) {
                                        final HashMap<String, String> hashMap2 = new HashMap<String, String>();
                                        hashMap2.put("series", SeriesDetailFragment.this.mSeriesInfo.getSeries().getName());
                                        Tracker.swrveScreenView("drama-series", hashMap2);
                                    }
                                }
                            }
                            catch (Exception ex) {
                                continue;
                            }
                            break;
                        }
                    }
                }
            });
        }
    }
    
    public static SeriesDetailFragment newInstance(final int n, final Long n2, final boolean b, final boolean b2, final long n3, final boolean b3, final boolean b4) {
        final SeriesDetailFragment seriesDetailFragment = new SeriesDetailFragment();
        final Bundle arguments = new Bundle();
        arguments.putInt("viewPagerId", n);
        arguments.putLong("seriesId", (long)n2);
        arguments.putBoolean("isPrepareToWatchOnStart", b);
        arguments.putBoolean("doScrollAnimation", b2);
        arguments.putLong("mediaId", n3);
        arguments.putBoolean("isShowMediaInfoOnStart", b3);
        arguments.putBoolean("isSelfTrack", b4);
        seriesDetailFragment.setArguments(arguments);
        return seriesDetailFragment;
    }
    
    private void setFloatingButtonSelected(final boolean selected) {
        if (selected) {
            this.mButtonFloatQueue.setRippleColor(this.getResources().getColor(2131558477));
        }
        else {
            this.mButtonFloatQueue.setRippleColor(this.getResources().getColor(2131558432));
        }
        this.mButtonFloatQueue.setSelected(selected);
    }
    
    private void showSnackBar(final String s) {
        if (this.mCoordinatorLayout != null) {
            if (this.mSnackbar != null) {
                this.mSnackbar.dismiss();
            }
            this.mSnackbar = Snackbar.make((View)this.mCoordinatorLayout, s, 0);
            this.mSnackbar.getView().addOnAttachStateChangeListener((View$OnAttachStateChangeListener)new View$OnAttachStateChangeListener() {
                public void onViewAttachedToWindow(final View view) {
                    final ViewGroup$LayoutParams layoutParams = SeriesDetailFragment.this.mCoordinatorLayout.getLayoutParams();
                    layoutParams.width = -1;
                    SeriesDetailFragment.this.mCoordinatorLayout.setLayoutParams(layoutParams);
                }
                
                public void onViewDetachedFromWindow(final View view) {
                    new Handler().postDelayed((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            final ViewGroup$LayoutParams layoutParams = SeriesDetailFragment.this.mCoordinatorLayout.getLayoutParams();
                            layoutParams.width = -2;
                            SeriesDetailFragment.this.mCoordinatorLayout.setLayoutParams(layoutParams);
                        }
                    }, 1000L);
                }
            });
            this.mSnackbar.show();
        }
    }
    
    public String getMediaType() {
        return this.mSeriesInfo.getSeries().getMediaType().orNull();
    }
    
    public int getSeriesImageHeight() {
        return this.mSeriesImageHeight;
    }
    
    public SeriesInfoWithMedia getSeriesInfo() {
        return this.mSeriesInfo;
    }
    
    public int getSeriesInfoIndex() {
        return this.mSeriesInfoIndex;
    }
    
    public int getSortType() {
        return this.mSortType;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        final boolean b = true;
        super.onCreate(bundle);
        this.mIsTablet = (this.getResources().getInteger(2131427330) != 0);
        this.mIsPortrait = (this.getResources().getConfiguration().orientation == 1 && b);
        this.mHasCollectionList = false;
        this.mHasSeriesInfo = false;
        this.mHasContinueWatching = false;
        this.mHasSeriesImageHeight = false;
        this.mId = this.getArguments().getInt("viewPagerId");
        this.mSeriesId = this.getArguments().getLong("seriesId");
        this.mIsPrepareToWatchOnStart = this.getArguments().getBoolean("isPrepareToWatchOnStart");
        this.mMediaInfoOnStartId = this.getArguments().getLong("mediaId");
        this.mIsShowMediaInfoOnStart = this.getArguments().getBoolean("isShowMediaInfoOnStart");
        this.mIsSelfTrack = this.getArguments().getBoolean("isSelfTrack");
        this.mScrollRunnable = new Runnable() {
            final /* synthetic */ boolean val$doScrollAnimation = this.getArguments().getBoolean("doScrollAnimation");
            
            @Override
            public void run() {
                if (!SeriesDetailFragment.this.mIsScrollInterrupted) {
                    final ValueAnimator ofInt = ValueAnimator.ofInt(new int[] { 0, SeriesDetailFragment.this.mSeriesImageHeight / 3 });
                    if (this.val$doScrollAnimation) {
                        ofInt.setDuration(300L);
                    }
                    else {
                        ofInt.setDuration(0L);
                    }
                    ofInt.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
                        private int mPrev;
                        
                        public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                            SeriesDetailFragment.this.mRecyclerView.scrollBy(0, (int)valueAnimator.getAnimatedValue() - this.mPrev);
                            this.mPrev = (int)valueAnimator.getAnimatedValue();
                        }
                    });
                    ofInt.addListener((Animator$AnimatorListener)new Animator$AnimatorListener() {
                        public void onAnimationCancel(final Animator animator) {
                        }
                        
                        public void onAnimationEnd(final Animator animator) {
                            EventBus.getDefault().post(new ShowTopBarEvent());
                            SeriesDetailActivity.fadeIn((View)SeriesDetailFragment.this.mButtonFloatQueue);
                        }
                        
                        public void onAnimationRepeat(final Animator animator) {
                        }
                        
                        public void onAnimationStart(final Animator animator) {
                        }
                    });
                    ofInt.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                    ofInt.start();
                }
            }
        };
        this.mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                super.onScrollStateChanged(recyclerView, n);
                if (!SeriesDetailFragment.this.mIsTablet) {
                    switch (n) {
                        case 0: {
                            if (SeriesDetailFragment.this.mRecyclerView.getVerticalOffset() <= SeriesDetailFragment.FADE_HEIGHT_PX) {
                                EventBus.getDefault().post(new HideTopBarEvent());
                                SeriesDetailActivity.fadeOut((View)SeriesDetailFragment.this.mButtonFloatQueue);
                                return;
                            }
                            if (SeriesDetailFragment.this.mRecyclerView.getVerticalOffset() > SeriesDetailFragment.FADE_HEIGHT_PX) {
                                EventBus.getDefault().post(new ShowTopBarEvent());
                                SeriesDetailActivity.fadeIn((View)SeriesDetailFragment.this.mButtonFloatQueue);
                                return;
                            }
                            break;
                        }
                    }
                }
            }
            
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                super.onScrolled(recyclerView, n, n2);
                SeriesDetailFragment.this.mIsScrollInterrupted = true;
                SeriesDetailFragment.this.mOverlay.setAlpha(Math.min(((ScrollRecyclerView)recyclerView).getVerticalOffset(), (float)(SeriesDetailFragment.this.mSeriesImageHeight * 0.7)) / SeriesDetailFragment.this.mSeriesImageHeight);
            }
        };
        this.mProgressUpdateReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getLongExtra("seriesId", 0L) == SeriesDetailFragment.this.mSeriesId) {
                    SeriesDetailFragment.this.getSeriesInfoWithMedia(true);
                }
            }
        };
        LocalBroadcastManager.getInstance((Context)this.getActivity()).registerReceiver(this.mProgressUpdateReceiver, new IntentFilter("VIDEO_PROGRESS_UPDATED"));
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, @Nullable final ViewGroup viewGroup, @Nullable final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2130903130, viewGroup, false);
        this.mParent = (ViewGroup)inflate.findViewById(2131624060);
        this.mCoordinatorLayout = (CoordinatorLayout)inflate.findViewById(2131624186);
        this.mSeriesImage = (ImageView)inflate.findViewById(2131624245);
        this.mOverlay = inflate.findViewById(2131624246);
        (this.mRecyclerView = (ScrollRecyclerView)inflate.findViewById(2131624242)).setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager((Context)this.getActivity()));
        this.mRecyclerViewSecondary = (ScrollRecyclerView)inflate.findViewById(2131624250);
        this.mButtonFloatQueue = (FloatingActionButton)inflate.findViewById(2131624247);
        this.mAnimatedCircleView = (AnimatedCircleView)inflate.findViewById(2131624248);
        this.mCastBar = (ViewGroup)inflate.findViewById(2131624092);
        if (this.mIsTablet && !this.mIsPortrait) {
            final Display defaultDisplay = this.getActivity().getWindowManager().getDefaultDisplay();
            final Point point = new Point();
            defaultDisplay.getSize(point);
            final int n = (point.y - Util.dpToPx((Context)this.getActivity(), SeriesDetailFragment.SERIES_IMAGE_TABLET_LANDSCAPE_EXTRA_DP)) * this.getResources().getInteger(2131427343) / this.getResources().getInteger(2131427342) * 100 / point.x;
            int n2;
            int n3;
            if ((n2 = n) > (n3 = 100 - n)) {
                n2 = 1;
                n3 = 1;
            }
            final View viewById = inflate.findViewById(2131624249);
            final LinearLayout$LayoutParams layoutParams = new LinearLayout$LayoutParams(0, -2);
            layoutParams.weight = n2;
            viewById.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            final LinearLayout$LayoutParams layoutParams2 = new LinearLayout$LayoutParams(0, -2);
            layoutParams2.weight = n3;
            this.mRecyclerView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
        }
        this.mButtonFloatQueue.addOnLayoutChangeListener((View$OnLayoutChangeListener)new View$OnLayoutChangeListener() {
            @TargetApi(21)
            public void onLayoutChange(final View view, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
                view.removeOnLayoutChangeListener((View$OnLayoutChangeListener)this);
                final int[] array = new int[2];
                SeriesDetailFragment.this.mButtonFloatQueue.getLocationOnScreen(array);
                final int[] array2 = new int[2];
                SeriesDetailFragment.this.mParent.getLocationOnScreen(array2);
                SeriesDetailFragment.this.mButtonFloatQueueX = array[0] - array2[0] + SeriesDetailFragment.this.mButtonFloatQueue.getWidth() / 2;
                SeriesDetailFragment.this.mButtonFloatQueueY = array[1] - array2[1] + SeriesDetailFragment.this.mButtonFloatQueue.getHeight() / 2;
            }
        });
        this.mButtonFloatQueue.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (SeriesDetailFragment.this.mSeriesInfo != null) {
                    final Series series = SeriesDetailFragment.this.mSeriesInfo.getSeries();
                    if (CrunchyrollApplication.getApp((Context)SeriesDetailFragment.this.getActivity()).getApplicationState().hasLoggedInUser()) {
                        if (!view.isSelected()) {
                            ApiManager.getInstance(SeriesDetailFragment.this.getActivity()).removeFromQueue(series, new BaseListener<Void>() {
                                @Override
                                public void onSuccess(final Void void1) {
                                    super.onSuccess(void1);
                                    SeriesDetailFragment.this.mAnimatedCircleView.animateCircle(SeriesDetailFragment.this.mButtonFloatQueueX, SeriesDetailFragment.this.mButtonFloatQueueY, SeriesDetailFragment.ANIMATED_CIRCLE_RADIUS_PX, 2131558477);
                                    SeriesDetailFragment.this.setFloatingButtonSelected(true);
                                    series.setInQueue(false);
                                    if (!SeriesDetailFragment.this.mIsTablet) {
                                        SeriesDetailFragment.this.mAdapter.animateBookmark(false);
                                        return;
                                    }
                                    if (SeriesDetailFragment.this.mIsPortrait) {
                                        SeriesDetailFragment.this.mAdapterSecondary.animateBookmark(false);
                                        return;
                                    }
                                    SeriesDetailFragment.this.mAdapter.animateBookmark(false);
                                }
                            });
                            Tracker.seriesDetailQueue("remove-from-queue", series.getName());
                            return;
                        }
                        ApiManager.getInstance(SeriesDetailFragment.this.getActivity()).addToQueue(series.getSeriesId(), new BaseListener<QueueEntry>() {
                            @Override
                            public void onSuccess(final QueueEntry queueEntry) {
                                super.onSuccess(queueEntry);
                                SeriesDetailFragment.this.mAnimatedCircleView.animateCircle(SeriesDetailFragment.this.mButtonFloatQueueX, SeriesDetailFragment.this.mButtonFloatQueueY, SeriesDetailFragment.ANIMATED_CIRCLE_RADIUS_PX, 2131558432);
                                SeriesDetailFragment.this.setFloatingButtonSelected(false);
                                series.setInQueue(true);
                                if (!SeriesDetailFragment.this.mIsTablet) {
                                    SeriesDetailFragment.this.mAdapter.animateBookmark(true);
                                    return;
                                }
                                if (SeriesDetailFragment.this.mIsPortrait) {
                                    SeriesDetailFragment.this.mAdapterSecondary.animateBookmark(true);
                                    return;
                                }
                                SeriesDetailFragment.this.mAdapter.animateBookmark(true);
                            }
                        });
                        Tracker.seriesDetailQueue("add-to-queue", series.getName());
                    }
                    else if (!CrunchyrollApplication.getApp((Context)SeriesDetailFragment.this.getActivity()).isPrepareToWatchLoading()) {
                        final PrepareToWatch prepareToWatchNoMedia = CrunchyrollApplication.getApp((Context)SeriesDetailFragment.this.getActivity()).prepareToWatchNoMedia(SeriesDetailFragment.this.getActivity(), PrepareToWatch.Type.PREPARE_UPSELL_FEATURE, false, 0, SeriesDetailFragment.this.mSeriesInfo.getSeries().getName());
                        prepareToWatchNoMedia.prepare(new BaseListener<Void>() {
                            @Override
                            public void onException(final Exception ex) {
                                if (ex instanceof ApiNetworkException) {
                                    EventBus.getDefault().post(new ErrorEvent(SeriesDetailFragment.this.mId, LocalizedStrings.ERROR_NETWORK.get()));
                                    return;
                                }
                                EventBus.getDefault().post(new ErrorEvent(SeriesDetailFragment.this.mId, LocalizedStrings.ERROR_UNKNOWN.get()));
                            }
                            
                            @Override
                            public void onFinally() {
                                super.onFinally();
                                SeriesDetailFragment.this.mProgressBarManager.hide();
                            }
                            
                            @Override
                            public void onPreExecute() {
                                SeriesDetailFragment.this.mProgressBarManager.show();
                            }
                            
                            @Override
                            public void onSuccess(final Void void1) {
                                prepareToWatchNoMedia.go(PrepareToWatch.Event.NONE);
                                CrunchyrollApplication.getApp((Context)SeriesDetailFragment.this.getActivity()).startQueueAfterLoginReceiver(series);
                            }
                        });
                    }
                }
            }
        });
        this.mProgressBarManager = new ProgressBarManager((Context)this.getActivity(), this.mParent, this.getResources().getColor(2131558518));
        if (bundle == null) {
            this.getCollectionList();
            this.getContinueWatching();
            return inflate;
        }
        this.mCollectionList = (ArrayList<Collection>)bundle.getParcelableArrayList("collections");
        this.mSeriesInfo = (SeriesInfoWithMedia)bundle.getSerializable("seriesInfoWithMedia");
        this.mContinueWatchingMedia = (Media)bundle.getSerializable("continueWatching");
        this.mSeriesInfoIndex = bundle.getInt("seriesInfoIndex");
        this.mSortType = bundle.getInt("sortType");
        this.mHasContinueWatching = true;
        if (this.mCollectionList == null) {
            this.getCollectionList();
            return inflate;
        }
        if (this.mSeriesInfo == null) {
            this.mHasCollectionList = true;
            this.getSeriesInfoWithMedia(false);
            return inflate;
        }
        this.mHasCollectionList = true;
        this.mHasSeriesInfo = true;
        this.finalizeUI();
        return inflate;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance((Context)this.getActivity()).unregisterReceiver(this.mProgressUpdateReceiver);
        LocalBroadcastManager.getInstance((Context)this.getActivity()).unregisterReceiver(this.mCastReceiver);
    }
    
    public void onEvent(final ErrorEvent errorEvent) {
        if (this.mId == errorEvent.getId()) {
            this.showSnackBar(errorEvent.getMessage());
        }
    }
    
    public void onEvent(final ShareEvent shareEvent) {
        if (this.mSeriesInfo != null && this.mId == shareEvent.getId()) {
            final Series series = this.mSeriesInfo.getSeries();
            ActionsUtil.share((Context)this.getActivity(), this.mId, series.getName(), null, series.getUrl());
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Util.updateCastBar(this.getActivity(), this.mCastBar);
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        bundle.putParcelableArrayList("collections", (ArrayList)this.mCollectionList);
        bundle.putSerializable("continueWatching", (Serializable)this.mContinueWatchingMedia);
        bundle.putSerializable("seriesInfoWithMedia", (Serializable)this.mSeriesInfo);
        bundle.putInt("seriesInfoIndex", this.mSeriesInfoIndex);
        bundle.putInt("sortType", this.mSortType);
        super.onSaveInstanceState(bundle);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    
    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (this.mSeriesInfo != null) {
            this.mAdapter = new SeriesDetailAdapter(this, SeriesDetailAdapter.Type.TYPE_PHONE, this.mSeriesInfo, this.mCollectionList, CrunchyrollApplication.getApp((Context)this.getActivity()).getCategories(this.mSeriesInfo.getSeries().getMediaType().orNull()), this.mContinueWatchingMedia);
            this.mRecyclerView.setLayoutManager((RecyclerView.LayoutManager)this.mAdapter.getGridLayoutManager());
        }
        final ViewTreeObserver viewTreeObserver = this.mSeriesImage.getViewTreeObserver();
        if (viewTreeObserver != null) {
            viewTreeObserver.addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    SeriesDetailFragment.this.mSeriesImageHeight = SeriesDetailFragment.this.mSeriesImage.getMeasuredHeight();
                    SeriesDetailFragment.this.mHasSeriesImageHeight = true;
                    SeriesDetailFragment.this.finalizeUI();
                    if (SeriesDetailFragment.this.mRecyclerView != null) {
                        final ViewTreeObserver viewTreeObserver = SeriesDetailFragment.this.mRecyclerView.getViewTreeObserver();
                        if (viewTreeObserver != null && viewTreeObserver.isAlive()) {
                            if (Build$VERSION.SDK_INT < 16) {
                                viewTreeObserver.removeGlobalOnLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                                return;
                            }
                            viewTreeObserver.removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                        }
                    }
                }
            });
        }
        if (this.mIsPrepareToWatchOnStart) {
            final PrepareToWatch prepareToWatch = CrunchyrollApplication.getApp((Context)this.getActivity()).getPrepareToWatch();
            prepareToWatch.setActivity(this.getActivity());
            prepareToWatch.prepare(new BaseListener<Void>() {
                @Override
                public void onException(final Exception ex) {
                    if (ex instanceof ApiNetworkException) {
                        EventBus.getDefault().post(new ErrorEvent(SeriesDetailFragment.this.mId, LocalizedStrings.ERROR_NETWORK.get()));
                        return;
                    }
                    EventBus.getDefault().post(new ErrorEvent(SeriesDetailFragment.this.mId, LocalizedStrings.ERROR_UNKNOWN.get()));
                }
                
                @Override
                public void onFinally() {
                    SeriesDetailFragment.this.mProgressBarManager.hide();
                }
                
                @Override
                public void onPreExecute() {
                    SeriesDetailFragment.this.mProgressBarManager.show();
                }
                
                @Override
                public void onSuccess(final Void void1) {
                    prepareToWatch.go(PrepareToWatch.Event.NONE);
                }
            });
        }
        else if (this.mIsShowMediaInfoOnStart) {
            PopupActivity.startMediaInfo(this.getActivity(), this.mMediaInfoOnStartId);
        }
        this.mCastReceiver = new CastBarReciever(this.getActivity(), this.mCastBar);
        LocalBroadcastManager.getInstance((Context)this.getActivity()).registerReceiver(this.mCastReceiver, CastBarReciever.getIntentFilter());
        Util.initCastBar(this.mCastBar);
        Util.updateCastBar(this.getActivity(), this.mCastBar);
        this.finalizeUI();
    }
    
    public void setSeriesInfo(final SeriesInfoWithMedia mSeriesInfo) {
        this.mSeriesInfo = mSeriesInfo;
    }
    
    public void setSeriesInfoIndex(final int mSeriesInfoIndex) {
        this.mSeriesInfoIndex = mSeriesInfoIndex;
    }
    
    public void setSortType(final int mSortType) {
        this.mSortType = mSortType;
    }
}
