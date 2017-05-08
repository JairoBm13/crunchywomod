// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.annotation;

import java.io.Serializable;

public abstract class ObjectIdGenerator<T> implements Serializable
{
    public abstract boolean canUseFor(final ObjectIdGenerator<?> p0);
    
    public abstract ObjectIdGenerator<T> forScope(final Class<?> p0);
    
    public abstract T generateId(final Object p0);
    
    public abstract Class<?> getScope();
    
    public abstract IdKey key(final Object p0);
    
    public abstract ObjectIdGenerator<T> newForSerialization(final Object p0);
    
    public static final class IdKey implements Serializable
    {
        private final int hashCode;
        private final Object key;
        private final Class<?> scope;
        private final Class<?> type;
        
        public IdKey(final Class<?> type, final Class<?> scope, final Object key) {
            this.type = type;
            this.scope = scope;
            this.key = key;
            int hashCode = key.hashCode() + type.getName().hashCode();
            if (scope != null) {
                hashCode ^= scope.getName().hashCode();
            }
            this.hashCode = hashCode;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o != this) {
                if (o == null) {
                    return false;
                }
                if (o.getClass() != this.getClass()) {
                    return false;
                }
                final IdKey idKey = (IdKey)o;
                if (!idKey.key.equals(this.key) || idKey.type != this.type || idKey.scope != this.scope) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            return this.hashCode;
        }
    }
}
