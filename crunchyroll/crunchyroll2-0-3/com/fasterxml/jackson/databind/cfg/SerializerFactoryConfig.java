// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import java.io.Serializable;

public final class SerializerFactoryConfig implements Serializable
{
    protected static final BeanSerializerModifier[] NO_MODIFIERS;
    protected static final Serializers[] NO_SERIALIZERS;
    protected final Serializers[] _additionalKeySerializers;
    protected final Serializers[] _additionalSerializers;
    protected final BeanSerializerModifier[] _modifiers;
    
    static {
        NO_SERIALIZERS = new Serializers[0];
        NO_MODIFIERS = new BeanSerializerModifier[0];
    }
    
    public SerializerFactoryConfig() {
        this(null, null, null);
    }
    
    protected SerializerFactoryConfig(Serializers[] no_SERIALIZERS, final Serializers[] array, final BeanSerializerModifier[] array2) {
        Serializers[] no_SERIALIZERS2 = no_SERIALIZERS;
        if (no_SERIALIZERS == null) {
            no_SERIALIZERS2 = SerializerFactoryConfig.NO_SERIALIZERS;
        }
        this._additionalSerializers = no_SERIALIZERS2;
        if ((no_SERIALIZERS = array) == null) {
            no_SERIALIZERS = SerializerFactoryConfig.NO_SERIALIZERS;
        }
        this._additionalKeySerializers = no_SERIALIZERS;
        BeanSerializerModifier[] no_MODIFIERS;
        if ((no_MODIFIERS = array2) == null) {
            no_MODIFIERS = SerializerFactoryConfig.NO_MODIFIERS;
        }
        this._modifiers = no_MODIFIERS;
    }
    
    public boolean hasKeySerializers() {
        return this._additionalKeySerializers.length > 0;
    }
    
    public boolean hasSerializerModifiers() {
        return this._modifiers.length > 0;
    }
    
    public Iterable<Serializers> keySerializers() {
        return ArrayBuilders.arrayAsIterable(this._additionalKeySerializers);
    }
    
    public Iterable<BeanSerializerModifier> serializerModifiers() {
        return ArrayBuilders.arrayAsIterable(this._modifiers);
    }
    
    public Iterable<Serializers> serializers() {
        return ArrayBuilders.arrayAsIterable(this._additionalSerializers);
    }
}
