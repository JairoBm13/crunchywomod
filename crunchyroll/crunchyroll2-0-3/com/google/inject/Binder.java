// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject;

import com.google.inject.spi.TypeConverter;
import java.lang.annotation.Annotation;
import com.google.inject.spi.TypeListener;
import com.google.inject.matcher.Matcher;
import com.google.inject.binder.AnnotatedConstantBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.spi.Message;

public interface Binder
{
    void addError(final Message p0);
    
    void addError(final String p0, final Object... p1);
    
    void addError(final Throwable p0);
    
     <T> AnnotatedBindingBuilder<T> bind(final TypeLiteral<T> p0);
    
     <T> AnnotatedBindingBuilder<T> bind(final Class<T> p0);
    
     <T> LinkedBindingBuilder<T> bind(final Key<T> p0);
    
    AnnotatedConstantBindingBuilder bindConstant();
    
    void bindListener(final Matcher<? super TypeLiteral<?>> p0, final TypeListener p1);
    
    void bindScope(final Class<? extends Annotation> p0, final Scope p1);
    
    void convertToTypes(final Matcher<? super TypeLiteral<?>> p0, final TypeConverter p1);
    
    Stage currentStage();
    
    void disableCircularProxies();
    
     <T> MembersInjector<T> getMembersInjector(final TypeLiteral<T> p0);
    
     <T> MembersInjector<T> getMembersInjector(final Class<T> p0);
    
     <T> Provider<T> getProvider(final Key<T> p0);
    
     <T> Provider<T> getProvider(final Class<T> p0);
    
    void install(final Module p0);
    
    PrivateBinder newPrivateBinder();
    
     <T> void requestInjection(final TypeLiteral<T> p0, final T p1);
    
    void requestInjection(final Object p0);
    
    void requestStaticInjection(final Class<?>... p0);
    
    void requireExplicitBindings();
    
    Binder skipSources(final Class... p0);
    
    Binder withSource(final Object p0);
}
