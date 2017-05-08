// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.module;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import java.util.LinkedHashSet;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import java.util.HashMap;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import java.io.Serializable;
import com.fasterxml.jackson.databind.Module;

public class SimpleModule extends Module implements Serializable
{
    protected SimpleAbstractTypeResolver _abstractTypes;
    protected BeanDeserializerModifier _deserializerModifier;
    protected SimpleDeserializers _deserializers;
    protected SimpleKeyDeserializers _keyDeserializers;
    protected SimpleSerializers _keySerializers;
    protected HashMap<Class<?>, Class<?>> _mixins;
    protected final String _name;
    protected BeanSerializerModifier _serializerModifier;
    protected SimpleSerializers _serializers;
    protected LinkedHashSet<NamedType> _subtypes;
    protected SimpleValueInstantiators _valueInstantiators;
    protected final Version _version;
    
    public SimpleModule() {
        this._serializers = null;
        this._deserializers = null;
        this._keySerializers = null;
        this._keyDeserializers = null;
        this._abstractTypes = null;
        this._valueInstantiators = null;
        this._deserializerModifier = null;
        this._serializerModifier = null;
        this._mixins = null;
        this._subtypes = null;
        this._name = "SimpleModule-" + System.identityHashCode(this);
        this._version = Version.unknownVersion();
    }
    
    @Override
    public Version version() {
        return this._version;
    }
}
