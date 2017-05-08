// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.internal.util.$ToStringBuilder;
import com.google.inject.internal.util.$ImmutableSet;
import com.google.inject.spi.Dependency;
import com.google.inject.internal.util.$Objects;
import java.lang.reflect.Constructor;
import com.google.inject.Binder;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.spi.BindingTargetVisitor;
import java.lang.annotation.Annotation;
import com.google.inject.ConfigurationException;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.util.$Classes;
import java.lang.reflect.Modifier;
import java.util.Set;
import com.google.inject.Key;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.ConstructorBinding;

final class ConstructorBindingImpl<T> extends BindingImpl<T> implements ConstructorBinding<T>
{
    private final InjectionPoint constructorInjectionPoint;
    private final Factory<T> factory;
    
    public ConstructorBindingImpl(final Key<T> key, final Object o, final Scoping scoping, final InjectionPoint constructorInjectionPoint, final Set<InjectionPoint> set) {
        super(o, key, scoping);
        this.factory = new Factory<T>(false, key);
        final ConstructionProxy<Object> create = new DefaultConstructionProxyFactory<Object>(constructorInjectionPoint).create();
        this.constructorInjectionPoint = constructorInjectionPoint;
        ((Factory<Object>)this.factory).constructorInjector = (ConstructorInjector<Object>)new ConstructorInjector(set, create, null, null);
    }
    
    private ConstructorBindingImpl(final InjectorImpl injectorImpl, final Key<T> key, final Object o, final InternalFactory<? extends T> internalFactory, final Scoping scoping, final Factory<T> factory, final InjectionPoint constructorInjectionPoint) {
        super(injectorImpl, key, o, internalFactory, scoping);
        this.factory = factory;
        this.constructorInjectionPoint = constructorInjectionPoint;
    }
    
    static <T> ConstructorBindingImpl<T> create(final InjectorImpl injectorImpl, final Key<T> key, final InjectionPoint injectionPoint, final Object o, final Scoping scoping, final Errors errors, final boolean b) throws ErrorsException {
        final int size = errors.size();
        Label_0174: {
            if (injectionPoint != null) {
                break Label_0174;
            }
            Class<?> clazz = key.getTypeLiteral().getRawType();
            while (true) {
                if (Modifier.isAbstract(clazz.getModifiers())) {
                    errors.missingImplementation(key);
                }
                if ($Classes.isInnerClass(clazz)) {
                    errors.cannotInjectInnerClass(clazz);
                }
                errors.throwIfNewErrors(size);
                Label_0077: {
                    InjectionPoint forConstructor;
                    if ((forConstructor = injectionPoint) != null) {
                        break Label_0077;
                    }
                    try {
                        forConstructor = InjectionPoint.forConstructorOf(key.getTypeLiteral());
                        Scoping injectable = scoping;
                        if (!scoping.isExplicitlyScoped()) {
                            final Class<? extends Annotation> scopeAnnotation = Annotations.findScopeAnnotation(errors, forConstructor.getMember().getDeclaringClass());
                            injectable = scoping;
                            if (scopeAnnotation != null) {
                                injectable = Scoping.makeInjectable(Scoping.forAnnotation(scopeAnnotation), injectorImpl, errors.withSource(clazz));
                            }
                        }
                        errors.throwIfNewErrors(size);
                        final Factory<Object> factory = new Factory<Object>(b, key);
                        return new ConstructorBindingImpl<T>(injectorImpl, (Key<Object>)key, o, Scoping.scope(key, injectorImpl, (InternalFactory<? extends T>)factory, o, injectable), injectable, factory, forConstructor);
                        clazz = injectionPoint.getDeclaringType().getRawType();
                        continue;
                    }
                    catch (ConfigurationException ex) {
                        throw errors.merge(ex.getErrorMessages()).toException();
                    }
                }
                break;
            }
        }
    }
    
    @Override
    public <V> V acceptTargetVisitor(final BindingTargetVisitor<? super T, V> bindingTargetVisitor) {
        $Preconditions.checkState(((Factory<Object>)this.factory).constructorInjector != null, (Object)"not initialized");
        return bindingTargetVisitor.visit((ConstructorBinding<? extends T>)this);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        this.getScoping().applyTo(binder.withSource(this.getSource()).bind(this.getKey()).toConstructor((Constructor<Object>)this.getConstructor().getMember(), this.getConstructor().getDeclaringType()));
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o instanceof ConstructorBindingImpl) {
            final ConstructorBindingImpl constructorBindingImpl = (ConstructorBindingImpl)o;
            b2 = b;
            if (this.getKey().equals(constructorBindingImpl.getKey())) {
                b2 = b;
                if (this.getScoping().equals(constructorBindingImpl.getScoping())) {
                    b2 = b;
                    if ($Objects.equal(this.constructorInjectionPoint, constructorBindingImpl.constructorInjectionPoint)) {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public InjectionPoint getConstructor() {
        $Preconditions.checkState(((Factory<Object>)this.factory).constructorInjector != null, (Object)"Binding is not ready");
        return ((Factory<Object>)this.factory).constructorInjector.getConstructionProxy().getInjectionPoint();
    }
    
    @Override
    public Set<Dependency<?>> getDependencies() {
        return Dependency.forInjectionPoints(new $ImmutableSet.Builder<InjectionPoint>().add(this.getConstructor()).addAll(this.getInjectableMembers()).build());
    }
    
    public Set<InjectionPoint> getInjectableMembers() {
        $Preconditions.checkState(((Factory<Object>)this.factory).constructorInjector != null, (Object)"Binding is not ready");
        return (Set<InjectionPoint>)((Factory<Object>)this.factory).constructorInjector.getInjectableMembers();
    }
    
    InjectionPoint getInternalConstructor() {
        if (((Factory<Object>)this.factory).constructorInjector != null) {
            return ((Factory<Object>)this.factory).constructorInjector.getConstructionProxy().getInjectionPoint();
        }
        return this.constructorInjectionPoint;
    }
    
    Set<Dependency<?>> getInternalDependencies() {
        final $ImmutableSet.Builder<InjectionPoint> builder = $ImmutableSet.builder();
        while (true) {
            if (((Factory<Object>)this.factory).constructorInjector == null) {
                builder.add(this.constructorInjectionPoint);
                while (true) {
                    try {
                        builder.addAll(InjectionPoint.forInstanceMethodsAndFields(this.constructorInjectionPoint.getDeclaringType()));
                        return Dependency.forInjectionPoints(builder.build());
                        builder.add(this.getConstructor()).addAll(this.getInjectableMembers());
                        return Dependency.forInjectionPoints(builder.build());
                    }
                    catch (ConfigurationException ex) {
                        return Dependency.forInjectionPoints(builder.build());
                    }
                    continue;
                }
            }
            continue;
        }
    }
    
    @Override
    public int hashCode() {
        return $Objects.hashCode(this.getKey(), this.getScoping(), this.constructorInjectionPoint);
    }
    
    public void initialize(final InjectorImpl injectorImpl, final Errors errors) throws ErrorsException {
        ((Factory<Object>)this.factory).allowCircularProxy = !injectorImpl.options.disableCircularProxies;
        ((Factory<Object>)this.factory).constructorInjector = (ConstructorInjector<Object>)injectorImpl.constructors.get(this.constructorInjectionPoint, errors);
    }
    
    boolean isInitialized() {
        return ((Factory<Object>)this.factory).constructorInjector != null;
    }
    
    @Override
    public String toString() {
        return new $ToStringBuilder(ConstructorBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("scope", this.getScoping()).toString();
    }
    
    @Override
    protected BindingImpl<T> withKey(final Key<T> key) {
        return new ConstructorBindingImpl(null, (Key<Object>)key, this.getSource(), this.factory, this.getScoping(), (Factory<Object>)this.factory, this.constructorInjectionPoint);
    }
    
    @Override
    protected BindingImpl<T> withScoping(final Scoping scoping) {
        return new ConstructorBindingImpl(null, this.getKey(), this.getSource(), this.factory, scoping, (Factory<Object>)this.factory, this.constructorInjectionPoint);
    }
    
    private static class Factory<T> implements InternalFactory<T>
    {
        private boolean allowCircularProxy;
        private ConstructorInjector<T> constructorInjector;
        private final boolean failIfNotLinked;
        private final Key<?> key;
        
        Factory(final boolean failIfNotLinked, final Key<?> key) {
            this.failIfNotLinked = failIfNotLinked;
            this.key = key;
        }
        
        @Override
        public T get(final Errors errors, final InternalContext internalContext, final Dependency<?> dependency, final boolean b) throws ErrorsException {
            $Preconditions.checkState(this.constructorInjector != null, (Object)"Constructor not ready");
            if (this.failIfNotLinked && !b) {
                throw errors.jitDisabled(this.key).toException();
            }
            return (T)this.constructorInjector.construct(errors, internalContext, dependency.getKey().getTypeLiteral().getRawType(), this.allowCircularProxy);
        }
    }
}
