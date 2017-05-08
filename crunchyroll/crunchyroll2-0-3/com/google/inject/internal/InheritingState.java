// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.internal.util.$Lists;
import java.util.Collections;
import com.google.inject.internal.util.$Maps;
import com.google.inject.Scope;
import java.lang.annotation.Annotation;
import com.google.inject.spi.TypeListenerBinding;
import com.google.inject.Binding;
import com.google.inject.Key;
import java.util.Map;
import com.google.inject.spi.TypeConverterBinding;
import java.util.List;

final class InheritingState implements State
{
    private final WeakKeySet blacklistedKeys;
    private final List<TypeConverterBinding> converters;
    private final Map<Key<?>, Binding<?>> explicitBindings;
    private final Map<Key<?>, Binding<?>> explicitBindingsMutable;
    private final List<TypeListenerBinding> listenerBindings;
    private final Object lock;
    private final State parent;
    private final Map<Class<? extends Annotation>, Scope> scopes;
    
    InheritingState(final State state) {
        this.explicitBindingsMutable = (Map<Key<?>, Binding<?>>)$Maps.newLinkedHashMap();
        this.explicitBindings = Collections.unmodifiableMap((Map<? extends Key<?>, ? extends Binding<?>>)this.explicitBindingsMutable);
        this.scopes = (Map<Class<? extends Annotation>, Scope>)$Maps.newHashMap();
        this.converters = (List<TypeConverterBinding>)$Lists.newArrayList();
        this.listenerBindings = (List<TypeListenerBinding>)$Lists.newArrayList();
        this.blacklistedKeys = new WeakKeySet();
        this.parent = $Preconditions.checkNotNull(state, "parent");
        Object lock;
        if (state == State.NONE) {
            lock = this;
        }
        else {
            lock = state.lock();
        }
        this.lock = lock;
    }
    
    @Override
    public void addConverter(final TypeConverterBinding typeConverterBinding) {
        this.converters.add(typeConverterBinding);
    }
    
    @Override
    public void addTypeListener(final TypeListenerBinding typeListenerBinding) {
        this.listenerBindings.add(typeListenerBinding);
    }
    
    @Override
    public void blacklist(final Key<?> key, final Object o) {
        this.parent.blacklist(key, o);
        this.blacklistedKeys.add(key, o);
    }
    
    @Override
    public TypeConverterBinding getConverter(final String s, final TypeLiteral<?> typeLiteral, final Errors errors, final Object o) {
        TypeConverterBinding typeConverterBinding = null;
        for (State parent = this; parent != State.NONE; parent = parent.parent()) {
            for (final TypeConverterBinding typeConverterBinding2 : parent.getConvertersThisLevel()) {
                if (typeConverterBinding2.getTypeMatcher().matches(typeLiteral)) {
                    if (typeConverterBinding != null) {
                        errors.ambiguousTypeConversion(s, o, typeLiteral, typeConverterBinding, typeConverterBinding2);
                    }
                    typeConverterBinding = typeConverterBinding2;
                }
            }
        }
        return typeConverterBinding;
    }
    
    @Override
    public Iterable<TypeConverterBinding> getConvertersThisLevel() {
        return this.converters;
    }
    
    @Override
    public <T> BindingImpl<T> getExplicitBinding(final Key<T> key) {
        final Binding<?> binding = this.explicitBindings.get(key);
        if (binding != null) {
            return (BindingImpl<T>)binding;
        }
        return this.parent.getExplicitBinding(key);
    }
    
    @Override
    public Map<Key<?>, Binding<?>> getExplicitBindingsThisLevel() {
        return this.explicitBindings;
    }
    
    @Override
    public Scope getScope(final Class<? extends Annotation> clazz) {
        final Scope scope = this.scopes.get(clazz);
        if (scope != null) {
            return scope;
        }
        return this.parent.getScope(clazz);
    }
    
    @Override
    public Map<Class<? extends Annotation>, Scope> getScopes() {
        return this.scopes;
    }
    
    @Override
    public Set<Object> getSourcesForBlacklistedKey(final Key<?> key) {
        return this.blacklistedKeys.getSources(key);
    }
    
    @Override
    public List<TypeListenerBinding> getTypeListenerBindings() {
        final List<TypeListenerBinding> typeListenerBindings = this.parent.getTypeListenerBindings();
        final ArrayList list = new ArrayList<Object>(typeListenerBindings.size() + 1);
        list.addAll((Collection<?>)typeListenerBindings);
        list.addAll((Collection<?>)this.listenerBindings);
        return (List<TypeListenerBinding>)list;
    }
    
    @Override
    public boolean isBlacklisted(final Key<?> key) {
        return this.blacklistedKeys.contains(key);
    }
    
    @Override
    public Object lock() {
        return this.lock;
    }
    
    @Override
    public State parent() {
        return this.parent;
    }
    
    @Override
    public void putAnnotation(final Class<? extends Annotation> clazz, final Scope scope) {
        this.scopes.put(clazz, scope);
    }
    
    @Override
    public void putBinding(final Key<?> key, final BindingImpl<?> bindingImpl) {
        this.explicitBindingsMutable.put(key, bindingImpl);
    }
}
