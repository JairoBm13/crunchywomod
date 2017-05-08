// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject;

import com.google.inject.spi.TypeConverter;
import com.google.inject.internal.util.$Preconditions;
import java.lang.annotation.Annotation;
import com.google.inject.spi.TypeListener;
import com.google.inject.matcher.Matcher;
import com.google.inject.binder.AnnotatedConstantBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.spi.Message;

public abstract class AbstractModule implements Module
{
    Binder binder;
    
    protected void addError(final Message message) {
        this.binder.addError(message);
    }
    
    protected void addError(final String s, final Object... array) {
        this.binder.addError(s, array);
    }
    
    protected void addError(final Throwable t) {
        this.binder.addError(t);
    }
    
    protected <T> AnnotatedBindingBuilder<T> bind(final TypeLiteral<T> typeLiteral) {
        return this.binder.bind(typeLiteral);
    }
    
    protected <T> AnnotatedBindingBuilder<T> bind(final Class<T> clazz) {
        return this.binder.bind(clazz);
    }
    
    protected <T> LinkedBindingBuilder<T> bind(final Key<T> key) {
        return this.binder.bind(key);
    }
    
    protected AnnotatedConstantBindingBuilder bindConstant() {
        return this.binder.bindConstant();
    }
    
    protected void bindListener(final Matcher<? super TypeLiteral<?>> matcher, final TypeListener typeListener) {
        this.binder.bindListener(matcher, typeListener);
    }
    
    protected void bindScope(final Class<? extends Annotation> clazz, final Scope scope) {
        this.binder.bindScope(clazz, scope);
    }
    
    protected Binder binder() {
        return this.binder;
    }
    
    protected abstract void configure();
    
    @Override
    public final void configure(final Binder binder) {
        synchronized (this) {
            Label_0042: {
                if (this.binder != null) {
                    break Label_0042;
                }
                boolean b = true;
                while (true) {
                    $Preconditions.checkState(b, (Object)"Re-entry is not allowed.");
                    this.binder = $Preconditions.checkNotNull(binder, "builder");
                    try {
                        this.configure();
                        return;
                        b = false;
                    }
                    finally {
                        this.binder = null;
                    }
                }
            }
        }
    }
    
    protected void convertToTypes(final Matcher<? super TypeLiteral<?>> matcher, final TypeConverter typeConverter) {
        this.binder.convertToTypes(matcher, typeConverter);
    }
    
    protected Stage currentStage() {
        return this.binder.currentStage();
    }
    
    protected <T> MembersInjector<T> getMembersInjector(final TypeLiteral<T> typeLiteral) {
        return this.binder.getMembersInjector(typeLiteral);
    }
    
    protected <T> MembersInjector<T> getMembersInjector(final Class<T> clazz) {
        return this.binder.getMembersInjector(clazz);
    }
    
    protected <T> Provider<T> getProvider(final Key<T> key) {
        return this.binder.getProvider(key);
    }
    
    protected <T> Provider<T> getProvider(final Class<T> clazz) {
        return this.binder.getProvider(clazz);
    }
    
    protected void install(final Module module) {
        this.binder.install(module);
    }
    
    protected void requestInjection(final Object o) {
        this.binder.requestInjection(o);
    }
    
    protected void requestStaticInjection(final Class<?>... array) {
        this.binder.requestStaticInjection(array);
    }
    
    protected void requireBinding(final Key<?> key) {
        this.binder.getProvider(key);
    }
    
    protected void requireBinding(final Class<?> clazz) {
        this.binder.getProvider(clazz);
    }
}
