// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Function;
import java.io.Serializable;

final class ByFunctionOrdering<F, T> extends Ordering<F> implements Serializable
{
    final Function<F, ? extends T> function;
    final Ordering<T> ordering;
    
    ByFunctionOrdering(final Function<F, ? extends T> function, final Ordering<T> ordering) {
        this.function = Preconditions.checkNotNull(function);
        this.ordering = Preconditions.checkNotNull(ordering);
    }
    
    @Override
    public int compare(final F n, final F n2) {
        return this.ordering.compare((T)this.function.apply(n), (T)this.function.apply(n2));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (!(o instanceof ByFunctionOrdering)) {
                return false;
            }
            final ByFunctionOrdering byFunctionOrdering = (ByFunctionOrdering)o;
            if (!this.function.equals(byFunctionOrdering.function) || !this.ordering.equals(byFunctionOrdering.ordering)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.function, this.ordering);
    }
    
    @Override
    public String toString() {
        return this.ordering + ".onResultOf(" + this.function + ")";
    }
}
