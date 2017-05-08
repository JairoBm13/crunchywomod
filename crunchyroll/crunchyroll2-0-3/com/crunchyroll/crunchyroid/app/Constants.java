// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.app;

public final class Constants
{
    public static final String ACCESS_TOKEN = "Scwg9PRRZ19iVwD";
    public static final int ANON_UPSELL_FREQ = 5;
    public static final int APP_ENTRY_DEEPLINK = 1;
    public static final int APP_ENTRY_DEEPLINK_BACKABLE = 2;
    public static final int APP_ENTRY_DEFAULT = 0;
    public static final String CHROMECAST_APP_ID_PRODUCTION_SERVER = "AA666EDD";
    public static final String CHROMECAST_APP_ID_STAGE_SERVER = "B42CCDB2";
    public static final String DEFAULT_LOCALE = "enUS";
    public static final int LOAD_ALL_ITEMS = 5000;
    public static final int LOAD_MORE_ITEMS_LIMIT = 50;
    public static final String OAUTH_SECRET = "pGp7amzPhUCJ9Zu";
    public static final int REQUEST_CODE_LOGIN = 1;
    public static final int REQUEST_CODE_POPUP = 31;
    public static final int REQUEST_CODE_VIDEO = 21;
    public static final int RESULT_CODE_FAIL = 12;
    public static final int RESULT_CODE_SUCCESS = 11;
    public static final int RESULT_CODE_USER_NOT_PREMIUM = 23;
    public static final int RESULT_CODE_VIDEO_NOT_AVAILABLE = 22;
    public static final String SWRVE_API_KEY = "QlwiEusxiK0YN8JZXH9J";
    public static final int SWRVE_APP_ID = 3086;
    public static final String TREMOR_API_KEY = "379731";
    public static final String USER_TYPE_NOT_REGISTERED = "not-registered";
    public static final String USER_TYPE_PREMIUM = "premium";
    public static final String USER_TYPE_REGISTERED = "registered";
    public static final int VIDEO_PLAYER_TYPE_BRIGHTCOVE = 1;
    public static final int VIDEO_PLAYER_TYPE_EXOPLAYER = 2;
    public static final int VIDEO_PLAYER_TYPE_NATIVE = 0;
    
    public enum BuildType
    {
        BETA, 
        DEBUG, 
        MONKEYTALK, 
        RELEASE;
    }
    
    public static final class Intents
    {
        public static final String HISTORY_UPDATED = "HISTORY_UPDATED";
        public static final String LOCALE_CHANGED = "LOCALE_CHANGED";
        public static final String PAYMENT_INFO_SENT = "PAYMENT_INFO_SENT";
        public static final String QUEUE_ADD = "QUEUE_ADD";
        public static final String QUEUE_UPDATED = "QUEUE_UPDATED";
        public static final String RECEIVED_MEDIA_LIST = "RECEIVED_MEDIA_LIST";
        public static final String SESSION_EXPIRED_EVENT = "SessionExpired";
        public static final String USER_LOGGED_IN = "USER_LOGGED_IN";
        public static final String USER_LOGGED_OUT = "USER_LOGGED_OUT";
        public static final String VIDEO_PROGRESS_UPDATED = "VIDEO_PROGRESS_UPDATED";
        public static final String VIDEO_STARTED = "VIDEO_STARTED";
    }
}
