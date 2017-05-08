// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Iterator;
import java.io.Serializable;

final class LexicographicalOrdering<T> extends Ordering<Iterable<T>> implements Serializable
{
    final Ordering<? super T> elementOrder;
    
    LexicographicalOrdering(final Ordering<? super T> elementOrder) {
        this.elementOrder = elementOrder;
    }
    
    @Override
    public int compare(final Iterable<T> iterable, final Iterable<T> iterable2) {
        final Iterator<T> iterator = iterable.iterator();
        final Iterator<T> iterator2 = iterable2.iterator();
        while (iterator.hasNext()) {
            if (!iterator2.hasNext()) {
                return 1;
            }
            final int compare = this.elementOrder.compare((Object)iterator.next(), (Object)iterator2.next());
            if (compare != 0) {
                return compare;
            }
        }
        if (iterator2.hasNext()) {
            return -1;
        }
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof LexicographicalOrdering && this.elementOrder.equals(((LexicographicalOrdering)o).elementOrder));
    }
    
    @Override
    public int hashCode() {
        return this.elementOrder.hashCode() ^ 0x7BB78CF5;
    }
    
    @Override
    public String toString() {
        return this.elementOrder + ".lexicographical()";
    }
}
