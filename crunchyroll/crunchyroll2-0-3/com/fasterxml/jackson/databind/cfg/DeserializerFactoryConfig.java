// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializers;
import com.fasterxml.jackson.databind.deser.ValueInstantiators;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.AbstractTypeResolver;
import com.fasterxml.jackson.databind.deser.KeyDeserializers;
import java.io.Serializable;

public class DeserializerFactoryConfig implements Serializable
{
    protected static final KeyDeserializers[] DEFAULT_KEY_DESERIALIZERS;
    protected static final AbstractTypeResolver[] NO_ABSTRACT_TYPE_RESOLVERS;
    protected static final Deserializers[] NO_DESERIALIZERS;
    protected static final BeanDeserializerModifier[] NO_MODIFIERS;
    protected static final ValueInstantiators[] NO_VALUE_INSTANTIATORS;
    protected final AbstractTypeResolver[] _abstractTypeResolvers;
    protected final Deserializers[] _additionalDeserializers;
    protected final KeyDeserializers[] _additionalKeyDeserializers;
    protected final BeanDeserializerModifier[] _modifiers;
    protected final ValueInstantiators[] _valueInstantiators;
    
    static {
        NO_DESERIALIZERS = new Deserializers[0];
        NO_MODIFIERS = new BeanDeserializerModifier[0];
        NO_ABSTRACT_TYPE_RESOLVERS = new AbstractTypeResolver[0];
        NO_VALUE_INSTANTIATORS = new ValueInstantiators[0];
        DEFAULT_KEY_DESERIALIZERS = new KeyDeserializers[] { new StdKeyDeserializers() };
    }
    
    public DeserializerFactoryConfig() {
        this(null, null, null, null, null);
    }
    
    protected DeserializerFactoryConfig(final Deserializers[] array, final KeyDeserializers[] array2, final BeanDeserializerModifier[] array3, final AbstractTypeResolver[] array4, final ValueInstantiators[] array5) {
        Deserializers[] no_DESERIALIZERS = array;
        if (array == null) {
            no_DESERIALIZERS = DeserializerFactoryConfig.NO_DESERIALIZERS;
        }
        this._additionalDeserializers = no_DESERIALIZERS;
        KeyDeserializers[] default_KEY_DESERIALIZERS;
        if ((default_KEY_DESERIALIZERS = array2) == null) {
            default_KEY_DESERIALIZERS = DeserializerFactoryConfig.DEFAULT_KEY_DESERIALIZERS;
        }
        this._additionalKeyDeserializers = default_KEY_DESERIALIZERS;
        BeanDeserializerModifier[] no_MODIFIERS;
        if ((no_MODIFIERS = array3) == null) {
            no_MODIFIERS = DeserializerFactoryConfig.NO_MODIFIERS;
        }
        this._modifiers = no_MODIFIERS;
        AbstractTypeResolver[] no_ABSTRACT_TYPE_RESOLVERS;
        if ((no_ABSTRACT_TYPE_RESOLVERS = array4) == null) {
            no_ABSTRACT_TYPE_RESOLVERS = DeserializerFactoryConfig.NO_ABSTRACT_TYPE_RESOLVERS;
        }
        this._abstractTypeResolvers = no_ABSTRACT_TYPE_RESOLVERS;
        ValueInstantiators[] no_VALUE_INSTANTIATORS;
        if ((no_VALUE_INSTANTIATORS = array5) == null) {
            no_VALUE_INSTANTIATORS = DeserializerFactoryConfig.NO_VALUE_INSTANTIATORS;
        }
        this._valueInstantiators = no_VALUE_INSTANTIATORS;
    }
    
    public Iterable<AbstractTypeResolver> abstractTypeResolvers() {
        return ArrayBuilders.arrayAsIterable(this._abstractTypeResolvers);
    }
    
    public Iterable<BeanDeserializerModifier> deserializerModifiers() {
        return ArrayBuilders.arrayAsIterable(this._modifiers);
    }
    
    public Iterable<Deserializers> deserializers() {
        return ArrayBuilders.arrayAsIterable(this._additionalDeserializers);
    }
    
    public boolean hasAbstractTypeResolvers() {
        return this._abstractTypeResolvers.length > 0;
    }
    
    public boolean hasDeserializerModifiers() {
        return this._modifiers.length > 0;
    }
    
    public boolean hasKeyDeserializers() {
        return this._additionalKeyDeserializers.length > 0;
    }
    
    public boolean hasValueInstantiators() {
        return this._valueInstantiators.length > 0;
    }
    
    public Iterable<KeyDeserializers> keyDeserializers() {
        return ArrayBuilders.arrayAsIterable(this._additionalKeyDeserializers);
    }
    
    public Iterable<ValueInstantiators> valueInstantiators() {
        return ArrayBuilders.arrayAsIterable(this._valueInstantiators);
    }
}
