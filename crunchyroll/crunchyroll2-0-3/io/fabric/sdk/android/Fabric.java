// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android;

import java.util.Arrays;
import android.os.Looper;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.CountDownLatch;
import io.fabric.sdk.android.services.concurrency.UnmetDependencyException;
import io.fabric.sdk.android.services.concurrency.Task;
import io.fabric.sdk.android.services.concurrency.DependsOn;
import android.os.Bundle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import io.fabric.sdk.android.services.concurrency.PriorityThreadPoolExecutor;
import android.os.Handler;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import io.fabric.sdk.android.services.common.IdManager;
import java.util.concurrent.ExecutorService;
import android.content.Context;
import android.app.Activity;
import java.lang.ref.WeakReference;

public class Fabric
{
    static final Logger DEFAULT_LOGGER;
    static volatile Fabric singleton;
    private WeakReference<Activity> activity;
    private ActivityLifecycleManager activityLifecycleManager;
    private final Context context;
    final boolean debuggable;
    private final ExecutorService executorService;
    private final IdManager idManager;
    private final InitializationCallback<Fabric> initializationCallback;
    private AtomicBoolean initialized;
    private final InitializationCallback<?> kitInitializationCallback;
    private final Map<Class<? extends Kit>, Kit> kits;
    final Logger logger;
    private final Handler mainHandler;
    
    static {
        DEFAULT_LOGGER = new DefaultLogger();
    }
    
    Fabric(final Context context, final Map<Class<? extends Kit>, Kit> kits, final PriorityThreadPoolExecutor executorService, final Handler mainHandler, final Logger logger, final boolean debuggable, final InitializationCallback initializationCallback, final IdManager idManager) {
        this.context = context;
        this.kits = kits;
        this.executorService = executorService;
        this.mainHandler = mainHandler;
        this.logger = logger;
        this.debuggable = debuggable;
        this.initializationCallback = (InitializationCallback<Fabric>)initializationCallback;
        this.initialized = new AtomicBoolean(false);
        this.kitInitializationCallback = this.createKitInitializationCallback(kits.size());
        this.idManager = idManager;
    }
    
    private static void addToKitMap(final Map<Class<? extends Kit>, Kit> map, final Collection<? extends Kit> collection) {
        for (final Kit kit : collection) {
            map.put(kit.getClass(), kit);
            if (kit instanceof KitGroup) {
                addToKitMap(map, ((KitGroup)kit).getKits());
            }
        }
    }
    
    private Activity extractActivity(final Context context) {
        if (context instanceof Activity) {
            return (Activity)context;
        }
        return null;
    }
    
    public static <T extends Kit> T getKit(final Class<T> clazz) {
        return (T)singleton().kits.get(clazz);
    }
    
    private static Map<Class<? extends Kit>, Kit> getKitMap(final Collection<? extends Kit> collection) {
        final HashMap hashMap = new HashMap(collection.size());
        addToKitMap(hashMap, collection);
        return (Map<Class<? extends Kit>, Kit>)hashMap;
    }
    
    public static Logger getLogger() {
        if (Fabric.singleton == null) {
            return Fabric.DEFAULT_LOGGER;
        }
        return Fabric.singleton.logger;
    }
    
    private void init() {
        this.setCurrentActivity(this.extractActivity(this.context));
        (this.activityLifecycleManager = new ActivityLifecycleManager(this.context)).registerCallbacks((ActivityLifecycleManager.Callbacks)new ActivityLifecycleManager.Callbacks() {
            @Override
            public void onActivityCreated(final Activity currentActivity, final Bundle bundle) {
                Fabric.this.setCurrentActivity(currentActivity);
            }
            
            @Override
            public void onActivityResumed(final Activity currentActivity) {
                Fabric.this.setCurrentActivity(currentActivity);
            }
            
            @Override
            public void onActivityStarted(final Activity currentActivity) {
                Fabric.this.setCurrentActivity(currentActivity);
            }
        });
        this.initializeKits(this.context);
    }
    
    public static boolean isDebuggable() {
        return Fabric.singleton != null && Fabric.singleton.debuggable;
    }
    
    private static void setFabric(final Fabric singleton) {
        (Fabric.singleton = singleton).init();
    }
    
    static Fabric singleton() {
        if (Fabric.singleton == null) {
            throw new IllegalStateException("Must Initialize Fabric before using singleton()");
        }
        return Fabric.singleton;
    }
    
    public static Fabric with(final Context context, final Kit... array) {
        Label_0036: {
            if (Fabric.singleton != null) {
                break Label_0036;
            }
            synchronized (Fabric.class) {
                if (Fabric.singleton == null) {
                    setFabric(new Builder(context).kits(array).build());
                }
                return Fabric.singleton;
            }
        }
    }
    
    void addAnnotatedDependencies(final Map<Class<? extends Kit>, Kit> map, final Kit kit) {
        final DependsOn dependsOn = kit.getClass().getAnnotation(DependsOn.class);
        if (dependsOn != null) {
            final Class<?>[] value = dependsOn.value();
            for (int length = value.length, i = 0; i < length; ++i) {
                final Class<?> clazz = value[i];
                if (clazz.isInterface()) {
                    for (final Kit kit2 : map.values()) {
                        if (clazz.isAssignableFrom(kit2.getClass())) {
                            kit.initializationTask.addDependency(kit2.initializationTask);
                        }
                    }
                }
                else {
                    if (map.get(clazz) == null) {
                        throw new UnmetDependencyException("Referenced Kit was null, does the kit exist?");
                    }
                    kit.initializationTask.addDependency(map.get(clazz).initializationTask);
                }
            }
        }
    }
    
    InitializationCallback<?> createKitInitializationCallback(final int n) {
        return (InitializationCallback<?>)new InitializationCallback() {
            final CountDownLatch kitInitializedLatch = new CountDownLatch(n);
            
            @Override
            public void failure(final Exception ex) {
                Fabric.this.initializationCallback.failure(ex);
            }
            
            @Override
            public void success(final Object o) {
                this.kitInitializedLatch.countDown();
                if (this.kitInitializedLatch.getCount() == 0L) {
                    Fabric.this.initialized.set(true);
                    Fabric.this.initializationCallback.success(Fabric.this);
                }
            }
        };
    }
    
    public ActivityLifecycleManager getActivityLifecycleManager() {
        return this.activityLifecycleManager;
    }
    
    public Activity getCurrentActivity() {
        if (this.activity != null) {
            return this.activity.get();
        }
        return null;
    }
    
    public ExecutorService getExecutorService() {
        return this.executorService;
    }
    
    public String getIdentifier() {
        return "io.fabric.sdk.android:fabric";
    }
    
    public Collection<Kit> getKits() {
        return (Collection<Kit>)this.kits.values();
    }
    
    Future<Map<String, KitInfo>> getKitsFinderFuture(final Context context) {
        return this.getExecutorService().submit((Callable<Map<String, KitInfo>>)new FabricKitsFinder(context.getPackageCodePath()));
    }
    
    public String getVersion() {
        return "1.3.6.79";
    }
    
    void initializeKits(final Context context) {
        final Future<Map<String, KitInfo>> kitsFinderFuture = this.getKitsFinderFuture(context);
        final Collection<Kit> kits = this.getKits();
        final Onboarding onboarding = new Onboarding(kitsFinderFuture, kits);
        final ArrayList list = new ArrayList<Kit>(kits);
        Collections.sort((List<Comparable>)list);
        onboarding.injectParameters(context, this, InitializationCallback.EMPTY, this.idManager);
        final Iterator<Kit<?>> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next().injectParameters(context, this, this.kitInitializationCallback, this.idManager);
        }
        onboarding.initialize();
        StringBuilder append;
        if (getLogger().isLoggable("Fabric", 3)) {
            append = new StringBuilder("Initializing ").append(this.getIdentifier()).append(" [Version: ").append(this.getVersion()).append("], with the following kits:\n");
        }
        else {
            append = null;
        }
        for (final Kit kit : list) {
            kit.initializationTask.addDependency(onboarding.initializationTask);
            this.addAnnotatedDependencies(this.kits, kit);
            kit.initialize();
            if (append != null) {
                append.append(kit.getIdentifier()).append(" [Version: ").append(kit.getVersion()).append("]\n");
            }
        }
        if (append != null) {
            getLogger().d("Fabric", append.toString());
        }
    }
    
    public Fabric setCurrentActivity(final Activity activity) {
        this.activity = new WeakReference<Activity>(activity);
        return this;
    }
    
    public static class Builder
    {
        private String appIdentifier;
        private String appInstallIdentifier;
        private final Context context;
        private boolean debuggable;
        private Handler handler;
        private InitializationCallback<Fabric> initializationCallback;
        private Kit[] kits;
        private Logger logger;
        private PriorityThreadPoolExecutor threadPoolExecutor;
        
        public Builder(final Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
        }
        
        public Fabric build() {
            if (this.threadPoolExecutor == null) {
                this.threadPoolExecutor = PriorityThreadPoolExecutor.create();
            }
            if (this.handler == null) {
                this.handler = new Handler(Looper.getMainLooper());
            }
            if (this.logger == null) {
                if (this.debuggable) {
                    this.logger = new DefaultLogger(3);
                }
                else {
                    this.logger = new DefaultLogger();
                }
            }
            if (this.appIdentifier == null) {
                this.appIdentifier = this.context.getPackageName();
            }
            if (this.initializationCallback == null) {
                this.initializationCallback = (InitializationCallback<Fabric>)InitializationCallback.EMPTY;
            }
            Map<Class<? extends Kit>, Kit> access$000;
            if (this.kits == null) {
                access$000 = new HashMap<Class<? extends Kit>, Kit>();
            }
            else {
                access$000 = getKitMap(Arrays.asList((Kit[])this.kits));
            }
            return new Fabric(this.context, (Map<Class<? extends Kit>, Kit>)access$000, this.threadPoolExecutor, this.handler, this.logger, this.debuggable, this.initializationCallback, new IdManager(this.context, this.appIdentifier, this.appInstallIdentifier, (Collection<Kit>)access$000.values()));
        }
        
        public Builder kits(final Kit... kits) {
            if (this.kits != null) {
                throw new IllegalStateException("Kits already set.");
            }
            this.kits = kits;
            return this;
        }
    }
}
