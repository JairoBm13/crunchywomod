// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.app;

import com.crunchyroll.android.util.SafeAsyncTask;
import com.crunchyroll.video.util.VideoUtil;
import com.crunchyroll.video.activities.VideoPlayerActivity;
import com.crunchyroll.android.api.models.QueueEntry;
import com.crunchyroll.android.api.models.Series;
import com.crunchyroll.android.api.models.Media;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.content.IntentFilter;
import com.crunchyroll.cast.CastHandler;
import com.crunchyroll.android.api.ClientInformation;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import java.util.TimerTask;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.secondtv.android.ads.AdTrackListener;
import com.secondtv.android.ads.tremor.Tremor;
import com.crunchyroll.android.api.models.NameValuePair;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import com.crunchyroll.android.api.ApiManager;
import com.swrve.sdk.SwrveSDK;
import java.util.Map;
import com.conviva.LivePass;
import com.crunchyroll.crunchyroid.BuildConfig;
import android.app.ActivityManager;
import android.app.ActivityManager$MemoryInfo;
import android.support.v4.app.Fragment;
import android.content.Context;
import java.util.Iterator;
import java.util.Locale;
import com.crunchyroll.android.util.LoggerFactory;
import android.app.Activity;
import android.content.BroadcastReceiver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.crunchyroll.android.api.models.LocaleData;
import java.util.List;
import com.crunchyroll.android.api.tasks.BaseListener;
import com.google.android.gms.analytics.Tracker;
import java.util.Timer;
import com.crunchyroll.android.api.models.EpisodeInfo;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.models.ClientOptions;
import com.crunchyroll.android.api.models.Categories;
import java.util.HashMap;
import com.crunchyroll.android.api.ApiService;
import com.crunchyroll.android.util.Logger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.crunchyroll.cast.CastEventListener;
import android.app.Application;

public class CrunchyrollApplication extends Application implements CastEventListener
{
    public static final String APPLICATION_DID_ENTER_BACKGROUND_EVENT = "APPLICATION_DID_ENTER_BACKGROUND_EVENT";
    public static final String APPLICATION_DID_ENTER_FOREGROUND_EVENT = "APPLICATION_DID_ENTER_FOREGROUND_EVENT";
    public static final String APPLICATION_STARTUP_COMPLETE_EVENT = "APPLICATION_STARTUP_COMPLETE_EVENT";
    public static final String[] MEDIA_TYPES;
    private static DisplayImageOptions mDisplayImageOptions;
    private static DisplayImageOptions mDisplayImageOptionsPlaceholderPortrait;
    private static DisplayImageOptions mDisplayImageOptionsPlaceholderWide;
    private static int mTremorActivityBindCount;
    private static boolean sIsDebuggable;
    private final Logger log;
    private int mActivityStartedCount;
    private ApiService mApiService;
    private ApplicationState mApplicationState;
    private CastReceiver mCastReceiver;
    private HashMap<String, Categories> mCategoriesMap;
    private ClientOptions mClientOptions;
    private Optional<EpisodeInfo> mCurrentlyCastingEpisode;
    private CrunchyrollDeepLinker mDeepLinker;
    private Timer mDelayedBackgroundCheckTimer;
    private boolean mDownloadLocalizedStringsComplete;
    private ApplicationFocusState mFocusState;
    private Tracker mGATracker;
    private boolean mGetCategoriesComplete;
    private boolean mGetClientOptionsComplete;
    private Exception mLaunchError;
    private BaseListener<List<LocaleData>> mListLocalesListener;
    private ObjectMapper mObjectMapper;
    private PrepareToWatch mPrepareToWatch;
    private BroadcastReceiver mQueueAfterLoginReceiver;
    private BroadcastReceiver mReceiver;
    private boolean mStartSessionComplete;
    private StartupState mStartupState;
    private Activity mTremorInitActivity;
    private boolean mUseCreditCardIap;
    
    static {
        MEDIA_TYPES = new String[] { "anime", "drama" };
        CrunchyrollApplication.sIsDebuggable = false;
    }
    
    public CrunchyrollApplication() {
        this.log = LoggerFactory.getLogger(this.getClass());
        this.mUseCreditCardIap = true;
        this.mFocusState = ApplicationFocusState.BACKGROUND;
        this.mLaunchError = null;
        this.mStartSessionComplete = false;
        this.mDownloadLocalizedStringsComplete = false;
        this.mGetCategoriesComplete = false;
        this.mGetClientOptionsComplete = false;
        this.mActivityStartedCount = 0;
        this.mCurrentlyCastingEpisode = Optional.absent();
        this.mListLocalesListener = new BaseListener<List<LocaleData>>() {
            @Override
            public void onException(final Exception ex) {
                CrunchyrollApplication.this.log.error("Failed to fetch locales", ex);
            }
            
            @Override
            public void onFinally() {
                CrunchyrollApplication.this.initLocalizedStrings();
            }
            
            @Override
            public void onPreExecute() {
            }
            
            @Override
            public void onSuccess(final List<LocaleData> list) {
                CrunchyrollApplication.this.log.debug("Successfully fetched locales", new Object[0]);
                final String replace = Locale.getDefault().toString().replace("_", "").replace("-", "");
                final String s = "enUS";
                final Iterator<LocaleData> iterator = list.iterator();
                while (true) {
                    do {
                        final String customLocale = s;
                        if (iterator.hasNext()) {
                            continue;
                        }
                        CrunchyrollApplication.this.mApplicationState.setCustomLocale(customLocale);
                        return;
                    } while (!iterator.next().getLocaleId().equalsIgnoreCase(replace));
                    final String customLocale = replace;
                    continue;
                }
            }
        };
    }
    
    public static CrunchyrollApplication getApp(final Context context) {
        return (CrunchyrollApplication)context.getApplicationContext();
    }
    
    public static CrunchyrollApplication getApp(final Fragment fragment) {
        return (CrunchyrollApplication)fragment.getActivity().getApplication();
    }
    
    public static DisplayImageOptions getDisplayImageOptions() {
        if (CrunchyrollApplication.mDisplayImageOptions == null) {
            CrunchyrollApplication.mDisplayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        }
        return CrunchyrollApplication.mDisplayImageOptions;
    }
    
    public static DisplayImageOptions getDisplayImageOptionsPlaceholderPortrait() {
        if (CrunchyrollApplication.mDisplayImageOptionsPlaceholderPortrait == null) {
            CrunchyrollApplication.mDisplayImageOptionsPlaceholderPortrait = new DisplayImageOptions.Builder().showImageOnLoading(2130837818).showImageForEmptyUri(2130837818).showImageOnFail(2130837818).cacheInMemory(true).cacheOnDisk(true).build();
        }
        return CrunchyrollApplication.mDisplayImageOptionsPlaceholderPortrait;
    }
    
    public static DisplayImageOptions getDisplayImageOptionsPlaceholderWide() {
        if (CrunchyrollApplication.mDisplayImageOptionsPlaceholderWide == null) {
            CrunchyrollApplication.mDisplayImageOptionsPlaceholderWide = new DisplayImageOptions.Builder().showImageOnLoading(2130837819).showImageForEmptyUri(2130837819).showImageOnFail(2130837819).cacheInMemory(true).cacheOnDisk(true).build();
        }
        return CrunchyrollApplication.mDisplayImageOptionsPlaceholderWide;
    }
    
    private long getFreeMemory() {
        final ActivityManager$MemoryInfo activityManager$MemoryInfo = new ActivityManager$MemoryInfo();
        ((ActivityManager)this.getSystemService("activity")).getMemoryInfo(activityManager$MemoryInfo);
        return activityManager$MemoryInfo.availMem;
    }
    
    private void initConviva() {
        try {
            if (BuildConfig.buildType == Constants.BuildType.RELEASE) {
                LivePass.init("4cac35af27856042182d4b583614edf2aea81124", (Context)this);
                return;
            }
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("gatewayUrl", "https://crunchyroll-test.testonly.conviva.com");
            LivePass.initWithSettings("24424a2feeb359d44cecb42677ebd06fab2e4f55", (Map<String, Object>)hashMap, (Context)this);
        }
        catch (Exception ex) {}
    }
    
    private void initSwrve() {
        try {
            SwrveSDK.createInstance((Context)this, 3086, "QlwiEusxiK0YN8JZXH9J");
        }
        catch (IllegalArgumentException ex) {}
    }
    
    public static boolean isDebuggable() {
        return CrunchyrollApplication.sIsDebuggable;
    }
    
    private void retrieveCategories() {
        this.mCategoriesMap = new HashMap<String, Categories>(CrunchyrollApplication.MEDIA_TYPES.length);
        final String[] media_TYPES = CrunchyrollApplication.MEDIA_TYPES;
        for (int length = media_TYPES.length, i = 0; i < length; ++i) {
            final String s = media_TYPES[i];
            ApiManager.getInstance(this).getCategories(s, new BaseListener<Categories>() {
                @Override
                public void onException(final Exception launchError) {
                    CrunchyrollApplication.this.setLaunchError(launchError);
                }
                
                @Override
                public void onFinally() {
                    CrunchyrollApplication.this.mGetCategoriesComplete = true;
                    CrunchyrollApplication.this.finalizeStartupFlow();
                }
                
                @Override
                public void onSuccess(final Categories categories) {
                    super.onSuccess(categories);
                    CrunchyrollApplication.this.mCategoriesMap.put(s, categories);
                }
            });
        }
    }
    
    private void retrieveClientOptions() {
        ApiManager.getInstance(this).clientOptions(new BaseListener<ClientOptions>() {
            @Override
            public void onException(final Exception launchError) {
                CrunchyrollApplication.this.setLaunchError(launchError);
            }
            
            @Override
            public void onFinally() {
                CrunchyrollApplication.this.mGetClientOptionsComplete = true;
                CrunchyrollApplication.this.finalizeStartupFlow();
            }
            
            @Override
            public void onSuccess(final ClientOptions clientOptions) {
                CrunchyrollApplication.this.mClientOptions = clientOptions;
                if (CrunchyrollApplication.this.mTremorInitActivity != null) {
                    String textValue = "379731";
                    for (final NameValuePair nameValuePair : CrunchyrollApplication.this.mClientOptions.getNameValuePairList()) {
                        if (nameValuePair.getValue() != null && nameValuePair.getValue().has("tremor_api_key")) {
                            textValue = nameValuePair.getValue().get("tremor_api_key").textValue();
                        }
                    }
                    Tremor.tremorInit(CrunchyrollApplication.this.mTremorInitActivity, textValue, new AdsListener());
                    CrunchyrollApplication.this.mTremorInitActivity = null;
                }
            }
        });
    }
    
    public static void tremorBind(final Activity activity) {
        ++CrunchyrollApplication.mTremorActivityBindCount;
    }
    
    public static void tremorUnbind() {
        --CrunchyrollApplication.mTremorActivityBindCount;
        if (CrunchyrollApplication.mTremorActivityBindCount == 0) {
            Tremor.tremorDestroy();
        }
    }
    
    public void clearStartupData() {
        this.mDownloadLocalizedStringsComplete = false;
        this.mStartSessionComplete = false;
        this.setLaunchError(null);
    }
    
    protected void filterLocales(final List<LocaleData> list) {
    }
    
    public void finalizeStartupFlow() {
        if (this.mStartSessionComplete && this.mDownloadLocalizedStringsComplete && this.mGetCategoriesComplete && this.mGetClientOptionsComplete) {
            this.mStartupState = StartupState.COMPLETE;
            LocalBroadcastManager.getInstance((Context)this).sendBroadcast(new Intent("APPLICATION_STARTUP_COMPLETE_EVENT"));
        }
    }
    
    public ApiService getApiService() {
        return this.mApiService;
    }
    
    public ApplicationState getApplicationState() {
        return this.mApplicationState;
    }
    
    public Categories getCategories(final String s) {
        return this.mCategoriesMap.get(s);
    }
    
    public ClientOptions getClientOptions() {
        return this.mClientOptions;
    }
    
    public Optional<EpisodeInfo> getCurrentlyCastingEpisode() {
        return this.mCurrentlyCastingEpisode;
    }
    
    public Map<Integer, String> getCustomDimensions() {
        final HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
        hashMap.put(1, this.getUserState());
        String s = null;
        switch (this.getApplicationState().getVideoPlayerType()) {
            default: {
                s = "native";
                break;
            }
            case 2: {
                s = "exoplayer";
                break;
            }
        }
        hashMap.put(2, s);
        String s2;
        if (this.getApplicationState().getAutoplay()) {
            s2 = "on";
        }
        else {
            s2 = "off";
        }
        hashMap.put(3, s2);
        return hashMap;
    }
    
    public CrunchyrollDeepLinker getDeepLinker() {
        return this.mDeepLinker;
    }
    
    public ApplicationFocusState getFocusState() {
        return this.mFocusState;
    }
    
    public Tracker getGATracker() {
        if (this.mGATracker == null) {
            this.mGATracker = GoogleAnalytics.getInstance((Context)this).newTracker(this.getResources().getString(2131165428));
        }
        return this.mGATracker;
    }
    
    public Exception getLaunchError() {
        return this.mLaunchError;
    }
    
    public ObjectMapper getObjectMapper() {
        return this.mObjectMapper;
    }
    
    public PrepareToWatch getPrepareToWatch() {
        return this.mPrepareToWatch;
    }
    
    public StartupState getStartupState() {
        return this.mStartupState;
    }
    
    public String getUserState() {
        if (!this.mApplicationState.hasLoggedInUser()) {
            return "not-registered";
        }
        if (this.mApplicationState.isAnimePremium() || this.mApplicationState.isDramaPremium()) {
            return "premium";
        }
        return "registered";
    }
    
    public void initLocale() {
        if (!this.mApplicationState.hasCustomLocale()) {
            this.log.debug("Begin downloading locales", new Object[0]);
            ApiManager.getInstance(this).getLocalesList(this.mListLocalesListener);
            return;
        }
        this.log.debug("Skip downloading locales, fetch strings", new Object[0]);
        this.initLocalizedStrings();
    }
    
    public void initLocalizedStrings() {
        if (!isDebuggable()) {
            Crashlytics.setString("locale", this.mApplicationState.getCustomLocale());
        }
        ApiManager.getInstance(this).getLocalizedStrings(new ApiTaskListener<Map<String, String>>() {
            @Override
            public void onCancel() {
            }
            
            @Override
            public void onException(final Exception ex) {
                CrunchyrollApplication.this.log.error("Failed to download localized strings", ex);
            }
            
            @Override
            public void onFinally() {
                CrunchyrollApplication.this.mDownloadLocalizedStringsComplete = true;
                CrunchyrollApplication.this.finalizeStartupFlow();
            }
            
            @Override
            public void onInterrupted(final Exception ex) {
            }
            
            @Override
            public void onPreExecute() {
            }
            
            @Override
            public void onSuccess(final Map<String, String> strings) {
                CrunchyrollApplication.this.log.debug("Downloaded localized strings", new Object[0]);
                LocalizedStrings.setStrings(strings);
            }
        });
    }
    
    public void invalidatePrepareToWatch() {
        if (this.mPrepareToWatch != null) {
            this.mPrepareToWatch.invalidate();
        }
    }
    
    public boolean isPrepareToWatchLoading() {
        return this.mPrepareToWatch != null && this.mPrepareToWatch.isLoading();
    }
    
    public boolean isUseCreditCardIap() {
        return this.mUseCreditCardIap;
    }
    
    public void onActivityDidStart() {
        ++this.mActivityStartedCount;
        if (this.mActivityStartedCount == 1 && this.mDelayedBackgroundCheckTimer == null) {
            this.mFocusState = ApplicationFocusState.FOREGROUND;
            this.log.info("App did enter foreground", new Object[0]);
            LocalBroadcastManager.getInstance((Context)this).sendBroadcast(new Intent("APPLICATION_DID_ENTER_FOREGROUND_EVENT"));
        }
    }
    
    public void onActivityDidStop() {
        --this.mActivityStartedCount;
        if (this.mActivityStartedCount == 0) {
            (this.mDelayedBackgroundCheckTimer = new Timer()).schedule(new TimerTask() {
                @Override
                public void run() {
                    if (CrunchyrollApplication.this.mActivityStartedCount == 0) {
                        CrunchyrollApplication.this.log.info("App did enter background", new Object[0]);
                        CrunchyrollApplication.this.mFocusState = ApplicationFocusState.BACKGROUND;
                        LocalBroadcastManager.getInstance((Context)CrunchyrollApplication.this).sendBroadcast(new Intent("APPLICATION_DID_ENTER_BACKGROUND_EVENT"));
                        CrunchyrollApplication.this.mDelayedBackgroundCheckTimer = null;
                    }
                }
            }, 1000L);
        }
    }
    
    public void onCreate() {
        super.onCreate();
        this.log.debug("*** onCreate ***", new Object[0]);
        final PackageManager packageManager = this.getPackageManager();
        while (true) {
            try {
                CrunchyrollApplication.sIsDebuggable = ((packageManager.getApplicationInfo(this.getPackageName(), 0).flags & 0x2) != 0x0);
                if (!CrunchyrollApplication.sIsDebuggable) {
                    Fabric.with((Context)this, new Crashlytics());
                }
                final ClientInformation clientInformation = new ClientInformation(this);
                LocalizedStrings.setApplication(this);
                this.mObjectMapper = new ObjectMapper();
                this.mApplicationState = new ApplicationState(this, this.mObjectMapper);
                this.mDeepLinker = new CrunchyrollDeepLinker();
                this.mApiService = new ApiService(this.mApplicationState, clientInformation, this.mObjectMapper);
                CastHandler.init((Context)this, "AA666EDD", clientInformation.getDeviceType(), this);
                if (CastHandler.isCastSupported((Context)this)) {
                    CastHandler.get().activate();
                    CastHandler.get().setLocale(this.mApplicationState.getCustomLocale());
                    this.mCastReceiver = new CastReceiver();
                    final IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("CAST_SESSION_STARTED_EVENT");
                    intentFilter.addAction("CAST_SESSION_ENDED_EVENT");
                    LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mCastReceiver, intentFilter);
                }
                CrunchyrollApplication.mTremorActivityBindCount = 0;
                ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder((Context)this).build());
                this.mReceiver = new BroadcastReceiver() {
                    public void onReceive(final Context context, final Intent intent) {
                        if (intent.getAction().equals("LOCALE_CHANGED")) {
                            CrunchyrollApplication.this.retrieveCategories();
                        }
                    }
                };
                final IntentFilter intentFilter2 = new IntentFilter();
                intentFilter2.addAction("LOCALE_CHANGED");
                LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mReceiver, new IntentFilter(intentFilter2));
                this.runStartupFlow();
                this.initConviva();
                this.initSwrve();
            }
            catch (PackageManager$NameNotFoundException ex) {
                continue;
            }
            break;
        }
    }
    
    public void onMediaChanged(final long n) {
        ApiManager.getInstance(this).getEpisodeInfoTask(n, null, new LoadEpisodeInfoTaskListener());
    }
    
    public void onPlaybackStop() {
        this.mCurrentlyCastingEpisode = Optional.absent();
    }
    
    public void onTeardown() {
        this.mCurrentlyCastingEpisode = Optional.absent();
    }
    
    public void onTerminate() {
        this.log.debug("*** onTerminate ***", new Object[0]);
        LocalizedStrings.setApplication(null);
        if (this.mCastReceiver != null) {
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.mCastReceiver);
        }
        LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.mReceiver);
        super.onTerminate();
    }
    
    public PrepareToWatch prepareToWatch(final Activity activity, final Media media, final boolean b, final int n) {
        return this.mPrepareToWatch = new PrepareToWatch(activity, PrepareToWatch.Type.PREPARE_MEDIA, media, null, b, n, null);
    }
    
    public PrepareToWatch prepareToWatch(final Activity activity, final List<Media> list, final boolean b, final int n) {
        return this.mPrepareToWatch = new PrepareToWatch(activity, PrepareToWatch.Type.PREPARE_MEDIA_LIST, null, list, b, n, null);
    }
    
    public boolean prepareToWatchGo(final PrepareToWatch.Event event) {
        if (this.mPrepareToWatch != null && this.mPrepareToWatch.isValid()) {
            this.mPrepareToWatch.go(event);
            return true;
        }
        return false;
    }
    
    public PrepareToWatch prepareToWatchNoMedia(final Activity activity, final PrepareToWatch.Type type, final boolean b, final int n, final String s) {
        return this.mPrepareToWatch = new PrepareToWatch(activity, type, null, null, b, n, s);
    }
    
    public void runStartupFlow() {
        this.clearStartupData();
        this.mStartupState = StartupState.CONNECTING;
        new OpenSessionTask((Context)this, this.mApiService).execute();
        this.initLocale();
        this.retrieveCategories();
        this.retrieveClientOptions();
        if (!isDebuggable()) {
            Crashlytics.setString("video_quality_preference", this.mApplicationState.getVideoQualityPreference(true));
        }
    }
    
    public void setCurrentlyCastingEpisode(final Optional<EpisodeInfo> mCurrentlyCastingEpisode) {
        this.mCurrentlyCastingEpisode = mCurrentlyCastingEpisode;
    }
    
    public void setLaunchError(final Exception mLaunchError) {
        this.mLaunchError = mLaunchError;
    }
    
    public void setTremorInitActivity(final Activity mTremorInitActivity) {
        this.mTremorInitActivity = mTremorInitActivity;
    }
    
    public void setUseCreditCardIap(final boolean mUseCreditCardIap) {
        this.mUseCreditCardIap = mUseCreditCardIap;
    }
    
    public void startQueueAfterLoginReceiver(final Series series) {
        this.mQueueAfterLoginReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("USER_LOGGED_IN")) {
                    ApiManager.getInstance(CrunchyrollApplication.this).addToQueue(series.getSeriesId(), new BaseListener<QueueEntry>() {
                        @Override
                        public void onFinally() {
                            super.onFinally();
                            CrunchyrollApplication.this.stopQueueAfterLoginReceiver();
                        }
                        
                        @Override
                        public void onSuccess(final QueueEntry queueEntry) {
                            super.onSuccess(queueEntry);
                            com.crunchyroll.crunchyroid.tracking.Tracker.addToQueue((Context)CrunchyrollApplication.this, series);
                        }
                    });
                }
            }
        };
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("USER_LOGGED_IN");
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mQueueAfterLoginReceiver, new IntentFilter(intentFilter));
    }
    
    public void stopQueueAfterLoginReceiver() {
        LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.mQueueAfterLoginReceiver);
    }
    
    public enum ApplicationFocusState
    {
        BACKGROUND, 
        FOREGROUND;
    }
    
    class CastReceiver extends BroadcastReceiver
    {
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction().equals("CAST_SESSION_STARTED_EVENT")) {
                com.crunchyroll.crunchyroid.tracking.Tracker.chromecastConnect();
            }
            if (intent.getAction().equals("CAST_SESSION_ENDED_EVENT")) {
                com.crunchyroll.crunchyroid.tracking.Tracker.chromecastDisonnect();
                if (!intent.hasExtra("episodeInfo")) {
                    CrunchyrollApplication.this.log.warn("Cast session ended but no episode info available", new Object[0]);
                    return;
                }
                final EpisodeInfo episodeInfo = Extras.getSerializable(intent, "episodeInfo", EpisodeInfo.class).get();
                CrunchyrollApplication.this.log.debug("Cast session ended with epsiode, playhead %d, will launch local playback", episodeInfo.getMedia().getPlayhead().or(Integer.valueOf(0)));
                final Intent startIntent = VideoPlayerActivity.getStartIntent(CrunchyrollApplication.this.getApplicationContext(), episodeInfo, false, 0);
                startIntent.setFlags(268435456);
                startIntent.putExtra("autoPlayback", true);
                CrunchyrollApplication.this.startActivity(startIntent);
            }
        }
    }
    
    private class LoadEpisodeInfoTaskListener extends BaseListener<EpisodeInfo>
    {
        @Override
        public void onException(final Exception ex) {
            CrunchyrollApplication.this.log.error("Load episode info fail", ex);
        }
        
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            CrunchyrollApplication.this.log.verbose("Begin load media info", new Object[0]);
        }
        
        @Override
        public void onSuccess(final EpisodeInfo episodeInfo) {
            CrunchyrollApplication.this.log.verbose("Load media info success", new Object[0]);
            CrunchyrollApplication.this.setCurrentlyCastingEpisode(Optional.of(episodeInfo));
            final CastHandler value = CastHandler.get();
            final String value2 = LocalizedStrings.CASTING_TO_W_TEXT.get();
            String deviceName;
            if (CastHandler.get() == null) {
                deviceName = "";
            }
            else {
                deviceName = CastHandler.get().getDeviceName();
            }
            value.onNewMediaLoaded(VideoUtil.toCastInfo(VideoPlayerActivity.class, episodeInfo, String.format(value2, deviceName)));
        }
    }
    
    class OpenSessionTask extends SafeAsyncTask<Void>
    {
        private final ApiService apiService;
        private final Context context;
        
        public OpenSessionTask(final Context context, final ApiService apiService) {
            this.context = context;
            this.apiService = apiService;
        }
        
        @Override
        public Void call() throws Exception {
            this.apiService.openSession();
            return null;
        }
        
        @Override
        protected void onException(final Exception launchError) throws RuntimeException {
            com.crunchyroll.crunchyroid.tracking.Tracker.sessionStartFailed((Context)CrunchyrollApplication.this);
            CrunchyrollApplication.this.log.error("Failed to start session", new Object[0]);
            CrunchyrollApplication.getApp(this.context).setLaunchError(launchError);
        }
        
        @Override
        protected void onFinally() throws RuntimeException {
            CrunchyrollApplication.this.mStartSessionComplete = true;
            CrunchyrollApplication.this.finalizeStartupFlow();
        }
        
        @Override
        protected void onSuccess(final Void void1) throws Exception {
            com.crunchyroll.crunchyroid.tracking.Tracker.sessionStartSuccess((Context)CrunchyrollApplication.this);
            CrunchyrollApplication.getApp(this.context).setLaunchError(null);
        }
    }
    
    public enum StartupState
    {
        COMPLETE, 
        CONNECTING;
    }
}
