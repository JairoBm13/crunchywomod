// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.tracking;

import android.app.Fragment;
import android.app.Activity;
import com.crunchyroll.android.api.requests.LogScreenImpressionRequest;
import com.crunchyroll.android.api.tasks.LogTask;
import com.crunchyroll.android.api.models.Collection;
import com.crunchyroll.android.api.requests.LogPlaybackProgressRequest;
import com.crunchyroll.android.api.AbstractApiRequest;
import com.crunchyroll.android.api.requests.LogAdRequest;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import com.crunchyroll.crunchyroid.util.UrlUtils;
import com.crunchyroll.android.api.models.Media;
import com.crunchyroll.android.api.Filters;
import android.text.TextUtils;
import com.crunchyroll.android.api.models.Series;
import android.content.Context;
import java.util.HashMap;
import com.crunchyroll.android.util.LoggerFactory;
import com.crunchyroll.android.util.Logger;

public final class Tracker
{
    private static final Logger LOG;
    private static TrackingOrigin sCreateAccountOrigin;
    private static String sUpgradeAccountOrigin;
    
    static {
        LOG = LoggerFactory.getLogger(Tracker.class);
    }
    
    public static void adServed(final String s) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("ad-type", s);
        SwrveEvent.track("engagement", "video", "ad-served", hashMap);
    }
    
    public static void addToQueue(final Context context, final Series series) {
        GAEvent.SERIES_OPTION_ADD_TO_QUEUE.track(context, series.getName());
        FacebookTracker.addToQueue(context);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", series.getName());
        SwrveEvent.track("engagement", "queue", "add-to-queue", hashMap);
    }
    
    public static void appEnd(final Context context) {
        GAEvent.APP_SHUTDOWN.track(context);
    }
    
    public static void appStart(final Context context, final int n) {
        GAEvent.APP_START.track(context, String.valueOf(n));
    }
    
    public static void authTokenExpired(final Context context) {
        GAEvent.AUTH_TOKEN_EXPIRED.track(context, "stale_token");
    }
    
    public static void autoNextToggle(final Context context, final String s) {
        GAEvent.AUTO_NEXT_VIDEO.track(context, s);
    }
    
    public static void browseCategory(final Context context, final String s, final String s2, final String s3) {
        final StringBuilder sb = new StringBuilder("type:");
        sb.append(s);
        if (!TextUtils.isEmpty((CharSequence)s2)) {
            sb.append("|label:").append(s2);
        }
        if (!TextUtils.isEmpty((CharSequence)s3)) {
            sb.append("|filter:").append(s3);
        }
        GAEvent.BROWSE_CATEGORY.track(context, sb.toString());
    }
    
    public static void browseGenre(final Context context, final String s) {
        GAEvent.BROWSE_GENRE.track(context, s);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("genre", s);
        SwrveEvent.track("engagement", "series-detail", "browse-genre", hashMap);
    }
    
    public static void browseMedia(final Context context, final Long n) {
        GAEvent.BROWSE_MEDIA.track(context, String.valueOf(n));
    }
    
    public static void browseSeries(final Context context, final String s, String removeTag, final String s2) {
        GAEvent.BROWSE_SERIES.track(context, s);
        String s3 = "anime-browse";
        if ("drama".equalsIgnoreCase(removeTag)) {
            s3 = "drama-browse";
        }
        String s4 = s2;
        removeTag = null;
        if (Filters.isTag(s2)) {
            removeTag = Filters.removeTag(s2);
            if (Filters.isSeason(removeTag)) {
                s4 = "seasons";
            }
            else {
                s4 = "genres";
            }
        }
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", s);
        if (removeTag == null) {
            SwrveEvent.track("engagement", s3, s4, hashMap);
            return;
        }
        SwrveEvent.track("engagement", s3, s4, removeTag, hashMap);
    }
    
    public static void changeVideoQuality(final Context context, final String s) {
        GAEvent.VIDEO_QUALITY.track(context, s);
        if ("high".equalsIgnoreCase(s)) {
            SwrveEvent.track("settings", "video-quality", "high", null);
            return;
        }
        if ("low".equalsIgnoreCase(s)) {
            SwrveEvent.track("settings", "video-quality", "low", null);
            return;
        }
        SwrveEvent.track("settings", "video-quality", "auto", null);
    }
    
    public static void chromecastConnect() {
        SwrveEvent.track("engagement", "chromecast", "chromecast-connect", null);
    }
    
    public static void chromecastDisonnect() {
        SwrveEvent.track("engagement", "chromecast", "chromecast-disconnect", null);
    }
    
    public static void chromecastStartPlayback(final Context context, final Media media) {
        GAEvent.CHROMECAST_START_PLAYBACK.track(context, UrlUtils.getEventUri(media.getSeriesName().orNull(), media.getEpisodeNumber()));
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", media.getSeriesName().or(""));
        hashMap.put("episode", "episode-" + media.getEpisodeNumber());
        SwrveEvent.track("engagement", "chromecast", "play", hashMap);
    }
    
    public static void createAccountAttempt(final Context context) {
        GAEvent.CREATE_ACCOUNT_ATTEMPT.track(context);
    }
    
    public static void createAccountFail(final Context context) {
        GAEvent.CREATE_ACCOUNT_FAIL.track(context);
    }
    
    public static void createAccountSuccess(final Context context) {
        GAEvent.CREATE_ACCOUNT_SUCCESS.track(context);
    }
    
    public static void drawerHimeUpsell(final Context context) {
        GAEvent.UPGRADE_TO_PREMIUM.track(context, "hime-upsell");
        SwrveEvent.track("side-menu", "hime-upsell", null);
    }
    
    public static void drawerLoginCreateAccount() {
        SwrveEvent.track("side-menu", "log-in-create-account", null);
    }
    
    public static void drawerSettings() {
        SwrveEvent.track("side-menu", "settings", null);
    }
    
    public static void drawerUpgrade(final Context context) {
        GAEvent.UPGRADE_TO_PREMIUM.track(context, "text-upsell");
        SwrveEvent.track("side-menu", "upgrade-to-premium", null);
    }
    
    public static void dropdownMenu(final String s, final String s2, final String s3, final String s4) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", s3);
        hashMap.put("episode", s4);
        SwrveEvent.track("engagement", s, "drop-down-menu", s2, hashMap);
    }
    
    public static void episodeInfo(final Context context, final String s, final String s2) {
        GAEvent.EPISODE_INFO.track(context);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", s);
        hashMap.put("episode", s2);
        SwrveEvent.track("engagement", "series-detail", "episode-info", hashMap);
    }
    
    public static void episodeInfoActions(final String s, final String s2, final String s3) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", s2);
        hashMap.put("episode", s3);
        SwrveEvent.track("engagement", "series-detail", "episode-info-actions", s, hashMap);
    }
    
    public static void episodeInfoPlay(final Context context, final Media media) {
        GAEvent.EPISODE_INFO_PLAY.track(context, UrlUtils.getEventUri(media.getSeriesName().orNull(), media.getEpisodeNumber()));
    }
    
    public static void episodeSelect(final Context context, final Media media) {
        GAEvent.EPISODE_SELECT.track(context, UrlUtils.getEventUri(media.getSeriesName().orNull(), media.getEpisodeNumber()));
    }
    
    public static void episodeSelected(final String s, final String s2, final boolean b) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", s);
        hashMap.put("episode", s2);
        if (b) {
            SwrveEvent.track("engagement", "episode-selected", "premium-episode", hashMap);
            return;
        }
        SwrveEvent.track("engagement", "episode-selected", "free-episode", hashMap);
    }
    
    public static void fastForwardChromecast(final Context context, final String s, final String s2) {
        GAEvent.CHROMECAST_FAST_FORWARD.track(context);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", s);
        hashMap.put("episode", s2);
        SwrveEvent.track("engagement", "chromecast", "fast-forward-10-sec", hashMap);
    }
    
    public static void forgotPassword(final Context context) {
        GAEvent.FORGOT_PASSWORD.track(context);
        SwrveEvent.track("user", "forgot-password", null);
    }
    
    public static void forgotPasswordActions(final String s) {
        SwrveEvent.track("user", "forgot-password-actions", s, null);
    }
    
    public static void forgotPasswordSent(final Context context) {
        GAEvent.FORGOT_PASSWORD_SENT.track(context);
    }
    
    public static void forwardVideo(final Context context) {
        GAEvent.VIDEO_FORWARD.track(context);
        SwrveEvent.track("engagement", "video", "fast-forward-10-sec", null);
    }
    
    public static void freeTrialFail(final Context context) {
        GAEvent.UPGRADE_FAIL.track(context, Tracker.sUpgradeAccountOrigin);
        SwrveEvent.track("upsell", "ft-upgrade-fail", null);
    }
    
    public static void freeTrialSuccess(final Context context) {
        GAEvent.UPGRADE_SUCCESS.track(context, Tracker.sUpgradeAccountOrigin);
        FacebookTracker.freetrial(context);
        SwrveEvent.track("upsell", "ft-upgrade-success", null);
    }
    
    public static void freeVideoUpsellBack(final Context context, final Media media) {
        final GAEvent free_VIDEO_UPSELL_BACK = GAEvent.FREE_VIDEO_UPSELL_BACK;
        String eventUri;
        if (media == null) {
            eventUri = "";
        }
        else {
            eventUri = UrlUtils.getEventUri(media.getSeriesName().orNull(), media.getEpisodeNumber());
        }
        free_VIDEO_UPSELL_BACK.track(context, eventUri);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", media.getSeriesName().or(""));
        hashMap.put("episode", "episode-" + media.getEpisodeNumber());
        SwrveEvent.track("upsell", "free-video-anon-upsell-trial", "exit", hashMap);
    }
    
    public static void freeVideoUpsellNothanks(final Context context, final Media media) {
        final GAEvent free_VIDEO_UPSELL_NOTHANKS = GAEvent.FREE_VIDEO_UPSELL_NOTHANKS;
        String eventUri;
        if (media == null) {
            eventUri = "";
        }
        else {
            eventUri = UrlUtils.getEventUri(media.getSeriesName().orNull(), media.getEpisodeNumber());
        }
        free_VIDEO_UPSELL_NOTHANKS.track(context, eventUri);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", media.getSeriesName().or(""));
        hashMap.put("episode", "episode-" + media.getEpisodeNumber());
        SwrveEvent.track("upsell", "free-video-anon-upsell-trial", "no-thanks", hashMap);
    }
    
    public static void freeVideoUpsellSignup(final Context context, final Media media) {
        final GAEvent free_VIDEO_UPSELL_SIGNUP = GAEvent.FREE_VIDEO_UPSELL_SIGNUP;
        String eventUri;
        if (media == null) {
            eventUri = "";
        }
        else {
            eventUri = UrlUtils.getEventUri(media.getSeriesName().orNull(), media.getEpisodeNumber());
        }
        free_VIDEO_UPSELL_SIGNUP.track(context, eventUri);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", media.getSeriesName().or(""));
        hashMap.put("episode", "episode-" + media.getEpisodeNumber());
        SwrveEvent.track("upsell", "free-video-anon-upsell-trial", "start-trial", hashMap);
    }
    
    public static void homeHistory(final String s) {
        SwrveEvent.track("engagement", "home-history", s, null);
    }
    
    public static void homeQueue(final String s) {
        SwrveEvent.track("engagement", "home-queue", s, null);
    }
    
    public static void loadImages(final Context context) {
        String s;
        if (ApplicationState.get(context).isImageLoadingEnabled()) {
            s = "checked";
        }
        else {
            s = "unchecked";
        }
        GAEvent.LOAD_IMAGES.track(context, s);
        if (ApplicationState.get(context).isImageLoadingEnabled()) {
            SwrveEvent.track("settings", "load-images", "checked", null);
            return;
        }
        SwrveEvent.track("settings", "load-images", "unchecked", null);
    }
    
    public static void logAd(final Context context, final String s, final String s2) {
        trackInternalRequest(context, new LogAdRequest(s, s2));
    }
    
    public static void logInAttempt(final Context context) {
        GAEvent.LOGIN_ATTEMPT.track(context);
    }
    
    public static void logInFailed(final Context context) {
        GAEvent.LOGIN_FAILED.track(context);
    }
    
    public static void logInSuccess(final Context context) {
        GAEvent.LOGIN_SUCCESS.track(context);
    }
    
    public static void logOut(final Context context, final String s) {
        GAEvent.LOGOUT.track(context);
        SwrveEvent.track(s, "log-out", null);
    }
    
    public static void mainMenu(final Context context, final String s) {
        GAEvent.MAIN_MENU.track(context, s);
    }
    
    public static void membershipUpgradeFail(final Context context) {
        GAEvent.NO_FT_UPGRADE_FAIL.track(context, Tracker.sUpgradeAccountOrigin);
        SwrveEvent.track("upsell", "no-ft-upgrade-fail", null);
    }
    
    public static void membershipUpgradeSuccess(final Context context) {
        GAEvent.NO_FT_UPGRADE_SUCCESS.track(context, Tracker.sUpgradeAccountOrigin);
        SwrveEvent.track("upsell", "no-ft-upgrade-success", null);
    }
    
    public static void playNextChromecast(final Context context, final Media media) {
        GAEvent.PLAY_NEXT_CHROMECAST.track(context, UrlUtils.getEventUri(media.getSeriesName().orNull(), media.getEpisodeNumber()));
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", media.getSeriesName().or(""));
        hashMap.put("episode", "episode-" + media.getEpisodeNumber());
        SwrveEvent.track("engagement", "chromecast", "play-next", hashMap);
    }
    
    public static void playNextVideo(final Context context, final Media media) {
        GAEvent.PLAY_NEXT_VIDEO.track(context, UrlUtils.getEventUri(media.getSeriesName().orNull(), media.getEpisodeNumber()));
    }
    
    public static void playbackProgress(final Context context, final Long n, final Long n2, final Long n3, final Integer n4, final Integer n5, final Integer n6) {
        trackInternalRequest(context, new LogPlaybackProgressRequest("playback_status", n, n2, n3, n4, n5, n6));
    }
    
    public static void preferredLanguage(final Context context, final String s) {
        GAEvent.PREFERRED_LANGUAGE.track(context, ApplicationState.get(context).getCustomLocale().toString());
        SwrveEvent.track("settings", "preferred-language", s, null);
    }
    
    public static void premiumVideoAnonUpsellBack(final Context context, final Media media) {
        final GAEvent premium_VIDEO_ANON_UPSELL_BACK = GAEvent.PREMIUM_VIDEO_ANON_UPSELL_BACK;
        String eventUri;
        if (media == null) {
            eventUri = "";
        }
        else {
            eventUri = UrlUtils.getEventUri(media.getSeriesName().orNull(), media.getEpisodeNumber());
        }
        premium_VIDEO_ANON_UPSELL_BACK.track(context, eventUri);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", media.getSeriesName().or(""));
        hashMap.put("episode", "episode-" + media.getEpisodeNumber());
        SwrveEvent.track("upsell", "premium-video-anon-upsell-trial", "exit", hashMap);
    }
    
    public static void premiumVideoAnonUpsellStartfreetrial(final Context context, final Media media) {
        final GAEvent premium_VIDEO_ANON_UPSELL_STARTFREETRIAL = GAEvent.PREMIUM_VIDEO_ANON_UPSELL_STARTFREETRIAL;
        String eventUri;
        if (media == null) {
            eventUri = "";
        }
        else {
            eventUri = UrlUtils.getEventUri(media.getSeriesName().orNull(), media.getEpisodeNumber());
        }
        premium_VIDEO_ANON_UPSELL_STARTFREETRIAL.track(context, eventUri);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", media.getSeriesName().or(""));
        hashMap.put("episode", "episode-" + media.getEpisodeNumber());
        SwrveEvent.track("upsell", "premium-video-anon-upsell-trial", "start-trial", hashMap);
    }
    
    public static void premiumVideoFreeUpsellBack(final Context context, final Media media) {
        final GAEvent premium_VIDEO_FREE_UPSELL_BACK = GAEvent.PREMIUM_VIDEO_FREE_UPSELL_BACK;
        String eventUri;
        if (media == null) {
            eventUri = "";
        }
        else {
            eventUri = UrlUtils.getEventUri(media.getSeriesName().orNull(), media.getEpisodeNumber());
        }
        premium_VIDEO_FREE_UPSELL_BACK.track(context, eventUri);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", media.getSeriesName().or(""));
        hashMap.put("episode", "episode-" + media.getEpisodeNumber());
        SwrveEvent.track("upsell", "premium-video-free-upsell-trial", "exit", hashMap);
    }
    
    public static void premiumVideoFreeUpsellStartfreetrial(final Context context, final Media media) {
        final GAEvent premium_VIDEO_FREE_UPSELL_STARTFREETRIAL = GAEvent.PREMIUM_VIDEO_FREE_UPSELL_STARTFREETRIAL;
        String eventUri;
        if (media == null) {
            eventUri = "";
        }
        else {
            eventUri = UrlUtils.getEventUri(media.getSeriesName().orNull(), media.getEpisodeNumber());
        }
        premium_VIDEO_FREE_UPSELL_STARTFREETRIAL.track(context, eventUri);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", media.getSeriesName().or(""));
        hashMap.put("episode", "episode-" + media.getEpisodeNumber());
        SwrveEvent.track("upsell", "premium-video-free-upsell-trial", "start-trial", hashMap);
    }
    
    public static void premiumVideoUpsellUpgradeBack(final Context context, final Media media) {
        final GAEvent premium_VIDEO_UPSELL_UPGRADE_BACK = GAEvent.PREMIUM_VIDEO_UPSELL_UPGRADE_BACK;
        String eventUri;
        if (media == null) {
            eventUri = "";
        }
        else {
            eventUri = UrlUtils.getEventUri(media.getSeriesName().orNull(), media.getEpisodeNumber());
        }
        premium_VIDEO_UPSELL_UPGRADE_BACK.track(context, eventUri);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", media.getSeriesName().or(""));
        hashMap.put("episode", "episode-" + media.getEpisodeNumber());
        SwrveEvent.track("upsell", "premium-video-upsell-upgrade", "exit", hashMap);
    }
    
    public static void premiumVideoUpsellUpgradeUpgradeNow(final Context context, final Media media) {
        final GAEvent premium_VIDEO_UPSELL_UPGRADE_UPGRADENOW = GAEvent.PREMIUM_VIDEO_UPSELL_UPGRADE_UPGRADENOW;
        String eventUri;
        if (media == null) {
            eventUri = "";
        }
        else {
            eventUri = UrlUtils.getEventUri(media.getSeriesName().orNull(), media.getEpisodeNumber());
        }
        premium_VIDEO_UPSELL_UPGRADE_UPGRADENOW.track(context, eventUri);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", media.getSeriesName().or(""));
        hashMap.put("episode", "episode-" + media.getEpisodeNumber());
        SwrveEvent.track("upsell", "premium-video-upsell-upgrade", "upgrade-now", hashMap);
    }
    
    public static void queueCreateAccount(final Context context, String urlEncode) {
        urlEncode = UrlUtils.urlEncode(urlEncode);
        GAEvent.QUEUE_CREATE_ACCOUNT.track(context, urlEncode);
        SwrveEvent.track("upsell", "queue-create-account", null);
    }
    
    public static void queueLogin(final Context context, String urlEncode) {
        urlEncode = UrlUtils.urlEncode(urlEncode);
        GAEvent.QUEUE_LOGIN.track(context, urlEncode);
        SwrveEvent.track("upsell", "queue-login", null);
    }
    
    public static void queueRefresh(final Context context) {
        GAEvent.QUEUE_REFRESH.track(context);
    }
    
    public static void relatedAppClicked(final Context context, final String s) {
        GAEvent.OTHER_APPS.track(context, s);
        if (s.contains("crmanga")) {
            SwrveEvent.track("side-menu", "other-crunchyroll-products", "crunchyroll-manga-app", null);
        }
        else {
            if (s.contains("crnews")) {
                SwrveEvent.track("side-menu", "other-crunchyroll-products", "crunchyroll-news-app", null);
                return;
            }
            if (s.contains("store")) {
                SwrveEvent.track("side-menu", "other-crunchyroll-products", "crunchyroll-store", null);
            }
        }
    }
    
    public static void removeFromQueue(final Context context, final Series series) {
        GAEvent.SERIES_OPTION_REMOVE_FROM_QUEUE.track(context, series.getName());
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", series.getName());
        SwrveEvent.track("engagement", "queue", "remove-from-queue", hashMap);
    }
    
    public static void rewindChromecast(final Context context, final String s, final String s2) {
        GAEvent.CHROMECAST_REWIND.track(context);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", s);
        hashMap.put("episode", s2);
        SwrveEvent.track("engagement", "chromecast", "rewind-10-sec", hashMap);
    }
    
    public static void rewindVideo(final Context context) {
        GAEvent.VIDEO_REWIND.track(context);
        SwrveEvent.track("engagement", "video", "rewind-10-sec", null);
    }
    
    public static void screenEpisodeSelected(final String s, final String s2, final String s3) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", s2);
        hashMap.put("episode", s3);
        SwrveEvent.track("engagement", s, "episode-selected", hashMap);
    }
    
    public static void searchResultSelection(final Context context, final String s, final Series series) {
        GAEvent.SEARCH_RESULT_SELECTION.track(context, s, UrlUtils.urlEncode(series.getName()));
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("search-query", s);
        hashMap.put("search-result-selected", series.getName());
        SwrveEvent.track("engagement", "search", hashMap);
    }
    
    public static void seasonsSelect(final Context context, final Collection collection, final String s) {
        final String string = collection.getName() + "/" + collection.getSeason();
        GAEvent.SEASONS_SELECT.track(context, string);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("season", string);
        hashMap.put("series", s);
        SwrveEvent.track("engagement", "series-detail", "season-selected", hashMap);
    }
    
    public static void seriesDetailContinueWatching(final String s) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", s);
        SwrveEvent.track("engagement", "series-detail", "continue-watching", hashMap);
    }
    
    public static void seriesDetailEpisodeSelected(final String s, final String s2) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", s);
        hashMap.put("episode", s2);
        SwrveEvent.track("engagement", "series-detail", "episode-selected", hashMap);
    }
    
    public static void seriesDetailQueue(final String s, final String s2) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", s2);
        SwrveEvent.track("engagement", "series-detail", s, hashMap);
    }
    
    public static void seriesOptionAddToQueue(final Context context, final Series series) {
        GAEvent.SERIES_OPTION_ADD_TO_QUEUE.track(context, series.getName());
    }
    
    public static void seriesOptionRemoveFromQueue(final Context context, final Series series) {
        GAEvent.SERIES_OPTION_REMOVE_FROM_QUEUE.track(context, series.getName());
    }
    
    public static void seriesSelected(final String s, final String s2) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", s2);
        SwrveEvent.track("engagement", s, "series-selected", hashMap);
    }
    
    public static void sessionStartFailed(final Context context) {
        GAEvent.SESSION_START_FAILED.track(context);
    }
    
    public static void sessionStartSuccess(final Context context) {
        GAEvent.SESSION_START_SUCCESS.track(context);
    }
    
    public static void setUpsellOrigin(final TrackingOrigin trackingOrigin) {
        setUpsellOrigin(trackingOrigin, null);
    }
    
    public static void setUpsellOrigin(final TrackingOrigin sCreateAccountOrigin, final String[] array) {
        Tracker.sCreateAccountOrigin = sCreateAccountOrigin;
        String eventUriFromComponents;
        if (array != null) {
            eventUriFromComponents = UrlUtils.getEventUriFromComponents(array);
        }
        else {
            eventUriFromComponents = null;
        }
        Tracker.sUpgradeAccountOrigin = eventUriFromComponents;
        Tracker.LOG.debug("setUpsellOrigin = " + Tracker.sCreateAccountOrigin + " path = " + Tracker.sUpgradeAccountOrigin, new Object[0]);
    }
    
    public static void settingsContactUs(final Context context) {
        GAEvent.CONTACT_US.track(context);
        SwrveEvent.track("settings", "contact-us", null);
    }
    
    public static void settingsContactUsBack() {
        SwrveEvent.track("settings", "contact-us-actions", "back", null);
    }
    
    public static void settingsContactUsFailure() {
        SwrveEvent.track("settings", "contact-us-actions", "failure", null);
    }
    
    public static void settingsContactUsSend(final String s, final String s2, final String s3) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("problem", s);
        hashMap.put("subject", s2);
        hashMap.put("email", s3);
        SwrveEvent.track("settings", "contact-us-actions", "send", hashMap);
    }
    
    public static void settingsHelp(final Context context) {
        GAEvent.HELP.track(context);
        SwrveEvent.track("settings", "help", null);
    }
    
    public static void settingsLogin() {
        SwrveEvent.track("settings", "log-in", null);
    }
    
    public static void settingsLogout() {
        SwrveEvent.track("settings", "log-out", null);
    }
    
    public static void settingsPrivacyPolicy(final Context context) {
        GAEvent.PRIVACY_POLICY.track(context);
        SwrveEvent.track("settings", "privacy-policy", null);
    }
    
    public static void settingsSwitchView(final Context context, final int n) {
        switch (n) {
            default: {}
            case 3: {
                GAEvent.CARD_SIZE.track(context, "large");
                SwrveEvent.track("settings", "switch-view", "large", null);
            }
            case 2: {
                GAEvent.CARD_SIZE.track(context, "small");
                SwrveEvent.track("settings", "switch-view", "small", null);
            }
            case 1: {
                GAEvent.CARD_SIZE.track(context, "list");
                SwrveEvent.track("settings", "switch-view", "list", null);
            }
        }
    }
    
    public static void shareEpisode(final Context context, final String s, final String s2) {
        GAEvent.SHARE_EPISODE.track(context, UrlUtils.getEventUri(s, s2));
        SwrveEvent.track("engagement", "social-sharing", "share-episode", null);
    }
    
    public static void shareSeries(final Context context, final String s) {
        GAEvent.SHARE_SERIES.track(context, UrlUtils.urlEncode(s));
        SwrveEvent.track("engagement", "social-sharing", "share-series", null);
    }
    
    public static void sideMenu(final Context context) {
        GAEvent.SIDE_MENU.track(context);
    }
    
    public static void sortEpisode(final Context context, final int n) {
        String s = "";
        String s2 = "sort-episode-ascending";
        switch (n) {
            case 1: {
                s = "ascending";
                s2 = "sort-episode-ascending";
                break;
            }
            case 0: {
                s = "descending";
                s2 = "sort-episode-descending";
                break;
            }
        }
        GAEvent.SORT_EPISODE.track(context, s);
        SwrveEvent.track("engagement", "series-detail", s2, null);
    }
    
    public static void submitContactUs(final Context context) {
        GAEvent.CONTACT_US.track(context, "submit");
    }
    
    public static void swrvePropertyAutoPlay(final boolean b) {
        if (b) {
            SwrveEvent.trackProperty("auto-play", "on");
            return;
        }
        SwrveEvent.trackProperty("auto-play", "off");
    }
    
    public static void swrvePropertyLanguage(final String s) {
        SwrveEvent.trackProperty("preferred-language", s);
    }
    
    public static void swrvePropertyLastWatched(final String s, final String s2) {
        SwrveEvent.trackProperty("last-series-watched", s);
        SwrveEvent.trackProperty("last-episode-watched", s2);
    }
    
    public static void swrvePropertyUserType(final String s) {
        if ("premium".equalsIgnoreCase(s)) {
            SwrveEvent.trackProperty("user-type", "premium");
            return;
        }
        if ("registered".equalsIgnoreCase(s)) {
            SwrveEvent.trackProperty("user-type", "registered");
            return;
        }
        SwrveEvent.trackProperty("user-type", "not-registered");
    }
    
    public static void swrvePropertyVideoQuality(final String s) {
        if ("ultra".equalsIgnoreCase(s)) {
            SwrveEvent.trackProperty("video-quality", "high");
            return;
        }
        if ("mid".equalsIgnoreCase(s)) {
            SwrveEvent.trackProperty("video-quality", "low");
            return;
        }
        SwrveEvent.trackProperty("video-quality", "auto");
    }
    
    public static void swrveScreenView(final String s) {
        SwrveEvent.track("screen-views", s, null);
    }
    
    public static void swrveScreenView(final String s, final HashMap<String, String> hashMap) {
        SwrveEvent.track("screen-views", s, hashMap);
    }
    
    private static void trackInternalRequest(final Context context, final AbstractApiRequest abstractApiRequest) {
        new LogTask(context, abstractApiRequest).execute((Object[])new Void[0]);
    }
    
    private static void trackInternalScreenImpression(final Context context, final String s) {
        trackInternalRequest(context, new LogScreenImpressionRequest(s));
    }
    
    public static void trackView(final Activity activity) {
        GATracker.trackView((Context)activity, activity.getResources().getString(GATracker.getStringIdentifier((Context)activity, activity.getClass().getCanonicalName())));
    }
    
    public static void trackView(final Fragment fragment) {
        final int stringIdentifier = GATracker.getStringIdentifier((Context)fragment.getActivity(), fragment.getClass().getCanonicalName());
        if (stringIdentifier > 0) {
            GATracker.trackView((Context)fragment.getActivity(), fragment.getActivity().getResources().getString(stringIdentifier));
        }
    }
    
    public static void trackView(final Context context, final Screen screen) {
        if (screen.getGAScreenName() != null) {
            GATracker.trackView(context, screen.getGAScreenName());
        }
        if (screen.getInternalScreenName() != null) {
            trackInternalScreenImpression(context, screen.getInternalScreenName());
        }
    }
    
    public static void trackView(final Context context, final String s) {
        GATracker.trackView(context, s);
    }
    
    public static void trackView(final android.support.v4.app.Fragment fragment) {
        final int stringIdentifier = GATracker.getStringIdentifier((Context)fragment.getActivity(), fragment.getClass().getCanonicalName());
        if (stringIdentifier > 0) {
            GATracker.trackView((Context)fragment.getActivity(), fragment.getActivity().getResources().getString(stringIdentifier));
        }
    }
    
    public static void trackView(final android.support.v4.app.Fragment fragment, final String s) {
        GATracker.trackView((Context)fragment.getActivity(), s);
    }
    
    public static void tremorIsAdReady(final boolean b) {
        String s = "true";
        if (!b) {
            s = "false";
        }
        SwrveEvent.track("ads", "is-ad-ready", s, null);
    }
    
    public static void tremorLoadAd() {
        SwrveEvent.track("ads", "load-ad", null);
    }
    
    public static void tremorShowAd(final boolean b) {
        String s = "success";
        if (!b) {
            s = "failure";
        }
        SwrveEvent.track("ads", "show-ad", s, null);
    }
    
    public static void upsellCreateAccount(final Context context) {
        GAEvent.SIGN_UP_CREATE_ACCOUNT.track(context);
        SwrveEvent.track("upsell", "create-account", "attempt", null);
    }
    
    public static void upsellCreateAccountFailure(final Context context) {
        GAEvent.SIGN_UP_CREATE_ACCOUNT_FAILURE.track(context);
        SwrveEvent.track("upsell", "create-account", "failure", null);
    }
    
    public static void upsellCreateAccountSuccess(final Context context) {
        GAEvent.SIGN_UP_CREATE_ACCOUNT_SUCCESS.track(context);
        SwrveEvent.track("upsell", "create-account", "success", null);
    }
    
    public static void upsellLogin(final Context context) {
        GAEvent.SIGN_UP_LOGIN.track(context);
        SwrveEvent.track("upsell", "log-in", "attempt", null);
    }
    
    public static void upsellLoginFailure(final Context context) {
        GAEvent.SIGN_UP_LOGIN_FAILURE.track(context);
        SwrveEvent.track("upsell", "log-in", "failure", null);
    }
    
    public static void upsellLoginSuccess(final Context context) {
        GAEvent.SIGN_UP_LOGIN_SUCCESS.track(context);
        SwrveEvent.track("upsell", "log-in", "success", null);
    }
    
    public static void upsellPayment(final Context context, final boolean b) {
        final GAEvent sign_UP_PAYMENT = GAEvent.SIGN_UP_PAYMENT;
        String s;
        if (b) {
            s = "start-trial";
        }
        else {
            s = "upgrade";
        }
        sign_UP_PAYMENT.track(context, s);
        String s2 = "upgrade";
        if (b) {
            s2 = "start-trial";
        }
        SwrveEvent.track("upsell", "payment", s2, null);
    }
    
    public static void upsellPaymentExit() {
        SwrveEvent.track("upsell", "payment", "exit", null);
    }
    
    public static void upsellShown(final Context context) {
        GAEvent.UPSELL_SHOWN.track(context);
    }
    
    public static void upsellSuccessExit(final Context context) {
        GAEvent.CREATE_ACCOUNT_EXIT.track(context);
        SwrveEvent.track("upsell", "success-screen-actions", "exit", null);
        SwrveEvent.track("user", "success-screen-actions", "exit", null);
    }
    
    public static void upsellSuccessLearnPremium(final Context context) {
        GAEvent.CREATE_ACCOUNT_LEARN_PREMIUM.track(context);
        SwrveEvent.track("user", "success-screen-actions", "learn-premium", null);
    }
    
    public static void upsellSuccessStartWatching(final Context context) {
        GAEvent.CREATE_ACCOUNT_START_WATCHING.track(context);
        SwrveEvent.track("upsell", "success-screen-actions", "start-watching", null);
        SwrveEvent.track("user", "success-screen-actions", "start-watching", null);
    }
    
    public static void userCreateAccount(final String s) {
        SwrveEvent.track("user", "create-account", s, null);
    }
    
    public static void userLogin(final String s) {
        SwrveEvent.track("user", "log-in", s, null);
    }
    
    public static void videoData(final Context context, final Long n) {
        GAEvent.VIDEO_DATA.track(context, String.valueOf(n));
    }
    
    public static void videoPlay(final String s, final String s2, final String s3) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", s2);
        hashMap.put("episode", s3);
        SwrveEvent.track("engagement", "video", s, hashMap);
    }
    
    public static void videoSeries(final String s, final String s2, final String s3, final String s4) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("series", s3);
        hashMap.put("episode", s4);
        SwrveEvent.track("engagement", "video", s, s2, hashMap);
    }
    
    public static void videoStartPlayback(final Context context, final Media media) {
        GAEvent.VIDEO_START_PLAYBACK.track(context, UrlUtils.getEventUri(media.getSeriesName().orNull(), media.getEpisodeNumber()));
    }
    
    public enum Screen
    {
        HISTORY("home_history", (String)null), 
        NEW_EPISODES("new_episodes", (String)null), 
        OPEN_DRAWER("side-menu", (String)null), 
        QUEUE("home_queue", (String)null), 
        SORT_MENU_GENRES("SortMenu_genres", (String)null), 
        THIS_SEASON("new_season", (String)null), 
        UPSELL_FREE_ACCOUNT("upsell", "free_account_upsell"), 
        UPSELL_FREE_ACCOUNT_CLICKTHROUGH((String)null, "free_account_upsell_clickthrough"), 
        UPSELL_FREE_TRIAL("upsell", "free_trial_upsell"), 
        UPSELL_FREE_TRIAL_CLICKTHROUGH((String)null, "free_trial_upsell_clickthrough"), 
        UPSELL_FREE_VIDEO("free-video-upsell", (String)null), 
        UPSELL_FROM_DRAWER("menu-upsell", (String)null), 
        UPSELL_PREMIUM_VIDEO("premium-video-upsell", (String)null), 
        UPSELL_UPGRADE("upsell", (String)null);
        
        private final String mGaScreenName;
        private final String mInternalScreenName;
        
        private Screen(final String mGaScreenName, final String mInternalScreenName) {
            this.mGaScreenName = mGaScreenName;
            this.mInternalScreenName = mInternalScreenName;
        }
        
        public String getGAScreenName() {
            return this.mGaScreenName;
        }
        
        public String getInternalScreenName() {
            return this.mInternalScreenName;
        }
    }
    
    public enum TrackingOrigin
    {
        ADD_TO_QUEUE("upsell-add-to-queue"), 
        FREE_CONTENT("upsell-free"), 
        LOGIN("login"), 
        PREMIUM_CONTENT("upsell-premium"), 
        USER_FEATURE("upsell-queue");
        
        private final String mSource;
        
        private TrackingOrigin(final String mSource) {
            this.mSource = mSource;
        }
        
        public String getSource() {
            return this.mSource;
        }
    }
}
