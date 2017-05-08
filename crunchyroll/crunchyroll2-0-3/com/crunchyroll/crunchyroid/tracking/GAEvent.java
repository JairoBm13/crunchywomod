// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.tracking;

import android.os.Build;
import android.os.Build$VERSION;
import java.util.Locale;
import junit.framework.Assert;
import android.content.Context;
import com.crunchyroll.android.util.LoggerFactory;
import com.crunchyroll.android.util.Logger;

public enum GAEvent
{
    APP_SHUTDOWN(GACategory.LIFECYCLE, "app_shutdown"), 
    APP_START((GACategory)null, (String)null), 
    AUTH_TOKEN_EXPIRED(GACategory.USER, "logout"), 
    AUTO_NEXT_VIDEO(GACategory.VIDEO, "auto-play"), 
    BROWSE_CATEGORY(GACategory.BROWSE, "browse_category"), 
    BROWSE_GENRE(GACategory.SERIES_OPTION, "browse-genre"), 
    BROWSE_MEDIA(GACategory.BROWSE, "browse_series"), 
    BROWSE_SERIES(GACategory.BROWSE, "browse_series"), 
    CARD_SIZE(GACategory.SETTINGS, "card-size"), 
    CHROMECAST_FAST_FORWARD(GACategory.CHROMECAST, "15sec-forward"), 
    CHROMECAST_REWIND(GACategory.CHROMECAST, "15sec-rewind"), 
    CHROMECAST_START_PLAYBACK(GACategory.CHROMECAST, "play"), 
    CONTACT_US(GACategory.SETTINGS, "contact-us"), 
    CREATE_ACCOUNT_ATTEMPT(GACategory.USER, "create-account-attempt"), 
    CREATE_ACCOUNT_EXIT(GACategory.USER, "create-account-exit"), 
    CREATE_ACCOUNT_FAIL(GACategory.USER, "create-account-fail"), 
    CREATE_ACCOUNT_LEARN_PREMIUM(GACategory.USER, "create-account-learn-premium"), 
    CREATE_ACCOUNT_START_WATCHING(GACategory.USER, "create-account-start-watching"), 
    CREATE_ACCOUNT_SUCCESS(GACategory.USER, "create-account-success"), 
    EPISODE_INFO(GACategory.SERIES_OPTION, "episode-info"), 
    EPISODE_INFO_PLAY(GACategory.SERIES_OPTION, "episode-info-play"), 
    EPISODE_SELECT(GACategory.SERIES_OPTION, "episode-select"), 
    FORGOT_PASSWORD(GACategory.USER, "forgot_password"), 
    FORGOT_PASSWORD_SENT(GACategory.USER, "forgot_password_sent"), 
    FREE_VIDEO_UPSELL_BACK(GACategory.SIGN_UP, "free-video-upsell-back"), 
    FREE_VIDEO_UPSELL_NOTHANKS(GACategory.SIGN_UP, "free-video-upsell-nothanks"), 
    FREE_VIDEO_UPSELL_SIGNUP(GACategory.SIGN_UP, "free-video-upsell-signup"), 
    HELP(GACategory.SETTINGS, "help"), 
    LOAD_IMAGES(GACategory.SETTINGS, "load-images"), 
    LOGIN_ATTEMPT(GACategory.USER, "login_attempt"), 
    LOGIN_FAILED(GACategory.USER, "login_fail"), 
    LOGIN_SUCCESS(GACategory.USER, "login_success"), 
    LOGOUT(GACategory.USER, "logout"), 
    MAIN_MENU(GACategory.BROWSE, "main-menu"), 
    NO_FT_UPGRADE_FAIL(GACategory.SIGN_UP, "no-ft-upgrade-fail"), 
    NO_FT_UPGRADE_SUCCESS(GACategory.SIGN_UP, "no-ft-upgrade-success"), 
    OTHER_APPS(GACategory.SETTINGS, "other_apps"), 
    PLAY_NEXT_CHROMECAST(GACategory.CHROMECAST, "play-next"), 
    PLAY_NEXT_VIDEO(GACategory.VIDEO, "play-next"), 
    PREFERRED_LANGUAGE(GACategory.SETTINGS, "preferred-language"), 
    PREMIUM_VIDEO_ANON_UPSELL_BACK(GACategory.SIGN_UP, "premium-video-anon-upsell-back"), 
    PREMIUM_VIDEO_ANON_UPSELL_STARTFREETRIAL(GACategory.SIGN_UP, "premium-video-anon-upsell-startfreetrial"), 
    PREMIUM_VIDEO_FREE_UPSELL_BACK(GACategory.SIGN_UP, "premium-video-free-upsell-back"), 
    PREMIUM_VIDEO_FREE_UPSELL_STARTFREETRIAL(GACategory.SIGN_UP, "premium-video-free-upsell-startfreetrial"), 
    PREMIUM_VIDEO_UPSELL_UPGRADE_BACK(GACategory.SIGN_UP, "premium-video-upsell-upgrade-back"), 
    PREMIUM_VIDEO_UPSELL_UPGRADE_UPGRADENOW(GACategory.SIGN_UP, "premium-video-upsell-upgrade-upgradenow"), 
    PRIVACY_POLICY(GACategory.SETTINGS, "privacy-policy"), 
    QUEUE_CREATE_ACCOUNT(GACategory.USER, "queue-create-account"), 
    QUEUE_LOGIN(GACategory.USER, "queue-login"), 
    QUEUE_REFRESH(GACategory.USER, "queue_refresh"), 
    SEARCH_RESULT_SELECTION(GACategory.SEARCH), 
    SEASONS_SELECT(GACategory.SERIES_OPTION, "seasons-select"), 
    SERIES_OPTION_ADD_TO_QUEUE(GACategory.SERIES_OPTION, "add-to-queue"), 
    SERIES_OPTION_REMOVE_FROM_QUEUE(GACategory.SERIES_OPTION, "remove-from-queue"), 
    SESSION_START_FAILED(GACategory.USER, "session_start_failed"), 
    SESSION_START_SUCCESS(GACategory.USER, "session_start_success"), 
    SHARE_EPISODE(GACategory.SOCIAL_SHARING, "share-episode"), 
    SHARE_SERIES(GACategory.SOCIAL_SHARING, "share-series"), 
    SIDE_MENU(GACategory.SETTINGS, "side-menu"), 
    SIGN_UP_CREATE_ACCOUNT(GACategory.SIGN_UP, "create-account"), 
    SIGN_UP_CREATE_ACCOUNT_FAILURE(GACategory.SIGN_UP, "create-account-failure"), 
    SIGN_UP_CREATE_ACCOUNT_SUCCESS(GACategory.SIGN_UP, "create-account-success"), 
    SIGN_UP_LOGIN(GACategory.SIGN_UP, "login"), 
    SIGN_UP_LOGIN_FAILURE(GACategory.SIGN_UP, "login-failure"), 
    SIGN_UP_LOGIN_SUCCESS(GACategory.SIGN_UP, "login-success"), 
    SIGN_UP_PAYMENT(GACategory.SIGN_UP, "payment"), 
    SORT_EPISODE(GACategory.SERIES_OPTION, "sort-episode"), 
    UPGRADE_FAIL(GACategory.SIGN_UP, "upgrade-fail"), 
    UPGRADE_SUCCESS(GACategory.SIGN_UP, "upgrade-success"), 
    UPGRADE_TO_PREMIUM(GACategory.SETTINGS, "upgrade-to-premium"), 
    UPSELL_EXISTING_USER(GACategory.USER, "upgrade"), 
    UPSELL_SHOWN(GACategory.USER, "upsell_shown"), 
    VIDEO_DATA(GACategory.VIDEO, "video_data"), 
    VIDEO_FORWARD(GACategory.VIDEO, "10sec-forward"), 
    VIDEO_QUALITY(GACategory.SETTINGS, "video-quality"), 
    VIDEO_REWIND(GACategory.VIDEO, "10sec-rewind"), 
    VIDEO_START_PLAYBACK(GACategory.VIDEO, "play");
    
    private final Logger log;
    private final String mAction;
    private final GACategory mCategory;
    
    private GAEvent(final GACategory gaCategory) {
        this(gaCategory, null);
    }
    
    private GAEvent(final GACategory mCategory, final String mAction) {
        this.log = LoggerFactory.getLogger(Tracker.class);
        assert mCategory != null;
        this.mCategory = mCategory;
        this.mAction = mAction;
    }
    
    public void track(final Context context) {
        this.track(context, this.mAction, null);
    }
    
    public void track(final Context context, final String s) {
        this.track(context, this.mAction, s, null);
    }
    
    public void track(final Context context, final String s, final String s2) {
        this.track(context, s, s2, null);
    }
    
    public void track(final Context context, final String s, final String s2, final Long n) {
        switch (this) {
            default: {
                Assert.fail("Unhandled tracking case " + this.name());
            }
            case APP_START: {
                if (s2 == null) {
                    this.log.warn("Warning: APP_START missing version parameter", new Object[0]);
                }
                GATracker.trackEvent(context, GACategory.SDK.categoryName, String.format(null, "SDK: %d, release: %s", Build$VERSION.SDK_INT, Build$VERSION.RELEASE), s2, n);
                GATracker.trackEvent(context, GACategory.DEVICE.categoryName, String.format("%s, %s", Build.MANUFACTURER, Build.MODEL), s2, n);
                GATracker.trackEvent(context, GACategory.LIFECYCLE.categoryName, "app_launch", String.format("launch_version:%s", s2), 0L);
            }
            case BROWSE_CATEGORY:
            case BROWSE_MEDIA:
            case BROWSE_SERIES:
            case VIDEO_DATA:
            case VIDEO_START_PLAYBACK:
            case CHROMECAST_START_PLAYBACK:
            case SEARCH_RESULT_SELECTION: {
                if (s2 == null) {
                    this.log.warn("Warning: %s missing parameter", this.name());
                }
                GATracker.trackEvent(context, this.mCategory.categoryName, s, s2, 0L);
            }
            case APP_SHUTDOWN:
            case LOGIN_ATTEMPT:
            case LOGIN_FAILED:
            case LOGIN_SUCCESS:
            case LOGOUT:
            case QUEUE_REFRESH:
            case SESSION_START_FAILED:
            case SESSION_START_SUCCESS:
            case UPSELL_SHOWN: {
                GATracker.trackEvent(context, this.mCategory.categoryName, s, null, null);
            }
            case CREATE_ACCOUNT_ATTEMPT:
            case CREATE_ACCOUNT_SUCCESS:
            case CREATE_ACCOUNT_FAIL:
            case CREATE_ACCOUNT_START_WATCHING:
            case CREATE_ACCOUNT_LEARN_PREMIUM:
            case CREATE_ACCOUNT_EXIT:
            case AUTH_TOKEN_EXPIRED:
            case OTHER_APPS:
            case VIDEO_REWIND:
            case VIDEO_FORWARD:
            case CHROMECAST_REWIND:
            case CHROMECAST_FAST_FORWARD:
            case SERIES_OPTION_ADD_TO_QUEUE:
            case SERIES_OPTION_REMOVE_FROM_QUEUE:
            case EPISODE_SELECT:
            case BROWSE_GENRE:
            case SORT_EPISODE:
            case SEASONS_SELECT:
            case EPISODE_INFO:
            case EPISODE_INFO_PLAY:
            case AUTO_NEXT_VIDEO:
            case PLAY_NEXT_VIDEO:
            case PLAY_NEXT_CHROMECAST:
            case QUEUE_CREATE_ACCOUNT:
            case QUEUE_LOGIN:
            case FORGOT_PASSWORD:
            case FORGOT_PASSWORD_SENT:
            case SIGN_UP_CREATE_ACCOUNT:
            case SIGN_UP_LOGIN:
            case SIGN_UP_CREATE_ACCOUNT_SUCCESS:
            case SIGN_UP_CREATE_ACCOUNT_FAILURE:
            case SIGN_UP_LOGIN_SUCCESS:
            case SIGN_UP_LOGIN_FAILURE:
            case VIDEO_QUALITY:
            case CARD_SIZE:
            case SIDE_MENU:
            case MAIN_MENU:
            case UPGRADE_TO_PREMIUM:
            case PREFERRED_LANGUAGE:
            case LOAD_IMAGES:
            case HELP:
            case PRIVACY_POLICY:
            case CONTACT_US: {
                GATracker.trackEvent(context, this.mCategory.categoryName, s, s2, null);
            }
            case UPGRADE_SUCCESS:
            case UPGRADE_FAIL:
            case NO_FT_UPGRADE_FAIL:
            case NO_FT_UPGRADE_SUCCESS:
            case FREE_VIDEO_UPSELL_BACK:
            case FREE_VIDEO_UPSELL_NOTHANKS:
            case FREE_VIDEO_UPSELL_SIGNUP:
            case PREMIUM_VIDEO_ANON_UPSELL_BACK:
            case PREMIUM_VIDEO_ANON_UPSELL_STARTFREETRIAL:
            case PREMIUM_VIDEO_FREE_UPSELL_BACK:
            case PREMIUM_VIDEO_FREE_UPSELL_STARTFREETRIAL:
            case PREMIUM_VIDEO_UPSELL_UPGRADE_BACK:
            case PREMIUM_VIDEO_UPSELL_UPGRADE_UPGRADENOW:
            case SHARE_EPISODE:
            case SHARE_SERIES:
            case UPSELL_EXISTING_USER:
            case SIGN_UP_PAYMENT: {
                GATracker.trackEvent(context, this.mCategory.categoryName, this.mAction, s2, null);
            }
        }
    }
    
    private enum GACategory
    {
        ADS_IMA_FILL("ads_ima_fill"), 
        ADS_TREMOR_FILL("ads_tremor_fill"), 
        ADS_VAST_FILL("ads_vast_fill"), 
        BROWSE("browse"), 
        CHROMECAST("chromecast"), 
        DEVICE("device"), 
        LIFECYCLE("lifecycle"), 
        SDK("sdk"), 
        SEARCH("search"), 
        SERIES_OPTION("series-option"), 
        SETTINGS("settings"), 
        SIGN_UP("sign-up"), 
        SOCIAL_SHARING("social-sharing"), 
        USER("user"), 
        VIDEO("video");
        
        final String categoryName;
        
        private GACategory(final String categoryName) {
            this.categoryName = categoryName;
        }
    }
}
