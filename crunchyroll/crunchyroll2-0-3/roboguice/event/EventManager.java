// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.event;

import roboguice.event.eventListener.ObserverMethodListener;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import com.google.inject.Inject;
import android.content.Context;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class EventManager
{
    @Inject
    protected Context context;
    protected Map<Class<?>, Set<EventListener<?>>> registrations;
    
    public EventManager() {
        this.registrations = new HashMap<Class<?>, Set<EventListener<?>>>();
    }
    
    public void destroy() {
        final Iterator<Map.Entry<Class<?>, Set<EventListener<?>>>> iterator = this.registrations.entrySet().iterator();
        while (iterator.hasNext()) {
            iterator.next().getValue().clear();
        }
        this.registrations.clear();
    }
    
    public void fire(final Object o) {
        final Set<EventListener<?>> set = this.registrations.get(o.getClass());
        if (set == null) {
            return;
        }
        synchronized (set) {
            final Iterator<EventListener<Object>> iterator = set.iterator();
            while (iterator.hasNext()) {
                iterator.next().onEvent(o);
            }
        }
    }
    // monitorexit(set)
    
    public <T> void registerObserver(final Class<T> clazz, final EventListener eventListener) {
        Set<EventListener<?>> synchronizedSet;
        if ((synchronizedSet = this.registrations.get(clazz)) == null) {
            synchronizedSet = Collections.synchronizedSet(new LinkedHashSet<EventListener<?>>());
            this.registrations.put(clazz, synchronizedSet);
        }
        synchronizedSet.add(eventListener);
    }
    
    public <T> void registerObserver(final Object o, final Method method, final Class<T> clazz) {
        this.registerObserver(clazz, new ObserverMethodListener(o, method));
    }
    
    public <T> void unregisterObserver(Class<T> set, final EventListener<T> eventListener) {
        set = this.registrations.get(set);
        if (set == null) {
            return;
        }
        synchronized (set) {
            final Iterator<EventListener<T>> iterator = set.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() == eventListener) {
                    iterator.remove();
                    break;
                }
            }
        }
    }
    
    public <T> void unregisterObserver(final Object o, Class<T> set) {
        set = this.registrations.get(set);
        if (set == null) {
            return;
        }
        synchronized (set) {
            final Iterator<EventListener<?>> iterator = set.iterator();
            while (iterator.hasNext()) {
                final EventListener<?> eventListener = iterator.next();
                if (eventListener instanceof ObserverMethodListener && ((ObserverMethodListener<?>)eventListener).getInstance() == o) {
                    iterator.remove();
                    break;
                }
            }
        }
    }
}
