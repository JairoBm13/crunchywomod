// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.lang.annotation.Annotation;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.Binder;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.PrivateElements;
import java.util.ArrayList;
import java.util.Collection;
import com.google.inject.spi.Elements;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.internal.util.$Stopwatch;
import java.util.Iterator;
import com.google.inject.internal.util.$Lists;
import com.google.inject.Stage;
import com.google.inject.Module;
import java.util.logging.Logger;
import com.google.inject.spi.InjectionPoint;
import java.util.Set;
import com.google.inject.Provider;
import com.google.inject.internal.util.$ImmutableSet;
import com.google.inject.internal.util.$SourceProvider;
import com.google.inject.Key;
import com.google.inject.Injector;
import com.google.inject.spi.Element;
import java.util.List;

final class InjectorShell
{
    private final List<Element> elements;
    private final InjectorImpl injector;
    
    private InjectorShell(final Builder builder, final List<Element> elements, final InjectorImpl injector) {
        this.elements = elements;
        this.injector = injector;
    }
    
    private static void bindInjector(final InjectorImpl injectorImpl) {
        final Key<Injector> value = Key.get(Injector.class);
        final InjectorFactory injectorFactory = new InjectorFactory((Injector)injectorImpl);
        injectorImpl.state.putBinding(value, new ProviderInstanceBindingImpl<Object>(injectorImpl, (Key<Object>)value, $SourceProvider.UNKNOWN_SOURCE, injectorFactory, Scoping.UNSCOPED, injectorFactory, (Set<InjectionPoint>)$ImmutableSet.of()));
    }
    
    private static void bindLogger(final InjectorImpl injectorImpl) {
        final Key<Logger> value = Key.get(Logger.class);
        final LoggerFactory loggerFactory = new LoggerFactory();
        injectorImpl.state.putBinding(value, new ProviderInstanceBindingImpl<Object>(injectorImpl, (Key<Object>)value, $SourceProvider.UNKNOWN_SOURCE, loggerFactory, Scoping.UNSCOPED, loggerFactory, (Set<InjectionPoint>)$ImmutableSet.of()));
    }
    
    List<Element> getElements() {
        return this.elements;
    }
    
    InjectorImpl getInjector() {
        return this.injector;
    }
    
    static class Builder
    {
        private final List<Element> elements;
        private final List<Module> modules;
        private InjectorImpl.InjectorOptions options;
        private InjectorImpl parent;
        private PrivateElementsImpl privateElements;
        private Stage stage;
        private State state;
        
        Builder() {
            this.elements = (List<Element>)$Lists.newArrayList();
            this.modules = (List<Module>)$Lists.newArrayList();
        }
        
        private State getState() {
            if (this.state == null) {
                this.state = new InheritingState(State.NONE);
            }
            return this.state;
        }
        
        void addModules(final Iterable<? extends Module> iterable) {
            final Iterator<? extends Module> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                this.modules.add((Module)iterator.next());
            }
        }
        
        List<InjectorShell> build(final Initializer initializer, final ProcessedBindingData processedBindingData, final $Stopwatch $Stopwatch, final Errors errors) {
            $Preconditions.checkState(this.stage != null, (Object)"Stage not initialized");
            $Preconditions.checkState(this.privateElements == null || this.parent != null, (Object)"PrivateElements with no parent");
            $Preconditions.checkState(this.state != null, (Object)"no state. Did you remember to lock() ?");
            if (this.parent == null) {
                this.modules.add(0, new RootModule(this.stage));
            }
            this.elements.addAll(Elements.getElements(this.stage, this.modules));
            final InjectorOptionsProcessor injectorOptionsProcessor = new InjectorOptionsProcessor(errors);
            injectorOptionsProcessor.process(null, this.elements);
            this.options = injectorOptionsProcessor.getOptions(this.stage, this.options);
            final InjectorImpl injectorImpl = new InjectorImpl(this.parent, this.state, this.options);
            if (this.privateElements != null) {
                this.privateElements.initInjector(injectorImpl);
            }
            if (this.parent == null) {
                new TypeConverterBindingProcessor(errors).prepareBuiltInConverters(injectorImpl);
            }
            $Stopwatch.resetAndLog("Module execution");
            new MessageProcessor(errors).process(injectorImpl, this.elements);
            new TypeListenerBindingProcessor(errors).process(injectorImpl, this.elements);
            injectorImpl.membersInjectorStore = new MembersInjectorStore(injectorImpl, injectorImpl.state.getTypeListenerBindings());
            $Stopwatch.resetAndLog("TypeListeners creation");
            new ScopeBindingProcessor(errors).process(injectorImpl, this.elements);
            $Stopwatch.resetAndLog("Scopes creation");
            new TypeConverterBindingProcessor(errors).process(injectorImpl, this.elements);
            $Stopwatch.resetAndLog("Converters creation");
            bindInjector(injectorImpl);
            bindLogger(injectorImpl);
            new BindingProcessor(errors, initializer, processedBindingData).process(injectorImpl, this.elements);
            new UntargettedBindingProcessor(errors, processedBindingData).process(injectorImpl, this.elements);
            $Stopwatch.resetAndLog("Binding creation");
            final ArrayList<Object> arrayList = $Lists.newArrayList();
            arrayList.add(new InjectorShell(this, this.elements, injectorImpl, null));
            final PrivateElementProcessor privateElementProcessor = new PrivateElementProcessor(errors);
            privateElementProcessor.process(injectorImpl, this.elements);
            final Iterator<Builder> iterator = privateElementProcessor.getInjectorShellBuilders().iterator();
            while (iterator.hasNext()) {
                arrayList.addAll(((Builder)iterator.next()).build(initializer, processedBindingData, $Stopwatch, errors));
            }
            $Stopwatch.resetAndLog("Private environment creation");
            return (List<InjectorShell>)arrayList;
        }
        
        Stage getStage() {
            return this.options.stage;
        }
        
        Object lock() {
            return this.getState().lock();
        }
        
        Builder parent(final InjectorImpl parent) {
            this.parent = parent;
            this.state = new InheritingState(parent.state);
            this.options = parent.options;
            this.stage = this.options.stage;
            return this;
        }
        
        Builder privateElements(final PrivateElements privateElements) {
            this.privateElements = (PrivateElementsImpl)privateElements;
            this.elements.addAll(privateElements.getElements());
            return this;
        }
        
        Builder stage(final Stage stage) {
            this.stage = stage;
            return this;
        }
    }
    
    private static class InjectorFactory implements Provider<Injector>, InternalFactory<Injector>
    {
        private final Injector injector;
        
        private InjectorFactory(final Injector injector) {
            this.injector = injector;
        }
        
        @Override
        public Injector get() {
            return this.injector;
        }
        
        @Override
        public Injector get(final Errors errors, final InternalContext internalContext, final Dependency<?> dependency, final boolean b) throws ErrorsException {
            return this.injector;
        }
        
        @Override
        public String toString() {
            return "Provider<Injector>";
        }
    }
    
    private static class LoggerFactory implements Provider<Logger>, InternalFactory<Logger>
    {
        @Override
        public Logger get() {
            return Logger.getAnonymousLogger();
        }
        
        @Override
        public Logger get(final Errors errors, final InternalContext internalContext, final Dependency<?> dependency, final boolean b) {
            final InjectionPoint injectionPoint = dependency.getInjectionPoint();
            if (injectionPoint == null) {
                return Logger.getAnonymousLogger();
            }
            return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
        }
        
        @Override
        public String toString() {
            return "Provider<Logger>";
        }
    }
    
    private static class RootModule implements Module
    {
        final Stage stage;
        
        private RootModule(final Stage stage) {
            this.stage = $Preconditions.checkNotNull(stage, "stage");
        }
        
        @Override
        public void configure(Binder withSource) {
            withSource = withSource.withSource($SourceProvider.UNKNOWN_SOURCE);
            withSource.bind(Stage.class).toInstance(this.stage);
            withSource.bindScope(Singleton.class, Scopes.SINGLETON);
            withSource.bindScope(javax.inject.Singleton.class, Scopes.SINGLETON);
        }
    }
}
