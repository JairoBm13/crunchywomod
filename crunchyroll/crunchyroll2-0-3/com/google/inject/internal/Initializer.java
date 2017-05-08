// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.TypeLiteral;
import com.google.inject.Stage;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.spi.InjectionPoint;
import java.util.Set;
import java.util.Iterator;
import com.google.inject.internal.util.$Lists;
import com.google.inject.internal.util.$Maps;
import java.util.concurrent.CountDownLatch;
import java.util.Map;

final class Initializer
{
    private final Thread creatingThread;
    private final Map<Object, InjectableReference<?>> pendingInjection;
    private final CountDownLatch ready;
    
    Initializer() {
        this.creatingThread = Thread.currentThread();
        this.ready = new CountDownLatch(1);
        this.pendingInjection = (Map<Object, InjectableReference<?>>)$Maps.newIdentityHashMap();
    }
    
    void injectAll(final Errors errors) {
        for (final InjectableReference injectableReference : $Lists.newArrayList((Iterable<? extends InjectableReference>)this.pendingInjection.values())) {
            try {
                injectableReference.get(errors);
            }
            catch (ErrorsException ex) {
                errors.merge(ex.getErrors());
            }
        }
        if (!this.pendingInjection.isEmpty()) {
            throw new AssertionError((Object)("Failed to satisfy " + this.pendingInjection));
        }
        this.ready.countDown();
    }
    
     <T> Initializable<T> requestInjection(final InjectorImpl injectorImpl, final T t, final Object o, final Set<InjectionPoint> set) {
        $Preconditions.checkNotNull(o);
        if (t == null || (set.isEmpty() && !injectorImpl.membersInjectorStore.hasTypeListeners())) {
            return Initializables.of(t);
        }
        final InjectableReference<Object> injectableReference = new InjectableReference<Object>(injectorImpl, t, o);
        this.pendingInjection.put(t, injectableReference);
        return (Initializable<T>)injectableReference;
    }
    
    void validateOustandingInjections(final Errors errors) {
        for (final InjectableReference<?> injectableReference : this.pendingInjection.values()) {
            try {
                injectableReference.validate(errors);
            }
            catch (ErrorsException ex) {
                errors.merge(ex.getErrors());
            }
        }
    }
    
    private class InjectableReference<T> implements Initializable<T>
    {
        private final InjectorImpl injector;
        private final T instance;
        private MembersInjectorImpl<T> membersInjector;
        private final Object source;
        
        public InjectableReference(final InjectorImpl injector, final T t, final Object o) {
            this.injector = injector;
            this.instance = $Preconditions.checkNotNull(t, "instance");
            this.source = $Preconditions.checkNotNull(o, "source");
        }
        
        @Override
        public T get(Errors withSource) throws ErrorsException {
            if (Initializer.this.ready.getCount() == 0L) {
                return this.instance;
            }
            if (Thread.currentThread() != Initializer.this.creatingThread) {
                try {
                    Initializer.this.ready.await();
                    return this.instance;
                }
                catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (Initializer.this.pendingInjection.remove(this.instance) != null) {
                final MembersInjectorImpl<T> membersInjector = this.membersInjector;
                final T instance = this.instance;
                withSource = withSource.withSource(this.source);
                membersInjector.injectAndNotify(instance, withSource, this.injector.options.stage == Stage.TOOL);
            }
            return this.instance;
        }
        
        @Override
        public String toString() {
            return this.instance.toString();
        }
        
        public void validate(final Errors errors) throws ErrorsException {
            this.membersInjector = this.injector.membersInjectorStore.get(TypeLiteral.get(this.instance.getClass()), errors.withSource(this.source));
        }
    }
}
