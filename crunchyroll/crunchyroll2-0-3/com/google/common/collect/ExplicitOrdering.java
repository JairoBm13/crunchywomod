// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.List;
import java.io.Serializable;

final class ExplicitOrdering<T> extends Ordering<T> implements Serializable
{
    final ImmutableMap<T, Integer> rankMap;
    
    ExplicitOrdering(final ImmutableMap<T, Integer> rankMap) {
        this.rankMap = rankMap;
    }
    
    ExplicitOrdering(final List<T> list) {
        this(buildRankMap(list));
    }
    
    private static <T> ImmutableMap<T, Integer> buildRankMap(final List<T> list) {
        final ImmutableMap.Builder<T, Integer> builder = ImmutableMap.builder();
        int n = 0;
        final Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            builder.put(iterator.next(), n);
            ++n;
        }
        return builder.build();
    }
    
    private int rank(final T t) {
        final Integer n = this.rankMap.get(t);
        if (n == null) {
            throw new IncomparableValueException(t);
        }
        return n;
    }
    
    @Override
    public int compare(final T t, final T t2) {
        return this.rank(t) - this.rank(t2);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof ExplicitOrdering && this.rankMap.equals(((ExplicitOrdering)o).rankMap);
    }
    
    @Override
    public int hashCode() {
        return this.rankMap.hashCode();
    }
    
    @Override
    public String toString() {
        return "Ordering.explicit(" + this.rankMap.keySet() + ")";
    }
}
