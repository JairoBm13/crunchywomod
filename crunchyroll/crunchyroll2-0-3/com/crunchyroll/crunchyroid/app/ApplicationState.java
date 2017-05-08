// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.app;

import com.crunchyroll.android.util.DateFormatter;
import com.swrve.sdk.SwrveSDKBase;
import android.os.Build$VERSION;
import com.crunchyroll.android.api.models.Session;
import com.crunchyroll.android.api.models.User;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.crashlytics.android.Crashlytics;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.content.SharedPreferences$Editor;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import android.content.Context;
import android.preference.PreferenceManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import android.content.SharedPreferences;
import com.google.common.base.Splitter;

public final class ApplicationState
{
    private static final String APP_SHOULD_LOG_REFERRER = "app/should_log_referrer";
    private static final String APP_STATE_STORE = "appStateStore";
    private static final String AUTOPLAY_KEY = "autoplay";
    public static final String CARD_TYPE = "card_type";
    public static final String HISTORY_TERMS = "history_terms";
    private static final String INSTALL_REFERRER = "installReferrer";
    private static final String LAUNCH_COUNT = "launchCount";
    public static final String PREFERENCE_IMAGE_LOADING_ENABLED = "imageLoadingEnabled";
    public static final String PREFERENCE_LOCALE = "userLocale";
    public static final String PREFERENCE_NUM_ANON_VIDEO_VIEWS = "numAnonVideoViews";
    public static final String PREFERENCE_NUM_VIDEO_VIEWS = "numVideoViews";
    public static final String PREFERENCE_VIDEO_QUALITY = "videoQuality";
    private static final String PREMIUM_FLAG_ANIME = "anime";
    private static final String PREMIUM_FLAG_DRAMA = "drama";
    private static final String PREMIUM_FLAG_TAISENG = "taiseng";
    private static final String SESSION_AUTH = "session/auth";
    private static final String SESSION_CC = "session/cc";
    private static final String SESSION_ID = "session/id";
    private static final String SESSION_OPERATIONS = "session/operations";
    private static final String USER_CREATED = "user/created";
    private static final String USER_EMAIL = "user/email";
    private static final String USER_FIRST_NAME = "user/firstName";
    private static final String USER_ID = "user/id";
    private static final String USER_LAST_NAME = "user/lastName";
    private static final String USER_PREMIUM = "user/premium";
    private static final String USER_USERNAME = "user/username";
    public static final String VIDEO_QUALITY_ADAPTIVE = "adaptive";
    public static final String VIDEO_QUALITY_MID = "mid";
    public static final String VIDEO_QUALITY_ULTRA = "ultra";
    private static final Splitter pipeSplitter;
    private final SharedPreferences mAppStateStore;
    private final CrunchyrollApplication mApplication;
    private final ObjectMapper mMapper;
    private boolean mUseHlsPlayer;
    private final SharedPreferences mUserSettings;
    
    static {
        pipeSplitter = Splitter.on("|");
    }
    
    public ApplicationState(final CrunchyrollApplication mApplication, final ObjectMapper mMapper) {
        this.mUseHlsPlayer = true;
        this.mUserSettings = PreferenceManager.getDefaultSharedPreferences((Context)mApplication);
        this.mAppStateStore = mApplication.getSharedPreferences("appStateStore", 0);
        this.mMapper = mMapper;
        this.mApplication = mApplication;
        if (!this.mUserSettings.contains("imageLoadingEnabled")) {
            this.mUserSettings.edit().putBoolean("imageLoadingEnabled", true).commit();
        }
    }
    
    public static ApplicationState get(final Context context) {
        return CrunchyrollApplication.getApp(context).getApplicationState();
    }
    
    private static Optional<String> getString(final SharedPreferences sharedPreferences, final String s) {
        if (sharedPreferences != null && sharedPreferences.contains(s)) {
            return Optional.fromNullable(sharedPreferences.getString(s, (String)null));
        }
        return Optional.absent();
    }
    
    private static JsonNode getStringAsJson(final ObjectMapper objectMapper, final SharedPreferences sharedPreferences, final String s) {
        final Optional<String> string = getString(sharedPreferences, s);
        if (!string.isPresent()) {
            return MissingNode.getInstance();
        }
        final String s2 = string.get();
        try {
            return objectMapper.readValue(s2, JsonNode.class);
        }
        catch (JsonParseException ex) {
            throw new GetPreferenceException(ex);
        }
        catch (JsonMappingException ex2) {
            throw new GetPreferenceException(ex2);
        }
        catch (IOException ex3) {
            throw new GetPreferenceException(ex3);
        }
    }
    
    public void clearAuth() {
        final SharedPreferences$Editor edit = this.mAppStateStore.edit();
        edit.remove("session/auth");
        edit.commit();
    }
    
    public void clearLoggedInUser() {
        if (this.hasLoggedInUser()) {
            this.mApplication.getApiService().clearCache();
        }
        final SharedPreferences$Editor edit = this.mAppStateStore.edit();
        edit.remove("user/id");
        edit.remove("user/username");
        edit.remove("user/email");
        edit.remove("user/firstName");
        edit.remove("user/lastName");
        edit.remove("user/premium");
        edit.remove("user/created");
        edit.commit();
        LocalBroadcastManager.getInstance((Context)this.mApplication).sendBroadcast(new Intent("USER_LOGGED_OUT"));
        if (!CrunchyrollApplication.isDebuggable()) {
            Crashlytics.setString("user_type", this.mApplication.getUserState());
        }
        Tracker.swrvePropertyUserType(this.mApplication.getUserState());
    }
    
    public Optional<String> getAuth() {
        if (this.mAppStateStore.contains("session/auth")) {
            return Optional.fromNullable(this.mAppStateStore.getString("session/auth", (String)null));
        }
        return Optional.absent();
    }
    
    public boolean getAutoplay() {
        return this.mAppStateStore.getBoolean("autoplay", false);
    }
    
    public int getCardType() {
        return this.mAppStateStore.getInt("card_type", 2);
    }
    
    public String getCustomLocale() {
        return this.mUserSettings.getString("userLocale", "");
    }
    
    public List<String> getHistoryTerms() {
        final ArrayList<String> list = new ArrayList<String>();
        for (final String s : ApplicationState.pipeSplitter.split(this.mAppStateStore.getString("history_terms", ""))) {
            if (!s.isEmpty()) {
                list.add(s);
            }
        }
        return list;
    }
    
    public Optional<String> getInstallReferrer() {
        return getString(this.mAppStateStore, "installReferrer");
    }
    
    public long getLaunchCount() {
        return this.mAppStateStore.getLong("launchCount", 0L);
    }
    
    public Optional<User> getLoggedInUser() {
        if (this.hasLoggedInUser()) {
            final long long1 = this.mAppStateStore.getLong("user/id", -1L);
            final String string = this.mAppStateStore.getString("user/username", (String)null);
            final String string2 = this.mAppStateStore.getString("user/email", (String)null);
            final String string3 = this.mAppStateStore.getString("user/firstName", (String)null);
            final String string4 = this.mAppStateStore.getString("user/lastName", (String)null);
            final String string5 = this.mAppStateStore.getString("user/premium", (String)null);
            final String string6 = this.mAppStateStore.getString("user/created", (String)null);
            final User user = new User();
            user.setId(long1);
            user.setUsername(string);
            user.setEmail(string2);
            user.setFirstName(string3);
            user.setLastName(string4);
            user.setPremium(string5);
            user.setCreated(string6);
            return Optional.of(user);
        }
        return Optional.absent();
    }
    
    public int getNumAnonVideoViews() {
        return this.mUserSettings.getInt("numAnonVideoViews", 0);
    }
    
    public int getNumVideoViews() {
        return this.mUserSettings.getInt("numVideoViews", -1);
    }
    
    public JsonNode getOperations() {
        try {
            return getStringAsJson(this.mMapper, this.mAppStateStore, "session/operations");
        }
        catch (GetPreferenceException ex) {
            this.mAppStateStore.edit().remove("session/operations").commit();
            return MissingNode.getInstance();
        }
    }
    
    public Session getSession() {
        final Session session = new Session();
        if (!this.mAppStateStore.contains("session/id")) {
            return session;
        }
        session.setId(this.mAppStateStore.getString("session/id", (String)null));
        session.setCountryCode(this.mAppStateStore.getString("session/cc", (String)null));
        final Optional<String> auth = this.getAuth();
        if (auth.isPresent()) {
            session.setAuth(auth.get());
        }
        final Optional<User> loggedInUser = this.getLoggedInUser();
        if (loggedInUser.isPresent()) {
            session.setUser(loggedInUser.get());
        }
        session.setOps(this.getOperations());
        return session;
    }
    
    public boolean getShouldLogReferrer() {
        return this.mAppStateStore.getBoolean("app/should_log_referrer", true);
    }
    
    public boolean getUseHls() {
        return this.mUseHlsPlayer;
    }
    
    public int getVideoPlayerType() {
        final int n = 2;
        Label_0070: {
            if (!this.mUseHlsPlayer) {
                break Label_0070;
            }
            int attributeAsInt = n;
            while (true) {
                if (Build$VERSION.SDK_INT < 16) {
                    break Label_0033;
                }
                try {
                    attributeAsInt = SwrveSDKBase.getResourceManager().getAttributeAsInt("video_player", "type", 2);
                    if (!CrunchyrollApplication.isDebuggable()) {
                        switch (attributeAsInt) {
                            case 0: {
                                Crashlytics.setString("video_player", "Default");
                                return attributeAsInt;
                            }
                            case 2: {
                                Crashlytics.setString("video_player", "ExoPlayer");
                                return attributeAsInt;
                            }
                        }
                    }
                    return attributeAsInt;
                    attributeAsInt = 0;
                    continue;
                }
                catch (Exception ex) {
                    attributeAsInt = n;
                    continue;
                }
                break;
            }
        }
    }
    
    public String getVideoQualityPreference(final boolean b) {
        String string;
        if (this.isAnyPremium()) {
            string = this.mAppStateStore.getString("videoQuality", "adaptive");
        }
        else {
            string = "adaptive";
        }
        if (b) {
            switch (string) {
                case "adaptive": {
                    return "auto";
                }
                case "ultra": {
                    return "high";
                }
                case "mid": {
                    return "low";
                }
            }
        }
        return string;
    }
    
    public boolean hasCustomLocale() {
        return this.mUserSettings.contains("userLocale");
    }
    
    public boolean hasLoggedInUser() {
        return this.mAppStateStore.contains("user/id");
    }
    
    public void incrementLaunchCount() {
        this.mAppStateStore.edit().putLong("launchCount", this.getLaunchCount() + 1L).commit();
    }
    
    public void incrementNumAnonVideoViews() {
        this.mUserSettings.edit().putInt("numAnonVideoViews", this.getNumAnonVideoViews() + 1).commit();
    }
    
    public boolean isAnimePremium() {
        return this.isUserPremiumForMediaType("anime");
    }
    
    public boolean isAnyPremium() {
        final Optional<User> loggedInUser = this.getLoggedInUser();
        if (loggedInUser.isPresent()) {
            if (!loggedInUser.get().getPremium().isEmpty()) {}
        }
        return true;
    }
    
    public boolean isDramaPremium() {
        return this.isUserPremiumForMediaType("drama");
    }
    
    public boolean isImageLoadingEnabled() {
        return this.mUserSettings.getBoolean("imageLoadingEnabled", true);
    }
    
    public boolean isTaisengPremium() {
        return this.isUserPremiumForMediaType("taiseng");
    }
    
    public boolean isUserPremiumForMediaType(final String s) {
        if (s == null) {
            throw new IllegalArgumentException("mediaType cannot be null");
        }
        final Optional<User> loggedInUser = this.getLoggedInUser();
        if (loggedInUser.isPresent()) {
            final Iterator<String> iterator = ApplicationState.pipeSplitter.split(loggedInUser.get().getPremium()).iterator();
            while (iterator.hasNext()) {
                if (s.equals(iterator.next())) {
                    return true;
                }
            }
        }
        return true;
    }
    
    public void setAuth(final String s) {
        final SharedPreferences$Editor edit = this.mAppStateStore.edit();
        edit.putString("session/auth", s);
        edit.commit();
    }
    
    public void setAutoplay(final boolean b) {
        final SharedPreferences$Editor edit = this.mAppStateStore.edit();
        edit.putBoolean("autoplay", b);
        edit.commit();
        Tracker.swrvePropertyAutoPlay(b);
    }
    
    public void setCardType(final int n) {
        final SharedPreferences$Editor edit = this.mAppStateStore.edit();
        edit.putInt("card_type", n);
        edit.commit();
    }
    
    public void setCustomLocale(final String s) {
        this.mUserSettings.edit().putString("userLocale", s).commit();
        Tracker.swrvePropertyLanguage(s);
    }
    
    public void setHistoryTerms(final List<String> list) {
        String s = "";
        for (int i = 0; i < list.size(); ++i) {
            final String s2 = s += list.get(i);
            if (i != list.size() - 1) {
                s = s2 + "|";
            }
        }
        final SharedPreferences$Editor edit = this.mAppStateStore.edit();
        edit.putString("history_terms", s);
        edit.commit();
    }
    
    public void setInstallReferrer(final String s) {
        this.mAppStateStore.edit().putString("installReferrer", s).commit();
    }
    
    public void setLoggedInUser(final User user) {
        boolean b = false;
        if (user != null) {
            if (!this.hasLoggedInUser()) {
                this.mApplication.getApiService().clearCache();
                b = true;
            }
            final String format = DateFormatter.format(user.getCreated());
            final SharedPreferences$Editor edit = this.mAppStateStore.edit();
            edit.putLong("user/id", (long)user.getId());
            edit.putString("user/username", user.getUsername());
            edit.putString("user/email", user.getEmail());
            edit.putString("user/firstName", user.getFirstName());
            edit.putString("user/lastName", user.getLastName());
            edit.putString("user/premium", user.getPremium());
            edit.putString("user/created", format);
            edit.commit();
            if (b) {
                LocalBroadcastManager.getInstance((Context)this.mApplication).sendBroadcast(new Intent("USER_LOGGED_IN"));
            }
            if (!CrunchyrollApplication.isDebuggable()) {
                Crashlytics.setString("user_type", this.mApplication.getUserState());
            }
            Tracker.swrvePropertyUserType(this.mApplication.getUserState());
        }
    }
    
    public void setNumVideoViews(final int n) {
        this.mUserSettings.edit().putInt("numVideoViews", n).commit();
    }
    
    public void setOperations(final JsonNode jsonNode) {
        if (jsonNode.isMissingNode()) {
            this.mAppStateStore.edit().remove("session/operations").commit();
            return;
        }
        final String string = jsonNode.toString();
        final SharedPreferences$Editor edit = this.mAppStateStore.edit();
        edit.putString("session/operations", string);
        edit.commit();
    }
    
    public void setSession(final Session session) {
        final SharedPreferences$Editor edit = this.mAppStateStore.edit();
        edit.putString("session/id", session.getId());
        edit.putString("session/cc", session.getCountryCode());
        edit.commit();
        final Optional<String> auth = session.getAuth();
        if (auth.isPresent()) {
            this.setAuth(auth.get());
        }
        else if (this.getAuth().isPresent()) {
            Tracker.authTokenExpired((Context)this.mApplication);
            this.clearAuth();
            this.clearLoggedInUser();
            LocalBroadcastManager.getInstance((Context)this.mApplication).sendBroadcast(new Intent("SessionExpired"));
        }
        final Optional<User> user = session.getUser();
        if (user.isPresent()) {
            this.setLoggedInUser(user.get());
        }
        else if (!CrunchyrollApplication.isDebuggable()) {
            Crashlytics.setString("user_type", this.mApplication.getUserState());
        }
        final JsonNode ops = session.getOps();
        this.setOperations(ops);
        if (!ops.isMissingNode()) {
            for (final JsonNode jsonNode : ops) {
                final String text = jsonNode.path("op").asText();
                final JsonNode path = jsonNode.path("params");
                if ("feature_matrix".equals(text)) {
                    final int int1 = path.path("use_credit_card_iap").asInt();
                    path.path("disable_free_trial_flow").asText();
                    final JsonNode path2 = path.path("use_brightcove_player");
                    if (!path2.isMissingNode()) {
                        this.mUseHlsPlayer = Boolean.parseBoolean(path2.asText());
                    }
                    final CrunchyrollApplication app = CrunchyrollApplication.getApp((Context)this.mApplication);
                    switch (int1) {
                        default: {
                            continue;
                        }
                        case 0: {
                            app.setUseCreditCardIap(false);
                            continue;
                        }
                        case 1: {
                            app.setUseCreditCardIap(true);
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    public void setShouldLogReferrer(final boolean b) {
        this.mAppStateStore.edit().putBoolean("app/should_log_referrer", false).commit();
    }
    
    public void setVideoQualityPreference(final String s) {
        this.mAppStateStore.edit().putString("videoQuality", s).commit();
        if (!CrunchyrollApplication.isDebuggable()) {
            Crashlytics.setString("video_quality_preference", this.getVideoQualityPreference(true));
        }
        Tracker.swrvePropertyVideoQuality(s);
    }
    
    private static class GetPreferenceException extends RuntimeException
    {
        private static final long serialVersionUID = 7046444524364737419L;
        
        public GetPreferenceException(final Throwable t) {
            super(t);
        }
    }
}
