// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.DeserializationConfig;
import java.util.HashMap;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.lang.reflect.Member;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.BeanDescription;

public class CreatorCollector
{
    protected final BeanDescription _beanDesc;
    protected AnnotatedWithParams _booleanCreator;
    protected final boolean _canFixAccess;
    protected AnnotatedWithParams _defaultConstructor;
    protected CreatorProperty[] _delegateArgs;
    protected AnnotatedWithParams _delegateCreator;
    protected AnnotatedWithParams _doubleCreator;
    protected AnnotatedParameter _incompleteParameter;
    protected AnnotatedWithParams _intCreator;
    protected AnnotatedWithParams _longCreator;
    protected CreatorProperty[] _propertyBasedArgs;
    protected AnnotatedWithParams _propertyBasedCreator;
    protected AnnotatedWithParams _stringCreator;
    
    public CreatorCollector(final BeanDescription beanDesc, final boolean canFixAccess) {
        this._propertyBasedArgs = null;
        this._beanDesc = beanDesc;
        this._canFixAccess = canFixAccess;
    }
    
    private <T extends AnnotatedMember> T _fixAccess(final T t) {
        if (t != null && this._canFixAccess) {
            ClassUtil.checkAndFixAccess((Member)t.getAnnotated());
        }
        return t;
    }
    
    public void addBooleanCreator(final AnnotatedWithParams annotatedWithParams) {
        this._booleanCreator = this.verifyNonDup(annotatedWithParams, this._booleanCreator, "boolean");
    }
    
    public void addDelegatingCreator(final AnnotatedWithParams annotatedWithParams, final CreatorProperty[] delegateArgs) {
        this._delegateCreator = this.verifyNonDup(annotatedWithParams, this._delegateCreator, "delegate");
        this._delegateArgs = delegateArgs;
    }
    
    public void addDoubleCreator(final AnnotatedWithParams annotatedWithParams) {
        this._doubleCreator = this.verifyNonDup(annotatedWithParams, this._doubleCreator, "double");
    }
    
    public void addIncompeteParameter(final AnnotatedParameter incompleteParameter) {
        if (this._incompleteParameter == null) {
            this._incompleteParameter = incompleteParameter;
        }
    }
    
    public void addIntCreator(final AnnotatedWithParams annotatedWithParams) {
        this._intCreator = this.verifyNonDup(annotatedWithParams, this._intCreator, "int");
    }
    
    public void addLongCreator(final AnnotatedWithParams annotatedWithParams) {
        this._longCreator = this.verifyNonDup(annotatedWithParams, this._longCreator, "long");
    }
    
    public void addPropertyCreator(final AnnotatedWithParams annotatedWithParams, final CreatorProperty[] propertyBasedArgs) {
        this._propertyBasedCreator = this.verifyNonDup(annotatedWithParams, this._propertyBasedCreator, "property-based");
        if (propertyBasedArgs.length > 1) {
            final HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
            for (int length = propertyBasedArgs.length, i = 0; i < length; ++i) {
                final String name = propertyBasedArgs[i].getName();
                if (name.length() != 0 || propertyBasedArgs[i].getInjectableValueId() == null) {
                    final Integer n = hashMap.put(name, i);
                    if (n != null) {
                        throw new IllegalArgumentException("Duplicate creator property \"" + name + "\" (index " + n + " vs " + i + ")");
                    }
                }
            }
        }
        this._propertyBasedArgs = propertyBasedArgs;
    }
    
    public void addStringCreator(final AnnotatedWithParams annotatedWithParams) {
        this._stringCreator = this.verifyNonDup(annotatedWithParams, this._stringCreator, "String");
    }
    
    public ValueInstantiator constructValueInstantiator(final DeserializationConfig deserializationConfig) {
        final StdValueInstantiator stdValueInstantiator = new StdValueInstantiator(deserializationConfig, this._beanDesc.getType());
        JavaType resolveType = null;
        Label_0026: {
            if (this._delegateCreator != null) {
            Label_0138:
                while (true) {
                    if (this._delegateArgs != null) {
                        for (int length = this._delegateArgs.length, i = 0; i < length; ++i) {
                            if (this._delegateArgs[i] == null) {
                                break Label_0138;
                            }
                        }
                    }
                    Label_0167: {
                        break Label_0167;
                        final int i;
                        resolveType = this._beanDesc.bindingsForBeanType().resolveType(this._delegateCreator.getGenericParameterType(i));
                        break Label_0026;
                    }
                    int i = 0;
                    continue Label_0138;
                }
            }
            resolveType = null;
        }
        stdValueInstantiator.configureFromObjectSettings(this._defaultConstructor, this._delegateCreator, resolveType, this._delegateArgs, this._propertyBasedCreator, this._propertyBasedArgs);
        stdValueInstantiator.configureFromStringCreator(this._stringCreator);
        stdValueInstantiator.configureFromIntCreator(this._intCreator);
        stdValueInstantiator.configureFromLongCreator(this._longCreator);
        stdValueInstantiator.configureFromDoubleCreator(this._doubleCreator);
        stdValueInstantiator.configureFromBooleanCreator(this._booleanCreator);
        stdValueInstantiator.configureIncompleteParameter(this._incompleteParameter);
        return stdValueInstantiator;
    }
    
    public boolean hasDefaultCreator() {
        return this._defaultConstructor != null;
    }
    
    @Deprecated
    public void setDefaultConstructor(final AnnotatedConstructor annotatedConstructor) {
        this._defaultConstructor = this._fixAccess(annotatedConstructor);
    }
    
    public void setDefaultCreator(final AnnotatedWithParams annotatedWithParams) {
        if (annotatedWithParams instanceof AnnotatedConstructor) {
            this.setDefaultConstructor((AnnotatedConstructor)annotatedWithParams);
            return;
        }
        this._defaultConstructor = this._fixAccess(annotatedWithParams);
    }
    
    protected AnnotatedWithParams verifyNonDup(final AnnotatedWithParams annotatedWithParams, final AnnotatedWithParams annotatedWithParams2, final String s) {
        if (annotatedWithParams2 != null && annotatedWithParams2.getClass() == annotatedWithParams.getClass()) {
            throw new IllegalArgumentException("Conflicting " + s + " creators: already had " + annotatedWithParams2 + ", encountered " + annotatedWithParams);
        }
        return this._fixAccess(annotatedWithParams);
    }
}
