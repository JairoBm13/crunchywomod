// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

public interface VisibilityChecker<T extends VisibilityChecker<T>>
{
    boolean isCreatorVisible(final AnnotatedMember p0);
    
    boolean isFieldVisible(final AnnotatedField p0);
    
    boolean isGetterVisible(final AnnotatedMethod p0);
    
    boolean isIsGetterVisible(final AnnotatedMethod p0);
    
    boolean isSetterVisible(final AnnotatedMethod p0);
    
    T with(final JsonAutoDetect p0);
    
    T withCreatorVisibility(final JsonAutoDetect.Visibility p0);
    
    T withFieldVisibility(final JsonAutoDetect.Visibility p0);
    
    T withGetterVisibility(final JsonAutoDetect.Visibility p0);
    
    T withIsGetterVisibility(final JsonAutoDetect.Visibility p0);
    
    T withSetterVisibility(final JsonAutoDetect.Visibility p0);
    
    @JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.ANY, fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, isGetterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, setterVisibility = JsonAutoDetect.Visibility.ANY)
    public static class Std implements VisibilityChecker<Std>, Serializable
    {
        protected static final Std DEFAULT;
        protected final JsonAutoDetect.Visibility _creatorMinLevel;
        protected final JsonAutoDetect.Visibility _fieldMinLevel;
        protected final JsonAutoDetect.Visibility _getterMinLevel;
        protected final JsonAutoDetect.Visibility _isGetterMinLevel;
        protected final JsonAutoDetect.Visibility _setterMinLevel;
        
        static {
            DEFAULT = new Std(Std.class.getAnnotation(JsonAutoDetect.class));
        }
        
        public Std(final JsonAutoDetect.Visibility getterMinLevel, final JsonAutoDetect.Visibility isGetterMinLevel, final JsonAutoDetect.Visibility setterMinLevel, final JsonAutoDetect.Visibility creatorMinLevel, final JsonAutoDetect.Visibility fieldMinLevel) {
            this._getterMinLevel = getterMinLevel;
            this._isGetterMinLevel = isGetterMinLevel;
            this._setterMinLevel = setterMinLevel;
            this._creatorMinLevel = creatorMinLevel;
            this._fieldMinLevel = fieldMinLevel;
        }
        
        public Std(final JsonAutoDetect jsonAutoDetect) {
            this._getterMinLevel = jsonAutoDetect.getterVisibility();
            this._isGetterMinLevel = jsonAutoDetect.isGetterVisibility();
            this._setterMinLevel = jsonAutoDetect.setterVisibility();
            this._creatorMinLevel = jsonAutoDetect.creatorVisibility();
            this._fieldMinLevel = jsonAutoDetect.fieldVisibility();
        }
        
        public static Std defaultInstance() {
            return Std.DEFAULT;
        }
        
        @Override
        public boolean isCreatorVisible(final AnnotatedMember annotatedMember) {
            return this.isCreatorVisible(annotatedMember.getMember());
        }
        
        public boolean isCreatorVisible(final Member member) {
            return this._creatorMinLevel.isVisible(member);
        }
        
        @Override
        public boolean isFieldVisible(final AnnotatedField annotatedField) {
            return this.isFieldVisible(annotatedField.getAnnotated());
        }
        
        public boolean isFieldVisible(final Field field) {
            return this._fieldMinLevel.isVisible(field);
        }
        
        @Override
        public boolean isGetterVisible(final AnnotatedMethod annotatedMethod) {
            return this.isGetterVisible(annotatedMethod.getAnnotated());
        }
        
        public boolean isGetterVisible(final Method method) {
            return this._getterMinLevel.isVisible(method);
        }
        
        @Override
        public boolean isIsGetterVisible(final AnnotatedMethod annotatedMethod) {
            return this.isIsGetterVisible(annotatedMethod.getAnnotated());
        }
        
        public boolean isIsGetterVisible(final Method method) {
            return this._isGetterMinLevel.isVisible(method);
        }
        
        @Override
        public boolean isSetterVisible(final AnnotatedMethod annotatedMethod) {
            return this.isSetterVisible(annotatedMethod.getAnnotated());
        }
        
        public boolean isSetterVisible(final Method method) {
            return this._setterMinLevel.isVisible(method);
        }
        
        @Override
        public String toString() {
            return "[Visibility:" + " getter: " + this._getterMinLevel + ", isGetter: " + this._isGetterMinLevel + ", setter: " + this._setterMinLevel + ", creator: " + this._creatorMinLevel + ", field: " + this._fieldMinLevel + "]";
        }
        
        @Override
        public Std with(final JsonAutoDetect jsonAutoDetect) {
            Std withFieldVisibility = this;
            if (jsonAutoDetect != null) {
                withFieldVisibility = this.withGetterVisibility(jsonAutoDetect.getterVisibility()).withIsGetterVisibility(jsonAutoDetect.isGetterVisibility()).withSetterVisibility(jsonAutoDetect.setterVisibility()).withCreatorVisibility(jsonAutoDetect.creatorVisibility()).withFieldVisibility(jsonAutoDetect.fieldVisibility());
            }
            return withFieldVisibility;
        }
        
        @Override
        public Std withCreatorVisibility(JsonAutoDetect.Visibility creatorMinLevel) {
            if (creatorMinLevel == JsonAutoDetect.Visibility.DEFAULT) {
                creatorMinLevel = Std.DEFAULT._creatorMinLevel;
            }
            if (this._creatorMinLevel == creatorMinLevel) {
                return this;
            }
            return new Std(this._getterMinLevel, this._isGetterMinLevel, this._setterMinLevel, creatorMinLevel, this._fieldMinLevel);
        }
        
        @Override
        public Std withFieldVisibility(JsonAutoDetect.Visibility fieldMinLevel) {
            if (fieldMinLevel == JsonAutoDetect.Visibility.DEFAULT) {
                fieldMinLevel = Std.DEFAULT._fieldMinLevel;
            }
            if (this._fieldMinLevel == fieldMinLevel) {
                return this;
            }
            return new Std(this._getterMinLevel, this._isGetterMinLevel, this._setterMinLevel, this._creatorMinLevel, fieldMinLevel);
        }
        
        @Override
        public Std withGetterVisibility(JsonAutoDetect.Visibility getterMinLevel) {
            if (getterMinLevel == JsonAutoDetect.Visibility.DEFAULT) {
                getterMinLevel = Std.DEFAULT._getterMinLevel;
            }
            if (this._getterMinLevel == getterMinLevel) {
                return this;
            }
            return new Std(getterMinLevel, this._isGetterMinLevel, this._setterMinLevel, this._creatorMinLevel, this._fieldMinLevel);
        }
        
        @Override
        public Std withIsGetterVisibility(JsonAutoDetect.Visibility isGetterMinLevel) {
            if (isGetterMinLevel == JsonAutoDetect.Visibility.DEFAULT) {
                isGetterMinLevel = Std.DEFAULT._isGetterMinLevel;
            }
            if (this._isGetterMinLevel == isGetterMinLevel) {
                return this;
            }
            return new Std(this._getterMinLevel, isGetterMinLevel, this._setterMinLevel, this._creatorMinLevel, this._fieldMinLevel);
        }
        
        @Override
        public Std withSetterVisibility(JsonAutoDetect.Visibility setterMinLevel) {
            if (setterMinLevel == JsonAutoDetect.Visibility.DEFAULT) {
                setterMinLevel = Std.DEFAULT._setterMinLevel;
            }
            if (this._setterMinLevel == setterMinLevel) {
                return this;
            }
            return new Std(this._getterMinLevel, this._isGetterMinLevel, setterMinLevel, this._creatorMinLevel, this._fieldMinLevel);
        }
    }
}
