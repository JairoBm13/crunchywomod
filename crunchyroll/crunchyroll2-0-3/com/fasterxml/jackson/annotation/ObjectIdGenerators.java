// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.annotation;

public class ObjectIdGenerators
{
    private abstract static class Base<T> extends ObjectIdGenerator<T>
    {
        protected final Class<?> _scope;
        
        protected Base(final Class<?> scope) {
            this._scope = scope;
        }
        
        @Override
        public boolean canUseFor(final ObjectIdGenerator<?> objectIdGenerator) {
            return objectIdGenerator.getClass() == this.getClass() && objectIdGenerator.getScope() == this._scope;
        }
        
        @Override
        public final Class<?> getScope() {
            return this._scope;
        }
    }
    
    public abstract static class None extends ObjectIdGenerator<Object>
    {
    }
    
    public abstract static class PropertyGenerator extends Base<Object>
    {
        protected PropertyGenerator(final Class<?> clazz) {
            super(clazz);
        }
    }
}
