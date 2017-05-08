// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.util.Named;

public abstract class BeanPropertyDefinition implements Named
{
    public boolean couldDeserialize() {
        return this.getMutator() != null;
    }
    
    public AnnotationIntrospector.ReferenceProperty findReferenceType() {
        return null;
    }
    
    public Class<?>[] findViews() {
        return null;
    }
    
    public abstract AnnotatedMember getAccessor();
    
    public abstract AnnotatedParameter getConstructorParameter();
    
    public abstract AnnotatedField getField();
    
    public abstract AnnotatedMethod getGetter();
    
    public abstract AnnotatedMember getMutator();
    
    public abstract String getName();
    
    public AnnotatedMember getPrimaryMember() {
        return null;
    }
    
    public abstract AnnotatedMethod getSetter();
    
    public abstract PropertyName getWrapperName();
    
    public abstract boolean hasConstructorParameter();
    
    public abstract boolean hasField();
    
    public abstract boolean hasGetter();
    
    public abstract boolean hasSetter();
    
    public abstract boolean isExplicitlyIncluded();
    
    public boolean isRequired() {
        return false;
    }
    
    public boolean isTypeId() {
        return false;
    }
}
