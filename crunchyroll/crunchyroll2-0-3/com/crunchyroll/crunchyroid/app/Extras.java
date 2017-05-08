// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.app;

import android.net.Uri;
import com.google.common.reflect.TypeToken;
import java.util.List;
import java.io.Serializable;
import android.os.Bundle;
import com.google.common.base.Optional;
import android.content.Intent;

public final class Extras
{
    public static final String APP_ENTRY_FROM = "intent_from";
    public static final String AUTO_PLAYBACK = "autoPlayback";
    public static final String CATEGORIES = "categories";
    public static final String COLLECTIONS = "collections";
    public static final String CONTINUE_WATCHING = "continueWatching";
    public static final String CURRENT_INDEX = "currentIndex";
    public static final String DEEPLINK_CREATE_ACCOUNT = "create_account";
    public static final String DEEPLINK_FILTER = "filter";
    public static final String DEEPLINK_FILTER_ALPHA = "alpha";
    public static final String DEEPLINK_FILTER_ANIME = "anime";
    public static final String DEEPLINK_FILTER_DRAMA = "drama";
    public static final String DEEPLINK_FILTER_GENRES = "genres";
    public static final String DEEPLINK_FILTER_POPULAR = "popular";
    public static final String DEEPLINK_FILTER_SEASONS = "seasons";
    public static final String DEEPLINK_HISTORY = "history";
    public static final String DEEPLINK_MEDIA = "media";
    public static final String DEEPLINK_PLAY_MEDIA = "playmedia";
    public static final String DEEPLINK_QUEUE = "queue";
    public static final String DEEPLINK_SCHEME_CRUNCHYROLL = "crunchyroll";
    public static final String DEEPLINK_SERIES = "series";
    public static final String DEEPLINK_THIS_SEASON = "this_season";
    public static final String DEEPLINK_UPDATED = "updated";
    public static final String DEEPLINK_UPSELL = "upsell";
    public static final String DO_SCROLL_ANIMATION = "doScrollAnimation";
    public static final String DURATION = "duration";
    public static final String EPISODE_INFO = "episodeInfo";
    public static final String EXCEPTION = "exception";
    public static final String FILTER = "filter";
    public static final String FRAGMENT_CONTENTS_ID = "fragmentContentsId";
    public static final String FREE_TRIAL_INFO = "freeTrialInfo";
    public static final String HAS_FREE_TRIAL_INFO = "hasFreeTrialInfo";
    public static final String HAS_POPUP = "hasPopup";
    public static final String HLS = "hls";
    public static final String IS_ALLOW_GENRE_SELECTION = "isAllowGenreSelection";
    public static final String IS_ALL_ACCESS = "isAllAccess";
    public static final String IS_EXHAUSTED = "isExhausted";
    public static final String IS_FREE_TRIAL = "isFreeTrial";
    public static final String IS_PLAYING = "isPlaying";
    public static final String IS_PREPARE_TO_WATCH_ON_START = "isPrepareToWatchOnStart";
    public static final String IS_SELF_TRACK = "isSelfTrack";
    public static final String IS_SHOW_LOGIN = "showLogin";
    public static final String IS_SHOW_MEDIA_INFO_ON_START = "isShowMediaInfoOnStart";
    public static final String IS_SHOW_TITLE = "isShowTitle";
    public static final String LABEL = "label";
    public static final String LIMIT = "limit";
    public static final String LIST = "list";
    public static final String MAIN_TYPE = "mainType";
    public static final String MEDIA = "media";
    public static final String MEDIA_ID = "mediaId";
    public static final String MEDIA_TYPE = "mediaType";
    public static final String MEMBERSHIP_INFO = "membrshipInfo";
    public static final String MESSAGE_RES_ID = "messageResId";
    public static final String MODE = "mode";
    public static final String NEW_USER = "newUser";
    public static final String OFFSET = "offset";
    public static final String PAYMENT_TYPE = "paymentType";
    public static final String PLAYHEAD = "playhead";
    public static final String POSITION = "position";
    public static final String PREROLL = "preroll";
    public static final String PRICE_AND_SYMBOL = "priceAndSymbol";
    public static final String QUEUE_LIST = "queue_list";
    public static final String RECURRING_PRICE = "recurringPrice";
    public static final String RETRY_VIDEO = "retryVideo";
    public static final String SEARCH_STRING = "searchString";
    public static final String SEASONS = "seasons";
    public static final String SERIES = "series";
    public static final String SERIES_ID = "seriesId";
    public static final String SERIES_INFO_INDEX = "seriesInfoIndex";
    public static final String SERIES_INFO_WITH_MEDIA = "seriesInfoWithMedia";
    public static final String SERIES_NAME = "seriesName";
    public static final String SHOULD_LOAD_MORE = "shouldLoadMore";
    public static final String SHOULD_SHOW_ADS = "shouldShowAds";
    public static final String SIGNUP_TYPE = "signupType";
    public static final String SKIP_RESUME_DIALOG = "skipResumeDialog";
    public static final String SKU = "sku";
    public static final String SORT_TYPE = "sortType";
    public static final String SPAN_TYPE = "spanType";
    public static final String START_TYPE = "startType";
    public static final String SUCCESS_TYPE = "successType";
    public static final String TAG = "tag";
    public static final String TITLE = "title";
    public static final String TRIAL_DURATION = "trialDuration";
    public static final String TRIAL_SPAN = "trialSpan";
    public static final String TYPE = "type";
    public static final String TYPE_PARAMETER = "typeParameter";
    public static final String UPSELL_TYPE = "upsellType";
    public static final String VIEW_PAGER_ID = "viewPagerId";
    
    public static Optional<Boolean> getBoolean(final Intent intent, final String s) {
        if (intent != null && intent.hasExtra(s)) {
            return Optional.fromNullable(intent.getExtras().getBoolean(s));
        }
        return Optional.absent();
    }
    
    public static Optional<Boolean> getBoolean(final Bundle bundle, final String s) {
        if (bundle != null && bundle.containsKey(s)) {
            return Optional.fromNullable(bundle.getBoolean(s));
        }
        return Optional.absent();
    }
    
    public static Optional<Integer> getInt(final Intent intent, final String s) {
        if (intent != null && intent.hasExtra(s)) {
            return Optional.fromNullable(intent.getExtras().getInt(s));
        }
        return Optional.absent();
    }
    
    public static Optional<Integer> getInt(final Bundle bundle, final String s) {
        if (bundle != null && bundle.containsKey(s)) {
            return Optional.fromNullable(bundle.getInt(s));
        }
        return Optional.absent();
    }
    
    public static <T extends Serializable> Optional<List<T>> getList(final Intent intent, final String s, final Class<T> clazz) {
        return getSerializable(intent, s, (TypeToken<List<T>>)new TypeToken<List<T>>() {});
    }
    
    public static <T extends Serializable> Optional<List<T>> getList(final Bundle bundle, final String s, final TypeToken<T> typeToken) {
        return getSerializable(bundle, s, (TypeToken<List<T>>)new TypeToken<List<T>>() {});
    }
    
    public static <T extends Serializable> Optional<List<T>> getList(final Bundle bundle, final String s, final Class<T> clazz) {
        return getSerializable(bundle, s, (TypeToken<List<T>>)new TypeToken<List<T>>() {});
    }
    
    public static Optional<Long> getLong(final Intent intent, final String s) {
        if (intent != null && intent.hasExtra(s)) {
            return Optional.fromNullable(intent.getExtras().getLong(s));
        }
        return Optional.absent();
    }
    
    public static Optional<Long> getLong(final Bundle bundle, final String s) {
        if (bundle != null && bundle.containsKey(s)) {
            return Optional.fromNullable(bundle.getLong(s));
        }
        return Optional.absent();
    }
    
    public static <T> Optional<T> getSerializable(final Intent intent, final String s, final TypeToken<T> typeToken) {
        if (intent != null && intent.hasExtra(s)) {
            return Optional.fromNullable((T)intent.getSerializableExtra(s));
        }
        return Optional.absent();
    }
    
    public static <T> Optional<T> getSerializable(final Intent intent, final String s, final Class<T> clazz) {
        if (intent != null && intent.hasExtra(s)) {
            return Optional.fromNullable((T)intent.getSerializableExtra(s));
        }
        return Optional.absent();
    }
    
    public static <T> Optional<T> getSerializable(final Bundle bundle, final String s, final TypeToken<T> typeToken) {
        if (bundle != null && bundle.containsKey(s)) {
            return Optional.fromNullable((T)bundle.getSerializable(s));
        }
        return Optional.absent();
    }
    
    public static <T> Optional<T> getSerializable(final Bundle bundle, final String s, final Class<T> clazz) {
        if (bundle != null && bundle.containsKey(s)) {
            return Optional.fromNullable((T)bundle.getSerializable(s));
        }
        return Optional.absent();
    }
    
    public static Optional<String> getString(final Intent intent, final String s) {
        if (intent != null && intent.hasExtra(s)) {
            return Optional.fromNullable(intent.getExtras().getString(s));
        }
        return Optional.absent();
    }
    
    public static Optional<String> getString(final Bundle bundle, final String s) {
        if (bundle != null && bundle.containsKey(s)) {
            return Optional.fromNullable(bundle.getString(s));
        }
        return Optional.absent();
    }
    
    public static String getUriMethod(final Uri uri) {
        if (uri == null) {
            return "";
        }
        return uri.getHost();
    }
    
    public static String getUriValue(final Uri uri, final int n) throws IndexOutOfBoundsException {
        if (uri == null) {
            return "";
        }
        final List pathSegments = uri.getPathSegments();
        if (pathSegments.size() == 0) {
            return "";
        }
        return pathSegments.get(n);
    }
    
    public static void putBoolean(final Intent intent, final String s, final boolean b) {
        intent.putExtra(s, b);
    }
    
    public static void putBoolean(final Bundle bundle, final String s, final boolean b) {
        bundle.putBoolean(s, b);
    }
    
    public static void putInt(final Intent intent, final String s, final Integer n) {
        if (n != null) {
            intent.putExtra(s, (Serializable)n);
        }
    }
    
    public static void putInt(final Bundle bundle, final String s, final Integer n) {
        if (n != null) {
            bundle.putInt(s, (int)n);
        }
    }
    
    public static <T extends Serializable> void putList(final Intent intent, final String s, final List<T> list) {
        if (list != null) {
            putSerializable(intent, s, (Serializable)list);
        }
    }
    
    public static <T extends Serializable> void putList(final Bundle bundle, final String s, final List<T> list) {
        if (list != null) {
            putSerializable(bundle, s, (Serializable)list);
        }
    }
    
    public static void putLong(final Intent intent, final String s, final Long n) {
        if (n != null) {
            intent.putExtra(s, (Serializable)n);
        }
    }
    
    public static void putLong(final Bundle bundle, final String s, final Long n) {
        if (n != null) {
            bundle.putLong(s, (long)n);
        }
    }
    
    public static <T extends Serializable> void putSerializable(final Intent intent, final String s, final T t) {
        if (t != null) {
            intent.putExtra(s, (Serializable)t);
        }
    }
    
    public static <T extends Serializable> void putSerializable(final Bundle bundle, final String s, final T t) {
        if (t != null) {
            bundle.putSerializable(s, (Serializable)t);
        }
    }
    
    public static void putString(final Intent intent, final String s, final String s2) {
        if (s2 != null) {
            intent.putExtra(s, s2);
        }
    }
    
    public static void putString(final Bundle bundle, final String s, final String s2) {
        if (s2 != null) {
            bundle.putString(s, s2);
        }
    }
}
