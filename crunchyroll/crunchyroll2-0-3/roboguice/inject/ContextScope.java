// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import com.google.inject.Provider;
import android.content.ContextWrapper;
import roboguice.util.RoboContext;
import java.io.Serializable;
import java.util.HashMap;
import android.content.Context;
import java.lang.ref.WeakReference;
import java.util.Stack;
import com.google.inject.Key;
import java.util.Map;
import android.app.Application;
import com.google.inject.Scope;

public class ContextScope implements Scope
{
    protected Application application;
    protected Map<Key<?>, Object> applicationScopedObjects;
    protected ThreadLocal<Stack<WeakReference<Context>>> contextThreadLocal;
    
    public ContextScope(final Application application) {
        this.contextThreadLocal = new ThreadLocal<Stack<WeakReference<Context>>>();
        this.applicationScopedObjects = new HashMap<Key<?>, Object>();
        this.enter((Context)(this.application = application));
    }
    
    public void enter(final Context context) {
        synchronized (ContextScope.class) {
            final Stack<WeakReference<Context>> contextStack = this.getContextStack();
            final Map<Key<?>, Object> scopedObjectMap = this.getScopedObjectMap(context);
            contextStack.push(new WeakReference<Context>(context));
            Serializable s = context.getClass();
            do {
                scopedObjectMap.put(Key.get((Class<Object>)s), context);
            } while ((s = ((Class<Object>)s).getSuperclass()) != Object.class);
        }
    }
    
    public void exit(final Context context) {
        synchronized (ContextScope.class) {
            final Context context2 = this.getContextStack().pop().get();
            if (context2 != null && context2 != context) {
                throw new IllegalArgumentException(String.format("Scope for %s must be opened before it can be closed", context));
            }
        }
    }
    // monitorexit(ContextScope.class)
    
    public Stack<WeakReference<Context>> getContextStack() {
        Stack<WeakReference<Context>> stack;
        if ((stack = this.contextThreadLocal.get()) == null) {
            stack = new Stack<WeakReference<Context>>();
            this.contextThreadLocal.set(stack);
        }
        return stack;
    }
    
    protected Map<Key<?>, Object> getScopedObjectMap(final Context context) {
        Context baseContext;
        for (baseContext = context; !(baseContext instanceof RoboContext) && !(baseContext instanceof Application) && baseContext instanceof ContextWrapper; baseContext = ((ContextWrapper)baseContext).getBaseContext()) {}
        if (baseContext instanceof Application) {
            return this.applicationScopedObjects;
        }
        if (!(baseContext instanceof RoboContext)) {
            throw new IllegalArgumentException(String.format("%s does not appear to be a RoboGuice context (instanceof RoboContext)", context));
        }
        return ((RoboContext)baseContext).getScopedObjectMap();
    }
    
    @Override
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> provider) {
        return new Provider<T>() {
            @Override
            public T get() {
                synchronized (ContextScope.class) {
                    final Map<Key<?>, Object> scopedObjectMap = ContextScope.this.getScopedObjectMap(ContextScope.this.getContextStack().peek().get());
                    if (scopedObjectMap == null) {
                        return null;
                    }
                    final Object value = scopedObjectMap.get(key);
                    T value2;
                    if ((value2 = (T)value) == null) {
                        value2 = (T)value;
                        if (!scopedObjectMap.containsKey(key)) {
                            value2 = provider.get();
                            scopedObjectMap.put(key, value2);
                        }
                    }
                    return value2;
                }
            }
        };
    }
}
