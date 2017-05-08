// 
// Decompiled by Procyon v0.5.30
// 

package de.greenrobot.event;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import android.util.Log;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SubscriberMethodFinder
{
    private static final Map<String, List<SubscriberMethod>> methodCache;
    private final Map<Class<?>, Class<?>> skipMethodVerificationForClasses;
    
    static {
        methodCache = new HashMap<String, List<SubscriberMethod>>();
    }
    
    SubscriberMethodFinder(final List<Class<?>> list) {
        this.skipMethodVerificationForClasses = new ConcurrentHashMap<Class<?>, Class<?>>();
        if (list != null) {
            for (final Class<?> clazz : list) {
                this.skipMethodVerificationForClasses.put(clazz, clazz);
            }
        }
    }
    
    List<SubscriberMethod> findSubscriberMethods(final Class<?> clazz) {
        final String name = clazz.getName();
        synchronized (SubscriberMethodFinder.methodCache) {
            final List<SubscriberMethod> list = SubscriberMethodFinder.methodCache.get(name);
            // monitorexit(SubscriberMethodFinder.methodCache)
            if (list != null) {
                return list;
            }
        }
        final ArrayList<SubscriberMethod> list2 = new ArrayList<SubscriberMethod>();
        final Class clazz2;
        Class superclass = clazz2;
        final HashSet<String> set = new HashSet<String>();
        final StringBuilder sb = new StringBuilder();
        while (superclass != null) {
            final String name2 = superclass.getName();
            if (name2.startsWith("java.") || name2.startsWith("javax.") || name2.startsWith("android.")) {
                break;
            }
            final Method[] declaredMethods = superclass.getDeclaredMethods();
            for (int length = declaredMethods.length, i = 0; i < length; ++i) {
                final Method method = declaredMethods[i];
                final String name3 = method.getName();
                if (name3.startsWith("onEvent")) {
                    final int modifiers = method.getModifiers();
                    if ((modifiers & 0x1) != 0x0 && (modifiers & 0x1448) == 0x0) {
                        final Class<?>[] parameterTypes = method.getParameterTypes();
                        if (parameterTypes.length == 1) {
                            final String substring = name3.substring("onEvent".length());
                            ThreadMode threadMode;
                            if (substring.length() == 0) {
                                threadMode = ThreadMode.PostThread;
                            }
                            else if (substring.equals("MainThread")) {
                                threadMode = ThreadMode.MainThread;
                            }
                            else if (substring.equals("BackgroundThread")) {
                                threadMode = ThreadMode.BackgroundThread;
                            }
                            else if (substring.equals("Async")) {
                                threadMode = ThreadMode.Async;
                            }
                            else {
                                if (!this.skipMethodVerificationForClasses.containsKey(superclass)) {
                                    throw new EventBusException("Illegal onEvent method, check for typos: " + method);
                                }
                                continue;
                            }
                            final Class<?> clazz3 = parameterTypes[0];
                            sb.setLength(0);
                            sb.append(name3);
                            sb.append('>').append(clazz3.getName());
                            if (set.add(sb.toString())) {
                                list2.add(new SubscriberMethod(method, threadMode, clazz3));
                            }
                        }
                    }
                    else if (!this.skipMethodVerificationForClasses.containsKey(superclass)) {
                        Log.d(EventBus.TAG, "Skipping method (not public, static or abstract): " + superclass + "." + name3);
                    }
                }
            }
            superclass = superclass.getSuperclass();
        }
        if (list2.isEmpty()) {
            throw new EventBusException("Subscriber " + clazz2 + " has no public methods called " + "onEvent");
        }
        synchronized (SubscriberMethodFinder.methodCache) {
            SubscriberMethodFinder.methodCache.put(name, list2);
            return list2;
        }
    }
}
