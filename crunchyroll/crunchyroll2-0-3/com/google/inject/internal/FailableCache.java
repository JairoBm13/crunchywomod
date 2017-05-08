// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.internal.util.$Nullable;
import com.google.inject.internal.util.$Function;
import com.google.inject.internal.util.$MapMaker;
import java.util.Map;

public abstract class FailableCache<K, V>
{
    private final Map<K, Object> delegate;
    
    public FailableCache() {
        this.delegate = (Map<K, Object>)new $MapMaker().makeComputingMap(($Function<? super Object, ?>)new $Function<K, Object>() {
            @Override
            public Object apply(@$Nullable final K k) {
                final Errors errors = new Errors();
                final Object o = null;
                Object create;
                while (true) {
                    try {
                        create = FailableCache.this.create(k, errors);
                        if (errors.hasErrors()) {
                            return errors;
                        }
                    }
                    catch (ErrorsException ex) {
                        errors.merge(ex.getErrors());
                        create = o;
                        continue;
                    }
                    break;
                }
                return create;
            }
        });
    }
    
    protected abstract V create(final K p0, final Errors p1) throws ErrorsException;
    
    public V get(final K k, final Errors errors) throws ErrorsException {
        final Errors value = (Errors)this.delegate.get(k);
        if (value instanceof Errors) {
            errors.merge(value);
            throw errors.toException();
        }
        return (V)value;
    }
    
    boolean remove(final K k) {
        return this.delegate.remove(k) != null;
    }
}
