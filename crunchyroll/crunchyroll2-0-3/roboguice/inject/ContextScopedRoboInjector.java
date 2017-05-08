// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.inject;

import android.support.v4.app.Fragment;
import android.app.Activity;
import com.google.inject.spi.TypeConverterBinding;
import java.util.Set;
import com.google.inject.Scope;
import java.lang.annotation.Annotation;
import com.google.inject.Provider;
import com.google.inject.MembersInjector;
import com.google.inject.Key;
import java.util.Map;
import com.google.inject.Binding;
import java.util.List;
import com.google.inject.TypeLiteral;
import com.google.inject.Module;
import com.google.inject.Injector;
import android.content.Context;

public class ContextScopedRoboInjector implements RoboInjector
{
    protected Context context;
    protected Injector delegate;
    protected ContextScope scope;
    protected ViewListener viewListener;
    
    public ContextScopedRoboInjector(final Context context, final Injector delegate, final ViewListener viewListener) {
        this.delegate = delegate;
        this.context = context;
        this.viewListener = viewListener;
        this.scope = this.delegate.getInstance(ContextScope.class);
    }
    
    @Override
    public Injector createChildInjector(final Iterable<? extends Module> iterable) {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.createChildInjector(iterable);
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public Injector createChildInjector(final Module... array) {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.createChildInjector(array);
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public <T> List<Binding<T>> findBindingsByType(final TypeLiteral<T> typeLiteral) {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.findBindingsByType(typeLiteral);
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public Map<Key<?>, Binding<?>> getAllBindings() {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.getAllBindings();
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public <T> Binding<T> getBinding(final Key<T> key) {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.getBinding(key);
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public <T> Binding<T> getBinding(final Class<T> clazz) {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.getBinding(clazz);
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public Map<Key<?>, Binding<?>> getBindings() {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.getBindings();
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public <T> Binding<T> getExistingBinding(final Key<T> key) {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.getExistingBinding(key);
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public <T> T getInstance(final Key<T> key) {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.getInstance(key);
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public <T> T getInstance(final Class<T> clazz) {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.getInstance(clazz);
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public <T> MembersInjector<T> getMembersInjector(final TypeLiteral<T> typeLiteral) {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.getMembersInjector(typeLiteral);
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public <T> MembersInjector<T> getMembersInjector(final Class<T> clazz) {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.getMembersInjector(clazz);
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public Injector getParent() {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.getParent();
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public <T> Provider<T> getProvider(final Key<T> key) {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.getProvider(key);
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public <T> Provider<T> getProvider(final Class<T> clazz) {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.getProvider(clazz);
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public Map<Class<? extends Annotation>, Scope> getScopeBindings() {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.getScopeBindings();
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public Set<TypeConverterBinding> getTypeConverterBindings() {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                return this.delegate.getTypeConverterBindings();
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public void injectMembers(final Object o) {
        this.injectMembersWithoutViews(o);
    }
    
    @Override
    public void injectMembersWithoutViews(final Object o) {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                this.delegate.injectMembers(o);
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
    
    @Override
    public void injectViewMembers(final Activity activity) {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                if (this.context != activity) {
                    throw new UnsupportedOperationException("internal error, how did you get here?");
                }
            }
            finally {
                this.scope.exit(this.context);
            }
        }
        final Throwable t;
        ViewListener.ViewMembersInjector.injectViews(t);
        this.scope.exit(this.context);
    }
    // monitorexit(ContextScope.class)
    
    @Override
    public void injectViewMembers(final Fragment fragment) {
        synchronized (ContextScope.class) {
            this.scope.enter(this.context);
            try {
                ViewListener.ViewMembersInjector.injectViews(fragment);
            }
            finally {
                this.scope.exit(this.context);
            }
        }
    }
}
