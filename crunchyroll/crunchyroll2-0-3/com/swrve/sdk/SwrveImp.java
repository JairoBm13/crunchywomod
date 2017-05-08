// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk;

import android.content.DialogInterface;
import android.content.DialogInterface$OnDismissListener;
import com.swrve.sdk.common.R;
import com.swrve.sdk.messaging.view.SwrveMessageViewFactory;
import java.lang.reflect.Method;
import com.swrve.sdk.rest.RESTResponse;
import com.swrve.sdk.rest.IRESTResponseListener;
import com.swrve.sdk.messaging.SwrveConversationCampaign;
import android.annotation.SuppressLint;
import java.util.Collection;
import com.swrve.sdk.messaging.SwrveCampaign;
import android.content.SharedPreferences$Editor;
import org.json.JSONArray;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.UUID;
import com.swrve.sdk.device.AndroidTelephonyManagerWrapper;
import com.swrve.sdk.messaging.SwrveOrientation;
import android.os.Build;
import com.swrve.sdk.device.ITelephonyManager;
import android.view.Display;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.swrve.sdk.rest.RESTClient;
import com.swrve.sdk.localstorage.SQLiteLocalStorage;
import com.swrve.sdk.localstorage.ILocalStorage;
import com.swrve.sdk.localstorage.MemoryLocalStorage;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import com.swrve.sdk.conversations.SwrveConversation;
import android.util.Log;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.Locale;
import java.util.Arrays;
import com.swrve.sdk.rest.IRESTClient;
import com.swrve.sdk.qa.SwrveQAUser;
import com.swrve.sdk.messaging.ISwrveMessageListener;
import java.util.concurrent.CountDownLatch;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;
import com.swrve.sdk.messaging.ISwrveInstallButtonListener;
import com.swrve.sdk.messaging.ISwrveDialogListener;
import com.swrve.sdk.messaging.ISwrveCustomButtonListener;
import com.swrve.sdk.messaging.view.SwrveDialog;
import com.swrve.sdk.conversations.ISwrveConversationListener;
import android.content.Context;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import com.swrve.sdk.messaging.SwrveBaseCampaign;
import com.swrve.sdk.localstorage.MemoryCachedLocalStorage;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ExecutorService;
import java.util.Set;
import android.util.SparseArray;
import android.app.Activity;
import java.lang.ref.WeakReference;
import com.swrve.sdk.messaging.SwrveMessage;
import java.util.List;
import com.swrve.sdk.config.SwrveConfigBase;

abstract class SwrveImp<T, C extends SwrveConfigBase>
{
    protected static int DEFAULT_DELAY_FIRST_MESSAGE;
    protected static long DEFAULT_MAX_SHOWS;
    protected static int DEFAULT_MIN_DELAY;
    private static String INSTALL_TIME_CATEGORY;
    protected static long MESSAGE_REAPPEAR_TIMEOUT;
    protected static final List<String> SUPPORTED_REQUIREMENTS;
    protected static long lastMessageDestroyed;
    protected static SwrveMessage messageDisplayed;
    protected static String version;
    protected WeakReference<Activity> activityContext;
    protected float android_device_xdpi;
    protected float android_device_ydpi;
    protected String apiKey;
    protected int appId;
    protected SparseArray<String> appStoreURLs;
    protected String appVersion;
    protected boolean assetsCurrentlyDownloading;
    protected Set<String> assetsOnDisk;
    protected ExecutorService autoShowExecutor;
    protected boolean autoShowMessagesEnabled;
    protected AtomicInteger bindCounter;
    protected File cacheDir;
    protected MemoryCachedLocalStorage cachedLocalStorage;
    protected List<SwrveBaseCampaign> campaigns;
    protected ScheduledThreadPoolExecutor campaignsAndResourcesExecutor;
    protected Integer campaignsAndResourcesFlushFrequency;
    protected Integer campaignsAndResourcesFlushRefreshDelay;
    protected boolean campaignsAndResourcesInitialized;
    protected String campaignsAndResourcesLastETag;
    protected Date campaignsAndResourcesLastRefreshed;
    protected String cdnRoot;
    protected C config;
    protected WeakReference<Context> context;
    protected ISwrveConversationListener conversationListener;
    protected WeakReference<SwrveDialog> currentDialog;
    protected ISwrveCustomButtonListener customButtonListener;
    protected boolean destroyed;
    protected float device_dpi;
    protected int device_height;
    protected int device_width;
    protected ISwrveDialogListener dialogListener;
    protected ISwrveEventListener eventListener;
    protected boolean eventsWereSent;
    protected boolean initialised;
    protected Date initialisedTime;
    protected ISwrveInstallButtonListener installButtonListener;
    protected AtomicLong installTime;
    protected final SimpleDateFormat installTimeFormat;
    protected CountDownLatch installTimeLatch;
    protected String language;
    protected long lastSessionTick;
    protected ISwrveMessageListener messageListener;
    protected long messagesLeftToShow;
    protected int minDelayBetweenMessage;
    protected boolean mustCleanInstance;
    protected long newSessionInterval;
    protected int previousOrientation;
    protected SwrveQAUser qaUser;
    protected SwrveResourceManager resourceManager;
    protected ISwrveResourcesListener resourcesListener;
    protected IRESTClient restClient;
    protected ExecutorService restClientExecutor;
    protected String sessionToken;
    protected Date showMessagesAfterDelay;
    protected Date showMessagesAfterLaunch;
    protected String sim_operator_code;
    protected String sim_operator_iso_country_code;
    protected String sim_operator_name;
    protected ExecutorService storageExecutor;
    protected final SimpleDateFormat timestampFormat;
    protected String userId;
    protected String userInstallTime;
    
    static {
        SwrveImp.version = "4.0";
        SUPPORTED_REQUIREMENTS = Arrays.asList("android");
        SwrveImp.DEFAULT_DELAY_FIRST_MESSAGE = 150;
        SwrveImp.DEFAULT_MAX_SHOWS = 99999L;
        SwrveImp.DEFAULT_MIN_DELAY = 55;
        SwrveImp.MESSAGE_REAPPEAR_TIMEOUT = 1500L;
        SwrveImp.INSTALL_TIME_CATEGORY = "SwrveSDK.installTime";
    }
    
    protected SwrveImp() {
        this.timestampFormat = new SimpleDateFormat("HH:mm:ss ZZZZ", Locale.US);
        this.installTimeFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
        this.campaignsAndResourcesInitialized = false;
        this.eventsWereSent = false;
        this.cdnRoot = "http://content-cdn.swrve.com/messaging/message_image/";
        this.initialised = false;
        this.installTime = new AtomicLong();
        this.installTimeLatch = new CountDownLatch(1);
        this.destroyed = false;
        this.autoShowExecutor = Executors.newSingleThreadExecutor();
        this.bindCounter = new AtomicInteger();
        this.autoShowMessagesEnabled = true;
        this.assetsOnDisk = new HashSet<String>();
        this.assetsCurrentlyDownloading = false;
    }
    
    private boolean supportsDeviceFilter(final String s) {
        return SwrveImp.SUPPORTED_REQUIREMENTS.contains(s.toLowerCase());
    }
    
    protected void autoShowConversation(final SwrveBase<T, C> swrveBase) {
        try {
            if (this.conversationListener != null && this.autoShowMessagesEnabled) {
                final SwrveConversation conversationForEvent = swrveBase.getConversationForEvent("Swrve.Messages.showAtSessionStart");
                if (conversationForEvent != null) {
                    this.conversationListener.onMessage(conversationForEvent);
                    this.autoShowMessagesEnabled = false;
                }
            }
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Could not launch conversation automatically.", (Throwable)ex);
        }
    }
    
    protected void autoShowInAppMessage(final SwrveBase<T, C> swrveBase) {
        try {
            if (this.messageListener != null && this.autoShowMessagesEnabled) {
                final SwrveMessage messageForEvent = swrveBase.getMessageForEvent("Swrve.Messages.showAtSessionStart");
                if (messageForEvent != null && messageForEvent.supportsOrientation(this.getDeviceOrientation())) {
                    this.messageListener.onMessage(messageForEvent, true);
                    this.autoShowMessagesEnabled = false;
                }
            }
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Could not launch campaign automatically.", (Throwable)ex);
        }
    }
    
    protected void autoShowMessages() {
        if (this.autoShowMessagesEnabled && this.campaignsAndResourcesInitialized && this.campaigns != null) {
            for (final SwrveBaseCampaign swrveBaseCampaign : this.campaigns) {
                final SwrveBase swrveBase = (SwrveBase)this;
                if (swrveBaseCampaign.hasElementForEvent("Swrve.Messages.showAtSessionStart")) {
                    synchronized (this) {
                        if (this.autoShowMessagesEnabled && this.activityContext != null) {
                            final Activity activity = this.activityContext.get();
                            if (activity != null) {
                                activity.runOnUiThread((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        SwrveImp.this.autoShowConversation(swrveBase);
                                        SwrveImp.this.autoShowInAppMessage(swrveBase);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
    }
    
    protected Context bindToContext(final Activity activity) {
        this.bindCounter.incrementAndGet();
        this.context = new WeakReference<Context>(activity.getApplicationContext());
        this.activityContext = new WeakReference<Activity>(activity);
        return this.context.get();
    }
    
    protected void checkForCampaignAndResourcesUpdates() {
        if (!this.cachedLocalStorage.getCombinedFirstNEvents(this.config.getMaxEventsPerFlush()).isEmpty() || this.eventsWereSent) {
            final SwrveBase swrveBase = (SwrveBase)this;
            swrveBase.sendQueuedEvents();
            this.eventsWereSent = false;
            Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
                @Override
                public void run() {
                    swrveBase.refreshCampaignsAndResources();
                }
            }, this.campaignsAndResourcesFlushRefreshDelay, TimeUnit.MILLISECONDS);
        }
    }
    
    protected void checkUserId(final String s) {
        if (s != null && s.matches("^.*\\..*@\\w+$")) {
            Log.w("SwrveSDK", "Please double-check your user id. It seems to be Object.toString(): " + s);
        }
    }
    
    protected MemoryCachedLocalStorage createCachedLocalStorage() {
        return new MemoryCachedLocalStorage(new MemoryLocalStorage(), null);
    }
    
    protected ILocalStorage createLocalStorage() {
        return new SQLiteLocalStorage(this.context.get(), this.config.getDbName(), this.config.getMaxSqliteDbSize());
    }
    
    protected IRESTClient createRESTClient() {
        return new RESTClient();
    }
    
    protected ExecutorService createRESTClientExecutor() {
        return Executors.newSingleThreadExecutor();
    }
    
    protected ExecutorService createStorageExecutor() {
        return Executors.newSingleThreadExecutor();
    }
    
    protected void disableAutoShowAfterDelay() {
        Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
            @Override
            public void run() {
                SwrveImp.this.autoShowMessagesEnabled = false;
            }
        }, this.config.getAutoShowMessagesMaxDelay(), TimeUnit.MILLISECONDS);
    }
    
    protected boolean downloadAssetSynchronously(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: new             Ljava/lang/StringBuilder;
        //     3: dup            
        //     4: invokespecial   java/lang/StringBuilder.<init>:()V
        //     7: aload_0        
        //     8: getfield        com/swrve/sdk/SwrveImp.cdnRoot:Ljava/lang/String;
        //    11: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    14: aload_1        
        //    15: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    18: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    21: astore          4
        //    23: aconst_null    
        //    24: astore          6
        //    26: aconst_null    
        //    27: astore_3       
        //    28: aconst_null    
        //    29: astore          5
        //    31: new             Lcom/swrve/sdk/rest/SwrveFilterInputStream;
        //    34: dup            
        //    35: new             Ljava/net/URL;
        //    38: dup            
        //    39: aload           4
        //    41: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //    44: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //    47: invokevirtual   java/net/URLConnection.getInputStream:()Ljava/io/InputStream;
        //    50: invokespecial   com/swrve/sdk/rest/SwrveFilterInputStream.<init>:(Ljava/io/InputStream;)V
        //    53: astore          4
        //    55: new             Ljava/io/ByteArrayOutputStream;
        //    58: dup            
        //    59: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
        //    62: astore_3       
        //    63: sipush          2048
        //    66: newarray        B
        //    68: astore          5
        //    70: aload           4
        //    72: aload           5
        //    74: invokevirtual   java/io/InputStream.read:([B)I
        //    77: istore_2       
        //    78: iload_2        
        //    79: iconst_m1      
        //    80: if_icmpeq       132
        //    83: aload_3        
        //    84: aload           5
        //    86: iconst_0       
        //    87: iload_2        
        //    88: invokevirtual   java/io/ByteArrayOutputStream.write:([BII)V
        //    91: goto            70
        //    94: astore_3       
        //    95: aload           4
        //    97: astore_1       
        //    98: aload_3        
        //    99: astore          4
        //   101: aload_1        
        //   102: astore_3       
        //   103: ldc_w           "SwrveSDK"
        //   106: ldc_w           "Error downloading campaigns"
        //   109: aload           4
        //   111: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   114: pop            
        //   115: aload_1        
        //   116: astore_3       
        //   117: aload           4
        //   119: invokevirtual   java/net/MalformedURLException.printStackTrace:()V
        //   122: aload_1        
        //   123: ifnull          130
        //   126: aload_1        
        //   127: invokevirtual   java/io/InputStream.close:()V
        //   130: iconst_0       
        //   131: ireturn        
        //   132: aload_3        
        //   133: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
        //   136: astore          5
        //   138: aload_1        
        //   139: aload_3        
        //   140: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
        //   143: invokestatic    com/swrve/sdk/SwrveHelper.sha1:([B)Ljava/lang/String;
        //   146: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   149: ifeq            202
        //   152: new             Ljava/io/FileOutputStream;
        //   155: dup            
        //   156: new             Ljava/io/File;
        //   159: dup            
        //   160: aload_0        
        //   161: getfield        com/swrve/sdk/SwrveImp.cacheDir:Ljava/io/File;
        //   164: aload_1        
        //   165: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //   168: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   171: astore_1       
        //   172: aload_1        
        //   173: aload           5
        //   175: invokevirtual   java/io/FileOutputStream.write:([B)V
        //   178: aload_1        
        //   179: invokevirtual   java/io/FileOutputStream.close:()V
        //   182: aload           4
        //   184: ifnull          192
        //   187: aload           4
        //   189: invokevirtual   java/io/InputStream.close:()V
        //   192: iconst_1       
        //   193: ireturn        
        //   194: astore_1       
        //   195: aload_1        
        //   196: invokevirtual   java/io/IOException.printStackTrace:()V
        //   199: goto            192
        //   202: aload           4
        //   204: ifnull          315
        //   207: aload           4
        //   209: invokevirtual   java/io/InputStream.close:()V
        //   212: iconst_0       
        //   213: ireturn        
        //   214: astore_1       
        //   215: aload_1        
        //   216: invokevirtual   java/io/IOException.printStackTrace:()V
        //   219: iconst_0       
        //   220: ireturn        
        //   221: astore_1       
        //   222: aload_1        
        //   223: invokevirtual   java/io/IOException.printStackTrace:()V
        //   226: iconst_0       
        //   227: ireturn        
        //   228: astore          4
        //   230: aload           6
        //   232: astore_1       
        //   233: aload_1        
        //   234: astore_3       
        //   235: ldc_w           "SwrveSDK"
        //   238: ldc_w           "Error downloading campaigns"
        //   241: aload           4
        //   243: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   246: pop            
        //   247: aload_1        
        //   248: astore_3       
        //   249: aload           4
        //   251: invokevirtual   java/io/IOException.printStackTrace:()V
        //   254: aload_1        
        //   255: ifnull          130
        //   258: aload_1        
        //   259: invokevirtual   java/io/InputStream.close:()V
        //   262: iconst_0       
        //   263: ireturn        
        //   264: astore_1       
        //   265: aload_1        
        //   266: invokevirtual   java/io/IOException.printStackTrace:()V
        //   269: iconst_0       
        //   270: ireturn        
        //   271: astore_1       
        //   272: aload_3        
        //   273: ifnull          280
        //   276: aload_3        
        //   277: invokevirtual   java/io/InputStream.close:()V
        //   280: aload_1        
        //   281: athrow         
        //   282: astore_3       
        //   283: aload_3        
        //   284: invokevirtual   java/io/IOException.printStackTrace:()V
        //   287: goto            280
        //   290: astore_1       
        //   291: aload           4
        //   293: astore_3       
        //   294: goto            272
        //   297: astore_3       
        //   298: aload           4
        //   300: astore_1       
        //   301: aload_3        
        //   302: astore          4
        //   304: goto            233
        //   307: astore          4
        //   309: aload           5
        //   311: astore_1       
        //   312: goto            101
        //   315: iconst_0       
        //   316: ireturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  31     55     307    315    Ljava/net/MalformedURLException;
        //  31     55     228    233    Ljava/io/IOException;
        //  31     55     271    272    Any
        //  55     70     94     101    Ljava/net/MalformedURLException;
        //  55     70     297    307    Ljava/io/IOException;
        //  55     70     290    297    Any
        //  70     78     94     101    Ljava/net/MalformedURLException;
        //  70     78     297    307    Ljava/io/IOException;
        //  70     78     290    297    Any
        //  83     91     94     101    Ljava/net/MalformedURLException;
        //  83     91     297    307    Ljava/io/IOException;
        //  83     91     290    297    Any
        //  103    115    271    272    Any
        //  117    122    271    272    Any
        //  126    130    221    228    Ljava/io/IOException;
        //  132    182    94     101    Ljava/net/MalformedURLException;
        //  132    182    297    307    Ljava/io/IOException;
        //  132    182    290    297    Any
        //  187    192    194    202    Ljava/io/IOException;
        //  207    212    214    221    Ljava/io/IOException;
        //  235    247    271    272    Any
        //  249    254    271    272    Any
        //  258    262    264    271    Ljava/io/IOException;
        //  276    280    282    290    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0280:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    protected void downloadAssets(final Set<String> set) {
        this.assetsCurrentlyDownloading = true;
        Executors.newSingleThreadExecutor().execute(SwrveRunnables.withoutExceptions(new Runnable() {
            @Override
            public void run() {
                for (final String s : SwrveImp.this.filterExistingFiles(set)) {
                    if (SwrveImp.this.downloadAssetSynchronously(s)) {
                        synchronized (SwrveImp.this.assetsOnDisk) {
                            SwrveImp.this.assetsOnDisk.add(s);
                            continue;
                        }
                        break;
                    }
                }
                SwrveImp.this.assetsCurrentlyDownloading = false;
                SwrveImp.this.autoShowMessages();
            }
        }));
    }
    
    protected Set<String> filterExistingFiles(final Set<String> set) {
        final Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            final String s = iterator.next();
            if (new File(this.cacheDir, s).exists()) {
                iterator.remove();
                synchronized (this.assetsOnDisk) {
                    this.assetsOnDisk.add(s);
                    continue;
                }
                break;
            }
        }
        return set;
    }
    
    protected void findCacheFolder(final Context context) {
        this.cacheDir = this.config.getCacheDir();
        if (this.cacheDir == null) {
            this.cacheDir = context.getCacheDir();
        }
        if (!this.cacheDir.exists()) {
            this.cacheDir.mkdirs();
        }
    }
    
    protected void generateNewSessionInterval() {
        this.lastSessionTick = this.getSessionTime() + this.newSessionInterval;
    }
    
    protected Activity getActivityContext() {
        if (this.activityContext != null) {
            final Activity activity = this.activityContext.get();
            if (activity != null) {
                return activity;
            }
        }
        return null;
    }
    
    public Set<String> getAssetsOnDisk() {
        synchronized (this.assetsOnDisk) {
            return this.assetsOnDisk;
        }
    }
    
    public String getAutoShowEventTrigger() {
        return "Swrve.Messages.showAtSessionStart";
    }
    
    protected void getDeviceInfo(final Context context) {
        try {
            final Display defaultDisplay = ((WindowManager)context.getSystemService("window")).getDefaultDisplay();
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            final int width = defaultDisplay.getWidth();
            final int height = defaultDisplay.getHeight();
            defaultDisplay.getMetrics(displayMetrics);
            final float xdpi = displayMetrics.xdpi;
            final float ydpi = displayMetrics.ydpi;
            int device_height = height;
            int device_width = width;
            float android_device_xdpi = xdpi;
            float android_device_ydpi = ydpi;
            if (width > height) {
                device_width = height;
                android_device_ydpi = xdpi;
                android_device_xdpi = ydpi;
                device_height = width;
            }
            this.device_width = device_width;
            this.device_height = device_height;
            this.device_dpi = displayMetrics.densityDpi;
            this.android_device_xdpi = android_device_xdpi;
            this.android_device_ydpi = android_device_ydpi;
            final ITelephonyManager telephonyManager = this.getTelephonyManager(context);
            this.sim_operator_name = telephonyManager.getSimOperatorName();
            this.sim_operator_iso_country_code = telephonyManager.getSimCountryIso();
            this.sim_operator_code = telephonyManager.getSimOperator();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Get device screen info failed", (Throwable)ex);
        }
    }
    
    protected String getDeviceName() {
        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        }
        return manufacturer + " " + model;
    }
    
    protected SwrveOrientation getDeviceOrientation() {
        final Context context = this.context.get();
        if (context != null) {
            return SwrveOrientation.parse(context.getResources().getConfiguration().orientation);
        }
        return SwrveOrientation.Both;
    }
    
    protected long getInstallTime() {
        final long time = new Date().getTime();
        try {
            final String savedInstallTime = this.getSavedInstallTime();
            if (!SwrveHelper.isNullOrEmpty(savedInstallTime)) {
                return Long.parseLong(savedInstallTime);
            }
            this.cachedLocalStorage.setAndFlushSharedEntry(SwrveImp.INSTALL_TIME_CATEGORY, String.valueOf(time));
            return time;
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Could not get or save install time", (Throwable)ex);
            return time;
        }
    }
    
    public Date getNow() {
        return new Date();
    }
    
    protected long getOrWaitForInstallTime() {
        try {
            this.installTimeLatch.await();
            return this.installTime.get();
        }
        catch (Exception ex) {
            return new Date().getTime();
        }
    }
    
    protected String getSavedInstallTime() {
        return this.cachedLocalStorage.getSharedCacheEntry(SwrveImp.INSTALL_TIME_CATEGORY);
    }
    
    protected long getSessionTime() {
        return this.getNow().getTime();
    }
    
    protected ITelephonyManager getTelephonyManager(final Context context) {
        return new AndroidTelephonyManagerWrapper(context);
    }
    
    public String getUniqueKey() {
        return this.userId + this.apiKey;
    }
    
    protected String getUniqueUserId(final Context context) {
        String s;
        if (SwrveHelper.isNullOrEmpty(s = context.getSharedPreferences("swrve_prefs", 0).getString("userId", (String)null))) {
            s = UUID.randomUUID().toString();
        }
        return s;
    }
    
    protected boolean hasShowTooManyMessagesAlready() {
        return this.messagesLeftToShow <= 0L;
    }
    
    protected void initCampaigns() {
        this.campaigns = new ArrayList<SwrveBaseCampaign>();
        try {
            final String secureCacheEntryForUser = this.cachedLocalStorage.getSecureCacheEntryForUser(this.userId, "CMCC2", this.getUniqueKey());
            if (!SwrveHelper.isNullOrEmpty(secureCacheEntryForUser)) {
                final JSONObject jsonObject = new JSONObject(secureCacheEntryForUser);
                final String cacheEntryForUser = this.cachedLocalStorage.getCacheEntryForUser(this.userId, "SwrveCampaignSettings");
                JSONObject jsonObject2 = null;
                if (!SwrveHelper.isNullOrEmpty(cacheEntryForUser)) {
                    jsonObject2 = new JSONObject(cacheEntryForUser);
                }
                this.updateCampaigns(jsonObject, jsonObject2);
                Log.i("SwrveSDK", "Loaded campaigns from cache.");
                return;
            }
            this.invalidateETag();
        }
        catch (JSONException ex) {
            this.invalidateETag();
            Log.e("SwrveSDK", "Invalid json in cache, cannot load campaigns", (Throwable)ex);
        }
        catch (SecurityException ex2) {
            this.invalidateETag();
            Log.e("SwrveSDK", "Signature validation failed when trying to load campaigns from cache.", (Throwable)ex2);
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("name", "Swrve.signature_invalid");
            this.queueEvent("event", (Map<String, Object>)hashMap, null, false);
        }
    }
    
    protected void initResources() {
        String secureCacheEntryForUser = null;
        Label_0107: {
            while (true) {
                try {
                    secureCacheEntryForUser = this.cachedLocalStorage.getSecureCacheEntryForUser(this.userId, "srcngt2", this.getUniqueKey());
                    if (secureCacheEntryForUser != null) {
                        final String s = secureCacheEntryForUser;
                        final JSONArray jsonArray = new JSONArray(s);
                        final SwrveImp swrveImp = this;
                        final SwrveResourceManager swrveResourceManager = swrveImp.resourceManager;
                        final JSONArray jsonArray2 = jsonArray;
                        swrveResourceManager.setResourcesFromJSON(jsonArray2);
                        return;
                    }
                    break Label_0107;
                }
                catch (SecurityException ex2) {
                    this.invalidateETag();
                    Log.i("SwrveSDK", "Signature for srcngt2 invalid; could not retrieve data from cache");
                    final HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("name", "Swrve.signature_invalid");
                    this.queueEvent("event", (Map<String, Object>)hashMap, null, false);
                    continue;
                }
                break;
            }
            try {
                final String s = secureCacheEntryForUser;
                final JSONArray jsonArray = new JSONArray(s);
                final SwrveImp swrveImp = this;
                final SwrveResourceManager swrveResourceManager = swrveImp.resourceManager;
                final JSONArray jsonArray2 = jsonArray;
                swrveResourceManager.setResourcesFromJSON(jsonArray2);
                return;
            }
            catch (JSONException ex) {
                Log.e("SwrveSDK", "Could not parse cached json content for resources", (Throwable)ex);
                return;
            }
        }
        this.invalidateETag();
    }
    
    protected void invalidateETag() {
        this.campaignsAndResourcesLastETag = null;
        final SharedPreferences$Editor edit = this.context.get().getSharedPreferences("swrve_prefs", 0).edit();
        edit.remove("campaigns_and_resources_etag");
        edit.commit();
    }
    
    protected void invokeResourceListener() {
        if (this.resourcesListener != null) {
            final Activity activityContext = this.getActivityContext();
            if (activityContext == null) {
                this.resourcesListener.onResourcesUpdated();
                return;
            }
            activityContext.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    SwrveImp.this.resourcesListener.onResourcesUpdated();
                }
            });
        }
    }
    
    protected boolean isActivityFinishing(final Activity activity) {
        return activity.isFinishing();
    }
    
    protected boolean isTooSoonToShowMessageAfterDelay(final Date date) {
        return this.showMessagesAfterDelay != null && date.before(this.showMessagesAfterDelay);
    }
    
    protected boolean isTooSoonToShowMessageAfterLaunch(final Date date) {
        return date.before(this.showMessagesAfterLaunch);
    }
    
    protected SwrveCampaign loadCampaignFromJSON(final JSONObject jsonObject, final Set<String> set) throws JSONException {
        return new SwrveCampaign((SwrveBase<?, ?>)this, jsonObject, set);
    }
    
    @SuppressLint({ "UseSparseArrays" })
    protected void loadCampaignsFromJSON(final JSONObject jsonObject, JSONObject jsonObject2) {
        if (jsonObject == null) {
            Log.i("SwrveSDK", "NULL JSON for campaigns, aborting load.");
        }
        else {
            if (jsonObject.length() == 0) {
                Log.i("SwrveSDK", "Campaign JSON empty, no campaigns downloaded");
                this.campaigns.clear();
                return;
            }
            Log.i("SwrveSDK", "Campaign JSON data: " + jsonObject);
            try {
                if (!jsonObject.has("version")) {
                    return;
                }
                if (!jsonObject.getString("version").equals("1")) {
                    Log.i("SwrveSDK", "Campaign JSON has the wrong version. No campaigns loaded.");
                    return;
                }
            }
            catch (JSONException ex) {
                Log.e("SwrveSDK", "Error parsing campaign JSON", (Throwable)ex);
                return;
            }
            this.cdnRoot = jsonObject.getString("cdn_root");
            Log.i("SwrveSDK", "CDN URL " + this.cdnRoot);
            final JSONObject jsonObject3 = jsonObject.getJSONObject("game_data");
            if (jsonObject3 != null) {
                final Iterator keys = jsonObject3.keys();
                while (keys.hasNext()) {
                    final String s = keys.next();
                    final JSONObject jsonObject4 = jsonObject3.getJSONObject(s);
                    if (jsonObject4.has("app_store_url")) {
                        final String string = jsonObject4.getString("app_store_url");
                        this.appStoreURLs.put(Integer.parseInt(s), (Object)string);
                        if (SwrveHelper.isNullOrEmpty(string)) {
                            Log.e("SwrveSDK", "App store link " + s + " is empty!");
                        }
                        else {
                            Log.i("SwrveSDK", "App store Link " + s + ": " + string);
                        }
                    }
                }
            }
            final JSONObject jsonObject5 = jsonObject.getJSONObject("rules");
            int n;
            if (jsonObject5.has("delay_first_message")) {
                n = jsonObject5.getInt("delay_first_message");
            }
            else {
                n = SwrveImp.DEFAULT_DELAY_FIRST_MESSAGE;
            }
            long messagesLeftToShow;
            if (jsonObject5.has("max_messages_per_session")) {
                messagesLeftToShow = jsonObject5.getLong("max_messages_per_session");
            }
            else {
                messagesLeftToShow = SwrveImp.DEFAULT_MAX_SHOWS;
            }
            int minDelayBetweenMessage;
            if (jsonObject5.has("min_delay_between_messages")) {
                minDelayBetweenMessage = jsonObject5.getInt("min_delay_between_messages");
            }
            else {
                minDelayBetweenMessage = SwrveImp.DEFAULT_MIN_DELAY;
            }
            final Date now = this.getNow();
            this.showMessagesAfterLaunch = SwrveHelper.addTimeInterval(this.initialisedTime, n, 13);
            this.minDelayBetweenMessage = minDelayBetweenMessage;
            this.messagesLeftToShow = messagesLeftToShow;
            Log.i("SwrveSDK", "App rules OK: Delay Seconds: " + n + " Max shows: " + messagesLeftToShow);
            Log.i("SwrveSDK", "Time is " + now.toString() + " show messages after " + this.showMessagesAfterLaunch.toString());
            final Map<Integer, String> map = null;
            Map<Integer, String> map2;
            if (jsonObject.has("qa")) {
                final JSONObject jsonObject6 = jsonObject.getJSONObject("qa");
                final HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
                Log.i("SwrveSDK", "You are a QA user!");
                (this.qaUser = new SwrveQAUser((SwrveBase<?, ?>)this, jsonObject6)).bindToServices();
                map2 = hashMap;
                if (jsonObject6.has("campaigns")) {
                    final JSONArray jsonArray = jsonObject6.getJSONArray("campaigns");
                    int n2 = 0;
                    while (true) {
                        map2 = hashMap;
                        if (n2 >= jsonArray.length()) {
                            break;
                        }
                        final JSONObject jsonObject7 = jsonArray.getJSONObject(n2);
                        final int int1 = jsonObject7.getInt("id");
                        final String string2 = jsonObject7.getString("reason");
                        Log.i("SwrveSDK", "Campaign " + int1 + " not downloaded because: " + string2);
                        hashMap.put(int1, string2);
                        ++n2;
                    }
                }
            }
            else {
                map2 = map;
                if (this.qaUser != null) {
                    this.qaUser.unbindToServices();
                    this.qaUser = null;
                    map2 = map;
                }
            }
            final JSONArray jsonArray2 = jsonObject.getJSONArray("campaigns");
            final ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 0; i < jsonArray2.length(); ++i) {
                list.add(jsonArray2.getJSONObject(i).getInt("id"));
            }
            if (jsonObject2 == null) {
                jsonObject2 = new JSONObject();
            }
            int n3;
            if (this.qaUser != null && this.qaUser.isResetDevice()) {
                n3 = 0;
            }
            else {
                n3 = 1;
            }
            for (int j = this.campaigns.size() - 1; j >= 0; --j) {
                final SwrveBaseCampaign swrveBaseCampaign = this.campaigns.get(j);
                if (!list.contains(swrveBaseCampaign.getId())) {
                    this.campaigns.remove(j);
                }
                else if (n3 != 0) {
                    jsonObject2.put(Integer.toString(swrveBaseCampaign.getId()), (Object)swrveBaseCampaign.createSettings());
                }
            }
            final ArrayList<SwrveBaseCampaign> list2 = new ArrayList<SwrveBaseCampaign>();
            final HashSet<String> set = new HashSet<String>();
            for (int k = 0; k < jsonArray2.length(); ++k) {
                final JSONObject jsonObject8 = jsonArray2.getJSONObject(k);
                final HashSet<String> set2 = new HashSet<String>();
                int n4 = 1;
                boolean supportsDeviceFilter = true;
                String s2 = null;
                String string3 = null;
                if (jsonObject8.has("filters")) {
                    final JSONArray jsonArray3 = jsonObject8.getJSONArray("filters");
                    int n5 = 0;
                    while (true) {
                        s2 = string3;
                        n4 = (supportsDeviceFilter ? 1 : 0);
                        if (n5 >= jsonArray3.length()) {
                            break;
                        }
                        s2 = string3;
                        if ((n4 = (supportsDeviceFilter ? 1 : 0)) == 0) {
                            break;
                        }
                        string3 = jsonArray3.getString(n5);
                        supportsDeviceFilter = this.supportsDeviceFilter(string3);
                        ++n5;
                    }
                }
                if (n4 != 0) {
                    SwrveBaseCampaign swrveBaseCampaign2 = null;
                    if (jsonObject8.has("conversation")) {
                        final int optInt = jsonObject8.optInt("conversation_version", 1);
                        if (optInt <= 2) {
                            swrveBaseCampaign2 = this.loadConversationCampaignFromJSON(jsonObject8, set2);
                        }
                        else {
                            Log.i("SwrveSDK", "Conversation version " + optInt + " cannot be loaded with this SDK version");
                        }
                    }
                    else {
                        swrveBaseCampaign2 = this.loadCampaignFromJSON(jsonObject8, set2);
                    }
                    if (swrveBaseCampaign2 != null) {
                        set.addAll((Collection<?>)set2);
                        if (this.qaUser == null || !this.qaUser.isResetDevice()) {
                            final String string4 = Integer.toString(swrveBaseCampaign2.getId());
                            if (jsonObject2.has(string4)) {
                                final JSONObject jsonObject9 = jsonObject2.getJSONObject(string4);
                                if (jsonObject9 != null) {
                                    swrveBaseCampaign2.loadSettings(jsonObject9);
                                }
                            }
                        }
                        list2.add(swrveBaseCampaign2);
                        Log.i("SwrveSDK", "Got campaign with id " + swrveBaseCampaign2.getId());
                        if (this.qaUser != null) {
                            map2.put(swrveBaseCampaign2.getId(), null);
                        }
                    }
                }
                else {
                    Log.i("SwrveSDK", "Not all requirements were satisfied for this campaign: " + s2);
                }
            }
            if (this.qaUser != null) {
                this.qaUser.talkSession(map2);
            }
            this.downloadAssets(set);
            this.campaigns = new ArrayList<SwrveBaseCampaign>(list2);
        }
    }
    
    protected SwrveConversationCampaign loadConversationCampaignFromJSON(final JSONObject jsonObject, final Set<String> set) throws JSONException {
        return new SwrveConversationCampaign((SwrveBase<?, ?>)this, jsonObject, set);
    }
    
    protected void noMessagesWereShown(final String s, final String s2) {
        Log.i("SwrveSDK", "Not showing message for " + s + ": " + s2);
        if (this.qaUser != null) {
            this.qaUser.triggerFailure(s, s2);
        }
    }
    
    protected void openLocalStorageConnection() {
        try {
            this.cachedLocalStorage.setSecondaryStorage(this.createLocalStorage());
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Error opening database", (Throwable)ex);
        }
    }
    
    protected void postBatchRequest(final C c, final String s, final IPostBatchRequestListener postBatchRequestListener) {
        this.restClient.post(c.getEventsUrl() + "/1/batch", s, new IRESTResponseListener() {
            @Override
            public void onException(final Exception ex) {
            }
            
            @Override
            public void onResponse(final RESTResponse restResponse) {
                if (SwrveHelper.userErrorResponseCode(restResponse.responseCode)) {
                    Log.e("SwrveSDK", "Error sending events to Swrve: " + restResponse.responseBody);
                }
                else if (SwrveHelper.successResponseCode(restResponse.responseCode)) {
                    Log.i("SwrveSDK", "Events sent to Swrve");
                }
                postBatchRequestListener.onResponse(restResponse.responseBody != null);
            }
        });
    }
    
    protected void queueDeviceInfoNow(final boolean b) {
        this.storageExecutorExecute(new Runnable() {
            final /* synthetic */ SwrveBase val$swrve = (SwrveBase)this;
            
            @Override
            public void run() {
                SwrveImp.this.userUpdate(this.val$swrve.getDeviceInfo());
                if (b) {
                    SwrveImp.this.storageExecutorExecute(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("SwrveSDK", "Sending device info");
                            Runnable.this.val$swrve.sendQueuedEvents();
                        }
                    });
                }
            }
        });
    }
    
    protected void queueEvent(final String s, final Map<String, Object> map, final Map<String, String> map2) {
        this.queueEvent(s, map, map2, true);
    }
    
    protected void queueEvent(final String s, final Map<String, Object> map, final Map<String, String> map2, final boolean b) {
        try {
            this.storageExecutorExecute(new QueueEventRunnable(s, map, map2));
            if (b && this.eventListener != null) {
                this.eventListener.onEvent(EventHelper.getEventName(s, map));
            }
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Unable to queue event", (Throwable)ex);
        }
    }
    
    protected void queueSessionStart() {
        this.queueEvent("session_start", null, null);
    }
    
    protected void removeCurrentDialog(Activity activity) {
        if (this.currentDialog != null) {
            final SwrveDialog swrveDialog = this.currentDialog.get();
            if (swrveDialog == null || !swrveDialog.isShowing()) {
                this.currentDialog = null;
                return;
            }
            final Activity ownerActivity = swrveDialog.getOwnerActivity();
            if (activity == null || activity == ownerActivity) {
                (SwrveImp.messageDisplayed = swrveDialog.getMessage()).setMessageController(null);
                SwrveImp.lastMessageDestroyed = new Date().getTime();
                activity = ownerActivity;
                Activity activityContext;
                if ((activityContext = activity) == null) {
                    activityContext = this.getActivityContext();
                }
                if (activityContext != null) {
                    activityContext.runOnUiThread((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            swrveDialog.dismiss();
                        }
                    });
                }
                else {
                    swrveDialog.dismiss();
                }
                this.currentDialog = null;
            }
        }
    }
    
    protected boolean restClientExecutorExecute(final Runnable runnable) {
        try {
            if (!this.restClientExecutor.isShutdown()) {
                this.restClientExecutor.execute(SwrveRunnables.withoutExceptions(runnable));
                return true;
            }
            Log.i("SwrveSDK", "Trying to schedule a rest execution while shutdown");
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Error while scheduling a rest execution", (Throwable)ex);
        }
        return false;
    }
    
    protected void saveCampaignSettings() {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
            for (final SwrveBaseCampaign swrveBaseCampaign : this.campaigns) {
                jsonObject.put(Integer.toString(swrveBaseCampaign.getId()), (Object)swrveBaseCampaign.createSettings());
            }
        }
        catch (JSONException ex) {
            Log.e("SwrveSDK", "Error saving campaigns settings", (Throwable)ex);
            return;
        }
        this.storageExecutorExecute(new Runnable() {
            final /* synthetic */ String val$serializedSettings = jsonObject.toString();
            
            @Override
            public void run() {
                final MemoryCachedLocalStorage cachedLocalStorage = SwrveImp.this.cachedLocalStorage;
                cachedLocalStorage.setCacheEntryForUser(SwrveImp.this.userId, "SwrveCampaignSettings", this.val$serializedSettings);
                if (cachedLocalStorage.getSecondaryStorage() != null) {
                    cachedLocalStorage.getSecondaryStorage().setCacheEntryForUser(SwrveImp.this.userId, "SwrveCampaignSettings", this.val$serializedSettings);
                }
                Log.i("SwrveSDK", "Saved campaigns in cache");
            }
        });
    }
    
    protected void saveCampaignsInCache(final JSONObject jsonObject) {
        this.storageExecutorExecute(new Runnable() {
            @Override
            public void run() {
                SwrveImp.this.cachedLocalStorage.setAndFlushSecureSharedEntryForUser(SwrveImp.this.userId, "CMCC2", jsonObject.toString(), SwrveImp.this.getUniqueKey());
            }
        });
    }
    
    protected void saveCurrentOrientation(final Context context) {
        if (context == null) {
            return;
        }
        try {
            this.previousOrientation = ((WindowManager)context.getSystemService("window")).getDefaultDisplay().getRotation();
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Could not obtain device orientation", (Throwable)ex);
        }
    }
    
    protected void saveResourcesInCache(final JSONArray jsonArray) {
        this.storageExecutorExecute(new Runnable() {
            @Override
            public void run() {
                SwrveImp.this.cachedLocalStorage.setAndFlushSecureSharedEntryForUser(SwrveImp.this.userId, "srcngt2", jsonArray.toString(), SwrveImp.this.getUniqueKey());
            }
        });
    }
    
    protected void saveUniqueUserId(final Context context, final String s) {
        final SharedPreferences$Editor edit = context.getSharedPreferences("swrve_prefs", 0).edit();
        edit.putString("userId", s);
        edit.commit();
    }
    
    protected void sendCrashlyticsMetadata() {
        try {
            final Class<?> forName = Class.forName("com.crashlytics.android.Crashlytics");
            if (forName != null) {
                final Method method = forName.getMethod("setString", String.class, String.class);
                if (method != null) {
                    method.invoke(null, "Swrve_version", SwrveImp.version);
                }
            }
        }
        catch (Exception ex) {
            Log.i("SwrveSDK", "Could not set Crashlytics metadata");
        }
    }
    
    protected void showPreviousMessage() {
        if (this.config.isTalkEnabled() && SwrveImp.messageDisplayed != null && this.messageListener != null) {
            if (this.getNow().getTime() < SwrveImp.lastMessageDestroyed + SwrveImp.MESSAGE_REAPPEAR_TIMEOUT) {
                SwrveImp.messageDisplayed.setMessageController((SwrveBase<?, ?>)this);
                this.messageListener.onMessage(SwrveImp.messageDisplayed, false);
            }
            SwrveImp.messageDisplayed = null;
        }
    }
    
    protected void startCampaignsAndResourcesTimer(final boolean b) {
        if (!this.config.isAutoDownloadCampaingsAndResources()) {
            return;
        }
        final SwrveBase swrveBase = (SwrveBase)this;
        if (this.campaignsAndResourcesExecutor != null) {
            this.campaignsAndResourcesExecutor.shutdown();
        }
        if (b) {
            swrveBase.refreshCampaignsAndResources();
        }
        this.eventsWereSent = true;
        (this.campaignsAndResourcesExecutor = new ScheduledThreadPoolExecutor(1)).setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        this.campaignsAndResourcesExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                SwrveImp.this.checkForCampaignAndResourcesUpdates();
            }
        }, 0L, this.campaignsAndResourcesFlushFrequency, TimeUnit.MILLISECONDS);
    }
    
    protected boolean storageExecutorExecute(final Runnable runnable) {
        try {
            if (!this.storageExecutor.isShutdown()) {
                this.storageExecutor.execute(SwrveRunnables.withoutExceptions(runnable));
                return true;
            }
            Log.i("SwrveSDK", "Trying to schedule a storage execution while shutdown");
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Error while scheduling a storage execution", (Throwable)ex);
        }
        return false;
    }
    
    protected void unbindAndShutdown(final Activity activity) {
        final int decrementAndGet = this.bindCounter.decrementAndGet();
        this.removeCurrentDialog(activity);
        if (decrementAndGet == 0) {
            this.activityContext = null;
            if (this.mustCleanInstance) {
                ((SwrveBase)this).shutdown();
            }
        }
    }
    
    protected void updateCampaigns(final JSONObject jsonObject, final JSONObject jsonObject2) {
        this.loadCampaignsFromJSON(jsonObject, jsonObject2);
    }
    
    protected void userUpdate(final JSONObject jsonObject) {
        if (jsonObject != null && jsonObject.length() != 0) {
            final HashMap<String, JSONObject> hashMap = new HashMap<String, JSONObject>();
            hashMap.put("attributes", jsonObject);
            this.queueEvent("user", (Map<String, Object>)hashMap, null);
        }
    }
    
    protected class DisplayMessageRunnable implements Runnable
    {
        private Activity activity;
        private boolean firstTime;
        private SwrveMessage message;
        private SwrveBase<?, ?> sdk;
        
        public DisplayMessageRunnable(final SwrveBase<?, ?> sdk, final Activity activity, final SwrveMessage message, final boolean firstTime) {
            this.sdk = sdk;
            this.activity = activity;
            this.message = message;
            this.firstTime = firstTime;
        }
        
        @Override
        public void run() {
            SwrveDialog swrveDialog = null;
            try {
                Log.d("SwrveSDK", "Called show dialog");
                if (SwrveImp.this.currentDialog != null) {
                    swrveDialog = SwrveImp.this.currentDialog.get();
                }
                if (swrveDialog == null || !swrveDialog.isShowing()) {
                    final SwrveOrientation deviceOrientation = SwrveImp.this.getDeviceOrientation();
                    Log.d("SwrveSDK", "Trying to show dialog with orientation " + deviceOrientation);
                    final SwrveDialog swrveDialog2 = new SwrveDialog(this.activity, this.message, SwrveMessageViewFactory.getInstance().buildLayout((Context)this.activity, this.message, deviceOrientation, SwrveImp.this.previousOrientation, SwrveImp.this.installButtonListener, SwrveImp.this.customButtonListener, this.firstTime, SwrveImp.this.config.getMinSampleSize()), R.style.SwrveDialogTheme);
                    swrveDialog2.setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
                        public void onDismiss(final DialogInterface dialogInterface) {
                            if (SwrveImp.this.currentDialog != null && (SwrveDialog)SwrveImp.this.currentDialog.get() == dialogInterface) {
                                SwrveImp.this.currentDialog = null;
                            }
                        }
                    });
                    SwrveImp.this.saveCurrentOrientation((Context)this.activity);
                    if (SwrveImp.this.dialogListener != null) {
                        SwrveImp.this.dialogListener.onDialog(swrveDialog2);
                    }
                    else {
                        this.sdk.currentDialog = new WeakReference<SwrveDialog>(swrveDialog2);
                        swrveDialog2.show();
                    }
                }
                this.activity = null;
                this.sdk = null;
                this.message = null;
            }
            catch (Exception ex) {
                Log.w("SwrveSDK", "Couldn't create a SwrveMessageView", (Throwable)ex);
            }
        }
    }
    
    private class QueueEventRunnable implements Runnable
    {
        private String eventType;
        private Map<String, Object> parameters;
        private Map<String, String> payload;
        
        public QueueEventRunnable(final String eventType, final Map<String, Object> parameters, final Map<String, String> payload) {
            this.eventType = eventType;
            this.parameters = parameters;
            this.payload = payload;
        }
        
        @Override
        public void run() {
            try {
                final String eventAsJSON = EventHelper.eventAsJSON(this.eventType, this.parameters, this.payload, SwrveImp.this.cachedLocalStorage);
                this.parameters = null;
                this.payload = null;
                SwrveImp.this.cachedLocalStorage.addEvent(eventAsJSON);
                Log.i("SwrveSDK", this.eventType + " event queued");
            }
            catch (JSONException ex) {
                Log.e("SwrveSDK", "Parameter or payload data not encodable as JSON", (Throwable)ex);
            }
            catch (Exception ex2) {
                Log.e("SwrveSDK", "Unable to insert into local storage", (Throwable)ex2);
            }
        }
    }
}
