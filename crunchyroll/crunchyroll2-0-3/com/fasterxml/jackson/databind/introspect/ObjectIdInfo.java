// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;

public class ObjectIdInfo
{
    protected final boolean _alwaysAsId;
    protected final Class<? extends ObjectIdGenerator<?>> _generator;
    protected final String _propertyName;
    protected final Class<?> _scope;
    
    public ObjectIdInfo(final String s, final Class<?> clazz, final Class<? extends ObjectIdGenerator<?>> clazz2) {
        this(s, clazz, clazz2, false);
    }
    
    protected ObjectIdInfo(final String propertyName, final Class<?> scope, final Class<? extends ObjectIdGenerator<?>> generator, final boolean alwaysAsId) {
        this._propertyName = propertyName;
        this._scope = scope;
        this._generator = generator;
        this._alwaysAsId = alwaysAsId;
    }
    
    public boolean getAlwaysAsId() {
        return this._alwaysAsId;
    }
    
    public Class<? extends ObjectIdGenerator<?>> getGeneratorType() {
        return this._generator;
    }
    
    public String getPropertyName() {
        return this._propertyName;
    }
    
    public Class<?> getScope() {
        return this._scope;
    }
    
    @Override
    public String toString() {
        final StringBuilder append = new StringBuilder().append("ObjectIdInfo: propName=").append(this._propertyName).append(", scope=");
        String name;
        if (this._scope == null) {
            name = "null";
        }
        else {
            name = this._scope.getName();
        }
        final StringBuilder append2 = append.append(name).append(", generatorType=");
        String name2;
        if (this._generator == null) {
            name2 = "null";
        }
        else {
            name2 = this._generator.getName();
        }
        return append2.append(name2).append(", alwaysAsId=").append(this._alwaysAsId).toString();
    }
    
    public ObjectIdInfo withAlwaysAsId(final boolean b) {
        if (this._alwaysAsId == b) {
            return this;
        }
        return new ObjectIdInfo(this._propertyName, this._scope, this._generator, b);
    }
}
