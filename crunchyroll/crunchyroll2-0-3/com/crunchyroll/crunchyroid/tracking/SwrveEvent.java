// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.tracking;

import java.util.Map;
import com.swrve.sdk.SwrveSDKBase;
import java.util.HashMap;

public class SwrveEvent
{
    public static final String ACCOUNT_CREATE_SUCCESS = "account-create-success";
    public static final String ADD_TO_QUEUE = "add-to-queue";
    public static final String ADS = "ads";
    public static final String AD_SERVED = "ad-served";
    public static final String ANIME = "anime";
    public static final String ANIME_BROWSE = "anime-browse";
    public static final String ANIME_EPISODE_INFO = "anime-episode-info";
    public static final String ANIME_PLAYS = "anime-plays";
    public static final String ANIME_SERIES = "anime-series";
    public static final String ANIME_SERIES_GENRE = "anime-series-genre";
    public static final String ANIME_SORT_ALPHA = "anime-sort-alpha";
    public static final String ANIME_SORT_GENRES = "anime-sort-genres";
    public static final String ANIME_SORT_MENU = "anime-sort-menu";
    public static final String ANIME_SORT_MENU_GENRES = "anime-sort-menu-genres";
    public static final String ANIME_SORT_POPULAR = "anime-sort-popular";
    public static final String ANIME_SORT_SEASONS = "anime-sort-seasons";
    public static final String ANIME_VIDEO = "anime-video";
    public static final String ATTEMPT = "attempt";
    public static final String AUTO = "auto";
    public static final String AUTO_PLAY = "auto-play";
    public static final String BACK = "back";
    public static final String BROWSE_GENRE = "browse-genre";
    public static final String CANCEL = "cancel";
    public static final String CHECKED = "checked";
    public static final String CHROMECAST = "chromecast";
    public static final String CHROMECAST_CONNECT = "chromecast-connect";
    public static final String CHROMECAST_CONTROL = "chromecast-control";
    public static final String CHROMECAST_DISCONNECT = "chromecast-disconnect";
    public static final String CHROMECAST_FULLSCREEN = "chromecast-fullscreen";
    public static final String CONTACT_US = "contact-us";
    public static final String CONTACT_US_ACTIONS = "contact-us-actions";
    public static final String CONTINUE_WATCHING = "continue-watching";
    public static final String CREATE_ACCOUNT = "create-account";
    public static final String CREATE_ACCOUNT_FAILURE = "create-account-failure";
    public static final String CREATE_ACCOUNT_SUCCESS = "create-account-success";
    public static final String CRUNCHYROLL_MANGA_APP = "crunchyroll-manga-app";
    public static final String CRUNCHYROLL_NEWS_APP = "crunchyroll-news-app";
    public static final String CRUNCHYROLL_STORE = "crunchyroll-store";
    public static final String DONE = "done";
    public static final String DRAMA = "drama";
    public static final String DRAMA_BROWSE = "drama-browse";
    public static final String DRAMA_EPISODE_INFO = "drama-episode-info";
    public static final String DRAMA_PLAYS = "drama-plays";
    public static final String DRAMA_SERIES = "drama-series";
    public static final String DRAMA_SERIES_GENRE = "drama-series-genre";
    public static final String DRAMA_SORT_ALPHA = "drama-sort-alpha";
    public static final String DRAMA_SORT_GENRES = "drama-sort-genres";
    public static final String DRAMA_SORT_MENU = "drama-sort-menu";
    public static final String DRAMA_SORT_MENU_GENRES = "drama-sort-menu-genres";
    public static final String DRAMA_SORT_POPULAR = "drama-sort-popular";
    public static final String DRAMA_SORT_SEASONS = "drama-sort-seasons";
    public static final String DRAMA_VIDEO = "drama-video";
    public static final String DROP_DOWN_MENU = "drop-down-menu";
    public static final String ENGAGEMENT = "engagement";
    public static final String EPISODE_INFO = "episode-info";
    public static final String EPISODE_INFO_ACTIONS = "episode-info-actions";
    public static final String EPISODE_SELECTED = "episode-selected";
    public static final String EXIT = "exit";
    public static final String FAILURE = "failure";
    public static final String FALSE = "false";
    public static final String FAST_FORWARD_10_SEC = "fast-forward-10-sec";
    public static final String FORGOT_PASSWORD = "forgot-password";
    public static final String FORGOT_PASSWORD_ACTIONS = "forgot-password-actions";
    public static final String FORGOT_PASSWORD_SENT = "forgot-password-sent";
    public static final String FREE_ACCOUNT_CREATE = "free-account-create";
    public static final String FREE_ACCOUNT_LOGIN = "free-account-login";
    public static final String FREE_EPISODE = "free-episode";
    public static final String FREE_VIDEO_ANON_UPSELL_TRIAL = "free-video-anon-upsell-trial";
    public static final String FREE_VIDEO_UPSELL = "free-video-upsell";
    public static final String FT_FAIL = "ft-upgrade-fail";
    public static final String FT_SUCCESS = "ft-upgrade-success";
    public static final String GENRES = "genres";
    public static final String HELP = "help";
    public static final String HIGH = "high";
    public static final String HIME_UPSELL = "hime-upsell";
    public static final String HOME_HISTORY = "home-history";
    public static final String HOME_QUEUE = "home-queue";
    public static final String IMA = "ima";
    public static final String INFORMATION = "information";
    public static final String IS_AD_READY = "is-ad-ready";
    public static final String LARGE = "large";
    public static final String LAST_EPISODE_WATCHED = "last-episode-watched";
    public static final String LAST_SERIES_WATCHED = "last-series-watched";
    public static final String LAYER_SEPARATOR = ".";
    public static final String LEARN_PREMIUM = "learn-premium";
    public static final String LIST = "list";
    public static final String LOAD_AD = "load-ad";
    public static final String LOAD_IMAGES = "load-images";
    public static final String LOGIN = "login";
    public static final String LOGIN_SIGN_UP = "login-sign-up";
    public static final String LOG_IN = "log-in";
    public static final String LOG_IN_ATTEMPT = "log-in-attempt";
    public static final String LOG_IN_CREATE_ACCOUNT = "log-in-create-account";
    public static final String LOG_OUT = "log-out";
    public static final String LOW = "low";
    public static final String MAIN_ANIME = "main-anime";
    public static final String MAIN_DRAMA = "main-drama";
    public static final String MAIN_HOME = "main-home";
    public static final String MAIN_NEW = "main-new";
    public static final String NEW_EPISODES = "new-episodes";
    public static final String NEW_SEASON = "new-season";
    public static final String NOT_REGISTERED = "not-registered";
    public static final String NO_FT_UPGRADE_FAIL = "no-ft-upgrade-fail";
    public static final String NO_FT_UPGRADE_SUCCESS = "no-ft-upgrade-success";
    public static final String NO_THANKS = "no-thanks";
    public static final String OFF = "off";
    public static final String ON = "on";
    public static final String OTHER_CRUNCHYROLL_PRODUCTS = "other-crunchyroll-products";
    public static final String PAYMENT = "payment";
    public static final String PAYMENT_INFO = "payment-info";
    public static final String PAYMENT_SUCCESS = "payment-success";
    public static final String PLAY = "play";
    public static final String PLAY_NEXT = "play-next";
    public static final String PREFERRED_LANGUAGE = "preferred-language";
    public static final String PREMIUM = "premium";
    public static final String PREMIUM_EPISODE = "premium-episode";
    public static final String PREMIUM_VIDEO_ANON_UPSELL_TRIAL = "premium-video-anon-upsell-trial";
    public static final String PREMIUM_VIDEO_FREE_UPSELL_TRIAL = "premium-video-free-upsell-trial";
    public static final String PREMIUM_VIDEO_UPSELL = "premium-video-upsell";
    public static final String PREMIUM_VIDEO_UPSELL_UPGRADE = "premium-video-upsell-upgrade";
    public static final String PRIVACY_POLICY = "privacy-policy";
    public static final String QUEUE = "queue";
    public static final String QUEUE_ACCOUNT_NEEDED = "queue-account-needed";
    public static final String QUEUE_CREATE_ACCOUNT = "queue-create-account";
    public static final String QUEUE_LOGIN = "queue-login";
    public static final String REGISTERED = "registered";
    public static final String REMOVE_FROM_QUEUE = "remove-from-queue";
    public static final String REWIND_10_SEC = "rewind-10-sec";
    public static final String SCREEN_VIEWS = "screen-views";
    public static final String SEARCH = "search";
    public static final String SEASONS = "seasons";
    public static final String SEASON_SELECTED = "season-selected";
    public static final String SEND = "send";
    public static final String SENT = "sent";
    public static final String SERIES_DETAIL = "series-detail";
    public static final String SERIES_NAME = "series-name";
    public static final String SERIES_OPTION = "series-option";
    public static final String SERIES_SELECTED = "series-selected";
    public static final String SETTINGS = "settings";
    public static final String SETTINGS_CONTACT_US = "settings-contact-us";
    public static final String SETTINGS_HELP = "settings-help";
    public static final String SETTINGS_LOG_OUT = "settings-log-out";
    public static final String SETTINGS_MENU_LOG_IN_CREATE_ACCOUNT = "settings-menu-log-in-create-account";
    public static final String SETTINGS_MENU_LOG_OUT = "settings-menu-log-out";
    public static final String SETTINGS_PREFERRED_LANGUAGE = "settings-preferred-language";
    public static final String SETTINGS_PRIVACY_POLICY = "settings-privacy-policy";
    public static final String SETTINGS_VIDEO_QUALITY = "settings-video-quality";
    public static final String SHARE = "share";
    public static final String SHARE_EPISODE = "share-episode";
    public static final String SHARE_SERIES = "share-series";
    public static final String SHOW_AD = "show-ad";
    public static final String SIDE_MENU = "side-menu";
    public static final String SIDE_MENU_LOG_IN_CREATE_ACCOUNT = "side-menu-log-in-create-account";
    public static final String SIDE_MENU_LOG_OUT = "side-menu-log-out";
    public static final String SIDE_MENU_UPSELL = "side-menu-upsell";
    public static final String SIGN_UP = "sign-up";
    public static final String SMALL = "small";
    public static final String SOCIAL_SHARING = "social-sharing";
    public static final String SORT_EPISODE_ASCENDING = "sort-episode-ascending";
    public static final String SORT_EPISODE_DESCENDING = "sort-episode-descending";
    public static final String SPLASH_SCREEN = "splash-screen";
    public static final String START_TRIAL = "start-trial";
    public static final String START_WATCHING = "start-watching";
    public static final String SUCCESS = "success";
    public static final String SUCCESS_SCREEN_ACTIONS = "success-screen-actions";
    public static final String SWITCH_VIEW = "switch-view";
    public static final String TREMOR = "tremor";
    public static final String TRUE = "true";
    public static final String UNCHECKED = "unchecked";
    public static final String UPGRADE = "upgrade";
    public static final String UPGRADE_NOW = "upgrade-now";
    public static final String UPGRADE_TO_PREMIUM = "upgrade-to-premium";
    public static final String UPSELL = "upsell";
    public static final String USER = "user";
    public static final String USER_TYPE = "user-type";
    public static final String VAST = "vast";
    public static final String VIDEO = "video";
    public static final String VIDEOS = "videos";
    public static final String VIDEO_QUALITY = "video-quality";
    
    public static void track(final String s, final String s2, final String s3, final String s4, final HashMap<String, String> hashMap) {
        track(s + "." + s2 + "." + s3 + "." + s4, hashMap);
    }
    
    public static void track(final String s, final String s2, final String s3, final HashMap<String, String> hashMap) {
        track(s + "." + s2 + "." + s3, hashMap);
    }
    
    public static void track(final String s, final String s2, final HashMap<String, String> hashMap) {
        track(s + "." + s2, hashMap);
    }
    
    public static void track(final String s, final HashMap<String, String> hashMap) {
        if (hashMap == null) {
            SwrveSDKBase.event(s);
            return;
        }
        SwrveSDKBase.event(s, hashMap);
    }
    
    public static void trackProperty(final String s, final String s2) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(s, s2);
        SwrveSDKBase.userUpdate(hashMap);
    }
    
    public static final class Payload
    {
        public static final String AD_TYPE = "ad-type";
        public static final String EMAIL = "email";
        public static final String EPISODE = "episode";
        public static final String GENRE = "genre";
        public static final String PROBLEM = "problem";
        public static final String SCREEN = "screen";
        public static final String SEARCH_QUERY = "search-query";
        public static final String SEARCH_RESULT_SELECTED = "search-result-selected";
        public static final String SEASON = "season";
        public static final String SERIES = "series";
        public static final String SERIES_EPISODE = "series-episode";
        public static final String SUBJECT = "subject";
    }
}
