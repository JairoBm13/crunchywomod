// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.app;

import java.util.UnknownFormatConversionException;
import java.util.Iterator;
import android.content.SharedPreferences$Editor;
import java.util.Map;
import android.content.SharedPreferences;

public enum LocalizedStrings
{
    ABOUT("about", 2131165266), 
    ACCOUNT_CREATED("account_created", 2131165267), 
    ACCOUNT_LOGIN("account_login", 2131165268), 
    ACCOUNT_REQUIRED("account_required", 2131165269), 
    ADD_THIS_SHOW_TO_QUEUE("add_this_show_to_queue", 2131165271), 
    ADD_TO_QUEUE("add_to_queue", 2131165272), 
    ALLACCESS_MEMBERSHIP_DURATION("allaccess_membership_duration", 2131165274), 
    ALL_DRAMA("all_drama", 2131165273), 
    ALPHABETICAL("alphabetical", 2131165275), 
    ALREADY_PREMIUM("already_premium", 2131165276), 
    ANIME("anime", 2131165277), 
    ANIME_MEMBERSHIP_DURATION("anime_membership_duration", 2131165278), 
    ANONYMOUS("anonymous", 2131165279), 
    ASCENDING("ascending", 2131165282), 
    AUTO("auto", 2131165285), 
    AUTOPLAY("autoplay", 2131165286), 
    A_TO_Z("a_to_z", 2131165261), 
    BACK("back", 2131165287), 
    CANCEL("cancel", 2131165288), 
    CAST("cast", 2131165289), 
    CASTING_TO_W_TEXT("casting_to_w_text", 2131165293), 
    CLEAR_SEARCH_HISTORY("clear_search_history", 2131165298), 
    CLEAR_SEARCH_HISTORY_REQUEST("clear_search_history_request", 2131165299), 
    CLOSE("close", 2131165300), 
    COMING_SOON("coming_soon", 2131165334), 
    CONNECTING_TO_W_TEXT("connecting_to_w_text", 2131165335), 
    CONTACT_US("contact_us", 2131165336), 
    CONTACT_US_SUCCESS("contact_us_success", 2131165337), 
    CONTINUE_STR("continue_str", 2131165338), 
    CONTINUE_WATCHING("continue_watching", 2131165339), 
    CREATE_ACCOUNT("create_account", 2131165340), 
    CREATE_FREE_ACCOUNT_OR_LOGIN_TO_USE_FEATURE("create_free_account_or_login_to_use_feature", 2131165341), 
    CREDIT_CARD_ALREADY_USED_FOR_FREE_TRIAL("credit_card_already_used_for_free_trial", 2131165342), 
    CREDIT_CARD_NUMBER("credit_card_number", 2131165343), 
    CRUNCHYROLL("crunchyroll", 2131165344), 
    CRUNCHYROLL_PRODUCTS("crunchyroll_products", 2131165345), 
    CRUNCHYROLL_PRODUCTS_ERROR("crunchyroll_products_error", 2131165346), 
    CURRENT_USER("current_user", 2131165347), 
    CVV("cvv", 2131165348), 
    DAYS("days", 2131165350), 
    DAYS_FROM_NOW("days_from_now", 2131165351), 
    DAY_FROM_NOW("day_from_now", 2131165349), 
    DESCENDING("descending", 2131165352), 
    DESCRIPTION("description", 2131165353), 
    DISCONNECT("disconnect", 2131165354), 
    DISCOVER_SHOWS_TO_ADD("discover_shows_to_add", 2131165355), 
    DISCOVER_SHOWS_TO_WATCH("discover_shows_to_watch", 2131165356), 
    DOWNLOADING_STRINGS_MESSAGE("downloading_strings_message", 2131165357), 
    DRAMA("drama", 2131165358), 
    EMAIL_OR_USERNAME_PROMPT("email_or_username_prompt", 2131165361), 
    EMAIL_PROMPT("email_prompt", 2131165362), 
    EMPTY_GENRE_DESCRIPTION("empty_genre_description", 2131165363), 
    EMPTY_GENRE_TEXT("empty_genre_text", 2131165364), 
    EMPTY_HISTORY("empty_history", 2131165365), 
    EMPTY_HISTORY_DESCRIPTION("empty_history_description", 2131165366), 
    EMPTY_HISTORY_DISCOVER("empty_history_discover", 2131165367), 
    EMPTY_QUEUE("empty_queue", 2131165368), 
    EMPTY_QUEUE_DESCRIPTION("empty_queue_description", 2131165369), 
    EMPTY_QUEUE_DISCOVER("empty_queue_discover", 2131165370), 
    EMPTY_SEARCH("empty_search", 2131165371), 
    EP("ep", 2131165372), 
    EPISODE("episode", 2131165373), 
    ERROR("error", 2131165374), 
    ERROR_BLOCKED("error_blocked", 2131165375), 
    ERROR_DOWNLOADING_STRINGS("error_downloading_strings", 2131165376), 
    ERROR_ENCODE_MISSING("error_encode_missing", 2131165377), 
    ERROR_INVALID_EXP_MONTH("error_invalid_exp_month", 2131165378), 
    ERROR_INVALID_EXP_YEAR("error_invalid_exp_year", 2131165379), 
    ERROR_LOADING_EPISODES("error_loading_episodes", 2131165380), 
    ERROR_LOADING_HISTORY("error_loading_history", 2131165381), 
    ERROR_LOADING_MEDIA("error_loading_media", 2131165382), 
    ERROR_LOADING_QUEUE("error_loading_queue", 2131165383), 
    ERROR_LOADING_SERIES("error_loading_series", 2131165384), 
    ERROR_MISSING_CC_EXP_MONTH("error_missing_cc_exp_month", 2131165385), 
    ERROR_MISSING_CC_EXP_YEAR("error_missing_cc_exp_year", 2131165386), 
    ERROR_MISSING_CC_NUMBER("error_missing_cc_number", 2131165387), 
    ERROR_MISSING_CC_SEC_CODE("error_missing_cc_sec_code", 2131165388), 
    ERROR_MISSING_FIRSTNAME("error_missing_firstname", 2131165389), 
    ERROR_MISSING_LASTNAME("error_missing_lastname", 2131165390), 
    ERROR_MISSING_ZIPCODE("error_missing_zipcode", 2131165391), 
    ERROR_NETWORK("error_network", 2131165392), 
    ERROR_NOT_AVAILABLE("error_not_available", 2131165395), 
    ERROR_NO_PREMIUM("error_no_premium", 2131165393), 
    ERROR_NO_RESULTS("error_no_results", 2131165394), 
    ERROR_QUEUE_ADD("error_queue_add", 2131165396), 
    ERROR_QUEUE_REMOVE("error_queue_remove", 2131165397), 
    ERROR_SERVER_PROBLEMS("error_server_problems", 2131165398), 
    ERROR_STARTING_APP("error_starting_app", 2131165399), 
    ERROR_UNKNOWN("error_unknown", 2131165400), 
    ERROR_VALIDATION_TITLE("error_validation_title", 2131165401), 
    ERROR_VIDEO("error_video", 2131165402), 
    EXISTING_USER("existing_user", 2131165403), 
    FORGOT_PASSWORD("forgot_password", 2131165405), 
    FORGOT_PASSWORD_DIALOG_MESSAGE("forgot_password_dialog_message", 2131165406), 
    FORGOT_PASSWORD_DIALOG_TITLE("forgot_password_dialog_title", 2131165407), 
    FORGOT_PASSWORD_EMAIL_HINT("forgot_password_email_hint", 2131165408), 
    FORGOT_PASSWORD_REQUEST_SUCCESS("forgot_password_request_success", 2131165409), 
    FORGOT_PASSWORD_REQUEST_SUCCESS_TITLE("forgot_password_request_success_title", 2131165410), 
    FREE("free", 2131165411), 
    FREETRIAL_CARD_EXPIRATION("freetrial_card_expiration", 2131165412), 
    FREETRIAL_CARD_NUMBER("freetrial_card_number", 2131165413), 
    FREETRIAL_COST("freetrial_cost", 2131165414), 
    FREETRIAL_COST_2("freetrial_cost_2", 2131165415), 
    FREETRIAL_COST_3("freetrial_cost_3", 2131165416), 
    FREETRIAL_COUNTRY("freetrial_country", 2131165417), 
    FREETRIAL_DURATION("freetrial_duration", 2131165418), 
    FREETRIAL_FIRST_NAME("freetrial_first_name", 2131165419), 
    FREETRIAL_LAST_NAME("freetrial_last_name", 2131165420), 
    FREETRIAL_NOTE("freetrial_note", 2131165421), 
    FREETRIAL_SECURITY_CODE("freetrial_security_code", 2131165422), 
    FREETRIAL_START_TRIAL("freetrial_start_trial", 2131165423), 
    FREETRIAL_SUCCESS("freetrial_success", 2131165424), 
    FREETRIAL_USE_WEBSITE("freetrial_use_website", 2131165425), 
    FREETRIAL_ZIP_CODE("freetrial_zip_code", 2131165426), 
    FRIDAY("friday", 2131165427), 
    GENERAL("general", 2131165429), 
    GENERAL_SETTINGS("general_settings", 2131165430), 
    GENRE("genre", 2131165431), 
    GENRES("genres", 2131165432), 
    GO_PREMIUM("go_premium", 2131165433), 
    HELP("help", 2131165436), 
    HIGH("high", 2131165444), 
    HISTORY("history", 2131165445), 
    HOME("home", 2131165446), 
    HOME_MENU_LOGIN("home_menu_login", 2131165447), 
    HOME_MENU_SETTINGS("home_menu_settings", 2131165448), 
    HOME_MENU_UPGRADE("home_menu_upgrade", 2131165449), 
    HOME_MENU_UPSELL("home_menu_upsell", 2131165450), 
    HOURS_FROM_NOW("hours_from_now", 2131165452), 
    HOUR_FROM_NOW("hour_from_now", 2131165451), 
    INFORMATION("information", 2131165456), 
    INFO_PUBLISHER("info_publisher", 2131165453), 
    INFO_VIDEOS_COUNT("info_videos_count", 2131165454), 
    INFO_YEAR("info_year", 2131165455), 
    JUST_ARRIVED("just_arrived", 2131165457), 
    KEEP_WAITING("keep_waiting", 2131165458), 
    LEARN_MORE("learn_more", 2131165459), 
    LESS("less", 2131165460), 
    LOADING("loading", 2131165463), 
    LOADING_LANGUAGES("loading_languages", 2131165464), 
    LOADING_LANGUAGES_ERROR("loading_languages_error", 2131165465), 
    LOADING_VIDEO("loading_video", 2131165467), 
    LOAD_IMAGES("load_images", 2131165461), 
    LOAD_IMAGES_SUMMARY("load_images_summary", 2131165462);
    
    private static final String LOCALIZED_STRINGS_STORE = "localizedStringsStore";
    
    LOG_IN("log_in", 2131165468), 
    LOG_IN_COPY_1("log_in_copy_1", 2131165469), 
    LOG_IN_SIGN_UP_LATER("log_in_sign_up_later", 2131165470), 
    LOG_IN_START_FREE("log_in_start_free", 2131165471), 
    LOG_OUT("log_out", 2131165472), 
    LOG_OUT_COPY("log_out_copy", 2131165473), 
    LOG_OUT_QUESTION("log_out_question", 2131165474), 
    LOW("low", 2131165475), 
    MEMBERSHIP("membership", 2131165476), 
    MEMBERSHIP_SUCCESS("membership_success", 2131165477), 
    MINS_FROM_NOW("mins_from_now", 2131165479), 
    MIN_FROM_NOW("min_from_now", 2131165478), 
    MONDAY("monday", 2131165480), 
    MONTH("month", 2131165481), 
    MORE("more", 2131165482), 
    MORE_ADS_INFO("more_ads_info", 2131165483), 
    MY_HISTORY("my_history", 2131165488), 
    MY_QUEUE("my_queue", 2131165489), 
    NEW_HOME_TAB("new_home_tab", 2131165490), 
    NEW_USER("new_user", 2131165491), 
    NOT_LOGGED_IN("not_logged_in", 2131165497), 
    NOT_NOW("not_now", 2131165498), 
    NOW_PLAYING("now_playing", 2131165499), 
    NO_EPISODES("no_episodes", 2131165492), 
    NO_HISTORY("no_history", 2131165493), 
    NO_QUEUE("no_queue", 2131165494), 
    NO_SERIES("no_series", 2131165495), 
    NO_THANKS("no_thanks", 2131165496), 
    OK("ok", 2131165500), 
    OTHER("other", 2131165501), 
    PASSWORD_PROMPT("password_prompt", 2131165502), 
    PASSWORD_RESET_SENT("password_reset_sent", 2131165503), 
    PAYMENT_INFO("payment_info", 2131165504), 
    PAYMENT_MONTH("payment_month", 2131165505), 
    PAYMENT_YEAR("payment_year", 2131165506), 
    PLAY("play", 2131165507), 
    PLEASE_WAIT("please_wait", 2131165508), 
    POPULAR("popular", 2131165509), 
    PREFERENCESCREEN("preferenceScreen", 2131165510), 
    PREFERRED_LANGUAGE("preferred_language", 2131165511), 
    PREMIUM("premium", 2131165512), 
    PREMIUM_MEMBERSHIP("premium_membership", 2131165513), 
    PRIVACY_POLICY("privacy_policy", 2131165514), 
    PROBLEM("problem", 2131165516), 
    QUEUE("queue", 2131165517), 
    RATING_RANGE("rating_range", 2131165518), 
    RECEIVE_REPLY("receive_reply", 2131165519), 
    REGISTRATION("registration", 2131165520), 
    REMOVE_FROM_QUEUE("remove_from_queue", 2131165521), 
    RESEND("resend", 2131165522), 
    RESET_PASSWORD("reset_password", 2131165523), 
    RESTART_OR_RESUME_VIDEO("restart_or_resume_video", 2131165524), 
    RESTART_VIDEO("restart_video", 2131165525), 
    RESUME_VIDEO("resume_video", 2131165526), 
    RETRY("retry", 2131165527), 
    SATURDAY("saturday", 2131165528), 
    SEARCH("search", 2131165529), 
    SEARCH_HINT("search_hint", 2131165530), 
    SEASONS("seasons", 2131165531), 
    SEND("send", 2131165532), 
    SENDING_REQUEST("sending_request", 2131165533), 
    SESSION_EXPIRED("session_expired", 2131165534), 
    SETTINGS("settings", 2131165535), 
    SHARE("share", 2131165536), 
    SHARE_FAILED("share_failed", 2131165537), 
    SHARING_SUBJECT("sharing_subject", 2131165538), 
    SIGN_UP("sign_up", 2131165539), 
    SIGN_UP_EXISTING("sign_up_existing", 2131165540), 
    SIGN_UP_NEW("sign_up_new", 2131165541), 
    SIMULCASTS("simulcasts", 2131165542), 
    SIZE_LARGE("size_large", 2131165543), 
    SIZE_LIST("size_list", 2131165544), 
    SIZE_SMALL("size_small", 2131165545), 
    SORT_BY("sort_by", 2131165546), 
    START_FREE_TRIAL("start_free_trial", 2131165548), 
    START_TRIAL("start_trial", 2131165549), 
    START_WATCHING("start_watching", 2131165550), 
    STEP_1_OF_2("step_1_of_2", 2131165552), 
    STEP_2_OF_2("step_2_of_2", 2131165553), 
    STOPPING_VIDEO("stopping_video", 2131165554), 
    SUBJECT("subject", 2131165555), 
    SUBTITLES("subtitles", 2131165556), 
    SUCCESS("success", 2131165557), 
    SUCCESS_BANG("success_bang", 2131165558), 
    SUNDAY("sunday", 2131165559), 
    SUPPORT("support", 2131165560), 
    SUPPORT_LIMIT("support_limit", 2131165561), 
    SWITCH_VIEW("switch_view", 2131165562), 
    TAP_FOR_MORE_INFO("tap_for_more_info", 2131165563), 
    THANKS_BANG("thanks_bang", 2131165565), 
    THANK_YOU_FOR_BEING_PREMIUM("thank_you_for_being_premium", 2131165564), 
    THIS_SEASON("this_season", 2131165566), 
    THURSDAY("thursday", 2131165567), 
    TRY_AGAIN("try_again", 2131165568), 
    TUESDAY("tuesday", 2131165569);
    
    private static final String UNDEFINED_STRING = "__undefined_default_text__";
    
    UPDATED("updated", 2131165570), 
    UPDATED_EPISODES("updated_episodes", 2131165571), 
    UPDATING_SERIES_INFO("updating_series_info", 2131165572), 
    UPGRADE("upgrade", 2131165573), 
    UPGRADE_NOT_QUALIFIED_FOR_FREE_TRIAL("upgrade_not_qualified_for_free_trial", 2131165574), 
    UPGRADE_NOW("upgrade_now", 2131165575), 
    UPGRADE_TITLE("upgrade_title", 2131165576), 
    UPGRADE_URL("upgrade_url", 2131165577), 
    UPSELL_1("upsell_1", 2131165578), 
    UPSELL_14DAY("upsell_14day", 2131165579), 
    UPSELL_3("upsell_3", 2131165580), 
    UPSELL_4("upsell_4", 2131165581), 
    UPSELL_5("upsell_5", 2131165582), 
    UPSELL_ACCOUNT("upsell_account", 2131165583), 
    UPSELL_ALL_ACCESS("upsell_all_access", 2131165584), 
    UPSELL_BEFORE("upsell_before", 2131165585), 
    UPSELL_DURATION("upsell_duration", 2131165586), 
    UPSELL_POINT_AD_FREE("upsell_point_ad_free", 2131165587), 
    UPSELL_POINT_HD_VIDEO("upsell_point_hd_video", 2131165588), 
    UPSELL_POINT_SIMULCASTS("upsell_point_simulcasts", 2131165589), 
    UPSELL_PREMIUM("upsell_premium", 2131165590), 
    UPSELL_SIGNUP("upsell_signup", 2131165591), 
    UPSELL_SUBSCRIPTION_VALUE("upsell_subscription_value", 2131165592), 
    UPSELL_SUBSCRIPTION_VALUE_LINE_1("upsell_subscription_value_line_1", 2131165593), 
    UPSELL_SUBSCRIPTION_VALUE_LINE_1_NO_FREE_TRIAL("upsell_subscription_value_line_1_no_free_trial", 2131165594), 
    UPSELL_SUBSCRIPTION_VALUE_LINE_2("upsell_subscription_value_line_2", 2131165595), 
    UPSELL_TRY_PREMIUM("upsell_try_premium", 2131165596), 
    UPSELL_UPGRADE("upsell_upgrade", 2131165597), 
    UPSELL_URL("upsell_url", 2131165598), 
    UPSELL_WITHOUT_ADS("upsell_without_ads", 2131165599), 
    VERSION("version", 2131165600), 
    VIDEOS("videos", 2131165604), 
    VIDEOS_COUNT("videos_count", 2131165605), 
    VIDEO_PLAYBACK("video_playback", 2131165601), 
    VIDEO_QUALITY("video_quality", 2131165602), 
    VIDEO_TAKING_WHILE_TO_LOAD("video_taking_while_to_load", 2131165603), 
    WATCH_NOW("watch_now", 2131165606), 
    WEDNESDAY("wednesday", 2131165607), 
    WHAT_AILS_YOU("what_ails_you", 2131165608), 
    YEAR("year", 2131165609), 
    YES("yes", 2131165610), 
    YESTERDAY("yesterday", 2131165611), 
    YOUR_EMAIL("your_email", 2131165614), 
    YOUR_EMAIL_OR_USERNAME("your_email_or_username", 2131165615), 
    YOUR_MESSAGE("your_message", 2131165616), 
    YOU_ARE_NOW_PREMIUM("you_are_now_premium", 2131165612), 
    YOU_ARE_SIGNED_IN_AS("you_are_signed_in_as", 2131165613);
    
    private static CrunchyrollApplication mApplication;
    private static SharedPreferences mLocalizedStrings;
    private int mResourceId;
    private String mStringToken;
    
    private LocalizedStrings(final String mStringToken, final int mResourceId) {
        this.mStringToken = mStringToken;
        this.mResourceId = mResourceId;
    }
    
    public static String getFromToken(final String s) {
        if (LocalizedStrings.mLocalizedStrings.contains(s)) {
            return LocalizedStrings.mLocalizedStrings.getString(s, "");
        }
        return null;
    }
    
    public static void setApplication(final CrunchyrollApplication mApplication) {
        LocalizedStrings.mApplication = mApplication;
        if (LocalizedStrings.mApplication != null) {
            LocalizedStrings.mLocalizedStrings = mApplication.getSharedPreferences("localizedStringsStore", 0);
            return;
        }
        LocalizedStrings.mLocalizedStrings = null;
    }
    
    public static void setStrings(final Map<String, String> map) {
        final SharedPreferences$Editor clear;
        synchronized (LocalizedStrings.mLocalizedStrings) {
            clear = LocalizedStrings.mLocalizedStrings.edit().clear();
            for (final String s : map.keySet()) {
                String replaceFirst = map.get(s);
                if (!replaceFirst.equals("__undefined_default_text__")) {
                    for (int n = 1; replaceFirst.matches(".*\\{\\w*\\}.*"); replaceFirst = replaceFirst.replaceFirst("\\{\\w*\\}", "%" + Integer.toString(n) + "\\$s"), ++n) {}
                    clear.putString(s, replaceFirst);
                }
            }
        }
        clear.commit();
    }
    // monitorexit(sharedPreferences)
    
    public String get() {
        if (LocalizedStrings.mLocalizedStrings.contains(this.mStringToken)) {
            return LocalizedStrings.mLocalizedStrings.getString(this.mStringToken, "");
        }
        return LocalizedStrings.mApplication.getResources().getString(this.mResourceId);
    }
    
    public String get(final Object... array) {
        if (!LocalizedStrings.mLocalizedStrings.contains(this.mStringToken)) {
            goto Label_0038;
        }
        try {
            return String.format(LocalizedStrings.mLocalizedStrings.getString(this.mStringToken, ""), array);
        }
        catch (RuntimeException ex) {}
        catch (UnknownFormatConversionException ex2) {
            goto Label_0038;
        }
    }
    
    public String getToken() {
        return this.mStringToken;
    }
}
