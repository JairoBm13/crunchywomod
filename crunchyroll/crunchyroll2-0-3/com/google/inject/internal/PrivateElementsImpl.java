// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.internal.util.$ToStringBuilder;
import java.util.LinkedHashMap;
import com.google.inject.internal.util.$Maps;
import java.util.Set;
import java.util.Iterator;
import com.google.inject.PrivateBinder;
import java.util.Map;
import com.google.inject.Binder;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.internal.util.$Lists;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.internal.util.$ImmutableMap;
import java.util.List;
import com.google.inject.spi.Element;
import com.google.inject.internal.util.$ImmutableList;
import com.google.inject.spi.PrivateElements;

public final class PrivateElementsImpl implements PrivateElements
{
    private $ImmutableList<Element> elements;
    private List<Element> elementsMutable;
    private $ImmutableMap<Key<?>, Object> exposedKeysToSources;
    private List<ExposureBuilder<?>> exposureBuilders;
    private Injector injector;
    private final Object source;
    
    public PrivateElementsImpl(final Object o) {
        this.elementsMutable = (List<Element>)$Lists.newArrayList();
        this.exposureBuilders = (List<ExposureBuilder<?>>)$Lists.newArrayList();
        this.source = $Preconditions.checkNotNull(o, "source");
    }
    
    @Override
    public <T> T acceptVisitor(final ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }
    
    public void addExposureBuilder(final ExposureBuilder<?> exposureBuilder) {
        this.exposureBuilders.add(exposureBuilder);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        final PrivateBinder privateBinder = binder.withSource(this.source).newPrivateBinder();
        final Iterator<Element> iterator = this.getElements().iterator();
        while (iterator.hasNext()) {
            iterator.next().applyTo(privateBinder);
        }
        this.getExposedKeys();
        for (final Map.Entry<K, Object> entry : this.exposedKeysToSources.entrySet()) {
            privateBinder.withSource(entry.getValue()).expose((Key<?>)entry.getKey());
        }
    }
    
    @Override
    public List<Element> getElements() {
        if (this.elements == null) {
            this.elements = $ImmutableList.copyOf((Iterable<? extends Element>)this.elementsMutable);
            this.elementsMutable = null;
        }
        return this.elements;
    }
    
    public List<Element> getElementsMutable() {
        return this.elementsMutable;
    }
    
    @Override
    public Set<Key<?>> getExposedKeys() {
        if (this.exposedKeysToSources == null) {
            final LinkedHashMap<Object, Object> linkedHashMap = $Maps.newLinkedHashMap();
            for (final ExposureBuilder<?> exposureBuilder : this.exposureBuilders) {
                linkedHashMap.put(exposureBuilder.getKey(), exposureBuilder.getSource());
            }
            this.exposedKeysToSources = $ImmutableMap.copyOf((Map<? extends Key<?>, ?>)linkedHashMap);
            this.exposureBuilders = null;
        }
        return this.exposedKeysToSources.keySet();
    }
    
    @Override
    public Object getExposedSource(final Key<?> key) {
        this.getExposedKeys();
        final Object value = this.exposedKeysToSources.get(key);
        $Preconditions.checkArgument(value != null, "%s not exposed by %s.", key, this);
        return value;
    }
    
    @Override
    public Injector getInjector() {
        return this.injector;
    }
    
    @Override
    public Object getSource() {
        return this.source;
    }
    
    public void initInjector(final Injector injector) {
        $Preconditions.checkState(this.injector == null, (Object)"injector already initialized");
        this.injector = $Preconditions.checkNotNull(injector, "injector");
    }
    
    @Override
    public String toString() {
        return new $ToStringBuilder(PrivateElements.class).add("exposedKeys", this.getExposedKeys()).add("source", this.getSource()).toString();
    }
}
