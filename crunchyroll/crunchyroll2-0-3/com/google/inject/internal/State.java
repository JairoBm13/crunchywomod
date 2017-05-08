// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.internal.util.$ImmutableList;
import java.util.List;
import java.util.Set;
import com.google.inject.internal.util.$ImmutableMap;
import com.google.inject.Scope;
import java.lang.annotation.Annotation;
import com.google.inject.Binding;
import java.util.Map;
import com.google.inject.internal.util.$ImmutableSet;
import com.google.inject.TypeLiteral;
import com.google.inject.Key;
import com.google.inject.spi.TypeListenerBinding;
import com.google.inject.spi.TypeConverterBinding;

interface State
{
    public static final State NONE = new State() {
        @Override
        public void addConverter(final TypeConverterBinding typeConverterBinding) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addTypeListener(final TypeListenerBinding typeListenerBinding) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void blacklist(final Key<?> key, final Object o) {
        }
        
        @Override
        public TypeConverterBinding getConverter(final String s, final TypeLiteral<?> typeLiteral, final Errors errors, final Object o) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Iterable<TypeConverterBinding> getConvertersThisLevel() {
            return (Iterable<TypeConverterBinding>)$ImmutableSet.of();
        }
        
        @Override
        public <T> BindingImpl<T> getExplicitBinding(final Key<T> key) {
            return null;
        }
        
        @Override
        public Map<Key<?>, Binding<?>> getExplicitBindingsThisLevel() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Scope getScope(final Class<? extends Annotation> clazz) {
            return null;
        }
        
        @Override
        public Map<Class<? extends Annotation>, Scope> getScopes() {
            return (Map<Class<? extends Annotation>, Scope>)$ImmutableMap.of();
        }
        
        @Override
        public Set<Object> getSourcesForBlacklistedKey(final Key<?> key) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public List<TypeListenerBinding> getTypeListenerBindings() {
            return (List<TypeListenerBinding>)$ImmutableList.of();
        }
        
        @Override
        public boolean isBlacklisted(final Key<?> key) {
            return true;
        }
        
        @Override
        public Object lock() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public State parent() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void putAnnotation(final Class<? extends Annotation> clazz, final Scope scope) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void putBinding(final Key<?> key, final BindingImpl<?> bindingImpl) {
            throw new UnsupportedOperationException();
        }
    };
    
    void addConverter(final TypeConverterBinding p0);
    
    void addTypeListener(final TypeListenerBinding p0);
    
    void blacklist(final Key<?> p0, final Object p1);
    
    TypeConverterBinding getConverter(final String p0, final TypeLiteral<?> p1, final Errors p2, final Object p3);
    
    Iterable<TypeConverterBinding> getConvertersThisLevel();
    
     <T> BindingImpl<T> getExplicitBinding(final Key<T> p0);
    
    Map<Key<?>, Binding<?>> getExplicitBindingsThisLevel();
    
    Scope getScope(final Class<? extends Annotation> p0);
    
    Map<Class<? extends Annotation>, Scope> getScopes();
    
    Set<Object> getSourcesForBlacklistedKey(final Key<?> p0);
    
    List<TypeListenerBinding> getTypeListenerBindings();
    
    boolean isBlacklisted(final Key<?> p0);
    
    Object lock();
    
    State parent();
    
    void putAnnotation(final Class<? extends Annotation> p0, final Scope p1);
    
    void putBinding(final Key<?> p0, final BindingImpl<?> p1);
}
