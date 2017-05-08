// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.DefaultBindingTargetVisitor;
import com.google.inject.internal.util.$ImmutableSet;
import com.google.inject.TypeLiteral;
import com.google.inject.Scope;
import com.google.inject.Provider;
import com.google.inject.Module;
import com.google.inject.MembersInjector;
import com.google.inject.Key;
import com.google.inject.Injector;
import com.google.inject.Binding;
import com.google.inject.Binder;
import com.google.inject.AbstractModule;
import java.util.Set;

abstract class AbstractBindingProcessor extends AbstractProcessor
{
    private static final Set<Class<?>> FORBIDDEN_TYPES;
    protected final ProcessedBindingData bindingData;
    
    static {
        FORBIDDEN_TYPES = $ImmutableSet.of((Class[])new Class[] { AbstractModule.class, Binder.class, Binding.class, Injector.class, Key.class, MembersInjector.class, Module.class, Provider.class, Scope.class, TypeLiteral.class });
    }
    
    AbstractBindingProcessor(final Errors errors, final ProcessedBindingData bindingData) {
        super(errors);
        this.bindingData = bindingData;
    }
    
    private boolean isOkayDuplicate(final BindingImpl<?> bindingImpl, final BindingImpl<?> bindingImpl2, final State state) {
        boolean b = false;
        if (bindingImpl instanceof ExposedBindingImpl) {
            if (((ExposedBindingImpl)bindingImpl).getPrivateElements().getInjector() == bindingImpl2.getInjector()) {
                b = true;
            }
        }
        else {
            final BindingImpl bindingImpl3 = (BindingImpl)state.getExplicitBindingsThisLevel().get(bindingImpl2.getKey());
            if (bindingImpl3 != null) {
                return bindingImpl3.equals(bindingImpl2);
            }
        }
        return b;
    }
    
    private <T> void validateKey(final Object o, final Key<T> key) {
        Annotations.checkForMisplacedScopeAnnotations(key.getTypeLiteral().getRawType(), o, this.errors);
    }
    
    protected <T> UntargettedBindingImpl<T> invalidBinding(final InjectorImpl injectorImpl, final Key<T> key, final Object o) {
        return new UntargettedBindingImpl<T>(injectorImpl, key, o);
    }
    
    protected void putBinding(final BindingImpl<?> bindingImpl) {
        final Key<?> key = bindingImpl.getKey();
        final Class<? super T> rawType = key.getTypeLiteral().getRawType();
        if (AbstractBindingProcessor.FORBIDDEN_TYPES.contains(rawType)) {
            this.errors.cannotBindToGuiceType(rawType.getSimpleName());
            return;
        }
        final BindingImpl<T> existingBinding = this.injector.getExistingBinding((Key<T>)key);
        Label_0123: {
            if (existingBinding != null) {
                if (this.injector.state.getExplicitBinding(key) != null) {
                    try {
                        if (!this.isOkayDuplicate(existingBinding, bindingImpl, this.injector.state)) {
                            this.errors.bindingAlreadySet(key, existingBinding.getSource());
                            return;
                        }
                        break Label_0123;
                    }
                    catch (Throwable t) {
                        this.errors.errorCheckingDuplicateBinding(key, existingBinding.getSource(), t);
                        return;
                    }
                }
                this.errors.jitBindingAlreadySet(key);
                return;
            }
        }
        this.injector.state.parent().blacklist(key, bindingImpl.getSource());
        this.injector.state.putBinding(key, bindingImpl);
    }
    
    abstract class Processor<T, V> extends DefaultBindingTargetVisitor<T, V>
    {
        final Key<T> key;
        final Class<? super T> rawType;
        Scoping scoping;
        final Object source;
        
        Processor(final BindingImpl<T> bindingImpl) {
            this.source = bindingImpl.getSource();
            this.key = bindingImpl.getKey();
            this.rawType = this.key.getTypeLiteral().getRawType();
            this.scoping = bindingImpl.getScoping();
        }
        
        protected void prepareBinding() {
            AbstractBindingProcessor.this.validateKey(this.source, (Key<Object>)this.key);
            this.scoping = Scoping.makeInjectable(this.scoping, AbstractBindingProcessor.this.injector, AbstractBindingProcessor.this.errors);
        }
        
        protected void scheduleInitialization(final BindingImpl<?> bindingImpl) {
            AbstractBindingProcessor.this.bindingData.addUninitializedBinding(new Runnable() {
                @Override
                public void run() {
                    try {
                        bindingImpl.getInjector().initializeBinding(bindingImpl, AbstractBindingProcessor.this.errors.withSource(Processor.this.source));
                    }
                    catch (ErrorsException ex) {
                        AbstractBindingProcessor.this.errors.merge(ex.getErrors());
                    }
                }
            });
        }
    }
}
