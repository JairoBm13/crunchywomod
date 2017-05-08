// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.app;

import com.crunchyroll.android.api.models.AuthenticateItem;
import com.crunchyroll.crunchyroid.fragments.UpsellFeatureFragment;
import com.crunchyroll.crunchyroid.fragments.SuccessFragment;
import com.crunchyroll.crunchyroid.fragments.ForgotPasswordFragment;
import com.crunchyroll.crunchyroid.util.Util;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import com.crunchyroll.crunchyroid.events.PopupCloseEvent;
import android.support.v4.app.Fragment;
import com.crunchyroll.crunchyroid.events.PopupNewFragmentEvent;
import com.crunchyroll.crunchyroid.fragments.PaymentFragment;
import com.crunchyroll.crunchyroid.fragments.CreateAccountLoginPillTabsPopupFragment;
import com.crunchyroll.cast.CastState;
import com.crunchyroll.cast.CastHandler;
import com.crunchyroll.video.activities.VideoPlayerActivity;
import com.crunchyroll.crunchyroid.activities.PopupActivity;
import com.crunchyroll.crunchyroid.events.Upsell;
import android.app.AlertDialog$Builder;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import com.crunchyroll.android.api.exceptions.ApiErrorException;
import android.content.Intent;
import com.crunchyroll.crunchyroid.activities.MainActivity;
import com.crunchyroll.crunchyroid.events.ErrorEvent;
import de.greenrobot.event.EventBus;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import com.crunchyroll.android.api.ApiManager;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.content.Context;
import java.util.List;
import com.crunchyroll.crunchyroid.fragments.UpsellFragment;
import com.crunchyroll.video.util.MediaManager;
import com.crunchyroll.android.api.models.Media;
import com.crunchyroll.android.api.models.MembershipInfoItem;
import com.crunchyroll.android.api.tasks.BaseListener;
import android.os.Handler;
import com.crunchyroll.android.api.models.FreeTrialInformationItem;
import com.crunchyroll.android.api.models.EpisodeInfo;
import android.app.Activity;

public class PrepareToWatch
{
    public static final int SET_LOADING_FALSE_DELAY_MS = 500;
    private boolean hasInitMedia;
    private Activity mActivity;
    private int mAppEntryFrom;
    private EpisodeInfo mEpisodeInfo;
    private FreeTrialInformationItem mFreeTrialInfoItem;
    private Handler mHandler;
    private boolean mIsLoading;
    private boolean mIsReInit;
    private boolean mIsValid;
    private BaseListener<Void> mListener;
    private MembershipInfoItem mMembershipInfoItem;
    private Type mPrepareType;
    private String mSeriesName;
    private SetNotLoadingRunnable mSetNotLoadingRunnable;
    private boolean mSkipResumeDialog;
    private Media mStartMedia;
    private State mState;
    private MediaManager.MediaManagerListener mTaskListener;
    private UpsellFragment.Type mUpsellType;
    private int mWatchStatus;
    
    public PrepareToWatch(final Activity mActivity, final Type mPrepareType, final Media mStartMedia, final List<Media> playlist, final boolean mSkipResumeDialog, final int mAppEntryFrom, final String mSeriesName) {
        this.mTaskListener = new MediaManager.MediaManagerListener() {
            @Override
            public void onException(final Exception ex) {
                PrepareToWatch.this.mState = State.STATE_ERROR;
                PrepareToWatch.this.showError(ex);
                PrepareToWatch.this.mListener.onException(ex);
                PrepareToWatch.this.mListener.onFinally();
            }
            
            @Override
            public void onFinally() {
                PrepareToWatch.this.mHandler.postDelayed((Runnable)PrepareToWatch.this.mSetNotLoadingRunnable, 500L);
            }
            
            @Override
            public void onNextEpisodeReceived(final EpisodeInfo episodeInfo) {
                PrepareToWatch.this.mState = State.STATE_PREPARED_WITH_MEDIA;
                PrepareToWatch.this.mEpisodeInfo = episodeInfo;
                PrepareToWatch.this.mWatchStatus = WatchStatus.get(CrunchyrollApplication.getApp((Context)PrepareToWatch.this.mActivity).getApplicationState(), PrepareToWatch.this.mEpisodeInfo.getMedia());
                Tracker.videoData((Context)PrepareToWatch.this.mActivity, PrepareToWatch.this.mEpisodeInfo.getMedia().getMediaId());
                PrepareToWatch.this.mListener.onSuccess(null);
                PrepareToWatch.this.mListener.onFinally();
            }
        };
        this.mActivity = mActivity;
        this.mPrepareType = mPrepareType;
        switch (this.mPrepareType) {
            case PREPARE_MEDIA: {
                this.hasInitMedia = true;
                this.mStartMedia = mStartMedia;
                break;
            }
            case PREPARE_MEDIA_LIST: {
                this.hasInitMedia = true;
                MediaManager.getInstance().setPlaylist(playlist);
                break;
            }
            case PREPARE_LOGIN:
            case PREPARE_SIGNUP:
            case PREPARE_UPSELL_NONE:
            case PREPARE_UPSELL_FEATURE: {
                this.mSeriesName = mSeriesName;
                this.hasInitMedia = false;
                break;
            }
        }
        this.mState = State.STATE_UNPREPARED;
        this.mUpsellType = UpsellFragment.Type.TYPE_NONE;
        this.mSkipResumeDialog = mSkipResumeDialog;
        this.mAppEntryFrom = mAppEntryFrom;
        this.mIsValid = true;
        this.mIsReInit = false;
        this.mIsLoading = false;
        this.mHandler = new Handler();
        this.mSetNotLoadingRunnable = new SetNotLoadingRunnable();
    }
    
    private void episodeInfoSetup() {
        final MediaManager instance = MediaManager.getInstance();
        if (this.mStartMedia == null) {
            this.mIsLoading = true;
            instance.getNext(this.mActivity, this.mTaskListener);
            return;
        }
        this.mIsLoading = true;
        instance.createPlaylistFromStartMedia(this.mActivity, this.mStartMedia, (MediaManager.CreatePlaylistListener)new MediaManager.CreatePlaylistListener() {
            @Override
            public void onException(final Exception ex) {
                PrepareToWatch.this.showError(ex);
                PrepareToWatch.this.mState = State.STATE_ERROR;
                PrepareToWatch.this.mListener.onException(ex);
                PrepareToWatch.this.mListener.onFinally();
                PrepareToWatch.this.mHandler.postDelayed((Runnable)PrepareToWatch.this.mSetNotLoadingRunnable, 500L);
            }
            
            @Override
            public void onPlaylistCreated() {
                instance.getNext(PrepareToWatch.this.mActivity, PrepareToWatch.this.mTaskListener);
            }
        });
    }
    
    private void getFreeTrialInfo(final boolean b, final boolean b2, final boolean b3) {
        this.mFreeTrialInfoItem = null;
        this.mMembershipInfoItem = null;
        this.mIsLoading = true;
        ApiManager.getInstance(this.mActivity).getFreeTrialInfo(new ApiTaskListener<List<FreeTrialInformationItem>>() {
            @Override
            public void onCancel() {
            }
            
            @Override
            public void onException(final Exception ex) {
                PrepareToWatch.this.getMembershipInfo(b, b2);
            }
            
            @Override
            public void onFinally() {
            }
            
            @Override
            public void onInterrupted(final Exception ex) {
            }
            
            @Override
            public void onPreExecute() {
                if (b3) {
                    PrepareToWatch.this.mListener.onPreExecute();
                }
            }
            
            @Override
            public void onSuccess(final List<FreeTrialInformationItem> list) {
                if (list == null || list.size() == 0) {
                    this.onException(new ArrayIndexOutOfBoundsException("No free trial info available"));
                    return;
                }
                PrepareToWatch.this.mFreeTrialInfoItem = list.get(0);
                if (b) {
                    PrepareToWatch.this.episodeInfoSetup();
                    return;
                }
                if (!b2) {
                    switch (PrepareToWatch.this.mPrepareType) {
                        case PREPARE_LOGIN: {
                            PrepareToWatch.this.mState = State.STATE_PREPARED_LOGIN;
                            break;
                        }
                        case PREPARE_SIGNUP: {
                            PrepareToWatch.this.mState = State.STATE_PREPARED_SIGNUP;
                            break;
                        }
                        case PREPARE_UPSELL_NONE: {
                            PrepareToWatch.this.mState = State.STATE_PREPARED_NO_MEDIA;
                            break;
                        }
                        case PREPARE_UPSELL_FEATURE: {
                            PrepareToWatch.this.mState = State.STATE_PREPARED_FEATURE;
                            break;
                        }
                    }
                }
                PrepareToWatch.this.mListener.onSuccess(null);
                PrepareToWatch.this.mListener.onFinally();
                PrepareToWatch.this.mHandler.postDelayed((Runnable)PrepareToWatch.this.mSetNotLoadingRunnable, 500L);
            }
        });
    }
    
    private void getMembershipInfo(final boolean b, final boolean b2) {
        this.mFreeTrialInfoItem = null;
        this.mMembershipInfoItem = null;
        this.mIsLoading = true;
        ApiManager.getInstance(this.mActivity).getMembershipInfo(new ApiTaskListener<List<MembershipInfoItem>>() {
            @Override
            public void onCancel() {
            }
            
            @Override
            public void onException(final Exception ex) {
                PrepareToWatch.this.mState = State.STATE_ERROR;
                EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.FREETRIAL_USE_WEBSITE.get()));
                PrepareToWatch.this.mListener.onException(ex);
                PrepareToWatch.this.mListener.onFinally();
                PrepareToWatch.this.mHandler.postDelayed((Runnable)PrepareToWatch.this.mSetNotLoadingRunnable, 500L);
            }
            
            @Override
            public void onFinally() {
            }
            
            @Override
            public void onInterrupted(final Exception ex) {
                this.onException(ex);
            }
            
            @Override
            public void onPreExecute() {
            }
            
            @Override
            public void onSuccess(final List<MembershipInfoItem> list) {
                if (list == null || list.size() == 0) {
                    this.onException(new ArrayIndexOutOfBoundsException("No membership info available"));
                    return;
                }
                PrepareToWatch.this.mMembershipInfoItem = list.get(0);
                if (b) {
                    PrepareToWatch.this.episodeInfoSetup();
                    return;
                }
                if (!b2) {
                    switch (PrepareToWatch.this.mPrepareType) {
                        case PREPARE_LOGIN: {
                            PrepareToWatch.this.mState = State.STATE_PREPARED_LOGIN;
                            break;
                        }
                        case PREPARE_SIGNUP: {
                            PrepareToWatch.this.mState = State.STATE_PREPARED_SIGNUP;
                            break;
                        }
                        case PREPARE_UPSELL_NONE: {
                            PrepareToWatch.this.mState = State.STATE_PREPARED_NO_MEDIA;
                            break;
                        }
                        case PREPARE_UPSELL_FEATURE: {
                            PrepareToWatch.this.mState = State.STATE_PREPARED_FEATURE;
                            break;
                        }
                    }
                }
                PrepareToWatch.this.mListener.onSuccess(null);
                PrepareToWatch.this.mListener.onFinally();
                PrepareToWatch.this.mHandler.postDelayed((Runnable)PrepareToWatch.this.mSetNotLoadingRunnable, 500L);
            }
        });
    }
    
    private void goToMainActivity() {
        MainActivity.Type type = MainActivity.Type.TYPE_NORMAL;
        if (!ApplicationState.get((Context)this.mActivity).hasLoggedInUser()) {
            type = MainActivity.Type.TYPE_ANIME;
        }
        final Intent intent = new Intent((Context)this.mActivity, (Class)MainActivity.class);
        Extras.putSerializable(intent, "mainType", type);
        intent.setFlags(335544320);
        this.mActivity.startActivity(intent);
    }
    
    private void showError(final Exception ex) {
        String s;
        if (ex instanceof ApiErrorException) {
            s = ex.getMessage();
        }
        else {
            s = LocalizedStrings.ERROR_LOADING_MEDIA.get();
        }
        if (ex instanceof ApiNetworkException) {
            s = LocalizedStrings.ERROR_NETWORK.get();
        }
        EventBus.getDefault().post(new ErrorEvent(s));
    }
    
    public FreeTrialInformationItem getFreeTrialInfoItem() {
        return this.mFreeTrialInfoItem;
    }
    
    public MembershipInfoItem getMembershipInfoItem() {
        return this.mMembershipInfoItem;
    }
    
    public Media getStartMedia() {
        return this.mStartMedia;
    }
    
    public void go(final Event event) {
        final boolean b = true;
        boolean b2 = true;
        if (this.isValid()) {
            if (this.mIsReInit) {
                this.mIsReInit = false;
                this.mIsValid = true;
            }
            final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.mActivity);
            String s = "";
            if (this.mMembershipInfoItem != null) {
                s = this.mMembershipInfoItem.getPriceAndSymbol();
            }
            else if (this.mFreeTrialInfoItem != null) {
                s = this.mFreeTrialInfoItem.getPriceAndSymbol();
            }
            switch (this.mState) {
                case STATE_PREPARED_WITH_MEDIA: {
                    if (this.mStartMedia == null) {
                        this.mStartMedia = this.mEpisodeInfo.getMedia();
                    }
                    final String[] array = { this.mStartMedia.getSeriesName().orNull(), String.format("episode-%s", this.mStartMedia.getEpisodeNumber()) };
                    switch (this.mWatchStatus) {
                        default: {
                            this.mState = State.STATE_ERROR;
                            CrunchyrollApplication.getApp((Context)this.mActivity).invalidatePrepareToWatch();
                            EventBus.getDefault().post(new Upsell.MediaNotAvailableEvent(LocalizedStrings.ERROR_NOT_AVAILABLE.get()));
                            return;
                        }
                        case 1: {
                            Tracker.setUpsellOrigin(Tracker.TrackingOrigin.FREE_CONTENT, array);
                            Tracker.trackView((Context)this.mActivity, Tracker.Screen.UPSELL_FREE_ACCOUNT);
                            ApplicationState.get((Context)this.mActivity).incrementNumAnonVideoViews();
                            this.mState = State.STATE_UPSELL_WITH_MEDIA;
                            this.mUpsellType = UpsellFragment.Type.TYPE_ANONYMOUS;
                            PopupActivity.startUpsell(this.mActivity, UpsellFragment.Type.TYPE_ANONYMOUS);
                            return;
                        }
                        case 5: {
                            Tracker.setUpsellOrigin(Tracker.TrackingOrigin.PREMIUM_CONTENT, array);
                            Tracker.trackView((Context)this.mActivity, Tracker.Screen.UPSELL_FREE_TRIAL);
                            this.mState = State.STATE_UPSELL_WITH_MEDIA;
                            this.mUpsellType = UpsellFragment.Type.TYPE_PREMIUM_REQUIRED;
                            PopupActivity.startUpsell(this.mActivity, UpsellFragment.Type.TYPE_PREMIUM_REQUIRED);
                            return;
                        }
                        case 6: {
                            Tracker.setUpsellOrigin(Tracker.TrackingOrigin.PREMIUM_CONTENT, array);
                            Tracker.trackView((Context)this.mActivity, Tracker.Screen.UPSELL_UPGRADE);
                            this.mState = State.STATE_UPSELL_WITH_MEDIA;
                            this.mUpsellType = UpsellFragment.Type.TYPE_ALL_ACCESS_REQUIRED;
                            PopupActivity.startUpsell(this.mActivity, UpsellFragment.Type.TYPE_ALL_ACCESS_REQUIRED);
                            return;
                        }
                        case 0: {
                            this.mState = State.STATE_WATCHING;
                            CrunchyrollApplication.getApp((Context)this.mActivity).invalidatePrepareToWatch();
                            VideoPlayerActivity.start(this.mActivity, this.mEpisodeInfo, this.mSkipResumeDialog, this.mAppEntryFrom);
                            this.mActivity.overridePendingTransition(0, 0);
                            return;
                        }
                        case 2: {
                            this.mState = State.STATE_WATCHING;
                            final CastState state = CastHandler.get().getState();
                            CrunchyrollApplication.getApp((Context)this.mActivity).invalidatePrepareToWatch();
                            VideoPlayerActivity.start(this.mActivity, this.mEpisodeInfo, this.mSkipResumeDialog, this.mAppEntryFrom, new Boolean(state != CastState.CONNECTING && state != CastState.CONNECTED));
                            this.mActivity.overridePendingTransition(0, 0);
                            return;
                        }
                        case 4: {
                            this.mState = State.STATE_ERROR;
                            CrunchyrollApplication.getApp((Context)this.mActivity).invalidatePrepareToWatch();
                            EventBus.getDefault().post(new Upsell.MediaNotAvailableEvent(LocalizedStrings.ERROR_ENCODE_MISSING.get()));
                            return;
                        }
                    }
                    break;
                }
                case STATE_PREPARED_NO_MEDIA: {
                    this.mState = State.STATE_UPSELL_NO_MEDIA;
                    PopupActivity.startUpsell(this.mActivity, UpsellFragment.Type.TYPE_NONE);
                }
                case STATE_PREPARED_LOGIN: {
                    this.mState = State.STATE_SIGNUP_LOGIN;
                    PopupActivity.startSignup(this.mActivity, CreateAccountLoginPillTabsPopupFragment.Type.TYPE_NOT_BACKABLE, true);
                }
                case STATE_PREPARED_SIGNUP: {
                    this.mState = State.STATE_SIGNUP_LOGIN;
                    PopupActivity.startSignup(this.mActivity, CreateAccountLoginPillTabsPopupFragment.Type.TYPE_NOT_BACKABLE, false);
                }
                case STATE_PREPARED_FEATURE: {
                    this.mState = State.STATE_UPSELL_FEATURE;
                    PopupActivity.startUpsellFeature(this.mActivity, this.mSeriesName);
                }
                case STATE_UPSELL_WITH_MEDIA: {
                    switch (event) {
                        default: {
                            return;
                        }
                        case OK: {
                            if (CrunchyrollApplication.getApp((Context)this.mActivity).getApplicationState().hasLoggedInUser()) {
                                this.mState = State.STATE_PAYMENT_WITH_MEDIA;
                                EventBus.getDefault().post(new PopupNewFragmentEvent(PaymentFragment.newInstance(true, PaymentFragment.Type.TYPE_ONLY_STEP)));
                                return;
                            }
                            this.mState = State.STATE_SIGNUP_WITH_MEDIA;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(CreateAccountLoginPillTabsPopupFragment.newInstance(CreateAccountLoginPillTabsPopupFragment.Type.TYPE_BACKABLE, false)));
                            return;
                        }
                        case CANCEL:
                        case CLOSE:
                        case BACK: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            return;
                        }
                        case UPSELL_DISMISSED: {
                            this.mState = State.STATE_WATCHING;
                            if (this.hasInitMedia) {
                                final CastState state2 = CastHandler.get().getState();
                                final Activity mActivity = this.mActivity;
                                final EpisodeInfo mEpisodeInfo = this.mEpisodeInfo;
                                final boolean mSkipResumeDialog = this.mSkipResumeDialog;
                                final int mAppEntryFrom = this.mAppEntryFrom;
                                if (state2 == CastState.CONNECTING || state2 == CastState.CONNECTED) {
                                    b2 = false;
                                }
                                VideoPlayerActivity.start(mActivity, mEpisodeInfo, mSkipResumeDialog, mAppEntryFrom, new Boolean(b2));
                                return;
                            }
                            EventBus.getDefault().post(new PopupCloseEvent());
                            return;
                        }
                    }
                    break;
                }
                case STATE_UPSELL_NO_MEDIA: {
                    switch (event) {
                        default: {
                            return;
                        }
                        case OK: {
                            if (CrunchyrollApplication.getApp((Context)this.mActivity).getApplicationState().hasLoggedInUser()) {
                                this.mState = State.STATE_PAYMENT_NO_MEDIA;
                                EventBus.getDefault().post(new PopupNewFragmentEvent(PaymentFragment.newInstance(this.mMembershipInfoItem == null && b, PaymentFragment.Type.TYPE_ONLY_STEP)));
                                return;
                            }
                            this.mState = State.STATE_SIGNUP_NO_MEDIA;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(CreateAccountLoginPillTabsPopupFragment.newInstance(CreateAccountLoginPillTabsPopupFragment.Type.TYPE_BACKABLE, false)));
                            return;
                        }
                        case CANCEL:
                        case CLOSE:
                        case BACK:
                        case UPSELL_DISMISSED: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            return;
                        }
                    }
                    break;
                }
                case STATE_UPSELL_FEATURE: {
                    switch (event) {
                        default: {
                            return;
                        }
                        case CANCEL:
                        case CLOSE:
                        case BACK: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            return;
                        }
                        case CREATE_ACCOUNT: {
                            this.mState = State.STATE_SIGNUP_FEATURE;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(CreateAccountLoginPillTabsPopupFragment.newInstance(CreateAccountLoginPillTabsPopupFragment.Type.TYPE_BACKABLE_SINGLE_STEP, false)));
                            return;
                        }
                        case LOGIN: {
                            this.mState = State.STATE_SIGNUP_FEATURE;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(CreateAccountLoginPillTabsPopupFragment.newInstance(CreateAccountLoginPillTabsPopupFragment.Type.TYPE_BACKABLE_SINGLE_STEP, true)));
                            return;
                        }
                    }
                    break;
                }
                case STATE_SIGNUP_WITH_MEDIA: {
                    switch (event) {
                        default: {
                            return;
                        }
                        case CANCEL:
                        case CLOSE: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            return;
                        }
                        case ACCOUNT_CREATED:
                        case LOGGED_IN: {
                            this.mState = State.STATE_PAYMENT_WITH_MEDIA;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(PaymentFragment.newInstance(true, PaymentFragment.Type.TYPE_SECOND_STEP)));
                            return;
                        }
                        case BACK: {
                            this.mState = State.STATE_UPSELL_WITH_MEDIA;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(UpsellFragment.newInstance(this.mUpsellType)));
                            return;
                        }
                        case ALREADY_PREMIUM: {
                            alertDialog$Builder.setCancelable(false);
                            alertDialog$Builder.setMessage((CharSequence)LocalizedStrings.ALREADY_PREMIUM.get());
                            alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.WATCH_NOW.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                                public void onClick(final DialogInterface dialogInterface, final int n) {
                                    dialogInterface.dismiss();
                                    EventBus.getDefault().post(new PopupCloseEvent());
                                    PrepareToWatch.this.reInitFromUpsellSuccess(new BaseListener<Void>() {
                                        @Override
                                        public void onException(final Exception ex) {
                                            super.onException(ex);
                                        }
                                        
                                        @Override
                                        public void onFinally() {
                                            super.onFinally();
                                            Util.hideProgressBar(PrepareToWatch.this.mActivity);
                                        }
                                        
                                        @Override
                                        public void onPreExecute() {
                                            super.onPreExecute();
                                            Util.showProgressBar(PrepareToWatch.this.mActivity, PrepareToWatch.this.mActivity.getResources().getColor(2131558519));
                                        }
                                        
                                        @Override
                                        public void onSuccess(final Void void1) {
                                            PrepareToWatch.this.go(Event.NONE);
                                        }
                                    });
                                }
                            });
                            alertDialog$Builder.create().show();
                            return;
                        }
                        case FORGOT_PASSWORD: {
                            this.mState = State.STATE_FORGOT_PASSWORD_WITH_MEDIA;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(ForgotPasswordFragment.newInstance()));
                            return;
                        }
                    }
                    break;
                }
                case STATE_SIGNUP_NO_MEDIA: {
                    switch (event) {
                        default: {
                            return;
                        }
                        case CANCEL:
                        case CLOSE: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            return;
                        }
                        case ACCOUNT_CREATED:
                        case LOGGED_IN: {
                            this.mState = State.STATE_PAYMENT_NO_MEDIA;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(PaymentFragment.newInstance(true, PaymentFragment.Type.TYPE_SECOND_STEP)));
                            return;
                        }
                        case BACK: {
                            this.mState = State.STATE_UPSELL_NO_MEDIA;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(UpsellFragment.newInstance(this.mUpsellType)));
                            return;
                        }
                        case ALREADY_PREMIUM: {
                            alertDialog$Builder.setCancelable(false);
                            alertDialog$Builder.setMessage((CharSequence)LocalizedStrings.ALREADY_PREMIUM.get());
                            alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.OK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                                public void onClick(final DialogInterface dialogInterface, final int n) {
                                    dialogInterface.dismiss();
                                    EventBus.getDefault().post(new PopupCloseEvent());
                                    PrepareToWatch.this.goToMainActivity();
                                }
                            });
                            alertDialog$Builder.create().show();
                            return;
                        }
                        case FORGOT_PASSWORD: {
                            this.mState = State.STATE_FORGOT_PASSWORD_NO_MEDIA;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(ForgotPasswordFragment.newInstance()));
                            return;
                        }
                    }
                    break;
                }
                case STATE_SIGNUP_LOGIN: {
                    switch (event) {
                        default: {
                            return;
                        }
                        case CANCEL:
                        case CLOSE:
                        case BACK: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            return;
                        }
                        case ACCOUNT_CREATED: {
                            this.mState = State.STATE_SUCCESS_CREATE_ACCOUNT;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(SuccessFragment.newInstance(SuccessFragment.Type.TYPE_ACCOUNT_CREATED_UPSELL)));
                            return;
                        }
                        case LOGGED_IN:
                        case ALREADY_PREMIUM: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            this.goToMainActivity();
                            return;
                        }
                        case FORGOT_PASSWORD: {
                            this.mState = State.STATE_FORGOT_PASSWORD_LOGIN;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(ForgotPasswordFragment.newInstance()));
                            return;
                        }
                    }
                    break;
                }
                case STATE_SIGNUP_FEATURE: {
                    switch (event) {
                        default: {
                            return;
                        }
                        case CANCEL:
                        case CLOSE:
                        case LOGGED_IN:
                        case ALREADY_PREMIUM: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            return;
                        }
                        case ACCOUNT_CREATED: {
                            this.mState = State.STATE_SUCCESS_CREATE_ACCOUNT;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(SuccessFragment.newInstance(SuccessFragment.Type.TYPE_ACCOUNT_CREATED_UPSELL)));
                            return;
                        }
                        case BACK: {
                            this.mState = State.STATE_UPSELL_FEATURE;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(UpsellFeatureFragment.newInstance(UpsellFeatureFragment.Type.TYPE_QUEUE, this.mSeriesName)));
                            return;
                        }
                        case FORGOT_PASSWORD: {
                            this.mState = State.STATE_FORGOT_PASSWORD_FEATURE;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(ForgotPasswordFragment.newInstance()));
                            return;
                        }
                    }
                    break;
                }
                case STATE_FORGOT_PASSWORD_WITH_MEDIA: {
                    switch (event) {
                        default: {
                            return;
                        }
                        case OK: {
                            EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.PASSWORD_RESET_SENT.get()));
                        }
                        case BACK: {
                            this.mState = State.STATE_SIGNUP_WITH_MEDIA;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(CreateAccountLoginPillTabsPopupFragment.newInstance(CreateAccountLoginPillTabsPopupFragment.Type.TYPE_BACKABLE, false)));
                            return;
                        }
                        case CANCEL:
                        case CLOSE: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            return;
                        }
                    }
                    break;
                }
                case STATE_FORGOT_PASSWORD_NO_MEDIA: {
                    switch (event) {
                        default: {
                            return;
                        }
                        case OK: {
                            EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.PASSWORD_RESET_SENT.get()));
                        }
                        case BACK: {
                            this.mState = State.STATE_SIGNUP_NO_MEDIA;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(CreateAccountLoginPillTabsPopupFragment.newInstance(CreateAccountLoginPillTabsPopupFragment.Type.TYPE_BACKABLE, false)));
                            return;
                        }
                        case CANCEL:
                        case CLOSE: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            return;
                        }
                    }
                    break;
                }
                case STATE_FORGOT_PASSWORD_LOGIN: {
                    switch (event) {
                        default: {
                            return;
                        }
                        case OK: {
                            EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.PASSWORD_RESET_SENT.get()));
                        }
                        case BACK: {
                            this.mState = State.STATE_SIGNUP_LOGIN;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(CreateAccountLoginPillTabsPopupFragment.newInstance(CreateAccountLoginPillTabsPopupFragment.Type.TYPE_NOT_BACKABLE, true)));
                            return;
                        }
                        case CANCEL:
                        case CLOSE: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            return;
                        }
                    }
                    break;
                }
                case STATE_FORGOT_PASSWORD_FEATURE: {
                    switch (event) {
                        default: {
                            return;
                        }
                        case OK: {
                            EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.PASSWORD_RESET_SENT.get()));
                        }
                        case BACK: {
                            this.mState = State.STATE_SIGNUP_FEATURE;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(CreateAccountLoginPillTabsPopupFragment.newInstance(CreateAccountLoginPillTabsPopupFragment.Type.TYPE_BACKABLE_SINGLE_STEP, true)));
                            return;
                        }
                        case CANCEL:
                        case CLOSE: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            return;
                        }
                    }
                    break;
                }
                case STATE_PAYMENT_WITH_MEDIA: {
                    switch (event) {
                        default: {
                            return;
                        }
                        case OK: {
                            this.mState = State.STATE_SUCCESS_WITH_MEDIA;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(SuccessFragment.newInstance(SuccessFragment.Type.TYPE_PREMIUM_NO_UPSELL)));
                            return;
                        }
                        case CANCEL:
                        case CLOSE: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            return;
                        }
                        case BACK: {
                            this.mState = State.STATE_UPSELL_WITH_MEDIA;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(UpsellFragment.newInstance(this.mUpsellType)));
                            return;
                        }
                        case NOT_QUALIFIED: {
                            alertDialog$Builder.setMessage((CharSequence)String.format(LocalizedStrings.UPGRADE_NOT_QUALIFIED_FOR_FREE_TRIAL.get(), s));
                            alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.UPGRADE.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                                public void onClick(final DialogInterface dialogInterface, final int n) {
                                    EventBus.getDefault().post(new Upsell.ContinueUpgradeEvent());
                                }
                            });
                            alertDialog$Builder.setNegativeButton((CharSequence)LocalizedStrings.BACK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                                public void onClick(final DialogInterface dialogInterface, final int n) {
                                    EventBus.getDefault().post(new PopupCloseEvent());
                                }
                            });
                            alertDialog$Builder.create().show();
                            return;
                        }
                        case CARD_ALREADY_USED: {
                            alertDialog$Builder.setMessage((CharSequence)String.format(LocalizedStrings.CREDIT_CARD_ALREADY_USED_FOR_FREE_TRIAL.get(), s));
                            alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.UPGRADE.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                                public void onClick(final DialogInterface dialogInterface, final int n) {
                                    EventBus.getDefault().post(new Upsell.ContinueUpgradeEvent());
                                }
                            });
                            alertDialog$Builder.setNegativeButton((CharSequence)LocalizedStrings.BACK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                                public void onClick(final DialogInterface dialogInterface, final int n) {
                                    EventBus.getDefault().post(new PopupCloseEvent());
                                }
                            });
                            alertDialog$Builder.create().show();
                            return;
                        }
                    }
                    break;
                }
                case STATE_PAYMENT_NO_MEDIA: {
                    switch (event) {
                        default: {
                            return;
                        }
                        case OK: {
                            this.mState = State.STATE_SUCCESS_NO_MEDIA;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(SuccessFragment.newInstance(SuccessFragment.Type.TYPE_PREMIUM_NO_UPSELL)));
                            return;
                        }
                        case CANCEL:
                        case CLOSE: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            return;
                        }
                        case BACK: {
                            this.mState = State.STATE_UPSELL_NO_MEDIA;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(UpsellFragment.newInstance(this.mUpsellType)));
                            return;
                        }
                        case NOT_QUALIFIED: {
                            alertDialog$Builder.setMessage((CharSequence)String.format(LocalizedStrings.UPGRADE_NOT_QUALIFIED_FOR_FREE_TRIAL.get(), s));
                            alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.UPGRADE.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                                public void onClick(final DialogInterface dialogInterface, final int n) {
                                    EventBus.getDefault().post(new Upsell.ContinueUpgradeEvent());
                                }
                            });
                            alertDialog$Builder.setNegativeButton((CharSequence)LocalizedStrings.BACK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                                public void onClick(final DialogInterface dialogInterface, final int n) {
                                    EventBus.getDefault().post(new PopupCloseEvent());
                                }
                            });
                            alertDialog$Builder.create().show();
                            return;
                        }
                        case CARD_ALREADY_USED: {
                            alertDialog$Builder.setMessage((CharSequence)String.format(LocalizedStrings.CREDIT_CARD_ALREADY_USED_FOR_FREE_TRIAL.get(), s));
                            alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.UPGRADE.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                                public void onClick(final DialogInterface dialogInterface, final int n) {
                                    EventBus.getDefault().post(new Upsell.ContinueUpgradeEvent());
                                }
                            });
                            alertDialog$Builder.setNegativeButton((CharSequence)LocalizedStrings.BACK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                                public void onClick(final DialogInterface dialogInterface, final int n) {
                                    EventBus.getDefault().post(new PopupCloseEvent());
                                }
                            });
                            alertDialog$Builder.create().show();
                            return;
                        }
                    }
                    break;
                }
                case STATE_SUCCESS_WITH_MEDIA: {
                    switch (event) {
                        default: {
                            return;
                        }
                        case OK:
                        case CLOSE:
                        case BACK: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            this.reInitFromUpsellSuccess(new BaseListener<Void>() {
                                @Override
                                public void onException(final Exception ex) {
                                    super.onException(ex);
                                }
                                
                                @Override
                                public void onSuccess(final Void void1) {
                                    PrepareToWatch.this.go(Event.NONE);
                                }
                            });
                            return;
                        }
                    }
                    break;
                }
                case STATE_SUCCESS_NO_MEDIA: {
                    switch (event) {
                        default: {
                            return;
                        }
                        case OK:
                        case CLOSE:
                        case BACK: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            this.goToMainActivity();
                            return;
                        }
                    }
                    break;
                }
                case STATE_SUCCESS_CREATE_ACCOUNT: {
                    switch (event) {
                        default: {
                            return;
                        }
                        case OK:
                        case CLOSE:
                        case BACK: {
                            EventBus.getDefault().post(new PopupCloseEvent());
                            this.goToMainActivity();
                            return;
                        }
                        case LEARN_MORE: {
                            this.mState = State.STATE_UPSELL_NO_MEDIA;
                            EventBus.getDefault().post(new PopupNewFragmentEvent(UpsellFragment.newInstance(UpsellFragment.Type.TYPE_NONE)));
                            return;
                        }
                    }
                    break;
                }
            }
        }
    }
    
    public void invalidate() {
        this.mIsValid = false;
    }
    
    public boolean isLoading() {
        return this.mIsLoading;
    }
    
    public boolean isValid() {
        return this.mIsValid || this.mIsReInit;
    }
    
    public void prepare(final BaseListener<Void> mListener) {
        this.mListener = mListener;
        final CrunchyrollApplication app = CrunchyrollApplication.getApp((Context)this.mActivity);
        if (app.getApplicationState().hasLoggedInUser()) {
            this.mIsLoading = true;
            ApiManager.getInstance(this.mActivity).authenticate(app.getApplicationState().getAuth().get(), null, new BaseListener<AuthenticateItem>() {
                @Override
                public void onException(final Exception ex) {
                    super.onException(ex);
                    PrepareToWatch.this.mState = State.STATE_ERROR;
                    PrepareToWatch.this.mListener.onException(ex);
                    PrepareToWatch.this.mListener.onFinally();
                    PrepareToWatch.this.mHandler.postDelayed((Runnable)PrepareToWatch.this.mSetNotLoadingRunnable, 500L);
                }
                
                @Override
                public void onPreExecute() {
                    super.onPreExecute();
                    PrepareToWatch.this.mListener.onPreExecute();
                }
                
                @Override
                public void onSuccess(final AuthenticateItem authenticateItem) {
                    app.getApplicationState().setLoggedInUser(authenticateItem.getUser());
                    PrepareToWatch.this.getFreeTrialInfo(PrepareToWatch.this.hasInitMedia, false, false);
                }
            });
            return;
        }
        this.getFreeTrialInfo(this.hasInitMedia, false, true);
    }
    
    public void reInitFromUpsellSuccess(final BaseListener<Void> mListener) {
        this.mIsReInit = true;
        (this.mListener = mListener).onPreExecute();
        if (this.hasInitMedia) {
            MediaManager.getInstance().reInit();
            this.episodeInfoSetup();
            return;
        }
        this.mListener.onSuccess(null);
    }
    
    public void refreshFreeTrialInfo(final BaseListener<Void> mListener) {
        this.mListener = mListener;
        this.getFreeTrialInfo(false, true, true);
    }
    
    public void refreshMembershipTrialInfo(final BaseListener<Void> mListener) {
        (this.mListener = mListener).onPreExecute();
        this.getMembershipInfo(false, true);
    }
    
    public void setActivity(final Activity mActivity) {
        this.mActivity = mActivity;
    }
    
    public enum Event
    {
        ACCOUNT_CREATED, 
        ALREADY_PREMIUM, 
        BACK, 
        CANCEL, 
        CARD_ALREADY_USED, 
        CLOSE, 
        CREATE_ACCOUNT, 
        FORGOT_PASSWORD, 
        LEARN_MORE, 
        LOGGED_IN, 
        LOGIN, 
        NONE, 
        NOT_QUALIFIED, 
        OK, 
        UPSELL_DISMISSED;
    }
    
    private class SetNotLoadingRunnable implements Runnable
    {
        @Override
        public void run() {
            PrepareToWatch.this.mIsLoading = false;
        }
    }
    
    public enum State
    {
        STATE_ERROR, 
        STATE_FORGOT_PASSWORD_FEATURE, 
        STATE_FORGOT_PASSWORD_LOGIN, 
        STATE_FORGOT_PASSWORD_NO_MEDIA, 
        STATE_FORGOT_PASSWORD_WITH_MEDIA, 
        STATE_PAYMENT_NO_MEDIA, 
        STATE_PAYMENT_WITH_MEDIA, 
        STATE_PREPARED_FEATURE, 
        STATE_PREPARED_LOGIN, 
        STATE_PREPARED_NO_MEDIA, 
        STATE_PREPARED_SIGNUP, 
        STATE_PREPARED_WITH_MEDIA, 
        STATE_SIGNUP_FEATURE, 
        STATE_SIGNUP_LOGIN, 
        STATE_SIGNUP_NO_MEDIA, 
        STATE_SIGNUP_WITH_MEDIA, 
        STATE_SUCCESS_CREATE_ACCOUNT, 
        STATE_SUCCESS_NO_MEDIA, 
        STATE_SUCCESS_WITH_MEDIA, 
        STATE_UNPREPARED, 
        STATE_UPSELL_FEATURE, 
        STATE_UPSELL_NO_MEDIA, 
        STATE_UPSELL_WITH_MEDIA, 
        STATE_WATCHING;
    }
    
    public enum Type
    {
        PREPARE_LOGIN, 
        PREPARE_MEDIA, 
        PREPARE_MEDIA_LIST, 
        PREPARE_SIGNUP, 
        PREPARE_UPSELL_FEATURE, 
        PREPARE_UPSELL_NONE;
    }
}
