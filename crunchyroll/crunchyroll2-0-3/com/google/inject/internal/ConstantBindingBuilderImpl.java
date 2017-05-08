// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.lang.annotation.Annotation;
import com.google.inject.spi.InjectionPoint;
import java.util.Set;
import com.google.inject.internal.util.$ImmutableSet;
import com.google.inject.Key;
import com.google.inject.spi.Element;
import java.util.List;
import com.google.inject.Binder;
import com.google.inject.binder.ConstantBindingBuilder;
import com.google.inject.binder.AnnotatedConstantBindingBuilder;

public final class ConstantBindingBuilderImpl<T> extends AbstractBindingBuilder<T> implements AnnotatedConstantBindingBuilder, ConstantBindingBuilder
{
    public ConstantBindingBuilderImpl(final Binder binder, final List<Element> list, final Object o) {
        super(binder, list, o, ConstantBindingBuilderImpl.NULL_KEY);
    }
    
    private void toConstant(final Class<?> clazz, final Object o) {
        if (this.keyTypeIsSet()) {
            this.binder.addError("Constant value is set more than once.", new Object[0]);
            return;
        }
        final BindingImpl<T> binding = this.getBinding();
        Object o2;
        if (binding.getKey().getAnnotation() != null) {
            o2 = Key.get(clazz, binding.getKey().getAnnotation());
        }
        else if (binding.getKey().getAnnotationType() != null) {
            o2 = Key.get(clazz, binding.getKey().getAnnotationType());
        }
        else {
            o2 = Key.get(clazz);
        }
        if (o == null) {
            this.binder.addError("Binding to null instances is not allowed. Use toProvider(Providers.of(null)) if this is your intended behaviour.", new Object[0]);
        }
        this.setBinding(new InstanceBindingImpl<T>(binding.getSource(), (Key<Object>)o2, binding.getScoping(), (Set<InjectionPoint>)$ImmutableSet.of(), o));
    }
    
    @Override
    public ConstantBindingBuilder annotatedWith(final Annotation annotation) {
        this.annotatedWithInternal(annotation);
        return this;
    }
    
    @Override
    public void to(final String s) {
        this.toConstant(String.class, s);
    }
    
    @Override
    public String toString() {
        return "ConstantBindingBuilder";
    }
}
