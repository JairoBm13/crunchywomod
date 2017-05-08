// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import java.io.Serializable;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

public class StdValueInstantiator extends ValueInstantiator implements Serializable
{
    protected final boolean _cfgEmptyStringsAsObjects;
    protected CreatorProperty[] _constructorArguments;
    protected AnnotatedWithParams _defaultCreator;
    protected CreatorProperty[] _delegateArguments;
    protected AnnotatedWithParams _delegateCreator;
    protected JavaType _delegateType;
    protected AnnotatedWithParams _fromBooleanCreator;
    protected AnnotatedWithParams _fromDoubleCreator;
    protected AnnotatedWithParams _fromIntCreator;
    protected AnnotatedWithParams _fromLongCreator;
    protected AnnotatedWithParams _fromStringCreator;
    protected AnnotatedParameter _incompleteParameter;
    protected final String _valueTypeDesc;
    protected AnnotatedWithParams _withArgsCreator;
    
    public StdValueInstantiator(final DeserializationConfig deserializationConfig, final JavaType javaType) {
        this._cfgEmptyStringsAsObjects = (deserializationConfig != null && deserializationConfig.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT));
        String string;
        if (javaType == null) {
            string = "UNKNOWN TYPE";
        }
        else {
            string = javaType.toString();
        }
        this._valueTypeDesc = string;
    }
    
    protected Object _createFromStringFallbacks(final DeserializationContext deserializationContext, final String s) throws IOException, JsonProcessingException {
        if (this._fromBooleanCreator != null) {
            final String trim = s.trim();
            if ("true".equals(trim)) {
                return this.createFromBoolean(deserializationContext, true);
            }
            if ("false".equals(trim)) {
                return this.createFromBoolean(deserializationContext, false);
            }
        }
        if (this._cfgEmptyStringsAsObjects && s.length() == 0) {
            return null;
        }
        throw new JsonMappingException("Can not instantiate value of type " + this.getValueTypeDesc() + " from String value; no single-String constructor/factory method");
    }
    
    @Override
    public boolean canCreateFromBoolean() {
        return this._fromBooleanCreator != null;
    }
    
    @Override
    public boolean canCreateFromDouble() {
        return this._fromDoubleCreator != null;
    }
    
    @Override
    public boolean canCreateFromInt() {
        return this._fromIntCreator != null;
    }
    
    @Override
    public boolean canCreateFromLong() {
        return this._fromLongCreator != null;
    }
    
    @Override
    public boolean canCreateFromObjectWith() {
        return this._withArgsCreator != null;
    }
    
    @Override
    public boolean canCreateFromString() {
        return this._fromStringCreator != null;
    }
    
    @Override
    public boolean canCreateUsingDefault() {
        return this._defaultCreator != null;
    }
    
    @Override
    public boolean canCreateUsingDelegate() {
        return this._delegateType != null;
    }
    
    public void configureFromBooleanCreator(final AnnotatedWithParams fromBooleanCreator) {
        this._fromBooleanCreator = fromBooleanCreator;
    }
    
    public void configureFromDoubleCreator(final AnnotatedWithParams fromDoubleCreator) {
        this._fromDoubleCreator = fromDoubleCreator;
    }
    
    public void configureFromIntCreator(final AnnotatedWithParams fromIntCreator) {
        this._fromIntCreator = fromIntCreator;
    }
    
    public void configureFromLongCreator(final AnnotatedWithParams fromLongCreator) {
        this._fromLongCreator = fromLongCreator;
    }
    
    public void configureFromObjectSettings(final AnnotatedWithParams defaultCreator, final AnnotatedWithParams delegateCreator, final JavaType delegateType, final CreatorProperty[] delegateArguments, final AnnotatedWithParams withArgsCreator, final CreatorProperty[] constructorArguments) {
        this._defaultCreator = defaultCreator;
        this._delegateCreator = delegateCreator;
        this._delegateType = delegateType;
        this._delegateArguments = delegateArguments;
        this._withArgsCreator = withArgsCreator;
        this._constructorArguments = constructorArguments;
    }
    
    public void configureFromStringCreator(final AnnotatedWithParams fromStringCreator) {
        this._fromStringCreator = fromStringCreator;
    }
    
    public void configureIncompleteParameter(final AnnotatedParameter incompleteParameter) {
        this._incompleteParameter = incompleteParameter;
    }
    
    @Override
    public Object createFromBoolean(final DeserializationContext deserializationContext, final boolean b) throws IOException, JsonProcessingException {
        try {
            if (this._fromBooleanCreator != null) {
                return this._fromBooleanCreator.call1(b);
            }
        }
        catch (Exception ex) {
            throw this.wrapException(ex);
        }
        catch (ExceptionInInitializerError exceptionInInitializerError) {
            throw this.wrapException(exceptionInInitializerError);
        }
        throw new JsonMappingException("Can not instantiate value of type " + this.getValueTypeDesc() + " from Boolean value; no single-boolean/Boolean-arg constructor/factory method");
    }
    
    @Override
    public Object createFromDouble(final DeserializationContext deserializationContext, final double n) throws IOException, JsonProcessingException {
        try {
            if (this._fromDoubleCreator != null) {
                return this._fromDoubleCreator.call1(n);
            }
        }
        catch (Exception ex) {
            throw this.wrapException(ex);
        }
        catch (ExceptionInInitializerError exceptionInInitializerError) {
            throw this.wrapException(exceptionInInitializerError);
        }
        throw new JsonMappingException("Can not instantiate value of type " + this.getValueTypeDesc() + " from Floating-point number; no one-double/Double-arg constructor/factory method");
    }
    
    @Override
    public Object createFromInt(final DeserializationContext deserializationContext, final int n) throws IOException, JsonProcessingException {
        try {
            if (this._fromIntCreator != null) {
                return this._fromIntCreator.call1(n);
            }
            if (this._fromLongCreator != null) {
                return this._fromLongCreator.call1((long)n);
            }
        }
        catch (Exception ex) {
            throw this.wrapException(ex);
        }
        catch (ExceptionInInitializerError exceptionInInitializerError) {
            throw this.wrapException(exceptionInInitializerError);
        }
        throw new JsonMappingException("Can not instantiate value of type " + this.getValueTypeDesc() + " from Integral number; no single-int-arg constructor/factory method");
    }
    
    @Override
    public Object createFromLong(final DeserializationContext deserializationContext, final long n) throws IOException, JsonProcessingException {
        try {
            if (this._fromLongCreator != null) {
                return this._fromLongCreator.call1(n);
            }
        }
        catch (Exception ex) {
            throw this.wrapException(ex);
        }
        catch (ExceptionInInitializerError exceptionInInitializerError) {
            throw this.wrapException(exceptionInInitializerError);
        }
        throw new JsonMappingException("Can not instantiate value of type " + this.getValueTypeDesc() + " from Long integral number; no single-long-arg constructor/factory method");
    }
    
    @Override
    public Object createFromObjectWith(final DeserializationContext deserializationContext, final Object[] array) throws IOException, JsonProcessingException {
        if (this._withArgsCreator == null) {
            throw new IllegalStateException("No with-args constructor for " + this.getValueTypeDesc());
        }
        try {
            return this._withArgsCreator.call(array);
        }
        catch (ExceptionInInitializerError exceptionInInitializerError) {
            throw this.wrapException(exceptionInInitializerError);
        }
        catch (Exception ex) {
            throw this.wrapException(ex);
        }
    }
    
    @Override
    public Object createFromString(final DeserializationContext deserializationContext, final String s) throws IOException, JsonProcessingException {
        if (this._fromStringCreator != null) {
            try {
                return this._fromStringCreator.call1(s);
            }
            catch (Exception ex) {
                throw this.wrapException(ex);
            }
            catch (ExceptionInInitializerError exceptionInInitializerError) {
                throw this.wrapException(exceptionInInitializerError);
            }
        }
        return this._createFromStringFallbacks(deserializationContext, s);
    }
    
    @Override
    public Object createUsingDefault(final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (this._defaultCreator == null) {
            throw new IllegalStateException("No default constructor for " + this.getValueTypeDesc());
        }
        try {
            return this._defaultCreator.call();
        }
        catch (ExceptionInInitializerError exceptionInInitializerError) {
            throw this.wrapException(exceptionInInitializerError);
        }
        catch (Exception ex) {
            throw this.wrapException(ex);
        }
    }
    
    @Override
    public Object createUsingDelegate(final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
        if (this._delegateCreator == null) {
            throw new IllegalStateException("No delegate constructor for " + this.getValueTypeDesc());
        }
        while (true) {
            while (true) {
                int n;
                try {
                    if (this._delegateArguments == null) {
                        return this._delegateCreator.call1(o);
                    }
                    final int length = this._delegateArguments.length;
                    final Object[] array = new Object[length];
                    n = 0;
                    if (n >= length) {
                        goto Label_0122;
                    }
                    final CreatorProperty creatorProperty = this._delegateArguments[n];
                    if (creatorProperty == null) {
                        array[n] = o;
                    }
                    else {
                        array[n] = deserializationContext.findInjectableValue(creatorProperty.getInjectableValueId(), creatorProperty, null);
                    }
                }
                catch (ExceptionInInitializerError exceptionInInitializerError) {
                    throw this.wrapException(exceptionInInitializerError);
                }
                catch (Exception ex) {
                    throw this.wrapException(ex);
                }
                ++n;
                continue;
            }
        }
    }
    
    @Override
    public AnnotatedWithParams getDefaultCreator() {
        return this._defaultCreator;
    }
    
    @Override
    public AnnotatedWithParams getDelegateCreator() {
        return this._delegateCreator;
    }
    
    @Override
    public JavaType getDelegateType(final DeserializationConfig deserializationConfig) {
        return this._delegateType;
    }
    
    @Override
    public SettableBeanProperty[] getFromObjectArguments(final DeserializationConfig deserializationConfig) {
        return this._constructorArguments;
    }
    
    @Override
    public AnnotatedParameter getIncompleteParameter() {
        return this._incompleteParameter;
    }
    
    @Override
    public String getValueTypeDesc() {
        return this._valueTypeDesc;
    }
    
    protected JsonMappingException wrapException(Throwable cause) {
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        if (cause instanceof JsonMappingException) {
            return (JsonMappingException)cause;
        }
        return new JsonMappingException("Instantiation of " + this.getValueTypeDesc() + " value failed: " + cause.getMessage(), cause);
    }
}
