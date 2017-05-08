// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import com.google.common.base.Objects;

public abstract class GenericMapMaker<K0, V0>
{
    MapMaker.RemovalListener<K0, V0> removalListener;
    
     <K extends K0, V extends V0> MapMaker.RemovalListener<K, V> getRemovalListener() {
        return (MapMaker.RemovalListener<K, V>)Objects.firstNonNull((MapMaker.RemovalListener)this.removalListener, NullListener.INSTANCE);
    }
    
    enum NullListener implements RemovalListener<Object, Object>
    {
        INSTANCE;
        
        @Override
        public void onRemoval(final RemovalNotification<Object, Object> removalNotification) {
        }
    }
}
