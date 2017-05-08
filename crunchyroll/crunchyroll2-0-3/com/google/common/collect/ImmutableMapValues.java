// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.Map;

abstract class ImmutableMapValues<K, V> extends ImmutableCollection<V>
{
    @Override
    public boolean contains(final Object o) {
        return this.map().containsValue(o);
    }
    
    @Override
    ImmutableList<V> createAsList() {
        return new ImmutableAsList<V>() {
            final /* synthetic */ ImmutableList val$entryList = ImmutableMapValues.this.map().entrySet().asList();
            
            @Override
            ImmutableCollection<V> delegateCollection() {
                return (ImmutableCollection<V>)ImmutableMapValues.this;
            }
            
            @Override
            public V get(final int n) {
                return ((Map.Entry)this.val$entryList.get(n)).getValue();
            }
        };
    }
    
    @Override
    boolean isPartialView() {
        return true;
    }
    
    @Override
    public UnmodifiableIterator<V> iterator() {
        return Maps.valueIterator(this.map().entrySet().iterator());
    }
    
    abstract ImmutableMap<K, V> map();
    
    @Override
    public int size() {
        return this.map().size();
    }
}
