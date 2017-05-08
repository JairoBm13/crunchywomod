// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.util.Iterator;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.UntargettedBinding;
import com.google.inject.spi.ProviderKeyBinding;
import com.google.inject.spi.ProviderBinding;
import com.google.inject.spi.LinkedKeyBinding;
import com.google.inject.spi.InjectionPoint;
import java.util.Set;
import com.google.inject.spi.InstanceBinding;
import com.google.inject.spi.ExposedBinding;
import com.google.inject.spi.ConvertedConstantBinding;
import com.google.inject.spi.ConstructorBinding;
import com.google.inject.Provider;
import com.google.inject.spi.ProviderInstanceBinding;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.spi.PrivateElements;

final class BindingProcessor extends AbstractBindingProcessor
{
    private final Initializer initializer;
    
    BindingProcessor(final Errors errors, final Initializer initializer, final ProcessedBindingData processedBindingData) {
        super(errors, processedBindingData);
        this.initializer = initializer;
    }
    
    private <T> void bindExposed(final PrivateElements privateElements, final Key<T> key) {
        final ExposedKeyFactory<Object> exposedKeyFactory = new ExposedKeyFactory<Object>((Key<Object>)key, privateElements);
        this.bindingData.addCreationListener(exposedKeyFactory);
        this.putBinding(new ExposedBindingImpl<Object>(this.injector, privateElements.getExposedSource(key), (Key<Object>)key, exposedKeyFactory, privateElements));
    }
    
    @Override
    public <T> Boolean visit(final Binding<T> binding) {
        final Class<? super T> rawType = binding.getKey().getTypeLiteral().getRawType();
        if (Void.class.equals(rawType)) {
            if (binding instanceof ProviderInstanceBinding && ((ProviderInstanceBinding)binding).getProviderInstance() instanceof ProviderMethod) {
                this.errors.voidProviderMethod();
            }
            else {
                this.errors.missingConstantValues();
            }
            return true;
        }
        if (rawType == Provider.class) {
            this.errors.bindingToProvider();
            return true;
        }
        return binding.acceptTargetVisitor((BindingTargetVisitor<? super Object, Boolean>)new Processor<T, Boolean>((BindingImpl<Object>)binding) {
            @Override
            public Boolean visit(final ConstructorBinding<? extends T> constructorBinding) {
                ((Processor)this).prepareBinding();
                try {
                    final ConstructorBindingImpl<T> create = ConstructorBindingImpl.create(BindingProcessor.this.injector, this.key, constructorBinding.getConstructor(), this.source, this.scoping, BindingProcessor.this.errors, false);
                    ((Processor)this).scheduleInitialization(create);
                    BindingProcessor.this.putBinding(create);
                    return true;
                }
                catch (ErrorsException ex) {
                    BindingProcessor.this.errors.merge(ex.getErrors());
                    BindingProcessor.this.putBinding(BindingProcessor.this.invalidBinding(BindingProcessor.this.injector, this.key, this.source));
                    return true;
                }
            }
            
            @Override
            public Boolean visit(final ConvertedConstantBinding<? extends T> convertedConstantBinding) {
                throw new IllegalArgumentException("Cannot apply a non-module element");
            }
            
            @Override
            public Boolean visit(final ExposedBinding<? extends T> exposedBinding) {
                throw new IllegalArgumentException("Cannot apply a non-module element");
            }
            
            @Override
            public Boolean visit(final InstanceBinding<? extends T> instanceBinding) {
                ((Processor)this).prepareBinding();
                final Set<InjectionPoint> injectionPoints = instanceBinding.getInjectionPoints();
                final T instance = (T)instanceBinding.getInstance();
                BindingProcessor.this.putBinding(new InstanceBindingImpl<Object>(BindingProcessor.this.injector, (Key<Object>)this.key, this.source, Scoping.scope(this.key, BindingProcessor.this.injector, (InternalFactory<? extends T>)new ConstantFactory<Object>((Initializable<Object>)BindingProcessor.this.initializer.requestInjection(BindingProcessor.this.injector, instance, this.source, injectionPoints)), this.source, this.scoping), injectionPoints, instance));
                return true;
            }
            
            @Override
            public Boolean visit(final LinkedKeyBinding<? extends T> linkedKeyBinding) {
                ((Processor)this).prepareBinding();
                final Key<? extends T> linkedKey = linkedKeyBinding.getLinkedKey();
                if (this.key.equals(linkedKey)) {
                    BindingProcessor.this.errors.recursiveBinding();
                }
                final FactoryProxy factoryProxy = new FactoryProxy<Object>(BindingProcessor.this.injector, (Key<? extends T>)this.key, (Key<? extends T>)linkedKey, this.source);
                BindingProcessor.this.bindingData.addCreationListener(factoryProxy);
                BindingProcessor.this.putBinding(new LinkedBindingImpl<Object>(BindingProcessor.this.injector, (Key<Object>)this.key, this.source, Scoping.scope(this.key, BindingProcessor.this.injector, (InternalFactory<? extends T>)factoryProxy, this.source, this.scoping), this.scoping, linkedKey));
                return true;
            }
            
            @Override
            public Boolean visit(final ProviderBinding<? extends T> providerBinding) {
                throw new IllegalArgumentException("Cannot apply a non-module element");
            }
            
            @Override
            public Boolean visit(final ProviderInstanceBinding<? extends T> providerInstanceBinding) {
                ((Processor)this).prepareBinding();
                final Provider<? extends T> providerInstance = providerInstanceBinding.getProviderInstance();
                final Set<InjectionPoint> injectionPoints = providerInstanceBinding.getInjectionPoints();
                BindingProcessor.this.putBinding(new ProviderInstanceBindingImpl<Object>(BindingProcessor.this.injector, (Key<Object>)this.key, this.source, Scoping.scope(this.key, BindingProcessor.this.injector, (InternalFactory<? extends T>)new InternalFactoryToProviderAdapter<Object>((Initializable<Provider<?>>)BindingProcessor.this.initializer.requestInjection(BindingProcessor.this.injector, (Provider<? extends T>)providerInstance, this.source, injectionPoints), this.source), this.source, this.scoping), this.scoping, providerInstance, injectionPoints));
                return true;
            }
            
            @Override
            public Boolean visit(final ProviderKeyBinding<? extends T> providerKeyBinding) {
                ((Processor)this).prepareBinding();
                final Key<? extends javax.inject.Provider<? extends T>> providerKey = providerKeyBinding.getProviderKey();
                final BoundProviderFactory boundProviderFactory = new BoundProviderFactory<Object>(BindingProcessor.this.injector, (Key<? extends javax.inject.Provider<? extends T>>)providerKey, this.source);
                BindingProcessor.this.bindingData.addCreationListener(boundProviderFactory);
                BindingProcessor.this.putBinding(new LinkedProviderBindingImpl<Object>(BindingProcessor.this.injector, (Key<Object>)this.key, this.source, Scoping.scope(this.key, BindingProcessor.this.injector, (InternalFactory<? extends T>)boundProviderFactory, this.source, this.scoping), this.scoping, providerKey));
                return true;
            }
            
            @Override
            public Boolean visit(final UntargettedBinding<? extends T> untargettedBinding) {
                return false;
            }
            
            @Override
            protected Boolean visitOther(final Binding<? extends T> binding) {
                throw new IllegalStateException("BindingProcessor should override all visitations");
            }
        });
    }
    
    @Override
    public Boolean visit(final PrivateElements privateElements) {
        final Iterator<Key<?>> iterator = privateElements.getExposedKeys().iterator();
        while (iterator.hasNext()) {
            this.bindExposed(privateElements, (Key<Object>)iterator.next());
        }
        return false;
    }
}
