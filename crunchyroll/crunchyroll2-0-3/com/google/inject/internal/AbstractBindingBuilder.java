// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.Scope;
import com.google.inject.spi.InstanceBinding;
import com.google.inject.internal.util.$Preconditions;
import java.lang.annotation.Annotation;
import com.google.inject.spi.Element;
import java.util.List;
import com.google.inject.Binder;
import com.google.inject.Key;

public abstract class AbstractBindingBuilder<T>
{
    protected static final Key<?> NULL_KEY;
    protected final Binder binder;
    private BindingImpl<T> binding;
    protected List<Element> elements;
    protected int position;
    
    static {
        NULL_KEY = Key.get((Class<?>)Void.class);
    }
    
    public AbstractBindingBuilder(final Binder binder, final List<Element> elements, final Object o, final Key<T> key) {
        this.binder = binder;
        this.elements = elements;
        this.position = elements.size();
        this.binding = new UntargettedBindingImpl<T>(o, key, Scoping.UNSCOPED);
        elements.add(this.position, this.binding);
    }
    
    protected BindingImpl<T> annotatedWithInternal(final Annotation annotation) {
        $Preconditions.checkNotNull(annotation, "annotation");
        this.checkNotAnnotated();
        return this.setBinding(this.binding.withKey(Key.get(this.binding.getKey().getTypeLiteral(), annotation)));
    }
    
    public void asEagerSingleton() {
        this.checkNotScoped();
        this.setBinding(this.getBinding().withScoping(Scoping.EAGER_SINGLETON));
    }
    
    protected void checkNotAnnotated() {
        if (this.binding.getKey().getAnnotationType() != null) {
            this.binder.addError("More than one annotation is specified for this binding.", new Object[0]);
        }
    }
    
    protected void checkNotScoped() {
        if (this.binding instanceof InstanceBinding) {
            this.binder.addError("Setting the scope is not permitted when binding to a single instance.", new Object[0]);
        }
        else if (this.binding.getScoping().isExplicitlyScoped()) {
            this.binder.addError("Scope is set more than once.", new Object[0]);
        }
    }
    
    protected void checkNotTargetted() {
        if (!(this.binding instanceof UntargettedBindingImpl)) {
            this.binder.addError("Implementation is set more than once.", new Object[0]);
        }
    }
    
    protected BindingImpl<T> getBinding() {
        return this.binding;
    }
    
    public void in(final Scope scope) {
        $Preconditions.checkNotNull(scope, "scope");
        this.checkNotScoped();
        this.setBinding(this.getBinding().withScoping(Scoping.forInstance(scope)));
    }
    
    public void in(final Class<? extends Annotation> clazz) {
        $Preconditions.checkNotNull(clazz, "scopeAnnotation");
        this.checkNotScoped();
        this.setBinding(this.getBinding().withScoping(Scoping.forAnnotation(clazz)));
    }
    
    protected boolean keyTypeIsSet() {
        return !Void.class.equals(this.binding.getKey().getTypeLiteral().getType());
    }
    
    protected BindingImpl<T> setBinding(final BindingImpl<T> binding) {
        this.binding = binding;
        this.elements.set(this.position, binding);
        return binding;
    }
}
