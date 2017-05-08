// 
// Decompiled by Procyon v0.5.30
// 

package de.greenrobot.event;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.ArrayList;
import android.util.Log;
import android.os.Looper;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.List;
import java.util.Map;

public class EventBus
{
    private static final EventBusBuilder DEFAULT_BUILDER;
    public static String TAG;
    static volatile EventBus defaultInstance;
    private static final Map<Class<?>, List<Class<?>>> eventTypesCache;
    private final AsyncPoster asyncPoster;
    private final BackgroundPoster backgroundPoster;
    private final ThreadLocal<PostingThreadState> currentPostingThreadState;
    private final boolean eventInheritance;
    private final ExecutorService executorService;
    private final boolean logNoSubscriberMessages;
    private final boolean logSubscriberExceptions;
    private final HandlerPoster mainThreadPoster;
    private final boolean sendNoSubscriberEvent;
    private final boolean sendSubscriberExceptionEvent;
    private final Map<Class<?>, Object> stickyEvents;
    private final SubscriberMethodFinder subscriberMethodFinder;
    private final Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;
    private final boolean throwSubscriberException;
    private final Map<Object, List<Class<?>>> typesBySubscriber;
    
    static {
        EventBus.TAG = "Event";
        DEFAULT_BUILDER = new EventBusBuilder();
        eventTypesCache = new HashMap<Class<?>, List<Class<?>>>();
    }
    
    public EventBus() {
        this(EventBus.DEFAULT_BUILDER);
    }
    
    EventBus(final EventBusBuilder eventBusBuilder) {
        this.currentPostingThreadState = new ThreadLocal<PostingThreadState>() {
            @Override
            protected PostingThreadState initialValue() {
                return new PostingThreadState();
            }
        };
        this.subscriptionsByEventType = new HashMap<Class<?>, CopyOnWriteArrayList<Subscription>>();
        this.typesBySubscriber = new HashMap<Object, List<Class<?>>>();
        this.stickyEvents = new ConcurrentHashMap<Class<?>, Object>();
        this.mainThreadPoster = new HandlerPoster(this, Looper.getMainLooper(), 10);
        this.backgroundPoster = new BackgroundPoster(this);
        this.asyncPoster = new AsyncPoster(this);
        this.subscriberMethodFinder = new SubscriberMethodFinder(eventBusBuilder.skipMethodVerificationForClasses);
        this.logSubscriberExceptions = eventBusBuilder.logSubscriberExceptions;
        this.logNoSubscriberMessages = eventBusBuilder.logNoSubscriberMessages;
        this.sendSubscriberExceptionEvent = eventBusBuilder.sendSubscriberExceptionEvent;
        this.sendNoSubscriberEvent = eventBusBuilder.sendNoSubscriberEvent;
        this.throwSubscriberException = eventBusBuilder.throwSubscriberException;
        this.eventInheritance = eventBusBuilder.eventInheritance;
        this.executorService = eventBusBuilder.executorService;
    }
    
    static void addInterfaces(final List<Class<?>> list, final Class<?>[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final Class<?> clazz = array[i];
            if (!list.contains(clazz)) {
                list.add(clazz);
                addInterfaces(list, clazz.getInterfaces());
            }
        }
    }
    
    public static EventBus getDefault() {
        Label_0028: {
            if (EventBus.defaultInstance != null) {
                break Label_0028;
            }
            synchronized (EventBus.class) {
                if (EventBus.defaultInstance == null) {
                    EventBus.defaultInstance = new EventBus();
                }
                return EventBus.defaultInstance;
            }
        }
    }
    
    private void handleSubscriberException(final Subscription subscription, final Object o, final Throwable t) {
        if (o instanceof SubscriberExceptionEvent) {
            if (this.logSubscriberExceptions) {
                Log.e(EventBus.TAG, "SubscriberExceptionEvent subscriber " + subscription.subscriber.getClass() + " threw an exception", t);
                final SubscriberExceptionEvent subscriberExceptionEvent = (SubscriberExceptionEvent)o;
                Log.e(EventBus.TAG, "Initial event " + subscriberExceptionEvent.causingEvent + " caused exception in " + subscriberExceptionEvent.causingSubscriber, subscriberExceptionEvent.throwable);
            }
        }
        else {
            if (this.throwSubscriberException) {
                throw new EventBusException("Invoking subscriber failed", t);
            }
            if (this.logSubscriberExceptions) {
                Log.e(EventBus.TAG, "Could not dispatch event: " + o.getClass() + " to subscribing class " + subscription.subscriber.getClass(), t);
            }
            if (this.sendSubscriberExceptionEvent) {
                this.post(new SubscriberExceptionEvent(this, t, o, subscription.subscriber));
            }
        }
    }
    
    private List<Class<?>> lookupAllEventTypes(final Class<?> clazz) {
        synchronized (EventBus.eventTypesCache) {
            List<Class<?>> list;
            if ((list = EventBus.eventTypesCache.get(clazz)) == null) {
                final ArrayList<Class<?>> list2 = new ArrayList<Class<?>>();
                for (Class<?> superclass = clazz; superclass != null; superclass = superclass.getSuperclass()) {
                    list2.add(superclass);
                    addInterfaces(list2, superclass.getInterfaces());
                }
                EventBus.eventTypesCache.put(clazz, list2);
                list = list2;
            }
            return list;
        }
    }
    
    private void postSingleEvent(final Object o, final PostingThreadState postingThreadState) throws Error {
        final Class<?> class1 = o.getClass();
        boolean b = false;
        boolean postSingleEventForEventType;
        if (this.eventInheritance) {
            final List<Class<?>> lookupAllEventTypes = this.lookupAllEventTypes(class1);
            final int size = lookupAllEventTypes.size();
            int n = 0;
            while (true) {
                postSingleEventForEventType = b;
                if (n >= size) {
                    break;
                }
                b |= this.postSingleEventForEventType(o, postingThreadState, lookupAllEventTypes.get(n));
                ++n;
            }
        }
        else {
            postSingleEventForEventType = this.postSingleEventForEventType(o, postingThreadState, class1);
        }
        if (!postSingleEventForEventType) {
            if (this.logNoSubscriberMessages) {
                Log.d(EventBus.TAG, "No subscribers registered for event " + class1);
            }
            if (this.sendNoSubscriberEvent && class1 != NoSubscriberEvent.class && class1 != SubscriberExceptionEvent.class) {
                this.post(new NoSubscriberEvent(this, o));
            }
        }
    }
    
    private boolean postSingleEventForEventType(final Object p0, final PostingThreadState p1, final Class<?> p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: iconst_0       
        //     1: istore          5
        //     3: aload_0        
        //     4: monitorenter   
        //     5: aload_0        
        //     6: getfield        de/greenrobot/event/EventBus.subscriptionsByEventType:Ljava/util/Map;
        //     9: aload_3        
        //    10: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    15: checkcast       Ljava/util/concurrent/CopyOnWriteArrayList;
        //    18: astore_3       
        //    19: aload_0        
        //    20: monitorexit    
        //    21: iload           5
        //    23: istore          4
        //    25: aload_3        
        //    26: ifnull          116
        //    29: iload           5
        //    31: istore          4
        //    33: aload_3        
        //    34: invokevirtual   java/util/concurrent/CopyOnWriteArrayList.isEmpty:()Z
        //    37: ifne            116
        //    40: aload_3        
        //    41: invokevirtual   java/util/concurrent/CopyOnWriteArrayList.iterator:()Ljava/util/Iterator;
        //    44: astore_3       
        //    45: aload_3        
        //    46: invokeinterface java/util/Iterator.hasNext:()Z
        //    51: ifeq            113
        //    54: aload_3        
        //    55: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    60: checkcast       Lde/greenrobot/event/Subscription;
        //    63: astore          6
        //    65: aload_2        
        //    66: aload_1        
        //    67: putfield        de/greenrobot/event/EventBus$PostingThreadState.event:Ljava/lang/Object;
        //    70: aload_2        
        //    71: aload           6
        //    73: putfield        de/greenrobot/event/EventBus$PostingThreadState.subscription:Lde/greenrobot/event/Subscription;
        //    76: aload_0        
        //    77: aload           6
        //    79: aload_1        
        //    80: aload_2        
        //    81: getfield        de/greenrobot/event/EventBus$PostingThreadState.isMainThread:Z
        //    84: invokespecial   de/greenrobot/event/EventBus.postToSubscription:(Lde/greenrobot/event/Subscription;Ljava/lang/Object;Z)V
        //    87: aload_2        
        //    88: getfield        de/greenrobot/event/EventBus$PostingThreadState.canceled:Z
        //    91: istore          4
        //    93: aload_2        
        //    94: aconst_null    
        //    95: putfield        de/greenrobot/event/EventBus$PostingThreadState.event:Ljava/lang/Object;
        //    98: aload_2        
        //    99: aconst_null    
        //   100: putfield        de/greenrobot/event/EventBus$PostingThreadState.subscription:Lde/greenrobot/event/Subscription;
        //   103: aload_2        
        //   104: iconst_0       
        //   105: putfield        de/greenrobot/event/EventBus$PostingThreadState.canceled:Z
        //   108: iload           4
        //   110: ifeq            45
        //   113: iconst_1       
        //   114: istore          4
        //   116: iload           4
        //   118: ireturn        
        //   119: astore_1       
        //   120: aload_0        
        //   121: monitorexit    
        //   122: aload_1        
        //   123: athrow         
        //   124: astore_1       
        //   125: aload_2        
        //   126: aconst_null    
        //   127: putfield        de/greenrobot/event/EventBus$PostingThreadState.event:Ljava/lang/Object;
        //   130: aload_2        
        //   131: aconst_null    
        //   132: putfield        de/greenrobot/event/EventBus$PostingThreadState.subscription:Lde/greenrobot/event/Subscription;
        //   135: aload_2        
        //   136: iconst_0       
        //   137: putfield        de/greenrobot/event/EventBus$PostingThreadState.canceled:Z
        //   140: aload_1        
        //   141: athrow         
        //    Signature:
        //  (Ljava/lang/Object;Lde/greenrobot/event/EventBus$PostingThreadState;Ljava/lang/Class<*>;)Z
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  5      21     119    124    Any
        //  76     93     124    142    Any
        //  120    122    119    124    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0113:
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
    
    private void postToSubscription(final Subscription subscription, final Object o, final boolean b) {
        switch (subscription.subscriberMethod.threadMode) {
            default: {
                throw new IllegalStateException("Unknown thread mode: " + subscription.subscriberMethod.threadMode);
            }
            case PostThread: {
                this.invokeSubscriber(subscription, o);
            }
            case MainThread: {
                if (b) {
                    this.invokeSubscriber(subscription, o);
                    return;
                }
                this.mainThreadPoster.enqueue(subscription, o);
            }
            case BackgroundThread: {
                if (b) {
                    this.backgroundPoster.enqueue(subscription, o);
                    return;
                }
                this.invokeSubscriber(subscription, o);
            }
            case Async: {
                this.asyncPoster.enqueue(subscription, o);
            }
        }
    }
    
    private void register(final Object o, final boolean b, final int n) {
        synchronized (this) {
            final Iterator<SubscriberMethod> iterator = this.subscriberMethodFinder.findSubscriberMethods(o.getClass()).iterator();
            while (iterator.hasNext()) {
                this.subscribe(o, iterator.next(), b, n);
            }
        }
    }
    // monitorexit(this)
    
    private void subscribe(Object stickyEvents, final SubscriberMethod subscriberMethod, boolean b, int n) {
        final Class<?> eventType = subscriberMethod.eventType;
        final CopyOnWriteArrayList<Subscription> list = this.subscriptionsByEventType.get(eventType);
        final Subscription subscription = new Subscription(stickyEvents, subscriberMethod, n);
    Label_0268_Outer:
        while (true) {
            while (true) {
                Object value = null;
                final int size;
                Label_0070: {
                    Label_0061: {
                        if (list == null) {
                            value = new CopyOnWriteArrayList<Subscription>();
                            this.subscriptionsByEventType.put(eventType, (CopyOnWriteArrayList<Subscription>)value);
                            break Label_0061;
                        }
                        Label_0213: {
                            break Label_0213;
                            while (true) {
                                stickyEvents = this.stickyEvents;
                                while (true) {
                                    Label_0282: {
                                        synchronized (stickyEvents) {
                                            value = this.stickyEvents.get(eventType);
                                            // monitorexit(stickyEvents)
                                            if (value != null) {
                                                if (Looper.getMainLooper() != Looper.myLooper()) {
                                                    break Label_0282;
                                                }
                                                b = true;
                                                this.postToSubscription(subscription, value, b);
                                            }
                                            return;
                                            ++n;
                                            break Label_0070;
                                            // iftrue(Label_0061:, !list.contains((Object)subscription))
                                            throw new EventBusException("Subscriber " + stickyEvents.getClass() + " already registered to event " + eventType);
                                        }
                                    }
                                    b = false;
                                    continue Label_0268_Outer;
                                }
                            }
                        }
                    }
                    size = ((CopyOnWriteArrayList)value).size();
                    n = 0;
                }
                if (n <= size) {
                    if (n != size && subscription.priority <= ((CopyOnWriteArrayList<Subscription>)value).get(n).priority) {
                        continue;
                    }
                    ((CopyOnWriteArrayList<Subscription>)value).add(n, subscription);
                }
                break;
            }
            List<Class<?>> list2;
            if ((list2 = this.typesBySubscriber.get(stickyEvents)) == null) {
                list2 = new ArrayList<Class<?>>();
                this.typesBySubscriber.put(stickyEvents, list2);
            }
            list2.add(eventType);
            if (b) {
                continue Label_0268_Outer;
            }
            break;
        }
    }
    
    private void unubscribeByEventType(final Object o, final Class<?> clazz) {
        final CopyOnWriteArrayList<Subscription> list = this.subscriptionsByEventType.get(clazz);
        if (list != null) {
            int n;
            int n2;
            for (int size = list.size(), i = 0; i < size; i = n + 1, size = n2) {
                final Subscription subscription = list.get(i);
                n = i;
                n2 = size;
                if (subscription.subscriber == o) {
                    subscription.active = false;
                    list.remove(i);
                    n = i - 1;
                    n2 = size - 1;
                }
            }
        }
    }
    
    ExecutorService getExecutorService() {
        return this.executorService;
    }
    
    void invokeSubscriber(final PendingPost pendingPost) {
        final Object event = pendingPost.event;
        final Subscription subscription = pendingPost.subscription;
        PendingPost.releasePendingPost(pendingPost);
        if (subscription.active) {
            this.invokeSubscriber(subscription, event);
        }
    }
    
    void invokeSubscriber(final Subscription subscription, final Object o) {
        try {
            subscription.subscriberMethod.method.invoke(subscription.subscriber, o);
        }
        catch (InvocationTargetException ex) {
            this.handleSubscriberException(subscription, o, ex.getCause());
        }
        catch (IllegalAccessException ex2) {
            throw new IllegalStateException("Unexpected exception", ex2);
        }
    }
    
    public void post(final Object o) {
        final PostingThreadState postingThreadState = this.currentPostingThreadState.get();
        final List<Object> eventQueue = postingThreadState.eventQueue;
        eventQueue.add(o);
        if (!postingThreadState.isPosting) {
            postingThreadState.isMainThread = (Looper.getMainLooper() == Looper.myLooper());
            postingThreadState.isPosting = true;
            if (postingThreadState.canceled) {
                throw new EventBusException("Internal error. Abort state was not reset");
            }
            try {
                while (!eventQueue.isEmpty()) {
                    this.postSingleEvent(eventQueue.remove(0), postingThreadState);
                }
            }
            finally {
                postingThreadState.isPosting = false;
                postingThreadState.isMainThread = false;
            }
            postingThreadState.isPosting = false;
            postingThreadState.isMainThread = false;
        }
    }
    
    public void register(final Object o) {
        this.register(o, false, 0);
    }
    
    public void unregister(final Object o) {
        // monitorexit(this)
        while (true) {
            final Throwable t;
            Label_0072: {
                synchronized (this) {
                    final List<Class<?>> list = this.typesBySubscriber.get(o);
                    if (list == null) {
                        break Label_0072;
                    }
                    final Iterator<Class<?>> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        this.unubscribeByEventType(o, iterator.next());
                    }
                }
                this.typesBySubscriber.remove(t);
                return;
            }
            Log.w(EventBus.TAG, "Subscriber to unregister was not registered before: " + t.getClass());
            continue;
        }
    }
    
    static final class PostingThreadState
    {
        boolean canceled;
        Object event;
        final List<Object> eventQueue;
        boolean isMainThread;
        boolean isPosting;
        Subscription subscription;
        
        PostingThreadState() {
            this.eventQueue = new ArrayList<Object>();
        }
    }
}
