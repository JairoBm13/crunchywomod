// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.Named;

public interface BeanProperty extends Named
{
    AnnotatedMember getMember();
    
    String getName();
    
    JavaType getType();
    
    public static class Std implements BeanProperty
    {
        protected final Annotations _contextAnnotations;
        protected final boolean _isRequired;
        protected final AnnotatedMember _member;
        protected final String _name;
        protected final JavaType _type;
        protected final PropertyName _wrapperName;
        
        public Std(final String name, final JavaType type, final PropertyName wrapperName, final Annotations contextAnnotations, final AnnotatedMember member, final boolean isRequired) {
            this._name = name;
            this._type = type;
            this._wrapperName = wrapperName;
            this._isRequired = isRequired;
            this._member = member;
            this._contextAnnotations = contextAnnotations;
        }
        
        @Override
        public AnnotatedMember getMember() {
            return this._member;
        }
        
        @Override
        public String getName() {
            return this._name;
        }
        
        @Override
        public JavaType getType() {
            return this._type;
        }
        
        public PropertyName getWrapperName() {
            return this._wrapperName;
        }
        
        public boolean isRequired() {
            return this._isRequired;
        }
        
        public Std withType(final JavaType javaType) {
            return new Std(this._name, javaType, this._wrapperName, this._contextAnnotations, this._member, this._isRequired);
        }
    }
}
