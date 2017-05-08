// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Map;

abstract class ImmutableMapKeySet<K, V> extends TransformedImmutableSet<Map.Entry<K, V>, K>
{
    ImmutableMapKeySet(final ImmutableSet<Map.Entry<K, V>> set) {
        super(set);
    }
    
    ImmutableMapKeySet(final ImmutableSet<Map.Entry<K, V>> set, final int n) {
        super(set, n);
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.map().containsKey(o);
    }
    
    @Override
    ImmutableList<K> createAsList() {
        return new ImmutableAsList<K>() {
            final /* synthetic */ ImmutableList val$entryList = ImmutableMapKeySet.this.map().entrySet().asList();
            
            @Override
            ImmutableCollection<K> delegateCollection() {
                return (ImmutableCollection<K>)ImmutableMapKeySet.this;
            }
            
            @Override
            public K get(final int n) {
                return ((Map.Entry)this.val$entryList.get(n)).getKey();
            }
        };
    }
    
    @Override
    boolean isPartialView() {
        return true;
    }
    
    abstract ImmutableMap<K, V> map();
    
    @Override
    K transform(final Map.Entry<K, V> entry) {
        return entry.getKey();
    }
}
