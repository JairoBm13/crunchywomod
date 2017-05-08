// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.TypeConverterBinding;
import java.util.Set;
import com.google.inject.Scope;
import java.lang.annotation.Annotation;
import com.google.inject.Provider;
import com.google.inject.MembersInjector;
import java.util.Map;
import com.google.inject.Binding;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Dependency;
import com.google.inject.internal.util.$ImmutableList;
import com.google.inject.internal.util.$Iterables;
import com.google.inject.Module;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Stage;
import java.util.Iterator;
import com.google.inject.internal.util.$Stopwatch;
import java.util.List;

public final class InternalInjectorCreator
{
    private final ProcessedBindingData bindingData;
    private final Errors errors;
    private final Initializer initializer;
    private final InjectionRequestProcessor injectionRequestProcessor;
    private final InjectorShell.Builder shellBuilder;
    private List<InjectorShell> shells;
    private final $Stopwatch stopwatch;
    
    public InternalInjectorCreator() {
        this.stopwatch = new $Stopwatch();
        this.errors = new Errors();
        this.initializer = new Initializer();
        this.shellBuilder = new InjectorShell.Builder();
        this.injectionRequestProcessor = new InjectionRequestProcessor(this.errors, this.initializer);
        this.bindingData = new ProcessedBindingData();
    }
    
    private void initializeStatically() {
        this.bindingData.initializeBindings();
        this.stopwatch.resetAndLog("Binding initialization");
        final Iterator<InjectorShell> iterator = this.shells.iterator();
        while (iterator.hasNext()) {
            iterator.next().getInjector().index();
        }
        this.stopwatch.resetAndLog("Binding indexing");
        this.injectionRequestProcessor.process(this.shells);
        this.stopwatch.resetAndLog("Collecting injection requests");
        this.bindingData.runCreationListeners(this.errors);
        this.stopwatch.resetAndLog("Binding validation");
        this.injectionRequestProcessor.validate();
        this.stopwatch.resetAndLog("Static validation");
        this.initializer.validateOustandingInjections(this.errors);
        this.stopwatch.resetAndLog("Instance member validation");
        new LookupProcessor(this.errors).process(this.shells);
        final Iterator<InjectorShell> iterator2 = this.shells.iterator();
        while (iterator2.hasNext()) {
            ((DeferredLookups)iterator2.next().getInjector().lookups).initialize(this.errors);
        }
        this.stopwatch.resetAndLog("Provider verification");
        for (final InjectorShell injectorShell : this.shells) {
            if (!injectorShell.getElements().isEmpty()) {
                throw new AssertionError((Object)("Failed to execute " + injectorShell.getElements()));
            }
        }
        this.errors.throwCreationExceptionIfErrorsExist();
    }
    
    private void injectDynamically() {
        this.injectionRequestProcessor.injectMembers();
        this.stopwatch.resetAndLog("Static member injection");
        this.initializer.injectAll(this.errors);
        this.stopwatch.resetAndLog("Instance injection");
        this.errors.throwCreationExceptionIfErrorsExist();
        if (this.shellBuilder.getStage() != Stage.TOOL) {
            final Iterator<InjectorShell> iterator = this.shells.iterator();
            while (iterator.hasNext()) {
                this.loadEagerSingletons(iterator.next().getInjector(), this.shellBuilder.getStage(), this.errors);
            }
            this.stopwatch.resetAndLog("Preloading singletons");
        }
        this.errors.throwCreationExceptionIfErrorsExist();
    }
    
    private boolean isEagerSingleton(final InjectorImpl injectorImpl, final BindingImpl<?> bindingImpl, final Stage stage) {
        return bindingImpl.getScoping().isEagerSingleton(stage) || (bindingImpl instanceof LinkedBindingImpl && this.isEagerSingleton(injectorImpl, injectorImpl.getBinding((Key<? extends T>)((LinkedBindingImpl)bindingImpl).getLinkedKey()), stage));
    }
    
    private Injector primaryInjector() {
        return this.shells.get(0).getInjector();
    }
    
    public InternalInjectorCreator addModules(final Iterable<? extends Module> iterable) {
        this.shellBuilder.addModules(iterable);
        return this;
    }
    
    public Injector build() {
        if (this.shellBuilder == null) {
            throw new AssertionError((Object)"Already built, builders are not reusable.");
        }
        synchronized (this.shellBuilder.lock()) {
            this.shells = this.shellBuilder.build(this.initializer, this.bindingData, this.stopwatch, this.errors);
            this.stopwatch.resetAndLog("Injector construction");
            this.initializeStatically();
            // monitorexit(this.shellBuilder.lock())
            this.injectDynamically();
            if (this.shellBuilder.getStage() == Stage.TOOL) {
                return new ToolStageInjector(this.primaryInjector());
            }
        }
        return this.primaryInjector();
    }
    
    void loadEagerSingletons(final InjectorImpl injectorImpl, final Stage stage, final Errors errors) {
        for (final BindingImpl<?> bindingImpl : $ImmutableList.copyOf($Iterables.concat((Iterable<?>)injectorImpl.state.getExplicitBindingsThisLevel().values(), (Iterable<?>)injectorImpl.jitBindings.values()))) {
            if (this.isEagerSingleton(injectorImpl, bindingImpl, stage)) {
                try {
                    injectorImpl.callInContext((ContextualCallable<Object>)new ContextualCallable<Void>() {
                        Dependency<?> dependency = Dependency.get(bindingImpl.getKey());
                        
                        @Override
                        public Void call(final InternalContext internalContext) {
                            final Dependency setDependency = internalContext.setDependency(this.dependency);
                            final Errors withSource = errors.withSource(this.dependency);
                            try {
                                bindingImpl.getInternalFactory().get(withSource, internalContext, this.dependency, false);
                                return null;
                            }
                            catch (ErrorsException ex) {
                                withSource.merge(ex.getErrors());
                                internalContext.setDependency(setDependency);
                                return null;
                            }
                            finally {
                                internalContext.setDependency(setDependency);
                            }
                        }
                    });
                    continue;
                }
                catch (ErrorsException ex) {
                    throw new AssertionError();
                }
                break;
            }
        }
    }
    
    public InternalInjectorCreator parentInjector(final InjectorImpl injectorImpl) {
        this.shellBuilder.parent(injectorImpl);
        return this;
    }
    
    public InternalInjectorCreator stage(final Stage stage) {
        this.shellBuilder.stage(stage);
        return this;
    }
    
    static class ToolStageInjector implements Injector
    {
        private final Injector delegateInjector;
        
        ToolStageInjector(final Injector delegateInjector) {
            this.delegateInjector = delegateInjector;
        }
        
        @Override
        public Injector createChildInjector(final Iterable<? extends Module> iterable) {
            return this.delegateInjector.createChildInjector(iterable);
        }
        
        @Override
        public Injector createChildInjector(final Module... array) {
            return this.delegateInjector.createChildInjector(array);
        }
        
        @Override
        public <T> List<Binding<T>> findBindingsByType(final TypeLiteral<T> typeLiteral) {
            return this.delegateInjector.findBindingsByType(typeLiteral);
        }
        
        @Override
        public Map<Key<?>, Binding<?>> getAllBindings() {
            return this.delegateInjector.getAllBindings();
        }
        
        @Override
        public <T> Binding<T> getBinding(final Key<T> key) {
            return this.delegateInjector.getBinding(key);
        }
        
        @Override
        public <T> Binding<T> getBinding(final Class<T> clazz) {
            return this.delegateInjector.getBinding(clazz);
        }
        
        @Override
        public Map<Key<?>, Binding<?>> getBindings() {
            return this.delegateInjector.getBindings();
        }
        
        @Override
        public <T> Binding<T> getExistingBinding(final Key<T> key) {
            return this.delegateInjector.getExistingBinding(key);
        }
        
        @Override
        public <T> T getInstance(final Key<T> key) {
            throw new UnsupportedOperationException("Injector.getInstance(Key<T>) is not supported in Stage.TOOL");
        }
        
        @Override
        public <T> T getInstance(final Class<T> clazz) {
            throw new UnsupportedOperationException("Injector.getInstance(Class<T>) is not supported in Stage.TOOL");
        }
        
        @Override
        public <T> MembersInjector<T> getMembersInjector(final TypeLiteral<T> typeLiteral) {
            throw new UnsupportedOperationException("Injector.getMembersInjector(TypeLiteral<T>) is not supported in Stage.TOOL");
        }
        
        @Override
        public <T> MembersInjector<T> getMembersInjector(final Class<T> clazz) {
            throw new UnsupportedOperationException("Injector.getMembersInjector(Class<T>) is not supported in Stage.TOOL");
        }
        
        @Override
        public Injector getParent() {
            return this.delegateInjector.getParent();
        }
        
        @Override
        public <T> Provider<T> getProvider(final Key<T> key) {
            throw new UnsupportedOperationException("Injector.getProvider(Key<T>) is not supported in Stage.TOOL");
        }
        
        @Override
        public <T> Provider<T> getProvider(final Class<T> clazz) {
            throw new UnsupportedOperationException("Injector.getProvider(Class<T>) is not supported in Stage.TOOL");
        }
        
        @Override
        public Map<Class<? extends Annotation>, Scope> getScopeBindings() {
            return this.delegateInjector.getScopeBindings();
        }
        
        @Override
        public Set<TypeConverterBinding> getTypeConverterBindings() {
            return this.delegateInjector.getTypeConverterBindings();
        }
        
        @Override
        public void injectMembers(final Object o) {
            throw new UnsupportedOperationException("Injector.injectMembers(Object) is not supported in Stage.TOOL");
        }
    }
}
