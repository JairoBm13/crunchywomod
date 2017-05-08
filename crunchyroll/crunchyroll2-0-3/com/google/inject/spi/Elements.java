// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import java.util.Collection;
import com.google.inject.internal.ProviderMethodsModule;
import com.google.inject.PrivateModule;
import com.google.inject.Provider;
import com.google.inject.MembersInjector;
import com.google.inject.Scope;
import java.lang.annotation.Annotation;
import com.google.inject.matcher.Matcher;
import com.google.inject.binder.AnnotatedConstantBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.internal.util.$ImmutableList;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ExposureBuilder;
import com.google.inject.binder.AnnotatedElementBuilder;
import com.google.inject.Key;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.internal.BindingBuilder;
import com.google.inject.internal.AbstractBindingBuilder;
import com.google.inject.internal.ConstantBindingBuilderImpl;
import com.google.inject.AbstractModule;
import com.google.inject.internal.util.$Lists;
import com.google.inject.internal.util.$Sets;
import com.google.inject.internal.util.$SourceProvider;
import com.google.inject.internal.PrivateElementsImpl;
import java.util.Set;
import com.google.inject.PrivateBinder;
import com.google.inject.Binder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collections;
import java.util.List;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.Binding;

public final class Elements
{
    private static final BindingTargetVisitor<Object, Object> GET_INSTANCE_VISITOR;
    
    static {
        GET_INSTANCE_VISITOR = new DefaultBindingTargetVisitor<Object, Object>() {
            @Override
            public Object visit(final InstanceBinding<?> instanceBinding) {
                return instanceBinding.getInstance();
            }
            
            @Override
            protected Object visitOther(final Binding<?> binding) {
                throw new IllegalArgumentException();
            }
        };
    }
    
    public static List<Element> getElements(final Stage stage, final Iterable<? extends Module> iterable) {
        final RecordingBinder recordingBinder = new RecordingBinder(stage);
        final Iterator<? extends Module> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            recordingBinder.install((Module)iterator.next());
        }
        return (List<Element>)Collections.unmodifiableList((List<?>)recordingBinder.elements);
    }
    
    public static List<Element> getElements(final Iterable<? extends Module> iterable) {
        return getElements(Stage.DEVELOPMENT, iterable);
    }
    
    public static List<Element> getElements(final Module... array) {
        return getElements(Stage.DEVELOPMENT, Arrays.asList(array));
    }
    
    private static class RecordingBinder implements Binder, PrivateBinder
    {
        private final List<Element> elements;
        private final Set<Module> modules;
        private final RecordingBinder parent;
        private final PrivateElementsImpl privateElements;
        private final Object source;
        private final $SourceProvider sourceProvider;
        private final Stage stage;
        
        private RecordingBinder(final Stage stage) {
            this.stage = stage;
            this.modules = (Set<Module>)$Sets.newHashSet();
            this.elements = (List<Element>)$Lists.newArrayList();
            this.source = null;
            this.sourceProvider = $SourceProvider.DEFAULT_INSTANCE.plusSkippedClasses(Elements.class, RecordingBinder.class, AbstractModule.class, ConstantBindingBuilderImpl.class, AbstractBindingBuilder.class, BindingBuilder.class);
            this.parent = null;
            this.privateElements = null;
        }
        
        private RecordingBinder(final RecordingBinder parent, final PrivateElementsImpl privateElements) {
            this.stage = parent.stage;
            this.modules = (Set<Module>)$Sets.newHashSet();
            this.elements = privateElements.getElementsMutable();
            this.source = parent.source;
            this.sourceProvider = parent.sourceProvider;
            this.parent = parent;
            this.privateElements = privateElements;
        }
        
        private RecordingBinder(final RecordingBinder recordingBinder, final Object source, final $SourceProvider sourceProvider) {
            boolean b = true;
            boolean b2;
            if (source == null) {
                b2 = true;
            }
            else {
                b2 = false;
            }
            if (sourceProvider != null) {
                b = false;
            }
            $Preconditions.checkArgument(b ^ b2);
            this.stage = recordingBinder.stage;
            this.modules = recordingBinder.modules;
            this.elements = recordingBinder.elements;
            this.source = source;
            this.sourceProvider = sourceProvider;
            this.parent = recordingBinder.parent;
            this.privateElements = recordingBinder.privateElements;
        }
        
        private <T> AnnotatedElementBuilder exposeInternal(final Key<T> key) {
            if (this.privateElements == null) {
                this.addError("Cannot expose %s on a standard binder. Exposed bindings are only applicable to private binders.", key);
                return new AnnotatedElementBuilder() {};
            }
            final ExposureBuilder<Object> exposureBuilder = new ExposureBuilder<Object>(this, this.getSource(), (Key<Object>)key);
            this.privateElements.addExposureBuilder(exposureBuilder);
            return exposureBuilder;
        }
        
        @Override
        public void addError(final Message message) {
            this.elements.add(message);
        }
        
        @Override
        public void addError(final String s, final Object... array) {
            this.elements.add(new Message(this.getSource(), Errors.format(s, array)));
        }
        
        @Override
        public void addError(final Throwable t) {
            this.elements.add(new Message($ImmutableList.of(this.getSource()), "An exception was caught and reported. Message: " + t.getMessage(), t));
        }
        
        @Override
        public <T> AnnotatedBindingBuilder<T> bind(final Key<T> key) {
            return new BindingBuilder<T>(this, this.elements, this.getSource(), key);
        }
        
        @Override
        public <T> AnnotatedBindingBuilder<T> bind(final TypeLiteral<T> typeLiteral) {
            return this.bind((Key<T>)Key.get((TypeLiteral<T>)typeLiteral));
        }
        
        @Override
        public <T> AnnotatedBindingBuilder<T> bind(final Class<T> clazz) {
            return this.bind((Key<T>)Key.get((Class<T>)clazz));
        }
        
        @Override
        public AnnotatedConstantBindingBuilder bindConstant() {
            return new ConstantBindingBuilderImpl<Object>(this, this.elements, this.getSource());
        }
        
        @Override
        public void bindListener(final Matcher<? super TypeLiteral<?>> matcher, final TypeListener typeListener) {
            this.elements.add(new TypeListenerBinding(this.getSource(), typeListener, matcher));
        }
        
        @Override
        public void bindScope(final Class<? extends Annotation> clazz, final Scope scope) {
            this.elements.add(new ScopeBinding(this.getSource(), clazz, scope));
        }
        
        @Override
        public void convertToTypes(final Matcher<? super TypeLiteral<?>> matcher, final TypeConverter typeConverter) {
            this.elements.add(new TypeConverterBinding(this.getSource(), matcher, typeConverter));
        }
        
        @Override
        public Stage currentStage() {
            return this.stage;
        }
        
        @Override
        public void disableCircularProxies() {
            this.elements.add(new DisableCircularProxiesOption(this.getSource()));
        }
        
        @Override
        public void expose(final Key<?> key) {
            this.exposeInternal(key);
        }
        
        @Override
        public <T> MembersInjector<T> getMembersInjector(final TypeLiteral<T> typeLiteral) {
            final MembersInjectorLookup<T> membersInjectorLookup = new MembersInjectorLookup<T>(this.getSource(), typeLiteral);
            this.elements.add(membersInjectorLookup);
            return membersInjectorLookup.getMembersInjector();
        }
        
        @Override
        public <T> MembersInjector<T> getMembersInjector(final Class<T> clazz) {
            return this.getMembersInjector((TypeLiteral<T>)TypeLiteral.get((Class<T>)clazz));
        }
        
        @Override
        public <T> Provider<T> getProvider(final Key<T> key) {
            final ProviderLookup<T> providerLookup = new ProviderLookup<T>(this.getSource(), key);
            this.elements.add(providerLookup);
            return providerLookup.getProvider();
        }
        
        @Override
        public <T> Provider<T> getProvider(final Class<T> clazz) {
            return this.getProvider((Key<T>)Key.get((Class<T>)clazz));
        }
        
        protected Object getSource() {
            if (this.sourceProvider != null) {
                return this.sourceProvider.get();
            }
            return this.source;
        }
        
        @Override
        public void install(final Module module) {
            if (!this.modules.add(module)) {
                return;
            }
            PrivateBinder privateBinder = this;
            if (module instanceof PrivateModule) {
                privateBinder = super.newPrivateBinder();
            }
            while (true) {
                try {
                    module.configure(privateBinder);
                    privateBinder.install(ProviderMethodsModule.forModule(module));
                }
                catch (RuntimeException ex) {
                    final Collection<Message> messagesFromThrowable = Errors.getMessagesFromThrowable(ex);
                    if (!messagesFromThrowable.isEmpty()) {
                        this.elements.addAll(messagesFromThrowable);
                        continue;
                    }
                    this.addError(ex);
                    continue;
                }
                break;
            }
        }
        
        @Override
        public PrivateBinder newPrivateBinder() {
            final PrivateElementsImpl privateElementsImpl = new PrivateElementsImpl(this.getSource());
            this.elements.add(privateElementsImpl);
            return new RecordingBinder(this, privateElementsImpl);
        }
        
        @Override
        public <T> void requestInjection(final TypeLiteral<T> typeLiteral, final T t) {
            this.elements.add(new InjectionRequest<Object>(this.getSource(), (TypeLiteral<Object>)typeLiteral, t));
        }
        
        @Override
        public void requestInjection(final Object o) {
            this.requestInjection((TypeLiteral<Object>)TypeLiteral.get(o.getClass()), o);
        }
        
        @Override
        public void requestStaticInjection(final Class<?>... array) {
            for (int length = array.length, i = 0; i < length; ++i) {
                this.elements.add(new StaticInjectionRequest(this.getSource(), array[i]));
            }
        }
        
        @Override
        public void requireExplicitBindings() {
            this.elements.add(new RequireExplicitBindingsOption(this.getSource()));
        }
        
        @Override
        public RecordingBinder skipSources(final Class... array) {
            if (this.source != null) {
                return this;
            }
            return new RecordingBinder(this, null, this.sourceProvider.plusSkippedClasses(array));
        }
        
        @Override
        public String toString() {
            return "Binder";
        }
        
        @Override
        public RecordingBinder withSource(final Object o) {
            return new RecordingBinder(this, o, null);
        }
    }
}
